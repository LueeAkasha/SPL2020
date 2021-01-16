package bgu.spl.net.api.Messages.ClientMessages;

import bgu.spl.net.api.Messages.Message;
import bgu.spl.net.api.Messages.ServerMessages.ACK;
import bgu.spl.net.api.Messages.ServerMessages.Error;
import bgu.spl.net.api.Users.Admin;
import bgu.spl.net.api.Users.Student;
import bgu.spl.net.api.Users.User;

public class STUDENTREG extends Message {
    private String username;
    private String password;

    public STUDENTREG(String username, String password){
        super(Short.parseShort("2"));
        this.username = username;
        this.password = password;
    }

    @Override
    public Message process(User activeuser) {
        if(db.Register(new Student(username, password))){
            return new ACK(Short.parseShort("2"));
        }
        return new Error(Short.parseShort("2"));
    }

    @Override
    public boolean isACK() {
        return false;
    }

    @Override
    public byte[] encode() {
        return null;
    }
}
