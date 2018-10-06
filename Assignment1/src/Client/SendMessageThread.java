package Client;

import java.net.*;

import Protocol.XMLProtocol;

import java.io.*;

public class SendMessageThread extends Thread{
	
	public SendMessageThread(Socket s, String msg) {
		// TODO Auto-generated constructor stub
		this.socket = s;
		this.msg = msg;
		protocol = new XMLProtocol();
		
	}
	@Override
	public void run() {
		try {
			
			sender = new DataOutputStream(socket.getOutputStream());
			
				String send = protocol.messageToXML(msg);
				sender.writeUTF(send + "\n");
				sender.flush();
			
		}catch (Exception e){
			
		}
	}
	public void start(){
		if (t == null){
			t = new Thread(this);
			t.start();
		}
	}
	DataOutputStream sender = null;
	Thread t = null;
	Socket socket = null;
	String msg;
	XMLProtocol protocol = null;
	//ClientGUI frm = null;
}
