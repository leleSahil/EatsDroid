package com.company.server;

import com.company.sockets.EatsSocketServer;
import com.sun.net.httpserver.HttpServer;
import org.joda.time.DateTime;

import java.net.InetSocketAddress;

class Main {

    public static void main(String[] args) throws Exception {

//        System.out.println(new AuthorizeController().getUserFromToken("ya29..xwIHVcMPO2UMDlB2Xeju40eBl6rwEz1UyUZvzlLYpWA4qad8vUm5OaM-dAGLFTsZ4g"));

        java.util.Date date = new DateTime( "2016-04-13T00:00:00Z" ).toDate();
        System.out.println(date);
        if(!true){
            return;
        }
        System.out.println("Starting socket server on port 8001...");
        EatsSocketServer.startServer();


        System.out.println("Starting server on port 8000...");
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/", new Router());
        server.setExecutor(null); // creates a default executor
        server.start();

    }

}
