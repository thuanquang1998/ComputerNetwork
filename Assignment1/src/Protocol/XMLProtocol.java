package Protocol;

import java.io.*;

import javax.swing.table.DefaultTableModel;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.*;
import org.xml.sax.InputSource;

import java.util.logging.*;


public class XMLProtocol {

	public String documentToString(Document doc) throws Exception{
		Transformer tran = TransformerFactory.newInstance().newTransformer();
		tran.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		StringWriter writer = new StringWriter();
		tran.transform(new DOMSource((Node)doc), new StreamResult(writer));
		return writer.getBuffer().toString();
	}
	
	public String register(String userName, String passWord){
		try{
			 DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
	         DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
	         Document doc = docBuilder.newDocument();
	         
	         Element reg = doc.createElement("REGISTER");
	         doc.appendChild(reg);
	         Element user = doc.createElement("USER_NAME");
	         user.setTextContent(userName);
	         reg.appendChild(user);
	         Element pass = doc.createElement("PASSWORD");
	         pass.setTextContent(passWord);
	         reg.appendChild(pass);
	         
	         return documentToString(doc);
		}
		catch(ParserConfigurationException | DOMException ex) {
	            ex.printStackTrace();
	            return null;
	     } 
		catch (Exception ex) {
	            Logger.getLogger(XMLProtocol.class.getName()).log(Level.SEVERE, null, ex);
	            return null;
	    }
	}
	
	public String logIn(String userName, String passWord, String ip, String port){
		try{
			 DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
	         DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
	         Document doc = docBuilder.newDocument();
	         
	         Element login = doc.createElement("LOGIN");
	         doc.appendChild(login);
	         Element user = doc.createElement("USER_NAME");
	         user.setTextContent(userName);
	         login.appendChild(user);
	         Element pass = doc.createElement("PASSWORD");
	         pass.setTextContent(passWord);
	         login.appendChild(pass);
	         Element _ip = doc.createElement("IP");
	         _ip.setTextContent(ip);
	         login.appendChild(_ip);
	         Element _port = doc.createElement("PORT");
	         _port.setTextContent(port);
	         login.appendChild(_port);
	         
	         return documentToString(doc);
		}
		catch(ParserConfigurationException | DOMException ex) {
	            ex.printStackTrace();
	            return null;
	     } 
		catch (Exception ex) {
	            Logger.getLogger(XMLProtocol.class.getName()).log(Level.SEVERE, null, ex);
	            return null;
	    }
	}
	public String logOut(String userName, String status){
		try{
			 DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
	         DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
	         Document doc = docBuilder.newDocument();
	         
	         Element logout = doc.createElement("PEER_KEEP_ALIVE");
	         doc.appendChild(logout);
	         Element user = doc.createElement("USER_NAME");
	         user.setTextContent(userName);
	         logout.appendChild(user);
	         Element _status = doc.createElement("STATUS");
	         _status.setTextContent(status);
	         logout.appendChild(_status);
	         
	         return documentToString(doc);
		}
		catch(ParserConfigurationException | DOMException ex) {
	            ex.printStackTrace();
	            return null;
	     } 
		catch (Exception ex) {
	            Logger.getLogger(XMLProtocol.class.getName()).log(Level.SEVERE, null, ex);
	            return null;
	    }
	}
	
	/* Status alive */
	public String alive(String userName, String status){
		try{
			 DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
	         DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
	         Document doc = docBuilder.newDocument();
	         
	         Element alive = doc.createElement("PEER_KEEP_ALIVE");
	         doc.appendChild(alive);
	         Element user = doc.createElement("USER_NAME");
	         user.setTextContent(userName);
	         alive.appendChild(user);
	         Element _status = doc.createElement("STATUS");
	         _status.setTextContent(status);
	         alive.appendChild(_status);
	         
	         return documentToString(doc);
		}
		catch(ParserConfigurationException | DOMException ex) {
	            ex.printStackTrace();
	            return null;
	     } 
		catch (Exception ex) {
	            Logger.getLogger(XMLProtocol.class.getName()).log(Level.SEVERE, null, ex);
	            return null;
	    }
	}
	
