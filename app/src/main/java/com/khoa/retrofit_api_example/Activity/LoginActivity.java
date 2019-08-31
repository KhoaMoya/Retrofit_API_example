package com.khoa.retrofit_api_example.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.khoa.retrofit_api_example.Control.Communicator;
import com.khoa.retrofit_api_example.Control.NetworkManager;
import com.khoa.retrofit_api_example.Control.PreferencesManager;
import com.khoa.retrofit_api_example.Event.EventFail;
import com.khoa.retrofit_api_example.Event.EventLoginSuccess;
import com.khoa.retrofit_api_example.Model.User;
import com.khoa.retrofit_api_example.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edtUsername, edtPassword;
    private Button btnLogin;
    private TextView txtForgotPassword, txtRegistration;
    private PreferencesManager preferencesManager;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        preferencesManager = new PreferencesManager(this);
        if (preferencesManager.isLoggedIn()) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        progressDialog = new ProgressDialog(this);
        edtUsername = findViewById(R.id.edt_username);
        edtPassword = findViewById(R.id.edt_password);
        btnLogin = findViewById(R.id.btn_login);
        txtForgotPassword = findViewById(R.id.txt_forgot_password);
        txtRegistration = findViewById(R.id.txt_registration);

        btnLogin.setOnClickListener(this);
        txtForgotPassword.setOnClickListener(this);
        txtRegistration.setOnClickListener(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                hideKeyBoard();
                String username = edtUsername.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();
                if (username.equals("") || password.equals("")) {
                    showAlertDialogMessage("Hãy nhập đủ thông tin");
                } else if(!NetworkManager.isConnectedToNetwork(this)){
                    showAlertDialogMessage("Lỗi kết nối mạng");
                } else {
                    progressDialog.setMessage("Đang đang nhập");
                    progressDialog.show();
                    Communicator communicator = new Communicator();
                    communicator.login(username, password);
                }
                break;
            case R.id.txt_forgot_password:

                break;
            case R.id.txt_registration:
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Subscribe
    public void onEventLoginSuccess(EventLoginSuccess eventLoginSuccess) {
        progressDialog.dismiss();
        User user = eventLoginSuccess.getUser();
        int confirmCode = user.getConfirm_code();
        if (confirmCode == 1) {
            preferencesManager.setIsLoggedIn(true);
            preferencesManager.saveUser(user);
            Log.e("Loi", "token: " + user.getToken());
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else if (confirmCode == 0) {
            showAlertDialogMessage("Mật khẩu không chính xác");
        } else {
            showAlertDialogMessage("Không tồn tại người dùng này");
        }
    }

    @Subscribe
    public void onEventFail(EventFail eventLoginFail) {
        progressDialog.dismiss();
        showAlertDialogMessage(eventLoginFail.getError());
    }

    public void showAlertDialogMessage(String message) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage(message);
        dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    public void hideKeyBoard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
