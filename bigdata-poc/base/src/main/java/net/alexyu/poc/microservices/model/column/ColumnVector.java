package net.alexyu.poc.microservices.model.column;

public interface ColumnVector {

    String name();

    Class type();

    Object data();

}
