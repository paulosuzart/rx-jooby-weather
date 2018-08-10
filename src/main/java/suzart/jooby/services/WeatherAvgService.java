package suzart.jooby.services;

import com.google.inject.Singleton;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.observables.MathObservable;
import suzart.jooby.clients.openwheather.TemperatureResult;
import suzart.jooby.clients.openwheather.WeatherClient;

import javax.inject.Inject;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Singleton
public class WeatherAvgService {

    @Inject
    private WeatherClient weatherClient;

    public Observable<AvgResult> getAvg(List<String> cities) {

        Observable<TemperatureResult> results = Observable.from(cities)
                .flatMap(weatherClient::getTemperatureByCityName);

        return results.toList().flatMap(temperatureResults -> {
            Observable<Double> temperatures = Observable.from(temperatureResults).map(TemperatureResult::getTemperature);
            Observable<List<String>> validCities = Observable.from(temperatureResults).map(TemperatureResult::getCity).toList();

            Observable<Double> doubleObservable =
                    MathObservable
                            .averageDouble(temperatures);
            return doubleObservable.zipWith(validCities, (avg, c) -> new AvgResult(c, avg));

        });

    }


}