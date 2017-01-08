package com.erikmafo.chess.gui.utils;

import javafx.application.Application;
import javafx.util.Callback;

import javax.inject.Inject;
import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Map;

/**
 * Created by erikmafo on 08.01.17.
 */
public class FXControllerFactory implements Callback<Class<?>,Object> {


    private final Map<String, Object> context;

    public FXControllerFactory(Map<String, Object> context) {
        this.context = context;
    }


    @Override
    public Object call(Class<?> clazz) {

        Object instance = createInstance(clazz);

        for (final Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(Inject.class)) {
                String fieldName = field.getName();
                Object value = context.get(fieldName);
                if (value != null) {
                    injectIntoField(field, instance, value);
                }
            }
        }

        return instance;
    }

    private Object createInstance(Class<?> clazz)  {
        try {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Unable to create instance of " + clazz, e);
        }
    }


    private void injectIntoField(Field field, Object instance, Object value) {
        AccessController.doPrivileged((PrivilegedAction<?>) () -> {
            boolean wasAccessible = field.isAccessible();
            try {
                field.setAccessible(true);
                field.set(instance, value);
                return null;
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                throw new IllegalStateException("Cannot set field: " + field + " with value " + value, ex);
            } finally {
                field.setAccessible(wasAccessible);
            }
        });
    }


}
