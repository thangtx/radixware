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
package org.radixware.kernel.common.msdl;

import java.util.List;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.check.RadixProblem;
import org.radixware.kernel.common.defs.ClipboardSupport;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.msdl.fields.AbstractFieldModel;
import org.radixware.kernel.common.msdl.fields.ISchemeSearcher;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.events.IRadixEventListener;
import org.radixware.kernel.common.utils.events.RadixEvent;
import org.radixware.kernel.common.utils.events.RadixEventSource;
import org.radixware.schemas.msdl.*;

public class MsdlField extends RadixObject {

    private AnyField anyField = null;
    private AbstractFieldModel model;
    private MsdlFieldStructureChangedSupport changeStructureSupport;
    private ISchemeSearcher searcher = null;
    private boolean fetchedFromTemplate = false;
    private int position = -1;
    
    public void setSchemeSearcher(ISchemeSearcher searcher) {
        this.searcher = searcher;
    }

    public ISchemeSearcher getSchemeSearcher() {
        return searcher;
    }

    public boolean isFetchedFromTemplate() {
        return fetchedFromTemplate;
    }

    public void setFetchedFromTemplate(boolean value) {
        fetchedFromTemplate = value;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int value) {
        position = value;
    }

    public MsdlField(Field field) {
        super(field.getName());
        create(field);
    }

    public MsdlField(AnyField f) {
        this(EFieldType.getField(f));
        anyField = (AnyField) f.copy();
        clearAnyField(anyField);
    }

    public MsdlField(Structure.Field f) {
        this(EFieldType.getField(f));
        anyField = (AnyField) f.copy();
        clearAnyField(anyField);
    }

    private void create(Field field) {
        model = AbstractFieldModel.Factory.newInstance(this, EFieldType.getFieldType(field), field);
        //setName(field.getName());
    }
    
    public AnyField getFullField() {
        AnyField res = (AnyField) anyField.copy();
        fillAnyField(res, model.getFullField());
        return res;
    }

    @Override
    public void collectDependences(List<Definition> list) {
        Definition enumeration = findReferencedEnum();
        if (enumeration != null) {
            list.add(enumeration);
        }
    }

    public Id getReferncedEnumId() {
        Id enumId = null;
        if (getModel() == null) {
            return null;
        }
        Field field = getModel().getField();
        if (field instanceof IntField && ((IntField) field).getEnumClassId() != null) {
            enumId = Id.Factory.loadFrom(((IntField) field).getEnumClassId());
        } else if (field instanceof StrField && ((StrField) field).getEnumClassId() != null) {
            enumId = Id.Factory.loadFrom(((StrField) field).getEnumClassId());
        }
        return enumId;
    }
    
    public Definition findDefinition(final Id defId) {
        if (defId != null) {
            final IAdsDefinitionLookup lookup = findAdsDefinitionLookup();
            return lookup != null ? lookup.findDefinition(defId) : null;
        }
        return null;
    }

    public Definition findReferencedEnum() {
        Id enumId = getReferncedEnumId();

        if (enumId != null) {
            return findEnum(enumId);
        }
        return null;
    }

    private Definition findEnum(Id id) {
        IEnumLookuper lookuper = findEnumLookuper();
        return lookuper == null ? null : lookuper.findEnumDef(id);
    }

    private IEnumLookuper findEnumLookuper() {
        for (RadixObject container = getContainer(); container != null; container = container.getContainer()) {
            if (container instanceof IEnumLookuper) {
                return (IEnumLookuper) container;
            }
        }
        return null;
    }
    
    private IAdsDefinitionLookup findAdsDefinitionLookup() {
        for (RadixObject container = getContainer(); container != null; container = container.getContainer()) {
            if (container instanceof IAdsDefinitionLookup) {
                return (IAdsDefinitionLookup) container;
            }
        }
        return null;
    }

    private void clearAnyField(AnyField anyField) {
        if (anyField.isSetBCH()) {
            anyField.unsetBCH();
        }
        if (anyField.isSetBin()) {
            anyField.unsetBin();
        }
        if (anyField.isSetBoolean()) {
            anyField.unsetBoolean();
        }
        if (anyField.isSetChoice()) {
            anyField.unsetChoice();
        }
        if (anyField.isSetDateTime()) {
            anyField.unsetDateTime();
        }
        if (anyField.isSetInt()) {
            anyField.unsetInt();
        }
        if (anyField.isSetNum()) {
            anyField.unsetNum();
        }
        if (anyField.isSetSequence()) {
            anyField.unsetSequence();
        }
        if (anyField.isSetStr()) {
            anyField.unsetStr();
        }
        if (anyField.isSetStructure()) {
            anyField.unsetStructure();
        }
    }

