package il.cshaifasweng.OCSFMediatorExample.server;

import java.io.IOException;

import static il.cshaifasweng.OCSFMediatorExample.server.SimpleServer.createDataBase;


public class SimpleChatServer
{
	
	private static SimpleServer server;
    public static void main( String[] args ) throws IOException
    {
        server = new SimpleServer(3000);

        createDataBase();
        // maybe call generateDataBase here,
        // check first if database already exists before generating (check return value of query?)

        System.out.println("server is listening");
        server.listen();
    }
}
