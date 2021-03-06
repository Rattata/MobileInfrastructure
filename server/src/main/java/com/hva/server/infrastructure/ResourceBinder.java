/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hva.server.infrastructure;

import com.hva.server.domain.AccountRepository;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

/**
 *
 * @author siege
 */
public class ResourceBinder extends AbstractBinder {

    @Override
    protected void configure() {
        bind(SpotifyService.class).to(SpotifyService.class);
        bind(AccountRepository.class).to(AccountRepository.class);
        bind(ConnectionFactory.class).to(ConnectionFactory.class);
    }
}
