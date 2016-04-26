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
    public  boolean hidePassword = false;
    
        
    public static enum BehaviorWhenVariablesIsNotDefined{ThrowExeption, CollectUndefinedVariables, Nothing};
    
    private BehaviorWhenVariablesIsNotDefined behaviorWhenVariablesIsNotDefined = BehaviorWhenVariablesIsNotDefined.ThrowExeption;

    public BehaviorWhenVariablesIsNotDefined getBehaviorWhenVariablesIsNotDefined() {
        return behaviorWhenVariablesIsNotDefined;
    }

    public void setBehaviorWhenVariablesIsNotDefined(BehaviorWhenVariablesIsNotDefined policyTypeWhenVariablesIsNotDefined) {
        this.behaviorWhenVariablesIsNotDefined = policyTypeWhenVariablesIsNotDefined;
    }

    
    private Set<String> undefinedVariablesCollection = new HashSet();
    public Set<String> getUndefinedVariablesCollection() {
        return undefinedVariablesCollection;
    }

    public void setUndefinedVariablesCollection(Set<String> undefinedVariablesCollection) {
        this.undefinedVariablesCollection = undefinedVariablesCollection;
    }
    
    private IAfterParseStatement afterParseStatement;

    public IAfterParseStatement getAfterProcessBlock() {
        return afterParseStatement;
    }

    public void setAfterProcessBlock(final IAfterParseStatement afterParseStatement) {
        this.afterParseStatement = afterParseStatement;
    }
    
    
        
    
    
}
