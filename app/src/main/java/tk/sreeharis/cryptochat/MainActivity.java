package tk.sreeharis.cryptochat;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.MainThread;
import android.support.design.widget.FloatingActionButton;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.bridge.Bridge;
import com.afollestad.bridge.BridgeException;
import com.afollestad.bridge.Form;
import com.afollestad.bridge.Request;
import com.afollestad.bridge.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.security.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private void AcctSetup() {
        Intent intent = new Intent(this, AccountSetup.class);
        finish();
        startActivity(intent);

    }
    private void restartActivity() {

        Intent intent = new Intent(this, MainActivity.class);
        finish();
        startActivity(intent);
    }
    private void CreateGroupLaunch() {
        Intent intent = new Intent(this, CreateGroup.class);
        startActivity(intent);
    }
    public boolean isJSONValid(String test) {
        try {
            new JSONObject(test);
        } catch (JSONException ex) {
            // edited, to include @Arthur's comment
            // e.g. in case JSONArray is valid as well...
            try {
                new JSONArray(test);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }
    private String getUUID() {
        try {
            FileInputStream fIn = openFileInput("yellow");
            StringBuilder bildar = new StringBuilder();
            int ch;
            while((ch = fIn.read()) != -1) {
                bildar.append((char)ch);
            }
            String token_skyaa = bildar.toString();
            String token = token_skyaa.split(":")[1];
            token = token.replaceAll("\n", "");
            return token;
        } catch (Exception e) {
            return "bulsit";
        }

    }
    private String GetMessages() {
        Form form = new Form()
                .add("uuid", getUUID())
                .add("group", "hakers");
        try {
            Request request = Bridge.post("https://sreeharis.tk/cc/get.php").body(form).request();
            Response response = request.response();
            if(response.isSuccess()) {
                String output = response.asString();
                return output;
            }
        } catch (Exception e) {
            return "exception";
        }
        return "end";
    }
    private void LogOut() {
        Thread thread = new Thread(new Runnable(){
            //@Override
            public void run() {
                try {
                    //Your code goes here
                    File dir = getFilesDir();
                    File file = new File(dir, "yellow");
                    StringBuilder contentBuilder = new StringBuilder();
                    try {
                        BufferedReader br = new BufferedReader(new FileReader(file.getAbsoluteFile()));
                        String sCurrentLine;
                        while ((sCurrentLine = br.readLine()) != null) {
                            contentBuilder.append(sCurrentLine).append("");
                        }
                        String token = contentBuilder.toString().split(":")[1];
                        Form form = new Form()
                                .add("uuid", token);

                        Request request = Bridge.post("https://sreeharis.tk/cc/logout.php").body(form).request();

                        Response response = request.response();

                        String output;
                        if (response.isSuccess()) {
                            output = response.asString();
                        } else {
                            output = "cyka";
                        }
                        file.delete();
                        finish();
                        restartActivity();
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateGroupLaunch();
            }
        });
        boolean iexits = false;
        try {
            FileInputStream fin = openFileInput("yellow");
            int c;
            String temp="";
            while( (c = fin.read()) != -1){
                temp = temp + Character.toString((char)c);
            }

            fin.close();
            if(temp.contains("token")) {
                iexits = true;
            }

        } catch (Exception e) {
            System.out.println("Look at the stars. Look how they shine for you. And everything you do.");
        }
        if(!iexits) {
            AcctSetup();
        }

        ListView listview = (ListView) findViewById(R.id.message_list);


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
        if (id == R.id.action_settings) {
            LogOut();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
