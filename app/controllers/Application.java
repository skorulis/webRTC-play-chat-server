package controllers;

import play.*;
import play.libs.F;
import play.mvc.*;

import views.html.*;

public class Application extends Controller {

    private static final ChatSocketManager chatManager = new ChatSocketManager();

    public static WebSocket<String> echo() {
        return new EchoSocket();
    }

    public static WebSocket<String> chat() {
        return new ChatSocket(chatManager);
    }

}
