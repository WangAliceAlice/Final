package chat;

import javax.swing.JFileChooser;
import javax.swing.JProgressBar;
import java.util.Calendar;
import javax.swing.JTextArea;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.net.*;
import java.io.*;
import java.net.Socket;

public class ChatFrame extends JFrame implements ActionListener, ItemListener, Runnable, KeyListener {
	// �����ļ���ر���
	private sendfilethread sendthread = null;// �����ļ��߳�;
	private acceptfilethread acceptthread = null;// �����ļ��߳�
	private Socket acceptfilesocket = null; // �����ļ���������
	private Socket sendfilesocket = null; // �����ļ���������
	private ServerSocket fileserver = null; // �����ļ�������
	private String filemsg = null; // �����ļ�������
	private String sendfilename = null;// ���������͵��ļ���
	private String IP = null;// ���汾��IP= InetAddress.getLocalHost();//
	private Boolean acceptboolean = false;
	private Boolean sendboolean = false;
	
	private int port = 6200;
	private JProgressBar acceptProgressbar = null;// ���ս�����
	private JProgressBar sendProgressbar = null; // ���ͽ�����

	private Box leftbox = null;
	private Box rightbox = null;
	private Box leftrightbox = null;
	private Box rightleftbox = null;
	private JButton sendfile; // �����ļ���ť
	private JButton cancelsendfile; // ȡ�������ļ���ť
	private JButton acceptfile; // �����ļ���ť
	private JButton refusefile; // �ܾ������ļ���ť
	private MyTextArea sendfileArea = null; // �����ļ���ʾ��
	private JTextPane acceptfileArea = null; // �����ļ���ʾ��

	private static final long serialVersionUID = 1L;

	// ***************�˵���***********************
	private Box box = null; // ���������������
	private JComboBox fontName = null, fontSize = null, fontStyle = null,
			fontColor = null;
	// ��������;�ֺŴ�С;������ʽ;������ɫ;����
	private StyledDocument doc = null;

	private JFileChooser jfc;// �ļ�����·��ѡ����

	private JTextPane commonArea = null; // ����������
	private JTextPane myMsgArea = null;// �ҵ�Ƶ��������
	public JComboBox perponsComboBox; // �����˵�
	private JTextArea inMsgField; // ���������
	private JCheckBox privateTalk;// ˽��checkbox
	private boolean privateTalkFlag = false; // �Ƿ���˽��,Ĭ��ֵΪ��
	private JButton sentButton; // ������Ϣ��ť

	private JButton screenCapture;// ������ť
	private JMenuItem menuItem;
	private JMenuItem cMenuItem;
	public BufferedReader in;
	public PrintWriter out;
	public String myName;
	private String withWho = "������";
	String outmsg;// ���͵���Ϣ
	String mywords;// Ҫ˵�Ļ�
	JPanel centerPanel;
	JScrollPane commonAreaScroll;
	JScrollPane myMsgAreaScroll;
	JScrollPane inMsgFieldScroll;

	public PList plist = null;

