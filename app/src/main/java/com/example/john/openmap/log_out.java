package com.example.john.openmap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.john.openmap.activity.LoginActivity;
import com.example.john.openmap.helper.SQLiteHandler;
import com.example.john.openmap.helper.SessionManager;

/**
 * Created by ppjj5 on 2018-01-09.
 *
 */

public class log_out extends Activity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SessionManager SessionManager = new SessionManager(getApplicationContext());
        SQLiteHandler sqLiteHandler = new SQLiteHandler(getApplicationContext());

        SessionManager.setLogin(false);
        sqLiteHandler.deleteUsers();

        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }
}