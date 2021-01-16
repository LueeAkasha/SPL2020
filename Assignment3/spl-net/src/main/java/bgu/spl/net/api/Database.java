package bgu.spl.net.api;


import bgu.spl.net.api.Users.*;

import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Passive object representing the Database where all courses and users are stored.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add private fields and methods to this class as you see fit.
 */
public class Database {
	private static Database singleton = null;
	private final ConcurrentHashMap<Short, Course> courses = new ConcurrentHashMap<>();
	private final ConcurrentHashMap<String, User> users = new ConcurrentHashMap<>();
	private final ConcurrentHashMap<String, User> active_users = new ConcurrentHashMap<>();
	private final List<Short> sorted_courses = new LinkedList<>();

	//to prevent user from creating new Database
	private Database() {
	}

	/**
	 * Retrieves the single instance of this class.
	 */
	public static Database getInstance() {
		if(singleton == null)
			singleton = new Database();
		return singleton;
	}
	
	/**
	 * loades the courses from the file path specified 
	 * into the Database, returns true if successful.
	 */
	public boolean initialize(String coursesFilePath) {
		try {
			File myObj = new File(coursesFilePath);
			Scanner myReader = new Scanner(myObj);
			while (myReader.hasNextLine()) {
				String data = myReader.nextLine();
				String[] line = data.split("\\|");
				short courseNum = Short.parseShort(line[0]);
				sorted_courses.add(courseNum);
				String courseName = line[1];
				String[] kdamcourses = line[2].substring(1,line[2].length() - 1).split(",");
				List<Short> kdamnums = new ArrayList<>();
				if(kdamcourses.length > 0 && !line[2].equals("[]")){
					for(String s : kdamcourses){
						kdamnums.add(Short.valueOf(s));
					}
				}
				int maxStudentNum = Integer.parseInt(line[3]);
				courses.put(courseNum,new Course(courseNum,courseName,kdamnums,maxStudentNum));
			}
			myReader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public User getUser(String username){
		return users.get(username);
	}

	public synchronized boolean Register(User user) {
		if(!users.containsKey(user.getUsername())) {
			users.putIfAbsent(user.getUsername(), user);
			return true;
		}
		return false;
	}

	public Course getCourse(Short courseNum){
		return courses.get(courseNum);
	}

	public synchronized boolean Login(String username, String password) {
		if(!active_users.containsKey(username)) {
			if (users.containsKey(username)) {
				if(users.get(username).getPassword().equals(password)) {
					active_users.putIfAbsent(username, users.get(username));
					return true;
				}
			}
		}
		return false;
	}

	public void Logout(String username){
		active_users.remove(username);
	}

	public synchronized boolean CourseRegister(User activeUser, short courseNum) {
		Course course = courses.get(courseNum);
		boolean output = false;
		if(courses.containsKey(courseNum)) {
			output = course.RegisterStudent(activeUser);
			if (output)
				activeUser.RegisterCourse(course);
		}
		return output;
	}

	public String getKdamCourses(short courseNum) {
		Course course = courses.get(courseNum);
		return sorting_courses(course.getKdamCoursesList()).toString();
	}

	public boolean Unregister(User activeuser, short courseNum) {
		if(activeuser.getCourses().contains(courses.get(courseNum))) {
			activeuser.getCourses().remove(courses.get(courseNum));
			courses.get(courseNum).Unregister(activeuser);
			return true;
		}
		return false;
	}

	public String CourseStat(short courseNum) {
		Course course = courses.get(courseNum);
		return course.toString();
	}

	public String StudentStat(String username) {
		User student = users.get(username);
		return student.toString();
	}

	public List<Short> sorting_courses(List<Short> lst_courses) {
		List<Short> output = new LinkedList<>();
		for (Short num : sorted_courses) {
			if (lst_courses.contains(num))
				output.add(num);
		}
		return output;
	}
}
