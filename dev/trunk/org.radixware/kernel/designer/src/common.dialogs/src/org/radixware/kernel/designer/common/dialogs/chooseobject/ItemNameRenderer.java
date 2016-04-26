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

package org.radixware.kernel.designer.common.dialogs.chooseobject;

import javax.swing.JList;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.AdsLibUserFuncWrapper;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.designer.common.general.displaying.HtmlNameSupport;
import org.radixware.kernel.designer.common.general.displaying.HtmlNameSupportsManager;

/**
 * Choose Radix object item visual renderer.
 *
 a-kiliyevich
 */
public class ItemNameRenderer extends AbstractItemRenderer {

    public ItemNameRenderer(JList list) {
        super(list);
    }

    protected RadixObject getRadixObject(Object object) {
        if (object instanceof RadixObject) {
            return (RadixObject) object;
        } else {
            return null;
        }
    }

    @Override
    public String getObjectName(Object object) {
        final RadixObject radixObject = getRadixObject(object);
        if (radixObject != null) {
            final HtmlNameSupport htmlNameSupport = HtmlNameSupportsManager.newInstance(radixObject);
            final String name = "<html>" + htmlNameSupport.getHtmlName();
            return name;
        } else {
            return String.valueOf(object);
        }
    }

    @Override
    public RadixIcon getObjectIcon(Object object) {
        final RadixObject radixObject = getRadixObject(object);
        if (radixObject != null) {
            return radixObject.getIcon();
        } else {
            return null;
        }
    }

    @Override
    public String getObjectLocation(Object object) {
        final RadixObject radixObject = getRadixObject(object);
        if (radixObject != null) {
            if(radixObject instanceof AdsLibUserFuncWrapper){
                final AdsLibUserFuncWrapper libUserFunc = (AdsLibUserFuncWrapper)radixObject;
                return libUserFunc.getLibName();
            }else{
                final RadixObject ownerForQualifedName = radixObject.getOwnerForQualifedName();
                final String ownerPath = ownerForQualifedName != null ? "  " + ownerForQualifedName.getQualifiedName() : "  ";
                return ownerPath;
            }
        } else {
            return "";
        }
    }

    @Override
    public RadixIcon getObjectLocationIcon(Object object) {
        final RadixObject radixObject = getRadixObject(object);
        if (radixObject != null) {
            if(radixObject instanceof AdsLibUserFuncWrapper){
                return RadixWareIcons.JML_EDITOR.USERFUNC_LIB;
            }else{
                final RadixObject ownerForQualifedName = radixObject.getOwnerForQualifedName();
                if (ownerForQualifedName != null) {
                    return ownerForQualifedName.getIcon();
                } else {
                    return null;
                }
            }
        } else {
            return null;
        }
    }
}
