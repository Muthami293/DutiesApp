package com.example.dutiesapp

import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.dutiesapp.model.Chore
import com.example.dutiesapp.model.ChoresDatabaseHandler

class ChoreListAdapter(private val list: ArrayList<Chore>, private val context: Context)
    : RecyclerView.Adapter<ChoreListAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        inflate the view of our xml
        val view = LayoutInflater.from(context).inflate(R.layout.list_row, parent, false)
        return ViewHolder(view, context, list)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(list[position])
    }

    override fun getItemCount(): Int {
//        how many items are in the list
        return list.size
    }

//    will link the information in the database to our recycler view

    inner class ViewHolder(itemView: View, context: Context, list: ArrayList<Chore>) : RecyclerView.ViewHolder(itemView), OnClickListener{
//        create objects out of our view
    //        instantiate
        var mContext = context
        var mList = list
        var choreName = itemView.findViewById<TextView>(R.id.listChoreName) /*our chore name will come from our listchorename*/
        var assignedBy = itemView.findViewById<TextView>(R.id.listAssignedBy)
        var assignedTo = itemView.findViewById<TextView>(R.id.listAssignedTo)
        var assignedDate = itemView.findViewById<TextView>(R.id.listDate)
        var deleteButton = itemView.findViewById<Button>(R.id.listDeleteButton)
        var editButton = itemView.findViewById<Button>(R.id.listEditButton)


        fun bindView(chore: Chore){
            choreName.text = chore.choreName /*matching with class Chore*/
            assignedBy.text = chore.assignedBy
            assignedTo.text = chore.assignedTo
            assignedDate.text = chore.showHumanDate(System.currentTimeMillis())
            // assignedDate.text = chore.timeAssigned.toString() TODO("Not yet implemented")

            deleteButton.setOnClickListener(this)
            editButton.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            var mPosition: Int = adapterPosition
            var chore = mList[mPosition]
            when(v!!.id){

                deleteButton.id ->{
                    deleteChore(chore.id!!)
                    mList.removeAt(adapterPosition)
                    notifyItemRemoved(adapterPosition)
                }
                editButton.id ->{
                    editChore(chore)
                }

            }
        }

        fun deleteChore(id : Int){
            var db: ChoresDatabaseHandler = ChoresDatabaseHandler(mContext)
            db.deleteChore(id)
        }

        fun editChore(chore: Chore) {
            var dialogBuilder: AlertDialog.Builder? = null
            var dialog: AlertDialog? = null
            var dbHandler: ChoresDatabaseHandler = ChoresDatabaseHandler(context)
            var view = LayoutInflater.from(context).inflate(R.layout.popup, null)

            var choreName = view.findViewById<EditText>(R.id.popEnterChoreId)
            var assignedBy = view.findViewById<EditText>(R.id.popAssignedById)
            var assignedTo = view.findViewById<EditText>(R.id.popAssignedToId)
            var saveButton = view.findViewById<Button>(R.id.popSaveChoreId)

            dialogBuilder = AlertDialog.Builder(context).setView(view)
            dialog = dialogBuilder!!.create()
            dialog!!.show()

            saveButton.setOnClickListener {
                if (!TextUtils.isEmpty(choreName.text.toString().trim())&&
                    !TextUtils.isEmpty(assignedBy.text.toString().trim())&&
                    !TextUtils.isEmpty(assignedTo.text.toString().trim())
                ){
//                    var chore = Chore()
                    chore.choreName = choreName.text.toString()
                    chore.assignedBy = assignedBy.text.toString()
                    chore.assignedTo = assignedTo.text.toString()

                    dbHandler!!.updateChore(chore)
                    notifyItemChanged(adapterPosition, chore)
                    dialog!!.dismiss()
                }
            }
        }

    }
}