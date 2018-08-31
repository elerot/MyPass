package com.elerot.mypass2;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class NewData extends AppCompatActivity {
    public DBAdapter dbAdapter;

    public Button btnNewSave;
    public EditText etTitle;
    public EditText etNewUserName;
    public EditText etNewPass;
    public EditText etDescription;

    public Intent intent;
    public long dataID;
    public long userID;
    //public String key;
    public String displayName;
    public String userName;
    public String pass;
    public String description;

    public void CheckAndFill() {
        if (dataID > -1) {
            etTitle.setText(displayName);
            etNewUserName.setText(userName);
            etNewPass.setText(pass);
            etDescription.setText(description);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_data);
        dbAdapter = new DBAdapter(this);
        btnNewSave = (Button) findViewById(R.id.btnNewSave);
        etTitle = (EditText) findViewById(R.id.etTitle);
        etNewUserName = (EditText) findViewById(R.id.etNewUserName);
        etNewPass = (EditText) findViewById(R.id.etNewPass);
        etDescription = (EditText) findViewById(R.id.etDescription);

        intent = getIntent();
        dataID = intent.getLongExtra("dataID", -1);
        userID = intent.getLongExtra("userID", -1);
        //key = intent.getStringExtra("key");

        displayName = intent.getStringExtra("displayName");
        userName = intent.getStringExtra("userName");
        pass = intent.getStringExtra("pass");
        description = intent.getStringExtra("description");

        CheckAndFill();

        btnNewSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cyperText =  etNewPass.getText().toString();
                String CryptedPass = cyperText;
                try
                {
                    CryptedPass = new AESCrypt(Main2Activity._key).encrypt(cyperText);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

                long result = dbAdapter.UpsertData(dataID, userID, etTitle.getText().toString(), etNewUserName.getText().toString(), CryptedPass, etDescription.getText().toString());
                if (result > -1) {
                    Toast.makeText(NewData.this,R.string.dataSaved, Toast.LENGTH_SHORT).show();
                    //intent.putExtra("key", key);
                    intent.putExtra("userID",userID);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                } else
                    Toast.makeText(NewData.this, R.string.dataUnSaved, Toast.LENGTH_SHORT).show();
            }
        });
    }


}
