// send POST request to server
function postURLEnc() {
    $.ajax({
        type: 'POST',
        url:'http://localhost:8086/url-enc',
        data: {
            'url': document.getElementById("url-to-enc").value,
        },
    }).done(function(res) {
        printResult(res, true);
    }).fail(function(err) {
        printResult(err.responseJSON, false);
    });
}

// print output
function printResult(res, is_success) {
    let result_msg = document.getElementById("result-msg");
    if(is_success) {
        document.getElementById("url-result").value = res.shortenUrl;
        result_msg.style.color = "green";
        result_msg.innerHTML = res.message;
    }
    else {
        document.getElementById("url-result").value = "";
        result_msg.style.color = "red";
        result_msg.innerHTML = res.message;
    }
}

function copyToClipboard() {
    navigator.clipboard.writeText(
        document.getElementById("url-result").value
    );
}
