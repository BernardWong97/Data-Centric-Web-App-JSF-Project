package com.student.Models;

import javax.faces.bean.ManagedBean;

/**
 * A Course class ManagedBean model.
 * @author Bernard Wong
 *
 */
@ManagedBean
public class Course {
	private String cID;
	private String cName;
	private int duration;
	
	// === Constructors ===
	public Course() {}
	
	public Course(String cID, String cName, int duration) {
		this.cID = cID;
		this.cName = cName;
		this.duration = duration;
	}
	
	// === Getters and Setters ===
	public String getcID() {
		return cID;
	}
	
	public void setcID(String cID) {
		this.cID = cID;
	}
	
	public String getcName() {
		return cName;
	}
	
	public void setcName(String cName) {
		this.cName = cName;
	}
	
	public int getDuration() {
		return duration;
	}
	
	public void setDuration(int duration) {
		this.duration = duration;
	}
} // class
