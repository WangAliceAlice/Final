package chat;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class PList extends JFrame implements ListSelectionListener, ActionListener {
	private JList peopleList; // ��ʾ���������ҵ�������
	private JButton refurbishButton;// ˢ���б�ť
	public DefaultListModel listModel;// �û��б�
	public ChatFrame chf;

	public PList(ChatFrame sup) {
		super(sup.myName);

		try { // ʹ��Windows�Ľ�����
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}

		chf = sup;

		JPanel eastPanel = new JPanel(new BorderLayout());
		JLabel title = new JLabel("�ҵ����ߺ���");
		listModel = new DefaultListModel();// ʵ�� java.util.Vector API �ڷ�������ʱ֪ͨ
											// ListDataListener
		peopleList = new JList(listModel);
		peopleList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);// ���õ�ѡ���Ƕ�ѡ
		JScrollPane ListScrollPane = new JScrollPane(peopleList);
		ListScrollPane.setPreferredSize(new Dimension(150, 400));

		refurbishButton = new JButton("ˢ���б�");
		refurbishButton.addActionListener(this);// ����ˢ�°�ť
		peopleList.addListSelectionListener(this);
		
		eastPanel.add(title, BorderLayout.NORTH);
		eastPanel.add(ListScrollPane, BorderLayout.CENTER);
		eastPanel.add(refurbishButton, BorderLayout.SOUTH);
		this.add(eastPanel);
		this.setVisible(true);
		this.setSize(new Dimension(180, 600));
		this.setLocation(270, 50);
	}

	public void valueChanged(ListSelectionEvent e) {// ���������б����ʾ����
		if (e.getSource() == peopleList) {
			try {
				String select = (String) peopleList.getSelectedValue();
				if (select != null) { // ȷ��ѡ��ǿ�
					String[] userInfo = select.split("��");
					String name = userInfo[0].trim(); // ��ȡ����

					if (!name.equals(chf.myName)) { // ��������Լ�
						int count = chf.perponsComboBox.getItemCount();
						for (int i = 0; i < count; i++) {
							chf.perponsComboBox.setSelectedIndex(i);// ѡ���i��
							String strName = (String) chf.perponsComboBox
									.getSelectedItem();
							if (name.equals(strName)) {
								// ����Ѿ����б��У��͸��Ͽ���ѡ�У�����Ҫȷ��û������
								return;
							}
						}
						// ���û����ӣ��ͻ�ִ��������� ���
						chf.perponsComboBox.addItem(name);
					}
				}
			} catch (Exception ee) {
				System.out.println("�������� ��valueChanged " + ee);
			}
		}
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == refurbishButton) {
			// ���������Ҫˢ���б�
			try {
				listModel.clear(); // ����б�
				chf.out.println("refurbish"); // ����ˢ�����󵽷�����
				chf.out.flush();
			} catch (Exception ee) {

			}
		}
	}

}
