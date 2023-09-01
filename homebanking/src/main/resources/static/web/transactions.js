const { createApp } = Vue
const options = {
    data() {
        return {
            accountOrigin: "",
            accountDest: "",
            amount: "",
            description: "",
            accounts: [],
            showOwnTransfer: true,

        }
    },
    created() {
        this.loadData()

    },
    methods: {
        showTransfer() {
            this.showOwnTransfer = false
        },
        showTransfer2() {
            this.showOwnTransfer = true
        },
        loadData() {
            axios.get('/api/clients/current/accounts')
                .then(response => {
                    this.accounts = response.data
                    this.accounts.sort((a, b) => a.id - b.id)
                    console.log(this.accounts);
                }).catch(error => console.log(error))
        },
        transaction() {
            Swal.fire({
                title: 'Do you want to transfer, bro?',
                inputAttributes: { autocapitalize: 'off', },
                showCancelButton: true, confirmButtonText: 'Yes!',
                showLoaderOnConfirm: true, preConfirm: login => {
                    return axios.post('/api/transactions', `amount=${this.amount}&description=${this.description}&originAccountNumber=${this.accountOrigin}&destinationAccountNumber=${this.accountDest}`)
                        .then(response => { window.location.href = './transactions.html' })
                        .catch(error => {
                            Swal.fire({
                                icon: 'error',
                                title: "ERROR",
                                text: error.response.data,
                                confirmButtonColor: '#5b31be93',
                            });
                        });
                }, allowOutsideClick: () => !Swal.isLoading(),
            })
        },
        logout() {
            axios.post('/api/logout')
        }
    }
}
const app = createApp(options)
app.mount('#app')