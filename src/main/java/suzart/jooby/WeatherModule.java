package suzart.jooby;

import com.google.inject.Binder;
import com.typesafe.config.Config;
import org.asynchttpclient.AsyncHttpClient;
import org.jooby.*;
import suzart.jooby.clients.openwheather.WeatherClient;
import suzart.jooby.clients.openwheather.WeatherCommand;
import suzart.jooby.controllers.WeatherAvgController;
import suzart.jooby.services.WeatherAvgService;
import suzart.jooby.utils.UnableToProcessMessage;

import static org.asynchttpclient.Dsl.asyncHttpClient;

public class WeatherModule implements Jooby.Module {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(WeatherModule.class);

    @Override
    public void configure(Env env, Config conf, Binder binder) {
        binder.bind(AsyncHttpClient.class).toInstance(asyncHttpClient());
        env.router().use(WeatherAvgController.class);

        env.lifeCycle(WeatherAvgService.class);
        env.lifeCycle(WeatherClient.class);

        env.router().err(IllegalArgumentException.class, (req, res, err) -> {
            log.error("Unable to process request due to: {}", err.getCause().getMessage());
            res.send(Results.json(new UnableToProcessMessage()));

        });

        env.onStop(registry -> {
            AsyncHttpClient client = registry.require(AsyncHttpClient.class);
            client.close();
        });
    }
}