	/* List XML user <SERVER_ACCEPT> */
	public String listUser(DefaultTableModel table){
		try{
			 DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
	         DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
	         Document doc = docBuilder.newDocument();
	         
	         Element list = doc.createElement("SERVER_ACCEPT");
	         doc.appendChild(list);
	         
	         for(int i = table.getRowCount() - 1; i>=0; i--)
	         {
	        	 if(table.getValueAt(i, 2)!=""){
		        	 Element peer = doc.createElement("PEER");
		        	 list.appendChild(peer);
		        	 Element user = doc.createElement("USER_NAME");
		        	 user.setTextContent(table.getValueAt(i, 0).toString());
		        	 peer.appendChild(user);
		        	 Element ip = doc.createElement("IP");
		        	 ip.setTextContent(table.getValueAt(i, 2).toString());
		        	 peer.appendChild(ip);
		        	 Element port = doc.createElement("PORT");
		        	 port.setTextContent(table.getValueAt(i, 3).toString());
		        	 peer.appendChild(port);
	        	 }
	         }
	         
	         return documentToString(doc);
		}
		catch(ParserConfigurationException | DOMException ex) {
				System.out.println("Loi xml2: " + ex.getMessage());
	            ex.printStackTrace();
	            return null;
	     } 
		catch (Exception ex) {
	            Logger.getLogger(XMLProtocol.class.getName()).log(Level.SEVERE, null, ex);
	            System.out.println("Loi xml: " + ex.getMessage());
	            return null;
	    }
	}
	public DefaultTableModel parseString(String s) throws Exception{
		DefaultTableModel table = new DefaultTableModel();
		table.addColumn("username");
		table.addColumn("pass");
		table.addColumn("ip");
		table.addColumn("port");
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.parse(new InputSource(new StringReader(s)));
		doc.getDocumentElement().normalize();
		 NodeList nList = doc.getElementsByTagName("PEER");
		
         for(int i = 0; i< nList.getLength(); i++){
         	Node nNode = nList.item(i);
         	
         	if(nNode.getNodeType()==Node.ELEMENT_NODE)
         	{
         		Element eElement = (Element)nNode;
         		String userName = eElement.getElementsByTagName("USER_NAME").item(0).getTextContent();
         		String pass = "";
         		String ip = eElement.getElementsByTagName("IP").item(0).getTextContent();
         		String port = eElement.getElementsByTagName("PORT").item(0).getTextContent();
         		
         		String[] data = {userName,pass,ip,port};
         		table.addRow(data);
         		
         	}
         }
		return table;
		
	}
	public String registerDeny(){
		try{
			 DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
	         DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
	         Document doc = docBuilder.newDocument();
	         
	         Element file = doc.createElement("REGISTER_DENY");
	         doc.appendChild(file);
	        
	         return documentToString(doc);
		}
		catch(ParserConfigurationException | DOMException ex) {
	            ex.printStackTrace();
	            return null;
	     } 
		catch (Exception ex) {
	            Logger.getLogger(XMLProtocol.class.getName()).log(Level.SEVERE, null, ex);
	            return null;
	    }
	}
	
	//  Tra ve danh sach cac user dang online dang XML khi register accept
	 
	public String registerAccept(DefaultTableModel table){
		try{
			 DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
	         DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
	         Document doc = docBuilder.newDocument();
	         
	         Element list = doc.createElement("REGISTER_ACCEPT");
	         doc.appendChild(list);
	         
	         for(int i = table.getRowCount() - 1; i>=0; i--)
	         {
	        	 if(table.getValueAt(i, 2)!=""){
		        	 Element peer = doc.createElement("PEER");
		        	 list.appendChild(peer);
		        	 Element user = doc.createElement("USER_NAME");
		        	 user.setTextContent(table.getValueAt(i, 0).toString());
		        	 peer.appendChild(user);
		        	 Element ip = doc.createElement("IP");
		        	 ip.setTextContent(table.getValueAt(i, 2).toString());
		        	 peer.appendChild(ip);
		        	 Element port = doc.createElement("PORT");
		        	 port.setTextContent(table.getValueAt(i, 3).toString());
		        	 peer.appendChild(port);
	        	 }
	         }
	         
	         return documentToString(doc);
		}
		catch(ParserConfigurationException | DOMException ex) {
	            ex.printStackTrace();
	            return null;
	     } 
		catch (Exception ex) {
	            Logger.getLogger(XMLProtocol.class.getName()).log(Level.SEVERE, null, ex);
	            return null;
	    }
	}
	public String loginDeny(){
		try{
			 DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
	         DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
	         Document doc = docBuilder.newDocument();
	         
	         Element file = doc.createElement("LOGIN_DENY");
	         doc.appendChild(file);
	        
	         return documentToString(doc);
		}
		catch(ParserConfigurationException | DOMException ex) {
	            ex.printStackTrace();
	            return null;
	     } 
		catch (Exception ex) {
	            Logger.getLogger(XMLProtocol.class.getName()).log(Level.SEVERE, null, ex);
	            return null;
	    }
	}
	
