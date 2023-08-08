const { createApp } = Vue
createApp({
    data() {
        return {
            client: [],
            accounts: [],
            idAccount: null,
            transactions: [],
            details: null,
        }
    },
    created() {
        this.loadData()
    },
    methods: {
        loadData() {
            axios.get('http://localhost:8080/api/clients/1')
                .then(res => {
                    this.client = res.data
                    const par = location.search
                    const searchP = new URLSearchParams(par)
                    this.idAccount = searchP.get('id')
                    for (const acc of this.client.accounts) {
                        this.accounts.push(acc)
                    }
                    this.details = this.accounts.find(account => account.id == this.idAccount)
                    for (const transacs of this.details.transactions) {
                        this.transactions.push(transacs)
                    }
                })
        }
    }
}).mount('#app')