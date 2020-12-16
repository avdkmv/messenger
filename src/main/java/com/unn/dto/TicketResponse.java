package com.unn.dto;

import java.util.List;
import com.unn.model.Ticket;
import com.unn.model.User;
import lombok.Data;

@Data
public class TicketResponse {
    private String name;
    private String link;
    private String participants;

    private Ticket ticket;

    public TicketResponse(Ticket ticket) {
        this.name = ticket.getName();
        this.ticket = ticket;
        this.link = ticket.getLink();
        this.participants = participants(ticket.getAllUsers());
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
}
