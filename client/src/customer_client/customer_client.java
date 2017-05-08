package customer_client;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

/**
 *
 * @author boofk
 */
public class customer_client {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        final int CREATED = 201;
        final int NO_CONTENT = 204;
        String myHost = "http://localhost:8080/";
        String myPart = "maven_customer1/services";
        String myLocation =  myHost + myPart + "/" + "customer";
        
        Client client = ClientBuilder.newClient();
        try {
            System.out.println("*** Creating new customer ***");
            String xml = "<customer>"
                    + "<first-name>Courtney</first-name>"
                    + "<last-name>Olsen</last-name>"
                    + "<street>356 Sibley street</street>"
                    + "<city>Minneapolis</city>"
                    + "<state>MN</state>"
                    + "<zip>76767</zip>"
                    + "<country>USA</country>"
                    + "</customer>";
            String target = myLocation;
            System.out.println("target=" + target);
            Response response = client.target(target).request().post(Entity.xml(xml));
            if (response.getStatus() != CREATED) 
                throw new RuntimeException("Failed to create!\n" + response.toString());
            // Check the returned info:
            String location = response.getLocation().toString();
            System.out.println(response.toString());
            System.out.println("location: " + location);
            String custIndex = location.substring(location.lastIndexOf('/') + 1);
            System.out.println("custIndex=" + custIndex);
            // Close:
            response.close();
            

            System.out.println("*** GET Created Customer ***");
            target = myLocation + "/" + custIndex;
            System.out.println("target=" + target);
            String customer = client.target(target).request().get(String.class);
            System.out.println(customer);

            
            String updateCustomer = "<customer>"
                    + "<first-name>Courtney Helen</first-name>"
                    + "<last-name>Rice-Olsen</last-name>"
                    + "<street>356 Sibley street</street>"
                    + "<city>Minneapolis</city>"
                    + "<state>MN</state>"
                    + "<zip>76767</zip>"
                    + "<country>USA</country>"
                    + "</customer>";
            target = myLocation + "/" + custIndex;
            System.out.println("target=" + target);
            response = client.target(target).request().put(Entity.xml(updateCustomer));
            if (response.getStatus() != NO_CONTENT)
                throw new RuntimeException("Failed to update!\n" + response.toString());
            response.close();
            
            
            System.out.println("*** After update ***");
            String customerUpd = client.target(target).request().get(String.class);
            System.out.println(customerUpd);
        } finally {
            client.close();
        }
    }

}
