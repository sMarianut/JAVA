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
            axios.get('http://localhost:8080/api/accounts/' + this.idAccount)
                .then(res => {
                    this.accounts = res.data
                    console.log(this.accounts);
                    for (const transacs of this.accounts.transactions) {
                        this.transactions.push(transacs)
                    }
                    this.date = this.transactions.map(tr => tr.date.slice(0, -16))
                    this.hour = this.transactions.map(tr => tr.date.slice(11, -7))
                    this.dateForm.hour = this.hour
                    this.dateForm.date = this.date
                    this.transactions.sort((a, b) => b.id - a.id)
                })
        },

    }
}).mount('#app')