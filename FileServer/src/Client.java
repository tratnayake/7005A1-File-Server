
import java.net.Socket;
import java.io.*;
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
    public static void main(String[] args) throws Exception{
        Socket clientSock = new Socket("127.0.0.1", 8006);
        File myFile = new File("send.txt");
        
        
           
                     
        
    }
    
    public void SEND(File file, Socket outgoingSocket)
    {
        BufferedInputStream bis = null;
        try {
            File myFile = file;
            Socket clientSock = outgoingSocket;
            byte[] mybytearray = new byte[(int) myFile.length()];
            bis = new BufferedInputStream(new FileInputStream(myFile));
            bis.read(mybytearray, 0, mybytearray.length);
            System.out.println("Works until here");
            OutputStream os = clientSock.getOutputStream();
            os.write(mybytearray, 0, mybytearray.length);
            os.flush();
            clientSock.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                bis.close();
            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    
}
