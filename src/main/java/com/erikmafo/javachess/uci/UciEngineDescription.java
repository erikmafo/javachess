package com.erikmafo.javachess.uci;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by erikf on 13.07.2016.
 */
public class UciEngineDescription {

    private final String name;
    private final String author;
    private final List<Option> options;

    public UciEngineDescription(String name, String author, List<Option> options) {
        this.name = name;
        this.author = author;
        this.options = options;
    }

    public String getAuthor() {
        return author;
    }

    public String getName() {
        return name;
    }

    public List<Option> getOptions() {
        return new ArrayList<>(options);
    }
}
