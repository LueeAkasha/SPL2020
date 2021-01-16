package bgu.spl.net.api.Users;

public class Student extends User{
    public Student(String username, String password) {
        super(username, password);
    }

    @Override
    public boolean isAdmin() {
        return false;
    }
}
