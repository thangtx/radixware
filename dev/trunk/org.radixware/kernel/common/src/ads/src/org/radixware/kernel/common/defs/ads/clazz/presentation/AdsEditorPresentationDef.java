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
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.check.RadixProblem.WarningSuppressionSupport;
import org.radixware.kernel.common.defs.*;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.ads.*;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.AdsModelClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityModelClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.EditorPages.IEditorPagesContainer;
import org.radixware.kernel.common.defs.ads.common.ICustomViewable;
import org.radixware.kernel.common.defs.ads.common.ICustomViewable.CustomViewSupport;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsExplorerItemDef;
import org.radixware.kernel.common.defs.ads.explorerItems.ExplorerItems;
import org.radixware.kernel.common.defs.ads.radixdoc.EditorPresentationRadixdoc;
import org.radixware.kernel.common.defs.ads.src.IJavaSource;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.clazz.presentation.AdsEditorPresentationWriter;
import org.radixware.kernel.common.defs.ads.ui.AdsAbstractUIDef;
import org.radixware.kernel.common.defs.ads.ui.AdsCustomEditorDef;
import org.radixware.kernel.common.defs.ads.ui.rwt.AdsRwtCustomEditorDef;
import org.radixware.kernel.common.enums.*;
import static org.radixware.kernel.common.enums.EEditorPresentationRightsInheritanceMode.FROM_DEFINED;
import static org.radixware.kernel.common.enums.EEditorPresentationRightsInheritanceMode.FROM_REPLACED;
import static org.radixware.kernel.common.enums.EEditorPresentationRightsInheritanceMode.NONE;
import org.radixware.kernel.common.radixdoc.DocumentOptions;
import org.radixware.kernel.common.radixdoc.IRadixdocPage;
import org.radixware.kernel.common.radixdoc.IRadixdocProvider;
import org.radixware.kernel.common.radixdoc.RadixdocSupport;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.RadixObjectsUtils;
import org.radixware.kernel.common.utils.events.RadixEvent;
import org.radixware.kernel.common.utils.events.RadixEventSource;
import org.radixware.schemas.adsdef.AbstractDialogDefinition;
import org.radixware.schemas.adsdef.EditorPresentationDefinition;
import org.radixware.schemas.adsdef.EditorPresentationDefinition.RestrictedProperties.PropertyAttributes;
import org.radixware.schemas.radixdoc.Page;

public class AdsEditorPresentationDef extends AdsPresentationDef implements IJavaSource, ICustomViewable<AdsEditorPresentationDef, AdsAbstractUIDef>, IEditorPagesContainer, IClientDefinition, IRadixdocProvider {

    public static final class Problems extends AdsDefinitionProblems {

        public static final int COLUMN_MULTI_EDIT_BY_REF = 4000;
        public static final int COLUMN_MULTI_EDIT_BY_REF_AND_PROP = 4001;
        public static final int CUSTOM_EDIT_PROPERTY_WITHOUT_EDITOR = 4002;

        private Problems(AdsEditorPresentationDef prop, List<Integer> warnings) {
            super(prop);
            if (warnings != null) {
                int arr[] = new int[warnings.size()];
                for (int i = 0; i < arr.length; i++) {
                    arr[i] = warnings.get(i);
                }
                setSuppressedWarnings(arr);
            }
        }

        @Override
        public boolean canSuppressWarning(int code) {
            switch (code) {
                case COLUMN_MULTI_EDIT_BY_REF:
                case COLUMN_MULTI_EDIT_BY_REF_AND_PROP:
                case CUSTOM_EDIT_PROPERTY_WITHOUT_EDITOR:
                    return true;
                default:
                    return super.canSuppressWarning(code);
            }
        }
    }

    public static final class Factory {

        public static AdsEditorPresentationDef newEditorPresentationForUser2Role() {
            return new AdsEditorPresentationDef(Id.Factory.loadFrom("epr3HIYWUVLLJCLDPWFCQPRHLHLWY"));
        }

        public static AdsEditorPresentationDef newEditorPresentationForUserGroup2Role() {
            return new AdsEditorPresentationDef(Id.Factory.loadFrom("eprEQ7UIJIUWJAFVAKCR6ELGWNPWY"));
        }

        public static AdsEditorPresentationDef loadFrom(EditorPresentationDefinition xEpr) {
            return new AdsEditorPresentationDef(xEpr);
        }

        public static AdsEditorPresentationDef newInstance() {
            return new AdsEditorPresentationDef();
        }
    }

    public interface IPropertyPresentationAttributesView {

        EEditPossibility getEditPossibility();

        Boolean getMandatory();

        Boolean getPresentable();

        EPropertyVisibility getVisibility();

        Id getTitleId();
    }

    public static final class PropertyPresentationAttributesCollection extends RadixObjects<AdsPropertyPresentationAttributes> {

        private abstract class PresentationWalker {

            void walk(final EScope scope) {
                final HierarchyIterator<AdsPresentationDef> iterator = getOwnEditorPresentation().getHierarchyIterator(scope, HierarchyIterator.Mode.FIND_ALL);
                while (iterator.hasNext()) {
                    if (!continueWalk((AdsEditorPresentationDef) iterator.next().first())) {
                        break;
                    }
                }
            }

            boolean continueWalk(AdsEditorPresentationDef presentation) {
                process(presentation);
                return presentation.getInheritanceMask().contains(EPresentationAttrInheritance.PROPERTY_PRESENTATION_ATTRIBUTES);
            }

            abstract void process(AdsEditorPresentationDef presentation);
        }

        private PropertyPresentationAttributesCollection(AdsEditorPresentationDef container) {
            setContainer(container);
        }

        public AdsPropertyPresentationAttributes findById(Id id) {
            for (final AdsPropertyPresentationAttributes r : this) {
                if (Objects.equals(r.getPropertyId(), id)) {
                    return r;
                }
            }
            return null;
        }

