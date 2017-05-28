/*
 * HvA licences apply
 */
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
 * @author siege
 */
@Path("/customer")
public interface AccountResource {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public StreamingOutput getAccount();
    
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public StreamingOutput getAccount(@PathParam("id") int id);
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createAccount(InputStream is);
    
    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void updateAccount(@PathParam("id") int id, InputStream is);
}
