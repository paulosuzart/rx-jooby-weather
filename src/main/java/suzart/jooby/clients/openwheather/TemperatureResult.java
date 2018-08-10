package suzart.jooby.clients.openwheather;

public class TemperatureResult {

    private final String city;
    private final Double temperature;

    public TemperatureResult(String city, Double temperature) {

        this.city = city;
        this.temperature = temperature;
    }

    public String getCity() {
        return city;
    }

    public Double getTemperature() {
        return temperature;
    }

}
