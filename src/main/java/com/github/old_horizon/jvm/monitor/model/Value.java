package com.github.old_horizon.jvm.monitor.model;

import com.amazonaws.services.cloudwatch.model.MetricDatum;
import com.amazonaws.services.cloudwatch.model.StandardUnit;

import static com.amazonaws.services.cloudwatch.model.StandardUnit.Bytes;
import static com.amazonaws.services.cloudwatch.model.StandardUnit.None;

class Value {

    private final double value;
    private final StandardUnit unit;

    static Value none(final double value) {
        return new Value(value, None);
    }

    static Value bytes(final double value) {
        return new Value(value, Bytes);
    }

    private Value(final double value, final StandardUnit unit) {
        this.value = value;
        this.unit = unit;
    }

    void accept(final MetricDatum metricDatum) {
        metricDatum.withValue(value).withUnit(unit);
    }

}
