/*
* Copyright (c) 2008-2016, Compass Plus Limited. All rights reserved.
*
* This Source Code Form is subject to the terms of the Mozilla Public
* License, v. 2.0. If a copy of the MPL was not distributed with this
* file, You can obtain one at http://mozilla.org/MPL/2.0/.
* This Source Code is distributed WITHOUT ANY WARRANTY; including any 
* implied warranties but not limited to warranty of MERCHANTABILITY 
* or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
* License, v. 2.0. for more details.
*/

package org.radixware.kernel.common.client.meta.sqml.defs;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.mask.EditMask;
import org.radixware.kernel.common.client.meta.mask.EditMaskDateTime;
import org.radixware.kernel.common.client.meta.mask.EditMaskInt;
import org.radixware.kernel.common.client.meta.mask.EditMaskNum;
import org.radixware.kernel.common.client.meta.mask.EditMaskStr;
import org.radixware.kernel.common.client.meta.sqml.ISqmlColumnDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlSelectorPresentationDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableIndexDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.editmask.EditMaskTimeInterval;
import org.radixware.kernel.common.types.Id;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.DefaultHandler;


final class SqmlSaxHandler extends DefaultHandler{
    
    private static enum ESqmlItemType {MODULE,ENUM,ENUM_ITEM,DOMAIN,PACKAGE,FUNCTION,FUNCTION_PARAM,
        TABLE,DET_REFERENCE,EDIT_MASK,PROPERTY,CLASS};
    
    private static abstract class ParserState{
        
        private final ESqmlItemType itemType;
        private final Attributes attributes;
        private Id definitionId;
        
        protected ParserState(final ESqmlItemType type, final Attributes attr){
            itemType = type;
            attributes = attr;
        }
        
        public final ESqmlItemType getSqmlItemType(){
            return itemType;
        }
        
        public final Attributes getAttributes(){
            return attributes;
        }
        
        public final Id getDefinitionId(){
            if (definitionId==null){
                definitionId = Id.Factory.loadFrom(attributes.getValue("Id"));
            }
            return definitionId;
        }
    }
    
    private static abstract class ItemWithValueParserState extends ParserState{
        
        private final StringBuilder valStrBuilder = new StringBuilder();
        
        public ItemWithValueParserState(final ESqmlItemType type, final Attributes attr){
            super(type,new AttributesImpl(attr));
        }
        
        public void appendValueChars(final char[] ch, final int start, final int length){
            valStrBuilder.append(ch, start, length);
        }
        
        protected final String getValueString(){
            return valStrBuilder.toString();
        }
    }
    
    private final static class ValueParserState extends ItemWithValueParserState{
        
        private final ItemWithValueParserState parentState;
        
        public ValueParserState(final ItemWithValueParserState parentState){
            super(parentState.getSqmlItemType(), parentState.getAttributes());
            this.parentState = parentState;
        }

        @Override
        public void appendValueChars(char[] ch, int start, int length) {
            parentState.appendValueChars(ch, start, length);
        }                
    }
        
    private final static class EnumParserState extends ParserState{
        
        private final List<SqmlEnumItemImpl> enumItems = new LinkedList<>();
        
        public EnumParserState(final Attributes attr){
            super(ESqmlItemType.ENUM,new AttributesImpl(attr));
        }
        
        public void addEnumItem(final SqmlEnumItemImpl item){
            enumItems.add(item);
        }
        
        public SqmlEnumDefImpl createSqmlDef(final SqmlModule module){
            return new SqmlEnumDefImpl(module, getAttributes(), enumItems);
        }
    }
    
    private final static class EnumItemParserState extends ItemWithValueParserState{
        
        public EnumItemParserState(final Attributes attributes){
            super(ESqmlItemType.ENUM_ITEM,attributes);
        }                
        
