/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hva.server.domain;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 *
 * @author siege
 */
@DatabaseTable()
public class Account {
    
    @DatabaseField(generatedId = true)
    public int ID;
    
    @DatabaseField
    public String name;
    
}
