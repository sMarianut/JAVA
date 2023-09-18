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
            dateForm: {},
            showForm: false,
            dateInit: null,
            dateEnd: null,
            numberAcc: null
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
                    this.numberAcc = res.data.number
                    console.log(this.numberAcc);
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
        showSearch() {
            this.showForm = true
        },
        searchTransactions() {
            const url = `http://localhost:8080/api/transactions/findDate?dateInit=${this.dateInit}:00&dateEnd=${this.dateEnd}:00&numberAcc=${this.numberAcc}`;
            axios
                .get(url, { responseType: 'blob' })
                .then((response) => {
                    console.log(response);
                    const blob = new Blob([response.data], { type: 'application/pdf' });
                    const url = window.URL.createObjectURL(blob);
                    const a = document.createElement('a');
                    a.href = url;
                    a.download = 'transactions-Table.pdf';
                    a.click();
                    window.URL.revokeObjectURL(url);
                })
                .catch((error) => {
                    console.log(JSON.stringify(error));

                });
        }

    }
}).mount('#app')