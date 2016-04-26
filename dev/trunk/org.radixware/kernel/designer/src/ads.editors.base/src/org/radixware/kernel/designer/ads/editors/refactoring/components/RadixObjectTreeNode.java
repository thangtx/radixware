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
package org.radixware.kernel.designer.ads.editors.refactoring.components;

import java.awt.Image;
import org.openide.nodes.Children;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObjectIcon;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.designer.common.dialogs.names.NamingService;

public class RadixObjectTreeNode<T extends RadixObject> extends TreeNode<T> {

    public static String getDisplayName(RadixObject object, String location) {
        if (object == null) {
            return "<Undefined>";
        }

        if (object instanceof AdsMethodDef) {
            final AdsMethodDef method = (AdsMethodDef) object;

            return method.getProfile().getName() + location;
        }

        if (object instanceof AdsPropertyDef) {
            final AdsPropertyDef property = (AdsPropertyDef) object;

            return property.getProfile().getName() + location;
        }

        return object.getName() + location;
    }

    public static String getHtmlDisplayName(RadixObject object, String location) {
        if (object == null) {
            return "<Undefined>";
        }

//        if (object instanceof AdsMethodDef) {
//            final AdsMethodDef method = (AdsMethodDef) object;
//
//            return NamingService.getDefault().getHtmlSignature(method) + location;
//        }
//
//        if (object instanceof AdsPropertyDef) {
//            final AdsPropertyDef property = (AdsPropertyDef) object;
//
//            return NamingService.getDefault().getHtmlSignature(property) + location;
//        }
        return NamingService.getDefault().getHtmlSignature(object) + (object instanceof AdsClassDef ? location : "");
    }

    public static String getObjectLocation(RadixObject object) {
        if (object == null) {
            return "<Undefined>";
        }
        if (object.isInBranch()) {
            return "    [" + object.getLayer().getName() + "::" + object.getModule().getName() + "]";
        } else {
            return object.getName();
        }
    }

    public RadixObjectTreeNode(Children children, T object) {
        super(children, object);
    }

    @Override
    public String getHtmlDisplayName() {
        return getHtmlDisplayName(getDisplayableObject(), getLocation());
    }

    @Override
    public String getDisplayName() {
        return getDisplayName(getDisplayableObject(), getLocation());
    }

    protected String getLocation() {
        return getObjectLocation(getObject());
    }

    protected T getDisplayableObject() {
        return (T) getObject().getDefinition();
    }

    @Override
    public Image getIcon(int type) {
        RadixIcon icon = getObject() != null ? getObject().getIcon() : null;
        if (icon == null) {
            return RadixObjectIcon.UNKNOWN.getImage();
        }
        Image image = icon.getImage();
        return image == null ? RadixObjectIcon.UNKNOWN.getImage() : image;
    }
}
