/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hva.server.infrastructure;

import com.hva.server.Main;
import com.hva.server.domain.Account;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import java.util.logging.Logger;
import org.glassfish.hk2.api.Factory;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

/**
 *
 * @author siege
 */
public class ConnectionFactory extends AbstractBinder implements Factory<ConnectionSource>{
    private ConnectionSource _source;

    public ConnectionFactory() {
        try {
            _source = new JdbcPooledConnectionSource("jdbc:mysql://localhost:3306/restful", "root", "root");
            TableUtils.createTableIfNotExists(_source, Account.class);
        } catch (Exception e) {
               Logger.getLogger(ConnectionFactory.class.getCanonicalName()).severe(e.getMessage());
        }
    }
    
    @Override
    protected void configure() {
        bindFactory(this).to(ConnectionSource.class);
    }

    @Override
    public ConnectionSource provide() {
        return _source;
    }

    @Override
    public void dispose(ConnectionSource t) {
        try {
            t.close();
        } catch (Exception e) {
             Logger.getLogger(ConnectionFactory.class.getCanonicalName()).severe(e.getMessage());
        }
    }
    
}
