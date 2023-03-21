package sg.edu.nus.iss.batch2.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import sg.edu.nus.iss.batch2.model.Item;

@Service
public class PurchaseOrderService {

    public static final List<String> ITEMS = Arrays.asList("apple", "orange", "bread", "cheese", "chicken",
            "mineral_water", "instant_noodles");

    public boolean isInString(Item item) {
        return ITEMS.stream().anyMatch(v -> v.equals(item.getItem()));
    }
}
