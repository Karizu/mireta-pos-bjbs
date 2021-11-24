package com.boardinglabs.mireta.standalone.component.network.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SecurityQuestions {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("security_question")
    @Expose
    private String question;
    @SerializedName("name")
    @Expose
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
