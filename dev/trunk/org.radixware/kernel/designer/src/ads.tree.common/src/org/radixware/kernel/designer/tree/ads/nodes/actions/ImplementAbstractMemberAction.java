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
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.IFilter;
import org.radixware.kernel.common.defs.SearchResult;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.*;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.enums.EClassType;
import org.radixware.kernel.common.exceptions.RadixObjectError;
import org.radixware.kernel.common.utils.agents.IObjectAgent;
import org.radixware.kernel.designer.ads.common.dialogs.ChooseDefinitionMemberCfgs;
import org.radixware.kernel.designer.ads.common.dialogs.ChooseDefinitionMembers.ChooseDefinitionMembersCfg;

public class ImplementAbstractMemberAction extends ClassInheritanceAction {

    public static abstract class ImplementCookie extends Cookie {

        final MembersGroup<?> group;

        public ImplementCookie(MembersGroup<?> group) {
            this.group = group;
        }

        @Override
        protected ChooseDefinitionMembersCfg createConfig() {
            final AdsClassDef ownerClass = getOwnerClass();

            final class Filter<T extends AdsClassMember & IAccessible> implements IFilter<T> {

                @Override
                public boolean isTarget(T object) {
                    if (object.getAccessFlags().isAbstract() || object.getOwnerClass().getClassDefType() == EClassType.INTERFACE) {
                        final SearchResult<AdsMethodDef> result = ownerClass.getMethods().findById(object.getId(), EScope.ALL);
                        return result.isEmpty() || result.get().getAccessFlags().isAbstract();
                    } else {
                        return false;
                    }
                }
            }


            return new ChooseDefinitionMemberCfgs.ChooseClassMembersCfg(ownerClass) {
                @Override
                public List<? extends AdsDefinition> listMembers(AdsDefinition def, boolean forOverwrite) {
                    if (!(def instanceof AdsClassDef)) {
                        return Collections.emptyList();
                    }
                    final AdsClassDef clazz = (AdsClassDef) def;
                    final List<AdsDefinition> members = new ArrayList<>();

                    if (acceptTarget(Target.METHODS)) {
                        members.addAll(clazz.getMethods().get(EScope.LOCAL, new Filter<AdsMethodDef>()));
                    }

                    if (acceptTarget(Target.PROPERTIES)) {
                        members.addAll(clazz.getProperties().get(EScope.LOCAL, new Filter<AdsPropertyDef>()));
                    }

                    return members;
                }

                @Override
                public String getTitle() {
                    return ImplementCookie.this.getTitle();
                }
            };
        }

        @Override
        protected boolean process(List<AdsDefinition> definitions) {
            final AdsClassDef ownerClass = getOwnerClass();
            for (final AdsDefinition def : definitions) {
                if (def instanceof AdsMethodDef) {
                    AdsMethodDef method = (AdsMethodDef) def;
                    final AdsMethodDef alreadyExists = ownerClass.getMethods().findById(method.getId(), EScope.LOCAL).get();
                    if (alreadyExists == null) {
                        if (method.getOwnerClass().getId() == ownerClass.getId()) {
                            method = ownerClass.getMethods().overwrite(method);
                        } else {
                            method = ownerClass.getMethods().override(method);
                        }

                        final AdsMethodGroup methodGroup = group instanceof AdsMethodGroup
                                ? (AdsMethodGroup) group
                                : ownerClass.getMethodGroup();
                        if (method != null && methodGroup != ownerClass.getMethodGroup()) {
                            methodGroup.addMember(method);
                        }
                    }
                } else if (def instanceof AdsPropertyDef) {
                    AdsPropertyDef property = (AdsPropertyDef) def;
                    final AdsPropertyDef alreadyExists = ownerClass.getProperties().findById(property.getId(), EScope.LOCAL).get();
                    if (alreadyExists == null) {
                        if (property.getOwnerClass().getId() == ownerClass.getId()) {
                            property = ownerClass.getProperties().overwrite(property);
                        } else {
                            property = ownerClass.getProperties().override(property);
                        }

                        final AdsPropertyGroup propertyGroup = group instanceof AdsPropertyGroup
                                ? (AdsPropertyGroup) group
                                : ownerClass.getPropertyGroup();
                        if (property != null && propertyGroup != ownerClass.getPropertyGroup()) {
                            propertyGroup.addMember(property);
                        }
                    }
                }
            }
            return true;
        }

