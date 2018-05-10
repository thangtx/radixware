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

package org.radixware.kernel.explorer.widgets.propeditors;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import org.radixware.kernel.common.client.models.items.properties.*;//NOPMD
import org.radixware.kernel.common.client.utils.ValueConverter;
import org.radixware.kernel.common.enums.EEditMaskType;
import org.radixware.kernel.common.enums.EValType;


public final class PropEditorsPool {

    private static final PropEditorsPool INSTANCE = new PropEditorsPool();
    private static final int MAX_CACHE_SIZE = 50;
    private final Map<String, Stack<PropEditor>> propEditorsCache = new HashMap<>();

    private PropEditorsPool() {
    }

    public static PropEditorsPool getInstance() {
        return INSTANCE;
    }

    public boolean cachePropEditor(final PropEditor propEditor) {
        if (propEditor.getClass().getName().startsWith(this.getClass().getPackage().getName())
            && propEditor instanceof ProxyPropEditor==false) {
            final Stack<PropEditor> propEditors = getPropEditorsCache(propEditor);
            if (propEditors.size() < MAX_CACHE_SIZE) {
                propEditor.clear();
                propEditors.push(propEditor);
                propEditor.setInCache(true);
                return true;
            }
        }
        return false;
    }

    private PropEditor getCachedPropEditor(final Property property) {
        final Stack<PropEditor> propEditors = getPropEditorsCache(property);
        if (propEditors.isEmpty()) {
            return null;
        } else {
            final PropEditor propEditor = propEditors.pop();
            propEditor.setInCache(false);
            propEditor.setProperty(property);
            return propEditor;
        }
    }

    private Stack<PropEditor> getPropEditorsCache(final Property property) {
        final String cacheKey = getCacheKey(property);
        Stack<PropEditor> propEditors = propEditorsCache.get(cacheKey);
        if (propEditors == null) {
            propEditors = new Stack<>();
            propEditorsCache.put(cacheKey, propEditors);
        }
        return propEditors;
    }
    
    private Stack<PropEditor> getPropEditorsCache(final PropEditor propEditor) {
        final String cacheKey = getCacheKey(propEditor);
        Stack<PropEditor> propEditors = propEditorsCache.get(cacheKey);
        if (propEditors == null) {
            propEditors = new Stack<>();
            propEditorsCache.put(cacheKey, propEditors);
        }
        return propEditors;
    }    

    private static String getCacheKey(final Property property) {
        final EValType valType = ValueConverter.serverValType2ClientValType(property.getType());
        final EEditMaskType editMaskType = property.getEditMask().getType();
        return valType.name() + (editMaskType == null ? "" : "/" + editMaskType.name());
    }
    
    private static String getCacheKey(final PropEditor propEditor) {
        final EValType valType = ValueConverter.serverValType2ClientValType(propEditor.getProperty().getType());
        final EEditMaskType editMaskType = propEditor.getEditMaskType();
        return valType.name() + (editMaskType == null ? "" : "/" + editMaskType.name());
    }    

    public PropEditor getPropArrEditor(final Property property) {
        final PropEditor propEditor = getCachedPropEditor(property);
        return propEditor == null ? new PropArrEditor(property) : propEditor;
    }

    public PropEditor getPropBinEditor(final Property property) {
        final PropEditor propEditor = getCachedPropEditor(property);
        return propEditor == null ? new PropBinEditor(property) : propEditor;
    }

    public PropEditor getPropBoolEditor(final Property property) {
        final PropEditor propEditor = getCachedPropEditor(property);
        return propEditor == null ? new PropBoolEditor(property) : propEditor;
    }

    public PropEditor getPropCharEditor(final Property property) {
        final PropEditor propEditor = getCachedPropEditor(property);
        return propEditor == null ? new PropCharEditor(property) : propEditor;
    }

    public PropEditor getPropDateTimeEditor(final PropertyDateTime property) {
        final PropEditor propEditor = getCachedPropEditor(property);
        return propEditor == null ? new PropDateTimeEditor(property) : propEditor;
    }

    public PropEditor getPropIntEditor(final PropertyInt property) {
        final PropEditor propEditor = getCachedPropEditor(property);
        return propEditor == null ? new PropIntEditor(property) : propEditor;
    }

    public PropEditor getPropListEditor(final Property property) {
        final PropEditor propEditor = getCachedPropEditor(property);
        return propEditor == null ? new PropListEditor(property) : propEditor;
    }

    public PropEditor getPropNumEditor(final PropertyNum property) {
        final PropEditor propEditor = getCachedPropEditor(property);
        return propEditor == null ? new PropNumEditor(property) : propEditor;
    }

    public PropEditor getPropObjectEditor(final Property property) {
        final PropEditor propEditor = getCachedPropEditor(property);
        return propEditor == null ? new PropObjectEditor(property) : propEditor;
    }

    public PropEditor getPropRefEditor(final PropertyRef property) {
        final PropEditor propEditor = getCachedPropEditor(property);
        return propEditor == null ? new PropRefEditor(property) : propEditor;
    }

    public PropEditor getPropStrEditor(final Property property) {
        final PropEditor propEditor = getCachedPropEditor(property);
        return propEditor == null ? new PropStrEditor(property) : propEditor;
    }

    public PropEditor getPropXmlEditor(final PropertyXml property) {
        final PropEditor propEditor = getCachedPropEditor(property);
        return propEditor == null ? new PropXmlEditor(property) : propEditor;
    }

    public void clear() {
        PropEditor propEditor;
        for (Stack<PropEditor> propEditors: propEditorsCache.values()){
            while(!propEditors.isEmpty()){
                propEditor = propEditors.pop();
                propEditor.setInCache(false);
                propEditor.close();                
                propEditor.dispose();
            }
        }
        propEditorsCache.clear();
    }
}
