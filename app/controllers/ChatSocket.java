package controllers;

import play.mvc.WebSocket;

/**
 * Created by alex on 19/03/15.
 */
public class ChatSocket extends WebSocket<String> {

    

    private WebSocket.In<String> in;
    private WebSocket.Out<String> out;

    public void onReady(final WebSocket.In<String> in, final WebSocket.Out<String> out) {
        this.in = in;
        this.out = out;
    }
}
