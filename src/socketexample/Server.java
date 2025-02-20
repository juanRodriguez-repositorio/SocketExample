/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package socketexample;

/**
 *
 * @author kamus
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;

public class Server extends JFrame {
    private JTextArea chatArea;
    private JTextField inputField;
    private ServerSocket serverSocket;
    private Socket socket;
    private ObjectInputStream reader;
    private ObjectOutputStream writer;
     

    public Server() {
        setTitle("Servidor");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        add(new JScrollPane(chatArea), BorderLayout.CENTER);

        inputField = new JTextField();
        add(inputField, BorderLayout.SOUTH);

        inputField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enviarMensaje(inputField.getText());
                inputField.setText("");
            }
        });

        new Thread(() -> iniciarServidor()).start();
    }

    private void iniciarServidor() {
        try {
            serverSocket = new ServerSocket(5000);
            chatArea.append("Esperando conexión...\n");
            socket = serverSocket.accept();
            chatArea.append("Cliente conectado.\n");

            // Streams de entrada/salida
            writer = new ObjectOutputStream(socket.getOutputStream());
            writer.flush();
            reader = new ObjectInputStream(socket.getInputStream());
            

            // Escuchar mensajes y objetos
            while (true) {
                Object received = reader.readObject(); // Recibir objeto
                System.out.println(received);
                if (received instanceof String) {
                    System.out.println("here");
                    System.out.println(received);
                    chatArea.append("Cliente: "+ received + "\n");
                } else if (received instanceof Person) { // Si es un objeto Persona
                    Person persona = (Person) received;
                    chatArea.append("persona recibida!   --  "+
                            persona.toString()+ "\n");
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void enviarMensaje(String mensaje) {
        try{
            writer.writeObject(mensaje); // Enviar String
            writer.flush(); 
            chatArea.append("Servidor: " + mensaje + "\n");
        }catch (IOException e) {
            e.printStackTrace();
        }
        
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Server().setVisible(true));
    }
}