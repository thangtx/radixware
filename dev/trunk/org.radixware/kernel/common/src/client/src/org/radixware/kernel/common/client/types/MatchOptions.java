/*
* Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
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


public class MatchOptions {
    
    private final boolean caseSensitive;
    private final boolean wholeWord;
    
    public MatchOptions(final boolean caseSensitive, final boolean wholeWord){
        this.caseSensitive = caseSensitive;
        this.wholeWord = wholeWord;
    }
    
    public final boolean isCaseSensitive(){
        return caseSensitive;
    }
    
    public final boolean isWholeWord(){
        return wholeWord;
    }
    
    public boolean matchToSearchString(final String sourceString, final String searchString){
        if (sourceString==null || searchString==null){
            return sourceString==null && searchString==null;
        }
        if (sourceString.isEmpty() || searchString.isEmpty()){
            return sourceString.isEmpty() && searchString.isEmpty();
        }
        if (caseSensitive && wholeWord) {
            return sourceString.equals(searchString);
        }
        if (caseSensitive && !wholeWord) {
            return sourceString.contains(searchString);
        }
        if (!caseSensitive && wholeWord) {
            return sourceString.toLowerCase().equals(searchString.toLowerCase());
        }
        return sourceString.toLowerCase().contains(searchString.toLowerCase());        
    }    
}
