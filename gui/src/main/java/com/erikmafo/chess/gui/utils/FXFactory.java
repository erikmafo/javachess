package com.erikmafo.chess.gui.utils;

import javafx.application.Application;
import javafx.util.Callback;

import javax.annotation.PreDestroy;
import javax.inject.Inject;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by erikmafo on 08.01.17.
 */
public class FXFactory implements Callback<Class<?>,Object> {


    private final Map<String, Object> context;

    private final List<WeakReference> instances = new ArrayList<>();

    public FXFactory(Map<String, Object> context) {
        this.context = context;
    }

    /**
     * Creates a new instance of the given class and injects any field
     * annotated with {@link Inject} that has a name matching a key
     * from the provided context map with the corresponding value.
     *
     * @param clazz the class to create a new instance of
     * @return an instance of the given class
     */
    @Override
    public Object call(Class<?> clazz) {

        Object instance = createInstance(clazz);

        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(Inject.class)) {
                String fieldName = field.getName();
                Object value = context.get(fieldName);
                if (value != null) {
                    injectIntoField(field, instance, value);
                }
            }
        }


        instances.add(new WeakReference(instance));

        return instance;
    }


    /**
     * Invokes any no-arg method annotated with {@link PreDestroy} on all instances that have been
     * created by this factory and are still alive.
     */
    public void destroy() {

        for (WeakReference reference : instances) {
            Object instance = reference.get();
            if (instance != null) {
                for (Method method : instance.getClass().getDeclaredMethods()) {
                    if (method.getParameterCount() == 0 && method.isAnnotationPresent(PreDestroy.class)) {
                        invokeMethod(instance, method);
                    }
                }
            }
        }
    }



    private void invokeMethod(Object instance, Method method) {
        try {
            method.invoke(instance);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
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
