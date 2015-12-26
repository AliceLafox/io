package net.lafox.io.exceptions;

/**
 * Created by Alice Lafox <alice@lafox.net> on 22.12.15
 * Lafox.Net Software Developers Team http://dev.lafox.net
 */

public class RollBackException extends Exception {
    public RollBackException(String message) {
        super(message);
    }

    public RollBackException(Throwable cause) {
        super(cause);
    }
}
