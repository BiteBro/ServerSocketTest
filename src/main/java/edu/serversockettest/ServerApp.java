package edu.serversockettest;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.time.ZonedDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerApp implements Runnable {
    private static String message;
public static void main(String[] args) throws IOException, InterruptedException {
    
    ServerApp server = new ServerApp();
    server.serverSocketStart(6666);
}

public void serverSocketStart(int port) throws IOException, InterruptedException{
    try (ServerSocket ss = new ServerSocket(port);){
        System.out.println("Waiting for a connection\n");
        while (true) {
            
            Socket s = ss.accept();
            
            new Thread(() -> {
            
            try (DataInputStream dis = new DataInputStream(s.getInputStream());
                DataOutputStream dos = new DataOutputStream(s.getOutputStream())){

                message = dis.readUTF();
                System.out.println("Client name " + message + " get date and time");
                System.out.println("");
                Thread.sleep(1000);
                dos.writeUTF("Date: " + ZonedDateTime.now().toLocalDate() + " Time: " + ZonedDateTime.now().toLocalTime());
                dos.flush();

                s.close();
            } catch (Exception ex){
                if (ex instanceof SocketException) {
                    System.err.println("-> Waiting for a connection");
                } else Logger.getLogger(ServerApp.class.getName()).log(Level.SEVERE, null, ex);
            }   
        }).start();

                         
        }
    }
}

    @Override
    public void run() {
        try {
            serverSocketStart(6666);
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(ServerApp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}

