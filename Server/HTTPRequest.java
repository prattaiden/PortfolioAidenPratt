import java.util.HashMap;
import java.net.Socket;

import java.util.Scanner;

public class HTTPRequest {

    public String _filename;

    public HashMap<String, String> headers_ = new HashMap<>();

    Boolean typeIsWebSocket = false;

    //CONSTRUCTOR
    public HTTPRequest() {
        //String input = getRequest(scanner);
        _filename = getFileName();

    }

    //HANDLE REQUEST
    public void getRequest(Socket clientSocket) throws Exception {
        String input = "";

        Scanner scanner = new Scanner(clientSocket.getInputStream());

        //READ-IN-FILE-NAME
        // Check if there is any data available to read from the client socket.

        //for web socket::::
        //get first line and get filename from it... GET /index.html http1.2
        input = scanner.nextLine();

        System.out.println("1st line of header: " + input);

        //then store
        _filename = input.split(" ")[1];

        if (_filename.equals("/")) {
            _filename = "/index.html";
        }

        System.out.println("file name requested: " + _filename);


        input = scanner.nextLine();
        System.out.println(" input " + input);

        while (!input.equals("")) {

            // System.out.println("in the while loop , input: " + _input);
            String key = input.split(": ")[0];
            String val = input.split(": ")[1];
            headers_.put(key, val);
            // System.out.println("header line: " + input);

            // Read the next line of input from the client socket.
            input = scanner.nextLine();
        }
        //what is going into the header
        if (headers_.get("Connection").equals("Upgrade")) {
            typeIsWebSocket = true;
            System.out.println("true!!!!");
        }

    }

    //FILE NAME
    // Split the input string using space as a delimiter to separate elements.
    // expecting an HTTP request, and we're interested in the second element,
    // which is the requested filename.
    public String getFileName() {
        return _filename;
    }

    //FILE EXTENSION
    public String getExtension(String filename) {

// Initialize an empty string to store the file extension
        String extension = "";
// Find the last occurrence of the dot (.) character in the filename
        int i = filename.lastIndexOf('.');
// Check if a dot was found in the filename
        if (i > 0) {
            // Extract the substring after the last dot, which represents the file extension
            extension = filename.substring(i + 1);
            // Print the extracted file extension to the console for debugging
        }
        return extension;
    }
}