package bgu.spl.net.api.Users;

import bgu.spl.net.api.Course;
import bgu.spl.net.api.Database;

import java.util.*;

public abstract class User {
    private String username;
    private String password;
    private List<Course> courses;

    public User(String username, String password){
        this.username = username;
        this.password = password;
        this.courses = new ArrayList<>();
    }
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void RegisterCourse(Course course){
        courses.add(course);
    }

    public List<Course> getCourses() {
        return courses;
    }

    public abstract boolean isAdmin();

    @Override
    public String toString() {
        String output = "Student: " + username + "\n";
        List<Short> nums = new ArrayList<>();
        for (Course course : courses) {
            nums.add(course.getCourseNum());
        }
        output += "Courses: " + Database.getInstance().sorting_courses(nums).toString();
        return output;
    }
}