        public PropertyAttributesSet findById(final Id id, final EScope scope) {

            if (scope == EScope.LOCAL) {
                final AdsPropertyPresentationAttributes restriction = findById(id);
                if (restriction != null) {
                    return new PropertyAttributesSet(getOwnEditorPresentation(), scope, restriction);
                }
                return null;
            }

            final PropertyAttributesSet resultRestriction = new PropertyAttributesSet(getOwnEditorPresentation(), scope, id);

            new PresentationWalker() {
                @Override
                void process(AdsEditorPresentationDef presentation) {
                    final AdsPropertyPresentationAttributes restriction = presentation.getPropertyPresentationAttributesCollection().findById(id);
                    if (restriction != null) {
                        resultRestriction.add(restriction);
                    }
                }
            }.walk(scope);

            if (!resultRestriction.hasNoElements() && scope == EScope.ALL) {
                final SearchResult<AdsPropertyDef> prop = getOwnEditorPresentation().getOwnerClass().getProperties().findById(id, EScope.ALL);
                if (!prop.isEmpty()) {
                    if (prop.get() instanceof IAdsPresentableProperty) {
                        final IAdsPresentableProperty presentableProp = (IAdsPresentableProperty) prop.get();
                        final PropertyPresentation presentation = presentableProp.getPresentationSupport().getPresentation();
                        final PropertyEditOptions editOptions = presentation.getEditOptions();

                        resultRestriction.add(new AdsPropertyPresentationAttributes(
                                id, presentation.isPresentable(),
                                editOptions.getEditPossibility(),
                                EPropertyVisibility.ALWAYS,
                                editOptions.isNotNull(),
                                presentation.getTitleId()));
                    }
                }
            }

            resultRestriction.local = findById(id);
            return resultRestriction.hasNoElements() ? null : resultRestriction;
        }

        public List<PropertyAttributesSet> getAll(final EScope scope) {
            final List<PropertyAttributesSet> result = new ArrayList<>();


            if (scope == EScope.LOCAL) {
                for (final AdsPropertyPresentationAttributes restriction : this) {
                    result.add(new PropertyAttributesSet(getOwnEditorPresentation(), scope, restriction));
                }
                return result;
            }

            final Set<Id> used = new HashSet<>();
            new PresentationWalker() {
                @Override
                void process(AdsEditorPresentationDef presentation) {

                    for (final AdsPropertyPresentationAttributes restriction : presentation.getPropertyPresentationAttributesCollection()) {
                        if (!used.contains(restriction.getPropertyId())) {
                            final PropertyAttributesSet restrictionResult = getOwnEditorPresentation().getPropertyPresentationAttributesCollection().findById(restriction.getPropertyId(), scope);
                            if (restrictionResult != null) {
                                result.add(restrictionResult);
                                used.add(restriction.getPropertyId());
                            }
                        }
                    }
                }
            }.walk(scope);

            return result;
        }

        public Set<Id> getPropertyIds(final EScope scope) {
            final Set<Id> result = new LinkedHashSet<>();

            if (scope == EScope.LOCAL) {
                for (final AdsPropertyPresentationAttributes restriction : this) {
                    result.add(restriction.getPropertyId());
                }
                return result;
            }

            new PresentationWalker() {
                @Override
                void process(AdsEditorPresentationDef presentation) {
                    for (final AdsPropertyPresentationAttributes restriction : presentation.getPropertyPresentationAttributesCollection()) {
                        result.add(restriction.getPropertyId());
                    }
                }
            }.walk(scope);

            return result;
        }

        public void remove(Id id) {
            final AdsPropertyPresentationAttributes restriction = findById(id);
            if (restriction != null) {
                remove(restriction);
            }
        }

        private AdsEditorPresentationDef getOwnEditorPresentation() {
            return (AdsEditorPresentationDef) getContainer();
        }

        @Override
        public void collectDependences(List<Definition> list) {
            super.collectDependences(list);

            for (final AdsPropertyPresentationAttributes restriction : this) {
                final AdsPropertyDef prop = restriction.findProperty();
                if (prop != null) {
                    list.add(prop);
                }
            }
        }
    }

    public static final class PropertyAttributesSet implements IPropertyPresentationAttributesView {

        private List<AdsPropertyPresentationAttributes> restrictions = new ArrayList<>();
        private AdsPropertyPresentationAttributes local = null;
        private final EScope scope;
        private final Id propId;
        private final AdsEditorPresentationDef owner;

        private PropertyAttributesSet(AdsEditorPresentationDef owner, EScope scope, AdsPropertyPresentationAttributes local) {
            this(owner, scope, local.getPropertyId());

            add(local);
            this.local = local;
        }

        private PropertyAttributesSet(AdsEditorPresentationDef owner, EScope scope, Id propId) {
            this.scope = scope;
            this.propId = propId;
            this.owner = owner;
        }

        private void add(AdsPropertyPresentationAttributes restriction) {
            assert restriction != null;
            restrictions.add(restriction);
        }

        public boolean hasLocal() {
            return local != null;
        }

        public AdsPropertyPresentationAttributes getLocal() {
            return local;
        }

        public AdsPropertyPresentationAttributes getOrCreateLocal() {
            if (local == null) {
                local = new AdsPropertyPresentationAttributes(propId);

                owner.getPropertyPresentationAttributesCollection().add(local);
                restrictions.add(0, local);
            }

            return local;
        }

        @Override
        public EEditPossibility getEditPossibility() {
            for (AdsPropertyPresentationAttributes r : restrictions) {
                if (r.getEditPossibility() != null) {
                    return r.getEditPossibility();
                }
            }
            return null;
        }

        @Override
        public Boolean getMandatory() {
            for (AdsPropertyPresentationAttributes r : restrictions) {
                if (r.getMandatory() != null) {
                    return r.getMandatory();
                }
            }
            return null;
        }

        @Override
        public Boolean getPresentable() {
            for (AdsPropertyPresentationAttributes r : restrictions) {
                if (r.getPresentable() != null) {
                    return r.getPresentable();
                }
            }
            return null;
        }

        @Override
        public EPropertyVisibility getVisibility() {
            for (AdsPropertyPresentationAttributes r : restrictions) {
                if (r.getVisibility() != null) {
                    return r.getVisibility();
                }
            }
            return null;
        }

        @Override
        public Id getTitleId() {
            for (AdsPropertyPresentationAttributes r : restrictions) {
                if (r.getTitleId() != null) {
                    return r.getTitleId();
                }
            }
            return null;
        }

        public EScope getScope() {
            return scope;
        }

        public Id getPropertyId() {
            return propId;
        }

        private boolean hasNoElements() {
            return restrictions.isEmpty();
        }

        public AdsDefinition findTitleOwnerDefinition() {
            for (final AdsPropertyPresentationAttributes restriction : restrictions) {
                if (restriction.getTitleId() != null) {
                    AdsDefinition ownDef = RadixObjectsUtils.findContainer(restriction, AdsDefinition.class);
                    if (ownDef == null) {
                        ownDef = restrictions.get(0).findProperty();
                    }
                    if (ownDef != null) {
                        return ownDef.findExistingLocalizingBundle();
                    }
                }
            }
            return null;
        }

        public boolean isEmpty() {
            return getPresentable() == null
                    && getEditPossibility() == null
                    && getVisibility() == null
                    && getMandatory() == null
                    && getTitleId() == null;
        }

        public List<AdsPropertyPresentationAttributes> getRestrictions() {
            return Collections.unmodifiableList(restrictions);
        }

