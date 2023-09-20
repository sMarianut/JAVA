const { createApp } = Vue;
createApp({
    data() {
        return {
            loans: [],
            accountDest: null,
            amount: null,
            accountSelect: {},
            loanSelect: {},
            loanSelected: {},
            payments: {},
        }
    },
    created() {
        this.loadAcc()
        this.loadLoans()
    },
    methods: {
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
                    console.log(this.loans);
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
                title: 'Do you want to apply to this loan, bro?',
                inputAttributes: { autocapitalize: 'off', },
                showCancelButton: true, confirmButtonText: 'Yes!',
                showLoaderOnConfirm: true, preConfirm: login => {
                    console.log(object);
                    return axios.post('/api/loans', object)
                        .then(response => { window.location.href = './loanapp.html' })
                        .catch(error => {
                            Swal.fire({
                                icon: 'error',
                                title: "Couldn't apply",
                                text: error.response.data,
                                confirmButtonColor: '#5b31be93',
                            });
                            console.log(error.response);
                        });
                }, allowOutsideClick: () => !Swal.isLoading(),
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
