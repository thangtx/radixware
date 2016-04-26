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

package org.radixware.kernel.common.builder.compare;

import java.util.List;
import org.radixware.kernel.common.builder.compare.DefinitionComparator.IReporter;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.Definitions;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassMember;
import org.radixware.kernel.common.defs.ads.clazz.IAdsPresentableClass;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsServerSidePropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.ProfileUtilities;
import org.radixware.kernel.common.defs.ads.clazz.members.ServerPresentationSupport;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AbstractFormPresentations;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsClassCatalogDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPageDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPresentationDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsFilterDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsScopeCommandDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsSelectorPresentationDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsSortingDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.ClassPresentations;
import org.radixware.kernel.common.defs.ads.clazz.presentation.EditorPages;
import org.radixware.kernel.common.defs.ads.clazz.presentation.EntityObjectPresentations;
import org.radixware.kernel.common.defs.ads.command.AdsCommandDef;
import org.radixware.kernel.common.defs.ads.command.AdsContextlessCommandDef;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumDef;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumItemDef;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsExplorerItemDef;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsParagraphExplorerItemDef;
import org.radixware.kernel.common.defs.ads.explorerItems.ExplorerItems;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.ads.src.xml.XBeansClass;
import org.radixware.kernel.common.defs.ads.src.xml.XBeansIfaceProp;
import org.radixware.kernel.common.defs.ads.src.xml.XBeansInterface;
import org.radixware.kernel.common.defs.ads.src.xml.XBeansTypeSystem;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.defs.ads.ui.AdsCustomDialogDef;
import org.radixware.kernel.common.defs.ads.ui.AdsUIDef;
import org.radixware.kernel.common.defs.ads.xml.IXmlDefinition;
import org.radixware.kernel.common.enums.EAccess;
import org.radixware.kernel.common.enums.ECommandNature;
import org.radixware.kernel.common.enums.EPropNature;
import org.radixware.kernel.common.utils.Utils;


public class DefinitionComparator {

    public interface IReporter {

        public void incompatibleChange(String message);

        public void compatibleChange(String message);

        public void removeChange(RadixObject removedObject);

        public void addChange(RadixObject removedObject);
    }

    private static abstract class ExtendableCollectionMatcher<T extends AdsDefinition, C extends RadixObjects<? extends T>> {

        protected abstract T findSame(C collection, T object);

        public final boolean match(Definition container, C clocal, C alocal, boolean apiMatchOnly, IReporter reporter) {
            boolean match = true;
            for (T cdef : clocal) {
                if (apiMatchOnly) {
                    if (!cdef.isPublished()) {
                        continue;
                    }
                    if (cdef.getAccessMode() != EAccess.PUBLIC && (cdef.getAccessMode() != EAccess.PROTECTED || container.isFinal())) {
                        continue;
                    }

                }
                T adef = findSame(alocal, cdef);

                if (adef != null) {
                    //accessibility decreased agains of method of source class
                    if (cdef.getAccessMode().isLess(adef.getAccessMode())) {
                        reporter.incompatibleChange("access level has been decreased from " + adef.getAccessMode().getName() + " to " + cdef.getAccessMode().getName());
                        match = false;
                    }
                    if (cdef.isFinal()) {
                        //definition was finalized
                        if (!adef.isFinal()) {
                            reporter.incompatibleChange("definition has been made \"final\"");
                            match = false;
                        }
                    }
                    if (!cdef.isPublished()) {
                        //definition was depublished
                        if (adef.isPublished()) {
                            reporter.incompatibleChange("definition has been depublished");
                            match = false;
                        }
                    }
                    if (cdef.getAccessMode() != EAccess.DEFAULT && cdef.getAccessMode() != EAccess.PRIVATE) {
                        if (!extendedMatch(cdef, adef, reporter)) {
                            return false;
                        }
                    }
                } else {
                    reporter.addChange(cdef);
                }
            }
            for (T adef : alocal) {
                T cdef = findSame(clocal, adef);
                if (apiMatchOnly) {
                    if (cdef == null || !cdef.isPublished() || (cdef.getAccessMode() == EAccess.PROTECTED && container.isFinal())) {
                        reporter.removeChange(adef);
                        match = false;
                    }
                } else {
                    if (cdef == null) {
                        //public method deletion
                        if (adef.getAccessMode() != EAccess.PRIVATE && adef.getAccessMode() != EAccess.DEFAULT) {
                            reporter.removeChange(adef);
                            match = false;
                        }
                    }
                }
            }
            return match;
        }

        protected abstract boolean extendedMatch(T current, T anoter, IReporter reporter);
    }

    private static abstract class ExtendableDefinitionCollectionMatcher<T extends AdsDefinition> extends ExtendableCollectionMatcher<T, Definitions<? extends T>> {

        @Override
        protected T findSame(Definitions<? extends T> collection, T object) {
            return collection.findById(object.getId());
        }
    }

    public static boolean checkDefinitionCompatibility(Definition current, Definition anoter, boolean apiCheckOnly, boolean recursive, IReporter reporter) {
        if (current.getClass() == anoter.getClass()) {
            boolean isCompatible = true;
            if (current instanceof AdsClassDef) {
                if (!DefinitionComparator.checkClassCompatibility((AdsClassDef) current, (AdsClassDef) anoter, apiCheckOnly, recursive, reporter)) {
                    isCompatible = false;
                }
            } else if (current instanceof AdsEnumDef) {
                if (!DefinitionComparator.checkEnumerationCompatibility((AdsEnumDef) current, (AdsEnumDef) anoter, apiCheckOnly, reporter)) {
                    isCompatible = false;
                }
            } else if (current instanceof IXmlDefinition) {
                if (!DefinitionComparator.checkXmlDefinitionCompatibility((IXmlDefinition) current, (IXmlDefinition) anoter, apiCheckOnly, reporter)) {
                    isCompatible = false;
                }
            } else if (current instanceof AdsContextlessCommandDef) {
                if (!DefinitionComparator.checkCommandsCompatibility((AdsContextlessCommandDef) current, (AdsContextlessCommandDef) anoter, reporter)) {
                    isCompatible = false;
                }
            } else if (current instanceof AdsCustomDialogDef) {
                if (!DefinitionComparator.checkCustomDialogCompatibility((AdsCustomDialogDef) current, (AdsCustomDialogDef) anoter, apiCheckOnly, reporter)) {
                    isCompatible = false;
                }
            } else if (current instanceof AdsParagraphExplorerItemDef) {
                if (!DefinitionComparator.checkParagraphCompatibility((AdsParagraphExplorerItemDef) current, (AdsParagraphExplorerItemDef) anoter, apiCheckOnly, reporter)) {
                    isCompatible = false;
                }
            }
            return isCompatible;
        } else {
            reporter.incompatibleChange("definition has been obstructed");
            return false;
        }
    }

