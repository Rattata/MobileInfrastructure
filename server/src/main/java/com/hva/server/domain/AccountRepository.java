/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hva.server.domain;

import com.hva.server.AuthResource;
import com.hva.server.infrastructure.ConnectionFactory;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import java.util.logging.Logger;
import javax.inject.Inject;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

/**
 *
 * @author siege
 */
public class AccountRepository {

    private ConnectionFactory _conn;

    @Inject
    public AccountRepository(ConnectionFactory conn) {
        _conn = conn;
    }

    public Account CreateOrUpdate(Account account) throws Exception {
        ConnectionSource conn = null;
        conn = _conn.provide();
        Dao<Account, Integer> dao = DaoManager.createDao(conn, Account.class);
        dao.createOrUpdate(account);
        conn.close();
        return account;
    }

}
