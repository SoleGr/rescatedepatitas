package domain.duenio;

import domain.controllers.CaracteristicasController;
import domain.models.entities.mascotas.CaracteristicaConRta;
import domain.models.entities.mascotas.Foto;
import domain.models.entities.mascotas.Mascota;
import domain.models.entities.notificaciones.estrategias.Estrategia;
import domain.models.entities.personas.Contacto;
import domain.models.entities.personas.Persona;
import domain.models.entities.personas.TipoDeDocumento;
import domain.models.entities.publicaciones.Pregunta;
import domain.models.entities.rol.Duenio;
import domain.models.entities.rol.Rol;
import domain.models.repositories.RepositorioDeCaracteristicas;
import org.junit.Before;
import org.junit.Test;
import services.EditorDeFotos;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class PersonaConMascota {
    Persona persona;
    Rol duenio;
    RepositorioDeCaracteristicas repoCaracteristicas;
    CaracteristicasController controller;
    CaracteristicaConRta caracteristicaConRta1, caracteristicaConRta2, caracteristicaConRta3, caracteristicaConRta4;
    ArrayList<CaracteristicaConRta> caracteristicasConRtas, caracteristicasConRtas2;
    Mascota mascota,mascota2;
    List<Contacto> contactos;
    Contacto contacto1,contacto2;
    EditorDeFotos editor;


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
    }

    @Test
    public void crearPersonaConMascotasTest() {

        contacto1 = new Contacto("Maria Victoria","Sanchez","1155555555","mvicsanchez@gmail.com", Estrategia.SMS);
        contacto2 = new Contacto("Agustin","Greco","1166666666","agugreco@gmail.com", Estrategia.SMS);
        contactos.add(contacto1);
        contactos.add(contacto2);

        persona.inicializar("Maria Victoria","Sanchez","Peru 1212,CABA",TipoDeDocumento.DNI,3333333, LocalDate.of(1987, 9, 24),contactos);

        System.out.println ("Datos del Dueño");
        System.out.println ("Nombre:" + persona.getNombre());
        System.out.println ("Apellido:" + persona.getApellido());
        System.out.println ("Direccion:" + persona.getDireccion());
        System.out.println ("Tipo de documento:" + persona.getTipoDoc());
        System.out.println ("Numero de documento:" + persona.getNroDoc());
        System.out.println ("Fecha de nacimiento:" + persona.getFechaDeNacimiento());
        System.out.println ("-------------");

        //Agrego el rol duenio a la persona para que pueda registrar sus mascotas
        persona.setRolElegido(duenio);

        //Registro de 1 mascota

        List<Pregunta> caracteristicas = controller.getRepositorio().caracteristicas;

        caracteristicaConRta1 = new CaracteristicaConRta(caracteristicas.get(0).getPregunta(),"Si");
        caracteristicaConRta2 = new CaracteristicaConRta(caracteristicas.get(1).getPregunta(),"Negro");

        //Armo la lista de caracteristicas para agregar a la mascota
        caracteristicasConRtas = new ArrayList<>();
        caracteristicasConRtas.add(caracteristicaConRta1);
        caracteristicasConRtas.add(caracteristicaConRta2);

        //Redimensiono las fotos para agregar a la mascota
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

        //Listo los datos de la mascota 1 cargada
        System.out.println ("Datos de la Mascota");
        mascota = persona.getMascotas().get(0);

        System.out.println ("ID Mascota:" + mascota.getIdMascota());
        System.out.println ("Nombre:" + mascota.getNombre());
        System.out.println ("Apodo:" + mascota.getApodo());
        System.out.println ("Edad:" + mascota.getEdad());
        System.out.println ("Descripcion:" + mascota.getDescripcion());
        System.out.println ("Especie:" + mascota.getEspecie());
        System.out.println ("Genero:" + mascota.getGenero());
        String descripcion1 = mascota.getCaracteristicas().get(0).getDescripcion();
        String respuesta1 = mascota.getCaracteristicas().get(0).getRespuestaElegida();
        System.out.println (descripcion1 + ":"+respuesta1);
        String descripcion2 = mascota.getCaracteristicas().get(1).getDescripcion();
        String respuesta2 = mascota.getCaracteristicas().get(1).getRespuestaElegida();
        System.out.println (descripcion2 + ":"+respuesta2);
        System.out.println ("-------------");

        //Registro de mascota 2

        //Agrego las caracteristicas con su respuesta elegida para registrar la mascota
        caracteristicaConRta3 = new CaracteristicaConRta(caracteristicas.get(0).getPregunta(),"No");
        caracteristicaConRta4 = new CaracteristicaConRta(caracteristicas.get(1).getPregunta(),"Marron");

        caracteristicasConRtas2 = new ArrayList<>();
        caracteristicasConRtas2.add(caracteristicaConRta3);
        caracteristicasConRtas2.add(caracteristicaConRta4);

        //Redimensiono la foto de la mascota 2
        ArrayList<Foto> fotos2 = new ArrayList<>();
        Foto foto2 = new Foto();
        foto2.setURLfoto("src/main/resources/FotoDePrueba.jpg");
        fotos2.add(foto2);

        fotos= editor.redimensionarFotos(fotos);

        mascotaDTO.inicializar(persona,"Susana","Susi",2,"tiene una mancha blanca en una pata.",
                "gato", "hembra", caracteristicasConRtas, fotos);
        persona.registrarMascota(mascotaDTO);

        //Listo los datos de la mascota 2

        mascota2 = persona.getMascotas().get(1);
        System.out.println ("Datos de la Mascota");
        System.out.println ("ID Mascota:" + mascota2.getIdMascota());
        System.out.println ("Nombre:" + mascota2.getNombre());
        System.out.println ("Apodo:" + mascota2.getApodo());
        System.out.println ("Edad:" + mascota2.getEdad());
        System.out.println ("Descripcion:" + mascota2.getDescripcion());
        System.out.println ("Especie:" + mascota2.getEspecie());
        System.out.println ("Genero:" + mascota2.getGenero());
        descripcion1 = mascota2.getCaracteristicas().get(0).getDescripcion();
        respuesta1 = mascota2.getCaracteristicas().get(0).getRespuestaElegida();
        System.out.println (descripcion1 + ":"+respuesta1);
        descripcion2 = mascota2.getCaracteristicas().get(1).getDescripcion();
        respuesta2 = mascota2.getCaracteristicas().get(1).getRespuestaElegida();
        System.out.println (descripcion2 + ":"+respuesta2);

        System.out.println ("-------------");
    }
}
