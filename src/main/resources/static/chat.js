var stompClient = null

function connect() {
    var socket = new SockJS("/stomp")
    stompClient = Stomp.over(socket)
    stompClient.debug = false
    stompClient.connect({}, function (frame) {
        // console.log("Connected: " + frame)
        url = "/topic/chat/".concat(chatName)
        stompClient.subscribe(url, function (messages) {
            updateMessageHistory(JSON.parse(messages.body))
        })
        stompClient.send("/app/chat/".concat(chatName).concat("/messages"), {}, "")
    })
}

function showMessage(message) {
    $("#messages").append("<tr><td>" + message + "</td></tr>")
}

function sendMessage() {
    url = "/app/chat/".concat(chatName).concat("/message")
    stompClient.send(url, {}, $("#message").val())
    $("#message").val("")
}

function updateMessageHistory(messages) {
    $("#messages tr").remove()
    messages.forEach((message) => {
        showMessage(message)
    })
}

$(function () {
    connect()

    $("form").on("submit", function (e) {
        e.preventDefault()
    })
    $("#send").click(function () {
        sendMessage()
    })
})
