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

package org.radixware.kernel.common.defs.dds;

import org.radixware.kernel.common.defs.dds.utils.ISqlDef;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.defs.ClipboardSupport;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.VisitorProviderFactory;
import org.radixware.kernel.common.enums.EDdsViewWithOption;
import org.radixware.kernel.common.enums.EDocGroup;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.sqml.Sqml;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;

/**
 * Metainformation about database view. Наследуется от
 * {@link DdsTableDef таблицы}, поскольку включает в себя дополнительную
 * метаинформацию о своих колонках и т.д.
 *
 */
public class DdsViewDef extends DdsTableDef implements ISqlDef{

    private boolean distinct;

    public class UsedTableRef extends IUsedTable{

        public UsedTableRef(Id tableId) {
            super(tableId);
        }

        public DdsTableDef findTable() {
            return findTable(DdsViewDef.this);
        }

        public DdsTableDef findTable(Definition context) {
            Module module = context.getModule();
            if (module instanceof DdsModule) {
                return ((DdsModule) module).getDdsTableSearcher().findById(tableId).get();
            } else {
                return null;
            }
        }

        public Id getTableId() {
            return tableId;
        }

        public String getDisplayName() {
            DdsTableDef table = findTable();
            if (table == null) {
                return tableId.toString();
            } else {
                return table.getQualifiedName(DdsViewDef.this);
            }
        }

        public boolean isValid() {
            return findTable() != null;
        }

        @Override
        public String getAlias() {
            return null;
        }

        @Override
        public void setAlias(String alias) {
        }
        
        @Override
        public boolean useAlias() {
            return false;
        }

        @Override
        public DdsTableDef getTable() {
            return findTable();
        }
        
        @Override
        public void collectDependences(final List<Definition> list) {
            super.collectDependences(list);
            final DdsTableDef table = findTable();
            if (table != null) {
                list.add(table);
            }
        }
    }

    public class UsedTables extends IUsedTables<UsedTableRef>{
        public UsedTables() {
        }
        
        private DdsTableDef getTable(RadixObject radixObject) {
            for (RadixObject obj = radixObject; obj != null; obj = obj.getContainer()) {
                if (radixObject instanceof DdsTableDef) {
                    return (DdsTableDef) radixObject;
                }
            }
            return null;
        }

        public List<Id> getUsedTableIds() {
            final List<Id> ids = new ArrayList<>();
            synchronized (this) {
                for (UsedTableRef tableRef : this){
                    Id id = tableRef.getTableId();
                    if (!ids.contains(id)) {
                        ids.add(id);
                    }
                }
            }
            return ids;
        }

        @Override
        public IUsedTable add(Id tableId, String alias) {
            final UsedTableRef usedTable = new UsedTableRef(tableId);
            add(usedTable);
            return usedTable;
        }

        

        @Override
        public boolean useAlias() {
            return false;
        }
        
        protected void loadFrom(List<Id> usedTableIds) {
            clear();

            for (Id  id : usedTableIds){
                add(new UsedTableRef(id));
            }
            
        }
        
    }

    /**
     * Добавляется-ли атрибут DISTINCT при генерации запроса. Если true, то
     * запрос будет пропускать повторяющиеся записи. Галочка используется только
     * сервером.
     */
    public boolean isDistinct() {
        return distinct;
    }

    public boolean isDependsFromOtherView(Id viewId) {
        return isDependsFromOtherView(viewId, this, new HashSet<Id>());
    }

    private boolean isDependsFromOtherView(Id viewId, Definition root, Set<Id> path) {
        path.add(this.getId());
        final List<UsedTableRef> refs = getUsedTables().list();
        final List<DdsViewDef> referencedViews = new LinkedList<>();
        for (UsedTableRef ref : refs) {
            if (ref.getTableId() == viewId) {
                return true;
            }
            DdsTableDef table = ref.findTable(root);
            if (table instanceof DdsViewDef) {
                referencedViews.add((DdsViewDef) table);
            }
        }
        for (DdsViewDef view : referencedViews) {
            if (path.contains(view.getId())) {
                continue;
            }
            if (view.isDependsFromOtherView(viewId)) {
                return true;
            }
        }
        return false;
    }

    public void setDistinct(boolean distinct) {
        if (!Utils.equals(this.distinct, distinct)) {
            this.distinct = distinct;
            setEditState(EEditState.MODIFIED);
        }
    }
    private EDdsViewWithOption withOption = EDdsViewWithOption.NONE;

    public EDdsViewWithOption getWithOption() {
        return withOption;
    }
    private UsedTables usedTables;

    public UsedTables getUsedTables() {
        synchronized (this) {
            if (usedTables == null) {
                usedTables = new UsedTables();
            }
            return usedTables;
        }
    }

