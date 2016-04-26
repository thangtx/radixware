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

package org.radixware.kernel.starter.config;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class ConfigFileAccessor {

    private final List<ConfigEntry> entries = new ArrayList<>();
    private final String fileName;
    private final byte[] data;
    private final String sectionName;

    private ConfigFileAccessor(final String fileName, final byte[] data, final String sectionName) {
        this.fileName = fileName;
        this.sectionName = sectionName;
        this.data = data;
    }

    public static ConfigFileAccessor get(final String fileName, final String sectionName) throws ConfigFileParseException {
        return doGet(fileName, null, sectionName);
    }

    public static ConfigFileAccessor get(final byte[] data, final String sectionName) throws ConfigFileParseException {
        return doGet(null, data, sectionName);
    }

    private static ConfigFileAccessor doGet(final String fileName, final byte[] data, final String sectionName) throws ConfigFileParseException {
        final ConfigFileAccessor accessor = new ConfigFileAccessor(fileName, data, sectionName);

        if (accessor.doReread()) {
            return accessor;
        }

        return null;
    }

    private boolean doReread() throws ConfigFileParseException {
        final ConfigFileParser parser = new ConfigFileParser();
        final ConfigFileParseResult parseResult;
        try {
            if (data == null) {
            try (FileInputStream fis = new FileInputStream(fileName)) {
                parseResult = parser.parse(fis);
            }
            } else {
                parseResult = parser.parse(new ByteArrayInputStream(data));
            }
        } catch (IOException ex) {
            throw new ConfigFileParseException(ex);
        }
        final List<ConfigFileParsedSection> sections = parseResult.getParsedSections();

        entries.clear();

        for (ConfigFileParsedSection section : sections) {
            if ((section.getName() == null && sections.size() == 1) || (sectionName == null && section.getName() == null) || sectionName.equals(section.getName())) {
                final boolean backwardCompatibilityMode = section.getName() == null && sections.size() == 1;
                for (ConfigFileParsedEntry parsedEntry : section.getParsedEntries()) {
                    if (backwardCompatibilityMode) {
                        entries.add(createBackwardsCompatibleEntry(parsedEntry));
                    } else {
                        entries.add(new ConfigEntry(parsedEntry.getKey(), parsedEntry.getValue()));
                    }
                }
                return true;
            }
        }
        return false;
    }

    private ConfigEntry createBackwardsCompatibleEntry(final ConfigFileParsedEntry parsedEntry) {
        //in previous versions settings were stored using Java Proprties API. For some reason,
        //it was replacing : with \: after rewriting the file. After that Java Properties API
        //still read the properly, but our parser doesn't.
        return new ConfigEntry(parsedEntry.getKey(), parsedEntry.getValue() != null ? parsedEntry.getValue().replace("\\:", ":") : null);
    }

    public void reread() throws ConfigFileParseException {
        if (!doReread()) {
            throw new ConfigFileParseException("Section '" + sectionName + "' not found");
        }
    }

    public List<ConfigEntry> getEntries() {
        return Collections.unmodifiableList(entries);
    }

    public void update(final Collection<String> keysToRemove, final Collection<ConfigEntry> entriesToUpdate) throws ConfigFileParseException {
        if (data != null) {
            throw new ConfigFileParseException("Config file accessor is built upon readonly byte array");
        }
        try {
            byte[] fileData = data == null ? Files.readAllBytes(new File(fileName).toPath()) : data;
            final ConfigFileParser.ConfigFileCursor cursor = new ConfigFileParser.ConfigFileCursor(new ByteArrayInputStream(fileData));
            while (cursor.nextSection()) {
                if (sectionName.equals(cursor.getSectionName()) || cursor.getSectionName() == null && !cursor.hasNextSection()) {//required section found or compatibility mode
                    final Set<String> updatedEntries = new HashSet<>();
                    final Map<String, ConfigEntry> keyToUpdatedEntry = new HashMap<>();
                    if (entriesToUpdate != null) {
                        for (ConfigEntry entryToUpdate : entriesToUpdate) {
                            keyToUpdatedEntry.put(entryToUpdate.getKey(), entryToUpdate);
                        }
                    }
                    while (cursor.nextEntry()) {
                        final String entryKey = cursor.getEntry().getKey();
                        if (keysToRemove != null && keysToRemove.contains(entryKey)) {
                            cursor.removeEntry();
                        } else if (keyToUpdatedEntry.containsKey(entryKey)) {
                            cursor.replaceEntry(keyToUpdatedEntry.get(entryKey));
                            updatedEntries.add(entryKey);
                        }
                    }
                    if (entriesToUpdate != null) {
                        for (ConfigEntry entry : entriesToUpdate) {
                            if (!updatedEntries.contains(entry.getKey())) {
                                cursor.insertAfter(entry);
                            }
                        }
                    }
                    Files.write(new File(fileName).toPath(), new String(cursor.flush()).getBytes());
                    reread();
                    return;
                }
            }
        } catch (IOException ex) {
            throw new ConfigFileParseException(ex);
        }
        throw new ConfigFileParseException("Config file doesn't contain section " + sectionName);
    }
}
