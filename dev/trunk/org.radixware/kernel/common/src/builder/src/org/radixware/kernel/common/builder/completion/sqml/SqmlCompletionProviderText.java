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

package org.radixware.kernel.common.builder.completion.sqml;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import org.radixware.kernel.common.defs.dds.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.IFilter;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObjectIcon;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsExpressionPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsFilterDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.AdsSqlClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.AdsSqlClassDef.UsedTable;
import org.radixware.kernel.common.defs.ads.common.AdsUtils;
import org.radixware.kernel.common.defs.ads.common.AdsVisitorProviders;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumDef;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumItemDef;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumUtils;
import org.radixware.kernel.common.defs.ads.type.AdsClassType.EntityObjectType;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.scml.Scml;
import org.radixware.kernel.common.scml.Scml.Item;
import org.radixware.kernel.common.scml.Scml.Text;
import org.radixware.kernel.common.scml.ScmlCompletionProvider;
import org.radixware.kernel.common.sqml.ISqmlProperty;
import org.radixware.kernel.common.sqml.Sqml;
import org.radixware.kernel.common.sqml.providers.SqmlVisitorProviderFactory;
import org.radixware.kernel.common.sqml.tags.ConstValueTag;
import org.radixware.kernel.common.sqml.tags.DbFuncCallTag;
import org.radixware.kernel.common.sqml.tags.DbNameTag;
import org.radixware.kernel.common.sqml.tags.EntityRefParameterTag;
import org.radixware.kernel.common.sqml.tags.ParameterTag;
import org.radixware.kernel.common.sqml.tags.PropSqlNameTag;
import org.radixware.kernel.common.sqml.tags.PropSqlNameTag.EOwnerType;
import org.radixware.kernel.common.sqml.tags.TableSqlNameTag;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.RadixObjectsUtils;


public class SqmlCompletionProviderText implements ScmlCompletionProvider {

    private final Sqml sqml;
    private final Sqml.Text text;

    private abstract class AbstractItem implements CompletionItem {

        protected final Definition definition;
        protected final int start, end;
        protected int priority;

        public AbstractItem(Definition obj, int start, int end) {
            this.definition = obj;
            this.start = start;
            this.end = end;
        }

        @Override
        public String getSortText() {
            return definition.getName();
        }

        @Override
        public int getRelevance() {
            return Integer.MAX_VALUE - priority;
        }

        @Override
        public String getLeadDisplayText() {
            return "<b>" + definition.getName() + "</b>";
        }

        @Override
        public String getTailDisplayText() {
            if (definition == null) {
                return "";
            }
            final RadixObject ownerForQualifiedName = definition.getOwnerForQualifedName();
            if (ownerForQualifiedName == null) {
                return "";
            }
            return ownerForQualifiedName.getQualifiedName();
        }

        @Override
        public RadixIcon getIcon() {
            return definition.getIcon();
        }

        @Override
        public int getReplaceStartOffset() {
            return start;
        }

        @Override
        public int getReplaceEndOffset() {
            return end;
        }

        @Override
        public String getEnclosingSuffix() {
            return "";
        }

        @Override
        public boolean removePrevious(Scml.Item prevItem) {
            return false;
        }

        @Override
        public RadixObject getRadixObject() {
            return definition;
        }
    }

    private class PropSqlNameItem extends AbstractItem {

        EOwnerType ownerType;
        String tableAlias;

        public PropSqlNameItem(Definition obj, EOwnerType ownerType, String tableAlias, int start, int end) {
            super(obj, start, end);
            this.ownerType = ownerType;
            this.tableAlias = tableAlias;
            if (EOwnerType.THIS.equals(ownerType)) {
                priority = 100;
            } else if (EOwnerType.CHILD.equals(ownerType)) {
                priority = 2000;
            } else if (EOwnerType.PARENT.equals(ownerType)) {
                priority = 3000;
            } else {
                priority = 4000;
            }
        }

        @Override
        public Item[] getNewItems() {
            final PropSqlNameTag tag = PropSqlNameTag.Factory.newInstance();
            tag.setPropId(definition.getId());
            tag.setOwnerType(ownerType);
            tag.setPropOwnerId(definition.getOwnerDefinition().getId());
            tag.setTableAlias(tableAlias);
            return new Item[]{tag};
        }

