package com.dicoding.todoapp.ui.add

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dicoding.todoapp.R
import com.dicoding.todoapp.data.Task
import com.dicoding.todoapp.databinding.ActivityAddTaskBinding
import com.dicoding.todoapp.ui.ViewModelFactory
import com.dicoding.todoapp.ui.list.TaskViewModel
import com.dicoding.todoapp.utils.DatePickerFragment
import com.dicoding.todoapp.utils.Helper
import java.text.SimpleDateFormat
import java.util.*

class AddTaskActivity : AppCompatActivity(), DatePickerFragment.DialogDateListener {
    private var dueDate: Long = System.currentTimeMillis()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        supportActionBar?.title = getString(R.string.add_task)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_add, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_save -> {
                //TODO 12 : Create AddTaskViewModel and insert new task to database
                val vmFactory = ViewModelFactory.getInstance(this)
                val vmAddTask = ViewModelProvider(this, vmFactory).get(AddTaskViewModel::class.java)

                val title = findViewById<TextView>(R.id.et_title)
                val desc = findViewById<TextView>(R.id.et_desc)
                val txtTitle = title.text.toString().trim()
                val txtDesc = desc.text.toString().trim()

                if (txtTitle.isNotEmpty() && txtDesc.isNotEmpty()) {
                    val task = Task(
                        id = 0,
                        title = txtTitle,
                        description = txtDesc,
                        dueDateMillis = dueDate
                    )
                    vmAddTask.insertTask(task)
                    finish()

                } else {
                    Toast.makeText(this, getString(R.string.empty_task_message), Toast.LENGTH_SHORT).show()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun showDatePicker(view: View) {
        val fragmentDate  = DatePickerFragment()
        fragmentDate.show(supportFragmentManager, "datePicker")
    }

    override fun onDialogDateSet(tag: String?, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        findViewById<TextView>(R.id.tv_due_date).text = dateFormat.format(calendar.time)

        dueDate = calendar.timeInMillis
    }
}