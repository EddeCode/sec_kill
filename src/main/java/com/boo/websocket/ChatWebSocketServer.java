package com.boo.websocket;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.util.MinimalPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Java 原生的websocket解决方案
 * @author song
 * @date 2022/4/24 10:51
 */
@Slf4j
// @Service
// @ServerEndpoint("/ws/chat/{userId}")
public class ChatWebSocketServer {

    public static int onlineCount = 0;
    public static Map<String, ChatWebSocketServer> chatWebSocketServerMap =
            new ConcurrentHashMap<>();
    private Session session;
    private String userId;

    private static ObjectMapper objectMapper;

    /**
     * 加载类时只会被加载一次
     */
    static {
        ChatWebSocketServer.objectMapper = new ObjectMapper();
        ChatWebSocketServer.objectMapper.setDefaultPrettyPrinter(new MinimalPrettyPrinter());
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId) {
        log.info("getRequestParameterMap<{}>",session.getRequestParameterMap());
        log.info("getUserProperties<{}>",session.getUserProperties());
        log.info("getNegotiatedExtensions<{}>",session.getNegotiatedExtensions());
        log.info("getNegotiatedSubprotocol<{}>",session.getNegotiatedSubprotocol());
        this.session = session;
        this.userId = userId;
        if (chatWebSocketServerMap.containsKey(userId)) {
            chatWebSocketServerMap.remove(userId);
            chatWebSocketServerMap.put(userId, this);
        } else {
            chatWebSocketServerMap.put(userId, this);
            addOnlineCount();
        }
        send("登录成功");
        log.info("userId<{}>,登录", this.userId);
    }

    @OnClose
    public void onClose() {
        if (chatWebSocketServerMap.containsKey(userId)) {
            chatWebSocketServerMap.remove(userId);
            subOnlineCount();
        }
        log.info("userId<{}>,退出", this.userId);
    }

    @OnMessage
    public void OnMessage(Session session, String message) {
        HashMap<String, Object> map = new HashMap<>();
        log.info("message<{}>",message);
        map.put("userId", this.userId);
        map.put("msg", message);
        sendAll(map);
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.error("用户错误:" + this.userId + ",原因:" + error.getMessage());
        error.printStackTrace();
    }

    public void send(String msg) {
        try {
            this.session.getBasicRemote().sendText(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void send(Object data) {
        try {
            this.session.getBasicRemote().sendText(objectMapper.writeValueAsString(data));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static synchronized void addOnlineCount() {
        onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        onlineCount--;
    }

    public static void sendAll(Object obj) {
        chatWebSocketServerMap.forEach((userId, ws) -> {
            ws.send(obj);
        });
    }
}
