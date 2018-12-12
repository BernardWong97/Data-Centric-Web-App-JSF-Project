package com.student.Controllers;

import java.sql.*;
import java.util.ArrayList;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.naming.NamingException;

import org.neo4j.driver.v1.exceptions.*;

import com.student.DAOs.*;
import com.student.Models.*;

/**
 * A manufacturer controller class between web pages and DAOs.
 * Responsible for all methods about students and catching all Exceptions thrown by DAOs.
 * It is a SessionScoped ManagedBean.
 * @author Bernard Wong
 *
 */
@ManagedBean
@SessionScoped
public class StudentController {
	private MysqlDAO mysqlDAO;
	private Neo4jDAO neo4jDAO;
	private ArrayList<Student> students;

	/**
	 * Constructor initializing both Mysql and Neo4j DAOs and the list of students.
	 */
	public StudentController() {
		try {
			this.setMysqlDAO(new MysqlDAO());
		} catch (NamingException e) {
			e.printStackTrace();
		}
		
		this.setNeo4jDAO(new Neo4jDAO());
		this.setStudents(new ArrayList<Student>());
	}

	/**
	 * Load and save students from Mysql DAO.
	 */
	public void loadStudents() {
		try {
			setStudents(getMysqlDAO().loadStudents());
		} catch (SQLException e) {
			FacesMessage message = new FacesMessage("Error: Cannot connect to MySQL Database");
			FacesContext.getCurrentInstance().addMessage(null, message);
			e.printStackTrace();
		}
	} // loadStudents()

	/**
	 * Parse Student object and call add student method from both Mysql and Neo4j DAOs.
	 * @param student The selected Student object to parse into DAOs.
	 * @return "list_students.xhtml" Go to Manage Students page or empty string to stay on page.
	 */
	public String addStudent(Student student) {
		try {
			getMysqlDAO().addStudent(student);
		} catch (SQLIntegrityConstraintViolationException e) {
			FacesMessage message = new FacesMessage("Error: " + e.getMessage());
			
			if(e.getErrorCode() == 1452)
				message.setSummary("Error: Course " + student.getCourse().getcID().toUpperCase() + " does not exist");

			FacesContext.getCurrentInstance().addMessage(null, message);
			e.printStackTrace();
			
			return "";
		} catch (SQLException e) {
			FacesMessage message = new FacesMessage("Error: " + e.getMessage());

			FacesContext.getCurrentInstance().addMessage(null, message);
			e.printStackTrace();
			
			return "";
		}
		
		try {
			getNeo4jDAO().addStudent(student);
		} catch (ServiceUnavailableException e){
            FacesMessage message = new FacesMessage("Warning: Student "+ student.getName() +" has not been deleted from Neo4j DB, as it offline." );
            FacesContext.getCurrentInstance().addMessage(null, message);
        }

		return "list_students.xhtml";
	} // addStudent()

	/**
	 * Parse Course object and call show students method from Mysql DAO.
	 * @param course The selected Course object to parse into DAO.
	 * @return "course_student.xhtml" Go to Course Student Details page or empty string to stay on page.
	 */
	public String showStudents(Course course) {
		try {
			setStudents(getMysqlDAO().getStudents(course));
		} catch (SQLException e) {
			FacesMessage message = new FacesMessage("Error: Cannot connect to MySQL Database");
			FacesContext.getCurrentInstance().addMessage(null, message);
			e.printStackTrace();
			
			return "";
		}
		
		return "course_student.xhtml";
	} // showStudents()
	
	/**
	 * Parse Student object and call show student method from Mysql DAO.
	 * @param student The selected Student object to parse into DAO.
	 * @return "student_details.xhtml" Go to Full Student Details page.
	 */
	public String showStudent(Student student) {
        setStudents(getMysqlDAO().getStudent(student));

        return "student_details.xhtml";
    } // showStudent()

	/**
	 * Parse Student object and call delete student method from Neo4j and Mysql DAOs.
	 * @param student The selected Student object to parse into DAOs.
	 * @return "list_students.xhtml" Go to Manage Students page or empty string to stay on page.
	 */
	public String deleteStudent(Student student) {
		try {
			getNeo4jDAO().deleteStudent(student);
		} catch (ClientException e) {
			FacesMessage message = new FacesMessage("Error: Student: " + student.getName() + " has not been deleted from any database as he/she has relationships in Neo4j");
			FacesContext.getCurrentInstance().addMessage(null, message);
			e.printStackTrace();
			
			return "";
		} catch (ServiceUnavailableException e){
            FacesMessage message = new FacesMessage("Warning: Student " + student.getName() + " has not been deleted from Neo4j DB, as it offline." );
            FacesContext.getCurrentInstance().addMessage(null, message);
            e.printStackTrace();
        }

		try {
			getMysqlDAO().deleteStudent(student);
		} catch (SQLIntegrityConstraintViolationException e) {
			FacesMessage message = new FacesMessage("Error: " + e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, message);
			e.printStackTrace();
			
			return "";
		} catch (SQLException e) {
			FacesMessage message = new FacesMessage("Error: " + e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, message);
			e.printStackTrace();
			
			return "";
		}
		
		return "list_students.xhtml";
	} // deleteStudent()

	// === Getters and setters ===
	public ArrayList<Student> getStudents() {
		return students;
	}

	public void setStudents(ArrayList<Student> student) {
		this.students = student;
	}

	public MysqlDAO getMysqlDAO() {
		return mysqlDAO;
	}

	public void setMysqlDAO(MysqlDAO mysqlDAO) {
		this.mysqlDAO = mysqlDAO;
	}

	public Neo4jDAO getNeo4jDAO() {
		return neo4jDAO;
	}

	public void setNeo4jDAO(Neo4jDAO neo4jDAO) {
		this.neo4jDAO = neo4jDAO;
	}
} // class
