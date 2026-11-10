package br.agr.fs.app.tasks;

public record Task(long id, String title, boolean done) {
    public Task markDone() {
        return new Task(id, title, true);
    }
}
