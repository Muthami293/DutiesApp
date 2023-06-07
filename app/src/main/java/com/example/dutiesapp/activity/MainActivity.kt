package com.example.dutiesapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.dutiesapp.ChoreListActivity
import com.example.dutiesapp.R
import com.example.dutiesapp.model.Chore
import com.example.dutiesapp.model.ChoresDatabaseHandler

class MainActivity : AppCompatActivity() {
    lateinit var enterChore: EditText
    lateinit var assignedBy: EditText
    lateinit var assignedTo: EditText
    var dbHandler : ChoresDatabaseHandler? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        dbHandler = ChoresDatabaseHandler(this)

        checkDB()




        var saveAchore = findViewById<Button>(R.id.saveChoreId)
        saveAchore.setOnClickListener {
            enterChore = findViewById(R.id.enterChoreId)
            assignedBy = findViewById(R.id.assignedById)
            assignedTo = findViewById(R.id.assignedToId)
            if (!TextUtils.isEmpty(enterChore.text.toString()) &&
                !TextUtils.isEmpty(assignedBy.text.toString()) &&
                !TextUtils.isEmpty(assignedTo.text.toString())){

                var chore = Chore()

                chore.choreName = enterChore.text.toString()
                chore.assignedTo = assignedTo.text.toString()
                chore.assignedBy = assignedBy.text.toString()

                saveToDB(chore)
                Toast.makeText(this, "Chore saved to database", Toast.LENGTH_LONG).show()
                startActivity(Intent(this, ChoreListActivity::class.java))
            }else{
                Toast.makeText(this, "Please enter a chore", Toast.LENGTH_LONG).show()
            }
        }

//        var chore = Chore()
//        chore.choreName = "Clean room"
//        chore.assignedTo = "James"
//        chore.assignedBy = "Alice"
//
//        dbHandler!!.createChore(chore)
    }
    fun saveToDB(chore: Chore){
        dbHandler!!.createChore(chore)
    }

    fun checkDB(){
        if (dbHandler!!.getChoreCount() > 0){
            startActivity(Intent(this, ChoreListActivity::class.java))

        }
    }
}