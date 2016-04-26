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
package org.radixware.kernel.common.builder.check.ads.clazz;

import java.text.MessageFormat;
import java.util.*;
import org.radixware.kernel.common.builder.check.ads.AdsDefinitionChecker;
import org.radixware.kernel.common.builder.check.ads.CheckUtils;
import org.radixware.kernel.common.builder.check.ads.clazz.presentations.EditOptionsChecker;
import org.radixware.kernel.common.builder.check.ads.jml.JmlChecker;
import org.radixware.kernel.common.builder.check.common.CheckHistory;
import static org.radixware.kernel.common.builder.check.common.RadixObjectChecker.error;
import org.radixware.kernel.common.builder.check.common.RadixObjectCheckerRegistration;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.check.RadixProblem;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.SearchResult;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.AdsValAsStr;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.AdsModelClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.*;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsParentPropertyDef.ParentInfo;
import org.radixware.kernel.common.defs.ads.clazz.presentation.*;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportClassDef;
import org.radixware.kernel.common.defs.ads.common.AdsCondition;
import org.radixware.kernel.common.defs.ads.common.AdsUtils;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumDef;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumItemDef;
import org.radixware.kernel.common.defs.ads.localization.AdsEventCodeDef;
import org.radixware.kernel.common.defs.ads.localization.AdsLocalizingBundleDef;
import org.radixware.kernel.common.defs.ads.localization.AdsMultilingualStringDef;
import org.radixware.kernel.common.defs.ads.src.xml.XBeansType;
import org.radixware.kernel.common.defs.ads.type.*;
import org.radixware.kernel.common.defs.dds.DdsColumnDef;
import org.radixware.kernel.common.defs.dds.DdsIndexDef;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.defs.dds.utils.DbNameUtils;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.*;
import org.radixware.kernel.common.jml.Jml.IHistory;
import org.radixware.kernel.common.jml.JmlTagEventCode;
import org.radixware.kernel.common.sqml.Sqml;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.common.utils.ValTypes;

@RadixObjectCheckerRegistration
public class AdsPropertyChecker<T extends AdsPropertyDef> extends AdsDefinitionChecker<T> {

    private AdsPropertyDef findAnotherRefPublisher(final ParentRefProperty src) {
        final Id refId = src.getParentReferenceInfo().getParentReferenceId();
        for (AdsPropertyDef prop : ((AdsPropertyDef) src).getOwnerClass().getProperties().get(EScope.ALL)) {
            if (prop != src && prop instanceof ParentRefProperty) {
                final ParentRefProperty ref = (ParentRefProperty) prop;
                if (Utils.equals(ref.getParentReferenceInfo().getParentReferenceId(), refId)) {
                    return prop;
                }
            }
        }
        return null;
    }

    @Override
    public Class<? extends RadixObject> getSupportedClass() {
        return AdsPropertyDef.class;
    }

