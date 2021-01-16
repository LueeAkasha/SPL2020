package bgu.spl.net.api;


import bgu.spl.net.api.Users.User;

import java.util.*;

public class Course {
    private short courseNum;
    private String courseName;
    private List<Short> KdamCoursesList;
    private int numOfMaxStudents;
    private List<User> RegisterStudents;

    public Course(short courseNum, String courseName,
                  List<Short> KdamCoursesList, int numOfMaxStudents) {
        this.courseNum = courseNum;
        this.courseName = courseName;
        this.KdamCoursesList = KdamCoursesList;
        this.numOfMaxStudents = numOfMaxStudents;
        this.RegisterStudents = new ArrayList<>();
    }

    public short getCourseNum() {
        return courseNum;
    }

    public int getNumOfMaxStudents() {
        return numOfMaxStudents;
    }

    public List<Short> getKdamCoursesList() {
        return KdamCoursesList;
    }

    public String getCourseName() {
        return courseName;
    }

    public synchronized boolean RegisterStudent(User user) {
        boolean kdam_courses = true;
        List<Course> student_courses = user.getCourses();
        List<Short> student_courses_num = new LinkedList<>();
        for (Course course : student_courses) {
            student_courses_num.add(course.getCourseNum());
        }
        for (short course : KdamCoursesList) {
            if (!student_courses_num.contains(course)) {
                kdam_courses = false;
                break;
            }
        }
        if (RegisterStudents.size() < numOfMaxStudents && kdam_courses) {
            RegisterStudents.add(user);
            return true;
        }
        return false;
    }

    public int RegisteredNum() {
        return RegisterStudents.size();
    }

    @Override
    public String toString() {
        String output = "Course: (" + courseNum + ") " + courseName + "\n";
        output += "Seats Available: " + (numOfMaxStudents - RegisterStudents.size()) + " / " + numOfMaxStudents + "\n";
        List<String> names = new ArrayList<>();
        for (User user : RegisterStudents) {
            names.add(user.getUsername());
        }
        Collections.sort(names);
        output += "Students Registered: " + names.toString();
        return output;
    }

    public void Unregister(User user) {
        RegisterStudents.remove(user);
    }
}
