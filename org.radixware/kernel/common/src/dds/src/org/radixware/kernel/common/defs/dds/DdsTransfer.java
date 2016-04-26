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

package org.radixware.kernel.common.defs.dds;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.radixware.kernel.common.defs.ClipboardSupport;
import org.radixware.kernel.common.defs.ClipboardSupport.Transfer;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.dds.utils.DbNameUtils;
import org.radixware.kernel.common.defs.localization.ILocalizedDef;
import org.radixware.kernel.common.defs.localization.ILocalizingBundleDef;
import org.radixware.kernel.common.defs.localization.IMultilingualStringDef;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;


class DdsTransfer<T extends DdsDefinition> extends ClipboardSupport.Transfer<T> {

    protected Map<Id, IMultilingualStringDef> mlStrings = null;

    public DdsTransfer(T source) {
        super(source);
        if (source.getContainer() != null) {
            Collection<ILocalizedDef.MultilingualStringInfo> usedMlStrings = collectStringInfo(source);
            if (!usedMlStrings.isEmpty()) {
                this.mlStrings = new HashMap<>();
                for (ILocalizedDef.MultilingualStringInfo info : usedMlStrings) {
                    IMultilingualStringDef sd = info.findString();
                    if (sd != null) {
                        this.mlStrings.put(sd.getId(), sd);
                    }
                }
            }
        }
    }

    private static Collection<ILocalizedDef.MultilingualStringInfo> collectStringInfo(RadixObject definition) {
        final ArrayList<ILocalizedDef.MultilingualStringInfo> stringInfo = new ArrayList<>();
        definition.visit(new IVisitor() {
            @Override
            public void accept(RadixObject object) {
                ILocalizedDef def = (ILocalizedDef) object;
                def.collectUsedMlStringIds(stringInfo);
            }
        }, new VisitorProvider() {
            @Override
            public boolean isTarget(RadixObject radixObject) {
                return radixObject instanceof ILocalizedDef;
            }
        });

        return stringInfo;
    }

    @Override
    protected void afterDuplicate() {
        super.afterDuplicate();
        final RadixObject radixObject = getObject();
        if (radixObject instanceof IPlacementSupport) {
            final IPlacementSupport placementSupport = (IPlacementSupport) radixObject;
            final DdsDefinitionPlacement placement = placementSupport.getPlacement();
            placement.setPosX(placement.getPosX() + 20);
            placement.setPosY(placement.getPosY() + 20);
        }
        updateCopyMlStrings();
    }

    @Override
    protected void afterCopy() {
        super.afterCopy();
        updateCopyMlStrings();
    }

    private void updateCopyMlStrings() {
        if (mlStrings != null) {
            final RadixObject radixObject = getObject();
            final Map<Id, IMultilingualStringDef> newMlStrings = new HashMap<>(mlStrings.size());
            final Collection<ILocalizedDef.MultilingualStringInfo> stringInfo = collectStringInfo(radixObject);
            for (ILocalizedDef.MultilingualStringInfo info : stringInfo) {
                IMultilingualStringDef string = mlStrings.get(info.getId());
                if (string != null) {
                    IMultilingualStringDef newString = string.cloneString(null);
                    newMlStrings.put(newString.getId(), newString);
                    info.updateId(newString.getId());
                } else {
                    info.updateId(null);
                }
            }
            this.mlStrings = newMlStrings;
        }
    }

    private static boolean isContain(RadixObjects<? extends RadixObject> radixObjects, String name, RadixObject ignore) {
        for (RadixObject radixObject : radixObjects) {
            if (Utils.equals(radixObject.getName(), name) && radixObject != ignore) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void afterPaste() {
        super.afterPaste();
        final RadixObject radixObject = getObject();

        final RadixObject container = radixObject.getContainer();
        if (container instanceof DdsDefinitions) {
            final DdsDefinitions definitions = (DdsDefinitions) container;
            String name = radixObject.getName();
            if (isContain(definitions, name, radixObject)) {
                while (name != null && !name.isEmpty() && Character.isDigit(name.charAt(name.length() - 1))) {
                    name = name.substring(0, name.length() - 1);
                }
                for (int i = 1; true; i++) {
                    final String newName = name + i;
                    if (!isContain(definitions, newName, radixObject)) {
                        radixObject.setName(newName);
                        break;
                    }
                }
            }
        }

        DbNameUtils.updateAutoDbNames(radixObject);
        RadixObject object = getObject();
        Definition ownerDef = object instanceof DdsDefinition ? (DdsDefinition) object : object.getOwnerDefinition();
        if (ownerDef != null) {
            afterPaste(ownerDef);
        }
    }

    public void afterPaste(Definition ownerDef) {
        if (mlStrings != null && !mlStrings.isEmpty()) {
            if (ownerDef instanceof DdsDefinition) {
                updateCopyMlStrings();
                ILocalizingBundleDef bundle = ((DdsDefinition) ownerDef).findLocalizingBundle();
                if (bundle != null) {
                    for (IMultilingualStringDef string : mlStrings.values()) {
                        assert string.getContainer() == null;
                        bundle.getStrings().getLocal().add((Definition) string);
                    }
                }
            }
        }
    }
}
