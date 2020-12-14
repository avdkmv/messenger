var stompClient = null
var username = null
var topic = null
var sendUrl = null

function connect(callback) {
    var socket = new SockJS("/stomp")
    stompClient = Stomp.over(socket)
    stompClient.debug = false
    console.log("Connecting...")

    topic = "/topic"
    queue = "/user/queue"
    sendUrl = "/app"
    stompClient.connect({}, function () {
        console.log("Connected")

        stompClient.subscribe(topic.concat("/users"), (users) => {
            stompClient.send(sendUrl.concat("/users/all"), {}, "")
        })
        stompClient.subscribe(queue.concat("/users"), (users) => {
            updateUserList(JSON.parse(users.body))
        })
        stompClient.send(sendUrl.concat("/users/all"), {}, "")

        stompClient.subscribe(topic.concat("/chats"), () => {
            stompClient.send(sendUrl.concat("/chats"), {}, "")
        })
        stompClient.subscribe(queue.concat("/chats"), (chats) => {
            updateChatList(JSON.parse(chats.body))
        })
        stompClient.send(sendUrl.concat("/chats"), {}, "")

        stompClient.subscribe(topic.concat("/tickets"), () => {
            stompClient.send(sendUrl.concat("/tickets"), {}, "")
        })
        stompClient.subscribe(queue.concat("/tickets"), (tickets) => {
            updateTicketList(JSON.parse(tickets.body))
        })
        stompClient.send(sendUrl.concat("/tickets"), {}, "")

        if (callback) {
            callback()
        }
    })
}

function createChat(item) {
    const username = $(item).parent().children("#username").text()
    stompClient.send(
        "/app/chat",
        {},
        JSON.stringify({
            username: username,
        }),
    )
}

function addUser(user) {
    $("#users").append(
        "<li id='user' class='list-group-item d-flex justify-content-between'><div id='username'>" +
            user.username +
            "</div><a id='chat' href='#' class='text-decoration-none' onclick='createChat(this)'>chat...</a></li>",
    )
}

function addChat(chat) {
    $("#chats").append(
        "<li id='chat' class='list-group-item shadow mb-2'><div id='chatname'>" +
            chat.name +
            "</div><div id='participants' class='text-muted mt-2'>" +
            chat.participants +
            "</div><a id='openchat' href='" +
            chat.link +
            "' class='text-decoration-none stretched-link' onclick='openChat()'></a></li>",
    )
}

function addTicket(ticket) {
    $("#tickets").append(
        "<li id='ticket' class='list-group-item shadow mb-2'><div id='ticketname'>" +
            ticket.name +
            "</div><div id='participants' class='text-muted mt-2'>" +
            ticket.participants +
            "</div><a id='openticket' href='" +
            ticket.link +
            "' class='text-decoration-none stretched-link' onclick='openTicket()'></a></li>",
    )
}

function updateUserList(users) {
    $("#users li").remove()
    users.forEach((user) => {
        addUser(user)
    })
}

function updateChatList(chats) {
    $("#chats li").remove()
    chats.forEach((chat) => {
        addChat(chat)
    })
}

function updateTicketList(tickets) {
    $("#tickets li").remove()
    tickets.forEach((ticket) => {
        addTicket(ticket)
    })
}

function createTicket() {
    fetch("/ticket/new", {
        method: "POST",
        body: JSON.stringify({
            ticketName: $("#ticketName").val(),
            description: $("#description").val(),
        }),
        headers: {
            "Content-Type": "application/json",
        },
    })
    $("#exampleModalCenter").modal("hide")
    $("#ticketName").val = ""
    $("#description").val = ""
}

$(function () {
    connect()
    $("form").on("submit", function (e) {
        e.preventDefault()
    })
    $("#send").click(function () {
        sendName()
    })
    $("#ticket-submit").click(function () {
        createTicket()
    })
})
