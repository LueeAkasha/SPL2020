package bgu.spl.net.api.Messages.ClientMessages;

import bgu.spl.net.api.Messages.Message;
import bgu.spl.net.api.Messages.ServerMessages.ACK;
import bgu.spl.net.api.Messages.ServerMessages.Error;
import bgu.spl.net.api.Users.User;

public class KDAMCHECK extends Message {
    private short courseNum;

    public KDAMCHECK(short courseNum) {
        super(Short.parseShort("6"));
        this.courseNum = courseNum;
    }

    @Override
    public Message process(User activeuser) {
        if(!activeuser.isAdmin()) {
            ACK ack = new ACK(Short.parseShort("6"));
            ack.setMessage(db.getKdamCourses(courseNum));
            return ack;
        }
        return new Error(Short.parseShort("6"));
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
