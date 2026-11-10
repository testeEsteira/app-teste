package br.agr.fs.app.tasks;

public class TaskNotFoundException extends RuntimeException {
    public TaskNotFoundException(long id) {
        super("Task " + id + " not found");
    }
}
