package com.student.Controllers;

import java.sql.*;
import java.util.ArrayList;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.naming.NamingException;

import com.student.DAOs.MysqlDAO;
import com.student.Models.Course;

/**
 * A manufacturer controller class between web pages and DAO.
 * Responsible for all methods about courses and catching all Exceptions thrown by DAO.
 * It is a SessionScoped ManagedBean.
 * @author Bernard Wong
 *
 */
@ManagedBean
@SessionScoped
public class CourseController {
	private MysqlDAO mysqlDAO;
	private ArrayList<Course> courses;

	/**
	 * Constructor initializing DAO and the list of course.
	 */
	public CourseController() {
		try {
			this.mysqlDAO = new MysqlDAO();
		} catch (NamingException e) {
			e.printStackTrace();
		}
		
		this.courses = new ArrayList<Course>();
	}

	/**
	 * Load and save courses from DAO.
	 */
	public void loadCourses() {
		try {
			setCourses(getMysqlDAO().loadCourses());
		} catch (SQLException e) {
			FacesMessage message = new FacesMessage("Error: Cannot connect to MySQL Database");
			FacesContext.getCurrentInstance().addMessage(null, message);
			e.printStackTrace();
		}
	} // loadCourses()

	/**
	 * Parse Course object and call add course method from DAO.
	 * @param course The selected Course object to parse into DAO.
	 * @return "list_courses.xhtml" Go to Manage Courses page or empty string to stay on page.
	 */
	public String addCourse(Course course) {
		try {
			getMysqlDAO().addCourse(course);
		} catch (SQLIntegrityConstraintViolationException e) {
			FacesMessage message = new FacesMessage("Error: Course ID " + course.getcID() + " already exists");
			FacesContext.getCurrentInstance().addMessage(null, message);
			e.printStackTrace();
			
			return "";
		} catch (Exception e) {
			FacesMessage message = new FacesMessage("Error: Cannot connect to MySQL Database");
			FacesContext.getCurrentInstance().addMessage(null, message);
			e.printStackTrace();
			
			return "";
		}
		
		return "list_courses.xhtml";
	} // addCourse()
	
	/**
	 * Parse Course object and call delete course method from DAO.
	 * @param course The selected Course object to parse into DAO.
	 * @return "list_courses.xhtml" Refresh the page.
	 */
	public String deleteCourse(Course course){
        try {
            getMysqlDAO().deleteCourse(course);
        } catch (SQLIntegrityConstraintViolationException e) {
            FacesMessage message = new FacesMessage("Error: Cannot Delete Course: " + course.getcID() + " as there are associated Students");
            FacesContext.getCurrentInstance().addMessage(null, message);
            e.printStackTrace();
        }
        catch (Exception e) {
            FacesMessage message = new FacesMessage("Error: Cannot connect to MySQL Database");
            FacesContext.getCurrentInstance().addMessage(null, message);
            e.printStackTrace();
        }

        return "list_courses.xhtml";
    } // deleteCourse()

	// === Getters and setters ===
	public MysqlDAO getMysqlDAO() {
		return mysqlDAO;
	}

	public ArrayList<Course> getCourses() {
		return courses;
	}

	public void setCourses(ArrayList<Course> courses) {
		this.courses = courses;
	}
} // class
