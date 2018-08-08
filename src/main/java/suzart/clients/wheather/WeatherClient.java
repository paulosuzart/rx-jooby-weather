package suzart.clients.wheather;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.typesafe.config.Config;

import static org.asynchttpclient.Dsl.*;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Named;
import javax.annotation.PostConstruct;

import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.BoundRequestBuilder;
import org.asynchttpclient.extras.rxjava.AsyncHttpObservable;

import rx.Observable;

public class WeatherClient {
    private String urlTemplate = "https://api.openweathermap.org/data/2.5/weather?q=%s&main.tem=Celsius&APPID=%s";

    @Inject
    private AsyncHttpClient asyncHttpClient;

	private final String apiKey;

    @Inject
    public WeatherClient(@Named("apikey") String apiKey) {
        this.apiKey = apiKey;
    }

    public Observable<JsonNode> getTemperatureByCityName(String city) {
        
        BoundRequestBuilder builder = asyncHttpClient.prepareGet(String.format(urlTemplate, city, this.apiKey));
        ObjectMapper objectMapper = new ObjectMapper();
        return AsyncHttpObservable.toObservable(() -> builder).flatMap(response -> {
            try {
                return Observable.just(objectMapper.readTree(response.getResponseBodyAsStream()));
            } catch (IOException e) {
                // TODO: Log here
                return Observable.error(e);
            }
        });
    }
}