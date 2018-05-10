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

package org.radixware.kernel.common.client.env;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.enums.EDatabaseType;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.schemas.types.MapStrStr;


public final class ProductInstallationOptions {
    
    private final static String DB_TYPE_OPTION_NAME = "org.radixware\\DB_TYPE";
    private final static String DB_VERSION_OPTION_NAME = "org.radixware\\DB_VERSION";
    private final static String PARTITIONING_OPTION_NAME = "org.radixware\\Partitioning";
    
    private final Map<String, String> options = new HashMap<>();
    
    private ProductInstallationOptions(){
    }
        
    public static ProductInstallationOptions loadOptions(final MapStrStr xmlMap) {
        final ProductInstallationOptions result = new ProductInstallationOptions();
        if (xmlMap!=null && xmlMap.getEntryList()!=null){
            for (MapStrStr.Entry entry: xmlMap.getEntryList()){
                result.options.put(entry.getKey(), entry.getValue());
            }
        }
        return result;
    }
    
    @Deprecated//use getProductInstallationOptions in IClientEnvironment
    public static ProductInstallationOptions loadOptions(final IClientEnvironment environment) throws ServiceClientException, InterruptedException{
        return environment.getProductInstallationOptions();
    }
    
    public static boolean isAccessible(final IClientEnvironment environment){
        return true;
    }

    public Set<String> getOptionNames(){
        return options.keySet();
    }
    
    public String getOptionValue(final String name){
        return options.get(name);
    }
    
    public boolean isOptionDefined(final String name){
        return options.containsKey(name);
    }
    
    public boolean isOptionEnabled(final String name){
        return  "ENABLED".equalsIgnoreCase(options.get(name));
    }
    
    public EDatabaseType getDatabaseType(){
        try{
            return EDatabaseType.getForValue(options.get(DB_TYPE_OPTION_NAME));
        }catch(NoConstItemWithSuchValueError error){
            return null;
        }        
    }
    
    public String getDatabaseVersion(){
        return options.get(DB_VERSION_OPTION_NAME);
    }
    
    public boolean isPartitioningEnabled(){
        return isOptionEnabled(PARTITIONING_OPTION_NAME);
    }
}
