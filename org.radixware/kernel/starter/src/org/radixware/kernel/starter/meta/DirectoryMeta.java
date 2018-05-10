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
package org.radixware.kernel.starter.meta;

import org.radixware.kernel.starter.utils.XMLReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.xml.stream.XMLStreamException;
import org.apache.commons.logging.LogFactory;
import org.radixware.kernel.starter.radixloader.RadixLoaderException;
import org.radixware.kernel.starter.utils.ClassPackage;
import org.radixware.kernel.starter.utils.HexConverter;

public class DirectoryMeta {

    public static final String DIRECTORY_XML_FILE = "directory.xml";
    private final LayerMeta layer;
    private final String directory;
    private final File file;
    private final Map<String, FileMeta> filesMap;
    private final Map<String, FileMeta> filesOverMap;
    private final Map<String, FileMeta> packagesOverMap;
    private final Map<String, FileMeta> resourcesOverMap;
    private final Map<String, FileMeta> classesMap;
    private final Collection<String> includes;
    private static final String TAG_DIRECTORY = "Directory";
    private static final String TAG_FILE_GROUPS = "FileGroups";
    private static final String TAG_INCLUDES = "Includes";
    private static final String TAG_INCLUDE = "Include";
    private static final String TAG_FILE_GROUP = "FileGroup";
    private static final String TAG_FILE = "File";
    private static final String TAG_ENTRY = "Entry";
    private static final String ATTR_GROUP_TYPE = "GroupType";
    private static final String ATTR_NAME = "Name";
    private static final String ATTR_DIGEST = "Digest";
    private static final String ATTR_FILENAME = "FileName";

    public DirectoryMeta(
            final File root,
            final LayerMeta layer,
            final String path,
            final byte[] data) throws IOException, XMLStreamException {
        try {
            this.layer = layer;
            file = new File(root, layer.getUri() + "/" + path);
            if (new File(path).getParent() != null) {
                directory = layer.getUri() + "/" + new File(path).getParent().replace(File.separatorChar, '/');
            } else {
                directory = layer.getUri();
            }
            filesMap = new HashMap<>();
            filesOverMap = new HashMap<>();
            packagesOverMap = new HashMap<>();
            resourcesOverMap = new HashMap<>();
            classesMap = new HashMap<>();
            includes = new ArrayList<>();
            read(new XMLReader(file.toString(), data));
        } catch (RadixLoaderException | XMLStreamException ex) {
            LogFactory.getLog(DirectoryMeta.class).error("Unable to load meta from '" + (layer.getUri() + "/" + path) + "'", ex);
            throw ex;
        }
    }

    public File getFile() {
        return file;
    }

    public String getDirectory() {
        return directory;
    }

    public Map<String, FileMeta> getPackagesOverMap() {
        return Collections.unmodifiableMap(packagesOverMap);
    }

    public Map<String, FileMeta> getFilesMap() {
        return Collections.unmodifiableMap(filesMap);
    }

    public Map<String, FileMeta> getFilesOverMap() {
        return Collections.unmodifiableMap(filesOverMap);
    }

    public Map<String, FileMeta> getResourcesOverMap() {
        return Collections.unmodifiableMap(resourcesOverMap);
    }

    /**
     * Transfer all data from classes map to provided destination. Designed to
     * be called only once so the data can be analyzed at the time of loading
     * freeing memory afterwards.
     *
     * @param classesMap
     */
    public void drainClassesMap(final Map<String, FileMeta> classesMap) {
        classesMap.putAll(this.classesMap);
        this.classesMap.clear();
    }

    public Collection<String> getIncludes() {
        return Collections.unmodifiableCollection(includes);
    }

    private void read(final XMLReader reader) throws XMLStreamException, RadixLoaderException {
        reader.getTag(TAG_DIRECTORY);
        for (;;) {
            final String tag = reader.nextTag();
            if (tag == null) {
                break;
            }
            if (tag.equals(TAG_FILE_GROUPS)) {
                for (;;) {
                    final String group_tag = reader.nextTag();
                    if (group_tag == null) {
                        break;
                    }
                    if (group_tag.equals(TAG_FILE_GROUP)) {
                        final String group_type = reader.getAttribute(ATTR_GROUP_TYPE);
                        for (;;) {
                            final String file_tag = reader.nextTag();
                            if (file_tag == null) {
                                break;
                            }
                            if (file_tag.equals(TAG_FILE)) {
                                final String file_path = directory + "/" + reader.getAttribute(ATTR_NAME);
                                final byte[] file_digest = HexConverter.fromHex(reader.getAttribute(ATTR_DIGEST));
                                if (filesMap.containsKey(file_path)) {
                                    throw new RadixLoaderException("File " + file_path + " was redefined in directory.xml");
                                }
                                final FileMeta file_meta = new FileMeta(file_path, layer, file_digest, group_type);
                                filesMap.put(file_path, file_meta);
                                filesOverMap.put(file_path.substring(layer.getUri().length() + 1), file_meta);
                                if (file_path.endsWith(".jar")) {
                                    for (;;) {
                                        final String entry_tag = reader.nextTag();
                                        if (entry_tag == null) {
                                            break;
                                        }
                                        if (entry_tag.equals(TAG_ENTRY)) {
                                            final String entry_name = reader.getAttribute(ATTR_NAME);
                                            if (entry_name.endsWith(".class")) {
                                                final String package_name = ClassPackage.getPackage(entry_name);
                                                final FileMeta file = packagesOverMap.get(package_name);
                                                if (file != null) {
                                                    if (!file.getArchive().equals(file_path)) {
                                                        file.addNext(new FileMeta(package_name, layer, file_path, file_digest, group_type));
                                                    }
                                                } else {
                                                    packagesOverMap.put(package_name, new FileMeta(package_name, layer, file_path, file_digest, group_type));
                                                }
                                                final FileMeta existingMeta = classesMap.put(entry_name, new FileMeta(entry_name, layer, file_path, null, group_type));
                                                if (existingMeta != null) {
                                                    if (file_path.startsWith(layer.getUri() + "/ads")) {
                                                        throw new RadixLoaderException("Class " + entry_name + " is ambigiously defined in files " + file_path + " and " + existingMeta.getArchive());
                                                    }
                                                }
                                            } else {
                                                final FileMeta new_meta = new FileMeta(entry_name, layer, file_path, file_digest, group_type);
                                                final FileMeta meta = resourcesOverMap.get(entry_name);
                                                if (meta == null) {
                                                    resourcesOverMap.put(entry_name, new_meta);
                                                } else {
                                                    meta.addNext(new_meta);
                                                }
                                            }
                                        }
                                        reader.skipTag();
                                    }
                                } else {
                                    reader.skipTag();
                                }
                            } else {
                                reader.skipTag();
                            }
                        }
                    } else {
                        reader.skipTag();
                    }
                }
            } else if (tag.equals(TAG_INCLUDES)) {
                for (;;) {
                    final String include_tag = reader.nextTag();
                    if (include_tag == null) {
                        break;
                    }
                    if (include_tag.equals(TAG_INCLUDE)) {
                        final String includeName = reader.getAttribute(ATTR_FILENAME);
                        if (includeName != null && !includeName.endsWith("directory-dist.xml")) {//RADIX-12768: Runtime meta should not know about dist starter
                            includes.add(includeName);
                        }
                    }
                    reader.skipTag();
                }
            } else {
                reader.skipTag();
            }
        }
    }
}
