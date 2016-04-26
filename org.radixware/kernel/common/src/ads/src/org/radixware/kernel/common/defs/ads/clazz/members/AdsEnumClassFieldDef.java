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

package org.radixware.kernel.common.defs.ads.clazz.members;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.defs.ClipboardSupport.CanPasteResult;
import org.radixware.kernel.common.defs.ClipboardSupport.DuplicationResolver;
import org.radixware.kernel.common.defs.ClipboardSupport.Transfer;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.*;
import org.radixware.kernel.common.defs.ads.AdsClipboardSupport;
import org.radixware.kernel.common.defs.ads.AdsDefinition.Hierarchy;
import org.radixware.kernel.common.defs.ads.AdsDefinitions;
import org.radixware.kernel.common.defs.ads.AdsValAsStr;
import org.radixware.kernel.common.defs.ads.clazz.*;
import org.radixware.kernel.common.defs.ads.clazz.enumeration.*;
import org.radixware.kernel.common.defs.ads.src.IJavaSource;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.agents.IObjectAgent;
import org.radixware.schemas.adsdef.ClassDefinition;
import org.radixware.schemas.adsdef.EnumClassFieldDefinition;
import org.radixware.schemas.adsdef.EnumClassFieldValue;


public final class AdsEnumClassFieldDef extends AdsClassMember implements IAdsEnumClassElement, IJavaSource, IOverwritable, IClassInclusive {

    public static final class Factory {

        public static AdsEnumClassFieldDef newInstanse() {
            return newInstanse("newField");
        }

        public static AdsEnumClassFieldDef newInstanse(String name) {
            return new AdsEnumClassFieldDef(Id.Factory.newInstance(EDefinitionIdPrefix.ADS_ENUM_CLASS_FIELD), name);
        }

        public static AdsEnumClassFieldDef copyFrom(AdsEnumClassFieldDef source) {
            if (source == null) {
                return null;
            }
            AdsEnumClassFieldDef newField = source.getClipboardSupport().duplicate();
            return newField;
        }

        public static AdsEnumClassFieldDef loadFrom(EnumClassFieldDefinition xField) {
            if (xField == null) {
                return null;
            }
            return new AdsEnumClassFieldDef(xField);
        }

        public static AdsEnumClassFieldDef newTemporaryInstance(AdsEnumClassDef context, String name) {
            AdsEnumClassFieldDef temp = newInstanse(name);
            temp.setContainer(context);
            return temp;
        }

        public static AdsEnumClassFieldDef newTemporaryInstanceFrom(AdsEnumClassDef context, AdsEnumClassFieldDef source) {
            if (source == null) {
                return null;
            }
            AdsEnumClassFieldDef temp = copyFrom(source);
            temp.setContainer(context);
            return temp;
        }
    }

    public static final class FieldValueController implements AdsValAsStr.IValueController {

        private AdsFieldParameterDef param;
        private AdsEnumClassFieldDef field;

        public FieldValueController(AdsFieldParameterDef param, AdsEnumClassFieldDef field) {
            assert param != null && field != null;

            this.param = param;
            this.field = field;
        }

        public AdsFieldParameterDef getContextParameter() {
            return param;
        }

        @Override
        public boolean isValueTypeAvailable(AdsValAsStr.EValueType type) {
            return param.getValue().getValueController().isValueTypeAvailable(type);
        }

        @Override
        public AdsTypeDeclaration getContextType() {
            return param.getValue().getType();
        }

        @Override
        public AdsEnumClassFieldDef getContextDefinition() {
            return field;
        }

        @Override
        public void setValue(AdsValAsStr value) {
            field.setValueByParam(param, value);
        }

        @Override
        public AdsValAsStr getValue() {
            return field.getValueByParam(param);
        }

        @Override
        public String getValuePresentation() {
            return AdsValAsStr.DefaultPresenter.getAsString(this);
        }
    }
    private final Hierarchy<AdsEnumClassFieldDef> hierarchy = new MemberHierarchy<AdsEnumClassFieldDef>(this, true) {

        @Override
        protected AdsEnumClassFieldDef findMember(AdsClassDef clazz, Id id, EScope scope) {
            if (clazz instanceof AdsEnumClassDef) {
                return ((AdsEnumClassDef) clazz).getFields().findById(id, scope).get();
            }
            return null;
        }
    };
    private final Map<Id, AdsValAsStr> values;
    private EnumFieldEmbeddedClass embeddedClass;
    private final Object embeddedClassLock = new Object();

