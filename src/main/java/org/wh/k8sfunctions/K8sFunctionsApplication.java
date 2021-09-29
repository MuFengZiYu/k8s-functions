package org.wh.k8sfunctions;

import io.prometheus.client.spring.boot.EnablePrometheusEndpoint;
import io.prometheus.client.spring.boot.EnableSpringBootMetricsCollector;
import jdk.nashorn.internal.runtime.logging.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;



@SpringBootApplication
@EnablePrometheusEndpoint // sets up the prometheus endpoint /prometheus-metrics
@EnableSpringBootMetricsCollector

public class K8sFunctionsApplication {

    public static void main(String[] args) {

        SpringApplication.run(K8sFunctionsApplication.class, args);
    }

}
