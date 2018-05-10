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
import java.util.ArrayList;
import java.util.List;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.radixdoc.ditagenerator.EDitaGenerationMode;

public class RadixdocGenerationTask {

    private final List<List<Id>> sourceDefIdClusters;
    private final List<EIsoLanguage> targetLangs;
    private final File targetDir;
    private final File radixDocDir;
    private File techDocDir;
    private File resourcesDir;
    private final ERadixdocOutputFormat outputFormat;
    private final EDitaGenerationMode generationMode;

    public RadixdocGenerationTask(List<List<Id>> sourceDefIdClusters, List<EIsoLanguage> targetLangs, File targetDir, ERadixdocOutputFormat outputFormat, EDitaGenerationMode generationMode, File radixDocDir) {
        this.sourceDefIdClusters = sourceDefIdClusters;
        this.targetLangs = targetLangs;
        this.targetDir = targetDir;
        this.outputFormat = outputFormat;
        this.generationMode = generationMode;
        this.radixDocDir = radixDocDir;
    }

    public RadixdocGenerationTask(List<List<Id>> sourceDefIdClusters, List<EIsoLanguage> targetLangs, File targetDir, ERadixdocOutputFormat outputFormat, EDitaGenerationMode generationMode) {
        this(sourceDefIdClusters, targetLangs, targetDir, outputFormat, generationMode, null);
    }
    

    public List<List<Id>> getSourceDefIdClusters() {
        List<List<Id>> result = new ArrayList<>();
        for (List<Id> defIdCluster : sourceDefIdClusters) {
            result.add(new ArrayList<>(defIdCluster));
        }

        return result;
    }

    public List<EIsoLanguage> getTargetLangs() {
        return new ArrayList<>(targetLangs);
    }

    public File getTargetDir() {
        return targetDir;
    }

    public ERadixdocOutputFormat getOutputFormat() {
        return outputFormat;
    }

    public EDitaGenerationMode getGenerationMode() {
        return generationMode;
    }

    public File getRadixDocDir() {
        return radixDocDir;
    }

    public void setResourcesDir(File resourcesDir) {
        this.resourcesDir = resourcesDir;
    }

    public File getResourcesDir() {
        return resourcesDir;
    }

    public File getTechDocDir() {
        return techDocDir;
    }

    public void setTechDocDir(File techDocDir) {
        this.techDocDir = techDocDir;
    }

}
