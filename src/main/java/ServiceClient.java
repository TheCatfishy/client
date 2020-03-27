import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.*;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

public class ServiceClient {

    public static void main(String... argv) throws IOException, ExecutionException, InterruptedException {

        WebSocketClient webSocketClient = new StandardWebSocketClient();
        //
        // use right port/chat/<user> url
        //
        String client = "timclient";
        String url = "ws://localhost:11000/chat/" + client;
        ListenableFuture<WebSocketSession> futureStandard = webSocketClient.doHandshake(new AbstractWebSocketHandler() {
            @Override
            public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
                System.out.println(message.getPayload());

                            }
        }, url);
        WebSocketSession socketSession = futureStandard.get();
        //
        // use right json format content:<msg>
        //
        socketSession.sendMessage(new TextMessage("{\"content\":\"test from tim client\"}"));

        //
        // To do use spring app, of use some thread to hang in
        //
        Client restClient = ClientBuilder.newClient();
        WebTarget getEndpointsTarget
                = restClient.target("http://localhost:11000/api/game/endpoints");

        Invocation.Builder builder = getEndpointsTarget.request(MediaType.APPLICATION_JSON);
        {
            Response response = builder.get();
            String output = response.readEntity(String.class);

            System.out.println("users online:");
            System.out.println(output);
        }

        Scanner commandLineInputScanner = new Scanner(System.in);
        String command = "";
        while(command.compareTo("q")  != 0) {
            command = commandLineInputScanner.nextLine(); //Don't close immediately.
            if (command != "q"){
                //
                // To do handle all commands and chats etc
                //
                if (command.startsWith("chat ")) {
                    TextMessage msg = new TextMessage("{\"content\":\"" + command +"\"}");
                    socketSession.sendMessage(msg);
                } else if (command.startsWith("users")){

                    //Invocation.Builder builder = getEndpointsTarget.request();
                    Response response = builder.get();
                    String output = response.readEntity(String.class);
                    System.out.println(output);
                }
            }
        }
    }
}

////        WebSocketHttpHeaders headers = new WebSocketHttpHeaders();
////        WebSocketHandler result;
////        ListenableFuture<WebSocketSession> webSocketSession = webSocketClient
////                .doHandshake(result, headers, url).get();
//
//    WebSocketStompClient stompClient = new WebSocketStompClient(webSocketClient);
//        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
//                stompClient.setTaskScheduler(new ConcurrentTaskScheduler());
//
//                //String url = "ws://127.0.0.1:8080/hello";
//
//                StompSessionHandler sessionHandler = new MySessionHandler();
//                ListenableFuture<StompSession> future = stompClient.connect(url, sessionHandler);
//        future.addCallback(new SuccessCallback<StompSession>() {
//public void onSuccess(StompSession stompSession) {
//        System.out.println("on Success!");
//        }
//        }, new FailureCallback() {
//public void onFailure(Throwable throwable) {
//        System.out.println("on Failure!");
//        }
//        });