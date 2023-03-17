package com.ssv.appsalephone.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ssv.appsalephone.Adapter.ProductAdapter;
import com.ssv.appsalephone.Adapter.ProductSearchAdapter;
import com.ssv.appsalephone.Adapter.SlidePhotoAdapter;
import com.ssv.appsalephone.Class.Product;
import com.ssv.appsalephone.Class.ProductResponse;
import com.ssv.appsalephone.Class.SlidePhoto;
import com.ssv.appsalephone.Home;
import com.ssv.appsalephone.R;
import com.ssv.appsalephone.api.ApiService;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProductFragment extends Fragment {

    // region Variable

    private Home home;
    private Timer mTimer;
    private List<SlidePhoto> listSlidePhoto;
    private List<Product> listAllProduct;

    private View mView;
    private RecyclerView rcvProduct;
    private ViewPager viewPagerSlidePhoto;
    private CircleIndicator circleIndicator;
    private AutoCompleteTextView atcProductSearch;

    private ProductAdapter productAdapter;
    private SlidePhotoAdapter slidePhotoAdapter;

    // endregion Variable

    public ProductFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_product, container, false);

        // Khởi tạo các item
        initItem();

        // Set Adapter cho viewPagerSlidePhoto
        setDataSlidePhotoAdapter();
        // Set Adapter cho rcvProduct
        setDataProductAdapter();
        getDataProduct();
        return mView;
    }

    // region Private menthod

    // Khởi tạo các item
    private void initItem(){
        rcvProduct = mView.findViewById(R.id.rcv_product);
        viewPagerSlidePhoto = mView.findViewById(R.id.vp_slide_photo);
        circleIndicator = mView.findViewById(R.id.circle_indicator);
        atcProductSearch = mView.findViewById(R.id.atc_product_search);

        listSlidePhoto = getListSlidePhoto();
//        listAllProduct = getDataProduct();

        home = (Home) getActivity();
    }

    // Set Adapter cho viewPagerSlidePhoto
    private void setDataSlidePhotoAdapter(){
        slidePhotoAdapter = new SlidePhotoAdapter(listSlidePhoto, this);
        viewPagerSlidePhoto.setAdapter(slidePhotoAdapter);
        circleIndicator.setViewPager(viewPagerSlidePhoto);
        slidePhotoAdapter.registerDataSetObserver(circleIndicator.getDataSetObserver());

        // Auto chuyển các slide photo
        autoSildeImage();
    }

    // Auto chuyển các slide photo
    private void autoSildeImage(){
        if(listSlidePhoto == null || listSlidePhoto.isEmpty() || viewPagerSlidePhoto == null){
            return;
        }
        if (mTimer == null){
            mTimer = new Timer();
        }
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        int currentItem = viewPagerSlidePhoto.getCurrentItem();
                        int totalItem = listSlidePhoto.size() - 1;

                        // Nếu item hiện tại chưa phải cuối cùng
                        if(currentItem < totalItem){
                            currentItem++;
                            viewPagerSlidePhoto.setCurrentItem(currentItem);
                        }else {
                            viewPagerSlidePhoto.setCurrentItem(0);
                        }
                    }
                });
            }

            // xử lý thêm để set time
        },500,3000 );
    }

    // Set Adapter cho rcvProduct
    private void setDataProductAdapter(){

        GridLayoutManager gridLayoutManager = new GridLayoutManager(home, 2);
        rcvProduct.setLayoutManager(gridLayoutManager);

        productAdapter = new ProductAdapter();
        productAdapter.setData(listAllProduct,home);

        rcvProduct.setAdapter(productAdapter);
    }

    // Set Adapter cho atcProductSearch
    private void setProductSearchAdapter(List<Product> listProduct ){
        ProductSearchAdapter productSearchAdapter = new ProductSearchAdapter(home,R.layout.item_search, listProduct);
        atcProductSearch.setAdapter(productSearchAdapter);

        // Sau khi chọn item search sẽ chuyển sang fragment detail
        atcProductSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                home.toDetailProductFragment(listProduct.get(position));
            }
        });
    }

    // Lấy Product để vào slide
    private List<SlidePhoto> getListSlidePhoto(){
        List<SlidePhoto> listSlidePhoto = new ArrayList<>();
        listSlidePhoto.add(new SlidePhoto(R.drawable.slide1));
        listSlidePhoto.add(new SlidePhoto(R.drawable.slide2));
        listSlidePhoto.add(new SlidePhoto(R.drawable.slide3));
        listSlidePhoto.add(new SlidePhoto(R.drawable.slide4));
        listSlidePhoto.add(new SlidePhoto(R.drawable.slide5));
        return listSlidePhoto;
    }

    private void getDataProduct(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://demo7879288.mockable.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiService apiService = retrofit.create(ApiService.class);

        Call<List<Product>> call = apiService.getProducts();

        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful()) {
                    List<Product> productList = response.body();
                    productAdapter.setData(productList, home);
                } else {
                    Toast.makeText(getActivity(), "Failed to retrieve data from API", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Toast.makeText(getActivity(), "Failed to retrieve data from API", Toast.LENGTH_SHORT).show();
                Log.d("MYTAG", "onFailure: " + t.getMessage());
            }
        });
    }
}