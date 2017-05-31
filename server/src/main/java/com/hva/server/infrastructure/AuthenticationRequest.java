/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hva.server.infrastructure;

/**
 *
 * @author siege
 */
public class AuthenticationRequest {
    public String client_id;
    public String response_type;
    public String redirect_uri;
    public String state;
    public String scope;
}
