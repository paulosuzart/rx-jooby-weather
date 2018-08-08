package suzart.jooby.controllers;

import java.io.Serializable;
import java.util.List;

public class AvgResult implements Serializable {
    private static final long serialVersionUID = 1L;
	private final List<String> cities;
    private final double avg;

    public AvgResult(List<String> cities, double avg) {
        this.cities = cities;
        this.avg = avg;
    }

    public double getAvg() {
        return this.avg;
    }

    public List<String> getCities() {
        return this.cities;
    }
    
    
}