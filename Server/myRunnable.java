import java.io.*;
import java.net.Socket;
import java.lang.Exception;
import java.security.NoSuchAlgorithmException;

public class myRunnable implements Runnable{

    public Socket client_;

    String filename = "";

    myRunnable(Socket client){
        client_ = client;
    }

    @Override
    public void run() {
        try {
            System.out.println("Entered the client handler thread");


            //READ-&-GRAB-FILE-NAME
            //HTTPRequest class
            HTTPRequest http = new HTTPRequest();
            try {
                http.getRequest(client_);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            System.out.println(client_);

            //if it is a websocket is true
            if( http.typeIsWebSocket ) {

                //helper static function in websocket helper class
                try {
                    WebSocketHelper.sendWebSocketHandshake(client_.getOutputStream(), http);
                } catch (NoSuchAlgorithmException ex) {
                    throw new RuntimeException(ex);
                }
                //while true loop for while the type is a web socket
                //will read and then parse through the bits being sent

                while (true) {
                    try {
                       String message = WebSocketHelper.decodeWSMessage(client_.getInputStream());

                        WebSocketHelper.writeWSMessage(message,client_.getOutputStream());
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }

            }

            //else get file name
            else {

                filename = http.getFileName();


                //Opening the file
                //relative path, bc its using current director, don't need '/', if included, won't find file
                File file = new File("Resources" + filename);
                File failfile = new File("Resources/error.html");

                // Get the output stream from the client socket to send the HTTP response
                //we obtain the output stream (outputStream) from the client socket (client). This stream allows
                // us to send data back to the client. and also create a PrintWriter (printWriter) to write text-based
                // data to the output stream.

                OutputStream outputStream = client_.getOutputStream();

                System.out.println("output stream of client created " + outputStream);

                //Create a httpResponse
                HTTPResponse httpResponse = new HTTPResponse(filename, file, outputStream, failfile, http);
                System.out.println("httpreponse created ? but notin " + httpResponse);

            }
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
