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

package org.radixware.kernel.common.client.models.items.properties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.LinkedList;
import java.util.Objects;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.client.enums.ETextOptionsMarker;
import org.radixware.kernel.common.client.enums.ETextOptionsMarkerGroup;
import org.radixware.kernel.common.client.errors.ActivatingPropertyError;
import org.radixware.kernel.common.client.errors.InvalidPropertyTypeError;
import org.radixware.kernel.common.client.errors.ModelCreationError;
import org.radixware.kernel.common.client.errors.ObjectNotFoundError;
import org.radixware.kernel.common.client.errors.SettingPropertyValueError;
import org.radixware.kernel.common.client.errors.UnacceptableInputError;
import org.radixware.kernel.common.client.exceptions.ModelException;
import org.radixware.kernel.common.client.meta.RadClassPresentationDef;
import org.radixware.kernel.common.client.meta.RadCommandDef;
import org.radixware.kernel.common.client.meta.RadEditorPresentationDef;
import org.radixware.kernel.common.client.meta.RadPresentationCommandDef;
import org.radixware.kernel.common.client.meta.RadPropertyDef;
import org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes;
import org.radixware.kernel.common.client.meta.mask.EditMask;
import org.radixware.kernel.common.client.meta.mask.validators.ValidationResult;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.FilterModel;
import org.radixware.kernel.common.client.models.FormModel;
import org.radixware.kernel.common.client.models.IContext;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.models.PropEditorModel;
import org.radixware.kernel.common.client.models.ReportParamDialogModel;
import org.radixware.kernel.common.client.models.items.ModelItem;
import org.radixware.kernel.common.client.text.CachedTextOptionsProvider;
import org.radixware.kernel.common.client.text.CustomTextOptions;
import org.radixware.kernel.common.client.text.ITextOptionsProvider;
import org.radixware.kernel.common.client.types.EntityRestrictions;
import org.radixware.kernel.common.client.text.ITextOptions;
import org.radixware.kernel.common.client.text.ITextOptionsManager;
import org.radixware.kernel.common.client.text.MergedTextOptionsProvider;
import org.radixware.kernel.common.client.types.MatchOptions;
import org.radixware.kernel.common.client.types.Reference;
import org.radixware.kernel.common.client.types.UnacceptableInput;
import org.radixware.kernel.common.client.utils.ValueConverter;
import org.radixware.kernel.common.client.views.IArrayEditorDialog;
import org.radixware.kernel.common.client.views.IDialog.DialogResult;
import org.radixware.kernel.common.client.views.IPropEditor;
import org.radixware.kernel.common.client.views.IPropEditorDialog;
import org.radixware.kernel.common.client.views.IPropLabel;
import org.radixware.kernel.common.client.views.IView;
import org.radixware.kernel.common.client.widgets.IModelWidget;

import org.radixware.kernel.common.enums.*;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.types.Id;

public abstract class Property extends ModelItem {

    public final class PropertyTextOptions implements ITextOptionsProvider {

        private CustomTextOptions customOptions;
        private CachedTextOptionsProvider cachedProvider;
        private PropertyTextOptions synchronizedOptions;
        private final ETextOptionsMarker guiComponentMarker;

        PropertyTextOptions(final ETextOptionsMarker componentMarker) {
            guiComponentMarker = componentMarker;
        }

        private ITextOptionsProvider getProvider() {
            return cachedProvider == null ? getEnvironment().getTextOptionsProvider() : cachedProvider;
        }

        private void initCachedTextOptionsProvider() {
            final ITextOptionsManager manager =
                    getEnvironment().getApplication().getTextOptionsManager();
            final MergedTextOptionsProvider mergedProvider =
                    new MergedTextOptionsProvider(getEnvironment().getTextOptionsProvider(), customOptions, manager);
            cachedProvider =
                    new CachedTextOptionsProvider(mergedProvider);
        }

        private CustomTextOptions getCustomOptions() {
            if (customOptions == null) {
                customOptions = new CustomTextOptions();
                initCachedTextOptionsProvider();
            }
            return customOptions;
        }

        private ESelectorRowStyle getRowStyle(final EnumSet<ETextOptionsMarker> markers) {
            if (markers.contains(ETextOptionsMarker.SELECTOR_ROW) && (owner instanceof EntityModel)) {
                return ((EntityModel) owner).getSelectorRowStyle();
            }
            return null;
        }

        public ITextOptions getOptions() {
            final EnumSet<ETextOptionsMarker> markers = getTextOptionsMarkers();
            return getOptions(markers, null);
        }

        public ITextOptions getOptions(final EnumSet<ETextOptionsMarker> markers) {
            return getOptions(markers, null);
        }

        public ITextOptions getOptions(final ETextOptionsMarker... markers) {
            final EnumSet<ETextOptionsMarker> markersSet = EnumSet.noneOf(ETextOptionsMarker.class);
            markersSet.addAll(Arrays.asList(markers));
            return getOptions(markersSet, null);
        }

