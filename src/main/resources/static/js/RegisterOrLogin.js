function registerOrLoginHandler() {
    const BASE_URL = "http://localhost:8080";
    const MEMBERS_ENDPOINT = `${BASE_URL}/api/members`;
    const LOGIN_ENDPOINT = `${BASE_URL}/api/auth/login`;

    const API = {
        async fetchMembers() {
            const token = localStorage.getItem("jwt");
            const res = await fetch(MEMBERS_ENDPOINT, {
                headers: {
                    "Authorization": `Bearer ${token}`
                }
            });
            return res;
        },

        async create(email, password, role) {
            const payload = {email, password, role};
            const res = await fetch(MEMBERS_ENDPOINT, {
                method: "POST",
                headers: {"Content-Type": "application/json"},
                body: JSON.stringify(payload)
            });
            return res;
        },

        async login(email, password) {
            const payload = {email, password};
            const res = await fetch(LOGIN_ENDPOINT, {
                method: "POST",
                headers: {"Content-Type": "application/json"},
                body: JSON.stringify(payload)
            });
            return res;
        }
    }

    // Register functions
    function getRegisterFormFields(form) {
        const email = form.querySelector("input[name=new-member-email]");
        const password = form.querySelector("input[name=new-member-password]");
        const role = form.querySelector("input[name=role]:checked");
        return {email, password, role};
    }

    async function handleCreateMemberSubmit(e) {
        e.preventDefault();
        const form = document.getElementById("register-form");
        const {role, email, password} = getRegisterFormFields(form);
        const res = await API.create(email.value, password.value, role.value)

        if (res.ok) {
            alert("User registered!");
            resetForm();
        } else {
            alert("Failed to register.");
        }
    }

    //Login functions
    function getLoginFormFields(form) {
        const memberEmail = form.querySelector("input[name=member-email]");
        const memberPassword = form.querySelector("input[name=member-password]");
        return {memberEmail, memberPassword};
    }

    async function handleLoginSubmit(e) {
        e.preventDefault();
        const form = document.getElementById("login-form");
        const {email, password} = getLoginFormFields(form);
        const memberEmail = email.value;
        const memberPassword = password.value;
        const res = await API.login(memberEmail, memberPassword);

        if (res.ok) {
            const {token} = await res.json();
            localStorage.setItem("jwt", token);
            alert("Login successful!");
            resetForm();
        } else {
            alert("Invalid login.");
        }
    }

    function resetForm() {
        const inputs = document.querySelectorAll("input:not([type=radio])");
        inputs.forEach(input => input.value = "");
    }

    function setupForms() {
        const registerForm = document.getElementById("register-form");
        const loginForm = document.getElementById("login-form");

        registerForm.addEventListener("submit", handleCreateMemberSubmit);
        loginForm.addEventListener("submit", handleLoginSubmit);
    }

    setupForms();
}