        public AdsEditorPresentationDef getEditorPresentation() {
            return owner;
        }
    }
//    public class ExplorerItemsOrder {
//
//        public class ExplorerItemRef {
//
//            private final Id id;
//            private final DefinitionLink<AdsExplorerItemDef> link = new DefinitionLink<AdsExplorerItemDef>() {
//                @Override
//                protected AdsExplorerItemDef search() {
//                    return explorerItems.getChildren().findById(id, EScope.ALL).get();
//                }
//            };
//
//            public ExplorerItemRef(Id id) {
//                this.id = id;
//            }
//
//            public Id getExplorerItemId() {
//                return id;
//            }
//
//            public AdsExplorerItemDef findExplorerItem() {
//                return link.find();
//            }
//        }
//
//        private List<Id> visibleItems = null;
//        private boolean isInUse = false;
//
//        public List<ExplorerItemRef> getUsedExplorerItems() {
//            List<Id> ids = getUsedIds();
//            List<ExplorerItemRef> refs = new LinkedList<>();
//            for (Id id : ids) {
//                refs.add(new ExplorerItemRef(id));
//            }
//            return refs;
//        }
//
//        public List<ExplorerItemRef> getUnusedExplorerItems() {
//            List<Id> ids = getUnusedIds();
//            List<ExplorerItemRef> refs = new LinkedList<>();
//            for (Id id : ids) {
//                refs.add(new ExplorerItemRef(id));
//            }
//            return refs;
//        }
//
//        public void setUsed(boolean use) {
//
//            if (use != isInUse) {
//                isInUse = use;
//                if (use) {
//                    if (visibleItems == null) {
//                        visibleItems = getAllIds();
//                    }
//                }
//                setEditState(EEditState.MODIFIED);
//            }
//        }
//
//        public boolean isUsed() {
//            return isInUse;
//        }
//
//        public void moveUp(Id id) {
//            move(id, -1);
//        }
//
//        public void moveDn(Id id) {
//            move(id, 1);
//        }
//
//        private void move(Id id, int dir) {
//            List<Id> virtualList = visibleItems;
//            if (visibleItems == null) {
//                return;
//            }
//
//            int index = virtualList.indexOf(id);
//            if (index < 0) {
//                return;
//            }
//            int next = index + dir;
//            if (next >= 0 && next < virtualList.size()) {
//                Id nextId = virtualList.get(next);
//                virtualList.set(next, id);
//                virtualList.set(index, nextId);
//            }
//
//            this.visibleItems = virtualList;
//            setEditState(EEditState.MODIFIED);
//        }
//
//        public List<Id> getAllIds() {
//            List<Id> virtualList = new LinkedList<>();
//            for (AdsExplorerItemDef item : getExplorerItems().getChildren().get(EScope.ALL)) {
//                Id itemId = item.getId();
//                virtualList.add(itemId);
//            }
//            return virtualList;
//        }
//
//        public List<Id> getUnusedIds() {
//            List<Id> ids = getAllIds();
//            List<Id> used = getUsedIds();
//            for (int i = 0; i < ids.size();) {
//                Id id = ids.get(i);
//                if (used.contains(id)) {
//                    ids.remove(i);
//                } else {
//                    i++;
//                }
//            }
//            return ids;
//        }
//
//        public List<Id> getUsedIds() {
//            if (visibleItems == null) {
//                return Collections.emptyList();
//            } else {
//                return new ArrayList<>(visibleItems);
//            }
//        }
//
//        public void remove(Id id) {
//            if (visibleItems == null) {
//                return;
//            }
//            if (visibleItems.contains(id)) {
//                visibleItems.remove(id);
//                setEditState(EEditState.MODIFIED);
//            }
//        }
//
//        public void add(Id id) {
//            if (visibleItems == null || visibleItems.contains(id)) {
//                return;
//            }
//            visibleItems.add(id);
//            setEditState(EEditState.MODIFIED);
//        }
//
//        private void loadFrom(EditorPresentationDefinition xDef) {
//            EditorPresentationDefinition.ChildExplorerItemsOrder xOrder = xDef.getChildExplorerItemsOrder();
//            if (xOrder != null) {
//                if (xOrder.isSetUse()) {
//                    isInUse = xOrder.getUse();
//                }
//                if (xOrder.isSetIds()) {
//                    this.visibleItems = new LinkedList<>();
//                    this.visibleItems.addAll(xOrder.getIds());
//                }
//            } else {
//                this.visibleItems = null;
//            }
//        }
//
//        private void appendTo(EditorPresentationDefinition xDef) {
//            if (isInUse || this.visibleItems != null) {
//                EditorPresentationDefinition.ChildExplorerItemsOrder xOrder = xDef.addNewChildExplorerItemsOrder();
//                if (isInUse) {
//                    xOrder.setUse(isInUse);
//                }
//                if (this.visibleItems != null) {
//                    xOrder.setIds(visibleItems);
//                }
//            }
//        }
//    }
    private Id replacedPresentationId;
    private Id rightsSrcId;
    private EEditorPresentationRightsInheritanceMode rightsInheritanceMode = EEditorPresentationRightsInheritanceMode.NONE;
    private AdsObjectTitleFormatDef objectTitleFormat;
    private final EditorPages editorPages;
    private final ExplorerItems explorerItems;
//    private final ExplorerItemsOrder explorerItemsOrder;
//    private ForbiddenProps forbiddenProps = null;
    private Problems warningsSupport = null;
    private ERuntimeEnvironmentType clientEnv;
    private final ICustomViewable.CustomViewSupport<AdsEditorPresentationDef, AdsAbstractUIDef> customViewSuppoort = new CustomViewSupport<AdsEditorPresentationDef, AdsAbstractUIDef>(this) {
        @Override
        protected AdsAbstractUIDef createOrLoadCustomView(AdsEditorPresentationDef context, ERuntimeEnvironmentType env, AbstractDialogDefinition xDef) {
            if (env == ERuntimeEnvironmentType.EXPLORER) {
                if (xDef != null) {
                    return AdsCustomEditorDef.Factory.loadFrom(context, xDef);
                } else {
                    return AdsCustomEditorDef.Factory.newInstance(context);
                }
            } else if (env == ERuntimeEnvironmentType.WEB) {
                if (xDef != null) {
                    return AdsRwtCustomEditorDef.Factory.loadFrom(context, xDef);
                } else {
                    return AdsRwtCustomEditorDef.Factory.newInstance(context);
                }
            } else {
                throw new UnsupportedOperationException("Not supported yet");
            }
        }
    };
    private PropertyPresentationAttributesCollection propertyAttributesCollection;

