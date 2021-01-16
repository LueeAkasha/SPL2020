package bgu.spl.net.api.Messages.ClientMessages;

import bgu.spl.net.api.Messages.Message;
import bgu.spl.net.api.Messages.ServerMessages.ACK;
import bgu.spl.net.api.Messages.ServerMessages.Error;
import bgu.spl.net.api.Users.User;

public class UNREGISTER extends Message {
    private short courseNum;

    public UNREGISTER(short courseNum){
        super(Short.parseShort("10"));
        this.courseNum = courseNum;
    }

    public int getCourseNum() {
        return courseNum;
    }

    @Override
    public Message process(User activeuser) {
        if(!activeuser.isAdmin()) {
            if (db.Unregister(activeuser, courseNum)) {
                return new ACK(Short.parseShort("10"));
            }
        }
        return new Error(Short.parseShort("10"));
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
