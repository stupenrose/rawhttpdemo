package mywebapp;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ServerSocketFactory;

public class WebFramework {

	
	static void serveHttp(Map<String, Response> files, int port) throws IOException {
		ServerSocketFactory ssf = ServerSocketFactory.getDefault();
		ServerSocket serverSocket = ssf.createServerSocket(port);
		
		while(true){
			System.out.println("Waiting for connection");
			Socket clientConnection = serverSocket.accept();
			
			System.out.println("Connection recieved!");
			
	
			InputStream bytesFromClient = clientConnection.getInputStream();
			BufferedReader linesFromClient = new BufferedReader(new InputStreamReader(bytesFromClient));
			
			String line = linesFromClient.readLine();
			System.out.println(line);
			String[] parts = line.split(" ");
			String method = parts[0];
			String path = parts[1];
			
			System.out.println("Received a '" + method + "' request for " + path);
	//		for(String line = linesFromClient.readLine(); line !=null; line=linesFromClient.readLine()){
	//
	//			System.out.println(line);
	//		}
			
	
			OutputStream bytesToClient = clientConnection.getOutputStream();
			
			Response response = files.get(path);
			if(response!=null){
				
				bytesToClient.write(("HTTP/1.1 " + response.responseCode + " " + response.responseReason + "\n").getBytes());
				for(String headerLine : response.header){
					bytesToClient.write((headerLine + "\n").getBytes());
				}
				bytesToClient.write("\n".getBytes());
				bytesToClient.write(response.content);
			}
			
			bytesToClient.flush();
			
			clientConnection.close();
			
			System.out.println("WROTE RESPONSE");
		}
	}
	public static class Response {
		final int responseCode;
		final String responseReason;
		final List<String> header;
		final byte[] content;
		
		public Response(int responseCode, String responseReason, List<String> header,
				byte[] content) {
			super();
			this.responseCode = responseCode;
			this.responseReason = responseReason;
			this.header = header;
			this.content = content;
		}
		public Response(int responseCode, String responseReason, String contentType,
				byte[] content) {
			super();
			this.responseCode = responseCode;
			this.responseReason = responseReason;
			this.header = new ArrayList<>();
			this.header.add("Content-Type: " + contentType);
			this.content = content;
		}
		
		
	}
}
