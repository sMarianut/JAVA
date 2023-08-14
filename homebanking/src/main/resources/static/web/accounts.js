const { createApp } = Vue
createApp({
    data() {
        return {
            clients: null,
            accounts: null,
            firstName: '',
            creationDate: '',
            number: '',
            balance: Number,
            loans: []
        }
    },
    created() {
        this.loadData(),
            this.kartaImgClass()
    },
    methods: {
        loadData() {
            axios.get('http://localhost:8080/api/clients/1')
                .then(res => {
                    this.clients = res
                    this.firstName = this.clients.data.firstName
                    this.accounts = this.clients.data.accounts
                    this.loans = this.clients.data.loans.sort((a, b) => a.loanId - b.loanId)
                    console.log(this.loans);
                    localStorage.setItem('client', JSON.stringify(this.clients))

                })
                .catch(error => console.error(error))
        },
        kartaImgClass() {
            const classCard = this.loans.name == 'Personal' ? 'karta__img--hover' : 'karta__img2--hover';
            return classCard;
        }
    }
}).mount('#app')