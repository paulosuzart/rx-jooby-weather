package suzart.jooby.controllers;

import java.util.List;

import javax.inject.Inject;

import com.google.inject.Singleton;

import org.jooby.mvc.Consumes;
import org.jooby.mvc.GET;
import org.jooby.mvc.Path;
import org.jooby.mvc.Produces;

import rx.Observable;
import suzart.jooby.services.WeatherAvgService;

@Path("/weather/avg")
@Singleton
public class WeatherAvgController {

    @Inject
    private WeatherAvgService weatherAvgService;

    @GET
    @Consumes("json")
    @Produces("json")
    public Observable<AvgResult> avg(List<String> cities) {
        return weatherAvgService.getAvg(cities).map(avg -> new AvgResult(cities, avg));
    }
}