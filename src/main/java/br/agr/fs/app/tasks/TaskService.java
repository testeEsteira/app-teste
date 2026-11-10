package br.agr.fs.app.tasks;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Service;

/** Armazenamento em memória: suficiente para validar a esteira (sem banco). */
@Service
public class TaskService {

    private final Map<Long, Task> store = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong();

    public Task create(String title) {
        Task task = new Task(sequence.incrementAndGet(), title, false);
        store.put(task.id(), task);
        return task;
    }

    public List<Task> findAll() {
        return store.values().stream().sorted((a, b) -> Long.compare(a.id(), b.id())).toList();
    }

    public Task get(long id) {
        Task task = store.get(id);
        if (task == null) {
            throw new TaskNotFoundException(id);
        }
        return task;
    }

    public Task complete(long id) {
        return store.compute(id, (k, v) -> {
            if (v == null) {
                throw new TaskNotFoundException(id);
            }
            return v.markDone();
        });
    }

    public void delete(long id) {
        if (store.remove(id) == null) {
            throw new TaskNotFoundException(id);
        }
    }
}
