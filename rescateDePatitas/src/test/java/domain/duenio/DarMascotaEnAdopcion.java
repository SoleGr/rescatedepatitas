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

public class DarMascotaEnAdopcion {
    Persona persona;
    Rol duenio;
    RepositorioDeCaracteristicas repoCaracteristicas;
    CaracteristicasController controller;
    CaracteristicaConRta caracteristicaConRta1, caracteristicaConRta2, caracteristicaConRta3, caracteristicaConRta4;
    ArrayList<CaracteristicaConRta> caracteristicasConRtas, caracteristicasConRtas2;
    List<Pregunta> caracteristicas;
    List<Contacto> contactos;
    List<Foto> fotos;
    Contacto contacto1,contacto2;
    EditorDeFotos editor;
    Organizacion organizacion;
    RespuestaConcreta rt1, rt2,rt3,rt4;
    List<RespuestaConcreta> respuestasOrganizacion;
    CuestionarioContestado cuestionarioContestadoDeAdopcion;
    List<Pregunta> preguntasCuestionario;
    List<RespuestaConcreta> respuestasGenerales;
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

        //Cargo caracteristicas al repositorio con el controller
        controller = CaracteristicasController.getInstancia();
        ArrayList<String> rtas = new ArrayList<>();
        rtas.add("Si");
        rtas.add("No");
        controller.crearCaracteristica("Esta castrado", rtas);

        ArrayList<String> rtas2 = new ArrayList<>();
        rtas2.add("Negro");
        rtas2.add("Marron");
        rtas2.add("Rubio");
        rtas2.add("Ninguno de estos");
        controller.crearCaracteristica("Color principal", rtas2);
        //Termino de cargar caracteristicas al repositorio

        contacto1 = new Contacto("Maria Victoria","Sanchez","1155555555","mvicsanchez@gmail.com", Estrategia.SMS);
        contacto2 = new Contacto("Agustin","Greco","1166666666","agugreco@gmail.com", Estrategia.SMS);
        contactos.add(contacto1);
        contactos.add(contacto2);

        persona.inicializar("Maria Victoria","Sanchez","Peru 1212,CABA", TipoDeDocumento.DNI,3333333, LocalDate.of(1987, 9, 24),contactos);

        persona.setRolElegido(duenio);

        List<Pregunta> caracteristicas = controller.getRepositorio().caracteristicas;

        caracteristicaConRta1 = new CaracteristicaConRta(caracteristicas.get(0).getPregunta(),"Si");
        caracteristicaConRta2 = new CaracteristicaConRta(caracteristicas.get(1).getPregunta(),"Negro");

        caracteristicasConRtas = new ArrayList<>();
        caracteristicasConRtas.add(caracteristicaConRta1);
        caracteristicasConRtas.add(caracteristicaConRta2);

        List<Foto> fotos = new ArrayList<>();
        Foto foto = new Foto();
        foto.setURLfoto("src/main/resources/FotoDePrueba2.jpg");
        fotos.add(foto);
        editor = new EditorDeFotos();
        fotos= editor.redimensionarFotos(fotos);

        Mascota.MascotaDTO mascotaDTO = new Mascota.MascotaDTO();
        mascotaDTO.inicializar(persona,"Susana","Susi",2,"tiene una mancha blanca en una pata.",
                "gato", "hembra", caracteristicasConRtas, fotos);
        persona.registrarMascota(mascotaDTO);

        caracteristicaConRta3 = new CaracteristicaConRta(caracteristicas.get(0).getPregunta(),"No");
        caracteristicaConRta4 = new CaracteristicaConRta(caracteristicas.get(1).getPregunta(),"Marron");

        caracteristicasConRtas2 = new ArrayList<>();
        caracteristicasConRtas2.add(caracteristicaConRta3);
        caracteristicasConRtas2.add(caracteristicaConRta4);

        ArrayList<Foto> fotos2 = new ArrayList<>();
        Foto foto2 = new Foto();
        foto2.setURLfoto("src/main/resources/FotoDePrueba.jpg");
        fotos2.add(foto2);

        fotos= editor.redimensionarFotos(fotos);

        mascotaDTO.inicializar(persona,"Susana","Susi",2,"tiene una mancha blanca en una pata.",
                "gato", "hembra", caracteristicasConRtas, fotos);
        persona.registrarMascota(mascotaDTO);

        organizacion = new Organizacion();

        respuestasGenerales = new ArrayList<>();
        rt1 = new RespuestaConcreta();
        rt1.setPregunta(preguntaTieneGatos);
        rt1.setRespuesta("Si");

        rt2 = new RespuestaConcreta();
        rt2.setPregunta(preguntaTienePatio);
        rt2.setRespuesta("No");

        respuestasGenerales.add(rt1);
        respuestasGenerales.add(rt2);

// preguntas propias de la org
/*
        preguntaTieneGatos = new Pregunta();
        preguntaTieneGatos.setPregunta("Tiene gatos?");
        preguntaTienePatio = new Pregunta();
        preguntaTienePatio.setPregunta("Tiene patio en su casa?");
*/
        respuestasOrganizacion = new ArrayList<>();
        rt1 = new RespuestaConcreta();
        preg1= new Pregunta();
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
/* // cuestionario
        cuestionarioDeAdopcion = new Cuestionario();


        preg1 = new Pregunta();
        preg1.setPregunta("Edad");
        preg2= new Pregunta();
        preg2.setPregunta("Raza");

        rt3 = new RespuestaSobrePregunta();
        rt3.setRespuesta("1");
        rt4 = new RespuestaSobrePregunta();
        rt4.setRespuesta("Collie");

        preguntasCuestionario = new ArrayList<>();
        preguntasCuestionario.add(preg2);
        preguntasCuestionario.add(preg1);

        respuestasCuestionario = new ArrayList<>();
        respuestasCuestionario.add(rt3);
        respuestasCuestionario.add(rt4);

        cuestionarioDeAdopcion.setPreguntas(preguntasCuestionario);
        cuestionarioDeAdopcion.setRespuestas(respuestasCuestionario);
*/
    }


    @Test
    public void darEnAdopcionMiMascota() {
        persona.darEnAdopcion(persona.getMascotas().get(0),organizacion,respuestasOrganizacion,respuestasGenerales);
        GestorDePublicaciones gestor = GestorDePublicaciones.getInstancia();
        Assert.assertEquals("Mascota dada en adopcion",gestor.getPublicaciones().get(0).getTipoPublicacion());
        Assert.assertEquals(persona.getMascotas().get(0),gestor.getPublicaciones().get(0).getMascota());
    }


}
