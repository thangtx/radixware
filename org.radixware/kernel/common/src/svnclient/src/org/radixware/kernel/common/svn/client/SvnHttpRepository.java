/*
 * Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * This Source Code is distributed WITHOUT ANY WARRANTY; including any 
 * implied warranties but not limited to warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
 * License, v. 2.0. for more details.
 */
package org.radixware.kernel.common.svn.client;

import java.io.OutputStream;
import java.net.URI;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import javax.net.ssl.X509TrustManager;
import org.radixware.kernel.common.svn.RadixSvnException;
import org.radixware.kernel.common.svn.client.impl.SvnPlainConnector;

/**
 *
 * @author akrylov
 */
public class SvnHttpRepository extends SvnRepository {

    public SvnHttpRepository(URI location, String path, SvnCredentials[] credentials, X509TrustManager trustManager) {
        super(location, path, credentials, trustManager);
    }

    @Override
    protected SvnConnector createConnector() {
        return new SvnPlainConnector();
    }

    protected String getFullPath(String path) throws RadixSvnException {
        return SvnPath.append(getRootUrl(), path);
    }

    static final boolean TRACE;

    static {
        TRACE = System.getProperty("org.radixware.svn.client.http.repo.trace") != null;
    }

    @Override
    public SvnEntry getDir(String path, long revision, boolean wantProps, boolean wantContents, boolean wantInheritedProps, SvnEntryHandler handler) throws RadixSvnException {
        final SvnEntry[] parent = new SvnEntry[1];
        final String[] parentVCC = new String[1];
        try {
            connect();
            path = getPathRelativeToMine(path);
            path = SvnPath.uriEncode(path);
            final String inititalPath = path;

            SvnHttpConnection connection = (SvnHttpConnection) getConnection();
            if (revision >= 0) {
                DAV.BaselineInfo info = getBaselineInfo(connection, this, "", revision, false, true, null);
                path = SvnPath.append(SvnPath.append(info.baselineBase, info.baselinePath), path);
            }
            while (path.endsWith("/")) {
                path = SvnPath.removeTail(path);
            }
            final int parentPathSegments = SvnPath.getSegmentsCount(path);

            DAV.Element[] dirProperties = new DAV.Element[]{
                DAV.Element.VERSION_CONTROLLED_CONFIGURATION,
                DAV.Element.VERSION_NAME,
                DAV.Element.GET_CONTENT_LENGTH,
                DAV.Element.RESOURCE_TYPE,
                DAV.Element.CREATOR_DISPLAY_NAME,
                DAV.Element.CREATION_DATE};

            Map<String, DAV.Properties> dirEntsMap = new HashMap<>();

            getProperties(connection, path, 1, null, null, dirEntsMap);
            SvnEntry thisEntry = null;
            for (Iterator dirEnts = dirEntsMap.keySet().iterator(); dirEnts.hasNext();) {
                String url = (String) dirEnts.next();
                DAV.Properties child = (DAV.Properties) dirEntsMap.get(url);
                String href = child.getURL();
                if (href.endsWith("/")) {
                    href = SvnPath.removeTail(href);
                }
                String asPath = href;
                boolean isThis = false;
//                if (asPath.endsWith("/")) {
//                    asPath = SvnPath.removeTail(asPath);
//                    if (Objects.equals(asPath, path)) {//ignore self
//                        
//                    }
//                }
                String name = "";
                if (parentPathSegments != SvnPath.getSegmentsCount(href)) {
                    name = SvnPath.uriDecode(SvnPath.tail(href));
                }
                if ("".equals(name)) {
                    isThis = true;
                }

                DAV.Property revisionStr = child.getPropertyValue(DAV.Element.VERSION_NAME);
                long lastRevision = -1;
                if (revisionStr != null) {
                    try {
                        lastRevision = Long.parseLong(revisionStr.getString());
                    } catch (NumberFormatException nfe) {
                        throw new RadixSvnException("Malformed response data");
                    }
                }
                final DAV.Property sizeValue = child.getPropertyValue(DAV.Element.GET_CONTENT_LENGTH);
                final String sizeValueString = sizeValue == null ? null : sizeValue.getString();
                long size = 0;
                if (sizeValueString != null && sizeValueString.trim().length() > 0) {
                    try {
                        size = Long.parseLong(sizeValueString.trim());
                    } catch (NumberFormatException nfe) {
                        throw new RadixSvnException("Malformed response data");
                    }
                }
                SvnEntry.Kind kind = SvnEntry.Kind.FILE;

                if (child.isCollection()) {
                    kind = SvnEntry.Kind.DIRECTORY;
                }
                DAV.Property authorValue = child.getPropertyValue(DAV.Element.CREATOR_DISPLAY_NAME);
                String author = authorValue == null ? null : authorValue.getString();
                DAV.Property dateValue = child.getPropertyValue(DAV.Element.CREATION_DATE);
                Date date = dateValue != null ? SvnUtil.parseDate(dateValue.getString()) : null;
                String repositoryRoot = getRootUrl();
                String repositoryPath = SvnPath.append(SvnPath.append(repositoryRoot, inititalPath), name);
                SvnEntry entry = new SvnEntry(SvnPath.append(inititalPath, name), name, repositoryPath, author, lastRevision, date, size, kind,
                        child.getProperties() == null ? "" : String.valueOf(child.getProperties().get("comment"))
                );
                if (isThis) {
                    thisEntry = entry;
                    if (handler == null) {
                        break;
                    }
                } else {
                    if (handler != null) {
                        handler.accept(entry);
                    }
                }
            }
            return thisEntry;
        } finally {
            disconnect();
        }
    }

