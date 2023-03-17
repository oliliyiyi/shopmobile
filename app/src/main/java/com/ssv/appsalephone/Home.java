package com.ssv.appsalephone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.aurelhubert.ahbottomnavigation.notification.AHNotification;
import com.ssv.appsalephone.Class.Order;
import com.ssv.appsalephone.Class.Product;
import com.ssv.appsalephone.Fragment.CartFragment;
import com.ssv.appsalephone.Fragment.DetailProductFragment;
import com.ssv.appsalephone.Fragment.HistoryFragment;
import com.ssv.appsalephone.Fragment.OrderInfoFragment;
import com.ssv.appsalephone.Fragment.ProductFragment;
import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity {

    // region Variable

    private List<Product> listCartProduct;

    // Đếm số sản phẩm trong giỏ hàng
    private int countProduct;

    private AHBottomNavigation ahBotNavHome;
    private FragmentTransaction fragmentTransaction;

    // endregion Variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Khởi tạo các item
        initItem();

        // Set data cho ahBotNavHome
        setDataBotNavHome();
    }

    // region  Private Menthod

    // Khởi tạo các item
    private void initItem() {
        ahBotNavHome = findViewById(R.id.ahbotnav_home);
        if(listCartProduct == null){
            listCartProduct = new ArrayList<>();
        }
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.contet_frame, new ProductFragment());

        fragmentTransaction.commit();
    }

    // Set data cho BotNavHome
    private void setDataBotNavHome() {

        // Create items
        AHBottomNavigationItem item1 = new AHBottomNavigationItem(R.string.tab_product, R.drawable.ic_baseline_home_24, R.color.teal_200);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(R.string.tab_cart, R.drawable.ic_baseline_add_shopping_cart_24, R.color.gray);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem(R.string.tab_history, R.drawable.ic_baseline_history_24, R.color.yellow);

        // Add items
        ahBotNavHome.addItem(item1);
        ahBotNavHome.addItem(item2);
        ahBotNavHome.addItem(item3);

        ahBotNavHome.setColored(false);

        // Set màu nav
        ahBotNavHome.setDefaultBackgroundColor(getResources().getColor(R.color.white));

        // Khi click vào các icon trên nav
        ahBotNavHome.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {

                switch (position){
                    case 0:
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.contet_frame, new ProductFragment());
                        fragmentTransaction.commit();
                        break;

                    case 1:
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.contet_frame, new CartFragment(listCartProduct));
                        fragmentTransaction.commit();
                        break;
                    case 2:
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.contet_frame, new HistoryFragment());
                        fragmentTransaction.commit();
                        break;
                }

                return true;
            }
        });
    }

    // endregion Private Menthod

    // region Public Menthod

    // Set số lượng các sản phẩm trong giỏ hàng
    public void setCountProductInCart(int count){
        countProduct = count;
        AHNotification notification = new AHNotification.Builder()
                .setText(String.valueOf(count))
                .setBackgroundColor(ContextCompat.getColor(Home.this, R.color.red))
                .setTextColor(ContextCompat.getColor(Home.this, R.color.white))
                .build();
        ahBotNavHome.setNotification(notification, 1);
    }

    // Mở Fragment DetailProduct
    public void toDetailProductFragment(Product product){
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.contet_frame, new DetailProductFragment(product,listCartProduct));
        fragmentTransaction.commit();
    }

    // Mở Fragment OrderInfo
    public void toOrderInfoFragment(Order orderInfo){
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.contet_frame, new OrderInfoFragment(orderInfo));
        fragmentTransaction.addToBackStack(OrderInfoFragment.TAG);
        fragmentTransaction.commit();
    }

    // Thêm sản phẩm đã chọn vào giỏ hàng
    public void addToListCartProdct(Product product){
        listCartProduct.add(product);
    }

    // Lấy ra các sản phẩm đã thêm vào giỏ hàng
    public List<Product> getListCartProduct() {
        return listCartProduct;
    }

    // Lấy ra số lượng các sản phẩm đã thêm vào giỏ hàng
    public int getCountProduct() {
        return countProduct;
    }

    // Set lại số lượng của sản phẩm khi mua nhiều
    public void setCountForProduct(int possion, int countProduct){
        listCartProduct.get(possion).setNumProduct(countProduct);
    }

    // endregion Public Menthod
}