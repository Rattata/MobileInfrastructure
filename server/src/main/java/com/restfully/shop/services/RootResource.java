/*
 * HvA licences apply
 */
package com.restfully.shop.services;

/**
 *
 * @author siege
 */
@javax.ws.rs.Path("API")
public class RootResource {

    public RootResource() {
    }

    @javax.ws.rs.core.Context
    private javax.ws.rs.core.HttpHeaders httpHeaders;

    @javax.annotation.PostConstruct
    private void myPostConstruct() {
        
        /* read from the httpHeaders if required */
    }

    @javax.ws.rs.GET
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
    public String get() {
        return "Hello world!";
    }
    
}
