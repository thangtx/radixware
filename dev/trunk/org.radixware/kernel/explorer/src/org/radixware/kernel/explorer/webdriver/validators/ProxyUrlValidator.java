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

package org.radixware.kernel.explorer.webdriver.validators;

import java.net.MalformedURLException;
import java.net.URL;

final class ProxyUrlValidator implements IJsonValueValidator{
    
    private final static ProxyUrlValidator INSTANCE = new ProxyUrlValidator();
    
    private ProxyUrlValidator(){        
    }
    
    public static ProxyUrlValidator getInstance(){
        return INSTANCE;
    }        

    @Override
    public boolean isValid(final Object value) {
        if (value!=null && value instanceof String){
            try{
                final URL proxyUrl = new URL((String)value);
                if (proxyUrl.getFile()!=null && !proxyUrl.getFile().isEmpty()){
                    return false;
                }
                if (proxyUrl.getPath()!=null && !proxyUrl.getPath().isEmpty()){
                    return false;
                }
                if (proxyUrl.getQuery()!=null && !proxyUrl.getQuery().isEmpty()){
                    return false;
                }
                if (proxyUrl.getRef()!=null && !proxyUrl.getRef().isEmpty()){
                    return false;
                }
                if (proxyUrl.getUserInfo()!=null && !proxyUrl.getUserInfo().isEmpty()){
                    return false;
                }
                if (proxyUrl.getHost()==null || proxyUrl.getHost().isEmpty()){
                    return false;
                }
            }catch(MalformedURLException exception){
                return false;
            }
            return true;
        }
        return false;
    }

}
