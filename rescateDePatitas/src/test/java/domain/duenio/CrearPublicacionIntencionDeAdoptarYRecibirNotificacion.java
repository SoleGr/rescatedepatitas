package domain.duenio;

import domain.controllers.CaracteristicasController;
import domain.models.entities.mascotas.*;
import domain.models.entities.notificaciones.estrategias.Estrategia;
import domain.models.entities.personas.Contacto;
import domain.models.entities.personas.Persona;
import domain.models.entities.personas.TipoDeDocumento;
import domain.models.entities.publicaciones.CuestionarioContestado;
import domain.models.entities.publicaciones.GestorDePublicaciones;
import domain.models.entities.publicaciones.Pregunta;
import domain.models.entities.publicaciones.RespuestaConcreta;
import domain.models.entities.rol.Duenio;
import domain.models.entities.rol.Rol;
import domain.models.repositories.RepositorioDeCaracteristicas;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import services.EditorDeFotos;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;



public class CrearPublicacionIntencionDeAdoptarYRecibirNotificacion { /*
    //Email que va a recibir la notificacion
    private static final String EMAIL = "pedrokuljich@gmail.com";

//<<<<<<<<<<<<<<<<<<<<<<<<<<IMPORTANTE: Modificar el trigger en la publicacion al trigger de testeo>>>>>>>>>>>>>>>>>>>>
//El test esperara dos minutos y medio desde su inicio para que se pueda comprobar el funcionamiento del Scheduler
    Persona persona;
    Rol duenio;
    RepositorioDeCaracteristicas repoCaracteristicas;
    CaracteristicasController controller;
    CaracteristicaConRta caracteristicaConRta1, caracteristicaConRta2, caracteristicaConRta3, caracteristicaConRta4;
    List<Pregunta> caracteristicas;
    ArrayList<CaracteristicaConRta> caracteristicasConRtas, caracteristicasConRtas2;
    List<Contacto> contactos;
    Contacto contacto;
    EditorDeFotos editor;
    Organizacion organizacion;
    RespuestaConcreta rt1, rt2, rt3, rt4, respuestaCuestionarioPreferenciasYComodidades1, respuestaCuestionarioPreferenciasYComodidades2, respuestaCuestionarioPreferenciasYComodidades3;
    List<RespuestaConcreta> respuestasOrganizacion, respuestasCuestionarioPreferenciasYComodidades;
    CuestionarioContestado cuestionarioContestadoPreferenciasYComodidades;
    List<Pregunta> preguntasCuestionarioPreferenciasYComodidades;
    List<RespuestaConcreta> respuestasGenerales1, respuestasGenerales2;
    Pregunta preguntaTieneGatos;
    Pregunta preguntaTienePatio;
    Pregunta preg1;
    Pregunta preg2;



    @Before
    public void Instanciar() {
        persona = new Persona();
        duenio = new Duenio();
        repoCaracteristicas = new RepositorioDeCaracteristicas();
        contactos = new ArrayList<>();

        controller = CaracteristicasController.getInstancia();
        ArrayList<String> rtas1 = new ArrayList<>();
        rtas1.add("Sí");
        rtas1.add("No");
        controller.crearCaracteristica("Esta castrado", rtas1);

        ArrayList<String> rtas2 = new ArrayList<>();
        rtas2.add("Negro");
        rtas2.add("Marron");
        rtas2.add("Rubio");
        rtas2.add("Ninguno de estos");

        controller.crearCaracteristica("Color principal", rtas2);

        contacto = new Contacto("Pedro Elias", "Kuljich", "1112345678", EMAIL, Estrategia.EMAIL);
        contactos.add(contacto);

        persona.inicializar("Pedro Elias", "Kuljich", "Calle Falsa 123, CABA", TipoDeDocumento.DNI, 3141592, LocalDate.of(2000, 3, 3), contactos);
        persona.setRol(duenio);

        List<Pregunta> caracteristicas = controller.getRepositorio().caracteristicas;

        caracteristicaConRta1 = new CaracteristicaConRta(caracteristicas.get(0).getPregunta(), "Si");
        caracteristicaConRta2 = new CaracteristicaConRta(caracteristicas.get(1).getPregunta(), "Negro");

        caracteristicasConRtas = new ArrayList<>();
        caracteristicasConRtas.add(caracteristicaConRta1);
        caracteristicasConRtas.add(caracteristicaConRta2);

        List<Foto> fotos = new ArrayList<>();
        Foto foto = new Foto();
        foto.setURLfoto("src/main/resources/fotoDePrueba2.jpg");
        fotos.add(foto);
        editor = new EditorDeFotos();
        fotos = editor.redimensionarFotos(fotos);

        Mascota.MascotaDTO mascotaDTO = new Mascota.MascotaDTO();
        mascotaDTO.inicializar(persona, "Susana", "Susi", 2, "tiene una mancha blanca en una pata.",
                "gato", "hembra", caracteristicasConRtas, fotos);
        persona.registrarMascota(mascotaDTO);

        caracteristicaConRta3 = new CaracteristicaConRta(caracteristicas.get(0).getPregunta(), "Si");
        caracteristicaConRta4 = new CaracteristicaConRta(caracteristicas.get(1).getPregunta(), "Marron");

        caracteristicasConRtas2 = new ArrayList<>();
        caracteristicasConRtas2.add(caracteristicaConRta3);
        caracteristicasConRtas2.add(caracteristicaConRta4);

        ArrayList<Foto> fotos2 = new ArrayList<>();
        Foto foto2 = new Foto();
        foto2.setURLfoto("src/main/resources/FotoDePrueba.jpg");
        fotos2.add(foto2);

        fotos = editor.redimensionarFotos(fotos);

        mascotaDTO.inicializar(persona, "Romulo", "Rom", 4, "es gordo.",
                "perro", "macho", caracteristicasConRtas2, fotos2);
        persona.registrarMascota(mascotaDTO);

        organizacion = new Organizacion();

        preguntaTieneGatos = new Pregunta();
        preguntaTieneGatos.setPregunta("Tiene gatos?");

        preguntaTienePatio = new Pregunta();
        preguntaTienePatio.setPregunta("Tiene patio?");

        respuestasGenerales1 = new ArrayList<>();
        rt1 = new RespuestaConcreta();
        rt1.setPregunta(preguntaTieneGatos);
        rt1.setRespuesta("Si");

        rt2 = new RespuestaConcreta();
        rt2.setPregunta(preguntaTienePatio);
        rt2.setRespuesta("No");

        respuestasGenerales1.add(rt1);
        respuestasGenerales1.add(rt2);

        rt3 = new RespuestaConcreta();
        rt3.setPregunta(preguntaTienePatio);
        rt3.setRespuesta("Si");

        respuestasGenerales2 = new ArrayList<>();
        respuestasGenerales2.add(rt1);
        respuestasGenerales2.add(rt3);

        respuestasOrganizacion = new ArrayList<>();
        rt1 = new RespuestaConcreta();
        preg1 = new Pregunta();
        preg1.setPregunta("Raza");
        rt1.setPregunta(preg1);
        rt1.setRespuesta("Collie");

        rt2 = new RespuestaConcreta();
        preg2 = new Pregunta();
        preg2.setPregunta("Es amigable con niños?");
        rt2.setPregunta(preg2);
        rt2.setRespuesta("Si");

        respuestasOrganizacion.add(rt1);
        respuestasOrganizacion.add(rt2);



        preguntasCuestionarioPreferenciasYComodidades = new ArrayList<>();
        preguntasCuestionarioPreferenciasYComodidades.add(preg2);
        preguntasCuestionarioPreferenciasYComodidades.add(preguntaTieneGatos);
        preguntasCuestionarioPreferenciasYComodidades.add(preguntaTienePatio);

        cuestionarioContestadoPreferenciasYComodidades = new CuestionarioContestado();
        cuestionarioContestadoPreferenciasYComodidades.setPreguntas(preguntasCuestionarioPreferenciasYComodidades);
        respuestasCuestionarioPreferenciasYComodidades = new ArrayList<>();
        respuestaCuestionarioPreferenciasYComodidades1 = new RespuestaConcreta();
        respuestaCuestionarioPreferenciasYComodidades1.setPregunta(preg2);
        respuestaCuestionarioPreferenciasYComodidades1.setRespuesta("Si");
        respuestasCuestionarioPreferenciasYComodidades.add(respuestaCuestionarioPreferenciasYComodidades1);


        cuestionarioContestadoPreferenciasYComodidades.setPreguntas(preguntasCuestionarioPreferenciasYComodidades);
        respuestaCuestionarioPreferenciasYComodidades2 = new RespuestaConcreta();
        respuestaCuestionarioPreferenciasYComodidades2.setPregunta(preguntaTieneGatos);
        respuestaCuestionarioPreferenciasYComodidades2.setRespuesta("Si");
        respuestasCuestionarioPreferenciasYComodidades.add(respuestaCuestionarioPreferenciasYComodidades2);

        respuestaCuestionarioPreferenciasYComodidades3 = new RespuestaConcreta();
        respuestaCuestionarioPreferenciasYComodidades3.setPregunta(preguntaTienePatio);
        respuestaCuestionarioPreferenciasYComodidades3.setRespuesta("Si");
        respuestasCuestionarioPreferenciasYComodidades.add(respuestaCuestionarioPreferenciasYComodidades3);
        cuestionarioContestadoPreferenciasYComodidades.setRespuestas(respuestasCuestionarioPreferenciasYComodidades);
    }


    @Test
    public void CreoPublicacionesIntencionDeAdoptaryEsperoLaNotificacion() {
        //Pongo una Mascota en adopcion
        persona.darEnAdopcion(persona.getMascotas().get(0), organizacion, respuestasOrganizacion, respuestasGenerales1 );
        GestorDePublicaciones gestor = GestorDePublicaciones.getInstancia();
        Assert.assertEquals("Mascota dada en adopcion", gestor.getPublicaciones().get(0).getTipoPublicacion());
        Assert.assertEquals(persona.getMascotas().get(0), gestor.getPublicaciones().get(0).getMascota());

        persona.darEnAdopcion(persona.getMascotas().get(1),organizacion, respuestasOrganizacion, respuestasGenerales2 );

        persona.intencionDeAdoptar(cuestionarioContestadoPreferenciasYComodidades);

        try {
            Thread.sleep(15000);
        } catch (InterruptedException e) {}
    } */
}
