package com.student.Models;

import javax.faces.bean.ManagedBean;

/**
 * A Student class ManagedBean model associated with Course class.
 * @author Bernard Wong
 *
 */
@ManagedBean
public class Student {
	private String sid;
	private String name;
	private String address;
	private Course course;
	
	// === Constructors ===
	public Student() {
		this.course = new Course();
	}

	public Student(String sid, String name, String address, Course course) {
		super();
		this.sid = sid;
		this.name = name;
		this.address = address;
		this.course = course;
	}
	
	// === Getters and Setters ===
	public String getSid() {
		return sid;
	}
	public void setSid(String sid) {
		this.sid = sid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Course getCourse() {
		return course;
	}
	public void setCourse(Course course) {
		this.course = course;
	}	
} // class
