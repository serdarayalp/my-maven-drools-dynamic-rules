package de.mydomain.drools.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Product {

    private final String name;
    private double price;
    private final Date dueDate;

    public Product(String name, double price, String dueDate) throws ParseException {
        this.name = name;
        this.price = price;
        this.dueDate = (new SimpleDateFormat("dd/MM/yyyy")).parse(dueDate);
    }

    public Product(String name, double price, Date dueDate) {
        this.name = name;
        this.price = price;
        this.dueDate = dueDate;
    }

    public void discount(double percent) {
        price = (price - ((percent * price) / 100));
    }

    @Override
    public String toString() {
        StringBuilder me = new StringBuilder("[" + this.getClass().getName());
        me.append(" | name = ");
        me.append(name);
        me.append(" | price = ");
        me.append(price);
        me.append(" | dueDate = ");
        me.append(((dueDate == null) ? null : (new SimpleDateFormat("dd/MM/yyyy")).format(dueDate)));
        me.append("]");

        return me.toString();
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public Date getDueDate() {
        return dueDate;
    }
}