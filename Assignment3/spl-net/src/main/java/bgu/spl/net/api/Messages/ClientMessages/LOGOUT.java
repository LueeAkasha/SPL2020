package bgu.spl.net.api.Messages.ClientMessages;

import bgu.spl.net.api.Messages.Message;
import bgu.spl.net.api.Messages.ServerMessages.ACK;
import bgu.spl.net.api.Messages.ServerMessages.Error;
import bgu.spl.net.api.Users.User;

public class LOGOUT extends Message {
    public LOGOUT() {
        super(Short.parseShort("4"));
    }

    @Override
    public Message process(User activeuser) {
        if(activeuser != null){
            activeUser = null;
            db.Logout(activeuser.getUsername());
            return new ACK(Short.parseShort("4"));
        }
        return new Error(Short.parseShort("4"));
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
