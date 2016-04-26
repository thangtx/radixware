/*
* Copyright (c) 2008-2016, Compass Plus Limited. All rights reserved.
*
* This Source Code Form is subject to the terms of the Mozilla Public
* License, v. 2.0. If a copy of the MPL was not distributed with this
* file, You can obtain one at http://mozilla.org/MPL/2.0/.
* This Source Code is distributed WITHOUT ANY WARRANTY; including any 
* implied warranties but not limited to warranty of MERCHANTABILITY 
* or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
* License, v. 2.0. for more details.
*/

package org.radixware.kernel.common.client.types;

import java.util.HashMap;
import java.util.Map;


public class FilterRules {

    
    private final Map<String,MatchOptions> rules = new HashMap<>(4);
    
    public FilterRules(){        
    }
    
    public void addRule(final String matchString){
        rules.put(matchString, new MatchOptions(false, false));
    }
    
    public void addRule(final String matchString, final boolean wholeWord){
        rules.put(matchString, new MatchOptions(false, wholeWord));
    }
    
    public void addRule(final String matchString, final boolean wholeWord, final boolean caseSensitive){
        rules.put(matchString, new MatchOptions(caseSensitive, wholeWord));
    }
    
    public void clear(){
        rules.clear();
    }
    
    public FilterRules copy(){
        final FilterRules copy = new FilterRules();
        copy.rules.putAll(rules);
        return copy;
    }
    
    public boolean isMatchToSomeFilter(final String filterText){
        if (filterText==null || filterText.isEmpty()){
            return true;
        }
        for (Map.Entry<String,MatchOptions> entry: rules.entrySet()){
            if (entry.getValue().matchToSearchString(entry.getKey(), filterText)){
                return true;
            }
        }
        return false;
    }
    
    public boolean isMatchToAllFilters(final String filterText){
        if (filterText==null || filterText.isEmpty()){
            return true;
        }
        for (Map.Entry<String,MatchOptions> entry: rules.entrySet()){
            if (!entry.getValue().matchToSearchString(entry.getKey(), filterText)){
                return false;
            }
        }
        return true;        
    }
}
