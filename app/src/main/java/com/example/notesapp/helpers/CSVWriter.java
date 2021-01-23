package com.example.notesapp.helpers;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

import static com.example.notesapp.util.Constants.DEFAULT_ESCAPE_CHARACTER;
import static com.example.notesapp.util.Constants.DEFAULT_LINE_END;
import static com.example.notesapp.util.Constants.DEFAULT_QUOTE_CHARACTER;
import static com.example.notesapp.util.Constants.DEFAULT_SEPARATOR;
import static com.example.notesapp.util.Constants.NO_ESCAPE_CHARACTER;
import static com.example.notesapp.util.Constants.NO_QUOTE_CHARACTER;

public class CSVWriter {
    private PrintWriter printWriter;
    private char separator;
    private char escapeChar;
    private String lineEnd;
    private char quoteChar;

    public CSVWriter(Writer writer) {
        this(writer, DEFAULT_SEPARATOR, DEFAULT_QUOTE_CHARACTER,
                DEFAULT_ESCAPE_CHARACTER, DEFAULT_LINE_END);
    }

    public CSVWriter(Writer writer, char separator, char quotechar, char escapechar, String lineEnd) {
        this.printWriter = new PrintWriter(writer);
        this.separator = separator;
        this.quoteChar = quotechar;
        this.escapeChar = escapechar;
        this.lineEnd = lineEnd;
    }

    public void writeNext(String[] nextLine) {
        if (nextLine == null)
            return;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < nextLine.length; i++) {
            if (i != 0) {
                sb.append(separator);
            }
            String nextElement = nextLine[i];
            if (nextElement == null)
                continue;
            if (quoteChar != NO_QUOTE_CHARACTER)
                sb.append(quoteChar);
            for (int j = 0; j < nextElement.length(); j++) {
                char nextChar = nextElement.charAt(j);
                if (escapeChar != NO_ESCAPE_CHARACTER && nextChar == quoteChar) {
                    sb.append(escapeChar).append(nextChar);
                } else if (escapeChar != NO_ESCAPE_CHARACTER && nextChar == escapeChar) {
                    sb.append(escapeChar).append(nextChar);
                } else {
                    sb.append(nextChar);
                }
            }
            if (quoteChar != NO_QUOTE_CHARACTER)
                sb.append(quoteChar);
        }
        sb.append(lineEnd);
        printWriter.write(sb.toString());
    }

    public void close() throws IOException {
        printWriter.flush();
        printWriter.close();
    }

    public void flush() throws IOException {
        printWriter.flush();
    }
}