package com.unn.dto;

import lombok.Data;

@Data
public class Message {
    private final String text;
    private final String sender;
    private final String date;
    private String justify;

    private static final String PLACE_END = "justify-content-end";
    private static final String PLACE_START = "justify-content-start";

    public Message(final String text, final String sender, final String date) {
        this.text = text;
        this.sender = sender;
        this.date = date;
    }
    public Message(final Message other, final String user) {
        this.text = other.text;
        this.sender = other.sender;
        this.date = other.date;
        updateAlignment(user);
    }

    private void updateAlignment(final String currentUser) {
        this.justify = this.sender == currentUser ? PLACE_END : PLACE_START;
    }
}
