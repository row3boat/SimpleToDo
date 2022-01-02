package com.example.simpletodo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.FileUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import java.io.IOException
import java.nio.charset.Charset


class MainActivity : AppCompatActivity() {

    var listOfTasks = mutableListOf<String>()
    lateinit var adapter: TaskItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val onLongClickListener = object : TaskItemAdapter.OnLongClickListener {
            override fun onItemLongClicked(position: Int) {
                //1. Remove item from list
                listOfTasks.removeAt(position)
                //2. Notify adapter something has changed
                adapter.notifyDataSetChanged()

                saveItems()
            }
        }


//        findViewById<Button>(R.id.button).setOnClickListener{
//            Log.i("Rowan", "User clicked on button")
//
//        }

        loadItems()

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        adapter = TaskItemAdapter(listOfTasks, onLongClickListener)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        //Set up the button and input field so that the user can enter a task and add it to the list.

        val inputTextField = findViewById<EditText>(R.id.addTaskField)


        findViewById<Button>(R.id.button).setOnClickListener {
            //1. Grab input text into @id/addTaskField
            val userInputtedTask = inputTextField.text.toString()

            //2. Add string to list of tasks.
            listOfTasks.add(userInputtedTask)
            adapter.notifyItemInserted(listOfTasks.size - 1)

            //3. Reset text field
            inputTextField.setText("")

            saveItems()
        }
    }

    //Save user inputted data
    //Save data by writing and reading from a file

    //Create a method to get the file we need
    fun getDataFile() : File {
        return File(filesDir, "data.txt")
    }

    //Load the items by reading every line in the data file
    fun loadItems() {
        try {
            listOfTasks =
                org.apache.commons.io.FileUtils.readLines(getDataFile(), Charset.defaultCharset())
        } catch (ioException : IOException){
            ioException.printStackTrace()
        }
    }

    //Save items by writing them into our data file
    fun saveItems() {
        try{
            org.apache.commons.io.FileUtils.writeLines(getDataFile(), listOfTasks)
        } catch (ioException : IOException) {
            ioException.printStackTrace()
        }
    }

}