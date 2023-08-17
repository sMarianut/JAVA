const { createApp } = Vue
createApp({
    data() {
        return {
            client: [],
            cards: [],
            debit: [],
            credit: [],
            fromDate: [],
            thruDate: []
        }
    },
    created() {
        this.loadCards()
    },
    methods: {
        loadCards() {
            axios.get('http://localhost:8080/api/clients/1')
                .then(res => {
                    this.client = res.data
                    this.cards = res.data.cards
                    console.log(this.cards);
                    this.debit = this.cards.filter(card => card.cardType == "DEBIT")
                    this.credit = this.cards.filter(card => card.cardType == "CREDIT")
                    this.fromDate = this.cards.map(card => card.fromDate.slice(2, 7).replace(/-/g, '/'))
                    this.thruDate = this.cards.map(card => card.thruDate.slice(2, 7).replace(/-/g, '/'))


                }
                )
        }
    }
}).mount('#app')