    protected void fillAnyField(AnyField anyField, Field field) {
        if (model == null || model.getType() == null) {
            return;
        }
        switch (model.getType()) {
            case BCH:
                anyField.addNewBCH().set(field);
                break;
            case BIN:
                anyField.addNewBin().set(field);
                break;
            case CHOICE:
                anyField.addNewChoice().set(field);
                break;
            case DATETIME:
                anyField.addNewDateTime().set(field);
                break;
            case INT:
                anyField.addNewInt().set(field);
                break;
            case NUM:
                anyField.addNewNum().set(field);
                break;
            case SEQUENCE:
                anyField.addNewSequence().set(field);
                break;
            case STR:
                anyField.addNewStr().set(field);
                break;
            case STRUCTURE:
                anyField.addNewStructure().set(field);
                break;
            case BOOLEAN:
                anyField.addNewBoolean().set(field);
                break;
            default:
                break;
        }
    }

    public AbstractFieldModel getModel() {
        return model;
    }

    public boolean hasChildren() {
        return getType() == EFieldType.CHOICE || getType() == EFieldType.SEQUENCE || getType() == EFieldType.STRUCTURE;
    }

    public MsdlField getParentMsdlField() {
        RadixObject res = getContainer();
        if (res == null) {
            return null;
        }
        while (!(res instanceof MsdlField)) {
            res = res.getContainer();
        }
        return (MsdlField) res;
    }

    public EFieldType getType() {
        if (model == null) {
            return EFieldType.NONE;
        } else {
            return model.getType();
        }
    }

    public void setType(EFieldType type) {
        if (model == null) {
            return;
        }
        if (type == null) {
            return;
        }
        if (model != null && model.getType() == type) {
            return;
        }

        Field newType = null;
        switch (type) {
            case BCH:
                newType = BCHField.Factory.newInstance();
                break;
            case BIN:
                newType = BinField.Factory.newInstance();
                break;
            case CHOICE:
                newType = ChoiceField.Factory.newInstance();
                ChoiceField choiceField = (ChoiceField) newType;
                StrField sField = choiceField.addNewSelector();
                sField.setName("dummy");
                sField.setIsRequired(true);
                break;
            case DATETIME:
                newType = DateTimeField.Factory.newInstance();
                break;
            case INT:
                newType = IntField.Factory.newInstance();
                break;
            case NUM:
                newType = NumField.Factory.newInstance();
                break;
            case SEQUENCE:
                newType = SequenceField.Factory.newInstance();
                SequenceField sequenceField = (SequenceField) newType;
                AnyField field = sequenceField.addNewItem();
                StrField strField = field.addNewStr();
                strField.setName("Item");
                strField.setIsRequired(true);
                break;
            case STR:
                newType = StrField.Factory.newInstance();
                break;
            case STRUCTURE:
                newType = StructureField.Factory.newInstance();
                StructureField structure = (StructureField) newType;
                structure.addNewStructure();
                break;
            case BOOLEAN:
                newType = BooleanField.Factory.newInstance();
                break;
            default:
                break;
        }
        if (newType == null) {
            return;
        }
        final Field oldField = model.getField();
        if (oldField == null) {
            return;
        }
        newType.setName(oldField.getName());
        if (oldField.isSetComment()) {
            newType.addNewComment().set(oldField.getComment());
        }
        if (oldField.isSetDevComment()) {
            newType.addNewDevComment().set(oldField.getDevComment());
        }
        newType.setIsRequired(oldField.getIsRequired());
        if (oldField.isSetMergeFunctionName()) {
            newType.setMergeFunctionName(oldField.getMergeFunctionName());
        }
        if (oldField.isSetParseFunctionName()) {
            newType.setParseFunctionName(oldField.getParseFunctionName());
        }
        if (oldField instanceof SimpleField && newType instanceof SimpleField) {
            SimpleField sf = (SimpleField) oldField;
            if (sf.isSetIsNilable()) {
                ((SimpleField) newType).setIsNilable(sf.getIsNilable());
            }
        }
        if (oldField instanceof ChoiceField && newType instanceof ChoiceField
                && ((ChoiceField) oldField).isSetSelectorAdvisorFunctionName()) {
            ((ChoiceField) newType).setSelectorAdvisorFunctionName(((ChoiceField) oldField).getSelectorAdvisorFunctionName());
        }
        AbstractFieldModel newModel = AbstractFieldModel.Factory.newInstance(this, type, newType);
        clearAnyField(anyField);
        model.delete();
        model = newModel;
        getStructureChangedSupport().fireEvent(new MsdlFieldStructureChangedEvent(MsdlFieldStructureChangedEvent.EType.TYPE_CHANGED));
        setModified();
    }

    @Override
    public boolean setName(String name) {
        return super.setName(name) && model.setName(name);
    }

    public AbstractFieldModel getFieldModel() {
        return model;
    }