	/*
	 * Chat
	 * */
	public String chatDeny(){
		try{
			 DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
	         DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
	         Document doc = docBuilder.newDocument();
	         
	         Element file = doc.createElement("CHAT_DENY");
	         doc.appendChild(file);
	        
	         return documentToString(doc);
		}
		catch(ParserConfigurationException | DOMException ex) {
	            ex.printStackTrace();
	            return null;
	     } 
		catch (Exception ex) {
	            Logger.getLogger(XMLProtocol.class.getName()).log(Level.SEVERE, null, ex);
	            return null;
	    }
	}
	public String chatAccept(){
		try{
			 DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
	         DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
	         Document doc = docBuilder.newDocument();
	         
	         Element file = doc.createElement("CHAT_ACCEPT");
	         doc.appendChild(file);
	        
	         return documentToString(doc);
		}
		catch(ParserConfigurationException | DOMException ex) {
	            ex.printStackTrace();
	            return null;
	     } 
		catch (Exception ex) {
	            Logger.getLogger(XMLProtocol.class.getName()).log(Level.SEVERE, null, ex);
	            return null;
	    }
	}
	public String chatClose(){
		try{
			 DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
	         DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
	         Document doc = docBuilder.newDocument();
	         
	         Element file = doc.createElement("CHAT_CLOSE");
	         doc.appendChild(file);
	        
	         return documentToString(doc);
		}
		catch(ParserConfigurationException | DOMException ex) {
	            ex.printStackTrace();
	            return null;
	     } 
		catch (Exception ex) {
	            Logger.getLogger(XMLProtocol.class.getName()).log(Level.SEVERE, null, ex);
	            return null;
	    }
	}
	public String chatRequest(String userName){
		try{
			 DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
	         DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
	         Document doc = docBuilder.newDocument();
	         
	         Element chat = doc.createElement("CHAT_REQ");
	         doc.appendChild(chat);
	         Element user = doc.createElement("USER_NAME");
	         user.setTextContent(userName);
	         chat.appendChild(user);
	         
	         return documentToString(doc);
		}
		catch(ParserConfigurationException | DOMException ex) {
	            ex.printStackTrace();
	            return null;
	     } 
		catch (Exception ex) {
	            Logger.getLogger(XMLProtocol.class.getName()).log(Level.SEVERE, null, ex);
	            return null;
	    }
	}
	/*
	 * Chuyen message dang string thanh XML
	 * */
	public String messageToXML(String message){
		String convertMess = addEscape(message);
		try{
			 DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
	         DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
	         Document doc = docBuilder.newDocument();
	         
	         Element chat = doc.createElement("CHAT_MSG");
	         chat.setTextContent(convertMess);
	         doc.appendChild(chat);
	         
	         return documentToString(doc);
		}
		catch(ParserConfigurationException | DOMException ex) {
	            ex.printStackTrace();
	            return null;
	     } 
		catch (Exception ex) {
	            Logger.getLogger(XMLProtocol.class.getName()).log(Level.SEVERE, null, ex);
	            return null;
	    }
	}
	/*
	 * String message co dang <CHAT_MSG> <CHAT_MSG/>
	 * */
	public String XMLToMessage(String message){
		try{
			 DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
	         DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
	         Document doc = docBuilder.parse(new InputSource(new StringReader(message)));
	         doc.getDocumentElement().normalize();
	         String element = doc.getElementsByTagName("CHAT_MSG").item(0).getTextContent();
	         return remoteEscape(element);
	         
		}
		catch(ParserConfigurationException | DOMException ex) {
	            ex.printStackTrace();
	            return null;
	     } 
		catch (Exception ex) {
	            Logger.getLogger(XMLProtocol.class.getName()).log(Level.SEVERE, null, ex);
	            return null;
	    }
	}
	private String addEscape(String message){
		int index = 0;
		while(message.indexOf('<',index)>-1){
			index = message.indexOf('<',index);
			String part1 = message.substring(0, index);
			String part2 = message.substring(index);
			message = part1+ "<" + part2;
			index = index+2;
		}
		 index = 0;
		while(message.indexOf('>',index)>-1){
			index = message.indexOf('<',index);
			String part1 = message.substring(0, index);
			String part2 = message.substring(index);
			message = part1+ ">" + part2;
			index = index+2;
		}
		return message;
	}
	private String remoteEscape(String message){
		int index = 0;
		while(message.indexOf('<',index)>-1){
			index = message.indexOf('<',index);
			String part1 = message.substring(0, index);
			String part2 = message.substring(index+1);
			message = part1 + part2;
			index = index+1;
		}
		 index = 0;
		while(message.indexOf('>',index)>-1){
			index = message.indexOf('<',index);
			String part1 = message.substring(0, index);
			String part2 = message.substring(index+1);
			message = part1 + part2;
			index = index+1;
		}
		return message;
	}
	
