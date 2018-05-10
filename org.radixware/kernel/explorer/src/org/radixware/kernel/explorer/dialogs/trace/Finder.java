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

package org.radixware.kernel.explorer.dialogs.trace;

import com.trolltech.qt.gui.QWidget;
import org.radixware.kernel.common.client.IClientEnvironment;


final class Finder extends AbstractFinder{

    private final ITokenProvider tokenProvider;    
    
    public Finder(final IClientEnvironment environment, final ITokenProvider tokenProvider) {
        super(environment, (QWidget)tokenProvider);
        this.tokenProvider = tokenProvider;
    }

    @Override
    protected boolean findNext(final IClientEnvironment environment, 
                                        final String searchString, final boolean forward, final boolean caseSensitive) {
        final String normalizedSearchString;
        if (caseSensitive){
            normalizedSearchString = searchString;
        }else{
            normalizedSearchString = searchString.toLowerCase(environment.getLocale());
        }
        String source;
        for (IToken token = getNextToken(forward); token!=null; token = getNextToken(forward)){
            source = token.getValue();
            if (!caseSensitive){
                source = source.toLowerCase(environment.getLocale());
            }
            if (source.contains(normalizedSearchString)) {
                token.select();
                return true;
            }            
        }
        showStringNotFoundMessage();
        return false;
    }
    
    private IToken getNextToken(final boolean forward){
        if (forward ? tokenProvider.hasNextToken() : tokenProvider.hasPrevToken()){
            return forward ? tokenProvider.getNextToken() : tokenProvider.getPrevToken();
        }else{
            return null;
        }
    }
}
