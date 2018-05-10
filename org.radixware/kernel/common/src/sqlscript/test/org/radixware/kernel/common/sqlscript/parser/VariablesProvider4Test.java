/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.radixware.kernel.common.sqlscript.parser;

import java.util.HashMap;
import java.util.Map;
import org.radixware.kernel.common.sqlscript.parser.spi.VariablesProvider;

public class VariablesProvider4Test implements VariablesProvider {
    private final Map<String,SQLScriptValue> varsRepo = new HashMap<>();
    
    VariablesProvider4Test(final Object... parameters) {
        for (int index = 0; index < parameters.length; index += 2) {
            varsRepo.put(parameters[index].toString(),(SQLScriptValue)parameters[index+1]);
        }
    }
    
    @Override
    public SQLScriptValue getVariable(final String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name can't be null or empty");
        }
        else {
            return varsRepo.get(name);
        }
    }

    @Override
    public void putVariable(final String name, final SQLScriptValue value) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name can't be null or empty");
        }
        else {
            varsRepo.put(name,value);
        }
    }

    @Override
    public void removeVariable(final String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name can't be null or empty");
        }
        else {
            varsRepo.remove(name);
        }
    }
};
