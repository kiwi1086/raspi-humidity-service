package com.kiwi.rhs;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("humidityservice")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class HumidityServiceController {

    @Inject
    DTH22 dth22;

    @GET
    @Path("temperature")
    public String getTemperature() {
        return dth22.getTemperature().toString();
    }

    @GET
    @Path("humidity")
    public String getHumidity() {
        return dth22.getHumidity().toString();
    }
}
