/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatappp;

import java.awt.BorderLayout;
import static java.awt.BorderLayout.SOUTH;
import static java.awt.Color.blue;
import static java.awt.Color.gray;
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
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.DefaultCaret;

/**
 *
 * @author Mustafa
 */
public class Server extends javax.swing.JFrame {  //GUI Class.

    /**
     * Creates new form Server
     */
    static ServerSocket s2_Socket;          // Private chat Server Socket.
    static Socket c2_socket_s2;              //Private chat client socket.
    static int counter = 0;                 // Online Users Counter.
    static int idx_counter = 0;              // Clients Thread Counter.
    static String[] clients_names = new String[100];  // Online Clients ArrayList.
    static int idxCount = 0;                    // Clients On Server Counter.
    public static Client_thread[] clientsArr = new Client_thread[100]; // ArrayList Of Clients Threads.
    public static Server_L s1;                   // Server Listener Object.
    static JFrame frame1;                       // Private chat Frame.
    static JTextArea text_Area1;                // Private chat textArea.
    static JTextField text_field1;               // Private Chat TextField.

    public static class private_C extends Thread {   // Private chat Class.

        DataInputStream dIn2;
        DataOutputStream dOut2;
        String name = "";
        int number;

        private_C(String naame, int numberr) {
            name = naame;
            number = numberr;
            try {

                c2_socket_s2 = s2_Socket.accept();

            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
            frame1 = new JFrame("Server ~~~ Private Conversation With : " + name);
            frame1.setSize(800, 600);
            frame1.setLayout(new BorderLayout());

            text_Area1 = new JTextArea();
            text_Area1.setSize(400, 200);
            text_field1 = new JTextField();
            text_field1.setSize(200, 100);
            JButton btn = new JButton("Exit");
            btn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        dOut2.writeUTF("EXIT_NOW");
                        frame1.setVisible(false);
                        p_c_Btn.setEnabled(true);
                        dIn2.close();
                        dOut2.close();
                    } catch (IOException ex) {
                        Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }

            });
            frame1.add(btn, BorderLayout.WEST);
            frame1.add(text_Area1);
            frame1.add(text_field1, SOUTH);
            frame1.setVisible(true);
            text_Area1.setEditable(false);
            DefaultCaret caret = (DefaultCaret) text_Area1.getCaret();
            caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        }

        @Override
        public void run() {
            super.run();
            p_c_Btn.setEnabled(false);
            p_c_field.setText(null);
            try {
                dOut2 = new DataOutputStream(c2_socket_s2.getOutputStream());
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
            text_field1.addKeyListener( // Send msg When Pressing Enter Button.
                    new KeyAdapter() {
                public void keyReleased(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        String msgout = text_field1.getText();
                        try {
                            dOut2.writeUTF(msgout);
                        } catch (IOException ex) {
                            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        text_Area1.setText(text_Area1.getText().trim() + "\nServer :: " + text_field1.getText());
                        text_field1.setText(null);

                    }
                }
            }
            );
            try {
                dIn2 = new DataInputStream(c2_socket_s2.getInputStream());
            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }

            while (true) {  // Reading loop.

                try {
                    String msgin = dIn2.readUTF();
                    if (!msgin.equals("EXIT_NOW")) {
                        text_Area1.setText(text_Area1.getText().trim() + "\n" + name + " :: " + msgin);
                    } else {
                        frame1.setVisible(false);
                        p_c_Btn.setEnabled(true);
                        dIn2.close();
                        dOut2.close();
                    }

                } catch (IOException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

        }

    }

    public static class Client_thread extends Thread { // Clients Thread.

        private Socket c_socket_s;  // Client Socket On Server.

        private int idx_counter2 = 0;

        private DataInputStream dIn;
        private DataOutputStream dOut;
        private String c_Name = ""; // Client Name.

        public Client_thread(Socket socket, int idxCounter) {

            idx_counter2 = idxCounter;
            c_socket_s = socket;

            start();

        }

        @Override
        public void run() {
            super.run();

            try {
                dIn = new DataInputStream(c_socket_s.getInputStream());
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                text_Area.setText("INPUT stream error: " + ex);
            }
            try {
                dOut = new DataOutputStream(c_socket_s.getOutputStream());
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                text_Area.setText("OUTPUT stream error: " + ex);
            }

            try {
                c_Name = dIn.readUTF();
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
            String name_V = "";
            for (int k = 0; k < idxCount - 1; k++) {                       // Check if this name exists in the list.
                if (clientsArr[k].c_Name.equals(this.c_Name)) {
                    name_V = "Name Exists!~!~!@@$!";
                }
            }
            if (name_V.equals("Name Exists!~!~!@@$!")) { // Force the user to change the name.
                try {
                    dOut.writeUTF(name_V);
                } catch (IOException ex) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                }

            } else {
                counter++;

                users_count.setText("Users Connected: " + counter); // online users counter increament.
                clients_names[idx_counter2] = c_Name + "\n"; // names list update.
                for (int i = idx_counter2; i < clients_names.length; i++) {
                    jTextArea1.append(clients_names[i]);   // Online users text Area update.
                }
                for (int j = 0; j < idxCount; j++) {  // Sending onlne users update to clients.
                    try {
                        clientsArr[j].dOut.writeUTF("-----------------------------\n~" + c_Name + " Connected~\n-----------------------------\n");
                        String oUmsgOut = jTextArea1.getText();
                        clientsArr[j].dOut.writeUTF("§§§" + oUmsgOut);
                        dOut.writeUTF("§§§" + oUmsgOut);
                    } catch (IOException ex) {
                        Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                String msgIn = "";

                while (!msgIn.equals("DISCONNECTING!~!")) {  // reading loop.

                    try {
                        msgIn = dIn.readUTF();

                    } catch (IOException ex) {
                        Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    if (!msgIn.equals("")) {
                        for (int i = 0; i < idxCount; i++) {
                            if (!msgIn.equals("DISCONNECTING!~!")) {
                                try {
                                    clientsArr[i].dOut.writeUTF(c_Name + " :: " + msgIn); // BroadCasting input to all clients.
                                } catch (IOException ex) {
                                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                                }

                                if (msgIn.charAt(0) == 'ü' && msgIn.charAt(1) == 'ü' && msgIn.charAt(2) == 'ü') {
                                    msgIn = msgIn.substring(1);
                                    msgIn = msgIn.substring(1);
                                    msgIn = msgIn.substring(1);
                                    for (int f = 0; f < 99; f++) {
                                        if (msgIn.equals(clientsArr[f].c_Name)) {
                                            try {
                                                clientsArr[f].dOut.writeUTF("üüü" + this.c_Name);
                                            } catch (IOException ex) {
                                                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                                            }
                                        }
                                    }

                                }
                            } else {
                                try {
                                    clientsArr[i].dOut.writeUTF("-----------------------------------------\n    " + c_Name + " Disconnected!\n-----------------------------------------\n");
                                } catch (IOException ex) {
                                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }

                        }
                        text_Area.transferFocus();
                        text_Area.setText(text_Area.getText().trim() + "\n" + c_Name + " : " + msgIn);

                    }

                }
                // when a client disconnects.
                text_Area.append("\n-----------------------------------------\n    " + c_Name + " Disconnected!\n-----------------------------------------\n");
                counter--;
                users_count.setText("Users Connected: " + counter);
                jTextArea1.setText(jTextArea1.getText().replaceAll(clients_names[idx_counter2], "")); // removing disconnected client from the Online list.
                c_Name = "";
                for (int ij = 0; ij < idxCount; ij++) { // sending online users list update  to clients.
                    String oUmsgOut = jTextArea1.getText();
                    try {
                        clientsArr[ij].dOut.writeUTF("§§§" + oUmsgOut);
                    } catch (IOException ex) {
                        Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                try {

                    dIn.close();
                    dOut.close();

                } catch (Exception e) {

                }

            }

        }

    }

    public Server() {
        initComponents();
        setTitle("~Server~");
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jDesktopPane1 = new javax.swing.JDesktopPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        text_Area = new javax.swing.JTextArea();
        msg_field = new javax.swing.JTextField();
        send_Btn = new javax.swing.JButton();
        users_count = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        exit_Btn = new javax.swing.JButton();
        k_field = new javax.swing.JTextField();
        k_Btn = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        sl_label = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        p_c_field = new javax.swing.JTextField();
        p_c_Btn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        text_Area.setColumns(20);
        text_Area.setRows(5);
        text_Area.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                text_AreaFocusGained(evt);
            }
        });
        jScrollPane1.setViewportView(text_Area);

        send_Btn.setFont(new java.awt.Font("Perpetua Titling MT", 0, 14)); // NOI18N
        send_Btn.setText("Send");
        send_Btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                send_BtnActionPerformed(evt);
            }
        });

        users_count.setFont(new java.awt.Font("Tele-Marines", 0, 14)); // NOI18N
        users_count.setText("Users Connected: 0");
        users_count.setPreferredSize(new java.awt.Dimension(229, 20));

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane3.setViewportView(jTextArea1);

        exit_Btn.setFont(new java.awt.Font("Tele-Marines", 0, 12)); // NOI18N
        exit_Btn.setText("Exit");
        exit_Btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exit_BtnActionPerformed(evt);
            }
        });

        k_Btn.setText("Kick");
        k_Btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                k_BtnActionPerformed(evt);
            }
        });

        jButton1.setText("Start Listener");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        sl_label.setText("Server Is Not Listening!");

        jLabel2.setText("~Online Users~");

        p_c_Btn.setText("Private Chat");
        p_c_Btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                p_c_BtnActionPerformed(evt);
            }
        });

        jDesktopPane1.setLayer(jScrollPane1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(msg_field, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(send_Btn, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(users_count, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(jScrollPane3, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(exit_Btn, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(k_field, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(k_Btn, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(jButton1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(sl_label, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(jLabel2, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(p_c_field, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(p_c_Btn, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jDesktopPane1Layout = new javax.swing.GroupLayout(jDesktopPane1);
        jDesktopPane1.setLayout(jDesktopPane1Layout);
        jDesktopPane1Layout.setHorizontalGroup(
            jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDesktopPane1Layout.createSequentialGroup()
                .addGroup(jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jDesktopPane1Layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addGroup(jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(exit_Btn, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDesktopPane1Layout.createSequentialGroup()
                                .addGroup(jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jDesktopPane1Layout.createSequentialGroup()
                                        .addComponent(msg_field)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(send_Btn, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jDesktopPane1Layout.createSequentialGroup()
                                        .addGap(8, 8, 8)
                                        .addComponent(users_count, javax.swing.GroupLayout.PREFERRED_SIZE, 303, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 64, Short.MAX_VALUE)
                                        .addComponent(jButton1)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(sl_label))
                                    .addComponent(jScrollPane1))
                                .addGroup(jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jDesktopPane1Layout.createSequentialGroup()
                                        .addGap(97, 97, 97)
                                        .addGroup(jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDesktopPane1Layout.createSequentialGroup()
                                                .addComponent(jLabel2)
                                                .addGap(23, 23, 23))
                                            .addComponent(k_field, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDesktopPane1Layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDesktopPane1Layout.createSequentialGroup()
                                                .addComponent(k_Btn, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(32, 32, 32))
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDesktopPane1Layout.createSequentialGroup()
                                                .addComponent(p_c_Btn)
                                                .addGap(20, 20, 20))))))))
                    .addGroup(jDesktopPane1Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(p_c_field, javax.swing.GroupLayout.DEFAULT_SIZE, 134, Short.MAX_VALUE))))
                .addGap(33, 33, 33))
        );
        jDesktopPane1Layout.setVerticalGroup(
            jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDesktopPane1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(exit_Btn)
                .addGap(35, 35, 35)
                .addGroup(jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton1)
                        .addComponent(users_count, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(sl_label)
                        .addComponent(jLabel2)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jDesktopPane1Layout.createSequentialGroup()
                        .addComponent(jScrollPane1)
                        .addGap(18, 18, 18)
                        .addGroup(jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(msg_field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(send_Btn, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(14, 14, 14))
                    .addGroup(jDesktopPane1Layout.createSequentialGroup()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 346, Short.MAX_VALUE)
                        .addGap(26, 26, 26)
                        .addComponent(p_c_field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(p_c_Btn, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addComponent(k_field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(k_Btn, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jDesktopPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jDesktopPane1)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void send_BtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_send_BtnActionPerformed
        // TODO add your handling code here:
        String msgOut = "";
        try {

            msgOut = msg_field.getText();

            for (int i = 0; i < idxCount; i++) {
                clientsArr[i].dOut.writeUTF("Server :: " + msgOut);
            }

            text_Area.append("\nServer : " + msgOut);
            msg_field.setText(null);
        } catch (Exception e) {
        }
    }//GEN-LAST:event_send_BtnActionPerformed

    private void exit_BtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exit_BtnActionPerformed
        try {
            // TODO add your handling code here:

            for (int i = 0; i < idxCount; i++) {
                clientsArr[i].dOut.writeUTF("~*~*~*~*~*~*~*~*~*~*~*~*~*~\n     !~Server Is Closing~!\n~*~*~*~*~*~*~*~*~*~*~*~*~*~\n");
            }

        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.exit(0);
        try {
            s1.s_Socket.close();
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }


    }//GEN-LAST:event_exit_BtnActionPerformed

    private void text_AreaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_text_AreaFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_text_AreaFocusGained

    private void k_BtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_k_BtnActionPerformed
        // TODO add your handling code here:
        for (int k = 0; k <= 99; k++) {
            if (clientsArr[k].c_Name.equals(k_field.getText())) {
                try {
                    text_Area.append("\n-----------------------------------------\n    " + clientsArr[k].c_Name + " Kicked!\n-----------------------------------------\n");
                    counter--;
                    users_count.setText("Users Connected: " + counter);
                    jTextArea1.setText(jTextArea1.getText().replaceAll(clients_names[k], ""));
                    k_field.setText(null);
                    /* try {
                        for (int l = 0; l <= idxCount; l++) {
                            clientsArr[l].dOut.writeUTF("~" + clientsArr[k].c_Name + " Kicked~");
                            
                        }
                    } catch (Exception e) {

                    }*/
                    for (int q = 0; q < idxCount; q++) {
                        String oUmsgOut = jTextArea1.getText();
                        try {
                            clientsArr[q].dOut.writeUTF("§§§" + oUmsgOut);
                            clientsArr[q].dOut.writeUTF("!::SERVER KICKED <" + clientsArr[k].c_Name + ">::!");
                        } catch (IOException ex) {
                            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    clientsArr[k].c_Name = "";

                    clientsArr[k].dOut.writeUTF("!~!~!~KICKED!#$#@~!@@");
                    clientsArr[k].dIn = null;

                } catch (IOException ex) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }

    }//GEN-LAST:event_k_BtnActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        // Listener Button Action.
        s1.start();
        jButton1.setVisible(false);
        sl_label.setText("Server Is Listening.");
        sl_label.setForeground(gray);
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void p_c_BtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_p_c_BtnActionPerformed
        // Private Chat Button Action.
        for (int k = 0; k <= 99; k++) {
            if (clientsArr[k].c_Name.equals(p_c_field.getText())) { // Matching the selected name for private chat.
                try {

                    clientsArr[k].dOut.writeUTF("PRIVATE_CHAT_REQ"); // sending after confirmation request.
                    new private_C(clientsArr[k].c_Name, k).start();
                } catch (IOException ex) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

        }

    }//GEN-LAST:event_p_c_BtnActionPerformed

    /**
     * @param args the command line arguments
     */
    public static class Server_L extends Thread { // Listener Thread.

        public ServerSocket s_Socket;

        @Override
        public void run() {
            super.run(); //To change body of generated methods, choose Tools | Templates.
            try {
                s_Socket = new ServerSocket(9095);
                while (true) {
                    clientsArr[idxCount] = new Client_thread(s_Socket.accept(), idxCount);
                    idxCount++;
                }
            } catch (Exception e) {
                text_Area.setText("Accepting error: " + e);
            }

        }

    }

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
            java.util.logging.Logger.getLogger(Server.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Server.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Server.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Server.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Server().setVisible(true);
                DefaultCaret caret = (DefaultCaret) text_Area.getCaret();
                caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

                text_Area.setEditable(false);
                jTextArea1.setEditable(false);
                sl_label.setForeground(red);
                jTextArea1.setForeground(blue);

                msg_field.addKeyListener( // Send msg When Enter Is Pressed.
                        new KeyAdapter() {
                    public void keyReleased(KeyEvent eb) {
                        if (eb.getKeyCode() == KeyEvent.VK_ENTER) {
                            String msgOut = "";
                            try {

                                msgOut = msg_field.getText();

                                for (int i = 0; i < idxCount; i++) {
                                    clientsArr[i].dOut.writeUTF("Server :: " + msgOut);
                                }

                            } catch (Exception e) {
                            }
                            text_Area.append("\nServer : " + msgOut);
                            msg_field.setText(null);

                        }
                    }
                }
                );

            }
        });
        try {
            s2_Socket = new ServerSocket(9099);
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        s1 = new Server_L();

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private static javax.swing.JButton exit_Btn;
    private javax.swing.JButton jButton1;
    private javax.swing.JDesktopPane jDesktopPane1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private static javax.swing.JTextArea jTextArea1;
    private javax.swing.JButton k_Btn;
    private javax.swing.JTextField k_field;
    private static javax.swing.JTextField msg_field;
    private static javax.swing.JButton p_c_Btn;
    private static javax.swing.JTextField p_c_field;
    public static javax.swing.JButton send_Btn;
    private static javax.swing.JLabel sl_label;
    private static javax.swing.JTextArea text_Area;
    private static javax.swing.JLabel users_count;
    // End of variables declaration//GEN-END:variables
}
