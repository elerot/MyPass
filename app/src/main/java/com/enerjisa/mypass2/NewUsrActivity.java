package com.enerjisa.mypass2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class NewUsrActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_usr);

        final DBAdapter dbAdapter = new DBAdapter(this);

        final EditText etUserName = (EditText) findViewById(R.id.etUserName);
        final EditText etPass = (EditText) findViewById(R.id.etPass);
        final EditText etPass2 = (EditText) findViewById(R.id.etPass2);

        Button buttonOk = (Button) findViewById(R.id.buttonOk);
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etUserName.getText().toString().trim().length() > 0) {
                    if (etPass.getText().toString().equals(etPass2.getText().toString())) {
                        long uID = dbAdapter.CheckUser(etUserName.getText().toString());
                        if (uID < 0) {
                            long newUID = dbAdapter.InsertUser(etUserName.getText().toString(), etPass.getText().toString());
                            if (newUID > 0)
                                Toast.makeText(NewUsrActivity.this, "Kullanıcı eklendi!", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(NewUsrActivity.this, "İşlem başarısız oldu!", Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(NewUsrActivity.this, "Kullanıcı adı zaten var!", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(NewUsrActivity.this, "Şifreler uyuşmuyor!", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(NewUsrActivity.this, "Kullanıcı adı boş girilemez!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_usr, menu);
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
