import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.*;
import java.io.*;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

public class Client extends Thread {

    final JTextArea textArea = new JTextArea();
    final JTextArea textArea1 = new JTextArea();
    final JTextArea textArea2 = new JTextArea();
    final JLabel methodLabel = new JLabel("Method");
    final JLabel modeLabel = new JLabel("Mode");

    public JRadioButton aes_button = new JRadioButton("AES",true);
    public JRadioButton des_button = new JRadioButton("DES");
    public JRadioButton cbc_button = new JRadioButton("CBC",true);
    public JRadioButton ofb_button = new JRadioButton("OFB");
    public ButtonGroup method_button = new ButtonGroup();
    public ButtonGroup mode_button = new ButtonGroup();
    private static String serverName;
    private static int PORT;
    private static String name;
    BufferedReader input;
    BufferedReader br;
    PrintWriter output;
    Socket server;
    Cryptor cryptor;
    boolean readContinue;
    DataInputStream dis;
    static byte[] AesKey;
    static byte[] DesKey;
    static byte[] AesIV;
    static byte[] DesIV;


    public Client() {
        this.readContinue = true;
        this.serverName = "localhost";
        this.PORT = 7520;
        this.name = "";


        final JFrame cryptoMessenger = new JFrame("Crypto Messenger");
        cryptoMessenger.getContentPane().setLayout(null);
        cryptoMessenger.setSize(700, 700);
        cryptoMessenger.setResizable(false);
        cryptoMessenger.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        textArea.setBounds(25, 125, 750, 350);
        textArea.setEditable(false);
        textArea.setMargin(new Insets(6, 6, 6, 6));
        textArea.setWrapStyleWord(true);
        JScrollPane jsp = new JScrollPane(textArea);
        jsp.setBounds(25, 125, 650, 250);

        textArea1.setBounds(0, 400, 400, 200);
        textArea1.setMargin(new Insets(6, 6, 6, 6));
        textArea1.setWrapStyleWord(true);
        textArea2.setBounds(350, 400, 400, 200);
        textArea2.setMargin(new Insets(6, 6, 6, 6));
        textArea2.setWrapStyleWord(true);
        textArea2.setEditable(false);

        methodLabel.setBounds(420,0,200,100);
        modeLabel.setBounds(570,0,200,100);
        aes_button.setBounds(370,70,50,50);
        des_button.setBounds(440,70,50,50);
        cbc_button.setBounds(530,70,50,50);
        ofb_button.setBounds(590,70,50,50);
        method_button.add(aes_button);
        method_button.add(des_button);
        mode_button.add(cbc_button);
        mode_button.add(ofb_button);
        final JScrollPane jspm = new JScrollPane(textArea1);
        jspm.setBounds(25, 400, 300, 100);
        final JScrollPane jspm2 = new JScrollPane(textArea2);
        jspm2.setBounds(350, 400, 300, 100);

        final JButton send_button = new JButton("Send");
        final JButton encrypt_button = new JButton("Encrypt");
        send_button.setBounds(575, 510, 100, 35);
        encrypt_button.setBounds(460,510,100,35);

        send_button.setEnabled(false);
        textArea1.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER && send_button.isEnabled()) {
                    sendMessage();
                    textArea.setCaretPosition(textArea.getDocument().getLength());
                }
            }
        });

        send_button.addActionListener(ae -> {
            sendMessage();
            encrypt_button.setEnabled(true);
            send_button.setEnabled(false);
        });

        encrypt_button.addActionListener(ae->{
            try {
                byte[] SelectedIv, SelectedKey;
                String message = textArea1.getText().trim();
                String mode, method;
                if(aes_button.isSelected()){
                    method= "AES";
                    SelectedKey = AesKey;
                    SelectedIv = AesIV;
                }
                else {
                    method = "DES";
                    SelectedKey = DesKey;
                    SelectedIv = DesIV;
                }

                if(cbc_button.isSelected()){
                    mode = "CBC";
                }
                else {
                    mode= "OFB";
                }

                String encrypted = Cryptor.encrypting_message(message, method, mode, SelectedIv, SelectedKey);
                textArea2.append("\n" + encrypted);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
                System.exit(0);
            }
            send_button.setEnabled(true);
            encrypt_button.setEnabled(false);
        });
        final JLabel label = new JLabel("Enter user name: ");
        final JTextField jtfName = new JTextField();
        final JButton connect_button = new JButton("Connect");
        label.setBounds( 50, 200,700, 70);
        jtfName.setBounds(50, 275, 600, 50);
        connect_button.setBounds(300, 350, 100, 50);

        connect_button.addActionListener(ae -> {
            try {
                name = jtfName.getText();
                String pass = "";
                server = new Socket(serverName, PORT);
                input = new BufferedReader(new InputStreamReader(server.getInputStream()));
                output = new PrintWriter(server.getOutputStream(), true);
                dis = new DataInputStream(server.getInputStream());
                br = new BufferedReader(new InputStreamReader(System.in));
                output.println(name);
                output.println(pass);
                String authStr = input.readLine();
                if (authStr.equals("added")) {
                    JOptionPane.showMessageDialog(cryptoMessenger, "User Added.");
                }

                textArea.append("\nWelcome " + name);

                this.AesIV = Base64.getDecoder().decode( dis.readUTF());
                this.DesIV = Base64.getDecoder().decode( dis.readUTF());
                this.AesKey = Base64.getDecoder().decode( dis.readUTF());
                this.DesKey= Base64.getDecoder().decode( dis.readUTF());

                cryptoMessenger.addWindowListener(new java.awt.event.WindowAdapter() {
                    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                        try {
                            output.println("~<N!_/_!D>~" + name + " left.");
                            System.exit(0);
                        } catch (Exception ex) {
                            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });
                Thread r = new Read();
                r.start();

                cryptoMessenger.remove(label);
                cryptoMessenger.remove(jtfName);
                cryptoMessenger.remove(connect_button);
                cryptoMessenger.add(send_button);
                cryptoMessenger.add(encrypt_button);
                cryptoMessenger.add(jsp);
                cryptoMessenger.add(jspm);
                cryptoMessenger.add(jspm2);
                cryptoMessenger.add(methodLabel);
                cryptoMessenger.add(modeLabel);
                cryptoMessenger.add(aes_button);
                cryptoMessenger.add(des_button);
                cryptoMessenger.add(cbc_button);
                cryptoMessenger.add(ofb_button);
                cryptoMessenger.revalidate();
                cryptoMessenger.repaint();
            } catch (Exception ex) {
                textArea.append("\nCould not connect to Server");
                JOptionPane.showMessageDialog(cryptoMessenger, ex.getMessage());
            }
        });
        cryptoMessenger.add(label);
        cryptoMessenger.add(connect_button);
        cryptoMessenger.add(jtfName);
        cryptoMessenger.setVisible(true);
    }

    public void sendMessage() {
        try {
            String message = textArea1.getText().trim();
            String encrypted = textArea2.getText().trim();
            if (message.equals("")) {
                return;
            }
            textArea.append("\n"+encrypted);
            textArea.append("\n" +"<"+name+"> : "+ message);
            if (message.length() < 30) {
                output.println(encrypted);
                output.println("<"+name+"> : "+ message);
            } else {
                output.println("~<N!_/_!D>~"+encrypted);
                output.println("~<N!_/_!D>~" +"<"+name+"> : "+ message);
            }
            textArea1.requestFocus();
            textArea1.setText(null);
            textArea2.requestFocus();
            textArea2.setText(null);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
            System.exit(0);
        }
    }

    public static void main(String[] args) throws Exception {
//        UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel"); 
        Client client = new Client();
    }

    class Read extends Thread {
        public void run() {
            String s;
            String message;
            while (true) {
                try {
                    s = input.readLine();
                    if (s.indexOf("~<N!_/_!D>~") == -1) {
                        textArea.append("\n" + s);
                    } else {
                        textArea.append("\n" + s.substring("~<N!_/_!D>~".length()));
                    }
                } catch (Exception ex) {
//                    System.err.println(ex.getMessage());
                    JOptionPane.showMessageDialog(null, "Connection closed by remote host.");
                    System.exit(0);
                }
            }
        }
    }
}
