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
package org.radixware.kernel.common.defs.ads.radixdoc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.IFilter;
import org.radixware.kernel.common.defs.SearchResult;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef.Resources;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassMember;
import org.radixware.kernel.common.defs.ads.clazz.IAdsClassMember;
import org.radixware.kernel.common.defs.ads.clazz.IAdsPresentableClass;
import org.radixware.kernel.common.defs.ads.clazz.Inheritance;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.MethodParameter;
import org.radixware.kernel.common.defs.ads.clazz.presentation.ClassPresentations;
import org.radixware.kernel.common.defs.ads.type.AdsClassType;
import org.radixware.kernel.common.defs.ads.type.AdsTransparence;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.enums.EAccess;
import org.radixware.kernel.common.radixdoc.DefaultMeta;
import org.radixware.kernel.common.radixdoc.DefaultStyle;
import org.radixware.kernel.common.radixdoc.DocumentOptions;
import org.radixware.kernel.common.radixdoc.RadixIconResource;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.graphs.GraphWalker;
import org.radixware.schemas.radixdoc.Block;
import org.radixware.schemas.radixdoc.ContentContainer;
import org.radixware.schemas.radixdoc.ElementList;
import org.radixware.schemas.radixdoc.Page;
import org.radixware.schemas.radixdoc.Ref;
import org.radixware.schemas.radixdoc.Resource;
import org.radixware.schemas.radixdoc.Table;
import org.radixware.schemas.radixdoc.Text;

public class ClassRadixdoc extends AdsDefinitionRadixdoc<AdsClassDef> {

    abstract class SummaryMembersChapterFactory<T extends AdsDefinition & IAdsClassMember> extends SummaryChapterFactory<AdsClassDef, T> {

        private BlockBuilder container;

        public SummaryMembersChapterFactory(AdsClassDef source, BlockBuilder container) {
            super(source);

            this.container = container;
        }

        @Override
        protected void prepareElementsTable(AdsClassDef cls, Table table) {
            if (cls == source) {
                final Table.Row head = table.addNewRow();
                head.setMeta("head");

                final Table.Row.Cell nameCell = head.addNewCell();
                nameCell.setColspan(2);

                nameCell.addNewText().setStringValue(getElementTypeName());
            } else {
                table.setMeta(DefaultMeta.CONTENT);
            }
            table.setStyle(DefaultStyle.MEMBERS);
        }

        @Override
        protected void prepareInheritTable(Table table) {
            final Table.Row head = table.addNewRow();
            head.setMeta("head");

            final Table.Row.Cell nameCell = head.addNewCell();
            nameCell.setColspan(2);
            nameCell.addNewText().setStringValue("Inherited " + getElementTypeName());

            table.setStyle(DefaultStyle.MEMBERS);
        }

        @Override
        protected Block createChapter() {
            return getWriter().createChapter(container.getBlock(), getElementTypeName());
        }

        @Override
        protected boolean isApiElement(T elem) {
            return elem.isPublished() && !elem.getAccessMode().isLess(EAccess.PROTECTED);
        }
    }

    public abstract static class DetailMembersChapterFactory<T extends AdsClassMember> {

//        boolean create = false;
        ElementList root;

        final ElementList getRootChapter() {
            if (root == null) {
                root = createRootList();
            }
            return root;
        }

        void document(AdsClassDef cls) {
            final HashSet<Id> used = new HashSet<>();
            final List<T> apiMembers = getApiMembers(getAllMembers(cls), used);
            if (!apiMembers.isEmpty()) {
                documentMembers(cls, apiMembers);
            }
        }

        private List<T> getApiMembers(List<T> allMembers, Set<Id> used) {
            final List<T> apiMembers = new ArrayList<>();
            for (T member : allMembers) {
                if (isApiMember(member) && !used.contains(member.getId())) {
                    apiMembers.add(member);
                    used.add(member.getId());
                }
            }
            if (apiMembers.isEmpty()) {
                return Collections.emptyList();
            }
            Collections.sort(apiMembers, new Comparator<T>() {
                @Override
                public int compare(T o1, T o2) {
                    return o1.getName().compareTo(o2.getName());
                }
            });
            return apiMembers;
        }

        protected void documentMembers(AdsClassDef cls, List<T> apiMembers) {
            for (final T member : apiMembers) {
                documentMemberDetail(member, createItem());
            }
        }

        ElementList.Item createItem() {
            final ElementList.Item item = getRootChapter().addNewItem();

            return item;
        }

        abstract ElementList createRootList();

        abstract void documentMemberDetail(T member, ContentContainer root);

        abstract List<T> getAllMembers(AdsClassDef c);
    }