    protected AdsEditorPresentationDef(EditorPresentationDefinition xDef) {
        super(xDef);
        this.editorPages = EditorPages.Factory.loadFrom(this, xDef.getEditorPages());
        this.objectTitleFormat = AdsObjectTitleFormatDef.Factory.loadFrom(this, xDef.getObjectTitleFormat());
        this.replacedPresentationId = xDef.getReplacedPresentationId();
        this.explorerItems = ExplorerItems.Factory.loadFrom(this, xDef.getChildExplorerItems());

        //  this.explorerItemsOrder = new ExplorerItemsOrder();
        //   this.explorerItemsOrder.loadFrom(xDef);

        if (xDef.getView() != null) {
            this.customViewSuppoort.loadCustomView(ERuntimeEnvironmentType.EXPLORER, xDef.getView());
        }
        if (xDef.getWebView() != null) {
            this.customViewSuppoort.loadCustomView(ERuntimeEnvironmentType.WEB, xDef.getWebView());
        }

        this.propertyAttributesCollection = new PropertyPresentationAttributesCollection(this);
        if (!inheritanceMask.contains(EPresentationAttrInheritance.PROPERTY_PRESENTATION_ATTRIBUTES)) {
            List<Id> ids = xDef.getForbiddenPropIds();
            if (ids != null && !ids.isEmpty()) {
                for (Id id : ids) {
                    getPropertyPresentationAttributesCollection().add(new AdsPropertyPresentationAttributes(id, false, null, null, null, null));
                }
            }
        }

        if (xDef.isSetRestrictedProperties()) {
            for (final PropertyAttributes attr : xDef.getRestrictedProperties().getPropertyAttributesList()) {
                final AdsPropertyPresentationAttributes restriction = new AdsPropertyPresentationAttributes(
                        attr.getPropertyId(),
                        attr.isSetPresentable() ? attr.getPresentable() : null,
                        attr.isSetEditPossibility() ? attr.getEditPossibility() : null,
                        attr.isSetVisibility() ? attr.getVisibility() : null,
                        attr.isSetMandatory() ? attr.getMandatory() : null,
                        attr.isSetTitleId() ? attr.getTitleId() : null);

                if (!restriction.isEmpty()) {
                    getPropertyPresentationAttributesCollection().add(restriction);
                } else {
                    Logger.getLogger(PropertyPresentationAttributesCollection.class.getName()).log(Level.INFO, "Attempt add to empty PropertyRestriction");
                }
            }
        }

        if (xDef.isSetRightsSourceId()) {
            this.rightsSrcId = xDef.getRightsSourceId();
        }
        if (xDef.isSetRightsInheritanceMode()) {
            this.rightsInheritanceMode = xDef.getRightsInheritanceMode();
        } else {
            if (this.replacedPresentationId == null) {
                this.rightsInheritanceMode = EEditorPresentationRightsInheritanceMode.NONE;
            } else {
                this.rightsInheritanceMode = EEditorPresentationRightsInheritanceMode.FROM_REPLACED;
            }
        }

        if (xDef.isSetSuppressedWarnings()) {
            @SuppressWarnings("unchecked")
            List<Integer> list = xDef.getSuppressedWarnings();
            if (!list.isEmpty()) {
                this.warningsSupport = new Problems(this, list);
            }
        }
        if (xDef.isSetClientEnvironment()) {
            this.clientEnv = xDef.getClientEnvironment();
        }
    }

    protected AdsEditorPresentationDef() {
        this(Id.Factory.newInstance(EDefinitionIdPrefix.EDITOR_PRESENTATION));
    }

    protected AdsEditorPresentationDef(Id id) {
        super(id, "NewEditorPresentation");
        this.editorPages = EditorPages.Factory.newInstance(this);
        this.objectTitleFormat = AdsObjectTitleFormatDef.Factory.newInstance(this);
        this.replacedPresentationId = null;
        this.explorerItems = ExplorerItems.Factory.newInstance(this);
        //   this.explorerItemsOrder = new ExplorerItemsOrder();
        this.inheritanceMask = EnumSet.of(EPresentationAttrInheritance.TITLE, EPresentationAttrInheritance.ICON, EPresentationAttrInheritance.OBJECT_TITLE_FORMAT, EPresentationAttrInheritance.PROPERTY_PRESENTATION_ATTRIBUTES);
        this.propertyAttributesCollection = new PropertyPresentationAttributesCollection(this);
    }

    protected AdsEditorPresentationDef(AdsEditorPresentationDef source) {
        super(source);
        this.editorPages = EditorPages.Factory.newInstance(this);
        this.objectTitleFormat = AdsObjectTitleFormatDef.Factory.newInstance(this);
        this.replacedPresentationId = source.replacedPresentationId;
        this.explorerItems = ExplorerItems.Factory.newInstance(this);
        //     this.explorerItemsOrder = new ExplorerItemsOrder();
        this.propertyAttributesCollection = new PropertyPresentationAttributesCollection(this);
    }

    @Override
    public CustomViewSupport<AdsEditorPresentationDef, AdsAbstractUIDef> getCustomViewSupport() {
        return findAttributeOwner(EPresentationAttrInheritance.CUSTOM_DIALOG).get().customViewSuppoort;
    }

    @Override
    public JavaSourceSupport getJavaSourceSupport() {
        return new JavaSourceSupport(this) {
            @Override
            public JavaSourceSupport.CodeWriter getCodeWriter(JavaSourceSupport.UsagePurpose purpose) {
                return new AdsEditorPresentationWriter(this, AdsEditorPresentationDef.this, purpose);
            }

            @Override
            public Set<JavaSourceSupport.CodeType> getSeparateFileTypes(ERuntimeEnvironmentType sc) {

                ERuntimeEnvironmentType ownEnv = getEffectiveClientEnvironment();
                if (ownEnv == ERuntimeEnvironmentType.COMMON_CLIENT) {
                    return sc == ERuntimeEnvironmentType.EXPLORER || sc == ERuntimeEnvironmentType.WEB ? EnumSet.of(JavaSourceSupport.CodeType.META) : null;
                } else {
                    if (ownEnv == sc) {
                        return EnumSet.of(JavaSourceSupport.CodeType.META);
                    } else {
                        return null;
                    }
                }
            }
        };
    }

    public final PropertyPresentationAttributesCollection getPropertyPresentationAttributesCollection() {
        return propertyAttributesCollection;
    }

    public ERuntimeEnvironmentType getEffectiveClientEnvironment() {
        AdsModelClassDef model = AdsEditorPresentationDef.this.findFinalModel();
        ERuntimeEnvironmentType env = model == null ? AdsEditorPresentationDef.this.getClientEnvironment() : model.getClientEnvironment();
        if (AdsEditorPresentationDef.this.isUseDefaultModel()) {
            AdsEntityObjectClassDef clazz = AdsEditorPresentationDef.this.getOwnerClass();
            if (clazz.getClientEnvironment() == ERuntimeEnvironmentType.COMMON_CLIENT) {
                return env;
            } else {
                return AdsEditorPresentationDef.this.getClientEnvironment();
            }
        } else {
            return env;
        }


    }

