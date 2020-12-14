package com.unn.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Message {
    private String text;
    private String sender;
    private String date;
    private boolean isCurrentUser;

    public void updateCurrentSenderFlag(String currentUser) {
        this.isCurrentUser = this.sender.equals(currentUser);
    }
}
