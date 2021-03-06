package Server;

import java.net.*;
import java.io.*;

import org.w3c.dom.*;
import org.xml.sax.InputSource;

import Protocol.XMLProtocol;

import javax.swing.table.DefaultTableModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class ServerThread extends Thread{
	private DefaultTableModel table; //chứa danh sách các user
	private DefaultTableModel tableuseronl;
	private DataInputStream input;
	private DataOutputStream output;
	private ServerForm form;
	private Socket socket = null;
	private int ID;
	
	public ServerThread(Socket _socket, Server _server){
		this.socket = _socket;
		this.ID = socket.getPort();
		this.table = _server.table;
		this.form = _server.form;
		this.tableuseronl = _server.tableuseronl;
		
	}
	private boolean checkUserName(String userName){

		for(int i = table.getRowCount()-1; i>=0; i--)
		{
			if(table.getValueAt(i, 0).equals(userName))
				
				return false;
		}
		return true;
	}
	private int checkLogin(String userName, String pass){
		for(int i = table.getRowCount()-1;i>=0;i--)
			if(table.getValueAt(i, 0).equals(userName) && table.getValueAt(i, 1).equals(pass)){
				return i;
				
			}
		return -1;
	}
	private void sendMessage(String message) throws IOException{
		output.writeUTF(message);
		output.flush();
	}
	private void close() throws IOException{
		form.txtArea.append("\nServer Thread "+ ID +" close.");
		if(socket!=null) socket.close();
		if(input!= null) input.close();
		if(output != null) output.close();
	}
	private void remote(String userName) throws IOException{
		for(int i = table.getRowCount()-1; i>=0; i--)
		{
			if(table.getValueAt(i, 0).equals(userName))
			{	
				//System.out.println("bn "+table.getValueAt(i, 2)+"gb" +table.getValueAt(i, 3));
				table.setValueAt("", i, 2);
				table.setValueAt("", i, 3);
				//System.out.println("bn "+table.getValueAt(i, 2)+"gb" +table.getValueAt(i, 3));
				break;
			}
		}
		this.close();
	}
	@SuppressWarnings("deprecation")
	@Override
	public void run(){
		form.txtArea.append("\nServer Thread "+ ID +" running.");
		try{
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			output = new DataOutputStream(socket.getOutputStream());
			output.flush();
			input = new DataInputStream(socket.getInputStream());
			long st = System.currentTimeMillis();
			while(true){
				//lấy thông điệp từ client
				String message = input.readUTF();
				long end = System.currentTimeMillis();
				if(message!=null){
					Document doc = docBuilder.parse(new InputSource(new StringReader(message)));
					doc.getDocumentElement().normalize();
					if(doc.getDocumentElement().getNodeName().equals("REGISTER")){
						String userName = doc.getElementsByTagName("USER_NAME").item(0).getTextContent();
						String pass = doc.getElementsByTagName("PASSWORD").item(0).getTextContent();

						if(checkUserName(userName)){
							String[] dataRow ={userName,pass,socket.getInetAddress().toString(),""+ID+""};
							table.addRow(dataRow);
							tableuseronl.addRow(dataRow);
							form.UpdateJList(tableuseronl);
							//Gửi thông điệp về Client bằng protocol
							sendMessage(new XMLProtocol().registerAccept(table));
						}
						else{
						
							sendMessage(new XMLProtocol().registerDeny());

						}
					}
					else if(doc.getDocumentElement().getNodeName().equals("PEER_KEEP_ALIVE")){
						if(!doc.getElementsByTagName("STATUS").item(0).getTextContent().equals("ALIVE")){
							String userName = doc.getElementsByTagName("USER_NAME").item(0).getTextContent();
							int k = 0;
							for (int i = 0; i < tableuseronl.getRowCount(); i++){
								if (tableuseronl.getValueAt(i,0).toString().equals(userName)){
									tableuseronl.removeRow(i);
									form.UpdateJList(tableuseronl);
									break;
								}
							}


							this.remote(doc.getElementsByTagName("USER_NAME").item(0).getTextContent());
							this.close();
							this.stop();
						}
						else sendMessage(new XMLProtocol().registerAccept(table));
					}
					//check tài khoản đăng nhập
					else if(doc.getDocumentElement().getNodeName().equals("LOGIN")){
						String userName = doc.getElementsByTagName("USER_NAME").item(0).getTextContent();
						String pass = doc.getElementsByTagName("PASSWORD").item(0).getTextContent();
						String ip = doc.getElementsByTagName("IP").item(0).getTextContent();
						String port = doc.getElementsByTagName("PORT").item(0).getTextContent();
						//Lấy index của userName trên table
						int row = checkLogin(userName,pass);
						if(row >= 0){
							String[] dataRow ={userName,pass,socket.getInetAddress().toString(),""+ID+""};
							table.setValueAt(ip, row, 2);
							table.setValueAt(port, row, 3);
							tableuseronl.addRow(dataRow);
							form.UpdateJList(tableuseronl);
							sendMessage(new XMLProtocol().registerAccept(table));
						}
						else sendMessage(new XMLProtocol().loginDeny());
					}
				}
			}
			
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
		
	}
}
