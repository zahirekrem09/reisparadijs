function includeHeader() {
    fetch('/partials/header.html')
        .then(response => response.text())
        .then(data => {
            document.querySelector('header').innerHTML = data;
        });
}

function includeFooter() {
    fetch('/partials/footer.html')
        .then(response => response.text())
        .then(data => {
            document.querySelector('footer').innerHTML = data;
        });
}

window.onload = function() {
    includeHeader();
    includeFooter();
};