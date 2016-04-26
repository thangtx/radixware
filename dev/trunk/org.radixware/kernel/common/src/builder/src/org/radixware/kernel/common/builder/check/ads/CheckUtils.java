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
package org.radixware.kernel.common.builder.check.ads;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.builder.check.common.CheckHistory;
import static org.radixware.kernel.common.builder.check.common.RadixObjectChecker.warning;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.check.ProblemAnnotationFactory;
import org.radixware.kernel.common.check.RadixProblem;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.DefinitionSearcher;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.AdsDefinition.Domains;
import org.radixware.kernel.common.defs.ads.AdsDefinitionProblems;
import org.radixware.kernel.common.defs.ads.AdsDomainDef;
import org.radixware.kernel.common.defs.ads.AdsValAsStr;
import org.radixware.kernel.common.defs.ads.IInheritableTitledDefinition;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.ServerPresentationSupport;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPageDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPresentationDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsSelectorPresentationDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.IAdsPresentableProperty;
import org.radixware.kernel.common.defs.ads.clazz.presentation.ParentRefPropertyPresentation;
import org.radixware.kernel.common.defs.ads.clazz.presentation.PropertyPresentation;
import org.radixware.kernel.common.defs.ads.clazz.presentation.editmask.*;
import org.radixware.kernel.common.defs.ads.common.AdsUtils;
import org.radixware.kernel.common.defs.ads.common.Restrictions;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumDef;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsSelectorExplorerItemDef;
import org.radixware.kernel.common.defs.ads.module.AdsImageDef;
import org.radixware.kernel.common.defs.ads.module.AdsPath;
import org.radixware.kernel.common.defs.ads.module.AdsSearcher;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.defs.localization.ILocalizingBundleDef;
import org.radixware.kernel.common.defs.localization.IMultilingualStringDef;
import org.radixware.kernel.common.enums.EAccess;
import org.radixware.kernel.common.enums.EDateTimeStyle;
import org.radixware.kernel.common.enums.ERestriction;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.WrongFormatError;
import org.radixware.kernel.common.meta.InputMask;
import org.radixware.kernel.common.types.Id;

public class CheckUtils {

    public static final void checkIconId(AdsDefinition owner, Id iconId, IProblemHandler problemHandler, String usageHint) {
        if (iconId != null) {
            DefinitionSearcher<AdsImageDef> searcher = AdsSearcher.Factory.newImageSearcher(owner);

            if (searcher.findById(iconId).get() == null) {
                problemHandler.accept(RadixProblem.Factory.newError(owner, MessageFormat.format("Image referenced from {0} not found, #{1}", usageHint, iconId)));
            }
        }
    }

    public static final void checkMLStringId(Definition definition, Id stringId, IProblemHandler problemHandler, String usageHint) {
        Definition owner = definition;
        if (owner instanceof IInheritableTitledDefinition) {
            owner = ((IInheritableTitledDefinition) owner).findOwnerTitleDefinition();
        }

        if (stringId != null) {
            final ILocalizingBundleDef bundle = owner.findExistingLocalizingBundle();
            if (bundle == null) {
                problemHandler.accept(RadixProblem.Factory.newError(owner, "Localizing bundle not found"));
            } else {
                final IMultilingualStringDef string = (IMultilingualStringDef) bundle.getStrings().findById(stringId, EScope.LOCAL_AND_OVERWRITE).get();
                if (string == null) {
                    problemHandler.accept(RadixProblem.Factory.newError(owner, MessageFormat.format("Multilingual string referenced from {0} not found: #{1}", usageHint, stringId)));
                }
            }
        }
    }

