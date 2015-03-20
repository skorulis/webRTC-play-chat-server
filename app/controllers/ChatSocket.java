package controllers;

import model.ChatControlMessage;
import model.ICECandidateModel;
import model.SDPModel;
import play.libs.F;
import play.mvc.WebSocket;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alex on 19/03/15.
 */
public class ChatSocket extends WebSocket<String> {

    private final ChatSocketManager chatManager;
    public final List<ICECandidateModel> waitingCandidates;
    private ChatSocket chattingWith;
    public SDPModel sdp;

    private WebSocket.In<String> in;
    private WebSocket.Out<String> out;
    private boolean hasDisconnected;

    public ChatSocket(ChatSocketManager manager) {
        this.chatManager = manager;
        waitingCandidates = new ArrayList<>();
    }

    public void onReady(final WebSocket.In<String> in, final WebSocket.Out<String> out) {
        this.in = in;
        this.out = out;
        in.onMessage(new F.Callback<String>() {
            @Override
            public void invoke(String s) throws Throwable {
                ChatControlMessage control = chatManager.gson.fromJson(s, ChatControlMessage.class);
                if(control.isChatRequest()) {
                    handleChatRequest();
                } else if(control.isChatOffer()) {
                    SDPModel sdp = chatManager.gson.fromJson(control.payload, SDPModel.class);
                    handleChatOffer(sdp);
                } else if(control.isChatAnswer()) {
                    SDPModel sdp = chatManager.gson.fromJson(control.payload, SDPModel.class);
                    handleChatAnswer(sdp);
                } else if(control.isIceCandidate()) {
                    ICECandidateModel ice = chatManager.gson.fromJson(control.payload, ICECandidateModel.class);
                    handleIceCandidate(ice);
                } else if(control.isDisconnect()) {
                    handleDisconnect();
                }
            }
        });

        in.onClose(new F.Callback0() {
            @Override
            public void invoke() throws Throwable {
                hasDisconnected = true;
                handleDisconnect();
            }
        });
    }

    private void sendMessage(ChatControlMessage message) {
        if(hasDisconnected) {
            return;
        }
        String json = chatManager.gson.toJson(message);
        out.write(json);
    }

    private void handleChatRequest() {
        ChatSocket other = chatManager.findOffer();
        ChatControlMessage message = new ChatControlMessage();
        if(other != null) {
            System.out.println("Found available connection");
            message.type = ChatControlMessage.CCT_CHAT_OFFER;
            message.payload = chatManager.gson.toJson(other.sdp);
            sendMessage(message);
            other.chattingWith = this;
            chattingWith = other;
            for(ICECandidateModel ice : other.waitingCandidates) {
                ChatControlMessage iceMessage = new ChatControlMessage();
                iceMessage.type = ChatControlMessage.CCT_ICE_CANDIDATE;
                iceMessage.payload = chatManager.gson.toJson(ice);
                sendMessage(iceMessage);
            }
        } else {
            message.type = ChatControlMessage.CCT_CHAT_INIT;
            sendMessage(message);
        }

    }

    private void handleChatOffer(SDPModel offer) {
        sdp = offer;
        chatManager.addOffer(this);
    }

    private void handleChatAnswer(SDPModel answer) {
        sdp = answer;
        ChatControlMessage message = new ChatControlMessage();
        message.type = ChatControlMessage.CCT_CHAT_ANSWER;
        message.payload = chatManager.gson.toJson(answer);
        chattingWith.sendMessage(message);
    }

    private void handleIceCandidate(ICECandidateModel ice) {
        if(chattingWith != null) {
            ChatControlMessage message = new ChatControlMessage();
            message.type = ChatControlMessage.CCT_ICE_CANDIDATE;
            message.payload = chatManager.gson.toJson(ice);
            chattingWith.sendMessage(message);
        } else {
            waitingCandidates.add(ice);
        }
    }

    private void handleDisconnect() {
        System.out.println("Disconnected");
        if(chattingWith != null) {
            chattingWith.sendMessage(new ChatControlMessage(ChatControlMessage.CCT_CHAT_DISCONNECT));
            chattingWith.clear();
        }
        chatManager.removeOffer(this);
        clear();
    }

    public void clear() {
        waitingCandidates.clear();
        sdp = null;
        chattingWith = null;
    }


}
