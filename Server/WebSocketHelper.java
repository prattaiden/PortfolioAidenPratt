import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;


public class WebSocketHelper {

    static Boolean masked_ = false;

   static int finalLength_ = 0;

    //constructor
    public WebSocketHelper() {

    }

    public static Boolean handshakeComplete_ = false;

    public static void sendWebSocketHandshake(OutputStream outputStream, HTTPRequest httpRequest) throws NoSuchAlgorithmException, IOException {

        HashMap headers = httpRequest.headers_;
        System.out.println("ready to send handshake");

        String encodeKey = Base64.getEncoder().encodeToString(MessageDigest.getInstance("SHA-1")
                .digest((headers.get("Sec-WebSocket-Key")
                        + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11").getBytes("UTF-8")));
        if (httpRequest.typeIsWebSocket) {
            System.out.println("handshake made websocket true");

            outputStream.write("HTTP/1.1 101 Switching Protocols\r\n".getBytes());
            outputStream.write("Upgrade: websocket\r\n".getBytes());
            outputStream.write("Connection: Upgrade\r\n".getBytes());
            outputStream.write(("Sec-WebSocket-Accept: " + encodeKey + "\r\n").getBytes());
            outputStream.write("\r\n".getBytes()); // blank line means end of headers

            //once these are written handshake is complete
            //switching the boolean

            outputStream.flush();

            handshakeComplete_ = true;
        }

    }

    public static String decodeWSMessage(InputStream inputStream) throws IOException {

        DataInputStream DIS = new DataInputStream(inputStream);

        byte[] byteArrayShort1 = DIS.readNBytes(2);
        System.out.println("WS 1st 2 bytes: " + byteArrayShort1);

        Byte zeroByte = byteArrayShort1[0];
        // System.out.println(zeroByte);
        Byte firstByte = byteArrayShort1[1];

        //PARSE FOR MASK AND LENGTH
        //Find maskKey, is it masked?
        byte maskKey = (byte) (firstByte & 0x80);
        if (maskKey != 0) {
            masked_ = true;
        }
        //is it a text message or binary? opcode
        byte opCode = (byte) (zeroByte & 0xF0);
        //somehow determine

        byte payloadLengthChecker = (byte) (firstByte & 0x7F);
        System.out.println("payload length: " + payloadLengthChecker);
        //if length is 125 or less, length is just found in B1 here
        if(payloadLengthChecker <= 125){
            finalLength_ = payloadLengthChecker;
        }
        else if (payloadLengthChecker == 126){
           // finalLength_ = payloadLengthChecker;
            byte byteTwoAndThree = (byte) DIS.readShort();
            finalLength_ = byteTwoAndThree;
          //  unsigned Short shortBytes = input.readShort();
        }
        else if (payloadLengthChecker == 127){
            //finalLength_ = payloadLengthChecker;
            byte byteTwoToNine = (byte) DIS.readLong();
            finalLength_ = byteTwoToNine;
        }


        //System.out.println("is it masked: " + masked_ + " length" + finalLength_);

        //if masked is true, read the next four bytes

        byte[] decodedArray = new byte[finalLength_];
        if(masked_){
            //masked array bytes is 4 bytes
            byte[] maskedArray = DIS.readNBytes(4);
            byte[] encodedArray = DIS.readNBytes(finalLength_);
            for(int i = 0; i < finalLength_; i++ ){
                //maskedArray[i] = DIS.readNByte();
                decodedArray[i] = (byte) (encodedArray[i] ^ maskedArray[i%4]);
            }
            //System.out.println("masked array: " + maskedArray);
        }
        else{
            decodedArray =DIS.readNBytes(finalLength_);
        }

        System.out.println("decoded array: " + new String(decodedArray));

        return new String(decodedArray);

    }

    public static void writeWSMessage(String message, OutputStream outputStream) throws IOException {

        DataOutputStream DOS = new DataOutputStream(outputStream);

        //first byte sending back
        //FIN and opcode as text message
        byte firstByte = (byte) 0x81;
        DOS.write(firstByte);

        int decodedMessage = message.length();

        if (decodedMessage <= 125){
            DOS.writeByte(message.length());
        }

        else if (decodedMessage == 126) {
            DOS.write(126);
            DOS.writeShort(message.length());
        }

        else if (decodedMessage == 127){
            DOS.write(127);
            DOS.writeLong(message.length());
        }

        DOS.writeBytes(message);
        DOS.flush();
    }

}


//then go into other method in web socket helper that will do the bit manipulation::::::
//for(int i = 0; i < header.length)
//read rest of header byters based on info from 1st tgwo bytes
//read payload "join davison mainroom" after it is unmasked
//get "mainroom" or create if not existed
//create JSON message "type: join: user: Davison room: mainroom
//room.sendjson(above message)
//the room for each loop to every client
//send json message from above directly to same client that sent it