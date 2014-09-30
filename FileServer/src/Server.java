import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
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
    
    private static int port= 7005;
    private static boolean started = true;
    private static String FileCommand = null;
    private static String FileName;
    private static String FileSize;
    private static String[] CommandElements;
    
    public Server (int port){
        this.port = port;
    }
    
    public static void main(String[] args) {
        //Create the new server on port 7005
        System.out.println("Start @ Main");
        Server server1 = new Server(7005);  
        //Start the server
        System.out.println("serverStart");
        server1.serverStart();
        
    }
    
    public static void serverStart(){
                    
                 
                    
        try {
            
            
            ServerSocket servsocket = new ServerSocket(port);
            System.out.println("Server started on port "+port);
            
            while (true){
            while(started){
                System.out.println("Awaiting connections on FILE TRANSFER COMMAND socket");
                Socket socket = servsocket.accept();
                
                try{
                    System.out.println("Connection Successful!");
                    
                    //Choose what Client wants server to do, Either go into GET or SEND
                    Server.ReadCommand(socket);	
                    System.out.println("Done Read");
                    break;
                }    
                catch (Exception e){
                    System.out.println("Exception is " +e);
                }
            }
            }
            
        }
        
        catch(IOException e){
            String msg = " Exception on new ServerSocket: " + e + "\n";
			         System.out.println(msg);
        }
    }

    
    
    public static void ReadCommand(Socket socket){
        try {
                //Make a container (byte array) to hold incoming bits of the COMMAND
                //Size 80 is arbitrary
                byte[] Container = new byte[80];
                //Read the inputstream into the Container from the FILE TRANSFER COMMAND channel
                socket.getInputStream().read(Container);
               
                //Encode bits with UTF so we can read it as Humans
                String Command = new String(Container, "UTF-8");
                //Remove whitespace (because we don't use all of the 80 bits)
                Command = Command.replaceAll("//s+","");
                
                System.out.println("Command received is "+Command);
                
                CommandElements = Command.split(",");

                FileCommand = CommandElements[0];
                
                FileName = CommandElements[1];
                
                switch(FileCommand){
                   
                    //If CLIENT is trying to SEND, then get the address and invoke Servers GET
                    case "SEND": {
                        FileSize = CommandElements[2];
                        System.out.println("FILESIZE Elem 2");
                        InetAddress addr = socket.getInetAddress();
                        System.out.println("FIle Command SEND has been received from CLIENT: "+ addr+socket.getPort()+"and filesize is "+FileSize);
                        
                        try {
                            //Create new Socket to wait for File Transfers
                            ServerSocket FileSocket = new ServerSocket(8006);
                            
                            System.out.println("Socket 8006 has been created for a file transfer");
                            
                            //Send RESPONSE message
                            String response = "SENDFILE,"+FileSocket.getLocalPort();
                            socket.getOutputStream().write(response.getBytes());
                            System.out.println("RESPONSE sent to client: "+response);
                            
                            //When Client reconnects to send file
                            boolean receiveMode = true;
                            
                            while(receiveMode){
                                    System.out.println("FILE TRANSFER Socket created on port "+FileSocket.getLocalPort()+" waiting for connections");
                                    Socket incomingSocket = FileSocket.accept();
                                    System.out.println("File Transfer Connection established");
                                    Server.GET(incomingSocket,FileName,FileSize);
                                    FileSocket.close();
                                    break;
                            
                            }
                            
                            
                        } catch (IOException ex) {
                            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    
                    break;}
                    
                    case "GET": {
                        System.out.println("Get is now invoked");
                        try{
                             File file  = new File(FileName.trim());
                             FileSize = String.valueOf(file.length()); 
                             ServerSocket servSock = new ServerSocket(5005);
                             System.out.println("Socket 5005 has been created for a file transfer");
                             socket.getOutputStream().write(("GETFILE," + servSock.getLocalPort()+","+ FileSize).getBytes());
                             System.out.println("Response message sent to the client");
                             
                        
                              boolean sendMode = true;
                              
                              while(sendMode){
                                 System.out.println("Awaiting connections");
                                 Socket outgoingSocket = servSock.accept();
                                 System.out.println("File transfer connection established");
                                 Server.SEND(outgoingSocket, file, FileSize);
                                 outgoingSocket.close();
                                 servSock.close();
                                 sendMode = false;
                                 
                                }
                              
                        }
                        catch(IOException ex)
                        {
                            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
         
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    public static void GET(Socket incomingSocket, String FileName, String FileSize){
       
        
        System.out.println("DEBUG: Starting GET");
        
        
        int FSize = Integer.parseInt(FileSize.trim());
        System.out.println("Size of File is "+FSize);
        //transient container to hold bit from stream
        byte[] bba = new byte[1];
        System.out.println("The size of the barray is "+bba.length);
        
        try {
            
            // Get the inputstream from socket
            InputStream in = incomingSocket.getInputStream();
            // Read the number of bytes that need to be read.
            
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
               
                int read;
                int count = 0;
                //while there are more bits in the stream, read to transietn container.
                    while ((in.read(bba)) != -1){
                        
                       //write from transient container into output stream
                      baos.write(bba);
                        count ++;

                    }
                    //take everything from output stream and  place into array.
                    byte[] container = baos.toByteArray();
                    //DEBUG: System.out.println("container is size: "+container.length);
                    //DEBUG: System.out.println("FILE FINISHED being sent over! + count = " +count);
                    
                    //Windows: String fileDirectory = fileDirectory = ".\\savedir\\";
            // Create a new file output stream to handle writing the file onto the server.
           String fileDirectory = fileDirectory = "./savedir/";
           //Get rid of any leading or trailing whitespaces
           FileOutputStream foutput = new FileOutputStream(fileDirectory+FileName.trim());
            //Write output file.
            foutput.write(container);
                }
                catch(Exception e) {
                    System.out.println(e);
                }
            
  
            System.out.println("File saved to /savedir");
           //Close the socket
            
            incomingSocket.close();
  
            
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    
    public static void SEND(Socket outgoingSocket, File file, String FileSize)
    {
        
        try{
            System.out.println("Starting Send");
            //finds out which file tosend
            //File file = new File(FileName.trim());
            int fSize = Integer.parseInt(FileSize.trim());
            System.out.println("Size of File is "+fSize);
            //creates buffer array to hold file
            byte[] bufferByteArray = new byte[10];
            //create a file input stream to take in the file
            
           
            
            try {
                //Create a file input stream to take in the file
                FileInputStream fInput = new FileInputStream(file);
                
                DataInputStream in = new DataInputStream(fInput);
                
                OutputStream sOutput = outgoingSocket.getOutputStream();
                
                try {
                int len =0;
                    while ((len = in.read(bufferByteArray)) != -1){
                       sOutput.write(bufferByteArray);
                       
                       

                    }
                    System.out.println("FILE FINISHED being sent over!");
                }
                catch(Exception e) {
                    System.out.println(e);
                }
                
                outgoingSocket.close();
                System.out.println("Socket Closed");
               
                
                
               

            } catch (FileNotFoundException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            } 
            
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
