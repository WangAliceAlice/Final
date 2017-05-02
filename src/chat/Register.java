package chat;

import Tools.find;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by zhuch on 2017/4/30.
 */
public class Register extends JFrame{
        private static final long serialVersionUID = 1L;
        //�������ֹ���
        private int screenHeight = (int)this.getToolkit().getScreenSize().getHeight();	//��Ļ�ĸ߶�
        private int screenWidth = (int)this.getToolkit().getScreenSize().getWidth();	//��Ļ�Ŀ��
        private int width = 400;	//���屾���ڵĿ��
        private int height = 350;	//���屾���ڵĸ߶�

        private ImageIcon LoginLogo = null;		//����ͼ��
        private JLabel showLogoLabel = null;	//����ͼ���ǩ
        private JLabel userLabel = null;		//�����û���������ǩ
        private JLabel sexLabel = null;         //�����Ա��ǩ
        private ButtonGroup sexGroup = null;    //�����Ա���
        private JRadioButton boyRadio = null, girlRadio = null, secretRadio = null; //�����Ա�ѡ���
        private JLabel psdLabel = null;			//��������������ǩ
        private JLabel repsdLabel = null;                // �����ظ����������ǩ
        private JTextField usernameText =null;	//������ͨ�ı������
        private JPasswordField pwdText = null;	//���������ı������
        private JPasswordField repsdText = null;    //�����ظ������ı������
        private JButton regBut = null;			//����ע�ᰴť
        private JButton reSetBut = null;          // �������ð�ť

        private String sex = null;				//����û���Ů��Ϣ

        private String username = null;
        private String pass = null;
        private String repass = null;

        public Register(int _width, int _height) {
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
            userLabel = new JLabel("�� �� ��");
            sexLabel = new JLabel("�ԡ�����");
            psdLabel = new JLabel("�ܡ�����");
            repsdLabel = new JLabel("�ٴ�����");

            sexGroup = new ButtonGroup();
            boyRadio = new JRadioButton("����");
            boyRadio.setActionCommand("boy");
            girlRadio = new JRadioButton("Ů��");
            girlRadio.setActionCommand("girl");
            secretRadio = new JRadioButton("����");
            secretRadio.setActionCommand("unknown");

            sexGroup.add(boyRadio);
            sexGroup.add(girlRadio);
            sexGroup.add(secretRadio);

            usernameText = new JTextField();
            pwdText = new JPasswordField();
            repsdText = new JPasswordField();
            reSetBut = new JButton("����");
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

            this.add(sexLabel);
            sexLabel.setBounds(25, (int)(0.4*height+1), 100, 25);
            this.add(boyRadio);
            boyRadio.setBounds(80, (int)(0.4*height+1), 60, 25 );
            this.add(girlRadio);
            girlRadio.setBounds(140, (int)(0.4*height+1), 60, 25 );
            this.add(secretRadio);
            secretRadio.setBounds(200, (int)(0.4*height+1), 60, 25 );
            // Ĭ��ѡ��ֵ
            secretRadio.setSelected(true);

            this.add(psdLabel);
            psdLabel.setBounds(25, (int)(0.5*height+1), 100, 25);
            this.add(pwdText);
            pwdText.setBounds(80, (int)(0.5*height+1), 200, 25);

            this.add(repsdLabel);
            repsdLabel.setBounds(25, (int)(0.6*height+1), 100, 25);
            this.add(repsdText);
            repsdText.setBounds(80, (int)(0.6*height+1), 200, 25);

            this.add(regBut);
            regBut.setBounds((width-400)/2+25, (int)(0.7*height+1), 100, 25);

            this.add(reSetBut);
            reSetBut.setBounds((width-400)/2+150, (int)(0.7*height+1), 100, 25);
        }

        /*
         * ��Ӽ������¼�����������ӹ���
         */
        private void addListener() {
            //�رմ���
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            //����½��ť����¼�
            regBut.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (usernameText.getText().equals("")) {
                        JOptionPane.showMessageDialog(null, "�û�������Ϊ�գ�");
                    } else if (pwdText.getText().equals("")){
                        JOptionPane.showMessageDialog(null, "���벻��Ϊ�գ�");
                    } else if (!pwdText.getText().equals(repsdText.getText())) {
                        JOptionPane.showMessageDialog(null, "�����������벻һ�£�");
                    } else {
                        boolean isCanDo = find.checkItems(usernameText.getText(),sexGroup.getSelection().getActionCommand() ,pwdText.getText());
                        if (isCanDo) {
                            boolean after = find.addUser(usernameText.getText(),sexGroup.getSelection().getActionCommand() ,pwdText.getText());
                            if (after) {
                                // ע��ɹ�����ת����¼����
                                Register.this.dispose();
                                new ClientLogin(400, 350);
                            } else {
                                // ע��ʧ��
                                JOptionPane.showMessageDialog(null, "ע��ʧ�ܣ�");
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "���û����Ѿ�ע�ᣡ");
                        }
                    }
                }
            });
            reSetBut.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    usernameText.setText("");
                    pwdText.setText("");
                    repsdText.setText("");
                    secretRadio.setSelected(true);
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
}
