const { createApp } = Vue;
createApp({
    data() {
        return {
            loans: [],
            accountDest: null,
            amount: null,
            loanSelect: {},
            accountSelect: {},
            payments: {},
            loan1: true,
            loan2: false,
            loan3: false


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
        showLoan1() {
            this.loan1 = false
            this.loan2 = true
            this.loan3 = false
        },
        showLoan2() {
            this.loan1 = false
            this.loan2 = false
            this.loan3 = true
        },
        showLoan3() {
            this.loan1 = true
            this.loan2 = false
            this.loan3 = false
        }
    },
    computed: {
        prueba() {
            console.log(this.amount);
            console.log(this.accountSelect);
        }
    }

}).mount('#app');



// data(){
//     return{
//         loans:[],
//         originAccounts:null,
//         amount:null,
//         payments:null,
//         selectLoan:{},
//         selectOriginAccount:{},
//         selectPayments:{}
//     }
// },
// created(){
//   this.loadData()
//   this.loadLoans()
// },
// computed:{
//     prueba(){
//         console.log(this.selectOriginAccount);
//         console.log(this.selectLoan);
//     }
// },
// methods:{
//     alerta(){
//         let mensaje;
//         let opcion = confirm("Do you want to create a new loan?");
//         let object = {
//             "id": this.id,
//             "amount": this.amount,
//             "payments": this.payments,
//             "destinationAccount":this.destinationAccount
//         }
//         console.log(object);
//         console.log("Hola");
//         if (opcion == true) {
//             axios.post('/api/loans', object)
//             .then( response => {
//               location.href ="/web/public/pages/accounts.html"})
//               .catch(error => {
//                 console.log(error.response);
//                 window.alert(error.response.data)
//         })
//         } else {
//             mensaje = "Cancel";
//         }

//     },
//     loadData(){
//         axios.get('/api/clients/current/accounts')
//         .then(response=>{
//             this.originAccounts = response.data
//             console.log(this.originAccounts);
//         }).catch(error => console.log(error))
//     },
//     loadLoans(){
//         axios.get('/api/loans')
//         .then(response=>{
//             this.loans = response.data
//             console.log(this.loans);
//         }).catch(error => console.log(error))
//     },
