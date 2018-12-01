import java.io.*;
import java.net.*;
import java.util.*;

class ClientHandler extends Thread{
	
	ClientHandler(){}
	
	ClientHandler handlers[];
	ServerSocket serverSocket;
	
	FileInputStream fis;
	BufferedInputStream bis;
	
	InputStream is;
	InputStreamReader isr;
	BufferedReader br;
	DataInputStream dIs;
			
    OutputStream os;
	OutputStreamWriter osw;
	BufferedWriter bw;
	ObjectOutputStream oos;
	
	File myFile;
	byte [] myByteArray;
	String FILE_TO_SEND;
	String FILE_TO_SAVE;
	String ip;
	
	String mode;
	int rLength;
	File folder;
	File[] fileList;
	String[] fileNames;
	
	public void run() {
		try {
			serverSocket = Server.returnServerSocket();
			Socket socket = serverSocket.accept();
			System.out.println("\n*User Logged In*\n");
			ip=(((InetSocketAddress) socket.getRemoteSocketAddress()).getAddress()).toString().replace("/","");
			os = socket.getOutputStream();
	        osw = new OutputStreamWriter(os);
	        bw = new BufferedWriter(osw);
	        
	        oos = new ObjectOutputStream(os);
	        
	        is = socket.getInputStream();
	        isr = new InputStreamReader(is);
	        br = new BufferedReader(isr);
	        
	        dIs = new DataInputStream(is);
	        
	        while(true){
	        	System.out.println("Listening... ");
	        	mode = br.readLine();
	        	folder = new File("Files");
	        	fileList = folder.listFiles();
	        	fileNames = new String[fileList.length];
	        	for(int i=0;i<fileList.length;i++) {
	        		fileNames[i] = fileList[i].getName();
	        	}
	        	if(mode.equalsIgnoreCase("read")){
			        System.out.println("Sending the list of current files to IP adress: " + ip);
			        oos.writeObject(fileNames);
			        os.flush();
			        System.out.println("Done.");
	        	}
	        	else if (mode.equalsIgnoreCase("send")) {
	        		System.out.println("File upload request detected!");
	        		FILE_TO_SAVE = br.readLine();
	        		System.out.println("File Name Recieved!");
	        		rLength = Integer.parseInt(br.readLine());
	        		myByteArray = new byte[rLength];
	        		System.out.println("Recieving " + FILE_TO_SAVE + "(" + myByteArray.length + " bytes) from IP adress: " + ip);
	        		dIs.readFully(myByteArray);
	        		System.out.println("File Succesfully Received;");
	        		try (FileOutputStream fos = new FileOutputStream("Files\\"+ FILE_TO_SAVE)) {
	        			fos.write(myByteArray);
	        			System.out.println("File Succesfully Saved to path: Files\\" + FILE_TO_SAVE);
	        		}catch(Exception e) {System.out.println("File Could Not Be Received");}
	        	}
	        	
	        	else {
	        		FILE_TO_SEND = br.readLine(); ;
		        	myFile = new File ("Files\\"+FILE_TO_SEND);
			        myByteArray = new byte [(int)myFile.length()];
			        
			        fis = new FileInputStream(myFile);
			        bis = new BufferedInputStream(fis);
			        
			        write(""+(int)myFile.length());
			        
			        bis.read(myByteArray,0,myByteArray.length);
			        System.out.println("Sending " + FILE_TO_SEND + "(" + myByteArray.length + " bytes) to IP adress: " + ip);
			        os.write(myByteArray,0,myByteArray.length);
			        os.flush();
			        System.out.println("File sent.");
	        	}
	        }
			
			
		}catch(Exception e){System.out.println("\n*User Logged Out* IP adress:" + ip +"\n");run();}
	}
	
	public void write(String mes){
		try{
			bw.write(mes);
			bw.newLine();
			bw.flush();
		}catch(Exception e){System.out.println("Error! Could not write to the server.");}
	}

}

public class Server {
	public static ServerSocket serverSocket;
	public static int numberOfThreadsToAllow;
	public static int i =0;
	public static ClientHandler handlers[];
	public static void main(String[] args) {
		String server_IP = "000.000.000.000";
		try {
	        InetAddress iAddress = InetAddress.getLocalHost();
	        server_IP = iAddress.getHostAddress();
	        System.out.println("Server IP address : " +server_IP);
	    } catch (UnknownHostException e) {System.out.println("Undefined Error");}
		Scanner console = new Scanner(System.in);
		System.out.print("Socket Number: ");
		int socketNumber = console.nextInt();
		System.out.print("Number Of Simultanious Connetions Allowed: ");
		numberOfThreadsToAllow = console.nextInt();
		System.out.println("--------------------------");
		System.out.println();
		try{
			System.out.println("Establishing Server Socket...");
			serverSocket = new ServerSocket(socketNumber);	
			System.out.println("Socket Established");
			System.out.println("Socket Number: " + socketNumber);
			System.out.println("Server IP address : " + server_IP);
			System.out.println("Number Of Simultanious Connetions Allowed: "+ numberOfThreadsToAllow);
			System.out.println("--------------------------");
			handlers = new ClientHandler[numberOfThreadsToAllow];

			while(i<numberOfThreadsToAllow){		
				ClientHandler handler = new ClientHandler();
				handler.start();
				handlers[i] = handler;
				i++;
			}
			
		}catch(Exception e){System.out.println("Socket Unavailable");console.close();}
	}
	
	public static ServerSocket returnServerSocket(){
		return serverSocket;
	}
	public static int returnI(){
		return i;
	}
	public static ClientHandler[] returnHandlerArray(){
		return handlers;
	}
}