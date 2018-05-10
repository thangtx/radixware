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

package org.radixware.kernel.common.defs.ads.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.AdsModelClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef.MethodValue;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPresentationSlotMethodDef;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.ENamingPolicy;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.schemas.ui.Connection;


public class AdsUIConnection extends RadixObject implements Cloneable {

    private Id senderId = null;
    private Id receiverId = null;
    private Id signalId = null;
    private String signal = null;
    private String interfaceName = null;
    private String registrator = null;
    private Id slotId = null;
    private String returnType;

    public static class Parameter extends MethodValue {

        public Parameter(String name, AdsTypeDeclaration decl) {
            super(decl, name, "", null);
        }

        @Override
        public boolean isDescriptionInheritable() {
            return false;
        }
        
    }

    public AdsUIConnection(String name, String interfaceName, String registrator, String returnType) {
        signal = name;
        this.interfaceName = interfaceName;
        this.registrator = registrator;
        this.returnType = returnType;
    }

    public AdsUIConnection(AdsUISignalDef sig) {
        signalId = sig.getId();
    }

    public AdsUIConnection(Connection c) {
        this.receiverId = Id.Factory.loadFrom(c.getReceiver());
        this.senderId = Id.Factory.loadFrom(c.getSender());

        String sig = c.getSignal();
        if (sig != null && sig.startsWith(EDefinitionIdPrefix.SIGNAL.getValue())) {
            this.signalId = Id.Factory.loadFrom(sig);
        } else {
            this.signal = sig;
        }
//        if ("rowSelectionChanged(org.radixware.wps.rwt.Grid.Row oldSelectedRow ,org.radixware.wps.rwt.Grid.Row newSelectedRow)".equals(c.getSignal())
//                && "addSelectionListener".equals(c.getRegistrator())) {
//            this.signal = "currentRowChanged(org.radixware.wps.rwt.Grid.Row oldSelectedRow ,org.radixware.wps.rwt.Grid.Row newSelectedRow)";
//            this.registrator = "addCurrentRowListener";
//        } else {
//            this.registrator = c.getRegistrator();
//        }

        this.registrator = c.getRegistrator();
        this.interfaceName = c.getInterface();

        this.returnType = c.getReturnType();

        this.slotId = Id.Factory.loadFrom(c.getSlot());
    }

    public void appendTo(Connection c) {
        if (receiverId != null) {
            c.setReceiver(receiverId.toString());
        }

        if (senderId != null) {
            c.setSender(senderId.toString());
        }

        if (signalId != null) {
            c.setSignal(signalId.toString());
        } else {
            c.setSignal(signal);
        }

        if (slotId != null) {
            c.setSlot(slotId.toString());
        }
        if (interfaceName != null) {
            c.setInterface(interfaceName);
        }
        if (registrator != null) {
            c.setRegistrator(registrator);
        }
        if (returnType != null) {
            c.setReturnType(returnType);
        }
    }

    public AdsUIConnection duplicate() {
        try {
            return (AdsUIConnection) clone();
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
        }
        return null;
    }

// signals
    public String getSignalName() {
        if (signalId != null) {
            return signalId.toString();
        }
        int idx = signal.indexOf('(');
        if (idx >= 0) {
            return signal.substring(0, idx);
        }
        return signal;
    }

    public AdsTypeDeclaration getSlotReturnType() {
        if (returnType != null && !returnType.isEmpty()) {
            return AdsTypeDeclaration.Factory.newPlatformClass(returnType);
        } else {
            return AdsTypeDeclaration.VOID;
        }
    }

    public List<Parameter> getSignalParams() {
        return getSignalParams(null);
    }

