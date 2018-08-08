package suzart.jooby;

import com.google.inject.Binder;
import com.google.inject.Provider;
import com.typesafe.config.Config;

import org.asynchttpclient.AsyncHttpClient;
import org.jooby.Env;
import org.jooby.Jooby;

import static org.asynchttpclient.Dsl.*;

import suzart.clients.wheather.WeatherClient;
import suzart.jooby.controllers.WeatherAvgController;
import suzart.jooby.services.WeatherAvgService;

public class WeatherModule implements Jooby.Module {

    @Override
    public void configure(Env env, Config conf, Binder binder) throws Throwable {
        binder.bind(AsyncHttpClient.class).toInstance(asyncHttpClient());
        env.router().use(WeatherAvgController.class);

        env.lifeCycle(WeatherAvgService.class);
        env.lifeCycle(WeatherClient.class);
        env.onStop(registry -> {
            AsyncHttpClient client = registry.require(AsyncHttpClient.class);
            client.close();
        });
    }
}