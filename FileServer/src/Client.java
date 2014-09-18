
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.*;
import java.net.*;
 

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
			//Socket socket = new Socket("127.0.0.1", 8005);
		} 
		// if it failed not much I can so
		catch(Exception ec) {
                        System.out.println("Error connectiong to server:" + ec);
			
		}
	 try (
                 //Create a new socket
            Socket echoSocket = new Socket("127.0.0.1", 8005);
            PrintWriter out = new PrintWriter(echoSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))
            ) 
         {
            String userInput;
            while ((userInput = stdIn.readLine()) != null) {
                System.out.println("Entered in: "+userInput);
                out.println(userInput);
                System.out.println("Finished writing:" +userInput);
                //System.out.println("echo: " + in.readLine());
            }
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + "127.0.0.1");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
               "127.0.01");
            System.exit(1);
        } 
	
		

		
		
    }
}
