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
package org.radixware.kernel.radixdoc.xmlexport;

import java.awt.HeadlessException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipOutputStream;
import javax.swing.JOptionPane;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.impl.xb.xsdschema.ImportDocument.Import;
import org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument;
import org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Schema;
import org.radixware.kernel.common.components.ICancellable;
import org.radixware.kernel.common.components.IProgressHandle;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.exceptions.RadixdocGenerationException;
import org.radixware.kernel.common.radixdoc.IDocLogger;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.kernel.common.utils.RequestProcessor;
import org.radixware.kernel.common.utils.XmlObjectProcessor;
import org.radixware.kernel.radixdoc.ditagenerator.EDitaGenerationMode;
import org.radixware.kernel.radixdoc.generator.ERadixdocOutputFormat;
import org.radixware.kernel.radixdoc.generator.GeneratorUtils;
import org.radixware.kernel.radixdoc.generator.RadixdocGenerationContext;
import org.radixware.kernel.radixdoc.generator.RadixdocGenerationTask;
import org.radixware.kernel.radixdoc.generator.RadixdocGenerator;
import org.radixware.schemas.adsdef.AdsDefinitionDocument;
import org.radixware.schemas.adsdef.XmlDefinition;
import org.xmlsoap.schemas.wsdl.DefinitionsDocument;
import org.xmlsoap.schemas.wsdl.TDefinitions;
import org.xmlsoap.schemas.wsdl.TImport;
import org.xmlsoap.schemas.wsdl.TTypes;

public class XmlSchemaExporter {

    private final static String WARNING_DLG_TITLE = "Skip or replace";
    private final static String WARNING_DLG_TEXT = "The destination already has a file named \"{0}\". What do you want to do?";
    private final static String[] WARNING_DLG_VARIANTS = {"Skip", "Skip All", "Replace", "Replace All", "Cancel Export"};

    private final Map<String, String> schemaNs2FileNameAds = new HashMap<>();
    private final Map<String, String> schemaNs2FileNameKernel = new HashMap<>();
    private final List<List<Id>> exportedSchemasList = new ArrayList<>();
    private final Set<Id> enumIds = new HashSet<>();

    private final XmlSchemasExportTask task;
    private final RadixdocGenerationContext context;
    private final ICancellable cancellable;

    private boolean isSkipAll = false;
    private boolean isReplaceAll = false;

    private Map<EExportResult, String> messages;

    public XmlSchemaExporter(XmlSchemasExportTask task, RadixdocGenerationContext context) {
        this.task = task;
        this.context = context;
        cancellable = context.getCancellable() == null ? GeneratorUtils.getMockCancellable() : context.getCancellable();
    }

    public void exportSchemas(final boolean sync) {
        exportSchemas(sync, null);
    }

