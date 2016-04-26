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

package org.radixware.kernel.common.builder.check.ads.clazz.presentations;

import java.text.MessageFormat;
import org.radixware.kernel.common.builder.check.ads.AdsDefinitionChecker;
import org.radixware.kernel.common.builder.check.ads.CheckUtils;
import static org.radixware.kernel.common.builder.check.common.RadixObjectChecker.error;
import org.radixware.kernel.common.builder.check.common.RadixObjectCheckerRegistration;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.IAccessible;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsFieldPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.ServerPresentationSupport;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPageDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPresentationDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsFilterDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.IAdsPresentableProperty;
import org.radixware.kernel.common.defs.ads.clazz.presentation.PropertyUsageSupport;
import org.radixware.kernel.common.defs.ads.common.AdsUtils;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsExplorerItemDef;
import org.radixware.kernel.common.enums.EEditorPageType;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.utils.RadixObjectsUtils;


@RadixObjectCheckerRegistration
public class AdsEditorPageChecker extends AdsDefinitionChecker<AdsEditorPageDef> {

    @Override
    public Class<? extends RadixObject> getSupportedClass() {
        return AdsEditorPageDef.class;
    }

    @Override
    public void check(final AdsEditorPageDef editorPage, final IProblemHandler problemHandler) {
        super.check(editorPage, problemHandler);
        switch (editorPage.getType()) {
            case CUSTOM:
                if (editorPage.getClientEnvironment() == ERuntimeEnvironmentType.COMMON_CLIENT) {
                    if (!editorPage.getCustomViewSupport().isUseCustomView(ERuntimeEnvironmentType.WEB)) {
                        warning(editorPage, problemHandler, "Missing WEB custom view for custom editor page");
                    }
                    if (!editorPage.getCustomViewSupport().isUseCustomView(ERuntimeEnvironmentType.WEB)) {
                        warning(editorPage, problemHandler, "Missing explorer custom view for custom editor page");
                    }
                } else {
                    if (!editorPage.getCustomViewSupport().isUseCustomView(editorPage.getClientEnvironment())) {
                        warning(editorPage, problemHandler, "Missing " + editorPage.getClientEnvironment().getName().toLowerCase() + " custom view for custom editor page");
                    }
                }
                break;
            case CONTAINER:
                if (editorPage.getEmbeddedExplorerItemId() != null) {
                    AdsExplorerItemDef ei = editorPage.findEmbeddedExplorerItem();
                    if (ei == null) {
                        error(editorPage, problemHandler, "Can not find embedded explorer item");
                    } else {
                        ERuntimeEnvironmentType eiEnv = ei.getClientEnvironment();
                        if (eiEnv != ERuntimeEnvironmentType.COMMON_CLIENT) {
                            if (editorPage.getClientEnvironment() != eiEnv) {
                                error(editorPage, problemHandler, "Client environment of explorer item " + ei.getQualifiedName() + "(" + ei.getClientEnvironment().getName() + ") is incompatible with editor page client environment (" + editorPage.getClientEnvironment().getName() + ")");
                            }
                        }

                        AdsUtils.checkAccessibility(editorPage, ei, false, problemHandler);
                    }
                } else {
                    error(editorPage, problemHandler, "Embedded explorer item must be specified");
                }
                break;
            default:
                break;
        }

        if (!editorPage.isIconInherited()) {
            CheckUtils.checkIconId(editorPage, editorPage.getIconId(), problemHandler, "icon");
        }

        if (!editorPage.isTitleInherited()) {
            CheckUtils.checkMLStringId(editorPage, editorPage.getTitleId(), problemHandler, "title");
        }

        if (editorPage.getType() == EEditorPageType.STANDARD) {// props check only for standard page
            //SEE RADIX-3873
            final PropertyUsageSupport support = editorPage.getUsedProperties();
            for (PropertyUsageSupport.PropertyRef ref : support.get()) {
                final AdsDefinition prop = ref.findProperty();
                if (prop == null) {
                    error(editorPage, problemHandler, "Unknown property on editor page: #" + ref.getPropertyId());
                } else {
                    AdsUtils.checkAccessibility(editorPage, prop, false, problemHandler);
                    //CheckUtils.checkExportedApiDatails(editorPage, prop, problemHandler);
                    if (prop instanceof AdsFieldPropertyDef) {//RADIX-3799
                        error(editorPage, problemHandler, "Field property on editor page: " + prop.getQualifiedName());
                    } else if (prop instanceof IAdsPresentableProperty) {
                        if (prop instanceof AdsPropertyDef) {
                            if (!((AdsPropertyDef) prop).isTransferableAsMeta(editorPage.getClientEnvironment())) {
                                if (!editorPage.isWarningSuppressed(AdsEditorPageDef.Problems.PROPERTY_WILL_NOT_BE_ACCESSIBLE_FOR_CLIENT_ENVIRONMENT)) {
                                    warning(editorPage, problemHandler, AdsEditorPageDef.Problems.PROPERTY_WILL_NOT_BE_ACCESSIBLE_FOR_CLIENT_ENVIRONMENT,
                                            prop.getQualifiedName(), editorPage.getClientEnvironment().getName());
                                }
                            }
                        }
                        final ServerPresentationSupport presentationSupport = ((IAdsPresentableProperty) prop).getPresentationSupport();
                        if (presentationSupport == null || presentationSupport.getPresentation() == null || !presentationSupport.getPresentation().isPresentable()) {
                            error(editorPage, problemHandler, "Property without presentation attributes on editor page: " + prop.getQualifiedName());
                        } else {
                            if (!presentationSupport.getPresentation().isTitleInherited()) {
                                CheckUtils.titleShouldBeDefined(prop, presentationSupport.getPresentation().getTitleId(), problemHandler, getHistory());
                            }
                            //   EditOptionsChecker.checkEditOptionsUsedInClientPart(this, editorPage, effectiveContainerEnv, prop, presentationSupport.getPresentation().getEditOptions(), problemHandler);
                        }

                        final AdsEditorPresentationDef editorPresentation = RadixObjectsUtils.findContainer(editorPage, AdsEditorPresentationDef.class);
                        if (editorPresentation != null) {
                            final AdsEditorPresentationDef.PropertyAttributesSet attributesSet = editorPresentation.getPropertyPresentationAttributesCollection().findById(prop.getId(), ExtendableDefinitions.EScope.ALL);

                            if (attributesSet != null) {
                                final Boolean presentable = attributesSet.getPresentable();
                                if (presentable == null || !presentable) {
                                    error(editorPage, problemHandler, "Non-presentable property on editor page: " + prop.getQualifiedName());
                                }
                            }
                        }
                    } else if (!(prop instanceof AdsFilterDef.Parameter)) {                        
                        error(editorPage, problemHandler, "Non-presentable property on editor page: " + prop.getQualifiedName());
                    }
                }
            }

            //check property matrix
            int columnCount = editorPage.getProperties().getColumnCount();
            int rowCount = editorPage.getProperties().getRowCount();
            if (columnCount > 0 && rowCount > 0) {
                final AdsEditorPageDef.PagePropertyRef[][] matrix = new AdsEditorPageDef.PagePropertyRef[columnCount][rowCount];
                for (AdsEditorPageDef.PagePropertyRef ref : editorPage.getProperties()) {
                    int c = ref.getColumn();
                    int r = ref.getRow();
                    if (matrix[c][r] != null) {
                        String propName1 = matrix[c][r].getReferencedPropertyName();
                        String propName2 = ref.getReferencedPropertyName();
                        error(editorPage, problemHandler, MessageFormat.format("Too many properties in single editor cell (column={0},row={1}) : {2} and {3}", c, r, propName1, propName2));
                    } else {
                        matrix[c][r] = ref;
                    }
                }
            }
        }

        availablePropertiesCheck(editorPage, problemHandler);
    }

    private void availablePropertiesCheck(final AdsEditorPageDef editorPage, final IProblemHandler problemHandler) {

        for (PropertyUsageSupport.PropertyRef propertyRef : editorPage.getUsedProperties().get()) {
            final AdsDefinition result = propertyRef.findProperty();

            if (result instanceof AdsPropertyDef) {
                if (((IAccessible) result).getAccessFlags().isStatic()) {
                    error(editorPage, problemHandler, "Editor page can't contains static properties: " + result.getName());
                }
            }
        }
    }
}
