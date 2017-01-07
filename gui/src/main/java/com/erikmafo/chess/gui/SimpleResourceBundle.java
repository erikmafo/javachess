package com.erikmafo.chess.gui;

import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Created by erikmafo on 07.01.17.
 */
public class SimpleResourceBundle extends ResourceBundle {

    private final Map<String, Object> map;

    public SimpleResourceBundle(Map<String, Object> map) {
        this.map = new HashMap<>(map);
    }

    @Override
    protected Object handleGetObject(String key) {
        return map.get(key);
    }

    @NotNull
    @Override
    public Enumeration<String> getKeys() {
        return Collections.enumeration(map.keySet());
    }
}
