package com.maricoolsapps.utils.others

import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity

abstract class ActionModeImpl
 constructor(val activity: AppCompatActivity):ActionMode.Callback {

     fun start() = activity.startActionMode(this)

    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
         performAction(mode, item)
        return true
    }

    abstract fun performAction(mode: ActionMode?, item: MenuItem?)

    override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
         createActionMode(mode, menu)
        return true
    }

    abstract fun createActionMode(mode: ActionMode?, menu: Menu?)

    override fun onPrepareActionMode(p0: ActionMode?, p1: Menu?): Boolean {
        return false
    }

    override fun onDestroyActionMode(mode: ActionMode?) {
        DestroyActionMode(mode)
    }

    abstract fun DestroyActionMode(mode: ActionMode?)

}