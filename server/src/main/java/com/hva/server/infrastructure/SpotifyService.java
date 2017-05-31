/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hva.server.infrastructure;

import com.hva.server.domain.Account;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.dao.Dao;
import java.util.logging.Logger;
import javax.inject.Inject;

/**
 *
 * @author siege
 */
public class SpotifyService {

     
    private final String ClientId = "97b2e28c509d450ba4be16b6a02c048c";
    private final String ClientSecret = "e34f7b0c3e1d4bf0ae596d7007945e44";
    private final String Code = "<insert code>";
    private final String callBack = "http://localhost:8080/myapp/spotifycallback";
    
    private Dao<Account, String> accountDao;
    
    @Inject
    public SpotifyService(ConnectionSource conn) {
        try {
            accountDao = DaoManager.createDao(conn, Account.class);
        } catch (Exception e) {
              Logger.getLogger(SpotifyService.class.getCanonicalName()).severe(e.getMessage());
        }
    }
    
    public AuthenticationRequest GetRequest(){
        AuthenticationRequest request = new AuthenticationRequest();
        request.client_id = ClientId;
        request.redirect_uri = callBack;
        request.response_type = "code";
        request.scope = "user-read-private";
        request.state = "unused";
        return request;
    }
    
    public String getAuthcode(String spotifyUserId){
        return "";
    }
    
    
    public String queryAlbums(String spotifyUserId, String barcode){
        //q=upc:{barcode}&type=album 
        return "";
    }
    
    
    public String addAlbumsToUser(String spotifyUserId, String barcode){
        return "";
    }
    

}
