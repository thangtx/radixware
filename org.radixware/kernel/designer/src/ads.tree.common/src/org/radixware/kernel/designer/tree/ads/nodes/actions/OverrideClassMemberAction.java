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

import java.awt.event.InputEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.Action;
import javax.swing.KeyStroke;
import org.openide.nodes.Node;
import org.radixware.kernel.common.defs.*;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.*;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsEnumClassFieldDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.PropertyPresentationEmbeddedClass;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsClassCatalogDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPresentationDef;
import org.radixware.kernel.common.defs.ads.explorerItems.ExplorerItems;
import org.radixware.kernel.common.defs.ads.type.AdsAccessFlags;
import org.radixware.kernel.common.enums.EMethodNature;
import org.radixware.kernel.common.exceptions.RadixObjectError;
import org.radixware.kernel.common.utils.RadixObjectsUtils;
import org.radixware.kernel.common.utils.agents.IObjectAgent;
import org.radixware.kernel.designer.ads.common.dialogs.ChooseDefinitionMemberCfgs.ChooseClassMembersCfg;
import org.radixware.kernel.designer.ads.common.dialogs.ChooseDefinitionMemberCfgs.ChoosePresentationMemberCfg;
import org.radixware.kernel.designer.ads.common.dialogs.ChooseDefinitionMembers;
import org.radixware.kernel.designer.ads.common.dialogs.ChooseDefinitionMembers.ChooseDefinitionMembersCfg;


@SuppressWarnings({"unchecked", "cast"})
public class OverrideClassMemberAction extends ClassInheritanceAction {

    public static abstract class OverrideCookie extends Cookie {

        abstract boolean mayOverwrite();

        @Override
        protected String getErrorMessage(RadixObjectError e) {
            return "Problem while overriding/overwriting some of selected definitions";
        }

        @Override
        public boolean isReadOnly() {
            return !getOwnerClass().isCodeEditable();
        }

        protected abstract AdsClassDef getOwnerClass();
    }

    private static class OverrideCollectionCookie extends OverrideCookie {

        private final ExtendableDefinitions< ? extends AdsDefinition> collection;

        public OverrideCollectionCookie(ExtendableDefinitions<? extends AdsDefinition> collection) {
            this.collection = collection;
        }

        @Override
        public ChooseDefinitionMembers.ChooseDefinitionMembersCfg createConfig() {

            final boolean mayOverwrite = mayOverwrite();
            if (collection.getOwnerDefinition() instanceof AdsEditorPresentationDef) {
                return new ChoosePresentationMemberCfg((AdsEditorPresentationDef) collection.getOwnerDefinition()) {
                    @Override
                    public List<? extends AdsDefinition> listMembers(AdsDefinition def, boolean forOverwrite) {
                        return listCollectionMembers(def, forOverwrite);
                    }

                    @Override
                    public String getTitle() {

                        return "Override" + (mayOverwrite ? "/Overwrite " : " ") + "Editor Presentation Member";
                    }

                    @Override
                    public boolean breakHierarchy(AdsDefinition def) {
                        if (def instanceof AdsEditorPresentationDef && collection instanceof ExplorerItems.Children) {
                            if (!((AdsEditorPresentationDef) def).isExplorerItemsInherited()) {
                                AdsEditorPresentationDef ovr = ((AdsEditorPresentationDef) def).getHierarchy().findOverwritten().get();
                                if (ovr == null) {
                                    return true;
                                } else {
                                    return false;
                                }
                            } else {
                                return false;
                            }
                        } else {
                            return super.breakHierarchy(def);
                        }
                    }
                };
            } else {
                if (collection instanceof ExtendableMembers) {
                    AdsClassDef ownerClass = ((ExtendableMembers) collection).getOwnerClass();
                    return new ChooseClassMembersCfg(ownerClass) {
                        @Override
                        public List<? extends AdsDefinition> listMembers(AdsDefinition def, boolean forOverwrite) {
                            return listCollectionMembers(def, forOverwrite);
                        }

                        @Override
                        public String getTitle() {
                            return "Override" + (mayOverwrite ? "/Overwrite " : " ") + "Class Member";
                        }
                    };
                }
            }
            return null;
        }

        @Override
        boolean mayOverwrite() {
            final AdsDefinition container = RadixObjectsUtils.findContainer(collection.getContainer(), AdsDefinition.class);
            return container != null && container.getHierarchy().findOverwritten().get() != null;
        }