        @Override
        public String getLeadDisplayText() {
            String prefix;
            switch (ownerType) {
                case CHILD:
                    prefix = "Child.";
                    break;
                case PARENT:
                    prefix = "Parent.";
                    break;
                case TABLE:
                    prefix = "";
                    break;
                case THIS:
                    prefix = "This.";
                    break;
                default:
                    prefix = "";
            }
            if (definition instanceof DdsColumnDef) {
                final DdsColumnDef columnDef = (DdsColumnDef) definition;
                if (columnDef.isPrimaryKey()) {
                    return "<u><b>" + prefix + definition.getName() + "</b></u>";
                } else if (columnDef.isNotNull()) {
                    return "<b>" + prefix + definition.getName() + "</b>";
                } else {
                    return prefix + definition.getName();
                }
            }
            return "<b>" + prefix + definition.getName() + "</b>";
        }

        @Override
        public RadixIcon getIcon() {
            if (definition instanceof AdsExpressionPropertyDef) {
                return RadixObjectIcon.getForValType(((AdsExpressionPropertyDef) definition).getValue().getType().getTypeId());
            }
            return super.getIcon();
        }
    }

    private class ThisFuncParamSqlNameItem extends AbstractItem {

        public ThisFuncParamSqlNameItem(Definition obj, int start, int end) {
            super(obj, start, end);
            this.priority = 500;
        }

        @Override
        public Item[] getNewItems() {
            return new Item[]{Scml.Text.Factory.newInstance(definition.getName())};
        }

        @Override
        public String getTailDisplayText() {
            return definition.getOwnerDefinition().getName();
        }
    }

    private class TableSqlNameItem extends AbstractItem {

        String alias;

        public TableSqlNameItem(Definition obj, String alias, boolean used, int start, int end) {
            super(obj, start, end);
            this.alias = alias;
            priority = used ? 700 : 4000;
        }

        @Override
        public String getLeadDisplayText() {
            final String postfix = alias == null || "".equals(alias) ? "" : " [" + alias + "]";
            return "<b>" + definition.getName() + postfix + "</b>";
        }

        @Override
        public Item[] getNewItems() {
            final TableSqlNameTag tag = TableSqlNameTag.Factory.newInstance();
            tag.setTableId(definition.getId());
            tag.setTableAlias(alias);
            return new Item[]{tag};
        }
    }

    private class EnumItem extends AbstractItem {

        public EnumItem(Definition obj, int start, int end) {
            super(obj, start, end);
            this.priority = 100;
        }

        @Override
        public Item[] getNewItems() {
            final ConstValueTag tag = ConstValueTag.Factory.newInstance();
            tag.setEnumId(definition.getOwnerDefinition().getId());
            tag.setItemId(definition.getId());
            return new Item[]{tag};
        }
    }

    private class ParameterItem extends AbstractItem {

        public ParameterItem(Definition obj, int start, int end) {
            super(obj, start, end);
            this.priority = 300;
        }

        @Override
        public Item[] getNewItems() {
            AdsFilterDef.Parameter filterParamDef = null;
            if (definition instanceof AdsFilterDef.Parameter) {
                filterParamDef = (AdsFilterDef.Parameter) definition;
                if (filterParamDef.getType().getTypeId() == EValType.PARENT_REF || filterParamDef.getType().getTypeId() == EValType.OBJECT || filterParamDef.getType().getTypeId() == EValType.ARR_REF) {
                    final EntityRefParameterTag tag = EntityRefParameterTag.Factory.newInstance();
                    if (filterParamDef.getType().getTypeId().isArrayType()){
                        tag.setExpressionList(true);
                    }
                    final AdsType type = filterParamDef.getType().resolve(filterParamDef).get();
                    if (type instanceof EntityObjectType) {
                        tag.setReferencedTableId(((EntityObjectType) type).getSourceEntityId());
                        tag.setParameterId(filterParamDef.getId());
                        return new Item[]{tag};
                    } else {
                        throw new IllegalStateException("Can not obtain Entity id from parameter by Parent Ref.");
                    }
                }
            }
            final ParameterTag tag = ParameterTag.Factory.newInstance();
            tag.setParameterId(definition.getId());
            if (filterParamDef != null) {
                if (filterParamDef.getType().getTypeId().isArrayType()){
                    tag.setExpressionList(true);
                }
            }
            return new Item[]{tag};
        }

