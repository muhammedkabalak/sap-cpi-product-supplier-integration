import com.sap.gateway.ip.core.customdev.util.Message;
import groovy.util.XmlSlurper ;



def Message processData(Message message) {
       def body = message.getBody(java.lang.String) as String;
       def reorderLevel;
       def unitsInStock;
       def product= new XmlSlurper().parseText(body);
       reorderLevel=product.Product.ReorderLevel.text().toInteger();
       unitsInStock=product.Product.UnitsInStock.text().toInteger();
       if(unitsInStock<=reorderLevel){
          message.setProperty("needsReorder", "true");
       }
       else{
           message.setProperty("needsReorder", "false");
       }
       return message;
}