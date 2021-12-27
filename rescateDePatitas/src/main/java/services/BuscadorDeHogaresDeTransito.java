package services;


import domain.models.entities.hogares.Hogar;
import domain.models.entities.hogares.ListadoDeHogares;
import org.junit.Assert;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.List;

public class BuscadorDeHogaresDeTransito {
    private static BuscadorDeHogaresDeTransito instancia = null;
    private final String urlAPIHogares = "https://api.refugiosdds.com.ar/api/";
    private Retrofit retrofit;

    private BuscadorDeHogaresDeTransito() {
        this.retrofit = new Retrofit.Builder()
                .baseUrl(urlAPIHogares)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }


    public static BuscadorDeHogaresDeTransito getInstancia() {
        if (instancia == null) {
            instancia = new BuscadorDeHogaresDeTransito();
        }
        return instancia;
    }

    public ListadoDeHogares listadoDeHogares(int offset) throws IOException {
        HomeSearcher homeSearcher = this.retrofit.create(HomeSearcher.class);
        Call<ListadoDeHogares> requestHogares = homeSearcher.hogares(offset);
        Response<ListadoDeHogares> responseHogares = requestHogares.execute();
        return responseHogares.body();
    }
}
