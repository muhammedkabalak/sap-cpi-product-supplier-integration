import com.sap.gateway.ip.core.customdev.util.Message
import groovy.util.XmlSlurper
import groovy.xml.XmlUtil

def Message processData(Message message) {
    def body = message.getBody(String) as String
    def rootNode = new XmlSlurper().parseText(body)
    def product = rootNode.Product

    // Önce supplier bilgisi olup olmadığını kontrol et
    if (product.Supplier && product.Supplier.size() > 0) {

        // Mevcut SupplierID varsa sil
        if (product.SupplierID) {
            product.SupplierID.replaceNode {}
        }

        // Supplier bilgilerini Product içerisine ekle
        def supplier = product.Supplier
        product.appendNode(supplier.SupplierID)
        product.appendNode(supplier.ContactName)
        product.appendNode(supplier.ContactTitle)
        product.appendNode(supplier.CompanyName)
        product.appendNode(supplier.Address)
        product.appendNode(supplier.Phone)
        product.appendNode(supplier.PostalCode)
        product.appendNode(supplier.Country)

        // Orijinal Supplier node'unu sil
        supplier.replaceNode {}
    }

    def flatXML = XmlUtil.serialize(product)
    flatXML = flatXML.replace("""<?xml version="1.0" encoding="UTF-8"?>""", "")
    flatXML = flatXML.replace("""<?xml version='1.0' encoding='UTF-8'?>""", "")
    message.setBody(flatXML.trim())
    return message
}
