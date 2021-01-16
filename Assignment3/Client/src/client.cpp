#include "../include/connectionHandler.h"
#include <pthread.h>
#include <stdlib.h>
#include <mutex>

using namespace std;
bool connected = true;
mutex send_mutex;
mutex receive_mutex;

vector<string> analyse(string &line){
    vector<string> commandline;
    // I have to handle edge cases here.
    string temp;
    char delimiter = ' ';
    long Size = line.length();
    for(int i = 0; i < Size; i++){
        if(line[i] == delimiter){
            commandline.push_back(temp);
            temp.clear();
        }else{
            temp.push_back(line[i]);
        }
    }
    commandline.push_back(temp);
    return commandline;
}

short bytesToShort(char* bytesArr)
{
    short result = (short)((bytesArr[0] & 0xff) << 8);
    result += (short)(bytesArr[1] & 0xff);
    return result;
}

void shortToBytes(short num, char* bytesArr)
{
    bytesArr[0] = ((num >> 8) & 0xFF);
    bytesArr[1] = (num & 0xFF);
}

class KeyboardReader{

private:
    ConnectionHandler * connectionHandler;
public:
    KeyboardReader(ConnectionHandler &connectionHandler) : connectionHandler(&connectionHandler){}
    void run() {
        while (connected){
            send_mutex.lock();
            if(connected == false){
                break;
            }
            cout<<"Enter command:"<<endl;
            string line;
            getline(cin,line);
            vector<string> commandline = analyse(line);
            if (commandline[0] == "ADMINREG"){
                char  * opcode = (char *)(malloc(2));
                shortToBytes(1, opcode);
                connectionHandler->sendBytes(opcode, 2);
                connectionHandler->sendFrameAscii(commandline[1],'\0');
                connectionHandler->sendFrameAscii(commandline[2],'\0');
                free(opcode);
                receive_mutex.unlock();
            }
            else if (commandline[0] == "STUDENTREG"){
                char  * opcode = (char *)(malloc(2));
                shortToBytes(2, opcode);
                connectionHandler->sendBytes(opcode, 2);
                connectionHandler->sendFrameAscii(commandline[1],'\0');
                connectionHandler->sendFrameAscii(commandline[2],'\0');
                free(opcode);
                receive_mutex.unlock();
            }
            else if (commandline[0] == "LOGIN"){
                char  * opcode = (char *)(malloc(2));
                shortToBytes(3, opcode);
                connectionHandler->sendBytes(opcode, 2);
                connectionHandler->sendFrameAscii(commandline[1],'\0');
                connectionHandler->sendFrameAscii(commandline[2],'\0');
                free(opcode);
                receive_mutex.unlock();
            }
            else if (commandline[0] == "LOGOUT"){
                char  * opcode = (char *)(malloc(2));
                shortToBytes(4, opcode);
                connectionHandler->sendBytes(opcode, 2);
                free(opcode);
                receive_mutex.unlock();
            }
            else if (commandline[0] == "COURSEREG"){
                char  * opcode = (char *)(malloc(2));
                shortToBytes(5, opcode);
                connectionHandler->sendBytes(opcode, 2);
                char * courseNumber = (char *)(malloc(2));
                short course_num = stoi(commandline[1]);
                shortToBytes(course_num, courseNumber);
                connectionHandler->sendBytes(courseNumber,2);
                free(opcode);
                free(courseNumber);
                receive_mutex.unlock();
            }
            else if (commandline[0] == "KDAMCHECK"){
                char  * opcode = (char *)(malloc(2));
                shortToBytes(6, opcode);
                connectionHandler->sendBytes(opcode, 2);
                char * courseNumber = (char *)(malloc(2));
                short course_num = stoi(commandline[1]);
                shortToBytes(course_num, courseNumber);
                connectionHandler->sendBytes(courseNumber,2);
                free(opcode);
                free(courseNumber);
                receive_mutex.unlock();
            }
            else if (commandline[0] == "COURSESTAT"){
                char  * opcode = (char *)(malloc(2));
                shortToBytes(7, opcode);
                connectionHandler->sendBytes(opcode, 2);
                char * courseNumber = (char *)(malloc(2));
                short course_num = stoi(commandline[1]);
                shortToBytes(course_num, courseNumber);
                connectionHandler->sendBytes(courseNumber,2);
                free(opcode);
                free(courseNumber);
                receive_mutex.unlock();
            }
            else if (commandline[0] == "STUDENTSTAT"){
                char  * opcode = (char *)(malloc(2));
                shortToBytes(8, opcode);
                connectionHandler->sendBytes(opcode,2);
                connectionHandler->sendFrameAscii(commandline[1],'\0');
                free(opcode);
                receive_mutex.unlock();
            }
            else if (commandline[0] == "ISREGISTERED"){
                char  * opcode = (char *)(malloc(2));
                shortToBytes(9, opcode);
                connectionHandler->sendBytes(opcode, 2);
                char * courseNumber = (char *)(malloc(2));
                short course_num = stoi(commandline[1]);
                shortToBytes(course_num, courseNumber);
                connectionHandler->sendBytes(courseNumber,2);
                free(opcode);
                free(courseNumber);
                receive_mutex.unlock();
            }
            else if (commandline[0] == "UNREGISTER"){
                char  * opcode = (char *)(malloc(2));
                shortToBytes(10, opcode);
                connectionHandler->sendBytes(opcode, 2);
                char * courseNumber = (char *)(malloc(2));
                short course_num = stoi(commandline[1]);
                shortToBytes(course_num, courseNumber);
                connectionHandler->sendBytes(courseNumber,2);
                free(opcode);
                free(courseNumber);
                receive_mutex.unlock();
            }
            else if (commandline[0] == "MYCOURSES"){
                char  * opcode = (char *)(malloc(2));
                shortToBytes(11, opcode);
                connectionHandler->sendBytes(opcode, 2);
                free(opcode);
                receive_mutex.unlock();
            }
            else{
                cout<< "Damn wrong message detected" <<endl;
                send_mutex.unlock();
            }
        }
    }

};


