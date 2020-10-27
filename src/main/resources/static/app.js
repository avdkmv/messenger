var stompClient = null

function setConnected(connected) {
    $("#connect").prop("disabled", connected)
    $("#disconnect").prop("disabled", !connected)
    if (connected) {
        $("#conversation").show()
    } else {
        $("#conversation").hide()
    }
    $("#greetings").html("")
}

function connect() {
    var socket = new SockJS("/stomp")
    stompClient = Stomp.over(socket)
    stompClient.debug = false
    stompClient.connect({}, function (frame) {
        setConnected(true)
        // console.log("Connected: " + frame)
        stompClient.subscribe("/topic/addUser", function (greeting) {
            showGreeting(JSON.parse(greeting.body).content)
        })
        stompClient.subscribe("/topic/chats", (chats) => {
            updateChatList(JSON.parse(chats.body))
        })
        stompClient.send("/app/chats", {}, "")
    })
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect()
    }
    setConnected(false)
    console.log("Disconnected")
}

function sendName() {
    document.cookie = "username=".concat($("#name").val())
    stompClient.send("/app/addUser", {}, JSON.stringify({ username: $("#name").val() }))
}

function showGreeting(message) {
    $("#greetings").append("<tr><td>" + message + "</td></tr>")
}

function createChat() {
    username = document.cookie.split(";").filter((cookie) => cookie.includes("username="))[0].replace("username=", "")
    
    stompClient.send(
        "/app/chat",
        {},
        JSON.stringify({
            chatName: $("#chat-name").val(),
            username: username,
        }),
    )
}

function getChatLink(chatName)
{
    theUrl = "/chat/" + chatName
    username = document.cookie.split(";").filter((cookie) => cookie.includes("username="))[0].replace("username=", "")
    var xmlHttp = new XMLHttpRequest();
    xmlHttp.open( "GET", theUrl, false ); // false for synchronous request
    xmlHttp.send( JSON.stringify({username : username}) );
    return xmlHttp.responseText;
}

function addChat(chat) {
    chatName = chat.name
    $("#chat-list").append('<tr><td><a link='+ chatName +' class="btn btn-primary link" id="chat-link" href="#" onclick="getChatLink(link)" >' + chatName + "</a></td></tr>")
}

function updateChatList(chats) {
    $("#chat-list tbody tr").remove()
    chats.forEach((chat) => {
        addChat(chat)
    })
}

$(function () {
    document.cookie = "username=pedik"
    $("form").on("submit", function (e) {
        e.preventDefault()
    })
    $("#connect").click(function () {
        connect()
    })
    // $("#chat-update").click(() => {
    //     stompClient.send("/app/chats", {}, "kek")
    // })
    $("#disconnect").click(function () {
        disconnect()
    })
    $("#send").click(function () {
        sendName()
    })
    $("#chat").click(() => createChat())
})