    @Override
    public void check(final T prop, final IProblemHandler problemHandler) {
        super.check(prop, problemHandler);
        final boolean isClientProperty = prop instanceof AdsClientSidePropertyDef;

        if (prop.getAccessFlags().isAbstract()) {
            final AdsClassDef ownerClass = prop.getOwnerClass();
            if (ownerClass.getClassDefType() != EClassType.INTERFACE && !ownerClass.getAccessFlags().isAbstract()) {
                error(prop, problemHandler, "Abstract property in concrete class");
            }
            checkAbstract(prop, problemHandler);
        }

        final AdsTypeDeclaration type = prop.getValue().getType();
        if (!prop.getValue().isTypeAllowed(type.getTypeId())) {
            error(prop, problemHandler, "Type " + String.valueOf(type.getTypeId()) + " is not allowed for this kind of property");
        }
        AdsDefinition referecedDef = null;
        AdsEnumDef referencedEnum = null;
        final AdsType resolvedTypeRef = type.resolve(prop).get(AdsDefinitionChecker.<AdsType>getSearchDuplicatesChecker(prop, problemHandler));
        if (resolvedTypeRef == null) {
            error(prop, problemHandler, "Property type can not be resoved");
        } else {
            resolvedTypeRef.check(prop, problemHandler);
            if (resolvedTypeRef instanceof AdsDefinitionType) {
                final Definition src = ((AdsDefinitionType) resolvedTypeRef).getSource();
                if (src instanceof AdsDefinition) {
                    referecedDef = (AdsDefinition) src;
                    CheckUtils.checkExportedApiDatails(prop.getValue(), (AdsDefinition) src, problemHandler);
                    if (src instanceof AdsEnumDef) {
                        referencedEnum = (AdsEnumDef) src;
                        AdsValAsStr propValue = prop.getValue().getInitialValueController().getValue();
                        if (propValue != null && propValue != AdsValAsStr.NULL_VALUE && propValue.getValAsStr() != null){
                            boolean findItem = false;
                            for (AdsEnumItemDef item : referencedEnum.getItems().list(EScope.LOCAL_AND_OVERWRITE)){
                                if (item.getValue().equals(propValue.getValAsStr())){
                                    findItem = true;
                                    break;
                                }
                            }
                            if (!findItem){
                                error(prop, problemHandler, "The initialization value cannot be found in " + referencedEnum.getQualifiedName());
                            }
                        }
                    }
                    if (prop.isTransferable(ERuntimeEnvironmentType.COMMON_CLIENT) || prop.isTransferable(ERuntimeEnvironmentType.WEB) || prop.isTransferable(ERuntimeEnvironmentType.EXPLORER)) {
                        if (resolvedTypeRef instanceof XmlType) {
                            XmlType x = (XmlType) resolvedTypeRef;
                            if (x.getSuffix() != null) {
                                XBeansType xType = x.getXmlType();
                                if (xType != null && !xType.isDocumentType()) {
                                    error(prop, problemHandler, "Property of xml type is referers to non-document type " + x.getSuffix() + " from " + src.getQualifiedName());
                                }
                            }
                        }
                    }
                }

            }
        }

        if (prop.isConst() && prop.isSetterDefined(EScope.LOCAL_AND_OVERWRITE)) {
            error(prop, problemHandler, "Read only properties should not define custom setter");
        }

        final AdsPropertyDef ovrt = prop.getHierarchy().findOverwritten().get();

        if (ovrt != null) {
            // CheckUtils.checkExportedApiDatails(prop, ovrt, problemHandler);

            if (prop.getAccessMode().isLess(ovrt.getAccessMode())) {
                error(prop, problemHandler, "Accessibility of overwritten property " + ovrt.getName() + " can not be reduced. Should be not less than " + ovrt.getAccessMode());
            }
            if (!AdsTypeDeclaration.equals(ovrt.getValue().getType(), type)) {
                final String typeName1 = ovrt.getValue().getType().getName(prop);
                final String typeName2 = type.getName(prop);
                error(prop, problemHandler, MessageFormat.format("Property type does not match to type of overwritten property {0} expected {1} got {2}", ovrt.getQualifiedName(), typeName1, typeName2));
            }
        } else {
            if (prop.isOverwrite()) {
                error(prop, problemHandler, "Property does not overwrites property from superclass. Turn off \"Overwrite\" option");
            }
        }

        final AdsPropertyDef ovr = prop.getHierarchy().findOverridden().get();

        if (ovr != null) {
            if (ovr.isFinal()) {
                problemHandler.accept(RadixProblem.Factory.newError(AdsPropertyDef.Problems.OVERRIDE_FINAL_PROPERTY, prop, ovr.getQualifiedName()));
            }
            if (prop.getAccessMode().isLess(ovr.getAccessMode())) {
                error(prop, problemHandler, "Accessibility of overriden property " + ovr.getName() + " can not be reduced. Should be not less than " + ovr.getAccessMode());
            }
            AdsUtils.checkAccessibility(prop, ovr, true, problemHandler);
            //CheckUtils.checkExportedApiDatails(prop, ovr, problemHandler);
            if (!prop.isOverride()) {
                warning(prop, problemHandler, MessageFormat.format("The property overrides property {0}. Turn on \"Override\" option", ovr.getQualifiedName()));
            }

            if (!AdsTypeDeclaration.isAssignable(ovr.getValue().getType(), type, prop)) {
                final String typeName1 = ovr.getValue().getType().getName(prop);
                final String typeName2 = type.getName(prop);
                error(prop, problemHandler, MessageFormat.format("Property type does not match to type of overridden property {0} expected {1} got {2}", ovr.getQualifiedName(), typeName1, typeName2));
            }
        } else {
            if (prop.isOverride()) {
                error(prop, problemHandler, "Property does not overrides property from superclass. Turn off \"Override\" option");
            }
        }
        //check value inheritance
        if (!isClientProperty) {
            final ValueInheritanceRules valInhRules = prop.getValueInheritanceRules();

            if (valInhRules != null) {
                if (prop.getNature() == EPropNature.USER) {
                    switch (valInhRules.getInitializationPolicy()) {
                        case DEFINE_ALWAYS:
                        case DEFINE_IF_NOT_INHERITED:
                            if ((prop.getValue().getInitial() == null || prop.getValue().getInitial().getValueType() == AdsValAsStr.EValueType.NULL) && !prop.isWarningSuppressed(AdsPropertyDef.Problems.NULL_INITIAL_VALUE_FOR_DEFINEABLE_PROPERTY)) {
                                problemHandler.accept(RadixProblem.Factory.newWarning(AdsPropertyDef.Problems.NULL_INITIAL_VALUE_FOR_DEFINEABLE_PROPERTY, prop));
                            }
                            break;
                        default:
                            break;
                    }
                    if (!valInhRules.getInheritable() && valInhRules.getInitializationPolicy() == EPropInitializationPolicy.DEFINE_IF_NOT_INHERITED) {
                        warning(prop, problemHandler, "Initialization policy \"Define If Not Inherited\" is meaningless for properties with non-inheritable value");
                    }
                }

                final List<ValueInheritanceRules.InheritancePath> pathes = valInhRules.getPathes();

                if (pathes.isEmpty()) {
                    if (valInhRules.getInheritable()) {
                        error(prop, problemHandler, "Property value is inheritable but no inheritance pathes specified");
                    }
                } else {
                    int counter = 1;
                    for (ValueInheritanceRules.InheritancePath path : pathes) {
                        final ArrayList<DdsReferenceDef> pks = new ArrayList<>();
                        final ValueInheritanceRules.InheritancePath.ResolvedPath resolved = path.resolvePath(pks);
                        if (!pks.isEmpty()) {
                            for (DdsReferenceDef ref : pks) {
                                error(prop, problemHandler, MessageFormat.format("Property is key of reference {1} int inheritane path #{0}", counter, ref.getQualifiedName()));
                            }
                        }
                        if (!resolved.isResolved) {
                            if (resolved.finalProperty == null) {
                                error(prop, problemHandler, MessageFormat.format("Can not find final property of inheritance path #{0}", counter));
                            }

                            for (ValueInheritanceRules.InheritancePath.PathItem item : resolved.items) {
                                if (item.property == null) {
                                    error(prop, problemHandler, MessageFormat.format("Can not find property referenced inheritance path #{0}", counter));
                                } else {
                                    if (item.clazz == null) {
                                        error(prop, problemHandler, MessageFormat.format("Can not find class referenced inheritance path #{0}", counter));
                                    }
                                }
                            }
                        } else {
                            for (ValueInheritanceRules.InheritancePath.PathItem item : resolved.items) {
                                if (item.property != null) {
                                    AdsUtils.checkAccessibility(prop, item.property, false, problemHandler);
                                }
                                if (item.clazz != null) {
                                    AdsUtils.checkAccessibility(prop, item.clazz, false, problemHandler);
                                }
                            }

                            final AdsPropertyDef finalProp = resolved.finalProperty;
                            AdsUtils.checkAccessibility(prop, finalProp, false, problemHandler);
                            if (!AdsTypeDeclaration.isAssignable(type, finalProp.getValue().getType(), prop)) {
                                error(prop, problemHandler, "Type of final property of value inheritance path does not match to property type");
                            }

                            // RADIX-7312
                            if (isPresentable(prop) != isPresentable(finalProp)) {
                                error(prop, problemHandler, "Invalid value inheritance rule, properties '" + prop.getQualifiedName() + ", " + finalProp.getQualifiedName() + "' have different presentability");
                            }
                        }
                        counter++;
                    }
                }
            }
        }

//        if (prop.getNature() == EPropNature.FIELD && !ValTypes.ADS_SQL_CLASS_FIELD_TYPES.contains(prop.getValue().getType().getTypeId())) {
//            error(prop, problemHandler, "Illegal field type: " + prop.getValue().getType().getTypeId().getName());
//        }

        if (prop.getValue().getInitial() != null) {
            CheckUtils.checkAdsValAsStr(problemHandler, prop, prop.getValue().getInitial(), prop.getValue().getType().getTypeId(), "Initial value", null);
        }

        //check presentation
        if (prop instanceof IAdsPresentableProperty && prop.getNature() != EPropNature.FIELD && prop.getNature() != EPropNature.FIELD_REF) {
            final ServerPresentationSupport ps = ((IAdsPresentableProperty) prop).getPresentationSupport();

            if (ps != null) {
                final PropertyPresentation presentation = ps.getPresentation();

                if (presentation != null) {
                    if (presentation.isTitleInherited()) {
                        final AdsDefinition pr = presentation.findTitleOwner();
                        if (pr == null) {
                            error(presentation, problemHandler, "Property title is inherited but not found");
                        } else {
                            AdsUtils.checkAccessibility(prop, pr, false, problemHandler);
                        }
                    } else {

                        CheckUtils.checkMLStringId(prop, presentation.getTitleId(), problemHandler, "title");
                    }

                    if (presentation.isPresentable()) {
                        if (presentation.isHintInherited()) {
                            final AdsDefinition pr = presentation.findHintOwner();
                            if (pr == null) {
                                error(presentation, problemHandler, "Property hint is inherited but not found");
                            } else {
                                AdsUtils.checkAccessibility(prop, pr, false, problemHandler);
                            }
                        } else {
                            CheckUtils.checkMLStringId(prop, presentation.getHintId(), problemHandler, "hint");
                        }
                        for (EPropAttrInheritance a : EPropAttrInheritance.values()) {
                            if (a == EPropAttrInheritance.TITLE) {
                                continue;
                            }
                            if ((a == EPropAttrInheritance.PARENT_SELECTOR || a == EPropAttrInheritance.PARENT_TITLE_FORMAT || a == EPropAttrInheritance.PARENT_SELECT_CONDITION) && !(presentation instanceof ParentRefPropertyPresentation)) {
                                continue;
                            }
                            final PropertyPresentation attOwner = presentation.findAttributeOwner(a);
                            if (attOwner != null && !attOwner.isPresentable()) {
                                error(presentation, problemHandler, "Attribute \"" + a.getName() + "\" is inherited from non-presentable property " + attOwner.getOwnerProperty().getQualifiedName());
                            }
                        }

                        if (presentation.isEditOptionsInherited()) {
                            final PropertyPresentation pr = presentation.findEditOptionsOwner();
                            if (pr == null) {
                                error(presentation, problemHandler, "Property edit options are inherited but not found");
                            } else {
                                AdsUtils.checkAccessibility(prop, pr.getOwnerProperty(), false, problemHandler);
                            }
                        } else {
                            PropertyEditOptions options = presentation.getEditOptions();
                            if (options != null) {
                                EditOptionsChecker.Factory.newInstance(prop, prop.getValue().getType(), referencedEnum).check(options, problemHandler);
                            }
                        }

                        if (presentation instanceof ParentRefPropertyPresentation) {
                            final ParentRefPropertyPresentation refPresentation = (ParentRefPropertyPresentation) presentation;
                            final ParentRefPropertyPresentation.ParentSelect parentSelect = refPresentation.getParentSelect();
//                            if (parentSelect.isParentSelectorInherited()) {
//                                if (parentSelect.getParentSelectorPresentationId() != null) {
//                                    AdsSelectorPresentationDef spr = parentSelect.findParentSelectorPresentation();
//                                    if (spr == null) {
//                                        error(presentation, problemHandler, "Parent selector presentation is inherited but not found");
//                                    }
//                                }
//                            } else {
//                                if (parentSelect.getParentSelectorPresentationId() == null) {
//                                }
//                            }

                            if (parentSelect.getParentSelectorPresentationId() != null) {
                                final AdsSelectorPresentationDef spr = parentSelect.findParentSelectorPresentation();
                                if (spr == null) {
                                    if (parentSelect.isParentSelectorInherited()) {
                                        error(presentation, problemHandler, "Parent selector presentation is inherited but not found: #" + parentSelect.getParentSelectorPresentationId());
                                    } else {
                                        error(presentation, problemHandler, "Parent selector presentation not found: #" + parentSelect.getParentSelectorPresentationId());
                                    }
                                } else {
                                    AdsUtils.checkAccessibility(prop, spr, false, problemHandler);
                                    CheckUtils.checkExportedApiDatails(prop, spr, problemHandler);
                                    CheckUtils.checkSelectorPresentationCreationOptions(prop, refPresentation.getParentSelectorRestrictions(), spr, problemHandler);
                                }
                            } else {
                                error(presentation, problemHandler, "Parent selector presentation is not specified");
                            }
                            if (parentSelect.isParentSelectConditionInherited()) {
                                final AdsCondition parentSelectCondition = parentSelect.getParentSelectorCondition();
                                if (parentSelectCondition == null) {
                                    warning(presentation, problemHandler, "Actual parent select condition is inherited and can not be determined");
                                }
                            }
                            final ParentRefPropertyPresentation.ParentTitle parentTitle = refPresentation.getParentTitle();
                            if (parentTitle.isParentTitleFormatInherited()) {
                                final AdsObjectTitleFormatDef ttf = parentTitle.getTitleFormat();
                                if (ttf == null) {
                                    error(prop, problemHandler, "Parent title format is inherited but not found");
                                }
                            }
                        } else if (presentation instanceof ObjectPropertyPresentation) {
                            final ObjectPropertyPresentation objPresentation = (ObjectPropertyPresentation) presentation;
                            if (objPresentation.getCreationClassCatalogId() != null) {
                                AdsClassCatalogDef cc = objPresentation.findCreationClassCatalog().get(AdsDefinitionChecker.<AdsClassCatalogDef>getSearchDuplicatesChecker(objPresentation, problemHandler));
                                if (cc == null) {
                                    error(presentation, problemHandler, "Object creation class catalog not found: #" + objPresentation.getCreationClassCatalogId());
                                } else {
                                    AdsUtils.checkAccessibility(prop, cc, false, problemHandler);
                                    CheckUtils.checkExportedApiDatails(prop, cc, problemHandler);
                                }
                            }
                            if (!objPresentation.getCreatePresentationsList().isEmpty()) {
                                List<CreatePresentationsList.PresentationRef> refs = objPresentation.getCreatePresentationsList().getPresentationRefs();
                                for (CreatePresentationsList.PresentationRef ref : refs) {
                                    AdsEditorPresentationDef epr = ref.findEditorPresentation();
                                    if (epr == null) {
                                        error(objPresentation, problemHandler, "Object creation editor presentation not found: #" + ref.getPresentationId().toString());
                                    } else {
                                        AdsUtils.checkAccessibility(prop, epr, false, problemHandler);
                                        CheckUtils.checkExportedApiDatails(prop, epr, problemHandler);
                                        CheckUtils.checkCreationEditorPresentation(prop, epr, problemHandler);
                                    }
                                }
                            }
                        }
                    }
                } else {
                    error(prop, problemHandler, "No presentation attributes defined");
                }
            }
        }
        switch (prop.getNature()) {
            case DETAIL_PROP:
                if (prop instanceof AdsDetailColumnPropertyDef) {
                    checkDetailColumnProp((AdsDetailColumnPropertyDef) prop, problemHandler);
                } else {
                    checkDetailRefProp((AdsDetailRefPropertyDef) prop, referecedDef, problemHandler);
                }
                break;
            case DYNAMIC:
                if (prop instanceof AdsTransparentPropertyDef) {
                    break;
                }
            case SQL_CLASS_PARAMETER:
                checkDynamicProp((AdsDynamicPropertyDef) prop, problemHandler);
                break;
            case EXPRESSION:
                checkExpressionProp((AdsExpressionPropertyDef) prop, ovr, ovrt, problemHandler);
                break;
            case FIELD:
                checkFieldProp((AdsFieldPropertyDef) prop, problemHandler);
                break;
            case FIELD_REF:
                checkFieldRefProp((AdsFieldRefPropertyDef) prop, resolvedTypeRef, problemHandler);
                break;
            //    case FORM_PROPERTY:
            case GROUP_PROPERTY:
                checkTransmittableProp((AdsTransmittablePropertyDef) prop, problemHandler);
                break;
            case INNATE:
                if (prop instanceof AdsInnateColumnPropertyDef) {
                    checkInnateColumnProp((AdsInnateColumnPropertyDef) prop, problemHandler);
                } else {
                    checkInnateRefProp((AdsInnateRefPropertyDef) prop, referecedDef, problemHandler);
                }
                break;
            case EVENT_CODE:
                checkEventCodeProp((AdsEventCodePropertyDef) prop, problemHandler);
                break;
            case PARENT_PROP:
                checkParentProp((AdsParentPropertyDef) prop, problemHandler);
                break;
            case PROPERTY_PRESENTATION:
                checkPropertyPresentationProp((AdsPropertyPresentationPropertyDef) prop, problemHandler);
                break;
            case USER:
                checkUserProp((AdsUserPropertyDef) prop, ovr, ovrt, problemHandler);
                break;

        }

        checkAccessors(prop, problemHandler);
        checkReadonly(prop, problemHandler);
    }

