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

package org.radixware.kernel.designer.api.editors.def;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.SearchResult;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.AdsInterfaceClassDef;
import org.radixware.kernel.common.defs.ads.clazz.Inheritance;
import org.radixware.kernel.common.defs.ads.type.AdsClassType;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.utils.graphs.GraphWalker;
import org.radixware.kernel.designer.api.ApiFilter;
import org.radixware.kernel.designer.api.IApiEditor;
import org.radixware.kernel.designer.api.IApiEditorFactory;
import org.radixware.kernel.designer.api.editors.Brick;
import org.radixware.kernel.designer.api.editors.BrickFactory;
import org.radixware.kernel.designer.api.editors.OpenMode;
import org.radixware.kernel.designer.api.editors.components.BorderedRollUpPanel;
import org.radixware.kernel.designer.api.editors.components.DefinitionsBrick;
import org.radixware.kernel.designer.api.editors.components.HierarchyBrick;
import org.radixware.kernel.designer.api.editors.components.OverviewBrick;
import org.radixware.kernel.designer.api.editors.components.RefLabel;
import org.radixware.kernel.designer.common.annotations.registrators.ApiEditorFactoryRegistration;


class AdsClassApiEditor extends AdsDefinitionApiEditor<AdsClassDef> {

    private static final class ClassHierarchyBrick extends HierarchyBrick<AdsClassDef> {

        public ClassHierarchyBrick(AdsClassDef object, GridBagConstraints constraints) {
            super(object, constraints);
        }

        @Override
        protected void beforeBuild(OpenMode mode, ApiFilter filter) {

            JPanel panel = ((BorderedRollUpPanel) component).getContent();

            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

            final List<AdsClassDef> hierarchy = new LinkedList<>();
            new GraphWalker<AdsClassDef>().depthWalk(new GraphWalker.NodeFilter<AdsClassDef>() {
                @Override
                protected boolean accept(AdsClassDef node, int level) {
                    hierarchy.add(0, node);
                    return true;
                }

                @Override
                protected Collection<AdsClassDef> collectNodes(AdsClassDef source) {
                    final AdsDefinition.Hierarchy<AdsClassDef> classHierarchy = source.getHierarchy();
                    final SearchResult<AdsClassDef> overwritten = classHierarchy.findOverwritten();
                    final List<AdsClassDef> result = new ArrayList<>();
                    if (!overwritten.isEmpty()) {
                        result.addAll(overwritten.all());
                        return result;
                    }

                    final Inheritance inheritance = source.getInheritance();
                    final SearchResult<AdsClassDef> superClasses = inheritance.findSuperClass();
                    if (!superClasses.isEmpty()) {
                        result.addAll(superClasses.all());
                    }
                    return result;
                }
            }, getSource());

            if (hierarchy.size() > 1) {
                final JPanel base = new JPanel();
                base.setLayout(new GridBagLayout());

                GridBagConstraints constraints;

                for (int i = 0; i < hierarchy.size(); i++) {
                    constraints = new GridBagConstraints();
                    constraints.gridx = 0;
                    constraints.gridy = i;
                    constraints.insets = new Insets(2, 4 + i * 20, 2, 0);
                    constraints.anchor = GridBagConstraints.FIRST_LINE_START;
                    if (i == hierarchy.size() - 1) {
                        constraints.weighty = 1;
                        constraints.weightx = 1;
                        constraints.insets.bottom = 4;
                    }

                    final RefLabel ref = new RefLabel(hierarchy.get(i));
                    base.add(ref, constraints);
                }

                panel.add(base);
            }
        }
    }

    private static class ClassOverviewBrick extends OverviewBrick<AdsClassDef> {

        public ClassOverviewBrick(AdsClassDef object, GridBagConstraints constraints, ClassBrickFactory factory) {
            super(object, constraints, factory);
        }

        @Override
        protected void buildHierarchy() {
            super.buildHierarchy();

            final List<AdsClassDef> interfaces = getInterfaces(getSource());
            if (interfaces.size() > 0) {

//                final DefinitionsList memberList = new DefinitionsList(getSource(), new ArrayList(interfaces), "Implemented interfaces");
//                final GridBagConstraints constr = new GridBagConstraints();
//                constr.gridx = 0;
//                constr.fill = GridBagConstraints.HORIZONTAL;
//                constr.anchor = GridBagConstraints.LINE_START;
//                getBricks().add(new SimpleBrick<>(getSource(), memberList, constr, "interfaces", null));
                getBricks().add(new DefinitionsBrick(getSource(), "interfaces", new ArrayList(interfaces), false));
            }
        }

