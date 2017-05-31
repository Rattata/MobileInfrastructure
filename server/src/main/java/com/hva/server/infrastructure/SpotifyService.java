/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hva.server.infrastructure;

import com.google.common.util.concurrent.SettableFuture;
import com.hva.server.domain.Account;
import com.hva.server.domain.AccountCodeExpiredException;
import com.hva.server.domain.AccountRepository;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.dao.Dao;
import com.wrapper.spotify.Api;
import com.wrapper.spotify.models.AuthorizationCodeCredentials;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;
import javax.inject.Inject;

/**
 *
 * @author siege
 */
public class SpotifyService {

    private final String ClientId = "97b2e28c509d450ba4be16b6a02c048c";
    private final String ClientSecret = "e34f7b0c3e1d4bf0ae596d7007945e44";
    private final String callBack = "http://localhost:8080/myapp/spotifycallback";

    private AccountRepository _accountRepo;
    private Api api;

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

    private Account SetAuthToken(Account account) {
        AuthorizationCodeCredentials credentials = null;
        try {
            credentials = api.authorizationCodeGrant(account.code).build().get();
            api.setAccessToken(credentials.getAccessToken());
            api.setRefreshToken(credentials.getRefreshToken());

            account.access_token = credentials.getAccessToken();
            account.refresh_token = credentials.getRefreshToken();
            account.access_token_expires = GetExpiryDate(credentials.getExpiresIn());
            _accountRepo.CreateOrUpdate(account);
            return account;
        } catch (Exception e) {
            Logger.getLogger(e.getMessage());
            e.getStackTrace();
            return null;
        }
    }

    public String queryAlbums(Account account, String barcode) {
        //q=upc:{barcode}&type=album 
        return "";
    }

    public String addAlbumsToUser(Account account, String barcode) {
        return "";
    }

    public Api GetAPI(Account account) throws AccountCodeExpiredException{

        api.setAccessToken(account.access_token);
        api.setRefreshToken(account.refresh_token);

        if (account.code == null) {
            Logger.getLogger(SpotifyService.class.getCanonicalName()).severe("account has no code: " + account.email);
        }

        if (account.access_token == null) {
            SetAuthToken(account);
        }

        Calendar date = Calendar.getInstance();
        long t = date.getTimeInMillis();
        Date Slackydate = new Date(t + 10000);

        Date now = new Date(Calendar.getInstance().getTimeInMillis());
        if (account.access_token_expires.before(now)) {
            throw new AccountCodeExpiredException();
        } else if (account.access_token_expires.before(Slackydate)) {
            api.refreshAccessToken();
        }

        try {
            account.email = api.getMe().build().get().getEmail();
            _accountRepo.CreateOrUpdate(account);
        } catch (Exception e) {
            Logger.getLogger(e.getMessage());
            e.getStackTrace();
        }

        return api;
    }

    private Date GetExpiryDate(int expiresIn) {

        Calendar date = Calendar.getInstance();
        long t = date.getTimeInMillis();
        return new Date(t + (expiresIn * 1000));
    }

}
