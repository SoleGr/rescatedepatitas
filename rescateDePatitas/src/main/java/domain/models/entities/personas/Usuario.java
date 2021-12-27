package domain.models.entities.personas;

import domain.models.entities.Persistente;
import domain.models.entities.validacion.validadores.ValidadorCaracteres;
import domain.models.entities.validacion.validadores.ValidadorLongitud;
import org.mindrot.jbcrypt.BCrypt;

import javax.persistence.*;

@Entity
@Table(name = "usuario")
public class Usuario extends Persistente {
    @Transient
    private static int workload = 12;
    @Column(name = "nombreDeUsuario")
    private String nombreDeUsuario;
    @Column(name = "contrasenia")
    private String contrasenia;
    @Transient
    private Integer fallosAlIniciarSesion = 0;

    public Usuario(){

    }

    public Usuario(String user, String contrasenia) {
        setNombreDeUsuario(user);
        if(verificarContrasenia(contrasenia)) {
            hashPassword(contrasenia);
        }
    }

    public String getNombreDeUsuario() {
        return nombreDeUsuario;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setNombreDeUsuario(String nombreDeUsuario) {
        this.nombreDeUsuario = nombreDeUsuario;
    }

    private void setContrasenia(String hashContrasenia) {
        this.contrasenia = hashContrasenia;
    }

    public Boolean verificarContrasenia(String contrasenia){
        ValidadorCaracteres validadorCaracteres = new ValidadorCaracteres();
        ValidadorLongitud validadorLongitud = new ValidadorLongitud();

        return validadorLongitud.validar(contrasenia) && validadorCaracteres.validar(contrasenia);
    }

    public void hashPassword(String password_plaintext) {
        String hashed_password = org.apache.commons.codec.digest.DigestUtils.md5Hex(password_plaintext);
        setContrasenia(hashed_password);
    }

    public Boolean login(String user, String contrasenia) {

        if(getNombreDeUsuario() != user) {
            System.out.println("El usuario ingresado es incorrecto");
            this.fallosAlIniciarSesion = fallosAlIniciarSesion + 1;
        } else if(!checkPassword(contrasenia, this.contrasenia)) {
            System.out.println("La contraseña ingresada es incorrecta");
            this.fallosAlIniciarSesion = fallosAlIniciarSesion+ 1;
        } else {
            System.out.println("Inicio de sesion exitoso!");
            this.fallosAlIniciarSesion = 0;
        }

        return this.getNombreDeUsuario() == user && checkPassword(contrasenia, this.contrasenia);
    }

    public Boolean iniciarSesion(String user, String contrasenia, Persona persona) {

        if(getNombreDeUsuario() != user) {
            System.out.println("El usuario ingresado es incorrecto");
            this.fallosAlIniciarSesion = fallosAlIniciarSesion + 1;
        } else if(!checkPassword(contrasenia, this.contrasenia)) {
            System.out.println("La contraseña ingresada es incorrecta");
            this.fallosAlIniciarSesion = fallosAlIniciarSesion+ 1;
        } else {
            System.out.println("Inicio de sesion exitoso!");
            this.fallosAlIniciarSesion = 0;
        }

        if(fallosAlIniciarSesion >= 3){
            persona.getContactos().forEach(contacto -> contacto.notificarContacto("detectamos una serie de ingresos fallidos en tu cuenta"));
        }

        return this.getNombreDeUsuario().equals(user) && checkPassword(contrasenia, this.contrasenia);
    }

    public Boolean checkPassword(String password_plaintext, String stored_hash) {
        //Boolean password_verified = false;

//        if(null == stored_hash || !stored_hash.startsWith("$2a$")) {
//            throw new java.lang.IllegalArgumentException("Invalid hash provided for comparison");
//        }
        String hashed_password = org.apache.commons.codec.digest.DigestUtils.md5Hex(password_plaintext);

        //password_verified = BCrypt.checkpw(password_plaintext, stored_hash);

        return stored_hash.equals(hashed_password);
    }

}