    public static boolean checkAPIUsageVariant(AdsClassMember current, AdsClassMember another, IReporter reporter) {
        if (current.getApiUsageVariant() != another.getApiUsageVariant()) {
            reporter.incompatibleChange("usage kind has been changed");
            return false;
        }
        return true;
    }

    public static boolean checkMethodCompatibility(AdsMethodDef current, AdsMethodDef another, IReporter reporter) {
        boolean isCompatible = true;
        if (!checkAPIUsageVariant(current, another, reporter)) {
            isCompatible = false;
        }
        if (!current.isReflectiveCallable()) {
            if (another.isReflectiveCallable()) {
                reporter.incompatibleChange("\"reflecive callable\" flag has been removed");
                isCompatible = false;
            }
        }

        if (current.getProfile().getAccessFlags().isStatic() != another.getProfile().getAccessFlags().isStatic()) {
            reporter.incompatibleChange("has been " + (current.getProfile().getAccessFlags().isStatic() ? "made static" : "made non-static"));
            isCompatible = false;
        }
        AdsTypeDeclaration[] cprofile = current.getProfile().getNormalizedProfile();
        AdsTypeDeclaration[] aprofile = another.getProfile().getNormalizedProfile();

        ProfileUtilities.ProfileCompareResults results = ProfileUtilities.compareProfiles(current, cprofile, aprofile);
        if (!results.ok()) {
            reporter.incompatibleChange("profile has been changed from\n" + another.getProfile().getName() + "\nto\n" + current.getProfile().getName());
            isCompatible = false;
        }
        return isCompatible;
    }

    public static boolean checkPropertyCompatibility(AdsPropertyDef current, AdsPropertyDef another, IReporter reporter) {
        boolean isCompatible = true;
        if (!checkAPIUsageVariant(current, another, reporter)) {
            isCompatible = false;
        }
        if (!Utils.equals(current.getName(), another.getName())) {
            reporter.compatibleChange("has been renamed to " + current.getName());
        }
        AdsTypeDeclaration ctype = current.getValue().getType();
        AdsTypeDeclaration atype = current.getValue().getType();

        if (!ctype.equalsTo(current, atype)) {
            reporter.incompatibleChange("type has been changed from " + atype.getQualifiedName(current) + " to " + ctype.getQualifiedName(current));
            isCompatible = false;
        }
        if (current instanceof AdsServerSidePropertyDef && another instanceof AdsServerSidePropertyDef) {
            ServerPresentationSupport csps = ((AdsServerSidePropertyDef) current).getPresentationSupport();
            ServerPresentationSupport asps = ((AdsServerSidePropertyDef) another).getPresentationSupport();
            if (csps != null && asps != null) {
                if (csps.getPresentation().isPresentable() != asps.getPresentation().isPresentable()) {
                    if (csps.getPresentation().isPresentable()) {
                        reporter.compatibleChange("has been made available for client side of application");
                    } else {
                        reporter.incompatibleChange("has been made unavailable for client side of application");
                        isCompatible = false;
                    }
                }
            }
        }
        if (current.isConst() != another.isConst() && current.getNature() != EPropNature.PARENT_PROP) {
            if (current.isConst()) {
                reporter.incompatibleChange("has been made read only");
                isCompatible = false;
            }
        }
        if (current.getAccessFlags().isStatic() != another.getAccessFlags().isStatic()) {
            if (current.isConst()) {
                reporter.incompatibleChange("has been made " + (current.getAccessFlags().isStatic() ? "static" : "non-static"));
                isCompatible = false;
            }
        }
        return isCompatible;
    }

    public static boolean checkEnumerationItemCompatibility(AdsEnumItemDef current, AdsEnumItemDef anoter, IReporter reporter) {
        if (!Utils.equals(current.getName(), anoter.getName())) {
            reporter.compatibleChange("has been renamed to " + current.getName());
        }
        if (!Utils.equals(current.getValue(), anoter.getValue())) {
            reporter.incompatibleChange("value has been changed from \"" + anoter.getValue() + "\" to \"" + current.getValue() + "\"");
            return false;
        } else {
            return true;
        }
    }

    public static boolean checkCommandsCompatibility(AdsCommandDef current, AdsCommandDef another, IReporter reporter) {
        boolean isCompatible = true;
        ECommandNature cnature = current.getData().getNature();
        ECommandNature anature = another.getData().getNature();
        if (cnature != anature) {
            reporter.incompatibleChange("nature has been changed from " + anature.getName() + " to " + cnature.getName());
            isCompatible = false;
        }
        if (cnature == ECommandNature.XML_IN_OUT) {
            AdsTypeDeclaration cin = current.getData().getInType();
            AdsTypeDeclaration ain = another.getData().getInType();

            if (!cin.equalsTo(current, ain)) {
                reporter.incompatibleChange("input type has been changed from " + ain.getQualifiedName(current) + " to " + cin.getQualifiedName(current));
                isCompatible = false;
            }

            AdsTypeDeclaration cout = current.getData().getOutType();
            AdsTypeDeclaration aout = another.getData().getOutType();

            if (!cout.equalsTo(current, aout)) {
                reporter.incompatibleChange("output type has been changed from " + aout.getQualifiedName(current) + " to " + cout.getQualifiedName(current));
                isCompatible = false;
            }
        }
        return isCompatible;
    }

