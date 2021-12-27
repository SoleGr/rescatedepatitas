package domain.models.entities.personas;

import domain.models.entities.Persistente;
import domain.models.entities.mascotas.*;
import domain.models.entities.publicaciones.*;
import domain.models.entities.notificaciones.estrategias.Estrategia;
import domain.models.entities.rol.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "persona")
public class Persona extends Persistente {
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "apellido")
    private String apellido;
    @Column(name = "fechaDeNacimiento", columnDefinition = "DATE")
    private LocalDate fechaDeNacimiento;
    @Enumerated(EnumType.STRING)
    private TipoDeDocumento tipoDoc;
    @Column(name = "numeroDocumento")
    private Integer nroDoc;
    @Column(name = "direccion")
    private String direccion;
    @OneToMany(mappedBy = "persona", cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    private List<Contacto> contactos;
    @OneToMany(mappedBy = "persona", cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    private Set<Rol> rolesDisponibles;
    @Transient
    private Rol rolElegido;
    @Column(name = "usuario_temporal")
    private String usuarioTemporal;
    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "usuario_id", referencedColumnName = "id")
    private Usuario usuario;

    public Persona() {
        this.contactos = new ArrayList<>();
        this.rolesDisponibles = new HashSet<Rol>();
    }

    public void inicializar(String nombre, String apellido, String direccion, TipoDeDocumento tipoDoc,
                            Integer nroDoc, LocalDate fechaDeNacimiento, List<Contacto> contactos) {
        setNombre(nombre);
        setApellido(apellido);
        setDireccion(direccion);
        setNroDoc(nroDoc);
        setTipoDoc(tipoDoc);
        setFechaDeNacimiento(fechaDeNacimiento);
        setContactos(contactos);
    }

    public Rol getRolElegido() {
        return rolElegido;
    }

    public Rol getRol(String rol) {
        if (rol.equals("Duenio")) {
            return this.getDuenio();
        } else if (rol.equals("Rescatista")) {
            return this.getRescatista();
        }

        return null;
    }

    //getters & setters

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public LocalDate getFechaDeNacimiento() {
        return fechaDeNacimiento;
    }

    public void setFechaDeNacimiento(LocalDate fechaDeNacimiento) {
        this.fechaDeNacimiento = fechaDeNacimiento;
    }

    public TipoDeDocumento getTipoDoc() {
        return tipoDoc;
    }

    public void setTipoDoc(TipoDeDocumento tipoDoc) {
        this.tipoDoc = tipoDoc;
    }

    public Integer getNroDoc() {
        return nroDoc;
    }

    public void setNroDoc(Integer nroDoc) {
        this.nroDoc = nroDoc;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public void setRolElegido(Rol rol) {
        Rol rolElegido = null;

        if (rolesDisponibles.contains(rol)) {
            for (Rol rolDisp : rolesDisponibles) {
                if (rolDisp.equals(rol))
                    rolElegido = rolDisp;
            }
        }

        this.rolElegido = rolElegido;
    }


    public void addRol(Rol rol) {
        this.rolesDisponibles.add(rol);
    }

    public List<Contacto> getContactos() {
        return contactos;
    }

    public void setContactos(List<Contacto> contactos) {
        this.contactos = contactos;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public void setRolesDisponibles(Set<Rol> rolesDisponibles) {
        this.rolesDisponibles = rolesDisponibles;
    }

    public Set<Rol> getRolesDisponibles() {
        return this.rolesDisponibles;
    }

    public List<Mascota> getMascotas() {
        return this.getDuenio().getMascotas();

    }

    public Duenio getDuenio() {
        Rol rolElegido = null;

        for (Rol rolDisp : rolesDisponibles) {
            if (rolDisp.getTipo().equals("Duenio"))
                rolElegido = rolDisp;
        }

        return (Duenio) rolElegido;
    }

    public Rescatista getRescatista() {
        Rol rolElegido = null;

        for (Rol rolDisp : rolesDisponibles) {
            if (rolDisp.getTipo().equals("Rescatista"))
                rolElegido = rolDisp;
        }

        return (Rescatista) rolElegido;
    }


    // methods

    public void agregarContacto(String nombre, String apellido, String numero, String email, Estrategia estrategiaDeEnvio) {
        Contacto contacto = new Contacto(nombre, apellido, numero, email, estrategiaDeEnvio);

        contactos.add(contacto);
    }

    public void notificarContactos(Mascota mascotaEncontrada, Contacto contactoRescatista, DatosMascotaEncontrada datos) {

        contactos.forEach(contacto -> contacto.notificarContacto("tu mascota " + mascotaEncontrada.getNombre() + " fue encontrada!\n" +
                "Fue encontrada por " + contactoRescatista.getNombre() + ", sus medios de contacto son:\n" +
                "Telefono: " + contactoRescatista.getNumeroCompleto() + "\n" + "Email: " + contactoRescatista.getEmail()));


    }

    public void notificarContactos(Mascota mascotaEncontrada, List<Contacto> contactoRescatista, DatosMascotaEncontrada datos) {
        for (Contacto contacto : contactoRescatista) {
            this.notificarContactos(mascotaEncontrada, contacto, datos);
        }
    }


    public void crearUsuario(String user, String contrasenia) {
        Usuario usuario = new Usuario(user, contrasenia);
        setUsuario(usuario);
    }

    public void registrarMascota(Mascota.MascotaDTO mascota) {
        if (rolElegido.getTipo().equals("Duenio")) {
            Duenio duenio = (Duenio) rolElegido;
            duenio.registrarMascota(mascota, this);
        }
    }

    public void notificarContactosRescatista(Contacto contactoDuenio) {

        contactos.forEach(c -> c.notificarContacto(contactoDuenio.getNombre() + " encontro su mascota en tu publicacion!\n" +
                "Sus medios de contacto son:\n" + "Telefono: " +
                contactoDuenio.getNumeroCompleto() + "\n" +
                "Email: " + contactoDuenio.getEmail()));

    }

    public Boolean iniciarSesion(String user, String contrasenia) {
        return this.usuario.iniciarSesion(user, contrasenia, this);
    }

    public boolean comprobarRol(String rol) {
        return rolElegido.getTipo().equals(rol);
    }

    //Voluntario//
    public void aprobarPublicacion(PublicacionGenerica unaPublicacion, Organizacion organizacion) {
        if (this.comprobarRol("Voluntario")) {
            Voluntario rolActual = (Voluntario) rolElegido;
            rolActual.aprobarPublicacion(unaPublicacion, organizacion);
        }
    }

    public void rechazarPublicacion(PublicacionGenerica unaPublicacion, Organizacion organizacion) {
        if (this.comprobarRol("Voluntario")) {
            Voluntario rolActual = (Voluntario) rolElegido;
            rolActual.rechazarPublicacion(unaPublicacion, organizacion);
        }
    }

    public void enRevisionPublicacion(PublicacionGenerica unaPublicacion, Organizacion organizacion) {
        if (this.comprobarRol("Voluntario")) {
            Voluntario rolActual = (Voluntario) rolElegido;
            rolActual.enRevisionPublicacion(unaPublicacion, organizacion);
        }
    }

    //Rescatista//
    public void encontreUnaMascotaPerdida(Mascota mascotaPerdida, Contacto
            contactoRescatista, DatosMascotaEncontrada
                                                  datosMascota) {
        //Con chapita
        if (this.comprobarRol("Rescatista")) {
            Rescatista rolActual = (Rescatista) rolElegido;
            rolActual.encontreUnaMascotaPerdida(mascotaPerdida, contactoRescatista, datosMascota);
        }
    }

    public void encontreUnaMascotaPerdidaSinChapita(Persona rescatista, DatosMascotaEncontrada datos) {
        if (this.comprobarRol("Rescatista")) {
            Rescatista rolActual = (Rescatista) rolElegido;
            rolActual.encontreUnaMascotaPerdidaSinChapita(this, datos);
        }
    }

    //Duenio//
//    public void encontreMiMascotaPerdida1(PublicacionMascotaEncontrada publicacion, Contacto contacto) {
//        publicacion.getRescatista().notificarContactosRescatista(contacto);
//
//    }

    public void encontreMiMascotaPerdida(PublicacionMascotaEncontrada publicacion) {
        for (Contacto contacto : contactos) {
            publicacion.getRescatista().notificarContactosRescatista(contacto);
        }

    }

    public void perdiUnaMascota(Mascota mascota) {
        if (this.comprobarRol("Duenio")) {
            Duenio rolActual = (Duenio) rolElegido;
            rolActual.perdiUnaMascota(mascota);
        }
    }

    public void darEnAdopcion(Mascota mascota, Organizacion
            organizacion, List<RespuestaConcreta> respuestasOrganizacion, List<RespuestaConcreta> respuestasGenerales1) {
        if (this.comprobarRol("Duenio")) {
            Duenio rolActual = (Duenio) rolElegido;
            rolActual.darEnAdopcion(mascota, organizacion, respuestasOrganizacion, respuestasGenerales1);
        }

    }

    public String hasheoPersona() {

        String cadena = this.fechaDeNacimiento + String.valueOf(this.nroDoc);

        return org.apache.commons.codec.digest.DigestUtils.md5Hex(cadena);
    }

    public void setUsuarioTemporal(String usuarioTemporal) {
        this.usuarioTemporal = usuarioTemporal;
    }

    public String getUsuarioTemporal() {
        return usuarioTemporal;
    }

    public void notificarPosibleAdopcion(Mascota mascota, Persona adoptante) {
        contactos.forEach(contacto -> contacto.notificarContacto("alguien quiere adoptar a " + mascota.getNombre() + "!\n" +
                "Su nombre es " + adoptante.getNombre() + ", sus medios de contacto son:\n" +
                "Telefono: " + adoptante.contactos.get(0).getNumeroCompleto() + "\n" + "Email: " + adoptante.contactos.get(0).getEmail()));

    }

    public void intencionDeAdoptar(CuestionarioContestado cuestionarioContestadoPreferenciasYComodidades) {
        GestorDePublicaciones gestor = GestorDePublicaciones.getInstancia();
        gestor.generarPublicacionIntencionAdoptar(this, cuestionarioContestadoPreferenciasYComodidades);

    }

    public void notificarMascotasEnAdopcion(ArrayList hypervinculosPublicacionesEnAdopcion) {
        String hypervinculosSinFormato = "";
        for (int i = 0; i < hypervinculosPublicacionesEnAdopcion.size(); i++) {
            hypervinculosSinFormato = hypervinculosPublicacionesEnAdopcion.get(i) + "\n";
        }
        String hypervinculosConFormato = hypervinculosSinFormato;

        contactos.forEach((contacto -> contacto.notificarContacto(
                "Pensamos que estas mascotas pueden interesarte:\n" + hypervinculosConFormato)));
    }

    public List<PublicacionEnAdopcion> accederPublicacionesDeAdopcion() {
        GestorDePublicaciones gestor = GestorDePublicaciones.getInstancia();
        return gestor.getPublicacionesDeAdopcion();
    }

    public void quieroAdoptar(Mascota mascotaEnAdopcion) {
        mascotaEnAdopcion.meQuiereAdoptar(this);
    }


    public void inicializar(PersonaDTO persona) {
        this.apellido = persona.getApellido();
        this.nombre = persona.getNombre();
        this.fechaDeNacimiento = persona.getFechaDeNacimiento();
        this.tipoDoc = persona.getTipoDoc();
        this.nroDoc = persona.getNroDoc();
        this.direccion = persona.getDireccion();
        this.contactos = persona.getContactos();
        this.rolElegido = persona.getRoElegido();
        this.usuario = persona.getUsuario();
    }

    public void notificarSuscripcion(int publicacion_id) {
        contactos.forEach(contacto -> contacto.notificarContacto("generamos una publicación para vos como posible adoptante."+"\n"+ "Podés darle de baja ingresando en: " + "localhost:9000/adoptantes/" + publicacion_id + "!\n"
        ));

    }

    public static class PersonaDTO {
        private String nombre;
        private String apellido;
        private LocalDate fechaDeNacimiento;
        private TipoDeDocumento tipoDoc;
        private Integer nroDoc;
        private String direccion;
        private List<Contacto> contactos;
        private Rol rol;
        private Usuario usuario;

        public void setApellido(String apellido) {
            this.apellido = apellido;
        }

        public LocalDate getFechaDeNacimiento() {
            return fechaDeNacimiento;
        }

        public void setFechaDeNacimiento(LocalDate fechaDeNacimiento) {
            this.fechaDeNacimiento = fechaDeNacimiento;
        }

        public TipoDeDocumento getTipoDoc() {
            return tipoDoc;
        }

        public void setTipoDoc(TipoDeDocumento tipoDoc) {
            this.tipoDoc = tipoDoc;
        }

        public Integer getNroDoc() {
            return nroDoc;
        }

        public void setNroDoc(Integer nroDoc) {
            this.nroDoc = nroDoc;
        }

        public String getDireccion() {
            return direccion;
        }

        public void setDireccion(String direccion) {
            this.direccion = direccion;
        }

        public List<Contacto> getContactos() {
            return contactos;
        }

        public void setContactos(List<Contacto> contactos) {
            this.contactos = contactos;
        }

        public Rol getRoElegido() {
            return this.rol;
        }

        public void setRol(Rol rol) {
            this.rol = rol;
        }

        public Usuario getUsuario() {
            return usuario;
        }

        public void setUsuario(Usuario usuario) {
            this.usuario = usuario;
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public String getApellido() {
            return apellido;
        }

        public void inicializar(String nombre, String apellido, String direccion, TipoDeDocumento tipoDoc, Integer nroDoc,
                                LocalDate fechaDeNacimiento, List<Contacto> contactos) {
            this.apellido = apellido;
            this.nombre = nombre;
            this.fechaDeNacimiento = fechaDeNacimiento;
            this.tipoDoc = tipoDoc;
            this.nroDoc = nroDoc;
            this.direccion = direccion;
            this.contactos = contactos;
        }
    }

}



