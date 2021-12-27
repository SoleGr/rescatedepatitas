package domain.models.entities.validacion.validadores;

public class ValidadorLongitud implements ValidadorPassword {
    Integer tamanioMinimo = 8;
    Integer tamanioMaximo = 20;

    @Override
    public boolean validar(String password) {
        if (password.length() > tamanioMaximo || password.length() < tamanioMinimo) {
            System.out.println("Longitud no permitida");
            return false;
        }
        return true;
    }
}