        @Override
        public String getLeadDisplayText() {
            return "<b>:" + definition.getName() + "</b>";
        }
    }

    public SqmlCompletionProviderText(Sqml sqml, Text text) {
        this.sqml = sqml;
        this.text = text;
    }

    private class PackageItem extends AbstractItem {

        public PackageItem(DdsPackageDef obj, int start, int end) {
            super(obj, start, end);
            this.priority = 5000;
        }

        @Override
        public Item[] getNewItems() {
            final DbNameTag tag = DbNameTag.Factory.newInstance(definition.getIdPath());
            return new Scml.Tag[]{tag};
        }
    }

    private class FunctionItem extends AbstractItem {

        private final boolean removePrev;

        public FunctionItem(DdsPlSqlObjectItemDef obj, int start, int end, boolean afterPackage) {
            super(obj, start, end);
            this.priority = afterPackage ? 100 : 6000;
            this.removePrev = afterPackage;
        }

        @Override
        public boolean removePrevious(Item prevItem) {
            return removePrev;
        }

        @Override
        public Item[] getNewItems() {
            final DbFuncCallTag tag = DbFuncCallTag.Factory.newInstance();
            tag.setFunctionId(definition.getId());
            tag.setParamsDefined(false);
            return new Scml.Tag[]{tag};
        }
    }

    @Override
    public void complete(int offset, CompletionRequestor requestor) {
        //first calc typed expression with possible dots:
        Character c;
        String expr = "";
        //helps to determine that dot has been typed after some tag
        int dotOffset = -1;
        int dotCount = 0;
        for (int i = offset - 1; i >= 0; i--) {
            c = text.getText().charAt(i);

            if (c == ':' && dotCount == 0) {
                expr = text.getText().substring(i, offset);
                break;
            }

            if (c == '.') {
                dotOffset = i;
                dotCount++;
            }

            if (!Character.isJavaIdentifierPart(c) && c != '.') {
                expr = text.getText().substring(i + 1, offset);
                break;
            }

            if (i == 0) {
                expr = text.getText().substring(i, offset);
                break;
            }
        }

        //determine whether expression contains a dot
        final int dotIndex = expr.lastIndexOf('.');
        if (dotIndex != -1) {
            final String owner = expr.substring(0, dotIndex);
            final String prefix = expr.substring(dotIndex + 1);
            if (dotOffset == 0 && dotCount == 1) {
                final Scml.Tag tag = getPreviosTag();
                if (tag != null) {
                    //dot after tag
                    completeAfterDot(tag, prefix, requestor);
                } else {
                    completeAfterDot(Scml.Text.Factory.newInstance(owner), prefix, requestor);
                }
            } else {
                completeAfterDot(Scml.Text.Factory.newInstance(owner), prefix, requestor);
            }
        } else if (expr.length() > 0 && expr.charAt(0) == ':') {
            completeAfterColon(expr.substring(1), requestor);
        } else {
            completeText(expr, requestor, offset);
        }

    }

    private Scml.Tag getPreviosTag() {
        Scml.Tag tag = null;
        final int idx = sqml.getItems().indexOf(text);
        if (idx > 0) {
            final Sqml.Item item = sqml.getItems().get(idx - 1);
            if (item instanceof Scml.Tag) {
                tag = (Scml.Tag) item;
            }
        }
        return tag;
    }

    private void completeAfterColon(String prefix, CompletionRequestor requestor) {
        addParameters(prefix, requestor, true);
    }