    public void exportSchemas(final boolean sync, final Map<EExportResult, String> resultMessages) {
        if (task == null || context == null) {
            return;
        }

        final File dir = task.getTargetDir();

        if (dir == null) {
            return;
        }

        if (!dir.exists()) {
            dir.mkdirs();
        }

        final IDocLogger logger = context.getDocLogger();
        if (logger != null) {
            logger.put(EEventSeverity.DEBUG, "Exporting schema files to " + dir.getAbsolutePath());
        }

        messages = resultMessages == null ? GeneratorUtils.getDefaultResultMessages() : resultMessages;

        final boolean[] taskResult = new boolean[1];
        taskResult[0] = true;

        final ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        Runnable tsk = new Runnable() {
            @Override
            public void run() {
                final ClassLoader prevContextClassLoader = Thread.currentThread().getContextClassLoader();
                Thread.currentThread().setContextClassLoader(contextClassLoader);

                IProgressHandle progress = context.getProgressHandler();

                progress.start();
                progress.switchToIndeterminate();
                progress.setDisplayName("Exporting schemas");

                try {
                    List<IExportableXmlSchema> schemas = task.getExportingSchemas();

                    loop:
                    for (IExportableXmlSchema schema : schemas) {
                        List<Id> exportedSchemas = new ArrayList<>();
                        enumIds.clear();
                        if (cancellable.wasCancelled()) {
                            if (logger != null) {
                                logger.put(EEventSeverity.WARNING, messages.get(EExportResult.CANCEL));
                            }
                            return;
                        }

                        ZipOutputStream zipStream = null;
                        String zipName = "";
                        if (task.isZipSchemas()) {
                            String layerVersion = !GeneratorUtils.isDefinitionHasZIPTitle(context, schema.getId()) ? "" : (" v." + GeneratorUtils.getLayerVersion(context, schema.getId()));
                            zipName = GeneratorUtils.getZIPName(task.getTargetLangs().get(0), context, schema.getId()) + layerVersion + ".zip";
                            if (".zip".equals(zipName)) {
                                XmlObject schemaObject = XsdExportUtils.getSchemaObject(schema, logger);
                                if (schemaObject == null) {
                                    taskResult[0] = false;
                                    return;
                                }
                                String namespace = XsdExportUtils.getSchemaNamespace(schemaObject);
                                String tmpZipName = transformNamespaceToPath(namespace, task.isUseExtendedNames());

                                if (tmpZipName == null || tmpZipName.isEmpty()) {
                                    zipName = schema.getTitle();
                                } else {
                                    if (tmpZipName.contains(".xsd")) {
                                        zipName = tmpZipName.replace(".xsd", zipName);
                                    } else if (tmpZipName.contains(".xsd")) {
                                        zipName = tmpZipName.replace(".xsd", zipName);
                                    } else {
                                        zipName = tmpZipName + zipName;
                                    }
                                }
                            }
                            zipName = zipName.replaceAll("[\\\\/:*?\"<>|]", "-");

                            File schemasZip = new File(dir, zipName);
                            if (!schemasZip.exists()) {
                                schemasZip.createNewFile();
                            } else {
                                if (isSkipAll) {
                                    continue;
                                }

                                if (!isReplaceAll) {
                                    if (cancellable.wasCancelled()) {
                                        if (logger != null) {
                                            logger.put(EEventSeverity.WARNING, messages.get(EExportResult.CANCEL));
                                        }
                                        return;
                                    }

                                    int result = JOptionPane.showOptionDialog(
                                            null,
                                            MessageFormat.format(WARNING_DLG_TEXT, zipName),
                                            WARNING_DLG_TITLE,
                                            JOptionPane.YES_NO_CANCEL_OPTION,
                                            JOptionPane.WARNING_MESSAGE,
                                            null,
                                            WARNING_DLG_VARIANTS,
                                            null
                                    );

                                    switch (result) {
                                        case 0:
                                            continue;
                                        case 1:
                                            isSkipAll = true;
                                            continue;
                                        case 2:
                                            break;
                                        case 3:
                                            isReplaceAll = true;
                                            break;
                                        default:
                                            if (logger != null) {
                                                logger.put(EEventSeverity.WARNING, messages.get(EExportResult.CANCEL));
                                            }
                                            cancellable.cancel();
                                            return;
                                    }
                                }
                            }

                            FileOutputStream schemasZipStream = new FileOutputStream(schemasZip);
                            zipStream = new ZipOutputStream(schemasZipStream);
                        }

                        try {
                            File tmpDir = task.isZipSchemas() ? XsdExportUtils.getTempSchemasDir(logger) : dir;

                            if (tmpDir == null) {
                                taskResult[0] = false;
                                return;
                            }

                            if (!exportSchema(schema, exportedSchemas, tmpDir)) {
                                taskResult[0] = false;
                                return;
                            }

                            if (cancellable.wasCancelled()) {
                                if (logger != null) {
                                    logger.put(EEventSeverity.WARNING, messages.get(EExportResult.CANCEL));
                                }
                                return;
                            }

                            if (zipStream != null) {
                                if (logger != null) {
                                    logger.put(EEventSeverity.DEBUG, "Packing schemas into " + zipName);
                                }
                                XsdExportUtils.copyXsdFromDirToZip(tmpDir, zipStream, logger);
                            }

                            if (cancellable.wasCancelled()) {
                                if (logger != null) {
                                    logger.put(EEventSeverity.WARNING, messages.get(EExportResult.CANCEL));
                                }
                                return;
                            }
                        } catch (Exception ex) {
                            logger.put(EEventSeverity.ERROR, ExceptionTextFormatter.throwableToString(ex));
                            Logger.getLogger(XmlSchemaExporter.class.getName()).log(Level.SEVERE, null, ex);
                            taskResult[0] = false;
                        } finally {
                            if (zipStream != null) {
                                zipStream.flush();
                                zipStream.close();
                            }
                        }

                        if (!exportedSchemas.isEmpty()) {
                            Collections.reverse(exportedSchemas);
                            exportedSchemas.addAll(enumIds);
                            exportedSchemasList.add(exportedSchemas);
                        }
                    }

                    if (task.isExportDoc()) {
                        progress.setDisplayName("Generating documentation");

                        File docDir = new File(dir, "doc");
                        if (docDir.exists()) {
                            if (FileUtils.deleteDirectory(docDir)) {
                                FileUtils.mkDirs(docDir);
                            }
                        } else {
                            FileUtils.mkDirs(docDir);
                        }

                        RadixdocGenerationTask docGenTask = new RadixdocGenerationTask(
                                exportedSchemasList,
                                task.getTargetLangs(),
                                docDir, ERadixdocOutputFormat.PDF,
                                EDitaGenerationMode.TOPICS_CLUSTER
                        );

                        RadixdocGenerator.generate(docGenTask, context);

                        if (docDir.isDirectory()) {
                            if (docDir.list().length == 0) {
                                FileUtils.deleteDirectory(docDir);
                            }
                        }
                    }

                    if (cancellable.wasCancelled()) {
                        if (logger != null) {
                            logger.put(EEventSeverity.WARNING, messages.get(EExportResult.CANCEL));
                        }
                        return;
                    }
                } catch (IOException | HeadlessException | RadixdocGenerationException ex) {
                    Logger.getLogger(XmlSchemaExporter.class.getName()).log(Level.SEVERE, null, ex);
                    if (logger != null) {
                        logger.put(EEventSeverity.ERROR, ExceptionTextFormatter.throwableToString(ex));
                    }
                    taskResult[0] = false;
                } finally {
                    Thread.currentThread().setContextClassLoader(prevContextClassLoader);
                    progress.finish();
                }

                if (taskResult[0]) {
                    context.getDocLogger().put(EEventSeverity.EVENT, messages.get(EExportResult.SUCCESS));
                } else {
                    context.getDocLogger().put(EEventSeverity.ERROR, messages.get(EExportResult.FAIL));
                }
            }
        };
        if (sync) {
            tsk.run();
        } else {
            RequestProcessor.submit(tsk);
        }
    }

