package Client;

import javax.swing.*;

import Protocol.XMLProtocol;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.net.*;
import java.io.*;
import javax.swing.table.*;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class UserStatusGUI extends JFrame{
	private UserStatusGUI fff;
	private JTextField txtHostname;
	public JTextField txthostport;
	private JTextField txtusername;
	private JPasswordField pwdTxtpass;
	public UserStatusGUI() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				if (btnLogout.isEnabled())
					Logout();
			}
		});
		setSize(new Dimension(442, 362));
		setTitle("Start form");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//socket = s;
		Initially();
		fff  =this;
	}
	
	private void Initially(){
		
		table = new DefaultTableModel();
		table.addColumn("username");
		table.addColumn("password");
		table.addColumn("ip");
		table.addColumn("port");
		
		
		SpringLayout springLayout = new SpringLayout();
		getContentPane().setLayout(springLayout);
		
		JLabel lblHostName = new JLabel("Host name");
		springLayout.putConstraint(SpringLayout.NORTH, lblHostName, 30, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, lblHostName, 10, SpringLayout.WEST, getContentPane());
		getContentPane().add(lblHostName);
		
		txtHostname = new JTextField();
		txtHostname.setText("localhost");
		springLayout.putConstraint(SpringLayout.NORTH, txtHostname, -3, SpringLayout.NORTH, lblHostName);
		springLayout.putConstraint(SpringLayout.WEST, txtHostname, 6, SpringLayout.EAST, lblHostName);
		getContentPane().add(txtHostname);
		txtHostname.setColumns(10);
		
		JLabel lblHostPort = new JLabel("Host port");
		springLayout.putConstraint(SpringLayout.NORTH, lblHostPort, 0, SpringLayout.NORTH, lblHostName);
		springLayout.putConstraint(SpringLayout.WEST, lblHostPort, 32, SpringLayout.EAST, txtHostname);
		getContentPane().add(lblHostPort);
		
		txthostport = new JTextField();
		txthostport.setText("6696");
		springLayout.putConstraint(SpringLayout.WEST, txthostport, 6, SpringLayout.EAST, lblHostPort);
		springLayout.putConstraint(SpringLayout.SOUTH, txthostport, 0, SpringLayout.SOUTH, lblHostName);
		getContentPane().add(txthostport);
		txthostport.setColumns(10);
		
		JLabel lblUserName = new JLabel("User name");
		springLayout.putConstraint(SpringLayout.NORTH, lblUserName, 23, SpringLayout.SOUTH, lblHostName);
		springLayout.putConstraint(SpringLayout.WEST, lblUserName, 0, SpringLayout.WEST, lblHostName);
		getContentPane().add(lblUserName);
		
		JLabel lblPassword = new JLabel("Password");
		springLayout.putConstraint(SpringLayout.NORTH, lblPassword, 0, SpringLayout.NORTH, lblUserName);
		springLayout.putConstraint(SpringLayout.EAST, lblPassword, 0, SpringLayout.EAST, lblHostPort);
		getContentPane().add(lblPassword);
		
		txtusername = new JTextField();
		txtusername.setText("gg");
		txtusername.setEnabled(false);
		springLayout.putConstraint(SpringLayout.WEST, txtusername, 0, SpringLayout.WEST, txtHostname);
		springLayout.putConstraint(SpringLayout.SOUTH, txtusername, 0, SpringLayout.SOUTH, lblUserName);
		getContentPane().add(txtusername);
		txtusername.setColumns(10);
		
		pwdTxtpass = new JPasswordField();
		pwdTxtpass.setEnabled(false);
		springLayout.putConstraint(SpringLayout.NORTH, pwdTxtpass, 17, SpringLayout.SOUTH, txthostport);
		springLayout.putConstraint(SpringLayout.WEST, pwdTxtpass, 0, SpringLayout.WEST, txthostport);
		springLayout.putConstraint(SpringLayout.EAST, pwdTxtpass, 0, SpringLayout.EAST, txthostport);
		getContentPane().add(pwdTxtpass);
		pwdTxtpass.setText("c");
		btnConnect = new JButton("Connect");
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				StartConnect();
			}
		});
		springLayout.putConstraint(SpringLayout.NORTH, btnConnect, 26, SpringLayout.SOUTH, lblUserName);
		springLayout.putConstraint(SpringLayout.WEST, btnConnect, 10, SpringLayout.WEST, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, btnConnect, 177, SpringLayout.WEST, getContentPane());
		getContentPane().add(btnConnect);
		
		btnRegister = new JButton("Register");
		btnRegister.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LoginOrResgister(false);
			}
		});
		btnRegister.setEnabled(false);
		springLayout.putConstraint(SpringLayout.NORTH, btnRegister, 18, SpringLayout.SOUTH, btnConnect);
		springLayout.putConstraint(SpringLayout.WEST, btnRegister, 0, SpringLayout.WEST, lblHostName);
		springLayout.putConstraint(SpringLayout.EAST, btnRegister, 0, SpringLayout.EAST, btnConnect);
		getContentPane().add(btnRegister);
		
		btnLogin = new JButton("Login");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				LoginOrResgister(true);
			}
		});
		btnLogin.setEnabled(false);
		springLayout.putConstraint(SpringLayout.NORTH, btnLogin, 20, SpringLayout.SOUTH, btnRegister);
		springLayout.putConstraint(SpringLayout.WEST, btnLogin, 0, SpringLayout.WEST, lblHostName);
		springLayout.putConstraint(SpringLayout.EAST, btnLogin, 0, SpringLayout.EAST, btnConnect);
		getContentPane().add(btnLogin);
		
		scrollPane = new JScrollPane();
		springLayout.putConstraint(SpringLayout.NORTH, scrollPane, -213, SpringLayout.SOUTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, scrollPane, -106, SpringLayout.EAST, txthostport);
		springLayout.putConstraint(SpringLayout.EAST, scrollPane, 0, SpringLayout.EAST, txthostport);
		scrollPane.setSize(new Dimension(106, 142));
		springLayout.putConstraint(SpringLayout.SOUTH, scrollPane, -45, SpringLayout.SOUTH, getContentPane());
		getContentPane().add(scrollPane);
		
		list = new JList<String>();
		list.setSize(new Dimension(106, 142));
		list.setEnabled(false);
		scrollPane.setViewportView(list);
		
		
		btnClose = new JButton("Close");
		springLayout.putConstraint(SpringLayout.WEST, btnClose, 0, SpringLayout.WEST, lblHostName);
		springLayout.putConstraint(SpringLayout.EAST, btnClose, 0, SpringLayout.EAST, btnConnect);
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (btnLogout.isEnabled())
					Logout();
				System.exit(0);
			}
		});
		getContentPane().add(btnClose);
		
		btnStartChat = new JButton("Start Chat");
		springLayout.putConstraint(SpringLayout.SOUTH, btnClose, 0, SpringLayout.SOUTH, btnStartChat);
		btnStartChat.setEnabled(false);
		springLayout.putConstraint(SpringLayout.NORTH, btnClose, 0, SpringLayout.NORTH, btnStartChat);
		springLayout.putConstraint(SpringLayout.NORTH, btnStartChat, 6, SpringLayout.SOUTH, scrollPane);
		springLayout.putConstraint(SpringLayout.WEST, btnStartChat, 229, SpringLayout.WEST, getContentPane());
		btnStartChat.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				StartChat();
			}
		});
		getContentPane().add(btnStartChat);
		
		btnLogout = new JButton("Logout");
		springLayout.putConstraint(SpringLayout.SOUTH, btnLogout, -15, SpringLayout.NORTH, btnClose);
		btnLogout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Logout();
			}
		});
		btnLogout.setEnabled(false);
		springLayout.putConstraint(SpringLayout.WEST, btnLogout, 0, SpringLayout.WEST, lblHostName);
		springLayout.putConstraint(SpringLayout.EAST, btnLogout, 0, SpringLayout.EAST, btnConnect);
		getContentPane().add(btnLogout);

	}

	private void LoginOrResgister(boolean isLogin){
		
		if (!txtusername.getText().isEmpty() && !pwdTxtpass.getText().isEmpty()){
			try{
				XMLProtocol protocol = new XMLProtocol();
				String ip = socket.getLocalAddress().toString();
				int port = socket.getLocalPort();
				
				//Gửi thông tin client đến server thông qua protocol
				String sendInfo;

				if (isLogin){
					sendInfo = protocol.logIn(txtusername.getText(), pwdTxtpass.getText(), ip,Integer.toString(port));
				}else sendInfo = protocol.register(txtusername.getText(), pwdTxtpass.getText());
				
				DataOutputStream dout = new DataOutputStream(socket.getOutputStream());
				dout.writeUTF(sendInfo);
				dout.flush();
				
				//Recieve list user online from server
				DataInputStream recieve = new DataInputStream(socket.getInputStream());
				String lstUser = recieve.readUTF();
				
				if (!lstUser.equals(protocol.registerDeny()) && !lstUser.equals(protocol.loginDeny())){
					
					UpdateJList(lstUser);
					//Create listenner to accept other chat
					
					roleServer = new SocketPeer(socket.getLocalPort() + 1, fff);
					roleServer.start();
					//Send status to server
					SendStatusClient stt = new SendStatusClient(socket, txtusername.getText(), fff);
					stt.start();
					
					//btnLogin.setEnabled(false);
		    		btnRegister.setEnabled(false);
		    		btnLogin.setEnabled(false);
		    		btnLogout.setEnabled(true);
		    		btnConnect.setEnabled(false);
		    		btnStartChat.setEnabled(true);
		    		list.setEnabled(true);
		    		
		    		txtHostname.setEnabled(false);
		    		txthostport.setEnabled(false);
		    		txtusername.setEnabled(false);
		    		pwdTxtpass.setEnabled(false);
				}
				else 
					JOptionPane.showMessageDialog(this,"Register/Login fail. Check your username or password");
			
			}
			catch(Exception e){
				System.out.println("kkkkk " +e.getMessage() + " vv " + e.getCause());
			}
		}
		else {
			JOptionPane.showMessageDialog(null, "Username or Password is empty");
		}
	}
	void Logout(){
		try{
			
			XMLProtocol protocol = new XMLProtocol();
			String stt = protocol.alive(txtusername.getText(), "OFFLINE");
			DataOutputStream dout = new DataOutputStream(socket.getOutputStream());
			dout.writeUTF(stt);
			dout.flush();
			
		}catch (IOException e){
			System.out.println(e.getMessage());
		}
		try {
		roleServer.stop();
		//Khong can do'ng socket vi van co the login vo cung 1 server
		socket.close();
		}
		catch(Exception e){}
		btnConnect.setEnabled(true);
		btnLogin.setEnabled(false);
		btnLogout.setEnabled(false);
		btnRegister.setEnabled(false);
		btnStartChat.setEnabled(false);
		txtusername.setEnabled(false);
		pwdTxtpass.setEnabled(false);
		
		txtHostname.setEnabled(true);
		txthostport.setEnabled(true);
		//Delete old list user online
		DefaultListModel<String> tb = (DefaultListModel<String>) list.getModel();
		tb.removeAllElements();
		
	}

	private void StartConnect(){
		if (!txthostport.getText().isEmpty() && !txthostport.getText().isEmpty()){
			try{
				socket = new Socket(txtHostname.getText(), Integer.parseInt(txthostport.getText()));
				
				btnLogin.setEnabled(true);
				btnRegister.setEnabled(true);
				btnLogout.setEnabled(false);
				btnStartChat.setEnabled(false);
				list.setEnabled(true);
				txtHostname.setEnabled(false);
				txthostport.setEnabled(false);
				btnConnect.setEnabled(false);
				txtusername.setEnabled(true);
				pwdTxtpass.setEnabled(true);
									
			}catch (Exception e){
				JOptionPane.showMessageDialog(null, "Not find server");
			}
		}
		else {
			JOptionPane.showMessageDialog(null, "Not find server");
		}
	}

	private void StartChat(){
		if (!list.isSelectionEmpty()){
			try{
				//chọn client trong danh sách các client đang online
				int index = list.getSelectedIndex();
				String ip = table.getValueAt(index, 2).toString();
				String port = table.getValueAt(index, 3).toString();
				String userchat = table.getValueAt(index, 0).toString();
				//Chat to client, that client is server
				
				Socket s = new Socket(ip.substring(1),  Integer.parseInt(port) + 1);
				Socket sFile = new Socket(ip.substring(1),  Integer.parseInt(port) + 4);
				DataOutputStream ddd = new DataOutputStream(s.getOutputStream());
				ddd.writeUTF(txtusername.getText());
				ddd.flush();
				//ddd.close();
				ClientChatThread frm = new ClientChatThread(s, sFile, userchat, fff);
				frm.start();
				
			}catch(Exception e){
				System.out.println("Loi form moi + " + e.getMessage() + " ||| Cause: " + e.getCause());
			}
		}
	}
	
	public void UpdateJList(String lstUser){
		try{
			try{
				DefaultListModel<String> tb = (DefaultListModel<String>) list.getModel();
				tb.removeAllElements();
			}catch(Exception e){}
			//JOptionPane.showMessageDialog(null, lstUser);
		XMLProtocol protocol = new XMLProtocol();
		table = protocol.parseString(lstUser);
		DefaultListModel<String> tmp = new DefaultListModel<String>();
		
		list.setModel(tmp);
		
		for (int i = 0; i < table.getRowCount(); i++){
			tmp.addElement(table.getValueAt(i, 0).toString());
		}
		}
		catch(Exception e){
			System.out.println("Cann't take list user");
		}
		
	}
	JButton btnClose;
	JList<String> list;
	JScrollPane scrollPane;
	JButton btnLogin;
	JButton btnRegister;
	JButton btnConnect;
	JButton btnLogout;
	JButton btnStartChat;
	
	Socket socket = null;
	//public String username = "";
	
	private DefaultTableModel table = null;
	private SocketPeer roleServer;
}
