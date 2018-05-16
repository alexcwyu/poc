package net.alexyu.poc.microservices.model.column;

import com.fasterxml.jackson.annotation.JsonGetter;

public class DoubleColumnVector implements ColumnVector {

    private final String name;
    private final double[] data;

    public DoubleColumnVector(String name, double... data) {
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
        return double.class;
    }

    @JsonGetter("data")
    @Override
    public double[] data() {
        return data;
    }


    public static DoubleColumnVector of(String name, double... data) {
        return new DoubleColumnVector(name, data);
    }
}
