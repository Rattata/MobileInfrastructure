package com.hva.server;

import com.hva.server.domain.Account;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("myresource")
public class MyResource {

    @Inject
    private ConnectionSource _source;
    
    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getIt() {
        Dao<Account, Integer> dao;
        try {
            dao = DaoManager.createDao(_source, Account.class);
            TableUtils.createTableIfNotExists(_source, Account.class);
        } catch (Exception e) {
            //maek table
        }
        return "Got it!";
    }
}
