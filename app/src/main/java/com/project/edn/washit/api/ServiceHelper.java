package com.project.edn.washit.api;


import java.io.IOException;
import java.util.List;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by bradhawk on 10/13/2016.
 */

public class ServiceHelper {

    private static final String ENDPOINT = "http://edwinnurwansyah.com/apiwashit/";

//    private static OkHttpClient httpClient = new OkHttpClient();

    private static ServiceHelper instance=null;
    private ApiService service;


    private ServiceHelper() {

        Retrofit retrofit = createAdapter().build();
        service = retrofit.create(ApiService.class);

    }

    public static ServiceHelper getInstance() {
        if (instance==null){
            instance=new ServiceHelper();
        }
        return instance;
    }

    private Retrofit.Builder createAdapter() {
//        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
//        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//        httpClient.interceptors().add(interceptor);
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(
                        new Interceptor() {
                            @Override
                            public Response intercept(Chain chain) throws IOException {
                                Request original = chain.request();

                                // Request customization: add request headers
                                Request.Builder requestBuilder = original.newBuilder()
                                        .method(original.method(), original.body());

                                Request request = requestBuilder.build();
                                return chain.proceed(request);
                            }
                        })
                .build();

        return new Retrofit.Builder()
                .baseUrl(ENDPOINT)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create());
    }
    public  Call<ResponseBody>login(String email, String password,String token){
        return service.login(email,password,token);
    }
    public Call<ResponseBody>logout(String token){
        return service.logout(token);
    }
    public Call<ResponseBody>getListLaundry(String ket){
        return service.getListLaundry(ket);
    }
    public Call<ResponseBody>getNear(String lat, String longit){
        return service.getNearLaundry(lat,longit);
    }
    public Call<ResponseBody>order(Map<String, String> params){
        return service.order(params);
    }
    public  Call<ResponseBody>register(Map<String, String> params){
        return service.register(params);
    }
    public  Call<ResponseBody>editProfile(Map<String, String> params){
        return service.editProfile(params);
    }
    public  Call<ResponseBody>changePassword(Map<String, String> params){
        return service.changePassword(params);
    }
    public  Call<ResponseBody>getHistory(Map<String, String> params){
        return service.getHistory(params);
    }
//    public Call<List<PengeluaranModel>> getAllHistory() {
//        return service.getHistory();
//    }
//    public Call<PengeluaranModel>addData(PengeluaranModel in){
//        return service.addData(in);
//    }

}
