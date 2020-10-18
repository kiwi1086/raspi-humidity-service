package com.kiwi.rhs;

import com.pi4j.wiringpi.Gpio;
import com.pi4j.wiringpi.GpioUtil;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.metrics.Gauge;
import org.eclipse.microprofile.metrics.Metadata;
import org.eclipse.microprofile.metrics.MetricRegistry;
import org.eclipse.microprofile.metrics.MetricType;

@Slf4j
@Singleton
public class DTH22 {

    private static final int MAX_TIMINGS = 85;
    private static final int PIN = 7;
    private final int[] dht22Data = {0, 0, 0, 0, 0};

    private int lastState = Gpio.HIGH;

    private float temperature = 0;

    private float humidity = 0;

    @Inject
    MetricRegistry metricRegistry;

    final Metadata temperatureMetric = Metadata.builder()
            .withName("temperature")
            .withType(MetricType.GAUGE)
            .withDescription("Temperature in celcius from DTH22 Sensor").build();

    final Metadata humidityMetric = Metadata.builder()
            .withName("humidity")
            .withType(MetricType.GAUGE)
            .withDescription("Humidity in percent from DTH22 Sensor").build();

    @PostConstruct
    public void init() {
        if (Gpio.wiringPiSetup() == -1)
            log.error("GPIO Setup failed");
        else
            GpioUtil.export(3, GpioUtil.DIRECTION_OUT);

        metricRegistry.register(temperatureMetric, (Gauge<Float>) () -> temperature);
        metricRegistry.register(humidityMetric, (Gauge<Float>) () -> humidity);
    }


    public void readOutTemperatureAndHumidity() {
        int j = 0;
        dht22Data[0] = dht22Data[1] = dht22Data[2] = dht22Data[3] = dht22Data[4] = 0;

        Gpio.pinMode(PIN, Gpio.OUTPUT);
        Gpio.digitalWrite(PIN, Gpio.LOW);
        Gpio.delay(1);

        Gpio.digitalWrite(PIN, Gpio.HIGH);
        Gpio.pinMode(PIN, Gpio.INPUT);

        for (int i = 0; i < MAX_TIMINGS; i++) {
            int counter = 0;
            while (Gpio.digitalRead(PIN) == lastState) {
                counter++;
                Gpio.delayMicroseconds(1);
                if (counter == 255) {
                    break;
                }
            }

            lastState = Gpio.digitalRead(PIN);

            if (counter == 255) {
                break;
            }

            /* ignore first 3 transitions */
            if (i >= 4 && i % 2 == 0) {
                /* shove each bit into the storage bytes */
                dht22Data[j / 8] <<= 1;
                if (counter > 16) {
                    dht22Data[j / 8] |= 1;
                }
                j++;
            }
        }

        log.info("-> j <{}>", j);
        log.info("{} {} {} {} {}",
                Integer.toBinaryString(dht22Data[0]),
                Integer.toBinaryString(dht22Data[1]),
                Integer.toBinaryString(dht22Data[2]),
                Integer.toBinaryString(dht22Data[3]),
                Integer.toBinaryString(dht22Data[4]));
        log.info("{}", Integer.toBinaryString((dht22Data[0] + dht22Data[1] + dht22Data[2] + dht22Data[3] & 0xFF)));
        log.info("{}", Integer.toBinaryString(dht22Data[4]));

        // check we read 40 bits (8bit x 5 ) + verify checksum in the last
        // byte
        if (j >= 40 && checkParity()) {
            float h = (float) ((dht22Data[0] << 8) + dht22Data[1]) / 10;
            if (h > 100) {
                h = dht22Data[0]; // for DHT22
            }
            float c = (float) (((dht22Data[2] & 0x7F) << 8) + dht22Data[3]) / 10;
            if (c > 125) {
                c = dht22Data[2]; // for DHT22
            }
            if ((dht22Data[2] & 0x80) != 0) {
                c = -c;
            }
            final float f = c * 1.8f + 32;

            temperature = c;
            humidity = h;
            log.info("Humidity = " + h + " Temperature = " + c + "(" + f + "f)");
        } else {
            log.warn("Data not good, skip");
        }

    }

    private boolean checkParity() {
        return dht22Data[4] == (dht22Data[0] + dht22Data[1] + dht22Data[2] + dht22Data[3] & 0xFF);
    }
}
