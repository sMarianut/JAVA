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
            loans: [],
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
                    this.accounts = res.data.filter(acc => acc.accOn)
                    console.log(this.accounts);
                    localStorage.setItem('client', JSON.stringify(this.clients))

                })
                .catch(error => console.error(error))
            axios.get('/api/clients/current')
                .then(res => {
                    console.log(res.data.loans);
                    this.firstName = res.data.firstName
                    this.loans = res.data.loans
                    console.log(this.loans);
                    this.loans.sort((a, b) => a.id - b.id)
                })
                .catch(error => console.error(error))
        },
        deleteAcc(id) {
            axios.patch('/api/clients/current/deleteAcc', `id=${id}`)
                .then(res => {
                    console.log(res);
                })
                .catch(error => console.error(error))
        }
        ,
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
                        .then(response => {
                            Swal.fire(
                                'Good job!',
                                'You clicked the button!',
                                'success',
                                location.href = './accounts.html')
                        })
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

