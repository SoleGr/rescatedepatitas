package services;

import domain.models.entities.hogares.*;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface HomeSearcher {

    @Headers({
            "accept: application/json",
            "Authorization: Bearer Zqn1qqFjvPYpClPm5byTrCUa58PXiHa6nKViOHcb8Gv1Ag1HBPCpAXXRPpB9"
    })
    @GET("hogares")
    Call<ListadoDeHogares> hogares(@Query("offset") int offset);
}
