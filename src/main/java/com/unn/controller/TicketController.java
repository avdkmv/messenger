package com.unn.controller;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import com.unn.dto.ChatRequest;
import com.unn.dto.TicketRequest;
import com.unn.dto.TicketResponse;
import com.unn.model.Chat;
import com.unn.model.Ticket;
import com.unn.model.User;
import com.unn.service.ChatService;
import com.unn.service.TicketService;
import com.unn.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TicketController {
    @Autowired
    private SimpMessagingTemplate template;

    private final ChatService chatService;
    private final UserService userService;
    private final TicketService ticketService;

    @PostMapping("/ticket/new")
    public void createTicket(@RequestBody TicketRequest request, Authentication auth) {
        ChatRequest chatRequest = new ChatRequest();
        chatRequest.setId(UUID.randomUUID().toString());
        chatRequest.setUsername(auth.getName());

        Chat newChat = chatService.addChat(chatRequest);
        ticketService.createTicket(request, userService.getUser(auth.getName()).get(), newChat);
        template.convertAndSend("/topic/tickets", true);
    }

    @MessageMapping("/tickets")
    @SendToUser(destinations = "/queue/tickets", broadcast = false)
    public List<TicketResponse> getAllTickets(Authentication auth) {
        List<Ticket> allTicketsForUser = ticketService.allTicketsForUser(auth.getName());

        return allTicketsForUser.stream().map(ticket -> new TicketResponse(ticket)).collect(Collectors.toList());
    }

    @MessageMapping("/ticket/{id}/user/{username}")
    @SendTo("/topic/tickets")
    public boolean addUser(
        @DestinationVariable("id") String id,
        @DestinationVariable("username") String userName
    ) {
        ticketService.addUser(id, userName);

        return true;
    }

    @MessageMapping("/ticket/{id}/user/{username}/delete")
    @SendTo("/topic/tickets")
    public Collection<User> deleteUser(
        @DestinationVariable("id") String id,
        @DestinationVariable("username") String userName
    ) {
        Optional<Ticket> ticket = ticketService.findById(id);

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
