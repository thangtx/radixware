/*
 * Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. This Source Code is distributed
 * WITHOUT ANY WARRANTY; including any implied warranties but not limited to
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Mozilla Public License, v. 2.0. for more details.
 */

package org.radixware.kernel.common.sqlscript.parser;


import java.util.HashSet;
import java.util.Set;
import org.radixware.kernel.common.sqlscript.parser.interfaces.IAfterParseStatement;


public class SQLAdditionParserOptions {
    
    private SQLAdditionParserOptions() {
    }    
    
    public static class Factory{
        public static SQLAdditionParserOptions create(final String supersedingSysUser){
            final SQLAdditionParserOptions options = new SQLAdditionParserOptions();
            
            options.supersedingSysUser = supersedingSysUser;
            return options;
        }
    }
    
    //public SQLAdditionParserOptions() {}
    
    public boolean hidePassword = false;
    private String supersedingSysUser = null;//RADIXMANAGER-293

    public String getSupersedingSysUser() {
        return supersedingSysUser;
    }

    public void setSupersedingSysUser(final String supersedingSysUser) {
        this.supersedingSysUser = supersedingSysUser;
    }
        
    public static enum BehaviorWhenVariablesIsNotDefined {
        ThrowExeption, CollectUndefinedVariables, Nothing
    };
    
    private BehaviorWhenVariablesIsNotDefined behaviorWhenVariablesIsNotDefined = BehaviorWhenVariablesIsNotDefined.ThrowExeption;

    public BehaviorWhenVariablesIsNotDefined getBehaviorWhenVariablesIsNotDefined() {
        return behaviorWhenVariablesIsNotDefined;
    }

    public void setBehaviorWhenVariablesIsNotDefined(final BehaviorWhenVariablesIsNotDefined policyTypeWhenVariablesIsNotDefined) {
        if (policyTypeWhenVariablesIsNotDefined == null) {
            throw new IllegalArgumentException("Bwhavior parameter can't be null");
        }
        else {
            this.behaviorWhenVariablesIsNotDefined = policyTypeWhenVariablesIsNotDefined;
        }
    }

    private Set<String> undefinedVariablesCollection = new HashSet();
    
    public Set<String> getUndefinedVariablesCollection() {
        return undefinedVariablesCollection;
    }

    public void setUndefinedVariablesCollection(final Set<String> undefinedVariablesCollection) {
        if (undefinedVariablesCollection == null) {
            throw new IllegalArgumentException("Connection parameter can't be null");
        }
        else {
            this.undefinedVariablesCollection = undefinedVariablesCollection;
        }
    }
    
    private IAfterParseStatement afterParseStatement;

    public IAfterParseStatement getAfterProcessBlock() {
        return afterParseStatement;
    }

    public void setAfterProcessBlock(final IAfterParseStatement afterParseStatement) {
        this.afterParseStatement = afterParseStatement;
    }
    
    public static interface IVariablesPositionCollector{
        public void collect(final char boundarySymbol, final int indexInScriptBody, final String variableName);
    }
    
    private IVariablesPositionCollector  variablesPositionCollector = null;

    public IVariablesPositionCollector getVariablesPositionCollector() {
        return variablesPositionCollector;
    }

    public void setVariablesPositionCollector(final IVariablesPositionCollector variablesPositionCollector) {
        this.variablesPositionCollector = variablesPositionCollector;
    }

    @Override
    public String toString() {
        return "SQLAdditionParserOptions{" + "hidePassword=" + hidePassword + ", supersedingSysUser=" + supersedingSysUser + ", behaviorWhenVariablesIsNotDefined=" + behaviorWhenVariablesIsNotDefined + ", undefinedVariablesCollection=" + undefinedVariablesCollection + ", afterParseStatement=" + afterParseStatement + ", variablesPositionCollector=" + variablesPositionCollector + '}';
    }
}
