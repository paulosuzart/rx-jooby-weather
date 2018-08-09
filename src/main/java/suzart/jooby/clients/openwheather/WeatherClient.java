package suzart.jooby.clients.openwheather;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.BoundRequestBuilder;
import rx.Observable;

import javax.inject.Inject;
import javax.inject.Named;

public class WeatherClient {

    private String byNameTemplate = "https://api.openweathermap.org/data/2.5/weather?q=%s&main.tem=Celsius&APPID=%s";

    @Inject
    private AsyncHttpClient asyncHttpClient;

    private final String apiKey;

    @Inject
    public WeatherClient(@Named("apikey") String apiKey) {
        this.apiKey = apiKey;
    }

    public Observable<Double> getTemperatureByCityName(String city) {
        BoundRequestBuilder builder = asyncHttpClient.prepareGet(String.format(byNameTemplate, city, this.apiKey));
        return new WeatherCommand(builder, city).toObservable()
                .flatMap(body -> Observable.just(body.get("main").get("temp").asDouble()));
    }
}