/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hva.server.infrastructure;

import com.hva.server.Main;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import java.util.logging.Logger;
import org.glassfish.hk2.api.Factory;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

/**
 *
 * @author siege
 */
public class DaoFactory extends AbstractBinder implements Factory<ConnectionSource>{
    private ConnectionSource _source;

    public DaoFactory() {
        try {
            _source = new JdbcPooledConnectionSource("jdbc:mysql://localhost:3306/restful?profileSQL=true", "root", "root");
        } catch (Exception e) {
               Logger.getLogger(DaoFactory.class.getCanonicalName()).severe(e.getMessage());
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
             Logger.getLogger(DaoFactory.class.getCanonicalName()).severe(e.getMessage());
        }
    }
    
}
