package bgu.spl.net.api.Messages;

import bgu.spl.net.api.Database;
import bgu.spl.net.api.Users.User;

public abstract class Message {
    protected short Opcode;
    protected Database db = Database.getInstance();
    protected User activeUser = null;

    public Message(short Opcode){
        this.Opcode = Opcode;
    }

    public short getOpcode() {
        return Opcode;
    }

    public abstract Message process(User activeuser);

    public User getActiveUser(){
        return activeUser;
    }

    public void setActiveUser(User activeUser) {
        this.activeUser = activeUser;
    }

    public abstract boolean isACK();

    public abstract byte[] encode();

    protected byte[] shortToBytes(short num)
    {
        byte[] bytesArr = new byte[2];
        bytesArr[0] = (byte)((num >> 8) & 0xFF);
        bytesArr[1] = (byte)(num & 0xFF);
        return bytesArr;
    }
}
