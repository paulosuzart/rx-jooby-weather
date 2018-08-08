package suzart.jooby.services;

import java.util.List;

import javax.inject.Inject;

import com.google.inject.Singleton;

import rx.Observable;
import suzart.clients.wheather.WeatherClient;

@Singleton
public class WeatherAvgService {

    @Inject
    private WeatherClient weatherClient;

    public Observable<Double> getAvg(List<String> cities) {
        weatherClient.getTemperatureByCityName("London,uk").subscribe(System.out::println, 
        throwable -> {
            throwable.printStackTrace();
        }); 
        
        return Observable.just(Double.valueOf(22.2)).single();
    }
}