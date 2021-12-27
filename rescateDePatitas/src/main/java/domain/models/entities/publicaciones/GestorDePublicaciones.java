package domain.models.entities.publicaciones;

import domain.models.entities.mascotas.*;
import domain.models.entities.personas.Persona;
import services.ComparadorDistancias;

import java.time.LocalDate;
import java.util.*;

// No lo persitimos porque este tomaria las Publicaciones ya persistidas
// Esta entidad se centra en el comportamiento
public class GestorDePublicaciones {
    private static GestorDePublicaciones instancia;
    private List<PublicacionGenerica> publicaciones = new ArrayList<>();
    private List<Organizacion> organizaciones = new ArrayList<>();

    public static GestorDePublicaciones getInstancia() {
        if (instancia == null) {
            instancia = new GestorDePublicaciones();
        }
        return instancia;
    }

    public void setOrganizaciones(List<Organizacion> organizaciones) {
        this.organizaciones = organizaciones;
    }

    public List<PublicacionGenerica> getPublicaciones() {
        return publicaciones;
    }


    public Organizacion buscarOrganizacionMasCercana(Lugar lugarEncuentro) {
        ComparadorDistancias comparador = new ComparadorDistancias();
        Organizacion organizacion = Collections.min(organizaciones, Comparator.comparing(o -> comparador.comparar(o.getUbicacion(), lugarEncuentro)));
        return organizacion;
    }

    public void generarPublicacionEnAdopcion(Mascota mascota, List<RespuestaConcreta> respuestasDeLaOrg,
                                             List<RespuestaConcreta> respuestasGenerales, Organizacion organizacion) {
        PublicacionEnAdopcion publicacion = new PublicacionEnAdopcion();
        List<RespuestaConcreta> respuestasCuestionario = new ArrayList<>();
        CuestionarioContestado cuestionarioContestado = new CuestionarioContestado();
        cuestionarioContestado.setRespuestas(respuestasCuestionario);
        cuestionarioContestado.agregarRespuestas(respuestasDeLaOrg);
        cuestionarioContestado.agregarRespuestas(respuestasGenerales);

        for(RespuestaConcreta respuestaConcreta : respuestasDeLaOrg){
            respuestaConcreta.setCuestionarioContestado(cuestionarioContestado);
        }
        for(RespuestaConcreta respuestaConcreta : respuestasGenerales){
            respuestaConcreta.setCuestionarioContestado(cuestionarioContestado);
        }

        publicacion.setFecha(LocalDate.now());
        publicacion.setCuestionario(cuestionarioContestado);
//        publicacion.setRespuestasGenerales(respuestasGenerales);
//        publicacion.setRespuestasOrganizacion(respuestasOrganizacion);
        publicacion.setMascota(mascota);
        publicacion.setOrganizacion(organizacion);
        publicacion.setTipoPublicacion("Mascota dada en adopcion");

        publicaciones.add(publicacion);

        //Datos Publicacion:
        //-Fecha
        //-Preguntas Organizacion (O sea que tiene que estar asociada a una organizacion. Elige una?)
        //-Preguntas generales
        //-Datos Mascota

    }

    public void generarPublicacionMascotaPerdida(Mascota mascota) {
        PublicacionPerdidaRegistrada publicacion = new PublicacionPerdidaRegistrada();
        publicacion.setFecha(LocalDate.now());
        publicacion.setMascota(mascota);
        publicacion.setTipoPublicacion("Mascota perdida");
        publicaciones.add(publicacion);

        //Datos Publicacion:
        //-Fecha
        //-Info de la mascota
        //-Estado (aprobada, pendiente, rechazada)

    }

    public void generarPublicacionMascotaEncontrada(Persona rescatista, DatosMascotaEncontrada datosMascota) {
        PublicacionMascotaEncontrada publicacion = new PublicacionMascotaEncontrada();
        publicacion.setEstadoDePublicacion(EstadoDePublicacion.SIN_REVISAR);
        publicacion.setDatosMascotaEncontrada(datosMascota);
        publicacion.setFecha(LocalDate.now());
        publicacion.setRescatista(rescatista);
        Organizacion organizacion = this.buscarOrganizacionMasCercana(datosMascota.getLugar());
        publicacion.setOrganizacion(organizacion);
        publicacion.setTipoPublicacion("Mascota encontrada");

        publicaciones.add(publicacion);

        //Datos Publicacion:
        //-Fecha
        //-Fotos
        //-Descripcion de como la encontro
        //-Lugar (seleccionable a traves de un mapa)
        //-Asignar organizacion mas cercana

    }

    public void generarPublicacionIntencionAdoptar(Persona adoptante, CuestionarioContestado cuestionarioContestadoPreferenciasYComodidades) {
        //Pasamos directamente las preguntas
        PublicacionIntencionAdopcion publicacion = new PublicacionIntencionAdopcion();
        publicacion.setAdoptante(adoptante);
        publicacion.setCuestionarioPreferenciasYComodidades(cuestionarioContestadoPreferenciasYComodidades);
        publicacion.setTipoPublicacion("Intencion de adoptar una mascota");
        publicacion.inicializarScheduler();

        publicaciones.add(publicacion);

    }


    public List<PublicacionEnAdopcion> getPublicacionesDeAdopcion() {
        List<PublicacionEnAdopcion> publicaciones = new ArrayList<>();
        for (PublicacionGenerica publicacion : this.publicaciones) {
            if (publicacion.getTipoPublicacion() == "Mascota dada en adopcion")
                publicaciones.add((PublicacionEnAdopcion) publicacion);
        }
        return publicaciones;
    }
}