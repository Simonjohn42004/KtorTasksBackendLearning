package com.example

import com.example.model.Priorities
import com.example.model.Task
import com.example.model.TaskRepository
import com.example.model.taskAsTable
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.lang.IllegalArgumentException

fun Application.configureRouting() {

    routing {

        staticResources("/task-ui", "task-ui")

        route("/tasks"){
            get(){

                val tasks = TaskRepository.allTasks()
                call.respondText(
                    contentType = ContentType.parse("text/html"),
                    text = tasks.taskAsTable()
                )
            }


            get("/byPriority/{priority}"){
                val priorityAsText = call.parameters["priority"]
                if(priorityAsText == null ){
                    call.respond(HttpStatusCode.BadRequest)
                    return@get
                }
                try {
                    val priority = Priorities.valueOf(priorityAsText)
                    val tasks = TaskRepository.tasksByPriority(priority)
                    if(tasks.isNotEmpty()){
                        call.respondText(
                            contentType = ContentType.parse("text/html"),
                            text = tasks.taskAsTable()
                        )
                    }else{
                        call.respond(HttpStatusCode.NotFound)
                    }
                }catch (
                    ex: IllegalArgumentException
                ){
                    call.respond(HttpStatusCode.BadRequest)
                }
            }


            get("/byName/{name}"){
                val name = call.parameters["name"]

                if(name == null){
                    call.respond(HttpStatusCode.BadRequest)
                    return@get
                }

                val task = TaskRepository.tasksByName(name)
                if(task == null){
                    call.respond(HttpStatusCode.NotFound)
                    return@get
                }

                call.respondText(
                    contentType = ContentType.parse("text/html"),
                    text = listOf(task).taskAsTable()
                )

            }


            post(){
                val formContent = call.receiveParameters()

                val parameters = Triple(
                    formContent["Name"] ?: "",
                    formContent["Description"] ?: "",
                    formContent["Priority"] ?: ""
                )

                if(parameters.toList().any { it.isEmpty() }){
                    call.respond(HttpStatusCode.BadRequest)
                    return@post
                }

                try {
                    val priority = Priorities.valueOf(parameters.third)
                    val task = Task(
                        name = parameters.first,
                        description = parameters.second,
                        priority = priority
                    )
                    TaskRepository.addTask(task = task)
                    call.respond(HttpStatusCode.NoContent)
                }catch (e: IllegalArgumentException){
                    call.respond(HttpStatusCode.BadRequest)
                }catch (e: IllegalStateException){
                    call.respond(HttpStatusCode.BadRequest)
                }

            }



        }


    }
}
