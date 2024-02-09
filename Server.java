import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;


import java.io.OutputStream;

public class Server {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private int portNo;
    

    public Server(int portNo) {
        this.portNo = portNo;
    }

    private void processConnection() throws IOException{
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        OutputStream out = clientSocket.getOutputStream();

        //*** Application Protocol *****
        //****************************** */

        String buffer = "";
        String line;

        while((line = in.readLine()) != null ) {
            if(line.equalsIgnoreCase("quit") || line.equals("")) {
                break;
            }
            buffer += line+"\n";
        }

        String filePath = buffer.split(" ")[1]; 

        try
        {
            if (filePath.equals("/")) {
                filePath = "/home.html";
            }
            
            byte[] file = Files.readAllBytes(Paths.get(System.getProperty("user.dir")+"\\docroot"+filePath));
            String response = 
            "HTTP/1.1 200 OK" + "\r\n" +
            "Date: " + new Date() + "\r\n" +
            "Content-Type: text/html" + "\r\n" + 
            "Content-Length: " + (file.length) + "\r\n" +
            "Connection: close\r\n\r\n";

            out.write(response.getBytes());
            out.write(file);
            out.write("\r\n".getBytes());
            out.flush();
                
            in.close();
            out.close();

        }catch(IOException e){
            byte[] errorFile = Files.readAllBytes(Paths.get(System.getProperty("user.dir")+"\\docroot\\404.html"));
            out.write(("HTTP/1.1 404: File Not Found").getBytes());
            out.write(("\r\n\r\n").getBytes());
            out.write(errorFile);
        }
    }

    public void run() throws IOException {
        boolean running = true;
       
        serverSocket = new ServerSocket(portNo);
        System.out.printf("Listen on Port: %d\n", portNo);
        while(running) {
            clientSocket = serverSocket.accept();
            //** Application Protocol
            processConnection();
            clientSocket.close();
        }
        serverSocket.close();
    }
    
    public static void main(String[] args0) throws IOException {
        Server server = new Server(8080);
        server.run();
    }
}
