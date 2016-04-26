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
package org.radixware.kernel.designer.tree.ads.nodes.defs.clazz;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.swing.Action;
import org.openide.nodes.Node;
import org.openide.util.actions.SystemAction;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef.GetterAndSetterChangeEvent;
import org.radixware.kernel.common.defs.ads.type.AdsTransparence;
import org.radixware.kernel.common.enums.EClassType;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.designer.ads.editors.refactoring.properties.PropertyRefactoringProvider;
import org.radixware.kernel.designer.ads.editors.refactoring.properties.PropertyReplaceAction;
import org.radixware.kernel.designer.common.annotations.registrators.NodeFactoryRegistration;
import org.radixware.kernel.designer.common.general.creation.CreationSupport;
import org.radixware.kernel.designer.common.general.creation.ICreatureGroup;
import org.radixware.kernel.designer.common.general.nodes.INodeFactory;
import org.radixware.kernel.designer.common.tree.actions.DefinitionRenameAction.RenameCookie;
import org.radixware.kernel.designer.tree.ads.nodes.actions.SetupPropAccessorAction.SetupGetterCookie;
import org.radixware.kernel.designer.tree.ads.nodes.actions.SetupPropAccessorAction.SetupPropGetterAction;
import org.radixware.kernel.designer.tree.ads.nodes.actions.SetupPropAccessorAction.SetupPropSetterAction;
import org.radixware.kernel.designer.tree.ads.nodes.actions.SetupPropAccessorAction.SetupSetterCookie;
import org.radixware.kernel.designer.tree.ads.nodes.defs.AdsMixedNode;
import org.radixware.kernel.designer.tree.ads.nodes.defs.MixedNodeChildrenAdapter;
import org.radixware.kernel.designer.tree.ads.nodes.defs.MixedNodeChildrenProvider;

class AdsPropertyNode extends AdsMixedNode<AdsPropertyDef> {

    @NodeFactoryRegistration
    public static final class Factory implements INodeFactory<AdsPropertyDef> {

        @Override
        public Node newInstance(AdsPropertyDef object) {
            return new AdsPropertyNode(object);
        }
    }

    protected AdsPropertyNode(AdsPropertyDef property) {
        super(property);
        addCookie(new SetupGetterCookie(property));
        addCookie(new SetupSetterCookie(property));

        refactoringInit(getRadixObject());
    }

    private void refactoringInit(AdsPropertyDef property) {
        if (property != null && !AdsTransparence.isTransparent(property)) {
            switch (getRadixObject().getNature()) {
                case DYNAMIC:
                    getLookupContent().add(new PropertyRefactoringProvider(property));
                    break;
                case INNATE:
                case USER:
                    final AdsClassDef clazz = property.getOwnerClass();
                    if (clazz.getClassDefType() != EClassType.ENTITY) {
                        getLookupContent().add(new PropertyRefactoringProvider(property));
                    }
                    break;
                default:
                    getLookupContent().add(new PropertyRefactoringProvider(property, PropertyReplaceAction.class));
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void addCustomActions(List<Action> actions) {
        super.addCustomActions(actions);
        final AdsPropertyDef prop = getRadixObject();
        if (prop != null) {
            final boolean setterDefined = prop.isSetterDefined(EScope.LOCAL);
            final boolean getterDefined = prop.isGetterDefined(EScope.LOCAL);
            final boolean iaAbstract = prop.getAccessFlags().isAbstract();

            if (!iaAbstract || setterDefined || getterDefined) {
                actions.add(null);

                if (!iaAbstract || getterDefined) {
                    actions.add(SystemAction.get(SetupPropGetterAction.class));
                }

                if (!iaAbstract && !prop.isConst() || setterDefined) {
                    actions.add(SystemAction.get(SetupPropSetterAction.class));
                }

                actions.add(null);
            }
        }
    }

    @Override
    protected Class[] getChildrenProviders() {
        return new Class[]{GetAndSetProvider.class};
    }

    @Override
    protected CreationSupport createNewCreationSupport() {
        return isNewCreationSupport() ? super.createNewCreationSupport() : null;
    }

    @Override
    protected RenameCookie createRenameCookie() {
        // disable renaming transparent property
        if (AdsTransparence.isTransparent(getRadixObject())) {
            return null;
        }
        return super.createRenameCookie();
    }

    protected boolean isNewCreationSupport() {
        return false;
    }

    private static class GetterOrSetterNode extends AdsMixedNode<AdsPropertyDef.Accessor> {

        // GetterOrSetterKey key;
        public GetterOrSetterNode(AdsPropertyDef.Accessor getterOrSetter/*
                 * , GetterOrSetterKey key
                 */) {
            super(getterOrSetter);
        }

        @Override
        protected Class[] getChildrenProviders() {
            return new Class[0];
        }
    }

    public enum GetterOrSetterKey implements MixedNodeChildrenProvider.Key {

        GETTER,
        SETTER
    }

    public static class GetAndSetProvider extends MixedNodeChildrenProvider<AdsPropertyDef> {

        private AdsPropertyDef property;
        private GetterOrSetterNode getter;
        private GetterOrSetterNode setter;
        MixedNodeChildrenAdapter<AdsPropertyDef> adapter;

        @Override
        public Collection<Key> updateKeys() {
            ArrayList<Key> keys = new ArrayList<Key>();
            if (property.isGetterDefined(EScope.LOCAL)) {
                keys.add(GetterOrSetterKey.GETTER);
            }
            if (property.isSetterDefined(EScope.LOCAL)) {
                keys.add(GetterOrSetterKey.SETTER);
            }
            return keys;
        }

        @Override
        protected Node findOrCreateNode(Key key) {
            synchronized (this) {
                if (key instanceof GetterOrSetterKey) {
                    switch ((GetterOrSetterKey) key) {
                        case GETTER:
                            AdsPropertyDef.Getter pg = property.findGetter(EScope.LOCAL).get();
                            if (pg != null) {
                                if (getter == null || getter.getRadixObject() != pg) {
                                    getter = new GetterOrSetterNode(pg);
                                }
                                return getter;
                            }
                            break;
                        case SETTER:
                            AdsPropertyDef.Setter ps = property.findSetter(EScope.LOCAL).get();
                            if (ps != null) {
                                if (setter == null || setter.getRadixObject() != ps) {
                                    setter = new GetterOrSetterNode(ps);
                                }
                                return setter;
                            }
                            break;
                    }
                }
                return null;
            }
        }
        private final AdsPropertyDef.GetterAndSeterEventListener getterAndSetterListener = new AdsPropertyDef.GetterAndSeterEventListener() {
            @Override
            public void onEvent(GetterAndSetterChangeEvent e) {
                if (adapter != null && property != null) {
                    adapter.refresh(GetAndSetProvider.this);
                }
            }
        };

        @Override
        public boolean enterContext(MixedNodeChildrenAdapter<AdsPropertyDef> adapter, AdsPropertyDef context) {
            this.adapter = adapter;
            this.property = context;
            this.property.getGetterAndSetterChangeSupport().addEventListener(getterAndSetterListener);
            return true;
        }

        @Override
        public ICreatureGroup[] createCreatureGroups() {
            return null;
        }
    }
}
