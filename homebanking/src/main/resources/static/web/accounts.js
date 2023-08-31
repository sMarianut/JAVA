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
        this.loadData()

    },
    methods: {
        loadData() {
            axios.get('http://localhost:8080/api/clients/current/accounts')
                .then(res => {
                    console.log(res);
                    this.accounts = res.data
                    console.log(this.accounts);
                    this.loans = this.accounts
                    localStorage.setItem('client', JSON.stringify(this.clients))

                })
                .catch(error => console.error(error))
        },
        logout() {
            axios.post('http://localhost:8080/api/logout')
                .then(response => {
                    location.href = '/index.html';
                })
        },
        createAcc() {
            Swal.fire({
                title: 'Do you want to create an account?',
                inputAttributes: { autocapitalize: 'off', },
                showCancelButton: true, confirmButtonText: 'Yes!',
                showLoaderOnConfirm: true, preConfirm: login => {
                    return axios
                        .post('/api/clients/current/accounts')
                        .then(response => { location.href = './accounts.html'; })
                        .catch(error => {
                            Swal.fire({
                                icon: 'error',
                                text: error.response.data,
                                confirmButtonColor: '#5b31be93',
                            });
                        });
                }, allowOutsideClick: () => !Swal.isLoading(),
            });
        }
    }
}).mount('#app')

///"/api/clients/current/accounts"
