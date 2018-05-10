/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.radixware.kernel.server.jdbc;

/**
 * <p>This class is a marker class to indicate setNull calls</p>
 */
public class NullParameterValue {
    public static final NullParameterValue instance = new NullParameterValue();
    
    private NullParameterValue(){}
    
    @Override
    public String toString() {
        return "NullParameterValue{" + '}';
    }    
}
