package com.enerjisa.mypass2;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText editTextUserName = (EditText) findViewById(R.id.editTextUserName);
        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String userName = sharedPref.getString("userName", "");
        String pass = sharedPref.getString("pass", "");

        Button btnLgn = (Button) findViewById(R.id.btnLogin);
        editTextUserName.setText(userName);

        final EditText editTextPass = (EditText) findViewById(R.id.editTextPass);
        editTextPass.setText(pass);
        final DBAdapter dbAdapter = new DBAdapter(this);
        final CheckBox cbRemMe = (CheckBox) findViewById(R.id.cbRememberMe);
        cbRemMe.setChecked(sharedPref.getBoolean("rememberMe", false));
        Button btnCreateNew = (Button) findViewById(R.id.btnCreateNewUser);
        btnCreateNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NewUsrActivity.class);
                //intent.putExtra("dbAdapterKey",dbAdapter);
                startActivity(intent);
            }
        });

        btnLgn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long userID = dbAdapter.GetUserID(editTextUserName.getText().toString(), editTextPass.getText().toString());
                if (userID > 0) {
                    SharedPreferences.Editor editor = sharedPref.edit();
                    if (cbRemMe.isChecked()) {
                        editor.putString("userName", editTextUserName.getText().toString());
                        editor.putString("pass", editTextPass.getText().toString());
                    }
                    else
                        editor.clear();
                    editor.putBoolean("rememberMe", cbRemMe.isChecked());
                    editor.commit();

                    Intent intent = new Intent(MainActivity.this, DatasActivity.class);
                    intent.putExtra("userID",userID);
                    startActivity(intent);
                } else if (userID == 0)
                    Toast.makeText(MainActivity.this, "Şifre Yanlış!", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(MainActivity.this, "Yanlış Kullanıcı Adı ya da Şifre!", Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }
}
