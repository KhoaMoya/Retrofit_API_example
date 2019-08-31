package com.khoa.retrofit_api_example.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.khoa.retrofit_api_example.Control.Communicator;
import com.khoa.retrofit_api_example.Control.NetworkManager;
import com.khoa.retrofit_api_example.Control.PreferencesManager;
import com.khoa.retrofit_api_example.Event.EventFail;
import com.khoa.retrofit_api_example.Event.EventSignupSuccess;
import com.khoa.retrofit_api_example.Model.User;
import com.khoa.retrofit_api_example.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edtEmail, edtPassword, edtConfirmPassword, edtUsername;
    private Button btnSignup;
    private TextView txtHadAccount;
    private LinearLayout linearLayoutSignupWithGoogle;
    private PreferencesManager preferencesManager;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sigup);
        getSupportActionBar().hide();

        preferencesManager = new PreferencesManager(this);
        edtEmail = findViewById(R.id.edt_email);
        edtPassword = findViewById(R.id.edt_password);
        edtUsername = findViewById(R.id.edt_username);
        edtConfirmPassword = findViewById(R.id.edt_confirm_password);
        btnSignup = findViewById(R.id.btn_signup);
        txtHadAccount = findViewById(R.id.txt_available_account);
        linearLayoutSignupWithGoogle = findViewById(R.id.signup_with_google);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        btnSignup.setOnClickListener(this);
        txtHadAccount.setOnClickListener(this);
        progressDialog = new ProgressDialog(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_signup:
                hideKeyBoard();
                String username = edtUsername.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();
                String confirmPassword = edtConfirmPassword.getText().toString().trim();
                String email = edtEmail.getText().toString().trim();
                if (username.equals("") || password.equals("") || confirmPassword.equals("") || email.equals("")) {
                    showAlertDialogMessage("Hãy nhập đủ thông tin");
                } else if (password.length() < 8) {
                    showAlertDialogMessage("Mật khẩu phải có ít nhất 8 ký tự");
                } else if (!password.equals(confirmPassword)) {
                    showAlertDialogMessage("Mật khẩu không giống nhau");
                } else if (!isEmail(email)) {
                    showAlertDialogMessage("Email không đúng");
                } else if(!NetworkManager.isConnectedToNetwork(this)){
                    showAlertDialogMessage("Lỗi kết nối mạng");
                }else {
                    progressDialog.setMessage("Đang đăng ký");
                    progressDialog.show();
                    Communicator communicator = new Communicator();
                    communicator.signup(username, password, email);
                }
                break;
            case R.id.txt_available_account:
                onBackPressed();
                break;
            default:
                break;
        }

    }

    @Subscribe
    public void onEventSignupSucces(EventSignupSuccess eventSignupSucces) {
        progressDialog.dismiss();
        User user = eventSignupSucces.getUser();
        int confirmCode = user.getConfirm_code();
        if (confirmCode == 0) {
            showAlertDialogMessage("Người dùng này đã tồn tại");
        } else if (confirmCode == -1) {
            showAlertDialogMessage("Đăng ký thất bại");
        } else {
            preferencesManager.setIsLoggedIn(true);
            preferencesManager.saveUser(user);
            Intent intent = new Intent(SignupActivity.this, MainActivity.class);
            startActivity(intent);
            finishAffinity();
        }

    }

    @Subscribe
    public void onEventSignupFail(EventFail eventSignupFail) {
        progressDialog.dismiss();
        showAlertDialogMessage(eventSignupFail.getError());
    }

    public boolean isEmail(String string) {

        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        return pat.matcher(string).matches();
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