    public List<Parameter> getSignalParams(AdsUIItemDef sender) {
        final List<Parameter> params = new ArrayList<Parameter>();

        int idx = 0;
        if (signalId != null) {
            AdsUISignalDef sig = getUISignal(sender);
            if (sig != null) {
                for (AdsTypeDeclaration type : sig.getTypes().list()) {
                    params.add(new Parameter("param" + (idx++), type));
                }
            }
            return params;
        }

        if (getSenderId() != null) {
            final AdsDefinition widget = getSender();
            if (widget != null) {
                String clazz = AdsUIUtil.getQtClassName(widget);
                while (clazz != null) {
                    AdsUIConnection origConn = AdsMetaInfo.getConnByName(clazz, getSignalName(), this);
                    if (origConn != null) {
                        if (!Utils.equals(origConn.getSignal(), getSignal())) {
                            setSignal(origConn.getSignal());
                        }
                        break;
                    }
                    clazz = AdsMetaInfo.getExtends(clazz, this);
                }
            }
        }

        int i = signal.indexOf('(');
        int j = signal.indexOf(')');
        if (i < 0 || j < 0 || i > j) {
            return params;
        }

        String s = signal.substring(i + 1, j).trim();
        if (s.length() == 0) {
            return params;
        }

        String[] ps = s.split(",");
        for (String param : ps) {
            String type = param.trim();
            int spaceIndex = type.indexOf(' ');
            String name;
            if (spaceIndex > 0) {
                name = type.substring(spaceIndex + 1);
                type = type.substring(0, spaceIndex);
            } else {
                name = "param" + (idx++);
            }

            String[] couple = type.split(" ");
            if (couple.length > 1) {
                type = couple[0];
                name = couple[couple.length - 1];
            }

            final EValType t = EValType.getForName(type);
            final AdsTypeDeclaration typeDecl = (t == null) ? AdsTypeDeclaration.Factory.newPlatformClass(type) : AdsTypeDeclaration.Factory.newInstance(t);
            params.add(new Parameter(name, typeDecl));
        }

        return params;
    }

    public AdsUISignalDef getUISignal() {
        return getUISignal(null);
    }

    public AdsUISignalDef getUISignal(AdsUIItemDef sender) {
        if (signalId != null) {
            AdsAbstractUIDef customUI = AdsMetaInfo.getCustomUI(sender != null ? sender : getSender());
            if (customUI instanceof AdsCustomWidgetDef) {
                return ((AdsCustomWidgetDef) customUI).getSignals().findById(signalId);
            }
        }
        return null;
    }

    public String getSignal() {
        return signal;
    }

    public void setSignal(String signal) {
        this.signal = signal;
        setEditState(EEditState.MODIFIED);
    }

    public Id getSignalId() {
        return signalId;
    }

    public void setSignalId(Id id) {
        signalId = id;
        setEditState(EEditState.MODIFIED);
    }

// slots
    public Id getSlotId() {
        return slotId;
    }

    public AdsPresentationSlotMethodDef getSlot() {
        AdsModelClassDef model = AdsUIUtil.getModelByUI(AdsUIUtil.getUiDef(this), false);
        if (model != null) {
            return (AdsPresentationSlotMethodDef) model.getMethods().getLocal().findById(getSlotId());
        }
        return null;
    }

    public void setSlotId(Id id) {
        slotId = id;
        setEditState(EEditState.MODIFIED);
    }

// sender
    public Id getSenderId() {
        return senderId;
    }

    public AdsUIItemDef getSender() {
        AdsAbstractUIDef ui = AdsUIUtil.getUiDef(this);
        assert ui != null : "ui can not be null";
        return ui.getWidget().findWidgetById(senderId);
    }

    public void setSenderId(Id senderId) {
        this.senderId = senderId;
        setEditState(EEditState.MODIFIED);
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public String getRegistrator() {
        return registrator;
    }

    public void setRegistrator(String registrator) {
        this.registrator = registrator;
        setEditState(EEditState.MODIFIED);
    }

// reseiver
    public Id getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Id receiverId) {
        this.receiverId = receiverId;
        setEditState(EEditState.MODIFIED);
    }

    @Override
    public void collectDependences(List<Definition> list) {
        super.collectDependences(list);
        Definition senderDef = getSender();
        if (senderDef != null) {
            list.add(senderDef);
        }
        Definition signalDef = getUISignal();
        if (signalDef != null) {
            list.add(signalDef);
        }
        Definition slotDef = getSlot();
        if (slotDef != null) {
            list.add(slotDef);
        }
    }

    @Override
    public boolean delete() {
        //AdsUIDef uiDef = (AdsUIDef) getOwnerDefinition();
        //AdsModelClassDef model = AdsUIUtil.getModelByUI(uiDef);
        boolean del = super.delete();
        /*
         * // delete event handlers if (del && model != null) { AdsMethodDef m
         * = model.getMethods().getLocal().findById(getSlotId()); if (m != null)
         * { m.delete(); } }
         */
        return del;
    }

    @Override
    public ENamingPolicy getNamingPolicy() {
        return ENamingPolicy.FREE;
    }
}
