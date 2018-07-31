function initialize() {
    $.ajax({url: "https://johnnywaity.com/SpellNite/avg.php", success: function (result) {
        document.getElementById("text").innerHTML = result
    }})
}