package com.unn.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ChatRequest {
    @JsonProperty("username")
    private String username;

    @JsonProperty("chat-name")
    private String chatName;
}