    protected DAV.BaselineInfo getBaselineInfo(SvnHttpConnection connection, SvnHttpRepository repos, String path, long revision,
            boolean includeType, boolean includeRevision, DAV.BaselineInfo info) throws RadixSvnException {
        DAV.Element[] properties = includeRevision ? DAV.Element.BASELINE_PROPERTIES : new DAV.Element[]{DAV.Element.BASELINE_COLLECTION};
        DAV.Properties baselineProperties = getBaselineProperties(connection, repos, path, revision, properties);

        info = info == null ? new DAV.BaselineInfo() : info;
        info.baselinePath = baselineProperties.getURL();
        DAV.Property baseValue = baselineProperties.getPropertyValue(DAV.Element.BASELINE_COLLECTION);
        info.baselineBase = baseValue == null ? null : baseValue.getString();
        info.baseline = baselineProperties.getOriginalURL();
        if (info.baselineBase == null) {
            throw new RadixSvnException("'DAV:baseline-collection' not present on the baseline resource");
        }
//        info.baselineBase = SVNEncodingUtil.uriEncode(info.baselineBase);
        if (includeRevision) {
            DAV.Property version = baselineProperties.getPropertyValue(DAV.Element.VERSION_NAME);
            if (version == null) {
                throw new RadixSvnException("'DAV:version-name' not present on the baseline resource");
            }
            try {
                info.revision = Long.parseLong(version.getString());
            } catch (NumberFormatException nfe) {
                throw new RadixSvnException("Malformed revision number");
            }
        }
        if (includeType) {
            Map<String, DAV.Properties> propsMap = new HashMap<>();
            path = SvnPath.append(info.baselineBase, info.baselinePath);

            getProperties(connection, path, 0, null, new DAV.Element[]{DAV.Element.RESOURCE_TYPE}, propsMap);

            if (!propsMap.isEmpty()) {
                DAV.Properties props = propsMap.values().iterator().next();
                info.isDirectory = props != null && props.isCollection();
            }
        }
        return info;
    }

