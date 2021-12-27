package domain.controllers.exceptions;

public class CaracteristicaSinDescripcionException extends RuntimeException  {
    public CaracteristicaSinDescripcionException(String mensaje) {
        super(mensaje);
    }
}
