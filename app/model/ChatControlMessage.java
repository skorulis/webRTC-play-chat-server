package model;

/**
 * Created by alex on 19/03/15.
 */
public class ChatControlMessage {

    public static final String CCT_CHAT_REQUEST = "CCT_CHAT_REQUEST";
    public static final String CCT_CHAT_INIT = "CCT_CHAT_INIT";
    public static final String CCT_CHAT_OFFER = "CCT_CHAT_OFFER";
    public static final String CCT_CHAT_ANSWER = "CCT_CHAT_ANSWER";
    public static final String CCT_CHAT_DISCONNECT = "CCT_CHAT_DISCONNECT";
    public static final String CCT_ICE_CANDIDATE = "CCT_ICE_CANDIDATE";

    public String type;
    public String payload;

    public ChatControlMessage() {

    }

    public ChatControlMessage(String type) {
        this.type = type;
    }

    public boolean isChatRequest() {
        return type.equals(CCT_CHAT_REQUEST);
    }

    public boolean isChatOffer() {
        return type.equals(CCT_CHAT_OFFER);
    }

    public boolean isChatAnswer() {
        return type.equals(CCT_CHAT_ANSWER);
    }

    public boolean isIceCandidate() {
        return type.equals(CCT_ICE_CANDIDATE);
    }

    public boolean isDisconnect() {
        return type.equals(CCT_CHAT_DISCONNECT);
    }

}
