package bgu.spl.net.api.Messages.ServerMessages;

import bgu.spl.net.api.Messages.Message;
import bgu.spl.net.api.Users.User;

public class Error extends Message {
    private short MessageOp;

    public Error(short MessageOp) {
        super(Short.parseShort("13"));
        this.MessageOp = MessageOp;
    }

    @Override
    public Message process(User activeuser) {
        return null;
    }

    @Override
    public boolean isACK() {
        return false;
    }

    @Override
    public byte[] encode() {
        byte[] OpcodeInBytes = this.shortToBytes(Opcode);
        byte[] MessageOpInBytes = this.shortToBytes(MessageOp);
        byte[] output = new byte[OpcodeInBytes.length + MessageOpInBytes.length];
        output[0] = OpcodeInBytes[0];
        output[1] = OpcodeInBytes[1];
        output[2] = MessageOpInBytes[0];
        output[3] = MessageOpInBytes[1];
        return output;
    }
}
