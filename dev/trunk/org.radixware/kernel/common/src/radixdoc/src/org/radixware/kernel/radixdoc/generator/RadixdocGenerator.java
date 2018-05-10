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
package org.radixware.kernel.radixdoc.generator;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import static java.util.zip.ZipEntry.DEFLATED;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import org.radixware.kernel.common.components.ICancellable;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.exceptions.RadixdocGenerationException;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.radixdoc.ditabridge.PackedZipExecutor;
import org.radixware.kernel.radixdoc.ditagenerator.DitaFilesGenerator;
import org.radixware.kernel.radixdoc.ditagenerator.EDitaGenerationMode;
import org.radixware.kernel.radixdoc.enums.EDecorationProperties;
import org.radixware.kernel.radixdoc.html.HtmlRadixdocGenerator;
import org.radixware.kernel.radixdoc.html.TmpFileInputStreamWrapper;
import org.radixware.kernel.radixdoc.xmlexport.DefinitionDocInfo;
import org.radixware.schemas.radixdoc.AdsXmlSchemeDefDocItem;

public class RadixdocGenerator extends DocumentationGenerator {

    public static void generate(RadixdocGenerationTask task, RadixdocGenerationContext context) throws RadixdocGenerationException {

        final ICancellable cancellable = context.getCancellable() == null ? GeneratorUtils.getMockCancellable() : context.getCancellable();

        for (EIsoLanguage lang : task.getTargetLangs()) {
            for (List<Id> definitions : task.getSourceDefIdClusters()) {

                DefinitionDocInfo rootDocInfo = context.getDefinitionDocInfo(definitions.get(0));
                if (rootDocInfo == null) {
                    continue;
                }

                InputStream ditaZipFile = null;
                try {
                    File outputDir = getOutputDir(task, lang);

                    File generatedDocsTmpZip = File.createTempFile("radixdoc", ".zip");
                    try (FileOutputStream radixdocZOS = new FileOutputStream(generatedDocsTmpZip)) {

                        // decorParameters
                        Properties decorParameters = new Properties();
                        if (rootDocInfo.getDocItem() instanceof AdsXmlSchemeDefDocItem) {
                            AdsXmlSchemeDefDocItem xmlItem = (AdsXmlSchemeDefDocItem) rootDocInfo.getDocItem();
                            decorParameters.setProperty(EDecorationProperties.TARGET_NAMESPACE, xmlItem.getTargetNamespace());
                        }

                        if (!Utils.emptyOrNull(rootDocInfo.getLayerUri())) {
                            decorParameters.setProperty(EDecorationProperties.LAYER_URI, rootDocInfo.getLayerUri());
                        }

                        String layerVersionFull = String.valueOf(rootDocInfo.getLayerVersion());

                        if (!Utils.emptyOrNull(layerVersionFull)) {
                            decorParameters.setProperty(EDecorationProperties.LAYER_VERSION_FULL, layerVersionFull);
                            decorParameters.setProperty(EDecorationProperties.LAYER_VERSION_BASE, GeneratorUtils.getBaseVersion(layerVersionFull));
                        }

                        decorParameters.setProperty(EDecorationProperties.DOC_LANGUAGE, lang.getValue());

                        Date currentDate = new Date();

                        decorParameters.setProperty(EDecorationProperties.GENERATION_DATE, (new SimpleDateFormat("dd.MM.yyyy").format(currentDate)));
                        decorParameters.setProperty(EDecorationProperties.GENERATION_TIME, (new SimpleDateFormat("HH:mm:ss").format(currentDate)));

                        // main
                        try (ZipInputStream decorStream = context.getDecorationsStream(rootDocInfo.getLayerUri())) {
                            ZipInputStream preparedDecor = prepareDecorations(decorStream, context, decorParameters);

                            String layerVersion = decorParameters.getProperty(EDecorationProperties.LAYER_VERSION_FULL, "-");
                            if (layerVersion.isEmpty()) {
                                layerVersion = "-";
                            }

                            String[] fileName = new String[1];
                            if (!GeneratorUtils.isDefinitionHasTitle(context, definitions.get(0))) {
                                fileName[0] = GeneratorUtils.getDefaultMapName(context, definitions.get(0), layerVersion);
                            }

                            DitaFilesGenerator ditaGenerator = new DitaFilesGenerator(definitions, context);
                            ditaZipFile = ditaGenerator.generate(lang, EDitaGenerationMode.TOPICS_CLUSTER, fileName, layerVersion);

                            if (ditaZipFile == null || "".equals(fileName[0])) {
                                continue;
                            }

                            if (context.getDocLogger() != null) {
                                context.getDocLogger().put(EEventSeverity.DEBUG, "Generating file '" + fileName[0].replace(".ditamap", ".pdf") + "'");
                            }

                            if (preparedDecor != null) {
                                PackedZipExecutor.unpackAndExecute(
                                        ditaZipFile,
                                        radixdocZOS,
                                        preparedDecor,
                                        "-input", fileName[0], "-format", task.getOutputFormat().getValue()
                                );
                            } else {
                                PackedZipExecutor.unpackAndExecute(
                                        ditaZipFile,
                                        radixdocZOS,
                                        "-input", fileName[0], "-format", task.getOutputFormat().getValue()
                                );
                            }
                        } catch (Throwable t) {
                            throw new RadixdocGenerationException("Radixdoc generation failed!", t);
                        }
                    } catch (Exception ex) {
                        throw new RadixdocGenerationException("Radixdoc generation failed!", ex);
                    }

                    try (ZipInputStream radixDocZIS = new ZipInputStream(new TmpFileInputStreamWrapper(generatedDocsTmpZip))) {
                        ZipEntry entry;
                        while ((entry = radixDocZIS.getNextEntry()) != null) {
                            File exportDocItemFile = new File(outputDir, entry.getName());
                            if (!exportDocItemFile.getParentFile().exists()) {
                                exportDocItemFile.getParentFile().mkdirs();
                            }
                            try (FileOutputStream tmpFos = new FileOutputStream(exportDocItemFile)) {
                                FileUtils.copyStream(radixDocZIS, tmpFos);
                            }
                        }
                    } catch (Throwable ex) {
                        Logger.getLogger(HtmlRadixdocGenerator.class.getName()).log(Level.SEVERE, null, ex);
                        throw new RadixdocGenerationException("Radixdoc generation failed!", ex);
                    }
                } catch (Throwable ex) {
                    Logger.getLogger(HtmlRadixdocGenerator.class.getName()).log(Level.SEVERE, null, ex);
                    throw new RadixdocGenerationException("Radixdoc generation failed!", ex);
                } finally {
                    if (ditaZipFile != null) {
                        try {
                            ditaZipFile.close();
                        } catch (IOException ex) {
                            Logger.getLogger(HtmlRadixdocGenerator.class.getName()).log(Level.SEVERE, null, ex);
                            throw new RadixdocGenerationException("Radixdoc generation failed!", ex);
                        }
                    }
                }

                if (cancellable.wasCancelled()) {
                    return;
                }
            }

            if (cancellable.wasCancelled()) {
                return;
            }
        }
    }

