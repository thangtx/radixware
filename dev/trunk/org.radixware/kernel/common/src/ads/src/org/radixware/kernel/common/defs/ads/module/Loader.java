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
package org.radixware.kernel.common.defs.ads.module;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.defs.RadixObject.EEditState;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.AdsDomainDef;
import org.radixware.kernel.common.defs.ads.clazz.AdsDynamicClassDef;
import org.radixware.kernel.common.defs.ads.clazz.AdsEnumClassDef;
import org.radixware.kernel.common.defs.ads.clazz.AdsExceptionClassDef;
import org.radixware.kernel.common.defs.ads.clazz.AdsInterfaceClassDef;
import org.radixware.kernel.common.defs.ads.clazz.algo.AdsAlgoClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsApplicationClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityGroupClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsPresentationEntityAdapterClassDef;
import org.radixware.kernel.common.defs.ads.clazz.form.AdsFormHandlerClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.AdsCursorClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.AdsProcedureClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.AdsStatementClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportClassDef;
import org.radixware.kernel.common.defs.ads.command.AdsContextlessCommandDef;
import org.radixware.kernel.common.defs.ads.doc.AdsDocMapDef;
import org.radixware.kernel.common.defs.ads.doc.AdsDocTopicDef;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumDef;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsParagraphExplorerItemDef;
import org.radixware.kernel.common.defs.ads.localization.AdsLocalizingBundleDef;
import org.radixware.kernel.common.defs.ads.localization.AdsPhraseBookDef;
import org.radixware.kernel.common.defs.ads.msdl.AdsMsdlSchemeDef;
import org.radixware.kernel.common.defs.ads.rights.AdsRoleDef;
import org.radixware.kernel.common.defs.ads.ui.AdsCustomDialogDef;
import org.radixware.kernel.common.defs.ads.ui.AdsCustomPropEditorDef;
import org.radixware.kernel.common.defs.ads.ui.AdsCustomWidgetDef;
import org.radixware.kernel.common.defs.ads.ui.rwt.AdsRwtCustomDialogDef;
import org.radixware.kernel.common.defs.ads.ui.rwt.AdsRwtCustomPropEditorDef;
import org.radixware.kernel.common.defs.ads.ui.rwt.AdsRwtCustomWidgetDef;
import org.radixware.kernel.common.defs.ads.xml.AdsXmlSchemeDef;
import org.radixware.kernel.common.defs.ads.xslt.AdsXsltDef;
import org.radixware.kernel.common.enums.EClassType;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.repository.ads.fs.IRepositoryAdsDefinition;
import org.radixware.kernel.common.repository.ads.fs.IRepositoryAdsLocaleDefinition;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.upgrade.Upgrader;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.schemas.adsdef.AdsDefinitionDocument;
import org.radixware.schemas.adsdef.ClassDefinition;

public class Loader {

    AdsModule module;

    Loader(AdsModule module) {
        this.module = module;
    }

    public static AdsDefinition loadFromStream(InputStream is, File file, boolean isUserModuleContext, boolean fromAPI) throws IOException {
        try {
            final AdsDefinitionDocument xDoc = AdsDefinitionDocument.Factory.parse(is);
            if (xDoc.getAdsDefinition() != null) {

                final AdsDefinition def = loadFrom(xDoc, isUserModuleContext, fromAPI);
                if (def != null) {
                    if (file != null) {
                        def.setFileLastModifiedTime(file.lastModified());
                    } else {
                        def.setFileLastModifiedTime(0l);
                    }
                    def.setEditState(EEditState.NONE);
                    return def;
                }
            }
            throw new RadixError("Definition format is not supported");
        } catch (XmlException ex) {
            throw new RadixError(ex.getMessage(), ex);
        }
    }

    static AdsDefinition loadFromRepository(AdsModule context, IRepositoryAdsDefinition repository, boolean fromAPI) throws IOException {
        try {
            final AdsDefinition preloaded = repository.getPreLoadedDefinition();
            AdsDefinition result = null;
            if (preloaded != null) {
                result = preloaded;
            } else if (repository instanceof IRepositoryAdsLocaleDefinition) {
                result = loadFromLocaleRepository(result, (IRepositoryAdsLocaleDefinition) repository, context.isUserModule(), fromAPI);
            } else {
                result = loadFromRepository(result, repository, repository.getFile(), context.isUserModule(), fromAPI);
            }
            if (result != null) {
                result.setEditState(EEditState.NONE);
            }
            return result;
        } catch (Throwable anyCause) { // NullPointer or EnumerationNotFound while parsing XML, DefinitionError, IO, XmlException, etc.
            throw new IOException("Cannot load definition: " + repository.getName(), anyCause);
        }
    }