        public SqmlEnumItemImpl createSqmlDef(final SqmlModule module, final Attributes enumAttrs){
            return new SqmlEnumItemImpl(module, getAttributes(), getValueString(), enumAttrs);
        }
    }
    
    private final static class DomainParserState extends ParserState{
        
        private final List<SqmlDomainDefImpl> subDomains = new LinkedList<>();
        
        public DomainParserState(final Attributes attributes){
            super(ESqmlItemType.DOMAIN, new AttributesImpl(attributes));
        }
        
        public void addSubDomain(SqmlDomainDefImpl domainDef){
            subDomains.add(domainDef);
        }
        
        public SqmlDomainDefImpl createSqmlDef(final SqmlModule module){
            return new SqmlDomainDefImpl(module, getAttributes(), subDomains);
        }
    }
    
    private final static class PackageParserState extends ParserState{
        
        private final List<SqmlFunctionDefImpl> functions = new LinkedList<>();
        
        public PackageParserState(final Attributes attributes){
            super(ESqmlItemType.PACKAGE,new AttributesImpl(attributes));
        }
        
        public void addFunction(final SqmlFunctionDefImpl functionDef){
            functions.add(functionDef);
        }
        
        public SqmlPackageDefImpl createSqmlDef(final SqmlModule module){
            return new SqmlPackageDefImpl(module, getAttributes(), functions);
        }
    }
    
    private final static class FunctionParserState extends ParserState{
        
        private final List<SqmlFunctionParameterImpl> parameters = new LinkedList<>();
        private final Id packageId;
        
        public FunctionParserState(final Attributes attributes, final Id packageId){
            super(ESqmlItemType.FUNCTION, new AttributesImpl(attributes));
            this.packageId = packageId;
        }
        
        public Id getPackageId(){
            return packageId;
        }
        
        public void addParameter(final SqmlFunctionParameterImpl parameter){
            parameters.add(parameter);
        }
        
        public SqmlFunctionDefImpl createSqmlDef(final SqmlModule module){
            return new SqmlFunctionDefImpl(module, getAttributes(), parameters);
        }
    }

    private final static class ParameterParserState extends ItemWithValueParserState{
        
        private final Id packageId;
        private final Id functionId;
        
        public ParameterParserState(final Attributes attributes, final Id functionId, final Id packageId){
            super(ESqmlItemType.FUNCTION_PARAM,attributes);
            this.packageId = packageId;
            this.functionId = functionId;
        }
      
        public SqmlFunctionParameterImpl createSqmlDef(final SqmlModule module){
            return new SqmlFunctionParameterImpl(module, getAttributes(), getValueString(), functionId, packageId);
        }
    }        
    
    private final static class TableParserState extends ParserState{
        
        public static enum ETableItems{COLUMNS,OUTGOING_REFS,INDICES};
        
        private final List<ISqmlColumnDef> columns = new LinkedList<>();        
        private final List<SqmlOutgoingTableReferenceImpl> outguingRefs = new LinkedList<>();
        private final List<SqmlTableIndexImpl> indices = new LinkedList<>();
        private Id masterReferenceId;
        
        private ETableItems currentItems;
        
        public TableParserState(final Attributes attributes){
            super(ESqmlItemType.TABLE,new AttributesImpl(attributes));
        }
        
        public void addColumn(final SqmlTableColumnDef column){
            columns.add(column);
        }
        
        public void addOutgoingRef(final SqmlOutgoingTableReferenceImpl ref){
            outguingRefs.add(ref);
        }
        
        public void addIndex(final SqmlTableIndexImpl index){
            indices.add(index);
        }
        
        public void setCurrentItems(final ETableItems items){
            currentItems = items;
        }

        public void setMasterReferenceId(final Id masterReferenceId) {
            this.masterReferenceId = masterReferenceId;
        }
        
        public Id getMasterTableId(){
            final int masterTableIdIndex = getAttributes().getIndex("MasterTableId");
            return masterTableIdIndex>-1 ? Id.Factory.loadFrom(getAttributes().getValue(masterTableIdIndex)) : null;
        }
        
