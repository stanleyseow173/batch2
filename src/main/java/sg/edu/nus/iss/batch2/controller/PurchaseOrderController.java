package sg.edu.nus.iss.batch2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import sg.edu.nus.iss.batch2.model.Invoice;
import sg.edu.nus.iss.batch2.model.Item;
import sg.edu.nus.iss.batch2.model.Order;
import sg.edu.nus.iss.batch2.model.Quotation;
import sg.edu.nus.iss.batch2.model.ShippingAddress;
import sg.edu.nus.iss.batch2.service.PurchaseOrderService;

@Controller
public class PurchaseOrderController {

    @Autowired
    private PurchaseOrderService poSvc;

    @GetMapping(path = { "/", "/index.html" })
    public String getView1(Model m, HttpSession sess) {
        m.addAttribute("item", new Item());
        m.addAttribute("order", getOrder(sess));
        return "view1";
    }

    @PostMapping(path = { "/item" })
    public String postItem(Model m, HttpSession sess, @Valid Item item, BindingResult bindings) {
        Order order = getOrder(sess);
        m.addAttribute("order", order);

        if (bindings.hasErrors()) {
            return "view1";
        }

        if (!poSvc.isInString(item)) {
            FieldError error = new FieldError("item", "item", "We do not stock %s".formatted(item.getItem()));
            bindings.addError(error);
            return "view1";
        }

        order.add(item);
        m.addAttribute("item", new Item());

        return "view1";
    }

    @GetMapping(path = {"/shippingaddress"})
    public String getView2(Model m, HttpSession sess){
        Order o = (Order) sess.getAttribute("order");
        m.addAttribute("order", o);
        try{
            if(!(o.getContents().isEmpty())){
                ShippingAddress shipAdd = new ShippingAddress();
                m.addAttribute("shipAdd", shipAdd);
                return "view2";
            }
            m.addAttribute("item", new Item());
            return "view1";
        }catch(NullPointerException e){
            e.printStackTrace();
            m.addAttribute("item", new Item());
            return "view1";
        }
        
    }

    @PostMapping(path = {"/checkout"})
    public String getView3(Model m, HttpSession sess, @ModelAttribute("shipAdd") @Valid ShippingAddress shipAddress, BindingResult bindings){
        
        
        if(bindings.hasErrors()){
            //m.addAttribute("shipAdd", shipAddress);
            return "view2";
        }

        Order order = (Order) sess.getAttribute("order");

        Invoice invoice;
        System.out.println("Before Quotation.....");
        try {
            System.out.println("Went through try block");
			invoice = poSvc.getInvoice(order, shipAddress);
		} catch (Exception ex) {
			ex.printStackTrace();
			m.addAttribute("error", ex.getMessage());
			return "view2";
		}
        m.addAttribute("invoice", invoice);

        sess.invalidate();
        return "view3";
    }


    private Order getOrder(HttpSession sess) {
        Order order = (Order) sess.getAttribute("order");
        if (null == order) {
            order = new Order();
            sess.setAttribute("order", order);
        }
        return order;
    }
}
