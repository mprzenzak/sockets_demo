//wysyłanie ciągu zleceń tekstowych do serwera i
//wyświetlanie komunikatów tekstowych z odpowiedziami serwera


import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class PhoneBookClient extends JFrame implements ActionListener, Runnable {

    private JTextField messageField = new JTextField(20);
    private JTextArea  textArea     = new JTextArea(15,18);

    static final int SERVER_PORT = 25000;
    private String name;
    private String serverHost;
    private Socket socket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;

    public static void main(String[] args) {
        String name;
        String host;

        host = JOptionPane.showInputDialog("Podaj adres serwera");
        name = JOptionPane.showInputDialog("Podaj nazwe klienta");
        if (name != null && !name.equals("")) {
            new MyClient(name, host);
        }
    }

    PhoneBookClient(String name, String host) {
        //super(name)
        this.name = name;
        this.serverHost = host;
        setSize(300, 310);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                try {
                    outputStream.close();
                    inputStream.close();
                    socket.close();
                } catch (IOException e) {
                    System.out.println(e);
                }
            }

            @Override
            public void windowClosed(WindowEvent event) {
                windowClosing(event);
            }
        });
        JPanel panel = new JPanel();
        JLabel messageLabel = new JLabel("Napisz:");
        JLabel textAreaLabel = new JLabel("Dialog:");
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        panel.add(messageLabel);
        panel.add(messageField);
        messageField.addActionListener(this);
        panel.add(textAreaLabel);
        JScrollPane scroll_bars = new JScrollPane(textArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        panel.add(scroll_bars);
        setContentPane(panel);
        setVisible(true);
        new Thread(this).start();  // Uruchomienie dodatkowego w¹tka
        // do oczekiwania na komunikaty od serwera
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override
    public void run() {

    }
}
