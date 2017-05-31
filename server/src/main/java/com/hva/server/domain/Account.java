/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hva.server.domain;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import java.util.Date;

/**
 *
 * @author siege
 */
@DatabaseTable()
public class Account {
        
    @DatabaseField(generatedId = false, id = true, width = 500)
    public String code;
        
    @DatabaseField
    public String access_token;
    
    @DatabaseField
    public String refresh_token;
    
    @DatabaseField
    public Date access_token_expires;
    
    @DatabaseField
    public String email;
    
     @DatabaseField
    public String state;
    
    
    
    
}