        @Override
        public ITextOptions getOptions(final EnumSet<ETextOptionsMarker> markers, final ESelectorRowStyle rowStyle) {
            final EnumSet<ETextOptionsMarker> allMarkers = markers.clone();
            allMarkers.removeAll(ETextOptionsMarker.getAllMarkersInGroup(ETextOptionsMarkerGroup.GUI_COMPONENT));
            allMarkers.add(guiComponentMarker);
            final ESelectorRowStyle style = rowStyle == null ? getRowStyle(allMarkers) : rowStyle;
            final ITextOptions options = getProvider().getOptions(allMarkers, style);
            return Property.this.getTextOptions(allMarkers, options);
        }

        public void setOptions(final ITextOptions options) {
            final CustomTextOptions customOpts = getCustomOptions();
            if (!Objects.equals(customOpts.getDefaultOptions(), options)) {
                if (synchronizedOptions != null) {
                    final ITextOptions prevOptions = synchronizedOptions.getOptions();
                    synchronizedOptions.getCustomOptions().setDefaultOptions(options);
                    synchronizedOptions.cachedProvider.clearCache();
                    if (!Objects.equals(prevOptions, synchronizedOptions.getOptions())) {
                        synchronizedProp.afterModify();
                    }
                }
                final ITextOptions prevOptions = getOptions();
                customOpts.setDefaultOptions(options);
                cachedProvider.clearCache();
                if (!Objects.equals(prevOptions, getOptions())) {
                    afterModify();
                }
            }
        }

        public void setOptions(final ITextOptions options, final ETextOptionsMarker... markers) {
            if (synchronizedOptions != null) {
                final ITextOptions prevTextOptions = synchronizedOptions.getOptions();
                synchronizedOptions.getCustomOptions().putOptions(options, markers);
                synchronizedOptions.cachedProvider.clearCache();
                if (!Objects.equals(prevTextOptions, synchronizedOptions.getOptions())) {
                    synchronizedProp.afterModify();
                }
            }
            final ITextOptions prevTextOptions = getOptions();
            getCustomOptions().putOptions(options, markers);
            cachedProvider.clearCache();
            if (!Objects.equals(prevTextOptions, getOptions())) {
                afterModify();
            }
        }

        void synchronizeOptions(final PropertyTextOptions source) {
            customOptions = source.customOptions == null ? null : source.customOptions.copy();
            if (customOptions != null) {
                initCachedTextOptionsProvider();
            }
            customOptions = source.customOptions;
            synchronizedOptions = source;
        }
    }
    
    private final RadPropertyDef def;
    private String title;
    private String hint;
    private boolean mandatory;
    private Boolean visible = null;
    private final EPropertyVisibility visibility;
    private boolean enabled = true;
    private boolean isCustomEditOnly;
    private boolean canOpenCustomEditor = true;
    private Boolean isPersistentlyReadonly;
    final private EEditPossibility editPossibility;
    final private EditMask editMask;
    private List<Id> commandIds;
    protected PropertyValue serverValue, initialValue, internalValue;
    private List<Object> predefinedValues;
    private boolean valEdited = false; // значение изменено по сравнению с сервером    
    private boolean valEditedByParentRef = false;//значение изменено, вследствие изменения значения свойства-ссылки
    private Property synchronizedProp = null;    
   
    private final PropertyTextOptions valueTextOptions;
    private final PropertyTextOptions titleTextOptions;
    private boolean canModifyValue = true;
    private EPropertyValueStorePossibility storePossiblity;
    private UnacceptableInput unacceptableInput;

    // published property
    @SuppressWarnings("LeakingThisInConstructor")
    protected Property(Model owner, RadPropertyDef def) {
        super(owner, def.getId());
        this.def = def;

        typeCheck(def);

        isCustomEditOnly = def.isCustomEditOnly();
        editMask = EditMask.newCopy(def.getEditMask());
        editMask.setOwnerModelItem(this);
        hint = def.getHint();
        initialValue = new PropertyValue(def, null);
        if (isLocal()) {
            internalValue = new PropertyValue(def, null);
        } else {
            internalValue = new NoValue(def);
        }
        final RadPropertyPresentationAttributes presentationAttributes;
        if (owner instanceof EntityModel) {
            final EntityModel entityModel = ((EntityModel) owner);
            final RadEditorPresentationDef editorPresentation = entityModel.getEditorPresentationDef();
            if (editorPresentation.isPropertyDefExistsById(def.getId())) {
                final RadClassPresentationDef classDef = entityModel.getClassPresentationDef();
                presentationAttributes =
                        editorPresentation.getPropertyAttributesByPropId(def.getId(), classDef);
            } else {
                presentationAttributes = null;
            }
        } else {
            presentationAttributes = null;
        }
        if (presentationAttributes == null) {
            title = def.getTitle();
            visibility = EPropertyVisibility.ALWAYS;
            if (def.getNature()==EPropNature.PARENT_PROP &&
                (owner instanceof FormModel || owner instanceof ReportParamDialogModel)
               ){
                editPossibility = EEditPossibility.PROGRAMMATICALLY;
                mandatory = false;
            }else{
                editPossibility = def.getEditPossibility();
                mandatory = def.isMandatory();
            }
        } else {
            title = presentationAttributes.getTitle();
            visibility = presentationAttributes.getVisibility();
            editPossibility = presentationAttributes.getEditPossibility();
            mandatory = presentationAttributes.isMandatory();
        }
        storePossiblity = def.getPropertyValueStorePossibility();        
        titleTextOptions = new PropertyTextOptions(ETextOptionsMarker.LABEL);
        valueTextOptions =
                new PropertyTextOptions(inSelectorRow() ? ETextOptionsMarker.SELECTOR_ROW : ETextOptionsMarker.EDITOR);
    }

