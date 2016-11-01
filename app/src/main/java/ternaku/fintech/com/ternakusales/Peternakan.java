package ternaku.fintech.com.ternakusales;

/**
 * Created by YORIS on 10/4/16.
 */

public class Peternakan {
    private String id_peternakan;
    private String id_pengguna;
    private String id_pegawai;
    private String nama_pemilik;

    private String nama_peternakan;
    private String alamat;
    private String telpon;
    private String latitude;
    private String longitude;
    private int ternak_pedaging;
    private int ternak_perah;
    private int jumlah_ternak;
    private int layanan_aktif;
    private String tgl_expired;

    public Peternakan() {

    }

    public Peternakan(String id_peternakan, String id_pengguna, String id_pegawai,String nama_pemilik ,String nama_peternakan, String alamat, String telpon, String latitude, String longitude, int ternak_pedaging, int ternak_perah, int jumlah_ternak, int layanan_aktif, String tgl_expired ) {
        this.setId_peternakan(id_peternakan);
        this.setId_pengguna(id_pengguna);
        this.setNama_peternakan(nama_peternakan);
        this.setAlamat(alamat);
        this.setTelpon(telpon);
        this.setLatitude(latitude);
        this.setLongitude(longitude);
        this.setTernak_pedaging(ternak_pedaging);
        this.setTernak_perah(ternak_perah);
        this.setLayanan_aktif(layanan_aktif);
        this.setTgl_expired(tgl_expired);
        this.setJumlah_ternak(jumlah_ternak);
        this.setId_pegawai(id_pegawai);
        this.setNama_pemilik(nama_pemilik);
    }

    public String getId_peternakan() {
        return id_peternakan;
    }

    public void setId_peternakan(String id_peternakan) {
        this.id_peternakan = id_peternakan;
    }

    public String getId_pengguna() {
        return id_pengguna;
    }

    public void setId_pengguna(String id_pengguna) {
        this.id_pengguna = id_pengguna;
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

    public String getTelpon() {
        return telpon;
    }

    public void setTelpon(String telpon) {
        this.telpon = telpon;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public int getTernak_pedaging() {
        return ternak_pedaging;
    }

    public void setTernak_pedaging(int ternak_pedaging) {
        this.ternak_pedaging = ternak_pedaging;
    }

    public int getTernak_perah() {
        return ternak_perah;
    }

    public void setTernak_perah(int ternak_perah) {
        this.ternak_perah = ternak_perah;
    }

    public int getLayanan_aktif() {
        return layanan_aktif;
    }

    public void setLayanan_aktif(int layanan_aktif) {
        this.layanan_aktif = layanan_aktif;
    }

    public String getTgl_expired() {
        return tgl_expired;
    }

    public void setTgl_expired(String tgl_expired) {
        this.tgl_expired = tgl_expired;
    }

    public int getJumlah_ternak() {
        return jumlah_ternak;
    }

    public void setJumlah_ternak(int jumlah_ternak) {
        this.jumlah_ternak = jumlah_ternak;
    }

    public String getId_pegawai() {
        return id_pegawai;
    }

    public void setId_pegawai(String id_pegawai) {
        this.id_pegawai = id_pegawai;
    }

    public String getNama_pemilik() {
        return nama_pemilik;
    }

    public void setNama_pemilik(String nama_pemilik) {
        this.nama_pemilik = nama_pemilik;
    }
}
