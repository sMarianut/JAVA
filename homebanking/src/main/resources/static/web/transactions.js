const { createApp } = Vue
const options = {
    data() {
        return {
            originAcc: "",
            originDest: "",
            amount: "",
            description: "",
            accounts: [],
            myTransfer: true,

        }
    },
    created() {
        this.loadData()

    },
    methods: {
        loadData() {
            axios.get('/api/clients/current/accounts')
                .then(response => {
                    this.accounts = response.data
                    console.log(this.accounts);
                }).catch(error => console.log(error))
        },
        logout() {
            axios.post(/api/logout)
        }
    }
}
const app = createApp(options)
app.mount('#app')