    @Override
    public final Model getOwner() {//Overrided to make final
        return super.getOwner();
    }

    private void typeCheck(RadPropertyDef def) {
        if (getType() != def.getType()) {
            throw new InvalidPropertyTypeError(this, getType());
        }
    }

    public RadPropertyDef getDefinition() {
        return def;
    }

    public Object getValueObject() {
        if (needForActivation()) {
            activate();
        }
        return getValObjectImpl();
    }

    public void setValueObject(Object x) {
        setValObjectImpl(x);
    }

    protected final void setInternalValue(final PropertyValue value) {//RADIX-3677
        final PropertyValue propValue = new PropertyValue(value);
        //установка идентичного значения может привести к нежелательному изменению атрибута наследования
        //и признака модификации (RADIX-8281)
        if (!propValue.hasSameValue(internalValue)){
            setValObjectImpl(propValue.getValue());
        }
        if (!propValue.isDefined() || propValue.getInheritedValue() != null || propValue.isReadonly()) {
            propValue.refineValue(internalValue.getValue());
            internalValue = propValue;
            unacceptableInput = null;
            afterModify();
            if (synchronizedProp != null) {
                synchronizedProp.internalValue = new PropertyValue(propValue);
                synchronizedProp.unacceptableInput = null;
                afterModify();
            }
        }
    }

    public final boolean isLocal() {
        return def.getNature() == EPropNature.VIRTUAL;
    }

    public Object getInitialValue() {
        return initialValue.getValue();
    }

    final void setInitialValObject(final Object value) {
        if (!initialValue.hasSameValue(value)) {
            initialValue.refineValue(value);
            if (synchronizedProp != null) {
                synchronizedProp.setInitialValObject(value);
            }
            afterModify();
        }
    }

    final void setInternalValObject(final Object internalValObject) {
        final boolean wasActivated = isActivated();
        if (internalValue instanceof NoValue) {
            internalValue = new PropertyValue(getDefinition(), internalValObject);
        } else {
            internalValue.setValue(internalValObject);
        }
        setValEdited(!wasActivated || !initialValue.hasSameValue(internalValue));
        unacceptableInput = null;
        afterModify();
    }

    public void setPropertyValueStorePossiblity(EPropertyValueStorePossibility possiblity) {
        this.storePossiblity = possiblity;
        afterModify();
        if (synchronizedProp != null) {
            synchronizedProp.storePossiblity = possiblity;
        }
    }

    public EPropertyValueStorePossibility getPropertyValueStorePossibility() {
        return this.storePossiblity;
    }

    public abstract Object getServerValObject();

    protected abstract Object getValObjectImpl();

    protected abstract void setValObjectImpl(Object x);

    public abstract EValType getType();

    public abstract Class<?> getValClass();

    private boolean inSelectorRow() {
        return owner.getContext() instanceof IContext.SelectorRow;
    }

    public void setServerValue(final PropertyValue value) {
        if (isLocal()) {
            throw new IllegalUsageError("This property cannot have server value (you may want to use setInitialValue method)");
        }
        if (value == null) {//deactivating property
            //do not deactivate property in selector: RADIX-2869
            if (!inSelectorRow()) {
                serverValue = null;
                internalValue = new NoValue(def);
                unacceptableInput = null;
                wasRead = false;
            }
            if (synchronizedProp != null && !synchronizedProp.inSelectorRow()) {
                synchronizedProp.wasRead = false;
                synchronizedProp.serverValue = null;
                synchronizedProp.internalValue = new NoValue(def);
                synchronizedProp.unacceptableInput = null;
            }
            return;
        }
        if (Objects.equals(serverValue, value) && !isValEdited() && !isValInheritanceChanged()) {
            removeUnacceptableInputRegistration();
            return;
        }
        final boolean wasForcedlyActivated = isActivated() && serverValue==null;
        serverValue = new PropertyValue(value);
        unacceptableInput = null;
        if (!wasForcedlyActivated){
            final boolean internalReadonly = internalValue.isReadonly();
            internalValue = new PropertyValue(value);            
            internalValue.setReadonly(internalReadonly);            
        }
        afterModify();
        if (wasForcedlyActivated){
            setValEdited(!serverValue.hasSameValue(internalValue));
        }else{
            setValEdited(false);
        }
        doSynchronization();
    }

    public void cancelChanges() {
        if (isValEdited() || isValInheritanceChanged()) {
            if (isLocal()) {
                internalValue = new PropertyValue(initialValue);
            } else {
                internalValue = serverValue != null ? new PropertyValue(serverValue) : new NoValue(def);
            }
            setValEdited(false);
            unacceptableInput = null;
            afterModify();
            if (synchronizedProp != null) {
                synchronizedProp.cancelChanges();
            }
        }else{
            removeUnacceptableInputRegistration();
        }
    }

