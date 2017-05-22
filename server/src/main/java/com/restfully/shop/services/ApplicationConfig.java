package com.restfully.shop.services;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import java.io.Console;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;
import javax.ws.rs.core.Application;

/**
 *
 * @author boofk
 */
@javax.ws.rs.ApplicationPath("/services") 
public class ApplicationConfig extends Application {
    
    private Set<Object> singletons = new HashSet<Object>();
    private Set<Class<?>> resources = new java.util.HashSet<>();

    public ApplicationConfig() {
        singletons.add(new CustomerResourceService());
        try {
            
        // this uses h2 by default but change to match your database
        String databaseUrl = "jdbc:mysql://localhost:3306/restful";
        // create a connection source to our database
        ConnectionSource connectionSource =
            new JdbcConnectionSource(databaseUrl, "root", "root");

        
        // if you need to create the 'accounts' table make this call
            TableUtils.createTableIfNotExists(connectionSource, Account.class);
        
        
        // instantiate the dao
            Dao<Account, String> accountDao =
            DaoManager.createDao(connectionSource, Account.class);

 	
        // create an instance of Account
        Account account = new Account();
        account.setName("Jim Coakley");

        // persist the account object to the database
        accountDao.createIfNotExists(account);
        
        // retrieve the account from the database by its id field (name)
        Account account2 = accountDao.queryForId("Jim Coakley");
        System.out.println("Account: " + account2.getName());

        // close the connection source
        connectionSource.close();
        } catch (Exception e) {
            Logger.getAnonymousLogger().severe("Error creating DAO");
            
        }
    }
    
    @Override
    public Set<Class<?>> getClasses() {
        //addRestResourceClasses(resources);
        return resources;
    }
    
    @Override
    public Set<Object> getSingletons() {
        return singletons;
    }

    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
    }
    
}
