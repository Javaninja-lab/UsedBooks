package com.example.usedbooks.dataClass
import kotlinx.serialization.Serializable


@Serializable
 class  Studente() {

    var id: String=""
    var nome: String=""
    var cognome: String=""
    var email: String=""
    var password: String=""

    constructor(id : String,  nome : String, cognome : String,  username : String,  password : String) : this(){
        this.id=id;
        this.nome=nome;
        this.cognome=cognome;
        this.email=username;
        this.password=password
     }
}
