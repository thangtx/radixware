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

import org.openide.nodes.Node;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityGroupClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsPresentationEntityAdapterClassDef;
import org.radixware.kernel.common.enums.EClassType;
import org.radixware.kernel.designer.ads.common.lookup.AdsClassLookupSupport;
import org.radixware.kernel.designer.ads.editors.creation.AdsClassCreature;


public abstract class CreateEntityFeatureAction extends AdsDefinitionAction {

    public static final class CreateAGCAction extends CreateEntityFeatureAction {

        @Override
        protected void execute(AdsEntityObjectClassDef clazz) {
            if (isEnabled(clazz)) {
                AdsEntityGroupClassDef agc = AdsEntityGroupClassDef.Factory.newInstance((AdsEntityClassDef) clazz);

                clazz.getModule().getDefinitions().add(agc);
                agc.setName(clazz.getName() + "Group");
                if (agc.getHierarchy().findOverwritten().get() == null) {
                    AdsClassLookupSupport.setupEntityGroupClass(agc);
                }
                AdsClassCreature.updateEntityGroupAfterCreate(agc, clazz);
            }
        }

        @Override
        protected boolean isEnabled(AdsEntityObjectClassDef clazz) {
            if (clazz.getModule().isReadOnly()) {
                return false;
            }
            if (clazz.getClassDefType() != EClassType.ENTITY) {
                return false;
            } else {
                AdsEntityClassDef e = (AdsEntityClassDef) clazz;
                AdsEntityGroupClassDef agc = e.findEntityGroup();
                return agc == null;
            }

        }

        @Override
        public String getName() {
            return "Create Entity Group";
        }
    }

    public static final class CreateAPAAction extends CreateEntityFeatureAction {

        @Override
        protected void execute(AdsEntityObjectClassDef clazz) {
            if (isEnabled(clazz)) {
                AdsPresentationEntityAdapterClassDef apa = AdsPresentationEntityAdapterClassDef.Factory.newInstance(clazz);
                clazz.getModule().getDefinitions().add(apa);
                apa.setName(clazz.getName() + "Adapter");
            }
        }

        @Override
        protected boolean isEnabled(AdsEntityObjectClassDef clazz) {
            if (clazz != null && clazz.getModule()!= null && clazz.getModule().isReadOnly()) {
                return false;
            }

            AdsPresentationEntityAdapterClassDef apa = clazz.findPresentationAdapter();
            if (apa != null) {
                return false;
            } else {
                if (clazz.getClassDefType() == EClassType.ENTITY) {
                    return true;
                }
                AdsEntityObjectClassDef basis = clazz.findBasis();
                if (basis != null) {
                    apa = basis.findPresentationAdapter();
                    if (apa == null) {
                        return false;
                    } else {
                        return true;
                    }
                } else {
                    return false;
                }
            }
        }

        @Override
        public String getName() {
            return "Create Presentation Entity Adapter Class";
        }
    }

    public static class CreateEntityFeatureCookie implements Node.Cookie {

        private final AdsEntityObjectClassDef clazz;

        public CreateEntityFeatureCookie(AdsEntityObjectClassDef clazz) {
            this.clazz = clazz;
        }

        private boolean calcEnabled(CreateEntityFeatureAction a) {
            return a.isEnabled(clazz);
        }

        private void execute(CreateEntityFeatureAction a) {
            a.execute(clazz);
        }
    }

    @Override
    protected boolean calcEnabled(Node[] activatedNodes) {
        if (activatedNodes.length == 1) {
            CreateEntityFeatureCookie c = activatedNodes[0].getCookie(CreateEntityFeatureCookie.class);
            if (c != null) {
                return c.calcEnabled(this);
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    protected abstract boolean isEnabled(AdsEntityObjectClassDef clazz);

    protected abstract void execute(AdsEntityObjectClassDef clazz);

    @Override
    protected int mode() {
        return MODE_EXACTLY_ONE;
    }

    @Override
    @SuppressWarnings({"rawtypes"})
    protected Class<?>[] cookieClasses() {
        return new Class[]{CreateEntityFeatureCookie.class};
    }

    @Override
    protected void performAction(Node[] nodes) {
        if (nodes.length == 1) {
            CreateEntityFeatureCookie c = nodes[0].getCookie(CreateEntityFeatureCookie.class);
            if (c != null) {
                c.execute(this);
            }
        }
    }
}
