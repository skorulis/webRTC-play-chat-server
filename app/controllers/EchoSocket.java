package controllers;

import play.libs.F;
import play.mvc.WebSocket;

/**
 * Created by alex on 19/03/15.
 */
public class EchoSocket extends WebSocket<String> {

    public void onReady(final WebSocket.In<String> in, final WebSocket.Out<String> out) {
        System.out.println("START ECHO SOCKET " + in.toString());
        in.onMessage(new F.Callback<String>() {
            @Override
            public void invoke(String s) throws Throwable {
                out.write(s);
            }
        });
    }

}