    private boolean exportSchema(IExportableXmlSchema schema, List<Id> exportedSchemasInfo, File dir) {
        IDocLogger logger = context.getDocLogger();

        XmlObject schemaObject = XsdExportUtils.getSchemaObject(schema, logger);
        if (schemaObject == null) {
            String schemaId = schema.getId() == null ? schema.getTitle() : schema.getId().toString();
            logger.put(EEventSeverity.WARNING, "Can't export schema '" + schema.getTitle() + "' with id '" + schemaId + "': file not found");
            return true;
        }

        if (schema.getDefinitionInputStream() != null) {
            XmlDefinition schemaDefObject = null;
            try {
                AdsDefinitionDocument xDoc = AdsDefinitionDocument.Factory.parse(schema.getDefinitionInputStream());
                if (xDoc != null && xDoc.getAdsDefinition() != null && xDoc.getAdsDefinition().isSetAdsXmlSchemeDefinition()) {
                    schemaDefObject = xDoc.getAdsDefinition().getAdsXmlSchemeDefinition();
                } else {
                    return false;
                }
            } catch (XmlException | IOException ex) {
                Logger.getLogger(XmlSchemaExporter.class.getName()).log(Level.SEVERE, "Can not parse schema", ex);
                if (context.getDocLogger() != null) {
                    context.getDocLogger().put(EEventSeverity.ERROR, ExceptionTextFormatter.exceptionStackToString(ex));
                }
                return false;
            }

            if (schemaDefObject == null) {
                return false;
            }

            if (task.isEmbeddedDoc()) {
                if (schemaDefObject.getDocumentation() != null && schemaDefObject.getDocumentation().getXmlItemDocEntries() != null) {
                    if (task.getEmbeddedDocLangs() != null) {
                        for (EIsoLanguage lang : task.getEmbeddedDocLangs()) {
                            XsdExportUtils.embedNodeDocumentation(
                                    schemaObject,
                                    schemaDefObject.getDocumentation().getXmlItemDocEntries(),
                                    schema.getLocalizedStrings(lang),
                                    lang
                            );
                        }
                    }
                }
            }
        }

        schema.processEnumerations(schemaObject.getDomNode());

        String fileName = getSchemaFilePath(schema, schemaObject, task.isUseExtendedNames());
        String ns = XsdExportUtils.getSchemaNamespace(schemaObject);
        boolean needReplaceSavedSchema = false;

        if (schema.getId() == null) {
            if (schemaNs2FileNameAds.containsKey(ns) && fileName.equals(schemaNs2FileNameAds.get(ns))) {
                return true;
            }

            if (schemaNs2FileNameKernel.containsKey(ns) && fileName.equals(schemaNs2FileNameKernel.get(ns))) {
                return true;
            }

            schemaNs2FileNameKernel.put(ns, fileName);
        } else {
            if (schemaNs2FileNameAds.containsKey(ns)) {
                return true;
            }

            if (schemaNs2FileNameKernel.containsKey(ns) && fileName.equals(schemaNs2FileNameKernel.get(ns))) {
                needReplaceSavedSchema = true;
            }

            schemaNs2FileNameAds.put(ns, fileName);
        }

        for (IExportableXmlSchema include : XsdExportUtils.getSchemaIncludes(schema, logger)) {
            exportSchema(include, exportedSchemasInfo, dir);
        }

        if (task.isSaveLinkledSchemas()) {
            for (IExportableXmlSchema linkedSchema : schema.getLinkedSchemas()) {
                if (!exportSchema(linkedSchema, exportedSchemasInfo, dir)) {
                    return false;
                }
            }
        }

        if (schema.getId() != null) {
            exportedSchemasInfo.add(schema.getId());
        }

        try {
            File out = new File(dir, fileName);

            if (out.exists() && !needReplaceSavedSchema) {
                if (isSkipAll) {
                    enumIds.addAll(schema.getEnumIds());
                    if (task.isSaveLinkledSchemas()) {
                        for (IExportableXmlSchema linkedSchema : schema.getLinkedSchemas()) {
                            if (!exportSchema(linkedSchema, exportedSchemasInfo, dir)) {
                                return false;
                            }
                        }
                    }
                    return true;
                }

                if (!isReplaceAll) {
                    if (cancellable.wasCancelled()) {
                        if (logger != null) {
                            logger.put(EEventSeverity.WARNING, messages.get(EExportResult.CANCEL));
                        }
                        return false;
                    }

                    int result = JOptionPane.showOptionDialog(
                            null,
                            MessageFormat.format(WARNING_DLG_TEXT, fileName),
                            WARNING_DLG_TITLE,
                            JOptionPane.YES_NO_CANCEL_OPTION,
                            JOptionPane.WARNING_MESSAGE,
                            null,
                            WARNING_DLG_VARIANTS,
                            null
                    );

                    switch (result) {
                        case 0:
                            if (task.isSaveLinkledSchemas()) {
                                enumIds.addAll(schema.getEnumIds());
                                for (IExportableXmlSchema linkedSchema : schema.getLinkedSchemas()) {
                                    if (!exportSchema(linkedSchema, exportedSchemasInfo, dir)) {
                                        return false;
                                    }
                                }
                            }
                            return true;
                        case 1:
                            isSkipAll = true;
                            enumIds.addAll(schema.getEnumIds());
                            if (task.isSaveLinkledSchemas()) {
                                for (IExportableXmlSchema linkedSchema : schema.getLinkedSchemas()) {
                                    if (!exportSchema(linkedSchema, exportedSchemasInfo, dir)) {
                                        return false;
                                    }
                                }
                            }
                            return true;
                        case 2:
                            break;
                        case 3:
                            isReplaceAll = true;
                            break;
                        default:
                            if (logger != null) {
                                logger.put(EEventSeverity.WARNING, messages.get(EExportResult.CANCEL));
                            }
                            cancellable.cancel();
                            return false;
                    }
                }
            }
            enumIds.addAll(schema.getEnumIds());
            actualizeLocation(schemaObject);

            schemaObject.save(out, new XmlOptions().setSavePrettyPrint());
            if (logger != null && !needReplaceSavedSchema) {
                logger.put(EEventSeverity.DEBUG, "Exported " + fileName);
            }
        } catch (IOException ex) {
            Logger.getLogger(XmlSchemaExporter.class.getName()).log(Level.SEVERE, null, ex);
            if (context.getDocLogger() != null) {
                context.getDocLogger().put(EEventSeverity.ERROR, ExceptionTextFormatter.exceptionStackToString(ex));
            }
            return false;
        }

        if (cancellable.wasCancelled()) {
            logger.put(EEventSeverity.WARNING, messages.get(EExportResult.CANCEL));
            return false;
        }

        return true;
    }