        @Override
        public boolean process(List<AdsDefinition> definitions) {
            for (final AdsDefinition def : definitions) {
                if (def instanceof IOverridable || def instanceof IOverwritable) {
                    final Definition exists = collection.findById(def.getId(), EScope.LOCAL).get();
                    if (exists == null) {
                        if (collection.getOwnerDefinition().getId() == def.getOwnerDefinition().getId() && (def instanceof IOverwritable)) {
                            ((ExtendableDefinitions<AdsDefinition>) collection).overwrite(def);
                        } else {
                            if (def instanceof IOverridable) {
                                collection.override((IOverridable) def);
                            }
                        }
                    }
                }
            }
            return true;
        }

        private List listCollectionMembers(AdsDefinition owner, final boolean forOverwrite) {
            ExtendableDefinitions clazzMembers = collection.findInstance(owner);
            if (clazzMembers != null) {
                return clazzMembers.get(EScope.LOCAL, new IFilter<RadixObject>() {
                    @Override
                    public boolean isTarget(RadixObject radixObject) {
                        if (radixObject instanceof AdsDefinition) {
                            if (radixObject instanceof AdsClassCatalogDef) {
                                AdsEntityObjectClassDef clazz = ((AdsClassCatalogDef) radixObject).getOwnerClass();
                                if (!clazz.isPolymorphic()) {
                                    return false;
                                }
                            }
                            if (forOverwrite) {
                                return radixObject instanceof IOverwritable
                                        && ((IOverwritable) radixObject).allowOverwrite();
                            } else if (!(radixObject instanceof IOverridable) || ((AdsDefinition) radixObject).isFinal()) {
                                return false;
                            }
                            return collection.findById(((AdsDefinition) radixObject).getId(), EScope.LOCAL).get() == null;
                        } else {
                            return false;
                        }
                    }
                });
            } else {
                return Collections.emptyList();
            }
        }

        @Override
        public boolean isReadOnly() {
            return collection.isReadOnly();
        }

        @Override
        protected AdsClassDef getOwnerClass() {
            return null;
        }
    }

    private static abstract class OverrideMemberCookie<MemberType> extends OverrideCookie {

        final MemberType member;

        public OverrideMemberCookie(MemberType member) {
            this.member = member;
        }

