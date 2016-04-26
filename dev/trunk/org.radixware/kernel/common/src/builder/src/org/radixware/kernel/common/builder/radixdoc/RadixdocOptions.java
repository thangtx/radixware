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

package org.radixware.kernel.common.builder.radixdoc;

import java.util.List;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.enums.EIsoLanguage;


public final class RadixdocOptions {

    public static class Factory {

        private Factory() {
        }

        public static RadixdocOptions newInstance(List<Module> modules, List<EIsoLanguage> languages, String rootLocation, boolean withDependencies) {
            return new RadixdocOptions(modules, languages, rootLocation, withDependencies);
        }
    }

    private final List<Module> modules;
    private final List<EIsoLanguage> languages;
    private final String rootLocation;
    private final boolean withDependencies;

    protected RadixdocOptions(List<Module> modules, List<EIsoLanguage> languages, String rootLocation, boolean withDependencies) {
        this.modules = modules;
        this.languages = languages;
        this.rootLocation = rootLocation;
        this.withDependencies = withDependencies;
    }

    public List<EIsoLanguage> getLanguages() {
        return languages;
    }

    public List<Module> getModules() {
        return modules;
    }

    public String getRootLocation() {
        return rootLocation;
    }

    public boolean withDependencies() {
        return withDependencies;
    }
}
