package bgu.spl.net.api.Messages.ClientMessages;

import bgu.spl.net.api.Messages.Message;
import bgu.spl.net.api.Messages.ServerMessages.ACK;
import bgu.spl.net.api.Messages.ServerMessages.Error;
import bgu.spl.net.api.Users.Admin;
import bgu.spl.net.api.Users.User;

public class ADMINREG extends Message {
    private String username;
    private String password;

    public ADMINREG(String username, String password){
        super(Short.parseShort("1"));
        this.username = username;
        this.password = password;
    }

    @Override
    public Message process(User activeuser) {
        if(db.Register(new Admin(username, password))){
            return new ACK(Short.parseShort("1"));
        }
        return new Error(Short.parseShort("1"));
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
