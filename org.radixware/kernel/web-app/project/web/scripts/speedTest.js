var POST_REQUEST_TIMEOUT_MS=30000;
var speedTestSendNow = false;
var packageSizes = [1, 1, 1, 1, 1024, 1024, 1024, 1024, 1024 * 1024, 1024 * 1024, 1024 * 1024, 1024 * 1024, 1024 * 1024];
var timerId = null;

function speedTestClick(button, table, interval, maxsize) {
    if (speedTestSendNow) {
        speedTestSendNow = false;
        if (timerId!=null){
            clearTimeout(timerId);
            timerId = null;
        }        
        $('#' + button).val("Start test");
    } else {
        var sendInderval = validate($('#' + interval),'Send interval is out of range');
        var maximumsize = validate($('#' + maxsize),'Maximum packet size is out of range');
        if (isNaN(sendInderval) || isNaN(maximumsize)){
            return;
        }
        speedTestSendNow = true;
        $('#' + table + ' > tbody').html("");
        packageSizes.pop();
        packageSizes.push(maximumsize);
        speedTestTransfer(window.location, 0, table, sendInderval, maximumsize);
        $('#' + button).val("Stop test");
    }
}

function validate(input,message){
    var value = input.val();
    if (isNaN(value)){
        return value;
    }
    var min=parseInt(input.attr('min'));
    var max=parseInt(input.attr('max'));
    if (value<min || value>max){
        alert(message);
        return message;
    }
    return value;
}

function speedTestTransfer(url, packetSizeIndex, table, interval, maxSize) {
    timerId = null;
    var startTime = new Date();
    var currentSize = packageSizes[Math.min(packetSizeIndex, packageSizes.length - 1 )];
    currentSize = Math.min(currentSize, maxSize);
    if (speedTestSendNow) {
        $.ajax({type: 'POST', url: url, data: "packetSize=" + currentSize, timeout: POST_REQUEST_TIMEOUT_MS})
                .done(function (data) {
                    var delta = new Date().getTime() - startTime.getTime();
                    var speed = currentSize / delta;

                    $('#' + table + ' > tbody:last-child').append('<tr><td>' + (packetSizeIndex + 1) + '</td><td>' + currentSize + '</td><td>' + delta + '</td><td>' + speed.toFixed(2) + '</td></tr>');
                    timerId = setTimeout(speedTestTransfer, interval, url, packetSizeIndex + 1, table, interval, maxSize);
                })
                .fail(function (header) {
                    var delta = new Date().getTime() - startTime.getTime();

                    $('#' + table + ' > tbody:last-child').append('<tr><td>' + (packetSizeIndex + 1) + '</td><td>' + currentSize + '</td><td><font color=red><b>' + delta + '</b> Err ' + header.status + ': ' + header.statusText + '</font></td><td> - </td></tr>');
                    timerId = setTimeout(speedTestTransfer, interval, url, packetSizeIndex + 1, table, interval, maxSize);
                });
    }
}
