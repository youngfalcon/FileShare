import java.net.*;
import java.io.*;
import java.util.Scanner;
public class Client {
	public static String username;
	public static int serverSocket;
	public static Socket socket;
	public static String serverIP;
	public static String FILE_TO_SAVE;
	public static String FILE_TO_SEND;
	public static int rLength;
	
	public static OutputStream os;
	public static OutputStreamWriter osw;
	public static BufferedWriter bw;
	
	public static InputStream is;
	public static InputStreamReader isr;
	public static BufferedReader br;
	public static DataInputStream dIs;
	public static ObjectInputStream oIs;
	
	public static FileInputStream fis;
	public static BufferedInputStream bis;
	
	public static String[] fileNames;
	public static byte [] myByteArray;
	public static File myFile;
	
	public static Scanner console;
	
	public static void main(String[] args){
		
		try{
			console = new Scanner(System.in);
			System.out.println("Server IP address: ");
			serverIP = console.nextLine();
			System.out.println("Server Socket: ");
			serverSocket = console.nextInt();
			System.out.println("Connecting...");
			socket = new Socket(serverIP,serverSocket);
			socket.setTcpNoDelay(true);
			System.out.println("Connection successful");
        	System.out.println("--------------------------");
        	System.out.println();
        	
        	os = socket.getOutputStream();
	        osw = new OutputStreamWriter(os);
	        bw = new BufferedWriter(osw);
	        
        	is = socket.getInputStream();
	        isr = new InputStreamReader(is);
	        br = new BufferedReader(isr);
	        
	        dIs = new DataInputStream(is);
	        oIs = new ObjectInputStream(is);
	        while(true) {
	        	System.out.println("Mode: ");
		        String mode = console.next();
	        	if(mode.equalsIgnoreCase("read")) {
		        	write("read");
		        	fileNames = (String[]) oIs.readObject();
		        	for(int i = 0; i<fileNames.length; i++) {
		        		System.out.println(fileNames[i]);
		        	}
		        }
		        else if(mode.equalsIgnoreCase("send")) {
		        	write("send");
		        	System.out.println("FileToSend: ");
		        	FILE_TO_SEND = console.next();
		        	myFile = new File ("C:\\\\Send\\"+FILE_TO_SEND);
		        	System.out.println("Full file path:" + "C:\\\\Send\\"+FILE_TO_SEND);
		        	write(FILE_TO_SEND);
		        	System.out.println("File name sent to the server.");
		        	write(""+(int)myFile.length());
		        	System.out.println("File length sent to the server.");
		        	myByteArray = new byte [(int)myFile.length()];
		        	System.out.println("Array initialized.");

		        	fis = new FileInputStream(myFile);
			        bis = new BufferedInputStream(fis);
			        System.out.println("Systems ready.");
			        
		        	bis.read(myByteArray,0,myByteArray.length);
			        System.out.println("Sending " + FILE_TO_SEND + "(" + myByteArray.length + " bytes) to Server");
			        os.write(myByteArray,0,myByteArray.length);
			        os.flush();
			        System.out.println("File Sent.");
		        }
		        else {
		        	System.out.println("FileToSave: ");
		        	FILE_TO_SAVE = console.next();
		        	write("receive");
		        	write(FILE_TO_SAVE);
	        		rLength = Integer.parseInt(br.readLine());
	        		myByteArray = new byte[rLength];
	        		dIs.readFully(myByteArray);
	        		System.out.println("File Succesfully Received;");
	        		try (FileOutputStream fos = new FileOutputStream("C:\\\\Recieve\\"+ FILE_TO_SAVE)) {
	        			fos.write(myByteArray);
	        			System.out.println("File Succesfully Saved to path: C:\\\\\\\\Recieve\\\\" + FILE_TO_SAVE);
	        		}catch(Exception e) {System.out.println("File Could Not Be Received");}
		        }
	        }      
		}catch (Exception e){System.out.println("Error!"); System.out.println(e.getMessage());console.close();}
	}
	
	public static void write(String mes){
		try{
			bw.write(mes);
			bw.newLine();
			bw.flush();
		}catch(Exception e){}
	}

}