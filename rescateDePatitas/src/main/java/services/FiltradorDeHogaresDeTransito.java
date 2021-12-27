package services;

import domain.models.entities.hogares.Hogar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FiltradorDeHogaresDeTransito {

    //Recibe una lista de hogares y un animal(perro o gato) y devuelve una lista de los hogares que admiten a ese animal
    public List<Hogar> filtrarPorAnimalAceptado(List<Hogar> listadoDeHogares, String animal){
        List<Hogar> listadoFiltrado = new ArrayList<>();
        Map<String, Boolean> mapa = new HashMap<>();
        mapa.put("perro", true);
        mapa.put("gato", true);
        for (Hogar hogar:listadoDeHogares) {
            mapa.replace("gato",hogar.admisiones.gatos);
            mapa.replace("perro",hogar.admisiones.perros);
            if(mapa.get( animal )) {listadoFiltrado.add(hogar);}
        }
        return listadoFiltrado;
    }

    //Recibe una lista de hogares y el tamanio de un animal(grande, mediano, pequenio) y devuelve una lista de los hogares que admiten ese animal
    public List<Hogar> filtrarPorTamanio(List<Hogar> listadoDeHogares, String tamanio){
        List<Hogar> listadoFiltrado = new ArrayList<>();
        Map<String, Boolean> mapa = new HashMap<>();
        mapa.put("pequenio", true);
        mapa.put("mediano", true);
        mapa.put("grande", true);
        for (Hogar hogar:listadoDeHogares) {
            mapa.replace("mediano", hogar.patio);
            mapa.replace("grande", hogar.patio);
            if(mapa.get(tamanio)) {listadoFiltrado.add(hogar);}
        }
        return listadoFiltrado;
    }

    //Recibe una lista de hogares y devuelve una lista de los hogares que tienen espacio disponible
    public List<Hogar> filtrarPorCapacidad(List<Hogar> listadoDeHogares){
        List<Hogar> listadoFiltrado = new ArrayList<>();
        for (Hogar hogar:listadoDeHogares) {
            if (hogar.lugares_disponibles > 0) {listadoFiltrado.add(hogar);}
        }
        return listadoFiltrado;
    }

    public List<Hogar> filtrarPorCercania(List<Hogar> listadoDeHogares, int radio, Double latitud, Double longitud){
        List<Hogar> listadoFiltrado = new ArrayList<>();
        ComparadorDistancias comparador = new ComparadorDistancias();

        for (Hogar hogar:listadoDeHogares) {
            double distancia = comparador.distancia(latitud,longitud,hogar.ubicacion.latitud,hogar.ubicacion.longitud);
            if (distancia*10 <= radio) {listadoFiltrado.add(hogar);}
        }
        return listadoFiltrado;
    }


    //Recibe una lista de hogares, un radio, una latitud y una longitud y devuelve una lista de hogares que entran en radio de las coordenadas
//    public List<Hogar> filtrarPorCercania(List<Hogar> listadoDeHogares, int radio, int latitud, int longitud){
//        List<Hogar> listadoFiltrado = new ArrayList<>();
//        float restaLatitud;
//        float restaLongitud;
//        for (Hogar hogar:listadoDeHogares) {
//            restaLatitud = Math.abs(latitud - hogar.direccion.latitud);
//            restaLongitud = Math.abs(longitud - hogar.direccion.longitud);
//            if (Math.hypot(restaLatitud, restaLongitud) <= radio) {listadoFiltrado.add(hogar);}
//        }
//        return listadoFiltrado;
//    }

    //Recibe una lista de hogares y una caracteristica y devuelve una lista de hogares que piden esa caracteristica
    //Se interpreta que los que no piden caracteristicas aceptan sin importar la caracteristica
    public List<Hogar> filtrarPorCaracteristica(List<Hogar> listadoDeHogares, String caracteristica){
        List<Hogar> listadoFiltrado = new ArrayList<>();
        for (Hogar hogar:listadoDeHogares) {
            if (hogar.caracteristicas.isEmpty() || hogar.caracteristicas.contains(caracteristica)) {listadoFiltrado.add(hogar);}
        }
        return listadoFiltrado;
    }

}
