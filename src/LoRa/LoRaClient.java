package LoRa;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.CompletableFuture;

import SQL.SQLConnection;

public class LoRaClient implements WebSocket.Listener {

    private static SQLConnection conn;

    public LoRaClient(){
        HttpClient client = HttpClient.newHttpClient();
        CompletableFuture<WebSocket> ws = client.newWebSocketBuilder()
                .buildAsync(URI.create("wss://iotnet.teracom.dk/app?token=vnoSZwAAABFpb3RuZXQudGVyYWNvbS5kayi5FAHMm-UwpqqO3SN2YBc="), this);
    }

    //onOpen()
    public void onOpen(WebSocket webSocket) {
        // testing and demonstration

        Scanner scanner = new Scanner(System.in);



        // This WebSocket will invoke onText, onBinary, onPing, onPong or onClose methods on the associated listener (i.e. receive methods) up to n more times
        webSocket.request(1);
        System.out.println("WebSocket Listener has been opened for requests.");

        /*while(true){


            //connect to sql server
            try {
                conn = new SQLConnection();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try {
                conn.insertIntoDatabase(1, 590);  // 1 co2 / 2 humidity / 3 temperature
                conn.insertIntoDatabase(2, 35.4);
                conn.insertIntoDatabase(3, 23.6);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            // close sql connection
            conn.close();

            try {
                Thread.sleep(25000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }*/
    }

    //onError()
    public void onError(WebSocket webSocket, Throwable error) {
        System.out.println("A " + error.getCause() + " exception was thrown.");
        System.out.println("Message: " + error.getLocalizedMessage());
        webSocket.abort();
    }
    //onClose()
    public CompletionStage<?> onClose (WebSocket webSocket, int statusCode, String reason) {
        System.out.println("WebSocket closed!");
        System.out.println("Status:" + statusCode + " Reason: " + reason);
        return null; //new CompletableFuture().completedFuture("onClose() completed.").thenAccept(System.out::println);
    }
    //onPing()
    public CompletionStage<?> onPing (WebSocket webSocket, ByteBuffer message) {
        webSocket.request(1);
        System.out.println("Ping: Client ---> Server");
        System.out.println(message.asCharBuffer().toString());
        return null; // new CompletableFuture().completedFuture("Ping completed.").thenAccept(System.out::println);
    }
    //onPong()
    public CompletionStage<?> onPong (WebSocket webSocket, ByteBuffer message) {
        webSocket.request(1);
        System.out.println("Pong: Client ---> Server");
        System.out.println(message.asCharBuffer().toString());
        return null; // new CompletableFuture().completedFuture("Pong completed.").thenAccept(System.out::println);
    }
    //onText()
    public CompletionStage<?> onText (WebSocket webSocket, CharSequence data, boolean last) {
        System.out.println(data);

        //saving a backup
        try {
            Files.write(Paths.get("backup.txt"), (data.toString()+"\n").getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //connect to sql server
        try {
            conn = new SQLConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        JSON json = new JSON(); // not an actual json object, just our class named that way

        String[] decodedData = json.getDataFromJSON(data.toString()); // 0 = humidity 1 = temp 2 = co2

        try {
            conn.insertIntoDatabase(1, Double.parseDouble(decodedData[2]));  // 1 co2 / 2 humidity / 3 temperature
            conn.insertIntoDatabase(2, Double.parseDouble(decodedData[0]));
            conn.insertIntoDatabase(3, Double.parseDouble(decodedData[1]));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // close sql connection
        conn.close();

        webSocket.request(1);
        return null; // new CompletableFuture().completedFuture("onText() completed.").thenAccept(System.out::println);
    };
}