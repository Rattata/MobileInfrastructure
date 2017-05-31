/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hva.server.infrastructure;

import com.j256.ormlite.support.ConnectionSource;
import javax.inject.Inject;
import org.glassfish.hk2.api.Factory;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

/**
 *
 * @author siege
 */
public class SpotifyServiceFactory extends AbstractBinder implements Factory<SpotifyService>{

    
    private final String ClientId = "97b2e28c509d450ba4be16b6a02c048c";
    private final String ClientSecret = "e34f7b0c3e1d4bf0ae596d7007945e44";
    private final String Code = "<insert code>";
    private final String callBack = "http://localhost:8080/myapp/spotifycallback";
    
    private ConnectionSource _conn;
    
    @Inject
    public SpotifyServiceFactory(ConnectionSource conn) {
        _conn = conn;
    }

    @Override
    protected void configure() {
        bindFactory(this).to(SpotifyService.class);
    }

    @Override
    public SpotifyService provide() {
        return new SpotifyService(_conn);
    }

    @Override
    public void dispose(SpotifyService t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
