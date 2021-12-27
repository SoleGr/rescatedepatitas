package domain.sistema;

import domain.models.entities.validacion.validadores.ValidadorCaracteres;
import domain.models.entities.validacion.validadores.ValidadorLongitud;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ValidadorPassTest {
    String password;
    String password2;
    ValidadorCaracteres validadorCaracteres;
    ValidadorLongitud validadorLongitud;

    @Before
    public void Instanciar() {
        password = "Hola@4124";
        password2 = "Ho124";
        validadorCaracteres = new ValidadorCaracteres();
        validadorLongitud = new ValidadorLongitud();
    }

    @Test
    public void laContrasenaEsCorrecta(){
        Assert.assertTrue(validadorCaracteres.validar(password));
        Assert.assertFalse(validadorCaracteres.validar(password2));
    }

    @Test
    public void laContraseniaTieneLaCantidadCorrecta(){
        Assert.assertTrue(validadorLongitud.validar(password));
        Assert.assertFalse(validadorLongitud.validar(password2));
    }
}