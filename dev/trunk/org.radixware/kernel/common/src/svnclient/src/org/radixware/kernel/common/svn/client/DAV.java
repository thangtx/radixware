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

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.svn.RadixSvnException;

/**
 *
 * @author akrylov
 */
class DAV {

    public static class BaselineInfo {

        public String baselinePath;
        public String baselineBase;

        public long revision;
        public boolean isDirectory;
        public String baseline;
    }

    public static class Element {

        public static final String DAV_NAMESPACE = "DAV:";
        public static final String SVN_NAMESPACE = "svn:";
        public static final String SVN_DAV_PROPERTY_NAMESPACE = "http://subversion.tigris.org/xmlns/dav/";
        public static final String SVN_CUSTOM_PROPERTY_NAMESPACE = "http://subversion.tigris.org/xmlns/custom/";
        public static final String SVN_SVN_PROPERTY_NAMESPACE = "http://subversion.tigris.org/xmlns/svn/";
        public static final String SVN_APACHE_PROPERTY_NAMESPACE = "http://apache.org/dav/xmlns";

        public static final Element ACTIVITY = new Element(DAV_NAMESPACE, "activity");
        public static final Element VERSION_HISTORY = new Element(DAV_NAMESPACE, "version-history");
        public static final Element DISPLAY_NAME = new Element(DAV_NAMESPACE, "displayname");
        public static final Element SUPPORTED_LIVE_PROPERTY = new Element(DAV_NAMESPACE, "supported-live-property");
        public static final Element MERGE_RESPONSE = new Element(DAV_NAMESPACE, "merge-response");
        public static final Element UPDATE_SET = new Element(DAV_NAMESPACE, "updated-set");
        public static final Element NO_AUTO_MERGE = new Element(DAV_NAMESPACE, "no-auto-merge");
        public static final Element NO_CHECKOUT = new Element(DAV_NAMESPACE, "no-checkout");
        public static final Element SOURCE = new Element(DAV_NAMESPACE, "source");
        public static final Element MULTISTATUS = new Element(DAV_NAMESPACE, "multistatus");
        public static final Element RESPONSE = new Element(DAV_NAMESPACE, "response");
        public static final Element RESPONSE_DESCRIPTION = new Element(DAV_NAMESPACE, "responsedescription");
        public static final Element HREF = new Element(DAV_NAMESPACE, "href");
        public static final Element PROPSTAT = new Element(DAV_NAMESPACE, "propstat");
        public static final Element PROP = new Element(DAV_NAMESPACE, "prop");
        public static final Element STATUS = new Element(DAV_NAMESPACE, "status");
        public static final Element BASELINE = new Element(DAV_NAMESPACE, "baseline");
        public static final Element BASELINE_COLLECTION = new Element(DAV_NAMESPACE, "baseline-collection");
        public static final Element CHECKED_IN = new Element(DAV_NAMESPACE, "checked-in");
        public static final Element COLLECTION = new Element(DAV_NAMESPACE, "collection");
        public static final Element RESOURCE_TYPE = new Element(DAV_NAMESPACE, "resourcetype");
        public static final Element VERSION_CONTROLLED_CONFIGURATION = new Element(DAV_NAMESPACE, "version-controlled-configuration");
        public static final Element VERSION_NAME = new Element(DAV_NAMESPACE, "version-name");
        public static final Element GET_CONTENT_LENGTH = new Element(DAV_NAMESPACE, "getcontentlength");
        public static final Element CREATION_DATE = new Element(DAV_NAMESPACE, "creationdate");
        public static final Element CREATOR_DISPLAY_NAME = new Element(DAV_NAMESPACE, "creator-displayname");
        public static final Element COMMENT = new Element(DAV_NAMESPACE, "comment");
        public static final Element DATE = new Element(SVN_NAMESPACE, "date");
        public static final Element POST_COMMIT_ERROR = new Element(SVN_NAMESPACE, "post-commit-err");
        public static final Element PROPFIND = new Element(DAV_NAMESPACE, "propfind");
        public static final Element ALLPROP = new Element(DAV_NAMESPACE, "allprop");
        public static final Element PROPNAME = new Element(DAV_NAMESPACE, "propname");
        public static final Element ACTIVE_LOCK = new Element(DAV_NAMESPACE, "activelock");
        public static final Element LOCK_TYPE = new Element(DAV_NAMESPACE, "locktype");
        public static final Element LOCK_SCOPE = new Element(DAV_NAMESPACE, "lockscope");
        public static final Element WRITE = new Element(DAV_NAMESPACE, "write");
        public static final Element EXCLUSIVE = new Element(DAV_NAMESPACE, "exclusive");
        public static final Element SHARED = new Element(DAV_NAMESPACE, "shared");
        public static final Element DEPTH = new Element(DAV_NAMESPACE, "depth");