	/*
	 * File
	 * */
	public String fileRequest(String fileName){
		try{
			 DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
	         DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
	         Document doc = docBuilder.newDocument();
	         
	         Element file = doc.createElement("FILE_REQ");
	         file.setTextContent(fileName);
	         doc.appendChild(file);
	         
	         return documentToString(doc);
		}
		catch(ParserConfigurationException | DOMException ex) {
	            ex.printStackTrace();
	            return null;
	     } 
		catch (Exception ex) {
	            Logger.getLogger(XMLProtocol.class.getName()).log(Level.SEVERE, null, ex);
	            return null;
	    }
	}
	public String fileRequestNoAck(){
		try{
			 DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
	         DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
	         Document doc = docBuilder.newDocument();
	         
	         Element file = doc.createElement("FILE_REQ_NOACK");
	         doc.appendChild(file);
	        
	         return documentToString(doc);
		}
		catch(ParserConfigurationException | DOMException ex) {
	            ex.printStackTrace();
	            return null;
	     } 
		catch (Exception ex) {
	            Logger.getLogger(XMLProtocol.class.getName()).log(Level.SEVERE, null, ex);
	            return null;
	    }
	}
	public String fileRequestAck(String port){
		try{
			 DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
	         DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
	         Document doc = docBuilder.newDocument();
	         
	         Element file = doc.createElement("FILE_REQ_ACK");
	         doc.appendChild(file);
	         Element _port = doc.createElement("PORT");
	         _port.setTextContent(port);
	         file.appendChild(_port);
	   
	         return documentToString(doc);
		}
		catch(ParserConfigurationException | DOMException ex) {
	            ex.printStackTrace();
	            return null;
	     } 
		catch (Exception ex) {
	            Logger.getLogger(XMLProtocol.class.getName()).log(Level.SEVERE, null, ex);
	            return null;
	    }
	}
	public String fileDataBegin(){
		try{
			 DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
	         DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
	         Document doc = docBuilder.newDocument();
	         
	         Element file = doc.createElement("FILE_DATA_BEGIN");
	         doc.appendChild(file);
	        
	         return documentToString(doc);
		}
		catch(ParserConfigurationException | DOMException ex) {
	            ex.printStackTrace();
	            return null;
	     } 
		catch (Exception ex) {
	            Logger.getLogger(XMLProtocol.class.getName()).log(Level.SEVERE, null, ex);
	            return null;
	    }
	}
	public String fileData(String content){
		try{
			 DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
	         DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
	         Document doc = docBuilder.newDocument();
	         
	         Element file = doc.createElement("FILE_DATA");
	         file.setTextContent(content);
	         doc.appendChild(file);
	        
	         return documentToString(doc);
		}
		catch(ParserConfigurationException | DOMException ex) {
	            ex.printStackTrace();
	            return null;
	     } 
		catch (Exception ex) {
	            Logger.getLogger(XMLProtocol.class.getName()).log(Level.SEVERE, null, ex);
	            return null;
	    }
	}
	public String fileDataEnd(){
		try{
			 DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
	         DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
	         Document doc = docBuilder.newDocument();
	         
	         Element file = doc.createElement("FILE_DATA_END");
	         doc.appendChild(file);
	        
	         return documentToString(doc);
		}
		catch(ParserConfigurationException | DOMException ex) {
	            ex.printStackTrace();
	            return null;
	     } 
		catch (Exception ex) {
	            Logger.getLogger(XMLProtocol.class.getName()).log(Level.SEVERE, null, ex);
	            return null;
	    }
	}
}
