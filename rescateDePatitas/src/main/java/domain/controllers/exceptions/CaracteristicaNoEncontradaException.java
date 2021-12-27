package domain.controllers.exceptions;

public class CaracteristicaNoEncontradaException extends RuntimeException {
    public CaracteristicaNoEncontradaException(String mensaje) {
        super(mensaje);
    }
}
