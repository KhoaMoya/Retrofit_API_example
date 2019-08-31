package com.khoa.retrofit_api_example.Control;

import android.content.Context;
import android.content.SharedPreferences;

import com.khoa.retrofit_api_example.Model.User;

public class PreferencesManager {

    private SharedPreferences sharedPreferences;
    private Context context;
    private SharedPreferences.Editor editor;
    private String PRE_NAME = "preferences";
    private String KEY_IS_LOGGEDIN = "isLoggedIn";
    private String KEY_USER_NAME = "username";
    private String KEY_EMAIL = "email";
    private String KEY_CREATED_AT = "createdAt";
    private String KEY_ID = "id";
    private String KEY_TOKEN = "token";
    private String KEY_ID_AVATAR = "id_avatar";

    public PreferencesManager(Context context) {
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences(PRE_NAME, Context.MODE_PRIVATE);
        this.editor = this.sharedPreferences.edit();
    }

    public void setIsLoggedIn(boolean isLoggedIn) {
        editor.putBoolean(KEY_IS_LOGGEDIN, isLoggedIn);
        editor.commit();
    }

    public void saveUser(User user) {
        editor.putInt(KEY_ID, user.getId());
        editor.putString(KEY_USER_NAME, user.getUsername());
        editor.putString(KEY_EMAIL, user.getEmail());
        editor.putString(KEY_CREATED_AT, user.getCreated_at());
        editor.putString(KEY_TOKEN, user.getToken());
        editor.putInt(KEY_ID_AVATAR, user.getId_avatar_image());
        editor.commit();
    }

    public void resetUser() {
        editor.putInt(KEY_ID, -1);
        editor.putString(KEY_USER_NAME, "");
        editor.putString(KEY_EMAIL, "");
        editor.putString(KEY_CREATED_AT, "");
        editor.putString(KEY_TOKEN, "");
        editor.putInt(KEY_ID_AVATAR, -1);
        editor.commit();
    }

    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(KEY_IS_LOGGEDIN, false);
    }

    public User getUser() {
        String username = sharedPreferences.getString(KEY_USER_NAME, "");
        String email = sharedPreferences.getString(KEY_EMAIL, "");
        int id = sharedPreferences.getInt(KEY_ID, -1);
        String createdAt = sharedPreferences.getString(KEY_CREATED_AT, "");
        String token = sharedPreferences.getString(KEY_TOKEN, "");
        int idAvatar = sharedPreferences.getInt(KEY_ID_AVATAR, -1);
        return new User(id, username, email, createdAt, 1, token, idAvatar);
    }
}
