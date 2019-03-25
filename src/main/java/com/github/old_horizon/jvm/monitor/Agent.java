package com.github.old_horizon.jvm.monitor;

import com.amazonaws.services.cloudwatch.AmazonCloudWatch;
import com.amazonaws.services.cloudwatch.AmazonCloudWatchClientBuilder;
import com.amazonaws.services.cloudwatch.model.MetricDatum;
import com.amazonaws.services.cloudwatch.model.PutMetricDataRequest;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Agent {

    public static void premain(final String agentArgs) {
        final Config config = new Config();
        final AmazonCloudWatch cloudWatch = AmazonCloudWatchClientBuilder.defaultClient();
        final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor(new DaemonThreadFactory());
        executor.scheduleAtFixedRate(() -> {
            try {
                final PutMetricDataRequest request = createRequest(config);
                cloudWatch.putMetricData(request);
            } catch (final Throwable t) {
                t.printStackTrace(System.err);
            }
        }, 0L, config.getPeriod(), TimeUnit.SECONDS);
    }

    private static PutMetricDataRequest createRequest(final Config config) {
        final PutMetricDataRequest request = new PutMetricDataRequest().withMetricData(getMetricData());
        config.getNamespace().ifPresent(request::setNamespace);
        return request;
    }

    private static List<MetricDatum> getMetricData() {
        return Stats.getCurrentValues().stream()
                .flatMap(category -> category.toMetricDatumList().stream())
                .collect(Collectors.toList());
    }


    private static class DaemonThreadFactory implements ThreadFactory {

        @Override
        public Thread newThread(final Runnable runnable) {
            final Thread thread = new Thread(runnable);
            thread.setName("jvm-monitor-for-cloudwatch");
            thread.setDaemon(true);
            return thread;
        }

    }

}
