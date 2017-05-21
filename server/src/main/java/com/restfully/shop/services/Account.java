/*
 * HvA licences apply
 */
package com.restfully.shop.services;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 *
 * @author siege
 */
@DatabaseTable(tableName = "Account")
public class Account {
    @DatabaseField(id = true)
    private String name;
    
    @DatabaseField
    private String password;
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
}
