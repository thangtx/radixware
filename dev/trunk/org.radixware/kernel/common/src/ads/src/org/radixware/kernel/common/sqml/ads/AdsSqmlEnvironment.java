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

package org.radixware.kernel.common.sqml.ads;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.check.RadixProblem;
import org.radixware.kernel.common.defs.*;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.algo.object.AdsAppObject;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityBasedClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityGroupClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.clazz.form.AdsFormHandlerClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.*;
import org.radixware.kernel.common.defs.ads.clazz.presentation.*;
import org.radixware.kernel.common.defs.ads.clazz.sql.AdsCursorClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.AdsSqlClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportClassDef;
import org.radixware.kernel.common.defs.ads.common.AdsCondition;
import org.radixware.kernel.common.defs.ads.common.AdsUtils;
import org.radixware.kernel.common.defs.ads.common.AdsVisitorProviders;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumDef;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsSelectorExplorerItemDef;
import org.radixware.kernel.common.defs.ads.module.AdsPath;
import org.radixware.kernel.common.defs.ads.module.AdsSearcher;
import org.radixware.kernel.common.defs.ads.src.clazz.sql.AdsSqlClassWriter;
import org.radixware.kernel.common.defs.ads.type.AdsClassType.EntityObjectType;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.ads.xml.AdsXmlSchemeDef;
import org.radixware.kernel.common.defs.dds.*;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.EPropNature;
import org.radixware.kernel.common.enums.ERepositorySegmentType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.sqml.ISqmlEnvironment;
import org.radixware.kernel.common.sqml.ISqmlProperty;
import org.radixware.kernel.common.sqml.ISqmlSchema;
import org.radixware.kernel.common.sqml.Sqml;
import org.radixware.kernel.common.sqml.Sqml.Tag;
import org.radixware.kernel.common.sqml.tags.ElseIfTag;
import org.radixware.kernel.common.sqml.tags.EndIfTag;
import org.radixware.kernel.common.sqml.tags.IfParamTag;
import org.radixware.kernel.common.sqml.tags.PropSqlNameTag;
import org.radixware.kernel.common.sqml.tags.PropSqlNameTag.EOwnerType;
import org.radixware.kernel.common.sqml.tags.TargetDbPreprocessorTag;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.ValTypes;


public class AdsSqmlEnvironment implements ISqmlEnvironment {

    public static final class Factory {

        public static AdsSqmlEnvironment newInstance(AdsCondition context) {
            return new AdsSqmlEnvironment(context);
        }

        public static AdsSqmlEnvironment newInstance(AdsSqlClassDef context) {
            return new AdsSqmlEnvironment(context);
        }

        public static AdsSqmlEnvironment newInstance(SelectorAddons context) {
            return new AdsSqmlEnvironment(context);
        }

        public static AdsSqmlEnvironment newInstance(AdsFilterDef context) {
            return new AdsSqmlEnvironment(context);
        }

        public static AdsSqmlEnvironment newInstance(AdsSortingDef context) {
            return new AdsSqmlEnvironment(context);
        }

//        public static AdsSqmlEnvironment newInstance(AdsColorSchemeDef context) {
//            return new AdsSqmlEnvironment(context);
//        }
        public static AdsSqmlEnvironment newInstance(AdsExpressionPropertyDef context) {
            return new AdsSqmlEnvironment(context);
        }
    }
    private RadixObject initialContext;
//    private AdsDefinition context = null;
//    private Properties propertySet = null;
//    private AdsClassDef classDef = null;

    private AdsSqmlEnvironment(RadixObject def) {
        this.initialContext = def;
    }

    private AdsDefinition findAdsDefinition() {
        for (RadixObject owner = initialContext; owner != null; owner = owner.getContainer()) {
            if (owner instanceof AdsDefinition) {
                return (AdsDefinition) owner;
            }
        }
        return null;
    }

//    private AdsClassDef findClassDef() {
//        for (RadixObject owner = initialContext; owner != null; owner = owner.getContainer()) {
//            if (owner instanceof AdsClassDef) {
//                return (AdsClassDef) owner;
//            }
//        }
//        return null;
//    }
    private AdsEntityGroupClassDef findGroupClass(AdsEntityObjectClassDef clazz) {

        if (clazz != null) {

            final AdsEntityClassDef entity = clazz.findRootBasis();
            if (entity != null) {
                return entity.findEntityGroup();
            }
        }

        return null;
    }

