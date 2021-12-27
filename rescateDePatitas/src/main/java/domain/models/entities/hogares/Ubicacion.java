package domain.models.entities.hogares;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Ubicacion {
    @JsonProperty("direccion")
    public String direccion;
    @JsonProperty("lat")
    public double latitud;
    @JsonProperty("long")
    public double longitud;

    public String getDireccion() {
        return direccion;
    }

    public double getLatitud() {
        return latitud;
    }

    public double getLongitud() {
        return longitud;
    }
}
