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
import java.util.prefs.Preferences;


final class RadixdocOptions {
    private static final String optionName = "RadixdocGeneratorOptions";
    private static final String optionOutputLocation = "outputLocation";
    private static final String optionIncludeDependencies = "includeDependencies";
    private static final String optionCompressToZip = "compressToZip";
    private static final String optionOpenInBrowser = "openInBrowser";
    
    
    private String outputLocation;
    private boolean includeDependencies;
    private boolean compressToZip;
    private boolean openInBrowser;
    
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
        compressToZip = prefs.getBoolean(optionCompressToZip, false);
        openInBrowser = prefs.getBoolean(optionOpenInBrowser, false);
    }
    
    public void save() {
        final Preferences prefs = Preferences.userRoot().node(optionName);
        
        prefs.put(optionOutputLocation, outputLocation);
        prefs.putBoolean(optionIncludeDependencies, includeDependencies);
        prefs.putBoolean(optionCompressToZip, compressToZip);
        prefs.putBoolean(optionOpenInBrowser, openInBrowser);
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
}