    @Override
    public List<Definition> findThisPropertiesOwner() {
        if (initialContext instanceof AdsCondition) {
            RadixObject conditionOwner = initialContext.getOwnerDefinition();
            if (conditionOwner instanceof AdsPropertyDef) {
                AdsType type = ((AdsPropertyDef) conditionOwner).getValue().getType().resolve((AdsPropertyDef) conditionOwner).get();
                if (type instanceof EntityObjectType) {
                    AdsEntityObjectClassDef clazz = ((EntityObjectType) type).getSource();
                    if (clazz != null) {
                        AdsEntityGroupClassDef group = findGroupClass(clazz);
                        if (group == null) {
                            return Collections.singletonList((Definition) clazz);
                        } else {
                            return Arrays.asList(new Definition[]{clazz, group});
                        }
                    }
                    return Collections.emptyList();
                } else {
                    return Collections.emptyList();
                }
            } else if (conditionOwner instanceof AdsSelectorExplorerItemDef) {
                AdsEntityObjectClassDef clazz = ((AdsSelectorExplorerItemDef) conditionOwner).findReferencedEntityClass();
                if (clazz == null) {
                    return Collections.emptyList();
                } else {
                    AdsEntityGroupClassDef group = findGroupClass(clazz);
                    if (group == null) {
                        return Collections.singletonList((Definition) clazz);
                    } else {
                        return Arrays.asList(new Definition[]{clazz, group});
                    }
                }
            } else if (conditionOwner instanceof AdsSelectorPresentationDef) {
                AdsEntityObjectClassDef clazz = ((AdsSelectorPresentationDef) conditionOwner).getOwnerClass();
                if (clazz == null) {
                    return Collections.emptyList();
                } else {
                    AdsEntityGroupClassDef group = findGroupClass(clazz);
                    if (group == null) {
                        return Collections.singletonList((Definition) clazz);
                    } else {
                        return Arrays.asList(new Definition[]{clazz, group});
                    }
                }
            } else if (conditionOwner instanceof AdsAppObject) {
                final AdsAppObject appObj = (AdsAppObject) conditionOwner;
                final AdsAppObject.Prop prop = appObj.getPropByName("objClassId");
                if (prop != null && prop.getValue() != null) {
                    final String idAsStr = prop.getValue().toString();
                    if (idAsStr != null) {
                        final Id id = Id.Factory.loadFrom(idAsStr);
                        final Definition clazz = AdsSearcher.Factory.newAdsDefinitionSearcher(appObj).findById(id).get();
                        if (clazz != null) {
                            return Collections.singletonList(clazz);
                        } else {
                            return Collections.emptyList();
                        }
                    }
                }
                return Collections.emptyList();
            } else {
                return Collections.emptyList();
            }
        } else if (initialContext instanceof SelectorAddons) {
            final AdsEntityObjectClassDef clazz = ((SelectorAddons) initialContext).getOwnerSelectorPresentation().getOwnerClass();
            if (clazz == null) {
                return Collections.emptyList();
            } else {
                return Collections.singletonList((Definition) clazz);
            }
        } else if (initialContext instanceof AdsFilterDef) {
            AdsEntityObjectClassDef clazz = ((AdsFilterDef) initialContext).getOwnerClass();
            if (clazz == null) {
                return Collections.emptyList();
            } else {
                AdsEntityGroupClassDef group = findGroupClass(clazz);
                if (group == null) {
                    return Collections.singletonList((Definition) clazz);
                } else {
                    return Arrays.asList(new Definition[]{clazz, group});
                }
            }
        } else if (initialContext instanceof AdsSortingDef) {
            AdsEntityObjectClassDef clazz = ((AdsSortingDef) initialContext).getOwnerClass();
            if (clazz == null) {
                return Collections.emptyList();
            } else {
                return Collections.singletonList((Definition) clazz);
            }
        } else if (initialContext instanceof AdsExpressionPropertyDef) {
            AdsClassDef clazz = ((AdsExpressionPropertyDef) initialContext).getOwnerClass();
            if (clazz == null) {
                return Collections.emptyList();
            } else {
                return Collections.singletonList((Definition) clazz);
            }
        } else if (initialContext instanceof AdsSqlClassDef) {
            return Collections.singletonList((Definition) initialContext);
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public AdsClassDef findChildPropertiesOwner() {
        if (initialContext instanceof AdsCondition) {
            RadixObject conditionOwner = initialContext.getOwnerDefinition();
            if (conditionOwner instanceof AdsPropertyDef) {
                AdsClassDef oc = ((AdsPropertyDef) conditionOwner).getOwnerClass();
                if (oc instanceof AdsEntityObjectClassDef || oc instanceof AdsFormHandlerClassDef || oc instanceof AdsReportClassDef) {
                    return oc;
                } else {
                    return null;
                }
            } else if (conditionOwner instanceof AdsSelectorExplorerItemDef) {
                return null;
            } else if (conditionOwner instanceof AdsSelectorPresentationDef) {
                return null;
            } else {
                return null;
            }
        } else if (initialContext instanceof SelectorAddons) {
            return null;
        } else {
            return null;
        }
    }

    @Override
    public AdsEntityObjectClassDef findParentPropertiesOwner() {
        if (initialContext instanceof AdsCondition) {
            RadixObject conditionOwner = initialContext.getOwnerDefinition();
            if (conditionOwner instanceof AdsPropertyDef) {
//                AdsType type = ((AdsPropertyDef) conditionOwner).getValue().getType().resolve((AdsPropertyDef) conditionOwner);
//                if (type instanceof EntityObjectType) {
//                    AdsEntityObjectClassDef clazz = ((EntityObjectType) type).getSource();
//                    return clazz;
//                } else {
//                    return null;
//                }
                return null;
            } else if (conditionOwner instanceof AdsSelectorPresentationDef) {
                return null;
            } else if (conditionOwner instanceof AdsSelectorExplorerItemDef) {
                AdsEditorPresentationDef oep = ((AdsSelectorExplorerItemDef) conditionOwner).findOwnerEditorPresentation();
                if (oep != null) {
                    return oep.getOwnerClass();
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } else if (initialContext instanceof SelectorAddons) {
            return null;
        } else {
            return null;
        }
    }

    @Override
    public DdsTableDef findTableById(Id tableId) {
        AdsDefinition def = findAdsDefinition();
        return def != null ? AdsSearcher.Factory.newDdsTableSearcher(def).findById(tableId).get() : null;
    }

    @Override
    public IEnumDef findEnumById(Id enumId) {
        if (enumId == null) {
            return null;
        }
        AdsDefinition def = findAdsDefinition();
        if (def != null) {
            AdsDefinition ec = AdsSearcher.Factory.newAdsDefinitionSearcher(def).findById(enumId).get();
            if (ec instanceof AdsEnumDef) {
                return (AdsEnumDef) ec;
            } else {
                return null;
            }
        } else {
            return null;
        }

    }

    @Override
    public DdsFunctionDef findFunctionById(Id funcId) {
        AdsDefinition def = findAdsDefinition();
        return def == null ? null : AdsSearcher.Factory.newDdsFunctionSearcher(def).findById(funcId).get();
    }

    @Override
    public Definition findDefinitionByIds(Id[] ids) {
        return findDefinitionByIds(ids, false);
    }

    @Override
    public Definition findDefinitionByIds(Id[] ids, boolean global) {
        AdsDefinition def = findAdsDefinition();
        if (def == null) {
            return null;
        }
        AdsPath path = new AdsPath(ids);
        return global ? path.resolveGlobal(def).get() : path.resolve(def).get();
    }

    /**
     * Adapter AdsFilter.Parameter to IParameterDef
     */
    private static class AdsFilterParameterAdapter implements IParameterDef {

        private final AdsFilterDef.Parameter parameter;

        public AdsFilterParameterAdapter(AdsFilterDef.Parameter parameter) {
            this.parameter = parameter;
        }

        @Override
        public AdsFilterDef.Parameter getDefinition() {
            return parameter;
        }

        @Override
        public String getName() {
            return parameter.getName();
        }

        @Override
        public boolean isMulty() {
            return parameter.getType().getTypeId().isArrayType();
        }

        @Override
        public boolean canBeUsedInSqml() {
            return true;
        }
    }

    private static class AdsSqlClassParameterAdapter implements IParameterDef {

        private final AdsPropertyDef propertyAsParameter;

        public AdsSqlClassParameterAdapter(AdsPropertyDef propertyAsParameter) {
            this.propertyAsParameter = propertyAsParameter;
        }

        @Override
        public Definition getDefinition() {
            return propertyAsParameter;
        }

        @Override
        public String getName() {
            return propertyAsParameter.getName();
        }

        @Override
        public boolean isMulty() {
            return propertyAsParameter.getValue().getType().getTypeId().isArrayType();
        }

        @Override
        public boolean canBeUsedInSqml() {
            if (propertyAsParameter instanceof AdsDynamicPropertyDef) {
                return ((AdsDynamicPropertyDef) propertyAsParameter).canBeUsedInSqml();
            } else {
                return false;
            }
//            AdsTypeDeclaration decl = propertyAsParameter.getValue().getType();
//            if (decl == null) {
//                return false;
//            } else {
//                if (decl.getArrayDimensionCount() == 0) {
//                    return ValTypes.ADS_SQL_CLASS_PARAM_TYPES.contains(decl.getTypeId());
//                } else {
//                    return false;
//                }
//            }

        }
    }

    @Override
    public IParameterDef findParameterById(Id parameterId) {
        final AdsDefinition def = findAdsDefinition();
        if (def instanceof AdsSqlClassDef) {
            final AdsSqlClassDef sqlClass = (AdsSqlClassDef) def;
            final AdsPropertyDef prop = sqlClass.getProperties().getLocal().findById(parameterId);
            if (prop != null && AdsVisitorProviders.newPropertyForSqlClassParameterFilter().isTarget(prop)) {
                return new AdsSqlClassParameterAdapter(prop);
            } else {
                return null;
            }
        } else if (def instanceof AdsFilterDef) {
            final AdsFilterDef filter = (AdsFilterDef) def;
            final AdsFilterDef.Parameter parameter = filter.getParameters().findById(parameterId);
            if (parameter != null) {
                return new AdsFilterParameterAdapter(parameter);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    private ISqmlProperty findPropertyById(final AdsClassDef ownerClass, final Id propertyId, EOwnerType ownerType) {


        final AdsPropertyDef prop = ownerClass.getProperties().findById(propertyId, EScope.ALL).get();
        if (prop != null) {
            //RADIX-2893
            if (initialContext instanceof AdsFilterDef && ownerType == EOwnerType.THIS) {
                //akrylov 16.07.10: old code :if (isColumnProperty(prop) || prop.getNature()) {
                if (isColumnProperty(prop) || prop.getNature() == EPropNature.EXPRESSION) {
                    return new SqmlProperty(prop);
                }
            } else {
                return new SqmlProperty(prop);
            }
        }


        if ((propertyId.getPrefix() == EDefinitionIdPrefix.DDS_COLUMN) && (ownerClass instanceof AdsEntityBasedClassDef)) {
            final Id classId = ownerClass.getId();
            final Id tableId = Id.Factory.changePrefix(classId, EDefinitionIdPrefix.DDS_TABLE);
            final DdsTableDef table = AdsSearcher.Factory.newDdsTableSearcher(ownerClass).findById(tableId).get();
            if (table != null) {
                return table.getColumns().findById(propertyId, EScope.LOCAL_AND_OVERWRITE).get();
            }
        }
        return null;
    }

    @Override
    public ISqmlProperty findPropertyById(Id propOwnerId, Id propertyId) {
        final AdsDefinition def = findAdsDefinition();
        if (def == null) {
            return null;
        }

        Id classId = propOwnerId;

        if (propOwnerId.getPrefix() == EDefinitionIdPrefix.DDS_TABLE) {
            if (propertyId.getPrefix() == EDefinitionIdPrefix.DDS_COLUMN) {
                final DdsTableDef table = AdsSearcher.Factory.newDdsTableSearcher(def).findById(propOwnerId).get();
                if (table != null) {
                    return table.getColumns().findById(propertyId, EScope.LOCAL_AND_OVERWRITE).get();
                } else {
                    return null;
                }
            }
            classId = Id.Factory.changePrefix(propOwnerId, EDefinitionIdPrefix.ADS_ENTITY_CLASS);
        }

        final AdsClassDef ownerClass = AdsSearcher.Factory.newAdsClassSearcher(def).findById(classId).get();
        if (ownerClass != null) {
            final AdsPropertyDef prop = ownerClass.getProperties().findById(propertyId, EScope.ALL).get();
            if (prop != null) {
                return new SqmlProperty(prop);
            }
        }

        return null;
    }

    @Override
    public ISqmlProperty findChildPropertyById(Id propertyId) {
        AdsClassDef childClass = findChildPropertiesOwner();
        if (childClass != null) {
            return findPropertyById(childClass, propertyId, EOwnerType.CHILD);
        } else {
            return null;
        }
    }

    @Override
    public ISqmlProperty findParentPropertyById(Id propertyId) {
        AdsEntityObjectClassDef parentClass = findParentPropertiesOwner();
        if (parentClass != null) {
            return findPropertyById(parentClass, propertyId, EOwnerType.PARENT);
        } else {
            return null;
        }
    }

    @Override
    public ISqmlProperty findThisPropertyById(Id propertyId) {
        for (Definition thisClass : findThisPropertiesOwner()) {
            if (thisClass instanceof AdsClassDef) {
                ISqmlProperty prop = findPropertyById((AdsClassDef) thisClass, propertyId, EOwnerType.THIS);
                if (prop != null) {
                    return prop;
                }
            }
        }
        return null;

    }

//    @Override
//    public Definition findMlStringById(Id mlStringId) {
//        AdsDefinition def = findAdsDefinition();
//        return def == null ? null : def.findLocalizingBundle().getStrings().findById(mlStringId, EScope.LOCAL_AND_OVERWRITE);
//    }
    @Override
    public DdsReferenceDef findReferenceById(Id referenceId) {
        AdsDefinition def = findAdsDefinition();
        return def == null ? null : AdsSearcher.Factory.newDdsReferenceSearcher(def).findById(referenceId).get();
    }

    @Override
    public DdsSequenceDef findSequenceById(Id sequenceId) {
        AdsDefinition def = findAdsDefinition();
        return def == null ? null : AdsSearcher.Factory.newDdsSequenceSearcher(def).findById(sequenceId).get();
    }

    @Override
    public DdsTableDef findThisTable() {
//        AdsClassDef cd = findClassDef();
//        if (cd instanceof AdsEntityBasedClassDef) {
//            return ((AdsEntityBasedClassDef) cd).findTable();
//        } else {
//            return null;
//        }
        List<Definition> list = findThisPropertiesOwner();
        for (Definition cd : list) {
            if (cd instanceof AdsEntityBasedClassDef) {
                return ((AdsEntityBasedClassDef) cd).findTable(cd);
            }
        }
        return null;
    }

    @Override
    public DdsFunctionDef findThisFunction() {
        return null;
    }

    @Override
    public ERepositorySegmentType getSegmentType() {
        return ERepositorySegmentType.ADS;
    }

//    @Override
//    public boolean isDbModificationAllowed() {
//        final AdsDefinition definition = findAdsDefinition();
//        if (definition instanceof AdsStatementClassDef) {
//            return true;
//        }
//        if (definition instanceof AdsProcedureClassDef) {
//            return true;
//        }
//        return false;
//    }
    private enum EContextType {

        PARENT_REF_PROP_CONDITION,
        CHILD_REF_ITEM_CONDITION,
        OTHER;
    }

    private EContextType getContextType() {
        if (initialContext instanceof AdsCondition) {
            RadixObject conditionOwner = initialContext.getOwnerDefinition();
            if (conditionOwner instanceof AdsPropertyDef) {
                return EContextType.PARENT_REF_PROP_CONDITION;
            } else if (conditionOwner instanceof AdsSelectorExplorerItemDef) {
                return EContextType.CHILD_REF_ITEM_CONDITION;
            }
        }
        return EContextType.OTHER;
    }

    private final class PropSqlNameTagProvider extends VisitorProvider {

        private final EOwnerType ownerType;
        private List<AdsClassDef> ownerClasses = null;
        private EContextType contextType = getContextType();

        public PropSqlNameTagProvider(final EOwnerType ownerType) {
            this.ownerType = ownerType;
            switch (ownerType) {
                case CHILD:
                    AdsClassDef clazz = findChildPropertiesOwner();
                    if (clazz != null) {
                        ownerClasses = Collections.singletonList(clazz);
                    }
                    break;
                case PARENT:
                    clazz = findParentPropertiesOwner();
                    if (clazz != null) {
                        ownerClasses = Collections.singletonList(clazz);
                    }
                    break;
                case TABLE:
                    break;
                case THIS:
                    List<Definition> owners = findThisPropertiesOwner();
                    for (Definition owner : owners) {
                        if (owner instanceof AdsClassDef) {
                            if (ownerClasses == null) {
                                ownerClasses = new LinkedList<AdsClassDef>();
                            }
                            ownerClasses.add((AdsClassDef) owner);
                        }
                    }
                    break;
            }
        }

        private boolean any(RadixObject radixObject) {
            if (ownerClasses == null) {
                return false;
            }
            if (radixObject instanceof AdsPropertyDef) {
                AdsPropertyDef prop = (AdsPropertyDef) radixObject;
                EValType typeId = prop.getValue().getType().getTypeId();
                if (typeId == null) {
                    return false;
                }
                switch (typeId) {
                    case USER_CLASS:
                    case JAVA_CLASS:
                    case JAVA_TYPE:
                        return false;
                }
                boolean found = false;
                for (AdsClassDef clazz : ownerClasses) {
                    if (clazz.getProperties().findById(prop.getId(), EScope.ALL).get() == prop) {
                        found = true;
                        break;
                    }
                }
                return found;
            } else if (radixObject instanceof DdsColumnDef) {
                boolean found = false;
                for (AdsClassDef ownerClass : ownerClasses) {
                    if (ownerClass instanceof AdsEntityObjectClassDef) {
                        DdsTableDef table = ((AdsEntityObjectClassDef) ownerClass).findTable(ownerClass);
                        if (table != null) {
                            if (table.getColumns().findById(((DdsColumnDef) radixObject).getId(), EScope.ALL).get() == radixObject) {
                                found = true;
                                break;
                            }
                        }
                    }
                }
                return found;
            }
            return false;
        }

        private boolean db(RadixObject radixObject) {
            if (ownerClasses == null) {
                return false;
            }
            if ((radixObject instanceof AdsFieldPropertyDef) && !(radixObject instanceof AdsFieldRefPropertyDef)) {
                if (ownerClasses.size() == 1) {
                    final AdsClassDef ownerClass = ownerClasses.get(0);
                    if (ownerClass instanceof AdsCursorClassDef || ownerClass instanceof AdsReportClassDef) {
                        final AdsFieldPropertyDef field = (AdsFieldPropertyDef) radixObject;
                        if (ownerClass.getProperties().findById(field.getId(), EScope.ALL).get() == field) {
                            return true;
                        }
                    }
                }
                return false;
            } else if (radixObject instanceof AdsPropertyDef) {
                // System.out.println(radixObject.getQualifiedName());
                boolean found = false;
                for (AdsClassDef ownerClass : ownerClasses) {
                    AdsPropertyDef prop = (AdsPropertyDef) radixObject;
                    if (isColumnProperty(prop) || prop.getNature() == EPropNature.USER || prop.getNature() == EPropNature.EXPRESSION || prop.getNature() == EPropNature.GROUP_PROPERTY) {
                        //RADIX-4659
                        if (prop.getNature() == EPropNature.EXPRESSION && initialContext instanceof AdsExpressionPropertyDef) {
                            AdsClassDef contextOwner = ((AdsExpressionPropertyDef) initialContext).getOwnerClass();
                            if (prop != initialContext) {
                                if (contextOwner.getProperties().findById(prop.getId(), EScope.ALL).get() == prop) {
                                    found = true;
                                    break;
                                }
                            }
                            return false;
                        }
                        //RADIX-2893
                        if (prop.getNature() == EPropNature.USER && ownerType == EOwnerType.THIS && initialContext instanceof AdsFilterDef) {//only db columns are allowed for filters
                            return false;
                        }

                        if (ownerClass.getProperties().findById(prop.getId(), EScope.ALL).get() == prop) {
                            found = true;
                            break;
                        }
                    }
                }
                return found;
            } else if (radixObject instanceof DdsColumnDef) {
                boolean found = false;
                for (AdsClassDef ownerClass : ownerClasses) {
                    if (ownerClass instanceof AdsEntityObjectClassDef) {
                        DdsTableDef table = ((AdsEntityObjectClassDef) ownerClass).findTable(ownerClass);
                        if (table != null) {
                            if (table.getColumns().findById(((DdsColumnDef) radixObject).getId(), EScope.ALL).get() == radixObject) {
                                found = true;
                                break;
                            }
                        }
                    }
                }
                return found;
            }
            return false;

        }

        @Override
        public boolean isTarget(final RadixObject radixObject) {

            switch (ownerType) {
                case NONE:
                case TABLE:
                    //#RADIX-2944
                    //block possibility to insert PropSqlName tag by parentRef if Owner is TABLE or NONE
                    if (radixObject instanceof ParentRefProperty) {
                        return false;
                    } else if (radixObject instanceof DdsColumnDef) {
                        return true;
                    } else if (radixObject instanceof AdsPropertyDef) {
                        final AdsPropertyDef prop = (AdsPropertyDef) radixObject;
                        return isColumnProperty(prop) || prop.getNature() == EPropNature.EXPRESSION;
                    } else {
                        return false;
                    }
                case CHILD://if we inparent ref property, any of child properties available
                    switch (contextType) {
                        case PARENT_REF_PROP_CONDITION:
                            return any(radixObject);
                        case CHILD_REF_ITEM_CONDITION:
                            return db(radixObject);
                        default:
                            return false;
                    }

                case PARENT://if we in parent ref property only user propertyies or column based existing in db properties
                    switch (contextType) {
                        case PARENT_REF_PROP_CONDITION:
                            return db(radixObject);
                        case CHILD_REF_ITEM_CONDITION:
                            return any(radixObject);
                        default:
                            return false;
                    }
                case THIS:
                    return db(radixObject);
            }
            return false;

        }

        public boolean isColumnProperty(AdsPropertyDef prop) {
            return AdsSqmlEnvironment.isColumnProperty(prop);
        }
    }

    @Override
    public VisitorProvider getPropProvider(final EOwnerType ownerType) {
        return new PropSqlNameTagProvider(ownerType);
    }

    private static class SqmlProperty implements ISqmlProperty {

        private final AdsPropertyDef source;

        private SqmlProperty(AdsPropertyDef source) {
            this.source = source;
        }

        @Override
        public EValType getValType() {
            return source.getValue().getType().getTypeId();
        }

        @Override
        public DdsTableDef findOwnerTable() {
            AdsClassDef cd = source.getOwnerClass();
            if (cd instanceof AdsEntityBasedClassDef) {
                return ((AdsEntityBasedClassDef) cd).findTable(source);
            } else {
                return null;
            }
        }

        @Override
        public String getDbName() {
            if (source instanceof ColumnProperty) {
                final DdsColumnDef column = ((ColumnProperty) source).getColumnInfo().findColumn();
                if (column != null) {
                    return column.getDbName();
                } else {
                    return null;
                }
            } else if (source instanceof AdsFieldPropertyDef) {
                return source.getName();
            } else {
                return null;
            }
        }

        @Override
        public Sqml getExpression() {
            if (source instanceof AdsExpressionPropertyDef) {
                final AdsExpressionPropertyDef expressionProp = (AdsExpressionPropertyDef) source;
                return expressionProp.getExpresssion();
            } else {
                return null;
            }
        }

        @Override
        public boolean isGeneratedInDb() {
            if (source instanceof ColumnProperty) {
                final DdsColumnDef column = ((ColumnProperty) source).getColumnInfo().findColumn();
                if (column != null) {
                    return column.isGeneratedInDb();
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }

        @Override
        public Definition getDefinition() {
            return source;
        }

        @Override
        public Id getId() {
            return source.getId();
        }

        @Override
        public Id[] getIdPath() {
            return source.getIdPath();
        }

        @Override
        public String getName() {
            return source.getName();
        }

        @Override
        public void check(final Tag tag, final IProblemHandler problemHandler) {
            if (tag instanceof PropSqlNameTag) { // by BAO 21.07.2010
                final PropSqlNameTag propTag = (PropSqlNameTag) tag;
                if (propTag.getOwnerType() == EOwnerType.CHILD || propTag.getOwnerType() == EOwnerType.PARENT)// parent and child columns come from java via query parameters so they can use custom getters
                {
                    return;
                }
                if (source.getValueInheritanceRules().getInheritable()) {
                    if (!tag.isWarningSuppressed(PropSqlNameTag.Problems.PROPERTY_VALUE_IS_INHERITABLE)) {
                        problemHandler.accept(RadixProblem.Factory.newWarning(PropSqlNameTag.Problems.PROPERTY_VALUE_IS_INHERITABLE, tag, source.getQualifiedName()));
                    }
                }
                if (source.isGetterDefined(EScope.ALL)) {
                    if (!tag.isWarningSuppressed(PropSqlNameTag.Problems.PROPERTY_HAS_USER_DEFINED_GETTER)) {
                        problemHandler.accept(RadixProblem.Factory.newWarning(PropSqlNameTag.Problems.PROPERTY_HAS_USER_DEFINED_GETTER, tag, source.getQualifiedName()));
                    }
                }
                if (source instanceof AdsParentPropertyDef) {
                    List<String> messages = new LinkedList<String>();
                    if (((AdsParentPropertyDef) source).isUnsafeForSqml(messages)) {
                        for (String message : messages) {
                            problemHandler.accept(RadixProblem.Factory.newWarning(tag, message));
                        }
                    }

                }
            }

        }
    }

    @Override
    public void printTagCondition(CodePrinter cp, IfParamTag ifParamTag) {
        AdsUtils.printTagCondition(cp, ifParamTag);
    }

    private static boolean isColumnProperty(AdsPropertyDef prop) {
        if (prop instanceof ColumnProperty) {
            final DdsColumnDef column = ((ColumnProperty) prop).getColumnInfo().findColumn();
            return (column != null && (column.isGeneratedInDb() || column.isExpression()));
        } else if (prop instanceof AdsTransmittablePropertyDef) {
            EValType type = prop.getValue().getType().getTypeId();
            return type != null && ValTypes.DDS_COLUMN_TYPES.contains(type);
        } else if (prop instanceof AdsParentRefPropertyDef) {
            final DdsReferenceDef ref = ((AdsParentRefPropertyDef) prop).getParentReferenceInfo().findParentReference();
            return ref != null;
        } else {
            return false;
        }
    }

    @Override
    public ISqmlSchema findSchemaById(Id schemaId) {

        final AdsDefinition owner = findAdsDefinition();
        if (owner == null) {
            return null;
        }

        final AdsDefinition def = AdsSearcher.Factory.newAdsDefinitionSearcher(owner).findById(schemaId).get();
        if (def instanceof AdsXmlSchemeDef) {
            final AdsXmlSchemeDef schema = (AdsXmlSchemeDef) def;
            return new ISqmlSchema() {
                @Override
                public Definition getDefinition() {
                    return schema;
                }

                @Override
                public String getNamespace() {
                    return schema.getTargetNamespace();
                }
            };
        }

        return null;
    }

    @Override
    public void printTargetDbPreprocessorTag(CodePrinter cp, TargetDbPreprocessorTag tag) {
        AdsSqlClassWriter.printTargetDbPreprocessorTag(cp, tag);
    }

    @Override
    public void printEndIf(CodePrinter cp, EndIfTag endIfTag) {
        cp.leaveBlock();
        cp.print("}");
    }

    @Override
    public void printElse(CodePrinter cp, ElseIfTag elseIfTag) {
        cp.leaveBlock();
        cp.print("} else {");
        cp.enterBlock();
    }

    @Override
    public RadixObject getContext() {
        return initialContext;
    }
}
