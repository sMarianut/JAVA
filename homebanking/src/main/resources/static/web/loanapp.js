const { createApp } = Vue;
createApp({
    data() {
        return {
            loans: [],
            accountDest: null,
            amount: 0,
            accountSelect: {},
            loanSelect: {},
            loanSelected: {},
            payments: {},
            finalAmount: 0,
            active: null
        }
    },
    created() {
        this.loadAcc()
        this.loadLoans()
    },
    methods: {
        hoverLoan(loanSelect) {
            this.active = loanSelect;
        },
        unhoverLoan() {
            this.active = null;
        },
        isCardActive(loanSelect) {
            return this.active === loanSelect;
        },
        loadAcc() {
            axios.get('/api/clients/current/accounts')
                .then(response => {
                    this.accountDest = response.data
                    this.accountDest.sort((a, b) => a.id - b.id)
                    console.log(this.accountDest);
                })
        },
        loadLoans() {
            axios.get('/api/loans')
                .then(response => {
                    this.loans = response.data
                }).catch(error => console.log(error))
        },
        appLoan() {
            const object = {
                "id": this.loanSelect.id,
                "amount": this.amount,
                "paymentsReq": this.payments,
                "accountDest": this.accountSelect
            }
            Swal.fire({
                title: 'Do you want to apply this loan?',
                inputAttributes: { autocapitalize: 'off', },
                showCancelButton: true,
                confirmButtonText: 'Yes!',
                confirmButtonColor: '#663399',
                showLoaderOnConfirm: true, preConfirm: login =>
                    setTimeout(() => {
                        axios.post('/api/loans', object)
                            .then(response => {
                                Swal.close();

                                Swal.fire({
                                    title: 'Loan App! Enjoy!',
                                    confirmButtonText: 'OK',
                                    confirmButtonColor: '#663399',
                                });

                                setTimeout(() => {
                                    window.location.href = './cards.html';
                                }, 1500);
                            })
                            .catch(error => {
                                Swal.close();

                                Swal.fire({
                                    icon: 'error',
                                    text: error.response.data,
                                    confirmButtonColor: '#663399',
                                });

                                console.log(error.response);
                            });
                    }, 1000)
            })
        },
        logout() {
            axios.post('/api/logout')
        }
    },
    computed: {
        calculateInterest() {
            if (this.payments == 3) {
                this.finalAmount = this.amount + (this.amount * (0.05 + (this.loanSelect.interest / 100)))
                return this.finalAmount;
            }
            else if (this.payments == 6) {
                this.finalAmount = this.amount + (this.amount * (0.10 + (this.loanSelect.interest / 100)))
                return this.finalAmount;
            }
            else if (this.payments == 12) {
                this.finalAmount = this.amount + (this.amount * (0.20 + (this.loanSelect.interest / 100)))
                return this.finalAmount;
            }
            else if (this.payments == 24) {
                this.finalAmount = this.amount + (this.amount * (0.45 + (this.loanSelect.interest / 100)))
                return this.finalAmount;
            }
            else if (this.payments == 36) {
                this.finalAmount = this.amount + (this.amount * (0.65 + (this.loanSelect.interest / 100)))
                return this.finalAmount;
            }
            else if (this.payments == 48) {
                this.finalAmount = this.amount + (this.amount * (0.70 + (this.loanSelect.interest / 100)))
                return this.finalAmount;
            }
            else if (this.payments == 60) {
                this.finalAmount = this.amount + (this.amount * (0.75 + (this.loanSelect.interest / 100)))
                return this.finalAmount;
            } else { return 0 };
        }
    }

}).mount('#app');
