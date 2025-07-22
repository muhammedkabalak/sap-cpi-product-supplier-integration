# 🛠️ SAP CPI: Product-Supplier Data Enrichment Integration Flow

This project demonstrates an SAP Cloud Integration (CPI) scenario that **enriches product data with supplier details** using a combination of **Content Modifier**, **Content Enricher**, and a custom **Groovy Script**. It consumes data from the publicly available Northwind OData API and dynamically enhances product records for further processing or delivery.

---

## 📌 Project Overview

- **Scenario**: 
  You want to send a list of products and enrich them with corresponding supplier details dynamically.

- **Use Case**:
  Ideal for B2B integrations where product data from one system must be complemented with metadata (e.g., supplier info) fetched from another service.

---

## ⚙️ Technologies & Tools

| Component       | Usage                                         |
|----------------|-----------------------------------------------|
| SAP Integration Suite | Main platform for the integration flow |
| Groovy Script   | Used for business logic and conditional routing |
| OData Adapter   | Consumes data from Northwind API |
| XML & XPath     | Used for filtering, parsing, and enrichment |
| GitHub          | Source control for the project artifacts |

---

## 📁 Project Structure

```
sap-cpi-product-supplier-integration/
├── src/
│   └── main/
│       └── resources/
│           └── script/
│               └── needsReorder.groovy      ← Main logic
├── META-INF/
│   └── manifest.xml                         ← Integration project metadata
├── .project                                  ← Eclipse project file
├── metainfo.prop                             ← SAP CPI specific info
└── README.md                                 ← This file
```

---

## 🔄 Integration Flow Description

### 🧱 Main Steps

1. **Start** – Flow begins with a manual or scheduled trigger.
2. **OData Receiver** – Fetches `Product` entities from `https://services.odata.org/V2/Northwind/Northwind.svc/Products`.
3. **Content Modifier** – Extracts `SupplierID` using XPath.
4. **Content Enricher** – Looks up `/Suppliers/Supplier` where `SupplierID` matches and injects supplier details into the product.
5. **Groovy Script** – Performs logic like checking if `UnitsInStock <= ReorderLevel` and sets a property `needsReorder`.
6. **Router** – Routes based on `needsReorder` flag.
7. **Final Receiver** – Can be Email, HTTP, or any target system.

---

## 🧠 Groovy Logic

### File: `needsReorder.groovy`

```groovy
import com.sap.gateway.ip.core.customdev.util.Message
import groovy.util.XmlSlurper

def Message processData(Message message) {
    def body = message.getBody(java.lang.String) as String
    def product = new XmlSlurper().parseText(body)

    def reorderLevel = product.Product.ReorderLevel.text().toInteger()
    def unitsInStock = product.Product.UnitsInStock.text().toInteger()

    if (unitsInStock <= reorderLevel) {
        message.setProperty("needsReorder", "true")
    } else {
        message.setProperty("needsReorder", "false")
    }

    return message
}
```

---

## 🌐 External API Reference

- **Northwind OData Service**  
  `https://services.odata.org/V2/Northwind/Northwind.svc/`

  > Example: `https://services.odata.org/V2/Northwind/Northwind.svc/Products?$format=json`

---

## 🚧 Error Handling

- **HTTP 400**: Usually caused by malformed OData queries. Ensure proper `$filter` and `$select` syntax.
- **Authentication Errors**: Ensure correct authentication setup (if using mail or external systems).
- **Content Enricher Issues**: Ensure both sides of the enrichment path (`Original` and `Lookup`) are configured with correct `XPath` and key mappings.

---

## 📬 Email Notification (Optional)

You can configure a Mail adapter at the end of the flow to send enriched products that need reorder.  
Example error to handle:
```
javax.mail.AuthenticationFailedException: Username and Password not accepted.
```
Ensure you:
- Enable "Less secure apps" or use App Passwords.
- Use the correct SMTP settings.

---

## 🧾 License

This project is provided for educational and demonstration purposes only.

---

## 👨‍💻 Author

**Muhammed Kabalak**  
SAP CPI Integrator  
📧 mo.kabalakk@gmail.com  
🔗 GitHub: [@muhammedkabalak](https://github.com/muhammedkabalak)

---

## 🌟 Feedback

Feel free to fork, use, and improve. If you encounter any issues or want to contribute, open a pull request or issue. Happy integrating! 🚀
