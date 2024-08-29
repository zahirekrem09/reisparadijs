import httpClient from "../../../js/httpClient.js";
import ui from "../../../js/auth/ui.js";
import {checkLogin} from "../../../js/auth/auth.js";


const emailInput = document.getElementById('email');
const submitButton = document.getElementById('submit-button');

//todo : add form validation
const handleInput = () => {
    // Enable or disable submit button
    submitButton.disabled = !(emailInput.value.length >= 4);

}
const handleForgotPassword = async (e) => {
    e.preventDefault();
    submitButton.disabled = true;

    const form = e.target;
    const email = form.elements['email'].value;
    try {
        const response = await httpClient.post('/api/auth/reset-password', {email});
        // Show success alert
        ui.showSuccessAlert(response?.message);
        // redirect to home page
        setTimeout(() => {
            window.location.href = '/';
        } , 8000);

    } catch (e) {
        ui.showErrorAlert(e);
        // Re-enable submit button
        submitButton.disabled = false;
    }
}
document.addEventListener('DOMContentLoaded', () => {
    checkLogin()
    const form = document.getElementById('forgotPasswordForm');
    emailInput.addEventListener('input', handleInput);
    if (form) {
        // Handle form submission
        form.addEventListener('submit', handleForgotPassword);
    }
});