    public static boolean checkScopeCommandsCompatibility(AdsScopeCommandDef current, AdsScopeCommandDef anoter, IReporter reporter) {
        return checkCommandsCompatibility(current, anoter, reporter);
    }

    private static boolean checkEditorPagesCompatibility(AdsDefinition currentContainer, EditorPages current, EditorPages anoter, boolean apiCheckOnly, IReporter reporter) {
        if (!new ExtendableDefinitionCollectionMatcher<AdsEditorPageDef>() {
            @Override
            protected boolean extendedMatch(AdsEditorPageDef current, AdsEditorPageDef anoter, IReporter reporter) {
                return true;
            }
        }.match(currentContainer, current.getLocal(), anoter.getLocal(), apiCheckOnly, reporter)) {
            return false;
        }
        return true;
    }

    public static boolean checkClassCatalogsCompatibility(AdsClassCatalogDef current, AdsClassCatalogDef anoter, IReporter reporter) {
        return true;
    }

    public static boolean checkEditorPresentationsCompatibility(AdsEditorPresentationDef current, AdsEditorPresentationDef another, boolean apiCheckOnly, IReporter reporter) {
        boolean isCompatible = true;
        if (current.isEditorPagesInherited() != another.isEditorPagesInherited()) {
            reporter.incompatibleChange(current.isEditorPagesInherited() ? "editor page list has been inherited" : "editor page list has been redefined");
            isCompatible = false;
        } else {
            if (!current.isEditorPagesInherited() && !another.isEditorPagesInherited()) {
                if (!checkEditorPagesCompatibility(current, current.getEditorPages(), another.getEditorPages(), apiCheckOnly, reporter)) {
                    isCompatible = false;
                }
            }
        }
        return isCompatible;
    }

    public static boolean checkClassCompatibility(AdsClassDef current, AdsClassDef another, final boolean apiCheckOnly, boolean recursive, IReporter reporter) {
        boolean isCompatible = true;
        if (current.getClassDefType() != another.getClassDefType()) {
            //class type mismatch
            isCompatible = false;
        }
        if (current.isFinal()) {
            if (!another.isFinal()) {
                //class was made winal
                isCompatible = false;
            }
        }
        AdsTypeDeclaration csuper = current.getInheritance().getSuperClassRef();
        AdsTypeDeclaration asuper = another.getInheritance().getSuperClassRef();

        if (csuper != null || asuper != null) {
            if (csuper != null && asuper != null) {
                if (!csuper.equalsTo(current, asuper)) {
                    //super class changed
                    isCompatible = false;
                }
            } else {
                //one of superclasses was reseted to Object
                isCompatible = false;
            }
        }
        List<AdsTypeDeclaration> cifaces = current.getInheritance().getInerfaceRefList(EScope.LOCAL);
        List<AdsTypeDeclaration> aifaces = current.getInheritance().getInerfaceRefList(EScope.LOCAL);

        if (cifaces.size() != aifaces.size()) {
            if (cifaces.size() < aifaces.size()) {
                //interface removed from interface list
                isCompatible = false;
            } else {
                for (AdsTypeDeclaration adecl : aifaces) {
                    boolean matched = false;
                    for (AdsTypeDeclaration cdecl : cifaces) {
                        if (adecl.equalsTo(current, cdecl)) {
                            matched = true;
                            break;
                        }
                    }
                    if (!matched) {
                        //interface removed from 'implements' list
                        isCompatible = false;
                    }
                }
            }
        }

        if (recursive) {
            if (!new ExtendableDefinitionCollectionMatcher<AdsMethodDef>() {
                @Override
                protected boolean extendedMatch(AdsMethodDef current, AdsMethodDef anoter, IReporter reporter) {
                    return checkMethodCompatibility(current, anoter, reporter);
                }
            }.match(current, current.getMethods().getLocal(), another.getMethods().getLocal(), apiCheckOnly, reporter)) {
                isCompatible = false;
            }
            if (!new ExtendableDefinitionCollectionMatcher<AdsPropertyDef>() {
                @Override
                protected boolean extendedMatch(AdsPropertyDef current, AdsPropertyDef anoter, IReporter reporter) {
                    return checkPropertyCompatibility(current, anoter, reporter);
                }
            }.match(current, current.getProperties().getLocal(), another.getProperties().getLocal(), apiCheckOnly, reporter)) {
                isCompatible = false;
            }

            if (current instanceof IAdsPresentableClass) {
                ClassPresentations cprs = ((IAdsPresentableClass) current).getPresentations();
                ClassPresentations aprs = ((IAdsPresentableClass) another).getPresentations();
                if (cprs != null && aprs != null) {
                    if (!new ExtendableDefinitionCollectionMatcher<AdsScopeCommandDef>() {
                        @Override
                        protected boolean extendedMatch(AdsScopeCommandDef current, AdsScopeCommandDef anoter, IReporter reporter) {
                            return checkScopeCommandsCompatibility(current, anoter, reporter);
                        }
                    }.match(current, cprs.getCommands().getLocal(), aprs.getCommands().getLocal(), apiCheckOnly, reporter)) {
                        isCompatible = false;
                    }
                    if (cprs.getClass() != aprs.getClass()) {
                        reporter.incompatibleChange("type of presentation settings has been changed");
                        isCompatible = false;
                    } else {
                        if (cprs instanceof EntityObjectPresentations) {
                            EntityObjectPresentations ceprs = (EntityObjectPresentations) cprs;
                            EntityObjectPresentations aeprs = (EntityObjectPresentations) aprs;
                            if (!new ExtendableDefinitionCollectionMatcher<AdsEditorPresentationDef>() {
                                @Override
                                protected boolean extendedMatch(AdsEditorPresentationDef current, AdsEditorPresentationDef anoter, IReporter reporter) {
                                    return checkEditorPresentationsCompatibility(current, anoter, apiCheckOnly, reporter);
                                }
                            }.match(current, ceprs.getEditorPresentations().getLocal(), aeprs.getEditorPresentations().getLocal(), apiCheckOnly, reporter)) {
                                isCompatible = false;
                            }
                            if (!new ExtendableDefinitionCollectionMatcher<AdsSelectorPresentationDef>() {
                                @Override
                                protected boolean extendedMatch(AdsSelectorPresentationDef current, AdsSelectorPresentationDef anoter, IReporter reporter) {
                                    return true;
                                }
                            }.match(current, ceprs.getSelectorPresentations().getLocal(), aeprs.getSelectorPresentations().getLocal(), apiCheckOnly, reporter)) {
                                isCompatible = false;
                            }
                            if (!new ExtendableDefinitionCollectionMatcher<AdsSortingDef>() {
                                @Override
                                protected boolean extendedMatch(AdsSortingDef current, AdsSortingDef anoter, IReporter reporter) {
                                    return true;
                                }
                            }.match(current, ceprs.getSortings().getLocal(), aeprs.getSortings().getLocal(), apiCheckOnly, reporter)) {
                                isCompatible = false;
                            }
                            if (!new ExtendableDefinitionCollectionMatcher<AdsFilterDef>() {
                                @Override
                                protected boolean extendedMatch(AdsFilterDef current, AdsFilterDef anoter, IReporter reporter) {
                                    return true;
                                }
                            }.match(current, ceprs.getFilters().getLocal(), aeprs.getFilters().getLocal(), apiCheckOnly, reporter)) {
                                isCompatible = false;
                            }
                            if (!new ExtendableDefinitionCollectionMatcher<AdsClassCatalogDef>() {
                                @Override
                                protected boolean extendedMatch(AdsClassCatalogDef current, AdsClassCatalogDef anoter, IReporter reporter) {
                                    return checkClassCatalogsCompatibility(current, anoter, reporter);
                                }
                            }.match(current, ceprs.getClassCatalogs().getLocal(), aeprs.getClassCatalogs().getLocal(), apiCheckOnly, reporter)) {
                                isCompatible = false;
                            }
                        } else if (cprs instanceof AbstractFormPresentations) {
                            AbstractFormPresentations cafprs = (AbstractFormPresentations) cprs;
                            AbstractFormPresentations aafprs = (AbstractFormPresentations) aprs;
                            if (!checkEditorPagesCompatibility(current, cafprs.getEditorPages(), aafprs.getEditorPages(), apiCheckOnly, reporter)) {
                                isCompatible = false;
                            }
                        }
                    }
                } else {
                    if (cprs != null || aprs != null) {
                        if (cprs == null) {
                            reporter.incompatibleChange("presentations settings has been removed");
                            isCompatible = false;
                        }
                    }
                }
            }
        }

        return isCompatible;
    }

