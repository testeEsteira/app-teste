package br.agr.fs.app.tasks;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class TaskServiceTest {

    private final TaskService service = new TaskService();

    @Test
    void createAssignsIncrementalIds() {
        assertThat(service.create("a").id()).isEqualTo(1);
        assertThat(service.create("b").id()).isEqualTo(2);
    }

    @Test
    void findAllReturnsOrdered() {
        service.create("a");
        service.create("b");
        assertThat(service.findAll()).extracting(Task::title).containsExactly("a", "b");
    }

    @Test
    void completeMarksDone() {
        Task t = service.create("a");
        assertThat(service.complete(t.id()).done()).isTrue();
        assertThat(service.get(t.id()).done()).isTrue();
    }

    @Test
    void deleteRemoves() {
        Task t = service.create("a");
        service.delete(t.id());
        assertThat(service.findAll()).isEmpty();
    }

    @Test
    void missingTaskThrows() {
        assertThatThrownBy(() -> service.get(99)).isInstanceOf(TaskNotFoundException.class);
        assertThatThrownBy(() -> service.complete(99)).isInstanceOf(TaskNotFoundException.class);
        assertThatThrownBy(() -> service.delete(99)).isInstanceOf(TaskNotFoundException.class);
    }
}
