
import java.net.Socket;
import java.io.*;
//import static java.time.Clock.system;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
 

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Thilina Ratnayake (A00802338) & Elton Sia 
 */
public class Client {
    static String send = "SEND";
    static String get = "GET";
    
    public static void main(String[] args) throws Exception{
        
        while(true){
        //Elton: Handle User Input here so they can either so SEND (FILENAME) or GET(FILENAME). Probably have to use a switch.
        System.out.println("Note: it has to be all capital letters, and server must be restarted after a command has been sent");
        System.out.println("Type SEND to send a file otherwise type GET to receive a file");
        Scanner scan = new Scanner(System.in);
        String input = scan.nextLine();
        
        
        if (input.equals(send))
        {
            System.out.println("Please type in the file you want to send to the server");
            System.out.println("Example: send.txt, get.txt, try.txt, test.txt");
            String chooseSend = scan.nextLine();
             //Send the file, three args are: file name, server address and port,
             Client.SEND(chooseSend, "127.0.0.1", 7005);
        
        }
        else if(input.equals(get))
        {
            System.out.println("Please choose a file to get from the server");
            System.out.println("Example: get.txt, test.txt, send.txt");
            String chooseGet = scan.nextLine();
            Client.GET(chooseGet, "127.0.0.1", 7005);
        }
           
                     
        }     
    }
    
    public static void SEND(String sendFileName, String serverAddress,int ServerPort)
    {
            
        try {
            //Find the file you want to send
            File file = new File(sendFileName);
            //Create the socket that will deal with the connection. NOTE this is for FILE TRANSFER COMMANDS only.
            Socket ServerSock = new Socket(serverAddress, 7005);
            
            String fileSize = String.valueOf(file.length());
                   
            String COMMAND = "SEND,"+file.getName()+","+fileSize;
            
            System.out.println("COMMAND SEND OUT IS "+COMMAND);
           
            //1. Send the FileTransfer command to the server
            try {
                ServerSock.getOutputStream().write(COMMAND.getBytes());
                ServerSock.getOutputStream().flush();
            }
            
            catch (Exception e){
                
            }
            
            //2. RESPONSE: Get the response from the server
            byte[] Container = new byte[80];
            ServerSock.getInputStream().read(Container);
            //Encode bits with UTF so we can read it as Humans
            String Response = new String(Container, "UTF-8");
            //Remove whitespace (because we don't use all of the 80 bits)
            Response = Response.trim();
                 
            System.out.println("RESPONSE from server:"+ Response);
            
            //Server sends back MESSAGE,PORT to initiate connection with.
            String[] responseMessageElements = Response.split(",");
            
            System.out.println("Reponse Message Elements "+ responseMessageElements[1]);
            
            //The port used for file transfer operations as sent from Server
            int ftport = Integer.parseInt(responseMessageElements[1]);
            
            //Create the new Socket for File Transfer
            Socket fileTransferSock = new Socket(serverAddress, ftport);
            
            //Create a new buffer array to hold the file
            byte[] bufferByteArray = new byte[10];
            
            try {
                //Create a file input stream to take in the file
                FileInputStream fInput = new FileInputStream(file);
                
                DataInputStream in = new DataInputStream(fInput);
                
                OutputStream sOutput = fileTransferSock.getOutputStream();
                
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
                
                fileTransferSock.close();
                System.out.println("Socket Closed");
               
                
                
               

            } catch (FileNotFoundException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            } 
            
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
            //Create a new buffer array to hold the file
            
    }
    
    public static void GET(String getFileName, String address, int port){
        int newPort;
        int fSize;
        try{
            
        Socket socket = new Socket(address, port);
        
        File file = new File(getFileName);
        String command ="GET," + file.getName();
        
        
        System.out.println(command);
 
        socket.getOutputStream().write(command.getBytes());
        socket.getOutputStream().flush();
        byte[] bit = new byte[80];
        socket.getInputStream().read(bit);
            
        String change = new String(bit, "UTF-8");
        
        //replace white spaces
        change = change.trim();
        
        System.out.println("change " + change);
        
        String[] Elements = change.split(",");          
            
        newPort = Integer.parseInt(Elements[1]);
        
        fSize = Integer.parseInt(Elements[2]);
            
            System.out.println("fSize is "+fSize);

        Socket newSock = new Socket(address, newPort);
        
        //make the average buffer 10 bits.
        byte[] bba = new byte[1];
            try{
        InputStream is = newSock.getInputStream();
   
       try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
               
                int read;
                int count = 0;
                //while there's more in the input stream, write to buffer
                    while ((is.read(bba)) != -1){
                        //send buffer to outputstream
                      baos.write(bba);
                        count ++;

                    }
                    //write outputstream to byte array
                    byte[] container = baos.toByteArray();
                    System.out.println("container is size: "+container.length);
                    System.out.println("FILE FINISHED being sent over! + count = " +count);
                    
                    //Windows: String fileDirectory = fileDirectory = ".\\savedir\\";
           String fileDirectory = fileDirectory = "./clientsavedir/";
            // Create a new file output stream to handle writing the file onto the server.
            FileOutputStream foutput = new FileOutputStream(fileDirectory+getFileName.trim());
            
            foutput.write(container);
                }
                catch(Exception e) {
                    System.out.println(e);
                }
        System.out.println("File Saved to clientsavedir directory"); 
            }
            catch(Exception e) {
                System.out.println(e);
            }
            
        }
        catch (IOException | NumberFormatException e){
            
        }

        
    }
}
