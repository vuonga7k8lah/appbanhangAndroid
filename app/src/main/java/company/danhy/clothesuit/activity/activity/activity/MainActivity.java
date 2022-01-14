package company.danhy.clothesuit.activity.activity.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;


import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import company.danhy.clothesuit.R;
import company.danhy.clothesuit.activity.activity.model.Account;
import company.danhy.clothesuit.activity.activity.ultil.checkconnect;
import company.danhy.clothesuit.activity.activity.adapter.LoaispAdapter;
import company.danhy.clothesuit.activity.activity.adapter.SanPhamFlashSaleAdapter;
import company.danhy.clothesuit.activity.activity.adapter.SanphamAdapter;
import company.danhy.clothesuit.activity.activity.model.Loaisp;
import company.danhy.clothesuit.activity.activity.ultil.Server;
import company.danhy.clothesuit.activity.activity.model.Giohang;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    ViewFlipper viewFlipper;
    RecyclerView recyclerViewflashSaleItems, recyclerViewNewItems;
    NavigationView navigationView;
    ListView listViewManHinhChinh;
    ImageView imageViewHotItems;
    DrawerLayout drawerLayout;
    ArrayList<Loaisp> mangloaisanpham;
    LoaispAdapter loaispAdapter;
    int ID = 0;
    String tenloaisanpham = "";
    String hinhanhloaisanpham = "";

    ArrayList<Sanpham> mangsanpham;
    ArrayList<Sanpham> mangSanPhamFlashSale;
    SanphamAdapter sanphamAdapter;

    UserLocalStore userLocalStore;

    SanPhamFlashSaleAdapter sanPhamFlashSaleAdapter;


    public static ArrayList<Giohang> manggiohang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userLocalStore = new UserLocalStore(this);

        AnhXa();

        if (checkconnect.isNetworkAvailable(getApplicationContext())) {
            ActionBar();
            ActionViewFlipper();
            getDuLieuLoaiSanPham();
            getDuLieuSPMoiNhat();
            getDuLieuSPFlashSale();
            catOnItemListView();
        } else {
            checkconnect.ShowToast_Short(getApplicationContext(), "Bạn kiểm tra lại kết nối ");
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menuu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        User user = userLocalStore.getLoggedInUser();
        switch (item.getItemId()) {
            case R.id.menugiohang:
                Intent intent = new Intent(getApplicationContext(), company.danhy.clothesuit.activity.activity.activity.Giohang.class);
                startActivity(intent);
                break;
            case R.id.menuLogout:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Logout");
                builder.setMessage("Username : " + user.username + "\nEmail :" + user.email);
                builder.setCancelable(false);
                builder.setPositiveButton("Trở về", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(MainActivity.this, "Mời bạn tiếp tục mua sắm", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("Logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Code Logout
                        userLocalStore.clearUserData();
                        userLocalStore.setUserLoggedIn(false);
                        Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(loginIntent);
                        Toast.makeText(MainActivity.this, "Đăng xuất thành công", Toast.LENGTH_SHORT).show();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (authenticate() == true) {
            displayUserDetails();
//            userLocalStore.clearUserData();
//            userLocalStore.setUserLoggedIn(false);
//            Intent loginIntent = new Intent(this, LoginActivity.class);
//            startActivity(loginIntent);
        }
    }

    private boolean authenticate() {
        if (userLocalStore.getLoggedInUser() == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            return false;
        }
        return true;
    }

    private void displayUserDetails() {
        User user = userLocalStore.getLoggedInUser();
        Toast.makeText(getApplicationContext(),
                user.username,
                Toast.LENGTH_SHORT).show();

    }


    private void catOnItemListView() {
        listViewManHinhChinh.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        if (checkconnect.isNetworkAvailable(getApplicationContext())) {
                            Intent intent = new Intent(MainActivity.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            checkconnect.ShowToast_Short(getApplicationContext(), "Bạn kiểm tra lại kết nối!");
                        }
                        drawerLayout.closeDrawer(Gravity.START);
                        break;
                    case 1:
                        if (checkconnect.isNetworkAvailable(getApplicationContext())) {
                            Intent intent = new Intent(MainActivity.this, GiayActivity.class);
                            intent.putExtra("idLoaiSanPham", mangloaisanpham.get(position).getId());
                            startActivity(intent);
                        } else {
                            checkconnect.ShowToast_Short(getApplicationContext(), "Bạn kiểm tra lại kết nối!");
                        }
                        drawerLayout.closeDrawer(Gravity.START);
                        break;
                    case 2:
                        if (checkconnect.isNetworkAvailable(getApplicationContext())) {
                            Intent intent = new Intent(MainActivity.this, NonActivity.class);
                            intent.putExtra("idLoaiSanPham", mangloaisanpham.get(position).getId());
                            startActivity(intent);
                        } else {
                            checkconnect.ShowToast_Short(getApplicationContext(), "Bạn kiểm tra lại kết nối!");
                        }
                        drawerLayout.closeDrawer(Gravity.START);
                        break;
                    case 3:
                        if (checkconnect.isNetworkAvailable(getApplicationContext())) {
                            Intent intent = new Intent(MainActivity.this, AoActivity.class);
                            intent.putExtra("idLoaiSanPham", mangloaisanpham.get(position).getId());
                            startActivity(intent);
                        } else {
                            checkconnect.ShowToast_Short(getApplicationContext(), "Bạn kiểm tra lại kết nối!");
                        }
                        drawerLayout.closeDrawer(Gravity.START);
                        break;
                    case 4:
                        if (checkconnect.isNetworkAvailable(getApplicationContext())) {
                            Intent intent = new Intent(MainActivity.this, QuanActivity.class);
                            intent.putExtra("idLoaiSanPham", mangloaisanpham.get(position).getId());
                            startActivity(intent);
                        } else {
                            checkconnect.ShowToast_Short(getApplicationContext(), "Bạn kiểm tra lại kết nối!");
                        }
                        drawerLayout.closeDrawer(Gravity.START);
                        break;
                    case 5:
                        if (checkconnect.isNetworkAvailable(getApplicationContext())) {
                            Intent intent = new Intent(MainActivity.this, DepActivity.class);
                            intent.putExtra("idLoaiSanPham", mangloaisanpham.get(position).getId());
                            startActivity(intent);
                        } else {
                            checkconnect.ShowToast_Short(getApplicationContext(), "Bạn kiểm tra lại kết nối!");
                        }
                        drawerLayout.closeDrawer(Gravity.START);
                        break;
                    case 6:
                        if (checkconnect.isNetworkAvailable(getApplicationContext())) {
                            Intent intent = new Intent(MainActivity.this, ThongTinActivity.class);
                            startActivity(intent);
                        } else {
                            checkconnect.ShowToast_Short(getApplicationContext(), "Bạn kiểm tra lại kết nối!");
                        }
                        drawerLayout.closeDrawer(Gravity.START);
                        break;
                    case 7:
                        if (checkconnect.isNetworkAvailable(getApplicationContext())) {
                            Intent intent = new Intent(MainActivity.this, ThongTinActivity.class);
                            startActivity(intent);
                        } else {
                            checkconnect.ShowToast_Short(getApplicationContext(), "Bạn kiểm tra lại kết nối!");
                        }
                        drawerLayout.closeDrawer(Gravity.START);
                        break;

                }
            }
        });
    }

    private void getDuLieuSPFlashSale() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest jsonArrayRequest = new StringRequest(Request.Method.GET, "http://192.168.50.136:9021/api-products",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("res 0", response);
                        String message = "";
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("status").equals("success")) {
                                JSONArray data = jsonObject.getJSONArray("data");
                                Log.d("a data", data.toString());
                                if (response != null) {
                                    int ID = 0;
                                    String Tensanpham = "";
                                    Integer Giasanpham = 0;
                                    String Hinhanhsanpham = "";
                                    String Motasanpham = "";
                                    int IDsanpham = 0;
                                    for (int i = 0; i < data.length(); i++) {
                                        try {
                                            JSONObject aData = data.getJSONObject(i);
                                            ID = aData.getInt("id");
                                            Tensanpham = aData.getString("tenSanPham");
                                            Giasanpham = aData.getInt("giaSanPham");
                                            Hinhanhsanpham = aData.getString("hinhAnhSanPham");
                                            Motasanpham = aData.getString("moTaSanPham");
                                            IDsanpham = aData.getInt("idLoaiSanPham");
                                            mangSanPhamFlashSale.add(new Sanpham(ID, Tensanpham, Giasanpham, Hinhanhsanpham, Motasanpham, IDsanpham));
                                            sanPhamFlashSaleAdapter.notifyDataSetChanged();
                                            mangsanpham.add(new Sanpham(ID, Tensanpham, Giasanpham, Hinhanhsanpham, Motasanpham, IDsanpham));
                                            sanphamAdapter.notifyDataSetChanged();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    Log.d("mangsp", mangSanPhamFlashSale.toString());
                                    Log.d("mangloaisp", mangloaisanpham.toString());
                                }
                            } else {
                                // Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        requestQueue.add(jsonArrayRequest);
    }


    private void getDuLieuSPMoiNhat() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest jsonArrayRequest = new StringRequest(Request.Method.GET, "http://192.168.50.136:9021/api-products",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("res 1", response);
                        String message = "";
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("status").equals("success")) {
                                JSONArray data = jsonObject.getJSONArray("data");
                                System.out.println(data.toString());
                                if (response != null) {
                                    int ID = 0;
                                    String Tensanpham = "";
                                    Integer Giasanpham = 0;
                                    String Hinhanhsanpham = "";
                                    String Motasanpham = "";
                                    int IDsanpham = 0;
                                    for (int i = 0; i < data.length(); i++) {
                                        try {
                                            JSONObject aData = data.getJSONObject(i);
                                            JSONArray array = aData.getJSONArray("data");
                                            ID = aData.getInt("id");
                                            Tensanpham = aData.getString("tenSanPham");
                                            Giasanpham = aData.getInt("giaSanPham");
                                            Hinhanhsanpham = aData.getString("hinhAnhSanPham");
                                            Motasanpham = aData.getString("moTaSanPham");
                                            IDsanpham = aData.getInt("idLoaiSanPham");
                                            mangSanPhamFlashSale.add(new Sanpham(ID, Tensanpham, Giasanpham, Hinhanhsanpham, Motasanpham, IDsanpham));
                                            sanPhamFlashSaleAdapter.notifyDataSetChanged();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            } else {
                                // Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        requestQueue.add(jsonArrayRequest);
    }

    private void getDuLieuLoaiSanPham() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest jsonArrayRequest = new StringRequest(Request.Method.GET, "http://192.168.50.136:9021/api-products",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("res Loai Sp", response);
                        String message = "";
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("status").equals("success")) {
                                JSONArray data = jsonObject.getJSONArray("data");
                                Log.d("data", data.toString());
                                for (int i = 0; i < data.length(); i++) {
                                    try {
                                        JSONObject aData = data.getJSONObject(i);
                                        Log.d("data", aData.toString());
                                        mangloaisanpham.add(new Loaisp(
                                                        aData.getInt("id"),
                                                        aData.getString("tenSanPham"),
                                                        aData.getInt("giaSanPham"),
                                                        aData.getString("hinhAnhSanPham"),
                                                        aData.getString("moTaSanPham"),
                                                        aData.getInt("idLoaiSanPham")
                                                )
                                        );
                                        Log.d("data", mangloaisanpham.toString());
                                        loaispAdapter.notifyDataSetChanged();

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            } else {
                                // Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        requestQueue.add(jsonArrayRequest);
    }

    private ArrayList<String> mangQuangCao() {
        ArrayList<String> mangQuangCao = new ArrayList<>();
        mangQuangCao.add(getString(R.string.anhQuangCao1));
        mangQuangCao.add(getString(R.string.anhQuangCao2));
        mangQuangCao.add(getString(R.string.anhQuangCao3));
        mangQuangCao.add(getString(R.string.anhQuangCao4));
        mangQuangCao.add(getString(R.string.anhQuangCao5));
        return mangQuangCao;
    }

    private void SetAnimationForViewFlipper(ViewFlipper viewFlipper) {
        Animation slide_in_right = AnimationUtils.loadAnimation(this, R.anim.slide_in_right);
        Animation slide_out_right = AnimationUtils.loadAnimation(this, R.anim.slide_out_right);
        viewFlipper.setOutAnimation(slide_out_right);
        viewFlipper.setInAnimation(slide_in_right);
    }


    private float oldX, newX;
    private boolean checkTouch = false;

    @SuppressLint("ClickableViewAccessibility")
    private void TouchMoveForViewFlipper(final ViewFlipper viewFlipper) {
        viewFlipper.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    oldX = event.getX();
                    Log.d("touch", "down");
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    newX = event.getX();
                    Log.d("touch", "up");
                    checkTouch = true;
                }
                if (checkTouch == true) {
                    if (oldX < newX) {
                        if (viewFlipper.isAutoStart()) {
                            viewFlipper.stopFlipping();
                            viewFlipper.showPrevious();
                            viewFlipper.startFlipping();
                            viewFlipper.setAutoStart(true);

                        }

                    } else {
                        viewFlipper.stopFlipping();
                        viewFlipper.showNext();
                        viewFlipper.startFlipping();
                        viewFlipper.setAutoStart(true);
                    }
                    checkTouch = false;
                    oldX = 0;
                    newX = 0;
                }
                return true;
            }

        });

    }

    private void ActionViewFlipper() {
        for (int i = 0; i < this.mangQuangCao().size(); i++) {
            ImageView imageView = new ImageView(this);
            Picasso.get().load(this.mangQuangCao().get(i)).into(imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            viewFlipper.addView(imageView);
        }
        viewFlipper.setFlipInterval(7000);
        viewFlipper.setAutoStart(true);
        this.SetAnimationForViewFlipper(viewFlipper);
        this.TouchMoveForViewFlipper(viewFlipper);

    }

    private void ActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_sort_by_size);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    void AnhXa() {
        toolbar = findViewById(R.id.toolbar);
        viewFlipper = findViewById(R.id.viewFlipper);
        recyclerViewflashSaleItems = findViewById(R.id.recyclerViewHotItems);
        recyclerViewNewItems = findViewById(R.id.recyclerViewNewItems);
        navigationView = findViewById(R.id.navigationView);
        listViewManHinhChinh = findViewById(R.id.listViewManHinhChinh);

        drawerLayout = findViewById(R.id.drawerLayout);

        mangloaisanpham = new ArrayList<>();
        loaispAdapter = new LoaispAdapter(mangloaisanpham, getApplicationContext());
        listViewManHinhChinh.setAdapter(loaispAdapter);

        mangsanpham = new ArrayList<>();
        mangSanPhamFlashSale = new ArrayList<>();
        sanphamAdapter = new SanphamAdapter(getApplicationContext(), mangsanpham);
        sanPhamFlashSaleAdapter = new SanPhamFlashSaleAdapter(getApplicationContext(), mangSanPhamFlashSale);

        recyclerViewflashSaleItems.setHasFixedSize(true);
        recyclerViewflashSaleItems.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewflashSaleItems.setAdapter(sanPhamFlashSaleAdapter);

        recyclerViewNewItems.setHasFixedSize(true);
        recyclerViewNewItems.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        recyclerViewNewItems.setAdapter(sanphamAdapter);
        if (manggiohang != null) {

        } else {
            manggiohang = new ArrayList<>();
        }

        imageViewHotItems = findViewById(R.id.imageViewHotItems);
        imageViewHotItems.setImageResource(R.drawable.flashsale);
        imageViewHotItems.setScaleType(ImageView.ScaleType.FIT_START);

    }
}
