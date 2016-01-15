package net.lafox.io.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Alice Lafox <alice@lafox.net> on 20.12.15
 * Lafox.Net Software Developers Team http://dev.lafox.net
 */

public class Token implements Serializable {
    private static final long serialVersionUID = 495411682022208001L;
    private Long id;
    private String readToken;
    private String writeToken;
    private String siteName;
    private String ownerName;
    private Long ownerId;
    private String ip;
    private Date created = new Date();

    public Token(String siteName, String ownerName, Long ownerId, String ip) {
        this.siteName = siteName;
        this.ownerName = ownerName;
        this.ownerId = ownerId;
        this.ip = ip;
        this.writeToken = UUID.randomUUID().toString();
        this.readToken = UUID.randomUUID().toString().substring(0,8);
    }

    public Token() {
    }

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

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
