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

package org.radixware.wps;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.client.env.SettingNames;


public final class DefaultSettings {
    
    private final static DefaultSettings INSTANCE = new DefaultSettings();
    private final Map<String,String> settings = new HashMap<>();
    
    private DefaultSettings(){
        try{
            readDefaultSettings();
        }catch(IOException ex){
            Logger.getLogger(DefaultSettings.class.getName()).log(Level.SEVERE, "Failed to read default settings", ex);
        }
    }
    
    private void readDefaultSettings() throws IOException{
        final InputStream stream = DefaultSettings.class.getResourceAsStream("defaults.ini");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))){
            for(String line; (line = reader.readLine()) != null;){
                final int pos = line.indexOf('=');
                if (pos>1 && pos<line.length()-1){
                    settings.put(SettingNames.SYSTEM+"/"+line.substring(0, pos), line.substring(pos+1));
                }
            }
        }
    }
    
    public static DefaultSettings getInstance(){
        return INSTANCE;
    }
    
    public String getValue(final String key){
        return settings.get(normalizeKey(key));
    }
    
    private static String normalizeKey(final String key) {
        String result = key.replace('\\', '/');
        while (result.contains("//")) {
            result = result.replaceFirst("//", "/");
        }
        return result;
    }    
}
