package br.agr.fs.app.tasks;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TaskRequest(@NotBlank @Size(max = 100) String title) {
}
