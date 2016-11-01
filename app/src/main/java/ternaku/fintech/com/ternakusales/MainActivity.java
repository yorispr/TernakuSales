package ternaku.fintech.com.ternakusales;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    ArrayList<HistoryPenjualan> listPenjualan;
    private SimpleDateFormat dateFormatter;
    private TextView txttidakada;
    private ImageView imgtidakada;

    private Button btnRefresh;

    HashMap<String, List<String>> listDataChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });fab.hide();
        listPenjualan = new ArrayList<>();
        listDataHeader = new ArrayList<>();

        btnRefresh = (Button)findViewById(R.id.btnRefresh);
        listDataChild = new HashMap<String, List<String>>();
        txttidakada = (TextView)findViewById(R.id.txtTidakada);
        imgtidakada = (ImageView)findViewById(R.id.imgTidakada);
        imgtidakada.setVisibility(View.GONE);
        txttidakada.setVisibility(View.GONE);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        expListView = (ExpandableListView) findViewById(R.id.expandList);
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                Toast.makeText(getApplicationContext(),"Post : "+listPenjualan.get(i).getId_penjualan(),Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MainActivity.this,DetailPeternakanActivity.class);
                intent.putExtra("idpeternakan",listPenjualan.get(i).getId_peternakan());
                startActivity(intent);
                return true;
            }
        });
        View header=navigationView.getHeaderView(0);
        TextView name = (TextView)header.findViewById(R.id.name);
        TextView email = (TextView)header.findViewById(R.id.email);
        name.setText(getSharedPreferences("PegawaiPref", Context.MODE_PRIVATE).getString("keyNama",null));
        email.setText(getSharedPreferences("PegawaiPref", Context.MODE_PRIVATE).getString("keyUsername",null));
        // preparing list data

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listPenjualan.clear();
                listDataHeader.clear();
                listDataChild.clear();
                String param = "idsales=" + getSharedPreferences("PegawaiPref",Context.MODE_PRIVATE).getString("keyIdPengguna",null)
                        ;
                Log.d("param",param);
                new GetDataPenjualan().execute("http://performance.ternaku.com/C_Dashboard/GetPenjualanSalesMobile", param);
            }
        });


        String param = "idsales=" + getSharedPreferences("PegawaiPref",Context.MODE_PRIVATE).getString("keyIdPengguna",null)
                ;
        Log.d("param",param);
        new GetDataPenjualan().execute("http://performance.ternaku.com/C_Dashboard/GetPenjualanSalesMobile", param);
    }


    private class GetDataPenjualan extends AsyncTask<String,Integer,String> {
        ProgressDialog progDialog;

        @Override
        protected void onPreExecute() {
            progDialog = new ProgressDialog(MainActivity.this);
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
            if (result.trim().equals("0")){
                //Toast.makeText(getApplication(),"Terjadi kesalahan",Toast.LENGTH_LONG).show();
                txttidakada.setVisibility(View.VISIBLE);
                imgtidakada.setVisibility(View.VISIBLE);
            }
            else {

                JSONArray jArray;
                try {
                    jArray  = new JSONArray(result);
                    JSONObject jObj;

                    for(int i=0;i<jArray.length();i++) {
                            jObj = jArray.getJSONObject(i);
                            HistoryPenjualan P = new HistoryPenjualan();
                            P.setId_penjualan(jObj.getString("ID_PENJUALAN"));
                            P.setId_peternakan(jObj.getString("ID_PETERNAKAN"));
                            P.setTgl_transaksi(jObj.getString("TGL_TRANSAKSI"));
                            P.setTelpon(jObj.getString("BIAYA"));
                            listPenjualan.add(P);
                            progDialog.dismiss();
                    }


                    listAdapter = new ExpandableListAdapter(getApplicationContext(), listDataHeader, listDataChild);

                    // setting list adapter
                    expListView.setAdapter(listAdapter);
                    setListData();


                }catch(JSONException e){
                    txttidakada.setVisibility(View.VISIBLE);
                    imgtidakada.setVisibility(View.VISIBLE);
                    progDialog.dismiss();
                }


            }
        }
    }


    private void setListData(){
        //dateFormatter = new SimpleDateFormat("dd MMMM yyyy", Locale.US);

        DatabaseHandler db = new DatabaseHandler(this);
       // db.DropTable();


        if(listPenjualan.size() != 0) {
            for (int i = 0; i < listPenjualan.size(); i++) {
                // Date d = dateFormatter.parse(listPenjualan.get(i).getTgl_transaksi());
                //String formatteddate = dateFormatter.parse(listPenjualan.get(i).getTgl_transaksi()).toString();
                listDataHeader.add(listPenjualan.get(i).getTgl_transaksi());

                Log.d("TANGGAL", listPenjualan.get(i).getTgl_transaksi());
                List<String> listIsi = new ArrayList<String>();

                listIsi.add("Nomor Penjualan : " + listPenjualan.get(i).getId_penjualan()
                        + "\nNomor Peternakan : " + listPenjualan.get(i).getId_peternakan()

                );
                listDataChild.put(listDataHeader.get(i), listIsi);

            }
        }else{
            txttidakada.setVisibility(View.VISIBLE);
            imgtidakada.setVisibility(View.VISIBLE);
        }
        listAdapter.notifyDataSetChanged();

    }

    @Override
    public void onResume(){
        super.onResume();
        // put your code here...
        //setListData();
        //listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
        // setting list adapter
        //expListView.setAdapter(listAdapter);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_registrasi) {
            Intent i = new Intent(MainActivity.this,RegistrasiPeternakanActivity.class);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