    private static AdsDefinition loadFromLocaleRepository(AdsDefinition result, IRepositoryAdsLocaleDefinition localeRepository, boolean isUserModule, boolean fromAPI) throws IOException, XmlException {
        for (IRepositoryAdsDefinition r : localeRepository.getRepositories().values()) {
            if (r instanceof IRepositoryAdsLocaleDefinition) {
                result = loadFromLocaleRepository(result, (IRepositoryAdsLocaleDefinition) r, isUserModule, fromAPI);
            } else {
                result = loadFromRepository(result, r, r.getFile(), isUserModule, fromAPI);
            }
        }
        return result;
    }

    static AdsDefinition loadFromRepository(AdsDefinition result, IRepositoryAdsDefinition repository, final File file, boolean isUserModule, boolean fromAPI) throws IOException, XmlException {
        try {
            long fileTime = (file != null ? file.lastModified() : 0L);

            final XmlObject xmlObject = Upgrader.loadFromRepository(repository);
            final AdsDefinitionDocument xDoc = (AdsDefinitionDocument) xmlObject.changeType(AdsDefinitionDocument.type);
            if (result == null) {
                result = loadFrom(xDoc, isUserModule, fromAPI);
                result.setFileLastModifiedTime(fileTime);
            } else {
                if (result instanceof AdsLocalizingBundleDef && xDoc.getAdsDefinition() != null && xDoc.getAdsDefinition().isSetAdsLocalizingBundleDefinition()) {
                    ((AdsLocalizingBundleDef) result).loadStrings(xDoc.getAdsDefinition().getAdsLocalizingBundleDefinition());
                    if (result.getFileLastModifiedTime() < fileTime) {
                        result.setFileLastModifiedTime(fileTime);
                    }
                }
            }
            if (file != null/* && !(result instanceof AdsLocalizingBundleDef)*/) {
                final Id defId = result.getId();
                final Id idFromFile = AdsDefinition.fileName2DefinitionId(file);
                if (!Utils.equals(defId, idFromFile)) {
                    throw new DefinitionError("Definition identifier '" + String.valueOf(defId) + "' must be equal to its file '" + file.getName() + "'.");
                }
            }
            return result;
        } catch (Throwable t) {
            throw new LoadDefFromFileExcepion(file, t);
        }
    }

    private static final AdsDefinition loadFrom(org.radixware.schemas.adsdef.AdsDefinitionDocument xDoc, boolean isUserModuleContext, boolean fromAPI) {
        return loadFrom(xDoc.getAdsDefinition(), isUserModuleContext, fromAPI);
    }

    public static final AdsDefinition loadFrom(org.radixware.schemas.adsdef.AdsDefinitionElementType xDefRoot, boolean fromAPI) {
        return loadFrom(xDefRoot, false, fromAPI);
    }

