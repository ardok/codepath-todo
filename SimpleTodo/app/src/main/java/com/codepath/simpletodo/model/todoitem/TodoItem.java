package com.codepath.simpletodo.model.todoitem;

import java.io.Serializable;
import java.util.Calendar;
import java.util.UUID;

public class TodoItem implements Serializable {
    // TODO use parcelable when object gets more complicated for faster processing time

    private Long id;
    private UUID uuid;
    private String text;
    private Calendar date;

    public TodoItem(Long id, String text, Calendar date, UUID uuid) {
        this.id = id;
        this.text = text;
        this.date = date;
        this.uuid = uuid;
    }

    public TodoItem(String text) {
        this(null, text, Calendar.getInstance(), UUID.randomUUID());
    }

    public Long getId() {
        return id;
    }

    public UUID getUUID() {
        return uuid;
    }

    public String getText() {
        return text;
    }

    public Calendar getDate() {
        return date;
    }

    public void setText(String text) {
        this.text = text;
    }
}
