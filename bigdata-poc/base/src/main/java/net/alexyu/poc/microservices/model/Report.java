package net.alexyu.poc.microservices.model;

import net.alexyu.poc.microservices.model.column.ColumnVector;

import java.util.List;
import java.util.Map;

public class Report {

    private final String reportId;
    private final List<ColumnVector> columns;
    private final long updateTime;

    public Report(String reportId, List<ColumnVector> columns, long updateTime) {
        this.reportId = reportId;
        this.columns = columns;
        this.updateTime = updateTime;
    }

    public String getReportId() {
        return reportId;
    }

    public List<ColumnVector> getColumns() {
        return columns;
    }

    public long getUpdateTime() {
        return updateTime;
    }
}
