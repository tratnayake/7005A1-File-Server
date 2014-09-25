
import java.net.Socket;
import java.io.*;
import java.net.ServerSocket;
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
    public static void main(String[] args) throws Exception{
        
        //Elton: Handle User Input here so they can either so SEND (FILENAME) or GET(FILENAME). Probably have to use a switch.
        
        //Send the file, three args are: file name, server address and port,
        Client.SEND("send.txt", "127.0.0.1", 7005);
        //Client.GET("receive.txt","127.0.0.1",7005);
        
           
                     
        
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
                   
            String COMMAND = "SEND,"+file.getName();
            
            System.out.println(COMMAND);
           
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
    
}
