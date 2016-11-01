package ternaku.fintech.com.ternakusales;

/**
 * Created by YORIS on 10/4/16.
 */

public class HistoryPenjualan {
    private String id_penjualan;
    private String id_peternakan;
    private String nama_peternakan;
    private String alamat;
    private String lat;
    private String lng;
    private String telpon;
    private String tgl_transaksi;
    private String username;
    private String password;

    public HistoryPenjualan(){

    }

    public HistoryPenjualan(String id_penjualan, String id_peternakan, String nama_peternakan,String alamat,String lat,String lng,String telpon,String tgl_transaksi, String username, String password){
        this.setId_penjualan(id_penjualan);
        this.setId_peternakan(id_peternakan);
        this.setNama_peternakan(nama_peternakan);
        this.setAlamat(alamat);
        this.setLat(lat);
        this.setLng(lng);
        this.setTelpon(telpon);
        this.setTgl_transaksi(tgl_transaksi);
        this.setUsername(username);
        this.setPassword(password);

    }

    public String getId_penjualan() {
        return id_penjualan;
    }

    public void setId_penjualan(String id_penjualan) {
        this.id_penjualan = id_penjualan;
    }

    public String getId_peternakan() {
        return id_peternakan;
    }

    public void setId_peternakan(String id_peternakan) {
        this.id_peternakan = id_peternakan;
    }

    public String getNama_peternakan() {
        return nama_peternakan;
    }

    public void setNama_peternakan(String nama_peternakan) {
        this.nama_peternakan = nama_peternakan;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getTelpon() {
        return telpon;
    }

    public void setTelpon(String telpon) {
        this.telpon = telpon;
    }

    public String getTgl_transaksi() {
        return tgl_transaksi;
    }

    public void setTgl_transaksi(String tgl_transaksi) {
        this.tgl_transaksi = tgl_transaksi;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