    private void checkAbstract(final T prop, final IProblemHandler problemHandler) {
        final SearchResult<AdsPropertyDef> overridden = prop.getHierarchy().findOverridden();
        if (!overridden.isEmpty()) {
            if (overridden.isMultiple()) {
                for (final AdsPropertyDef p : overridden.all()) {
                    if (!p.getAccessFlags().isAbstract()) {
                        error(prop, problemHandler, "Abstract property can't override concrete property");
                        return;
                    }
                }
            } else {
                if (!overridden.get().getAccessFlags().isAbstract()) {
                    error(prop, problemHandler, "Abstract property can't override concrete property");
                }
            }
        }
    }

    private boolean isPresentable(AdsPropertyDef prop) {
        if (!(prop instanceof IAdsPresentableProperty)) {
            return false;
        } else {
            final ServerPresentationSupport presentationSupport = ((IAdsPresentableProperty) prop).getPresentationSupport();
            if (presentationSupport == null) {
                return false;
            } else {
                final PropertyPresentation propertyPresentation = presentationSupport.getPresentation();
                if (propertyPresentation == null || !propertyPresentation.isPresentable()) {
                    return false;
                }
            }
        }
        return true;
    }

    private void checkEventCodeProp(final AdsEventCodePropertyDef prop, final IProblemHandler problemHandler) {
        if (prop.getEventId() == null) {
            error(prop, problemHandler, "Event code string must be specified for event code property");
        } else {
            AdsEventCodeDef ec = prop.findEventCode();
            if (ec == null) {
                error(prop, problemHandler, "Event code #" + prop.getEventId().toString() + " not found");
            } else {
                final CheckHistory history = getHistory();
                JmlTagEventCode.checkForEventDuplication(ec, prop, problemHandler, new IHistory() {
                    @Override
                    public Map<Object, Object> getHistory() {
                        return JmlChecker.JmlSupport.initialize(history);
                    }

                    @Override
                    public void setHistory(Map<Object, Object> h) {
                        throw new IllegalStateException("This method should never be invoked");
                    }
                });
            }
        }
    }

