
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
        
        //Elton: Handle User Input here so they can either so SEND (FILENAME) or GET(FILENAME). Probably have to use a switch.
        System.out.println("Note: it has to be all capital letters, and server must be restarted after a command has been sent");
        System.out.println("Type SEND to send a file otherwise type GET to receive a file");
        Scanner scan = new Scanner(System.in);
        String input = scan.nextLine();
        if (input.equals(send))
        {
        
        //Send the file, three args are: file name, server address and port,
        Client.SEND("LV4_IG.pdf", "127.0.0.1", 7005);
        
        }
        else if(input.equals(get))
        {
            Client.GET("get.txt", "127.0.0.1", 7005);
        }
           
                     
        
    }
    
    public static void SEND(String sendFileName, String serverAddress,int ServerPort)
    {
            
        try {
            //Find the file you want to send
            File file = new File(sendFileName);
            //Create the socket that will deal with the connection
            Socket ServerSock = new Socket(serverAddress, 7005);
                //Debug
                //System.out.println("Local port is:");
                //System.out.println(ServerSock.getLocalPort());
            
            String fileSize = String.valueOf(file.length());
                   
            String COMMAND = "SEND,"+file.getName()+","+fileSize;
            
            System.out.println("COMMAND SEND OUT IS "+COMMAND);
           
            //Send the FileTransfer command to the server
            try {
                ServerSock.getOutputStream().write(COMMAND.getBytes());
                ServerSock.getOutputStream().flush();
            }
            
            catch (Exception e){
                
            }
            
            //Get the response from the server
            byte[] Container = new byte[80];
            ServerSock.getInputStream().read(Container);
            //Encode bits with UTF so we can read it as Humans
            String Response = new String(Container, "UTF-8");
            //Remove whitespace (because we don't use all of the 80 bits)
            Response = Response.trim();
                    //DEBUG Print out the contents of the command
                    //System.out.println("Response from server is "+ Response);
            
            //Server sends back MESSAGE,PORT to initiate connection with.
            String[] responseMessageElements = Response.split(",");
            
                    //DEBUG: Check slot 1 which SHOULD contain the port.
            System.out.println("Reponse Message Elements "+ responseMessageElements[1]);
            
            //The port used for file transfer operations as sent from Server
            int ftport = Integer.parseInt(responseMessageElements[1]);
            
            //Create the new Socket for File Transfer
            Socket fileTransferSock = new Socket(serverAddress, ftport);
            
            //Create a new buffer array to hold the file
            byte[] bufferByteArray = new byte[(int) file.length()];
            
            try {
                //Create a file input stream to take in the file
                FileInputStream fInput = new FileInputStream(file);
                
                //Create a buffered input stream to read that file input stream
                BufferedInputStream bInput = new BufferedInputStream(fInput);
                
                //Read the file through the buffered input stream until the length of the file
                bInput.read(bufferByteArray, 0,bufferByteArray.length);
                
                //Make an output stream to write the file to the socket
                OutputStream os = fileTransferSock.getOutputStream();
                
                //Write everything in buffer to outputstream, until the size of the outputstream
                os.write(bufferByteArray, 0, bufferByteArray.length);

                //flush the output stream
                os.flush();
                
                //Close the socket
                fileTransferSock.close();
                
                //DEBUG
                System.out.println("File has been written to the output stream and the socket is closed.");

            } catch (FileNotFoundException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
            //Create a new buffer array to hold the file
            
    }
    
    public static void GET(String getFileName, String address, int port){
        int newPort;
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

        Socket newSock = new Socket(address, newPort);
        
        byte[] container = new byte[1048];
            try{
        InputStream is = newSock.getInputStream();
   
        int fileSize = is.read(container,0,container.length);
        
                System.out.println("FILE SIZE is "+fileSize);
                
        String savedir = ".\\clientsavedir\\";
         
                System.out.println("FILE OUTPUTSTREAM");
        FileOutputStream fout = new FileOutputStream(savedir+getFileName.trim());

            try (BufferedOutputStream boutput = new BufferedOutputStream(fout)) {
                boutput.write(container,0,fileSize);
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
