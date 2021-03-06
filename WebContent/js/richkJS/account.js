$(document).ready(function () {

    $.get("user", {channel: "webapp"}, function (data) {
        console.log(data)
        var JSONdata = JSON.parse(data)
        if (JSONdata.statusCode == 1000) {
            console.log(JSONdata.message + " " + typeof(JSONdata.message))

            $("#signupNavBar").hide()
            $("#loginNavBar").hide()
            $("#logoutNavBar").show()
            $("#rmcsNavBar").show()
            $("#menuDevices").show()
            $("#menuRMCs").show()

            var JSONmessage = JSON.parse(JSONdata.message)
            if (JSONmessage.admin == true) {
                //$("#userNearBrand").html(" - " + JSONmessage.user + " (ADMIN)")
                $("#userNearBrand").html("").append($("<i></i>").text(JSONmessage.user + " (ADMIN)"))
                $("#usersNavBar").show()
                $("#menuUsers").show()
            } else {
                //$("#userNearBrand").html(" - " + JSONmessage.user)
                $("#menuUsers").hide()
                $("#userNearBrand").html("").append($("<i></i>").text(JSONmessage.user))
            }
            $("#devicesNavBar").show()
            append($("<p></p>").text(response.description))
        } else if (JSONdata.statusCode == 2100) {
            $("#logoutBtn").hide()
            $("#menuDevices").hide()
            $("#menuUsers").hide()
            $("#menuRMCs").hide()
            var JSONmessage = JSON.parse(JSONdata.message)
            var choice = confirm(JSONmessage)
            if (choice)
                window.location.replace("/Richkware-Manager-Server/index.html")
        }
    }).fail(function() {
        $("#signupNavBar").show()
        $("#loginNavBar").show()
        $("#usersNavBar").hide()
        $("#rmcsNavBar").hide()
    })

    $("#logoutBtn").click(function() {
        console.log("logout clicked")
        $.get("LogOut", function(data) {
            var JSONdata = JSON.parse(data)
            if (JSONdata.statusCode == 1000) {
                window.location.replace("/Richkware-Manager-Server/index.html")
            } else {
                alert(JSONdata.message)
            }
        })
    })
 });