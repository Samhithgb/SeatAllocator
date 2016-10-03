package com.samhith.attendance;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RequestInterface {


    @FormUrlEncoded
    @POST("seatinfo.php")
    Call<JSONResponse> getJSON(@Field("username") String your_token);
}
