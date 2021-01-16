package bgu.spl.net.api.Messages.ClientMessages;

import bgu.spl.net.api.Messages.Message;
import bgu.spl.net.api.Messages.ServerMessages.ACK;
import bgu.spl.net.api.Messages.ServerMessages.Error;
import bgu.spl.net.api.Users.User;

public class ISREGISTER extends Message {
    private short courseNum;

    public ISREGISTER(short courseNum){
        super(Short.parseShort("9"));
        this.courseNum = courseNum;
    }

    @Override
    public Message process(User activeuser){
        if(!activeuser.isAdmin()) {
            ACK ack = new ACK(Short.parseShort("9"));
            ack.setMessage(activeuser.getCourses().contains(db.getCourse(courseNum)) ? "REGISTERED" : "NOT REGISTERED");
            return ack;
        }
        return new Error(Short.parseShort("9"));
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
