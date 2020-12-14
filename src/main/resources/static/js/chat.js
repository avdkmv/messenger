var stompClient = null
var url = null
var sendUrl = null

function connect() {
    var socket = new SockJS("/stomp")
    stompClient = Stomp.over(socket)
    stompClient.debug = false
    console.log("Connecting...")

    url = "/topic/chat/".concat(chatId)
    queue = "/user/queue/chat/".concat(chatId)
    sendUrl = url.replace("/topic", "/app")
    stompClient.connect({}, () => {
        console.log("Connected")
        stompClient.subscribe(url, function () {
            stompClient.send(sendUrl.concat("/messages"), {}, "")
        })

        stompClient.subscribe(queue, function (messages) {
            updateMessageHistory(JSON.parse(messages.body))
        })

        // stompClient.subscribe(url.concat("/users"), (users) => {
        //     updateUserList(JSON.parse(users.body))
        // })

        // stompClient.subscribe(url.concat("/users/search"), (users) => {
        //     showSearchResults(JSON.parse(users.body))
        // })

        stompClient.send(sendUrl.concat("/messages"), {}, "")
    })
}

function showMessage(message) {
    const justify = message.currentUser ? "justify-content-end" : "justify-content-start"

    $("#messages").append(
        "<div id='msg' class='d-flex " +
            justify +
            " mb-3'>" +
            "<div class='msg rounded shadow p-3 text-wrap'><span>" +
            message.text +
            "</span><div class='text-muted'><small>" +
            message.sender +
            "</small></div></div></div>",
    )
    $("#messages").scrollTop($("#messages")[0].scrollHeight)
}

function sendMessage() {
    stompClient.send(sendUrl.concat("/message"), {}, $("#message").val())
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
    stompClient.send(sendUrl.concat("/users"), {}, "")
}

function deleteUser() {
    stompClient.send(sendUrl.concat("/user/").concat(userName).concat("/delete"), {}, "")
}

function addUser(username) {
    stompClient.send(sendUrl.concat("/user/").concat(username), {}, "")
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
    // $("#username-input").change(function (e) {
    //     if (e.target.value == null || e.target.value.length === 0) {
    //         $(".dropdown").dropdown("hide")
    //         $("#results").empty()
    //         $("#results").append("<label>Search for the users to add to this conversation</label>")
    //     }

    //     stompClient.send(sendUrl.concat("/users/search"), {}, e.target.value)
    // })
})
