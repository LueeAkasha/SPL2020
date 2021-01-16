package bgu.spl.net.api.Messages.ServerMessages;

import bgu.spl.net.api.Messages.Message;
import bgu.spl.net.api.Users.User;

import java.nio.charset.StandardCharsets;


public class ACK extends Message {
    private short MessageOp;
    private String message;

    public ACK(short MessageOp) {
        super((short) 12);
        this.MessageOp = MessageOp;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public Message process(User activeuser) {
        return null;
    }

    @Override
    public boolean isACK() {
        return true;
    }

    @Override
    public byte[]   encode() {
        byte[] OpcodeInBytes = this.shortToBytes(Opcode);
        byte[] MessageOpInBytes = this.shortToBytes(MessageOp);
        byte[] output = new byte[OpcodeInBytes.length + MessageOpInBytes.length + 1];
        byte[] messageInBytes;
        if (MessageOp == 6 || MessageOp == 7 || MessageOp == 8 || MessageOp == 9 || MessageOp == 11) {
            messageInBytes = message.getBytes();
            output = new byte[OpcodeInBytes.length + MessageOpInBytes.length + messageInBytes.length + 1];
            for (int j = 0; j < messageInBytes.length; j++)
                output[4 + j] = (messageInBytes[j]);
        }
        output[0] = OpcodeInBytes[0];
        output[1] = OpcodeInBytes[1];
        output[2] = MessageOpInBytes[0];
        output[3] = MessageOpInBytes[1];
        output[output.length - 1] = "\0".getBytes()[0];
        return output;
    }
}
