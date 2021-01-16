package bgu.spl.net.api.Messages.ClientMessages;

import bgu.spl.net.api.Messages.Message;
import bgu.spl.net.api.Messages.ServerMessages.ACK;
import bgu.spl.net.api.Messages.ServerMessages.Error;
import bgu.spl.net.api.Users.User;

public class LOGIN extends Message {
    private String username;
    private String password;

    public LOGIN(String username, String password){
        super(Short.parseShort("3"));
        this.username = username;
        this.password = password;
    }

    @Override
    public Message process(User activeuser) {
        if (activeuser == null){
            if(db.Login(username, password)) {
                activeUser = db.getUser(username);
                return new ACK(Short.parseShort("3"));
            }
        }
        return new Error(Short.parseShort("3"));
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
