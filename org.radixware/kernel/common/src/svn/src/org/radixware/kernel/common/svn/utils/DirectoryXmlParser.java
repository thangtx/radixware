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
package org.radixware.kernel.common.svn.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.stream.XMLStreamException;
import org.radixware.kernel.common.constants.FileNames;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.svn.RadixSvnException;
import org.radixware.kernel.common.svn.client.SvnPath;

/**
 *
 * @author npopov
 */
class DirectoryXmlParser {

    private final Utils.LayerInfo layer;
    private final boolean suppressWarnings;
    private static final String TAG_DIRECTORY = "Directory";
    private static final String TAG_FILE_GROUPS = "FileGroups";
    private static final String TAG_INCLUDES = "Includes";
    private static final String TAG_INCLUDE = "Include";
    private static final String TAG_FILE_GROUP = "FileGroup";
    private static final String TAG_FILE = "File";
    private static final String TAG_ENTRY = "Entry";
    private static final String ATTR_GROUP_TYPE = "GroupType";
    private static final String ATTR_NAME = "Name";
    private static final String ATTR_FILENAME = "FileName";
    private static final String CLASS_EXT = ".class";

    public DirectoryXmlParser(Utils.LayerInfo layer, boolean suppressWarnings) {
        this.layer = layer;
        this.suppressWarnings = suppressWarnings;
    }

    public Map<ERuntimeEnvironmentType, List<String>> getAllDirectoryXmlFilesByEnv(String directoryXmlPath) {
        Map<ERuntimeEnvironmentType, List<String>> result = new HashMap<>();
        List<String> filesToVisit = new ArrayList<>();
        filesToVisit.add(directoryXmlPath);

        while (!filesToVisit.isEmpty()) {
            final String basePath = filesToVisit.remove(0);
            try {
                XMLReader reader = new XMLReader(basePath, layer.getFile(buildPath(basePath)));
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
                                final ERuntimeEnvironmentType type = getEnvByXmlType(group_type);
                                if (type != null) {
                                    List<String> files = result.get(type);
                                    if (files == null) {
                                        files = new ArrayList<>();
                                        result.put(type, files);
                                    }
                                    files.add(basePath);
                                }
                                for (;;) {
                                    final String file_tag = reader.nextTag();
                                    if (file_tag == null) {
                                        break;
                                    }
                                    reader.skipTag();
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
                                String localPath = reader.getAttribute(ATTR_FILENAME);
                                if (localPath.endsWith(FileNames.DIRECTORY_XML)) {
                                    String trimmed = localPath.substring(0, localPath.lastIndexOf('/'));
                                    String path = SvnPath.append(basePath, trimmed);
                                    filesToVisit.add(path);
                                }
                            }
                            reader.skipTag();
                        }
                    } else {
                        reader.skipTag();
                    }
                }
            } catch (XMLStreamException | IOException | RadixSvnException ex) {
                layer.getVerifier().error(ex);
            }
        }
        return result;
    }

    private ERuntimeEnvironmentType getEnvByXmlType(String type) {
        switch (type) {
            case "KernelCommon":
                return ERuntimeEnvironmentType.COMMON;
            case "KernelServer":
                return ERuntimeEnvironmentType.SERVER;
            case "KernelExplorer":
                return ERuntimeEnvironmentType.EXPLORER;
            case "KernelWeb":
                return ERuntimeEnvironmentType.WEB;
            default:
                return null;
        }
    }

    private String buildPath(String localPath) {
        return SvnPath.append(localPath, FileNames.DIRECTORY_XML);
    }
    
    public Set<String> getDefinedClasses(String path) {
        try {
            return getDefinedClasses(layer.getFile(buildPath(path)));
        } catch (RadixSvnException ex) {
            layer.getVerifier().error(ex);
        }
        return Collections.emptySet();
    }

    public Set<String> getDefinedClasses(byte[] directoryXmlBytes) {
        final Set<String> definedFiles = new HashSet<>();
        final Map<String, String> classNameByFile = new HashMap<>();
        try {
            XMLReader reader = new XMLReader(FileNames.DIRECTORY_XML, directoryXmlBytes);
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
                            //final String group_type = reader.getAttribute(ATTR_GROUP_TYPE);
                            for (;;) {
                                final String file_tag = reader.nextTag();
                                if (file_tag == null) {
                                    break;
                                }
                                if (file_tag.equals(TAG_FILE)) {
                                    final String file_path = reader.getAttribute(ATTR_NAME);
                                    if (definedFiles.contains(file_path)) {
                                        layer.getVerifier().error("File " + file_path + " was redefined in directory.xml");
                                    }
                                    definedFiles.add(file_path);
                                    if (file_path.endsWith(".jar")) {
                                        for (;;) {
                                            final String entry_tag = reader.nextTag();
                                            if (entry_tag == null) {
                                                break;
                                            }
                                            if (entry_tag.equals(TAG_ENTRY)) {
                                                final String entry_name = reader.getAttribute(ATTR_NAME);
                                                if (entry_name.endsWith(".class")) {
                                                    final String className = entry_name.substring(0, entry_name.length() - CLASS_EXT.length()).replace('/', '.');
                                                    final String existingFile = classNameByFile.put(className, file_path);
                                                    if (!suppressWarnings && existingFile != null) {
                                                        layer.getVerifier().error("Class " + entry_name + " is ambigiously defined in files " + file_path + " and " + existingFile);
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
                } else {
                    reader.skipTag();
                }
            }
        } catch (XMLStreamException | IOException ex) {
            layer.getVerifier().error(ex);
        }
        return classNameByFile.keySet();
    }
}
