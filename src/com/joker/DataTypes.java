package com.joker;

/**
 * Handles the organization of all possible types of data in Inventory, Sales and Transactions combined in an array.
 * Stores the type of data based on the declared variables when it's being instantiated from another class as an object.
 */
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

    /**
     * Getter for ID
     * @return get ID from ArrayList
     */
    public String getID() {
        return ID;
    }

    /**
     * Getter for Name
     * @return get name from ArrayList
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for Supplier
     * @return get supplier from ArrayList
     */
    public String getSupplier() {
        return supplier;
    }

    /**
     * Getter for Stock
     * @return get stock from ArrayList
     */
    public int getStock() {
        return stock;
    }

    /**
     * Getter for Location
     * @return get location from ArrayList
     */
    public String getLocation() {
        return location;
    }

    /**
     * Getter for Price
     * @return get price from ArrayList
     */
    public double getPrice() {
        return price;
    }

    /**
     * Getter for Quantity
     * @return get quantity from ArrayList
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Getter for Payment Type
     * @return get paymentType from ArrayList
     */
    public String getPaymentType() {
        return paymentType;
    }

    /**
     * Getter for Credit Card Number
     * @return get cardNumber from ArrayList
     */
    public String getCardNumber() {
        return cardNumber;
    }

    /**
     * Getter for Amount
     * @return get amount from ArrayList
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Setter for ID
     * @param ID set ID to ArrayList
     */
    public void setID(String ID) {
        this.ID = ID;
    }

    /**
     * Setter for Name
     * @param name set name to ArrayList
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Setter for Supplier
     * @param supplier set supplier to ArrayList
     */
    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    /**
     * Setter for Stock
     * @param stock set stock to ArrayList
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * Setter for Location
     * @param location set location to ArrayList
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Setter for Price
     * @param price set price to ArrayList
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Setter for Quantity
     * @param quantity set quantity to ArrayList
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Setter for Payment Type
     * @param paymentType set paymentType to ArrayList
     */
    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    /**
     * Setter for Credit Card Number
     * @param cardNumber set cardNumber to ArrayList
     */
    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    /**
     * Setter for Amount
     * @param amount set amount to ArrayList
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }
}