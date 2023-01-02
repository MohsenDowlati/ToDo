package com.example.todo;

import androidx.annotation.NonNull;

public class Tasks {
    private final String title , description;

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public boolean isCompleteness() {
        return completeness;
    }

    public void setCompleteness(boolean completeness) {
        this.completeness = completeness;
    }

    private boolean completeness;

    public int getId() {
        return id;
    }

    private final int id;
    public Tasks(int id,String title, String description, boolean completeness) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.completeness = completeness;
    }

    @NonNull
    @Override
    public String toString() {
        return "Tasks{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", completeness=" + completeness +
                ", id=" + id +
                '}';
    }
}