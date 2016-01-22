package ccasws;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Date;
import java.text.*;
import java.util.*;
import java.net.*;
import javax.swing.table.*;

public class CLog {
	static FileOutputStream out; //declare a file output object
	static PrintWriter p; //declare a print stream object
	//static String configfile = "d:\\ccasconf\\ccas.xml";
	static String configfile=URLDecoder.decode(Thread.currentThread().getContextClassLoader().getResource(".").getPath())+".."+File.separator+"lib"+File.separator+"ccas.xml";
	static String path =URLDecoder.decode(Thread.currentThread().getContextClassLoader().getResource(".").getPath())+".."+File.separator+"logs";



	public CLog() {
		//if	(System.getProperty("os.name").indexOf("Window")<0) configfile = File.separator+"ccasconf"+File.separator+"ccas.xml";
		//path = getXmlValue(configfile,"clog","log","path","d:\\ccaslog");
		try {
			configfile=URLDecoder.decode(Thread.currentThread().getContextClassLoader().getResource(".").getPath())+".."+File.separator+"lib"+File.separator+"ccas.xml";
			path =URLDecoder.decode(Thread.currentThread().getContextClassLoader().getResource(".").getPath())+".."+File.separator+"logs";
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static String getConfigfile()
	{
		return configfile;
	}
	public static String getPath()
	{
		return path;
	}

	public static String getLogfile()
	{
		return path+File.separator+App.getCurDateStr(11)+".log";
	}


	synchronized public static void writeLog(String logmsg) {
		App.Mkdir(path);
		Date currTime = new Date();
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd HH:mm:ss",Locale.US);
		try {
			out = new FileOutputStream(path+File.separator+App.getCurDateStr(11)+".log",true);
			//Connect print stream to the output stream
			p = new PrintWriter(out);
			//logmsg = new String(logmsg.getBytes("GBK"));
			p.println(sf.format(currTime) + " " + logmsg);
			p.close();
			out.close();
		} catch (Exception e) {
			System.err.println("Error writing to file[" + path+File.separator+App.getCurDateStr(11)+".log]");
		}
	}

	public static void writeLog(int logmsg) {
		writeLog(""+logmsg);
	}


	public static void writeLog(double logmsg) {
		writeLog(""+logmsg);
	}

	public static void initLog(String filename) {
		if (filename == null || filename.trim().equals("")) {
			return;
		}
		try {
			out = new FileOutputStream(filename);
			//Connect print stream to the output stream
			//p = new PrintWriter(out);

			//p.close();
			out.close();
		} catch (Exception e) {
			System.err.println("Error writing to file:" + filename);
		}
		return ;
	}

}

class MyMainFrame extends JFrame
{
	//TxnMsgPanel txnMsgPanel=new TxnMsgPanel();
	JTextArea infoText=new JTextArea();

	public MyMainFrame() {

		//Add window listener.
		this.addWindowListener
		(
		 	new WindowAdapter() {
		 	 	public void windowClosing(WindowEvent e) {
		 	 		MyMainFrame.this.windowClosed();
		 	 	}
		 	}
		);
/**/
	  JPanel contentPane;

	  JTabbedPane jTabbedPane1=new JTabbedPane();
		contentPane = (JPanel)getContentPane();
		contentPane.setLayout(new BorderLayout());
		contentPane.add(jTabbedPane1, "Center");
		jTabbedPane1.add("Txn Message",new JPanel());
		jTabbedPane1.add("Txn Message1",new JPanel());


		//设置背景色
		//setBackground(new Color(0x9f,0x8f,0x7f));
		setTitle("ccas Ulitility");
		JMenuBar jmenuBar = new JMenuBar();
		//setSize(new Dimension(400, 400));

		setSize(560,400);
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		/*	测试屏幕分辨率	*/
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		if (getSize().height > screenSize.height) {
			getSize().height = screenSize.height;
		}
		if (getSize().width > screenSize.width) {
			getSize().width = screenSize.width;
		}
		/* 使窗口自动居中 */
		setLocation((screenSize.width - getSize().width)/2,(screenSize.height - getSize().height)/2);

		JMenu jmenuFile = new JMenu();
		JMenuItem jmenuFileExit = new JMenuItem();

		jmenuFile.setLabel("File");
		jmenuFileExit.setLabel("Exit");
		//Add action listener.for the menu button
		jmenuFileExit.addActionListener
		(
		 	new ActionListener() {
		 		public void actionPerformed(ActionEvent e) {
		 			windowClosed();
		 		}
		 	}
		);

		jmenuFile.add(jmenuFileExit);
		jmenuBar.add(jmenuFile);

/**/

		JMenu jmenuHelp = new JMenu();
		JMenuItem jmenuHelpVersion = new JMenuItem();
		JMenuItem jmenuHelpParameter = new JMenuItem();


		jmenuHelp.setLabel("Help");
		jmenuHelpParameter.setLabel("Parameter");
		//Add action listener.for the menu button
		jmenuHelpParameter.addActionListener
		(
		 	new ActionListener() {
		 		public void actionPerformed(ActionEvent e) {
		 			Parameter();
		 		}
		 	}
		);
		jmenuHelp.add(jmenuHelpParameter);

		jmenuHelpVersion.setLabel("Version");
		//Add action listener.for the menu button
		jmenuHelpVersion.addActionListener
		(
		 	new ActionListener() {
		 		public void actionPerformed(ActionEvent e) {
		 			Version();
		 		}
		 	}
		);
		jmenuHelp.add(jmenuHelpVersion);

		jmenuBar.add(Box.createGlue());
		jmenuBar.add(jmenuHelp);

		JScrollPane jScrollPane1 = new JScrollPane();
		jScrollPane1.getViewport().add(infoText, null);
		infoText.setText("");
		infoText.setToolTipText("");
		infoText.setFont(new Font("default",0,12));
		infoText.setRows(4);
		add(jScrollPane1,"South");

		setJMenuBar(jmenuBar);

	}


	/**
		* Shutdown procedure when run as an application.
	*/
	protected void windowClosed() {
	//TODO: Check if it is save to close the application
	//Exit application.
		System.exit(0);
	}

	protected void Parameter() {
		Frame frame=new Frame("Parameter"); //实例化框架 frame

		final Dialog dl=new Dialog(frame,"ccas Parameter");
		dl.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				dl.setVisible(false);
				infoText.append("==>Parameter...\n");
			}
		});
		dl.setLayout(new BorderLayout());

