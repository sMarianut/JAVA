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
            showForm: false
        }
    },
    created() {
        this.loadCards()
    },
    methods: {
        loadCards() {
            axios.get('/api/clients/current/cards')
                .then(res => {
                    this.clients = res
                    this.cards = res.data
                    console.log(this.cards);
                    this.debit = this.cards.filter(card => card.cardType == "DEBIT")
                    this.credit = this.cards.filter(card => card.cardType == "CREDIT")
                    this.fromDate = this.cards.map(card => card.fromDate.slice(2, 7).replace(/-/g, '/'))
                    this.thruDate = this.cards.map(card => card.thruDate.slice(2, 7).replace(/-/g, '/'))
                }
                )
        },
        toggleForm() {
            this.showForm = true
        },
        createCard() {
            Swal.fire({
                title: 'Quieres crear una cuenta?',
                inputAttributes: { autocapitalize: 'off', },
                showCancelButton: true, confirmButtonText: 'Si!',
                showLoaderOnConfirm: true, preConfirm: login => {
                    return axios.post('/api/clients/current/cards', `cardType=${this.cardType}&cardColor=${this.cardColor}`, { headers: { 'content-type': 'application/x-www-form-urlencoded' } })
                        .then(response => { window.location.href = './cards.html' })
                        .catch(error => {
                            Swal.fire({
                                icon: 'error',
                                text: error.response.data,
                                confirmButtonColor: '#5b31be93',
                            });
                        });
                }, allowOutsideClick: () => !Swal.isLoading(),
            })
        }

    }
}).mount('#app')