        public static final Element SUPPORTED_LOCK = new Element(DAV_NAMESPACE, "supportedlock");
        public static final Element LOCK_DISCOVERY = new Element(DAV_NAMESPACE, "lockdiscovery");
        public static final Element LOCK_OWNER = new Element(DAV_NAMESPACE, "owner");
        public static final Element LOCK_TIMEOUT = new Element(DAV_NAMESPACE, "timeout");
        public static final Element LOCK_TOKEN = new Element(DAV_NAMESPACE, "locktoken");
        public static final Element LOCK_ENTRY = new Element(DAV_NAMESPACE, "lockentry");

        public static final Element SVN_LOCK_TOKEN_LIST = new Element(SVN_NAMESPACE, "lock-token-list");
        public static final Element SVN_LOCK = new Element(SVN_NAMESPACE, "lock");
        public static final Element SVN_LOCK_PATH = new Element(SVN_NAMESPACE, "path");
        public static final Element SVN_LOCK_TOKEN = new Element(SVN_NAMESPACE, "token");
        public static final Element SVN_LOCK_COMMENT = new Element(SVN_NAMESPACE, "comment");
        public static final Element SVN_LOCK_OWNER = new Element(SVN_NAMESPACE, "owner");
        public static final Element SVN_LOCK_CREATION_DATE = new Element(SVN_NAMESPACE, "creationdate");
        public static final Element SVN_LOCK_EXPIRATION_DATE = new Element(SVN_NAMESPACE, "expirationdate");

        //servlet defined svn namespace properties
        public static final Element PATH = new Element(SVN_NAMESPACE, "path");
        public static final Element REVISION = new Element(SVN_NAMESPACE, "revision");
        public static final Element START_REVISION = new Element(SVN_NAMESPACE, "start-revision");
        public static final Element END_REVISION = new Element(SVN_NAMESPACE, "end-revision");
        public static final Element PEG_REVISION = new Element(SVN_NAMESPACE, "peg-revision");
        public static final Element INCLUDE_MERGED_REVISIONS = new Element(SVN_NAMESPACE, "include-merged-revisions");

        public static final Element BASELINE_RELATIVE_PATH = new Element(SVN_DAV_PROPERTY_NAMESPACE, "baseline-relative-path");
        public static final Element REPOSITORY_UUID = new Element(SVN_DAV_PROPERTY_NAMESPACE, "repository-uuid");
        public static final Element MD5_CHECKSUM = new Element(SVN_DAV_PROPERTY_NAMESPACE, "md5-checksum");
        public static final Element SHA1_CHECKSUM = new Element(SVN_DAV_PROPERTY_NAMESPACE, "sha1-checksum");
        public static final Element DEADPROP_COUNT = new Element(SVN_DAV_PROPERTY_NAMESPACE, "deadprop-count");

        public static final Element AUTO_VERSION = new Element(DAV_NAMESPACE, "auto-version");

        public static final Element MERGE_INFO_ITEM = new Element(SVN_NAMESPACE, "mergeinfo-item");
        public static final Element MERGE_INFO_PATH = new Element(SVN_NAMESPACE, "mergeinfo-path");
        public static final Element MERGE_INFO_INFO = new Element(SVN_NAMESPACE, "mergeinfo-info");

        //Supported live properties
        public static final Element GET_CONTENT_LANGUAGE = new Element(DAV_NAMESPACE, "getcontentlanguage");
        public static final Element GET_CONTENT_TYPE = new Element(DAV_NAMESPACE, "getcontenttype");
        public static final Element GET_ETAG = new Element(DAV_NAMESPACE, "getetag");
        public static final Element GET_LAST_MODIFIED = new Element(DAV_NAMESPACE, "getlastmodified");
        public static final Element[] BASELINE_PROPERTIES = {BASELINE_COLLECTION, VERSION_NAME};
        public static final Element[] STARTING_PROPERTIES = {VERSION_CONTROLLED_CONFIGURATION, RESOURCE_TYPE, BASELINE_RELATIVE_PATH, REPOSITORY_UUID};

        public final String property;
        public final String namespace;

