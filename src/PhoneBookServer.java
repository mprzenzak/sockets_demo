/*
 * Program: Apliakcja typu „klient-serwer” realizującą funkcję prostej książki telefonicznej
 *    Plik: PhoneBookServer.java
 *          - definicja klasy PhoneBookServer, program serwera realizujący działania wykonane przez
 *            przez klientów, wywołując metody na obiekcie książki telefonicznej
 *
 *    Autor: Mikołaj Przenzak
 *    indeks: 259066
 *    Data: 25.01.2022 r.
 */


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;


class PhoneBookServer extends JFrame implements ActionListener, Runnable {

    private static final long serialVersionUID = 1L;

    static final int SERVER_PORT = 25000;

    public static void main(String[] args) {
        new PhoneBookServer();
    }


    private JLabel clientLabel = new JLabel("Odbiorca:");
    private JLabel messageLabel = new JLabel("Napisz:");
    private JLabel textAreaLabel = new JLabel("Dialog:");
    private JComboBox<PhoneBookClientThread> clientMenu = new JComboBox<PhoneBookClientThread>();
    private JTextField messageField = new JTextField(20);
    private JTextArea textArea = new JTextArea(15, 18);
    private JScrollPane scroll = new JScrollPane(textArea,
            ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    private PhoneBook phoneBook = new PhoneBook();

    Thread thread = new Thread(this);


    PhoneBookServer() {
        super("SERWER");
        setSize(300, 340);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();
        panel.add(clientLabel);
        clientMenu.setPrototypeDisplayValue(new PhoneBookClientThread("#########################"));
        panel.add(clientMenu);
        panel.add(messageLabel);
        panel.add(messageField);
        messageField.addActionListener(this);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        panel.add(textAreaLabel);
        textArea.setEditable(false);
        panel.add(scroll);
        setContentPane(panel);
        setVisible(true);
        thread.start();
    }

    synchronized public void printReceivedMessage(PhoneBookClientThread client, String message) {
        String text = textArea.getText();
        textArea.setText(client.getName() + " >>> " + message + "\n" + text);
    }

    synchronized public void printSentMessage(PhoneBookClientThread client, String message) {
        String text = textArea.getText();
        textArea.setText(client.getName() + " <<< " + message + "\n" + text);
    }

    synchronized void addClient(PhoneBookClientThread client) {
        clientMenu.addItem(client);
    }

    synchronized void removeClient(PhoneBookClientThread client) {
        clientMenu.removeItem(client);
    }

    void LOAD(String fileName) throws IOException {
        textArea.setText(phoneBook.LOAD(fileName));
    }

    void SAVE(String fileName) throws IOException {
        textArea.setText(phoneBook.SAVE(fileName));
    }

    void GET(String name) {
        textArea.setText(phoneBook.GET(name));
    }

    void PUT(String name, String number) {
        textArea.setText(phoneBook.PUT(name, number));
    }

    void REPLACE(String name, String number) {
        textArea.setText(phoneBook.REPLACE(name, number));
    }

    void DELETE(String name) {
        textArea.setText(phoneBook.DELETE(name));
    }

    void LIST() {
        textArea.setText(phoneBook.LIST());
    }

    void CLOSE(Socket socket) throws IOException {
        socket.close();
        socket.close();
    }

    void BYE(PhoneBookClientThread phoneBookClientThread, Socket socket, ObjectInputStream input, ObjectOutputStream outputStream) throws IOException {
        removeClient(phoneBookClientThread);
        socket.close();
        input.close();
        outputStream.close();
        socket.close();
    }

    public void actionPerformed(ActionEvent event) {
        String message;
        Object source = event.getSource();
        if (source == messageField) {
            PhoneBookClientThread client = (PhoneBookClientThread) clientMenu.getSelectedItem();
            if (client != null) {
                message = messageField.getText();
                printSentMessage(client, message);
                client.sendMessage(message);
            }
        }
        repaint();
    }

    public void run() {
        boolean socket_created = false;

        try (ServerSocket server = new ServerSocket(SERVER_PORT)) {
            String host = InetAddress.getLocalHost().getHostName();
            System.out.println("Serwer zostal uruchomiony na hoscie " + host);
            socket_created = true;

            while (true) {
                Socket socket = server.accept();
                if (socket != null) {
                    new PhoneBookClientThread(this, socket);
                }
            }
        } catch (IOException e) {
            System.out.println(e);
            if (!socket_created) {
                JOptionPane.showMessageDialog(null, "Gniazdko dla serwera nie może być utworzone");
                System.exit(0);
            } else {
                JOptionPane.showMessageDialog(null, "BLAD SERWERA: Nie mozna polaczyc sie z klientem ");
            }
        }
    }
}
