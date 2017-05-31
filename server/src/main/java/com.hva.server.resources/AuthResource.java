package com.hva.server;

import com.google.gson.Gson;
import com.hva.server.domain.Account;
import com.hva.server.domain.AccountCodeExpiredException;
import com.hva.server.domain.AccountRepository;
import com.hva.server.infrastructure.SpotifyService;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import java.net.URI;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("auth")
public class AuthResource {

    private Gson gson = new Gson();
    
    @Inject
    private SpotifyService _spotify;
    
    @Inject
    private AccountRepository _accountRepo;
    
    @GET
    @Path("/request")
    @Produces(MediaType.APPLICATION_JSON)
    public String authcode(@PathParam("clientcode") String authCode) {
        return gson.toJson(_spotify.GetRequest());
    }
    
    @GET
    @Path("/redirect")
    @Produces(MediaType.APPLICATION_JSON)
    public Response SpotifyRequestCallback(@QueryParam("code") String clientCode, @QueryParam("state") String clientState) {
        try {
            Account account = new Account();
            account.code = clientCode;
            account.state = clientState;
            _accountRepo.CreateOrUpdate(account);
            _spotify.GetAPI(account);
        } catch (AccountCodeExpiredException e) {
            return Response.status(Response.Status.UNAUTHORIZED).link("/myapp/auth/request", "request").build();
        } catch (Exception e) {
            Logger.getLogger(AuthResource.class.getCanonicalName()).severe(e.getMessage());
            e.printStackTrace();
            return Response.serverError().build();
        }
        
        return Response.ok("Authtoken created!", MediaType.APPLICATION_JSON).build();
    }
}
