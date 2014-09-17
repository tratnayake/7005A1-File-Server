
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
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
public class Client {
    public static void main(String[] args){
        try {
			Socket socket = new Socket("127.0.0.1", 8005);
		} 
		// if it failed not much I can so
		catch(Exception ec) {
                        System.out.println("Error connectiong to server:" + ec);
			
		}
		Socket socket;
        try {
            socket = new Socket("127.0.0.1", 8005);
            
		String msg = "Connection accepted " + socket.getInetAddress() + ":" + socket.getPort();
		      System.out.println(msg);
                      ObjectOutputStream sOutput;
                   ObjectInputStream sInput;
                   
			sInput  = new ObjectInputStream(socket.getInputStream());
			sOutput = new ObjectOutputStream(socket.getOutputStream());
                        System.out.println("WRiting test");
                        sOutput.writeObject("Test");
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
                
	
		

		
		
    }
}
