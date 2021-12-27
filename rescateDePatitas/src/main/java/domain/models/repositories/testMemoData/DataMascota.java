package domain.models.repositories.testMemoData;

import db.EntityManagerHelper;
import domain.controllers.CaracteristicasController;
import domain.models.entities.Persistente;
import domain.models.entities.mascotas.CaracteristicaConRta;
import domain.models.entities.mascotas.Foto;
import domain.models.entities.mascotas.Mascota;
import domain.models.entities.personas.Persona;
import domain.models.entities.publicaciones.Pregunta;
import services.EditorDeFotos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DataMascota {
    private static List<Mascota> mascotas = new ArrayList<>();

    public static List<Persistente> getList() {
        if(mascotas.size() == 0) {
            Persona duenio = new Persona();
            CaracteristicasController controller;

            // TODO SACARLO DE ACA
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
            // TODO SACARLO DE ACA

            Mascota.MascotaDTO mascotaDTO = new Mascota.MascotaDTO();
            List<Pregunta> caracteristicas = controller.getRepositorio().caracteristicas;

            CaracteristicaConRta caracteristicaConRta1 = new CaracteristicaConRta(caracteristicas.get(0).getPregunta(),"Si");
            CaracteristicaConRta caracteristicaConRta2 = new CaracteristicaConRta(caracteristicas.get(1).getPregunta(),"Negro");

            ArrayList<CaracteristicaConRta> caracteristicasConRtas = new ArrayList<>();
            caracteristicasConRtas.add(caracteristicaConRta1);
            caracteristicasConRtas.add(caracteristicaConRta2);


            List<Foto> fotos = new ArrayList<>();
            Foto foto = new Foto("img/perro1.jpg");
            Foto foto12 = new Foto("img/perro2.jpg");
            Foto foto13 = new Foto("img/perro3.jpg");
            fotos.add(foto);
            fotos.add(foto13);
            fotos.add(foto12);

            List<Foto> fotos2 = new ArrayList<>();
            Foto foto2 = new Foto("img/perro2.jpg");
            fotos2.add(foto2);

            List<Foto> fotos3 = new ArrayList<>();
            Foto foto3 = new Foto("img/gato.jpg");
            fotos3.add(foto3);

            List<Foto> fotos4 = new ArrayList<>();
            Foto foto4 = new Foto("img/perro4.jpg");
            fotos4.add(foto4);

            List<Foto> fotos5 = new ArrayList<>();
            Foto foto5 = new Foto("img/gato2.jpg");
            fotos5.add(foto5);

            Mascota susana = new Mascota(duenio);
            susana.setNombre("Susana");
            susana.setEspecie("Perro");
            susana.setEdad(1);
            susana.setGenero("Hembra");
            susana.setFotos(fotos);
            susana.setDescripcion("Es una perra tranquila y cariñosa.");

            Mascota rafa = new Mascota(duenio);
            rafa.setNombre("Rafa");
            rafa.setEspecie("Perro");
            rafa.setGenero("Macho");
            rafa.setEdad(2);
            rafa.setFotos(fotos2);
            rafa.setDescripcion("Es una perro acostumbrado a vivir con otros perros");

            Mascota agata = new Mascota(duenio);
            agata.setNombre("Agata");
            agata.setEspecie("Gato");
            agata.setGenero("Hembra");
            agata.setEdad(3);
            agata.setFotos(fotos3);
            agata.setDescripcion("orem ipsum dolor sit amet, consectetur adipiscing elit. ");

            Mascota toby = new Mascota(duenio);
            toby.setNombre("Toby");
            toby.setEspecie("Perro");
            toby.setGenero("Macho");
            toby.setEdad(4);
            toby.setFotos(fotos4);
            toby.setDescripcion("Lorem ipsum dolor sit amet, consectetur adipiscing elit.");

            Mascota michi = new Mascota(duenio);
            michi.setNombre("Michi");
            michi.setEdad(5);
            michi.setEspecie("Gato");
            michi.setGenero("Macho");
            michi.setFotos(fotos5);
            michi.setDescripcion("Lorem ipsum dolor sit amet, consectetur adipiscing elit.");

            EntityManagerHelper.beginTransaction();
            EntityManagerHelper.getEntityManager().persist(duenio.getMascotas().get(0));
            EntityManagerHelper.getEntityManager().persist(duenio.getMascotas().get(1));
            EntityManagerHelper.commit();
            //addAll(susana, rafa, agata, toby, michi);
        }
        return (List<Persistente>)(List<?>) mascotas;
    }

    protected static void addAll(Mascota... mascotas){
        Collections.addAll(DataMascota.mascotas, mascotas);
    }

}
