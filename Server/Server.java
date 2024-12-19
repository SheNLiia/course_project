package Server;

import java.io.*;
import java.net.*;

public class Server {
    public static void main(String[] arg) {
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(8081);

            Socket clientSocket = serverSocket.accept();

            InputStream inputStream = clientSocket.getInputStream();
            OutputStream outputStream = clientSocket.getOutputStream();

            byte[] array = new byte[500];

            while (true) {
                while (inputStream.read(array) == 0) ;
                new MultyAccept(outputStream, inputStream, array).start();
                clientSocket = serverSocket.accept();
                inputStream = clientSocket.getInputStream();
                outputStream = clientSocket.getOutputStream();

                array = new byte[500];
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

