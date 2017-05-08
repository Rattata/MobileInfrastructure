package com.restfully.shop.services;

import java.io.InputStream;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

/**
 *
 * @author boofk
 */
@Path("/customer")
public interface CustomerResource {
    
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public StreamingOutput getCustomer();
    
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_XML)
    public StreamingOutput getCustomer(@PathParam("id") int id);
    
    @POST
    @Consumes(MediaType.APPLICATION_XML)
    public Response createCustomer(InputStream is);
    
    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_XML)
    public void updateCustomer(@PathParam("id") int id, InputStream is);
    
    
}
