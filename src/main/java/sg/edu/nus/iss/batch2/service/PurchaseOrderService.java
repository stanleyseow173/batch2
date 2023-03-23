package sg.edu.nus.iss.batch2.service;

import java.io.StringReader;
import java.util.Arrays;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import sg.edu.nus.iss.batch2.model.Invoice;
import sg.edu.nus.iss.batch2.model.Item;
import sg.edu.nus.iss.batch2.model.Order;
import sg.edu.nus.iss.batch2.model.Quotation;
import sg.edu.nus.iss.batch2.model.ShippingAddress;

@Service
public class PurchaseOrderService {


    public static final String QUOTATION = "https://giant-nut-production.up.railway.app/quotation";

    public static final List<String> ITEMS = Arrays.asList("apple", "orange", "bread", "cheese", "chicken",
            "mineral_water", "instant_noodles");

    public boolean isInString(Item item) {
        return ITEMS.stream().anyMatch(v -> v.equals(item.getItem()));
    }

    public Invoice getInvoice(Order order, ShippingAddress shippingAddress) throws Exception{
        Invoice invoice = new Invoice();

        Quotation quotation = getQuotations(order);

        Float total = 0f;
        for (int i=0; i< order.getContents().size(); i++){
            String itemName = order.getContents().get(i).getItem();
            Integer itemQuantity = order.getContents().get(i).getQuantity();
            Float itemPrice = quotation.getQuotations().get(itemName);
            total += (itemQuantity*itemPrice);
        }
        invoice.setName(shippingAddress.getName());
        invoice.setAddress(shippingAddress.getAddress());
        invoice.setTotal(total);
        return invoice;
    }

    public Quotation getQuotations(Order order) throws Exception {
		JsonArrayBuilder arrBuilder = Json.createArrayBuilder(order.toItemNameList());
        //System.out.println("The string array builder build is : " + arrBuilder.build().toString());
        String bodStr = arrBuilder.build().toString();
        System.out.println("assigning to string : " + bodStr);

        //arrBuilder.build().toString()
        //String bodyStr = "['orange','bread','mineral_water']";
        String bodyStr = Arrays.asList("apple","orange","bread").toString();
        System.out.println("The bodyStr " + bodyStr);
		RequestEntity<String> req = RequestEntity.post(QUOTATION)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.body(bodStr);

		ResponseEntity<String> resp;
		RestTemplate template = new RestTemplate();
		try {
			resp = template.exchange(req, String.class);
		} catch (Exception ex) {
			throw ex;
		}

        ObjectMapper mapper = new ObjectMapper();

		String payload = resp.getBody();
		JsonReader reader = Json.createReader(new StringReader(payload));
		JsonObject json = reader.readObject();

		Quotation quotation = new Quotation();
        quotation = mapper.readValue(payload, Quotation.class);
		//quotation.setQuoteId(json.getString("quoteId"));

        //System.out.println("Received Output.....");
        //System.out.println("Json String to decode into map is....." + json.getString("quotations"));
        System.out.println("Quotation ID is:" + quotation.getQuoteId());
        //System.out.println("Quotation Map is" + quotation.toString());
        // json.getJsonArray("quotations").stream()
		// 	.forEach(i -> {
		// 		quotation.addQuotation(i., (float)i.getJsonNumber("unitPrice").doubleValue());
		// 	});

		// json.getJsonArray("quotations").stream()
		// 	.map(i -> i.asJsonObject())
		// 	.forEach(i -> {
		// 		quotation.addQuotation(i.getString("item"), (float)i.getJsonNumber("unitPrice").doubleValue());
		// 	});

		return quotation;
	}
}
