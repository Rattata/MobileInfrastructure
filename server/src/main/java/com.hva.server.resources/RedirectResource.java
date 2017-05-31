package com.hva.server;

import com.hva.server.domain.Account;
import com.hva.server.infrastructure.SpotifyService;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("spotifycallback")
public class RedirectResource {

    @Inject
    private ConnectionSource _source;
    
    @Inject
    private SpotifyService _spotify;
    
    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
    //https://example.com/callback?code=NApCCg..BkWtQ&state=profile%2Factivity
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String SpotifyRequestCallback(@QueryParam("code") String clientCode, @QueryParam("state") String clientState) {
        Dao<Account, Integer> dao;
        try {
            dao = DaoManager.createDao(_source, Account.class);
            TableUtils.createTableIfNotExists(_source, Account.class);
            
        } catch (Exception e) {
            Logger.getLogger(AuthResource.class.getCanonicalName()).severe(e.getMessage());
        }
        //request refresh token
        return "Got it!";
    }
    
    @GET
    @Path("auth/{clientcode}")
    @Produces(MediaType.APPLICATION_JSON)
    public String authcode(@PathParam("clientcode") String authCode) {
        return "";
    }
}