        public ETableItems getCurrentItems(){
            return currentItems;
        }
        
        public SqmlTableDef createSqmlDef(final SqmlModule module, final SqmlRepository repository){
            final SqmlTableColumns tableColumns = new SqmlTableColumns(columns);
            final SqmlTableIndices tableIndices = new SqmlTableIndices(indices);
            return new SqmlTableDef(module, getAttributes(), tableIndices, masterReferenceId, outguingRefs, tableColumns, repository);
        }
    }
    
    private final static class DetailReferenceParserState extends ParserState{
        
        private final Id ownerTableId;
        
        public DetailReferenceParserState(final Attributes attributes, final Id ownerTableId){
            super(ESqmlItemType.DET_REFERENCE, attributes);
            this.ownerTableId = ownerTableId;
        }
        
        public SqmlDetailTableReferenceImpl createSqmlDef(final SqmlModule module){
            return new SqmlDetailTableReferenceImpl(module, getAttributes(), ownerTableId);
        }
        
    }
    
    private final static class EditMaskParserState extends ParserState{
        
        private EditMask editMask;
    
        public EditMaskParserState(final Attributes attributes){
            super(ESqmlItemType.EDIT_MASK,attributes);
        }
    
        public EditMask getResultEditMask(){
            return editMask;
        }
        
        public void parseEditMask(final String maskType, final Attributes attributes){
            switch(maskType){
                case "Int":{
                    final int maxValueIndex = attributes.getIndex("DbMaxValue");
                    final EditMaskInt maskInt = new EditMaskInt();
                    if (maxValueIndex>-1){
                        final Long maxValue = Long.parseLong(attributes.getValue(maxValueIndex));
                        maskInt.setMaxValue(maxValue);
                    }
                    editMask = maskInt;
                    break;
                }case "Num":{                    
                    final EditMaskNum maskNum = new EditMaskNum();
                    final int maxValIndex = attributes.getIndex("DbMaxValue");
                    if (maxValIndex>-1){
                        final BigDecimal maxVal = new BigDecimal(attributes.getValue(maxValIndex));                        
                        maskNum.setMaxValue(maxVal);                    
                    }
                    final int precisionIndex = attributes.getIndex("Precision");
                    if (precisionIndex>-1){
                        final Integer precision = Integer.parseInt(attributes.getValue(precisionIndex));
                        maskNum.setPrecision(precision);
                    }
                    editMask = maskNum;
                    break;
                }case "Str":{
                    final EditMaskStr maskStr = new EditMaskStr();
                    final int maxLengthIndex = attributes.getIndex("DbMaxLen");
                    if (maxLengthIndex>-1){
                        final Integer maxLength = Integer.parseInt(attributes.getValue(maxLengthIndex));
                        maskStr.setMaxLength(maxLength);
                    }
                    editMask = maskStr;
                    break;
                }case "TimeInterval": {
                    final String scaleName = attributes.getValue("Scale");
                    final EditMaskTimeInterval.Scale scale = EditMaskTimeInterval.Scale.valueOf(scaleName);
                    final org.radixware.kernel.common.client.meta.mask.EditMaskTimeInterval editMaskTimeInterval =
                        new org.radixware.kernel.common.client.meta.mask.EditMaskTimeInterval(scale);
                    final int inputMaskIndex = attributes.getIndex("Mask");
                    if (inputMaskIndex>-1){
                        editMaskTimeInterval.setDisplayFormat(attributes.getValue(inputMaskIndex));
                    }
                    editMask = editMaskTimeInterval;
                    break;
                }case "DateTime":{
                    final EditMaskDateTime editMaskDateTime = new EditMaskDateTime();
                    final int inputMaskIndex = attributes.getIndex("Mask");
                    if (inputMaskIndex>-1){
                        editMaskDateTime.setCustomPattern(attributes.getValue(inputMaskIndex));
                    }
                    editMask = editMaskDateTime;
                    break;
                }
            }
        }
    }        
    
