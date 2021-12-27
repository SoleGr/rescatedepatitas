package services;

import domain.models.entities.mascotas.Lugar;

public class ComparadorDistancias {

    public double comparar(Lugar ubicacionOrg, Lugar ptoEncuentro) {

        return distancia(ubicacionOrg.getLatitud(),ubicacionOrg.getLongitud(),ptoEncuentro.getLatitud(),ptoEncuentro.getLongitud());
    }

    public double distancia(Double lat1, Double lng1, Double lat2, Double lng2) {
        //Ejemplo Grados decimales (DD): lat 41.40338, lon 2.17403
        double radioTierra = 6371;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);
        double va1 = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));
        double va2 = 2 * Math.atan2(Math.sqrt(va1), Math.sqrt(1 - va1));

        return radioTierra * va2;
    }


}