    protected static boolean isApiMember(AdsDefinition member) {
        return member.isPublished() && (!member.getAccessMode().isLess(EAccess.PROTECTED));
    }

    public ClassRadixdoc(AdsClassDef source, Page page, DocumentOptions options) {
        super(source, page, options);
    }

    public class ClassWriter extends AdsPageWriter<AdsClassDef> {

        public ClassWriter(AdsDefinitionRadixdoc<AdsClassDef> page) {
            super(page);
        }

        public void writeResources(ContentContainer overview, Resources resources) {
            if (resources != null && !resources.isEmpty()) {
                Table resTable = getClassWriter().setBlockCollapsibleAndAddTable(overview.addNewBlock(), "Resources", "Name", "Value");
                for (AdsClassDef.Resources.Resource res : resources) {
                    getClassWriter().addAllStrRow(resTable, res.getName(), res.getData());
                }
            }
        }

        public void writeOverwriteInfo(AdsClassDef classDef, Table overviewTable) {
            if (!classDef.isOverwrite()) {
                getClassWriter().addStr2BoolRow(overviewTable, "Overwrite", false);
            } else {
                getClassWriter().addStr2RefRow(overviewTable, "Overwritten class", classDef.getHierarchy().findOverwritten().get(), getSource());
            }
        }
    }

    @Override
    protected AdsPageWriter<AdsClassDef> createWriter() {
        return new ClassWriter(this);
    }

    public ClassWriter getClassWriter() {
        return (ClassWriter) getWriter();
    }

    @Override
    protected void documentContent(final Page page) {

        final BlockBuilder blockBuilder = new BlockBuilder() {
            @Override
            public Block createElement() {
                final Block summaryChapter = page.addNewBlock();
                summaryChapter.setStyle(DefaultStyle.SUMMARY);
                return summaryChapter;
            }
        };

        final List<AdsClassDef> hierarchy = new LinkedList<>();
        new GraphWalker<AdsClassDef>().depthWalk(new GraphWalker.NodeFilter<AdsClassDef>() {
            @Override
            protected boolean accept(AdsClassDef node, int level) {
                if (node.isPublished()) {
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
                    return result;
                }
                final Inheritance inheritance = source.getInheritance();
                final SearchResult<AdsClassDef> superClasses = inheritance.findSuperClass();
                if (!superClasses.isEmpty()) {
                    result.addAll(superClasses.all());
                }
                return result;
            }
        }, source);
        documentMembersSummary(blockBuilder, hierarchy);

        final Block detailChapter = page.addNewBlock();
        detailChapter.setStyle(DefaultStyle.DETAIL);

        documentMembersDetail(detailChapter);
    }

    protected void collectModifiers(AdsClassDef classDef, List<String> modifiers) {
        if (classDef.isFinal()) {
            modifiers.add("Final");
        }
        if (classDef.isDeprecated()) {
            modifiers.add("Depricated");
        }
    }

    @Override
    protected void writeDefSpecificInfo(ContentContainer container) {
        final Block innerContent = container.addNewBlock();
        innerContent.setStyle(DefaultStyle.NAMED);
        
        Table commonAttrTable = getClassWriter().addGeneralAttrTable(innerContent);

        List<String> modifiers = new ArrayList<>();
        collectModifiers(source, modifiers);
        if (!modifiers.isEmpty()) {
            getClassWriter().addAllStrRow(commonAttrTable, "Modifiers", modifiers.toString().substring(1, modifiers.toString().length() - 1));
        }
        getClassWriter().writeOverwriteInfo(source, commonAttrTable);
        writeClassDefInfo(innerContent, commonAttrTable);
        getClassWriter().writeResources(innerContent, source.getResources());
    }

    protected void writeClassDefInfo(ContentContainer overview, Table overviewTable) {
        //Overrided in descendant classes
    }

    @Override
    protected Block documentHead(Page page) {
        final Block headChapter = page.addNewBlock();
        headChapter.setStyle(DefaultStyle.HEAD);
        final Block subTitleBlock = headChapter.addNewBlock();
        subTitleBlock.setStyle(DefaultStyle.SUB_TITLE);
        subTitleBlock.addNewText().setStringValue("Module ");
        final Ref moduleRef = subTitleBlock.addNewRef();
        moduleRef.setPath(resolve(source, source.getModule()));
        moduleRef.addNewText().setStringValue(source.getModule().getQualifiedName());
        final Block titleBlock = headChapter.addNewBlock();
        titleBlock.setStyle(DefaultStyle.TITLE);
        final Resource icon = titleBlock.addNewResource();
        final RadixIconResource iconResource = new RadixIconResource(source.getIcon());
        icon.setSource(iconResource.getKey());
        addResource(iconResource);
        final StringBuilder name = new StringBuilder();
        name.append(source.getTypeTitle()).append(' ').append(source.getName());
        final AdsTypeDeclaration.TypeArguments typeArguments = source.getTypeArguments();
        if (!typeArguments.isEmpty()) {
            name.append('<');
            boolean first = true;
            for (AdsTypeDeclaration.TypeArgument argument : typeArguments.getArgumentList()) {
                if (first) {
                    first = false;
                } else {
                    name.append(", ");
                }
                name.append(argument.getQualifiedName(source));
            }
            name.append('>');
        }
        getWriter().addStrTitle(titleBlock, name.toString());
        return headChapter;
    }

