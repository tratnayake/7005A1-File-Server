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
                    
                   
                    
        try {
            
            
            ServerSocket servsocket = new ServerSocket(port);
            
            while(started){
                Socket socket = servsocket.accept();
                
                try{
                    System.out.println("Connection Accepted!");
                    
                    // create output first
				PrintWriter sOutput = new PrintWriter(socket.getOutputStream(),true);
				BufferedReader sInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                                BufferedReader userInput=(new BufferedReader(new InputStreamReader(System.in)));
                                
                                InputStream stream = socket.getInputStream();
                                byte[] buffer = new byte[80];
                                
                                
				// read the username
                              
                               //String inputLine;
                               //System.out.println(input);
                                
//                                while (stream.read(buffer)> 0){
//                                    System.out.println((new BufferedReader(new InputStreamReader(stream))).readLine());
//                                }
                     String test;           
                                
            while ((test = sInput.readLine()) != null) {
                System.out.println("Not Null");
                
               
                //System.out.println(input.readLine());
                System.out.println(test);
                System.out.println("End Sandwich");
                
                
            }
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
    
    
}