    private ERuntimeEnvironmentType getOwnClientEnvironment() {
        return clientEnv == null ? ERuntimeEnvironmentType.COMMON_CLIENT : clientEnv;
    }

//    public ExplorerItemsOrder getExplorerItemsOrder() {
//        if (isExplorerItemsOrderInherited()) {
//            AdsEditorPresentationDef finalOwner = findAttributeOwner(EPresentationAttrInheritance.CHILDREN_ORDER).get();
//            if (finalOwner == null || finalOwner == this) {
//                return explorerItemsOrder;
//            } else {
//                return finalOwner.explorerItemsOrder;
//            }
//        } else {
//            return explorerItemsOrder;
//        }
//    }
    public boolean isExplorerItemsOrderInherited() {
        synchronized (this) {
            return this.inheritanceMask.contains(EPresentationAttrInheritance.CHILDREN_ORDER);
        }
    }

    public void setExplorerItemsOrderInherited(boolean inherit) {
        if (isExplorerItemsOrderInherited() != inherit) {
            if (inherit) {
                if (isExplorerItemsOrderMayBeInherited()) {
                    synchronized (this) {
                        this.inheritanceMask.add(EPresentationAttrInheritance.CHILDREN_ORDER);
                    }
                }
            } else {
                this.inheritanceMask.remove(EPresentationAttrInheritance.CHILDREN_ORDER);
            }
        }
    }

    public boolean isExplorerItemsOrderMayBeInherited() {
        if (isExplorerItemsOrderInherited()) {
            AdsEditorPresentationDef finalOwner = findAttributeOwner(EPresentationAttrInheritance.CHILDREN_ORDER).get();
            if (finalOwner == null || finalOwner == this) {
                return false;
            } else {
                return true;
            }
        } else {
            synchronized (this) {
                try {
                    this.inheritanceMask.add(EPresentationAttrInheritance.CHILDREN_ORDER);
                    return isExplorerItemsOrderMayBeInherited();
                } finally {
                    this.inheritanceMask.remove(EPresentationAttrInheritance.CHILDREN_ORDER);
                }
            }
        }
    }

    @Override
    public ERuntimeEnvironmentType getClientEnvironment() {
        AdsEntityObjectClassDef clazz = getOwnerClass();
        if (clazz != null) {
            ERuntimeEnvironmentType clazzEnv = clazz.getClientEnvironment();
            if (clazzEnv == ERuntimeEnvironmentType.COMMON_CLIENT) {
                return getOwnClientEnvironment();
            } else {
                return clazzEnv;
            }
        } else {
            return getOwnClientEnvironment();
        }
    }

    public boolean canChangeClientEnvironment() {
        AdsEntityObjectClassDef clazz = getOwnerClass();
        if (clazz != null) {
            ERuntimeEnvironmentType clazzEnv = clazz.getClientEnvironment();
            if (clazzEnv == ERuntimeEnvironmentType.COMMON_CLIENT) {
                return true;
            } else {
                return false;
            }
        }
        return true;
    }