    public static final AdsDefinition loadFrom(org.radixware.schemas.adsdef.AdsDefinitionElementType xDefRoot, boolean isUserModuleContext, boolean fromAPI) {
        AdsDefinition result = null;
        if (xDefRoot != null) {
            if (xDefRoot.isSetAdsClassDefinition()) {
                ClassDefinition classDef = xDefRoot.getAdsClassDefinition();
                EClassType classType = classDef.getType();
                switch (classType) {
                    case ALGORITHM:
                        result = AdsAlgoClassDef.Factory.loadFrom(classDef);
                        break;
                    case APPLICATION:
                        result = AdsApplicationClassDef.Factory.loadFrom(classDef);
                        break;
                    case DIALOG_MODEL:
                        break;
                    //should not be here
                    case DYNAMIC:
                        result = AdsDynamicClassDef.Factory.loadFrom(classDef);
                        break;
                    case ENUMERATION:
                        result = AdsEnumClassDef.Factory.loadFrom(classDef);
                        break;
                    case ENTITY:
                        result = AdsEntityClassDef.Factory.loadFrom(classDef);
                        break;
                    case ENTITY_GROUP:
                        result = AdsEntityGroupClassDef.Factory.loadFrom(classDef);
                        break;
                    case ENTITY_MODEL:
                        break;
                    //should not be here
                    case EXCEPTION:
                        result = AdsExceptionClassDef.Factory.loadFrom(classDef);
                        break;
                    case FORM_HANDLER:
                        result = AdsFormHandlerClassDef.Factory.loadFrom(classDef);
                        break;
                    case FORM_MODEL:
                        //should not be here
                        break;
                    case INTERFACE:
                        result = AdsInterfaceClassDef.Factory.loadFrom(classDef);
                        break;
                    case PARAGRAPH_MODEL:
                        break;
                    case PRESENTATION_ENTITY_ADAPTER:
                        result = AdsPresentationEntityAdapterClassDef.Factory.loadFrom(classDef);
                        break;
                    case PROP_EDITOR_MODEL:
                        break;
                    case REPORT:
                        if (isUserModuleContext) {
                            result = AdsReportClassDef.Factory.loadFromUser(classDef);
                        } else {
                            result = AdsReportClassDef.Factory.loadFrom(classDef, fromAPI);
                        }
                        break;
                    case SQL_CURSOR:
                        result = AdsCursorClassDef.Factory.loadFrom(classDef);
                        break;
                    case SQL_PROCEDURE:
                        result = AdsProcedureClassDef.Factory.loadFrom(classDef);
                        break;
                    case SQL_STATEMENT:
                        result = AdsStatementClassDef.Factory.loadFrom(classDef);
                        break;
                    default:
                        throw new DefinitionError("Unsupported class type");
                }
            } else if (xDefRoot.isSetAdsContextlessCommandDefinition()) {
                result = AdsContextlessCommandDef.Factory.loadFrom(xDefRoot.getAdsContextlessCommandDefinition());
            } else if (xDefRoot.isSetAdsPhraseBookDefinition()) {
                result = AdsPhraseBookDef.Factory.loadFrom(xDefRoot.getAdsPhraseBookDefinition());
            } else if (xDefRoot.isSetAdsCustomDialogDefinition()) {
                result = AdsCustomDialogDef.Factory.loadFrom(xDefRoot.getAdsCustomDialogDefinition());
            } else if (xDefRoot.isSetAdsCustomPropEditorDefinition()) {
                result = AdsCustomPropEditorDef.Factory.loadFrom(xDefRoot.getAdsCustomPropEditorDefinition());
            } else if (xDefRoot.isSetAdsEnumDefinition()) {
                result = AdsEnumDef.Factory.loadFrom(xDefRoot.getAdsEnumDefinition());
            } else if (xDefRoot.isSetAdsLocalizingBundleDefinition()) {
                result = AdsLocalizingBundleDef.Factory.loadFrom(xDefRoot.getAdsLocalizingBundleDefinition());
            } else if (xDefRoot.isSetAdsMsdlSchemeDefinition()) {
                result = AdsMsdlSchemeDef.Factory.loadFrom(xDefRoot.getAdsMsdlSchemeDefinition());
            } else if (xDefRoot.isSetAdsXsltSchemeDefinition()) {
                result = AdsXsltDef.Factory.loadFrom(xDefRoot.getAdsXsltSchemeDefinition());
            } else if (xDefRoot.isSetAdsParagraphDefinition()) {
                result = AdsParagraphExplorerItemDef.Factory.loadFrom(xDefRoot.getAdsParagraphDefinition());
            } else if (xDefRoot.isSetAdsRoleDefinition()) {
                result = AdsRoleDef.Factory.loadFrom(xDefRoot.getAdsRoleDefinition(), xDefRoot.getFormatVersion());
            } else if (xDefRoot.isSetAdsXmlSchemeDefinition()) {
                result = AdsXmlSchemeDef.Factory.loadFrom(xDefRoot.getAdsXmlSchemeDefinition());
            } else if (xDefRoot.isSetAdsDomainDefinition()) {
                result = AdsDomainDef.Factory.loadFrom(xDefRoot.getAdsDomainDefinition());
            } else if (xDefRoot.isSetAdsCustomWidgetDefinition()) {
                result = AdsCustomWidgetDef.Factory.loadFrom(xDefRoot.getAdsCustomWidgetDefinition());
            } else if (xDefRoot.isSetAdsWebCustomDialogDefinition()) {
                result = AdsRwtCustomDialogDef.Factory.loadFrom(xDefRoot.getAdsWebCustomDialogDefinition());
            } else if (xDefRoot.isSetAdsWebCustomPropEditorDefinition()) {
                result = AdsRwtCustomPropEditorDef.Factory.loadFrom(xDefRoot.getAdsWebCustomPropEditorDefinition());
            } else if (xDefRoot.isSetAdsWebCustomWidgetDefinition()) {
                result = AdsRwtCustomWidgetDef.Factory.loadFrom(xDefRoot.getAdsWebCustomWidgetDefinition());
            } else if (xDefRoot.isSetAdsDocumentationTopicDefinition()) {
                result = AdsDocTopicDef.Factory.loadFrom(xDefRoot.getAdsDocumentationTopicDefinition());
            } else if (xDefRoot.isSetAdsDocumentationMapDefinition()) {
                result = AdsDocMapDef.Factory.loadFrom(xDefRoot.getAdsDocumentationMapDefinition());
            } else {
                throw new DefinitionError("Unsupported definition format");
            }
        }
        if (result == null) {
            throw new DefinitionError("Unsupported definition format");
        } else if (fromAPI) {
            result.markAsLoadedFromAPI();
        }

        return result;
    }

    public static class LoadDefFromFileExcepion extends IOException {

        private final File file;

        public LoadDefFromFileExcepion(File file) {
            this.file = file;
        }

        public LoadDefFromFileExcepion(File file, String message) {
            super(message);
            this.file = file;
        }

        public LoadDefFromFileExcepion(File file, String message, Throwable cause) {
            super(message, cause);
            this.file = file;
        }

        public LoadDefFromFileExcepion(File file, Throwable cause) {
            super(cause);
            this.file = file;
        }

        public File getFile() {
            return file;
        }
    }
}
