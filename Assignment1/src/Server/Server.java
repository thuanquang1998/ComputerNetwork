package Server;

import javax.swing.table.DefaultTableModel;

import java.net.*;

public class Server implements Runnable{
	protected DefaultTableModel table;
	protected DefaultTableModel tableonl;
	protected ServerForm form;
	private ServerSocket server;
	private Thread thread = null;
	private int port = 6996;

	
	public Server(ServerForm form){
		this.form = form;
		this.table= form.table;
		this.tableonl = form.tableonl;
		try{
			server = new ServerSocket(port);
			//Lấy IP máy
			form.txtArea.append("Server started. IP: "+InetAddress.getLocalHost() +", Port: "+ server.getLocalPort());
			Start();
		}
		catch(Exception e){
			//khởi động lại khi gặp lỗi
			form.RetryStart(0);
			//System.err.println(e.getMessage());
		}
	}
	public Server(ServerForm form, int port){
		this.form = form;
		this.port = port;
		try{
			server = new ServerSocket(port);
			form.txtArea.append("Server started. IP: "+InetAddress.getLocalHost() +", Port: "+ server.getLocalPort());
			Start();
		}
		catch(Exception e){
			form.RetryStart(0);
			//System.err.println(e.getMessage());
		}
	}
	private void addThread(Socket socket){
		form.txtArea.append("\nClient accepted: " + socket);
		ServerThread client = new ServerThread(socket, this);
		client.start();
	}
	private void Start(){
		if(thread == null){
			thread = new Thread(this);
			thread.start();
		}
	}
	@SuppressWarnings("deprecation")
	public void Stop(){
		if(thread!=null){
			thread.stop();
			thread = null;
		}
	}
	@Override
	public void run(){
		while(thread != null){
			try{
				form.txtArea.append("\nWaiting for a client.....");
				addThread(server.accept());
			}
			catch(Exception e){
				form.txtArea.append("\nServer accept error: ");
				form.RetryStart(0);
				//System.err.println(e.getMessage());
			}
		}
	}
}
