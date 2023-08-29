const { createApp } = Vue
createApp({
    data() {
        return {
            client: [],
            accounts: [],
            idAccount: null,
            transactions: [],
            details: null,
            date: null,
            hour: null,
            dateForm: {}
        }
    },
    created() {
        this.loadData()
    },
    methods: {
        loadData() {
            const par = location.search
            const searchP = new URLSearchParams(par)
            this.idAccount = searchP.get('id')
            axios.get('/api/clients/current/accounts/' + this.idAccount)
                .then(res => {
                    console.log(res);
                    this.transactions = res.data.transactions
                    console.log(this.transactions);
                    this.date = this.transactions.map(tr => tr.date.slice(0, -16).replace(/-/g, '/'))
                    this.hour = this.transactions.map(tr => tr.date.slice(11, -7))
                    this.dateForm.hour = this.hour
                    this.dateForm.date = this.date
                    this.transactions.sort((a, b) => b.id - a.id)
                })
                .catch(error => {
                    console.error(error)
                    window.location.href = "/web/error.html"

                })
        },

    }
}).mount('#app')