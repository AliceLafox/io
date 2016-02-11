package net.lafox.io.entity;

import java.io.Serializable;

/**
 * Created by tsyma on 1/21/16.
 */
public class SiteIp implements Serializable {
    private static final long serialVersionUID = 6796698908629665L;

    private Long id;
    private String siteName;
    private String network;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }
}
