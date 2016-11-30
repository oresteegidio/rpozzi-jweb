package com.rp.socket.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLEncoder;

public class SimpleSocketClientExample {
	
	public static void main(String[] args) {
		if (args.length < 3) {
			System.out.println("Usage: SimpleSocketClientExample <server> <port> <path>");
			System.exit(0);
		}
		Socket client = null;
		PrintStream out = null;
		BufferedReader in = null;
		try {
			//sitlinkserver.eu-gb.mybluemix.net
			//nodesockettest.eu-gb.mybluemix.net
			String server = args[0];
			int port = new Integer(args[1]).intValue();
			String path = args[2];
			System.out.println("Sending content over socket on : " + server + ":" + port);
			
			// Connect to the server
			client = new Socket(server, port);

			// Create input and output streams to read from and write to the server
			out = new PrintStream(client.getOutputStream());
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));

			//String msg = _GET(path, server);
			//String msg = _POST(path, server);
			String msg = _rawSocket();
			System.out.println("Message to be sent : ");
			System.out.println(msg);
			out.println(msg);
			
			// Read data from the server until we finish reading the document
			System.out.println("Message received : ");
			String line = in.readLine();
			while (line != null) {
				System.out.println(line);
				line = in.readLine();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// Close our streams
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (out != null) {
				out.close();
			}
			if (client != null) {
				try {
					client.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private static String _GET(String path, String server) {
		System.out.println("##################### HTTP GET Method - START #####################");
		// Follow the HTTP protocol of GET <path> HTTP/1.1 followed by an empty line
		StringBuffer buffer = new StringBuffer();
		buffer.append("GET " + path + " HTTP/1.1\r\n");
		buffer.append("Host: " + server + "\r\n");
		String msg = buffer.toString();
		System.out.println("##################### HTTP GET Method - END #####################");
		return msg;
	}
	
	private static String _POST(String path, String server) {
		String msg = null;
		try {
			System.out.println("==================== HTTP POST Method - START ====================");
			// Follow the HTTP protocol of POST <path> HTTP/1.1 followed by an empty line
			StringBuffer buffer = new StringBuffer();
			buffer.append("POST " + path + " HTTP/1.1\r\n");
			buffer.append("Host: " + server + "\r\n");
			buffer.append("Content-Type: application/x-www-form-urlencode\r\n");
			buffer.append("Content-Length: 15\r\n\r\n");
			String data = URLEncoder.encode("key", "UTF-8") + "=" + URLEncoder.encode("value", "UTF-8");
			//buffer.append(data);
			buffer.append("111111pacchetto\r\n\r\n");
			msg = buffer.toString();
			System.out.println("==================== HTTP POST Method - END ====================");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return msg;
	}
	
	private static String _rawSocket() {
		String msg = null;
		System.out.println("==================== TCP - START ====================");
		StringBuffer buffer = new StringBuffer();
		buffer.append("This is my message over TCP socket\r\n");
		msg = buffer.toString();
		System.out.println("==================== TCP - END ====================");
		return msg;
	}
}