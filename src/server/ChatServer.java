package server;

import java.net.*;
import java.util.*;
import java.io.*;

public class ChatServer {
	static final int DEFAULT_PORT = 6000;
	static ServerSocket serverSocket;
	static Vector<Socket> connections;// ����
	static Vector<ClientProc> clients;

	/**
	 * ������Ϣ�����е���
	 * @param s
	 */
	public static void sendAll(String s) {
		if (connections != null) {
			for (Enumeration e = connections.elements(); e.hasMoreElements(); ) {
				try {
					PrintWriter pw = new PrintWriter(((Socket) e.nextElement()).getOutputStream());
					pw.println(s);
					pw.flush();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}
		System.out.println(s);
	}

	/**
	 * ������Ϣ��һ����
	 * @param name
	 * @param msg
	 * @return
	 */
	public static boolean sendOne(String name, String msg) {
		if (clients != null) {
			for (Enumeration e = clients.elements(); e.hasMoreElements();) {
				ClientProc cp = (ClientProc) e.nextElement();
				if ((cp.getName()).equals(name)) {
					try {
						PrintWriter pw = new PrintWriter((cp.getSocket()).getOutputStream());
						pw.println(msg);
						pw.flush();
						return true; // ����ֵΪ�棬�ҵ�������˿��Խ�������
					} catch (IOException ioe) {
						ioe.printStackTrace();
					}
				}
			}
		}
		return false;// û���ҵ�����ˣ�Ӧ���Ǵ����Ѿ��˳���������
	}

	/**
	 * ���һ������
	 * @param s
	 * @param cp
	 */
	public static void addConnection(Socket s, ClientProc cp) {
		if (connections == null) {
			connections = new Vector<Socket>();
		}
		connections.addElement(s);

		if (clients == null) {
			clients = new Vector<ClientProc>();
		}
		clients.addElement(cp);
	}

	/**
	 * �û��˳������ң�ɾ������
	 * @param s
	 * @param cp
	 * @throws IOException
	 */
	public static void deleteConnection(Socket s, ClientProc cp) throws IOException {
		if (connections != null) {
			connections.removeElement(s);
			s.close();
		}
		if (clients != null) {
			clients.removeElement(cp);
		}
	}

	/**
	 * ��ȡ�û�����Ϣ
	 * @return
	 */
	public static Vector getClients()
	{
		return clients;
	}

	/**
	 * �����������˿�
	 * @param arg
	 */
	public static void main(String[] arg) {
		int port = DEFAULT_PORT;
		try {
			serverSocket = new ServerSocket(port);
			System.out.println("�������Ѿ����������ڼ���...");
		} catch (IOException e) {
			System.out.println("�쳣");
			System.err.println(e);
			System.exit(1);
		}
		while (true) {
			try {
				Socket cs = serverSocket.accept();
				ClientProc cp = new ClientProc(cs); // ����һ���û��߳�
				Thread ct = new Thread(cp);
				ct.start();
				addConnection(cs, cp);
			} catch (IOException e) {
				System.err.println(e);
			}
		}
	}
}