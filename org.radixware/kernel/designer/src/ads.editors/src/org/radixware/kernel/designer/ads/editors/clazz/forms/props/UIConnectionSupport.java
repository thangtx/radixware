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
package org.radixware.kernel.designer.ads.editors.clazz.forms.props;

import java.beans.PropertyEditor;
import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.lang.reflect.InvocationTargetException;
import org.openide.nodes.PropertySupport;
import org.radixware.kernel.common.defs.ads.ui.AdsAbstractUIDef;
import org.radixware.kernel.common.defs.ads.ui.AdsCustomWidgetDef;
import org.radixware.kernel.common.defs.ads.ui.AdsMetaInfo;
import org.radixware.kernel.common.defs.ads.ui.AdsUIConnection;
import org.radixware.kernel.common.defs.ads.ui.AdsUIItemDef;
import org.radixware.kernel.common.defs.ads.ui.AdsUISignalDef;

public class UIConnectionSupport extends PropertySupport<AdsUIConnection> {

    private final AdsUIItemDef widget;
    private final AdsAbstractUIDef uiDef;
    private final AdsUIConnection conn;
    private Reference<PropertyEditor> edRef = null;

    public UIConnectionSupport(AdsUIConnection conn, AdsAbstractUIDef uiDef, AdsUIItemDef widget) {
        super(conn.getSignalName(), AdsUIConnection.class, getTitle(conn, widget), getTitle(conn, widget), true, !widget.isReadOnly());
        this.widget = widget;
        this.conn = conn;
        this.uiDef = uiDef;
    }

    public AdsAbstractUIDef getRootUIDef() {
        return uiDef;
    }

    private static String getTitle(AdsUIConnection conn, AdsUIItemDef widget) {
        if (conn.getSignalId() != null) {
            AdsAbstractUIDef customUI = AdsMetaInfo.getCustomUI(widget);
            if (customUI instanceof AdsCustomWidgetDef) {
                AdsUISignalDef signal = ((AdsCustomWidgetDef) customUI).getSignals().findById(conn.getSignalId());
                if (signal != null) {
                    return signal.getName();
                }
            }
        }
        return conn.getSignalName();
    }

    public AdsUIItemDef getWidget() {
        return widget;
    }

    @Override
    public AdsUIConnection getValue() throws IllegalAccessException, InvocationTargetException {
        return conn;
    }

    @Override
    public void setValue(AdsUIConnection c) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    }

    @Override
    public PropertyEditor getPropertyEditor() {
        edRef = new SoftReference<PropertyEditor>(new ConnectionEditor(this));
        if (edRef != null) {
            return edRef.get();
        }
        return super.getPropertyEditor();
    }
}

/*
public class UIConnectionSupport extends PropertySupport<Boolean> {

    private final AdsWidgetDef widget;
    private final AdsUIConnection conn;

    public UIConnectionSupport(AdsUIConnection conn, AdsWidgetDef widget) {
        super(conn.getSignalName(), Boolean.TYPE, getTitle(conn, widget), getTitle(conn, widget), true, !widget.isReadOnly());
        this.widget = widget;
        this.conn = conn;
    }

    private static String getTitle(AdsUIConnection conn, AdsWidgetDef widget) {
        if (conn.getSignalId() != null) {
            AdsCustomWidgetDef customUI = AdsMetaInfo.getCustomUI(widget);
            if (customUI != null) {
                AdsUISignalDef signal = customUI.getSignals().findById(conn.getSignalId());
                if (signal != null)
                    return signal.getName();
            }
        }
        return conn.getSignalName();
    }

    public AdsWidgetDef getWidget() {
        return widget;
    }

    public AdsUIConnection getConnection() {
        return conn;
    }

    @Override
    public Boolean getValue() throws IllegalAccessException, InvocationTargetException {
        AdsUIDef uiDef = AdsUIUtil.getUiDef(widget);
        return Boolean.valueOf(uiDef.getConnections().get(widget, conn.getSignalName()) != null);
    }

    @Override
    public void setValue(Boolean val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        AdsUIDef uiDef = AdsUIUtil.getUiDef(widget);
        AdsModelClassDef model = AdsUIUtil.getModelByUI(uiDef);
        if (val.equals(Boolean.TRUE)) {
            if (model == null) {
                DialogUtils.messageInformation(NbBundle.getMessage(getClass(), "ModelNotFound"));
                return;
            }
            AdsUIConnection c = conn;
            c.setSenderId(widget.getId());
            c.setSlotId(Id.Factory.newInstance(EDefinitionIdPrefix.ADS_CLASS_METHOD));
            uiDef.getConnections().add(c);
            createHandler(c);
        } else {
            if (!DialogUtils.messageConfirmation(NbBundle.getMessage(getClass(), "DeleteConfirmation"))) {
                return;
            }
            AdsUIConnection c = uiDef.getConnections().get(widget, conn.getSignalName());
            if (c != null)
                uiDef.getConnections().remove(c);
            dropHandler(c);
        }
    }

    private void dropHandler(AdsUIConnection c) {
        AdsUIDef uiDef = AdsUIUtil.getUiDef(widget);
        AdsModelClassDef model = AdsUIUtil.getModelByUI(uiDef);
        if (model == null)
            return;

        AdsPresentationSlotMethodDef method = (AdsPresentationSlotMethodDef)model.getMethods().getLocal().findById(c.getSlotId());
        if (method != null) {
            method.delete();
        }
    }

    private void createHandler(AdsUIConnection c) {
        AdsUIDef uiDef = AdsUIUtil.getUiDef(widget);
        AdsModelClassDef model = AdsUIUtil.getModelByUI(uiDef);
        if (model == null)
            return;

        AdsPresentationSlotMethodDef method = AdsPresentationSlotMethodDef.Factory.newInstance(c);
        model.getMethods().getLocal().add(method);
    }
}
*/