    private void checkColumnProp(final ColumnProperty prop, final IProblemHandler problemHandler) {
        final DdsColumnDef column = prop.getColumnInfo().findColumn();
        if (column == null) {
            final DdsTableDef table = prop.getColumnInfo().findColumnTable();
            if (table != null) {
                error((AdsPropertyDef) prop, problemHandler, MessageFormat.format("Corresponding column not found in table {0}", table.getQualifiedName()));
            } else {
                error((AdsPropertyDef) prop, problemHandler, "Table for corresponding column not found");
            }

        } else {
            final EValType propValType = ((AdsPropertyDef) prop).getValue().getType().getTypeId();
            if (column.getValType() != propValType && !(column.getValType() == EValType.CLOB && (propValType == EValType.XML || propValType == EValType.STR))) {
                error((AdsPropertyDef) prop, problemHandler, MessageFormat.format("Property type does not match to corresponding column type: got {0} expected {1}", propValType.name(), column.getValType().name()));
            }

            final ValueInheritanceRules rules = ((AdsPropertyDef) prop).getValueInheritanceRules();
            if (rules.getInheritable()) {
                if (column.isForeignKey()) {
                    problemHandler.accept(RadixProblem.Factory.newError((AdsPropertyDef) prop, "Value inheritance is not allowed for foreign key column based properties"));
                }
            }
            boolean errorsInDefaultVal = false;
            final AdsPropertyDef adsProp = (AdsPropertyDef) prop;

            final AdsValAsStr propInitVal = adsProp.getValue().getInitial();

            ValAsStr propVal = null;
            if (propInitVal != null) {
                if (propInitVal.getValueType() == AdsValAsStr.EValueType.VAL_AS_STR) {
                    propVal = propInitVal.getValAsStr();
                } else {
                    if (propInitVal.getValueType() != AdsValAsStr.EValueType.NULL) {
                        error(adsProp, problemHandler, "Initial value of column-based property can not be coded via jml expression");
                    }
                }
            }
            //public static void checkAdsValAsStr(IProblemHandler problemHandler, RadixObject context, AdsValAsStr value, EValType type, String valueHint, String details) {

            final ValAsStr colVal = column.getDefaultValue() != null && column.getDefaultValue().getChoice() == ERadixDefaultValueChoice.VAL_AS_STR ? column.getDefaultValue().getValAsStr() : null;
            if (propVal != null || colVal != null) {
                if (propVal != null) {
                    if (colVal != null) {
                        if (!propVal.equals(colVal)) {
                            errorsInDefaultVal = true;
                        }
                    }
                } else {
                    if (colVal != null) {
                        errorsInDefaultVal = true;
                    }
                }
            }
            if (errorsInDefaultVal && !((RadixObject) prop).isWarningSuppressed(AdsPropertyDef.Problems.COLUMN_DEFAULT_VALUE_MISMATCH)) {
                problemHandler.accept(RadixProblem.Factory.newWarning((AdsPropertyDef) prop, AdsPropertyDef.Problems.COLUMN_DEFAULT_VALUE_MISMATCH));
            }

            if (column.getAuditInfo().isSaveValues()) {
                checkTitleAvailabilityForAuditProperty(adsProp, problemHandler);
            }
        }
    }

