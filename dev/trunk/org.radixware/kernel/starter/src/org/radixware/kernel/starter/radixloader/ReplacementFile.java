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
package org.radixware.kernel.starter.radixloader;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import org.radixware.kernel.common.svn.utils.ReplacementUtils;
import org.radixware.kernel.starter.config.ConfigEntry;
import org.radixware.kernel.starter.config.ConfigFileAccessor;


public class ReplacementFile {
    
    private static final String CREATION_DATE_FORMAT = "YYYY-MM-dd-hh:mm";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(CREATION_DATE_FORMAT);
    static {
        DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
    }
    
    private final Map<String, ReplacementEntry> entries = new HashMap<>();
    private final String path;
    private final String cfgFilePath;
    private final Calendar creationDate;
    private final int[] startVersion;
    private final int[] endVersion;
    private final String creationDateStr;
    private final String versions;
    private final String comment;
    
    
    private boolean thereIsKernelReplacements;
    private boolean thereIsAppReplacements;

    private ReplacementFile(String path, String cfgFilePath, Calendar creationDate, int[] startVersion, int[] endVersion, String creationDateStr, String versions, String comment) {
        this.path = path;
        this.cfgFilePath = cfgFilePath;
        this.creationDate = creationDate;
        this.startVersion = startVersion;
        this.endVersion = endVersion;
        this.creationDateStr = creationDateStr;
        this.versions = versions;
        this.comment = comment;
    }
    
    public static ReplacementFile newInstance(String filePath, String cfgFilePath) {
        String versions = null;
        String creationDateStr = null;
        String comment = null;
        if (cfgFilePath != null && !cfgFilePath.isEmpty()) {
            try {
                final ConfigFileAccessor accessor = ConfigFileAccessor.get(cfgFilePath, "");
                for (ConfigEntry entry : accessor.getEntries()) {
                    switch (entry.getKey().toLowerCase()) {
                        case "compatibleversions":
                            versions = entry.getValue();
                            break;
                        case "creationdate":
                            creationDateStr = entry.getValue();
                            break;
                        case "comment":
                            comment = entry.getValue();
                            break;
                        default:
                            RadixLoader.traceError("Replacement file " + cfgFilePath + " contains unknown parameter: " + entry.getKey() + "=" + entry.getValue());
                    }
                }
            } catch (Exception ex) {
                RadixLoader.traceError("Unable to parse replacement config from file " + filePath, ex);
                return null;
            }
        }
        
        final Calendar creationDate = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        if (creationDateStr != null && !creationDateStr.isEmpty()) {
            try {
                creationDate.setTimeInMillis(DATE_FORMAT.parse(creationDateStr).getTime());
            } catch (Exception ex) {
                RadixLoader.traceError("Replacement file " + cfgFilePath + " contains wrong creation time format: " + creationDateStr + ", expected: " + CREATION_DATE_FORMAT, ex);
                return null;
            }
        } else {
            final File file = new File(filePath);
            if (file.exists()) {
                creationDate.setTimeInMillis(file.lastModified());
            } else {
                RadixLoader.traceError("File '" + filePath + "' does not exists");
                return null;
            }
        }

        final int[] startVersion, endVersion;
        if (versions != null && !versions.isEmpty()) {
            final String[] vers = versions.split("-");
            try {
                startVersion = ReplacementUtils.parseVersion(vers[0], filePath);
                endVersion = vers.length == 2 ? ReplacementUtils.parseVersion(vers[1], filePath) : null;
            } catch (ParseException ex) {
                RadixLoader.traceError("Unable to parse compatible version info from replacement file " + filePath + "\nDetails: " + ex.getMessage());
                return null;
            }
        } else {
            startVersion = null;
            endVersion = null;
        }
        
        return new ReplacementFile(filePath, cfgFilePath, creationDate, startVersion, endVersion, creationDateStr, versions, comment);
    }
    
    private boolean compatibleFor(String version) {
        if (version == null) {
            return false;
        }
        if (isUnversioned() || "null".equals(version)) {
            return true;
        } 
        
        final int[] other;
        try {
            other = ReplacementUtils.parseVersion(version, null);
        } catch (ParseException ex) {
            RadixLoader.traceError("Error on parse version string: " + version, ex);
            return false;
        }
        
        if (endVersion == null) {
            return compareVersions(startVersion, other) == 0;
        } else {
            return compareVersions(startVersion, other) <= 0 && compareVersions(endVersion, other) >= 0;
        }
    }
    
