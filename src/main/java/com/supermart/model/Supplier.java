/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.supermart.model;

/**
 *
 * @author pabodini
 */

public class Supplier {

    private int supplierId;
    private String name;

    public Supplier(int supplierId, String name) {
        this.supplierId = supplierId;
        this.name = name;
    }

    public int getSupplierId() {
        return supplierId;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}