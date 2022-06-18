package com.example.usedbooks.customView

import android.content.Context
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.drawable.Drawable
import android.util.TypedValue
import android.view.View
import android.widget.ProgressBar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.RecyclerView
import com.example.usedbooks.dataClass.Database
import com.example.usedbooks.dataClass.Materiale
import kotlin.concurrent.thread


class PersonalProgressBar(val contesto: Context, val layout : ConstraintLayout) : ProgressBar(contesto, null, android.R.attr.progressBarStyleLarge) {
    init {
        val progressDrawable: Drawable = this.indeterminateDrawable.mutate()
        val typedValue = TypedValue()
        contesto.theme.resolveAttribute(android.R.attr.colorPrimary, typedValue, true)
        val color = typedValue.data
        progressDrawable.colorFilter = BlendModeColorFilter(color, BlendMode.SRC_ATOP)
        this.progressDrawable = progressDrawable

        this.id = View.generateViewId()

        val set = ConstraintSet()
        layout.addView(this,0)
        set.clone(layout)
        set.connect(this.id, ConstraintSet.BOTTOM, layout.id, ConstraintSet.BOTTOM, 0)
        set.connect(this.id, ConstraintSet.END, layout.id, ConstraintSet.END, 0)
        set.connect(this.id, ConstraintSet.START, layout.id, ConstraintSet.START, 0)
        set.connect(this.id, ConstraintSet.TOP, layout.id, ConstraintSet.TOP, 0)
        set.applyTo(layout)

        this.layoutParams.height = 500
        this.layoutParams.width = 500
        this.requestLayout()
    }

    fun caricamento(runnable: Runnable) {
        thread(start = true) {
            runnable.run()
        }
    }

    fun caricamento(response : ArrayList<Materiale>, recyclerView : RecyclerView) {
        thread(start = true) {
            response.clear()
            for (materiale in Database.getMateriali()) {
                response.add(materiale)
            }
            this.post {
                recyclerView.visibility = View.VISIBLE
                this.visibility = View.GONE
                recyclerView.adapter?.notifyDataSetChanged()
            }
        }
    }
}