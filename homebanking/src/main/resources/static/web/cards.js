const { createApp } = Vue
createApp({
    data() {
        return {
            client: [],
            cards: [],
            debit: [],
            credit: [],
            fromDate: [],
            thruDate: [],
            accounts: [],
            cardType: null,
            cardColor: null,
            showForm: false,
            mostrarForm: false,
            idCard: null,
            dateExp: new Date().toISOString().slice(2, 7).replace(/-/g, '/'),
        }
    },
    created() {
        this.loadCards()
        this.loadData()
    },
    methods: {
        loadCards() {
            axios.get('/api/clients/current/cards')
                .then(res => {
                    this.cards = res.data.filter(card => card.onCard)
                    this.debit = this.cards.filter(card => card.cardType == "DEBIT")
                    this.credit = this.cards.filter(card => card.cardType == "CREDIT")
                    this.fromDate = this.cards.map(card => card.fromDate.slice(2, 7).replace(/-/g, '/'))
                    this.thruDate = this.cards.map(card => card.thruDate.slice(2, 7).replace(/-/g, '/'))
                }
                )
        },
        loadData() {
            axios.get('/api/clients/current')
                .then(res => {
                    this.client = res.data.firstName
                    console.log(this.client);
                })
        }
        ,
        toggleForm() {
            this.showForm = !this.showForm
        },
        showDelete() {
            this.mostrarForm = !this.mostrarForm
        },
        createCard() {
            Swal.fire({
                title: 'Do you want to create a new card?',
                inputAttributes: { autocapitalize: 'off', },
                showCancelButton: true,
                confirmButtonText: 'Yes!',
                confirmButtonColor: '#663399',
                showLoaderOnConfirm: true, preConfirm: login => {
                    return axios.post('/api/clients/current/cards', `cardType=${this.cardType}&cardColor=${this.cardColor}`, { headers: { 'content-type': 'application/x-www-form-urlencoded' } })
                        .then(res => {
                            Swal.fire({
                                title: 'Card created! Enjoy!',
                                confirmButtonText: 'OK',
                                confirmButtonColor: '#663399',
                            })
                            setTimeout(() => {
                                window.location.href = './cards.html';
                            }, 1500);
                        })
                        .catch(error => {
                            Swal.fire({
                                icon: 'error',
                                text: error.response.data,
                                confirmButtonColor: '#663399',
                            });
                        });
                }, allowOutsideClick: () => !Swal.isLoading(),
            })
        },
        deleteCard() {
            Swal.fire({
                title: 'Are you sure, the card will be removed',
                inputAttributes: { autocapitalize: 'off', },
                showCancelButton: true,
                confirmButtonText: 'Yes!',
                confirmButtonColor: '#663399',
                showLoaderOnConfirm: true,
                preConfirm: login => {
                    axios.patch('/api/clients/current/deleteCard', `id=${this.idCard}`)
                        .then(res => {
                            Swal.fire({
                                title: 'Card eliminated',
                            })
                            setTimeout(() => {
                                window.location.href = './cards.html';
                            }, 1500);
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
        }, allowOutsideClick: () => !Swal.isLoading(),

    }
}).mount('#app')