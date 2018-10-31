package com.prometheus.factory;

import io.prometheus.client.Summary;

public interface PrometheusSummaryTrace {

    default Summary createSummary(final String name, final String help) {
        return Summary.build().
                name(name).
                help(help).
                labelNames("api", "class", "method").
                register();
    }

}