    public static final void checkDomains(AdsDefinition user, IProblemHandler problemHandler) {
        final Domains domains = user.getDomains();
        final List<AdsPath> domainPathess = domains.getUsedDomainPathes();
        for (AdsPath path : domainPathess) {
            final Definition def = path.resolve(user).get(AdsDefinitionChecker.<Definition>getSearchDuplicatesChecker(user, problemHandler));
            if (!(def instanceof AdsDomainDef)) {
                problemHandler.accept(RadixProblem.Factory.newError(user, "Referenced domain cannot be found: " + path.toString(), ProblemAnnotationFactory.newPathAnnotation(path)));
            } else {
                if (def != null) {
                    AdsUtils.checkAccessibility(user, (AdsDomainDef) def, false, problemHandler);
                }
            }
        }
    }

    private static void editMaskError(EditMask editMask, IProblemHandler handler, String maskType, EValType propType, String containerName) {
        handler.accept(RadixProblem.Factory.newError(editMask, MessageFormat.format("Edit mask of type {0} does not match to " + containerName + " of type {1}", maskType, propType.getName())));
    }

    private static class TitleCheckMap {

        private HashSet<AdsDefinition> cjheckedDefs = new HashSet<AdsDefinition>();
    }

    public static void titleShouldBeDefined(AdsDefinition def, Id titleId, IProblemHandler problemHandler, CheckHistory history) {
        if (titleId == null) {
            TitleCheckMap map = history.findItemByClass(TitleCheckMap.class);
            if (map == null || !map.cjheckedDefs.contains(def)) {
                problemHandler.accept(RadixProblem.Factory.newWarning(def, "Title not defined"));
                if (map == null) {
                    map = new TitleCheckMap();
                    history.registerItemByClass(map);
                }
                map.cjheckedDefs.add(def);
            }
        }
    }

    private static boolean isDefinitionPublished(AdsDefinition def) {
        if (def.isPublished()) {
            return true;
        } else {
            AdsDefinition ovr = def.getHierarchy().findOverwritten().get();
            while (ovr != null) {
                if (ovr.isPublished()) {
                    return true;
                }
                ovr = ovr.getHierarchy().findOverwritten().get();
            }
            ovr = def.getHierarchy().findOverridden().get();
            while (ovr != null) {
                if (ovr.isPublished()) {
                    return true;
                }
                ovr = ovr.getHierarchy().findOverridden().get();
            }
            return false;
        }
    }

    public static boolean checkExportedApiDatails(RadixObject exporter, AdsDefinition exported, IProblemHandler problemHandler) {
        if (exported == null) {
            return true;
        }
        String[] desc = new String[1];
        boolean[] pub = new boolean[1];
        EAccess minimumAccessRequired = ReferenceInfo.minimumRequiredAccessForCorrectAPIExporting(exported, exporter, desc, pub);
        if (exported.getAccessMode().isLess(minimumAccessRequired) || (!isDefinitionPublished(exported) && pub[0])) {
            String hint = desc[0];
            if (hint == null) {
                hint = "";
            }
            problemHandler.accept(RadixProblem.Factory.newError(exporter, "Exporting not " + (pub[0] ? "published" : "public") + " definition " + exported.getQualifiedName() + ". " + hint));
            return false;
        }
        return true;
    }

