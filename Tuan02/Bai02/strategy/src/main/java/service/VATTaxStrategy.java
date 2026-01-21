package service;

public class VATTaxStrategy implements TaxStrategy {
    public double calculateTax(double price) {
        return price * 0.1;
    }
}