    private void completeAfterDot(Item ownerItem, String prefix, CompletionRequestor requestor) {
        if (ownerItem instanceof Scml.Text) {
            final String owner = ((Scml.Text) ownerItem).getText();
            if ("this".equals(owner.toLowerCase())) {
                for (Definition column : getThisProperties(prefix)) {
                    requestor.accept(new PropSqlNameItem(column, EOwnerType.THIS, null, owner.length() + 1 + prefix.length(), 0));
                }
            } else if ("parent".equals(owner.toLowerCase())) {
                for (Definition column : getParentProperties(prefix)) {
                    requestor.accept(new PropSqlNameItem(column, EOwnerType.PARENT, null, owner.length() + 1 + prefix.length(), 0));
                }
            } else if ("child".equals(owner.toLowerCase())) {
                for (Definition column : getChildProperties(prefix)) {
                    requestor.accept(new PropSqlNameItem(column, EOwnerType.CHILD, null, owner.length() + 1 + prefix.length(), 0));
                }
            }
            completeAfterDotWithTextOwner(owner, prefix, requestor);
        } else if (ownerItem instanceof TableSqlNameTag) {
            final TableSqlNameTag tag = (TableSqlNameTag) ownerItem;
            completeAfterTableTag(tag, prefix, requestor);
        } else if (ownerItem instanceof DbNameTag) {
            final DbNameTag tag = (DbNameTag) ownerItem;
            final Definition def = tag.findTarget();
            if (def instanceof DdsPackageDef) {
                completeAfterPackage((DdsPackageDef) def, prefix, requestor);
            }
        }
    }

    private void completeAfterDotWithTextOwner(String owner, String prefix, CompletionRequestor requestor) {
        Map<Id, Set<String>> id2aliases = calcUsedTables();
        for (Entry<Id, Set<String>> entry : id2aliases.entrySet()) {
            Iterator<String> it = entry.getValue().iterator();
            DdsTableDef table = sqml.getEnvironment().findTableById(entry.getKey());
            while (it.hasNext()) {
                String alias = it.next();
                boolean wasEmptyAlias = false;
                if (alias.isEmpty()) {
                    wasEmptyAlias = true;
                    alias = table.getName();
                }
                if (alias.toLowerCase().equals(owner.toLowerCase())) {
                    for (Definition column : getTableColumns(table, prefix)) {
                        requestor.accept(new PropSqlNameItem(column, EOwnerType.TABLE, wasEmptyAlias ? "" : alias, owner.length() + 1 + prefix.length(), 0));
                    }
                }
            }
        }
    }

    private void completeAfterPackage(final DdsPackageDef packageDef, final String prefix, CompletionRequestor requestor) {
        if (packageDef != null) {
            for (Definition function : getPackageFunctions(packageDef, prefix)) {
                requestor.accept(new FunctionItem((DdsPlSqlObjectItemDef) function, prefix.length() + 1, 0, true));
            }
        }
    }

    private void completeAfterTableTag(final TableSqlNameTag tag, final String prefix, CompletionRequestor requestor) {
        final DdsTableDef table = tag.findTable();
        if (table != null) {
            for (Definition column : getTableColumns(table, prefix)) {
                requestor.accept(new PropSqlNameItem(column, EOwnerType.TABLE, tag.getTableAlias(), 1 + prefix.length(), 0) {

                    @Override
                    public boolean removePrevious(Item prevItem) {
                        return true;
                    }
                });
            }
        }
    }

    private void completeText(final String prefix, final CompletionRequestor requestor, final int offset) {

        addColumns(prefix, requestor);

        addTables(prefix, requestor);

        addThisFunctionParams(prefix, requestor);

        addEnumItems(prefix, requestor, offset);

        addParameters(prefix, requestor, false);

        if (requestor.isAll()) {
            addPackages(prefix, requestor);
        }
    }

    private void addThisFunctionParams(final String prefix, final CompletionRequestor requestor) {
        for (Definition parameter : getThisFunctionParams(prefix)) {
            requestor.accept(new ThisFuncParamSqlNameItem(parameter, prefix.length(), 0));
        }
    }