    private final static class PropertyParserState extends ParserState{
        
        private final Id ownerClassId;
        private final String ownerClassName;
        private final Id referencedTableId;
        private final List<SqmlClassPropertyDef.ChildColumnInfo> childColumns = new LinkedList<>();
        private EditMask editMask;
        private ISqmlTableIndexDef parentIndex;        
        
        public PropertyParserState(final Attributes attributes, 
                                                 final Id ownerClassId, 
                                                 final String ownerClassName){
            super(ESqmlItemType.PROPERTY,new AttributesImpl(attributes));
            this.ownerClassId = ownerClassId;
            this.ownerClassName = ownerClassName;
            final int referencedTableIdAttrIndex = attributes.getIndex("ReferencedTableId");
            if (referencedTableIdAttrIndex>-1){
                referencedTableId = Id.Factory.loadFrom(attributes.getValue(referencedTableIdAttrIndex));
            }else{
                referencedTableId = null;
            }
        }
        
        public Id getReferencedTableId(){
            return referencedTableId;
        }
        
        public void setEditMask(final EditMask editMask){
            this.editMask = editMask;
        }
        
        public void setParentIndex(final ISqmlTableIndexDef indexDef){
            parentIndex = indexDef;
        }
        
        public void addChildColumn(final Id tableId, final Id columnId){
            childColumns.add(new SqmlClassPropertyDef.ChildColumnInfo(tableId, columnId));
        }
        
        public SqmlClassPropertyDef createSqmlDef(final SqmlModule module){
            return new SqmlClassPropertyDef(module, getAttributes(), editMask, parentIndex, childColumns, ownerClassId, ownerClassName);
        }
    }
    
    private final static class ClassParserState extends ParserState{
        
        public static enum EClassItems{PROPERTIES,SELECTOR_PRESENTATIONS};
        
        private final List<ISqmlColumnDef> properties = new LinkedList<>();                
        private final List<SqmlSelectorPresentationImpl> presentations = new LinkedList<>();
        private EClassItems currentClassItem = null;
        
        public ClassParserState(final Attributes attributes){
            super(ESqmlItemType.CLASS,new AttributesImpl(attributes));
        }
        
        public void addProperty(final SqmlClassPropertyDef property){
            properties.add(property);
        }
        
        public void addSelectorPresentation(final SqmlSelectorPresentationImpl presentation){
            presentations.add(presentation);
        }
                
        public SqmlClassDef createSqmlDef(final SqmlModule mosule){
            final SqmlTableColumns props = new SqmlTableColumns(properties);
            final SqmlSelectorPresentations selPresentations;
            if (presentations.isEmpty()){
                selPresentations = SqmlSelectorPresentations.EMPTY;
            }else{
                selPresentations = new SqmlSelectorPresentations(presentations);
            }
            return new SqmlClassDef(mosule, getAttributes(), props, selPresentations);
        }
        
        public String getClassName(){
            return getAttributes().getValue("Name");
        }
        
        public EClassItems getCurrentClassItem(){
            return currentClassItem;
        }
        
        public void setCurrentItems(final EClassItems classItem){
            currentClassItem = classItem;
        }
    }
    
    private final SqmlRepository repository;
    private final IClientEnvironment environment;
    private final ILoadingHandler loadingHandler;
    private SqmlModule sqmlModule;    
    private final Stack<ParserState> state = new Stack<>();    
    
    public SqmlSaxHandler(final IClientEnvironment env, final SqmlRepository sqmlRepository, final ILoadingHandler loadingHandler){
        repository=sqmlRepository;
        this.loadingHandler = loadingHandler;
        environment = env;
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (!state.isEmpty() && state.peek() instanceof ValueParserState){
            ((ValueParserState)state.peek()).appendValueChars(ch, start, length);
        }
    }

