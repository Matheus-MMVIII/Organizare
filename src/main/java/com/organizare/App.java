package com.organizare;

import java.io.IOException;

import com.organizare.http.ApiServer;

public class App {

    public static void main(String[] args) throws IOException {
        ApiServer server = new ApiServer();
        server.start();
    }
}
