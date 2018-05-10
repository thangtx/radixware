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
package org.radixware.kernel.common.defs.ads.src.clazz;

import java.util.*;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsParentPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyPresentationPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.ServerPresentationSupport;
import org.radixware.kernel.common.defs.ads.clazz.presentation.*;
import org.radixware.kernel.common.defs.ads.clazz.presentation.CreatePresentationsList.PresentationRef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.editmask.EditMask;
import org.radixware.kernel.common.defs.ads.common.AdsCondition;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumDef;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.UsagePurpose;
import org.radixware.kernel.common.defs.ads.src.RadixObjectWriter;
import org.radixware.kernel.common.defs.ads.src.WriterUtils;
import org.radixware.kernel.common.defs.ads.type.AdsClassType;
import org.radixware.kernel.common.defs.ads.type.AdsEnumType;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.enums.EPropAttrInheritance;
import org.radixware.kernel.common.enums.EPropNature;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.CharOperations;

public class PropertyPresentationWriter extends RadixObjectWriter<PropertyPresentation> {

    private final transient AdsPropertyDef ownerProp;

    public PropertyPresentationWriter(JavaSourceSupport support, PropertyPresentation target, UsagePurpose usagePurpose) {
        super(support, target, usagePurpose);
        this.ownerProp = def.getOwnerProperty();
    }

    @Override
    public void writeUsage(final CodePrinter printer) {
        //do not used
    }
    /**
     * public RadPropertyPresentationDef( final Id propId, final Id constSetId,
     * final EEditPossibility editPossibility, final boolean isNotNull, final
     * boolean readSeparately, final EnumSet<EPropAttrInheritance>
     * inheritanceMask)
     *
     * public RadParentTitlePropertyPresentationDef( final Id propId, final
     * EEditPossibility editPossibility, final boolean isNotNull, final boolean
     * readSeparately, final RadEntityTitleFormatDef parentTitleFormat, final Id
     * parentSelectorPresentationId, final RadConditionDef
     * parentSelectCondition, final int parentSelectorRestrictions, final Id
     * parentClassCatalogId, final EnumSet<EPropAttrInheritance>
     * inheritanceMask)
     */
    static final char[] PROPERTY_PRESENTATION_CLASS_NAME = CharOperations.merge(WriterUtils.PRESENTATIONS_META_SERVER_PACKAGE_NAME, "RadPropertyPresentationDef".toCharArray(), '.');
    private static final char[] PARENT_TITLE_PROPERTY_PRESENTATION_CLASS_NAME = CharOperations.merge(WriterUtils.PRESENTATIONS_META_SERVER_PACKAGE_NAME, "RadParentTitlePropertyPresentationDef".toCharArray(), '.');
    static final char[] EXPLORER_PARENT_REF_PROPERTY_PRESENTATION_CLASS_NAME = CharOperations.merge(WriterUtils.PRESENTATIONS_META_EXPLORER_PACKAGE_NAME, "RadParentRefPropertyDef".toCharArray(), '.');
    static final char[] EXPLORER_FILTER_PARAM_PRESENTATION_CLASS_NAME = CharOperations.merge(WriterUtils.FILTERS_META_EXPLORER_PACKAGE_NAME, "RadFilterParamDef".toCharArray(), '.');
    static final char[] EXPLORER_PROPERTY_PRESENTATION_CLASS_NAME = CharOperations.merge(WriterUtils.PRESENTATIONS_META_EXPLORER_PACKAGE_NAME, "RadPropertyDef".toCharArray(), '.');

    private Collection<PropertyPresentation> collectOverwrittenPresentations() {
        final ArrayList<PropertyPresentation> ppss = new ArrayList<>();
        AdsPropertyDef ovr = def.getOwnerProperty().getHierarchy().findOverwritten().get();
        while (ovr != null) {
            if (ovr instanceof IAdsPresentableProperty) {
                final ServerPresentationSupport support = ((IAdsPresentableProperty) ovr).getPresentationSupport();
                if (support != null) {
                    ppss.add(support.getPresentation());
                }
            }
            ovr = ovr.getHierarchy().findOverwritten().get();
        }
        return ppss;
    }

