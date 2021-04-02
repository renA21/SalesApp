package com.joker;

public class SalesData {

    private final String ID;
    private final String name;
    private final String supplier;
    private final String sold;
    private final String stock;

    public SalesData(String ID, String name, String supplier, String sold, String stock) {
        this.ID = ID;
        this.name = name;
        this.supplier = supplier;
        this.sold = sold;
        this.stock = stock;
    }

    public String getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public String getSupplier() {
        return supplier;
    }

    public String getSold() {
        return sold;
    }

    public String getStock() {
        return stock;
    }
}