    @Override
    public void endElement(final String uri, 
                                        final String localName, 
                                        final String qName) throws SAXException {
        if (loadingHandler.wasCancelled()){
            return;
        }
        final ParserState curState = state.isEmpty() ? null : state.peek();
        final ESqmlItemType curItemType = curState==null ? null : curState.getSqmlItemType();        
        switch(qName){
            case "Item":{
                if (curItemType==ESqmlItemType.ENUM_ITEM){
                    final EnumItemParserState itemState = (EnumItemParserState)state.pop();
                    final EnumParserState enumState = (EnumParserState)state.peek();
                    enumState.addEnumItem(itemState.createSqmlDef(sqmlModule, enumState.getAttributes()));
                }
                break;
            }case "EnumDef":{
                if (curItemType==ESqmlItemType.ENUM){
                    final SqmlEnumDefImpl enumDef = ((EnumParserState)state.pop()).createSqmlDef(sqmlModule);
                    repository.addEnum(enumDef);
                }
                break;
            }case "DomainDef":
              case "SubDomain":{
                if (curItemType==ESqmlItemType.DOMAIN){
                    final SqmlDomainDefImpl domainDef = ((DomainParserState)state.pop()).createSqmlDef(sqmlModule);
                    if (!state.isEmpty() && state.peek().getSqmlItemType()==ESqmlItemType.DOMAIN){
                        ((DomainParserState)state.peek()).addSubDomain(domainDef);
                    }else{
                        repository.addDomain(domainDef);
                    }
                }
                break;
            }case "Parameter":{
                if (curItemType==ESqmlItemType.FUNCTION_PARAM){
                    final SqmlFunctionParameterImpl paramDef = ((ParameterParserState)state.pop()).createSqmlDef(sqmlModule);
                    ((FunctionParserState)state.peek()).addParameter(paramDef);
                }
                break;
            }case "Function":{
                if (curItemType==ESqmlItemType.FUNCTION){
                    final SqmlFunctionDefImpl functionDef = ((FunctionParserState)state.pop()).createSqmlDef(sqmlModule);
                    ((PackageParserState)state.peek()).addFunction(functionDef);
                }
                break;
            }case "PackageDef":{
                if (curItemType==ESqmlItemType.PACKAGE){
                    final SqmlPackageDefImpl packageDef = ((PackageParserState)state.pop()).createSqmlDef(sqmlModule);
                    repository.addPackage(packageDef);
                }
                break;
            }case "Indexes":
              case "DetailReferences":
              case "OutgoingReferences":
              case "Columns":{
                if (curItemType==ESqmlItemType.TABLE){
                    ((TableParserState)state.peek()).setCurrentItems(null);
                }
                break;
            }case "Properties":
              case "ContextlessSelectorPresentations":{
              if (curItemType==ESqmlItemType.CLASS){
                  ((ClassParserState)state.peek()).setCurrentItems(null);
              }
              break;
            }case "ReferenceToMasterTable":{
                if (curItemType==ESqmlItemType.DET_REFERENCE){
                    final SqmlDetailTableReferenceImpl reference = 
                        ((DetailReferenceParserState)state.pop()).createSqmlDef(sqmlModule);
                    final TableParserState tableState = (TableParserState)state.peek();
                    final Id masterTableId = tableState.getMasterTableId();
                    if (masterTableId!=null){
                        tableState.setMasterReferenceId(reference.getId());
                        repository.addDetailReference(masterTableId, reference);
                    }
                }
                break;
            }case "TableDef":{
                if (curItemType==ESqmlItemType.TABLE){
                    final SqmlTableDef tableDef = ((TableParserState)state.pop()).createSqmlDef(sqmlModule,repository);
                    repository.addTable(tableDef);
                }
                break;
            }case "EditMask":{                
                if (curItemType==ESqmlItemType.EDIT_MASK){
                    final EditMaskParserState maskState = (EditMaskParserState)state.pop();
                    final PropertyParserState propState = (PropertyParserState)state.peek();
                    propState.setEditMask(maskState.getResultEditMask());
                }
                break;
            }case "Property":{
                if (curItemType==ESqmlItemType.PROPERTY){
                    final SqmlClassPropertyDef propertyDef = ((PropertyParserState)state.pop()).createSqmlDef(sqmlModule);
                    ((ClassParserState)state.peek()).addProperty(propertyDef);
                }
                break;
            }case "ClassDef":{
                if (curItemType==ESqmlItemType.CLASS){
                    repository.addClass(((ClassParserState)state.pop()).createSqmlDef(sqmlModule));
                }
                break;
            }case "ValAsStr":
              case "DefaultValue":{
                  if (curState instanceof ValueParserState){
                      state.pop();
                  }
                  break;
              }
        }
    }

