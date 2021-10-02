import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;


public class cli2{
	public static void main(String[] args) throws UnknownHostException, IOException{
		
		String serverIP = "192.168.4.101";		//放自己的IP
		//String serverIP = "192.168.2.60";
		//String serverIP = "172.20.10.2";
		//String serverIP = "192.168.42.219";			//我的手機
		int serverPort = 7070;
		
		new Thread(new ClientThread(serverIP,serverPort)).start();
	}

}

class ClientThread implements Runnable{
	
	private String clientIP = null;
	private int clientPort;
	Socket clientSocket;
	private BufferedReader br = null;
	private OutputStream os = null;
	//private InputStream is = null;
	
	public ClientThread(String serverIP , int serverPort) throws UnknownHostException, IOException {
		clientIP = serverIP;
		clientPort = serverPort;
		clientSocket = new Socket(clientIP,clientPort);
		br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(),"UTF-8"));
		os = clientSocket.getOutputStream();
	}
	
	public void run() {
		try {	
			while(true) {
				System.out.println("請輸入字串");
				Scanner sc = new Scanner(System.in);
				String content = sc.next();
				os.write((content+"\n").getBytes("UTF-8"));
			}
		}catch (IOException ex) {
			String error = ex.toString();
			System.out.println(error);
		}
	}
}