    public static boolean checkEnumerationCompatibility(AdsEnumDef current, AdsEnumDef another, boolean apiCheckOnly, IReporter reporter) {
        boolean isCompatible = true;
        if (!new ExtendableDefinitionCollectionMatcher<AdsEnumItemDef>() {
            @Override
            protected boolean extendedMatch(AdsEnumItemDef current, AdsEnumItemDef anoter, IReporter reporter) {
                return checkEnumerationItemCompatibility(current, anoter, reporter);
            }
        }.match(current, current.getItems().getLocal(), another.getItems().getLocal(), apiCheckOnly, reporter)) {
            isCompatible = false;
        }
        return isCompatible;
    }

    public static boolean checkAdsModuleCompatibility(AdsModule current, AdsModule another, final boolean apiCheckOnly, final IReporter reporter) {
        boolean isCompatible = true;
        if (!new ExtendableDefinitionCollectionMatcher<AdsDefinition>() {
            @Override
            protected boolean extendedMatch(AdsDefinition current, AdsDefinition anoter, IReporter reporter) {
                return checkDefinitionCompatibility(current, anoter, apiCheckOnly, true, reporter);
            }
        }.match(current, current.getDefinitions(), another.getDefinitions(), apiCheckOnly, reporter)) {
            isCompatible = false;
        }
        return isCompatible;
    }

    public static boolean checkXmlDefinitionCompatibility(IXmlDefinition current, IXmlDefinition another, final boolean apiCheckOnly, final IReporter reporter) {
        boolean isCompatible = true;
        XBeansTypeSystem cts = current.getSchemaTypeSystem();
        XBeansTypeSystem ats = another.getSchemaTypeSystem();
        if (cts == null || ats == null) {
            if (cts == null) {
                reporter.incompatibleChange("No type system found for current version");
                isCompatible = false;
            }
            if (ats == null) {
                reporter.incompatibleChange("No type system found for another version");
                isCompatible = false;
            }
        } else {
            if (!compareXBeansTypeSystems(cts, ats, reporter)) {
                isCompatible = false;
            }
        }
        return isCompatible;
    }

    public static boolean checkUICompatibility(AdsUIDef current, AdsUIDef another, boolean apiCheckOnly, IReporter reporter) {
        boolean isCompatible = true;

        return isCompatible;
    }

    public static boolean checkCustomDialogCompatibility(AdsCustomDialogDef current, AdsCustomDialogDef another, boolean apiCheckOnly, IReporter reporter) {
        boolean isCompatible = true;
        if (!checkUICompatibility(current, another, apiCheckOnly, reporter)) {
            isCompatible = false;
        }
        AdsClassDef cmodel = current.getModelClass();
        AdsClassDef amodel = another.getModelClass();
        if (!checkClassCompatibility(cmodel, amodel, apiCheckOnly, true, reporter)) {
            isCompatible = false;
        }
        return isCompatible;
    }

