package com.example.model

enum class Priorities{
    Low,
    Medium,
    High,
    Vital
}

data class Task(
    val name: String,
    val description: String,
    val priority: Priorities
)

fun Task.addRow() = """
        <tr>
        <td>$name</td>
        <td>$description</td>
        <td>$priority</td>
        </tr>
        """.trimIndent()

fun List<Task>.taskAsTable() = this.joinToString(
    separator = "\n",
    prefix = "<table rules=\"all\">",
    postfix = "</table>",
    transform = {
        it.addRow()
    }
)