    private String transformNamespaceToPath(String ns, boolean isUseExtendedNames) {
        if (ns == null) {
            return null;
        }

        if (!isUseExtendedNames) {
            String parts[] = ns.trim().split("/");
            if (parts.length > 0) {
                return parts[parts.length - 1];
            }
        }

        return ns.trim().replace("http://", "").replaceAll("/", "-");
    }

    private String getSchemaFilePath(IExportableXmlSchema schema, XmlObject schemaObject, boolean isUseExtendedNames) {
        if (schema instanceof KernelXmlSchemaExportableWrapper) {
            return schema.getTitle();
        }

        String namespace = XsdExportUtils.getSchemaNamespace(schemaObject);
        String tmpFilePath = transformNamespaceToPath(namespace, isUseExtendedNames);
        String fileName = tmpFilePath == null || tmpFilePath.isEmpty() ? schema.getTitle() : tmpFilePath;

        if (!fileName.endsWith(".xsd") && !fileName.endsWith(".wsdl")) {
            if (schemaObject instanceof SchemaDocument) {
                fileName = fileName + ".xsd";
            } else {
                fileName = fileName + ".wsdl";
            }
        }

        fileName = fileName.replaceAll("[^a-zA-Z0-9.-]", "-").toLowerCase();

        if (!schemaNs2FileNameAds.containsKey(namespace) && schemaNs2FileNameAds.containsValue(fileName)) {
            if (!isUseExtendedNames) {
                return getSchemaFilePath(schema, schemaObject, true);
            } else {
                String fileNameBody = fileName.substring(0, fileName.lastIndexOf("."));
                String fileNameExt = fileName.substring(fileName.lastIndexOf("."), fileName.length());

                int suffix = 1;                
                
                while (schemaNs2FileNameAds.containsValue(fileName)) {
                    fileName = fileNameBody + suffix + fileNameExt;
                    suffix++;
                }                
            }
        }

        return fileName;
    }