    private final IObjectAgent<AdsEmbeddedClassDef> agent = new AdsEmbeddedClassDef.ClassAgent<AdsEmbeddedClassDef>(this) {

        @Override
        protected AdsEmbeddedClassDef createTemporary() {
            return EnumFieldEmbeddedClass.Factory.newTemporaryInstance(AdsEnumClassFieldDef.this);
        }

        @Override
        public AdsEmbeddedClassDef getObject() {
            return getObjectSource();
        }

        @Override
        public boolean isActual() {
            return getLocalEmbeddedClass(false) != null;
        }
    };

    protected AdsEnumClassFieldDef(Id id, String name) {
        super(id, name);

        values = new HashMap<>();
        embeddedClass = null;
    }

    protected AdsEnumClassFieldDef(EnumClassFieldDefinition xField) {
        super(xField);

        final List<EnumClassFieldValue> vals = xField.getValues().getValueList();
        values = new HashMap<>(vals.size());
        for (final EnumClassFieldValue val : vals) {
            values.put(Id.Factory.loadFrom(val.getParamId()), AdsValAsStr.Factory.loadFrom(this, val.getValue()));
        }

        final ClassDefinition xCLass = xField.getEmbeddedClass();
        if (xCLass != null) {
            embeddedClass = EnumFieldEmbeddedClass.Factory.newInstance(this, xCLass);
        } else {
            embeddedClass = null;
        }
    }

    @Override
    public AdsEnumClassDef getOwnerEnumClass() {
        AdsClassDef clazz = getOwnerClass();
        if (clazz instanceof AdsEnumClassDef) {
            return (AdsEnumClassDef) clazz;
        }
        return null;
    }

    @Override
    public EDefType getDefinitionType() {
        return EDefType.ENUM_CLASS_FIELD;
    }

    @Override
    public RadixIcon getIcon() {
        return RadixObjectIcon.ENUM_ITEM;
    }

    @Override
    public JavaSourceSupport getJavaSourceSupport() {
        return new JavaSourceSupport(this) {

            @Override
            public CodeWriter getCodeWriter(UsagePurpose purpose) {
                return new AdsEnumClassFieldWriter(this, AdsEnumClassFieldDef.this, purpose);
            }
        };
    }

    @Override
    public void afterOverwrite() {
        values.clear();

        synchronized (embeddedClassLock) {
            // workaround
            if (embeddedClass != null) {
                embeddedClass.afterOverwrite();
                embeddedClass = null;
            }
        }
    }

    @Override
    public boolean allowOverwrite() {
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Hierarchy<AdsEnumClassFieldDef> getHierarchy() {
        return hierarchy;
    }

    @Override
    public ClipboardSupport<? extends AdsEnumClassFieldDef> getClipboardSupport() {
        return new AdsClipboardSupport<AdsEnumClassFieldDef>(this) {

            @Override
            protected XmlObject copyToXml() {
                final EnumClassFieldDefinition xDef = ClassDefinition.EnumClassFields.Factory.newInstance().addNewField();
                AdsEnumClassFieldDef.this.appendTo(xDef, ESaveMode.NORMAL);
                return xDef;
            }

            @Override
            protected AdsEnumClassFieldDef loadFrom(final XmlObject xmlObject) {
                if (xmlObject instanceof EnumClassFieldDefinition) {
                    return Factory.loadFrom((EnumClassFieldDefinition) xmlObject);
                }
                return super.loadFrom(xmlObject);
            }

            @Override
            public CanPasteResult canPaste(List<Transfer> transfers, DuplicationResolver resolver) {
                CanPasteResult canPaste = canPasteInFields(transfers, resolver);

                if (canPaste == CanPasteResult.NO) {
                    canPaste = getEmbeddedClassAgent().getObject().getClipboardSupport().canPaste(transfers, resolver);
                }

                return canPaste;
            }

            @Override
            public void paste(List<Transfer> transfers, DuplicationResolver resolver) {
                if (canPasteInFields(transfers, resolver) != CanPasteResult.NO) {
                    super.paste(transfers, resolver);
                }

                if (getEmbeddedClassAgent().invite(true)) {
                    getEmbeddedClassAgent().getObject().getClipboardSupport().paste(transfers, resolver);
                }
            }

            private CanPasteResult canPasteInFields(List<Transfer> transfers, DuplicationResolver resolver) {
                return super.canPaste(transfers, resolver);
            }
        };
    }

    @Override
    public boolean isSuitableContainer(final AdsDefinitions collection) {
        return collection instanceof AdsFields.LocalFields;
    }

    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        super.visitChildren(visitor, provider);
        synchronized (embeddedClassLock) {
            if (embeddedClass != null) {
                embeddedClass.visit(visitor, provider);
            }
        }

        for (final AdsValAsStr val : values.values()) {
            if (val != null) {
                val.visit(visitor, provider);
            }
        }
    }