    public final void startSynchronization(final Property property2) {
        if (synchronizedProp != null) {
            throw new IllegalStateException("Synchronization already established");
        }

        synchronizedProp = property2;
        property2.synchronizedProp = this;
        copy(property2);//TWRBS-2060
    }

    protected final Property getSynchronizedProperty() {
        return synchronizedProp;
    }

    public void copy(Property source) {
        if (def != source.def) {
            throw new IllegalArgumentException("Cannot copy from property which have another definition");
        }
        if (!def.isReadSeparately()) {
            serverValue = source.serverValue == null ? null : new PropertyValue(source.serverValue);
            if (source.internalValue instanceof NoValue) {
                internalValue = new NoValue(def);
            } else {
                internalValue = new PropertyValue(source.internalValue);
            }
            unacceptableInput = source.unacceptableInput;
            initialValue = new PropertyValue(source.initialValue);
            valEdited = source.valEdited;
            valEditedByParentRef = source.valEditedByParentRef;
            wasRead = source.wasRead;
        }
        title = source.title;
        hint = source.hint;
        mandatory = source.mandatory;
        isPersistentlyReadonly = source.isPersistentlyReadonly;
        isCustomEditOnly = source.isCustomEditOnly;
        visible = source.visible;
        enabled = source.enabled;
        canOpenCustomEditor = source.canOpenCustomEditor;
        titleTextOptions.synchronizeOptions(source.titleTextOptions);
        valueTextOptions.synchronizeOptions(source.valueTextOptions);
        afterModify();
    }

    public final void stopSynchronization() {
        if (synchronizedProp == null) {
            return;
        }
        synchronizedProp.synchronizedProp = null;
        synchronizedProp = null;
    }

    protected final void doSynchronization() {
        if (synchronizedProp != null) {
            synchronizedProp.setServerValue(serverValue);
        }
    }

    public String getTitle() {
        return title;
    }

    public String getHint() {
        return hint;
    }

    public void setTitle(final String title) {
        if (!Objects.equals(title, this.title)) {
            this.title = title;
            afterModify();
            if (synchronizedProp != null) {
                synchronizedProp.setTitle(title);
            }
        }
    }

    public void setHint(final String newHint) {
        if (!Objects.equals(hint, newHint)) {
            hint = newHint;
            afterModify();
            if (synchronizedProp != null) {
                synchronizedProp.setHint(newHint);
            }
        }
    }

    public boolean isMandatory() {
        return mandatory;
    }

    public void setMandatory(boolean x) {
        if (mandatory != x) {
            mandatory = x;
            afterModify();
            if (synchronizedProp != null) {
                synchronizedProp.setMandatory(x);
            }
        }
    }

    public boolean isCustomEditOnly() {
        return isCustomEditOnly;
    }

    public void setCustomEditOnly(final boolean customEdit) {
        if (isCustomEditOnly != customEdit) {
            isCustomEditOnly = customEdit;
            afterModify();
            if (synchronizedProp != null) {
                synchronizedProp.isCustomEditOnly = customEdit;
                synchronizedProp.afterModify();
            }
        }
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean isEnabled) {
        if (enabled != isEnabled) {
            enabled = isEnabled;
            afterModify();
            if (synchronizedProp != null) {
                synchronizedProp.setEnabled(isEnabled);
            }
        }
    }

    public EditMask getEditMask() {
        return editMask;
    }

    public boolean isValEdited() {
        return valEdited;
    }

    public final boolean isValueModifiedByChangingParentRef() {//RADIX-6561, DBP-1547
        return valEdited && valEditedByParentRef;
    }

    private boolean isValInheritanceChanged() {
        return serverValue != null
                && !Objects.equals(serverValue.getInheritedValue(), internalValue.getInheritedValue());
    }

    private void setModifiableValueState(final boolean canModify) {
        canModifyValue = canModify;
    }

    private boolean isInUnmodifiableState() {
        return !canModifyValue;
    }

    public void setValEdited(final boolean wasChanged) {
        if (wasChanged && isInUnmodifiableState()) {
            final String message = "Value of property '%s' (#%s)\ncannot be modified";
            throw new IllegalStateException(String.format(message, getTitle(), getDefinition().getId().toString()));
        }
        if (!wasChanged) {
            initialValue = new PropertyValue(internalValue);            
        }
        valEdited = wasChanged;
        valEditedByParentRef = false;
        if (owner instanceof EntityModel) {
            EntityModel e = (EntityModel) owner;
            if (wasChanged) {
                e.setIsEdited(true);
            } else if (e.getEditedProperties().isEmpty()) {
                e.setIsEdited(false);
            }
        } else if (owner instanceof FilterModel) {
            FilterModel f = (FilterModel) owner;
            if (wasChanged) {
                f.setIsPropertyValueEdited(true);
            } else if (f.getEditedProperties().isEmpty()) {
                f.setIsPropertyValueEdited(false);
            }
        }
    }

    final void setValueModifiedByChangingParentRef(final boolean isEdited) {
        valEditedByParentRef = isEdited;
        if (synchronizedProp!=null){
            synchronizedProp.valEditedByParentRef = isEdited;
        }
    }

