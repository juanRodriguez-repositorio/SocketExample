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

public class SocketExample extends JFrame {
    private JTextArea chatArea;
    private JTextField inputField;
    private JTextField nameField;
    private JTextField ageField;
    private Socket socket;
    private ObjectInputStream reader;
    private ObjectOutputStream writer;
    private JButton sendObject;

    public SocketExample() {
        setTitle("Cliente");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        add(new JScrollPane(chatArea), BorderLayout.CENTER);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(3, 2, 5, 5));

        inputPanel.add(new JLabel("Nombre:"));
        nameField = new JTextField();
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("Edad:"));
        ageField = new JTextField();
        inputPanel.add(ageField);

        inputField = new JTextField();
        inputPanel.add(inputField);

        sendObject = new JButton("Enviar al servidor");
        inputPanel.add(sendObject);

        add(inputPanel, BorderLayout.SOUTH);

        inputField.addActionListener(e -> enviarMensaje(inputField.getText())); // Solo afecta a inputField

        sendObject.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enviarObjeto();
            }
        });
        sendObject.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke("ENTER"), "none");
        sendObject.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke("ENTER"), "none");

        new Thread(() -> conectarServidor()).start();
    }

    private void enviarObjeto() {
        try {
            System.out.println("here");
            String nombre = nameField.getText();
            int edad = Integer.parseInt(ageField.getText());
            if(nombre.equals("")){
                return;
            }
            ;
            writer.writeObject(new Person(nombre, edad));
            writer.flush();
            chatArea.append("Objeto enviado: " + nombre + ", " + edad + "\n");
            nameField.setText("");
            ageField.setText("");
        } catch (Exception e) {
            System.out.println("error al enviar los datos");
            
        }
    }

    private void enviarMensaje(String mensaje) {
        try{
            writer.writeObject(mensaje); // Enviar String
            writer.flush();
            chatArea.append("Yo: " + mensaje + "\n");
            inputField.setText("");
        }catch (IOException e) {
        e.printStackTrace();
        }
        
    }

    private void conectarServidor() {
        try {
            socket = new Socket("localhost", 5000);
            writer = new ObjectOutputStream(socket.getOutputStream());
            reader = new ObjectInputStream(socket.getInputStream());
            while(true){
            
                Object received = reader.readObject(); // Recibir objeto
                System.out.println(received);
                if (received instanceof String) {
                    System.out.println("here");
                    System.out.println(received);
                    chatArea.append("Cliente: "+ received + "\n");
                }else{
                    System.out.println("problemas obteniendo el mensaje");
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SocketExample().setVisible(true));
    }
}
