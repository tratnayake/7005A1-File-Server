import java.io.*;
import java.net.*;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author thilinaratnayake
 */
public class Server {
    
    private static int port= 8006;
    private static boolean started = true;
    File fileToSend = new File("alice.txt");
    
    public Server (int port){
        this.port = port;
    }
    
    public static void main(String[] args) {
        //Create the new server on port 7005
        System.out.println("Start @ Main");
        Server server1 = new Server(8006);  
        //Start the server
        System.out.println("serverStart");
        server1.serverStart();
        
    }
    
    public static void serverStart(){
                    
                   
                    
        try {
            
            
            ServerSocket servsocket = new ServerSocket(port);
            
            while(started){
                Socket socket = servsocket.accept();
                
                try{
                    System.out.println("Connection Accepted!");
                    
                    //Choose what Client wants server to do, Either go into GET or SEND
                    Server.ChooseMode(socket);
                    
                    
                    
                    
                    System.out.println("Starting GET");
                    Server.GET(socket);
                    
                    
                    
//            
                                
				
                }
                
                catch (Exception e){
                    System.out.println("Exception is " +e);
                }
            }
        }
        
        catch(IOException e){
            String msg = " Exception on new ServerSocket: " + e + "\n";
			         System.out.println(msg);
        }
    }
    
    
    
    public static void ChooseMode(Socket socket){
        
        try {
            //Get Input from Stream
           
            
           BufferedReader sInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
            String decision;
            
            while ((decision= sInput.readLine()) != null) {
                System.out.println(decision);
               
            }             
                                
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
        
        
    }
    
    public static void GET(Socket incomingSocket){
        System.out.println("Start Get");
        //Choose your incoming socket
       Socket socket = incomingSocket;
        //Define buffer size
       byte[] bufferByteArray = new byte[1048];
        
        try {
            // Get the inputstream from socket
            InputStream is = socket.getInputStream();
            // Read the number of bytes that need to be read.
            int fileSizeInBits = is.read(bufferByteArray, 0, bufferByteArray.length);
            System.out.println(fileSizeInBits);
            
            // Create a new file output stream to handle writing the file onto the server.
            FileOutputStream foutput = new FileOutputStream("ReceivedFile.txt");
            // Use a buffered output stream that writes to the file output stream
            BufferedOutputStream boutput = new BufferedOutputStream(foutput);
            
            //Write the file
            boutput.write(bufferByteArray, 0, fileSizeInBits);
            
            
           //CLose the buffer
            boutput.close();
           //Close the socket
            System.out.println("Before close");
            socket.close();
  
            
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    
    public static void SEND(File fileToSend, Socket socket){
        while (true){
            
            //Create a new buffer array to hold the file
            byte[] bufferByteArray = new byte[(int) fileToSend.length()];
            
            try {
                //Create a file input stream to take in the file
                FileInputStream fInput = new FileInputStream(fileToSend);
                
                //Create a buffered input stream to read that file input stream
                BufferedInputStream bInput = new BufferedInputStream(fInput);
                
                //Read the file through the buffered input stream until the length of the file
                bInput.read(bufferByteArray, 0,bufferByteArray.length);
                
                //Make an output stream to write the file to the socket
                OutputStream os = socket.getOutputStream();
                
                //Write everything in buffer to outputstream, until the size of the outputstream
                os.write(bufferByteArray, 0, bufferByteArray.length);
                
                System.out.println("File Written");
                
                //flush the output stream
                os.flush();
                
                //Close the socket
                socket.close();
                
                
                
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        
    
}
    
    
}
