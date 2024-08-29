import httpClient from "../../../js/httpClient.js";
import ui from "../../../js/auth/ui.js";
import {checkLogin} from "../../../js/auth/auth.js";


const identifierInput = document.getElementById('identifier');
const passwordInput = document.getElementById('password');
const submitButton = document.getElementById('submit-button');

//todo : add form validation
const handleInput = () => {
    // Enable or disable submit button
    submitButton.disabled = !(identifierInput.value.length >= 4 && passwordInput.value.length >= 8);

}
const handleLogin = async (e) => {
    e.preventDefault();
    submitButton.disabled = true;

    const form = e.target;
    const identifier = form.elements['identifier'].value;
    const password = form.elements['password'].value;
    try {
        const response = await httpClient.post('/api/auth/login', {identifier, password});
        localStorage.setItem('accessToken', response.accessToken);
        localStorage.setItem('refreshToken', response.refreshToken);

        // Set tokens
        //fixme: this is not working
        httpClient.setTokens(response.accessToken, response.refreshToken);
        // Show success alert
        ui.showSuccessAlert();
        // redirect to home page
        setTimeout(() => {
            window.location.href = '/dashboard';
        } , 1000);

    } catch (e) {
        ui.showErrorAlert(e);
        // Re-enable submit button
        submitButton.disabled = false;
    }
}
document.addEventListener('DOMContentLoaded', () => {
    checkLogin()
    const form = document.getElementById('loginForm');
    identifierInput.addEventListener('input', handleInput);
    passwordInput.addEventListener('input', handleInput);
    if (form) {
        // Handle form submission
        form.addEventListener('submit', handleLogin);
    }
});
