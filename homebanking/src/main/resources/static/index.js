const { createApp } = Vue
createApp({
    data() {
        return {

            email: "",
            password: "",
            firstName: "",
            lastName: "",
            showLoginForm: true,
        }
    },
    created() {
    },
    methods: {
        login() {
            axios.post('/api/login', `email=${this.email}&password=${this.password}`, { headers: { 'content-type': 'application/x-www-form-urlencoded' } })
                .then(response => {
                    console.log('entra a la response');
                    console.log(this.email);
                    if ("admin@adm.com".includes(this.email)) {
                        location.href = '/admin/manager.html';
                    } else {
                        location.href = '/web/accounts.html';
                    }
                })
                .catch(error => {
                    window.alert("Your login its incorrect")
                });
        },
        register() {
            axios.post('/api/clients', `firstName=${this.firstName}&lastName=${this.lastName}&email=${this.email}&password=${this.password}`, { headers: { 'content-type': 'application/x-www-form-urlencoded' } })
                .then(response => {
                    this.login()
                })
                .catch(error => {
                    console.log('error', error);
                })
        },
        showLogin() {
            this.showLoginForm = true;
        },
        showRegister() {
            this.showLoginForm = false;
        }
        // showPassword() {
        //     this.isPasswordShowing = !(this.isPasswordShowing);
        // }
    }
}).mount('#app')


// }:type="isPasswordShowing ? 'text' : 'password'"

