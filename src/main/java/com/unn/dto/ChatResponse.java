package com.unn.dto;

import java.util.List;

import com.unn.model.Chat;
import com.unn.model.User;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ChatResponse {
    private String name = "Conversation";
    private String link;
    private String participants;

    private Chat chat;

    public ChatResponse(Chat chat) {
        this.chat = chat;
        this.link = chat.getLink();
        this.participants = participants(chat.getAllUsers());
    }

    public String participants(List<User> users) {
        StringBuilder result = new StringBuilder();
        users.forEach(
            user -> {
                if (result.length() != 0) {
                    result.append(", ");
                }

                result.append(user.getUsername());
            }
        );

        return result.toString();
    }

    public void generatePrivateName(String currentUser) {
        this.name = chat.generatePrivateName(currentUser);
    }
}
