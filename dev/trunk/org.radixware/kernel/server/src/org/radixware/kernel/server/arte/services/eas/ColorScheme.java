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

import java.lang.reflect.InvocationTargetException;
import org.radixware.kernel.common.enums.ESelectorRowStyle;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.server.types.Pid;
import org.radixware.kernel.server.arte.Arte;
import org.radixware.kernel.server.meta.presentations.ICommonSelectorAddon;
import org.radixware.kernel.server.types.Entity;


public class ColorScheme  implements  ICommonSelectorAddon{

    private final static Id SELECTOR_ADDONS_TABLE_ID = Id.Factory.loadFrom("tblHQSOHRWZH5ESLD2Y6IVF6XFBE4");
    private final static Id SELECTOR_ADDONS_GUID_COLUMN_ID = Id.Factory.loadFrom("colTOTOEP36JFH35MA7BJYRU67GKU");    
    private final static String USER_FUNC_METHOD_ID="mthEA22RKNUDNGEJBOX6JUBJTUT6E";
    
    private final Id id;
    private final Id tableId;
    private final long modifyTime;
    private final String title;    
    
    public ColorScheme(final org.radixware.schemas.eas.ColorScheme xColorScheme){
        this.id=xColorScheme.getId();
        this.tableId=xColorScheme.getTableId();
        this.title=xColorScheme.getTitle();
        this.modifyTime=xColorScheme.getLastUpdateTime();        
    }
    
    public ColorScheme(final Id id,final Id tableId,final String title, final long modifyTime){
        this.id=id;
        this.tableId=tableId;
        this.title=title;
        this.modifyTime=modifyTime;        
    }
    
    public void addToXml(final org.radixware.schemas.eas.ColorScheme colorScheme){
        colorScheme.setId(id);
        colorScheme.setTableId(tableId);
        colorScheme.setTitle(title);        
        colorScheme.setLastUpdateTime(modifyTime);
    }
    
    @Override
    public Id getId() {
        return id;
    }

    @Override
    public long getLastUpdateTime() {
        return modifyTime;
    } 
    
    public Id getTableId() {
        return tableId;
    }
    
    public String getTitle() {
        return title;
    }
    
    private Pid getPid(final Arte arte){
        return new Pid(arte, SELECTOR_ADDONS_TABLE_ID, SELECTOR_ADDONS_GUID_COLUMN_ID, id.toString());        
    }    
    
    public ESelectorRowStyle apply(final Entity applyTo){
        final Arte arte = applyTo.getArte();
        final Entity colorSchemeEntityObject=arte.getEntityObject(getPid(arte));
        try {
            java.lang.reflect.Method mth = 
                colorSchemeEntityObject.getClass().getMethod(USER_FUNC_METHOD_ID, new Class<?>[] { Entity.class });
            return (ESelectorRowStyle)mth.invoke(colorSchemeEntityObject, new Object[]{ applyTo });
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
              arte.getTrace().put(Messages.MLS_ID_EAS_FAILED_TO_APPLY_COLOR_SCHEME, 
                                  new ArrStr(applyTo.getRadMeta().getName(),
                                             applyTo.getRadMeta().getId().toString(),
                                              ExceptionTextFormatter.exceptionStackToString(ex)));              
        }
        return ESelectorRowStyle.NORMAL;        
    }
}
