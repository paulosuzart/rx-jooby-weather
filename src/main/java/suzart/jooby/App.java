package suzart.jooby;

import org.jooby.Jooby;
import org.jooby.json.Jackson;
import org.jooby.rx.Rx;

import suzart.jooby.controllers.WeatherAvgController;

/**
 * rx-jooby-weather entry point and configs
 */
public class App extends Jooby {

  {
    use(new Rx());
    use(new Jackson());
    use(new WeatherModule());
  }

  public static void main(String[] args) {
    run(App::new, args);
  }
}
