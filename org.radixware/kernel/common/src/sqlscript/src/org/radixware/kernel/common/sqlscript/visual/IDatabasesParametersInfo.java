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

package org.radixware.kernel.common.sqlscript.visual;

import java.util.List;


public interface IDatabasesParametersInfo{
    
    //public static enum EThreeBoolValues{TRUE, FALSE, NOT_DEFINED}
    
    static public interface IParameter extends Comparable<IParameter>{
        public String getName();
        public String getDescription();
        
        public boolean getDefaultValue();
        public boolean mayEdit();
    }
    
    static public interface IDatabase{
        public List<? extends IParameter> getParameters();
        public String getTitle();
        public List<String> getVersions();
    }
    
    
    public List<? extends IDatabase> getDatabases();
    
}
