package com.unn.controller;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import com.unn.dto.ChatRequest;
import com.unn.dto.TicketRequest;
import com.unn.model.Chat;
import com.unn.model.Ticket;
import com.unn.model.User;
import com.unn.service.ChatService;
import com.unn.service.TicketService;
import com.unn.service.UserService;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TicketController {
    private final ChatService chatService;
    private final UserService userService;
    private final TicketService ticketService;

    @MessageMapping("/ticket")
    @SendTo("/topic/tickets")
    public List<Ticket> createTicket(TicketRequest request) {
        Chat newChat = chatService.addChat(new ChatRequest(request.getUsername(), request.getTicketName()));
        ticketService.createTicket(request, userService.getUser(request.getUsername()).get(), newChat);
        return ticketService.allTickets();
    }

    @MessageMapping("/tickets")
    @SendTo("/topic/tickets")
    public List<Ticket> getAllTickets(@CookieValue("username") String userName) {
        return ticketService.allTicketsForUser(userName);
    }

    @MessageMapping("/ticket/{name}/user/{username}")
    @SendTo("/topic/ticket/{name}/users")
    public Collection<User> addUser(
        @DestinationVariable("name") String ticketName,
        @DestinationVariable("username") String userName
    ) {
        Collection<User> userList = ticketService.addUser(ticketName, userName);
        userService.getUser(userName).ifPresent(user -> getAllTickets(user.getUsername()));

        return userList;
    }

    @MessageMapping("/ticket/{name}/user/{username}/delete")
    @SendTo("/topic/ticket/{name}/users")
    public Collection<User> deleteUser(
        @DestinationVariable("name") String chatName,
        @DestinationVariable("username") String userName
    ) {
        Optional<Ticket> ticket = ticketService.findTicket(chatName);

        if (ticket.isPresent()) {
            if (userName != null) {
                ticket.get().removeUser(userName);
                ticket.get().getChat().removeUser(userName);
            }
            return ticket.get().getUsers().values();
        } else {
            return null;
        }
    }
}
