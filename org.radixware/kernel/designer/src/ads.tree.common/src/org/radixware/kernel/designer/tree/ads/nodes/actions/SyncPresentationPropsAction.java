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

package org.radixware.kernel.designer.tree.ads.nodes.actions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.ads.clazz.AdsModelClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyPresentationPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.IModelPublishableProperty;
import org.radixware.kernel.common.defs.ads.type.IAdsTypedObject;
import org.radixware.kernel.common.enums.EPropNature;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.common.general.utils.RadixMutex;
import org.radixware.kernel.designer.tree.ads.nodes.actions.AdsDefinitionAction;


public class SyncPresentationPropsAction extends AdsDefinitionAction {

    public static class SyncPresentationPropsCookie implements Node.Cookie {

        private AdsModelClassDef model;

        public SyncPresentationPropsCookie(AdsModelClassDef model) {
            this.model = model;
        }

        private boolean isEnabled() {
            return !model.isReadOnly();
        }

        private void sync() {
            RadixMutex.writeAccess(new Runnable() {

                @Override
                public void run() {
                    final ArrayList<AdsPropertyDef> redundant = new ArrayList<AdsPropertyDef>();
                    final ArrayList<IModelPublishableProperty> missing = new ArrayList<IModelPublishableProperty>();

                    final Map<Id, AdsPropertyDef> props = new HashMap<Id, AdsPropertyDef>();


                    for (AdsPropertyDef prop : model.getProperties().get(EScope.ALL)) {
                        if (prop.getNature() == EPropNature.PROPERTY_PRESENTATION) {
                            final AdsPropertyPresentationPropertyDef pp = (AdsPropertyPresentationPropertyDef) prop;
                            final IModelPublishableProperty serverProp = pp.findServerSideProperty();
                            if (serverProp == null || !serverProp.isTransferable(model.getClientEnvironment())) {
                                redundant.add(pp);
                            } else {
                                props.put(prop.getId(), prop);
                            }
                        }
                    }
                    IModelPublishableProperty.Provider provider = model.findModelPropertyProvider();
                    if (provider == null) {
                        return;
                    }

                    List<? extends IModelPublishableProperty> allProps = provider.getModelPublishablePropertySupport().list(model.getClientEnvironment(), EScope.ALL, null);


                    for (IModelPublishableProperty prop : allProps) {
                        if (prop.isTransferable(model.getClientEnvironment())) {
                            final AdsPropertyDef cl = props.get(prop.getId());
                            if (cl == null) {
                                missing.add(prop);
                            } else {
                                if (cl.getOwnerClass() == model) {
                                    cl.setName(prop.getName());
                                }
                            }
                        }
                    }

                    for (AdsPropertyDef p : redundant) {
                        if (p.getOwnerClass() == model) {
                            p.delete();
                        }
                    }

                    for (IModelPublishableProperty p : missing) {
                        IAdsTypedObject typed = p.getTypedObject();
                        if (typed != null && typed.getType().getTypeId() != null && typed.getType().getTypeId().isAllowedForPresentationProperty()) {
                            final AdsPropertyPresentationPropertyDef pp = AdsPropertyPresentationPropertyDef.Factory.newInstance(p);
                            model.getProperties().getLocal().add(pp);
                        }
                    }
                }
            });

        }
    }

    @Override
    protected boolean calcEnabled(final Node[] activatedNodes) {
        for (Node n : activatedNodes) {
            final SyncPresentationPropsCookie c = n.getCookie(SyncPresentationPropsCookie.class);
            if (c == null || !c.isEnabled()) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected int mode() {
        return MODE_EXACTLY_ONE;
    }

    @Override
    protected Class<?>[] cookieClasses() {
        return new Class[]{SyncPresentationPropsCookie.class};
    }

    @Override
    protected void performAction(Node[] activatedNodes) {
        for (Node n : activatedNodes) {
            final SyncPresentationPropsCookie c = n.getCookie(SyncPresentationPropsCookie.class);
            if (c != null && c.isEnabled()) {
                c.sync();
            }
        }
    }

    @Override
    public String getName() {
        return "Synchronize Properties with Server Side Class";
    }
}