    private void checkTitleAvailabilityForAuditProperty(AdsPropertyDef prop, IProblemHandler problemHandler) {
        boolean noTitle = true;
        if (prop instanceof IAdsPresentableProperty) {
            final ServerPresentationSupport support = ((IAdsPresentableProperty) prop).getPresentationSupport();
            if (support != null && support.getPresentation() != null) {

                final Id titleId = support.getPresentation().getTitleId();
                if (titleId != null) {
                    final AdsDefinition def = support.getPresentation().findTitleOwner();
                    if (def != null) {
                        final AdsLocalizingBundleDef bundle = def.findExistingLocalizingBundle();
                        if (bundle != null) {
                            final AdsMultilingualStringDef nls = bundle.getStrings().findById(titleId, EScope.ALL).get();
                            if (nls != null) {
                                noTitle = false;
                            }
                        }
                    }
                }
            }
        }
        if (noTitle) {
            if (!prop.isWarningSuppressed(AdsPropertyDef.Problems.NO_TITLE_FOR_AUDIT_PROPERTY)) {
                warning(prop, problemHandler, AdsPropertyDef.Problems.NO_TITLE_FOR_AUDIT_PROPERTY);
            }
        }
    }

    private void checkDetailProp(final DetailProperty prop, final IProblemHandler problemHandler) {
        final DdsReferenceDef ref = prop.getDetailReferenceInfo().findDetailReference();
        if (ref == null) {
            error((AdsPropertyDef) prop, problemHandler, "Detail reference not found: #" + prop.getDetailReferenceInfo().getDetailReferenceId());
        } else {
            final DdsTableDef detailTable = ref.getChildTable((AdsPropertyDef) prop);
            if (detailTable == null) {
                error((AdsPropertyDef) prop, problemHandler, "Detail table not found: #" + ref.getChildTableId());
            } else {
                final AdsEntityObjectClassDef entityClass = (AdsEntityObjectClassDef) ((AdsPropertyDef) prop).getOwnerClass();
                if (!entityClass.isDetailAllowed(ref.getId())) {
                    error((AdsPropertyDef) prop, problemHandler, MessageFormat.format("Usage of detail table {0} is not allowed", detailTable.getQualifiedName()));
                }
            }
        }
    }