    public static int compareVersions(int[] thisVer, int[] otherVer) {
        if (thisVer.length != ReplacementUtils.VERSION_COMPONENTS_CNT || otherVer.length != ReplacementUtils.VERSION_COMPONENTS_CNT) {
            throw new IllegalArgumentException("Wrong version string: " + (thisVer.length != ReplacementUtils.VERSION_COMPONENTS_CNT ? Arrays.toString(thisVer) : Arrays.toString(otherVer)));
        }
        for (int i = 0; i < ReplacementUtils.VERSION_COMPONENTS_CNT; i++) {
            final int diff = thisVer[i] - otherVer[i];
            if (noneOfX(thisVer[i], otherVer[i]) && diff != 0) {
                return diff;
            }
        }
        return 0;
    }
    
    private static boolean noneOfX(final int thisVersion, final int otherVersion) {
        return thisVersion != ReplacementUtils.X_VERSION_COMPONENT && otherVersion != ReplacementUtils.X_VERSION_COMPONENT;
    }

    public void addEntry(ReplacementEntry entry) {
        final ReplacementEntry prevEntry = entries.put(entry.getRemote(), entry);
        if (prevEntry != null) {
            RadixLoader.traceError("Replacement entry " + entry.getRemote() + " duplicated in file " + path);
        }
        if (entry.getRemote().contains("kernel")) {//evil
            thereIsKernelReplacements = true;
        } else {
            thereIsAppReplacements = true;
        }
    }
    
    public Collection<ReplacementEntry> getEntries() {
        return Collections.unmodifiableCollection(entries.values());
    }
    
    public int getSize() {
        return entries.size();
    }

    public ReplacementEntry getByRemotePath(String remote) {
        return entries.get(remote);
    }
    
    public String getHumanReadableLocal(String remote) {
        final ReplacementEntry e = getByRemotePath(remote);
        if (e != null) {
            if (fromLocalFileList()) {
                return e.getLocal();
            }
            return (path + "/" + e.getRemote()).replace(File.separator, "/");
        }
        return null;
    }
    
    public String getFilePath() {
        return path;
    }

    public String getVersions() {
        return versions;
    }
    
    public String getComment() {
        return comment;
    }
    
    private String getVersionNumber(final String fullVersionStr) {
        final String[] layerNameAndNumber = fullVersionStr.split("=");
        return layerNameAndNumber.length == 2 ? layerNameAndNumber[1] : null;
    }

    public boolean compatibleFor(final String layerKernelVersionsStr, final String layerAppVersionsStr) {
        if (thereIsKernelReplacements && !thereIsAppReplacements) {
            return compatibleFor(getVersionNumber(layerKernelVersionsStr));
        } else if (thereIsAppReplacements && !thereIsKernelReplacements) {
            return compatibleFor(getVersionNumber(layerAppVersionsStr));
        } else {
            return compatibleFor(getVersionNumber(layerKernelVersionsStr)) 
                    && compatibleFor(getVersionNumber(layerAppVersionsStr));
        }
    }
    
    public boolean isNewer(final ReplacementFile otherFile) {
        return creationDate.after(otherFile.creationDate);
    }
    
    public boolean fromLocalFileList() {
        return !path.toLowerCase().endsWith(".zip");
    }

    public boolean isUnversioned() {
        return startVersion == null;
    }
    
    public Set<String> checkEntriesConflict(ReplacementFile other) {
         final Set<String> result = new HashSet<>(entries.keySet());
         result.retainAll(other.entries.keySet());
         return result;
    }
    
    public String getFullInfo(boolean used) {
        final StringBuilder sb = new StringBuilder();
        sb.append('"').append(path).append('"');
        if (!used) {
            sb.append(" (unused)");
        }
        sb.append(" {\n");
        sb.append('\t').append("Date: ").append(creationDateStr).append('\n');
        sb.append('\t').append("Versions: ").append(versions).append('\n');
        sb.append('\t').append("Comment: ").append(comment).append('\n');
        sb.append('\t').append("Entries:").append('\n');
        for (ReplacementEntry e : this.getEntries()) {
            sb.append("\t\t").append(e.getRemote()).append('\n');
        }
        sb.append("}\n");
        return sb.toString();
    }

    @Override
    public String toString() {
        return "Replacement file: " + path + "; Date: " + creationDateStr + "; Versions: " + versions;
    }
    
}