    public static boolean checkParagraphCompatibility(AdsParagraphExplorerItemDef current, AdsParagraphExplorerItemDef another, boolean apuCheckOnly, IReporter reporter) {
        boolean isCompatible = true;
        if (!checkExplorerItemsCompatibility(current, current.getExplorerItems(), another.getExplorerItems(), apuCheckOnly, reporter)) {
            isCompatible = false;
        }
        return isCompatible;
    }

    private static boolean checkExplorerItemsCompatibility(AdsDefinition owner, ExplorerItems current, ExplorerItems another, final boolean apiCheckOnly, IReporter reporter) {
        boolean isCompatible = true;

        if (!new ExtendableDefinitionCollectionMatcher<AdsExplorerItemDef>() {
            @Override
            protected boolean extendedMatch(AdsExplorerItemDef current, AdsExplorerItemDef another, IReporter reporter) {
                if (current.getClass() == another.getClass()) {
                    if (current instanceof AdsParagraphExplorerItemDef) {
                        return checkParagraphCompatibility((AdsParagraphExplorerItemDef) current, (AdsParagraphExplorerItemDef) another, apiCheckOnly, reporter);
                    } else {
                        return true;
                    }
                } else {
                    reporter.incompatibleChange("obstruction: definition type has been changed");
                    return false;
                }
            }
        }.match(owner, current.getChildren().getLocal(), another.getChildren().getLocal(), apiCheckOnly, reporter)) {
            isCompatible = false;
        }
        return isCompatible;
    }

    private static boolean compareXBeansTypeSystems(XBeansTypeSystem current, XBeansTypeSystem another, IReporter reporter) {
        boolean isCompatible = true;
        if (!Utils.equals(current.getInterfacePackageName(), another.getInterfacePackageName())) {
            reporter.incompatibleChange("Interface package name has been changed from " + another.getInterfacePackageName() + " to " + current.getInterfacePackageName());
            isCompatible = false;
        }
        if (!Utils.equals(current.getImplementationPackageName(), another.getImplementationPackageName())) {
            reporter.incompatibleChange("Implementation package name has been changed from " + another.getImplementationPackageName() + " to " + current.getImplementationPackageName());
            isCompatible = false;
        }

        for (XBeansInterface iface : current.getInterfaceList()) {
            XBeansInterface aiface = another.findInterface(iface.getName());
            if (aiface == null) {
                reporter.compatibleChange("Interface type " + iface.getName() + " has been added");
            } else {
                if (!compareXBeansInterfaces(iface, aiface, reporter)) {
                    isCompatible = false;
                }
            }
        }
        for (XBeansInterface aiface : another.getInterfaceList()) {
            XBeansInterface iface = current.findInterface(aiface.getName());
            if (iface == null) {
                reporter.incompatibleChange("Interface type " + aiface.getName() + " has been removed");
                isCompatible = false;
            }
        }
        return isCompatible;
    }

    private static boolean compareXBeansInterfaces(XBeansInterface current, XBeansInterface another, IReporter reporter) {
        boolean isCompatible = true;
        XBeansInterface.Content ccontent = current.getContent();
        XBeansInterface.Content acontent = another.getContent();


        if (ccontent == null) {
            if (acontent != null) {
                reporter.incompatibleChange("Content type of interface type " + current.getQName() + " has been undefined");
                isCompatible = false;
            }
        } else {
            if (acontent == null) {
                reporter.compatibleChange("Content type of interface type " + current.getQName() + " has been defined");
            } else {
                if (ccontent instanceof XBeansInterface.SimpleContent) {
                    if (acontent instanceof XBeansInterface.SimpleContent) {
                        if (!compareXBeansInterfaceSimpleContents((XBeansInterface.SimpleContent) ccontent, (XBeansInterface.SimpleContent) acontent, reporter)) {
                            isCompatible = false;
                        }
                    } else {
                        reporter.incompatibleChange("Content type of interface type " + current.getQName() + " has been changed to simple");
                        isCompatible = false;
                    }
                } else {
                    if (acontent instanceof XBeansInterface.ComplexContent) {
                        if (!compareXBeansInterfaceComplexContents((XBeansInterface.ComplexContent) ccontent, (XBeansInterface.ComplexContent) acontent, reporter)) {
                            isCompatible = false;
                        }
                    } else {
                        reporter.incompatibleChange("Content type of interface type " + current.getQName() + " has been changed to complex");
                        isCompatible = false;
                    }
                }
            }
        }
        XBeansInterface.FactoryInfo cfactory = current.getFactoryInfo();
        XBeansInterface.FactoryInfo afactory = another.getFactoryInfo();
        if (cfactory == null) {
            if (afactory != null) {
                reporter.incompatibleChange("Factory of interface type " + current.getQName() + " has been removed");
                isCompatible = false;
            }
        } else {
            if (afactory == null) {
                reporter.incompatibleChange("Factory of interface type " + current.getQName() + " has been added");
                isCompatible = false;
            } else {
                if (!Utils.equals(cfactory.getFactoryName(), afactory.getFactoryName())) {
                    reporter.incompatibleChange("Created type of factory of interface type " + current.getQName() + " has been changed");
                    isCompatible = false;
                }
                if (cfactory.isAbstractTypeFactory() != afactory.isAbstractTypeFactory()) {
                    reporter.incompatibleChange("Interface type " + current.getQName() + " has been made " + (cfactory.isAbstractTypeFactory() ? "abstract" : "concrete"));
                    isCompatible = false;
                }
                if (cfactory.isFullFactory() != afactory.isFullFactory()) {
                    reporter.incompatibleChange("Factory of interface type " + current.getQName() + " has been changed");
                    isCompatible = false;
                }
                if (cfactory.isSimpleTypeFactory() != afactory.isSimpleTypeFactory()) {
                    reporter.incompatibleChange("Factory of interface type " + current.getQName() + " has been converted to factory of " + (cfactory.isSimpleTypeFactory() ? "simple type" : "complex type"));
                    isCompatible = false;
                }
            }
        }


        List<XBeansInterface> cinners = current.getInnerInterfaces();

        for (XBeansInterface cinner : cinners) {
            XBeansInterface ainner = another.findInnerInterface(cinner.getName());
            if (ainner == null) {
                reporter.compatibleChange("Interface type " + cinner.getQName() + " has been added");
            } else {
                if (!compareXBeansInterfaces(cinner, ainner, reporter)) {
                    isCompatible = false;
                }
            }
        }
        List<XBeansInterface> ainners = another.getInnerInterfaces();
        for (XBeansInterface ainner : ainners) {
            XBeansInterface cinner = current.findInnerInterface(ainner.getName());
            if (cinner == null) {
                reporter.incompatibleChange("Interface type " + ainner.getQName() + " has been removed");
                isCompatible = false;
            }
        }
        return isCompatible;
    }

