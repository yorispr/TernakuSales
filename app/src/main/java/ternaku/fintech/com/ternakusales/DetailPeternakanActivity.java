package ternaku.fintech.com.ternakusales;

import android.*;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.Locale;

public class DetailPeternakanActivity extends AppCompatActivity {
    private TextView txtidpeternakan, txtpemilik, txtalamat, txttelepon, txtjenisternak, txtjumlah, txtlayanan, txtexpire;
    private Button btnMap, btnTelepon, btnSMS;

    boolean isCallPermission = false;

    String idpeternakan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_peternakan);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });fab.hide();
        initUI();
        idpeternakan = getIntent().getStringExtra("idpeternakan");

        String param = "idpeternakan=" + idpeternakan;
        Log.d("param",param);
        new GetPeternakan().execute("http://service.ternaku.com/C_Peternakan/getPeternakan", param);

        ActivityCompat.requestPermissions(DetailPeternakanActivity.this,
                new String[]{Manifest.permission.CALL_PHONE},
                1);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    isCallPermission = true;
                } else {
                    Toast.makeText(DetailPeternakanActivity.this, "Akses ke perangkat ditolak", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    private void initUI(){
        txtidpeternakan = (TextView)findViewById(R.id.txtIdPeternakan);
        txtpemilik = (TextView)findViewById(R.id.txtPemilik);
        txtalamat = (TextView)findViewById(R.id.txtAlamat);
        txttelepon = (TextView)findViewById(R.id.txtTelepon);
        txtjenisternak = (TextView)findViewById(R.id.txtJenisTernak);
        txtjumlah = (TextView)findViewById(R.id.txtJumlahTernak);
        txtlayanan = (TextView)findViewById(R.id.txtLayanan);
        txtexpire = (TextView)findViewById(R.id.txtExpire);
        btnMap = (Button)findViewById(R.id.btnMap);
        btnTelepon = (Button)findViewById(R.id.btnTeleponPeternakan);
        btnSMS = (Button)findViewById(R.id.btnSMS);
    }
    private class GetPeternakan extends AsyncTask<String,Integer,String> {
        ProgressDialog progDialog;

        @Override
        protected void onPreExecute() {
            progDialog = new ProgressDialog(DetailPeternakanActivity.this);
            progDialog.setMessage("Mengambil Data...");
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
            Log.d("RESzzz",result);

            progDialog.dismiss();
            if (result.trim().equals("404")){
                Toast.makeText(getApplication(),"Data tidak ditemukan", Toast.LENGTH_LONG).show();
            }
            else {
                JSONArray jArray;
                try {

                        jArray  = new JSONArray(result);
                        JSONObject jObj;
                        jObj = jArray.getJSONObject(0);
                        if(jObj.has("ID_PETERNAKAN")) {
                            Peternakan P = new Peternakan();
                            P.setId_peternakan(jObj.getString("ID_PETERNAKAN"));
                            P.setId_pengguna(jObj.getString("ID_PEMILIK"));
                            if(!jObj.isNull("ID_PEGAWAI")) {
                                P.setId_pegawai(jObj.getString("ID_PEGAWAI"));
                            }
                            P.setNama_peternakan(jObj.getString("NAMA_PETERNAKAN"));
                            P.setAlamat(jObj.getString("ALAMAT_PETERNAKAN"));
                            P.setTelpon(jObj.getString("TELPON_PETERNAKAN"));
                            P.setLatitude(jObj.getString("LAT"));
                            P.setLongitude(jObj.getString("LNG"));
                            P.setTernak_pedaging(jObj.getInt("TERNAK_PEDAGING"));
                            P.setTernak_perah(jObj.getInt("TERNAK_PERAH"));
                            P.setJumlah_ternak(jObj.getInt("JUMLAH_TERNAK"));
                            P.setLayanan_aktif(jObj.getInt("LAYANAN_AKTIF"));
                            P.setTgl_expired(jObj.getString("tanggal_expire"));
                            P.setNama_pemilik(jObj.getString("NAMA"));
                            setDetail(P);
                            getSupportActionBar().setTitle(P.getNama_peternakan());

                        }else{
                            Toast.makeText(getApplication(),"Terjadi kesalahan!", Toast.LENGTH_LONG).show();
                        }

                }catch(JSONException e){
                    progDialog.dismiss();
                }
            }
        }
    }

    private void setDetail(Peternakan p){
        txtidpeternakan.setText(p.getId_peternakan());
        txtpemilik.setText(p.getNama_pemilik());
        txtalamat.setText(p.getAlamat());
        txttelepon.setText(p.getTelpon());
        if(p.getTernak_pedaging() == 1){
            txtjenisternak.setText("Ternak Pedaging");
        }
        if(p.getTernak_perah() == 1){
            txtjenisternak.setText("Ternak Perah");
        }
        txtjumlah.setText(String.valueOf(p.getJumlah_ternak()));
        txtlayanan.setText(String.valueOf(p.getLayanan_aktif()));
        txtexpire.setText(p.getTgl_expired());
        final double latitude = Double.parseDouble(p.getLatitude());
        final double longitude = Double.parseDouble(p.getLongitude());
        final String notelp = p.getTelpon();
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uri = String.format(Locale.ENGLISH, "geo:%f,%f", latitude, longitude);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(intent);
            }
        });
        btnTelepon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (isCallPermission) {
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:" + notelp));
                        callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(callIntent);
                    }
                }catch (SecurityException se){se.printStackTrace();}
            }
        });
        btnSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                sendIntent.setType("vnd.android-dir/mms-sms");
                sendIntent.putExtra("address", notelp);
                startActivity(sendIntent);
            }
        });
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
}
