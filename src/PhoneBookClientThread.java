/*
 * Program: Apliakcja typu „klient-serwer” realizującą funkcję prostej książki telefonicznej
 *    Plik: PhoneBookClientThread.java
 *          - tworzy wątek do obsługi komunikacji sieciowej i na podstawie odpowiednich komend
 *            od użytkownika, wywołuje metody na serwerze
 *
 *    Autor: Mikołaj Przenzak
 *    indeks: 259066
 */

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

class PhoneBookClientThread implements Runnable {
    private Socket socket;
    private String name;
    private PhoneBookServer myServer;

    private ObjectOutputStream outputStream = null;

    PhoneBookClientThread(String prototypeDisplayValue) {
        name = prototypeDisplayValue;
    }

    PhoneBookClientThread(PhoneBookServer server, Socket socket) {
        myServer = server;
        this.socket = socket;
        new Thread(this).start();
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return name;
    }

    public void sendMessage(String message) {
        try {
            outputStream.writeObject(message);
            if (message.equals("exit")) {
                myServer.removeClient(this);
                socket.close();
                socket = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void run() {
        String message;
        try (ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream input = new ObjectInputStream(socket.getInputStream());) {
            outputStream = output;
            name = (String) input.readObject();
            myServer.addClient(this);
            while (true) {
                message = (String) input.readObject();
                myServer.printReceivedMessage(this, message);
                if (message.startsWith("LOAD")) {
                    myServer.LOAD(message.split("LOAD ")[1]);
                } else if (message.startsWith("SAVE")) {
                    myServer.SAVE(message.split("SAVE ")[1]);
                } else if (message.startsWith("GET")) {
                    myServer.GET(message.split("GET ")[1]);
                } else if (message.startsWith("PUT")) {
                    myServer.PUT(message.split(" ")[1], message.split(" ")[2]);
                } else if (message.startsWith("REPLACE")) {
                    myServer.REPLACE(message.split(" ")[1], message.split(" ")[2]);
                } else if (message.startsWith("DELETE")) {
                    myServer.DELETE(message.split("DELETE ")[1]);
                } else if (message.startsWith("LIST")) {
                    myServer.LIST();
                } else if (message.startsWith("CLOSE")) {
                    myServer.CLOSE(socket);
                } else if (message.startsWith("BYE")) {
                    myServer.BYE(this, socket, input, outputStream);
                }
            }
        } catch (Exception e) {
            myServer.removeClient(this);
        }
    }

}