    public void setClientEnvironment(ERuntimeEnvironmentType clientEnv) {
        if (clientEnv != null && clientEnv.isClientEnv() && clientEnv != this.clientEnv) {
            this.clientEnv = clientEnv;
            setEditState(EEditState.MODIFIED);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public SearchResult<AdsEditorPresentationDef> findAttributeOwner(EPresentationAttrInheritance attributeMask) {
        return (SearchResult<AdsEditorPresentationDef>) super.findAttributeOwner(attributeMask);
    }

    public Id getReplacedEditorPresentationId() {
        return replacedPresentationId;
    }

    public boolean setReplacedEditorPresentationId(Id id) {
        if (this.replacedPresentationId != id) {
            this.replacedPresentationId = id;
            setEditState(EEditState.MODIFIED);
            return true;
        } else {
            return false;
        }
    }

    public Id getRightsSourceEditorPresentationId() {
        if (isRightsInheritanceModeInherited()) {
            AdsEditorPresentationDef owner = findBaseEditorPresentation().get();//findAttributeOwner(EPresentationAttrInheritance.RIGHTS_INHERITANCE_MODE).get();
            if (owner == null || owner == this) {//attreibute owner not found
                return this.rightsSrcId;
            } else {
                return owner.rightsSrcId;
            }
        } else {
            return this.rightsSrcId;
        }
    }

    public boolean setRightsSourceEditorPresentationId(Id id) {
        if (isRightsInheritanceModeInherited()) {
            return false;
        } else {
            if (rightsSrcId != id) {
                rightsSrcId = id;
                setEditState(EEditState.MODIFIED);
                return true;
            } else {
                return false;
            }
        }
    }

    public EEditorPresentationRightsInheritanceMode getRightInheritanceMode() {
        if (isRightsInheritanceModeInherited()) {
            AdsEditorPresentationDef owner = findBaseEditorPresentation().get();//findAttributeOwner(EPresentationAttrInheritance.RIGHTS_INHERITANCE_MODE).get();
            if (owner == null || owner == this) {//attreibute owner not found
                return this.rightsInheritanceMode;
            } else {
                return owner.getRightInheritanceMode();
            }
        } else {
            return this.rightsInheritanceMode;
        }
    }

    public boolean setRightsInheritanceMode(EEditorPresentationRightsInheritanceMode inheritanceMode) {
        if (isRightsInheritanceModeInherited()) {
            return false;
        } else {
            this.rightsInheritanceMode = inheritanceMode;
            setEditState(EEditState.MODIFIED);
            return true;
        }
    }

    public SearchResult<AdsEditorPresentationDef> findRightsInheritanceDefinePresentation() {
        return findAttributeOwner(EPresentationAttrInheritance.RIGHTS_INHERITANCE_MODE);
    }

    public boolean isRightsInheritanceModeInherited() {
        return this.inheritanceMask.contains(EPresentationAttrInheritance.RIGHTS_INHERITANCE_MODE);
    }

    public boolean setRightsInheritanceModeInherited(boolean inherit) {
        if (inherit) {
            if (!isRightsInheritanceModeInherited()) {
                this.inheritanceMask.add(EPresentationAttrInheritance.RIGHTS_INHERITANCE_MODE);
                setEditState(EEditState.MODIFIED);
                return true;
            } else {
                return false;
            }
        } else {
            if (isRightsInheritanceModeInherited()) {
                this.inheritanceMask.remove(EPresentationAttrInheritance.RIGHTS_INHERITANCE_MODE);
                setEditState(EEditState.MODIFIED);
                return true;
            } else {
                return false;
            }
        }
    }

    private class RightsSrcRef extends DefinitionLink<AdsEditorPresentationDef> {

        @Override
        protected AdsEditorPresentationDef search() {
            EntityObjectPresentations eprs = getOwnerPresentations();
            if (eprs == null) {
                return null;
            }
            return eprs.getEditorPresentations().findById(getRightsSourceEditorPresentationId(), EScope.ALL).get();
        }
    }
    private final RightsSrcRef rightsSrcRef = new RightsSrcRef();

    public AdsEditorPresentationDef findRightSourceEditorPresentation() {
        if (this.rightsSrcId == null) {
            return null;
        } else {
            return rightsSrcRef.find();
        }
    }

    @Override
    public AdsEntityModelClassDef getModel() {
        return (AdsEntityModelClassDef) modelClass;
    }

    public boolean isExplorerItemsInherited() {
        return this.inheritanceMask.contains(EPresentationAttrInheritance.CHILDREN);
    }

    public boolean setExplorerItemsInherited(boolean inherit) {
        if (inherit) {
            if (!isExplorerItemsInherited()) {
                this.inheritanceMask.add(EPresentationAttrInheritance.CHILDREN);
                setEditState(EEditState.MODIFIED);
                fireExplorerItemInheritanceChange();
                return true;
            }
        } else {
            if (isExplorerItemsInherited()) {
                this.inheritanceMask.remove(EPresentationAttrInheritance.CHILDREN);
                setEditState(EEditState.MODIFIED);
                fireExplorerItemInheritanceChange();
                return true;
            }
        }
        return false;
    }
    private RadixEventSource eiSupport = null;

    public RadixEventSource getExplorerItemsInheritanceChangesSupport() {
        synchronized (this) {
            if (eiSupport == null) {
                this.eiSupport = new RadixEventSource();
            }
            return eiSupport;
        }
    }

    @SuppressWarnings("unchecked")
    private void fireExplorerItemInheritanceChange() {
        if (eiSupport != null) {
            eiSupport.fireEvent(new RadixEvent());
        }
        EntireChangesSupport.getInstance(AdsExplorerItemDef.class).fireChange(this.explorerItems);
    }

    public ExplorerItems getExplorerItems() {
        //if (isExplorerItemsInherited()) {
        //    return findAttributeOwner(EPresentationAttrInheritance.CHILDREN).explorerItems;
        //} else {
        return explorerItems;
        //}
    }
    private RadixEventSource epSupport = null;

    public RadixEventSource getEditorPagesInheritanceChangesSupport() {
        synchronized (this) {
            if (epSupport == null) {
                this.epSupport = new RadixEventSource();
            }
            return epSupport;
        }
    }

    @SuppressWarnings("unchecked")
    private void fireEditorPagesInheritanceChange() {
        if (epSupport != null) {
            epSupport.fireEvent(new RadixEvent());
        }
    }

    public boolean isEditorPagesInherited() {
        return inheritanceMask.contains(EPresentationAttrInheritance.PAGES);
    }

    public boolean setEditorPagesInherited(boolean inherit) {
        if (inherit) {
            if (!isEditorPagesInherited()) {
                this.inheritanceMask.add(EPresentationAttrInheritance.PAGES);
                fireEditorPagesInheritanceChange();
                setEditState(EEditState.MODIFIED);
                return true;
            }
        } else {
            if (isEditorPagesInherited()) {
                this.inheritanceMask.remove(EPresentationAttrInheritance.PAGES);
                fireEditorPagesInheritanceChange();
                setEditState(EEditState.MODIFIED);
                return true;
            }
        }
        return false;
    }

    /**
     * ap
     *
     * Returns editor pages (with insheritance support)
     */
    @Override
    public EditorPages getEditorPages() {
        return findAttributeOwner(EPresentationAttrInheritance.PAGES).get().editorPages;
    }

    /**
     * Looks for base editor presentation
     */
    public SearchResult<AdsEditorPresentationDef> findBaseEditorPresentation() {
        EntityObjectPresentations eprs = getOwnerPresentations();
        if (eprs != null) {
            return eprs.getEditorPresentations().findById(getBasePresentationId(), EScope.ALL);
        } else {
            return SearchResult.empty();
        }
    }

    public SearchResult<AdsEditorPresentationDef> findReplacedEditorPresentation() {
        if (replacedPresentationId == null) {
            return SearchResult.empty();
        } else {
            EntityObjectPresentations eprs = getOwnerPresentations();
            if (eprs == null) {
                return SearchResult.empty();
            }
            return eprs.getEditorPresentations().findById(replacedPresentationId, EScope.ALL);
        }
    }

    public boolean isObjectTitleFormatInherited() {
        return inheritanceMask.contains(EPresentationAttrInheritance.OBJECT_TITLE_FORMAT);
    }

    public boolean setObjectTitleFormatInherited(boolean inherit) {
        if (inherit) {
            if (!isObjectTitleFormatInherited()) {
                this.inheritanceMask.add(EPresentationAttrInheritance.OBJECT_TITLE_FORMAT);
                setEditState(EEditState.MODIFIED);
            }
        } else {
            if (isObjectTitleFormatInherited()) {
                AdsObjectTitleFormatDef ttf = getObjectTitleFormat();
                this.inheritanceMask.remove(EPresentationAttrInheritance.OBJECT_TITLE_FORMAT);
                if (ttf != null) {
                    this.objectTitleFormat = new AdsObjectTitleFormatDef(this, ttf);
                } else {
                    this.objectTitleFormat = new AdsObjectTitleFormatDef(this);
                }
                setEditState(EEditState.MODIFIED);
            }
        }
        return true;
    }

    /**
     * Returns object title format (with insheritance support)
     */
    public AdsObjectTitleFormatDef getObjectTitleFormat() {
        AdsEditorPresentationDef owner = findAttributeOwner(EPresentationAttrInheritance.OBJECT_TITLE_FORMAT).get();
        if (owner == this && inheritanceMask.contains(EPresentationAttrInheritance.OBJECT_TITLE_FORMAT)) {
            AdsEntityObjectClassDef ownerClass = (AdsEntityObjectClassDef) getOwnerClass();
            if (ownerClass == null) {
                return null;
            } else {
                return ((EntityPresentations) ownerClass.findRootBasis().getPresentations()).getObjectTitleFormat();
            }
        } else {
            return findAttributeOwner(EPresentationAttrInheritance.OBJECT_TITLE_FORMAT).get().objectTitleFormat;
        }
    }
    private final Hierarchy<AdsEditorPresentationDef> hierarchy = new MemberHierarchy<AdsEditorPresentationDef>(this) {
        @Override
        protected AdsEditorPresentationDef findMember(ClassPresentations clazz, Id id) {

            return ((EntityObjectPresentations) clazz).getEditorPresentations().getLocal().findById(id);
        }
    };

    @Override
    @SuppressWarnings("unchecked")
    public Hierarchy<AdsEditorPresentationDef> getHierarchy() {
        return hierarchy;
    }

    @SuppressWarnings("unchecked")
    @Override
    PresentationHierarchyIterator<AdsEditorPresentationDef> getHierarchyIteratorImpl(EScope scope, HierarchyIterator.Mode mode) {
        return new PresentationHierarchyIterator<AdsEditorPresentationDef>(this, scope, mode) {
            @Override
            public ExtendableDefinitions<AdsEditorPresentationDef> getCollectionForClass(AdsEntityObjectClassDef c) {
                return c.getPresentations().getEditorPresentations();
            }
        };
    }

    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        super.visitChildren(visitor, provider);
        if (!isObjectTitleFormatInherited()) {
            this.objectTitleFormat.visit(visitor, provider);
        }
        if (!isEditorPagesInherited()) {
            this.editorPages.visit(visitor, provider);
        }
        //if (!isExplorerItemsInherited()) {
        this.explorerItems.visit(visitor, provider);
        //}
        if (!isCustomViewInherited()) {
            if (this.customViewSuppoort.isUseCustomView(ERuntimeEnvironmentType.EXPLORER)) {
                this.customViewSuppoort.getCustomView(ERuntimeEnvironmentType.EXPLORER).visit(visitor, provider);
            }
            if (this.customViewSuppoort.isUseCustomView(ERuntimeEnvironmentType.WEB)) {
                this.customViewSuppoort.getCustomView(ERuntimeEnvironmentType.WEB).visit(visitor, provider);
            }
        }

        propertyAttributesCollection.visit(visitor, provider);
    }

    public void appendTo(EditorPresentationDefinition xDef, ESaveMode saveMode) {
        super.appendTo(xDef, saveMode);
        if (saveMode == ESaveMode.NORMAL) {
            if (replacedPresentationId != null) {
                xDef.setReplacedPresentationId(replacedPresentationId);
            }
        }
        if (rightsSrcId != null) {
            xDef.setRightsSourceId(rightsSrcId);

        }
        switch (this.rightsInheritanceMode) {
            case FROM_DEFINED:
            case NONE:
                xDef.setRightsInheritanceMode(rightsInheritanceMode);
                break;
            case FROM_REPLACED:
                if (this.replacedPresentationId == null) {
                    xDef.setRightsInheritanceMode(rightsInheritanceMode);
                }
                break;
        }
        this.objectTitleFormat.appendTo(xDef.addNewObjectTitleFormat(), saveMode);
        if (!this.editorPages.getLocal().isEmpty() || !this.editorPages.getOrder().isEmpty()) {
            this.editorPages.appendTo(xDef.addNewEditorPages(), saveMode);
        }
        this.explorerItems.appendTo(xDef.addNewChildExplorerItems(), saveMode);

        if (this.clientEnv != null && this.clientEnv != ERuntimeEnvironmentType.COMMON_CLIENT) {
            xDef.setClientEnvironment(this.clientEnv);
        }

        if (saveMode == ESaveMode.NORMAL || (saveMode == ESaveMode.API && !isFinal())) {
            if (!isCustomViewInherited()) {
                if (this.customViewSuppoort.isUseCustomView(ERuntimeEnvironmentType.EXPLORER)) {
                    ((AdsCustomEditorDef) this.customViewSuppoort.getCustomView(ERuntimeEnvironmentType.EXPLORER)).appendTo(xDef, saveMode);
                }
                if (this.customViewSuppoort.isUseCustomView(ERuntimeEnvironmentType.WEB)) {
                    ((AdsRwtCustomEditorDef) this.customViewSuppoort.getCustomView(ERuntimeEnvironmentType.WEB)).appendTo(xDef, saveMode);
                }
            }

            if (getPropertyPresentationAttributesCollection().size() > 0) {
                final EditorPresentationDefinition.RestrictedProperties restrictedProperties = xDef.addNewRestrictedProperties();
                for (final AdsPropertyPresentationAttributes restriction : getPropertyPresentationAttributesCollection()) {

                    if (restriction.isEmpty()) {
                        continue;
                    }

                    final PropertyAttributes attributes = restrictedProperties.addNewPropertyAttributes();

                    if (restriction.getEditPossibility() != null) {
                        attributes.setEditPossibility(restriction.getEditPossibility());
                    }

                    if (restriction.getVisibility() != null) {
                        attributes.setVisibility(restriction.getVisibility());
                    }

                    if (restriction.getMandatory() != null) {
                        attributes.setMandatory(restriction.getMandatory());
                    }

                    if (restriction.getPresentable() != null) {
                        attributes.setPresentable(restriction.getPresentable());
                    }

                    if (restriction.getTitleId() != null) {
                        attributes.setTitleId(restriction.getTitleId());
                    }

                    attributes.setPropertyId(restriction.getPropertyId());
                }
            }

        }
        // }
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
    public WarningSuppressionSupport getWarningSuppressionSupport(boolean createIfAbsent) {
        synchronized (this) {
            if (warningsSupport == null && createIfAbsent) {
                warningsSupport = new Problems(this, null);
            }
            return warningsSupport;
        }
    }

    @Override
    public RadixIcon getIcon() {
        return AdsDefinitionIcon.EDITOR_PRESENTATION;
    }

    @Override
    public void collectDependences(final List<Definition> list) {
        super.collectDependences(list);
        findReplacedEditorPresentation().save(list);
        findBaseEditorPresentation().save(list);

//        if (!isForbiddenPropertiesInherited() && forbiddenProps != null) {
//            forbiddenProps.collectDependences(list);
//        }
        propertyAttributesCollection.collectDependences(list);

        ModelClassInfo modelInfo = findActualModelClass();

        if (modelInfo.clazz != null) {
            list.add(modelInfo.clazz);
            if (modelInfo.baseEpr != null) {
                list.add(modelInfo.baseEpr);
            }
        }
    }

    @Override
    public ClipboardSupport<AdsEditorPresentationDef> getClipboardSupport() {
        return new AdsClipboardSupport<AdsEditorPresentationDef>(this) {
            @Override
            protected XmlObject copyToXml() {
                EditorPresentationDefinition xDef = EditorPresentationDefinition.Factory.newInstance();
                appendTo(xDef, ESaveMode.NORMAL);
                return xDef;
            }

            @Override
            protected AdsEditorPresentationDef loadFrom(XmlObject xmlObject) {
                if (xmlObject instanceof EditorPresentationDefinition) {
                    return Factory.loadFrom((EditorPresentationDefinition) xmlObject);
                } else {
                    return super.loadFrom(xmlObject);
                }
            }

            @Override
            protected Method getDecoderMethod() {
                try {
                    return AdsEditorPresentationDef.Factory.class.getDeclaredMethod("loadFrom", EditorPresentationDefinition.class);
                } catch (NoSuchMethodException | SecurityException ex) {
                    return null;
                }
            }
        };
    }

    @Override
    public boolean isSuitableContainer(AdsDefinitions collection) {
        return collection instanceof ExtendablePresentations.ExtendablePresentationsLocal && collection.getContainer() instanceof EditorPresentations;
    }

    @Override
    public EDefType getDefinitionType() {
        return EDefType.EDITOR_PRESENTATION;
    }

    @Override
    public boolean setBasePresentationId(Id id) {
        if (super.setBasePresentationId(id)) {
            fireEditorPagesInheritanceChange();
            fireExplorerItemInheritanceChange();
            return true;
        }
        return false;
    }

    @Override
    public boolean delete() {
        if (super.delete()) {
            fireExplorerItemInheritanceChange();
            return true;
        }
        return false;
    }

    @Override
    public void afterOverwrite() {
        super.afterOverwrite();
        setEditorPagesInherited(true);
        editorPages.afterOverwrite();
        setExplorerItemsInherited(true);
        explorerItems.getChildren().getLocal().clear();
        setObjectTitleFormatInherited(true);
        objectTitleFormat = new AdsObjectTitleFormatDef(this);
        setEditorPagesInherited(false);
        setExplorerItemsInherited(false);

        final SearchResult<AdsEditorPresentationDef> result = getHierarchy().findOverwritten();
        if (!result.isEmpty()) {
            if (!result.get().isUseDefaultModel()) {
                setUseDefaultModel(false);
                getModel().setOverwrite(true);
            }
        }
    }

    public boolean isPropertyPresentationAttributesInherited() {
        return inheritanceMask.contains(EPresentationAttrInheritance.PROPERTY_PRESENTATION_ATTRIBUTES);
    }

    public void setPropertyPresentationAttributesInherited(boolean inherit) {
        if (inherit) {
            if (!isPropertyPresentationAttributesInherited()) {
                inheritanceMask.add(EPresentationAttrInheritance.PROPERTY_PRESENTATION_ATTRIBUTES);
                setEditState(EEditState.MODIFIED);
            }
        } else {
            if (isPropertyPresentationAttributesInherited()) {
                inheritanceMask.remove(EPresentationAttrInheritance.PROPERTY_PRESENTATION_ATTRIBUTES);
                setEditState(EEditState.MODIFIED);
            }
        }
    }

    @Override
    public AdsModelClassDef findFinalModel() {
        if (modelClass == null) {
            Id baseId = getBasePresentationId();
            EntityObjectPresentations eprs = getOwnerPresentations();
            if (eprs == null) {
                return null;
            }
            while (baseId != null) {
                AdsEditorPresentationDef epr = eprs.getEditorPresentations().findById(baseId, EScope.ALL).get();
                if (epr != null) {

                    if (epr.modelClass == null || (epr.getClientEnvironment() != getClientEnvironment() && epr.getClientEnvironment() != ERuntimeEnvironmentType.COMMON_CLIENT)) {
                        baseId = epr.getBasePresentationId();
                    } else {
                        return epr.modelClass;
                    }
                } else {
                    return null;
                }
            }
            return null;

        } else {
            return modelClass;
        }
    }

    @Override
    public SearchResult<? extends AdsDefinition> findComponentDefinition(Id id) {
        switch (id.getPrefix()) {
            case EXPLORER_ITEM:
            case PARAGRAPH_LINK:
            case PARAGRAPH:
                AdsExplorerItemDef def = getExplorerItems().findChildExplorerItem(id);
                if (def != null) {
                    return SearchResult.single(def);
                }
            case EDITOR_PAGE:
                return getEditorPages().findById(id, EScope.ALL);

        }
        return super.findComponentDefinition(id);
    }

    @Override
    public SearchResult<AdsEditorPresentationDef> findBasePresentation() {
        return findBaseEditorPresentation();
    }

    public static class ModelClassInfo {

        public final AdsClassDef clazz;
        public final boolean useDefaultModel;
        public final Id basePresentationId;
        public final AdsEditorPresentationDef baseEpr;

        public ModelClassInfo(AdsClassDef clazz, boolean useDefaultModel, Id basePresentationId, AdsEditorPresentationDef baseEpr) {
            this.clazz = clazz;
            this.useDefaultModel = useDefaultModel;
            this.basePresentationId = basePresentationId;
            this.baseEpr = baseEpr;
        }
    }

    public final ModelClassInfo findActualModelClass() {
        boolean useDefaultModel = false;
        AdsClassDef clazz;
        Id basePresentationId = null;
        AdsEditorPresentationDef baseEpr = null;
        if (isUseDefaultModel()) {
            AdsEntityModelClassDef model = (AdsEntityModelClassDef) findFinalModel();
            clazz = getOwnerClass();
            if (model != null) {
                baseEpr = findBaseEditorPresentation().get();
                basePresentationId = baseEpr.getId();
                if (baseEpr.getOwnerClass() == clazz || baseEpr.getOwnerClass().getId() == clazz.getId()) {
                    clazz = model;
                } else {
                    useDefaultModel = true;
                }
            } else {
                useDefaultModel = true;
            }
        } else {
            clazz = getModel();
        }
        return new ModelClassInfo(clazz, useDefaultModel, basePresentationId, baseEpr);
    }

    @Override
    protected void appendAdditionalToolTip(StringBuilder sb) {
        super.appendAdditionalToolTip(sb);
        if (!findBasePresentation().isEmpty()) {
            String basePresentation =
                    findBasePresentation().get().getEditorPages().getOrder().getQualifiedName();
            sb.append("<br>Base: <br>&nbsp;").append("<a href=\"").append("\">").append(basePresentation).append("</a>");
        }
        if (!findReplacedEditorPresentation().isEmpty()) {
            String replacedPresentation =
                    findReplacedEditorPresentation().get().getEditorPages().getOrder().getQualifiedName();
            sb.append("<br>Replace: <br>&nbsp;").append("<a href=\"").append("\">").append(replacedPresentation).append("</a>");
        }
    }

    @Override
    public boolean isRadixdocProvider() {
        return true;
    }

    @Override
    public RadixdocSupport<? extends RadixObject> getRadixdocSupport() {
        return new RadixdocSupport<AdsEditorPresentationDef>(this) {
            @Override
            public IRadixdocPage document(Page page, DocumentOptions options) {
                return new EditorPresentationRadixdoc(getSource(), page, options);
            }
        };
    }
}