package suzart.jooby.clients.openwheather;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.util.Pair;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.BoundRequestBuilder;
import rx.Observable;

import javax.inject.Inject;
import javax.inject.Named;

public class WeatherClient {

    private String byNameTemplate = "https://api.openweathermap.org/data/2.5/weather?q=%s&main.temp=Celsius&APPID=%s";

    @Inject
    private AsyncHttpClient asyncHttpClient;

    private final String apiKey;

    @Inject
    public WeatherClient(@Named("apikey") String apiKey) {
        this.apiKey = apiKey;
    }

    public Observable<TemperatureResult> getTemperatureByCityName(String city) {

        BoundRequestBuilder builder = asyncHttpClient.prepareGet(String.format(byNameTemplate, city, this.apiKey));
        return new WeatherCommand(builder, city).toObservable()
                .flatMap(body -> Observable.just(new TemperatureResult(city, body.get("main").get("temp").asDouble())));
    }

}