class SocketReader{

private:
    ConnectionHandler * connectionHandler;
public:
    SocketReader(ConnectionHandler &connectionHandler) : connectionHandler(&connectionHandler){}
    void run() {
        while (connected) {
            receive_mutex.lock();
            char replyBytes[4];
            connectionHandler->getBytes(replyBytes,4);
            char opcodeAsBytes[] = {replyBytes[0], replyBytes[1]};
            char messageOpcodeAsBytes[] = {replyBytes[2],replyBytes[3]};
            short opcode = bytesToShort(opcodeAsBytes);
            short messageOpcode = bytesToShort(messageOpcodeAsBytes);

            if (opcode == 12){
                cout << "ACK " << messageOpcode << endl;
                string optionalPart = "";
                if(connectionHandler->getFrameAscii(optionalPart,'\0')){
                    if(optionalPart != ""){
                        cout << optionalPart << endl;
                    }
                }
                if(messageOpcode == 4){
                    connected = false;
                }
                send_mutex.unlock();
            }
            else if (opcode == 13){

                cout << "ERROR " << messageOpcode << endl;
                send_mutex.unlock();
            }
            else{
                cout << "Damn wrong message detected" <<endl;
                break;
            }
        }
    }
};



int main (int argc, char *argv[]) {
        if (argc < 3) {
            cerr << "Usage: " << argv[0] << " host port" << endl << endl;
            return -1;
        }
        string host = argv[1];
        short port = atoi(argv[2]);

        ConnectionHandler connectionHandler(host, port);
        if (!connectionHandler.connect()) {
            cerr << "Cannot connect to " << host << ":" << port << endl;
            return -1;
        }


        KeyboardReader keyboardReader(connectionHandler);
        SocketReader socketReader(connectionHandler);

        receive_mutex.lock();
        thread keyboardListener(&KeyboardReader::run,keyboardReader);
        thread socketListener(&SocketReader::run,socketReader);

        keyboardListener.join();
        socketListener.join();
        
        connectionHandler.close();
        return 0;
}