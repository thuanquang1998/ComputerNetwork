package Client;

import Protocol.*;

import java.io.*;
import java.net.*;
import java.awt.event.*;

import javax.swing.*;

public class ClientGUI extends JFrame{

	private static final long serialVersionUID = 1L;
	private ClientGUI frame = null;
	/**
	 * Launch the application.
	 */
	
	/**
	 * Create the application.
	 */
	public ClientGUI(UserStatusGUI frmStt) {
		
		initialize();
		this.frmStt = frmStt;
		
	}
	public void connect(Socket s, Socket sFile, String userchat) throws IOException{
		client = s;
		reciever = new RecieveMessageThread(this, s);
		reciever.userChat = userchat;
		reciever.start();
        StartShareFile(sFile);
	}
	/**
	 * Initialize the contents of the frame.
	 */
	
	private void initialize() {
		//frame = new JFrame();
		this.setBounds(100, 100, 506, 304);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		SpringLayout springLayout = new SpringLayout();
		this.getContentPane().setLayout(springLayout);
		
		btnSend = new JButton("Send");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//ClientGUI f = new ClientGUI();
				//f.setVisible

				if (Sender)
				{
					long size = file.length();
					if (size<150*1024*1024)
					{
						share.send(new XMLProtocol().fileRequest(file.getName()));
						
						//share.sendfile(filepath);
						textFieldMess.setText("");
						//txtrMsg.append("Bạn gửi tập tin thành công \n");
						Sender = false;
					}
					else 
					{
						Sender = false;
						textFieldMess.setText("");
						//txtrMsg.append("File is size too large\n");
					}
							
				}
				else{
					sendMessage();
				}

			}
		});
		this.getContentPane().add(btnSend);
		
		btnLinkSend = new JButton("...");
		springLayout.putConstraint(SpringLayout.EAST, btnLinkSend, -197, SpringLayout.EAST, getContentPane());
		springLayout.putConstraint(SpringLayout.NORTH, btnSend, 0, SpringLayout.NORTH, btnLinkSend);
		springLayout.putConstraint(SpringLayout.WEST, btnSend, 22, SpringLayout.EAST, btnLinkSend);
		btnLinkSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				actionChooseFile();
			}
		});
		this.getContentPane().add(btnLinkSend);
		
		textFieldMess = new JTextField();
		springLayout.putConstraint(SpringLayout.WEST, btnLinkSend, 19, SpringLayout.EAST, textFieldMess);
		springLayout.putConstraint(SpringLayout.EAST, textFieldMess, -262, SpringLayout.EAST, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, textFieldMess, 10, SpringLayout.WEST, getContentPane());
		textFieldMess.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				if (arg0.getKeyCode() == KeyEvent.VK_ENTER)
				{
					btnSend.doClick();
				}
			}
		});
		this.getContentPane().add(textFieldMess);
		textFieldMess.setColumns(10);
		
		scrPnlMSg = new JScrollPane();
		springLayout.putConstraint(SpringLayout.NORTH, btnLinkSend, 13, SpringLayout.SOUTH, scrPnlMSg);
		springLayout.putConstraint(SpringLayout.NORTH, textFieldMess, 14, SpringLayout.SOUTH, scrPnlMSg);
		springLayout.putConstraint(SpringLayout.EAST, scrPnlMSg, 458, SpringLayout.WEST, getContentPane());
		springLayout.putConstraint(SpringLayout.NORTH, scrPnlMSg, 10, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, scrPnlMSg, 10, SpringLayout.WEST, getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, scrPnlMSg, 215, SpringLayout.NORTH, getContentPane());
		getContentPane().add(scrPnlMSg);
		
		txtrMsg = new JTextArea();
		scrPnlMSg.setViewportView(txtrMsg);
		txtrMsg.setEditable(false);
		txtrMsg.setWrapStyleWord(true);
		
		JButton btnOnline = new JButton("Online");
		springLayout.putConstraint(SpringLayout.EAST, btnSend, -25, SpringLayout.WEST, btnOnline);
		springLayout.putConstraint(SpringLayout.NORTH, btnOnline, 228, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, btnOnline, -10, SpringLayout.EAST, getContentPane());
		btnOnline.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frmStt.setState(JFrame.NORMAL);
				frmStt.setVisible(true);
			}
		});
		getContentPane().add(btnOnline);
		frame = this;
	}	
	public void actionChooseFile(){

		file = share.actionChooseFile();
		Sender = true;
	}
	
	public void StartShareFile( Socket socket) {

		try{
			//Socket shareSocket = new Socket(socket.getLocalAddress(), socket.getLocalPort());
			share = new SharedFile(socket,frame);
			share.start();
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
	}

	public void addMessage(String msg, String src)
	{
		String message = src + ":" + msg + "\r\n";
		txtrMsg.setText(txtrMsg.getText() + message);
	}

	public void sendMessage()
	{
		addMessage(textFieldMess.getText(), "Me");
		sender = new SendMessageThread(client, textFieldMess.getText());
		//System.out.println("Msg trong GUI: " + textFieldMess.getText());
		sender.start();
		textFieldMess.setText("");
	}

	public javax.swing.JButton btnSend;
    public javax.swing.JButton btnLinkSend;
    public javax.swing.JTextField textFieldMess;
   
    private JScrollPane scrPnlMSg;
    public JTextArea txtrMsg;
    SharedFile share;
    private SendMessageThread sender = null;
    private RecieveMessageThread reciever = null;
  //private JFrame frame;
  	public Socket client;
    //public int port;


  //  public Thread clientThread;
    public File file;

    public String filepath;
    public boolean Sender = false;

    private UserStatusGUI frmStt;
  
}
