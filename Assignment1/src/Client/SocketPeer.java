package Client;

import java.awt.Dialog;
import java.io.*;
import java.net.*;
import javax.swing.*;

import Protocol.XMLProtocol;

public class SocketPeer implements Runnable{

	public SocketPeer(int port, UserStatusGUI frmStt){
		// TODO Auto-generated constructor stub
		try{
		serverChat = new ServerSocket(port);
		serverFile = new ServerSocket(port + 3);
		this.port = port;
		}catch(Exception e){
			System.out.print("socket perr ham tao\n"+e.getMessage());
		}
		this.frmstt = frmStt;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try{
			while (true)
			{
				socket = serverChat.accept();
				socketFile = serverFile.accept();
				
				DataInputStream ddd = new DataInputStream(socket.getInputStream());
				String userChat = ddd.readUTF();
				DataOutputStream buff = new DataOutputStream(socket.getOutputStream());
				
				String msgbox = "Client " + userChat /* + socket.getInetAddress() */ + " want to chat. Are you agree?";
				
				int result = JOptionPane.showConfirmDialog(null, msgbox, "Information", JOptionPane.YES_NO_OPTION);
				
				XMLProtocol pro = new XMLProtocol();
				String _result;
				
				if (result == 0){
					_result = pro.chatAccept();
					buff.writeUTF(_result);
					ClientGUI frm = new ClientGUI(frmstt);
					frm.connect(socket, socketFile, userChat);
					frm.setTitle("Chat with: " + userChat);
					frm.setVisible(true);
					
				} else{
					
					_result = pro.chatDeny();
					buff.writeUTF(_result);
					
				}
			}//end while true
		}
		catch (Exception e){
			System.out.println("Loi o PeerThread: " + e.getMessage());
		}
	}
	
	public void start(){
		if (t== null){
			t = new Thread(this);
			t.start();
		}
	}
	public void stop(){
		if (t != null){
			try{
				socket.close();
				serverChat.close();
				serverFile.close();
			}catch(Exception e){
				System.out.println("Loi o stop PeerThread: " + e.getMessage());
			}
		}
	}
	private Socket socket = null;
	private Socket socketFile = null;
	private ServerSocket serverChat = null;
	private ServerSocket serverFile = null;
	
	//private BufferedWriter buff = null;
	public int port;
	private Thread t = null;
	UserStatusGUI frmstt;
}
