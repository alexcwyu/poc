package net.alexyu.poc.microservices.model;

import java.util.List;

public class Query {
    private int date;

    //TODO hierarchical filters, with AND, OR, IN conditions
    private List<String> filters;
    private List<String> columns;
    private List<String> groupBy;

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public List<String> getFilters() {
        return filters;
    }

    public void setFilters(List<String> filters) {
        this.filters = filters;
    }

    public List<String> getColumns() {
        return columns;
    }

    public void setColumns(List<String> columns) {
        this.columns = columns;
    }

    public List<String> getGroupBy() {
        return groupBy;
    }

    public void setGroupBy(List<String> groupBy) {
        this.groupBy = groupBy;
    }
}
