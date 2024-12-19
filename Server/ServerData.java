package Server;

import java.io.*;

public class ServerData implements Serializable {
    public static class Request implements Serializable {
        private PackageData message;

        public Request(PackageData message) {
            this.message = message;
        }

        public PackageData getMessage() {
            return message;
        }
    }

    public byte[] objToBytes(Object o) {
        try (
                ByteArrayOutputStream b = new ByteArrayOutputStream();
                ObjectOutputStream os = new ObjectOutputStream(b)) {
            os.writeObject(o);
            return b.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Object bytesToObject(byte[] buff) {
        try (
                ByteArrayInputStream b = new ByteArrayInputStream(buff);
                ObjectInputStream os = new ObjectInputStream(b)) {
            return os.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
