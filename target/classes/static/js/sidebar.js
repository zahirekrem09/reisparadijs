import {logout} from "./auth/auth.js";

// fixme for role base sidebar
function includeSidebar() {
    fetch('/partials/sidebar.html')
        // load the sidebar partial
        .then(response => response.text())
        .then(data => {
            // insert the partial into the sidebar
            document.querySelector('aside').innerHTML = data;
            const sidebar = document.querySelector(".sidebar");
            const toggle = document.querySelector(".toggle");
            const logoutBtn = document.getElementById('logoutBtn');
            // Event listeners
            logoutBtn?.addEventListener('click', () => logout())
            toggle?.addEventListener("click", () => {
                sidebar?.classList.toggle("close");
            })
            // Add active class to clicked nav item
            const navItems = document.querySelectorAll('.menu-links .nav-link a');
            navItems.forEach(item => {
                item.addEventListener('click', function () {
                    // Remove active class from all nav items
                    navItems.forEach(i => i.classList.remove('active'));
                    this.classList.add('active');
                });
            });

        });
}
window.onload = function (){
    includeSidebar();
}