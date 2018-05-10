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

package org.radixware.kernel.designer.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.Definitions;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.IAccessible;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodThrowsList;
import org.radixware.kernel.common.defs.ads.clazz.members.MethodParameter;
import org.radixware.kernel.common.defs.ads.clazz.members.MethodReturnValue;
import org.radixware.kernel.common.defs.localization.ILocalizedDescribable;
import org.radixware.kernel.common.enums.EAccess;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.utils.RadixObjectsUtils;
import org.radixware.kernel.designer.api.filters.DescriptionMode;
import org.radixware.kernel.designer.api.filters.RadixObjectNameFilter;


public class ApiFilter {

    private EAccess[] accessLevel = {EAccess.PUBLIC, EAccess.PROTECTED};
    private boolean includeNotPublished;
    private DescriptionMode[] descriptionModes = null;
    private final RadixObjectNameFilter textFilter = new RadixObjectNameFilter();

    ApiFilter() {
    }
    
    public boolean accept(RadixObject object) {
        return acceptByText(object)&&
                acceptByAccess(object)  &&
                acceptByDescriptionMode(object);
                
    }
    
    private boolean acceptByAccess(RadixObject object){
        if (object instanceof Definition) {
            if (!includeNotPublished && !((Definition) object).isPublished()) {
                return false;
            }

            if (object instanceof IAccessible) {
                final EAccess access = ((IAccessible) object).getAccessFlags().getAccessMode();
                return checkAccess(access);
            }

            if (object instanceof AdsDefinition) {
                final EAccess access = ((AdsDefinition) object).getAccessMode();
                return checkAccess(access);
            }
        }
        return true;
    }
  
    private boolean acceptByDescriptionMode(final RadixObject object) {
        if (object instanceof MethodParameter || object instanceof AdsMethodThrowsList.ThrowsListItem) {
            return true;
        }
        
        if (descriptionModes == null) {
            if (object.getDescription() == null || object.getDescription().isEmpty()) {
                if (object instanceof ILocalizedDescribable) {
                    if (((ILocalizedDescribable) object).getDescriptionId() == null) {
                        return false;
                    }
                } else {
                    return false;
                }
            }
            return true;
        }
        
        final List<DescriptionMode> list = new ArrayList<>(Arrays.asList(descriptionModes));
        final boolean[] result = new boolean[1];
        result[0] = acceptByDescriptionMode(object, list);
        
        if (!result[0]){
            object.visitChildren(new IVisitor() {
                @Override
                public void accept(RadixObject radixObject) {
                    if (result[0]) return;
                    
                    if (radixObject instanceof Definitions) {

                        final Definitions<Definition> defs = (Definitions) radixObject;
                        for (Definition def : defs){
                            if (acceptByAccess(def) && acceptByDescriptionMode(def, list)){
                                result[0] = true;
                            }
                            break;
                        }
                    } else if (radixObject instanceof MethodReturnValue){
                        MethodReturnValue methodReturnValue = (MethodReturnValue) radixObject;
                        if (methodReturnValue.getType().isVoid()) {
                            return;
                        }
                        if (acceptByDescriptionMode(methodReturnValue, list)) {
                            result[0] = true;
                        }
                    }
                }
            }, new VisitorProvider() {
                @Override
                public boolean isTarget(RadixObject radixObject) {
                    return acceptByAccess(radixObject) && radixObject instanceof Definitions || radixObject instanceof MethodReturnValue;
                }
            });
        }
        return result[0];
    }
    
    private boolean acceptByDescriptionMode(RadixObject object, List<DescriptionMode> list){
        if (list.contains(DescriptionMode.SHOW_ALL)){
            return true;
        }
        
        boolean legacy = list.contains(DescriptionMode.LEGACY_DESCRIPTION);
        boolean isDescriptionLegacy = object.getDescription() != null && !object.getDescription().isEmpty();
        list.remove(DescriptionMode.LEGACY_DESCRIPTION);
        
        if (isDescriptionLegacy) {
            return legacy;
        }
        
        boolean result = list.isEmpty();
        if (object instanceof ILocalizedDescribable) {
            ILocalizedDescribable def = (ILocalizedDescribable) object;
            if (def.getDescriptionId() != null){
                if (result) {
                    return false;
                }
                
                for (final DescriptionMode mode : list) {
                    if (def.getDescription(mode.getLanguage()) != null) {
                        result = true;
                    }
                }
            }
        } else {
            return true;
        }
       
        
        return result;
    }
    
    private boolean acceptByText(RadixObject object){
        if(object instanceof MethodParameter || object instanceof AdsMethodThrowsList.ThrowsListItem){
            return true;
        }
        return textFilter.accept(object);
    }

    synchronized void setAccessLevel(EAccess... level) {
        this.accessLevel = level;
    }

    synchronized void setShowNotPublished(boolean showNotPublished) {
        this.includeNotPublished = showNotPublished;
    }

    private boolean checkAccess(EAccess access) {

        if (accessLevel == null) {
            return true;
        }

        for (final EAccess a : accessLevel) {
            if (a == access) {
                return true;
            }
        }
        return false;
    }

    synchronized void setDescriptionModes(DescriptionMode... descriptionModes) {
        this.descriptionModes = descriptionModes;
    }

    public RadixObjectNameFilter getTextFilter() {
        return textFilter;
    }
}
