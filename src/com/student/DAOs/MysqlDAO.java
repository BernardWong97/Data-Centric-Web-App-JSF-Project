package com.student.DAOs;

import java.sql.*;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.student.Models.*;

/**
 * Mysql Data Access Object (DAO) which connects and communicates with Mysql Database.
 * Responsible for all the functionality of database related methods.
 * @author Bernard Wong
 *
 */
public class MysqlDAO {
	private DataSource mysqlDS;
	private boolean isExist;

	/**
	 * Constructor setting up connection to the Mysql database.
	 * @throws NamingException If database error occurs.
	 */
	public MysqlDAO() throws NamingException {
		Context context = new InitialContext();
		String jndiName = "java:comp/env/jdbc/collegeDB";
		setMysqlDS((DataSource) context.lookup(jndiName));
	}
	
	// === Course Methods ===

	/**
	 * Load courses from the database and save into an ArrayList then return it.
	 * @return courses Returns the list of courses.
	 * @throws SQLException If database error occurs.
	 */
	public ArrayList<Course> loadCourses() throws SQLException {
		Connection conn = mysqlDS.getConnection();
		Statement myStmt = conn.createStatement();
		String query = "select * from course";
		ResultSet rs = myStmt.executeQuery(query);
		ArrayList<Course> courses = new ArrayList<Course>();

		while (rs.next()) {
			String courseID = rs.getString("cID");
			String courseName = rs.getString("cName");
			int duration = rs.getInt("duration");
			Course c = new Course(courseID, courseName, duration);
			courses.add(c);
		} // while

		conn.close();
		return courses;
	} // loadCourses()

	/**
	 * Add a course into the database.
	 * @param c The Course object that user inputed from the web page.
	 * @throws SQLException If database error occurs.
	 */
	public void addCourse(Course c) throws SQLException {
		String cID = c.getcID();
		String cName = c.getcName();
		int duration = c.getDuration();

		Connection conn = mysqlDS.getConnection();
		String query = "insert into course values(?, ?, ?);";
		PreparedStatement statement = conn.prepareStatement(query);
		statement.setString(1, cID);
		statement.setString(2, cName);
		statement.setInt(3, duration);
		statement.executeUpdate();
		
		conn.close();
	} // addCourse()

	/**
	 * Delete a specific course in the database.
	 * @param course The Course object that user selected.
	 * @throws SQLException If database error occurs.
	 */
	public void deleteCourse(Course course) throws SQLException {
		Connection conn = mysqlDS.getConnection();
		PreparedStatement statement = conn.prepareStatement("delete from course where cID=?;");
		statement.setString(1, course.getcID());
		statement.executeUpdate();
		
		conn.close();
	} // deleteCourse()
	
	// === Student Methods ===

	/**
	 * Load students from the database and save into an ArrayList then return it.
	 * @return students Returns the list of students.
	 * @throws SQLException If database error occurs.
	 */
	public ArrayList<Student> loadStudents() throws SQLException {
		Connection conn = mysqlDS.getConnection();
		String query = "select * from student join course on student.cID = course.cID;";
		PreparedStatement statement = conn.prepareStatement(query);
		ResultSet rs = statement.executeQuery();
		ArrayList<Student> students = new ArrayList<Student>();

		while (rs.next()) {
			String sid = rs.getString("sid");
			String name = rs.getString("name");
			String address = rs.getString("address");
			String courseID = rs.getString("cID");
			String courseName = rs.getString("cName");
			int duration = rs.getInt("duration");
			Course c = new Course(courseID, courseName, duration);
			Student s = new Student(sid, name, address, c);
			students.add(s);
		} // while

		conn.close();
		return students;
	} // loadStudents()

	/**
	 * Add a student into the database.
	 * @param s The Student object that user inputed from the web page.
	 * @throws SQLException If database error occurs.
	 */
	public void addStudent(Student s) throws SQLException {
		String sid = s.getSid();
		String name = s.getName();
		String address = s.getAddress();
		String cID = s.getCourse().getcID();

		Connection conn = mysqlDS.getConnection();
		String query = "insert into student values(?, ?, ?, ?);";
		PreparedStatement statement = conn.prepareStatement(query);
		statement.setString(1, sid);
		statement.setString(2, cID);
		statement.setString(3, name);
		statement.setString(4, address);
		statement.executeUpdate();
		
		conn.close();
	} // addStudent()

	/**
	 * Get all the students which associate with the specific course.
	 * @param course The Course object that user selected.
	 * @return students The list of students on the course.
	 * @throws SQLException If database error occurs.
	 */
	public ArrayList<Student> getStudents(Course course) throws SQLException {
		Connection conn = mysqlDS.getConnection();
		String query = "select * from student where cID=?;";
		PreparedStatement statement = conn.prepareStatement(query);
		statement.setString(1, course.getcID());
		ResultSet rs = statement.executeQuery();
		ArrayList<Student> students = new ArrayList<Student>();

		while (rs.next()) {
			String sid = rs.getString("sid");
			String name = rs.getString("name");
			String address = rs.getString("address");

			Student s = new Student(sid, name, address, course);
			students.add(s);
		} // while
		
		conn.close();
		return students;
	} // getStudents()
	
	/**
	 * Return the selected student object.
	 * @param student The Student object that user selected.
	 * @return students The list of students which should only contains one entry.
	 */
	public ArrayList<Student> getStudent(Student student) {
		ArrayList<Student> students = new ArrayList<Student>();
		students.add(student);
		
		return students;
	} // getStudent()

	/**
	 * Delete a specific student in the database.
	 * @param student The Student object that user selected.
	 * @throws SQLException If database error occurs.
	 */
	public void deleteStudent(Student student) throws SQLException {
		Connection conn = mysqlDS.getConnection();
		PreparedStatement statement = conn.prepareStatement("delete from student where sid=?;");
		statement.setString(1, student.getSid());
		statement.executeUpdate();
		
		conn.close();
	} // deleteStudent()

	// === Getters and Setters ===
	public DataSource getMysqlDS() {
		return mysqlDS;
	}

	public void setMysqlDS(DataSource mysqlDS) {
		this.mysqlDS = mysqlDS;
	}

	public boolean isExist() {
		return isExist;
	}

	public void setExist(boolean isExist) {
		this.isExist = isExist;
	}
} // class