    private void checkParentRefProp(final ParentRefProperty prop, AdsDefinition referencedDef, final IProblemHandler problemHandler) {
        final ParentReferenceInfo info = prop.getParentReferenceInfo();
        final DdsReferenceDef ref = info.findParentReference();
        if (ref == null) {
            error((AdsPropertyDef) prop, problemHandler, "Parent reference not found: #" + info.getParentReferenceId());
        } else {
            final AdsPropertyDef anotherRef = findAnotherRefPublisher(prop);
            if (anotherRef != null && !((RadixObject) prop).isWarningSuppressed(AdsPropertyDef.Problems.PARENT_REFERENCE_IS_ALREADY_PUBLISHED)) {
                warning((AdsPropertyDef) prop, problemHandler, AdsPropertyDef.Problems.PARENT_REFERENCE_IS_ALREADY_PUBLISHED, ref.getQualifiedName(), anotherRef.getQualifiedName());
            }
            if (referencedDef instanceof AdsClassDef && ref.getType() != DdsReferenceDef.EType.MASTER_DETAIL) {
                final AdsClassDef target = (AdsClassDef) referencedDef;
                for (DdsReferenceDef.ColumnsInfoItem c : ref.getColumnsInfo()) {
                    final Id childColumnId = c.getChildColumnId();
                    final Id parentColumnId = c.getParentColumnId();
                    final AdsPropertyDef childProperty = ((AdsPropertyDef) prop).getOwnerClass().getProperties().findById(childColumnId, EScope.ALL).get();
                    if (childProperty != null) {
                        final AdsPropertyDef parentProperty = target.getProperties().findById(parentColumnId, EScope.ALL).get();
                        if (parentProperty != null) {
                            final AdsTypeDeclaration childType = childProperty.getValue().getType();
                            final AdsTypeDeclaration parentType = parentProperty.getValue().getType();
                            if (!AdsTypeDeclaration.equals(childType, parentType)) {
                                error((RadixObject) prop, problemHandler, "Type of property "
                                        + childProperty.getQualifiedName()
                                        + " does not match to type of property "
                                        + parentProperty.getQualifiedName()
                                        + " in case of reference " + ref.getQualifiedName());
                            }
                        }
                    }
                }
            }
        }
    }

    private void checkDetailColumnProp(final AdsDetailColumnPropertyDef prop, final IProblemHandler problemHandler) {
        checkColumnProp(prop, problemHandler);
        checkDetailProp(prop, problemHandler);
    }

    private void checkDetailRefProp(final AdsDetailRefPropertyDef prop, AdsDefinition referencedDef, final IProblemHandler problemHandler) {
        checkDetailProp(prop, problemHandler);
        checkParentRefProp(prop, referencedDef, problemHandler);
    }

    private void checkDynamicProp(AdsDynamicPropertyDef prop, IProblemHandler problemHandler) {
        if (prop.getNature() == EPropNature.SQL_CLASS_PARAMETER) {
            final EValType valType = prop.getValue().getType().getTypeId();
            if (valType == EValType.PARENT_REF || valType == EValType.ARR_REF) {
                final AdsEntityObjectClassDef entity = AdsParameterPropertyDef.findEntity(prop);
                if (entity == null) {
                    error(prop, problemHandler, "Entity not found for parameter");
                }
            }
        }

        if (prop.getNature() == EPropNature.SQL_CLASS_PARAMETER && prop.getOwnerClass().getClassDefType() == EClassType.REPORT) {
            final ServerPresentationSupport support = prop.getPresentationSupport();
            if (support != null) {
                final PropertyPresentation pp = support.getPresentation();
                if (pp != null) {
                    if (!((AdsReportClassDef) prop.getOwnerClass()).isSubreport() && pp.getTitleId() == null) {
                        warning(prop, problemHandler, "Untitled report parameter");
                    }
                }
            }
        }

        final AdsAccessFlags accessFlags = prop.getAccessFlags();
        if (accessFlags.isAbstract()) {
            if (accessFlags.isStatic()) {
                error(prop, problemHandler, "Illegal combination of modifiers: abstract and static");
            }
            if (accessFlags.isFinal()) {
                error(prop, problemHandler, "Illegal combination of modifiers: abstract and final");
            }
            if (accessFlags.isPrivate()) {
                error(prop, problemHandler, "Illegal combination of modifiers: abstract and private");
            }
            if (prop.isSetterDefined(EScope.LOCAL) || prop.isGetterDefined(EScope.LOCAL)) {
                error(prop, problemHandler, "Abstract property can't contains custom getter/setter");
            }
        }
    }

