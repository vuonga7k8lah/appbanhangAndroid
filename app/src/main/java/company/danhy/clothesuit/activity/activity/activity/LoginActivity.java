package company.danhy.clothesuit.activity.activity.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import company.danhy.clothesuit.R;
import company.danhy.clothesuit.activity.activity.model.Account;

public class LoginActivity extends AppCompatActivity {

    public static final String TAG = LoginActivity.class.getSimpleName();
    private EditText edtUserName;
    private EditText edtPassWord;
    private Button btnLogin;
    private Button btnRegister;
    private ProgressDialog pDialog;
    UserLocalStore userLocalStore;
    /**
     * URL : URL_LOGIN
     * param : KEY_USERNAME KEY_PASSWORD
     */
    public static final String KEY_USERNAME = "Email";
    public static final String KEY_PASSWORD = "Password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userLocalStore  = new UserLocalStore(this);
        addControl();
        addEvent();
    }

    private void addEvent() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get value input
                String username = edtUserName.getText().toString().trim();
                String password = edtPassWord.getText().toString().trim();
                // Call method
                loginAccount(username, password);
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void addControl() {
        edtUserName =  findViewById(R.id.txtEmail);
        edtPassWord =  findViewById(R.id.txtPass);
        btnLogin =  findViewById(R.id.btnDangnhap);
        btnRegister =  findViewById(R.id.btnDangki);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Đang đăng nhập...");
        pDialog.setCanceledOnTouchOutside(false);
    }

    /**
     * Method login
     *
     * @param username
     * @param password result json
     */
    public void loginAccount(final String username, final String password) {

        if (checkEditText(edtUserName) && checkEditText(edtPassWord)) {
            pDialog.show();
            RequestQueue queue = Volley.newRequestQueue(this);
            StringRequest requestLogin = new StringRequest(Request.Method.POST, "http://192.168.1.12:9021/api-login",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("res", response);
                            String message = "";
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getString("status").equals("success")) {
                                    Account account = new Account();
                                    JSONObject data = jsonObject.getJSONObject("data");
                                    account.setToken(data.getString("token"));
//                                    account.setUserName(jsonObject.getString("user_name"));
//                                    account.setEmail(jsonObject.getString("email"));
//                                    message = jsonObject.getString("msg");
                                    Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                                    userLocalStore.setUserLoggedIn(true);
                                    //End
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    intent.putExtra("login", "");
                                    startActivity(intent);
                                } else {
                                    message = jsonObject.getString("msg");
                                    Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            pDialog.dismiss();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
//                            VolleyLog.d(TAG, "Error: " + error.getMessage());
                            pDialog.dismiss();
                            if (error instanceof TimeoutError ) {
                                Toast.makeText(LoginActivity.this, "timeout", Toast.LENGTH_SHORT).show();
                            } else if (error instanceof AuthFailureError) {
                                Toast.makeText(LoginActivity.this, "AuthFail", Toast.LENGTH_SHORT).show();
                            } else if (error instanceof NoConnectionError) {
                                Toast.makeText(LoginActivity.this, "NoConnectionError", Toast.LENGTH_SHORT).show();
                            }
                            else if (error instanceof ServerError) {
                                Toast.makeText(LoginActivity.this, "server", Toast.LENGTH_SHORT).show();
                            } else if (error instanceof NetworkError) {
                                Toast.makeText(LoginActivity.this, "network", Toast.LENGTH_SHORT).show();
                            } else if (error instanceof ParseError) {
                                Toast.makeText(LoginActivity.this, "parse", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }) {
                /**
                 * set paramater
                 * */
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put(KEY_USERNAME, username);
                    params.put(KEY_PASSWORD, password);
                    return params;
                }
            };
            queue.add(requestLogin);

        }
    }

    /**
     * Check input
     */
    private boolean checkEditText(EditText editText) {
        if (editText.getText().toString().trim().length() > 0)
            return true;
        else {
            editText.setError("Vui lòng nhập dữ liệu!");
        }
        return false;
    }
}

