package net.alexyu.poc.microservices.model.column;

import com.fasterxml.jackson.annotation.JsonGetter;

public class LongColumnVector implements ColumnVector {

    private final String name;
    private final long[] data;

    public LongColumnVector(String name, long... data) {
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
        return long.class;
    }

    @JsonGetter("data")
    @Override
    public long[] data() {
        return data;
    }


    public static LongColumnVector of(String name, long... data){
        return new LongColumnVector(name, data);
    }
}
