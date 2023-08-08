const { createApp } = Vue

createApp({
    data() {
        return {
            clients: [],
            firstName: '',
            lastName: '',
            email: '',
            jsonR: null,
            isNavActive: false
        }
    },
    created() {
        this.cargarDatos()

    },
    methods: {
        cargarDatos() {
            axios.get('http://localhost:8080/rest/clients')
                .then(res => {
                    this.clients = res.data._embedded.clients
                    this.jsonR = JSON.stringify(res.data, null, 1)

                })
                .catch(error => console.error(error))
        },
        addClient() {
            let clientNew = { firstName: this.firstName, lastName: this.lastName, email: this.email }
            axios.post('http://localhost:8080/rest/clients', clientNew)
                .then(res => {
                    this.firstName = '',
                        this.lastName = '',
                        this.email = '',
                        this.cargarDatos();


                })
                .catch(error => console.error(error))

        },
        verifier() {
            if (this.firstName && this.lastName && this.email) {
                this.addClient();
            } else {
                window.alert("Please fill in all required fields.");

            }
        }
    }
}).mount('#app');