    @Override
    public String getDescription() {
        Field f = model.getField();
        if (f != null && f.isSetComment()) {
            return f.getComment().newCursor().getTextValue();
        }
        return "";
    }

    public static class MsdlFieldStructureChangedEvent extends RadixEvent {

        private final EType type;

        public MsdlFieldStructureChangedEvent() {
            type = EType.PROP_CHANGED;
        }

        public MsdlFieldStructureChangedEvent(EType type) {
            this.type = type;
        }

        public static enum EType {

            TYPE_CHANGED, PROP_CHANGED, NAME_ONLY
        }

        public EType getType() {
            return type;
        }

    }

    public interface MsdlFieldStructureChangedListener extends IRadixEventListener<MsdlFieldStructureChangedEvent> {
    }

    public static class MsdlFieldStructureChangedSupport extends RadixEventSource<MsdlFieldStructureChangedListener, MsdlFieldStructureChangedEvent> {
    }

    public MsdlFieldStructureChangedSupport getStructureChangedSupport() {
        if (changeStructureSupport == null) {
            changeStructureSupport = new MsdlFieldStructureChangedSupport();
        }
        return changeStructureSupport;
    }

    public void bitmapSetChanged() {
        getStructureChangedSupport().fireEvent(new MsdlFieldStructureChangedEvent());
    }

    private class MsdlFieldModelClipboardSupport extends ClipboardSupport<MsdlField> {

        public MsdlFieldModelClipboardSupport() {
            super(MsdlField.this);
        }

        @Override
        public XmlObject copyToXml() {
            return getFullField().copy();
        }

        @Override
        public MsdlField loadFrom(XmlObject xmlObject) {
            return new MsdlField((AnyField) xmlObject);
        }
    }

    @Override
    public ClipboardSupport<? extends MsdlField> getClipboardSupport() {
        return new MsdlFieldModelClipboardSupport();
    }

    @Override
    public RadixIcon getIcon() {
        return MsdlIcon.MSDL_SCHEME_FIELD;
    }

    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        super.visitChildren(visitor, provider);
        model.visit(visitor, provider);
    }

    public void check(IProblemHandler handler) {
        getFieldModel().getParser().check(this, handler);
        if (getFieldModel().getField().isSetTemplateFieldPath()) {
            checkFieldTemplate(handler);
        }
    }

    private void checkFieldTemplate(IProblemHandler handler) {
        if (searcher == null) {
            return;
        }
        try {
            String templateFieldPath = getFieldModel().getField().getTemplateFieldPath();
            String templateSchemeId = null;
            if (getFieldModel().getField().isSetTemplateSchemeId()) {
                templateSchemeId = getFieldModel().getField().getTemplateSchemeId();
            }
            Id schemeId = Id.Factory.loadFrom(templateSchemeId);
            AbstractFieldModel afm = searcher.findField(schemeId, templateFieldPath, getType());
            if (afm == null) {
                handler.accept(RadixProblem.Factory.newError(this, "Could not find template specified"));
            }
        } catch (Exception e) {
            handler.accept(RadixProblem.Factory.newError(this, "Exception has occured while checking MSDL field template: " + e.toString()));
        }
    }

    public RootMsdlScheme getRootMsdlScheme() {
        RadixObject cur = this;
        while (!(cur instanceof RootMsdlScheme)) {
            cur = cur.getContainer();
        }
        return (RootMsdlScheme) cur;
    }

    @Override
    protected void appendAdditionalToolTip(StringBuilder sb) {
        super.appendAdditionalToolTip(sb);
        sb.append("\nNamespace: '").append(getRootMsdlScheme().getNamespace()).append('\'');
    }

    public String getTreeItemName() {
        return getName() + " (" + getType().toString() + ")";
    }

    @Override
    protected boolean isQualifiedNamePart() {
        return true;
    }
    
    public void setModified() {
        setModified(false);
    }

    public void setModified(boolean fireChangeEvent) {
        setEditState(EEditState.MODIFIED);
        visitChildren(new IVisitor() {
            @Override
            public void accept(RadixObject radixObject) {
                if (radixObject instanceof MsdlField) {
                    MsdlField mf = (MsdlField) radixObject;
                    mf.getFieldModel().clearParser();
                }
                if (radixObject instanceof AbstractFieldModel) {
                    AbstractFieldModel afm = (AbstractFieldModel) radixObject;
                    afm.clearParser();
                }
            }
        }, new VisitorProvider() {
            @Override
            public boolean isTarget(RadixObject radixObject) {
                return true;
            }
        });
        if(fireChangeEvent) {
            getStructureChangedSupport().fireEvent(new MsdlFieldStructureChangedEvent());
        }
    }

    @Override
    public void setContainer(RadixObject container) {
        super.setContainer(container);
    }
}
