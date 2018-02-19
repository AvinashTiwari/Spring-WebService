package com.avinash.learn.webservice.enpoint;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.avinash.learn.webservice.bean.Course;
import com.avinash.learn.webservice.execption.CourseNotFoundException;
import com.avinash.learn.webservice.service.CourseDetailsService;
import com.avinash.learn.webservice.service.CourseDetailsService.Status;

import learn.avinash.com.courses.CourseDetails;
import learn.avinash.com.courses.DeleteCourseDetailRequest;
import learn.avinash.com.courses.DeleteCourseDetailResponse;
import learn.avinash.com.courses.GetAllCourseDetailRequest;
import learn.avinash.com.courses.GetAllCourseDetailResponse;
import learn.avinash.com.courses.GetCourseDetailRequest;
import learn.avinash.com.courses.GetCourseDetailResponse;

@Endpoint
public class CourseDetailEnpoint {
	@Autowired
	CourseDetailsService service;

	@PayloadRoot(namespace="http://com.avinash.learn/courses",
			localPart="GetCourseDetailRequest")
	@ResponsePayload
	public GetCourseDetailResponse processDetailRequest(@RequestPayload GetCourseDetailRequest request) throws CourseNotFoundException
	{
		Course course = service.findById(request.getId());

		if (course == null)
			throw new CourseNotFoundException("Invalid Course Id " + request.getId());

		return mapCourseDetails(course);
	}
	private GetCourseDetailResponse mapCourseDetails(Course course) {
		GetCourseDetailResponse response = new GetCourseDetailResponse();
		response.setCourseDetails(mapCourse(course));
		return response;
	}

	
	@PayloadRoot(namespace="http://com.avinash.learn/courses",
			localPart="GetAllCourseDetailRequest")
	@ResponsePayload
public GetAllCourseDetailResponse processCourseDetailsRequest(@RequestPayload GetAllCourseDetailRequest request) {
		
		List<Course> course = service.findAll();

		return mapAllCourseDetails(course);
	}

	
	@PayloadRoot(namespace="http://com.avinash.learn/courses",
			localPart="DeleteCourseDetailRequest")
	@ResponsePayload
public DeleteCourseDetailResponse deleteCourseDetailsRequest(@RequestPayload DeleteCourseDetailRequest request) {
		
		Status status = service.deleteById(request.getId());
		DeleteCourseDetailResponse response = new DeleteCourseDetailResponse();
		response.setStatus(mapStatus(status));
		return response;
	}

	
	private learn.avinash.com.courses.Status mapStatus(Status status) {
		// TODO Auto-generated method stub
		if(status == Status.FAILURE)
		{
			return learn.avinash.com.courses.Status.FAILURE;
		}
		
		
		
		return learn.avinash.com.courses.Status.SUCESS;
	}


	private GetAllCourseDetailResponse mapAllCourseDetails(List<Course> courses)
	{
		GetAllCourseDetailResponse newGetAllCourseDetailResponse = new GetAllCourseDetailResponse();
		
		for(Course course:courses )
		{
			newGetAllCourseDetailResponse.getCourseDetails().add(mapCourse( course));
		}
		
		return newGetAllCourseDetailResponse;
	}
	
	
	private CourseDetails mapCourse(Course course)
	{
      CourseDetails courseDetails = new CourseDetails();
		
		courseDetails.setId(course.getId());
		
		courseDetails.setName(course.getName());
		
		courseDetails.setDescription(course.getDescription());
		
		return courseDetails;
	}
	
	
}