    public static void checkEditMask(EditMask editMask, EValType valType, AdsEnumDef cs, IProblemHandler handler, AdsTypeDeclaration propType, String containerName) {

        if (editMask == null || valType == EValType.PARENT_REF || valType == EValType.ARR_REF || valType == EValType.BOOL) {
            return;
        }

        final EValType contextValType = editMask.getContextValType();
        if (contextValType == null || contextValType != valType) {
            if (contextValType == null) {
                handler.accept(RadixProblem.Factory.newError(editMask, "Undefined context value type"));
            } else {
                handler.accept(RadixProblem.Factory.newError(editMask, MessageFormat.format("Invalid context value type: {0}, expeted {1}", contextValType.getName(), valType.getName())));
            }

        }

        if (editMask instanceof EditMaskInt) {
            if (valType != EValType.BOOL && valType != EValType.INT && valType != EValType.CHAR
                    && valType != EValType.ARR_BOOL && valType != EValType.ARR_INT && valType != EValType.ARR_CHAR) {
                editMaskError(editMask, handler, "Int", valType, containerName);
            }

            final EditMaskInt m = (EditMaskInt) editMask;
            if (m.getMinValue() != null && m.getMaxValue() != null && m.getMinValue().longValue() >= m.getMaxValue().longValue()) {
                handler.accept(RadixProblem.Factory.newWarning(editMask, "Illegal editmask minimum or maximum value"));
            }

            if (m.getMinLength() != null && (m.getMinLength() < 0 || m.getMinLength() > 20)) {
                handler.accept(RadixProblem.Factory.newWarning(editMask, "Illegal editmask length"));
            }

            if (m.getNumberBase() < 2 || m.getNumberBase() > 16) {
                handler.accept(RadixProblem.Factory.newWarning(editMask, "Illegal edit mask number base"));
            }

            if (m.getStepSize() < 0) {
                handler.accept(RadixProblem.Factory.newWarning(editMask, "Illegal edit mask step size"));
            }
        } else if (editMask instanceof EditMaskNum) {
            if (valType != EValType.NUM && valType != EValType.ARR_NUM) {
                editMaskError(editMask, handler, "Num", valType, containerName);
            }

            final EditMaskNum m = (EditMaskNum) editMask;
            if (m.getMinValue() != null && m.getMaxValue() != null && m.getMinValue().doubleValue() >= m.getMaxValue().doubleValue()) {
                handler.accept(RadixProblem.Factory.newWarning(editMask, "Illegal editmask minimum or maximum value"));
            }

            if (m.getPrecision() != null && (m.getPrecision() < 0 || m.getPrecision() > 20)) {
                handler.accept(RadixProblem.Factory.newWarning(editMask, "Illegal editmask precision"));
            }

            if (m.getScale() < 0) {
                handler.accept(RadixProblem.Factory.newWarning(editMask, "Illegal editmask scale"));
            }
        } else if (editMask instanceof EditMaskDateTime) {
            if (valType != EValType.DATE_TIME && valType != EValType.ARR_DATE_TIME) {
                editMaskError(editMask, handler, "DateTime", valType, containerName);
            }

            final EditMaskDateTime m = (EditMaskDateTime) editMask;
            if (m.getMinValue() != null && m.getMaxValue() != null && (m.getMinValue().equals(m.getMaxValue()) || m.getMinValue().after(m.getMaxValue()))) {
                handler.accept(RadixProblem.Factory.newWarning(editMask, "Illegal editmask minimum or maximum value"));
            }

            if (m.getTimeStyle() == EDateTimeStyle.CUSTOM && m.getDateStyle() == EDateTimeStyle.CUSTOM) {
                String mask = m.getMask();

                if (mask == null || mask.isEmpty()) {
                    handler.accept(RadixProblem.Factory.newError(editMask, "No date/time mask defined for custom date/time style"));
                }
            }
        } else if (editMask instanceof EditMaskTimeInterval) {
            if (valType != EValType.INT && valType != EValType.ARR_INT
                    && valType != EValType.DATE_TIME && valType != EValType.ARR_DATE_TIME) {
                editMaskError(editMask, handler, "TimeInterval", valType, containerName);
            }

            final EditMaskTimeInterval m = (EditMaskTimeInterval) editMask;

            if (valType == EValType.DATE_TIME || valType == EValType.ARR_DATE_TIME) {
                if (EditMaskTimeInterval.Scale.MILLIS != m.getScale()) {
                    handler.accept(RadixProblem.Factory.newError(editMask, MessageFormat.format("Scale of interval should be in milliseconds for type {0}", valType.getName())));
                }
            }

            if (m.getMinValue() != null && m.getMaxValue() != null && m.getMinValue().longValue() >= m.getMaxValue().longValue()) {
                handler.accept(RadixProblem.Factory.newWarning(editMask, "Illegal editmask minimum or maximum value"));
            }

        } else if (editMask instanceof EditMaskList) {

            if (valType == EValType.STR
                    && valType != EValType.INT
                    && valType != EValType.NUM
                    && valType != EValType.CHAR
                    && valType == EValType.ARR_STR
                    && valType != EValType.ARR_INT
                    && valType != EValType.ARR_NUM
                    && valType != EValType.ARR_CHAR) {
                editMaskError(editMask, handler, "List", valType, containerName);
            }
        } else if (editMask instanceof EditMaskStr) {
            if (valType != EValType.BLOB && valType != EValType.CLOB && valType != EValType.BIN && valType != EValType.STR
                    && valType != EValType.ARR_BLOB && valType != EValType.ARR_CLOB && valType != EValType.ARR_BIN && valType != EValType.ARR_STR) {
                editMaskError(editMask, handler, "Str", valType, containerName);
            }

            final EditMaskStr m = (EditMaskStr) editMask;
            if (m.getMaxLen() != null && m.getMaxLen() <= 0) {
                handler.accept(RadixProblem.Factory.newWarning(editMask, "Illegal editmask length"));
            }

            EditMaskStr.ValidatorDef validator = m.getValidator();

            if (validator != null) {

                switch (validator.getType()) {
                    case SIMPLE:
                        EditMaskStr.DefaultValidatorDef dv = (EditMaskStr.DefaultValidatorDef) validator;
                        InputMask inputMask = InputMask.Factory.newInstance(dv.getMask());
                        String check = inputMask.checkPattern(dv.isKeepSeparators());

                        if (check != null) {
                            handler.accept(RadixProblem.Factory.newError(editMask, check));
                        }
                        break;

                    case INT:
                        EditMaskStr.IntValidatorDef iv = (EditMaskStr.IntValidatorDef) validator;
                        Long min = iv.getMinValue();
                        Long max = iv.getMaxValue();
                        if (min != null && max != null) {
                            if (min.longValue() > max.longValue()) {
                                handler.accept(RadixProblem.Factory.newError(editMask, "Minimym value must not be greater than maximum value"));
                            }
                        }
                        break;
                    case NUM:
                        EditMaskStr.NumValidatorDef nv = (EditMaskStr.NumValidatorDef) validator;
                        BigDecimal minn = nv.getMinValue();
                        BigDecimal maxn = nv.getMaxValue();
                        if (minn != null && maxn != null) {
                            if (minn.doubleValue() > maxn.doubleValue()) {
                                handler.accept(RadixProblem.Factory.newError(editMask, "Minimym value must not be greater than maximum value"));
                            }
                        }
                        break;

                }
            } else {
                handler.accept(RadixProblem.Factory.newError(editMask, "This type of string edit mask is not allowed"));
            }

        } else if (editMask instanceof EditMaskEnum) {
            if (cs == null) {
                if (propType.getPath() == null || propType.getPath().isEmpty()) {
                    handler.accept(RadixProblem.Factory.newError(editMask, "Edit mask with enumeration is not allowed,because " + containerName + " does not refer to any of enumerations"));
                } else {
                    handler.accept(RadixProblem.Factory.newError(editMask, "Enum edit mask requires enumeration to be found"));
                }

            } else {
                AdsUtils.checkAccessibility(editMask, cs, false, handler);
                CheckUtils.checkExportedApiDatails(editMask, cs, handler);

                if (cs.getItemType() == EValType.INT) {
                    if (valType != EValType.BOOL && valType != EValType.INT
                            && valType != EValType.ARR_BOOL && valType != EValType.ARR_INT) {
                        handler.accept(RadixProblem.Factory.newError(editMask, "Type of enum in editmask does not match to " + containerName + " type"));
                    }

                } else if (cs.getItemType() == EValType.CHAR) {
                    if (valType != EValType.CHAR && valType != EValType.ARR_CHAR) {
                        handler.accept(RadixProblem.Factory.newError(editMask, "Type of enum in editmask does not match to " + containerName + " type"));
                    }

                } else if (cs.getItemType() == EValType.STR) {
                    if (valType != EValType.BLOB && valType != EValType.CLOB && valType != EValType.BIN && valType != EValType.STR
                            && valType != EValType.ARR_BLOB && valType != EValType.ARR_CLOB && valType != EValType.ARR_BIN && valType != EValType.ARR_STR) {
                        handler.accept(RadixProblem.Factory.newError(editMask, "Type of enum in editmask does not match to " + containerName + " type"));
                    }
                } else {
                    handler.accept(RadixProblem.Factory.newError(editMask, MessageFormat.format("Edit mask with enumeration is not supported for " + containerName + " of type {0}", valType.getName())));
                }
            }
        }
    }

