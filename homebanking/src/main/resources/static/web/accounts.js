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
            selectedAccountTypes: null
        }
    },
    created() {
        this.loadData()
        this.getLoans()


    },
    methods: {
        loadData() {
            axios.get('http://localhost:8080/api/clients/current/accounts')
                .then(res => {
                    this.accounts = res.data.filter(acc => acc.accOn)
                    this.accounts = this.accounts.sort((a, b) => a.id - b.id)
                    localStorage.setItem('client', JSON.stringify(this.clients))
                })
                .catch(error => console.error(error))
            axios.get('/api/clients/current')
                .then(res => {
                    this.firstName = res.data.firstName
                })
                .catch(error => console.error(error))
        },
        deleteAcc(id) {
            axios.patch('/api/clients/current/deleteAcc', `id=${id}`)
                .then(res => {
                    Swal.fire({
                        title: 'Account successfuly eliminated',
                    })
                    setTimeout(() => {
                        window.location.href = './accounts.html';
                    }, 1200);
                })
                .catch(error => {
                    Swal.fire({
                        icon: 'ERROR',
                        text: error.response.data,
                        confirmButtonColor: '#5b31be93',
                    });
                })
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
                html:
                    '<label><input class="radio-buttons" type="radio" name="accountType" value="CURRENT"> Current Account</label><br>' +
                    '<label><input class="radio-buttons" type="radio" name="accountType" value="SAVINGS"> Savings Account</label>',
                inputAttributes: { autocapitalize: 'off', },
                showCancelButton: true, confirmButtonText: 'Yes!',
                showLoaderOnConfirm: true,
                preConfirm: login => {
                    const selectedAccountType = document.querySelector('input[name="accountType"]:checked')
                    if (!selectedAccountType) {
                        Swal.showValidationMessage('Please select one account type.');
                        return false;
                    }

                    const selectedType = selectedAccountType.value
                    return axios.post('/api/clients/current/accounts', `type=${selectedType}`, { headers: { 'content-type': 'application/x-www-form-urlencoded' } })
                        .then(response => {
                            Swal.fire({
                                title: 'Account created! Enjoy!',
                            })
                            setTimeout(() => {
                                window.location.href = './accounts.html';
                            }, 1500);
                        })
                        .catch(error => {
                            console.log(error);
                            Swal.fire({
                                icon: 'error',
                                text: error,
                                confirmButtonColor: '#5b31be93',
                            });
                        });
                }, allowOutsideClick: () => !Swal.isLoading(),
            });
        },
        getLoans() {
            axios.get("/api/clients/current/loans")
                .then(res => {
                    this.loans = res.data
                    this.loans.sort((a, b) => a.id - b.id)
                })
                .catch(error => console.log(error))
        }
    }
}).mount('#app')