        @Override
        boolean mayOverwrite() {
            final AdsClassDef cls = getOwnerClass();

            if (cls != null) {
                final AdsClassDef ovr = cls.getHierarchy().findOverwritten().get();
                if (ovr != null) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public boolean process(List<AdsDefinition> definitions) {
            final AdsClassDef ownClass = getOwnerClass();
            for (final AdsDefinition def : definitions) {
                if (def instanceof AdsMethodDef) {
                    AdsMethodDef method = (AdsMethodDef) def;
                    AdsMethodDef exists = ownClass.getMethods().findById(method.getId(), EScope.LOCAL).get();
                    if (exists == null) {
                        if (method.getOwnerClass().getId() == ownClass.getId()) {
                            method = (AdsMethodDef) ownClass.getMethods().overwrite(method);
                        } else {
                            method = (AdsMethodDef) ownClass.getMethods().override(method);
                        }
                        if (method != null && member instanceof AdsMethodGroup) {
                            ((AdsMethodGroup) member).addMember(method);
                        }
                    }
                } else if (def instanceof AdsPropertyDef) {
                    AdsPropertyDef property = (AdsPropertyDef) def;
                    AdsPropertyDef exists = ownClass.getProperties().findById(property.getId(), EScope.LOCAL).get();
                    if (exists == null) {
                        if (property.getOwnerClass().getId() == ownClass.getId()) {
                            property = (AdsPropertyDef) ownClass.getProperties().overwrite(property);
                        } else {
                            property = (AdsPropertyDef) ownClass.getProperties().override(property);
                        }
                        if (property != null && member instanceof AdsPropertyGroup) {
                            ((AdsPropertyGroup) member).addMember(property);
                        }
                    }
                } else if (def instanceof AdsClassDef) {
                    final AdsClassDef cls = (AdsClassDef) def;
                    final AdsClassDef exists = ownClass.getNestedClasses().findById(cls.getId(), EScope.LOCAL).get();
                    if (exists == null) {
                        ownClass.getNestedClasses().overwrite(cls);
                    }
                } else if (def instanceof AdsEnumClassFieldDef && ownClass instanceof AdsEnumClassDef) {
                    final AdsEnumClassFieldDef field = (AdsEnumClassFieldDef) def;
                    final AdsEnumClassFieldDef exists = ((AdsEnumClassDef) ownClass).getFields().findById(field.getId(), EScope.LOCAL).get();
                    if (exists == null) {
                        ((AdsEnumClassDef) ownClass).getFields().overwrite(field);
                    }
                }
            }
            return true;
        }

        @Override
        public ChooseDefinitionMembersCfg createConfig() {
            final AdsClassDef ownClass = getOwnerClass();
            return new ChooseClassMembersCfg(ownClass) {
                @Override
                @SuppressWarnings("cast")
                public List<? extends AdsDefinition> listMembers(final AdsDefinition def, final boolean forOverwrite) {

                    if (!(def instanceof AdsClassDef)) {
                        return Collections.emptyList();
                    }
                    AdsClassDef clazz = (AdsClassDef) def;
                    ArrayList<AdsDefinition> all = new ArrayList<>();
                    if (acceptTarget(Target.PROPERTIES)) {
                        List<AdsPropertyDef> props = clazz.getProperties().get(EScope.LOCAL, new IFilter<AdsPropertyDef>() {
                            @Override
                            public boolean isTarget(AdsPropertyDef object) {
                                if (!acceptAccess(object.getAccessFlags()) || (!forOverwrite && object.isFinal())) {
                                    return false;
                                } else {
                                    return ownClass.getProperties().findById(object.getId(), EScope.LOCAL).get() == null;
                                }
                            }
                        });
                        all.addAll(props);
                    }

                    if (acceptTarget(Target.METHODS)) {
                        List<AdsMethodDef> methods = clazz.getMethods().get(EScope.LOCAL, new IFilter<AdsMethodDef>() {
                            @Override
                            public boolean isTarget(AdsMethodDef object) {
                                if (object.isConstructor() || !acceptAccess(object.getAccessFlags()) || object.getNature() == EMethodNature.SYSTEM || (!forOverwrite && object.isFinal())) {
                                    return false;
                                } else {
                                    return ownClass.getMethods().findById(object.getId(), EScope.LOCAL).get() == null;
                                }
                            }
                        });
                        all.addAll(methods);
                    }

                    // collect all nested classes only for overwrite
                    if (forOverwrite) {

                        if (acceptTarget(Target.CLASSES) && clazz.getId().equals(ownClass.getId())) {
                            final List<AdsClassDef> nested = clazz.getNestedClasses().get(EScope.LOCAL, new IFilter<AdsClassDef>() {
                                @Override
                                public boolean isTarget(AdsClassDef object) {
                                    if (object.getAccessFlags().isPrivate()) {
                                        return false;
                                    }
                                    return ownClass.getNestedClasses().findById(object.getId(), EScope.LOCAL).get() == null;
                                }
                            });
                            all.addAll(nested);
                        }

                        if (acceptTarget(Target.ENUM_FIELDS) && clazz instanceof AdsEnumClassDef && ownClass instanceof AdsEnumClassDef) {
                            final AdsEnumClassDef enumClass = (AdsEnumClassDef) clazz;

                            final List<AdsEnumClassFieldDef> fields = enumClass.getFields().get(EScope.LOCAL, new IFilter<AdsEnumClassFieldDef>() {
                                @Override
                                public boolean isTarget(AdsEnumClassFieldDef object) {
                                    return ((AdsEnumClassDef) ownClass).getFields().findById(object.getId(), EScope.LOCAL).get() == null;
                                }
                            });
                            all.addAll(fields);
                        }

                    }
                    return all;
                }

                @Override
                public String getTitle() {
                    return "Override" + (mayOverwrite() ? "/Overwrite " : " ") + "Class Member";
                }

                private boolean acceptAccess(AdsAccessFlags flags) {
                    return !flags.isStatic() && !flags.isPrivate();
                }
            };
        }

        abstract boolean acceptTarget(Target target);
    }

    private static abstract class OverrideClassMemberCookie<MemberType extends AdsClassMember> extends OverrideMemberCookie<MemberType> {

        public OverrideClassMemberCookie(MemberType member) {
            super(member);
        }

        @Override
        public boolean isReadOnly() {
            return super.isReadOnly() || getOwnerClass().isReadOnly();
        }

        @Override
        protected AdsClassDef getOwnerClass() {
            return member.getOwnerClass();
        }
    }

    private static class OverrideMethodCookie extends OverrideClassMemberCookie<AdsMethodGroup> {

        public OverrideMethodCookie(AdsMethodGroup member) {
            super(member);
        }

        @Override
        boolean acceptTarget(Target target) {
            return target == Target.METHODS;
        }
    }

    private static class OverridePropertyCookie extends OverrideClassMemberCookie<AdsPropertyGroup> {

        public OverridePropertyCookie(AdsPropertyGroup member) {
            super(member);
        }

        @Override
        boolean acceptTarget(Target target) {
            return target == Target.PROPERTIES;
        }
    }

    private static class OverrideClassCookie extends OverrideMemberCookie<AdsClassDef> {

        public OverrideClassCookie(AdsClassDef member) {
            super(member);
        }

        @Override
        public boolean isReadOnly() {
            return super.isReadOnly() || member.isReadOnly();
        }

        @Override
        protected AdsClassDef getOwnerClass() {
            return member;
        }

        @Override
        boolean acceptTarget(Target target) {
            return target != Target.ENUM_FIELDS;
        }
    }

    private static class OverrideEnumCookie extends OverrideClassCookie {

        public OverrideEnumCookie(AdsEnumClassDef member) {
            super(member);
        }

        @Override
        boolean acceptTarget(Target target) {
            return true;
        }
    }

    private static class OverrideClassInclusiveCookie extends OverrideMemberCookie<IClassInclusive> {

        private final IObjectAgent<AdsEmbeddedClassDef> classAgent;

        public OverrideClassInclusiveCookie(final IClassInclusive classInclusive) {
            super(classInclusive);

            classAgent = classInclusive.getEmbeddedClassAgent();
        }

        @Override
        protected AdsClassDef getOwnerClass() {
            return classAgent.getObject();
        }

        @Override
        boolean acceptTarget(Target target) {
            return target == Target.METHODS || target == Target.PROPERTIES;
        }

        @Override
        public boolean isReadOnly() {
            if (super.isReadOnly()) {
                return true;
            }
            if (member instanceof AdsDefinition) {
                return ((AdsDefinition) member).isReadOnly();
            }
            return true;
        }

        @Override
        boolean mayOverwrite() {
            if (member instanceof AdsDefinition) {
                return !AdsEmbeddedClassDef.searchInLevel(((AdsDefinition) member).getHierarchy().findOverwritten().all()).isEmpty();
            }
            return false;
        }

        @Override
        protected void commit() {
            classAgent.invite(false);
        }
    }

    public static final class CookieFactory {

        private CookieFactory() {
        }

        public static OverrideCookie newInstance(AdsClassDef cls) {
            if (cls instanceof AdsEnumClassDef) {
                return new OverrideEnumCookie((AdsEnumClassDef) cls);
            } else {
                return new OverrideClassCookie(cls);
            }
        }

        public static OverrideCookie newInstance(AdsMethodGroup group) {
            return new OverrideMethodCookie(group);
        }

        public static OverrideCookie newInstance(AdsPropertyGroup group) {
            return new OverridePropertyCookie(group);
        }

        public static OverrideCookie newInstance(ExtendableDefinitions<? extends AdsDefinition> collection) {
            return new OverrideCollectionCookie(collection);
        }

        public static OverrideCookie newInstance(IClassInclusive classInclusive) {
            return new OverrideClassInclusiveCookie(classInclusive);
        }
    }

    @Override
    protected boolean calcEnabled(Node[] nodes) {
        if (nodes.length == 1) {
            final OverrideCookie c = nodes[0].getCookie(OverrideCookie.class);

            return c != null && !c.isReadOnly();
        } else {
            return false;
        }
    }

    @Override
    protected Class<?>[] cookieClasses() {
        return new Class[]{OverrideCookie.class};
    }

    public OverrideClassMemberAction() {
        putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke('O', InputEvent.ALT_MASK));
    }

    @Override
    protected void performAction(Node[] activatedNodes) {
        final OverrideCookie c = activatedNodes[0].getCookie(OverrideCookie.class);

        if (c != null) {
            c.perform();
        }
    }

    @Override
    public String getName() {
        Node[] nodes = getActivatedNodes();
        if (mayOverwrite(nodes)) {
            final OverrideCookie ck = nodes[0].getLookup().lookup(OverrideCookie.class);
            if (ck != null && ck.getOwnerClass() instanceof PropertyPresentationEmbeddedClass) {
                return "Override/Overwrite property member";
            }
            return "Override/Overwrite...";
        } else {
            if (nodes.length > 0) {
                final OverrideCookie ck = nodes[0].getLookup().lookup(OverrideCookie.class);
                if (ck != null && ck.getOwnerClass() instanceof PropertyPresentationEmbeddedClass) {
                    return "Override property member";
                }

            }
            return "Override...";
        }
    }

    private boolean mayOverwrite(Node[] nodes) {
        if (nodes != null && nodes.length == 1) {
            final OverrideCookie cookie = nodes[0].getCookie(OverrideCookie.class);
            return cookie != null && cookie.mayOverwrite();
        }
        return false;
    }
}
