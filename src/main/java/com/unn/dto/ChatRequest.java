package com.unn.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ChatRequest {
    @JsonProperty("username")
    private String username;

    private String id;
}
