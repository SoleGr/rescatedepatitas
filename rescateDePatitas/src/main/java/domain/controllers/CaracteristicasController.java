package domain.controllers;

import domain.controllers.exceptions.CaracteristicaSinDescripcionException;
import domain.models.entities.publicaciones.Pregunta;
import domain.models.repositories.RepositorioDeCaracteristicas;

import java.util.List;

public class CaracteristicasController {
    private static CaracteristicasController instancia;
    private RepositorioDeCaracteristicas repositorio;


    public static CaracteristicasController getInstancia() {
        if (instancia == null) {
            instancia = new CaracteristicasController();
        }
        return instancia;
    }

    private CaracteristicasController() {
        this.repositorio = new RepositorioDeCaracteristicas();
    }

    public void crearCaracteristica(String descripcion, List<String> respuestasPosibles) {
        this.validarDatos(descripcion, respuestasPosibles);
        Pregunta caracteristica = new Pregunta();
        caracteristica.setPregunta(descripcion);
        caracteristica.setRespuestas(respuestasPosibles);
        this.repositorio.agregar(caracteristica);
    }

    public void modificar(String descripcion, Pregunta caracteristica) {
//        this.validarDatos(caracteristicaDTO);
//        Optional<Caracteristica> caracteristicaAmodificar = this.repositorio.buscar(descripcion);
//        if (!caracteristicaAmodificar.isPresent()){
//            throw new CaracteristicaNoEncontradaException("La caracteristica" + descripcion + "no existe");
//        }
//        this.asignarParametrosA(caracteristicaAmodificar.get(),caracteristicaDTO);
//        this.repositorio.modificar(caracteristicaAmodificar.get());
    }

    public void eliminar(String descripcion) {
//        Optional<Caracteristica> caracteristicaAEliminar = this.repositorio.buscar(descripcion);
//        if (!caracteristicaAEliminar.isPresent()){
//            throw new CaracteristicaNoEncontradaException("La caracteristica" + descripcion + "no existe");
//        }
//
//        this.repositorio.eliminar(caracteristicaAEliminar.get());
    }

    private void validarDatos(String descripcion, List<String> respuestasPosibles) {
        if (descripcion == null) {
            throw new CaracteristicaSinDescripcionException("La caracteristica no tiene descripcion");
        }
        if (respuestasPosibles == null) {
            throw new CaracteristicaSinDescripcionException("La caracteristica no tiene respuestas posibles");
        }
    }

    public RepositorioDeCaracteristicas getRepositorio() {
        return repositorio;
    }


}
