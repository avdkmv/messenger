var stompClient = null

const topic = "/topic/{type}/{id}"
const queue = "/user/queue/{type}/{id}"

var chatTopic = null
var chatSend = null
var chatQueue = null

var ticketTopic = null
var ticketSend = null
var ticketQueue = null

function connect() {
    var socket = new SockJS("/stomp")
    stompClient = Stomp.over(socket)
    stompClient.debug = false
    console.log("Connecting...")

    chatTopic = topic.replace("{type}", "chat").replace("{id}", chatId)
    chatSend = chatTopic.replace("topic", "app")
    chatQueue = queue.replace("{type}", "chat").replace("{id}", chatId)

    ticketTopic = topic.replace("{type}", "ticket").replace("{id}", ticketId)
    ticketSend = ticketTopic.replace("topic", "app")
    ticketQueue = queue.replace("{type}", "ticket").replace("{id}", ticketId)
    stompClient.connect({}, () => {
        console.log("Connected")
        stompClient.subscribe(chatTopic, function () {
            stompClient.send(chatSend.concat("/messages"), {}, "")
        })

        stompClient.subscribe(chatQueue, function (messages) {
            updateMessageHistory(JSON.parse(messages.body))
        })

        // stompClient.subscribe(url.concat("/users"), (users) => {
        //     updateUserList(JSON.parse(users.body))
        // })

        // stompClient.subscribe(url.concat("/users/search"), (users) => {
        //     showSearchResults(JSON.parse(users.body))
        // })

        stompClient.send(chatSend.concat("/messages"), {}, "")
    })
}

function showMessage(message) {
    console.log(message)

    $("#messages").append(
        "<div id='msg' class='msg-container d-flex " +
            message.justify +
            " mt-3'>" +
            "<div class='msg rounded shadow p-3 text-wrap text-break'><span>" +
            message.text +
            "</span><div class='text-muted'><small>" +
            message.sender +
            "</small></div></div></div>",
    )
    $("#messages").scrollTop($("#messages")[0].scrollHeight)
}

function sendMessage() {
    stompClient.send(chatSend.concat("/message"), {}, $("#message").val())
    $("#message").val("")
}

function updateMessageHistory(messages) {
    $("#messages #msg").remove()
    messages.forEach((message) => {
        showMessage(message)
    })
}

function updateUserList(users) {
    $("#members tr").remove()

    users.forEach((user) => {
        $("#members").append("<tr><td>" + user.username + "</td></tr>")
    })
}

function updateUsers() {
    stompClient.send(chatSend.concat("/users"), {}, "")
}

function deleteUser() {
    stompClient.send(chatSend.concat("/user/").concat(userName).concat("/delete"), {}, "")
}

function addUser(username) {
    stompClient.send(ticketSend.concat("/user/").concat(username), {}, "")
}

function showSearchResults(results) {
    if (results.length === 0) {
        $(".dropdown").dropdown("show")
        return
    }

    $("#results").empty()

    results.forEach((user) => {
        $("#results").append(
            '<button class="btn btn-outline-primary btn-sm" type="button" onclick=addUser(this.innerText)>' +
                user +
                "</button>",
        )
    })
    $(".dropdown").dropdown("show")
}

$(function () {
    connect()

    $("form").on("submit", function (e) {
        e.preventDefault()
    })
    $("#send").click(function () {
        sendMessage()
    })

    $("#send-user").click(() => {
        addUser($("#user").val())
    })
    // $("#username-input").change(function (e) {
    //     if (e.target.value == null || e.target.value.length === 0) {
    //         $(".dropdown").dropdown("hide")
    //         $("#results").empty()
    //         $("#results").append("<label>Search for the users to add to this conversation</label>")
    //     }

    //     stompClient.send(sendUrl.concat("/users/search"), {}, e.target.value)
    // })
})
