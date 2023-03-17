package com.ssv.appsalephone.api;

import com.ssv.appsalephone.Class.Product;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("product")
    Call<List<Product>> getProducts();
}
