package com.example.todoapp.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@Entity
@Table(name = "todo_items")
public class ToDoItem implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Description is required")
    private String description;

    private Boolean isComplete;
    private Instant createdAt;
    private Instant updatedAt;

    @Override
    public String toString() {
        return String.format("TodoItem{id=%d, description='%s', isComplete='%s', createdAt='%s', updatedAt='%s'}",
        id, description, isComplete, createdAt, updatedAt);
    }

    public String getFormattedCreatedAt() {
        return formatInstant(createdAt);
    }

    public String getFormattedUpdatedAt() {
        return formatInstant(updatedAt);
    }

    private String formatInstant(Instant instant) {
        if (instant == null) {
            return "";
        }
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zonedDateTime = instant.atZone(zoneId);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
        return formatter.format(zonedDateTime);
    }

    @ManyToOne
    @JoinColumn(name = "category_id")
    private ListCategory category;

}
