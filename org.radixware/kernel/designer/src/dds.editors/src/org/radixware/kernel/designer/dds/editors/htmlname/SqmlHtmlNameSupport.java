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

package org.radixware.kernel.designer.dds.editors.htmlname;

import java.awt.Color;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObject.RenameEvent;
import org.radixware.kernel.common.defs.RadixObject.RenameListener;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.defs.RadixObjects.ContainerChangedEvent;
import org.radixware.kernel.common.defs.dds.DdsModelDef;
import org.radixware.kernel.common.sqml.Sqml;
import org.radixware.kernel.designer.common.annotations.registrators.HtmlNameSupportFactoryRegistration;
import org.radixware.kernel.designer.common.general.displaying.HtmlNameSupport;
import org.radixware.kernel.designer.common.general.displaying.IHtmlNameSupportFactory;


public class SqmlHtmlNameSupport extends HtmlNameSupport {

    private final RadixObjects.ContainerChangesListener containerChangesListener = new RadixObjects.ContainerChangesListener() {

        @Override
        public void onEvent(ContainerChangedEvent e) {
            fireChanged();
        }
    };
    private RenameListener ownerRenameListener = null;

    public SqmlHtmlNameSupport(Sqml sqml) {
        super(sqml);
        sqml.getItems().getContainerChangesSupport().addEventListener(containerChangesListener);
    }

    public Sqml getSqml() {
        return (Sqml) getRadixObject();
    }

    @Override
    public Color getColor() {
        final Sqml sqml = getSqml();

        if (sqml.getItems().isEmpty()) {
            return Color.GRAY;
        } else {
            return super.getColor();
        }
    }

    @Override
    public Color getEditorColor() {
        return super.getColor(); // super
    }

    @Override
    public String getDisplayName() {
        final Sqml sqml = getSqml();

        final RadixObject owner = sqml.getOwnerDefinition();
        if (owner instanceof DdsModelDef) {
            DdsModelDef model = (DdsModelDef) owner;
            if (sqml == model.getBeginScript()) {
                return "Begin Script";
            } else if (sqml == model.getEndScript()) {
                return "End Script";
            }
        }

        return super.getDisplayName();
    }

    @Override
    public String getEditorDisplayName() {
        final Sqml sqml = getSqml();

        String name = sqml.getName();
        RadixObject owner = sqml.getOwnerDefinition();
        if (owner instanceof DdsModelDef) {
            final DdsModelDef model = (DdsModelDef) owner;
            if (sqml == model.getBeginScript()) {
                name = "BeginScript";
            } else if (sqml == model.getEndScript()) {
                name = "EndScript";
            }
            owner = owner.getOwnerDefinition();
        }

        if (owner != null) {
            if (ownerRenameListener == null) {
                ownerRenameListener = new RadixObject.RenameListener() {

                    @Override
                    public void onEvent(RenameEvent e) {
                        fireChanged();
                    }
                };
                owner.addRenameListener(ownerRenameListener);
            }
            return owner.getName() + '.' + name;
        } else {
            return name;
        }
    }

    @HtmlNameSupportFactoryRegistration
    public static final class Factory implements IHtmlNameSupportFactory {

        public Factory() {
        }

        @Override
        public HtmlNameSupport newInstance(RadixObject object) {
            return new SqmlHtmlNameSupport((Sqml) object);
        }
    }
}