    private void addParameters(final String prefix, final CompletionRequestor requestor, final boolean afterSemicolon) {
        final int addLenght = afterSemicolon ? 1 : 0;
        final Definition ownerDefinition = sqml.getOwnerDefinition();

        if (ownerDefinition instanceof AdsFilterDef) {
            final AdsFilterDef filterDef = (AdsFilterDef) ownerDefinition;
            for (AdsFilterDef.Parameter parameter : filterDef.getParameters().list()) {
                if (matchPrefix(parameter, prefix)) {
                    requestor.accept(new ParameterItem(parameter, prefix.length() + addLenght, 0));
                }
            }
        }

        if (ownerDefinition instanceof AdsSqlClassDef) {
            final AdsSqlClassDef classDef = (AdsSqlClassDef) ownerDefinition;
            final IFilter<AdsPropertyDef> paramtersFilter = AdsVisitorProviders.newPropertyForSqlClassParameterFilter();

            for (Definition sqlParameter : classDef.getProperties().get(EScope.LOCAL, paramtersFilter)) {
                if (matchPrefix(sqlParameter, prefix)) {
                    requestor.accept(new ParameterItem(sqlParameter, prefix.length() + addLenght, 0));
                }
            }
        }

    }

    private void addColumns(final String prefix, final CompletionRequestor requestor) {
        for (Definition column : getThisProperties(prefix)) {
            requestor.accept(new PropSqlNameItem(column, EOwnerType.THIS, null, prefix.length(), 0));
        }
        for (Definition column : getParentProperties(prefix)) {
            requestor.accept(new PropSqlNameItem(column, EOwnerType.PARENT, null, prefix.length(), 0));
        }
        for (Definition column : getChildProperties(prefix)) {
            requestor.accept(new PropSqlNameItem(column, EOwnerType.CHILD, null, prefix.length(), 0));
        }

        if (requestor.isAll()) {
            for (Definition column : getColumns(prefix)) {
                requestor.accept(new PropSqlNameItem(column, EOwnerType.NONE, null, prefix.length(), 0));
            }
        }
    }

    private void addPackages(final String prefix, final CompletionRequestor requestor) {
        final VisitorProvider provider = SqmlVisitorProviderFactory.newDbNameTagProvider(DdsPackageDef.class);
        final List<Definition> availablePackages = RadixObjectsUtils.collectAllAround(sqml.getDefinition(), provider);
        for (Definition def : availablePackages) {
            if (matchPrefix(def, prefix)) {
                requestor.accept(new PackageItem((DdsPackageDef) def, prefix.length(), 0));
            }
        }
    }

    private void addEnumItems(final String prefix, final CompletionRequestor requestor, final int offset) {
        final Scml.Tag prevTag = getPreviosTag();
        if (prevTag instanceof PropSqlNameTag) {
            final PropSqlNameTag propSqlNameTag = (PropSqlNameTag) prevTag;
            final ISqmlProperty sqmlProperty = propSqlNameTag.findProperty();
            if (sqmlProperty instanceof DdsColumnDef) {
                final DdsColumnDef columnDef = (DdsColumnDef) sqmlProperty;

                String textAfterTag = text.getText().substring(0, offset - prefix.length());
                textAfterTag = textAfterTag.trim();

                if (isOperator(textAfterTag)) {
                    final AdsEnumDef enumDef = AdsEnumUtils.findColumnEnum(columnDef);
                    if (enumDef != null) {
                        for (AdsEnumItemDef enumItemDef : enumDef.getItems().list(EScope.ALL)) {
                            if (matchPrefix(enumItemDef, prefix)) {
                                requestor.accept(new EnumItem(enumItemDef, prefix.length(), 0));
                            }
                        }
                    }
                }
            }
        }
    }

    private void addTables(final String prefix, final CompletionRequestor requestor) {
        final Map<Id, Set<String>> id2aliases = calcUsedTables();
        for (Definition table : getTables(prefix)) {
            if (id2aliases.containsKey(table.getId())) {
                boolean wasEmpty = false;
                final Set<String> aliases = id2aliases.get(table.getId());
                for (String s : aliases) {
                    requestor.accept(new TableSqlNameItem(table, s, true, prefix.length(), 0));
                    if (s == null || "".equals(s)) {
                        wasEmpty = true;
                    }
                }
                if (!wasEmpty) {
                    requestor.accept(new TableSqlNameItem(table, null, false, prefix.length(), 0));
                }
            } else {
                if (requestor.isAll()) {
                    requestor.accept(new TableSqlNameItem(table, null, false, prefix.length(), 0));
                }
            }
        }
    }

