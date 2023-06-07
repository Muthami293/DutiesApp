package com.example.dutiesapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dutiesapp.model.Chore
import com.example.dutiesapp.model.ChoresDatabaseHandler

class ChoreListActivity : AppCompatActivity() {
    private var adapter: ChoreListAdapter? = null
    private var choreList: ArrayList<Chore>? = null
    private var choreListItems: ArrayList<Chore>? = null
    private var dialogBuilder: AlertDialog.Builder? = null
    private var dialog: AlertDialog? = null



    private var layoutManager: RecyclerView.LayoutManager? = null
    var dbHandler: ChoresDatabaseHandler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chore_list)

        dbHandler = ChoresDatabaseHandler(this)


        choreList = ArrayList<Chore>()
        choreListItems = ArrayList()
        layoutManager = LinearLayoutManager(this)
        adapter = ChoreListAdapter(choreListItems!!, this)


        // setup list = recyclerview
        var recyclerView = findViewById<RecyclerView>(R.id.recyclerViewId)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

        //Load our chores
        choreList = dbHandler!!.readReadChores()
        choreList!!.reverse()




        for (c in choreList!!.iterator()) {

            val chore = Chore()
            chore.choreName = "Chore: ${c.choreName}"
            chore.assignedBy = "Assigned By: ${c.assignedBy}"
            chore.assignedTo = "Assigned to: ${c.assignedTo}"
            chore.id = c.id
            chore.showHumanDate(c.timeAssigned!!)


//            Log.d("====ID=====", c.id.toString())
//            Log.d("====Name=====", c.choreName)
//            Log.d("====Date=====", chore.showHumanDate(c.timeAssigned!!))
//            Log.d("====aTo=====", c.assignedTo)
//            Log.d("====aBy=====", c.assignedTo)
            choreListItems!!.add(chore)


        }
        adapter!!.notifyDataSetChanged()








    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item!!.itemId == R.id.add_menu_button){
            createPopUpDialog()
        }
        return super.onOptionsItemSelected(item)
    }
    @SuppressLint("MissingInflatedId")
    fun createPopUpDialog(){
        var view = layoutInflater.inflate(R.layout.popup, null)

        var choreName = view.findViewById<EditText>(R.id.popEnterChoreId)
        var assignedBy = view.findViewById<EditText>(R.id.popAssignedById)
        var assignedTo = view.findViewById<EditText>(R.id.popAssignedToId)
        var saveButton = view.findViewById<Button>(R.id.popSaveChoreId)

        dialogBuilder = AlertDialog.Builder(this).setView(view)
        dialog = dialogBuilder!!.create()
        dialog!!.show()

        saveButton.setOnClickListener {
            if (!TextUtils.isEmpty(choreName.text.toString().trim())&&
                !TextUtils.isEmpty(assignedBy.text.toString().trim())&&
                !TextUtils.isEmpty(assignedTo.text.toString().trim())
                    ){
                var chore = Chore()
                chore.choreName = choreName.text.toString()
                chore.assignedBy = assignedBy.text.toString()
                chore.assignedTo = assignedTo.text.toString()

                dbHandler!!.createChore(chore)
                dialog!!.dismiss()
                startActivity(Intent(this, ChoreListActivity::class.java))

            }
        }

    }
}