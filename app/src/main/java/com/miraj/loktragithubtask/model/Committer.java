package com.miraj.loktragithubtask.model;

import java.io.Serializable;

/**
 * Created by miraj on 26/4/17.
 */

public class Committer implements Serializable {

    private long id;
    private String name;
    private String avatarUrl;
    private String committerUrl;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getCommitterUrl() {
        return committerUrl;
    }

    public void setCommitterUrl(String committerUrl) {
        this.committerUrl = committerUrl;
    }

    @Override
    public String toString() {
        return "Committer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                ", committerUrl='" + committerUrl + '\'' +
                '}';
    }
}
