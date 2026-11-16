/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.kuis_pbo_hotel;

/**
 *
 * @author Annisa
 */
public class Tamu extends Pengguna {
    private static final long serialVersionUID = 1L;

    public Tamu(String id, String nama) {
        super(id, nama);
    }

    @Override
    public String getInfoPeran() {
        return "Tamu Hotel (" + getId() + ") - " + getNama();
    }
}
