import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class Server {
	
	private static int ServerPort = 7070;
	static ServerSocket server ;
	
	public static ArrayList<Socket> clients = new ArrayList<>();  //儲存clients socket的ArrayList
	private static int count = 0;
	
	public static void main(String[] args) throws IOException {
		System.out.println("Local Host : "+InetAddress.getLocalHost().getHostAddress());
		System.out.println("啟動伺服器");
		server = new ServerSocket(ServerPort);		//建立server
		
		while(true) {								//不斷監聽有無client進入
			try {
				Socket socket = server.accept();
				clients.add(socket);				//將socket丟入ArrayList
				count++;
				System.out.println("Client"+count+"連線成功");
				//System.out.println("執行Client"+count+"的多執行緒");
				new Thread(new ServerThread(socket,count)).start();		//執行多執行緒 第49行
				
			} catch (SocketException e) {
				e.printStackTrace();
				String error = e.toString();
				System.out.println(error);
			}
		}
	}
	
}

class ServerThread extends Thread implements Runnable {
	
	public Socket ClientSocket = null;
	//private InetAddress ClientAddress = null;
	public int ClientCount = 0;
	private BufferedReader br = null;		//讀取進來的東西
	private OutputStream os = null;			//把東西寫出去
	private BufferedWriter bw = null;
	
	
	public ServerThread(Socket socket , int count) throws IOException ,UnsupportedEncodingException{
		ClientSocket = socket;
		ClientCount = ClientSocket.getPort();
		br = new BufferedReader(new InputStreamReader(ClientSocket.getInputStream(),"UTF-8"));
		os = ClientSocket.getOutputStream();
		System.out.println(ClientCount);
		//ClientAddress = ClientSocket.getInetAddress();
		//System.out.println(ClientAddress.toString());
	}
	
	@Override
	public void run() {
		try {
			String content = null;
			while( (content=br.readLine()) != null) {		
				System.out.println("[Server收到訊息]\n"+ClientCount+"說 : "+content);
				//Server發送訊息
				for (Iterator<Socket> it = Server.clients.iterator() ; it.hasNext() ; ) {
					Socket s = it.next();
					if(s.getPort()==ClientCount)
						continue;
					try {
						os = s.getOutputStream();
						os.write((content+"\n").getBytes("UTF-8"));
						//System.out.println("訊息已送給"+ClientCount+content);
					} catch (SocketException e) {
						e.printStackTrace();
						it.remove();
					}
				}
				content = null;
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Server.clients.remove(ClientSocket);
			System.out.println("Client"+ClientCount+"已斷線");
		}
	}
	
}

