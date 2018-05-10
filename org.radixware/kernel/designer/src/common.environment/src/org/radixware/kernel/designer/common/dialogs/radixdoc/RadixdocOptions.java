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


package org.radixware.kernel.designer.common.dialogs.radixdoc;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.prefs.Preferences;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;


final class RadixdocOptions {
    private static final String optionName = "RadixdocGeneratorOptions";
    private static final String optionOutputLocation = "outputLocation";
    private static final String optionIncludeDependencies = "includeDependencies";
    private static final String optionCompressToZip = "compressToZip";
    private static final String optionOpenInBrowser = "openInBrowser";
    private static final String optionExportHTMLDoc = "exportHTMLDoc";
    private static final String optionExportTechDocHtml = "exportTechDocHtml";
    private static final String optionExportTechDocPdf = "exportTechDocPdf";
    private static final String optionExportTechDocDoc = "exportTechDocDoc";
    private static final String optionSelectedLanguages = "selectedLanguages";
    
    
    private String outputLocation;    
    private boolean includeDependencies;
    private boolean compressToZip;
    private boolean openInBrowser;
    private boolean exportHTMLDoc;
    private boolean exportTechDocHtml;
    private boolean exportTechDocPdf;
    private boolean exportTechDocDoc;
    private List<EIsoLanguage> selectedLanguages;
    
    public static class Factory {
        private Factory() {}
        
        public static RadixdocOptions getInstance() {
            return new RadixdocOptions();
        }
    }

    private RadixdocOptions() {
        restore();
    }
    
    private void restore() {
        final Preferences prefs = Preferences.userRoot().node(optionName);
        
        outputLocation = prefs.get(optionOutputLocation, System.getProperty("user.home")+ File.separator + "radixdoc");
        includeDependencies = prefs.getBoolean(optionIncludeDependencies, true);
        exportHTMLDoc = prefs.getBoolean(optionExportHTMLDoc, false);
        exportTechDocHtml = prefs.getBoolean(optionExportTechDocHtml, false);
        exportTechDocPdf = prefs.getBoolean(optionExportTechDocPdf, false);
        exportTechDocDoc = prefs.getBoolean(optionExportTechDocDoc, false);
        compressToZip = prefs.getBoolean(optionCompressToZip, false);
        openInBrowser = prefs.getBoolean(optionOpenInBrowser, false);
        selectedLanguages = stringToList(prefs.get(optionSelectedLanguages, ""));
    }
    
    public void save() {
        final Preferences prefs = Preferences.userRoot().node(optionName);
        
        prefs.put(optionOutputLocation, outputLocation);
        prefs.putBoolean(optionIncludeDependencies, includeDependencies);
        prefs.putBoolean(optionExportHTMLDoc, exportHTMLDoc);
        prefs.putBoolean(optionExportTechDocHtml, exportTechDocHtml);
        prefs.putBoolean(optionExportTechDocPdf, exportTechDocPdf);
        prefs.putBoolean(optionExportTechDocDoc, exportTechDocDoc);
        prefs.putBoolean(optionCompressToZip, compressToZip);
        prefs.putBoolean(optionOpenInBrowser, openInBrowser);
        prefs.put(optionSelectedLanguages, listToString(selectedLanguages));
    }
    
    private List<EIsoLanguage> stringToList(String listAsStr) {        
        List<EIsoLanguage> result = new ArrayList<>();
        for (String lang : listAsStr.split(" ")) {
            try {
                result.add(EIsoLanguage.getForValue(lang));
            } catch (NoConstItemWithSuchValueError e) {                
            }
        }
        
        return result;
    }
    
    private String listToString(List<EIsoLanguage> list) {
        StringBuilder result = new StringBuilder();
        for (EIsoLanguage lang : list) {
            if (result.length() > 0) {
                result.append(" ");
            }
            result.append(lang.getValue());
        }
        
        return result.toString();
    }
    
    public String getOutputLocation() {
        return outputLocation;
    }

    public void setOutputLocation(String outputLocation) {
        this.outputLocation = outputLocation;
    }

    public boolean isIncludeDependencies() {
        return includeDependencies;
    }

    public void setIncludeDependencies(boolean includeDependencies) {
        this.includeDependencies = includeDependencies;
    }

    public boolean isCompressToZip() {
        return compressToZip;
    }

    public void setCompressToZip(boolean compressToZip) {
        this.compressToZip = compressToZip;
    }

    public boolean isOpenInBrowser() {
        return openInBrowser;
    }

    public void setOpenInBrowser(boolean openInBrowser) {
        this.openInBrowser = openInBrowser;
    }

    public boolean isExportHTMLDoc() {
        return exportHTMLDoc;
    }

    public void setExportHTMLDoc(boolean exportHTMLDoc) {
        this.exportHTMLDoc = exportHTMLDoc;
    }   
    
    public List<EIsoLanguage> getSelectedLanguages() {
        return selectedLanguages;
    }

    public void setSelectedLanguages(List<EIsoLanguage> selectedLanguages) {
        this.selectedLanguages = selectedLanguages;
    }

    public boolean isExportTechDocHtml() {
        return exportTechDocHtml;
    }

    public boolean isExportTechDocPdf() {
        return exportTechDocPdf;
    }

    public boolean isExportTechDocDoc() {
        return exportTechDocDoc;
    }

    public void setExportTechDocHtml(boolean exportTechDocHtml) {
        this.exportTechDocHtml = exportTechDocHtml;
    }

    public void setExportTechDocPdf(boolean exportTechDocPdf) {
        this.exportTechDocPdf = exportTechDocPdf;
    }

    public void setExportTechDocDoc(boolean exportTechDocDoc) {
        this.exportTechDocDoc = exportTechDocDoc;
    }
}
