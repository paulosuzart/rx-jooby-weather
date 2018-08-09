package suzart.jooby.clients.openwheather;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixObservableCommand;
import jdk.nashorn.internal.ir.ObjectNode;
import org.asynchttpclient.BoundRequestBuilder;
import org.asynchttpclient.Response;
import org.asynchttpclient.extras.rxjava.AsyncHttpObservable;
import rx.Observable;
import rx.exceptions.Exceptions;

import java.io.IOException;

public class WeatherCommand extends HystrixObservableCommand<JsonNode> {

    private final BoundRequestBuilder boundRequestBuilder;
    private final String city;

    WeatherCommand(BoundRequestBuilder boundRequestBuilder, String city) {

        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("weather-commands"))
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                        .withExecutionTimeoutInMilliseconds(5000)
                        .withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.SEMAPHORE)));

        this.boundRequestBuilder = boundRequestBuilder;
        this.city = city;

    }

    @Override
    protected Observable<JsonNode> construct() {

        return AsyncHttpObservable.toObservable(() -> boundRequestBuilder).single().flatMap(response -> {
            if (response.getStatusCode() != 200) {
                return Observable.error(new Exception("Unable to fetch for " + this.city));
            }
            System.out.println(response.getStatusCode());
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode tree = objectMapper.readTree(response.getResponseBodyAsStream());
                System.out.println("Ja mapeout");
                System.out.println(tree.toString());

                return Observable.just(tree);
            } catch (IOException e) {
                e.printStackTrace();
                return Observable.error(e);
            }
        });
    }

    @Override
    protected Observable<JsonNode> resumeWithFallback() {
        System.out.println("Veio no fall" + this.boundRequestBuilder.toString());
        return Observable.empty();
    }
}
