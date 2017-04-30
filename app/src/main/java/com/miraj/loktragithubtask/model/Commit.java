package com.miraj.loktragithubtask.model;

import java.io.Serializable;

/**
 * Created by miraj on 26/4/17.
 */

public class Commit implements Serializable {

    private String sha;
    private String commitMessage;
    private String commitUrl;
    private Committer committer;

    public boolean isBookmarked() {
        return bookmarked;
    }

    public void setBookmarked(boolean bookmarked) {
        this.bookmarked = bookmarked;
    }

    private boolean bookmarked;

    public String getSha() {
        return sha;
    }

    public void setSha(String sha) {
        this.sha = sha;
    }

    public String getCommitMessage() {
        return commitMessage;
    }

    public void setCommitMessage(String commitMessage) {
        this.commitMessage = commitMessage;
    }

    public String getCommitUrl() {
        return commitUrl;
    }

    public void setCommitUrl(String commitUrl) {
        this.commitUrl = commitUrl;
    }

    public Committer getCommitter() {
        return committer;
    }

    public void setCommitter(Committer committer) {
        this.committer = committer;
    }

    @Override
    public String toString() {
        return "Commit{" +
                "sha='" + sha + '\'' +
                ", commitMessage='" + commitMessage + '\'' +
                ", commitUrl='" + commitUrl + '\'' +
                ", committer=" + committer +
                '}';
    }
}