    private void actualizeLocation(XmlObject schemaObject) {
        if (schemaObject instanceof SchemaDocument) {
            Schema xSchema = ((SchemaDocument) schemaObject).getSchema();
            actualizeXsdLocation(xSchema);
        } else if (schemaObject instanceof DefinitionsDocument) {
            TDefinitions xDefinitions = ((DefinitionsDocument) schemaObject).getDefinitions();
            actualizeWsdlLocation(xDefinitions);
        }
    }

    private void actualizeXsdLocation(Schema schemaObject) {
        for (Import imp : schemaObject.getImportArray()) {
            String impNs = imp.getNamespace();

            String oldLocation = imp.getSchemaLocation();
            if (oldLocation != null) {
                String newlocation;
                if (oldLocation.startsWith("radix:/")) {
                    newlocation = schemaNs2FileNameKernel.containsKey(impNs) ? schemaNs2FileNameKernel.get(impNs) : oldLocation;
                } else {
                    newlocation = schemaNs2FileNameAds.containsKey(impNs) ? schemaNs2FileNameAds.get(impNs) : oldLocation;
                }

                if (!oldLocation.equals(newlocation)) {
                    imp.setSchemaLocation(newlocation);
                }
            }
        }
    }

    private void actualizeWsdlLocation(TDefinitions schemaObject) {
        for (TImport imp : schemaObject.getImportList()) {
            String impNs = imp.getNamespace();

            String oldLocation = imp.getLocation();
            if (oldLocation != null) {
                String newlocation;
                if (oldLocation.startsWith("radix:/")) {
                    newlocation = schemaNs2FileNameKernel.containsKey(impNs) ? schemaNs2FileNameKernel.get(impNs) : oldLocation;
                } else {
                    newlocation = schemaNs2FileNameAds.containsKey(impNs) ? schemaNs2FileNameAds.get(impNs) : oldLocation;
                }

                if (!oldLocation.equals(newlocation)) {
                    imp.setLocation(newlocation);
                }
            }
        }
        for (TTypes type : schemaObject.getTypesList()) {
            XmlObject obj = XmlObjectProcessor.getXmlObjectFirstChild(type);
            if (obj != null && obj instanceof Schema) {
                actualizeXsdLocation((Schema) obj);
            }
        }
    }
}
