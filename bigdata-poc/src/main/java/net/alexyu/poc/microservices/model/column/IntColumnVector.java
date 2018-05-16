package net.alexyu.poc.microservices.model.column;

import com.fasterxml.jackson.annotation.JsonGetter;

public class IntColumnVector implements ColumnVector{

    private final String name;
    private final int[] data;

    public IntColumnVector(String name, int... data) {
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
        return int.class;
    }

    @JsonGetter("data")
    @Override
    public int[] data() {
        return data;
    }


    public static IntColumnVector of(String name, int... data){
        return new IntColumnVector(name, data);
    }

}
