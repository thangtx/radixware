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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


class SQLParsePragmaStatementUtils {
    
    protected static class InnerErrorPrefix {

        public InnerErrorPrefix(final String value) {
            this.value = value;
        }
        
        protected final String value;
    }
    
    protected static final InnerErrorPrefix ORA_PREFIX = new InnerErrorPrefix("ORA");
    protected static final InnerErrorPrefix PG_PREFIX = new InnerErrorPrefix("PG");
     
    
    protected static class InnerError {
        protected InnerError(final InnerErrorPrefix prefix, final int postfix) {//ORA-01451 - for example --> preffix=ORA, postfix=1451
            this.prefix = prefix;
            this.postfix = postfix;
        }
        protected final InnerErrorPrefix prefix;
        protected final int postfix;
    }
            
    
    protected static enum PredefinedSuccess {//RADIXMANAGER-269
        
        COLUMN_CANNOT_BE_NULL("COLUMN_CANNOT_BE_NULL", new InnerError(ORA_PREFIX, 1451)),
        COLUMN_CANNOT_BE_NOT_NULL("COLUMN_CANNOT_BE_NOT_NULL", new InnerError(ORA_PREFIX, 1442)),
        COLUMN_ALREADY_EXISTS("COLUMN_ALREADY_EXISTS", new InnerError(ORA_PREFIX, 1430)),
        UNIQUE_CONSTRAINT("UNIQUE_CONSTRAINT", new InnerError(ORA_PREFIX, 1)),
        DUPLICATE_COLUMN_NAME("DUPLICATE_COLUMN_NAME", new InnerError(ORA_PREFIX, 957)),
        CONSTRAINT_ALREADY_EXISTS("CONSTRAINT_ALREADY_EXISTS", new InnerError(ORA_PREFIX, 2275)),
        CANNOT_BE_DROP_CONSTRAINT("CANNOT_BE_DROP_CONSTRAINT", new InnerError(ORA_PREFIX, 2443)),
        NAME_ALREADY_USED("NAME_ALREADY_USED", new InnerError(ORA_PREFIX, 955)),
        INDEX_DOES_NOT_EXIST("INDEX_DOES_NOT_EXIST", new InnerError(ORA_PREFIX, 1418)),        
        ;
        
        final String radixSynonym;
        final List<InnerError> innerErrors = new ArrayList<>();
        
        PredefinedSuccess(final String radixSynonym, final InnerError ... innerDatabaseSynonyms) {
            this.radixSynonym = radixSynonym;
            innerErrors.addAll(Arrays.asList(innerDatabaseSynonyms));
        }

    }
    
    
    
    protected static PredefinedSuccess getCorrectPredefinedSuccessPragma(final String name) {
        if (name == null) {
            return null;
        }
        final String name2 = name.toUpperCase();
        for (PredefinedSuccess predefinedSuccess : PredefinedSuccess.values()) {
            if (predefinedSuccess.radixSynonym.equals(name2)) {
                return predefinedSuccess;
            }
        }
        return null;
    }
}
