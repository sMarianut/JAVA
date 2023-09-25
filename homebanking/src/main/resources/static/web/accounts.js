const { createApp } = Vue
createApp({
    data() {
        return {
            clients: null,
            accounts: [],
            firstName: '',
            creationDate: '',
            number: '',
            balance: Number,
            loans: [],
            selectedAccountTypes: null,
            active: null
        }
    },
    created() {
        this.loadData()
        this.getLoans()


    },
    methods: {
        hoverLoan(loan) {
            this.active = loan;
        },
        unhoverLoan() {
            this.active = null;
        },
        isCardActive(loan) {
            return this.active === loan;
        },
        loadData() {
            axios.get('http://localhost:8080/api/clients/current/accounts')
                .then(res => {
                    this.accounts = res.data.filter(acc => acc.accOn)
                    console.log(this.accounts);
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
        //confirmButtonColor: '#3085d6',
        deleteAcc(id) {
            Swal.fire({
                title: 'Are you sure? The account will be removed.',
                inputAttributes: { autocapitalize: 'off', },
                showCancelButton: true,
                confirmButtonText: 'Yes!',
                confirmButtonColor: '#663399',
                showLoaderOnConfirm: true,
                preConfirm: login => {
                    axios.patch('/api/clients/current/deleteAcc', `id=${id}`)
                        .then(res => {
                            Swal.fire({
                                title: 'Account successfuly eliminated',
                                confirmButtonText: 'OK',
                                confirmButtonColor: '#663399',
                            })
                            setTimeout(() => {
                                window.location.href = './accounts.html';
                            }, 1200);
                        })
                        .catch(error => {
                            Swal.fire({
                                icon: 'error',
                                text: error.response.data,
                                confirmButtonColor: '#663399',
                            });
                        })
                }
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
                showCancelButton: true,
                confirmButtonText: 'Yes!',
                confirmButtonColor: '#663399',
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

