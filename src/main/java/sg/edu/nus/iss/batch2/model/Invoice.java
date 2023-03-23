package sg.edu.nus.iss.batch2.model;

import java.util.UUID;

public class Invoice {
    private String invoice_id;
    private String name;
    private String address;
    private float total;

    public String getInvoice_id() {
        return invoice_id;
    }
    public void setInvoice_id(String invoice_id) {
        this.invoice_id = invoice_id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public float getTotal() {
        return total;
    }
    public void setTotal(float total) {
        this.total = total;
    }

    public Invoice() {
        this.setInvoice_id(UUID.randomUUID().toString().substring(0, 8));
    }

    
}
