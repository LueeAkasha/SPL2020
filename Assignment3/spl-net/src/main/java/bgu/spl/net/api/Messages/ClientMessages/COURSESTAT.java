package bgu.spl.net.api.Messages.ClientMessages;

import bgu.spl.net.api.Messages.Message;
import bgu.spl.net.api.Messages.ServerMessages.ACK;
import bgu.spl.net.api.Messages.ServerMessages.Error;
import bgu.spl.net.api.Users.User;

public class COURSESTAT extends Message {
    private short courseNum;

    public COURSESTAT(short courseNum){
        super(Short.parseShort("7"));
        this.courseNum = courseNum;
    }

    @Override
    public Message process(User activeuser) {
        if (activeuser.isAdmin()){
            ACK ack = new ACK(Short.parseShort("7"));
            ack.setMessage(db.CourseStat(courseNum));
            return ack;
        }
        return new Error(Short.parseShort("7"));
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