    private SvnEntry mkEntry(String entryPath, DAV.Properties child) throws RadixSvnException {
        String href = child.getURL();
        SvnEntry.Kind kind = SvnEntry.Kind.FILE;
        DAV.Property revisionStr = child.getPropertyValue(DAV.Element.VERSION_NAME);
        long lastRevision = -1;
        if (revisionStr != null) {
            try {
                lastRevision = Long.parseLong(revisionStr.getString());
            } catch (NumberFormatException nfe) {
                throw new RadixSvnException(RadixSvnException.Type.MALFORMED_DATA);
            }
        }
        DAV.Property sizeValue = child.getPropertyValue(DAV.Element.GET_CONTENT_LENGTH);
        final String sizeValueString = sizeValue == null ? null : sizeValue.getString();//getPropertyValue(DAV.Element.HREF);
        long size = 0;
        if (sizeValueString != null && sizeValueString.trim().length() > 0) {
            try {
                size = Long.parseLong(sizeValueString.trim());
            } catch (NumberFormatException nfe) {
                throw new RadixSvnException(RadixSvnException.Type.MALFORMED_DATA);
            }
        }
        if (child.isCollection()) {
            kind = SvnEntry.Kind.DIRECTORY;
        }
        DAV.Property authorValue = child.getPropertyValue(DAV.Element.CREATOR_DISPLAY_NAME);
        String author = authorValue == null ? null : authorValue.getString();
        DAV.Property dateValue = child.getPropertyValue(DAV.Element.CREATION_DATE);
        Date date = dateValue != null ? SvnUtil.parseDate(dateValue.getString()) : null;
        //boolean hasProperties = false;
//        for (Iterator props = child.getProperties().keySet().iterator(); props.hasNext();) {
//            DAV.Element property = (DAV.Element) props.next();
//            if (DAV.Element.SVN_CUSTOM_PROPERTY_NAMESPACE.equals(property.namespace)
//                    || DAV.Element.SVN_SVN_PROPERTY_NAMESPACE.equals(property.namespace)) {
//                hasProperties = true;
//                break;
//            }
//        }
//        SvnHttpConnection connection = (SvnHttpConnection) getConnection();
        String repositoryRoot = getRootUrl();
        String name = SvnPath.tail(entryPath);

        return new SvnEntry(entryPath, name, SvnPath.append(repositoryRoot, entryPath), author, lastRevision, date, size, kind, "");
    }

    private void getProperties(SvnHttpConnection connection, String path, int depth, String label, DAV.Element[] properties, Map<String, DAV.Properties> result) throws RadixSvnException {
        connection.execPropFindRequest(path, label, depth, properties, result);
    }

    private DAV.Properties getBaselineProperties(SvnHttpConnection connection, SvnHttpRepository repos, String path, long revision, DAV.Element[] elements) throws RadixSvnException {
        DAV.Properties properties = null;
        String loppedPath = "";
        properties = findStartingProperties(connection, repos, path);
        DAV.Property vccValue = properties.getPropertyValue(DAV.Element.VERSION_CONTROLLED_CONFIGURATION);
        if (vccValue == null) {
            throw new RadixSvnException("The VCC property was not found on the resource");
        }
        loppedPath = properties.getLoppedPath();
        DAV.Property baselineRelativePathValue = properties.getPropertyValue(DAV.Element.BASELINE_RELATIVE_PATH);
        if (baselineRelativePathValue == null) {
            throw new RadixSvnException("The relative-path property was not found on the resource");
        }
        String baselineRelativePath = SvnPath.uriEncode(baselineRelativePathValue.getString());
        baselineRelativePath = SvnPath.append(baselineRelativePath, loppedPath);
        String label = null;
        String vcc = vccValue.getString();
        if (revision < 0) {

            vcc = getPropertyValue(connection, vcc, null, DAV.Element.CHECKED_IN);
        } else {
            label = Long.toString(revision);
        }
        properties = getResourceProperties(connection, vcc, label, elements);
        properties.setURL(baselineRelativePath);
        return properties;
    }

    @Override
    public String getRootUrl() throws RadixSvnException {
        if (super.getRootUrl() == null) {
            try {
                connect();
                SvnHttpConnection connection = (SvnHttpConnection) getConnection();
                findStartingProperties(connection, this, getLocation().getPath());
            } finally {
                disconnect();
            }
        }
        return super.getRootUrl();
    }