    public boolean isVisible() {
        if (def.getType() == EValType.OBJECT && !((EntityModel) owner).isExists()) {
            return false;
        }
        if (visible == null) {
            switch (visibility) {
                case ALWAYS:
                    return true;
                case ONLY_FOR_NEW:
                    return owner instanceof EntityModel ? ((EntityModel) owner).isNew() : false;
                case ONLY_FOR_EXISTENT:
                    return owner instanceof EntityModel ? !((EntityModel) owner).isNew() : false;
                default:
                    return false;
            }
        } else {
            return visible;
        }
    }

    public void setVisible(final boolean visible) {
        if (this.visible == Boolean.valueOf(visible)) {
            return;
        }
        final boolean currentVisible = isVisible();
        this.visible = Boolean.valueOf(visible);
        if (currentVisible != visible) {
            afterModify();
        }
        if (synchronizedProp != null) {
            synchronizedProp.setVisible(visible);
        }
    }

    // Передать фокус виджету свойства
    public boolean setFocused() {
        final IView view;
        if (inSelectorRow() && synchronizedProp != null && synchronizedProp.owner.getView() != null) {
            view = synchronizedProp.owner.getView();
        } else {
            view = owner.getView();
        }

        if (view == null || !isVisible() || !isEnabled()) {
            return false;
        } else if (view.setFocusedProperty(getId())) {//Для стандартых представлений
            return true;
        } else if (widgets != null) {//Для кастомных представлений
            for (IModelWidget w : widgets) {
                if (w.setFocus(this)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void finishEdit() {
        if (isVisible() && widgets != null) {
            for (IModelWidget w : widgets) {
                if (w instanceof IPropEditor) {
                    ((IPropEditor) w).finishEdit();
                }
            }
        }
    }

    public boolean isReadonly() {
        if (isEditingForbidden() || (internalValue != null && internalValue.isReadonly())) {
            return true;
        }

        if (owner instanceof EntityModel) {
            EntityModel entity = (EntityModel) owner;
            if (entity.getRestrictions().getIsUpdateRestricted()) {
                return true;
            }
        }

        return false;
    }

    private boolean isPersistentlyReadonly() {
        if (def.getEditingEnvironmentType() != null
                && def.getEditingEnvironmentType() != ERuntimeEnvironmentType.COMMON_CLIENT
                && def.getEditingEnvironmentType() != ERuntimeEnvironmentType.COMMON
                && def.getEditingEnvironmentType() != owner.getEnvironment().getApplication().getRuntimeEnvironmentType()) {
            return true;
        }

        if (editPossibility == EEditPossibility.NEVER) {
            return true;
        }

        if ((editPossibility == EEditPossibility.ALWAYS)
                || !(owner instanceof EntityModel)) {
            return false;
        }

        return false;
    }

    private boolean isEditingForbidden() {
        if (isPersistentlyReadonly == null) {
            isPersistentlyReadonly = Boolean.valueOf(isPersistentlyReadonly());
        }

        if (isPersistentlyReadonly == Boolean.TRUE || (serverValue != null && serverValue.isReadonly())) {
            return true;
        }

        if (owner instanceof EntityModel) {
            final EntityModel e = (EntityModel) owner;
            final EntityRestrictions restrictions = e.getRestrictions();
            if (restrictions.getIsUpdateRestricted() && !restrictions.canBeAllowed(ERestriction.UPDATE)) {
                return true;
            }
            switch (editPossibility) {
                case ONLY_IN_EDITOR:
                    return e.getContext() instanceof IContext.SelectorRow;
                case ON_CREATE:
                    return !e.isNew();
                case ONLY_EXISTING:
                    return e.isNew();
                case NEVER:
                    return true;
                default:
                    return false;
            }
        }
        return false;
    }

    public EEditPossibility getEditPossibility() {
        return editPossibility;
    }

    public void setReadonly(boolean readonly) {
        if (internalValue.isReadonly() == readonly) {
            return;
        }
        boolean wasReadonly = isReadonly();
        internalValue.setReadonly(readonly);
        if (wasReadonly != isReadonly()) {
            afterModify();
        }
        if (synchronizedProp != null) {
            synchronizedProp.setReadonly(readonly);
        }
    }

    public final boolean isValueDefined() {
        if (needForActivation()) {
            activate();
        }
        return internalValue.isOwn() || (internalValue.isDefined() && def.isInheritable());
    }

    public final boolean hasOwnValue() {
        if (needForActivation()) {
            activate();
        }
        return internalValue.isOwn();
    }

    public final void setHasOwnValue(boolean isOwn) throws ServiceClientException, ModelException, InterruptedException {
        if (needForActivation()) {
            activate();
        }
        checkForUnacceptableInput();
        if (!def.isInheritable() && !def.isDefineable()) {
            final String msg = getEnvironment().getMessageProvider().translate("ExplorerException", "Property %s is not inheritable.");
            throw new IllegalUsageError(String.format(msg, def.toString()));
        }
        if (internalValue.isOwn() == isOwn) {
            return;
        }
        if (isOwn && !internalValue.isDefined()) {
            // Делаем свойство собственным и значения не было
            if (this instanceof PropertyRef) {
                final PropertyRef propertyRef = (PropertyRef) this;
                final Reference ref = propertyRef.selectParent();
                if (ref == null) {
                    return;
                }
                try {
                    propertyRef.setValueObject(ref);
                } catch (Exception ex) {
                    getEnvironment().processException(new SettingPropertyValueError(propertyRef, ex));
                }
            } else if (this instanceof PropertyObject) {
                final PropertyObject propertyObject = (PropertyObject) this;
                if (propertyObject.create() != null) {
                    internalValue.setOwn(true);
                    if (synchronizedProp != null) {
                        synchronizedProp.internalValue.setOwn(true);
                        synchronizedProp.afterModify();
                    }
                }
                return;
            } else if (this instanceof PropertyArr) {
                PropertyArr property = (PropertyArr) this;
                /*				final Arr initVal;
                 if (def.getInitialVal() != null
                 && !def.getInitialVal().isEmpty())
                 initVal = (Arr) DbpValueConverter.valAsStr2ObjVal(def
                 .getInitialVal(), def.getType());
                 else
                 initVal = null;*/
                final IArrayEditorDialog dialog = property.getEditorDialog(null, null);
                if (dialog.execDialog() == DialogResult.ACCEPTED) {
                    property.setValueObject(dialog.getCurrentValue());
                } else {
                    return;
                }
            } else {
                setValueObject(def.getInitialVal().toObject(def.getType()));
            }

            internalValue.setOwn(true);
            setValEdited(true);
            if (synchronizedProp != null) {
                synchronizedProp.internalValue.setOwn(true);
                synchronizedProp.setValEdited(true);
                synchronizedProp.afterModify();
            }
        } else if (serverValue.isOwn() == isOwn && serverValue.getInheritedValue() != null) {
            // Признак наследования совпадает с последним полученным с сервера
            cancelChanges();
        } else {
            // Признак наследования отличается и значение было определено
            if (!isOwn) {
                final Object null_value;
                if (getInheritableValue() != null) {
                    null_value = getInheritableValue().getInheritedValue();
                } else if (def.getNature() != EPropNature.USER) {
                    null_value = def.getValInheritanceMark();
                } else {
                    null_value = null;
                }
                setValueObject(null_value);
            } else {
                if (this instanceof PropertyObject) {
                    if (isMandatory()) {
                        ((PropertyObject) this).create();
                        return;
                    } else {
                        setValueObject(null);
                    }
                } else {
                    setValueObject(getServerValObject());
                }
            }
            internalValue.setOwn(isOwn);
            setValEdited(true);
            if (synchronizedProp != null) {
                synchronizedProp.internalValue.setOwn(isOwn);
                synchronizedProp.setValEdited(true);
                synchronizedProp.afterModify();
            }
        }
        afterChangeValueInheritance(isOwn);
        afterModify();
    }
    
    protected void afterChangeValueInheritance(final boolean isOwnValue){
        
    }

    public final PropertyValue.InheritableValue getInheritableValue() {
        if (internalValue.getInheritedValue() != null) {
            //RADIX-3677: It may be result of setParent call
            return internalValue.getInheritedValue();
        } else {
            return serverValue != null ? serverValue.getInheritedValue() : null;
        }
    }

    public List<Id> getEnabledCommands() {
        if (commandIds == null) {
            commandIds = new ArrayList<>();
            final List<Id> modelCommands = owner.getAccessibleCommandIds();
            RadCommandDef command;
            RadPresentationCommandDef entityCommand;
            for (Id cmdId : modelCommands) {
                try {
                    if (owner.getDefinition().isCommandDefExistsById(cmdId)){
                        command = owner.getDefinition().getCommandDefById(cmdId);
                    }else{
                        command = owner.getCommand(cmdId).getDefinition();
                    }
                } catch (RuntimeException exception) {
                    getEnvironment().getTracer().error(exception);
                    continue;
                }
                if (command instanceof RadPresentationCommandDef) {
                    entityCommand = (RadPresentationCommandDef) command;
                    if (entityCommand.scope == ECommandScope.PROPERTY
                            && entityCommand.isApplicableForProperty(def.getId())) {
                        commandIds.add(cmdId);
                    }
                }
            }
        }
        return Collections.unmodifiableList(commandIds);
    }

    public void writeValue2Xml(org.radixware.schemas.eas.Property xmlProp) {
        if (needForActivation()) {
            activate();
        }
        checkForUnacceptableInput();
        ValueConverter.objVal2EasPropXmlVal(getValueObject(), def.getType(), xmlProp);
        xmlProp.setId(getId());
        if (isReadonly()) {
            xmlProp.setReadOnly(true);
        }
        if (!hasOwnValue()) {
            xmlProp.setIsOwnVal(false);
        }
    }

    public void setPredefinedValues(final List<Object> values) {
        if (RadPropertyDef.isPredefinedValuesSupported(getType(), getEditMask().getType())) {
            if (values == null) {
                predefinedValues = null;
            } else {
                predefinedValues = new LinkedList<>();
                for (Object value : values) {
                    predefinedValues.add(PropertyValue.copyValue(value, getType()));
                }
            }
            afterModify();
            if (synchronizedProp != null) {
                synchronizedProp.predefinedValues = predefinedValues;
                synchronizedProp.afterModify();
            }
        } else {
            throw new UnsupportedOperationException("Predefined values is not supported for this property");
        }
    }

    public List<Object> getPredefinedValues() {
        if (predefinedValues == null) {
            return null;
        } else {
            final List<Object> result = new LinkedList<>();
            for (Object value : predefinedValues) {
                result.add(PropertyValue.copyValue(value, getType()));
            }
            return Collections.unmodifiableList(result);
        }
    }

    public abstract IPropEditor createPropertyEditor();

    public IPropLabel createPropertyLabel() {
        return getEnvironment().getApplication().getStandardViewsFactory().newPropLabel(this);
    }

    public String getValueAsString() {
        return editMask.toStr(getEnvironment(), getValueObject());
    }

    public boolean canOpenPropEditorDialog() {
        return def.customDialog() && owner.canUsePropEditorDialog(getId()) && canOpenCustomEditor;
    }

    public void setCanOpenPropEditorDialog(final boolean canOpen) {
        if (canOpenCustomEditor != canOpen) {
            canOpenCustomEditor = canOpen;
            afterModify();
            if (synchronizedProp != null) {
                synchronizedProp.canOpenCustomEditor = canOpen;
                synchronizedProp.afterModify();
            }
        }
    }

    public final boolean isOwnValueAcceptable(final Object value) {
        if (getDefinition().isInheritable()
                && !getDefinition().isDefineable()
                && getInheritableValue() != null) {//RADIX-6710
            Object markValue = getDefinition().getValInheritanceMark();
            if (markValue instanceof XmlObject) {
                markValue = ((XmlObject) markValue).xmlText();
            }
            return !Objects.equals(markValue, value);
        } else {
            return true;
        }
    }

    public DialogResult execPropEditorDialog() {
        final IContext.PropEditorContext context = new IContext.PropEditorContext(this, -1);
        if (def.customDialog()) {
            IPropEditorDialog dialog = def.getPropEditorDialog(getEnvironment());
            if (dialog != null) {

                dialog.getModel().setContext(context);
                owner.beforeOpenPropEditorDialog(this, (PropEditorModel) dialog.getModel());
                final IView view = getOwnerView();
                final DialogResult result;
                setModifiableValueState(!isEditingForbidden());
                try {
                    if (view != null && view.hasUI()) {
                        result = dialog.execDialog(view.getParentWindow());
                    } else {
                        result = dialog.execDialog();
                    }
                } finally {
                    setModifiableValueState(true);
                }
                return owner.afterClosePropEditorDialog(this, (PropEditorModel) dialog.getModel(), result);
            }
        }
        final String msg = getEnvironment().getMessageProvider().translate("ExplorerError", "Custom dialog is not defined for this property");
        throw new ModelCreationError(ModelCreationError.ModelType.PROPERTY_EDITOR_DIALOG_MODEL, owner.getDefinition(), def, context, msg);
    }

    public DialogResult execPropEditorDialog(int arrIdx) {
        final IContext.PropEditorContext context = new IContext.PropEditorContext(this, arrIdx);
        if (def.customDialog()) {
            IPropEditorDialog dialog = def.getPropEditorDialog(getEnvironment());
            dialog.getModel().setContext(new IContext.PropEditorContext(this, arrIdx));
            owner.beforeOpenPropEditorDialog(this, (PropEditorModel) dialog.getModel());
            final IView view = getOwnerView();
            final DialogResult result;
            setModifiableValueState(!isEditingForbidden());
            try {
                if (view != null && view.hasUI()) {
                    result = dialog.execDialog(view.getParentWindow());
                } else {
                    result = dialog.execDialog();
                }
            } finally {
                setModifiableValueState(true);
            }
            return owner.afterClosePropEditorDialog(this, (PropEditorModel) dialog.getModel(), result);
        }
        final String msg = getEnvironment().getMessageProvider().translate("ExplorerException",
                "Custom dialog is not defined for this property");
        throw new ModelCreationError(ModelCreationError.ModelType.PROPERTY_EDITOR_DIALOG_MODEL, owner.getDefinition(), def, context, msg);
    }

    private IView getOwnerView() {
        if (owner.getView() != null) {
            return owner.getView();
        } else if (owner.getContext() instanceof IContext.SelectorRow) {
            final IContext.SelectorRow context = (IContext.SelectorRow) owner.getContext();
            return context.parentGroupModel.getView();
        }
        return null;
    }

    public EnumSet<ETextOptionsMarker> getTextOptionsMarkers() {
        EnumSet<ETextOptionsMarker> markers = EnumSet.noneOf(ETextOptionsMarker.class);
        if (isReadonly()) {
            markers.add(ETextOptionsMarker.READONLY_VALUE);
        }
        if (getValueObject() == null) {
            markers.add(ETextOptionsMarker.UNDEFINED_VALUE);
        }
        if (!hasValidMandatoryValue()) {
            markers.add(ETextOptionsMarker.MANDATORY_VALUE);
        }
        if (isUnacceptableInputRegistered()
            || getOwner().getPropertyValueState(getId()) != ValidationResult.ACCEPTABLE) {
            markers.add(ETextOptionsMarker.INVALID_VALUE);
        }
        if (getOwner() instanceof EntityModel
                && (getDefinition().isInheritable() && getInheritableValue() != null)) {
            markers.add(hasOwnValue() ? ETextOptionsMarker.OVERRIDDEN_VALUE : ETextOptionsMarker.INHERITED_VALUE);
        }
        if (inSelectorRow()){
            final IContext.SelectorRow context = (IContext.SelectorRow)owner.getContext();
            if (context.parentGroupModel.getSelection().isObjectSelected((EntityModel)owner)){
                markers.add(ETextOptionsMarker.CHOOSEN_OBJECT);
            }
        }
        return markers;
    }

    protected ITextOptions getTextOptions(final EnumSet<ETextOptionsMarker> markers, final ITextOptions defaultOptions) {
        return defaultOptions;
    }

    public final PropertyTextOptions getTitleTextOptions() {
        return titleTextOptions;
    }

    public final PropertyTextOptions getValueTextOptions() {
        return valueTextOptions;
    }
    
    public final boolean hasServerValue(){
        return serverValue!=null;
    }

    public final boolean isActivated() {//DBP-1624
        return !(internalValue instanceof NoValue);
    }        

    protected final boolean needForActivation() {
        if (!isActivated() && (owner instanceof EntityModel)) {
            final EntityModel entity = (EntityModel) owner;
            return !entity.isNew() || entity.wasRead();
        }
        return false;
    }
    private boolean wasRead;

    @SuppressWarnings("UseSpecificCatch")
    protected final void activate() {//DBP-1624
        if (serverValue != null || !(owner instanceof EntityModel)) {
            return;
        }
        final EntityModel entity = (EntityModel) owner;
        if (!entity.isExists()) {
            if (entity.isNew()) {
                throw new ActivatingPropertyError(entity, def);
            } else {
                throw new ActivatingPropertyError(entity, def, new ObjectNotFoundError(entity));
            }
        }
        if (wasRead || (entity.wasRead() && !def.isReadSeparately())) {
            throw new ActivatingPropertyError(entity, def);
        } else {
            wasRead = true;
            try {
                if (def.isReadSeparately()) {
                    final String info = getEnvironment().getMessageProvider().translate("ExplorerMessage", "Property %s was not activated in object of class %s (presentation %s) yet. Reading this property.");
                    getEnvironment().getTracer().put(EEventSeverity.DEBUG, String.format(info, def.toString(), entity.getClassPresentationDef().toString(),
                            entity.getEditorPresentationDef().toString()), EEventSource.EXPLORER);
                    entity.readProperty(getId());
                } else if (!entity.wasRead()) {
                    final String info = getEnvironment().getMessageProvider().translate("ExplorerMessage", "Property %s was not activated in object of class %s (presentation %s) yet. Reading this object.");
                    final EEventSeverity severity;
                    if (!entity.wasActivated() && entity.getContext() instanceof IContext.SelectorRow) {
                        //activation during afterRead event is potentially slow
                        severity = EEventSeverity.WARNING;
                    } else {
                        severity = EEventSeverity.DEBUG;
                    }
                    getEnvironment().getTracer().put(severity, String.format(info, def.toString(), entity.getClassPresentationDef().toString(), entity.getEditorPresentationDef().toString()), EEventSource.EXPLORER);
                    entity.activateAllProperties();
                }
            } catch (Exception e) {
                throw new ActivatingPropertyError(entity, def, e);
            }
            if (serverValue == null)//serverValue shoud be not null after activating
            {
                throw new ActivatingPropertyError(entity, def);
            }
        }
    }

    public ValidationResult validateValue() {
        return getEditMask().validate(getEnvironment(), getValueObject());
    }

    public boolean hasValidMandatoryValue() {//RADIX-4803
        return !isMandatory() || getValueObject() != null;
    }
    
    public final void registerUnacceptableInput(final UnacceptableInput input){
        if (input!=null && !input.equals(unacceptableInput)){
            unacceptableInput = input;            
            afterModify();
            if (synchronizedProp!=null){
                synchronizedProp.registerUnacceptableInput(input);
            }
        }
    }
    
    public final void removeUnacceptableInputRegistration(){
        if (unacceptableInput!=null){
            unacceptableInput = null;
            afterModify();
            if (synchronizedProp!=null){
                synchronizedProp.removeUnacceptableInputRegistration();
            }
        }
    }    
    
    public final boolean isUnacceptableInputRegistered(){
        return unacceptableInput!=null;
    }
    
    public final UnacceptableInput getUnacceptableInput(){
        return unacceptableInput;
    }
    
    protected final void checkForUnacceptableInput() throws UnacceptableInputError{
        if (isUnacceptableInputRegistered()){
            throw new UnacceptableInputError(this);
        }
    }

    public void onStartEditValue() {
        owner.onStartEditPropertyValue(this);
    }

    public void onFinishEditValue(final boolean valueWasAccepted) {
        owner.onFinishEditPropertyValue(this, valueWasAccepted);
    }
    
    public boolean valueMatchesToSearchString(final String displayString, final String searchString, final MatchOptions options){
        return options.matchToSearchString(displayString, searchString);
    }
}