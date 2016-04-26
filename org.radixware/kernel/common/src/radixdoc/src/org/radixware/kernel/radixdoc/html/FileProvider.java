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

package org.radixware.kernel.radixdoc.html;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.radixware.kernel.common.conventions.RadixdocConventions;
import org.radixware.kernel.common.enums.EIsoLanguage;
import static org.radixware.kernel.radixdoc.html.EFileSource.INTERNAL;
import static org.radixware.kernel.radixdoc.html.EFileSource.PROVIDER;


public abstract class FileProvider {

    public static abstract class Entry {

        private String id;
        private Entry parent;

        public Entry(String identifier) {
            this.id = identifier;
        }

        public final String getIdentifier() {
            return id;
        }

        final Entry getParentEntry() {
            return parent;
        }

        void setParent(Entry parent) {
            this.parent = parent;
        }

        boolean isProcessable() {
            return true;
        }

        abstract String getOutputPath();

        abstract String getInputPath();

        abstract String getRootPath();

        abstract String getSubPath();
    }

    public static final class LayerEntry extends Entry {

        private final int radixdocFormatVersion;
        private final List<ModuleEntry> modules;
        private final String rootPath;

        public LayerEntry(String rootPath, String uri, int radixdocFormatVersion, List<ModuleEntry> modules) {
            super(uri);
            this.radixdocFormatVersion = radixdocFormatVersion;
            this.modules = new ArrayList<>(modules.size());

            for (final ModuleEntry module : modules) {
                addModule(module);
            }
            this.rootPath = rootPath;
        }

        public LayerEntry(String rootPath, String uri, int radixdocFormatVersion) {
            super(uri);
            this.modules = new ArrayList<>();
            this.radixdocFormatVersion = radixdocFormatVersion;
            this.rootPath = rootPath;
        }

        @Override
        protected String getRootPath() {
            return rootPath;
        }

        @Override
        String getInputPath() {
            return null;
        }

        @Override
        boolean isProcessable() {
            return false;
        }

        @Override
        String getSubPath() {
            return getIdentifier();
        }

        public void addModule(ModuleEntry module) {
            modules.add(module);
            module.setParent(this);
        }

        public Collection<ModuleEntry> getModules() {
            return Collections.unmodifiableCollection(modules);
        }

        public int getRadixdocFormatVersion() {
            return radixdocFormatVersion;
        }

        @Override
        String getOutputPath() {
            return getSubPath();
        }
    }

    public static final class ModuleEntry extends Entry {

        private final String segmentName;

        public ModuleEntry(String segmentName, String moduleName) {
            super(moduleName);
            this.segmentName = segmentName;
        }

        @Override
        protected String getRootPath() {
            return getParentEntry().getRootPath() + "/" + getSubPath();
        }

        @Override
        String getInputPath() {
            return getRootPath() + "/" + RadixdocConventions.RADIXDOC_ZIP_FILE;
        }

        @Override
        String getSubPath() {
            return segmentName + "/" + getIdentifier();
        }

        @Override
        String getOutputPath() {
            return getParentEntry().getOutputPath() + "/" + getSubPath();
        }
    }

    protected abstract InputStream getInputStream(String fileName);

    protected abstract Collection<LayerEntry> getLayers();

    protected abstract File getOutput();

    final InputStream getInputStream(EFileSource source, String fileName) {
        switch (source) {
            case INTERNAL:
                return getInternalInputStream(fileName);
            case PROVIDER:
                return getInputStream(fileName);
        }

        return null;
    }

    final InputStream getRadixdocInputStream(ModuleEntry module) throws IOException {
        final ZipInputStream inputStream = new ZipInputStream(getInputStream(module.getInputPath()));
        final ZipEntry entry = inputStream.getNextEntry();

        if (entry.isDirectory() || !entry.getName().equals(RadixdocConventions.RADIXDOC_XML_FILE)) {
            return null;
        }

        return inputStream;
    }

    InputStream getInternalInputStream(String fileName) {
        return LocalFileProvider.class.getResourceAsStream(fileName);
    }

    InputStream getXsltSchemeStream() {
        return getInternalInputStream(Constants.RADIX_DOC_TRANSFORM_XLS_PATH);
    }

    InputStream getLocalizedBandle(String modulePath, String bundleId, EIsoLanguage language) {
        final String path;
        if (modulePath.contains("/dds/")) {
            path = modulePath + "/locale/" + language.getValue() + ".xml";
        } else {
            path = modulePath + "/locale/" + language.getValue() + "/" + bundleId + ".xml";
        }

        final InputStream inputStream = getInputStream(path);
        if (inputStream != null) {
            return inputStream;
        }

        return loadFromJarLocalizedBandle(modulePath, bundleId, language);
    }

    InputStream loadFromJarLocalizedBandle(String modulePath, String bundleId, EIsoLanguage language) {
        final String path = modulePath + "/bin/locale-" + language.getValue() + ".jar";

        InputStream inputStream = getInputStream(path);

        if (inputStream == null) {
            return null;
        }

        try {
            final JarInputStream stream = new JarInputStream(inputStream);
            JarEntry entry = stream.getNextJarEntry();

            while (entry != null) {
                if (entry.getName().length() <= 4) {
                    continue;
                }

                final int index = entry.getName().lastIndexOf("/");
                if (index + 1 >= entry.getName().length() - 4) {
                    continue;
                }
                final String id = entry.getName().substring(index + 1, entry.getName().length() - 4);

                if (Objects.equals(id, bundleId)) {
                    return stream;
                }

                entry = stream.getNextJarEntry();
            }
        } catch (IOException e) {
            return null;
        }

        return null;
    }
}
