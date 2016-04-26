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

package org.radixware.kernel.common.defs.ads.clazz.members;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ads.IDependentId;
import org.radixware.kernel.common.defs.ads.clazz.AdsModelClassDef;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.defs.ads.ui.AdsAbstractUIDef;
import org.radixware.kernel.common.defs.ads.ui.AdsCustomWidgetDef;
import org.radixware.kernel.common.defs.ads.ui.AdsMetaInfo;
import org.radixware.kernel.common.defs.ads.ui.AdsUIConnection;
import org.radixware.kernel.common.defs.ads.ui.AdsUIItemDef;
import org.radixware.kernel.common.defs.ads.ui.AdsUISignalDef;
import org.radixware.kernel.common.defs.ads.ui.AdsUIUtil;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.EMethodNature;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.adsdef.AbstractMethodDefinition;


public class AdsPresentationSlotMethodDef extends AdsUserMethodDef implements IDependentId {

    public static class Factory {

        public static AdsPresentationSlotMethodDef newInstance(AdsPresentationSlotMethodDef source, boolean overwrite) {
            return new AdsPresentationSlotMethodDef(source, overwrite);
        }

        public static AdsPresentationSlotMethodDef newInstance(AdsUIConnection conn, AdsUIItemDef widget) {
            return new AdsPresentationSlotMethodDef(conn, widget);
        }
    }

    public AdsPresentationSlotMethodDef(AdsUIConnection c, AdsUIItemDef widget) {
        super(Id.Factory.newInstance(EDefinitionIdPrefix.ADS_CLASS_METHOD), "");
        setName(createName(c, widget));

        final Profile profile = getProfile();
        for (AdsUIConnection.Parameter param : c.getSignalParams(widget)) {
            profile.getParametersList().add(MethodParameter.Factory.newInstance(param.getName(), param.getType()));
        }
        if (c.getSlotReturnType() != AdsTypeDeclaration.VOID) {
            profile.getReturnValue().setType(c.getSlotReturnType());
        }
        setUsageEnvironment(widget.getUsageEnvironment());
    }

    public AdsPresentationSlotMethodDef(AbstractMethodDefinition xMethod) {
        super(xMethod);
    }

    public AdsPresentationSlotMethodDef(AdsPresentationSlotMethodDef source, boolean overwrite) {
        super(source, overwrite);
    }

    @Override
    public EMethodNature getNature() {
        return EMethodNature.PRESENTATION_SLOT;
    }

    public AdsAbstractUIDef findUI() {
        return AdsUIUtil.findUIByModel((AdsModelClassDef) getOwnerClass(), getUsageEnvironment());
    }

    public Set<AdsUIConnection> findUIConnections() {
        AdsAbstractUIDef ui = findUI();
        if (ui != null) {
            return ui.getConnections().getBySlot(getId());
        }
        return null;
    }

    public Set<AdsUIItemDef> findWidgets() {
        Set<AdsUIConnection> cs = findUIConnections();
        if (cs != null) {
            Set<AdsUIItemDef> ws = new HashSet<AdsUIItemDef>();
            for (AdsUIConnection c : cs) {
                ws.add(c.getSender());
            }
            return ws;
        }
        return null;
    }

    /*
    private class WidgetNameListener implements RenameListener {
    
    private WeakReference<AdsWidgetDef> ref;
    
    WidgetNameListener(AdsWidgetDef w) {
    setWidget(w);
    }
    
    @Override
    public void onEvent(RenameEvent e) {
    AdsPresentationSlotMethodDef.super.setName(checkName());
    AdsPresentationSlotMethodDef.this.fireNameChange();
    }
    
    AdsWidgetDef getWidget() {
    return this.ref == null ? null : this.ref.get();
    }
    
    private void setWidget(AdsWidgetDef w) {
    if (w != null) {
    AdsWidgetDef old = getWidget();
    if (old != null) {
    old.removeRenameListener(this);
    }
    this.ref = new WeakReference<AdsWidgetDef>(w);
    w.addRenameListener(this);
    }
    }
    
    String checkName() {
    AdsUIConnection c = findUIConnection();
    if (c != null) {
    AdsWidgetDef w = c.getSender();
    if (w != null) {
    if (w != getWidget()) {
    setWidget(w);
    }
    return createName(c, w);
    }
    }
    return null;
    }
    }
    private final WidgetNameListener connectionListener = new WidgetNameListener(null);
     */
    @Override
    public String getName() {
        return super.getName();
        /*
        String calcName = connectionListener.checkName();
        if (calcName != null) {
        return calcName;
        } else {
        return super.getName();
        }
         */
    }

    private String createName(AdsUIConnection c, AdsUIItemDef widget) {
        AdsUIItemDef sender = widget == null ? c.getSender() : widget;
        if (c.getSignalId() != null) {
            AdsAbstractUIDef customUI = AdsMetaInfo.getCustomUI(sender);
            if (customUI instanceof AdsCustomWidgetDef) {
                AdsUISignalDef signal = ((AdsCustomWidgetDef) customUI).getSignals().findById(c.getSignalId());
                if (signal != null) {
                    return sender.getName() + "_" + signal.getName();
                }
            }
        }
        return sender.getName() + "_" + c.getSignalName();
    }

    private String createName(AdsUIConnection c) {
        return createName(c, null);
    }

    @Override
    public boolean delete() {
        /*
        Set<AdsUIConnection> cs = findUIConnections();
        if (cs != null)
        for (AdsUIConnection c: cs) {
        c.delete();
        }
         */
        return super.delete();
    }

    @Override
    public void collectDependences(List<Definition> list) {
        super.collectDependences(list);
        Set<AdsUIItemDef> ws = findWidgets();
        if (ws != null) {
            list.addAll(ws);
        }
    }

    @Override
    public boolean canDelete() {
        if (getOwnerClass() != null && getOwnerClass().isReadOnly()) {
            return false;
        }
        return true;
    }

    @Override
    public boolean isReflectiveCallable() {
        return true;
    }
}