    public void setWithOption(EDdsViewWithOption withOption) {
        assert (withOption != null);
        if (!Utils.equals(this.withOption, withOption)) {
            this.withOption = withOption;
            setEditState(EEditState.MODIFIED);
        }
    }
    private final Sqml query = new DdsSqml(this);

    /**
     * Получить SQML выражение, по которому строится запрос DdsViewDef в базе
     * данных.
     */
    public Sqml getSqml() {
        return query;
    }

    protected DdsViewDef(final String name) {
        super(name);
    }

    public DdsViewDef(org.radixware.schemas.ddsdef.View xView) {
        super(xView);

        this.query.loadFrom(xView.getQuery());

        if (xView.isSetUsedTables()) {
            if (xView.getUsedTables().getTableList() != null) {
                getUsedTables().loadFrom(xView.getUsedTables().getTableList());
            }
        }

        this.setDistinct(xView.getDistinct());

        if (xView.isSetWithOption()) {
            this.setWithOption(xView.getWithOption());
        }
    }

    @Override
    public String toString() {
        return super.toString()
                + "; WithOption: " + String.valueOf(withOption)
                + "; Distinct: " + String.valueOf(distinct);
    }

    public static final class Factory {

        private Factory() {
        }

        public static DdsViewDef newInstance(final String name) {
            return new DdsViewDef(name);
        }

        public static DdsViewDef loadFrom(org.radixware.schemas.ddsdef.View xView) {
            return new DdsViewDef(xView);
        }
    }

    @Override
    public RadixIcon getIcon() {
        return DdsDefinitionIcon.VIEW;
    }

    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        super.visitChildren(visitor, provider);
        getUsedTables().visit(visitor, provider);
        query.visit(visitor, provider);
    }

    private class DdsViewClipboardSupport extends DdsClipboardSupport<DdsViewDef> {

        public DdsViewClipboardSupport() {
            super(DdsViewDef.this);
        }

        @Override
        protected XmlObject copyToXml() {
            org.radixware.schemas.ddsdef.View xView = org.radixware.schemas.ddsdef.View.Factory.newInstance();
            DdsModelWriter.writeView(DdsViewDef.this, xView);
            return xView;
        }

        @Override
        protected DdsViewDef loadFrom(XmlObject xmlObject) {
            org.radixware.schemas.ddsdef.View xView = (org.radixware.schemas.ddsdef.View) xmlObject;
            return DdsViewDef.Factory.loadFrom(xView);
        }

        @Override
        public CanPasteResult canPaste(List<Transfer> objectsInClipboard, DuplicationResolver resolver) {
            if ((getColumns().getLocal().getClipboardSupport().canPaste(objectsInClipboard, resolver) == CanPasteResult.YES)
                    || (getIndices().getLocal().getClipboardSupport().canPaste(objectsInClipboard, resolver) == CanPasteResult.YES)
                    || (getTriggers().getLocal().getClipboardSupport().canPaste(objectsInClipboard, resolver) == CanPasteResult.YES)) {
                return CanPasteResult.YES;
            } else {
                return super.canPaste(objectsInClipboard, resolver);
            }
        }

        @Override
        public void paste(List<Transfer> objectsInClipboard, DuplicationResolver resolver) {
            if (getColumns().getLocal().getClipboardSupport().canPaste(objectsInClipboard, resolver) == CanPasteResult.YES) {
                getColumns().getLocal().getClipboardSupport().paste(objectsInClipboard, resolver);
            } else if (getIndices().getLocal().getClipboardSupport().canPaste(objectsInClipboard, resolver) == CanPasteResult.YES) {
                getIndices().getLocal().getClipboardSupport().paste(objectsInClipboard, resolver);
            } else if (getTriggers().getLocal().getClipboardSupport().canPaste(objectsInClipboard, resolver) == CanPasteResult.YES) {
                getTriggers().getLocal().getClipboardSupport().paste(objectsInClipboard, resolver);
            } else {
                super.paste(objectsInClipboard, resolver);
            }
        }

        @Override
        protected Method getDecoderMethod() {
            try {
                return DdsViewDef.Factory.class.getDeclaredMethod("loadFrom", org.radixware.schemas.ddsdef.View.class);
            } catch (NoSuchMethodException | SecurityException ex) {
                Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
            }
            return null;
        }
    }

    @Override
    public ClipboardSupport<? extends DdsViewDef> getClipboardSupport() {
        return new DdsViewClipboardSupport();
    }
    public static final String VIEW_TYPE_TITLE = "View";
    public static final String VIEW_TYPES_TITLE = "Views";

    @Override
    public String getTypeTitle() {
        return VIEW_TYPE_TITLE;
    }

    @Override
    public String getTypesTitle() {
        return VIEW_TYPES_TITLE;
    }

    @Override
    public EDocGroup getDocGroup() {
        return EDocGroup.VIEW;
    }
}