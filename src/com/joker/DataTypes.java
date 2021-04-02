package com.joker;

public class DataTypes {

    private String ID;
    private String name;
    private String supplier;
    private int stock;
    private String location;
    private double price;
    private int quantity;
    private String paymentType;
    private String cardNumber;
    private double amount;

    public String getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public String getSupplier() {
        return supplier;
    }

    public int getStock() {
        return stock;
    }

    public String getLocation() {
        return location;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public double getAmount() {
        return amount;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}