    private static boolean compareXBeansInterfaceSimpleContents(XBeansInterface.SimpleContent current, XBeansInterface.SimpleContent another, IReporter reporter) {
        boolean isCompatible = true;
        if (current.isTableDefined() != another.isTableDefined()) {
            reporter.incompatibleChange("Enumeration index table has been " + (current.isTableDefined() ? "defined" : "undefined"));
            isCompatible = false;
        }
        if (!Utils.equals(current.getEnumClass(), another.getEnumClass())) {
            reporter.incompatibleChange("Enumeration class name has been changed from " + another.getEnumClass() + " to " + current.getEnumClass());
            isCompatible = false;
        }
        for (XBeansInterface.SimpleContent.EnumField cf : current.getFieldList()) {
            XBeansInterface.SimpleContent.EnumField af = another.findFiledByName(cf.getName());
            if (af == null) {
                reporter.compatibleChange("Enumeration field " + cf.getQName() + " has been added");
            } else {
                if (!Utils.equals(cf.getValue(), af.getValue())) {
                    reporter.incompatibleChange("Value of enumeration field " + cf.getQName() + " has been changed");
                    isCompatible = false;
                }
            }
        }
        for (XBeansInterface.SimpleContent.EnumField af : another.getFieldList()) {
            XBeansInterface.SimpleContent.EnumField cf = current.findFiledByName(af.getName());
            if (cf == null) {
                reporter.incompatibleChange("Enumeration field " + af.getQName() + " has been removed");
                isCompatible = false;
            }
        }
        for (String cf : current.getIndexList()) {
            if (!another.isIndexExists(cf)) {
                reporter.compatibleChange("Enumeration index field " + cf + " has been added");
            }
        }
        for (String af : another.getIndexList()) {
            if (!current.isIndexExists(af)) {
                reporter.compatibleChange("Enumeration index field " + af + " has been removed");
            }
        }
        for (XBeansInterface.SimpleContent.EnumerationElement cf : current.getElementList()) {
            XBeansInterface.SimpleContent.EnumerationElement af = another.findElementByName(cf.getName());
            if (af == null) {
                reporter.compatibleChange("Enumeration element " + cf.getName() + " has been added");
            } else {
                if (!Utils.equals(cf.getValue(), af.getValue())) {
                    reporter.incompatibleChange("Value of enumeration element " + cf.getQName() + " has been changed");
                    isCompatible = false;
                }
                if (cf.getIntValue() != af.getIntValue()) {
                    reporter.incompatibleChange("Value index of enumeration element " + cf.getQName() + " has been changed");
                    isCompatible = false;
                }
            }
        }
        for (XBeansInterface.SimpleContent.EnumerationElement af : another.getElementList()) {
            XBeansInterface.SimpleContent.EnumerationElement cf = current.findElementByName(af.getName());
            if (cf == null) {
                reporter.incompatibleChange("Enumeration element " + af.getQName() + " has been removed");
                isCompatible = false;
            }
        }
        return isCompatible;
    }

    private static boolean compareXBeansInterfaceComplexContents(XBeansInterface.ComplexContent current, XBeansInterface.ComplexContent another, IReporter reporter) {
        boolean isCompatible = true;
        for (XBeansIfaceProp cprop : current.getPropertyList()) {
            XBeansIfaceProp aprop = another.findProperty(cprop.getName());
            if (aprop == null) {
                reporter.compatibleChange("Property " + cprop.getQName() + " has been added");
            } else {
                if (!compareXBeansIfaceProps(cprop, aprop, reporter)) {
                    isCompatible = false;
                }
            }
        }
        for (XBeansIfaceProp aprop : another.getPropertyList()) {
            XBeansIfaceProp cprop = current.findProperty(aprop.getName());
            if (cprop == null) {
                reporter.incompatibleChange("Property " + aprop.getQName() + " has been removed");
                isCompatible = false;
            }
        }
        return isCompatible;
    }

