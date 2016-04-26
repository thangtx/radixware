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

package org.radixware.kernel.server.arte.services.eas;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.radixware.kernel.common.enums.ETimingSection;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.server.arte.Arte;
import org.radixware.kernel.server.meta.clazzes.RadClassDef;
import org.radixware.kernel.server.meta.presentations.ICommonSelectorAddon;


abstract class CommonSelectorAddons<T extends ICommonSelectorAddon> {
    private static final String LAST_MODIFY_TIME_RECORD_GUID = "LAST_MODIFY_TIME";
    private static final long MODIFY_TIME_CHECK_PERIOD_MILLIS = 10000; //10 seconds
    
    private final PreparedStatement qryGetLastModifyTime;
    private final Map<String, Map<Id,T>> cache = new HashMap<String, Map<Id,T>>(256);
    private final List<String> capturedAddons = new LinkedList<String>();
    
    private long lastModifyTimeCheckMillis = 0;
    private Timestamp lastModifyTime = null;

    public CommonSelectorAddons(final Arte arte) throws SQLException {
        qryGetLastModifyTime = arte.getDbConnection().get().prepareStatement("select addons.lastUpdateTime from rdx_easselectoraddons addons " + 
                                                                            "where addons.guid='" + LAST_MODIFY_TIME_RECORD_GUID + "'");
    }

    public final Map<Id,T> get(final Arte arte, final RadClassDef classDef, final Id selPresentationId) throws SQLException {
        final String cacheKey = classDef.getEntityId().toString() + selPresentationId.toString() + 
                                "#"+(arte.getVersion()==null ? "" : arte.getVersion().toString());
        if (capturedAddons.contains(cacheKey)){            
            return Collections.unmodifiableMap(cache.get(cacheKey));
        }
        else if (checkModifyTime(arte)){
            final Map<Id,T> addons = cache.get(cacheKey);
            if (addons==null){
                return Collections.unmodifiableMap(updateCache(arte, classDef, selPresentationId));
            }
            else{                
                return Collections.unmodifiableMap(addons);
            }
        }
        else{
            return Collections.unmodifiableMap(updateCache(arte, classDef, selPresentationId));
        }
    }
    
    private Map<Id,T> updateCache(final Arte arte, final RadClassDef classDef, final Id selPresentationId) throws SQLException{        
        Map<Id,T> addons = loadAddons(arte, classDef, selPresentationId);
        if (addons==null){
            addons = Collections.<Id,T>emptyMap();
        }
        final String cacheKey = classDef.getEntityId().toString() + selPresentationId.toString() + 
                                "#"+(arte.getVersion()==null ? "" : arte.getVersion().toString());
        cache.put(cacheKey, addons);
        capturedAddons.add(cacheKey);
        return addons;
    }
    
    private void clearCache(){        
        cache.clear();
        capturedAddons.clear();
    }
    
    protected abstract Map<Id,T> loadAddons(final Arte arte, final RadClassDef classDef, final Id selPresentationId) throws SQLException;

    private boolean checkModifyTime(final Arte arte) throws SQLException {
        if (System.currentTimeMillis() - lastModifyTimeCheckMillis > MODIFY_TIME_CHECK_PERIOD_MILLIS) {            
            final ResultSet rs;
            arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
            try {
                rs = qryGetLastModifyTime.executeQuery();
            } finally {
                arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
            }
            try {
                lastModifyTimeCheckMillis = System.currentTimeMillis();
                if (rs.next()) {                    
                    final Timestamp actualLastModifyTime = rs.getTimestamp("lastUpdateTime");
                    if (lastModifyTime == null) {
                        lastModifyTime = actualLastModifyTime;
                        return false;
                    } else if (!lastModifyTime.equals(actualLastModifyTime)) {
                        clearCache();
                        lastModifyTime = actualLastModifyTime;
                        return false;
                    }
                    return true;
                } else {
                    return true;
                }
            } finally {
                rs.close();                
            }            
        } else {
            return true;
        }
    }    
    
    public final T find(final Arte arte, 
                             final RadClassDef classDef, 
                             final Id selPresentationId, 
                             final Id addonId,
                             final long lastUpdateTime) throws SQLException{
        
        final String cacheKey = classDef.getEntityId().toString() + selPresentationId.toString() + 
                                "#"+(arte.getVersion()==null ? "" : arte.getVersion().toString());        
                
        if (capturedAddons.contains(cacheKey) || checkModifyTime(arte)) { 
            final Map<Id,T> cachedAddons = cache.get(cacheKey);
            if (cachedAddons!=null){
                final T cachedAddon = cachedAddons.get(addonId);
                if (cachedAddon!=null && cachedAddon.getLastUpdateTime()>=lastUpdateTime){
                    return cachedAddon;
                }
                else{
                    clearCache();
                }
            }            
        }
        return updateCache(arte, classDef, selPresentationId).get(addonId);
    }    
    
    public final void clearLocalCache() {
        capturedAddons.clear();
    }    
    
}
