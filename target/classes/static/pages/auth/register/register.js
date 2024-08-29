import httpClient from "../../../js/httpClient.js";
import ui from "../../../js/auth/ui.js";
import {checkLogin} from "../../../js/auth/auth.js";



//todo : Add form validation
const submitButton = document.getElementById('submit-button');
const handleRegister = async (e) => {
    e.preventDefault();

    submitButton.disabled = true;

    const form = e.target;
    const email = form.elements['email'].value;
    const password = form.elements['password'].value;
    const firstName = form.elements['firstName'].value;
    const infix = form.elements['infix'].value;
    const lastName = form.elements['lastName'].value;
    const userName = form.elements['userName'].value;
    const role = form.elements['role'].value;
    try {
        const response = await httpClient.post('/api/auth/register',
            {email, password, firstName, infix, lastName, userName,
                authorities:[role],gender:"UNDETERMINED"});
        // Show success alert
        ui.showSuccessAlert(response?.message);
        // redirect to home page
        setTimeout(() => {
            window.location.href = '/auth/login';
        } , 4000);
    } catch (e) {
        ui.showErrorAlert(e);
    }
    finally {
        submitButton.disabled = false;
    }
}

document.addEventListener('DOMContentLoaded', () => {
    checkLogin()
    const registerForm = document.getElementById('registerForm');
    if (registerForm) {
        // Handle form submission
        registerForm.addEventListener('submit', handleRegister);
    }
});