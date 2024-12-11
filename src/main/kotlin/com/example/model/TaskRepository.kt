package com.example.model

object TaskRepository {

    private val tasks = mutableListOf(
        Task("cleaning", "Clean the house", Priorities.Low),
        Task("gardening", "Mow the lawn", Priorities.Medium),
        Task("shopping", "Buy the groceries", Priorities.High),
        Task("painting", "Paint the fence", Priorities.Medium)
    )

    fun allTasks()= tasks

    fun tasksByPriority(priorities: Priorities) = tasks.filter {
        it.priority == priorities
    }

    fun tasksByName(name: String) = tasks.find {
        it.name.equals(other = name, ignoreCase = true)
    }

    fun addTask(task: Task){
        if(tasksByName(task.name) != null){
            throw IllegalStateException("Duplicate tasks cannot be used")
        }

        tasks.add(task)
    }
}