        public Element(String namespace, String property) {
            this.property = property;
            this.namespace = namespace;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Element) {
                Element e = (Element) obj;
                return Objects.equals(namespace, e.namespace) && Objects.equals(property, e.property);
            } else {
                return false;
            }

        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 37 * hash + Objects.hashCode(this.property);
            hash = 37 * hash + Objects.hashCode(this.namespace);
            return hash;
        }

    }

    public static class Properties {

        public static final String SVN_WC_PREFIX = "svn:wc:";

        public static final String WC_URL = SVN_WC_PREFIX + "ra_dav:version-url";

        private Map<Element, Property> propsMap = new HashMap<>();
        private boolean isCollection;
        private String url;
        private String loppedPath;
        private String originalPath;

        public Properties(String myURL) {
            this.url = myURL;
        }

        public String getURL() {
            return url;
        }

        public boolean isCollection() {
            return isCollection;
        }

        public Map getProperties() {
            return propsMap;
        }

        public Property getPropertyValue(DAV.Element name) {
            return propsMap.get(name);
        }

        public void setLoppedPath(String loppedPath) {
            this.loppedPath = loppedPath;
        }

        public String getLoppedPath() {
            return loppedPath;
        }

        public void setProperty(DAV.Element name, Property value) {
            propsMap.put(name, value);
        }

        public void setURL(String url) {
            originalPath = this.url;
            this.url = url;
        }

        public String getOriginalURL() {
            return originalPath;
        }

        public void setCollection(boolean collection) {
            isCollection = collection;
        }
    }

    public static class Property {

        String stringValue;
        byte[] byteValue;

        public Property(String stringValue) {
            this.stringValue = stringValue;
        }

        public String getString() {
            if (stringValue == null) {
                if (byteValue == null) {
                    return null;
                }
                try {
                    stringValue = new String(byteValue, "UTF-8");
                } catch (UnsupportedEncodingException ex) {
                    Logger.getLogger(DAV.class.getName()).log(Level.SEVERE, null, ex);
                    return null;
                }
            }
            return stringValue;
        }
    }

    static class PathDataCache {

        static class PathData {

            private String versionUrl;
            private String workingUrl;
        }
        private final Map<String, PathData> cache = new HashMap<>();

        private String getWorkingUrlForPath(String path) {
            PathData data = cache.get(path);
            if (data != null) {
                return data.workingUrl;
            } else {
                return null;
            }
        }

        public void putVersionUrlForPath(String path, String versionUrl) {
            PathData data = cache.get(path);
            if (data == null) {
                data = new PathData();
                cache.put(path, data);
            }
            data.versionUrl = versionUrl;
        }

        public void putWorkingUrlForPath(String path, String workingUrl) {
            PathData data = cache.get(path);
            if (data == null) {
                data = new PathData();
                cache.put(path, data);
            }
            data.workingUrl = workingUrl;
        }
    }

    static class Resource {

        private String wcUrl;
        private String versionUrl;
        private String url;
        private String path;
        private long revision;
        private boolean isCopy;
        private SvnHttpConnection connection;
        private boolean wasAdded;
        private final PathDataCache cache;
        private SvnProperties properties;

        public Resource(SvnHttpConnection connection, PathDataCache cache, String path, long revision) {
            this(connection, cache, path, revision, false);
        }

        public Resource(SvnHttpConnection connection, PathDataCache cache, String path, long revision, boolean isCopy) {
            this.path = path;
            String locationPath = SvnPath.uriEncode(connection.getLocation().getPath());
            this.url = SvnPath.append(locationPath, path);
            this.revision = revision;
            this.connection = connection;
            this.isCopy = isCopy;
            this.cache = cache;
            this.wcUrl = cache.getWorkingUrlForPath(path); 
        }
        
        
        
        public void setAdded(boolean added) {
            wasAdded = added;
        }

        public boolean isAdded() {
            return wasAdded;
        }

        public boolean isCopy() {
            return isCopy;
        }

        public String getURL() {
            return url;
        }

        public String getPath() {
            return path;
        }

        public String getVersionURL() {
            return versionUrl;
        }

        public void fetchVersionURL(Resource parent, boolean force) throws RadixSvnException {
            if (wcUrl != null) {
                return;
            }
            if (!force && getVersionURL() != null) {
                return;
            }
            if (!force) {
                if (parent != null && parent.getVersionURL() != null && parent.revision == revision) {
                    versionUrl = SvnPath.append(parent.getVersionURL(), SvnPath.tail(path));
                    return;
                }
            }

            String path = url;
            if (revision >= 0) {
                DAV.BaselineInfo info = connection.getRepository().getBaselineInfo(connection, null, path, revision, false, false, null);
                path = SvnPath.append(info.baselineBase, info.baselinePath);
            }
            try {
                versionUrl = connection.getRepository().getPropertyValue(connection, path, null, DAV.Element.CHECKED_IN);
            } catch (RadixSvnException e) {
                try {
                    final String vcc = connection.getRepository().getPropertyValue(connection, path, null, DAV.Element.VERSION_CONTROLLED_CONFIGURATION);
                    versionUrl = connection.getRepository().getPropertyValue(connection, vcc, null, DAV.Element.CHECKED_IN);
                } catch (RadixSvnException ex) {
                    throw ex;
                }
            }
            cache.putVersionUrlForPath(this.path, versionUrl);
        }

        public String getWorkingURL() {
            return wcUrl;
        }

        public void dispose() {
            properties = null; 
        }

        public void setWorkingURL(String location) {
            wcUrl = location;
            cache.putWorkingUrlForPath(path, location);
        }
        
        protected Iterator<Map.Entry <String, SvnProperties.Value> > getPropertiesAsIterator(){
            if (properties == null){
                return null;
            }
            return properties.map().entrySet().iterator();
        }
        
        protected void putProperty(final String name, final SvnProperties.Value val) {
            if (properties == null){
                properties = new SvnProperties();
            }
            properties.set(name, val);
        }
        
    }

}
