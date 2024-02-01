package dev.shann.mcproductservice.utility;

import dev.shann.mcproductservice.model.Product;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class MessageReceiver {

//    @JmsListener(destination = "mailbox", containerFactory = "messageFactory")
//    public void receiveMessage(Product product){
//        System.out.println("Received <" + product + ">");
//    }
}
