/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatappp;

import java.awt.BorderLayout;
import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.SOUTH;
import java.awt.Color;
import static java.awt.Color.black;
import static java.awt.Color.blue;
import static java.awt.Color.orange;
import static java.awt.Color.red;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.DefaultCaret;

/**
 *
 * @author Mustafa
 */
public class Client extends javax.swing.JFrame { // GUI Class.

    /**
     * Creates new form Client
     */
    static Socket c_Socket, c2_Socket; // Client Socket & Private clients Socket.
    static DataOutputStream dOut;
    static DataInputStream dIn;
    static boolean c;
    static String ip = ""; // variable stores the ip address.
    static String msgIn; // variables stores the input.
    static JFrame frame11; // private chat frame.
    static JTextArea text_Area11; //private chat textArea.
    static JTextField text_field11; // Private chat TextField.

    public static class Clients_Private_l extends Thread {

        private ServerSocket ss;
        private Socket cc_ss;

        private DataInputStream dIN_s;
        private DataOutputStream dOUT_s;
        String name;

        Clients_Private_l(String naame) {
            name = naame;
        }

        @Override
        public void run() {
            super.run();
            JFrame frm1 = new JFrame();
            frm1.setSize(800, 800);

            JButton eBtn1 = new JButton("Exit");
            eBtn1.setForeground(Color.white);
            eBtn1.setBackground(Color.LIGHT_GRAY);

            JTextField tf1 = new JTextField();
            JTextArea ta1 = new JTextArea();
            JScrollPane jsp1 = new JScrollPane(ta1);

            frm1.add(jsp1, BorderLayout.CENTER);
            frm1.add(tf1, BorderLayout.AFTER_LAST_LINE);
            frm1.add(eBtn1, BorderLayout.BEFORE_FIRST_LINE);
            frm1.setVisible(true);

            try {
                ss = new ServerSocket(9097);
                cc_ss = ss.accept();
                dIN_s = new DataInputStream(cc_ss.getInputStream());
                dOUT_s = new DataOutputStream(cc_ss.getOutputStream());

            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }

            tf1.addKeyListener(new KeyAdapter() {
                public void keyReleased(KeyEvent eb) {
                    if (eb.getKeyCode() == KeyEvent.VK_ENTER) {
                        String msgOut = "";
                        try {

                            msgOut = tf1.getText();

                            dOUT_s.writeUTF(msgOut);

                        } catch (Exception e) {
                        }
                        ta1.append("You : " + msgOut + "\n");
                        tf1.setText(null);

                    }
                }
            });
            eBtn1.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    frm1.setVisible(false);
                    try {
                        dIN_s.close();
                        dOUT_s.close();
                    } catch (IOException ex) {
                        Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }

            });
            while (true) {
                try {
                    String msgIn = dIN_s.readUTF();
                    ta1.append(name + " :: " + msgIn + "\n");
                } catch (IOException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    }

    public static class Clients_Private_c extends Thread {

        private ServerSocket ss;
        private Socket cc_ss;
        private Socket cc_Socket;
        private DataInputStream dIN_s, dIN_c;
        private DataOutputStream dOUT_s, dOUT_c;
        String name;

        Clients_Private_c(String naame) {
            name = naame;
        }

        @Override
        public void run() {
            super.run(); //To change body of generated methods, choose Tools | Templates

            JFrame frm = new JFrame();
            frm.setSize(800, 800);

            JButton eBtn = new JButton("Exit");
            eBtn.setForeground(Color.white);
            eBtn.setBackground(Color.LIGHT_GRAY);

            JTextField tf = new JTextField();
            JTextArea ta = new JTextArea();
            JScrollPane jsp = new JScrollPane(ta);

            frm.add(jsp, BorderLayout.CENTER);
            frm.add(tf, BorderLayout.AFTER_LAST_LINE);
            frm.add(eBtn, BorderLayout.BEFORE_FIRST_LINE);
            frm.setVisible(true);
            try {
                cc_Socket = new Socket(ip_field.getText(), 9097);
                dIN_c = new DataInputStream(cc_Socket.getInputStream());
                dOUT_c = new DataOutputStream(cc_Socket.getOutputStream());
            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
            tf.addKeyListener(new KeyAdapter() {
                public void keyReleased(KeyEvent eb) {
                    if (eb.getKeyCode() == KeyEvent.VK_ENTER) {
                        String msgOut = "";
                        try {

                            msgOut = tf.getText();

                            dOUT_c.writeUTF(msgOut);

                        } catch (Exception e) {
                        }
                        ta.append("You : " + msgOut + "\n");
                        tf.setText(null);

                    }
                }
            });
            eBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    frm.setVisible(false);
                    try {
                        dIN_c.close();
                        dOUT_c.close();
                    } catch (IOException ex) {
                        Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }

            });

            while (true) {
                try {
                    String msgIn = dIN_c.readUTF();
                    ta.append(name + " :: " + msgIn + "\n");
                } catch (IOException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    }

    public static class private_C extends Thread { // Private Chat Class.

        DataInputStream dIn2;
        DataOutputStream dOut2;

        private_C() { // Setting up the frame & initiating Connection.
            try {
                c2_Socket = new Socket(ip_field.getText(), 9099);

            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
            frame11 = new JFrame(name_field.getText() + " ~~~~ Private Conversation with Server");
            frame11.setSize(800, 700);
            frame11.setLayout(new BorderLayout());

            text_Area11 = new JTextArea();
            text_Area11.setSize(400, 200);
            JScrollPane jsp = new JScrollPane(text_Area11);
            JButton btn = new JButton("Exit");
            btn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        dOut2.writeUTF("EXIT_NOW");
                        frame11.setVisible(false);

                        dIn2.close();
                        dOut2.close();
                    } catch (IOException ex) {
                        Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    //To change body of generated methods, choose Tools | Templates.
                }
            });
            text_field11 = new JTextField(3);
            text_field11.setSize(200, 100);

            frame11.add(btn, BorderLayout.WEST);
            frame11.add(jsp, CENTER);
            frame11.add(text_field11, SOUTH);
            frame11.setVisible(true);
            text_Area11.setEditable(false);
            DefaultCaret caret = (DefaultCaret) text_Area11.getCaret();
            caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        }

        @Override
        public void run() {
            super.run(); //To change body of generated methods, choose Tools | Templates.
            try {
                dOut2 = new DataOutputStream(c2_Socket.getOutputStream());
            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
            text_field11.addKeyListener( // Send msg When Enter Is Pressed.
                    new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        String msgout = text_field11.getText();
                        try {
                            if (!msgout.equals("EXIT_NOW")) {
                                dOut2.writeUTF(msgout);
                            } else {
                                dOut2.writeUTF("EXIT_NOW");
                                frame11.setVisible(false);

                                dIn2.close();
                                dOut2.close();
                            }
                        } catch (IOException ex) {
                            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        text_Area11.setText(text_Area11.getText().trim() + "\nYou :: " + text_field11.getText());
                        text_field11.setText(null);

                    }
                }
            }
            );
            try {
                dIn2 = new DataInputStream(c2_Socket.getInputStream());
            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }

            while (true) { // Reading loop.

                try {
                    String msgin = dIn2.readUTF();
                    if (!msgin.equals("EXIT_NOW")) {
                        text_Area11.setText(text_Area11.getText().trim() + "\nServer :: " + msgin);
                    } else {
                        frame11.setVisible(false);

                        dIn2.close();
                        dOut2.close();
                    }

                } catch (IOException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

        }

    }

    private static class c_connect extends Thread { // Connection Thread Class.

        String c_Name = "";

        public c_connect() {
            text_Area.setText("~You Are Connecting~\n");

        }

        @Override
        public void run() {
            super.run(); //To change body of generated methods, choose Tools | Templates.

            ip = ip_field.getText();
            try {
                c_Socket = new Socket(ip, 9095);
            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                text_Area.setText("Socket Creation error : " + ex);
                c_Btn.setEnabled(true);
            }
            try {
                dOut = new DataOutputStream(c_Socket.getOutputStream());
            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                text_Area.setText("Output stream error : " + ex);
            }
            try {
                dIn = new DataInputStream(c_Socket.getInputStream());

            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                text_Area.setText("Input Stream error : " + ex);
            }
            c_Name = name_field.getText();
            try {
                dOut.writeUTF(c_Name); // Sending client name to server to be checked.
            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }

            String name_validation;
            try {

                name_validation = dIn.readUTF(); // recieving name check result from server.
                if (name_validation.equals("Name Exists!~!~!@@$!")) {   // if name exists.
                    name_field.setText(null);
                    name_label.setForeground(orange);
                    try {
                        c_connect.sleep(1000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    text_Area.append("!!Connection Failure!!\n");

                    name_label.setText("!~Used NickName~!");
                    c_Socket.close();

                    c_Btn.setEnabled(true);
                    name_field.setEditable(true);
                    send_Btn.setEnabled(false);
                    msg_field.setEnabled(false);
                } else { // If name doesn't exist.
                    try {
                        c_connect.sleep(1000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    text_Area.append("~Connected Successfully~");

                    while (true) { //Reading Loop.

                        msgIn = "";
                        msgIn = dIn.readUTF();
                        if (msgIn.equals("")) {

                        }

                        if (msgIn.charAt(0) == '§' && msgIn.charAt(1) == '§' && msgIn.charAt(2) == '§') { // Decoding the online user update msg from server.
                            msgIn = msgIn.substring(1);
                            msgIn = msgIn.substring(1);
                            msgIn = msgIn.substring(1);
                            o_u_Area.setText(msgIn);

                        } else {
                            if (msgIn.charAt(0) == 'ü' && msgIn.charAt(1) == 'ü' && msgIn.charAt(2) == 'ü') {
                                msgIn = msgIn.substring(1);
                                msgIn = msgIn.substring(1);
                                msgIn = msgIn.substring(1);
                                new Clients_Private_c(msgIn).start();
                            }
                            if (msgIn.equals(c_Name + " :: " + "DISCONNECTING!~!")) { // when client disconnects.
                                msgIn = "\n-----------------------------------------\n    " + c_Name + " Disconnected!\n-----------------------------------------\n";
                            }
                            if (msgIn.equals("PRIVATE_CHAT_REQ")) { // when server send Pchat Request.

                                msgIn = "";
                                p_c_a_Btn.setVisible(true);
                                p_c_label.setVisible(true);
                                p_c_label2.setVisible(true);
                                for (int v = 0; v < 6; v++) { // Request popup effect.

                                    try {
                                        p_c_label.setForeground(red);
                                        p_c_label2.setForeground(red);
                                        sleep(500);
                                        p_c_label.setForeground(black);
                                        p_c_label2.setForeground(black);
                                        sleep(500);
                                    } catch (InterruptedException ex) {
                                        Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                                    }

                                }

                            }
                            if (msgIn.equals("!~!~!~KICKED!#$#@~!@@")) { //When kick from server.

                                dOut.writeUTF("");
                                c_Socket.close();
                                text_Area.setText(text_Area.getText() + "\n~You'r Kicked From The Server~");
                                ip_field.setEnabled(false);
                                name_field.setEnabled(false);
                                c_Btn.setForeground(red);
                                c_Btn.setText("Forbidden Conn.!");
                                send_Btn.setEnabled(false);
                                msg_field.setEnabled(false);
                                d_Btn.setEnabled(false);
                                c_Btn.setEnabled(false);
                                c = false;
                            } else {
                                if (msgIn.equals("-----------------------------\n~" + c_Name + " Connected~\n-----------------------------\n")) {
                                    text_Area.setText(text_Area.getText());
                                }
                                text_Area.setText(text_Area.getText() + "\n" + msgIn);
                            }
                        }
                    }
                }

            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                if (c == false) {

                } else {
                    text_Area.setText(text_Area.getText() + "\n~Disconnected From Sever~");

                    name_field.setEditable(true);
                    send_Btn.setEnabled(false);
                    msg_field.setEnabled(false);
                    c_Btn.setEnabled(true);
                }

            }

        }

    }

    public Client() {
        initComponents();
        setTitle("<~Anonymous Client~>");
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        ola = new javax.swing.JDesktopPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        text_Area = new javax.swing.JTextArea();
        msg_field = new javax.swing.JTextField();
        c_Btn = new javax.swing.JButton();
        name_field = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        d_Btn = new javax.swing.JButton();
        ip_field = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel3 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        name_label = new javax.swing.JLabel();
        send_Btn = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        p_c_label = new javax.swing.JLabel();
        p_c_label2 = new javax.swing.JLabel();
        p_c_a_Btn = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JSeparator();
        jScrollPane2 = new javax.swing.JScrollPane();
        o_u_Area = new javax.swing.JTextArea();
        jLabel5 = new javax.swing.JLabel();
        rpc_Btn = new javax.swing.JButton();
        rpc_field = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        text_Area.setColumns(20);
        text_Area.setRows(5);
        text_Area.setRequestFocusEnabled(false);
        jScrollPane1.setViewportView(text_Area);

        c_Btn.setText("Connect");
        c_Btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                c_BtnActionPerformed(evt);
            }
        });

        name_field.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                name_fieldActionPerformed(evt);
            }
        });

        jLabel1.setText("Name");

        d_Btn.setText("Disconnect");
        d_Btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                d_BtnActionPerformed(evt);
            }
        });

        ip_field.setText("127.0.0.1");
        ip_field.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ip_fieldActionPerformed(evt);
            }
        });

        jLabel2.setText("IP- Address");

        jLabel3.setText("< Emotions >");

        jButton2.setText(" :) ");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText(" :( ");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText(":'( ");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setText(" :D");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setText(" ^_^");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton7.setText(":P");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton8.setText(";)");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jButton9.setText(":O");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Dialog", 1, 10)); // NOI18N

        send_Btn.setText("Send");
        send_Btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                send_BtnActionPerformed(evt);
            }
        });

        jButton1.setText("Exit");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/chatappp/Ali2.jpg"))); // NOI18N
        jButton11.setText("jButton11");

        p_c_label.setText("Server Is Requesting ");

        p_c_label2.setText("Private Session");

        p_c_a_Btn.setText("Accept");
        p_c_a_Btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                p_c_a_BtnActionPerformed(evt);
            }
        });

        o_u_Area.setEditable(false);
        o_u_Area.setColumns(20);
        o_u_Area.setRows(5);
        jScrollPane2.setViewportView(o_u_Area);

        jLabel5.setText("~Online Users~");

        rpc_Btn.setText("Request P_Chat");
        rpc_Btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rpc_BtnActionPerformed(evt);
            }
        });

        ola.setLayer(jScrollPane1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        ola.setLayer(msg_field, javax.swing.JLayeredPane.DEFAULT_LAYER);
        ola.setLayer(c_Btn, javax.swing.JLayeredPane.DEFAULT_LAYER);
        ola.setLayer(name_field, javax.swing.JLayeredPane.DEFAULT_LAYER);
        ola.setLayer(jLabel1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        ola.setLayer(d_Btn, javax.swing.JLayeredPane.DEFAULT_LAYER);
        ola.setLayer(ip_field, javax.swing.JLayeredPane.DEFAULT_LAYER);
        ola.setLayer(jLabel2, javax.swing.JLayeredPane.DEFAULT_LAYER);
        ola.setLayer(jSeparator1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        ola.setLayer(jLabel3, javax.swing.JLayeredPane.DEFAULT_LAYER);
        ola.setLayer(jButton2, javax.swing.JLayeredPane.DEFAULT_LAYER);
        ola.setLayer(jButton3, javax.swing.JLayeredPane.DEFAULT_LAYER);
        ola.setLayer(jButton4, javax.swing.JLayeredPane.DEFAULT_LAYER);
        ola.setLayer(jButton5, javax.swing.JLayeredPane.DEFAULT_LAYER);
        ola.setLayer(jButton6, javax.swing.JLayeredPane.DEFAULT_LAYER);
        ola.setLayer(jButton7, javax.swing.JLayeredPane.DEFAULT_LAYER);
        ola.setLayer(jButton8, javax.swing.JLayeredPane.DEFAULT_LAYER);
        ola.setLayer(jButton9, javax.swing.JLayeredPane.DEFAULT_LAYER);
        ola.setLayer(jLabel4, javax.swing.JLayeredPane.DEFAULT_LAYER);
        ola.setLayer(name_label, javax.swing.JLayeredPane.DEFAULT_LAYER);
        ola.setLayer(send_Btn, javax.swing.JLayeredPane.DEFAULT_LAYER);
        ola.setLayer(jButton1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        ola.setLayer(jButton11, javax.swing.JLayeredPane.DEFAULT_LAYER);
        ola.setLayer(p_c_label, javax.swing.JLayeredPane.DEFAULT_LAYER);
        ola.setLayer(p_c_label2, javax.swing.JLayeredPane.DEFAULT_LAYER);
        ola.setLayer(p_c_a_Btn, javax.swing.JLayeredPane.DEFAULT_LAYER);
        ola.setLayer(jSeparator2, javax.swing.JLayeredPane.DEFAULT_LAYER);
        ola.setLayer(jScrollPane2, javax.swing.JLayeredPane.DEFAULT_LAYER);
        ola.setLayer(jLabel5, javax.swing.JLayeredPane.DEFAULT_LAYER);
        ola.setLayer(rpc_Btn, javax.swing.JLayeredPane.DEFAULT_LAYER);
        ola.setLayer(rpc_field, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout olaLayout = new javax.swing.GroupLayout(ola);
        ola.setLayout(olaLayout);
        olaLayout.setHorizontalGroup(
            olaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(olaLayout.createSequentialGroup()
                .addGroup(olaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(olaLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(olaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                    .addGroup(olaLayout.createSequentialGroup()
                        .addGap(44, 44, 44)
                        .addComponent(jLabel5))
                    .addGroup(olaLayout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addGroup(olaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(rpc_field)
                            .addComponent(rpc_Btn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(18, 18, 18)
                .addGroup(olaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(olaLayout.createSequentialGroup()
                        .addComponent(msg_field)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(send_Btn)
                        .addGap(80, 80, 80)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(olaLayout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 577, Short.MAX_VALUE)
                        .addGroup(olaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, olaLayout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addGroup(olaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(olaLayout.createSequentialGroup()
                                        .addGap(12, 12, 12)
                                        .addGroup(olaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(olaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jButton9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jButton8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jButton6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addGap(12, 12, 12))))
                            .addGroup(olaLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jSeparator2))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, olaLayout.createSequentialGroup()
                                .addGroup(olaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, olaLayout.createSequentialGroup()
                                        .addGap(69, 69, 69)
                                        .addComponent(jLabel1))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, olaLayout.createSequentialGroup()
                                        .addGap(50, 50, 50)
                                        .addComponent(jLabel3))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, olaLayout.createSequentialGroup()
                                        .addGap(18, 18, 18)
                                        .addComponent(name_field, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, olaLayout.createSequentialGroup()
                                        .addGap(54, 54, 54)
                                        .addComponent(jLabel2))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, olaLayout.createSequentialGroup()
                                        .addGap(6, 6, 6)
                                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, olaLayout.createSequentialGroup()
                                        .addGap(18, 18, 18)
                                        .addGroup(olaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(name_label, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(olaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(c_Btn, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(ip_field, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(d_Btn, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(olaLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(olaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(olaLayout.createSequentialGroup()
                                        .addComponent(p_c_label)
                                        .addGap(18, 18, 18))
                                    .addGroup(olaLayout.createSequentialGroup()
                                        .addGroup(olaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(p_c_a_Btn, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(p_c_label2))
                                        .addGap(33, 33, 33)))))))
                .addGap(17, 17, 17))
        );
        olaLayout.setVerticalGroup(
            olaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(olaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(olaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(olaLayout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addGroup(olaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1)
                            .addGroup(olaLayout.createSequentialGroup()
                                .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(rpc_Btn, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(rpc_field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jScrollPane2))))
                    .addGroup(olaLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(1, 1, 1)
                        .addComponent(name_field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(name_label, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2)
                        .addGap(14, 14, 14)
                        .addComponent(ip_field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(c_Btn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(d_Btn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3)
                        .addGap(26, 26, 26)
                        .addGroup(olaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton2)
                            .addComponent(jButton3))
                        .addGap(26, 26, 26)
                        .addGroup(olaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton4)
                            .addComponent(jButton8))
                        .addGap(28, 28, 28)
                        .addGroup(olaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton5)
                            .addComponent(jButton6))
                        .addGap(18, 18, 18)
                        .addGroup(olaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton7)
                            .addComponent(jButton9))
                        .addGap(18, 18, 18)
                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(p_c_label)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(p_c_label2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(p_c_a_Btn, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 25, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(olaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(olaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(msg_field, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(send_Btn)
                        .addComponent(jButton1))
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ola)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ola)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void name_fieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_name_fieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_name_fieldActionPerformed

    private void c_BtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_c_BtnActionPerformed

        try {
            if (name_field.getText().equals("")) {
                name_label.setText("  Name Required!");
            } else {
                c_connect client1 = new c_connect();
                client1.start();

                setTitle("~" + name_field.getText() + "~");
                name_label.setText(null);
                ip_field.setEditable(false);
                name_field.setEditable(false);
                c_Btn.setEnabled(false);
                c_Btn.setText("Connected");
                send_Btn.setEnabled(true);
                msg_field.setEnabled(true);
                d_Btn.setEnabled(true);
            }

        } catch (Exception e) {

        }

    }//GEN-LAST:event_c_BtnActionPerformed

    private void d_BtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_d_BtnActionPerformed

        try {
            dOut.writeUTF("DISCONNECTING!~!");
            dIn.close();
            dOut.close();
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        text_Area.setText("~You Are Disconnected~");
        c_Btn.setEnabled(true);
        name_field.setEditable(true);
        send_Btn.setEnabled(false);
        msg_field.setEnabled(false);
        c_Btn.setText("Connect");
        setTitle("<~Anonymous Client~>");


    }//GEN-LAST:event_d_BtnActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:

        msg_field.setText(msg_field.getText() + " :) ");
        msg_field.requestFocus();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        msg_field.setText(msg_field.getText() + " :( ");
        msg_field.requestFocus();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        msg_field.setText(msg_field.getText() + " :'( ");
        msg_field.requestFocus();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // TODO add your handling code here:
        msg_field.setText(msg_field.getText() + " ;) ");
        msg_field.requestFocus();
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        msg_field.setText(msg_field.getText() + " :D ");
        msg_field.requestFocus();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
        msg_field.setText(msg_field.getText() + " ^_^ ");
        msg_field.requestFocus();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
        msg_field.setText(msg_field.getText() + " :P ");
        msg_field.requestFocus();
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        // TODO add your handling code here:
        msg_field.setText(msg_field.getText() + " :O ");
        msg_field.requestFocus();
    }//GEN-LAST:event_jButton9ActionPerformed

    private void ip_fieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ip_fieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ip_fieldActionPerformed

    private void send_BtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_send_BtnActionPerformed
        // TODO add your handling code here:

        try {
            String msgOut = "";
            msgOut = msg_field.getText();
            dOut.writeUTF(msgOut);

            msg_field.setText(null);

        } catch (Exception e) {

        }
    }//GEN-LAST:event_send_BtnActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void p_c_a_BtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_p_c_a_BtnActionPerformed
        // TODO add your handling code here:
        new private_C().start();
        p_c_a_Btn.setVisible(false);
        p_c_label.setVisible(false);
        p_c_label2.setVisible(false);
    }//GEN-LAST:event_p_c_a_BtnActionPerformed

    private void rpc_BtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rpc_BtnActionPerformed
        try {
            dOut.writeUTF("üüü" + rpc_field.getText());
            new Clients_Private_l(rpc_field.getText()).start();
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_rpc_BtnActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {

                new Client().setVisible(true);
                p_c_a_Btn.setVisible(false);
                p_c_label.setVisible(false);
                p_c_label2.setVisible(false);
                o_u_Area.setForeground(blue);
                DefaultCaret caret = (DefaultCaret) text_Area.getCaret(); // Setting textArea to update scrolling.
                caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
                text_Area.setEditable(false);

                msg_field.addKeyListener( // Send msg when enter is pressed.
                        new KeyAdapter() {
                    @Override
                    public void keyReleased(KeyEvent e) {
                        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                            String msgOut = "";

                            msgOut = msg_field.getText();
                            try {
                                dOut.writeUTF(msgOut);
                            } catch (IOException ex) {
                                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                            }

                            msg_field.setText(null);

                        }
                    }
                }
                );

            }

        });

    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private static javax.swing.JButton c_Btn;
    private static javax.swing.JButton d_Btn;
    private static javax.swing.JTextField ip_field;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private static javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private static javax.swing.JTextField msg_field;
    private static javax.swing.JTextField name_field;
    private static javax.swing.JLabel name_label;
    private static javax.swing.JTextArea o_u_Area;
    private javax.swing.JDesktopPane ola;
    private static javax.swing.JButton p_c_a_Btn;
    private static javax.swing.JLabel p_c_label;
    private static javax.swing.JLabel p_c_label2;
    private static javax.swing.JButton rpc_Btn;
    private static javax.swing.JTextField rpc_field;
    private static javax.swing.JButton send_Btn;
    private static javax.swing.JTextArea text_Area;
    // End of variables declaration//GEN-END:variables
}