    private boolean isOperator(String text) {
        if ("=".equals(text)
                || "<".equals(text)
                || ">".equals(text)
                || "<>".equals(text)) {
            return true;
        }
        return false;
    }

    private Map<Id, Set<String>> calcUsedTables() {
        final Map<Id, Set<String>> id2aliases = new HashMap<Id, Set<String>>();
        for (Scml.Item item : sqml.getItems()) {
            if (item instanceof TableSqlNameTag) {
                final TableSqlNameTag tag = (TableSqlNameTag) item;
                addTableAliasRecord(id2aliases, tag.getTableId(), tag.getTableAlias());
            }
        }
        if (sqml.getOwnerDefinition() instanceof AdsSqlClassDef) {
            AdsSqlClassDef sqlClass = (AdsSqlClassDef) sqml.getOwnerDefinition();
            for (UsedTable usedTable : sqlClass.getUsedTables().list()) {
                addTableAliasRecord(id2aliases, usedTable.getTableId(), usedTable.getAlias());
            }

        }
        return id2aliases;
    }

    private void addTableAliasRecord(Map<Id, Set<String>> id2aliases, Id tableId, String tableAlias) {
        final String alias = (tableAlias == null ? "" : tableAlias);
        if (id2aliases.containsKey(tableId)) {
            id2aliases.get(tableId).add(alias);
        } else {
            final Set<String> set = new HashSet<String>();
            set.add(alias);
            id2aliases.put(tableId, set);
        }
    }

    private List<Definition> getPackageFunctions(final DdsPackageDef packageDef, final String prefix) {
        final List<Definition> list = new LinkedList<Definition>();
        if (packageDef != null) {
            final VisitorProvider provider = SqmlVisitorProviderFactory.newDbFuncCallTagProvider(sqml);
            final List<DdsPlSqlObjectItemDef> functions = packageDef.getBody().getItems().list();
            if (functions != null) {
                for (DdsPlSqlObjectItemDef function : functions) {
                    if (provider.isTarget(function) && matchPrefix(function, prefix)) {
                        list.add(function);
                    }
                }
            }
        }
        return list;
    }

    private List<Definition> getTables(final String prefix) {
        final VisitorProvider provider = SqmlVisitorProviderFactory.newTableSqlNameTagProvider();
        final List<Definition> list = new LinkedList<Definition>();
        final Collection<Definition> c = RadixObjectsUtils.collectAllAround(sqml, provider);
        for (Definition def : c) {
            if (matchPrefix(def, prefix)) {
                list.add(def);
            }
        }
        return list;
    }

    private List<Definition> getTableColumns(final DdsTableDef table, final String prefix) {
        final VisitorProvider provider = SqmlVisitorProviderFactory.newPropSqlNameTagProvider(sqml, EOwnerType.TABLE);
        final List<Definition> list = new LinkedList<Definition>();
        if (table != null) {
            final List<DdsColumnDef> columns = table.getColumns().get(EScope.ALL);
            if (columns != null) {
                for (DdsColumnDef column : columns) {
                    if (provider.isTarget(column) && matchPrefix(column, prefix)) {
                        list.add(column);
                    }
                }
            }
            AdsEntityClassDef entity = AdsUtils.findEntityClass(table);
            if (entity != null) {
                for (AdsPropertyDef property : entity.getProperties().get(EScope.LOCAL)) {
                    if (property instanceof AdsExpressionPropertyDef && matchPrefix(property, prefix)) {
                        list.add(property);
                    }
                }
            }
        }
        return list;
    }

