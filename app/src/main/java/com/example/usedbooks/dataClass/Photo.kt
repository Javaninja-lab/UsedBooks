package com.example.usedbooks.dataClass

import java.io.Serializable
import java.util.*

class Photo(val localUri:String ="", var remoteUri :String ="", val description: String ="", val dateTaken:Date= Date(), var id: String= "") : Serializable