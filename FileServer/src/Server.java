import java.io.*;
import java.net.*;
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
    File fileToSend = new File("alice.txt");
    private static String FileCommand = null;
    
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
            
            while(started){
                Socket socket = servsocket.accept();
                
                try{
                    System.out.println("Connection Successful!");
                    
                    //Choose what Client wants server to do, Either go into GET or SEND
                    Server.ReadCommand(socket);	
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
    
    
    public static void ReadCommand(Socket socket){
        try {
                //Make a container (byte array) to hold incoming bits of the COMMAND
                //Size 80 is arbitrary
                byte[] Container = new byte[80];
                //Read the inputstream into the Container
                socket.getInputStream().read(Container);
                        //DEBUG: See raw data thats been captured
                        //System.out.println(Container.toString());
               
                //Encode bits with UTF so we can read it as Humans
                String Command = new String(Container, "UTF-8");
                //Remove whitespace (because we don't use all of the 80 bits)
                Command = Command.replaceAll("//s+","");
                    //DEBUG Print out the contents of the command
                System.out.println("Command received is "+Command);
                String[] CommandElements = Command.split(",");
                //Debug:System.out.println(CommandElements[0]);
                FileCommand = CommandElements[0];
                String FileName = CommandElements[1];
                String FileSize = CommandElements[2];
                
                //Debug System.out.println(CommandElements[2]);
                
                switch(FileCommand){
                    //If CLIENT is trying to SEND, then get the address and invoke Servers GET
                    case "SEND": {
                        InetAddress addr = socket.getInetAddress();
                        System.out.println("SEND has been invoked from Address "+ addr+socket.getPort());
                        
                        try {
                            //Create new Socket to wait for File Transfers
                            ServerSocket FileSocket = new ServerSocket(8006);
                            System.out.println("Socket 8006 has been created for a file transfer");
                            //Send RESPONSE message
                            socket.getOutputStream().write(("SENDFILE,"+FileSocket.getLocalPort()).getBytes());
                            System.out.println("Response message sent to the client");
                            //When Client reconnects to send file
                            boolean receiveMode = true;
                            while(receiveMode){
                                System.out.println("Awaiting connections");
                            Socket incomingSocket = FileSocket.accept();
                                System.out.println("File Transfer Connection established");
                            Server.GET(incomingSocket,FileName,FileSize);
                            
                            }
                            
                            
                        } catch (IOException ex) {
                            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    case "GET": {
                        System.out.println("Get is now invoked");
                        try{
                             ServerSocket servSock = new ServerSocket(5005);
                             System.out.println("Socket 5005 has been created for a file transfer");
                             socket.getOutputStream().write(("GETFILE," + servSock.getLocalPort()).getBytes());
                             System.out.println("Response message sent to the client");
                        
                              boolean sendMode = true;
                              
                              while(sendMode){
                                 System.out.println("Awaiting connections");
                                 Socket outgoingSocket = servSock.accept();
                                 System.out.println("File transfer connection established");
                                 Server.SEND(outgoingSocket, FileName);
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
       
        
        System.out.println("Starting GET");
        //Debug
         //System.out.println("Start writing file");
        //Choose your incoming socket
       //Socket socket = incomingSocket;
        //Define buffer size
        int FSize = Integer.parseInt(FileSize.trim());
        System.out.println("Size of File is "+FSize);
       byte[] bufferByteArray = new byte[FSize];
        System.out.println("The size of the barray is "+bufferByteArray.length);
        
        try {
            // Get the inputstream from socket
            InputStream is = incomingSocket.getInputStream();
            // Read the number of bytes that need to be read.
            int fileSizeInBits = is.read(bufferByteArray, 0, bufferByteArray.length);
                
                //Debug: Print file size
                //System.out.println(fileSizeInBits);
            
            
           //Windows: String fileDirectory = fileDirectory = ".\\savedir\\";
           String fileDirectory = fileDirectory = "./savedir/";
            // Create a new file output stream to handle writing the file onto the server.
            FileOutputStream foutput = new FileOutputStream(fileDirectory+FileName.trim());
            // Use a buffered output stream that writes to the file output stream
            BufferedOutputStream boutput = new BufferedOutputStream(foutput);
            
            //Write the file
            boutput.write(bufferByteArray, 0, fileSizeInBits);
            
            
           //CLose the buffer
            boutput.close();
            System.out.println("File saved to /savedir");
           //Close the socket
            //System.out.println("Before close");
            incomingSocket.close();
  
            
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    
    public static void SEND(Socket outgoingSocket, String FileName)
    {
        
        try{
            System.out.println("Starting Send");
            //finds out which file tosend
            File file = new File(FileName.trim());
            //where the file path is
            String abpath = file.getAbsolutePath();
            String fpath = abpath.substring(0,abpath.lastIndexOf(File.separator));
            System.out.println("filepath is " +fpath);
            //creates buffer array to hold file
            byte[] bit = new byte[(int) file.length()];
            System.out.println("File is " + file);
            System.out.println("File name is " + FileName);
            //create a file input stream to take in the file
            FileInputStream  fis = new FileInputStream(file); 
                System.out.println(fis);
            //create buffered input stream to read that file input stream
            BufferedInputStream bis = new BufferedInputStream(fis);
            
            //read the file through the buffered input stream until the length of the file
            bis.read(bit, 0, bit.length);
            
            //make an output stream to write the file  to the socket
            OutputStream os = outgoingSocket.getOutputStream();
               
            //write everything in buffer to outputstream, until the size of the outputstream
            os.write(bit, 0, bit.length);
                System.out.println("file has been written");
                            
                //flush the output stream
            os.flush();
            
            
            //close the socket
            outgoingSocket.close();
            
            //test
            System.out.println("File has been written to output stream and socket is closed");
            }
            
            catch (FileNotFoundException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
    
}
