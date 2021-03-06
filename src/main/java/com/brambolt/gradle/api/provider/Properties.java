package com.brambolt.gradle.api.provider;

import java.util.List;
import java.util.Map;

import org.gradle.api.Project;
import org.gradle.api.provider.ListProperty;
import org.gradle.api.provider.MapProperty;
import org.gradle.api.provider.Property;

public class Properties {

    public static <T> Property<T> createProperty(Project project, Class<T> cls) {
        return project.getObjects().property(cls);
    }

    public static <T> Property<T> createProperty(Project project, Class<T> cls, T value) {
        Property<T> p = project.getObjects().property(cls);
        p.set(value);
        return p;
    }

    public static <T> ListProperty<T> createListProperty(Project project, Class<T> cls, List<T> values) {
        ListProperty<T> p = project.getObjects().listProperty(cls);
        p.set(values);
        return p;
    }

    public static <K, V> MapProperty<K, V> createMapProperty(Project project, Class<K> keyType, Class<V> valueType, Map<K, V> value) {
        MapProperty<K, V> p = project.getObjects().mapProperty(keyType, valueType);
        p.set(value);
        return p;
    }
}
