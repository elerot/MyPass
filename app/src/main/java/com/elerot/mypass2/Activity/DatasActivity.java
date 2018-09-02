package com.elerot.mypass2.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.os.Handler;
import android.widget.Toast;

import com.elerot.mypass2.Class.AESCrypt;
import com.elerot.mypass2.Adaptor.DBAdapter;
import com.elerot.mypass2.Class.data;
import com.elerot.mypass2.Adaptor.MyAdabtor;
import com.elerot.mypass2.R;
import com.elerot.mypass2.SimpleFileChooser.Constants;
import com.elerot.mypass2.SimpleFileChooser.ui.FileChooserActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class DatasActivity extends AppCompatActivity {
    private ListView listView;
    private DBAdapter dbAdapter;
    private long userID;
    private String key;
    public ArrayList<data> fullRecords;
    public ArrayList<data> filtered;
    private boolean orderWay = false;
    public AppCompatActivity datasActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datas);
        Intent intent = getIntent();
        userID = intent.getLongExtra("userID", -1);
//        key = intent.getStringExtra("key");
//        if(key.isEmpty())
//            key = "s12345678s.";

        Button btnNewData = (Button) findViewById(R.id.btnNewData);
        Button btnOrder = (Button) findViewById(R.id.btnOrder);
        Button btnDeleteData = (Button) findViewById(R.id.btnDeleteData);
        final EditText etSearch = (EditText) findViewById(R.id.etSearch);
        Button btnSettings = (Button) findViewById(R.id.btnSettings);


        listView = (ListView) findViewById(R.id.lwData);
        fullRecords = new ArrayList<data>();
        filtered = new ArrayList<data>();
        dbAdapter = new DBAdapter(this);

        Fill();
        datasActivity = this;
        checkStoragePermissions(datasActivity);
        btnNewData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DatasActivity.this, NewDataActivity.class);
                intent.putExtra("dataID", -1);
                intent.putExtra("userID", userID);
                //intent.putExtra("key", key);
                startActivityForResult(intent, 1);
            }
        });

        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderWay = !orderWay;
                //Collections.sort(filtered, new CustomComparator());
                Collections.sort(filtered, new Comparator<data>() {
                    @Override
                    public int compare(data o1, data o2) {
                        if (orderWay)
                            return o1.displayName.toUpperCase().compareTo(o2.displayName.toUpperCase());
                        else
                            return o2.displayName.toUpperCase().compareTo(o1.displayName.toUpperCase());
                    }
                });

                ArrayAdapter<data> arr = new MyAdabtor(DatasActivity.this, filtered);
                arr.notifyDataSetChanged();
                listView.setAdapter(arr);
            }
        });

        etSearch.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filtered = new ArrayList<data>();
                for (data data : fullRecords) {
                    if (data.displayName.toUpperCase().contains(etSearch.getText().toString().toUpperCase())) {
                        filtered.add(data);
                    }
                }
                ArrayAdapter<data> arr = new MyAdabtor(DatasActivity.this, filtered);
                arr.notifyDataSetChanged();
                listView.setAdapter(arr);
            }
        });

        btnDeleteData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<data> delArr = new ArrayList<data>();
                for (int i = 0; i < filtered.size(); i++) {
                    data d = filtered.get(i);
                    if (d.vsblty == View.GONE)
                        d.vsblty = View.VISIBLE;
                    else
                        d.vsblty = View.GONE;
                    filtered.set(i, d);

                    if (d.cbChecked)
                        delArr.add(d);
                }

                for (int i = 0; i < delArr.size(); i++) {
                    if (dbAdapter.deleteData(delArr.get(i)._id))
                        filtered.remove(delArr.get(i));
                }

                ArrayAdapter<data> arr = new MyAdabtor(DatasActivity.this, filtered);
                arr.notifyDataSetChanged();
                listView.setAdapter(arr);
            }
        });

        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.activity_settings);
                Intent intentS = getIntent();
                //userID = intentS.getLongExtra("userID", -1);
                checkStoragePermissions(datasActivity);
                Button btnExport = (Button) findViewById(R.id.btnExport);
                Button btnImport = (Button) findViewById(R.id.btnImport);
                Button btnDeleteAllData = (Button) findViewById(R.id.btnDeleteData);

                btnExport.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intentFc = new Intent(datasActivity, FileChooserActivity.class);
                        intentFc.putExtra("type", 2);
                        startActivityForResult(intentFc, 2);
                    }
                });

                btnImport.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intentFc = new Intent(datasActivity, FileChooserActivity.class);
                        intentFc.putExtra("type", 3);
                        startActivityForResult(intentFc, 3);
                    }
                });

                btnDeleteAllData.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AlertDialog.Builder(datasActivity)
                                //.setTitle("Title")
                                //.setMessage("Do you really want to whatever?")
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        //Toast.makeText(datasActivity, R.string.yes, Toast.LENGTH_SHORT).show();
                                        if (dbAdapter.deleteAllData())
                                            Toast.makeText(DatasActivity.this, R.string.dataDeleted, Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .setNegativeButton(R.string.no, null).show();
                    }
                });
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String pass = filtered.get(position).pass;
                String dePass = "";// = filtered.get(position).pass;
                try {
                    dePass = new AESCrypt(MainActivity._key).decrypt(pass);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //String pass = filtered.get(position).pass;
                new AlertDialog.Builder(DatasActivity.this)
                        .setTitle(R.string.pass)
                        .setMessage(dePass)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final long _id = filtered.get(position)._id;
                final boolean[] result = {false};

                Intent intent = new Intent(DatasActivity.this, NewDataActivity.class);
                if (_id > -1) {
                    String pass = filtered.get(position).pass;
                    String dePass = "";
                    try {
                        dePass = new AESCrypt(MainActivity._key).decrypt(pass);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    intent.putExtra("dataID", _id);
                    intent.putExtra("userID", userID);
                    intent.putExtra("displayName", filtered.get(position).displayName);// ((SQLiteCursor) selectedItemObj[0]).getString(1));
                    intent.putExtra("userName", filtered.get(position).userName);//((SQLiteCursor) selectedItemObj[0]).getString(2));
                    intent.putExtra("pass", dePass);//((SQLiteCursor) selectedItemObj[0]).getString(3));
                    intent.putExtra("description", filtered.get(position).description);//((SQLiteCursor) selectedItemObj[0]).getString(4));
                    startActivityForResult(intent, 1);
                } else
                    Toast.makeText(DatasActivity.this, R.string.pres_long, Toast.LENGTH_SHORT).show();

                return result[0];
            }
        });
    }

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private void wrtieData2File(String fileSelected) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
            String currentDateandTime = sdf.format(new Date());
            String fullPath = fileSelected + "/" + currentDateandTime + "_MyPass.txt";
            FileWriter writer = new FileWriter(fullPath);
            for (data item : fullRecords) {
                String line = item.userID + ","
                        + (item.displayName.isEmpty() ? "-" : item.displayName) + ","
                        + (item.userName.isEmpty() ? "-" : item.userName) + ","
                        + (item.pass.isEmpty() ? "-" : item.pass) + ","
                        + (item.description.isEmpty() ? "-" : item.description)
                        + ";#";
                writer.write(line);
            }
            writer.flush();
            writer.close();
            Toast.makeText(DatasActivity.this, R.string.ok, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(DatasActivity.this, R.string.notok, Toast.LENGTH_SHORT).show();
        }
    }

    private void readDataFromFile(String fileSelected) {
        try {
            File file = new File(fileSelected);
            BufferedReader br = new BufferedReader(new FileReader(file));
            String st;
            StringBuilder stFull = new StringBuilder();
            while ((st = br.readLine()) != null) {
                stFull.append(st);
            }
            String[] strs = stFull.toString().split(";#");
            for (String item : strs) {
                String[] colums = item.split(",");
                int userid = Integer.parseInt(colums[0]);
                long result = dbAdapter.UpsertData(-1, userid, colums[1], colums[2], colums[3], colums[4]);
            }
            //long result = dbAdapter.UpsertData(dataID, userID, etTitle.getText().toString(), etNewUserName.getText().toString(), CryptedPass, etDescription.getText().toString());
            Toast.makeText(DatasActivity.this, R.string.ok, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(DatasActivity.this, R.string.notok, Toast.LENGTH_SHORT).show();
        }
    }

    private void checkStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    private void Fill() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                dbAdapter.openR();
                //dataCursorAdaptor = new DataCursorAdaptor(DatasActivity.this, dbAdapter.GetAllDatas(userID));
                fullRecords.clear();
                Cursor c = dbAdapter.GetAllDatas(userID);
                if (c != null && c.getCount() > 0) {
                    c.moveToFirst();
                    do {
                        data d = new data(false, View.GONE, c.getInt(0), userID, c.getString(1), c.getString(2), c.getString(3), c.getString(4));
                        fullRecords.add(d);
                    } while (c.moveToNext());
                }
                if (c != null) {
                    c.close();
                }
                filtered = fullRecords;
                ArrayAdapter<data> arr = new MyAdabtor(DatasActivity.this, filtered);
                listView.setAdapter(arr);
                dbAdapter.close();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == 1) && (resultCode == RESULT_OK)) {
            Fill();
        } else if ((requestCode == 2) && (resultCode == RESULT_OK)) {
            String fileSelected = data.getStringExtra(Constants.KEY_FILE_SELECTED);
            Toast.makeText(this, getString(R.string.exportFilePath) + fileSelected, Toast.LENGTH_SHORT).show();
            wrtieData2File(fileSelected);
        } else if ((requestCode == 3) && (resultCode == RESULT_OK)) {
            String fileSelected = data.getStringExtra(Constants.KEY_FILE_SELECTED);
            Toast.makeText(this, getString(R.string.file2Import) + fileSelected, Toast.LENGTH_SHORT).show();
            readDataFromFile(fileSelected);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_datas, menu);
        return true;
    }

}
