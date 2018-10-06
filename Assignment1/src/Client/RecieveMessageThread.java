package Client;

import java.io.*;
import java.net.*;

import Protocol.XMLProtocol;

public class RecieveMessageThread implements Runnable{
	
	public RecieveMessageThread(ClientGUI frm, Socket s){
		this.frm = frm;
		socket = s;
		protocol = new XMLProtocol();
	}
	
	public void run(){
		try{
			while (true){
				//recieve = new BufferedReader(new InputStreamReader(socket.getInputStream()));//get inputstream
				recieve = new DataInputStream(socket.getInputStream());
				String msgRecieved = null;
				while((msgRecieved = recieve.readUTF())!= null)
				{
					System.out.println("Tn nhan: " + msgRecieved);
					String msg = protocol.XMLToMessage(msgRecieved);
					frm.addMessage(msg, userChat);
					//System.out.println("Tn nhan: " + msg);
				}
			}
		}
		catch(Exception e){
			System.out.println(e.getMessage());
			try {
				socket.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	public void start(){
		if (t == null){
			t = new Thread(this);
			t.start();
		}
	}
	DataInputStream recieve = null;
	XMLProtocol protocol = null;
	ClientGUI frm = null;
	Socket socket = null;
	Thread t = null;
	public String userChat = "";
}
