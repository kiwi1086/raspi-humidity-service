package com.kiwi.rhs;

import io.quarkus.scheduler.Scheduled;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Willi Kisser
 */

@Slf4j
@ApplicationScoped
public class HumidityService {

    @Inject
    DTH22 dth22;

    @Scheduled(every = "{evaluation.interval}")
    public void determineTemperature() {
        dth22.readOutTemperatureAndHumidity();
    }

    @Scheduled(every = "{evaluation.interval}")
    public void determineHumidity() {

    }

}