    private List<Definition> getThisProperties(final String wholePrefix) {
        final List<Definition> thisPropsOwners = sqml.getEnvironment().findThisPropertiesOwner();
        if (thisPropsOwners.isEmpty()) {
            return Collections.emptyList();
        }
        final String prefix = removeOwnerType(wholePrefix, "this");
        final VisitorProvider provider = SqmlVisitorProviderFactory.newPropSqlNameTagProvider(sqml, EOwnerType.THIS);
        final List<Definition> matchedDefinitions = new ArrayList<Definition>(10);
        for (Definition thisPropsOwner : thisPropsOwners) {
            final Collection<Definition> thisProperties = RadixObjectsUtils.collectAllInside(thisPropsOwner, provider);

            for (Definition def : thisProperties) {
                if (matchPrefix(def, prefix)) {
                    matchedDefinitions.add(def);
                }
            }
        }
        return matchedDefinitions;
    }

    private List<Definition> getColumns(final String prefix) {
        final VisitorProvider provider = SqmlVisitorProviderFactory.newPropSqlNameTagProvider(sqml, EOwnerType.NONE);
        final List<Definition> list = new LinkedList<Definition>();
        final DdsTableDef thisTable = sqml.getEnvironment().findThisTable();
        if (thisTable != null) {
            final List<DdsColumnDef> columns = thisTable.getColumns().get(EScope.ALL);
            if (columns != null) {
                for (DdsColumnDef column : columns) {
                    if (provider.isTarget(column) && matchPrefix(column, prefix)) {
                        list.add(column);
                    }
                }
            }
        }
        return list;
    }

    private List<Definition> getParentProperties(final String wholePrefix) {
        final Definition parentPropsOwner = sqml.getEnvironment().findParentPropertiesOwner();
        if (parentPropsOwner == null) {
            return Collections.emptyList();
        }
        final String prefix = removeOwnerType(wholePrefix, "parent");
        final VisitorProvider provider = SqmlVisitorProviderFactory.newPropSqlNameTagProvider(sqml, EOwnerType.PARENT);
        final Collection<Definition> parentProperties = RadixObjectsUtils.collectAllInside(parentPropsOwner, provider);
        final List<Definition> matchedProperties = new LinkedList<Definition>();
        for (Definition def : parentProperties) {
            if (matchPrefix(def, prefix)) {
                matchedProperties.add(def);
            }
        }
        return matchedProperties;
    }

    private List<Definition> getChildProperties(final String wholePrefix) {
        final Definition childPropsOwner = sqml.getEnvironment().findParentPropertiesOwner();
        if (childPropsOwner == null) {
            return Collections.emptyList();
        }

        final String prefix = removeOwnerType(wholePrefix, "child");
        final VisitorProvider provider = SqmlVisitorProviderFactory.newPropSqlNameTagProvider(sqml, EOwnerType.CHILD);
        final Collection<Definition> childProperties = RadixObjectsUtils.collectAllAround(childPropsOwner, provider);
        final List<Definition> matchedProperties = new LinkedList<Definition>();
        for (Definition def : childProperties) {
            if (matchPrefix(def, prefix)) {
                matchedProperties.add(def);
            }
        }
        return matchedProperties;
    }

    private List<Definition> getThisFunctionParams(final String prefix) {
        final List<Definition> list = new LinkedList<Definition>();
        final DdsFunctionDef thisFunction = sqml.getEnvironment().findThisFunction();
        if (thisFunction != null) {
            final List<DdsParameterDef> parameters = thisFunction.getParameters().list();
            if (parameters != null) {
                for (DdsParameterDef parameter : parameters) {
                    if (matchPrefix(parameter, prefix)) {
                        list.add(parameter);
                    }
                }
            }
        }
        return list;
    }

    private String removeOwnerType(String prefix, String ownerType) {
        ownerType = ownerType.toLowerCase();
        prefix = prefix.toLowerCase();
        if (prefix.startsWith(ownerType + ".")) {
            prefix = prefix.substring(ownerType.length() + 1);
        } else if (prefix.length() <= ownerType.length() && ownerType.startsWith(prefix)) {
            prefix = "";
        }
        return prefix;
    }

    private boolean matchPrefix(Definition def, String prefix) {
        if (def == null || prefix == null || def.getName() == null) {
            return false;

        }
        return def.getName().toLowerCase().startsWith(prefix.toLowerCase());
    }
}
