package domain.db;

import db.EntityManagerHelper;
import domain.controllers.CaracteristicasController;
import domain.controllers.PreguntasController;
import domain.models.entities.mascotas.*;
import domain.models.entities.notificaciones.estrategias.Estrategia;
import domain.models.entities.personas.Contacto;
import domain.models.entities.personas.Persona;
import domain.models.entities.personas.TipoDeDocumento;
import domain.models.entities.publicaciones.*;
import domain.models.entities.rol.Administrador;
import domain.models.entities.rol.Duenio;
import domain.models.entities.rol.Rescatista;
import domain.models.entities.rol.Voluntario;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CreacionEntidades {
    Persona persona,persona2,persona3;
    Contacto contacto, contacto2,contacto3;
    List<Contacto> contactos,contactos2,contactos3;
    PreguntasController controller;
    CaracteristicaConRta caracteristicaConRta1, caracteristicaConRta2, caracteristicaConRta3, caracteristicaConRta4;
    ArrayList<CaracteristicaConRta> caracteristicasConRtas, caracteristicasConRtas2;
    Organizacion org1,org2,org3,org4,org5;
    Lugar puntoEncuentro;
    List<Organizacion> organizaciones;
    GestorDePublicaciones gestor;
    Organizacion organizacion;
    RespuestaConcreta rt1, rt2,rt3,rt4;
    List<RespuestaConcreta> respuestasOrganizacion;
    CuestionarioContestado cuestionarioContestadoDeAdopcion;
    List<Pregunta> preguntasCuestionario = new ArrayList<>();
    List<RespuestaConcreta> respuestasGenerales;
    Pregunta preguntaTieneGatos;
    Pregunta preguntaTienePatio;
    Pregunta preg1;
    Pregunta preg2;
    CuestionarioContestado cuestionarioContestadoPyC;
    List<RespuestaConcreta> respuestasCuestionarioPyC;
    List<Pregunta> preguntasCuestionarioPyC;
    RespuestaConcreta rta3, rta4,r3,r4;
    Pregunta preg3,preg4;
    List<RespuestaConcreta> respuestas;
    DatosMascotaEncontrada encontrada;


    @Before
    public void setup(){
        //Agrego 5 organizaciones al gestor
        organizaciones = new ArrayList<>();
        org1 = new Organizacion();
        org1.setNombre("Huellitas");
        org1.setUbicacion(new Lugar(-34.6335328,-58.4921025));
        organizaciones.add(org1);

        org2 = new Organizacion();
        org2.setNombre("Naricitas Frias");
        org2.setUbicacion(new Lugar(-34.5888834,-58.5455626));
        organizaciones.add(org2);

        org3 = new Organizacion();
        org3.setNombre("El Hogar de Claudia");
        org3.setUbicacion(new Lugar(-34.6038713,-58.5754228));
        organizaciones.add(org3);

        org4 = new Organizacion();
        org4.setNombre("Ayudacan");
        org4.setUbicacion(new Lugar(-34.6321582,-58.468661));
        organizaciones.add(org4);

        org5 = new Organizacion();
        org5.setNombre("El refugio"); //Lugano
        org5.setUbicacion(new Lugar(-34.6766714,-58.4790033));
        organizaciones.add(org5);
        gestor = GestorDePublicaciones.getInstancia();
        gestor.setOrganizaciones(organizaciones);

        //Persona 1 con contactos
        persona = new Persona();
        persona.setNombre("Maria");
        persona.setApellido("Gonzalez");
        persona.setFechaDeNacimiento(LocalDate.of(1956, 9, 24));
        persona.setTipoDoc(TipoDeDocumento.DNI);
        persona.setNroDoc(9444444);
        persona.setDireccion("Av Mitre 234");
        contacto = new Contacto("Soledad", "Grilleta", "+541157530658", "sole.012@gmail.com", Estrategia.EMAIL);
        contacto.setPersona(persona);
        contactos = new ArrayList<>();
        contactos.add(contacto);
        persona.setContactos(contactos);

        //Persona 2 con contactos
        persona2 = new Persona();
        persona2.setNombre("Julio");
        persona2.setApellido("Miguel");
        persona2.setFechaDeNacimiento(LocalDate.of(1987, 3, 11));
        persona2.setTipoDoc(TipoDeDocumento.DNI);
        persona2.setNroDoc(34444444);
        persona2.setDireccion("Las heras 2461");
        contacto2 = new Contacto("Julio", "Miguel", "+541198766543", "julioMiguel_24868@gmail.com", Estrategia.EMAIL);
        contacto2.setPersona(persona2);
        contactos2 = new ArrayList<>();
        contactos2.add(contacto2);
        persona2.setContactos(contactos2);
        Rescatista rescatista = new Rescatista();
        rescatista.setPersona(persona2);
        persona2.addRol(rescatista);
        persona2.setRolElegido(rescatista);

        //Datos Mascota encontrada
        List<Foto> fotos3 = new ArrayList<>();
        Foto foto3 = new Foto();
        foto3.setURLfoto("img/perro3.jpg");
        encontrada = new DatosMascotaEncontrada(fotos3,"En buen estado",new Lugar(12345,12234));
        foto3.setDatosMascotaEncontrada(encontrada);
        fotos3.add(foto3);
        gestor = GestorDePublicaciones.getInstancia();
        gestor.generarPublicacionMascotaEncontrada(persona2, encontrada);

        //Persona 3 con contactos
        persona3 = new Persona();
        contacto3 = new Contacto("Carlos", "Mendez", "+541157535555", "x0sole0x@gmail.com", Estrategia.EMAIL);
        contacto3.setPersona(persona3);
        contactos3 = new ArrayList<>();
        contactos3.add(contacto3);
        persona3.inicializar("Sole", "Grilletta", "Peru 1212,CABA", TipoDeDocumento.DNI, 3333333, LocalDate.of(1987, 9, 24),contactos3);


        //Donde se encontro una mascota
        puntoEncuentro = new Lugar(-34.6621347,-58.4803575); //Campus

        //Persona 1 tiene usuario y es duenio
        persona.crearUsuario("Victoria09", "MiPerro22!!##");
        Duenio duenio = new Duenio();
        duenio.setPersona(persona);
        Voluntario voluntario = new Voluntario();
        voluntario.setPersona(persona);
        rescatista = new Rescatista();
        rescatista.setPersona(persona);
        Administrador administrador = new Administrador();
        administrador.setPersona(persona);
        persona.addRol(duenio);
        persona.addRol(voluntario);
        persona.addRol(rescatista);
        persona.addRol(administrador);
        persona.setRolElegido(duenio);

        //Agrego caracteristicas generales
        controller = PreguntasController.getInstancia();
        ArrayList<String> rtas = new ArrayList<>();
        rtas.add("Si");
        rtas.add("No");
        controller.crearCaracteristica("Esta castrado", rtas,"general");


        ArrayList<String> rtas2 = new ArrayList<>();
        rtas2.add("Negro");
        rtas2.add("Marron");
        rtas2.add("Rubio");
        rtas2.add("Ninguno de estos");
        controller.crearCaracteristica("Color principal", rtas2,"general");

        //Agrego preguntas para una organizacion
        ArrayList<Pregunta> preguntasOrganizacion = new ArrayList<>();
        Pregunta preguntaOrg1 = new Pregunta();
        preguntaOrg1.setPregunta("Es malcriado?");
        ArrayList<String> respuestasPreguntasOrg = new ArrayList<>();
        respuestasPreguntasOrg.add("Sí");
        respuestasPreguntasOrg.add("No");
        preguntaOrg1.setRespuestas(respuestasPreguntasOrg);
        preguntaOrg1.setOrganizacion(org1);
        preguntasOrganizacion.add(preguntaOrg1);
        org1.setPreguntasAdopcion(preguntasOrganizacion);



        List<Pregunta> caracteristicas = controller.getRepositorio().buscarPorTipo("general");

        caracteristicaConRta1 = new CaracteristicaConRta(caracteristicas.get(0).getPregunta(),"Si");
        caracteristicaConRta2 = new CaracteristicaConRta(caracteristicas.get(1).getPregunta(),"Negro");
        //Armo la lista de caracteristicas para agregar a la mascota
        caracteristicasConRtas = new ArrayList<>();
        caracteristicasConRtas.add(caracteristicaConRta1);
        caracteristicasConRtas.add(caracteristicaConRta2);

        caracteristicaConRta3 = new CaracteristicaConRta(caracteristicas.get(0).getPregunta(),"No");
        caracteristicaConRta4 = new CaracteristicaConRta(caracteristicas.get(1).getPregunta(),"Marron");

        //Armo la lista de caracteristicas para agregar a la mascota
        caracteristicasConRtas2 = new ArrayList<>();
        caracteristicasConRtas2.add(caracteristicaConRta3);
        caracteristicasConRtas2.add(caracteristicaConRta4);

        //Redimensiono las fotos para agregar a la mascota
        List<Foto> fotos = new ArrayList<>();
        Foto foto = new Foto();
        foto.setURLfoto("img/perro1.jpg");
        fotos.add(foto);

        List<Foto> fotos2 = new ArrayList<>();
        Foto foto2 = new Foto();
        foto2.setURLfoto("img/gato2.jpg");
        fotos2.add(foto2);

        Mascota.MascotaDTO mascotaDTO = new Mascota.MascotaDTO();
        Mascota.MascotaDTO mascotaDTO2 = new Mascota.MascotaDTO();
        mascotaDTO.inicializar(persona,"Susana","Susi",2,"tiene una mancha blanca en una pata.",
                "gato", "hembra", caracteristicasConRtas, fotos);
        mascotaDTO2.inicializar(persona,"Firulais","Firu",5,"tiene collar rojo.",
                "perro", "macho", caracteristicasConRtas2, fotos2);
        persona.registrarMascota(mascotaDTO);
        persona.registrarMascota(mascotaDTO2);
        Mascota mascota1 = persona.getMascotas().get(0);
        Mascota mascota2 = persona.getMascotas().get(1);
        foto.setMascota(mascota1);
        foto2.setMascota(mascota2);
        mascota1.setDuenio(duenio);
        mascota2.setDuenio(duenio);

        caracteristicaConRta1.setMascota(mascota1);
        caracteristicaConRta2.setMascota(mascota1);
        caracteristicaConRta3.setMascota(mascota2);
        caracteristicaConRta4.setMascota(mascota2);

        //Cuestionario
        //Seteo las preguntas
        preguntaTieneGatos= new Pregunta();
        preguntaTieneGatos.setPregunta("Tiene gatos?");
        preguntaTieneGatos.setTipoDePregunta("pyc");
        preguntaTienePatio = new Pregunta();
        preguntaTienePatio.setPregunta("Tiene patio en su casa?");
        preguntaTienePatio.setTipoDePregunta("pyc");

        //Seteo las respuestas
        respuestas = new ArrayList<>();
        rt1 = new RespuestaConcreta();
        rt1.setPregunta(preguntaTieneGatos);
        rt1.setRespuesta("Si");

        rt2 = new RespuestaConcreta();
        rt2.setPregunta(preguntaTienePatio);
        rt2.setRespuesta("No");

        respuestas.add(rt1);
        respuestas.add(rt2);

        //Cuestionario PreferenciasYComodidades
        cuestionarioContestadoPyC = new CuestionarioContestado();
        preg1 = new Pregunta();
        preg2 = new Pregunta();
        preg1.setPregunta("Tamaño");
        preg2.setPregunta("Edad");

        rta3 = new RespuestaConcreta();
        rta4 = new RespuestaConcreta();
        rta3.setPregunta(preg1);
        rta3.setRespuesta("Grande");
        rta4.setPregunta(preg2);
        rta4.setRespuesta("Cachorro");

        preguntasCuestionarioPyC = new ArrayList<>();
        preguntasCuestionarioPyC.add(preg1);
        preguntasCuestionarioPyC.add(preg2);

        respuestasCuestionarioPyC = new ArrayList<>();
        respuestasCuestionarioPyC.add(rt1);
        respuestasCuestionarioPyC.add(rt2);

        preg3 = new Pregunta();
        preg4 = new Pregunta();
        preg3.setPregunta("¿Tiene Patio?");
        preg3.setTipoDePregunta("pyc");
        preg4.setPregunta("¿Tiene un canil cerca?");
        preg4.setTipoDePregunta("pyc");

        r3 = new RespuestaConcreta();
        r4 = new RespuestaConcreta();
        r3.setPregunta(preg3);
        r3.setRespuesta("Si");
        r4.setPregunta(preg4);
        r4.setRespuesta("No");

        preguntasCuestionarioPyC.add(preg3);
        preguntasCuestionarioPyC.add(preg4);
        respuestasCuestionarioPyC.add(r3);
        respuestasCuestionarioPyC.add(r4);

        cuestionarioContestadoPyC.setRespuestas(respuestasCuestionarioPyC);

        //Publicacion En Adopcion
        Cuestionario cuest = new Cuestionario();
        respuestasGenerales = new ArrayList<>();
        rt1 = new RespuestaConcreta();
        rt1.setPregunta(preguntaTieneGatos);
        rt1.setRespuesta("Si");

        rt2 = new RespuestaConcreta();
        rt2.setPregunta(preguntaTienePatio);
        rt2.setRespuesta("No");

        respuestasGenerales.add(rt1);
        respuestasGenerales.add(rt2);

        respuestasOrganizacion = new ArrayList<>();
        ArrayList<String> opcionesRespuesta = new ArrayList<>();
        rt1 = new RespuestaConcreta();
        preg1= new Pregunta();
        preg1.setPregunta("Raza");
        preg1.setTipoDePregunta("asociacion");
        preg1.setVisible(true);
        opcionesRespuesta.add("Collie");
        opcionesRespuesta.add("Chihuahua");
        opcionesRespuesta.add("Beagle");
        opcionesRespuesta.add("Pug");
        opcionesRespuesta.add("Beagle");
        preg1.setRespuestas(opcionesRespuesta);
        preg1.setOrganizacion(org1);
        preg1.setCuestionario(cuest);
        rt1.setPregunta(preg1);
        rt1.setRespuesta("Collie");

        rt2 = new RespuestaConcreta();
        preg2 = new Pregunta();
        preg2.setPregunta("Es amigable con niños?");
        preg2.setTipoDePregunta("asociacion");
        preg2.setVisible(true);
        preg2.setOrganizacion(organizacion);
        preg2.setCuestionario(cuest);
        rt2.setPregunta(preg2);
        rt2.setRespuesta("Si");

        respuestasOrganizacion.add(rt1);
        respuestasOrganizacion.add(rt2);
        preguntasCuestionario.add(preg1);
        preguntasCuestionario.add(preg2);
        cuest.setPreguntas(preguntasCuestionario);

        gestor = GestorDePublicaciones.getInstancia();
        gestor.generarPublicacionEnAdopcion(persona.getMascotas().get(0),respuestasOrganizacion,respuestasGenerales,organizacion);

        //Publicacion Perdida Registrada
        persona.getMascotas().get(1).setOrganizacion(organizacion);
        gestor.generarPublicacionMascotaPerdida(persona.getMascotas().get(1));

        //Publicacion Intencion de Adopcion
        gestor.generarPublicacionIntencionAdoptar(persona, cuestionarioContestadoPyC);

    }

    @Test
    public void persistirUsuarioTest1(){

        EntityManagerHelper.beginTransaction();
        EntityManagerHelper.getEntityManager().persist(persona);
        EntityManagerHelper.getEntityManager().persist(persona2);
        EntityManagerHelper.getEntityManager().persist(persona3);
        EntityManagerHelper.commit();
    }

    @Test
    public void recuperandoAJulio(){
        Persona julio = (Persona) EntityManagerHelper.createQuery("from Persona where id = 1").getSingleResult();
        Assert.assertEquals("Julio", julio.getNombre());
    }

    @Test
    public void eliminandoAJulio(){
        /*
        Persona julio = (Persona) EntityManagerHelper.createQuery("from Persona where nombre = 'Julio' and id = 2").getSingleResult();

        EntityManagerHelper.beginTransaction();
        EntityManagerHelper.getEntityManager().remove(julio);
        EntityManagerHelper.commit();
        */
    }

    @Test
    public void persistirTodo(){

        Duenio duenio = (Duenio) persona.getRolElegido();

        gestor = GestorDePublicaciones.getInstancia();
        gestor.generarPublicacionMascotaEncontrada(persona2, encontrada);

        EntityManagerHelper.beginTransaction();
        EntityManagerHelper.getEntityManager().persist(persona);
        EntityManagerHelper.getEntityManager().persist(persona2);
        EntityManagerHelper.getEntityManager().persist(persona3);
        EntityManagerHelper.getEntityManager().persist(org1);
        EntityManagerHelper.getEntityManager().persist(org2);
        EntityManagerHelper.getEntityManager().persist(org3);
        EntityManagerHelper.getEntityManager().persist(org4);
        EntityManagerHelper.getEntityManager().persist(org5);
        EntityManagerHelper.getEntityManager().persist(duenio.getMascotas().get(0));
        EntityManagerHelper.getEntityManager().persist(duenio.getMascotas().get(1));
        //EntityManagerHelper.getEntityManager().persist(gestor.getPublicaciones().get(0));
        //EntityManagerHelper.getEntityManager().persist(gestor.getPublicaciones().get(1));
        //EntityManagerHelper.getEntityManager().persist(gestor.getPublicaciones().get(2));
        //EntityManagerHelper.getEntityManager().persist(gestor.getPublicaciones().get(3));
        EntityManagerHelper.commit();

    }

    @Test
    public void persistirPublicaciones(){
        //Publicacion encontrada
        Rescatista rescatista = new Rescatista();
        persona2.addRol(rescatista);
        persona2.setRolElegido(rescatista);
        List<Foto> fotos3 = new ArrayList<>();
        Foto foto3 = new Foto();
        foto3.setURLfoto("img/perro3.jpg");
        DatosMascotaEncontrada encontrada = new DatosMascotaEncontrada(fotos3,"En buen estado",new Lugar(12345,12234));
        foto3.setDatosMascotaEncontrada(encontrada);
        fotos3.add(foto3);
        gestor = GestorDePublicaciones.getInstancia();
        gestor.generarPublicacionMascotaEncontrada(persona2, encontrada);

        //Publicacion En Adopcion
        Cuestionario cuest = new Cuestionario();
        organizacion = new Organizacion();
        organizacion.setNombre("VeteCan");
        organizacion.setUbicacion(new Lugar(5432,2345));
        respuestasGenerales = new ArrayList<>();
        rt1 = new RespuestaConcreta();
        rt1.setPregunta(preguntaTieneGatos);
        rt1.setRespuesta("Si");

        rt2 = new RespuestaConcreta();
        rt2.setPregunta(preguntaTienePatio);
        rt2.setRespuesta("No");

        respuestasGenerales.add(rt1);
        respuestasGenerales.add(rt2);

        respuestasOrganizacion = new ArrayList<>();
        rt1 = new RespuestaConcreta();
        preg1= new Pregunta();
        preg1.setPregunta("Raza");
        preg1.setTipoDePregunta("Caracteristica");
        preg1.setVisible(true);
        preg1.setOrganizacion(organizacion);
        preg1.setCuestionario(cuest);
        rt1.setPregunta(preg1);
        rt1.setRespuesta("Collie");

        rt2 = new RespuestaConcreta();
        preg2 = new Pregunta();
        preg2.setPregunta("Es amigable con niños?");
        preg2.setTipoDePregunta("Caracteristica");
        preg2.setVisible(true);
        preg2.setOrganizacion(organizacion);
        preg2.setCuestionario(cuest);
        rt2.setPregunta(preg2);
        rt2.setRespuesta("Si");

        respuestasOrganizacion.add(rt1);
        respuestasOrganizacion.add(rt2);
        preguntasCuestionario.add(preg1);
        preguntasCuestionario.add(preg2);
        cuest.setPreguntas(preguntasCuestionario);

        gestor = GestorDePublicaciones.getInstancia();
        gestor.generarPublicacionEnAdopcion(persona.getMascotas().get(0),respuestasOrganizacion,respuestasGenerales,organizacion);

        //Publicacion Perdida Registrada
        persona.getMascotas().get(1).setOrganizacion(organizacion);
        gestor.generarPublicacionMascotaPerdida(persona.getMascotas().get(1));

        //Publicacion Intencion de Adopcion
        gestor.generarPublicacionIntencionAdoptar(persona, cuestionarioContestadoPyC);

        EntityManagerHelper.beginTransaction();
        EntityManagerHelper.getEntityManager().persist(gestor.getPublicaciones().get(0));
        EntityManagerHelper.getEntityManager().persist(gestor.getPublicaciones().get(1));
        EntityManagerHelper.getEntityManager().persist(gestor.getPublicaciones().get(2));
        EntityManagerHelper.getEntityManager().persist(gestor.getPublicaciones().get(3));
        EntityManagerHelper.commit();

    }

}
