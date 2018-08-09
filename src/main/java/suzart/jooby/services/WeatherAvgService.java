package suzart.jooby.services;

import com.google.inject.Singleton;
import rx.Observable;
import rx.observables.MathObservable;
import suzart.jooby.clients.openwheather.WeatherClient;

import javax.inject.Inject;
import java.util.List;

@Singleton
public class WeatherAvgService {

    @Inject
    private WeatherClient weatherClient;

    public Observable<Double> getAvg(List<String> cities) {
        Observable<Double> doubleObservable = MathObservable.averageDouble(Observable.from(cities).flatMap(weatherClient::getTemperatureByCityName));
        return doubleObservable;
    }
}