    private DAV.Properties findStartingProperties(SvnHttpConnection connection, SvnHttpRepository repos, String fullPath) throws RadixSvnException {
        DAV.Properties props = null;
        String originalPath = fullPath;
        String loppedPath = "";
        if ("".equals(fullPath)) {
            props = getStartingProperties(connection, fullPath, null);
        }
        if (props == null) {
            while (!"".equals(fullPath)) {
                try {
                    props = getStartingProperties(connection, fullPath, null);
                    break;
                } catch (RadixSvnException e) {

                }
                loppedPath = SvnPath.append(SvnPath.tail(fullPath), loppedPath);
                int length = fullPath.length();
                fullPath = SvnPath.removeTail(fullPath);
                // will return "" for "/dir", hack it here, to make sure we're not missing root.
                // we assume full path always starts with "/". 
                if (length > 1 && "".equals(fullPath)) {
                    fullPath = "/";
                }
                if (length == fullPath.length()) {
                    throw new RadixSvnException("The path was not part of repository");
                }
            }
            if ("".equals(fullPath)) {
                throw new RadixSvnException("No part of path " + originalPath + " was found in repository HEAD", RadixSvnException.Type.IO, 404);
            }
            if (props != null && super.getRootUrl() == null) {
                if (props.getPropertyValue(DAV.Element.REPOSITORY_UUID) != null && repos != null) {
                    repos.setUUID(props.getPropertyValue(DAV.Element.REPOSITORY_UUID).getString());
                }

                if (props.getPropertyValue(DAV.Element.BASELINE_RELATIVE_PATH) != null && repos != null) {
                    //path of found entry relative to root;
                    String relativePath = props.getPropertyValue(DAV.Element.BASELINE_RELATIVE_PATH).getString();
                    if (relativePath.endsWith("/")) {
                        relativePath = relativePath.substring(0, relativePath.length() - 1);
                    }
                    String fullUrl = repos.getLocation().toString();
                    if (fullUrl.endsWith("/")) {
                        fullUrl = fullUrl.substring(0, fullUrl.length() - 1);
                    }
                    String rootUrl;
                    if (fullUrl.endsWith(relativePath)) {
                        rootUrl = fullUrl.substring(0, fullUrl.length() - relativePath.length());
                    } else {
                        rootUrl = fullUrl;
                    }

                    if (rootUrl.endsWith("/")) {
                        rootUrl = rootUrl.substring(0, rootUrl.length() - 1);
                    }
                    repos.setRootUrl(rootUrl);
                }

            }
            props.setLoppedPath(loppedPath);
        }

        return props;
    }

    DAV.Properties getResourceProperties(SvnHttpConnection connection, String path, String label,
            DAV.Element[] properties) throws RadixSvnException {
        Map<String, DAV.Properties> resultMap = new HashMap<>();

        getProperties(connection, path, 0, label, properties, resultMap);

        if (!resultMap.isEmpty()) {
            return resultMap.values().iterator().next();
        }
        label = label == null ? "NULL" : label;
        throw new RadixSvnException("Failed to find label " + label + " for URL " + path);
    }

    public String getPropertyValue(SvnHttpConnection connection, String path, String label, DAV.Element property) throws RadixSvnException {
        DAV.Properties props = getResourceProperties(connection, path, label, new DAV.Element[]{property});
        DAV.Property value = props.getPropertyValue(property);
        if (value == null) {
            throw new RadixSvnException(property.namespace + ":" + property.property + "was not present on the resource");
        }
        return value.getString();
    }

    public DAV.Properties getStartingProperties(SvnHttpConnection connection, String path, String label) throws RadixSvnException {
        return getResourceProperties(connection, path, label, DAV.Element.STARTING_PROPERTIES);
    }

    @Override
    public long getLatestRevision() throws RadixSvnException {
        try {
            connect();
            SvnHttpConnection connection = (SvnHttpConnection) getConnection();
            DAV.BaselineInfo info = getBaselineInfo(connection, this, "", -1, false, true, null);
            return info.revision;
        } finally {
            disconnect();
        }
    }

    @Override
    public SvnEntry info(String path, long revision) throws RadixSvnException {
        try {
            connect();
            SvnHttpConnection connection = (SvnHttpConnection) getConnection();

            path = SvnPath.uriEncode(path);
            final String inititalPath = path;
            while (path.endsWith("/")) {
                path = SvnPath.removeTail(path);
            }
            path = getPathRelativeToMine(path);
            if (revision >= 0) {
                DAV.BaselineInfo info = getBaselineInfo(connection, this, "", revision, false, true, null);
                path = SvnPath.append(SvnPath.append(info.baselineBase, info.baselinePath), path);
            }

            if (revision >= 0) {
                try {
                    DAV.BaselineInfo info = getBaselineInfo(connection, this, path, revision, false, true, null);
                    path = SvnPath.append(info.baselineBase, info.baselinePath);
                } catch (RadixSvnException e) {
                    throw e;
                }
            }
            DAV.Element[] elements = null;
            Map propsMap = new HashMap();
            getProperties(connection, path, 0, null, elements, propsMap);
            if (!propsMap.isEmpty()) {
                DAV.Properties props = (DAV.Properties) propsMap.values().iterator().next();
                return mkEntry(inititalPath, props);
            } else {
                throw new RadixSvnException(RadixSvnException.Type.MALFORMED_DATA);
            }
        } finally {
            disconnect();
        }
    }