    private static ZipInputStream prepareDecorations(final ZipInputStream decorations, final RadixdocGenerationContext context, Properties decorParameters) throws RadixdocGenerationException {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ZipOutputStream zos = new ZipOutputStream(baos);

            if (decorations == null) {
//                zos.putNextEntry(new ZipEntry(PackedZipExecutor.DECOR_PROPS) {
//                    {
//                        setMethod(DEFLATED);
//                    }
//                });
//
//                decorParameters.store(zos, null);
                return null;
            } else {
                ZipEntry entry = decorations.getNextEntry();
                while (entry != null) {
                    zos.putNextEntry(new ZipEntry(entry.getName()) {
                        {
                            setMethod(DEFLATED);
                        }
                    });

                    if (PackedZipExecutor.DECOR_PROPS.equals(entry.getName())) {
                        Properties styleProps = new Properties();
                        styleProps.load(decorations);

                        for (String propKey : styleProps.stringPropertyNames()) {
                            String bundlePrefix = EDefinitionIdPrefix.ADS_LOCALIZING_BUNDLE.getValue();
                            String stringPrefix = EDefinitionIdPrefix.ADS_LOCALIZED_STRING.getValue();

                            String propValue = styleProps.getProperty(propKey);

                            if (propValue.matches(".* " + bundlePrefix + "\\w*-" + stringPrefix + "\\w*")) {
                                String modulePath = styleProps.getProperty(propKey).split(" ")[0];
                                String stringPath = styleProps.getProperty(propKey).split(" ")[1];

                                String[] parts = stringPath.split("-");

                                String bundleId = parts[0];
                                String stringId = parts[1];
                                EIsoLanguage lang = EIsoLanguage.getForValue(decorParameters.getProperty(EDecorationProperties.DOC_LANGUAGE));

                                propValue = GeneratorUtils.getLocalizedStringValueByModulePath(
                                        context,
                                        modulePath,
                                        Id.Factory.loadFrom(bundleId),
                                        Id.Factory.loadFrom(stringId),
                                        lang
                                );
                            }

                            styleProps.setProperty(propKey, propValue);
                        }

                        String fullVersion = styleProps.getProperty(EDecorationProperties.LAYER_VERSION_FULL);
                        String baseVersion = styleProps.getProperty(EDecorationProperties.LAYER_VERSION_BASE);

                        if (!Utils.emptyOrNull(fullVersion)) {
                            if (Utils.emptyOrNull(baseVersion)) {
                                styleProps.setProperty(EDecorationProperties.LAYER_VERSION_BASE, GeneratorUtils.getBaseVersion(fullVersion));
                            }
                        } else {
                            if (!Utils.emptyOrNull(baseVersion)) {
                                styleProps.setProperty(EDecorationProperties.LAYER_VERSION_FULL, baseVersion);
                            }
                        }

                        styleProps.putAll(decorParameters);
                        decorParameters.putAll(styleProps);

                        styleProps.store(zos, null);
                    } else {
                        FileUtils.copyStream(decorations, zos);
                    }
                    entry = decorations.getNextEntry();
                }
            }
            zos.flush();
            zos.close();

            return new ZipInputStream(new ByteArrayInputStream(baos.toByteArray()));
        } catch (IOException ex) {
            throw new RadixdocGenerationException("Radixdoc generation failed!", ex);
        }
    }

}
