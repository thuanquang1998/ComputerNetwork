package Client;

import java.io.DataInputStream;
import java.net.Socket;

import Protocol.XMLProtocol;

public class ClientChatThread implements Runnable{

	public ClientChatThread(Socket s, Socket sFile, String userchat, UserStatusGUI frmstt) {
		// TODO Auto-generated constructor stub
		socket = s;
		this.sFile = sFile;
		userChat = userchat;
		frmStt = frmstt;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try{
			DataInputStream dip = new DataInputStream(socket.getInputStream());
			String aod = dip.readUTF();
			XMLProtocol proto = new XMLProtocol();
			
			if (!aod.equals(proto.chatDeny())){
				ClientGUI client = new ClientGUI(frmStt);
				client.connect(socket,sFile, userChat);
				client.setVisible(true);
				client.setTitle("Chat with: " + userChat);
			}
			else socket.close();
			
		}catch (Exception e){
			System.out.println(e.getMessage());
			
		}
		
	}
	public void start(){
		if (t == null){
			t = new Thread(this);
			t.start();
		}
	}
	
	Thread t = null;
	Socket socket = null;
	Socket sFile;
	String userChat;
	UserStatusGUI frmStt;
}