    public static void checkCreationEditorPresentation(AdsDefinition context, AdsEditorPresentationDef checkPresentation, IProblemHandler problemHandler) {
        if (!context.isWarningSuppressed(AdsDefinitionProblems.PRESENTATION_WITH_LAZY_PROPS_IN_CREATE_CONTEXT)) {
            for (AdsEditorPageDef page : checkPresentation.getEditorPages().get(EScope.ALL)) {
                for (AdsEditorPageDef.PagePropertyRef ref : page.getProperties().list()) {
                    AdsDefinition def = ref.findProperty();
                    if (def instanceof IAdsPresentableProperty) {
                        ServerPresentationSupport support = ((IAdsPresentableProperty) def).getPresentationSupport();
                        if (support != null) {
                            PropertyPresentation pp = support.getPresentation();
                            if (pp != null && pp.isPresentable()) {
                                if (pp.getEditOptions().isReadSeparately()) {
                                    problemHandler.accept(RadixProblem.Factory.newWarning(context, AdsDefinitionProblems.PRESENTATION_WITH_LAZY_PROPS_IN_CREATE_CONTEXT, checkPresentation.getQualifiedName(), def.getQualifiedName(), page.getQualifiedName()));
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public static void checkAdsValAsStr(IProblemHandler problemHandler, RadixObject context, AdsValAsStr value, EValType type, String valueHint, String details) {

        switch (type) {
            case JAVA_TYPE:
            case JAVA_CLASS:
            case USER_CLASS:
                break;
            default:
                try {
                    if (value.getValueType() == AdsValAsStr.EValueType.VAL_AS_STR) {
                        value.getValAsStr().toObject(type);
                    }
                } catch (WrongFormatError e) {
                    String message = valueHint + "of " + context.getTypeTitle() + "'" + context.getName();
                    if (details != null) {
                        message += details;
                    }
                    problemHandler.accept(RadixProblem.Factory.newError(context, message));
                } catch (Exception ex) {
                    Logger.getLogger(CheckUtils.class.getName()).log(Level.FINE, ex.getMessage(), ex);
                }
        }
    }

    public static void checkSelectorPresentationCreationOptions(RadixObject context, Restrictions restrictions, AdsSelectorPresentationDef presentation, IProblemHandler problemHandler) {
        if ((restrictions != null && !restrictions.isDenied(ERestriction.CREATE)) && !presentation.getRestrictions().isDenied(ERestriction.CREATE)) {
            if (presentation.getCreationClassCatalogId() == null) {
                AdsEntityObjectClassDef clazz = presentation.getOwnerClass();
                if (clazz != null) {
                    AdsEntityClassDef entity = clazz.findRootBasis();

                    if (entity != null && entity.isPolymorphic()) {
                        boolean report = false;
                        if (context instanceof AdsSelectorExplorerItemDef) {
                            AdsSelectorExplorerItemDef item = (AdsSelectorExplorerItemDef) context;
                            if (item.isClassCatalogInherited() || item.getCreationClassCatalogId() == null) {
                                report = true;
                            }
                        } else if (context instanceof ParentRefPropertyPresentation) {
                            report = true;
                        }
                        if (report) {
                            warning(context, problemHandler, "Creation class catalog not specified for selector presentation " + presentation.getQualifiedName());
                        }
                    }
                }
            }
        }
    }
}
