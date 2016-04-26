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


package org.radixware.kernel.designer.ads.editors.refactoring.replace;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.openide.nodes.Children;
import static org.openide.nodes.Children.LEAF;
import org.openide.nodes.Node;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.IFilter;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.SearchResult;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.IAdsClassMember;
import org.radixware.kernel.common.defs.ads.clazz.Inheritance;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.type.AdsClassType;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.designer.ads.editors.refactoring.components.RadixObjectTreeNode;
import org.radixware.kernel.designer.ads.editors.refactoring.components.TreeNode;


public abstract class SubstituteTreeFactory {

    private static abstract class ClassMembersSubstituteTreeFactory extends SubstituteTreeFactory {

        public ClassMembersSubstituteTreeFactory(AdsDefinition member, TreeNode.INodeSelector<RadixObject> filter) {
            super(member, filter);
        }

        private Set<AdsClassDef> getInterfaces(AdsClassDef cls) {
            final Set<AdsClassDef> interfaces = new HashSet<>();

            final Inheritance inheritance = cls.getInheritance();
            final List<AdsTypeDeclaration> inerfaceRefList = inheritance.getInerfaceRefList(ExtendableDefinitions.EScope.ALL);

            for (final AdsTypeDeclaration declaration : inerfaceRefList) {
                final SearchResult<AdsType> resolve = declaration.resolve(cls);
                if (!resolve.isEmpty()) {
                    final AdsClassType classType = (AdsClassType) resolve.get();
                    final AdsClassDef interfaceClass = classType.getSource();
                    if (interfaceClass != null && !interfaces.contains(interfaceClass) && selector.acceptNode(cls)) {
                        interfaces.addAll(getInterfaces(interfaceClass));
                        interfaces.add(interfaceClass);
                    }
                }
            }

            return interfaces;
        }

        private void collect(AdsClassDef ownerClass, List<AdsClassDef> classes, Set<AdsClassDef> interfaces) {
            if (selector.acceptNode(ownerClass)) {
                classes.add(ownerClass);
            }
            interfaces.addAll(getInterfaces(ownerClass));

            final SearchResult<AdsClassDef> result = ownerClass.getInheritance().findSuperClass();

            if (!result.isEmpty()) {
                collect(result.get(), classes, interfaces);
            }
        }

        @Override
        protected Node createTree(AdsDefinition member) {

            final List<AdsClassDef> classes = new ArrayList<>();
            final Set<AdsClassDef> interfaces = new HashSet<>();

            final AdsClassDef ownerClass = ((IAdsClassMember) member).getOwnerClass();
            collect(ownerClass, classes, interfaces);
            
            Collections.reverse(classes);

            return new RadixObjectTreeNode<>(new OwnerChildren(classes, interfaces, this), member);
        }

        @Override
        public Node createNode(AdsDefinition key, List<AdsDefinition> keys) {
            return new RadixObjectTreeNode<>(new MembersChildren(keys), key);
        }
    }

    private static final class MethodsSubstituteTreeFactory extends ClassMembersSubstituteTreeFactory {

        public MethodsSubstituteTreeFactory(AdsDefinition member, TreeNode.INodeSelector<RadixObject> filter) {
            super(member, filter);
        }

        @Override
        public List<? extends AdsDefinition> createKeys(AdsDefinition owner) {
            return ((AdsClassDef) owner).getMethods().get(ExtendableDefinitions.EScope.LOCAL, new IFilter<AdsMethodDef>() {
                @Override
                public boolean isTarget(AdsMethodDef radixObject) {
                    return selector.acceptNode(radixObject);
                }
            });
        }
    }

    private static final class PropertiesSubstituteTreeFactory extends ClassMembersSubstituteTreeFactory {

        public PropertiesSubstituteTreeFactory(AdsDefinition member, TreeNode.INodeSelector<RadixObject> filter) {
            super(member, filter);
        }

