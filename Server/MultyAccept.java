package Server;

import Commands.Commander;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MultyAccept extends Thread {
    private OutputStream outputStream;
    private InputStream inputStream;

    private byte[] array;

    public MultyAccept(OutputStream outputStream, InputStream inputStream, byte[] array) {
        this.outputStream = outputStream;
        this.array = array;
        this.inputStream = inputStream;
    }

    @Override
    public void run() {
        ServerData serverdata = new ServerData();
        ServerData.Request request = (ServerData.Request) serverdata.bytesToObject(array);
        System.out.println("Запрос: " + request.getMessage().getElem());

        PackageData command = request.getMessage();
        Commander commander = Commander.getINSTANCE();
        command = commander.runCommand(command);
        System.out.println(command.getResult());
        ServerData.Request answer = new ServerData.Request(command);
        try {
            outputStream.write(serverdata.objToBytes(answer));
        } catch (IOException e) {
            System.out.println("Неверный запрос");
        }
        array = new byte[500];
//        try {
//            outputStream.close();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
    }
}
