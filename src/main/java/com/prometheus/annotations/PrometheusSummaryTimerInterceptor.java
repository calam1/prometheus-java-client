package com.prometheus.annotations;

import io.prometheus.client.SimpleTimer;
import io.prometheus.client.Summary;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@PrometheusSummaryTimer(name = "", help = "", labelNames = "", labels = "")
@Interceptor
public class PrometheusSummaryTimerInterceptor {

    private final static Summary summary = Summary.build().
            name("prometheus_summary_timer_annotation_map").
            help("monitor synchronized map in PrometheusSummaryTimerInterceptor annotation").
            register();

    // This is static so that we have global access to what is registered, if we were to use the raw prometheus client we would have to
    // make Summary static for the same reason.
    private final static Map<String, Summary> summaries = Collections.synchronizedMap(new HashMap<>());

    private final Summary ensureSummary(final PrometheusSummaryTimer prometheusSummaryTimer) {
        return summary.time(() -> {
            final String key = prometheusSummaryTimer.name();

            Summary summary = summaries.get(key);
            if (summary != null) {
                return summary;
            }

            summary = Summary.build().
                    name(key).
                    help(prometheusSummaryTimer.help()).
                    labelNames(prometheusSummaryTimer.labelNames()).
                    register();

            summaries.put(key, summary);
            return summary;
        });
    }

    @AroundInvoke
    public Object simpleTimer(final InvocationContext ctx) throws Exception {
        final PrometheusSummaryTimer prometheusSummaryTimer = getPrometheusSimpleTimerAnnotation(ctx.getMethod());
        final Summary summary = ensureSummary(prometheusSummaryTimer);
        final SimpleTimer simpleTimer = new SimpleTimer();
        final Object result = ctx.proceed();
        summary.labels(prometheusSummaryTimer.labels()).observe(simpleTimer.elapsedSeconds());

        return result;
    }

    private final PrometheusSummaryTimer getPrometheusSimpleTimerAnnotation(final Method m) {
        for (Annotation a : m.getAnnotations()) {
            if (a instanceof PrometheusSummaryTimer) {
                return (PrometheusSummaryTimer) a;
            }
        }
        for (Annotation a : m.getDeclaringClass().getAnnotations()) {
            if (a instanceof PrometheusSummaryTimer) {
                return (PrometheusSummaryTimer) a;
            }
        }

        throw new RuntimeException("@PrometheusSummaryTimer not found on method " + m.getName() + " or its class " + m.getClass().getName());
    }
}
