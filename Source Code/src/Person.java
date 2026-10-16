public class Person {
    private String nim;
    private String nama;
    private int angkatan;
    private String jenisKelamin;

    public Person(String nim, String nama, int angkatan, String jenisKelamin){
        this.nim = nim;
        this.nama = nama;
        this.angkatan = angkatan;
        this.jenisKelamin = jenisKelamin;
    }

    public void setNim(String nim){ this.nim = nim;}
    public String getNim(){ return this.nim;}

    public void setNama(String nama){ this.nama = nama;}
    public String getNama(){return this.nama;}

    public void setAngkatan(int angkatan){ this.angkatan = angkatan;}
    public int getAngkatan(){return this.angkatan;}

    public void setJenisKelamin(String jenisKelamin){ this.jenisKelamin = jenisKelamin; }
    public String getJenisKelamin(){return this.jenisKelamin; }
}
