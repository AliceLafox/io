package net.lafox.io.entity;


import org.springframework.beans.BeanUtils;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Alice Lafox <alice@lafox.net> on 20.12.15
 * Lafox.Net Software Developers Team http://dev.lafox.net
 */

@Entity
@Table(name = "image")
public class Image implements Serializable {

    private static final long serialVersionUID = 679756698908629665L;
    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @ManyToOne(optional = false)
    private Token token;

    private int sortIndex;
    private int version;
    private int width;
    private int height;
    private Date created = new Date();
    private Date modified = new Date();
    private String contentType = "image/png";
    private String fileName;
    private String title;
    private String description;
    private Long size;

    private boolean active;
    private boolean avatar;

    @PreUpdate
    public void setLastUpdate() {
        this.modified = new Date();
    }

    public Image(Token token) {
        this.token = token;
    }

    public Image(Token token, String contentType, String fileName, Long size) {
        this.token = token;
        this.contentType = contentType;
        this.fileName = fileName;
        this.size = size;

    }


    public Image() {
    }

    public ImageDto asDto(){
        ImageDto imageDto=new ImageDto();
        BeanUtils.copyProperties(this, imageDto);
        return imageDto;
    }
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isAvatar() {
        return avatar;
    }

    public void setAvatar(boolean avatar) {
        this.avatar = avatar;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public int getSortIndex() {
        return sortIndex;
    }

    public void setSortIndex(int sortIndex) {
        this.sortIndex = sortIndex;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

}
