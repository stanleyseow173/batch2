package sg.edu.nus.iss.batch2.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Order {

    private List<Item> contents = new LinkedList<Item>();

    public List<Item> getContents() {
        return contents;
    }

    public void setContents(List<Item> contents) {
        this.contents = contents;
    }

    public void add(Item item) {
        int found = 0;
        for (int i = 0; i < this.contents.size(); i++) {
            Item iterItem = this.contents.get(i);
            if (iterItem.getItem().equals(item.getItem())) {
                int sum = this.contents.get(i).getQuantity() + item.getQuantity();
                this.contents.get(i).setQuantity(sum);
                found = 1;
            }
        }
        if (found == 0) {
            contents.add(item);
        }
    }

    public List<String> toItemNameList(){
        List<String> listName = new ArrayList<String>();
        for (int i=0; i< this.contents.size(); i++){
            String itemName = this.contents.get(i).getItem();
            listName.add(itemName);
        }
        return listName;
    }

}