		JScrollPane jScrollPane1 = new JScrollPane();
		JPanel jPanel1 = new JPanel();
		JTable jTable1 = new JTable();
		this.getContentPane().add(jPanel1, BorderLayout.CENTER);
		jScrollPane1.getViewport().add(jTable1, null);
		//jPanel1.add(jScrollPane1, null);
		jPanel1.add(jScrollPane1,BorderLayout.CENTER);
		DefaultTableModel model= new DefaultTableModel(){
			public boolean isCellEditable(int row, int column){
				if	(column<=0)	return false;	else	return true;
			}
		};
		jTable1 = new JTable(model);
		model.addColumn("Parameter");
		model.addColumn("values");
		RowSorter sorter = new TableRowSorter(model);
		jTable1.setRowSorter(sorter);
		//jTable1.setEnabled(false);


		String val1="none",val2="none";
		try {
			//if	(log.getConfigfile()!=null)		val1=log.getConfigfile();
			//if	(log.getLogfile()!=null)			val2=log.getLogfile();
		}	catch (Exception ex) {
			ex.printStackTrace();
			//JOptionPane.showMessageDialog(null, ex);
		}
		model.insertRow(0, new Object[]{"configfile",val1});
		model.insertRow(1, new Object[]{"logfile",val2});
		//jTable1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); //滚动条设置左右滚
		jScrollPane1.getViewport().add(jTable1,null); //在滚动条中放入表
		JButton	jb1=new JButton("send");
		//jb1.addActionListener(this);
		jScrollPane1.add(jb1,null);
		dl.add(jb1,BorderLayout.SOUTH);
		//dl.add(jScrollPane1,null);
		dl.add(jScrollPane1,null);
		jTable1.getColumnModel().getColumn(0).setMaxWidth(80);
		//jTable1.getColumnModel().getColumn(0).setPreferredWidth(80);
		jTable1.setRowHeight(24);
		dl.setSize(500,200);
		dl.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - dl.getSize().width)/2,(Toolkit.getDefaultToolkit().getScreenSize().height - dl.getSize().height)/2);

		dl.setVisible(true);

	}


	protected void Version() {
		Frame frame=new Frame("Version"); //实例化框架 frame

		final Dialog dl=new Dialog(frame,"ccas Version");
		dl.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				dl.setVisible(false);
				infoText.append("==>Version...\n");
			}
		});

		//dl.setBounds(200,200,400,200);
		dl.setSize(300,120);
		dl.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - dl.getSize().width)/2,(Toolkit.getDefaultToolkit().getScreenSize().height - dl.getSize().height)/2);
		//dl.setBackground(Color.cyan);
		//dl.setForeground(Color.blue);

		dl.setFont(new java.awt.Font("Dialog",0,16));
		Label l1=new Label("ccas Ver1.0.0R1(20071122)",Label.CENTER);
		l1.setForeground(Color.blue);
		Label l2=new Label("MSN:nieyong_abc@hotmail.com",Label.CENTER);
		l2.setFont(new java.awt.Font("Dialog",0,12));
		dl.setLayout(new BorderLayout());
		dl.add(l1,BorderLayout.CENTER);
		dl.add(l2,BorderLayout.SOUTH);
		dl.setVisible(true);
	}

}

class JStartWindow extends JWindow implements Runnable {
	Thread	startThread=null;

	public JStartWindow() {
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		JPanel start = new JPanel(new BorderLayout());
		//读取图片文件
		URL url = getClass().getResource("start.jpg");
		if(url != null){
			start.add(new JLabel(new ImageIcon(url)),
			BorderLayout.CENTER);
		}
		setContentPane(start);
		//Dimension screenSize = getToolkit().getScreenSize();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		pack();
		//使窗口居中显示
		setLocation((screenSize.width - getSize().width)/2,(screenSize.height - getSize().height)/2);
	}

	public void start(){
		this.toFront();
		startThread=new Thread(this);
		startThread.start();
	}

	public void run(){
		try {
			show();
			//延时3s后，关闭窗口
			Thread.sleep(1000);
		}	catch (Exception ex) {
			ex.printStackTrace();
		}
		this.dispose();
	}

	static void showFrame(String title){
		JFrame frame = new JFrame(title);
		frame.setSize(400,300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		/*	测试屏幕分辨率	*/
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		if (frame.getSize().height > screenSize.height) {
			frame.getSize().height = screenSize.height;
		}
		if (frame.getSize().width > screenSize.width) {
			frame.getSize().width = screenSize.width;
		}
		/* 使窗口自动居中 */
		frame.setLocation((screenSize.width - frame.getSize().width)/2,(screenSize.height - frame.getSize().height)/2);

	Label lb1=new Label("ccas");
	lb1.setFont(new Font("default",1,24));
	frame.getContentPane().add(lb1,BorderLayout.CENTER);
	frame.setVisible(true);
	}

	public static void main(String[] args)
	{
		showFrame("Demo start window");//显示主窗口
		JStartWindow start = new JStartWindow();
		/*	显示启动界面，3s钟后自动消失	*/
		start.start();
	}
}