	public ChatFrame(String host) {
		super(host + "��������");
		try {
			// ʹ��Windows�Ľ�����
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}
		myName = host;
		plist = new PList(this);
		try {
			InetAddress addr = InetAddress.getLocalHost();
			IP = addr.getHostAddress().toString();// ��ñ���IP

		} catch (Exception e) {
			System.out.print("�޷���ȡ��������");

		}

		acceptProgressbar = new JProgressBar();
		acceptProgressbar.setOrientation(JProgressBar.HORIZONTAL);
		acceptProgressbar.setMinimum(0);
		acceptProgressbar.setMaximum(100);
		acceptProgressbar.setValue(0);
		acceptProgressbar.setStringPainted(true);

		sendProgressbar = new JProgressBar();
		sendProgressbar.setOrientation(JProgressBar.HORIZONTAL);
		sendProgressbar.setMinimum(0);
		sendProgressbar.setMaximum(100);
		sendProgressbar.setValue(0);
		sendProgressbar.setStringPainted(true);

		// *****�������Ҳ�************//
		sendfile = new JButton("�����ļ�"); // �����ļ���ť
		cancelsendfile = new JButton("ȡ������"); // ȡ�������ļ���ť
		acceptfile = new JButton("�����ļ�"); // �����ļ���ť
		refusefile = new JButton("�ܾ��ļ�"); // �ܾ������ļ���ť
		acceptfileArea = new JTextPane(); // �����ļ���ʾ��
		acceptfileArea.setEditable(false); // ���ɴ��ⲿд
		sendfileArea = new MyTextArea(); // �����ļ���ʾ��
		sendfileArea.setEditable(false);
		leftrightbox = Box.createHorizontalBox(); // �нṹ

		leftrightbox.add(acceptfile, BorderLayout.WEST);
		leftrightbox.add(refusefile, BorderLayout.EAST);
		Box rightabove = Box.createVerticalBox();
		rightabove.add(leftrightbox);
		rightabove.add(acceptProgressbar);

		rightbox = Box.createVerticalBox();// ���ṹ
		rightbox.add(Box.createVerticalStrut(10));
		rightbox.add(rightabove);
		rightbox.add(Box.createVerticalStrut(10));

		JScrollPane inMsgFieldScroll1 = new JScrollPane(acceptfileArea);
		inMsgFieldScroll1
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

		inMsgFieldScroll1.setBorder(BorderFactory.createTitledBorder("�����յ��ļ�"));
		inMsgFieldScroll1.setBackground(new Color(188, 193, 199));
		rightbox.add(inMsgFieldScroll1, BorderLayout.CENTER);
		rightbox.setBackground(new Color(250, 0, 2));// /

		rightbox.add(Box.createVerticalStrut(10)); // /
		rightleftbox = Box.createHorizontalBox();
		rightleftbox.add(sendfile, BorderLayout.WEST);
		rightleftbox.add(cancelsendfile, BorderLayout.EAST);
		Box rightmiddle = Box.createVerticalBox();
		rightmiddle.add(rightleftbox);
		rightmiddle.add(sendProgressbar);
		rightbox.add(rightmiddle, BorderLayout.CENTER);

		JScrollPane inMsgFieldScroll2 = new JScrollPane(sendfileArea);
		inMsgFieldScroll2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		inMsgFieldScroll2.setBorder(BorderFactory.createTitledBorder("�����͵��ļ�"));
		inMsgFieldScroll2.setBackground(new Color(188, 193, 199));
		rightbox.add(inMsgFieldScroll2, BorderLayout.CENTER);
		rightbox.setBackground(new Color(0, 0, 255));

		// ****************************************************//

		// *****�������Ҳ�************//
		// *********************�����Ҷ���**************//

		JPanel upperPanel = new JPanel();
		String[] str_name = { "����", "����", "Dialog", "Gulim" };
		String[] str_Size = { "12", "14", "18", "22", "30", "40" };
		String[] str_Style = { "����", "б��", "����", "��б��" };
		String[] str_Color = { "��ɫ", "��ɫ", "��ɫ", "��ɫ", "��ɫ" };
		fontName = new JComboBox(str_name); // ��������
		fontSize = new JComboBox(str_Size); // �ֺ�
		fontStyle = new JComboBox(str_Style); // ��ʽ
		fontColor = new JComboBox(str_Color); // ��ɫ

		box = Box.createVerticalBox(); // ���ṹ
		Box box_1 = Box.createHorizontalBox(); // ��ṹ
		Box box_2 = Box.createVerticalBox(); // ��ṹ
		box.add(box_1);
		box.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8)); // 8���ı߾�
		// ��ʼ�����������������
		box_1.add(new JLabel("���壺")); // �����ǩ
		box_1.add(fontName); // �������
		box_1.add(Box.createHorizontalStrut(8)); // ���
		box_1.add(new JLabel("��ʽ��"));
		box_1.add(fontStyle);
		box_1.add(Box.createHorizontalStrut(8));
		box_1.add(new JLabel("�ֺţ�"));
		box_1.add(fontSize);
		box_1.add(Box.createHorizontalStrut(8));
		box_1.add(new JLabel("��ɫ��"));
		box_1.add(fontColor);
		box_1.add(Box.createHorizontalStrut(8));
		box_1.add(Box.createHorizontalStrut(8));

		upperPanel.add(box, BorderLayout.SOUTH);

		// **************�м���������������***********************//

		Border brd = BorderFactory.createMatteBorder(2, 2, 2, 1, new Color(125, 161, 253));

		centerPanel = new JPanel(new BorderLayout());

		commonArea = new JTextPane(); // ����������
		commonArea.setBorder(brd);
		commonArea.setEditable(false); // ���ɱ༭
		commonArea.getScrollableUnitIncrement(new Rectangle(10, 20), SwingConstants.VERTICAL, -2);
		commonAreaScroll = new JScrollPane(commonArea);
		commonAreaScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);// ���ù�����ʲôʱ�����
		commonAreaScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		commonAreaScroll.setBorder(BorderFactory.createTitledBorder("Ⱥ����"));

		box_2.add(commonAreaScroll);
		box_2.add(Box.createVerticalStrut(2));

		myMsgArea = new JTextPane(); // �ҵķ���
		myMsgArea.setBorder(brd);
		myMsgArea.setEditable(false);
		myMsgAreaScroll = new JScrollPane(myMsgArea);
		myMsgAreaScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		myMsgAreaScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		myMsgAreaScroll.setBorder(BorderFactory.createTitledBorder("��������"));

		box_2.add(myMsgAreaScroll);
		centerPanel.add(box_2);

		// ******************���뷢����***********************
		JPanel centerLowerPanel = new JPanel(new BorderLayout());
		JPanel tempPanel1 = new JPanel(new BorderLayout());
		JPanel tempPanel2 = new JPanel(new FlowLayout(FlowLayout.LEFT));

		JLabel withWho = new JLabel("��");
		perponsComboBox = new JComboBox();
		perponsComboBox.addItem("������");
		privateTalk = new JCheckBox("˽��");

		inMsgField = new JTextArea(3, 2);
		inMsgField.setBorder(brd);
		inMsgField.setBackground(new Color(248, 243, 209));// ��������������ɫ
		inMsgField.addKeyListener(this);
		sentButton = new JButton("����");

		screenCapture = new JButton("����");
		inMsgFieldScroll = new JScrollPane(inMsgField);
		inMsgFieldScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		inMsgFieldScroll.setBorder(BorderFactory.createTitledBorder("�༭��"));
		tempPanel1.add(inMsgFieldScroll, BorderLayout.CENTER);

		sentButton.setBackground(Color.WHITE);
		tempPanel2.add(withWho);
		tempPanel2.add(new JLabel(" "));
		tempPanel2.add(perponsComboBox);
		tempPanel2.add(new JLabel(" "));
		tempPanel2.add(privateTalk);
		tempPanel2.add(new JLabel("   "));
		tempPanel2.add(screenCapture);
		tempPanel2.add(new JLabel("    "));
		tempPanel2.add(sentButton);
		tempPanel2.add(new JLabel(""));

		centerLowerPanel.add(tempPanel1, BorderLayout.CENTER);
		centerLowerPanel.add(tempPanel2, BorderLayout.SOUTH);

		centerPanel.add(centerLowerPanel, BorderLayout.SOUTH);

		// ********************** ����������(South)*****************//
		JLabel BordBottomLabel = new JLabel();
		Icon BordBottom = new ImageIcon("images\\BordBottom.gif");
		BordBottomLabel.setIcon(BordBottom);

		leftbox = Box.createVerticalBox();// ���ṹ
		leftbox.add(upperPanel, BorderLayout.NORTH);
		leftbox.add(centerPanel, BorderLayout.CENTER);
		leftbox.add(BordBottomLabel, BorderLayout.SOUTH);
		this.add(leftbox, BorderLayout.CENTER);
		this.add(rightbox, BorderLayout.EAST);

		sentButton.addActionListener(this);// �������Ͱ�ť

		sendfile.addActionListener(this);// �����ļ����Ͱ�ť/
		acceptfile.addActionListener(this);// �����ļ����հ�ť
		refusefile.addActionListener(this);// �����ļ��ܾ���ť
		cancelsendfile.addActionListener(this);// ����ȡ�������ļ�

		screenCapture.addActionListener(this);// ������������

		perponsComboBox.addActionListener(this);// ���������˵�////rfer

		privateTalk.addItemListener(this);// ������ѡ��״̬
		this.createPopupMenu();
		inMsgField.requestFocus();
		this.setLocation(450, 50); // ����
		this.setSize(600, 600);
		this.addWindowListener(new WindowAdapter()
		{ // ������ �������ڹر�ʱ��

					public void windowClosing(WindowEvent event)
					{
						shutDown();
					}
				});
		this.setVisible(true);
	}

	public void createPopupMenu() {
		JPopupMenu popup = new JPopupMenu();
		menuItem = new JMenuItem("���Ⱥ������Ϣ");
		menuItem.addActionListener(this);
		popup.add(menuItem); // ����һ���ʾ
		cMenuItem = new JMenuItem("���˽������Ϣ");
		cMenuItem.addActionListener(this);
		popup.add(cMenuItem);
		MouseListener popupListener = new PopupListener(popup);// PopupListener�̳�MouseAdapter
		commonArea.addMouseListener(popupListener);
		myMsgArea.addMouseListener(popupListener);
	}

	public void init(BufferedReader in, PrintWriter out) {
		// ������롢�����
		this.in = in;
		this.out = out;
		// ������Լ�������
		Thread th = new Thread(this);
		th.start();
	}

	public void run() {
		String inmsg;
		acceptthread = new acceptfilethread();
		while (true) {
			try {
				if ((inmsg = in.readLine()) != null) {
					// �Ѿ����������������ʾ���б���
					if (inmsg.startsWith("old")) {
						String[] userInfo = inmsg.split("&");
						plist.listModel.addElement(userInfo[1] + " ��" + userInfo[2] + "��"); // �����û��б�
					} else if (inmsg.startsWith("new")) { // ���յ�һ�η��������ͻ�ӭ��Ϣ
						String[] userInfo = inmsg.split("&");
						plist.listModel.addElement(userInfo[1] + " ��" + userInfo[2] + "��"); // �����û��б� new & zhangsan &boy
						insert(commonArea, userInfo[1] + "������");
						insert(myMsgArea, userInfo[1] + "������");
					} else if (inmsg != null) { // һ����Ϣ
						String[] sendfile = inmsg.split("&");
						if (sendfile[0].compareTo("cancelsendfile") == 0) {
							insert(myMsgArea, sendfile[1] + "ȡ�����ļ�����");
							acceptboolean = false;
							this.acceptfile.setEnabled(true);
							this.acceptfileArea.setText("");
							// this.acceptfileArea.s
						} else if (sendfile[0].compareTo("sendfile") == 0) {
							// ����Ǵ����ļ�����
							this.acceptfileArea.setText(sendfile[2]);
							insert(myMsgArea, sendfile[1] + " �����ļ�");
							filemsg = inmsg;
						} else if (sendfile[0].compareTo("acceptfile") == 0) {
							// ����Ǵ����ļ�����
							insert(myMsgArea, sendfile[1] + " �������㷢���ļ�");

						} else if (sendfile[0].compareTo("refusefile") == 0) {
							// ����Ǵ����ļ�����
							insert(myMsgArea, sendfile[1] + " �ܾ����㷢���ļ�");
							sendboolean = false;
							this.sendfile.setEnabled(true);
							this.sendfileArea.setText("");
						} else if (sendfile[0].compareTo("withWho") == 0) {
							if (sendfile[2].equals(myName)) {
								// ����Ƿ����Լ�����Ϣ
								insert(myMsgArea, sendfile[1] + "�ԡ�" + sendfile[2] + "��˵:" + sendfile[3]);
							} // ��ʾ���ҵ�Ƶ��
							insert(commonArea, sendfile[1] + "�ԡ�" + sendfile[2] + "��˵:" + sendfile[3]);

						} else if (inmsg.startsWith("privateTalk")) {
							String showmsg[] = inmsg.split("&");
							if (showmsg[1].equals(myName)) {
								// ������յ��������Լ����͵���Ϣ
								insert(commonArea, "���ԡ�" + showmsg[2] + "��˵: " + showmsg[3]);
							} else {
								// ���յ����Ǳ��˷����ҵ���Ϣ�����Ļ���
								insert(myMsgArea, "��" + showmsg[1] + "������˵: " + showmsg[3]);
							}
						} else {
							insert(commonArea, inmsg);
						}
					}
				}
			} catch (Exception ee) {
				ee.printStackTrace();
				insert(myMsgArea, "��������жϣ������µ�¼��");
				in = null;
				out = null;
				return;
			}
		}
	}

	// //////***********�����ļ��߳�********************************//
	class sendfilethread extends Thread {
		sendfilethread() {

		}

		public void run() {
			File file = new File(sendfilename);
			FileInputStream fos = null;
			try {
				fos = new FileInputStream(file);
			} catch (IOException e1) {
				System.out.print("�����ļ����ļ��쳣");
			}
			out.println("sendfile" + "&" + withWho + "&" + sendfileArea.getText() + "&" + IP + "&" + port + "&" + (int) file.length() / 1000);
			out.flush();

			// ����������������ܿͻ�����
			try {
				fileserver = new ServerSocket(port);
			} catch (IOException e1) {
				System.out.print("�����ļ����������������쳣");
			}
			try {
				sendfilesocket = fileserver.accept();
			} catch (IOException e1) {
				System.out.print("�����ļ����������쳣");
			}
			sendProgressbar.setMaximum((int) file.length() / 1000); // :
			sendProgressbar.setMinimum(0);

			// ����������������ṩ���ݰ�װ��
			int filetemp = 0;
			OutputStream netOut = null;
			OutputStream doc = null;
			try {
				netOut = sendfilesocket.getOutputStream();
				doc = new DataOutputStream(new BufferedOutputStream(netOut));
			} catch (IOException e1) {
				System.out.print("�����ļ�����������������ṩ���ݰ�װ���쳣");
			}
			byte[] buf = new byte[8000000];
			int num = -1;
			try {
				num = fos.read(buf);// ���ļ�
			} catch (IOException e1) {
				System.out.print("�����ļ����ļ��쳣");
			}

			while (num != (-1) && sendboolean)
			{// �Ƿ�����ļ�
				filetemp = filetemp + num / 1000;
				sendProgressbar.setValue(filetemp);
				try {
					doc.write(buf, 0, num);// ���ļ�
					doc.flush();
				} catch (IOException e1) {
					System.out.print("�����ļ����ļ�����д�����绺�����쳣");
				}
				try {
					num = fos.read(buf);// �������ļ��ж�ȡ����
				} catch (IOException e1) {
					System.out.print("�����ļ��������ļ��ж�ȡ�����쳣");
				}
			}
			if (num == (-1) && sendboolean) {
				insert(myMsgArea, "�ļ��������");
			} else {
				insert(myMsgArea, "�ļ������ж�");
			}
			sendProgressbar.setValue(0);
			try {
				fos.close();
				doc.close();
			} catch (IOException e1) {
				System.out.print("�����ļ��رն���д�쳣");
			}
			try {
				sendfilesocket.close();
				fileserver.close();
			} catch (IOException e1) {
				System.out.print("�����ļ��ر����ӻ�������쳣");
			}
			sendfileArea.setText("");
			sendfile.setEnabled(true);
			return;

		}
	}

	// //////////************�����ļ��߳�********************////
	public class acceptfilethread extends Thread {
		private String ip, filepath;
		int port;

		public void ipport(String ipp, int portt, String filepathh) {
			ip = ipp;
			port = portt;
			filepath = filepathh;
		}

		acceptfilethread() {

		}

		public void run() {
			File file = new File(filepath);// �ɼӵ�����
			RandomAccessFile raf = null;
			try {
				file.createNewFile();
				raf = new RandomAccessFile(file, "rw");
			} catch (IOException e1) {
				System.out.print("�����ļ��½��ļ������쳣");
			}

			try {
				acceptfilesocket = new Socket(ip, port);
			} catch (IOException e1) {
				System.out.print("�����ļ�ͨ��Socket�����ļ��������쳣");
			}

			String[] tem = filemsg.split("&");
			acceptProgressbar.setMaximum(Integer.parseInt(tem[5]));
			acceptProgressbar.setMinimum(0);
			InputStream netIn = null;
			InputStream in = null;
			try {
				netIn = acceptfilesocket.getInputStream();
				in = new DataInputStream(new BufferedInputStream(netIn));
			} catch (IOException e1) {
				System.out.print("�����ļ�����������������ܷ������ļ������쳣");
			}

			// ����������������������
			byte[] buf = new byte[8000000];
			int num = -1;
			try {
				num = in.read(buf);
			} catch (IOException e1) {
				System.out.print("�����ļ������������������������쳣");
			}
			int temleng = num / 1000;
			while (num != (-1) && acceptboolean) {
				temleng = temleng + num / 1000;
				acceptProgressbar.setValue(temleng);
				try {
					raf.write(buf, 0, num);// ������д���ļ�
					raf.skipBytes(num);// ˳��д�ļ��ֽ�
					num = in.read(buf);// �����������ж�ȡ�ļ�
				} catch (IOException e1) {
					System.out.print("�����ļ�������д���ļ�������������ж�ȡ�ļ��쳣");
				}
			}
			if (acceptboolean) {
				insert(myMsgArea, "�ļ��������");
			} else {
				insert(myMsgArea, "�ļ������ж�");
			}
			acceptProgressbar.setValue(0);
			try {
				in.close();
				raf.close();
			} catch (IOException e1) {
				System.out.print("�����ļ��رն�дʧ���쳣");
			}
			try {
				acceptfilesocket.close();
			} catch (IOException e1) {
				System.out.print("�����ļ��ر������쳣");
			}
			acceptfileArea.setText("");
			acceptfile.setEnabled(true);
			return;

		}
	}
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == acceptfile) {
			// �����ļ����䰴ť
			if (this.acceptfileArea.getText().length() == 0) {
				this.insert(myMsgArea, "û���ļ���Ҫ����");
			} else {
				String[] acpfile = filemsg.split("&");
				out.println("acceptfile" + "&" + acpfile[1]);
				out.flush();
				acceptthread = new acceptfilethread();
				acceptboolean = true;
				String filepath = "D:\\a.rar";
				JFileChooser jfc = new JFileChooser();
				int result = jfc.showSaveDialog(this);
				File file = null;
				if (result == JFileChooser.APPROVE_OPTION) {
					file = jfc.getSelectedFile();
				}
				filepath = file.getPath();
				// �ɼӵ�����ѡ�񱣴�·��hhhuy
				acceptthread.ipport(acpfile[3], (Integer.parseInt(acpfile[4])), filepath);
				acceptfile.setEnabled(false);// ��ֹ�ظ������
				acceptthread.start();// 
			}
		}
		if (e.getSource() == refusefile) {
			// �ܾ��ļ����䰴ť
			if (this.acceptfileArea.getText().length() == 0) {
				this.insert(myMsgArea, "û���ļ���Ҫ���գ��ܾ���Ч");
			} else {
				String[] acpfile = filemsg.split("&");
				out.println("refusefile" + "&" + acpfile[1]);
				out.flush();
				this.insert(myMsgArea, "��ȡ���� " + acpfile[1] + " �������ļ�");
				this.acceptfileArea.setText("");
				acceptboolean = false;
				acceptfile.setEnabled(true);
			}
		}
		if (e.getSource() == sendfile) {
			// �����ļ���ť
			if (withWho.endsWith("������")) {
				this.insert(myMsgArea, "��ѡ��Ҫ���͵�Ŀ��");
			} else {
				sendfilename = "";
				if (this.sendfileArea.getText().length() != 0) {
					sendfilename = this.sendfileArea.getText();
				}
				if (sendfilename.length() == 0) {
					// û�л����Ҫ���͵��ļ�
					this.insert(myMsgArea, "û���ļ����ͣ���ѡ�������͵��ļ���������������ļ���");
				} else{ // �����ļ�
					sendthread = new sendfilethread();
					sendboolean = true;
					sendfile.setEnabled(false);
					sendthread.start();
				}
			}
		}

		if (e.getSource() == cancelsendfile) {
			// ȡ���ļ����Ͱ�ť
			if (this.sendfileArea.getText().length() == 0) {
				this.insert(myMsgArea, "û���ļ����ͣ�����Ҫȡ��");
			} else {
				this.insert(myMsgArea, "��ȡ�����ļ�����");
				this.sendfileArea.setText("");
				if (this.sendboolean) {
					out.println("cancelsendfile" + "&" + withWho);
					out.flush();
					sendboolean = false;
				    sendfile.setEnabled(true);
				}
			}

		}

		if (e.getSource() == sentButton)
		{// ���������������Ϣ��ť�����
			try
			{
				mywords = inMsgField.getText();
				if ((mywords.trim()).length() != 0) {
					// ���ܷ��Ϳ���ϢҲ���ܶ����ո�
					if (withWho.equals("������")) {
						outmsg = mywords;
						// ���͵�������
						out.println(outmsg);
						out.flush();
						// ��ʾ���ҵ�Ƶ������
						insert(myMsgArea, myName + "��" + mywords);
					} else {
						// ��ĳ���˽�̸
						outmsg = "withWho" + "&" + "privateFalse" + "&" + withWho + "&" + mywords;
						if (privateTalkFlag) {
							outmsg = "withWho" + "&" + "privateTure" + "&" + withWho + "&" + mywords;
							insert(myMsgArea, "���ԡ�" + withWho + "��˵: " + mywords);
						} else {
							insert(myMsgArea, myName + " �ԡ�" + withWho + "��˵: " + mywords);
						}
						out.println(outmsg);
						out.flush();
					}
				}
			} catch (Exception ee) {
				ee.printStackTrace();
				insert(myMsgArea, "������������жϣ������µ�¼��");
			} finally {
				inMsgField.setText(""); // ��������
			}
		}
		if (e.getSource() == perponsComboBox) {
			// ��������������˵���Ϣ
			withWho = (String) perponsComboBox.getSelectedItem(); // ���ѡ�������
		}
		if (e.getSource() == menuItem) {
			// ����������һ����������Ƶ����ѡ��
			commonArea.setText("");
		}
		if (e.getSource() == cMenuItem) {
			// ����������һ����˽����Ƶ����ѡ��
			myMsgArea.setText("");
		}
		if (e.getSource() == screenCapture) {
			new CaptureScreen();
		}
	}

	public void itemStateChanged(ItemEvent e) {
		if (e.getSource() == privateTalk) {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				// ���ѡ��
				privateTalkFlag = true;
			} else {
				privateTalkFlag = false;
			}
		}
	}

	public void shutDown() {
		try {
			out.println("quit");
			out.flush();
		} catch (Exception ee) {
			ee.printStackTrace();
		} finally {
			System.exit(0);
		}
	}

	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == 10) {
			// �������س����������Ϣ
			try {
				mywords = inMsgField.getText();
				if ((mywords.trim()).length() != 0) {
					// ���ܷ��Ϳ���ϢҲ���ܶ����ո�
					if (withWho.equals("������")) {
						outmsg = mywords;
						// ���͵�������
						out.println(outmsg);
						out.flush();
						// ��ʾ���ҵ�Ƶ������
						insert(myMsgArea, myName + "��" + mywords);
					} else {
						// ��ĳ���˽�̸
						outmsg = "withWho" + "&" + "privateFalse" + "&" + withWho + "&" + mywords;
						if (privateTalkFlag) {
							outmsg = "withWho" + "&" + "privateTure" + "&" + withWho + "&" + mywords;
							insert(myMsgArea, "���ԡ�" + withWho + "��˵: " + mywords);
						} else {
							insert(myMsgArea, myName + " �ԡ�" + withWho + "��˵: "
									+ mywords);
						}
						out.println(outmsg);
						out.flush();
					}
				}
			} catch (Exception ee) {
				System.out.println(ee);
				insert(myMsgArea, "������������ж�,�����µ�¼��");
			} finally {
				inMsgField.setText("");// ��������
			}
		}
	}

	// ��Ϊ�ǳ����࣬���롰��д��������Щ���󷽷�
	public void keyReleased(KeyEvent e) {
	}

	public void keyTyped(KeyEvent e) {

	}

	// *********************���ý׶�**************************//

	/**
	 * ���ı�����JTextPane
	 * @param attrib
	 */
	private void insert(JTextPane j, String words)
	{
		int y, mi, d, h, m, s;
		Calendar cal = Calendar.getInstance();
		y = cal.get(Calendar.YEAR);
		mi = cal.get(Calendar.MONTH);
		d = cal.get(Calendar.DATE);
		h = cal.get(Calendar.HOUR_OF_DAY);
		m = cal.get(Calendar.MINUTE);
		s = cal.get(Calendar.SECOND);
		doc = j.getStyledDocument();
		try {
			// �����ı�
			doc.insertString(doc.getLength(), h + ":" + m + ":" + s + " " + words + "\n", getFontAttrib().getAttrSet());
			this.inMsgField.setText("");
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ��ȡ����Ҫ����������
	 * @return FontAttrib
	 */
	private FontAttrib getFontAttrib() {
		FontAttrib att = new FontAttrib();
		att.setText(inMsgField.getText());
		att.setName((String) fontName.getSelectedItem());
		att.setSize(Integer.parseInt((String) fontSize.getSelectedItem()));
		String temp_style = (String) fontStyle.getSelectedItem();
		if (temp_style.equals("����")) {
			att.setStyle(FontAttrib.GENERAL);
		} else if (temp_style.equals("����")) {
			att.setStyle(FontAttrib.BOLD);
		} else if (temp_style.equals("б��")) {
			att.setStyle(FontAttrib.ITALIC);
		} else if (temp_style.equals("��б��")) {
			att.setStyle(FontAttrib.BOLD_ITALIC);
		}
		String temp_color = (String) fontColor.getSelectedItem();
		if (temp_color.equals("��ɫ")) {
			att.setColor(new Color(0, 0, 0));
		} else if (temp_color.equals("��ɫ")) {
			att.setColor(new Color(255, 0, 0));
		} else if (temp_color.equals("��ɫ")) {
			att.setColor(new Color(0, 0, 255));
		} else if (temp_color.equals("��ɫ")) {
			att.setColor(new Color(255, 255, 0));
		} else if (temp_color.equals("��ɫ")) {
			att.setColor(new Color(0, 255, 0));
		}

		return att;
	}

	/**
	 * �����������
	 */
	private class FontAttrib
	{
		public static final int GENERAL = 0; // ����

		public static final int BOLD = 1; // ����

		public static final int ITALIC = 2; // б��

		public static final int BOLD_ITALIC = 3; // ��б��

		private SimpleAttributeSet attrSet = null; // ���Լ�

		private String text = null, name = null; // Ҫ������ı�����������

		private int style = 0, size = 0; // ��ʽ���ֺ�

		private Color color = null, backColor = null; // ������ɫ�ͱ�����ɫ

		/**
		 * һ���յĹ��죨�ɵ�������ʹ�ã�
		 */
		public FontAttrib() {
		}

		/**
		 * �������Լ�
		 * @return
		 */
		public SimpleAttributeSet getAttrSet() {
			attrSet = new SimpleAttributeSet();
			if (name != null)
				StyleConstants.setFontFamily(attrSet, name);
			if (style == FontAttrib.GENERAL) {
				StyleConstants.setBold(attrSet, false);
				StyleConstants.setItalic(attrSet, false);
			} else if (style == FontAttrib.BOLD) {
				StyleConstants.setBold(attrSet, true);
				StyleConstants.setItalic(attrSet, false);
			} else if (style == FontAttrib.ITALIC) {
				StyleConstants.setBold(attrSet, false);
				StyleConstants.setItalic(attrSet, true);
			} else if (style == FontAttrib.BOLD_ITALIC) {
				StyleConstants.setBold(attrSet, true);
				StyleConstants.setItalic(attrSet, true);
			}
			StyleConstants.setFontSize(attrSet, size);
			if (color != null)
				StyleConstants.setForeground(attrSet, color);
			if (backColor != null)
				StyleConstants.setBackground(attrSet, backColor);
			return attrSet;
		}

		/**
		 * �������Լ�
		 * @param attrSet
		 */
		public void setAttrSet(SimpleAttributeSet attrSet)
		{
			this.attrSet = attrSet;
		}
		public String getText()
		{
			return text;
		}
		public void setText(String text)
		{
			this.text = text;
		}
		public Color getColor()
		{
			return color;
		}
		public void setColor(Color color)
		{
			this.color = color;
		}
		public Color getBackColor()
		{
			return backColor;
		}
		public void setBackColor(Color backColor)
		{
			this.backColor = backColor;
		}
		public String getName()
		{
			return name;
		}
		public void setName(String name)
		{
			this.name = name;
		}
		public int getSize()
		{
			return size;
		}
		public void setSize(int size)
		{
			this.size = size;
		}
		public int getStyle()
		{
			return style;
		}
		public void setStyle(int style)
		{
			this.style = style;
		}
	}
	public static void main(String[] args) {
		new ChatFrame("�������");
	}
}
