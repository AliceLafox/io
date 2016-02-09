package net.lafox.ioClient;

/**
 * Created by Alice Lafox <alice@lafox.net> on 21.01.16
 * Lafox.Net Software developers Team http://dev.lafox.net
 */
public class Token {
    private String readToken;
    private String writeToken;


    public String getReadToken() {
        return readToken;
    }

    public void setReadToken(String readToken) {
        this.readToken = readToken;
    }

    public String getWriteToken() {
        return writeToken;
    }

    public void setWriteToken(String writeToken) {
        this.writeToken = writeToken;
    }
}
