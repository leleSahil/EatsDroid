package com.company;

import com.company.server.Router;
import com.sun.net.httpserver.HttpServer;

import java.net.InetSocketAddress;

class Main {

    public static void main(String[] args) throws Exception {

        System.out.println("Starting server on port 8000...");
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/", new Router());
        server.setExecutor(null); // creates a default executor
        server.start();

    }

}
