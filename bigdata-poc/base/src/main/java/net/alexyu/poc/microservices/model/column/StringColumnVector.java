package net.alexyu.poc.microservices.model.column;

import com.fasterxml.jackson.annotation.JsonGetter;

public class StringColumnVector implements ColumnVector {

    private final String name;
    private final String[] data;

    public StringColumnVector(String name, String... data) {
        this.name = name;
        this.data = data;
    }

    @JsonGetter("name")
    @Override
    public String name() {
        return name;
    }

    @Override
    public Class type() {
        return String.class;
    }

    @JsonGetter("data")
    @Override
    public String[] data() {
        return data;
    }

    public static StringColumnVector of(String name, String... data) {
        return new StringColumnVector(name, data);
    }
}
