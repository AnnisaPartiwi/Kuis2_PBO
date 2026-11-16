/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.kuis_pbo_hotel;

/**
 *
 * @author Annisa
 */
import java.io.Serializable;

public class Kamar implements Serializable {
    private static final long serialVersionUID = 1L;
    private String kodeKamar;
    private String tipeKamar;
    private int kuota;

    public Kamar(String kodeKamar, String tipeKamar, int kuota) {
        this.kodeKamar = kodeKamar;
        this.tipeKamar = tipeKamar;
        this.kuota = kuota;
    }

    public String getKodeKamar() {
        return kodeKamar;
    }

    public String getTipeKamar() {
        return tipeKamar;
    }

    public int getKuota() {
        return kuota;
    }

    public void kurangiKuota(int jumlah) throws TransaksiException {
        if (jumlah <= 0) {
            throw new TransaksiException("Jumlah pemesanan harus lebih dari 0.");
        }
        if (this.kuota < jumlah) {
            throw new TransaksiException("Kuota kamar " + tipeKamar + " (" + kodeKamar + ") tidak mencukupi, Sisa: " + this.kuota + ", Diminta: " + jumlah);
        }
        this.kuota -= jumlah;
    }
}