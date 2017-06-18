/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hva.server.resources;

import com.google.gson.Gson;
import com.hva.server.AuthResource;
import com.hva.server.domain.*;
import com.hva.server.infrastructure.SpotifyService;
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
 *
 * @author siege
 */
@Path("album")
public class AlbumResource {

    @Inject
    AccountRepository _accountRepo;

    @Inject
    SpotifyService _spotify;
    private Gson gson = new Gson();

    @GET()
    @Path("barcode")
    @Produces(MediaType.APPLICATION_JSON)
    public Response BarcodeQuery(@QueryParam("barcode") String barcode, @QueryParam("userid") int userid) {
        try {
            Account account = _accountRepo.Get(userid);
            return Response.ok(gson.toJson(_spotify.queryAlbums(account, barcode)), MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            Logger.getLogger(AlbumResource.class.getCanonicalName()).severe(e.getMessage());
            e.printStackTrace();
        }
        return Response.serverError().build();
    }
    
    
    @GET()
    @Path("artist/{artist}/album/{album}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response AlbumArtistQuery(@PathParam("artist") String artist, @PathParam("album") String album, @QueryParam("userid") int userid) {
        try {
            Account account = _accountRepo.Get(userid);
            return Response.ok(gson.toJson(_spotify.GetSpotifyUrl(account,artist,album)), MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            Logger.getLogger(AlbumResource.class.getCanonicalName()).severe(e.getMessage());
            e.printStackTrace();
        }
        return Response.serverError().build();
    }
}
