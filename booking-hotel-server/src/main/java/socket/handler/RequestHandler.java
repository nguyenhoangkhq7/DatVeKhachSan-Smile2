package socket.handler;

import model.Request;
import model.Response;

import java.io.IOException;

public interface RequestHandler {
    Response<?> handle(Request<?> request) throws IOException;
}