    private static boolean compareXBeansIfaceProps(XBeansIfaceProp current, XBeansIfaceProp another, IReporter reporter) {
        boolean isCompatible = true;
        if (!Utils.equals(current.getType(), another.getType())) {
            reporter.incompatibleChange("Type of interface property " + current.getQName() + " has been changed from " + another.getType() + " to " + current.getType());
            isCompatible = false;
        }
        if (!Utils.equals(current.getXType(), another.getXType())) {
            reporter.incompatibleChange("Xml type of interface property " + current.getQName() + " has been changed from " + another.getXType() + " to " + current.getXType());
            isCompatible = false;
        }
        if (current.isDeprecated() != another.isDeprecated()) {
            reporter.compatibleChange("Interface property " + current.getQName() + " has been " + (current.isDeprecated() ? "deprecated" : "undeprecated"));
        }
        if (current.isAttr() != another.isAttr()) {
            reporter.incompatibleChange("Interface property " + current.getQName() + " has been converted to " + (current.isAttr() ? "attribute property" : "element property"));
            isCompatible = false;
        }
        if (current.hasSingletonSetter() != another.hasSingletonSetter()) {
            if (current.hasSingletonSetter()) {
                reporter.compatibleChange("Singleton setter of interface property " + current.getQName() + " has been added");
            } else {
                reporter.incompatibleChange("Singleton setter of interface property " + current.getQName() + " has been removed");
                isCompatible = current.hasSingletonSetter();
            }
        }
        if (current.hasSingletonGetter() != another.hasSingletonGetter()) {
            if (current.hasSingletonGetter()) {
                reporter.compatibleChange("Singleton getter of interface property " + current.getQName() + " has been added");
            } else {
                reporter.incompatibleChange("Singleton getter of interface property " + current.getQName() + " has been removed");
                isCompatible = false;
            }

        }
        if (current.hasSingletonSetterXml() != another.hasSingletonSetterXml()) {
            if (current.hasSingletonSetterXml()) {
                reporter.compatibleChange("Singleton xml setter of interface property " + current.getQName() + " has been added");
            } else {
                reporter.incompatibleChange("Singleton xml setter of interface property " + current.getQName() + " has been removed");
                isCompatible = false;
            }

        }
        if (current.hasSingletonGetterXml() != another.hasSingletonGetterXml()) {
            if (current.hasSingletonGetterXml()) {
                reporter.compatibleChange("Singleton xml getter of interface property " + current.getQName() + " has been added");
            } else {
                reporter.incompatibleChange("Singleton xml getter of interface property " + current.getQName() + " has been removed");
                isCompatible = false;
            }

        }
        if (current.hasSingletonNullCheck() != another.hasSingletonNullCheck()) {
            if (current.hasSingletonNullCheck()) {
                reporter.compatibleChange("Singleton null check of interface property " + current.getQName() + " has been added");
            } else {
                reporter.incompatibleChange("Singleton null check of interface property " + current.getQName() + " has been removed");
                isCompatible = false;
            }

        }
        if (current.hasSingletonCreator() != another.hasSingletonCreator()) {
            if (current.hasSingletonCreator()) {
                reporter.compatibleChange("Singleton creator of interface property " + current.getQName() + " has been added");
            } else {
                reporter.incompatibleChange("Singleton creator of interface property " + current.getQName() + " has been removed");
                isCompatible = false;
            }

        }
        if (current.hasSingletonUnset() != another.hasSingletonUnset()) {
            if (current.hasSingletonUnset()) {
                reporter.compatibleChange("Singleton unset of interface property " + current.getQName() + " has been added");
            } else {
                reporter.incompatibleChange("Singleton unset of interface property " + current.getQName() + " has been removed");
                isCompatible = false;
            }

        }

        if (current.hasOptionalUnset() != another.hasOptionalUnset()) {
            if (current.hasOptionalUnset()) {
                reporter.compatibleChange("Optional unset of interface property " + current.getQName() + " has been added");
            } else {
                reporter.incompatibleChange("Optional unset of interface property " + current.getQName() + " has been removed");
                isCompatible = false;
            }

        }
        if (current.hasOptionalExistanceCheck() != another.hasOptionalExistanceCheck()) {
            if (current.hasOptionalExistanceCheck()) {
                reporter.compatibleChange("Optional existance check of interface property " + current.getQName() + " has been added");
            } else {
                reporter.incompatibleChange("Optional existance check of interface property " + current.getQName() + " has been removed");
                isCompatible = false;
            }

        }

        if (current.hasSeveralPropListGetter() != another.hasSeveralPropListGetter()) {
            if (current.hasSeveralPropListGetter()) {
                reporter.compatibleChange("List getter of interface property " + current.getQName() + " has been added");
            } else {
                reporter.incompatibleChange("List getter of interface property " + current.getQName() + " has been removed");
                isCompatible = false;
            }
        } else {
            if (!Utils.equals(current.getSeveralPropListGetterWrappedType(), another.getSeveralPropListGetterWrappedType())) {
                if (another.getSeveralPropListGetterWrappedType() == null) {
                    reporter.compatibleChange("Type of list getter of interface property " + current.getQName() + " has been specified");
                }else 
                    reporter.incompatibleChange("Type of list getter of interface property type" + current.getQName() + " has been removed");
            }
        }

        if (current.hasSeveralPropListAssignment() != another.hasSeveralPropListAssignment()) {
            if (current.hasSeveralPropListAssignment()) {
                reporter.compatibleChange("List setter of interface property " + current.getQName() + " has been added");
            } else {
                reporter.incompatibleChange("List setter of interface property " + current.getQName() + " has been removed");
                isCompatible = false;
            }

        }

        if (current.hasSeveralPropArrayGetter() != another.hasSeveralPropArrayGetter()) {
            if (current.hasSeveralPropArrayGetter()) {
                reporter.compatibleChange("Array getter of interface property " + current.getQName() + " has been added");
            } else {
                reporter.incompatibleChange("Array getter of interface property " + current.getQName() + " has been removed");
                isCompatible = false;
            }

        }

        if (current.hasSeveralPropArraySetter() != another.hasSeveralPropArraySetter()) {
            if (current.hasSeveralPropArraySetter()) {
                reporter.compatibleChange("Array setter of interface property " + current.getQName() + " has been added");
            } else {
                reporter.incompatibleChange("Array setter of interface property " + current.getQName() + " has been removed");
                isCompatible = false;
            }

        }

        if (current.hasSeveralPropArrayElementGetter() != another.hasSeveralPropArrayElementGetter()) {
            if (current.hasSeveralPropArrayElementGetter()) {
                reporter.compatibleChange("Array element getter of interface property " + current.getQName() + " has been added");
            } else {
                reporter.incompatibleChange("Array element getter of interface property " + current.getQName() + " has been removed");
                isCompatible = false;
            }
        }

        if (current.hasSeveralPropArrayElementSetter() != another.hasSeveralPropArrayElementSetter()) {
            if (current.hasSeveralPropArrayElementSetter()) {
                reporter.compatibleChange("Array element setter of interface property " + current.getQName() + " has been added");
            } else {
                reporter.incompatibleChange("Array element setter of interface property " + current.getQName() + " has been removed");
                isCompatible = false;
            }
        }

        if (current.hasSeveralPropArrayElementSetNull() != another.hasSeveralPropArrayElementSetNull()) {
            if (current.hasSeveralPropArrayElementSetNull()) {
                reporter.compatibleChange("Array element null setter of interface property " + current.getQName() + " has been added");
            } else {
                reporter.incompatibleChange("Array element null setter of interface property " + current.getQName() + " has been removed");
                isCompatible = false;
            }

        }

        if (current.hasSeveralPropListXmlGetter() != another.hasSeveralPropListXmlGetter()) {
            if (current.hasSeveralPropListXmlGetter()) {
                reporter.compatibleChange("Xml list getter of interface property " + current.getQName() + " has been added");
            } else {
                reporter.incompatibleChange("Xml list getter of interface property " + current.getQName() + " has been removed");
                isCompatible = false;
            }

        }

        if (current.hasSeveralPropArrayXmlGetter() != another.hasSeveralPropArrayXmlGetter()) {
            if (current.hasSeveralPropArrayXmlGetter()) {
                reporter.compatibleChange("Xml array getter of interface property " + current.getQName() + " has been added");
            } else {
                reporter.incompatibleChange("Xml array getter of interface property " + current.getQName() + " has been removed");
                isCompatible = false;
            }

        }
        if (current.hasSeveralPropArrayXmlSetter() != another.hasSeveralPropArrayXmlSetter()) {
            if (current.hasSeveralPropArrayXmlSetter()) {
                reporter.compatibleChange("Xml array setter of interface property " + current.getQName() + " has been added");
            } else {
                reporter.incompatibleChange("Xml array setter of interface property " + current.getQName() + " has been removed");
                isCompatible = false;
            }

        }

        if (current.hasSeveralPropArrayElementXmlGetter() != another.hasSeveralPropArrayElementXmlGetter()) {
            if (current.hasSeveralPropArrayElementXmlGetter()) {
                reporter.compatibleChange("Xml array element getter of interface property " + current.getQName() + " has been added");
            } else {
                reporter.incompatibleChange("Xml array element getter of interface property " + current.getQName() + " has been removed");
                isCompatible = false;
            }

        }

        if (current.hasSeveralPropArrayElementXmlSetter() != another.hasSeveralPropArrayElementXmlSetter()) {
            if (current.hasSeveralPropArrayElementXmlSetter()) {
                reporter.compatibleChange("Xml array element setter of interface property " + current.getQName() + " has been added");
            } else {
                reporter.compatibleChange("Xml array element setter of interface property " + current.getQName() + " has been removed");
                isCompatible = false;
            }

        }

        if (current.hasSeveralPropArrayElementInsertion() != another.hasSeveralPropArrayElementInsertion()) {
            if (current.hasSeveralPropArrayElementInsertion()) {
                reporter.compatibleChange("Element insertion of interface property " + current.getQName() + " has been added");
            } else {
                reporter.incompatibleChange("Element insertion of interface property " + current.getQName() + " has been removed");
                isCompatible = false;
            }

        }
        if (current.hasSeveralPropArrayElementAddition() != another.hasSeveralPropArrayElementAddition()) {
            if (current.hasSeveralPropArrayElementAddition()) {
                reporter.compatibleChange("Element addition of interface property " + current.getQName() + " has been added");
            } else {
                reporter.incompatibleChange("Element addition of interface property " + current.getQName() + " has been removed");
                isCompatible = false;
            }
        }

        if (current.hasSeveralPropArrayNewElementAddition() != another.hasSeveralPropArrayNewElementAddition()) {
            if (current.hasSeveralPropArrayNewElementAddition()) {
                reporter.compatibleChange("New element addition of interface property " + current.getQName() + " has been added");
            } else {
                reporter.incompatibleChange("New element addition of interface property " + current.getQName() + " has been removed");
                isCompatible = false;
            }

        }

        if (current.hasSeveralPropArrayNewElementInsertion() != another.hasSeveralPropArrayNewElementInsertion()) {
            if (current.hasSeveralPropArrayNewElementInsertion()) {
                reporter.compatibleChange("New element insertion of interface property " + current.getQName() + " has been added");
            } else {
                reporter.incompatibleChange("New element insertion of interface property " + current.getQName() + " has been removed");
                isCompatible = false;
            }

        }

        if (current.hasSeveralPropArrayElementRemoving() != another.hasSeveralPropArrayElementRemoving()) {
            if (current.hasSeveralPropArrayElementRemoving()) {
                reporter.compatibleChange("Element removing of interface property " + current.getQName() + " has been added");
            } else {
                reporter.incompatibleChange("Element removing of interface property " + current.getQName() + " has been removed");
                isCompatible = false;
            }

        }

        if (current.hasSeveralPropNullCheck() != another.hasSeveralPropNullCheck()) {
            if (current.hasSeveralPropNullCheck()) {
                reporter.compatibleChange("Null check of interface property " + current.getQName() + " has been added");
            } else {
                reporter.incompatibleChange("Null check of interface property " + current.getQName() + " has  been removed");
                isCompatible = false;
            }

        }
        if (current.hasSeveralPropSizeAccess() != another.hasSeveralPropSizeAccess()) {
            if (current.hasSeveralPropSizeAccess()) {
                reporter.compatibleChange("Size access of interface property " + current.getQName() + " has been added");
            } else {
                reporter.incompatibleChange("Size access of interface property " + current.getQName() + " has been removed");
                isCompatible = false;
            }

        }
        return isCompatible;
    }

    private static boolean compareXBeansClasses(XBeansClass current, XBeansClass another, IReporter reporter) {
        return true;
    }
}
