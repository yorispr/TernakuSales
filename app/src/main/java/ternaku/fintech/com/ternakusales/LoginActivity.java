package ternaku.fintech.com.ternakusales;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {
    private EditText edtUsername,edtPass;
    private Button btnLogin;
    private int attempt = 0;

    private SharedPreferences shr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shr = getSharedPreferences("PegawaiPref", Context.MODE_PRIVATE);
        if(!shr.contains("keyUsername") || shr.getString("keyUsername",null)==null) {
            setContentView(R.layout.activity_login);
            if (getSupportActionBar() != null) {
                getSupportActionBar().hide();
            }
            edtUsername = (EditText) findViewById(R.id.email);
            edtPass = (EditText) findViewById(R.id.password);
            btnLogin = (Button) findViewById(R.id.email_sign_in_button);

            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    try {
                        String password = generateHash(edtPass.getText().toString()+edtUsername.getText().toString());
                        Log.d("Pass",password);

                        String urlParameters = "username=" + URLEncoder.encode(edtUsername.getText().toString(), "UTF-8") + "&password=" + URLEncoder.encode(password, "UTF-8");
                        new LoginTask().execute("http://registrasi.ternaku.com/C_Registrasi/cekLoginPegawai", urlParameters);

                    } catch (UnsupportedEncodingException u) {
                        u.printStackTrace();
                    }
                }
            });
        }
        else{
            Intent i = new Intent(LoginActivity.this,MainActivity.class);
            startActivity(i);
            finish();
        }
    }
    private class LoginTask extends AsyncTask<String, Integer, String> {
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute(){
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setMessage("Harap tunggu...");
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... urls) {
            Connection c = new Connection();
            String json = c.GetJSONfromURL(urls[0],urls[1]);
            return json;
        }

        protected void onPostExecute(String result) {
            Log.d("RES",result);
            if (result.trim().equals("0")) {
                Toast.makeText(getApplication(),"Terjadi Kesalahan..",Toast.LENGTH_LONG).show();
                pDialog.dismiss();
            }
            else{
                JSONArray jArray;
                JSONObject jObj;
                try {

                    jArray  = new JSONArray(result);
                    jObj = jArray.getJSONObject(0);
                    if(jObj.has("id_pegawai"))
                    {
                        Intent i = new Intent(LoginActivity.this,MainActivity.class);
                        startActivity(i);
                        savetoLocal(result);
                        pDialog.dismiss();
                        finish();
                    }
                    else{
                        Toast.makeText(getApplication(),"Terjadi Kesalahan..",Toast.LENGTH_LONG).show();
                        pDialog.dismiss();
                    }

                }catch(JSONException e){
                    Toast.makeText(getApplication(),"Terjadi Kesalahan..",Toast.LENGTH_LONG).show();
                    pDialog.dismiss();
                }


            }
        }

    }

    private void savetoLocal(String user)
    {
        SharedPreferences sharedpreferences;
        sharedpreferences = getSharedPreferences("PegawaiPref", Context.MODE_PRIVATE);
        try {
            JSONArray jArray = new JSONArray(user);
            JSONObject jObj = jArray.getJSONObject(0);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("keyNama",jObj.get("nama_lengkap").toString());
            editor.putString("keyRole",jObj.get("role").toString());
            editor.putString("keyIdPengguna",jObj.get("id_pegawai").toString());
            editor.putString("keyAlamat",jObj.get("alamat").toString());
            editor.putString("keyTelpon",jObj.get("telepon").toString());
            editor.putString("keyUsername",jObj.get("username").toString());

            editor.apply();
        }
        catch (JSONException e){e.printStackTrace();}
    }

    public String generateHash(String password) {
        //String toHash = "someRandomString";
        MessageDigest md = null;
        byte[] hash = null;
        try {
            md = MessageDigest.getInstance("SHA-512");
            hash = md.digest(password.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return convertToHex(hash);
    }

    /**
     * Converts the given byte[] to a hex string.
     * @param raw the byte[] to convert
     * @return the string the given byte[] represents
     */
    private String convertToHex(byte[] raw) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < raw.length; i++) {
            sb.append(Integer.toString((raw[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }



    public String get_SHA_512_SecurePassword(String passwordToHash, String salt){
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(salt.getBytes("UTF-8"));
            byte[] bytes = md.digest(passwordToHash.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++){
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        }
        catch (UnsupportedEncodingException | NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        return generatedPassword;
    }

}

