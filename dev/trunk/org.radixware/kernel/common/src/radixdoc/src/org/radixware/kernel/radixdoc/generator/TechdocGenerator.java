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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.radixware.kernel.common.components.ICancellable;
import org.radixware.kernel.common.defs.ads.doc.AdsDocDef;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.exceptions.RadixdocGenerationException;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.radixdoc.ditabridge.PackedZipExecutor;
import org.radixware.kernel.radixdoc.ditagenerator.TechDocDitaFilesGenerator;
import org.radixware.kernel.radixdoc.html.HtmlRadixdocGenerator;
import org.radixware.kernel.radixdoc.html.TmpFileInputStreamWrapper;

public class TechdocGenerator extends DocumentationGenerator {

    public static void generate(RadixdocGenerationTask task, RadixdocGenerationContext context) throws RadixdocGenerationException {
        final ICancellable cancellable = context.getCancellable() == null ? GeneratorUtils.getMockCancellable() : context.getCancellable();

        for (EIsoLanguage lang : task.getTargetLangs()) {
            List<Id> definitions = task.getSourceDefIdClusters().get(0);
            InputStream ditaZipFile = null;
            try {
                File outputDir = getOutputDir(task, lang);

                // copy resourceDir
                File resourceDir = context.copyResources(outputDir, lang);
                task.setResourcesDir(resourceDir);
                task.setTechDocDir(outputDir);

                File generatedDocsTmpZip = File.createTempFile("radixdoc", ".zip");
                try (FileOutputStream radixdocZOS = new FileOutputStream(generatedDocsTmpZip)) {
                    String[] fileName = new String[1];

                    TechDocDitaFilesGenerator ditaGenerator = new TechDocDitaFilesGenerator(definitions, context, task);
                    ditaZipFile = ditaGenerator.generate(lang, fileName);

                    if (ditaZipFile == null || "".equals(fileName[0])) {
                        continue;
                    }

                    // exec
                    String errorLog = PackedZipExecutor.unpackAndExecute(
                            ditaZipFile,
                            radixdocZOS,
                            "-input", fileName[0],
                            "-format", task.getOutputFormat().getValue()
                    );

                    // log
                    if (!Utils.emptyOrNull(errorLog) && context.getDocLogger() != null) {
                        String displyaErrorLog = "-------------------- During the generation of technical documentation, errors occurred --------------------"
                                + System.lineSeparator() + System.lineSeparator()
                                + errorLog
                                + System.lineSeparator() + System.lineSeparator()
                                + "--------------------------------------------------------------------------------------------------------------------------";
                        context.getDocLogger().put(EEventSeverity.WARNING, displyaErrorLog);
                    }

                } catch (Throwable t) {
                    throw new RadixdocGenerationException("Radixdoc generation failed!", t);
                }

                try (ZipInputStream radixDocZIS = new ZipInputStream(new TmpFileInputStreamWrapper(generatedDocsTmpZip))) {
                    ZipEntry entry;
                    while ((entry = radixDocZIS.getNextEntry()) != null) {
                        File exportDocItemFile = new File(outputDir, entry.getName());
                        try (FileOutputStream tmpFos = new FileOutputStream(exportDocItemFile)) {
                            FileUtils.copyStream(radixDocZIS, tmpFos);
                        }
                    }
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
    }

//    public static void preview(RadixdocGenerationTask task, RadixdocGenerationContext context) throws RadixdocGenerationException {
//        EIsoLanguage lang = null;// TODO: 
//        AdsDocDef docObject; // TODO: 
//        
//        InputStream ditaZipFile = null;
//        try {
//            File outputDir = getOutputDir(task, lang);
//
//            // copy resourceDir
//            File resourceDir = context.copyResources(outputDir, lang);
//            task.setResourcesDir(resourceDir);
//            task.setTechDocDir(outputDir);
//
//            File generatedDocsTmpZip = File.createTempFile("radixdoc", ".zip");
//            try (FileOutputStream radixdocZOS = new FileOutputStream(generatedDocsTmpZip)) {
//                String[] fileName = new String[1];
//
//                TechDocDitaFilesGenerator ditaGenerator = new TechDocDitaFilesGenerator(definitions, context, task);
//                ditaZipFile = ditaGenerator.generate(lang, fileName);
//
//                if (ditaZipFile == null || "".equals(fileName[0])) {
//                    continue;
//                }
//
//                PackedZipExecutor.unpackAndExecute(
//                        ditaZipFile,
//                        radixdocZOS,
//                        "-input", fileName[0], "-format", task.getOutputFormat().getValue()
//                );
//
//            } catch (Throwable t) {
//                throw new RadixdocGenerationException("Radixdoc generation failed!", t);
//            }
//
//            try (ZipInputStream radixDocZIS = new ZipInputStream(new TmpFileInputStreamWrapper(generatedDocsTmpZip))) {
//                ZipEntry entry;
//                while ((entry = radixDocZIS.getNextEntry()) != null) {
//                    File exportDocItemFile = new File(outputDir, entry.getName());
//                    try (FileOutputStream tmpFos = new FileOutputStream(exportDocItemFile)) {
//                        FileUtils.copyStream(radixDocZIS, tmpFos);
//                    }
//                }
//            }
//        } catch (Throwable ex) {
//            Logger.getLogger(HtmlRadixdocGenerator.class.getName()).log(Level.SEVERE, null, ex);
//            throw new RadixdocGenerationException("Radixdoc generation failed!", ex);
//        } finally {
//            if (ditaZipFile != null) {
//                try {
//                    ditaZipFile.close();
//                } catch (IOException ex) {
//                    Logger.getLogger(HtmlRadixdocGenerator.class.getName()).log(Level.SEVERE, null, ex);
//                    throw new RadixdocGenerationException("Radixdoc generation failed!", ex);
//                }
//            }
//        }
//
//    }
}