    private void checkExpressionProp(final AdsExpressionPropertyDef prop, AdsPropertyDef ovr, AdsPropertyDef ovrt, final IProblemHandler problemHandler) {
        final Sqml expression = prop.getExpresssion();

        if (expression == null || expression.getItems().isEmpty()) {
            error(prop, problemHandler, "Expression not defined");

        }
        if (prop.isInvisibleForArte()) {
            if (prop.isGetterDefined(EScope.ALL) || prop.isSetterDefined(EScope.ALL)) {
                error(prop, problemHandler, "User-defined getters and setters are not allowed for pure sql properties");

            }
        }
        if (ovr != null && ovr.getNature() == EPropNature.EXPRESSION) {
            if (prop.isInvisibleForArte() != ((AdsExpressionPropertyDef) ovr).isInvisibleForArte()) {
                if (prop.isInvisibleForArte()) {
                    error(prop, problemHandler, "Property should not be pure sql, because it overrides not pure sql property " + ovr.getQualifiedName());

                } else {
                    error(prop, problemHandler, "Property should be pure sql, because it overrides pure sql property " + ovr.getQualifiedName());

                }
            }
        }
        if (ovrt != null && ovrt.getNature() == EPropNature.EXPRESSION) {
            if (prop.isInvisibleForArte() != ((AdsExpressionPropertyDef) ovrt).isInvisibleForArte()) {
                if (prop.isInvisibleForArte()) {
                    error(prop, problemHandler, "Property should not be pure sql, because it overwrites not pure sql property " + ovrt.getQualifiedName());

                } else {
                    error(prop, problemHandler, "Property should be pure sql, because it overwrites pure sql property " + ovrt.getQualifiedName());

                }
            }
        }
    }

    private void checkFieldProp(final AdsFieldPropertyDef prop, final IProblemHandler problemHandler) {
        final int idx = prop.calcIndex();

        if (idx == 0) {
            warning(prop, problemHandler, "Field not used in sql");

        }

        final String dbName = prop.getName();

        if (!DbNameUtils.isCorrectDbName(dbName)) {
            error(prop, problemHandler, "Illegal database name: '" + String.valueOf(dbName) + "'");

        }
    }

    private void checkFieldRefProp(final AdsFieldRefPropertyDef prop, final AdsType resolvedType, final IProblemHandler problemHandler) {
        if (resolvedType instanceof ParentRefType && ((ParentRefType) resolvedType).getSource() != null) {
            final DdsTableDef referencedTable = ((ParentRefType) resolvedType).getSource().findTable(prop);

            if (referencedTable != null) {
                final AdsClassDef thisClass = prop.getOwnerClass();
                final List<AdsFieldRefPropertyDef.RefMapItem> items = prop.getFieldToColumnMap().list();
                final List<Id> columnIds = new ArrayList<>();

                for (AdsFieldRefPropertyDef.RefMapItem item : items) {
                    final AdsPropertyDef fieldProp = thisClass.getProperties().findById(item.getFieldId(), EScope.ALL).get();
                    if (fieldProp == null) {
                        error(prop, problemHandler, "Unknown field in field to column record");
                    }

                    final DdsColumnDef column = referencedTable.getColumns().findById(item.getColumnId(), EScope.LOCAL_AND_OVERWRITE).get();

                    if (column == null) {
                        error(prop, problemHandler, "Unknown column in field to column record");
                    } else {
                        columnIds.add(column.getId());
                    }

                    if (fieldProp != null && column != null) {
                        if (!fieldProp.getValue().getType().isBasedOn(column.getValType())) {
                            error(prop, problemHandler, MessageFormat.format("Type of field {0} does not match to type of column {1}", fieldProp.getName(), column.getQualifiedName()));
                        }
                    }
                }
                final List<DdsIndexDef> indices = new LinkedList<>();
                indices.add(referencedTable.getPrimaryKey());
                for (DdsIndexDef index : referencedTable.getIndices().get(EScope.ALL)) {
                    if (!indices.contains(index)) {
                        indices.add(index);
                    }
                }
                DdsIndexDef usedIndex = null;
                for (DdsIndexDef index : indices) {
                    boolean completed = true;
                    for (DdsIndexDef.ColumnInfo info : index.getColumnsInfo()) {
                        if (!columnIds.contains(info.getColumnId())) {
                            completed = false;
                        }
                    }
                    if (completed) {
                        usedIndex = index;
                        break;
                    }
                }
                if (usedIndex == null) {
                    error(prop, problemHandler, MessageFormat.format("No index of table {0} matches to field column set", referencedTable.getQualifiedName()));
                }
            } else {
                error(prop, problemHandler, "Referenced table not found");
            }
        } else {
            error(prop, problemHandler, "Type of cursor field reference must refer to entity class");
        }

    }

    private void checkTransmittableProp(final AdsTransmittablePropertyDef prop, final IProblemHandler problemHandler) {

    }