    @Override
    public SearchResult<AdsClassDef> findEmbeddedClass(EScope scope) {

        if (embeddedClass != null) {
            return SearchResult.single((AdsClassDef) embeddedClass);
        }

        if (scope != EScope.LOCAL) {
            final List<AdsClassDef> searchInLevel = AdsEmbeddedClassDef.searchInLevel(this.getHierarchy().findOverwritten().all());

            if (!searchInLevel.isEmpty()) {
                return SearchResult.list(searchInLevel);
            }
        }
        return SearchResult.<AdsClassDef>empty();
    }

    @Override
    public AdsEmbeddedClassDef getLocalEmbeddedClass(boolean create) {

        synchronized (embeddedClassLock) {
            if (embeddedClass == null && create) {

                final AdsClassDef overEmbedded = findEmbeddedClass(EScope.LOCAL_AND_OVERWRITE).get();

                if (overEmbedded != null) {
                    embeddedClass = EnumFieldEmbeddedClass.Factory.newInstance(this, overEmbedded.getId());
                    embeddedClass.setOverwrite(true);
                } else {
                    embeddedClass = EnumFieldEmbeddedClass.Factory.newInstance(this);
                }
                setEditState(EEditState.MODIFIED);
            }
            return embeddedClass;
        }
    }

    @Override
    public IObjectAgent<AdsEmbeddedClassDef> getEmbeddedClassAgent() {
        return agent;
    }

    MemberHierarchyIterator<AdsEnumClassFieldDef> getHierarchyIterator(EScope scope, HierarchyIterator.Mode mode) {
        return new MemberHierarchyIterator<AdsEnumClassFieldDef>(this, scope, mode) {

            @Override
            public AdsEnumClassFieldDef findInClass(AdsClassDef clazz) {
                return ((AdsEnumClassDef) clazz).getFields().findById(getId(), EScope.LOCAL).get();
            }
        };
    }

    public boolean containsLocal(Id id) {
        return values.containsKey(id);
    }

    public Map<Id, AdsValAsStr> getLocalView() {
        return new HashMap<>(values);
    }

    public AdsValAsStr.IValueController getValueController(AdsFieldParameterDef param) {
        if (param == null) {
            return null;
        }
        return new FieldValueController(param, this);
    }

    public AdsValAsStr getValueByParam(AdsFieldParameterDef param) {

        if (param == null) {
            return null;
        }

        AdsEnumClassFieldDef current = this;

        while (current != null && !current.values.containsKey(param.getId())) {
            current = current.getHierarchy().findOverwritten().get();
        }
        if (current != null) {
            return current.values.get(param.getId());
        }
        return param.getValue().getInitialValue();
    }

    public boolean setValueByParam(AdsFieldParameterDef param, AdsValAsStr value) {
        if (value == null || param == null) {
            return false;
        }

        return setValueByParamId(param.getId(), value);
    }

    private boolean setValueByParamId(Id id, AdsValAsStr value) {
        if (value == null) {
            return false;
        }

        if (!value.equals(values.get(id))) {
            values.put(id, value);
            setEditState(EEditState.MODIFIED);
        }
        return true;
    }

    public AdsValAsStr removeByParam(AdsFieldParameterDef param) {
        if (param == null) {
            return null;
        }
        return removeByParamId(param.getId());
    }

    private AdsValAsStr removeByParamId(Id id) {
        if (values.containsKey(id)) {
            AdsValAsStr value = values.remove(id);
            setEditState(EEditState.MODIFIED);
            return value;
        }
        return null;
    }

    private void appendValueTo(EnumClassFieldValue xVal, Id id, AdsValAsStr val) {
        xVal.setParamId(id.toString());
        val.appendTo(xVal.addNewValue());
    }

    public void appendTo(EnumClassFieldDefinition xField, ESaveMode saveMode) {
        super.appendTo(xField, saveMode);

        final EnumClassFieldDefinition.Values vals = xField.addNewValues();
        final Id[] keys = values.keySet().toArray(new Id[0]);
        Arrays.sort(keys);

        for (final Id id : keys) {
            appendValueTo(vals.addNewValue(), id, values.get(id));
        }

        synchronized (embeddedClassLock) {
            if (embeddedClass != null && embeddedClass.isUsed()) {
                ClassDefinition xClass = xField.addNewEmbeddedClass();
                embeddedClass.appendTo(xClass, saveMode);
            }
        }
    }
}