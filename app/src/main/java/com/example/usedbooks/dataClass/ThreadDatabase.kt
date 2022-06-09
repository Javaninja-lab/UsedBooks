package com.example.usedbooks.dataClass

import android.os.Looper
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import java.util.logging.Handler

class ThreadDatabase(


) {

    private lateinit var executor: Executor
    private lateinit var resultHandler: Handler

    fun getMateriali(
        callback: (ArrayList<Materiale>) -> Unit
    ) {

        executor= Executors.newSingleThreadExecutor()
        executor.execute {
            try {
                val response = Database.getMateriali()
                //resultHandler.post { callback(response) }

            } catch (e: Exception) {
                //val errorResult = Result.Error(e)
                //resultHandler.post { callback(errorResult) }
            }
        }
    }

}