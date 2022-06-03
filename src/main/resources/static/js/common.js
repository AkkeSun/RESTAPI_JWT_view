
let REST_API_URL = "http://localhost:8080"

function restAjaxComm(url, type, contentType, data){
    let ajax = $.ajax({
        url: REST_API_URL + url,
        type: type,
        contentType : contentType,
        data: data
    });

    return ajax;
}

function homeAjaxComm(url, type, contentType, data){
    let ajax = $.ajax({
        url: url,
        type: type,
        contentType : contentType,
        data: data
    });

    return ajax;
}