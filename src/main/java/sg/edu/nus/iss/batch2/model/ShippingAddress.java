package sg.edu.nus.iss.batch2.model;

import org.springframework.stereotype.Component;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


public class ShippingAddress {
    
    @NotNull(message="Cannot be empty")
    @NotEmpty(message="Cannot be empty")
    @Size(min=2, message ="Size must be at least 2")
    private String name;
    
    @NotNull(message="Cannot be empty")
    @NotEmpty(message="Cannot be empty")
    private String address;

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

    
}
