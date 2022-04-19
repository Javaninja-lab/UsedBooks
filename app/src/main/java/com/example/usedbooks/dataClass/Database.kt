package com.example.usedbooks.dataClass

import android.util.Log
import android.widget.Toast
import java.sql.Connection
import java.sql.DriverManager

class Database() {
    private lateinit var connection: Connection

    private fun Database() {
        connection = DriverManager.getConnection(jdbcUrl, "sql4486484", "Lw6ul89qq8")
    }

    companion object {
        val jdbcUrl = "jdbc:postgresql://sql4.freesqldatabase.com:3306/sql4486484"

        private lateinit var istance : Database

        fun getIstance() : Database {
            if(this::istance.isInitialized){
                istance = Database()
            } else {
                istance = Database()
            }
            return istance
        }

        fun getPrimoNome() : String {
            val query = getIstance().connection.prepareStatement("SELECT * FROM studenti")
            val result = query.executeQuery()
            return result.getString("nome")
        }
    }
}