    @Override
    public void startElement(final String uri, 
                                         final String localName, 
                                         final String qName, 
                                         final Attributes attributes) throws SAXException {
        if (loadingHandler.wasCancelled()){
            return;
        }
        final ParserState curState = state.isEmpty() ? null : state.peek();
        final ESqmlItemType curItemType = curState==null ? null : curState.getSqmlItemType();        
        switch(qName){
            case "SqmlModule":{
                sqmlModule = 
                    new SqmlModule(environment, Id.Factory.loadFrom(attributes.getValue("Id")), attributes.getValue("QualifiedName"));               
                break;
            }case "EnumDef":{
                final EnumParserState enumState = new EnumParserState(attributes);
                if (repository.findEnumById(enumState.getDefinitionId())==null){
                    state.push(enumState);
                }
                break;
            }case "Item":{
                if (curItemType==ESqmlItemType.ENUM){
                    state.push(new EnumItemParserState(attributes));
                }
                break;
            }case "DomainDef":{
                final DomainParserState domainState = new DomainParserState(attributes);
                if (repository.findDomainById(domainState.getDefinitionId())==null){
                    state.push(domainState);
                }
                break;
            }case "SubDomain":{
                if (curItemType==ESqmlItemType.DOMAIN){
                    state.push(new DomainParserState(attributes));
                }
                break;
            }case "PackageDef":{
                state.push(new PackageParserState(attributes));
                break;
            }case "Function":{
                if (curItemType==ESqmlItemType.PACKAGE){
                    state.push(new FunctionParserState(attributes, state.peek().getDefinitionId()));
                }
                break;
            }case "Parameter":{
                if (curItemType==ESqmlItemType.FUNCTION){
                    final FunctionParserState functionState = (FunctionParserState)state.peek();
                    state.push(new ParameterParserState(attributes, functionState.getDefinitionId(), functionState.getPackageId()));
                }
                break;
            }case "Indexes":{
                if (curItemType==ESqmlItemType.TABLE){
                    ((TableParserState)curState).setCurrentItems(TableParserState.ETableItems.INDICES);
                }
                break;
            }case "ReferenceToMasterTable":{
                if (curItemType==ESqmlItemType.TABLE){
                    final Id tableId = ((TableParserState)curState).getDefinitionId();
                    state.push(new DetailReferenceParserState(attributes,tableId));
                }
                break;
            }case "OutgoingReferences":{
                if (curItemType==ESqmlItemType.TABLE){
                    ((TableParserState)curState).setCurrentItems(TableParserState.ETableItems.OUTGOING_REFS);
                }
                break;
            }case "Columns":{
                if (curItemType==ESqmlItemType.TABLE){
                    ((TableParserState)curState).setCurrentItems(TableParserState.ETableItems.COLUMNS);
                }
                break;
            }case "Index":{
                if (curItemType==ESqmlItemType.TABLE){
                    final TableParserState tableState = (TableParserState)curState;
                    tableState.addIndex(new SqmlTableIndexImpl(sqmlModule, attributes, tableState.getDefinitionId()));
                }
                break;                
            }case "Reference":{
                if (curItemType==ESqmlItemType.TABLE){
                    final TableParserState tableState = (TableParserState)curState;
                    final Id tableId = tableState.getDefinitionId();
                    if (tableState.getCurrentItems()==TableParserState.ETableItems.OUTGOING_REFS){
                        tableState.addOutgoingRef(new SqmlOutgoingTableReferenceImpl(sqmlModule, attributes, tableId));
                    }
                }
                break;                
            }case "Column":{                
                if (curItemType==ESqmlItemType.TABLE){
                    final TableParserState tableState = (TableParserState)curState;
                    tableState.addColumn(new SqmlTableColumnDef(sqmlModule, attributes));
                }else if (curItemType==ESqmlItemType.PROPERTY){
                    final PropertyParserState propState = (PropertyParserState)curState;
                    final Id tableId = Id.Factory.loadFrom(attributes.getValue("TableId"));
                    final Id columnId = Id.Factory.loadFrom(attributes.getValue("Id"));
                    propState.addChildColumn(tableId, columnId);                    
                }
                break;
            }case "TableDef":{
                final TableParserState tableState = new TableParserState(attributes);
                final ISqmlTableDef tableDef = repository.findTableById(tableState.getDefinitionId());
                if (tableDef==null || ( tableDef instanceof SqmlClassDef && ((SqmlClassDef)tableDef).getLinkedTable()==null )){
                    state.push(tableState);
                }
                break;
            }case "EditMask":{
                if (curItemType==ESqmlItemType.PROPERTY){
                    state.push(new EditMaskParserState(attributes));
                }
                break;
            }case "Int":
              case "Num":
              case "Str":
              case "TimeInterval":
              case "DateTime":{
                  if (curItemType==ESqmlItemType.EDIT_MASK){
                      ((EditMaskParserState)curState).parseEditMask(qName, attributes);
                  }
                  break;
            }case "ParentIndex":{
                if (curItemType==ESqmlItemType.PROPERTY){
                    final PropertyParserState propState = (PropertyParserState)curState;
                    propState.setParentIndex(new SqmlTableIndexImpl(sqmlModule, attributes, propState.getReferencedTableId()));
                }
                break;
            }case "Properties":{
                if (curItemType==ESqmlItemType.CLASS){
                    ((ClassParserState)curState).setCurrentItems(ClassParserState.EClassItems.PROPERTIES);
                }
                break;
            }case "ContextlessSelectorPresentations": {
                if (curItemType==ESqmlItemType.CLASS){
                    ((ClassParserState)curState).setCurrentItems(ClassParserState.EClassItems.SELECTOR_PRESENTATIONS);
                }
                break;
            }case "Property":{
                if (curItemType==ESqmlItemType.CLASS){
                    final ClassParserState classState = (ClassParserState)curState;                    
                    state.push(new PropertyParserState(attributes, curState.getDefinitionId(), classState.getClassName()));
                }
                break;
            }case "Presentation":{
                if (curItemType==ESqmlItemType.CLASS){
                    final ClassParserState classState = (ClassParserState)curState;                    
                    classState.addSelectorPresentation(new SqmlSelectorPresentationImpl(sqmlModule, attributes));
                }
                break;
            }case "ClassDef":{
                final ClassParserState classState = new ClassParserState(attributes);
                final ISqmlTableDef classDef = repository.findTableById(classState.getDefinitionId());
                if (classDef==null || classDef instanceof SqmlTableDef){
                    state.push(classState);
                }
                break;
            }case "ValAsStr":
              case "DefaultValue":{
                if (curState instanceof ItemWithValueParserState){
                    state.push(new ValueParserState((ItemWithValueParserState)curState));
                }
                break;    
            }
        }
    }
}
