package com.example.notesapp.listeners;

public interface ExporterListener {
    void success(String s);

    void fail(String message, String exception);
}