    @Override
    protected void documentHierarchy(ContentContainer container) {

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
        }, source);

        if (hierarchy.size() > 1) {
            final ElementList hierarchyList = container.addNewList();
            hierarchyList.setMeta(DefaultMeta.List.HIERARCHY);
            for (final AdsClassDef node : hierarchy) {
                final ElementList.Item item = hierarchyList.addNewItem();
                if (node == source) {
                    item.addNewText().setStringValue(node.getQualifiedName());
                } else {
                    final Ref ref = item.addNewRef();
                    ref.addNewText().setStringValue(node.getQualifiedName());
                    ref.setPath(resolve(source, node));
                }
            }
        }

        final List<AdsTypeDeclaration> interfaces = new LinkedList<>();
        new GraphWalker<AdsClassDef>().depthWalk(new GraphWalker.NodeFilter<AdsClassDef>() {
            @Override
            protected boolean accept(AdsClassDef node, int level) {
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
                    interfaces.add(iface);
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
        }, source);

        if (!interfaces.isEmpty()) {
            final Block implementInterfaces = container.addNewBlock();
            implementInterfaces.setStyle(DefaultStyle.DEFINITION);
            getWriter().addStrTitle(implementInterfaces, "Implemented interfaces:");

            final TypeDocument typeDoc = new TypeDocument();
            boolean first = true;
            for (final AdsTypeDeclaration iface : interfaces) {
                if (first) {
                    first = false;
                } else {
                    typeDoc.addString(", ");
                }
                typeDoc.addType(iface, getSource());
            }

            getWriter().documentType(implementInterfaces.addNewBlock(), typeDoc, getSource());
        }
    }

    @Override
    protected void documentDescriptionExtensions(ContentContainer container) {

        if (AdsTransparence.isTransparent(source)) {
            final Block titleDefinition = container.addNewBlock();
            titleDefinition.setStyle(DefaultStyle.DEFINITION);
            getWriter().addStrTitle(titleDefinition, "Publicated class:");
            final Text titleText = titleDefinition.addNewBlock().addNewText();
            titleText.setStringValue(source.getTransparence().getPublishedName());
        }
    }

    private void documentMembersSummary(final BlockBuilder summary, final List<AdsClassDef> hierarchy) {

        final BlockBuilder summaryChapter = new BlockBuilder() {
            @Override
            public Block createElement() {
                final Block summaryChapter = summary.getBlock().addNewBlock();
                summaryChapter.setStyle(DefaultStyle.CHAPTER);
                getWriter().addStrTitle(summaryChapter, "Summary");
                return summaryChapter;
            }
        };

        //
        // Nested classes summary
        //
        new SummaryMembersChapterFactory<AdsClassDef>(source, summaryChapter) {
            @Override
            protected void documentElementSummary(AdsClassDef member, Table table) {
                final Table.Row methodRow = table.addNewRow();

                final Table.Row.Cell typeCell = methodRow.addNewCell();
                typeCell.setStyle("modifiers");
                String modifiers = member.getAccessFlags().getRadixdocPresentation();
                getWriter().addText(typeCell, modifiers);

                final Table.Row.Cell descriptionCell = methodRow.addNewCell();
                final Ref ref = descriptionCell.addNewRef();
                ref.setPath(resolve(source, member));
                final RadixIconResource icon = new RadixIconResource(member.getIcon());
                ref.addNewResource().setSource(icon.getKey());
                addResource(icon);
                getWriter().addText(ref, member.getName());
                getWriter().documentDescription(descriptionCell, member);
            }

            @Override
            protected String getElementTypeName() {
                return "Nested Classes";
            }

            @Override
            protected List<AdsClassDef> getAllElements(AdsClassDef c) {
                return c.getNestedClasses().getLocal().list();
            }
        }.document(hierarchy);

        //
        // Properties summary
        //
        new SummaryMembersChapterFactory<AdsPropertyDef>(source, summaryChapter) {
            @Override
            protected void documentElementSummary(AdsPropertyDef member, Table table) {
                final Table.Row methodRow = table.addNewRow();

                final Table.Row.Cell typeCell = methodRow.addNewCell();
                typeCell.setStyle("modifiers");
                String modifiers = member.getAccessFlags().getRadixdocPresentation();

                final TypeDocument typeDoc = new TypeDocument();
                typeDoc.addString(modifiers + " ").addType(member.getValue().getType(), member);
                getWriter().documentType(typeCell, typeDoc, member);

                final Table.Row.Cell descriptionCell = methodRow.addNewCell();
                final Ref ref = descriptionCell.addNewRef();
                ref.setPath(resolve(source, member));
                final RadixIconResource icon = new RadixIconResource(member.getIcon());
                ref.addNewResource().setSource(icon.getKey());
                addResource(icon);
                getWriter().addText(ref, member.getName());
            }

            @Override
            protected String getElementTypeName() {
                return "Properties";
            }

            @Override
            protected List<AdsPropertyDef> getAllElements(AdsClassDef c) {
                return c.getProperties().getLocal().list();
            }
        }.document(hierarchy);

        abstract class MethodChapterFactory extends SummaryMembersChapterFactory<AdsMethodDef> {

            final boolean constructor;

            public MethodChapterFactory(AdsClassDef source, boolean constructor) {
                super(source, summaryChapter);

                this.constructor = constructor;
            }

            @Override
            protected void documentElementSummary(AdsMethodDef member, Table table) {
                final AdsMethodDef.Profile profile = member.getProfile();
                final Table.Row methodRow = table.addNewRow();

                final Table.Row.Cell typeCell = methodRow.addNewCell();
                typeCell.setStyle("modifiers");

                final TypeDocument returnTypeDoc = new TypeDocument();

                returnTypeDoc.addString(member.getAccessFlags().getRadixdocPresentation());
                if (profile.getReturnValue() != null) {
                    returnTypeDoc.addType(profile.getReturnValue().getType(), member);
                }

                getWriter().documentType(typeCell, returnTypeDoc, member);

                final Table.Row.Cell descriptionCell = methodRow.addNewCell();
                final Ref ref = descriptionCell.addNewRef();

                final RadixIconResource icon = new RadixIconResource(member.getIcon());
                ref.addNewResource().setSource(icon.getKey());
                addResource(icon);

                ref.setPath(resolve(source, member));
                getWriter().addText(ref, member.getName());

                final TypeDocument argsTypeDoc = new TypeDocument();
                argsTypeDoc.addString("(");

                boolean first = true;
                for (final MethodParameter parametr : profile.getParametersList()) {
                    if (first) {
                        first = false;
                    } else {
                        argsTypeDoc.addString(", ");
                    }
                    argsTypeDoc.addType(parametr.getType(), member).addString(" ").addString(parametr.getName());
                }
                argsTypeDoc.addString(")");

                getWriter().documentType(descriptionCell, argsTypeDoc, member);
                getWriter().documentDescription(descriptionCell, member);
            }

            @Override
            protected List<AdsMethodDef> getAllElements(AdsClassDef c) {
                return c.getMethods().get(ExtendableDefinitions.EScope.LOCAL, new IFilter<AdsMethodDef>() {
                    @Override
                    public boolean isTarget(AdsMethodDef radixObject) {
                        return constructor == radixObject.isConstructor();
                    }
                });
            }
        }
        //
        // Constructors summary
        //
        new MethodChapterFactory(source, true) {
//            @Override
//            protected void documentTable(AdsClassDef cls, Table table) {
//                final Table.Row head = table.addNewRow();
//                head.setMeta("head");
//                final Table.Row.Cell typeColumn = head.addNewCell();
//                typeColumn.setStyle("modifiers");
//                typeColumn.addNewText().setStringValue("Modifiers");
//                final Table.Row.Cell descriptionColumn = head.addNewCell();
//                descriptionColumn.addNewText().setStringValue("Description");
//                descriptionColumn.setStyle("description");
//                table.setStyle(DefaultStyle.MEMBERS);
//            }
            @Override
            protected String getElementTypeName() {
                return "Constructors";
            }
        }.document(Collections.singletonList(source));

        //
        // Methods summary
        //
        new MethodChapterFactory(source, false) {
            @Override
            protected String getElementTypeName() {
                return "Methods";
            }
        }.document(hierarchy);
    }

    private void documentMembersDetail(final Block detail) {

        new PropertyDetailsChapterFactory(this, detail).document(source);

        new MethodDetailsChapterFactory(this, detail).document(source);

        if (source instanceof IAdsPresentableClass) {
            ClassPresentations pres = ((IAdsPresentableClass) source).getPresentations();
            ClassPresentationsRadixdoc.Factory.getInstance(detail, this, pres).writePresentationsInfo();
        }
    }
}