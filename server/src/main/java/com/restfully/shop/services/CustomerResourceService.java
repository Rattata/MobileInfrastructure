package com.restfully.shop.services;

import com.restfully.shop.domain.Customer;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * REST Web Service
 *
 * @author boofk
 */
public class CustomerResourceService implements CustomerResource {

    @Context
    private UriInfo context;
    
    // Added from book:
    private Map<Integer, Customer> customerDB = new ConcurrentHashMap<Integer, Customer>();
    private AtomicInteger idCounter = new AtomicInteger();

    /**
     * Creates a new instance of CustomerResource
     */
    public CustomerResourceService() {
        
    }

    /**
     * Retrieves representation of an instance of com.mycompany.mavenproject_fbo.CustomerResourceService
     * @return an instance of com.mycompany.mavenproject_fbo.Customer
     */
    public StreamingOutput getCustomer() {
        final Customer customer = customerDB.get(1);
        if (customer == null)
        {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return new StreamingOutput() {
            public void write(OutputStream outputStream) 
                    throws IOException, WebApplicationException {
                outputCustomer(outputStream, customer);
            }
        };
    }
    public StreamingOutput getCustomer(int id) {
        final Customer customer = customerDB.get(id);
        if (customer == null)
        {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return new StreamingOutput() {
            public void write(OutputStream outputStream) 
                    throws IOException, WebApplicationException {
                outputCustomer(outputStream, customer);
            }
        };
    }

    public Response createCustomer(InputStream is) {
        Customer customer = readCustomer(is);
        customer.setId(idCounter.incrementAndGet());
        customerDB.put(customer.getId(), customer);
        System.out.println("Created customer " + customer.getId());
        return Response.created(URI.create("/customer/" + customer.getId())).build();
    }
    
    /**
     * PUT method for updating or creating an instance of CustomerResourceService
     * @param content representation for the resource
     */
    public void updateCustomer(int id, InputStream is) {
        Customer update = readCustomer(is);
        Customer current = customerDB.get(id);
        if (current == null)
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        current.setFirstName(update.getFirstName());
        current.setLastName(update.getLastName());
        current.setStreet(update.getStreet());
        current.setState(update.getState());
        current.setZip(update.getZip());
        current.setCountry(update.getCountry());
    }

    protected void outputCustomer(OutputStream os, Customer c) 
            throws IOException {
        PrintStream writer = new PrintStream(os);
        writer.println("<customer id=\"" + c.getId() + "\">");
        writer.println("   <first-name>" + c.getFirstName() + "</first-name>");
        writer.println("   <last-name>" + c.getLastName() + "</last-name>");
        writer.println("   <street>" + c.getStreet() + "</street>");
        writer.println("   <state>" + c.getState() + "</state>");
        writer.println("   <zip>" + c.getZip() + "</zip>");
        writer.println("   <country>" + c.getCountry() + "</country>");
        writer.println("</customer>");
    }
    
    protected Customer readCustomer(InputStream is) {
        try {
            DocumentBuilder builder =
                    DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(is);
            Element root = doc.getDocumentElement();
            
            Customer cust = new Customer();
            if (root.getAttribute("id") != null
                    && !root.getAttribute("id").trim().equals("")) {
                cust.setId(Integer.valueOf(root.getAttribute("id")));
            }
            NodeList nodes = root.getChildNodes();
            for (int i=0; i < nodes.getLength(); i++) {
                Element element = (Element)nodes.item(i);
                if (element.getTagName().equals("first-name")) {
                    cust.setFirstName((element.getTextContent()));
                }
                else if (element.getTagName().equals("last-name")) {
                    cust.setLastName((element.getTextContent()));
                }
                else if (element.getTagName().equals("street")) {
                    cust.setStreet((element.getTextContent()));
                }
                else if (element.getTagName().equals("city")) {
                    cust.setCity((element.getTextContent()));
                }
                else if (element.getTagName().equals("state")) {
                    cust.setState((element.getTextContent()));
                }
                else if (element.getTagName().equals("zip")) {
                    cust.setZip((element.getTextContent()));
                }
                else if (element.getTagName().equals("country")) {
                    cust.setCountry((element.getTextContent()));
                }
            }
        return cust;
        }
        catch (Exception ex) {
            throw new WebApplicationException(ex, Response.Status.BAD_REQUEST);
        }
    }
}
