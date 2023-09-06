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
    }
    // computed: {
    //     prueba() {
    //         // console.log(this.amount);
    //         // console.log(this.accountSelect);
    //         console.log(this.loanSelect.id);
    //         console.log(this.loanSelected);
    //         console.log(this.payments);
    //     }
    // }

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
