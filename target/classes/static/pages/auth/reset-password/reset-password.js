import httpClient from "../../../js/httpClient.js";
import ui from "../../../js/auth/ui.js";
import {checkLogin} from "../../../js/auth/auth.js";


const passwordInput = document.getElementById('password');
const passwordConfirmInput = document.getElementById('passwordConfirm');
const submitButton = document.getElementById('submit-button');

//todo : add form validation
const handleInput = () => {
    // Enable or disable submit button
    submitButton.disabled = !(passwordInput.value.length >= 8 && passwordInput.value === passwordConfirmInput.value);

}
const handleResetPassword = async (e) => {
    e.preventDefault();
    submitButton.disabled = true;

    const params = new URLSearchParams(window.location.search);
    const token = params.get('token');
    if (!token) {
        ui.showErrorAlertMessage('Invalid token');
        submitButton.disabled = false;
        return;
    }

    const form = e.target;
    const password = form.elements['password'].value;
    const passwordConfirm = form.elements['passwordConfirm'].value;
    try {
        const response = await httpClient.post(`/api/auth/reset-password/${token}`, {
            password,
            passwordConfirm
        });
        // Show success alert
        ui.showSuccessAlert(response?.message);
        // redirect to login page
        setTimeout(() => {
            window.location.href = '/auth/login';
        }, 4000);

    } catch (e) {
        ui.showErrorAlert(e);
        // Re-enable submit button
        submitButton.disabled = false;
    }
}
document.addEventListener('DOMContentLoaded', () => {
    checkLogin()
    const form = document.getElementById('resetPasswordForm');
    passwordInput.addEventListener('input', handleInput);
    passwordConfirmInput.addEventListener('input', handleInput);
    if (form) {
        // Handle form submission
        form.addEventListener('submit', handleResetPassword);
    }
});
