package ternaku.fintech.com.ternakusales;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RegistrasiPeternakanActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private Spinner spinJenis;
    private Button btnDaftar;
    private EditText nama,alamat,notelp,jmlhternak;
    int pedaging=0,perah=0;
    double latitude,longitude;
    String uname,pass;
    String idpeternakan;

    GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrasi_peternakan);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        ActivityCompat.requestPermissions(RegistrasiPeternakanActivity.this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                1);

        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        nama = (EditText)findViewById(R.id.nama_peternakan);
        alamat = (EditText)findViewById(R.id.alamat_peternakan);
        notelp = (EditText)findViewById(R.id.telepon);
        jmlhternak = (EditText)findViewById(R.id.jumlahternak);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        fab.hide();
        spinJenis = (Spinner)findViewById(R.id.spinnerJenis);
        String spinnerdata[] = {"Pedaging", "Perah"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, spinnerdata);
        spinJenis.setAdapter(adapter);

        spinJenis.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0){
                    pedaging = 1;
                    perah=0;
                }else{
                    pedaging = 0;
                    perah=1;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        btnDaftar = (Button)findViewById(R.id.button_daftar);
        btnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String param = "idpemilik=null"
                        + "&namapeternakan=" + nama.getText().toString()
                        + "&alamatpeternakan=" + alamat.getText().toString()
                        + "&teleponpeternakan=" + notelp.getText().toString()
                        + "&lat=" + latitude
                        + "&lng=" + longitude
                        + "&ternakpedaging=" + pedaging
                        + "&ternakperah=" + perah
                        + "&jumlahternak=" + jmlhternak.getText().toString()
                        + "&idpegawai=" + getSharedPreferences("PegawaiPref",Context.MODE_PRIVATE).getString("keyIdPengguna",null)
                        + "&tgltransaksi=" + new Date()
                        + "&biaya=0"
                        ;
                Log.d("param",param);
                new AddPeternakan().execute("http://registrasi.ternaku.com/C_Registrasi/insertPeternakan", param);

            }
        });



    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }


    private class AddPeternakan extends AsyncTask<String,Integer,String> {
        ProgressDialog progDialog;

        @Override
        protected void onPreExecute() {
            progDialog = new ProgressDialog(RegistrasiPeternakanActivity.this);
            progDialog.setMessage("Tunggu Sebentar...");
            progDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            Connection c = new Connection();
            String json = c.GetJSONfromURL(params[0],params[1]);
            return json;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d("RES",result);

            progDialog.dismiss();
            if (result.trim().equals("0")){
                Toast.makeText(getApplication(),"Terjadi kesalahan",Toast.LENGTH_LONG).show();
            }
            else {

                JSONArray jArray;
                JSONObject jObj;
                try {
                    jArray  = new JSONArray(result);
                    jObj = jArray.getJSONObject(0);
                    if(jObj.has("username"))
                    {
                        showCredential(jObj.getString("username"),jObj.getString("password"));
                        /*String username = jObj.getString("username");
                        String password = jObj.getString("password");
                        String pwd = generateHash(password+username);

                        JSONObject j = new JSONObject();
                        j = jObj.getJSONObject("0");
                        HistoryPenjualan P = new HistoryPenjualan();
                        P.setId_peternakan(j.getString("ID_PETERNAKAN"));
                        P.setNama_peternakan(j.getString("NAMA_PETERNAKAN"));
                        P.setAlamat(j.getString("ALAMAT_PETERNAKAN"));
                        P.setTelpon(j.getString("TELPON_PETERNAKAN"));
                        P.setLat(j.getString("LAT"));
                        P.setLng(j.getString("LNG"));
                        P.setUsername(username);
                        P.setPassword(password);
                        DatabaseHandler db = new DatabaseHandler(RegistrasiPeternakanActivity.this);
                        db.AddHistory(P);*/
                        progDialog.dismiss();
                    }
                    else{
                        Toast.makeText(getApplication(),"Terjadi Kesalahan..",Toast.LENGTH_LONG).show();
                        progDialog.dismiss();
                    }
                }catch(JSONException e){
                    Toast.makeText(getApplication(),"Terjadi Kesalahan..",Toast.LENGTH_LONG).show();
                    progDialog.dismiss();
                }


            }
        }
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

    private void showCredential(String username, String pass){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(RegistrasiPeternakanActivity.this);
        builder1.setTitle("Detail Login");
        builder1.setMessage("Data pengguna : "+"\n Username : "+username+"\n Password : "+pass+"\n\n* Segera catat username dan password!");
        builder1.setCancelable(true);
        final String un = username, pas = pass;
        builder1.setPositiveButton(
                "Copy data",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("Data login", "Username : "+un+"\n Password : "+pas);
                        clipboard.setPrimaryClip(clip);
                        Toast.makeText(getApplicationContext(),"Data sudah disalin",Toast.LENGTH_LONG).show();
                    }
                });

        builder1.setNegativeButton(
                "Tutup",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    GetLocation();
                } else {
                    Toast.makeText(RegistrasiPeternakanActivity.this, "Akses ke perangkat ditolak", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    private void GetLocation(){
        StringBuilder builder = new StringBuilder();
        Geocoder geoCoder = new Geocoder(this, Locale.getDefault());
        try {
            LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if(location!=null) {
                longitude = location.getLongitude();
                latitude = location.getLatitude();
                Log.d("LATLNG", String.valueOf(latitude) + ", " + String.valueOf(longitude));
            }else{
                location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                longitude = location.getLongitude();
                latitude = location.getLatitude();
                Log.d("LATLNG", String.valueOf(latitude) + ", " + String.valueOf(longitude));
            }
        }catch(SecurityException se){se.printStackTrace();}

    }

}
