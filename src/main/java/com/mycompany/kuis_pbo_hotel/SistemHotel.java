package com.mycompany.kuis_pbo_hotel;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Annisa
 */
import java.io.*;
import java.util.*;

public class SistemHotel implements Serializable {
    private static final long serialVersionUID = 1L;
    private String namaHotel;
    private HashMap<String, Tamu> daftarTamu;
    private ArrayList<Kamar> daftarKamar;
    private ArrayList<Transaksi> daftarTransaksi;

    public SistemHotel(String namaHotel) {
        this.namaHotel = namaHotel;
        this.daftarTamu = new HashMap<>();
        this.daftarKamar = new ArrayList<>();
        this.daftarTransaksi = new ArrayList<>();
    }

    public void tambahTamu(String id, String nama) {
        daftarTamu.put(id, new Tamu(id, nama));
    }

    public void tambahKamar(String kode, String tipe, int kuota) {
        daftarKamar.add(new Kamar(kode, tipe, kuota));
    }

    private Kamar cariKamar(String kode) {
        for (Kamar k : daftarKamar) {
            if (k.getKodeKamar().equalsIgnoreCase(kode)) {
                return k;
            }
        }
        return null;
    }

    public void jalankanTransaksiDariFile(String namaFile) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(namaFile));
        String line;

        while ((line = reader.readLine()) != null) {
            if (line.trim().isEmpty()) continue;
            
            String[] daftarRecord = line.split("\\|");

            for (String record : daftarRecord) {
                record = record.trim();
                if (record.isEmpty()) continue;

                try {
                    String[] bagian = record.split("/");
                    if (bagian.length < 3) {
                        throw new TransaksiException("Format data rusak pada: " + record);
                    }
                    String idTamu = bagian[0].replaceFirst("ID-", "").trim();
                    String kodeKamar = bagian[1].replaceFirst("KD-", "").trim();
                    int jumlah = Integer.parseInt(bagian[2].replaceFirst("JML-", "").trim());

                    prosesTransaksi(idTamu, kodeKamar, jumlah);
                } catch (NumberFormatException e) {
                    System.out.println("[TRANSAKSI GAGAL] Format angka jumlah tidak valid pada: " + record);
                } catch (TransaksiException e) {
                    System.out.println("[TRANSAKSI GAGAL] " + e.getMessage());
                }
            }
        }
        reader.close();
    }
    private void prosesTransaksi(String idTamu, String kodeKamar, int jumlah) throws TransaksiException {
        Tamu tamu = daftarTamu.get(idTamu);
        if (tamu == null) {
            throw new TransaksiException("ID Tamu '" + idTamu + "' tidak terdaftar di sistem");
        }
        
        Kamar kamar = cariKamar(kodeKamar);
        if (kamar == null) {
            throw new TransaksiException("Kode Kamar '" + kodeKamar + "' tidak ditemukan");
        }
        
        kamar.kurangiKuota(jumlah);
        Transaksi trx = new Transaksi(tamu, kamar, jumlah);
        daftarTransaksi.add(trx);
        System.out.println("[BERHASIL] " + tamu.getInfoPeran() + " memesan " + jumlah + "x " + kamar.getTipeKamar() + " (Sisa kuota: " + kamar.getKuota() + ")");
    }
    
    public void simpanLaporanAkhir() {
        System.out.println("\n--- MENYIMPAN LAPORAN AKHIR & SERIALIZATION ---");
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("archive_mem.bin"))) {
            oos.writeObject(this);
            System.out.println("[OK] Status sistem berhasil diserialisasi ke 'archive_mem.bin'");
        } catch (IOException e) {
            System.out.println("[ERROR SERIALIZATION] Gagal menyimpan binary: " + e.getMessage());
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("laporan.txt"))) {
            writer.write("=== LAPORAN SISA KUOTA KAMAR - " + namaHotel.toUpperCase() + " ===");
            writer.newLine();
            for (Kamar k : daftarKamar) {
                writer.write(k.getKodeKamar() + " | " + k.getTipeKamar() + " | Sisa Kuota: " + k.getKuota());
                writer.newLine();
            }
            System.out.println("[OK] Laporan sisa kuota berhasil ditulis ke 'laporan.txt'");
        } catch (IOException e) {
            System.out.println("[ERROR FILE I/O] Gagal menulis laporan.txt: " + e.getMessage());
        }
    }
}