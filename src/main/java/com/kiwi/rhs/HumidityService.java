package com.kiwi.rhs;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.metrics.MetricRegistry;

import javax.inject.Inject;

/**
 * @author Willi Kisser
 */

@Slf4j
public class HumidityService {

    @Inject
    MetricRegistry metricRegistry;

    public void determineTemperature() {

    }

    public void determineHumidity() {

    }

}