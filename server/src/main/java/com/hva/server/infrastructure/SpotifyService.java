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
import com.wrapper.spotify.models.RefreshAccessTokenCredentials;
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

    private final String ClientId = "97b2e28c509d450ba4be16b6a02c048c";
    private final String ClientSecret = "e34f7b0c3e1d4bf0ae596d7007945e44";
    private final String callBack = "http://192.168.0.100:8080/auth/redirect";

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
            api.setAccessToken(credentials.getAccessToken());
            api.setRefreshToken(credentials.getRefreshToken());
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

    public String addAlbumsToUser(Account account, String barcode) {
        return "";
    }

    public Account SetTokens(Account account) throws Exception {

        api.setAccessToken(account.access_token);
        api.setRefreshToken(account.refresh_token);
        Account updatedAccount = account;
        if (account.code == null) {
            Logger.getLogger(SpotifyService.class.getCanonicalName()).severe("account has no code: " + account.email);
        }

        Date now = new Date(Calendar.getInstance().getTimeInMillis());
        if (account.access_token == null || account.access_token_expires == null || account.access_token_expires.before(now)) {
            updatedAccount = GetAuthToken(account);
        }

        return updatedAccount;
    }

    private Date GetExpiryDate(int expiresIn) {

        Calendar date = Calendar.getInstance();
        long t = date.getTimeInMillis();
        return new Date(t + (expiresIn * 1000));
    }

}
