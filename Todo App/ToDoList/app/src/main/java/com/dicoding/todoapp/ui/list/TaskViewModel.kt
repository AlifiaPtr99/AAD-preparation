package com.dicoding.todoapp.ui.list

import androidx.lifecycle.*
import androidx.paging.PagedList
import com.dicoding.todoapp.R
import com.dicoding.todoapp.data.Task
import com.dicoding.todoapp.data.TaskRepository
import com.dicoding.todoapp.utils.Event
import com.dicoding.todoapp.utils.TasksFilterType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TaskViewModel(private val taskRepository: TaskRepository) : ViewModel() {

    private val _filter = MutableLiveData<TasksFilterType>()

    val tasks: LiveData<PagedList<Task>> = _filter.switchMap {
        taskRepository.getListTask(it)
    }

    private val _snackbarText = MutableLiveData<Event<Int>>()
    val snackbarText: LiveData<Event<Int>> = _snackbarText

    init {
        _filter.value = TasksFilterType.ALL_TASKS
    }

    fun filter(filterType: TasksFilterType) {
        _filter.value = filterType
    }

    fun completeTask(task: Task, completed: Boolean) = viewModelScope.launch {
        taskRepository.completeTask(task, completed)
        if (completed) {
            _snackbarText.value = Event(R.string.task_marked_complete)
        } else {
            _snackbarText.value = Event(R.string.task_marked_active)
        }
    }

    fun getTaskById(taskId: Int) : LiveData<Task>{
        return taskRepository.getTaskById(taskId = taskId)
    }

    fun insertTask(task: Task) {
        viewModelScope.launch {
            taskRepository.insertTask(task)
        }

    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            taskRepository.deleteTask(task)
        }
    }
}