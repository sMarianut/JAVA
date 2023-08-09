const { createApp } = Vue
createApp({
    data() {
        return {
            clients: null,
            accounts: null,
            firstName: '',
            creationDate: '',
            number: '',
            balance: Number
        }
    },
    created() {
        this.loadData()
    },
    methods: {
        loadData() {
            axios.get('http://localhost:8080/api/clients/1')
                .then(res => {
                    this.clients = res
                    this.firstName = this.clients.data.firstName
                    this.accounts = this.clients.data.accounts
                    localStorage.setItem('client', JSON.stringify(this.clients))
                })
                .catch(error => console.error(error))
        }
    }
}).mount('#app')