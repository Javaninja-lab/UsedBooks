package com.example.usedbooks.dataClass

class User
{
    var id: String? = null
    var username: String? = null
    constructor() {}

    constructor(id: String?, username:String?){
        this.id=id
        this.username=username
    }
}