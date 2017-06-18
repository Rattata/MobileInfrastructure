/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hva.server.infrastructure;

import com.google.common.util.concurrent.SettableFuture;
import com.google.gson.Gson;
import com.hva.server.AuthResource;
import com.hva.server.domain.Account;
import com.hva.server.domain.AccountCodeExpiredException;
import com.hva.server.domain.AccountRepository;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.dao.Dao;
import com.wrapper.spotify.Api;
import com.wrapper.spotify.models.AuthorizationCodeCredentials;
import com.wrapper.spotify.models.Page;
import com.wrapper.spotify.models.RefreshAccessTokenCredentials;
import com.wrapper.spotify.models.SimpleAlbum;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

/**
 *
 * @author siege
 */
public class SpotifyService {

    private final String ClientId = "a1af33735cdd4190bea4d17a907e9a56";
    private final String ClientSecret = "16398191f8bd4d7ab9580e53078e601d";
    private final String callBack = "http://192.168.0.19:8080/auth/redirect";

    @Context
    UriInfo uriInfo;
    private AccountRepository _accountRepo;
    private Api api;
    private Gson gson = new Gson();

    @Inject
    public SpotifyService(AccountRepository accountRepository) {
        _accountRepo = accountRepository;
        api = Api.builder()
                .clientId(ClientId)
                .clientSecret(ClientSecret)
                .redirectURI(callBack)
                .build();
    }

    public AuthenticationRequest GetRequest() {
        AuthenticationRequest request = new AuthenticationRequest();
        request.client_id = ClientId;
        request.redirect_uri = callBack;
        request.response_type = "code";
        request.scope = "user-read-private";
        request.state = "unused";
        return request;
    }

    private Account GetAuthToken(Account account) throws Exception {
        if (account.refresh_token != null) {
            RefreshAccessTokenCredentials cred = api.refreshAccessToken().build().get();
            account.access_token = cred.getAccessToken();
            account.access_token_expires = GetExpiryDate(cred.getExpiresIn());
        } else {
            AuthorizationCodeCredentials credentials = null;
            credentials = api.authorizationCodeGrant(account.code).build().get();
            account.access_token = credentials.getAccessToken();
            account.refresh_token = credentials.getRefreshToken();
            account.access_token_expires = GetExpiryDate(credentials.getExpiresIn());
        }
        _accountRepo.CreateOrUpdate(account);
        return account;
    }

    public String queryAlbums(Account account, String barcode) throws AccountCodeExpiredException, Exception {
        Account updatedAccount = SetTokens(account);
//        System.out.println(api.searchAlbums(String.format("nicki", barcode)).limit(1).build().getJson());
        System.out.println("\n");
        System.out.println(api.searchAlbums(String.format("%s", barcode)).limit(1).build().getJson());
        return api.searchAlbums(barcode).limit(1).build().getJson();
    }

    public String GetSpotifyUrl(Account account, String artist, String album) throws AccountCodeExpiredException, Exception {
        SetTokens(account);
        Page<SimpleAlbum> albumPage = api.searchAlbums(String.format("artist:\"%s\"album:\"%s\"", artist, album)).limit(1).build().get();
        if(albumPage.getTotal() < 1){return "no results";}
        return albumPage.getItems().get(0).getExternalUrls().get("spotify");
    }
    
    public String addAlbumsToUser(Account account, String barcode) {
        return "";
    }

    public Account SetTokens(Account account) throws Exception {
     
        Account updatedAccount = account;
        if (account.code == null) {
            Logger.getLogger(SpotifyService.class.getCanonicalName()).severe("account has no code: " + account.email);
        }

        Date now = new Date(Calendar.getInstance().getTimeInMillis());
        if (account.access_token == null || account.access_token_expires == null || account.access_token_expires.before(now)) {
            updatedAccount = GetAuthToken(account);
        }
        api.setAccessToken(updatedAccount.access_token);
        api.setRefreshToken(updatedAccount.refresh_token);
        
        return updatedAccount;
    }

    private Date GetExpiryDate(int expiresIn) {

        Calendar date = Calendar.getInstance();
        long t = date.getTimeInMillis();
        return new Date(t + (expiresIn * 1000));
    }

}
