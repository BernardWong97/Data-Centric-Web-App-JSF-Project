package com.student.DAOs;

import org.neo4j.driver.v1.*;
import static org.neo4j.driver.v1.Values.parameters;

import com.student.Models.Student;

/**
 * Neo4j Data Access Object (DAO) which connects and communicates with Neo4j Database.
 * Responsible for all the functionality of database related methods.
 * @author Bernard Wong
 *
 */
public class Neo4jDAO {

	public Neo4jDAO() {}

	/**
	 * Connect and add a student node to the database.
	 * @param student The Student object inputed by user.
	 */
	public void addStudent(Student student) {
		Driver driver = GraphDatabase.driver("bolt://localhost:7687", AuthTokens.basic("neo4j", "neo4jdb"));
		Session session = driver.session();

		session.writeTransaction(tx -> tx.run("CREATE(:STUDENT{name: $name, address: $address})",
				parameters("name", student.getName(), "address", student.getAddress())));

		session.close();
		driver.close();
	} // addStudent()

	/**
	 * Connect and delete a student node to the database.
	 * @param student The Student object inputed by user.
	 */
	public void deleteStudent(Student student) {
		Driver driver = GraphDatabase.driver("bolt://localhost:7687", AuthTokens.basic("neo4j", "neo4jdb"));
		Session session = driver.session();

		session.run("MATCH(s:STUDENT{name: $name}) delete s;", parameters("name", student.getName()));

		session.close();
		driver.close();
	} // deleteStudent()
} // class
