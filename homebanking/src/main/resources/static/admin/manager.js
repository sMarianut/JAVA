const { createApp } = Vue

createApp({
    data() {
        return {
            clients: [],
            firstName: '',
            lastName: '',
            email: '',
            jsonR: null,
            showLoan: true,
            loanName: "",
            maxAmount: 0,
            payments: [],
            interest: 0

        }
    },
    created() {
        this.cargarDatos()

    },
    methods: {
        cargarDatos() {
            axios.get('http://localhost:8080/rest/clients')
                .then(res => {
                    this.clients = res.data._embedded.clients
                    this.jsonR = JSON.stringify(res.data, null, 1)

                })
                .catch(error => console.error(error))
        },
        addClient() {
            let clientNew = { firstName: this.firstName, lastName: this.lastName, email: this.email }
            axios.post('http://localhost:8080/rest/clients', clientNew)
                .then(res => {
                    this.firstName = '',
                        this.lastName = '',
                        this.email = '',
                        this.cargarDatos();


                })
                .catch(error => console.error(error))

        },
        createLoan() {
            let object = {
                "name": this.loanName,
                "maxAmount": this.maxAmount,
                "payments": this.payments,
                "interest": this.interest
            }
            Swal.fire({
                title: 'Do you want to create this loan?',
                inputAttributes: { autocapitalize: 'off', },
                showCancelButton: true, confirmButtonText: 'Yes!',
                showLoaderOnConfirm: true, preConfirm: login => {
                    console.log(object);
                    return axios.post('/api/createLoan', object)
                        .then(response => {
                            Swal.fire({
                                icon: 'success',
                                title: "Loan created PETON",
                                confirmButtonColor: '#5b31be93',
                            })
                        })
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
        verifier() {
            if (this.firstName && this.lastName && this.email) {
                this.addClient();
            } else {
                window.alert("Please fill in all required fields.");

            }
        },
        showLoanForm() {
            this.showLoan = !this.showLoan
        }
    }
}).mount('#app');
