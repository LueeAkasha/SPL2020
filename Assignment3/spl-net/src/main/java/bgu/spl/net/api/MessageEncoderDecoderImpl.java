package bgu.spl.net.api;

import bgu.spl.net.api.Messages.ClientMessages.*;
import bgu.spl.net.api.Messages.Message;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class MessageEncoderDecoderImpl implements MessageEncoderDecoder<Message> {
    private short Opcode = 0;
    private List<Byte> bytes = new LinkedList<>();
    private int zeroByteNum = 0;

    @Override
    public Message decodeNextByte(byte nextByte) {
        bytes.add(nextByte);
        if (bytes.size() >= 2) {
            byte[] op = new byte[2];
            op[0] = bytes.get(0);
            op[1] = bytes.get(1);
            Opcode = bytesToShort(op);
            if (Opcode == 1 || Opcode == 2 || Opcode == 3) {
                if(nextByte == 0){
                    zeroByteNum++;
                }
                if(zeroByteNum == 2){
                    int first_zero = indexof(bytes.subList(2, bytes.size()), (byte) 0);
                    int second_zero = indexof(bytes.subList(first_zero + 3, bytes.size()), (byte) 0);
                    String username = DecodeIntoString(bytes.subList(2, 2 + first_zero));
                    String password = DecodeIntoString(bytes.subList(3 + first_zero, 3 + first_zero + second_zero));
                    System.out.println("username: " + username);
                    System.out.println("password: " + password);
                    bytes = new LinkedList<>();
                    zeroByteNum = 0;
                    if (Opcode == 1) {
                        Opcode = 0;
                        return new ADMINREG(username, password);
                    }
                    if (Opcode == 2) {
                        Opcode = 0;
                        return new STUDENTREG(username, password);
                    }
                    if (Opcode == 3) {
                        Opcode = 0;
                        return new LOGIN(username, password);
                    }
                }
            }
            if (Opcode == 4 || Opcode == 11) {
                System.out.println("Command: " + Opcode);
                if (Opcode == 4) {
                    Opcode = 0;
                    bytes = new LinkedList<>();
                    return new LOGOUT();
                } else {
                    Opcode = 0;
                    bytes = new LinkedList<>();
                    return new MYCOURSES();
                }
            }
            if (Opcode == 5 || Opcode == 6 || Opcode == 7 || Opcode == 9 || Opcode == 10) {
                if(bytes.size() == 4) {
                    byte[] courseNum = new byte[2];
                    courseNum[0] = bytes.get(2);
                    courseNum[1] = bytes.get(3);
                    short course = bytesToShort(courseNum);
                    bytes = new LinkedList<>();
                    System.out.println("Command: " + Opcode + "  " + course);
                    if (Opcode == 5) {
                        Opcode = 0;
                        return new COURSEREG(course);
                    }
                    if (Opcode == 6) {
                        Opcode = 0;
                        return new KDAMCHECK(course);
                    }
                    if (Opcode == 7) {
                        Opcode = 0;
                        return new COURSESTAT(course);
                    }
                    if (Opcode == 9) {
                        Opcode = 0;
                        return new ISREGISTER(course);
                    }
                    if (Opcode == 10) {
                        Opcode = 0;
                        return new UNREGISTER(course);
                    }
                }
            }
            if (Opcode == 8) {
                if(nextByte == 0){
                    zeroByteNum++;
                }
                if(zeroByteNum == 1){
                    int zero = indexof(bytes.subList(2, bytes.size()), (byte) 0);
                    String username = DecodeIntoString(bytes.subList(2, 2 + zero));
                    System.out.println("Commmand: " + Opcode + " " + username);
                    bytes = new LinkedList<>();
                    zeroByteNum = 0;
                    Opcode = 0;
                    return new STUDENTSTAT(username);
                }
            }
        }
        return null;
    }

    @Override
    public byte[] encode(Message message) {
        return message.encode();
    }

    private short bytesToShort(byte[] byteArr) {
        short result = (short) ((byteArr[0] & 0xff) << 8);
        result += (short) (byteArr[1] & 0xff);
        return result;
    }

    private String DecodeIntoString(List<Byte> lst){
        byte[] str = new byte[lst.size()];
        for (int i = 0; i < lst.size(); i++)
            str[i] = lst.get(i);
        return new String(str, StandardCharsets.UTF_8);
    }

    private int indexof(List<Byte> byt, byte b){
        for (int i = 0; i < byt.size(); i++) {
            if(byt.get(i) == b)
                return i;
        }
        return -1;
    }
}
