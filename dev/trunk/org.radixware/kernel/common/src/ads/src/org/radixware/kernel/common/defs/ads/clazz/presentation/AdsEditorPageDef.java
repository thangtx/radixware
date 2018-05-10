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
package org.radixware.kernel.common.defs.ads.clazz.presentation;

import java.lang.reflect.Method;
import java.util.*;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.check.RadixProblem;
import org.radixware.kernel.common.defs.*;
import org.radixware.kernel.common.defs.ads.*;
import org.radixware.kernel.common.defs.ads.AdsDefinition.Hierarchy;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.form.AdsFormHandlerClassDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.EditorPages.EditorPagesOperationSupport;
import org.radixware.kernel.common.defs.ads.common.AdsUtils;
import org.radixware.kernel.common.defs.ads.common.ICustomViewable;
import org.radixware.kernel.common.defs.ads.common.ICustomViewable.CustomViewSupport;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsExplorerItemDef;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsSelectorExplorerItemDef;
import org.radixware.kernel.common.defs.ads.localization.AdsLocalizingBundleDef;
import org.radixware.kernel.common.defs.ads.localization.AdsMultilingualStringDef;
import org.radixware.kernel.common.defs.ads.module.AdsSearcher;
import org.radixware.kernel.common.defs.ads.radixdoc.EditorPageRadixdoc;
import org.radixware.kernel.common.defs.ads.src.IJavaSource;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.clazz.presentation.AdsEditorPageWriter;
import org.radixware.kernel.common.defs.ads.ui.AdsAbstractUIDef;
import org.radixware.kernel.common.defs.ads.ui.AdsCustomPageEditorDef;
import org.radixware.kernel.common.defs.ads.ui.rwt.AdsRwtCustomPageEditorDef;
import org.radixware.kernel.common.defs.localization.ILocalizedDef;
import org.radixware.kernel.common.defs.localization.IMultilingualStringDef;
import org.radixware.kernel.common.enums.*;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.radixdoc.DocumentOptions;
import org.radixware.kernel.common.radixdoc.IRadixdocPage;
import org.radixware.kernel.common.radixdoc.IRadixdocProvider;
import org.radixware.kernel.common.radixdoc.RadixdocSupport;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.RadixObjectsUtils;
import org.radixware.schemas.adsdef.AbstractDialogDefinition;
import org.radixware.schemas.adsdef.EditorPage;
import org.radixware.schemas.adsdef.EditorPageItem;
import org.radixware.schemas.adsdef.EditorPagePropertyGroup;
import org.radixware.schemas.radixdoc.Page;

