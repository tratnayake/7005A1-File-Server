import java.io.*;
import java.net.*;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.*;
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
    
    private static int port= 8005;
    private static boolean started = true;
    
    public Server (int port){
        this.port = port;
    }
    
    public static void main(String[] args) {
        //Create the new server on port 7005
        System.out.println("Start @ Main");
        Server server1 = new Server(8005);  
        //Start the server
        System.out.println("serverStart");
        server1.serverStart();
        
    }
    
    public static void serverStart(){
                    
                   ObjectOutputStream sOutput;
                   ObjectInputStream sInput;
                    
        try {
            ServerSocket servsocket = new ServerSocket(port);
            
            while(started){
                Socket socket = servsocket.accept();
                
                try{
                    System.out.println("Connection Accepted!");
                    
                    // create output first
				sOutput = new ObjectOutputStream(socket.getOutputStream());
				sInput  = new ObjectInputStream(socket.getInputStream());
				// read the username
                                String input = (String) sInput.readObject();
                                System.out.println(input);
				
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
    
    
}
