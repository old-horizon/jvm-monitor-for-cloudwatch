package com.github.old_horizon.jvm.monitor.model;

import com.amazonaws.services.cloudwatch.model.Dimension;
import com.amazonaws.services.cloudwatch.model.MetricDatum;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class Category {

    private final Dimension dimension;
    private final Map<String, Value> data = new HashMap<>();

    public Category(final String name) {
        dimension = new Dimension().withName("Category").withValue(name);
    }

    public Category data(final String name, final double value) {
        data.put(name, Value.none(value));
        return this;
    }

    public Category bytesData(final String name, final double value) {
        data.put(name, Value.bytes(value));
        return this;
    }

    public List<MetricDatum> toMetricDatumList() {
        return data.entrySet().stream().map(this::toMetricDatum).collect(Collectors.toList());
    }

    private MetricDatum toMetricDatum(final Entry<String, Value> entry) {
        final MetricDatum metricDatum = new MetricDatum()
                .withDimensions(dimension)
                .withMetricName(entry.getKey());
        entry.getValue().accept(metricDatum);
        return metricDatum;
    }

}
