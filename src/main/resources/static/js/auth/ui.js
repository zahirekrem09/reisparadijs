class UI {
    constructor() {
        this.successAlert = document.getElementById('success-alert');
        this.alertMessage = document.getElementById('alert-message');
    }

    showSuccessAlert(message='Successfully') {
        this.successAlert.getElementsByClassName("alert-title")[0].innerText = message;
        this.successAlert.style.display = 'block';
        setTimeout(() => {
            this.successAlert.style.display = 'none';
            // window.location.href = '/';
        } , 5000);
    }

    showErrorAlert(e) {
        const p = document.createElement("p");
        this.alertMessage.getElementsByClassName("alert-title")[0].innerText =e?.data?.message || 'Error';
        p.classList.add("alert-message");
        if (e?.data?.items && Object.keys(e.data.items).length > 0) {
            for (const [key, value] of Object.entries(e.data.items)) {
                p.innerHTML += `<br> <b>${key}:</b>${value}`;
            }
        }
        this.alertMessage.appendChild(p);
        this.alertMessage.style.display = 'block';
        setTimeout(() => {
            this.alertMessage.style.display = 'none';
            p.innerHTML = '';
            this.alertMessage.getElementsByClassName("alert-title")[0].innerText.innerText='';
        } , 5000);
    }

    showErrorAlertMessage(message='Error') {
        const p = document.createElement("p");
        this.alertMessage.getElementsByClassName("alert-title")[0].innerText = message|| 'Error';
        p.classList.add("alert-message");
        this.alertMessage.appendChild(p);
        this.alertMessage.style.display = 'block';
        setTimeout(() => {
            this.alertMessage.style.display = 'none';
            p.innerHTML = '';
            this.alertMessage.getElementsByClassName("alert-title")[0].innerText='';
        } , 5000);
    }
}

const ui = new UI();
export default ui;