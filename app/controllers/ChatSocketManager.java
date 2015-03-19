package controllers;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alex on 19/03/15.
 */
public class ChatSocketManager {

    private final List<ChatSocket> offers;
    public final Gson gson;

    public ChatSocketManager() {
        offers = new ArrayList<>();
        gson = new Gson();
    }

    public synchronized ChatSocket findOffer() {
        if(offers.size() > 0) {
            return offers.remove(0);
        }
        return null;
    }

    public synchronized void addOffer(ChatSocket socket) {
        offers.add(socket);
    }

    public synchronized  void removeOffer(ChatSocket socket) {
        offers.remove(socket);
    }


}
