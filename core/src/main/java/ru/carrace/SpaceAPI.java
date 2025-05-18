package ru.carrace;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SpaceAPI {
    @GET("spacewar.php")
    Call<List<DataFromDB>> sendQuery(@Query("action") String action);

    @GET("spacewar.php")
    Call<List<DataFromDB>> sendQuery(@Query("action") String action, 
                                   @Query("name") String name,
                                   @Query("score") int score,
                                   @Query("coins") int coins);
}