    @Override
    protected boolean writeMeta(CodePrinter printer) {

        final Collection<PropertyPresentation> overwritten = collectOverwrittenPresentations();

        final EnumSet<EPropAttrInheritance> finalInheritanceMask = def.getInheritanceMask();

        Id finalTitleId = null;
        Id finalHintId = null;

        if (def.isTitleInherited()) {
            for (PropertyPresentation ovr : overwritten) {
                if (!ovr.isTitleInherited()) {
                    finalInheritanceMask.remove(EPropAttrInheritance.TITLE);
                    finalTitleId = ovr.getTitleId();
                    break;
                }
            }
        } else {
            finalTitleId = def.getTitleId();
        }
        if (def.isHintInherited()) {
            for (PropertyPresentation ovr : overwritten) {
                if (!ovr.isHintInherited()) {
                    finalInheritanceMask.remove(EPropAttrInheritance.HINT);
                    finalHintId = ovr.getHintId();
                    break;
                }
            }
        } else {
            finalHintId = def.getHintId();
        }

        PropertyEditOptions finalEditOptions = null;
        if (def.isEditOptionsInherited()) {
            for (PropertyPresentation ovr : overwritten) {
                if (!ovr.isEditOptionsInherited()) {
                    finalInheritanceMask.remove(EPropAttrInheritance.EDITING);
                    finalEditOptions = ovr.getEditOptions();
                    break;
                }
            }
            if (finalEditOptions == null) {
                finalEditOptions = def.getEditOptions();
            }
        } else {
            finalEditOptions = def.getEditOptions();
        }

        switch (usagePurpose.getEnvironment()) {
            case WEB:
            case EXPLORER:
                /**
                 *
                 * public RadPropertyDef(	final Id id, final String name, final
                 * Id titleId, final Id titleOwnerId,//ID of owner class
                 *
                 * final EValType type, final EPropNature nature, final long
                 * inheritanceMask, final Id ownerEntityId,//Для UserProp,
                 * ParentProp, DetailProp – сущность, которой принадлежит
                 * свойство final Id origPropId,//Для ParentProp, DetailProp –
                 * ид. оригинального свойства final Id constSetId, final boolean
                 * readSeparately,
                 *
                 * final boolean isInheritable, final ValAsStr
                 * valInheritableMark, final ValAsStr initVal,
                 *
                 * final EEditPossibility editPossibility, final boolean
                 * mandatory, final boolean storeHistory, final boolean
                 * customDialog, final Id customDialogId, final boolean
                 * customEditOnly, final EditMask editMask, final Id
                 * nullStringId, final boolean isDuplicatesEnabled,
                 * //onlyForArrayProperties final boolean isMemo )
                 */
                printer.print("new ");
                final AdsClassDef clazz = ownerProp.getOwnerClass();
                final ParentRefPropertyPresentation pps = def instanceof ParentRefPropertyPresentation ? (ParentRefPropertyPresentation) def : null;
                final ObjectPropertyPresentation ops = def instanceof ObjectPropertyPresentation ? (ObjectPropertyPresentation) def : null;

                final boolean isRefProp = def instanceof ParentRefPropertyPresentation || def instanceof ObjectPropertyPresentation;
                if (isRefProp) {
                    printer.print(EXPLORER_PARENT_REF_PROPERTY_PRESENTATION_CLASS_NAME);
                } else {
                    printer.print(EXPLORER_PROPERTY_PRESENTATION_CLASS_NAME);
                }
                printer.print('(');
                printer.enterBlock();
                WriterUtils.writeIdUsage(printer, ownerProp.getId());
                printer.printComma();
                printer.println();
                printer.printStringLiteral(ownerProp.getName());
                printer.printComma();
                printer.println();

                if (finalInheritanceMask.contains(EPropAttrInheritance.TITLE)) {
                    WriterUtils.writeNull(printer);
                } else {
                    WriterUtils.writeIdUsage(printer, finalTitleId);
                }

                printer.printComma();
                printer.println();


                if (finalInheritanceMask.contains(EPropAttrInheritance.HINT)) {
                    WriterUtils.writeNull(printer);
                } else {
                    WriterUtils.writeIdUsage(printer, finalHintId);
                }

                printer.printComma();
                printer.println();
                final Id titleOwnerId;
                if (!clazz.isTopLevelDefinition()) {
                    AdsDefinition def = clazz.findTopLevelDef();
                    if (def == null) {
                        return false;
                    } else {
                        titleOwnerId = def.getId();
                    }
                } else {
                    titleOwnerId = clazz.getId();
                }

                WriterUtils.writeIdUsage(printer, titleOwnerId);
                printer.printComma();
                printer.println();
                WriterUtils.writeEnumFieldInvocation(printer, ownerProp.getValue().getType().getTypeId());
                printer.printComma();
                printer.println();

                EPropNature nature = ownerProp.getNature();
                if (ownerProp.getContainer() instanceof AdsPropertyPresentationPropertyDef) {
                    nature = EPropNature.VIRTUAL;
                }
                WriterUtils.writeEnumFieldInvocation(printer, nature);
                printer.printComma();
                printer.println();
                printer.print(EPropAttrInheritance.toBitField(finalInheritanceMask));
                printer.printComma();
                printer.println();
                switch (ownerProp.getNature()) {
                    case PARENT_PROP:
                        final AdsParentPropertyDef pp = (AdsParentPropertyDef) ownerProp;
                        final AdsPropertyDef op = pp.getParentInfo().findOriginalProperty();
                        if (op != null) {
                            final AdsClassDef pc = op.getOwnerClass();
                            WriterUtils.writeIdUsage(printer, pc.getId());
                            printer.printComma();
                            printer.println();
                            WriterUtils.writeIdUsage(printer, op.getId());
                        } else {
                            //target ptoperty might be not published
                            AdsClassDef targetClass = pp.getParentInfo().findOriginalPropertyOwnerClass();
                            if (targetClass != null && targetClass.getApiPropIds().contains(pp.getParentInfo().getOriginalPropertyId())) {
                                WriterUtils.writeIdUsage(printer, targetClass.getId());
                                printer.printComma();
                                printer.println();
                                WriterUtils.writeIdUsage(printer, pp.getParentInfo().getOriginalPropertyId());
                            } else {
                                WriterUtils.writeNull(printer);
                                printer.printComma();
                                printer.println();
                                WriterUtils.writeNull(printer);
                            }
                        }
                        break;
                    default:
                        WriterUtils.writeNull(printer);
                        printer.printComma();
                        printer.println();
                        WriterUtils.writeNull(printer);
                }
                final PropertyEditOptions editOptions = finalEditOptions;
                printer.printComma();
                printer.println();
                //const set id
                final AdsType type = ownerProp.getValue().getType().resolve(ownerProp).get();

                if (!isRefProp) {
                    if (type instanceof AdsEnumType) {
                        final AdsEnumDef e = ((AdsEnumType) type).getSource();
                        WriterUtils.writeIdUsage(printer, e.getId());
                    } else {
                        WriterUtils.writeNull(printer);
                    }
                    printer.printComma();
                    printer.println();
                    printer.print(editOptions.isReadSeparately());
                    printer.printComma();
                    printer.println();
                }


                printer.print(ownerProp.getValueInheritanceRules().getInheritable());
                printer.printComma();
                printer.println();
                WriterUtils.writeRadixValAsStr(printer, ownerProp.getValueInheritanceRules().getInheritanceMark());
                printer.printComma();
                printer.println();
                WriterUtils.writeRadixValAsStr(printer, ownerProp.getValue().getInitial());
                printer.printComma();
                printer.println();

                printer.println("/*Edit options*/");
                WriterUtils.writeEnumFieldInvocation(printer, editOptions.getEditPossibility());
                printer.printComma();
                printer.println();
                WriterUtils.writeEnumFieldInvocation(printer, editOptions.getEditEnvironment());
                printer.printComma();
                printer.println();
                printer.print(editOptions.isNotNull());
                printer.printComma();
                //RADIX-4678:if (!isRefProp) {
                printer.print(editOptions.isArrayElementMandatory());
                printer.printComma();
                //}
                printer.println();
                printer.print(editOptions.isStoreEditHistory());
                printer.printComma();
                printer.println();
                printer.print(editOptions.isShowDialogButton());
                printer.printComma();
                printer.println();
                WriterUtils.writeIdUsage(printer, editOptions.getCustomDialogId(usagePurpose.getEnvironment()));
                printer.printComma();
                printer.println();
                printer.print(editOptions.isCustomEditOnly());
                printer.printComma();
                printer.println();
                EditMask editMask = editOptions.getEditMask();
                //if not eddit mask defined use default edit mask
                if (editMask == null) {
                    editMask = EditMask.Factory.newDefaultInstance(editOptions);
                    //WriterUtils.writeNull(printer);
                }// else {
                if (editMask == null) {
                    WriterUtils.writeNull(printer);

                } else {
                    writeCode(printer, editMask);
                }
                //}
                printer.printComma();
                printer.println();
                WriterUtils.writeIdUsage(printer, editOptions.getNullValTitleId());
                printer.printComma();
                printer.println();
                //RADIX-4678:if (!isRefProp) {
                WriterUtils.writeIdUsage(printer, editOptions.getNullArrayElementTitleId());
                printer.printComma();
                printer.println();
                //}

                WriterUtils.writeIdUsage(printer, editOptions.getEmptyArrayValTitleId());
                printer.printComma();
                printer.println();

                printer.print(editOptions.isDuplicatesEnabled());
                printer.printComma();

                printer.print(editOptions.getMinArrayItemCount());
                printer.printComma();
                printer.print(editOptions.getMaxArrayItemCount());
                printer.printComma();
                printer.print(editOptions.getFirstArrayItemIndex());
                printer.printComma();


                printer.println();
                if (isRefProp) {
                    if (type instanceof AdsClassType) {
                        final AdsClassDef targetClass = ((AdsClassType) type).getSource();
                        if (targetClass != null) {
                            WriterUtils.writeIdUsage(printer, targetClass.getId());
                        } else {
                            WriterUtils.writeNull(printer);
                        }
                        printer.printComma();
                        printer.println();
                        if (targetClass instanceof AdsEntityObjectClassDef) {
                            WriterUtils.writeIdUsage(printer, ((AdsEntityObjectClassDef) targetClass).getEntityId());
                        } else {
                            WriterUtils.writeNull(printer);
                        }
                    } else {
                        WriterUtils.writeNull(printer);
                        printer.printComma();
                        printer.println();
                        WriterUtils.writeNull(printer);
                    }
                    printer.printComma();
                    printer.println();

                    //check for client env
                    final List<AdsEditorPresentationDef> editorPresentations = editOptions.findObjectEditorPresentations();
                    final List<Id> editorPresentationIds = new ArrayList<>();

                    if (editorPresentations != null && !editorPresentations.isEmpty()) {
                        for (AdsEditorPresentationDef editorPresentation : editorPresentations) {

                            if (editorPresentation.getEffectiveClientEnvironment() == ERuntimeEnvironmentType.COMMON_CLIENT
                                    || editorPresentation.getClientEnvironment() == usagePurpose.getEnvironment()) {
                                editorPresentationIds.add(editorPresentation.getId());
                            }
                        }
                    }
                    if (editorPresentationIds.isEmpty()) {
                        WriterUtils.writeNull(printer);
                    } else {
                        WriterUtils.writeIdArrayUsage(printer, editorPresentationIds);
                    }

                    printer.printComma();
                    printer.println();
                    if (ops != null) {
                        //we need to filter presentations against current environment
                        List<Id> ids = new LinkedList<>();
                        for (PresentationRef ref : ops.getCreatePresentationsList().getPresentationRefs()) {
                            AdsEditorPresentationDef epr = ref.findEditorPresentation();
                            if (epr == null) {
                                ids.add(ref.getPresentationId());
                            } else {
                                if (epr.getEffectiveClientEnvironment() == ERuntimeEnvironmentType.COMMON_CLIENT || epr.getClientEnvironment() == usagePurpose.getEnvironment()) {
                                    ids.add(epr.getId());
                                }
                            }
                        }

                        WriterUtils.writeIdArrayUsage(printer, ids);
                    } else {
                        WriterUtils.writeNull(printer);
                    }
                    printer.printComma();
                    printer.println();
                    if (pps != null) {
                        ParentRefPropertyPresentation.ParentSelect ps = pps.getParentSelect();
                        if (pps.getInheritanceMask().contains(EPropAttrInheritance.PARENT_SELECTOR)) {
                            WriterUtils.writeNull(printer);
                        } else {
                            AdsSelectorPresentationDef spr = ps.findParentSelectorPresentation();
                            if (spr == null) {
                                //not found. this error must be checked by check operation
                                WriterUtils.writeIdUsage(printer, ps.getParentSelectorPresentationId());
                            } else {
                                if (spr.getClientEnvironment() == ERuntimeEnvironmentType.COMMON_CLIENT || spr.getClientEnvironment() == usagePurpose.getEnvironment()) {
                                    WriterUtils.writeIdUsage(printer, ps.getParentSelectorPresentationId());
                                } else {
                                    WriterUtils.writeIdUsage(printer, null);
                                }
                            }
                        }
                        printer.printComma();
                        printer.println();
                        writeCode(printer, pps.getParentSelectorRestrictions());
                        printer.printComma();
                        printer.println();
                        writeCode(printer, pps.getParentEditorRestrictions());
                    } else {
                        WriterUtils.writeNull(printer);
                        printer.printComma();
                        printer.println();
                        printer.print("0L");
                        printer.printComma();
                        printer.println();
                        printer.print("0L");
                    }
                } else {
                    printer.print(editOptions.isMemo());
                    printer.printComma();
                    printer.print(ownerProp.canBeUsedInSorting());
                    printer.printComma();
                    WriterUtils.writeEnumFieldInvocation(printer, editOptions.getValueStorePossibility());
                }
                printer.printComma();
                printer.print(ownerProp.isDeprecated());
                
                if (isRefProp && (ops != null)){
                    printer.printComma();
                    printer.print(ops.isAutoSortClasses());
                }
                
                printer.print(')');
                printer.leaveBlock();
                return true;

            case SERVER:
                printer.print("new ");

                if (def instanceof ParentRefPropertyPresentation || def instanceof ObjectPropertyPresentation) {
                    /**
                     * public RadParentTitlePropertyPresentationDef( final Idra
                     * propId, final EEditPossibility editPossibility, final
                     * boolean isNotNull, final boolean readSeparately, final
                     * RadEntityTitleFormatDef parentTitleFormat, final Id
                     * parentSelectorPresentationId, final RadConditionDef
                     * parentSelectCondition, final int
                     * parentSelectorRestrictions, final Id
                     * parentClassCatalogId, final EnumSet<EPropAttrInheritance>
                     * inheritanceMask) {
                     */
                    final ParentRefPropertyPresentation rp = def instanceof ParentRefPropertyPresentation ? (ParentRefPropertyPresentation) def : null;
                    AdsObjectTitleFormatDef finalTitleFormat = null;
                    AdsCondition finalParentSelectCondition = null;
                    Id finalParentSelectorPresentationId = null;
                    if (rp != null) {
                        if (rp.getParentTitle().isParentTitleFormatInherited()) {
                            for (PropertyPresentation ovr : overwritten) {
                                if (ovr instanceof ParentRefPropertyPresentation) {
                                    ParentRefPropertyPresentation ovrp = (ParentRefPropertyPresentation) ovr;
                                    if (!ovrp.getParentTitle().isParentTitleFormatInherited()) {
                                        finalTitleFormat = ovrp.getParentTitle().getTitleFormat();
                                        finalInheritanceMask.remove(EPropAttrInheritance.PARENT_TITLE_FORMAT);
                                    }
                                }
                            }
                        } else {
                            finalTitleFormat = rp.getParentTitle().getTitleFormat();
                        }
                        if (rp.getParentSelect().isParentSelectConditionInherited()) {
                            for (PropertyPresentation ovr : overwritten) {
                                if (ovr instanceof ParentRefPropertyPresentation) {
                                    ParentRefPropertyPresentation ovrp = (ParentRefPropertyPresentation) ovr;
                                    if (!ovrp.getParentSelect().isParentSelectConditionInherited()) {
                                        finalParentSelectCondition = ovrp.getParentSelect().getParentSelectorCondition();
                                        finalInheritanceMask.remove(EPropAttrInheritance.PARENT_SELECT_CONDITION);
                                    }
                                }
                            }
                        } else {
                            finalParentSelectCondition = rp.getParentSelect().getParentSelectorCondition();
                        }
                        if (rp.getParentSelect().isParentSelectorInherited()) {
                            for (PropertyPresentation ovr : overwritten) {
                                if (ovr instanceof ParentRefPropertyPresentation) {
                                    ParentRefPropertyPresentation ovrp = (ParentRefPropertyPresentation) ovr;
                                    if (!ovrp.getParentSelect().isParentSelectorInherited()) {
                                        finalParentSelectorPresentationId = ovrp.getParentSelect().getParentSelectorPresentationId();
                                        finalInheritanceMask.remove(EPropAttrInheritance.PARENT_SELECTOR);
                                    }
                                }
                            }
                        } else {
                            finalParentSelectorPresentationId = rp.getParentSelect().getParentSelectorPresentationId();
                        }
                    }


                    final ObjectPropertyPresentation op = def instanceof ObjectPropertyPresentation ? (ObjectPropertyPresentation) def : null;
                    printer.print(PARENT_TITLE_PROPERTY_PRESENTATION_CLASS_NAME);
                    printer.print('(');
                    WriterUtils.writeIdUsage(printer, ownerProp.getId());
                    printer.printComma();
                    WriterUtils.writeEnumFieldInvocation(printer, finalEditOptions.getEditPossibility());
                    printer.printComma();
                    printer.print(finalEditOptions.isNotNull());
                    printer.printComma();
                    printer.print(finalEditOptions.isReadSeparately());
                    printer.printComma();
                    if (finalTitleFormat != null) {
                        finalTitleFormat.getJavaSourceSupport().getCodeWriter(UsagePurpose.getPurpose(ERuntimeEnvironmentType.SERVER, JavaSourceSupport.CodeType.META)).writeCode(printer);
                    } else {
                        WriterUtils.writeNull(printer);
                    }
                    printer.printComma();
                    if (finalParentSelectorPresentationId != null) {
                        WriterUtils.writeIdUsage(printer, finalParentSelectorPresentationId);
                    } else {
                        WriterUtils.writeNull(printer);
                    }
                    printer.printComma();
                    if (finalParentSelectCondition != null) {
                        finalParentSelectCondition.getJavaSourceSupport().getCodeWriter(UsagePurpose.getPurpose(ERuntimeEnvironmentType.SERVER, JavaSourceSupport.CodeType.META)).writeCode(printer);
                    } else {
                        WriterUtils.writeNull(printer);
                    }

                    printer.printComma();
                    if (rp != null) {
                        //printer.print(rp.getParentSelectorRestrictions().toBitField());
                        writeCode(printer, rp.getParentSelectorRestrictions());
                    } else {
                        //printer.print(0L);
                        WriterUtils.writeNull(printer);
                    }
                    printer.printComma();
                    final List<Id> objectEditorPresentations = def.getEditOptions().getObjectEditorPresentations();
                    if (def.getEditOptions() != null && objectEditorPresentations != null && !objectEditorPresentations.isEmpty()) {
                        WriterUtils.writeIdArrayUsage(printer, objectEditorPresentations);
                    } else {
                        WriterUtils.writeNull(printer);
                    }
                    printer.printComma();
                    if (op != null) {
                        WriterUtils.writeIdUsage(printer, op.getCreationClassCatalogId());//class catalog
                        printer.printComma();
                        WriterUtils.writeIdArrayUsage(printer, op.getCreatePresentationsList().getPresentationIds());//class catalog
                    } else {
                        WriterUtils.writeNull(printer);
                        printer.printComma();
                        WriterUtils.writeNull(printer);
                    }
                    printer.printComma();
                    WriterUtils.writeEnumSet(printer, finalInheritanceMask, EPropAttrInheritance.class);
                    printer.print(')');

                } else {
                    printer.print(PROPERTY_PRESENTATION_CLASS_NAME);
                    printer.print('(');
                    WriterUtils.writeIdUsage(printer, ownerProp.getId());
                    printer.printComma();
                    //see RADIX-4078
                    /*
                     * final AdsType resolvedType =
                     * ownerProp.getValue().getType().resolve(ownerProp); if
                     * (resolvedType instanceof AdsEnumType) {
                     * WriterUtils.writeIdUsage(printer, ((AdsEnumType)
                     * resolvedType).getSource().getId()); } else {
                     * WriterUtils.writeNull(printer); } printer.printComma();
                     */
                    WriterUtils.writeEnumFieldInvocation(printer, finalEditOptions.getEditPossibility());
                    printer.printComma();
                    printer.print(finalEditOptions.isNotNull());
                    printer.printComma();
                    printer.print(finalEditOptions.isReadSeparately());
                    printer.printComma();
                    WriterUtils.writeEnumSet(printer, finalInheritanceMask, EPropAttrInheritance.class);
                    printer.print(')');
                }
                return true;

            default:
                return false;
        }
    }
}
