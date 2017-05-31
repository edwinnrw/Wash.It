package com.project.edn.washit.api;


import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by EDN on 5/30/2017.
 */

public interface ApiService {
//    @GET("/apiV3/pengeluaran/get_history")
//    Call<List<PengeluaranModel>>getHistory();
//
    @FormUrlEncoded
    @POST("Login.php")
    Call<ResponseBody> login(@Field("email") String email, @Field("password") String password,@Field("token")String token);

    @FormUrlEncoded
    @POST("logout.php")
    Call<ResponseBody>logout(@Field("token")String token);

    @FormUrlEncoded
    @POST("get_laundry_list.php")
    Call<ResponseBody>getListLaundry(@Field("type")String type);

    @FormUrlEncoded
    @POST("near_me.php")
    Call<ResponseBody>getNearLaundry(@Field("lat")String lat,@Field("long")String longitude);

    @FormUrlEncoded
    @POST("process_order.php")
    Call<ResponseBody>order(@FieldMap Map<String, String> fields);

    @FormUrlEncoded
    @POST("Register.php")
    Call<ResponseBody>register(@FieldMap Map<String, String> fields);

    @FormUrlEncoded
    @POST("edit_profile.php")
    Call<ResponseBody>editProfile(@FieldMap Map<String, String> fields);

    @FormUrlEncoded
    @POST("change_password.php")
    Call<ResponseBody>changePassword(@FieldMap Map<String, String> fields);

    @FormUrlEncoded
    @POST("getHistory.php")
    Call<ResponseBody>getHistory(@FieldMap Map<String, String> fields);

}
