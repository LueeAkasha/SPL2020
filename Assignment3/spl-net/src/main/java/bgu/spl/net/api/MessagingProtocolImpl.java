package bgu.spl.net.api;

import bgu.spl.net.api.Messages.Message;
import bgu.spl.net.api.Messages.ServerMessages.Error;
import bgu.spl.net.api.Users.User;

public class MessagingProtocolImpl implements MessagingProtocol<Message> {
    private User activeuser = null;
    private boolean terminate = false;

    @Override
    public Message process(Message msg) {
        Message message;
        if(msg.getOpcode() == 3 || msg.getOpcode() == 1 || msg.getOpcode() == 2 || activeuser != null) {
            message = msg.process(activeuser);
            if (msg.getOpcode() == 3 && message.isACK())
                activeuser = msg.getActiveUser();
            if (msg.getOpcode() == 4 && message.isACK()) {
                activeuser = null;
                terminate = true;
            }
        }else{
            message = new Error(msg.getOpcode());
        }
        return message;
    }

    @Override
    public boolean shouldTerminate() {
        return terminate;
    }
}