    @Override
    public SvnEntry.Kind checkPath(String path, long revision) throws RadixSvnException {
        try {
            connect();

            SvnHttpConnection connection = (SvnHttpConnection) getConnection();
            path = SvnPath.uriEncode(path);
            while (path.endsWith("/")) {
                path = SvnPath.removeTail(path);
            }
            path = getPathRelativeToMine(path);
            if (revision >= 0) {
                DAV.BaselineInfo info = getBaselineInfo(connection, this, "", revision, false, true, null);
                path = SvnPath.append(SvnPath.append(info.baselineBase, info.baselinePath), path);                
            }
            try {
                DAV.BaselineInfo info = getBaselineInfo(connection, this, path, revision, true, false, null);

                return info.isDirectory ? SvnEntry.Kind.DIRECTORY : SvnEntry.Kind.FILE;
            } catch (RadixSvnException ex) {
                if (ex.isIOError() && ex.tag == 404) {
                    return SvnEntry.Kind.NONE;
                } else {
                    throw ex;
                }
            }
        } catch (RadixSvnException e) {
            throw e;
        } finally {
            disconnect();
        }
    }

    @Override
    public long getFile(String path, long revision, Map<String, String> props, OutputStream contents) throws RadixSvnException {
        long fileRevision = revision;
        try {
            connect();
            path = SvnPath.uriEncode(path);
            path = getPathRelativeToMine(path);
            SvnHttpConnection connection = (SvnHttpConnection) getConnection();
            if (revision != -2) {
                DAV.BaselineInfo info = getBaselineInfo(connection, this, "", revision, false, true, null);
                path = SvnPath.append(SvnPath.append(info.baselineBase, info.baselinePath), path);
                fileRevision = info.revision;
            }

            if (props != null) {

                DAV.Properties resourceProps = getResourceProperties(connection, path, null, null);
                /*
                 svn:entry:revision = 107867
                 svn:entry:checksum = cdc3a0e41b0569862c708609806de2b3
                 svn:entry:last-author = akrylov
                 svn:entry:uuid = 82f1349f-fc4f-0410-84fd-a15d3b6d731c
                 svn:entry:committed-rev = 107805
                 svn:entry:committed-date = 2016-04-06T07:14:57.025998Z
                 */
                DAV.Property prop = resourceProps.getPropertyValue(DAV.Element.CREATOR_DISPLAY_NAME);
                if (prop != null) {
                    props.put("svn:entry:last-author", prop.getString());
                }
                prop = resourceProps.getPropertyValue(DAV.Element.SHA1_CHECKSUM);
                if (prop != null) {
                    props.put("svn:entry:checksum", prop.getString());
                }
                prop = resourceProps.getPropertyValue(DAV.Element.CREATION_DATE);
                if (prop != null) {
                    props.put("svn:entry:committed-date", prop.getString());
                }
                prop = resourceProps.getPropertyValue(DAV.Element.VERSION_NAME);
                if (prop != null) {
                    props.put("svn:entry:committed-rev", prop.getString());
                }
                SvnProperties svnProps = new SvnProperties();
                filterProperties(resourceProps, svnProps);

                Map<String, SvnProperties.Value> map = svnProps.map();

                if (fileRevision >= 0) {
                    props.put("svn:entry:revision", String.valueOf(fileRevision));
                }
            }

            if (contents != null) {
                connection.get(path, contents);
            }
        } finally {
            disconnect();
        }
        return fileRevision;
    }

    @Override
    public void replay(long lowRevision, long highRevision, boolean sendDeltas, SvnEditor editor) throws RadixSvnException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setRevisionPropertyValue(long revision, String propertyName, SvnProperties.Value value) throws RadixSvnException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SvnProperties getRevisionProperties(long revision, SvnProperties properties) throws RadixSvnException {

        properties = properties == null ? new SvnProperties() : properties;
        try {
            connect();
            String path = getLocation().getPath();
            path = SvnPath.uriEncode(path);
            path = getPathRelativeToMine(path);
            SvnHttpConnection connection = (SvnHttpConnection) getConnection();
            DAV.Properties source = getBaselineProperties(connection, this, "", revision, null/*new DAV.Element[]{
             DAV.Element.REVISION,
             DAV.Element.CREATION_DATE,
             DAV.Element.CREATOR_DISPLAY_NAME,
             DAV.Element.COMMENT
             }*/);
            properties = filterProperties(source, properties);

        } finally {
            disconnect();
        }
        return properties;
    }

    String getCommitMessage(long revision) throws RadixSvnException {
        SvnProperties props = getRevisionProperties(revision, null);
        SvnProperties.Value val = props.get("svn:log");
        return val == null ? "" : val.getValue();
    }

