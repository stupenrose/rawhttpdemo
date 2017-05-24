package mywebapp;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import mywebapp.WebFramework.Response;

/**
 * What's a network?
 *   "Long range, digital communication"
 *                     - Jacob Keller
 * 
 *  These going a long way: 01110010010010010001000 
 * 
 * Internet Protocol
 * 
 *   IP
 *   IP Address
 * 
 * Transmission Control Protocol
 *   - Guarantees that no packets will be lost
 *   - Guarantees that packets will be processed in the right order
 *   
 * TCP/IP
 * 
 * 
 *     [Z]
 *      |
 *  [A]--[010101]-[ROUTER]-[01010010]-[01010010]-[01010010]-[01010010]---[B]
 *      |             |
 *     [X]            _
 *                    0
 *                    1
 *                    0
 *                    0
 *                    1
 *                    1
 *                    _      
 *                    |   
 *                    |     
 *                   [C]
 * 
 *  _DESTINATION_ADDRESS_PACKET#_DATA_
 * [1010010101001010100100101010100101]
 * 
 * 
 *                NETWORK_MAGIC_HERE
 * [server] <---magic--{_HTML_}--magic---> [web-browser]
 * 
 */
public class MyWebApp {
	
	
	public static final void main(String[] args) throws IOException{
		
		// response code
		// content type
		// content
		
		Map<String, Response> files = new HashMap<>();
		files.put("/", 
				new Response(200, "OK", "text/html", (
				"<html>\n" + 
				"<head>\n" + 
				"  <title>An Example Page</title>\n" + 
				"</head>\n" + 
				"<body>\n" + 
				"  Hello World, this is a very simple HTML document.\n" + 
				"  " + 
				"  <a href=\"/secret\"><img width=100 src=\"/pic\"></img></a>\n" + 
				"</body>\n" + 
				"</html>").getBytes()));
		
		files.put("/secret",  
				new Response(200, "OK", "text/html", (
				"<html>\n" + 
				"<head>\n" + 
				"  <title>An Example Page</title>\n" + 
				"</head>\n" + 
				"<body>\n" + 
				"  Ssssh!.  Don't <b><i>tell</i></b> anyone.\n" + 
				"</body>\n" + 
				"</html>").getBytes()));
		
		files.put("/pic", new Response(303, "See Other", 
							Arrays.asList("Location: http://www.grit.com/-/media/Images/GRT/Editorial/Articles/Magazine-Articles/2012/02-01/Popular-Show--Pet-Rabbit-Breeds/Lop-Eared-Rabbit-Laying-Dow.jpg"), 
							"".getBytes()));
		
		files.put("/evil", new Response(200, "See Other", 
							"image/jpeg",
							resourceBytes("mywebapp/evil-rabbit.jpg")));
		
		WebFramework.serveHttp(files, 8080);
		
	}
	
	static byte[] resourceBytes(String key) throws IOException{

		return toBytes(MyWebApp.class.getClassLoader().getResourceAsStream(key));

	}
	
	
	static byte[] toBytes(InputStream in) throws IOException {
		ByteArrayOutputStream accumulator = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		
		for(int numRead = in.read(buffer); numRead != -1 ; numRead = in.read(buffer)){
			accumulator.write(buffer, 0, numRead);
		}
		
		return accumulator.toByteArray();
	}
	
}
