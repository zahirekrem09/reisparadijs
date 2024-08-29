import httpClient from "../../../js/httpClient.js";
import {getAccessToken} from "../../../js/auth/auth.js";
import {formatDistance} from "../../../js/utils.js";



const fetchMessages = async () => {
    try {
        return await httpClient.get('/api/messages');
    } catch (error) {
        console.log(error);
    }
}

document.addEventListener('DOMContentLoaded', function () {
    // checkAuth();
    // const userDetails = document.getElementById('userDetails');
    const emailList = document.getElementById('email-list');
    const emailDetails = document.getElementById('email-details');
    httpClient.setDefaultHeaders(
        {'Authorization': `Bearer ${getAccessToken()}`}
    )



    fetchMessages().then(messages => {
        messages.forEach(message => {
            const emailItem = document.createElement('div');
            emailItem.classList.add('email-item');
            emailItem.id = message.id;
            emailItem.innerHTML = `
            <header class="email-item-header" >
              <img src="https://s3-us-west-2.amazonaws.com/s.cdpn.io/3364143/download+%281%29+%281%29.png" alt="Grace Collins">
              <div class="email-info">
                <h4>${message.receiver.firstName}</h4>
                <p>${message.messageSubject.title}</p>
              </div>
              <span class="email-date">${formatDistance(new Date(message.createdAt), new Date())}</span>
            </header>
            <div class="email-info">
              <p class="email-preview">${message.content.slice(0, 50)} ...</p>
            </div>
            `;
            emailList.appendChild(emailItem);
        })
        const emailItems = document.querySelectorAll('.email-item');
        emailItems[0].classList.add('active');
        emailDetails.innerHTML = `<pre><code class="email-details-code" id="email-details">${JSON.stringify(messages[0], null, 2)}</code></pre>`

        emailItems.forEach(item => {
            item.addEventListener('click', function () {
                // Remove active class from all nav items
                emailItems.forEach(i => i.classList.remove('active'));
                this.classList.add('active');
                const message = messages.find(m => m.id == item.id);
                console.log({message});
                emailDetails.innerHTML = `<pre><code class="email-details-code" id="email-details">${JSON.stringify(message, null, 2)}</code></pre>`
                // TODO: show email details
            })
        })
    });
});
