package bgu.spl.net.api.Messages.ClientMessages;

import bgu.spl.net.api.Messages.Message;
import bgu.spl.net.api.Messages.ServerMessages.ACK;
import bgu.spl.net.api.Messages.ServerMessages.Error;
import bgu.spl.net.api.Users.User;

public class STUDENTSTAT extends Message {
    private String username;

    public STUDENTSTAT(String username){
        super(Short.parseShort("8"));
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public Message process(User activeuser) {
        if (activeuser.isAdmin() && !db.getUser(username).isAdmin()){
            ACK ack = new ACK(Short.parseShort("8"));
            ack.setMessage(db.StudentStat(username));
            return ack;
        }
        return new Error(Short.parseShort("8"));
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
