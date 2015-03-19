package controllers;

import play.*;
import play.libs.F;
import play.mvc.*;

import views.html.*;

public class Application extends Controller {

    public static Result index() {
        return ok(index.render("Your new application is ready."));
    }

    public static WebSocket<String> echo() {
        return new EchoSocket();
    }

    public static WebSocket<String> chat() {
        return new ChatSocket();
    }

}
