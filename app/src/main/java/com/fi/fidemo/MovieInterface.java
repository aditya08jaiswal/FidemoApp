package com.fi.fidemo;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface MovieInterface {

    @GET("v2/list")
    Call<String> STRING_CALL(
            @Query("page") int page,
            @Query("limit") int limit
    );
}
