package chat;

import Tools.find;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientLogin extends JFrame{
	private static final long serialVersionUID = 1L;
	//�������ֹ���
	private int screenHeight = (int)this.getToolkit().getScreenSize().getHeight();	//��Ļ�ĸ߶�
	private int screenWidth = (int)this.getToolkit().getScreenSize().getWidth();	//��Ļ�Ŀ��
	private int width = 400;	//���屾���ڵĿ��
	private int height = 350;	//���屾���ڵĸ߶�
	
	private ImageIcon LoginLogo = null;		//����ͼ��
	private JLabel showLogoLabel = null;	//����ͼ���ǩ
	private JLabel userLabel = null;		//�����û���������ǩ
	private JLabel psdLabel = null;			//��������������ǩ
	private JTextField usernameText =null;	//������ͨ�ı������
	private JPasswordField pwdText = null;	//���������ı������
	private JButton loginBut = null;		//���õ�½��ť
	private JButton regBut = null;			//����ע�ᰴť

	private String hostName = "localhost";
	private int port = 6000;
	private Socket client = null;
	private BufferedReader in = null;
	private PrintWriter out = null;

	private String sex = null;				//����û���Ů��Ϣ

	public ClientLogin(int _width, int _height) {
		width = _width;
		height = _height;
		init();
		addComponent();
		addListener();
		showFrame();
	}
	
	/*
	 * init������ʼ�����
	 */
	private void init() {
//		JFrameĬ�ϲ�����BoardLayout���߿򲼾֣����������ֻ��һ��������Ҳ�ָ��λ�ã�Ĭ�Ϸ����������м�ռ������
//		��ʼ��ͼƬ
		LoginLogo = new ImageIcon("Imagics/login.jpg");
//		��ʼ��JLabel ���Ұ�ͼƬ�ŵ�Label��
		showLogoLabel = new JLabel(LoginLogo, JLabel.CENTER);
		userLabel = new JLabel("�û���");
		psdLabel = new JLabel("�ܡ���");
		usernameText = new JTextField();
		pwdText = new JPasswordField();
		loginBut = new JButton("��½");
		regBut = new JButton("ע��");
	}
	
	/*
	 * �Ѹ�������ڴ˷����н�����װ
	 */
	private void addComponent() {
		this.setLayout(null);
		this.add(showLogoLabel);		//���ͼ�����
		showLogoLabel.setBounds(0, 0, width, (int)(0.2*height+1));
//		setBounds�����Ĺ��ܣ�ǰ��������ֱ�Ϊ�������е�����λ��
//		setBounds�����Ĺ��ܣ�����Ŀ�Ⱥ͸߶�
		
		this.add(userLabel);
		userLabel.setBounds(25, (int)(0.3*height+1), 100, 25);
		this.add(usernameText);
		usernameText.setBounds(80, (int)(0.3*height+1), 200, 25);
		
		this.add(psdLabel);
		psdLabel.setBounds(25, (int)(0.4*height+1), 100, 25);
		this.add(pwdText);
		pwdText.setBounds(80, (int)(0.4*height+1), 200, 25);
		
		this.add(loginBut);
		loginBut.setBounds((width-400)/2+25, (int)(0.6*height+1), 100, 25);
		
		this.add(regBut);
		regBut.setBounds((width-400)/2+150, (int)(0.6*height+1), 100, 25);
	}
	
	/*
	 * ��Ӽ������¼�����������ӹ���
	 */
	private void addListener() {
		//�رմ���
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//����½��ť����¼�
		loginBut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean b = find.checkLogin(usernameText.getText(), pwdText.getText());
				//�������Ӷ��û�����������ж�
				if (b == true) {
					sex = find.getSex(usernameText.getText());
					this.link();
					ChatFrame app = new ChatFrame(usernameText.getText());
					app.init(in, out);
					//���д��ڵĹر�
					loginBut.setEnabled(false); // ȷ�����ᱻ�ٴε��
					ClientLogin.this.dispose();
				} else {
					//������Ϣ�Ի���
					JOptionPane.showMessageDialog(null, "�û��������������");
				}
			}

			private void link(){
				try {
					client = new Socket(hostName, port);
					in = new BufferedReader(new InputStreamReader(client.getInputStream()));
					out = new PrintWriter(client.getOutputStream());
					out.println(usernameText.getText() + "&" + sex);
					out.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}

		});
		//��ע�ᰴť����¼�
		regBut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ClientLogin.this.dispose();
				new Register(400, 350);
			}
		});
	}
	
	/*
	 * �ô�����ʾ����
	 */
	private void showFrame() {
		//���ڴ��м䵯��:˼·
		//�õ���Ļ�Ŀ�Ⱥ͸߶�
		//�õ������ڵĸ߶ȺͿ��
		this.setLocation((screenWidth-width)/2, (screenHeight-height)/2);
		this.setSize(width, height);
		this.setVisible(true);
	}

	public static void main(String args[]) {
		new ClientLogin(400, 350);
	}
}
/*
 * sun��˾�Զ������� 	Swing
 * ϵͳ�Զ�������		AWT
 * */