    public static SvnProperties filterProperties(DAV.Properties source, SvnProperties target) {
        target = target == null ? new SvnProperties() : target;
        for (Iterator props = source.getProperties().entrySet().iterator(); props.hasNext();) {
            Map.Entry entry = (Map.Entry) props.next();
            DAV.Element element = (DAV.Element) entry.getKey();
            DAV.Property value = (DAV.Property) entry.getValue();
            SvnProperties.Value v = new SvnProperties.Value(value.stringValue, value.byteValue);
            String propertyName = getPropertyNameByElement(element);
            if (propertyName != null) {
                target.set(propertyName, v);
            }
        }
        return target;
    }

    public static String getPropertyNameByElement(DAV.Element element) {
        if (element == null) {
            return null;
        }
        String namespace = element.namespace;
        String name = element.property;
        if (namespace.equals(DAV.Element.SVN_CUSTOM_PROPERTY_NAMESPACE)) {
            // hack!
            if (name.startsWith("svk_")) {
                name = name.substring(0, "svk".length()) + ":" + name.substring("svk".length() + 1);
            }
            return name;
        } else if (namespace.equals(DAV.Element.SVN_SVN_PROPERTY_NAMESPACE)) {
            return "svn:" + name;
        } else if (DAV.Element.CHECKED_IN.equals(element)) {
            return DAV.Properties.WC_URL;
        }
        return null;
    }

    @Override
    public SvnEditor getEditor(String logMessage) throws RadixSvnException {
        return new SvnHttpEditor(this);
    }

    @Override
    protected SvnConnection createConnection() {
        return new SvnHttpConnection();
    }

    protected long logImpl(String[] targetPaths, long startRevision, long endRevision,
            boolean changedPath, boolean strictNode, long limit,
            boolean includeMergedRevisions, String[] revPropNames,
            final ISvnLogHandler handler) throws RadixSvnException {
        if (targetPaths == null || targetPaths.length == 0) {
            targetPaths = new String[]{""};
        }

        long latestRev = -1;
        if (startRevision <= 0) {
            startRevision = latestRev = getLatestRevision();
        }
        if (endRevision <= 0) {
            endRevision = latestRev != -1 ? latestRev : getLatestRevision();
        }

        try {
            connect();
            SvnHttpConnection connection = (SvnHttpConnection) getConnection();

            String[] fullPaths = new String[targetPaths.length];
            for (int i = 0; i < targetPaths.length; i++) {
                fullPaths[i] = targetPaths[i];
            }
            Collection<String> relativePaths = new HashSet<>();
            String path = SvnPath.condencePaths(fullPaths, relativePaths, true);
            if (relativePaths.isEmpty()) {
                relativePaths.add("");
            }
            fullPaths = new String[0];
            long revision = Math.max(startRevision, endRevision);
            path = SvnPath.uriEncode(path);
            DAV.BaselineInfo info = getBaselineInfo(connection, this, "", revision, false, false, null);
            path = SvnPath.append(SvnPath.append(info.baselineBase, info.baselinePath), path);
            try {
                return connection.log(path, startRevision, endRevision, changedPath,
                        strictNode, includeMergedRevisions, revPropNames, limit, fullPaths, handler);
            } catch (RadixSvnException e) {
                throw e;
            }
        } finally {
            disconnect();
        }
    }

    @Override
    public void log(String path, long startRevision, long endRevision, boolean changedPaths, ISvnLogHandler logHandler) throws RadixSvnException {
        log(new String[]{path}, startRevision, endRevision, changedPaths, logHandler);
    }

    @Override
    public void log(String[] pathes, long startRevision, long endRevision, boolean changedPaths, ISvnLogHandler logHandler) throws RadixSvnException {
        logImpl(pathes, startRevision, endRevision, changedPaths, true, 0, false, null, logHandler);
    }

    @Override
    public void log(String path, long startRevision, long endRevision, boolean changedPaths, boolean strictNode, ISvnLogHandler logHandler) throws RadixSvnException {
        log(new String[]{path}, startRevision, endRevision, changedPaths, strictNode, logHandler);
    }

    @Override
    public void log(String[] pathes, long startRevision, long endRevision, boolean changedPaths, boolean strictNode, ISvnLogHandler logHandler) throws RadixSvnException {
        logImpl(pathes, startRevision, endRevision, changedPaths, strictNode, 0, false, null, logHandler);
    }

}