        @Override
        protected boolean hasHierarchy(RadixObject object) {
            final AdsClassDef cls = (AdsClassDef) object;
            final SearchResult<AdsClassDef> overwritten = cls.getHierarchy().findOverwritten();
            if (!overwritten.isEmpty()) {
                return true;
            }

            final SearchResult<AdsClassDef> superClass = cls.getInheritance().findSuperClass();
            if (!superClass.isEmpty()) {
                return true;
            }

            return false;
        }

        private List<AdsClassDef> getInterfaces(final AdsClassDef cls) {

            final List<AdsClassDef> hierarchy = new LinkedList<>();

            new GraphWalker<AdsClassDef>().depthWalk(new GraphWalker.NodeFilter<AdsClassDef>() {
                @Override
                protected boolean accept(AdsClassDef node, int level) {
                    if (node != cls && node instanceof AdsInterfaceClassDef) {
                        hierarchy.add(node);
                    }
                    return true;
                }

                @Override
                protected Collection<AdsClassDef> collectNodes(AdsClassDef source) {
                    final AdsDefinition.Hierarchy<AdsClassDef> classHierarchy = source.getHierarchy();
                    final SearchResult<AdsClassDef> overwritten = classHierarchy.findOverwritten();
                    final List<AdsClassDef> result = new ArrayList<>();
                    if (!overwritten.isEmpty()) {
                        result.addAll(overwritten.all());
                    }
                    final Inheritance inheritance = source.getInheritance();
                    final List<AdsTypeDeclaration> inerfaceRefList = inheritance.getInerfaceRefList(ExtendableDefinitions.EScope.LOCAL);
                    final AdsClassDef superClass = inheritance.findSuperClass().get();
                    if (superClass != null) {
                        result.add(superClass);
                    }
                    for (AdsTypeDeclaration iface : inerfaceRefList) {
                        final SearchResult<AdsClassDef> superInterface = findSuperInterface(iface, source);
                        if (!superInterface.isEmpty()) {
                            result.addAll(superInterface.all());
                        }
                    }
                    return result;
                }

                SearchResult<AdsClassDef> findSuperInterface(AdsTypeDeclaration superInterface, AdsClassDef source) {
                    if (superInterface == null) {
                        return SearchResult.empty();
                    }
                    final List<AdsType> types = superInterface.resolveAll(source);
                    if (types.size() > 0) {
                        List<AdsClassDef> classes = new ArrayList<>();
                        for (AdsType type : types) {
                            if (type instanceof AdsClassType) {
                                classes.add(((AdsClassType) type).getSource());
                            }
                        }
                        if (classes.isEmpty()) {
                            return SearchResult.empty();
                        } else {
                            return SearchResult.list(classes);
                        }
                    } else {
                        return SearchResult.empty();
                    }
                }
            }, cls);

            return hierarchy;
        }
    }

    private static class ClassBrickFactory extends BrickFactory {

        @Override
        public Brick<? extends RadixObject> create(int key, RadixObject object, GridBagConstraints constraints) {

            switch (key) {
                case OVERVIEW:
                    return new ClassOverviewBrick((AdsClassDef) object, constraints, this);
                case HIERARCHY:
                    return new ClassHierarchyBrick((AdsClassDef) object, constraints);
            }
            return super.create(key, object, constraints);
        }
    }

    @ApiEditorFactoryRegistration
    public static final class Factory implements IApiEditorFactory<AdsClassDef> {

        @Override
        public IApiEditor<AdsClassDef> newInstance(AdsClassDef classDef) {
            return new AdsClassApiEditor(classDef);
        }
    }

    public AdsClassApiEditor(AdsClassDef source) {
        super(source);
    }

    @Override
    protected BrickFactory createFactory() {
        return new ClassBrickFactory();
    }

    //    @Override
    //    protected ApiEditorBuilder createBuilder() {
    //        return new ClassApiBuilder(getSource(), createFactory());
    //    }
    @Override
    public boolean isEmbedded() {
        return false;
    }
}