    private void checkPropertyPresentationProp(final AdsPropertyPresentationPropertyDef prop, final IProblemHandler problemHandler) {
        if (prop.getAccessFlags().isStatic()) {
            error(prop, problemHandler, "Property presentation can not be static");
        }
        final AdsClassDef ownerClass = prop.getOwnerClass();

        if (ownerClass instanceof AdsModelClassDef) {
            final AdsClassDef modelOwner = ((AdsModelClassDef) ownerClass).findServerSideClasDef();
            if (modelOwner == null && !prop.isLocal()) {
                error(prop, problemHandler, "Model owner class not found");
            } else {
                final IModelPublishableProperty source = prop.findServerSideProperty();
                if (source == null) {
                    error(prop, problemHandler, "Server side property not found");
                } else {
                    if (!prop.isLocal() && !source.isTransferable(prop.getClientEnvironment())) {
                        error(prop, problemHandler, MessageFormat.format("Property {0} can not be used for client-server interaction for client environment " + prop.getClientEnvironment().getName(), ((RadixObject) source).getQualifiedName(prop)));
                    }
                }
            }
        }
    }

    private void checkInnateColumnProp(final AdsInnateColumnPropertyDef prop, final IProblemHandler problemHandler) {
        checkColumnProp(prop, problemHandler);
    }

    private void checkInnateRefProp(final AdsInnateRefPropertyDef prop, AdsDefinition referencedDef, final IProblemHandler problemHandler) {
        checkParentRefProp(prop, referencedDef, problemHandler);
        // RADIX-2160
        final ParentReferenceInfo refInfo = prop.getParentReferenceInfo();
        final DdsReferenceDef ref = refInfo.findParentReference();
        if (ref != null && ref.getType() != DdsReferenceDef.EType.MASTER_DETAIL) {
            final AdsEntityObjectClassDef entityClass = (AdsEntityObjectClassDef) ((AdsPropertyDef) prop).getOwnerClass();
            for (DdsReferenceDef.ColumnsInfoItem item : ref.getColumnsInfo()) {
                final DdsColumnDef childColumn = item.findChildColumn();
                if (childColumn != null && entityClass.getProperties().findById(childColumn.getId(), EScope.ALL).get() == null) {
                    error(prop, problemHandler, MessageFormat.format("Child column {0} is not published", childColumn.getQualifiedName()));
                }
            }
        }
    }

    private void checkUserProp(final AdsUserPropertyDef prop, AdsPropertyDef ovr, AdsPropertyDef ovrt, final IProblemHandler problemHandler) {
        if (prop.isAuditUpdate()) {
            boolean isTableAudited = false;
            final AdsEntityObjectClassDef clazz = (AdsEntityObjectClassDef) prop.getOwnerClass();
            String tableName = "base table";
            if (clazz != null) {
                final DdsTableDef table = clazz.findTable(prop);
                if (table != null) {
                    isTableAudited = table.getAuditInfo().isEnabledForUpdate();
                    tableName = table.getQualifiedName();
                }
            }
            if (!isTableAudited) {
                error(prop, problemHandler, "Audit update is not enabled for table " + tableName);
            }
            checkTitleAvailabilityForAuditProperty(prop, problemHandler);
        } else {
            if (((ovr instanceof AdsUserPropertyDef && ((AdsUserPropertyDef) ovr).isAuditUpdate()) || (ovrt instanceof AdsUserPropertyDef && ((AdsUserPropertyDef) ovrt).isAuditUpdate())) && !prop.isWarningSuppressed(AdsPropertyDef.Problems.AUDIT_UPDATE_SHOULD_BE_ENABLED)) {
                warning(prop, problemHandler, AdsPropertyDef.Problems.AUDIT_UPDATE_SHOULD_BE_ENABLED);
            }
        }
    }

    private void checkParentProp(final AdsParentPropertyDef prop, final IProblemHandler problemHandler) {
        final ParentInfo info = prop.getParentInfo();
        final List<AdsPropertyDef> path = new LinkedList<>();

        if (info.findOriginalProperty(path, problemHandler) == null) {
            error(prop, problemHandler, "Original property not found");
        }
    }

    private void checkAccessors(final AdsPropertyDef prop, final IProblemHandler problemHandler) {

        checkAccessors(prop, "set", problemHandler);
        checkAccessors(prop, "get", problemHandler);

        if (prop.isSetReadAccess() && prop.isSetWriteAccess()) {
            error(prop, problemHandler, "Cannot specify accessibility modifiers for both accessors of the property");
        }

        if (prop.isReadOnly() && (prop.isSetReadAccess() || prop.isSetWriteAccess())) {
            error(prop, problemHandler, "Accessibility modifiers on accessors may only be used if the property has both a get and a set accessor");
        }
    }

    private void checkAccessors(final AdsPropertyDef prop, String accessorType, IProblemHandler problemHandler) {
        if (prop != null) {
            final EAccess access = accessorType.equals("set") ? prop.getWriteAccess() : prop.getReadAccess();
            final boolean isSetAccess = accessorType.equals("set") ? prop.isSetWriteAccess() : prop.isSetReadAccess();
            final EAccess propAccess = prop.getAccessMode();

            if (isSetAccess && propAccess.isLess(access)) {
                error(prop, problemHandler, "The accessibility modifier of the '" + accessorType + "' accessor must be more restrictive than the modifier of the property");
            }

            if (prop.getAccessFlags().isAbstract() && access == EAccess.PRIVATE) {
                error(prop, problemHandler, "Abstract properties cannot have private accessors");
            }
        }
    }

    private void checkReadonly(final AdsPropertyDef prop, IProblemHandler problemHandler) {
        final AdsPropertyDef overridden = prop.getHierarchy().findOverridden().get();

        if (overridden != null) {
            if (!overridden.isConst() && prop.isConst()) {
                error(prop, problemHandler, "The property " + prop.getName() + " cannot be readonly because overridden property is not readonly");
            }
        }
    }
}