        @Override
        public List<? extends AdsDefinition> createKeys(AdsDefinition owner) {
            return ((AdsClassDef) owner).getProperties().get(ExtendableDefinitions.EScope.LOCAL, new IFilter<AdsPropertyDef>() {
                @Override
                public boolean isTarget(AdsPropertyDef radixObject) {
                    return selector.acceptNode(radixObject);
                }
            });
        }
    }

    private static final class OwnerChildren extends Children.Array {

        private static Collection<Node> createNodes(List<AdsClassDef> classes, Set<AdsClassDef> interfaces, SubstituteTreeFactory substituteTreeFactory) {
            final List<Node> nodes = new ArrayList<>();

            final Set<AdsDefinition> used = new HashSet<>();
            nodes.addAll(createNodes(classes, substituteTreeFactory, used));
            nodes.addAll(createNodes(interfaces, substituteTreeFactory, used));

            return nodes;
        }

        private static Collection<Node> createNodes(Collection<AdsClassDef> keys, SubstituteTreeFactory substituteTreeFactory, Set<AdsDefinition> used) {
            final List<Node> nodes = new ArrayList<>();

            for (AdsDefinition definition : keys) {
                final List<? extends AdsDefinition> allKeys = substituteTreeFactory.createKeys(definition);
                final List<AdsDefinition> children = new ArrayList<>(allKeys.size());

                for (final AdsDefinition def : allKeys) {
                    if (!used.contains(def)) {
                        children.add(def);
                        used.add(def);
                    }
                }
                
                if (!children.isEmpty()) {
                    Collections.sort(children, new Comparator<AdsDefinition>() {
                        @Override
                        public int compare(AdsDefinition o1, AdsDefinition o2) {                         
                            return o1.getName().compareTo(o2.getName());
                        }
                    });
                    
                    nodes.add(substituteTreeFactory.createNode(definition, children));
                }
            }

            return nodes;
        }

        public OwnerChildren(List<AdsClassDef> classes, Set<AdsClassDef> interfaces, SubstituteTreeFactory substituteTreeFactory) {
            super(createNodes(classes, interfaces, substituteTreeFactory));
        }
    }

    private static final class MembersChildren extends Children.Array {

        private static Collection<Node> createNodes(List<AdsDefinition> keys) {
            List<Node> nodes = new ArrayList<>();

            for (AdsDefinition definition : keys) {
                nodes.add(new RadixObjectTreeNode<>(LEAF, definition));
            }

            return nodes;
        }

        public MembersChildren(List<AdsDefinition> keys) {
            super(createNodes(keys));
        }
    }

    public static Node createSubstituteTree(AdsDefinition member, TreeNode.INodeSelector<RadixObject> selector) {
        if (member instanceof AdsMethodDef) {
            return new MethodsSubstituteTreeFactory(member, selector).createTree(member);
        }
        if (member instanceof AdsPropertyDef) {
            return new PropertiesSubstituteTreeFactory(member, selector).createTree(member);
        }
        return null;
    }
    
    public static SubstituteTreeFactory createSubstituteTreeFactory(AdsDefinition member, TreeNode.INodeSelector<RadixObject> filter) {
        if (member instanceof AdsMethodDef) {
            return new MethodsSubstituteTreeFactory(member, filter);
        }
        if (member instanceof AdsPropertyDef) {
            return new PropertiesSubstituteTreeFactory(member, filter);
        }
        return null;
    }
    
    protected final TreeNode.INodeSelector<RadixObject> selector;
//    protected final AdsDefinition rootMember;
    
    protected SubstituteTreeFactory(AdsDefinition member, TreeNode.INodeSelector<RadixObject> filter) {
        this.selector = filter;
//        this.rootMember = member;
    }
    
//    @Override
//    public final Node createRootNode(INodeFilter filter) {
//        return createTree(rootMember, filter);
//    }

    protected abstract Node createTree(AdsDefinition member);

    public abstract List<? extends AdsDefinition> createKeys(AdsDefinition owner);

    public abstract Node createNode(AdsDefinition key, List<AdsDefinition> keys);
}
