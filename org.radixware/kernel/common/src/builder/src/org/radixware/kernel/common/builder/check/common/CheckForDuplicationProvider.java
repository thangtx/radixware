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

package org.radixware.kernel.common.builder.check.common;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.Definitions;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPageDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsPresentationDef;
import org.radixware.kernel.common.defs.ads.ui.AdsLayout;
import org.radixware.kernel.common.defs.ads.ui.AdsMetaInfo;
import org.radixware.kernel.common.defs.ads.ui.AdsUIDef;
import org.radixware.kernel.common.defs.ads.ui.AdsUIUtil;
import org.radixware.kernel.common.defs.ads.ui.AdsWidgetDef;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.enums.ENamingPolicy;
import org.radixware.kernel.common.repository.dds.DdsScripts;
import org.radixware.kernel.common.repository.dds.DdsSegment;
import org.radixware.kernel.common.utils.Utils;


public class CheckForDuplicationProvider {

    private final RadixObjects container;
    private RadixObject ignored;
    private EDefType defType;

    protected CheckForDuplicationProvider(RadixObjects container, RadixObject ignored, EDefType defType) {
        this.container = container;
        this.ignored = ignored;
        this.defType = defType;
    }

    private boolean isUI(RadixObject radixObject) {
        return (radixObject instanceof AdsWidgetDef) || (radixObject instanceof AdsLayout) || (radixObject instanceof AdsLayout.SpacerItem);
    }

    public RadixObject findUIDuplicated(final String newName) {
        RadixObject c = ignored.getContainer();
        if (c instanceof AdsUIDef) {
            return null;
        }

        while (!(c instanceof RadixObjects)) {
            c = c.getContainer();
        }
        c = c.getContainer();

        final Set<RadixObject> objects = new HashSet<RadixObject>();
        if (c instanceof AdsWidgetDef) {
            AdsWidgetDef widget = (AdsWidgetDef) c;
            if (AdsUIUtil.getUiClassName(widget).equals(AdsMetaInfo.TAB_WIDGET_CLASS)) {
                for (AdsWidgetDef w : widget.getWidgets()) {
                    objects.add(w);
                }
            } else {
                for (AdsWidgetDef w : widget.getWidgets()) {
                    if (AdsUIUtil.getUiClassName(w).equals(AdsMetaInfo.WIDGET_CLASS) && w.getLayout() != null) {
                        objects.add(w.getLayout());
                    } else {
                        objects.add(w);
                    }
                }
            }
        }
        if (c instanceof AdsLayout) {
            AdsLayout layout = (AdsLayout) c;
            for (AdsLayout.Item item : layout.getItems()) {
                final RadixObject o = AdsUIUtil.getItemNode(item);
                if (!(o instanceof AdsLayout.SpacerItem)) {
                    objects.add(o);
                }
            }
        }

        if (!objects.contains(ignored)) {
            return null;
        }

        for (RadixObject o : objects) {
            if (Utils.equals(o, ignored)) {
                continue;
            }
            if (Utils.equals(o.getName(), newName)) {
                return o;
            }
        }

        return null;
    }

    public RadixObject findDuplicated(final String newName) {
        if (isUI(ignored)) {
            return findUIDuplicated(newName);
        }

        if (container == null || newName == null || newName.isEmpty()) {
            return null;
        }

        final Iterable<RadixObject> iterator;


        final RadixObject owner = container.getContainer();
        if (owner instanceof ExtendableDefinitions) {
            final ExtendableDefinitions extendableDefinitions = (ExtendableDefinitions) owner;
            final List<RadixObject> list = extendableDefinitions.get(ExtendableDefinitions.EScope.ALL);
            final RadixObject any = list == null || list.isEmpty() ? null : list.get(0);
            if (any instanceof AdsPresentationDef || any instanceof AdsEditorPageDef) { // :-( nonstandard
                iterator = container;
            } else {
                iterator = list;
            }
        } else {
            iterator = container;
        }

        if ((owner instanceof DdsSegment) && newName.equalsIgnoreCase("scripts")) {
            final DdsScripts scripts = ((DdsSegment) owner).getScripts();
            if (scripts != ignored) {
                return scripts;
            }
        }

        for (RadixObject sibling : iterator) {
            if (sibling == ignored) {
                continue;
            }
            if (defType == EDefType.DOMAIN && getDefType(sibling) != defType) { // :-( nonstandard
                continue;
            }
            final String siblingName = sibling.getName();
            if (newName.equals(siblingName)) {//was equalsIgnoreCase(). i do not know why??
                final ENamingPolicy siblingNamingPolicy = sibling.getNamingPolicy();
                if (siblingNamingPolicy == ENamingPolicy.UNIQUE_IDENTIFIER) { // otherwise, for example, it is impossible to create DDS Function with 'CustomText' name.
                    return sibling;
                }
            }
        }

        return null;
    }

    private static EDefType getDefType(RadixObject radixObject) {
        if (radixObject instanceof AdsDefinition) {
            return ((AdsDefinition) radixObject).getDefinitionType();
        } else {
            return null;
        }
    }

    public static class Factory {

        private Factory() {
        }

        public static CheckForDuplicationProvider newForCreation(final RadixObjects container, final EDefType defType) {
            return new CheckForDuplicationProvider(container, null, defType);
        }

        private static RadixObject resolveSingleSideLink(RadixObjects container, RadixObject radixObject) {
            final boolean singleSidedLink = (container instanceof Definitions) && (radixObject instanceof Definition) && !container.contains(radixObject);
            if (singleSidedLink) {
                // DdsTableDef and some other definitions single sided linked to container until its in editor
                final Definition definition = (Definition) radixObject;
                final Definitions definitions = (Definitions) container;
                return definitions.findById(definition.getId());
            } else {
                return radixObject;
            }
        }

        public static CheckForDuplicationProvider newForRenaming(final RadixObject radixObject) {
            final RadixObjects container = ((radixObject.getContainer() instanceof RadixObjects) ? (RadixObjects) radixObject.getContainer() : null);
            final RadixObject ignored = resolveSingleSideLink(container, radixObject);
            final EDefType defType = getDefType(radixObject);
            return new CheckForDuplicationProvider(container, ignored, defType);
        }

        public static CheckForDuplicationProvider newForCheck(final RadixObject radixObject) {
            final RadixObjects container = ((radixObject.getContainer() instanceof RadixObjects) ? (RadixObjects) radixObject.getContainer() : null);
            final EDefType defType = getDefType(radixObject);
            return new CheckForDuplicationProvider(container, radixObject, defType);
        }
    }
}
