package net.lafox.io.entity;

import java.io.Serializable;

/**
 * Created by tsyma on 1/21/16.
 */
public class Site implements Serializable {

    private static final long serialVersionUID = 679756698908429665L;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
