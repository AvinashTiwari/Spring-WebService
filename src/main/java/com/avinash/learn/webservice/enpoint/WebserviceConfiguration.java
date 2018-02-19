package com.avinash.learn.webservice.enpoint;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;
import java.util.Collections;
import java.util.List;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.server.EndpointInterceptor;
import org.springframework.ws.soap.security.xwss.XwsSecurityInterceptor;
import org.springframework.ws.soap.security.xwss.callback.SimplePasswordValidationCallbackHandler;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

@Configuration
@EnableWs
public class WebserviceConfiguration extends WsConfigurerAdapter{
	
	@Bean
	ServletRegistrationBean messageDispacterSrvlet(ApplicationContext context)
	{
		MessageDispatcherServlet mds = new MessageDispatcherServlet();
		mds.setApplicationContext(context);
		mds.setTransformWsdlLocations(true);
		return new ServletRegistrationBean(mds, "/ws/*");
		
	}
	
	@Bean(name="corses")
	public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchema xsdSchema)
	{
		DefaultWsdl11Definition definition = new DefaultWsdl11Definition();
		definition.setPortTypeName("CoursePort");
		definition.setTargetNamespace("http://com.avinash.learn/courses");
		definition.setLocationUri("/ws");
		definition.setSchema(xsdSchema);
		return definition;
		
	}
	
	@Bean
	public XsdSchema courSechma()
	{
		return new SimpleXsdSchema(new ClassPathResource("coursedetails.xsd"));
	}
	
	//XwsSecurityInterceptor
		@Bean
		public XwsSecurityInterceptor securityInterceptor(){
			XwsSecurityInterceptor securityInterceptor = new XwsSecurityInterceptor();
			//Callback Handler -> SimplePasswordValidationCallbackHandler
			securityInterceptor.setCallbackHandler(callbackHandler());
			//Security Policy -> securityPolicy.xml
			securityInterceptor.setPolicyConfiguration(new ClassPathResource("securityPolicy.xml"));
			return securityInterceptor;
		}
		
		@Bean
		public SimplePasswordValidationCallbackHandler callbackHandler() {
			SimplePasswordValidationCallbackHandler handler = new SimplePasswordValidationCallbackHandler();
			handler.setUsersMap(Collections.singletonMap("user", "password"));
			return handler;
		}

		//Interceptors.add -> XwsSecurityInterceptor
		@Override
		public void addInterceptors(List<EndpointInterceptor> interceptors) {
			interceptors.add(securityInterceptor());
		}

	

}
