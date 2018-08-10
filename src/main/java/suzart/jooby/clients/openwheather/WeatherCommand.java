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
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(WeatherCommand.class);

    private final BoundRequestBuilder boundRequestBuilder;
    private final String city;

    WeatherCommand(BoundRequestBuilder boundRequestBuilder, String city) {

        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("weather-commands"))
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                        .withExecutionTimeoutInMilliseconds(5000)
                        .withExecutionIsolationStrategy(
                                HystrixCommandProperties.ExecutionIsolationStrategy.SEMAPHORE)));

        this.boundRequestBuilder = boundRequestBuilder;
        this.city = city;

    }

    @Override
    protected Observable<JsonNode> construct() {

        return AsyncHttpObservable.toObservable(() -> boundRequestBuilder).single().flatMap(response -> {
            if (response.getStatusCode() != 200) {
                log.warn("no 200 on getting data for {}. Ignoring...", this.city);
                return Observable.error(new Exception("Unable to fetch for " + this.city));
            }

            try {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode tree = objectMapper.readTree(response.getResponseBodyAsStream());
                log.info("Got 200 and json response for key {}", this.city);
                return Observable.just(tree);
            } catch (IOException e) {
                log.error("Failed to parse response for " + this.city, e);
                return Observable.error(e);
            }
        });
    }

    @Override
    protected Observable<JsonNode> resumeWithFallback() {
        log.warn("Falling back to default value for city {}", this.city);
        return Observable.empty();
    }
}
