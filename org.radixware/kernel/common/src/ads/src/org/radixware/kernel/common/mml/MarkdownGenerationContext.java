/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.radixware.kernel.common.mml;

import java.io.File;
import org.radixware.kernel.common.enums.EIsoLanguage;

/**
 *
 * @author dkurlyanov
 */
public class MarkdownGenerationContext {

    private EIsoLanguage language;
    private File radixDocDir;
    private File techDocDir;
    private File resourceDir;

    public MarkdownGenerationContext(EIsoLanguage language, File radixDocDir, File techDocDir, File resourceDir) {
        this.language = language;
        this.radixDocDir = radixDocDir;
        this.techDocDir = techDocDir;
        this.resourceDir = resourceDir;
    }

    public EIsoLanguage getLanguage() {
        return language;
    }

    public File getRadixDocDir() {
        return radixDocDir;
    }

    public File getResourceDir() {
        return resourceDir;
    }

    public void setLanguage(EIsoLanguage language) {
        this.language = language;
    }

    public void setRadixDocDir(File radixDocDir) {
        this.radixDocDir = radixDocDir;
    }

    public void setResourceDir(File resourceDir) {
        this.resourceDir = resourceDir;
    }

    public File getTechDocDir() {
        return techDocDir;
    }

    public void setTechDocDir(File techDocDir) {
        this.techDocDir = techDocDir;
    }
    
}