        @Override
        protected String getErrorMessage(RadixObjectError e) {
            return "Problem while implementing some of selected methods";
        }

        public String getTitle() {
            if (acceptTarget(Target.METHODS) && acceptTarget(Target.PROPERTIES)) {
                return "Implement Method/Property";
            } else if (acceptTarget(Target.METHODS)) {
                return "Implement Method";
            } else if (acceptTarget(Target.PROPERTIES)) {
                return "Implement Property";
            }
            return "Implement";
        }

        abstract boolean acceptTarget(Target target);

        abstract AdsClassDef getOwnerClass();
    }

    private static class ImplementMemberCookie extends ImplementCookie {

        private final AdsClassDef ownerClass;

        public ImplementMemberCookie(MembersGroup<?> group) {
            super(group);

            this.ownerClass = group.getOwnerClass();
        }

        public ImplementMemberCookie(AdsClassDef ownerClass) {
            super(null);
            this.ownerClass = ownerClass;
        }

        private ImplementMemberCookie(AdsClassDef ownerClass, MembersGroup<?> group) {
            super(group);
            this.ownerClass = ownerClass;
        }

        @Override
        boolean acceptTarget(Target target) {
            if (group instanceof AdsMethodGroup) {
                return target == Target.METHODS;
            } else if (group instanceof AdsPropertyGroup) {
                return target == Target.PROPERTIES;
            }
            return target == Target.METHODS || target == Target.PROPERTIES;
        }

        @Override
        AdsClassDef getOwnerClass() {
            return ownerClass;
        }

        @Override
        public boolean isReadOnly() {
            return ownerClass.isReadOnly() || !ownerClass.isCodeEditable();
        }
    }

    private static class ImplementClassInclusiveCookie extends ImplementCookie {

        private final IClassInclusive classInclusive;
        private final IObjectAgent<AdsEmbeddedClassDef> classAgent;

        public ImplementClassInclusiveCookie(final IClassInclusive classInclusive) {
            super(null);
            this.classInclusive = classInclusive;

            classAgent = classInclusive.getEmbeddedClassAgent();
        }

        @Override
        boolean acceptTarget(Target target) {
            return target == Target.METHODS || target == Target.PROPERTIES;
        }

        @Override
        AdsClassDef getOwnerClass() {
            return classAgent.getObject();
        }

        @Override
        public boolean isReadOnly() {
            if (classInclusive instanceof AdsDefinition) {
                return ((AdsDefinition) classInclusive).isReadOnly();
            }
            return true;
        }

        @Override
        protected void commit() {
            classAgent.invite(false);
        }
    }

    public static final class CookieFactory {

        private CookieFactory() {
        }

        public static ImplementCookie newInstance(AdsClassDef cls) {
            return new ImplementAbstractMemberAction.ImplementMemberCookie(cls);
        }

        public static ImplementCookie newInstance(MembersGroup<?> group) {
            return new ImplementAbstractMemberAction.ImplementMemberCookie(group);
        }

        public static ImplementCookie newInstance(IClassInclusive classInclusive) {
            return new ImplementAbstractMemberAction.ImplementClassInclusiveCookie(classInclusive);
        }
    }

    @Override
    protected Class<?>[] cookieClasses() {
        return new Class[]{ImplementCookie.class};
    }

    @Override
    protected void performAction(Node[] activatedNodes) {
        final ImplementCookie cookie = activatedNodes[0].getCookie(ImplementCookie.class);

        if (cookie != null) {
            cookie.perform();
        }
    }

    @Override
    public String getName() {
        final Node[] activatedNodes = getActivatedNodes();
        if (activatedNodes.length > 0) {
            final ImplementCookie cookie = activatedNodes[0].getCookie(ImplementCookie.class);
            if (cookie != null) {
                return cookie.getTitle() + "...";
            }
        }
        return "Implement...";
    }

    @Override
    protected boolean calcEnabled(Node[] nodes) {
        if (nodes.length == 1) {
            final ImplementCookie cookie = nodes[0].getCookie(ImplementCookie.class);
            return cookie != null && !cookie.isReadOnly();
        } else {
            return false;
        }
    }

    public ImplementAbstractMemberAction() {
        putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke('I', InputEvent.ALT_MASK));
    }
}