public class AdsEditorPageDef extends AdsPresentationsMember implements IJavaSource, ICustomViewable<AdsEditorPageDef, AdsAbstractUIDef>, IOverridable<AdsEditorPageDef>, IOverwritable<AdsEditorPageDef>,
        IClientDefinition, IInheritableTitledDefinition, IRadixdocProvider {

    public static final class Problems extends AdsDefinitionProblems {

        public static final int PROPERTY_WILL_NOT_BE_ACCESSIBLE_FOR_CLIENT_ENVIRONMENT = 20001;

        public Problems(AdsDefinition owner, List warnings) {
            super(owner, warnings);
        }

        @Override
        public boolean canSuppressWarning(int code) {
            switch (code) {
                case PROPERTY_WILL_NOT_BE_ACCESSIBLE_FOR_CLIENT_ENVIRONMENT:
                    return true;
                default:
                    return false;
            }
        }
    }

    public static final class Factory {

        public static AdsEditorPageDef newInstance() {
            return new AdsEditorPageDef();
        }

        public static AdsEditorPageDef loadFrom(org.radixware.schemas.adsdef.EditorPage xDef) {
            if (xDef == null) {
                return newInstance();
            }
            return new AdsEditorPageDef(xDef);
        }
    }

    public final static class PagePropertyRef extends RadixObject {

        public static final class Factory {

            private Factory() {
                super();
            }

            public static PagePropertyRef newInstance(final Id id) {
                return new PagePropertyRef(id, 0, 0);
            }
            
            public static PagePropertyRef copyPagePropertyRef(PagePropertyRef sourse) {
                return new PagePropertyRef(sourse);
            }
            
            public static PagePropertyRef loadFrom(EditorPageItem xProp) {
                return new PagePropertyRef(xProp);
            }
        }
        
        private final Id id;
        private final AdsProperiesGroupDef groupDef;
        private int col;
        private int row;
        private int colSpan = 1;
        private boolean glueToLeft;
        private boolean glueToRight;
        
        public PagePropertyRef(EditorPageItem xProp) {
            if (xProp.getPropertyGroup() != null) {
                this.groupDef = new AdsProperiesGroupDef(xProp.getPropertyGroup());
                groupDef.setContainer(this);
            } else {
                this.groupDef = null;
            }
            this.id = xProp.isSetId()? xProp.getId(): null;
            
            this.col = xProp.getColumn();
            this.row = xProp.getRow();
            this.colSpan = xProp.getColSpan();
            this.glueToLeft = xProp.getGlutToLeft();
            this.glueToRight = xProp.getGlutToRight();
            
        }
        
        public PagePropertyRef(PagePropertyRef source) {
            this.id = source.getId();
            this.col = source.getColumn();
            this.row = source.getRow();
            this.groupDef = source.getGroupDef();
            if (this.groupDef != null){
                groupDef.setContainer(this);
            }
        }
        
        public PagePropertyRef(Id id, int col, int row) {
            this.id = id;
            this.col = col;
            this.row = row;
            this.groupDef = null;
        }
        
        public PagePropertyRef(AdsProperiesGroupDef group, int col, int row) {
            this.id = null;
            this.col = col;
            this.row = row;
            this.groupDef = group;
            groupDef.setContainer(this);
        }

        public Id getId() {
            return id;
        }

        public int getColumn() {
            return col;
        }

        public void setColumn(int col) {
            if (col < 0) {
                return;
            }
            this.col = col;
            setEditState(EEditState.MODIFIED);
        }

        public int getRow() {
            return row;
        }

        public void setRow(int row) {
            if (row < 0) {
                return;
            }
            this.row = row;
            setEditState(EEditState.MODIFIED);
        }

        public int getColumnSpan() {
            return colSpan < 1 ? 1 : colSpan;
        }

        public void setColumnSpan(int colSpan) {
            if (this.colSpan != colSpan) {
                this.colSpan = colSpan;
                setEditState(EEditState.MODIFIED);
            }
        }

        public boolean isGlueToLeft() {
            return glueToLeft;
        }

        public void setGlueToLeft(boolean glueToLeft) {
            if (this.glueToLeft != glueToLeft) {
                this.glueToLeft = glueToLeft;
                setEditState(EEditState.MODIFIED);
            }
        }

        public boolean isGlueToRight() {
            return glueToRight;
        }

        public void setGlueToRight(boolean glueToRight) {
            if (this.glueToRight != glueToRight) {
                this.glueToRight = glueToRight;
                setEditState(EEditState.MODIFIED);
            }
        }

        public AdsDefinition findItem() {
            final AdsEditorPageDef page = getOwnerEditorPageDef();
            if (page == null) {
                return null;
            }
            if (id != null) {
                return page.getUsedProperties().getReference(id).findProperty();
            } else {
                return groupDef;
            }
        }
        
        public String getReferencedPropertyName() {
            final AdsDefinition def = findItem();
            return def == null ? "#" + id.toString() : def.getQualifiedName(this);
        }

        public AdsEditorPageDef getOwnerEditorPageDef() {
            return RadixObjectsUtils.findContainer(getContainer(), AdsEditorPageDef.class);
        }

        protected synchronized void appendTo(EditorPageItem xProp, ESaveMode saveMode) {
            if (this.id != null){
                xProp.setId(id);
            }
            
            if (this.groupDef != null){
                EditorPagePropertyGroup xGroup = xProp.addNewPropertyGroup();
                groupDef.appendTo(xGroup, saveMode);
            }
            
            xProp.setRow(row);
            xProp.setColumn(col);
            if (colSpan > 1) {
                xProp.setColSpan(colSpan);
            }
            if (glueToLeft) {
                xProp.setGlutToLeft(true);
            }
            if (glueToRight) {
                xProp.setGlutToRight(true);
            }
        }
        
        @Override
        public void setContainer(RadixObject container) {
            super.setContainer(container);
        }

        public AdsProperiesGroupDef getGroupDef() {
            return groupDef;
        }

        @Override
        public void visitChildren(IVisitor visitor, VisitorProvider provider) {
            super.visitChildren(visitor, provider);
            if (groupDef != null){
                groupDef.visit(visitor, provider);
            }
        }

    }

    public static final class Properties extends RadixObjects<PagePropertyRef> implements IPagePropertyGroup{

        private Properties(AdsEditorPageDef container, EditorPage.Properties xDef) {
            this(container);
            if (xDef != null) {
                for (EditorPageItem xProp : xDef.getPropertyList()) {
                    add(PagePropertyRef.Factory.loadFrom(xProp));
                }
            }
        }

        private Properties(AdsEditorPageDef container) {
            this.setContainer(container);
        }

        private Properties(AdsEditorPageDef container, RadixObjects<PagePropertyRef> source) {
            this(container);
            if (!source.isEmpty()) {
                for (PagePropertyRef ref : source) {
                    add(new PagePropertyRef(ref));
                }
            }
        }

        @Override
        public void getIds(List<Id> ids) {
            for (PagePropertyRef ref : this) {
                if (ref.getId() != null) {
                    ids.add(ref.getId());
                } else if (ref.getGroupDef() != null){
                    ref.getGroupDef().getIds(ids);
                }
            }
        }

        public List<Id> getIds() {
            if (isEmpty()) {
                return Collections.emptyList();
            } else {
                final ArrayList<Id> ids = new ArrayList<>();
                getIds(ids);
                return ids;
            }
        }

        boolean contains(final Id id) {
            return deepContains(id, false);
        }
        
        boolean deepContains(final Id id, boolean isDeep) {
            if (isEmpty()) {
                return false;
            } else {
                for (PagePropertyRef ref : this) {
                    if (ref.getId() == id) {
                            return true;
                    } else {
                        if (isDeep && ref.getGroupDef() != null){
                            PagePropertyRef pageProperty = ref.getGroupDef().findItemById(id);
                            if (pageProperty != null){
                                return true;
                            }
                        }
                    }
                }
                return false;
            }
        }

        private void appendTo(EditorPage xDef, ESaveMode saveMode) {
            if (!isEmpty()) {
                final EditorPage.Properties xProps = xDef.addNewProperties();
                for (PagePropertyRef ref : this) {
                    ref.appendTo(xProps.addNewProperty(), saveMode);
                }
            }
        }

        @Override
        public PagePropertyRef findItemById(Id id) {
            synchronized (this) {
                for (PagePropertyRef ref : this) {
                    if (ref.getId() == id) {
                        return ref;
                    } else if (ref.getGroupDef() != null) {
                        AdsProperiesGroupDef group = ref.getGroupDef();
                        if (group.getId() == id) {
                            return ref;
                        } else {
                            PagePropertyRef pageProperty = group.findItemById(id);
                            if (pageProperty != null) {
                                return pageProperty;
                            }
                        }
                    }
                }
                return null;
            }
        }

        public boolean removeByPropId(Id id) {
            synchronized (this) {
                PagePropertyRef ref = findItemById(id);
                if (ref == null) {
                    return false;
                } else {
                    return remove(ref);
                }
            }
        }

        public PagePropertyRef addPropId(Id id) {
            synchronized (this) {
                PagePropertyRef ref = findItemById(id);
                if (ref != null) {
                    return ref;
                } else {
                    ref = PagePropertyRef.Factory.newInstance(id);
                    int row = getAutoRowNumver(0);
                    add(ref);
                    ref.setColumn(0);
                    ref.setRow(row);
                    return ref;
                }
            }
        }

        @Override
        public PagePropertyRef addGroup(AdsProperiesGroupDef group) {
            PagePropertyRef ref = new PagePropertyRef(group, 0, 0);
            synchronized (this) {
                int row = getAutoRowNumver(0);
                add(ref);
                ref.setColumn(0);
                ref.setRow(row);
                return ref;
            }
        }
        
        
        @Override
        public boolean removeGroup(AdsProperiesGroupDef group) {
            PagePropertyRef ref = group.getOwnerRef();
            if (ref != null) {
                return remove(ref);
            }
            return false;
        }
       
        @Override
        public int getColumnCount() {
            int minColumn = -1;//changed by yremizov, old code: int minColumn = 0;
            for (PagePropertyRef ref : this) {
                if (ref.getColumn() > minColumn) {
                    minColumn = ref.getColumn();
                }
            }
            return minColumn + 1;
        }

        @Override
        public int getRowCount() {
            int minRow = -1;//changed by yremizov, old code: int minRow = 0;
            for (PagePropertyRef ref : this) {
                if (ref.getRow() > minRow) {
                    minRow = ref.getRow();
                }
            }
            return minRow + 1;
        }

        private int getAutoRowNumver(int column) {
            boolean[] allownerRows = new boolean[getRowCount() + 1];//changed by yremizov old, code:  boolean[] allownerRows = new boolean[getRowCount()];
            Arrays.fill(allownerRows, false);
            for (PagePropertyRef ref : this) {
                if (ref.getColumn() == column) {
                    allownerRows[ref.getRow()] = true;
                }
            }
            for (int i = 0; i < allownerRows.length; i++) {
                if (!allownerRows[i]) {
                    return i;
                }
            }
            return 0;
        }
        
        public List<AdsProperiesGroupDef> getProperiesGroups(){
            List<AdsProperiesGroupDef> result = new ArrayList<>();
            for(PagePropertyRef ref : this){
                if (ref.getGroupDef() != null){
                    AdsProperiesGroupDef group = ref.getGroupDef();
                    result.add(group);
                    group.collectProperiesGroups(result);
                }
            }
            return result;
        }

        @Override
        public void collectDependences(List<Definition> list) {
            super.collectDependences(list);
            for (PagePropertyRef ref : this) {
                final AdsDefinition def = ref.findItem();
                if (def != null) {
                    list.add(def);
                }
            }
        }
    }
    private Id embeddedExplorerItemId;
    private Id iconId;
    private EEditorPageType type;
    private final Properties properties;
    private ERuntimeEnvironmentType clientEnvironment = ERuntimeEnvironmentType.COMMON_CLIENT;
    private final ICustomViewable.CustomViewSupport<AdsEditorPageDef, AdsAbstractUIDef> customViewSuppoort = new CustomViewSupport<AdsEditorPageDef, AdsAbstractUIDef>(this) {
        @Override
        protected AdsAbstractUIDef createOrLoadCustomView(AdsEditorPageDef context, ERuntimeEnvironmentType env, AbstractDialogDefinition xDef) {
            if (env == ERuntimeEnvironmentType.EXPLORER) {
                if (xDef != null) {
                    return AdsCustomPageEditorDef.Factory.loadFrom(context, xDef);
                } else {
                    return AdsCustomPageEditorDef.Factory.newInstance(context);
                }
            } else if (env == ERuntimeEnvironmentType.WEB) {
                if (xDef != null) {
                    return AdsRwtCustomPageEditorDef.Factory.loadFrom(context, xDef);
                } else {
                    return AdsRwtCustomPageEditorDef.Factory.newInstance(context);
                }
            } else {
                throw new UnsupportedOperationException("Not supported yet");
            }
        }

        @Override
        public boolean isUseCustomView(ERuntimeEnvironmentType env) {
            if (clientEnvironment == ERuntimeEnvironmentType.WEB || clientEnvironment == ERuntimeEnvironmentType.EXPLORER) {
                return clientEnvironment == env && super.isUseCustomView(env);
            } else {
                return super.isUseCustomView(env);
            }
        }
    };
    private Problems warningsSupport = null;

    private AdsEditorPageDef() {
        super(Id.Factory.newInstance(EDefinitionIdPrefix.EDITOR_PAGE), "NewEditorPage", null);
        this.embeddedExplorerItemId = null;
        this.iconId = null;
        this.properties = new Properties(this);
        this.type = EEditorPageType.STANDARD;

    }

    private AdsEditorPageDef(AdsEditorPageDef source) {
        super(source.getId(), source.getName(), null);
        this.embeddedExplorerItemId = source.embeddedExplorerItemId;
        this.iconId = source.iconId;
        this.properties = new Properties(this, source.getProperties());
        this.type = source.type;

    }

    private AdsEditorPageDef(org.radixware.schemas.adsdef.EditorPage xDef) {
        super(xDef);
        this.embeddedExplorerItemId = xDef.getEmbeddedExplorerItemId();
        this.iconId = xDef.getIconId();
        this.type = xDef.getType();

        if (type == EEditorPageType.STANDARD) {
            this.properties = new Properties(this, xDef.getProperties());
        } else {
            this.properties = new Properties(this);
        }

        if (this.type == EEditorPageType.CUSTOM) {
            customViewSuppoort.loadCustomView(ERuntimeEnvironmentType.EXPLORER, xDef.getView());
            customViewSuppoort.loadCustomView(ERuntimeEnvironmentType.WEB, xDef.getWebView());
        }
        if (xDef.getClientEnvironment() != null) {
            clientEnvironment = xDef.getClientEnvironment();
        }

        if (xDef.isSetSuppressedWarnings()) {
            List list = xDef.getSuppressedWarnings();
            if (!list.isEmpty()) {
                this.warningsSupport = new Problems(this, list);
            }
        }
    }

    @Override
    public CustomViewSupport<AdsEditorPageDef, AdsAbstractUIDef> getCustomViewSupport() {
        return customViewSuppoort;
    }

    @Override
    public void afterOverride() {
        setTitleInherited(true);
    }

    @Override
    public void afterOverwrite() {
        AdsAbstractUIDef def = customViewSuppoort.getCustomView(ERuntimeEnvironmentType.EXPLORER);
        if (def != null) {
            def.afterOverwrite();
        }
        def = customViewSuppoort.getCustomView(ERuntimeEnvironmentType.WEB);
        if (def != null) {
            def.afterOverwrite();
        }
        setTitleInherited(true);
    }

    @Override
    public boolean allowOverwrite() {
        return true;
    }

    @Override
    public ERuntimeEnvironmentType getClientEnvironment() {
        EditorPages editorPages = getOwnerEditorPages();
        if (editorPages == null) {
            return clientEnvironment == null ? ERuntimeEnvironmentType.COMMON_CLIENT : clientEnvironment;
        } else {
            Definition def = editorPages.getOwnerDefinition();
            if (def instanceof AdsEditorPresentationDef) {
                ERuntimeEnvironmentType env = ((AdsEditorPresentationDef) def).getClientEnvironment();
                if (env == ERuntimeEnvironmentType.COMMON_CLIENT) {
                    return clientEnvironment == null ? ERuntimeEnvironmentType.COMMON_CLIENT : clientEnvironment;
                } else {
                    return env;
                }
            } else if (def instanceof AdsFormHandlerClassDef) {
                ERuntimeEnvironmentType env = ((AdsFormHandlerClassDef) def).getClientEnvironment();
                if (env == ERuntimeEnvironmentType.COMMON_CLIENT) {
                    return clientEnvironment == null ? ERuntimeEnvironmentType.COMMON_CLIENT : clientEnvironment;
                } else {
                    return env;
                }
            } else {
                return clientEnvironment == null ? ERuntimeEnvironmentType.COMMON_CLIENT : clientEnvironment;
            }
        }
    }

    public boolean canChangeClientEnvironment() {
        EditorPages editorPages = getOwnerEditorPages();
        if (editorPages == null) {
            return true;
        } else {
            Definition def = editorPages.getOwnerDefinition();
            if (def instanceof AdsEditorPresentationDef) {
                ERuntimeEnvironmentType env = ((AdsEditorPresentationDef) def).getClientEnvironment();
                if (env == ERuntimeEnvironmentType.COMMON_CLIENT) {
                    return true;
                } else {
                    return false;
                }
            } else if (def instanceof AdsFormHandlerClassDef) {
                ERuntimeEnvironmentType env = ((AdsFormHandlerClassDef) def).getClientEnvironment();
                if (env == ERuntimeEnvironmentType.COMMON_CLIENT) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return true;
            }
        }
    }

    public void setClientEnvironment(ERuntimeEnvironmentType clientEnvironment) {
        if (this.clientEnvironment != clientEnvironment) {
            this.clientEnvironment = clientEnvironment;
            if (getType() == EEditorPageType.CUSTOM) {
                customViewSuppoort.getChangeSupport().fireEvent(new CustomViewChangedEvent());
            }
            setEditState(EEditState.MODIFIED);
        }
    }

    public Properties getProperties() {
        return properties;
    }

    public Id getCustomViewId(ERuntimeEnvironmentType env) {
        if (customViewSuppoort.isUseCustomView(env)) {
            AdsAbstractUIDef cv = customViewSuppoort.getCustomView(env);
            if (cv != null) {
                return cv.getId();
            } else {
                return null;
            }
        } else {
            return null;
        }
//        if (customViewId == null) {
//            customViewId = Id.Factory.newInstance( EDefinitionIdPrefix.CUSTOM_EDITOR_PAGE);
//        }
//        return customViewId;
//        return Id.Factory.changePrefix(getId(), EDefinitionIdPrefix.CUSTOM_EDITOR_PAGE);
    }

    public Id getEmbeddedExplorerItemId() {
        if (getType() == EEditorPageType.CONTAINER) {
            return embeddedExplorerItemId;
        } else {
            return null;
        }
    }

    /**
     * Associates container editor page with given id of explorer item
     *
     * @throws {@linkplain DefinitionError} if
     * {@linkplain #getType() type of the page} is other than {
     * @linkplaun EEditorPageType#CONTAINER}
     */
    public void setEmbeddedExplorerItemId(Id embeddedExplorerItemId) {
        if (this.type != EEditorPageType.CONTAINER) {
            throw new DefinitionError("Embedded explorer item may be defined for container pages only.", this);
        }
        this.embeddedExplorerItemId = embeddedExplorerItemId;
        setEditState(EEditState.MODIFIED);
    }

    public AdsExplorerItemDef findEmbeddedExplorerItem() {
        if (getEmbeddedExplorerItemId() == null) {
            return null;
        } else {
            if (getOwnerDefinition() instanceof AdsEditorPresentationDef) {
                AdsEditorPresentationDef epr = (AdsEditorPresentationDef) getOwnerDefinition();
                return epr.getExplorerItems().findChildExplorerItem(getEmbeddedExplorerItemId());
            } else {
                return null;
            }
        }
    }

    public Id getIconId() {
        return iconId;
    }

    public void setIconId(Id iconId) {
        this.iconId = iconId;
        setEditState(EEditState.MODIFIED);
    }

    /**
     * Returns editor page type
     */
    public synchronized EEditorPageType getType() {
        return type;
    }

    /**
     * Sets the type of the page to the value given by newType parameter Side
     * effects: <ul> <li>if newType is {@linkplain EEditorPageType#CUSTOM}
     * custom view for the page will be created</li> <li>if newType is not
     * {@linkplain EEditorPageType#CUSTOM} custom view for the page will be
     * deleted</li> <li>if newType is {@linkplain EEditorPageType#CONTAINER}
     * properties list for the page will be cleaned</li> <li>if newType is not
     * {@linkplain EEditorPageType#CONTAINER} page will be deassociated with
     * explorer item</li> <ul>
     */
    public synchronized void setType(EEditorPageType newType) {
        if (newType == EEditorPageType.CUSTOM) {
            if (!customViewSuppoort.isUseCustomView(ERuntimeEnvironmentType.EXPLORER)) {
                customViewSuppoort.setUseCustomView(ERuntimeEnvironmentType.EXPLORER, true);
            }
            if (!customViewSuppoort.isUseCustomView(ERuntimeEnvironmentType.WEB)) {
                customViewSuppoort.setUseCustomView(ERuntimeEnvironmentType.WEB, true);
            }

            this.embeddedExplorerItemId = null;
        } else {
            if (newType != EEditorPageType.STANDARD) {
                this.properties.clear();
            }
            if (type == EEditorPageType.CUSTOM) {
                if (customViewSuppoort.isUseCustomView(ERuntimeEnvironmentType.EXPLORER)) {
                    customViewSuppoort.setUseCustomView(ERuntimeEnvironmentType.EXPLORER, false);
                }
                if (customViewSuppoort.isUseCustomView(ERuntimeEnvironmentType.WEB)) {
                    customViewSuppoort.setUseCustomView(ERuntimeEnvironmentType.WEB, false);
                }
            }
        }
        this.type = newType;
        setEditState(EEditState.MODIFIED);
    }

    public PropertyUsageSupport getUsedProperties() {
        return PropertyUsageSupport.newInstance(this, properties.getIds());
    }

    public synchronized void appendTo(org.radixware.schemas.adsdef.EditorPage xDef, ESaveMode saveMode) {
        super.appendTo(xDef, saveMode);
        xDef.setType(type);
        if (embeddedExplorerItemId != null) {
            xDef.setEmbeddedExplorerItemId(embeddedExplorerItemId);
        }
        if (iconId != null) {
            xDef.setIconId(iconId);
        }
        if (type == EEditorPageType.STANDARD) {
            properties.appendTo(xDef, saveMode);
        }
//        if (customViewId != null) {
//            xDef.setCustomEditorId(customViewId);
//        }
        if (customViewSuppoort.getCustomView(ERuntimeEnvironmentType.EXPLORER) != null) {
            ((AdsCustomPageEditorDef) customViewSuppoort.getCustomView(ERuntimeEnvironmentType.EXPLORER)).appendTo(xDef, saveMode);
        }
        if (customViewSuppoort.getCustomView(ERuntimeEnvironmentType.WEB) != null) {
            ((AdsRwtCustomPageEditorDef) customViewSuppoort.getCustomView(ERuntimeEnvironmentType.WEB)).appendTo(xDef, saveMode);
        }
        if (clientEnvironment != ERuntimeEnvironmentType.COMMON_CLIENT) {
            xDef.setClientEnvironment(clientEnvironment);
        }

        if (saveMode == ESaveMode.NORMAL) {
            if (warningsSupport != null && !warningsSupport.isEmpty()) {
                int[] warnings = warningsSupport.getSuppressedWarnings();
                List<Integer> list = new ArrayList<>(warnings.length);
                for (int w : warnings) {
                    list.add(Integer.valueOf(w));
                }
                xDef.setSuppressedWarnings(list);
            }
        }
    }

    @Override
    public RadixIcon getIcon() {
        switch (type) {
            case CUSTOM:
                return AdsDefinitionIcon.EDITOR_PAGE_CUSTOM;
            default:
                if (!properties.isEmpty()) {
                    return AdsDefinitionIcon.EDITOR_PAGE_PROP;
                } else {
                    return AdsDefinitionIcon.EDITOR_PAGE;
                }
        }
    }

    @Override
    public JavaSourceSupport getJavaSourceSupport() {
        return new JavaSourceSupport(this) {
            @Override
            public CodeWriter getCodeWriter(UsagePurpose purpose) {
                return new AdsEditorPageWriter(this, AdsEditorPageDef.this, purpose);
            }
        };
    }

    @Override
    public void collectDependences(List<Definition> list) {
        AdsExplorerItemDef ei = findEmbeddedExplorerItem();
        if (ei != null) {
            list.add(ei);
        }
//        properties.collectDependences(list);

        if (iconId != null) {
            AdsSearcher.Factory.newImageSearcher(this).findById(iconId).save(list);
        }
    }

    @Override
    public ClipboardSupport<AdsEditorPageDef> getClipboardSupport() {
        return new AdsClipboardSupport<AdsEditorPageDef>(this) {
            @Override
            protected XmlObject copyToXml() {
                org.radixware.schemas.adsdef.EditorPage xDef = org.radixware.schemas.adsdef.EditorPage.Factory.newInstance();
                appendTo(xDef, ESaveMode.NORMAL);
                return xDef;
            }

            @Override
            protected AdsEditorPageDef loadFrom(XmlObject xmlObject) {
                if (xmlObject instanceof org.radixware.schemas.adsdef.EditorPage) {
                    return Factory.loadFrom((org.radixware.schemas.adsdef.EditorPage) xmlObject);
                } else {
                    return super.loadFrom(xmlObject);
                }
            }

            @Override
            protected Method getDecoderMethod() {
                try {
                    return AdsEditorPageDef.Factory.class.getDeclaredMethod("loadFrom", org.radixware.schemas.adsdef.EditorPage.class);
                } catch (NoSuchMethodException | SecurityException ex) {
                    return null;
                }
            }

            @Override
            protected boolean isIdChangeRequired(RadixObject copyRoot) {
                if (copyRoot instanceof AdsClassDef) {
                    if (!radixObject.getHierarchy().findOverridden().isEmpty() || !radixObject.getHierarchy().findOverwritten().isEmpty()) {
                        return false;
                    } else {
                        return super.isIdChangeRequired(copyRoot);
                    }
                } else {
                    return super.isIdChangeRequired(copyRoot);
                }
            }
        };
    }

    @Override
    public boolean isSuitableContainer(AdsDefinitions collection) {
        return collection instanceof EditorPages.PageList;
    }

    public AdsEditorPresentationDef findOwnerEditorPresentation() {
        for (RadixObject owner = getContainer(); owner != null; owner = owner.getContainer()) {
            if (owner instanceof AdsEditorPresentationDef) {
                return (AdsEditorPresentationDef) owner;
            }
        }
        return null;
    }

    public AbstractFormPresentations findOwnerPresentations() {
        for (RadixObject owner = getContainer(); owner != null; owner = owner.getContainer()) {
            if (owner instanceof AdsFormHandlerClassDef) {
                return ((AdsFormHandlerClassDef) owner).getPresentations();
            }
        }
        return null;
    }

    @Override
    public EDefType getDefinitionType() {
        return EDefType.EDITOR_PAGE;
    }

    @Override
    public void visitChildren(final IVisitor visitor, final VisitorProvider provider) {
        super.visitChildren(visitor, provider);
        if (customViewSuppoort.isUseCustomView(ERuntimeEnvironmentType.EXPLORER)) {
            customViewSuppoort.getCustomView(ERuntimeEnvironmentType.EXPLORER).visit(visitor, provider);
        }
        if (customViewSuppoort.isUseCustomView(ERuntimeEnvironmentType.WEB)) {
            customViewSuppoort.getCustomView(ERuntimeEnvironmentType.WEB).visit(visitor, provider);
        }
        properties.visit(visitor, provider);
    }

    public EditorPages getOwnerEditorPages() {
        return getContainer() == null ? null : (EditorPages) getContainer().getContainer();
    }

    private static class EditorPageHierarchy extends Hierarchy<AdsEditorPageDef> {

        public EditorPageHierarchy(AdsEditorPageDef object) {
            super(object);
        }

        @Override
        public SearchResult<AdsEditorPageDef> findOverridden() {
            List<AdsEditorPageDef> ovrs = new LinkedList<>();
            collectOverriden(ovrs);
            if (ovrs.isEmpty()) {
                return SearchResult.empty();
            } else {
                return SearchResult.list(ovrs);
            }
        }

        @Override
        public SearchResult<AdsEditorPageDef> findOverwritten() {
            List<AdsEditorPageDef> ovrs = new LinkedList<>();
            collectOverwritten(ovrs);
            if (ovrs.isEmpty()) {
                return SearchResult.empty();
            } else {
                return SearchResult.list(ovrs);
            }
        }

        private void collectOverwritten(List<AdsEditorPageDef> collection) {
            EditorPages pages = object.getOwnerEditorPages();
            if (pages == null) {
                return;
            }
            HierarchyIterator<ExtendableDefinitions<AdsEditorPageDef>> iter = pages.getHierarchyIterator(HierarchyIterator.Mode.FIND_ALL);

            if (!iter.hasNext()) {
                return;
            }
            ExtendableDefinitions<AdsEditorPageDef> next = iter.next().first();

            assert next == pages;

            Id ownerId = pages.getOwnerDefinition().getId();
            while (iter.hasNext()) {
                next = iter.next().first();
                if (next.getOwnerDefinition().getId() != ownerId) {//skip override
                    continue;
                }
                AdsEditorPageDef page = next.getLocal().findById(object.getId());
                if (page != null) {
                    if (!collection.contains(page)) {
                        collection.add(page);
                    }
                }
            }
        }

        private void collectOverriden(List<AdsEditorPageDef> collection) {
            EditorPages pages = object.getOwnerEditorPages();
            if (pages == null) {
                return;
            }
            HierarchyIterator<ExtendableDefinitions<AdsEditorPageDef>> iter = pages.getHierarchyIterator(HierarchyIterator.Mode.FIND_ALL);

            if (!iter.hasNext()) {
                return;
            }
            ExtendableDefinitions<AdsEditorPageDef> next = iter.next().first();

            assert next == pages;
            Id ownerId = pages.getOwnerDefinition().getId();
            while (iter.hasNext()) {
                next = iter.next().first();
                if (next.getOwnerDefinition().getId() == ownerId) {//skip overwrite
                    continue;
                }
                AdsEditorPageDef page = next.getLocal().findById(object.getId());
                if (page != null) {

                    collection.add(page);

                }
            }
        }
    }

    @Override
    public Hierarchy<AdsEditorPageDef> getHierarchy() {
        return new EditorPageHierarchy(this);
    }

    @Override
    public boolean setName(String name) {
        if (super.setName(name)) {
            if (isInBranch()) {
                EditorPagesOperationSupport.getDefault().fireChange();
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void setTitleId(Id id) {
        assert !isTitleInherited() : "Can't set title id. Title is inherited.";
        if (isTitleInherited()) {
            return;
        }
        super.setTitleId(id);
    }

    @Override
    public Id getTitleId() {
        if (isTitleInherited()) {
            AdsExplorerItemDef reference = findEmbeddedExplorerItem();
            if (reference != null) {
                return getTitleIdFromEmbeddedExplorerItem((AdsExplorerItemDef) reference);
            }
            return null;
        }
        return super.getTitleId();
    }

    @Override
    public boolean isTitleInherited() {
        final AdsExplorerItemDef reference = findEmbeddedExplorerItem();

        if (reference != null) {
            final Id refTitleId = getTitleIdFromEmbeddedExplorerItem((AdsExplorerItemDef) reference);
            return titleId == null || Objects.equals(titleId, refTitleId);
        }
        return false;
    }

    @Override
    public boolean setTitleInherited(boolean inherit) {

        if (inherit == isTitleInherited()) {
            return false;
        }

        if (inherit) {
            titleId = null;
        } else {
            AdsMultilingualStringDef string = AdsMultilingualStringDef.Factory.newInstance();
            findLocalizingBundle().getStrings().getLocal().add(string);
            titleId = string.getId();
        }

        setEditState(EEditState.MODIFIED);
        return true;
    }

    @Override
    public String getTitle(EIsoLanguage language) {
        if (isTitleInherited()) {
            final Definition ownerTitleDefinition = findOwnerTitleDefinition();
            if (ownerTitleDefinition != null) {
                return ownerTitleDefinition.getLocalizedStringValue(language, getTitleId());
            }
            return null;
        }
        return super.getTitle(language);
    }

    @Override
    public boolean setTitle(EIsoLanguage language, String title) {
        assert !isTitleInherited() : "Title is inherited. Modification not allowed.";
        if (isTitleInherited()) {
            return false;
        }
        return super.setTitle(language, title);
    }

    @Override
    public Definition findOwnerTitleDefinition() {
        if (isTitleInherited()) {
            return AdsUtils.findTitleOwnerDefinition(findEmbeddedExplorerItem());
        }
        return this;
    }

    public boolean isIconInherited() {
        AdsExplorerItemDef reference = findEmbeddedExplorerItem();
        Id ownIconId = getIconId();
        Id refIconId = null;
        if (reference != null) {
            refIconId = getIconIdFromEmbeddedExplorerItem((AdsExplorerItemDef) reference);
        }
        return reference != null && (ownIconId == null || (refIconId != null && ownIconId.equals(refIconId)));
    }

    private Id getTitleIdFromEmbeddedExplorerItem(AdsExplorerItemDef reference) {
        if (reference instanceof AdsSelectorExplorerItemDef) {
            AdsSelectorExplorerItemDef asSelectorItem = (AdsSelectorExplorerItemDef) reference;
            if (asSelectorItem.isTitleInherited()) {
                AdsSelectorPresentationDef refref = asSelectorItem.findReferencedSelectorPresentation().get();
                if (refref != null) {
                    return refref.getTitleId();
                }
            }
        }
        return reference.getTitleId();
    }

    private Id getIconIdFromEmbeddedExplorerItem(AdsExplorerItemDef reference) {
        if (reference instanceof AdsSelectorExplorerItemDef) {
            AdsSelectorExplorerItemDef asSelectorItem = (AdsSelectorExplorerItemDef) reference;
            if (asSelectorItem.isIconInherited()) {
                AdsSelectorPresentationDef refref = asSelectorItem.findReferencedSelectorPresentation().get();
                if (refref != null) {
                    return refref.getIconId();
                }
            }
        }
        return null;
    }

    public boolean containsProperty(Id id) {
        if (type == EEditorPageType.CONTAINER) {
            return false;
        } else {
            return properties.contains(id);
        }
    }

    @Override
    public boolean canBeFinal() {
        return false;
    }

    @Override
    public boolean canChangeFinality() {
        return false;
    }

    @Override
    public boolean isFinal() {
        return false;
    }

    @Override
    public RadixProblem.WarningSuppressionSupport getWarningSuppressionSupport(boolean createIfAbsent) {
        synchronized (this) {
            if (warningsSupport == null && createIfAbsent) {
                warningsSupport = new Problems(this, null);
            }
            return warningsSupport;
        }
    }

    @Override
    public RadixProblem.ProblemFixSupport getProblemFixSupport() {
        synchronized (this) {
            if (warningsSupport == null) {
                return new Problems(this, null);
            } else {
                return warningsSupport;
            }
        }
    }

    @Override
    protected void appendAdditionalToolTip(StringBuilder sb) {
        super.appendAdditionalToolTip(sb);
        if (getTitleId() != null) {
            AdsLocalizingBundleDef bundle = findExistingLocalizingBundle();
            if (bundle != null) {
                for (EIsoLanguage lan : bundle.getLanguages()) {
                    sb.append("<br>Title:");
                    if (getTitle(lan) != null) {
                        sb.append("<br>&nbsp;(").append(lan.toString()).append("): ").append(getTitle(lan));
                    }
                }
            }
        }
    }

    @Override
    public boolean isRadixdocProvider() {
        return true;
    }

    @Override
    public RadixdocSupport<? extends RadixObject> getRadixdocSupport() {
        return new RadixdocSupport<AdsEditorPageDef>(this) {
            @Override
            public IRadixdocPage document(Page page, DocumentOptions options) {
                return new EditorPageRadixdoc(getSource(), page, options);
            }
        };
    }
}
