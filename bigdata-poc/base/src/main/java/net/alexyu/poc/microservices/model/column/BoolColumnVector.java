package net.alexyu.poc.microservices.model.column;

import com.fasterxml.jackson.annotation.JsonGetter;

public class BoolColumnVector implements ColumnVector {

    private final String name;
    private final boolean[] data;

    public BoolColumnVector(String name, boolean... data) {
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
        return boolean.class;
    }

    @JsonGetter("data")
    @Override
    public boolean[] data() {
        return data;
    }

    public static BoolColumnVector of(String name, boolean... data){
        return new BoolColumnVector(name, data);
    }
}
