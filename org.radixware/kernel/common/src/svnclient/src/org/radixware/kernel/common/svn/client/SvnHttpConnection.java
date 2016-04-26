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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.xml.namespace.QName;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.radixware.kernel.common.svn.RadixSvnException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author akrylov
 */
public class SvnHttpConnection extends SvnConnection {

    public static int DEPTH_ZERO = 0;
    public static int DEPTH_ONE = 1;
    public static int DEPTH_INFINITE = -1;
    public static final String CONNECTION_HEADER = "Connection";
    public static final String PROXY_CONNECTION_HEADER = "Proxy-Connection";
    public static final String TRANSFER_ENCODING_HEADER = "Transfer-Encoding";
    public static final String CONTENT_LENGTH_HEADER = "Content-Length";
    public static final String CONTENT_ENCODING_HEADER = "Content-Encoding";
    public static final String CONTENT_TYPE_HEADER = "Content-Type";
    public static final String AUTHENTICATE_HEADER = "WWW-Authenticate";
    public static final String PROXY_AUTHENTICATE_HEADER = "Proxy-Authenticate";
    public static final String LOCATION_HEADER = "Location";
    public static final String LOCK_OWNER_HEADER = "X-SVN-Lock-Owner";
    public static final String CREATION_DATE_HEADER = "X-SVN-Creation-Date";
    public static final String SVN_VERSION_NAME_HEADER = "X-SVN-Version-Name";
    public static final String SVN_OPTIONS_HEADER = "X-SVN-Options";
    public static final String TEXT_MD5 = "X-SVN-Result-Fulltext-MD5";
    public static final String BASE_MD5 = "X-SVN-Base-Fulltext-MD5";
    public static final String LOCK_TOKEN_HEADER = "Lock-Token";
    public static final String IF_HEADER = "If";
    public static final String DEPTH_HEADER = "Depth";
    public static final String LABEL_HEADER = "Label";
    public static final String DESTINATION_HEADER = "Destination";
    public static final String TIMEOUT_HEADER = "Timeout";
    public static final String DAV_HEADER = "DAV";
    public static final String SVN_DELTA_BASE_HEADER = "X-SVN-VR-Base";
    public static final String ACCEPT_ENCODING_HEADER = "Accept-Encoding";
    public static final String CONTENT_RANGE_HEADER = "content-range";
    public static final String HOST_HEADER = "Host";
    public static final String NEW_URI_HEADER = "New-uri";
    public static final String OVERWRITE_HEADER = "Overwrite";
    public static final String SVN_DAV_PROPERTY_NAMESPACE = "http://subversion.tigris.org/xmlns/dav/";
    public static final String SVN_CUSTOM_PROPERTY_NAMESPACE = "http://subversion.tigris.org/xmlns/custom/";
    public static final String SVN_SVN_PROPERTY_NAMESPACE = "http://subversion.tigris.org/xmlns/svn/";
    public static final String SVN_APACHE_PROPERTY_NAMESPACE = "http://apache.org/dav/xmlns";
    public static final String DEPTH_OPTION = SVN_DAV_PROPERTY_NAMESPACE + "svn/depth";
    public static final String MERGE_INFO_OPTION = SVN_DAV_PROPERTY_NAMESPACE + "svn/mergeinfo";
    public static final String LOG_REVPROPS_OPTION = SVN_DAV_PROPERTY_NAMESPACE + "svn/log-revprops";
    public static final String DAV_NAMESPACE_PREFIX = "D";
    public static final String SVN_NAMESPACE_PREFIX = "S";

    public static final String SVNDIFF_MIME_TYPE = "application/vnd.svn-svndiff";
    public static final String SET_COOKIE = "Set-Cookie";
    public static final String COOKIE = "Cookie";

    public static final Map<String, String> WELL_KNOWN_NS_PREFIXES = new HashMap<>();

    public static final String SVN_DAV_PROPERTY_PREFIX = "SD";
    public static final String SVN_CUSTOM_PROPERTY_PREFIX = "SC";
    public static final String SVN_SVN_PROPERTY_PREFIX = "SS";
    public static final String SVN_APACHE_PROPERTY_PREFIX = "SA";

    static {
        WELL_KNOWN_NS_PREFIXES.put(DAV.Element.DAV_NAMESPACE, DAV_NAMESPACE_PREFIX);
        WELL_KNOWN_NS_PREFIXES.put(DAV.Element.SVN_NAMESPACE, SVN_NAMESPACE_PREFIX);
        WELL_KNOWN_NS_PREFIXES.put(DAV.Element.SVN_DAV_PROPERTY_NAMESPACE, SVN_DAV_PROPERTY_PREFIX);
        WELL_KNOWN_NS_PREFIXES.put(DAV.Element.SVN_SVN_PROPERTY_NAMESPACE, SVN_SVN_PROPERTY_PREFIX);
        WELL_KNOWN_NS_PREFIXES.put(DAV.Element.SVN_CUSTOM_PROPERTY_NAMESPACE, SVN_CUSTOM_PROPERTY_PREFIX);
        WELL_KNOWN_NS_PREFIXES.put(DAV.Element.SVN_APACHE_PROPERTY_NAMESPACE, SVN_APACHE_PROPERTY_PREFIX);
    }

    private SvnHttpRepository repo;

    public URI getLocation() {
        return repo.getLocation();
    }

    public SvnHttpRepository getRepository() {
        return (SvnHttpRepository) repo;
    }
    private HttpClient client;

    private final CredentialsProvider credentialsProvider = new CredentialsProvider() {
        final Map<AuthScope, Credentials> scope2Credentials = new HashMap<>();

        @Override
        public void setCredentials(AuthScope as, Credentials c) {
            scope2Credentials.put(as, c);
        }

        @Override
        public Credentials getCredentials(final AuthScope as) {
            Credentials credentials = scope2Credentials.get(as);
            if (credentials != null) {
                return credentials;
            } else {
                final SvnCredentials c = repo.getCredentials();
                if (c.getAuthType() == SvnAuthType.SSH_PASSWORD || c.getAuthType() == SvnAuthType.SVN_PASSWORD) {
                    return new Credentials() {

                        @Override
                        public Principal getUserPrincipal() {
                            return new Principal() {

                                @Override
                                public String getName() {
                                    return c.getUserName();
                                }
                            };
                        }

                        @Override
                        public String getPassword() {
                            try {
                                return new String(c.getPassword(SvnPath.append(repo.getLocation().toString(), repo.getPath())));
                            } catch (RadixSvnException ex) {
                                return null;
                            }
                        }
                    };
                }
            }
            return null;
        }

        @Override
        public void clear() {
            scope2Credentials.clear();
        }
    };

    private HttpClient getConnection(SvnRepository repo) throws RadixSvnException {
        if (client == null) {
            HttpClientBuilder builder = HttpClients.custom();
            if ("https".equals(repo.getLocation().getScheme())) {
                builder.setSSLContext(makeSSLContext(repo));
            }
            builder.setDefaultCredentialsProvider(credentialsProvider);
            return client = builder.setMaxConnPerRoute(Integer.MAX_VALUE)
                    .setMaxConnTotal(Integer.MAX_VALUE)
                    .build();
        } else {
            return client;
        }
    }

    public boolean isAlive() {
        return true;
    }

    private HttpPost prepareRequest(final String method, String path) throws RadixSvnException {
        return prepareRequest(method, path, true);
    }

    private HttpPost prepareRequest(final String method, String path, boolean withHeaders) throws RadixSvnException {
        String url = getReuqestUrl(method, path);
        HttpPost request = new HttpPost(url) {

            @Override
            public String getMethod() {
                return method;
            }
        };
        if (withHeaders) {
            request.addHeader(DAV_HEADER, DEPTH_OPTION);
            request.addHeader(DAV_HEADER, MERGE_INFO_OPTION);
            request.addHeader(DAV_HEADER, LOG_REVPROPS_OPTION);
            request.addHeader("ContentType", "text/xml; charset=utf-8");
        }

        return request;
    }

    private String getReuqestUrl(final String method, String path) throws RadixSvnException {
        String url = repo.getLocation().toString();
        if (path != null && !path.isEmpty()) {
            if (path.startsWith("/")) {
                URI uri = repo.getLocation();
                try {
                    URI newUri = new URI(uri.getScheme(),
                            uri.getUserInfo(),
                            uri.getHost(),
                            uri.getPort(),
                            path,
                            uri.getQuery(),
                            uri.getFragment());
                    url = newUri.toString();
                } catch (URISyntaxException ex) {
                    throw new RadixSvnException(ex);
                }
            } else {
                url = repo.getRootUrl() + "/" + path;
            }
        }
        return url;
    }

    @Override
    public void open(SvnRepository repo) throws RadixSvnException {
        this.repo = (SvnHttpRepository) repo;
        try {
            while (true) {
                HttpUriRequest request = prepareRequest("OPTIONS", "");

                HttpResponse rs = getConnection(repo).execute(request);
                if (rs.getStatusLine().getStatusCode() == HttpURLConnection.HTTP_OK) {
                    readResponse(200, rs.getEntity(), null);
                    return;
                } else {
                    throw new RadixSvnException("Connection refused. Code: " + rs.getStatusLine().getStatusCode() + ": " + rs.getStatusLine().getReasonPhrase());
                }
            }
        } catch (IOException ex) {
            throw new RadixSvnException(ex);
        } finally {
            cleanupConnection();
        }
    }

    @Override
    public void authenticate(SvnRepository repository) throws RadixSvnException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void close() {
        if (client != null) {
            this.client.getConnectionManager().closeIdleConnections(10000, TimeUnit.MILLISECONDS);
            this.client.getConnectionManager().closeExpiredConnections();
            this.client.getConnectionManager().shutdown();
            this.client = null;
        }
    }

    public String getRepositoryRoot() {
        return null;
    }

    private static SSLContext makeSSLContext(SvnRepository repository) throws RadixSvnException {
        try {
            //org.radixware.kernel.common.svn.client.impl.KeyManager.getKeyManagers(repository); //
            SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
            final KeyManager[] kms = org.radixware.kernel.common.svn.client.impl.KeyManager.getKeyManagers(repository);//new KeyManager[]{new org.radixware.kernel.common.svn.client.impl.KeyManager(repository)};
            sslContext.init(kms, new TrustManager[]{new org.radixware.kernel.common.svn.client.impl.TrustManager(repository.getTrustManager())}, null);
            return sslContext;
        } catch (RadixSvnException ex) {
            throw ex;
        } catch (Throwable ex) {
            throw new RadixSvnException(ex);
        }
    }

    private void setDeptHeaders(HttpUriRequest request, String label, int depth) throws RadixSvnException {
        if (depth == DEPTH_ZERO) {
            request.addHeader(DEPTH_HEADER, "0");
        } else if (depth == DEPTH_ONE) {
            request.addHeader(DEPTH_HEADER, "1");
        } else if (depth == DEPTH_INFINITE) {
            request.addHeader(DEPTH_HEADER, "infinity");
        } else {
            throw new RadixSvnException("Invalid PROPFIND depth value: " + depth);
        }
        if (label != null) {
            request.addHeader(LABEL_HEADER, label);
        }
    }

    private HttpEntity getPropfindRequest(DAV.Element[] properties) {
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        openNamespaceDeclarationTag(null, "propfind", Collections.singleton(DAV.Element.DAV_NAMESPACE), null, null, sb, true);
        if (properties != null) {
            sb.append("<prop>\n");
            for (int i = 0; i < properties.length; i++) {
                openXMLTag(null, properties[i].property, true, "xmlns", properties[i].namespace, sb);
            }
            sb.append("</prop>\n");
        } else {
            sb.append("<allprop/>\n");
        }
        sb.append("</propfind>");
        return new StringEntity(sb.toString(), ContentType.TEXT_XML);
    }

    private static HttpEntity getLogRequest(long startRevision, long endRevision,
            boolean includeChangedPaths, boolean strictNodes, boolean includeMergedRevisions,
            String[] revPropNames, long limit, String[] paths) {
        StringBuilder xmlBuffer = new StringBuilder();
        addXmlHeader(xmlBuffer);
        openNamespaceDeclarationTag(SVN_NAMESPACE_PREFIX, "log-report",
                Collections.singleton(DAV.Element.SVN_NAMESPACE), WELL_KNOWN_NS_PREFIXES, null, xmlBuffer, true);
        if (startRevision >= 0) {
            openCDataTag(SVN_NAMESPACE_PREFIX, "start-revision",
                    String.valueOf(startRevision), xmlBuffer);
        }
        if (endRevision >= 0) {
            openCDataTag(SVN_NAMESPACE_PREFIX, "end-revision",
                    String.valueOf(endRevision), xmlBuffer);
        }
        if (limit > 0) {
            openCDataTag(SVN_NAMESPACE_PREFIX, "limit", String.valueOf(limit), xmlBuffer);
        }
        if (includeChangedPaths) {
            openXmlTag(SVN_NAMESPACE_PREFIX, "discover-changed-paths",
                    true, null, xmlBuffer);
        }
        if (strictNodes) {
            openXmlTag(SVN_NAMESPACE_PREFIX, "strict-node-history",
                    true, null, xmlBuffer);
        }
        if (includeMergedRevisions) {
            openXmlTag(SVN_NAMESPACE_PREFIX, "include-merged-revisions",
                    true, null, xmlBuffer);
        }
        if (revPropNames != null) {
            for (int i = 0; i < revPropNames.length; i++) {
                String revPropName = revPropNames[i];
                openCDataTag(SVN_NAMESPACE_PREFIX, "revprop", revPropName, xmlBuffer);
            }
        } else {
            openXmlTag(SVN_NAMESPACE_PREFIX, "all-revprops",
                    true, null, xmlBuffer);
        }

        for (int i = 0; i < paths.length; i++) {
            openCDataTag(SVN_NAMESPACE_PREFIX, "path", paths[i], xmlBuffer);
        }
        closeXmlTag(SVN_NAMESPACE_PREFIX, "log-report", xmlBuffer);
        return new StringEntity(xmlBuffer.toString(), ContentType.TEXT_XML);
    }

    static void openNamespaceDeclarationTag(String prefix, String header, Collection<String> namespaces, Map<String, String> prefixMap, Map<String, String> attrs, StringBuilder target, boolean addEOL) {
        target.append("<");
        if (prefix != null) {
            target.append(prefix);
            target.append(":");
        }
        target.append(header);
        if (namespaces != null && !namespaces.isEmpty()) {
            Collection<String> usedNamespaces = new ArrayList<>();
            for (Iterator<String> iterator = namespaces.iterator(); iterator.hasNext();) {
                Object item = iterator.next();
                String currentNamespace = null;
                if (item instanceof DAV.Element) {
                    DAV.Element currentElement = (DAV.Element) item;
                    currentNamespace = currentElement.namespace;
                } else if (item instanceof String) {
                    currentNamespace = (String) item;
                }
                if (currentNamespace != null && currentNamespace.length() > 0 && !usedNamespaces.contains(currentNamespace)) {
                    usedNamespaces.add(currentNamespace);
                    target.append(" xmlns");
                    if (prefixMap != null) {
                        target.append(":");
                        target.append(prefixMap.get(currentNamespace));
                    }
                    target.append("=\"");
                    target.append(currentNamespace);
                    target.append("\"");
                }
            }
            usedNamespaces.clear();
        }
        if (attrs != null && !attrs.isEmpty()) {
            for (Iterator<Map.Entry<String, String>> iterator = attrs.entrySet().iterator(); iterator.hasNext();) {
                Map.Entry<String, String> entry = iterator.next();
                String name = (String) entry.getKey();
                String value = (String) entry.getValue();
                target.append(" ");
                target.append(name);
                target.append("=\"");
                target.append(value);
                target.append("\"");
            }
        }
        target.append(">");
        if (addEOL) {
            target.append('\n');
        }
    }

    static void addXmlHeader(StringBuilder sb) {
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
    }

    static void openXMLTag(String prefix, String tagName, boolean close, String attr, String value, StringBuilder target) {
        Map<String, String> attrs = new HashMap<>();
        attrs.put(attr, value);
        openXmlTag(prefix, tagName, close, attrs, target);
    }

    static void openCDataTag(String prefix, String name, String data, StringBuilder target) {
        openXmlTag(prefix, name, false, null, target);
        target.append("<![CDATA[").append(data).append("]]>");
        closeXmlTag(prefix, name, target);
    }

    static void closeXmlTag(String prefix, String header, StringBuilder target) {
        target.append("</");
        if (prefix != null) {
            target.append(prefix);
            target.append(":");
        }
        target.append(header);
        target.append(">");
    }

    static void openXmlTag(String prefix, String tagName, boolean close, Map<String, String> attributes, StringBuilder target) {
        target.append("<");
        if (prefix != null) {
            target.append(prefix);
            target.append(":");
        }
        target.append(tagName);
        if (attributes != null && !attributes.isEmpty()) {
            for (Iterator iterator = attributes.entrySet().iterator(); iterator.hasNext();) {
                Map.Entry entry = (Map.Entry) iterator.next();
                String name = (String) entry.getKey();
                String value = (String) entry.getValue();
                target.append(" ");
                target.append(name);
                target.append("=\"");
                target.append(value);
                target.append("\"");
            }
            attributes.clear();
        }
        if (close) {
            target.append("/");
        }
        target.append(">");
        target.append("\n");
    }

    void execPropFindRequest(final String path, String label, int dept, final DAV.Element[] properties, final Map<String, DAV.Properties> result) throws RadixSvnException {
        try {
            HttpPost builder = prepareRequest("PROPFIND", path);
            setDeptHeaders(builder, label, dept);
            builder.setEntity(getPropfindRequest(properties));
            HttpResponse rs = getConnection(repo).execute(builder);
            processResponse(rs, new IPropAcceptor() {

                @Override
                public void accept(String relativePath, QName name, String value, List<QName> children) {
                    if (properties != null) {
                        for (DAV.Element e : properties) {
                            if (e.namespace.equals(name.getNamespaceURI()) && e.property.equals(name.getLocalPart())) {
                                DAV.Properties props = result.get(relativePath);
                                if (props == null) {
                                    props = new DAV.Properties(relativePath);
                                    result.put(relativePath, props);
                                }
                                props.setProperty(e, new DAV.Property(value));
                                if (children != null && !children.isEmpty()) {
                                    for (QName qname : children) {
                                        if ("collection".equals(qname.getLocalPart())) {
                                            props.setCollection(true);
                                        }
                                    }
                                }

                            }
                        }
                    } else {
                        DAV.Element e = new DAV.Element(name.getNamespaceURI(), name.getLocalPart());

                        DAV.Properties props = result.get(relativePath);
                        if (props == null) {
                            props = new DAV.Properties(relativePath);
                            result.put(relativePath, props);
                        }
                        props.setProperty(e, new DAV.Property(value));
                        if (children != null && !children.isEmpty()) {
                            for (QName qname : children) {
                                if ("collection".equals(qname.getLocalPart())) {
                                    props.setCollection(true);
                                }
                            }
                        }
                    }
                }
            });

        } catch (IOException ex) {
            Logger.getLogger(SvnHttpConnection.class
                    .getName()).log(Level.SEVERE, null, ex);
            throw new RadixSvnException(ex);
        } finally {
            cleanupConnection();
        }
    }

    public void get(final String path, final OutputStream out) throws RadixSvnException {
        try {
            HttpPost builder = prepareRequest("GET", path);
            HttpResponse rs = getConnection(repo).execute(builder);
            if (rs.getEntity() != null) {
                InputStream in = rs.getEntity().getContent();
                if (in != null) {
                    try {
                        FileUtils.copyStream(in, out);
                    } finally {
                        try {
                            in.close();

                        } catch (IOException ex) {
                        }
                    }
                }
            }

        } catch (IOException ex) {
            Logger.getLogger(SvnHttpConnection.class
                    .getName()).log(Level.SEVERE, null, ex);
            throw new RadixSvnException(ex);
        } finally {
            cleanupConnection();
        }
    }

    private void processResponse(HttpResponse rs, IPropAcceptor acceptor) throws RadixSvnException, IOException {
        if (rs.getStatusLine().getStatusCode() == 200 || rs.getStatusLine().getStatusCode() == 207) {
            readResponse(rs.getStatusLine().getStatusCode(), rs.getEntity(), acceptor);
        } else {
            throw new RadixSvnException("Server returned HTTP response code: " + rs.getStatusLine().getStatusCode() + " for URL: " + repo.getLocation().toString(), RadixSvnException.Type.IO, rs.getStatusLine().getStatusCode());
        }
    }

    interface IPropAcceptor {

        void accept(String relativePath, QName name, String value, List<QName> children);
    }

    private void readResponse(int code, HttpEntity response, final IPropAcceptor acceptor) throws IOException {
        if (code == 207) {
            try {
                SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
                class Element {

                    String qname;
                    StringBuilder content;
                    Attributes attributes;
                    Element parent;
                    List<Element> children;
                    String href;
                    Map<String, String> prefix2ns;

                    public List<QName> getChildNames() {
                        if (children == null) {
                            return null;
                        }
                        List<QName> list = new ArrayList<>();
                        for (Element qname : children) {
                            list.add(qname.getName());
                        }
                        return list;
                    }

                    public QName getName() {
                        return resolve(qname);
                    }

                    private String findNsByPrefix(String prefix) {
                        Element n = this;
                        while (n != null) {
                            if (n.prefix2ns != null) {
                                String ns = n.prefix2ns.get(prefix);
                                if (ns != null) {
                                    return ns;
                                }
                            }
                            n = n.parent;
                        }
                        return "";
                    }

                    private QName resolve(String name) {
                        int index = name.indexOf(":");
                        String prefix = "";
                        String local = name;
                        if (index > 0) {
                            prefix = name.substring(0, index);
                            local = name.substring(index + 1);
                        }
                        return new QName(findNsByPrefix(prefix), local);
                    }

                    public String getValue() {
                        if (children == null) {
                            return content == null ? "" : content.toString();
                        } else {
                            StringBuilder value = new StringBuilder();
                            for (Element e : children) {
                                if (e.content != null) {
                                    value.append(e.content);
                                }
                            }
                            return value.toString();
                        }
                    }

                    boolean isPropValueItem() {
                        return parent != null && parent.isPropValue();
                    }

                    boolean isPropValue() {
                        return parent != null && parent.isPropElement();
                    }

                    boolean isPropElement() {
                        if (qname.endsWith("prop")) {
                            return parent != null && parent.isPropStatElement();
                        } else {
                            return false;
                        }
                    }

                    boolean isPropStatElement() {
                        if (qname.endsWith("propstat")) {
                            return parent != null && parent.isResponseElement();
                        } else {
                            return false;
                        }
                    }

                    boolean isHrefElement() {
                        if (qname.endsWith("href")) {
                            return parent != null && parent.isResponseElement();
                        } else {
                            return false;
                        }
                    }

                    Element getResponseElement() {
                        Element p = this;
                        while (p != null) {
                            if (p.isResponseElement()) {
                                return p;
                            }
                            p = p.parent;
                        }
                        return null;
                    }

                    boolean isResponseElement() {
                        if (qname.endsWith("response")) {
                            return parent != null && parent.isMultistatusElement();
                        } else {
                            return false;
                        }
                    }

                    boolean isMultistatusElement() {
                        if (qname.endsWith("multistatus")) {
                            return parent == null;
                        } else {
                            return false;
                        }
                    }
                }

                final Stack<Element> elementStack = new Stack<>();
//                ByteArrayOutputStream out = new ByteArrayOutputStream();
//                FileUtils.copyStream(response.getContent(), out);
//                String string = new String(out.toByteArray());
//                System.out.println(string);
//                ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
                parser.parse(response.getContent(), new DefaultHandler() {

                    @Override
                    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                        Element parent = elementStack.isEmpty() ? null : elementStack.peek();
                        Element node = new Element();
                        node.qname = qName;
                        node.attributes = attributes;
                        node.parent = parent;
                        elementStack.push(node);
                        if (attributes != null) {
                            for (int i = 0; i < attributes.getLength(); i++) {
                                String name = attributes.getLocalName(i);
                                if (name.startsWith("xmlns")) {
                                    int indexOfColon = name.indexOf(":");
                                    String prefix = "";
                                    if (indexOfColon > 0) {
                                        prefix = name.substring(indexOfColon + 1);
                                    }
                                    String nameSpace = attributes.getValue(i);
                                    if (node.prefix2ns == null) {
                                        node.prefix2ns = new HashMap<>();
                                    }
                                    node.prefix2ns.put(prefix, nameSpace);
                                }
                            }
                        }
                    }

                    @Override
                    public void characters(char[] ch, int start, int length) throws SAXException {
                        Element current = elementStack.isEmpty() ? null : elementStack.peek();
                        if (current != null) {
                            if (current.content == null) {
                                current.content = new StringBuilder();
                            }
                            current.content.append(ch, start, length);
                        }
                    }

                    @Override
                    public void endElement(String uri, String localName, String qName) throws SAXException {
                        Element current = elementStack.isEmpty() ? null : elementStack.pop();
                        if (current != null && current.qname.equals(qName)) {
                            if (current.isPropValueItem()) {
                                Element propValue = current.parent;
                                if (propValue.children == null) {
                                    propValue.children = new LinkedList<>();
                                }
                                propValue.children.add(current);
                            } else if (current.isPropValue()) {
                                Element response = current.getResponseElement();
                                if (response != null && response.href != null) {
                                    acceptor.accept(response.href, current.getName(), current.getValue(), current.getChildNames());
                                }
                            } else if (current.isHrefElement()) {
                                Element response = current.getResponseElement();
                                if (response != null) {
                                    response.href = current.getValue();
                                }
                            }
                        } else {
                            throw new SAXException("Unexpected end of XML element: " + qName);
                        }
                    }

                });
            } catch (ParserConfigurationException | SAXException ex) {
                throw new IOException(ex);
            }
        }
    }

    public String checkout(String activityPath, String repositoryPath, String path, boolean allow404) throws RadixSvnException {
        StringBuilder request = new StringBuilder();
        Collection<String> namespaces = new LinkedList<>();
        namespaces.add(DAV.Element.DAV_NAMESPACE);

        addXmlHeader(request);
        openNamespaceDeclarationTag(DAV_NAMESPACE_PREFIX, "checkout", namespaces, WELL_KNOWN_NS_PREFIXES, null, request, false);
        openXmlTag(DAV_NAMESPACE_PREFIX, "activity-set", false, null, request);
        openCDataTag(DAV_NAMESPACE_PREFIX, "href", activityPath, request);
        closeXmlTag(DAV_NAMESPACE_PREFIX, "activity-set", request);
        closeXmlTag(DAV_NAMESPACE_PREFIX, "checkout", request);

        HttpPost checkout = prepareRequest("CHECKOUT", path);
        HttpEntity entity = new org.apache.http.entity.StringEntity(request.toString(), ContentType.TEXT_XML);
        checkout.setEntity(entity);

        try {
            HttpResponse rs = getConnection(repo).execute(checkout);
            Header header = rs.getFirstHeader(LOCATION_HEADER);
            if (header != null) {
                String location = header.getValue();
                if (!location.startsWith("/")) {
                    try {
                        URI uri = new URI(location);
                        location = uri.getPath();
                    } catch (URISyntaxException ex) {
                        throw new RadixSvnException(ex);
                    }
                }
                return location;
            }
        } catch (IOException ex) {
            throw new RadixSvnException(ex);
        } finally {
            cleanupConnection();
        }

        return null;
    }

    public String makeActivity() throws RadixSvnException {
        String url = null;

        String locationPath = SvnPath.uriEncode(getLocation().getPath());
        if (url == null) {
            url = getActivityCollectionURL(locationPath, false);
        }

        HttpPost checkout = prepareRequest("MKACTIVITY", SvnPath.append(url, UUID.randomUUID().toString()));
        String activityUrl = checkout.getRequestLine().getUri();
        try {
            HttpResponse rs = getConnection(repo).execute(checkout);
            if (rs.getStatusLine().getStatusCode() != 201) {
                url = getActivityCollectionURL(locationPath, true);
                checkout = prepareRequest("MKACTIVITY", SvnPath.append(url, UUID.randomUUID().toString()));
                activityUrl = checkout.getRequestLine().getUri();
                rs = getConnection(repo).execute(checkout);
                if (rs.getStatusLine().getStatusCode() != 201) {
                    throw new RadixSvnException("Unable to initialize tranzaction activity");
                }
            }
        } catch (IOException ex) {
            throw new RadixSvnException(ex);
        } finally {
            cleanupConnection();
        }
        return activityUrl;
    }
    private String activityCollectionUrl = null;
    private static final List<String> DAV_NSS = Arrays.asList(new String[]{DAV.Element.SVN_NAMESPACE, DAV.Element.DAV_NAMESPACE});

    private String getActivityCollectionURL(String path, boolean force) throws RadixSvnException {
        if (!force && activityCollectionUrl != null) {
            return activityCollectionUrl;
        }

        StringBuilder request = new StringBuilder();
        addXmlHeader(request);
        openNamespaceDeclarationTag(DAV_NAMESPACE_PREFIX, "options",
                DAV_NSS, WELL_KNOWN_NS_PREFIXES, null, request, false);
        openXmlTag(DAV_NAMESPACE_PREFIX, "activity-collection-set",
                true, null, request);
        closeXmlTag(DAV_NAMESPACE_PREFIX, "options", request);

        HttpPost checkout = prepareRequest("OPTIONS", path, true);
        HttpEntity entity = new org.apache.http.entity.StringEntity(request.toString(), ContentType.TEXT_XML);
        checkout.setEntity(entity);

        try {
            HttpResponse rs = getConnection(repo).execute(checkout);
            if (rs.getStatusLine().getStatusCode() != 200) {
                throw new RadixSvnException("Connection refused");
            }

            SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
            final StringBuilder url = new StringBuilder();
            final boolean doParse[] = new boolean[]{false};
            parser.parse(rs.getEntity().getContent(), new DefaultHandler() {

                @Override
                public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                    if (qName.endsWith(":href")) {
                        doParse[0] = true;
                    }
                    super.startElement(uri, localName, qName, attributes);
                }

                @Override
                public void characters(char[] ch, int start, int length) throws SAXException {
                    if (doParse[0]) {
                        url.append(ch, start, length);
                    }
                }

                @Override
                public void endElement(String uri, String localName, String qName) throws SAXException {
                    if (qName.endsWith(":href")) {
                        doParse[0] = false;
                    }
                }

            });

            activityCollectionUrl = url.length() == 0 ? null : url.toString();
        } catch (IOException ex) {
            throw new RadixSvnException(ex);
        } catch (SAXException | ParserConfigurationException ex) {
            throw new RadixSvnException(ex);
        } finally {
            cleanupConnection();
        }

        return activityCollectionUrl;
    }

    public String makeCollection(String path) throws RadixSvnException {
        HttpPost mkColl = prepareRequest("MKCOL", path);
        String collectionUrl = mkColl.getRequestLine().getUri();
        try {
            HttpResponse rs = getConnection(repo).execute(mkColl);
            if (rs.getStatusLine().getStatusCode() != 201) {
                throw new RadixSvnException("Unable to initialize collection");
            }
        } catch (IOException ex) {
            throw new RadixSvnException(ex);
        } finally {
            cleanupConnection();
        }
        return collectionUrl;
    }

    public void copy(String src, String dst, int depth) throws RadixSvnException {
        HttpPost post = prepareRequest("COPY", src, false);
        post.addHeader(DESTINATION_HEADER, dst);
        post.addHeader(DEPTH_HEADER, depth > 0 ? "infinity" : "0");

        try {
            HttpResponse rs = getConnection(repo).execute(post);
            if (rs.getStatusLine().getStatusCode() >= 300) {
                throw new RadixSvnException("Unable to copy from " + src + " to " + dst);
            }
        } catch (IOException ex) {
            throw new RadixSvnException("Unable to copy from " + src + " to " + dst, ex);
        } finally {
            cleanupConnection();
        }
    }

    public void deleteUrl(String url) throws RadixSvnException {

        HttpPost post = new HttpPost(url) {

            @Override
            public String getMethod() {
                return "DELETE";
            }
        };

        try {
            HttpResponse rs = getConnection(repo).execute(post);
            if (rs.getStatusLine().getStatusCode() != 204) {
                throw new RadixSvnException("Unable to delete " + url);
            }
        } catch (IOException ex) {
            throw new RadixSvnException("Unable to delete " + url, ex);
        } finally {
            cleanupConnection();
        }
    }

    public void delete(String repositoryPath, String path, long revision) throws RadixSvnException {
        HttpPost post = prepareRequest("DELETE", path, false);
        if (revision >= 0) {
            post.addHeader(SVN_VERSION_NAME_HEADER, Long.toString(revision));
        }
        post.addHeader(DEPTH_HEADER, "infinity");

        try {
            HttpResponse rs = getConnection(repo).execute(post);
            if (rs.getStatusLine().getStatusCode() != 204) {
                throw new RadixSvnException("Unable to delete " + path);
            }
        } catch (IOException ex) {
            throw new RadixSvnException("Unable to delete " + path, ex);
        } finally {
            cleanupConnection();
        }
    }

    private void cleanupConnection() {
        if (client != null) {
            client.getConnectionManager().shutdown();
            client = null;
        }
    }

    private static StringBuilder generateMergeRequest(String path, String activityURL) {
        StringBuilder xmlBuffer = new StringBuilder();
        Collection<String> namespaces = new LinkedList<>();
        namespaces.add(DAV.Element.DAV_NAMESPACE);
        addXmlHeader(xmlBuffer);
        openNamespaceDeclarationTag(DAV_NAMESPACE_PREFIX, "merge", namespaces,
                WELL_KNOWN_NS_PREFIXES, null, xmlBuffer, true);
        openXmlTag(DAV_NAMESPACE_PREFIX, "source", false, null,
                xmlBuffer);
        openCDataTag(DAV_NAMESPACE_PREFIX, "href", activityURL, xmlBuffer);
        closeXmlTag(DAV_NAMESPACE_PREFIX, "source", xmlBuffer);
        openXmlTag(DAV_NAMESPACE_PREFIX, "no-auto-merge",
                true, null, xmlBuffer);
        openXmlTag(DAV_NAMESPACE_PREFIX, "no-checkout",
                true, null, xmlBuffer);
        openXmlTag(DAV_NAMESPACE_PREFIX, "prop", false, null,
                xmlBuffer);
        openXmlTag(DAV_NAMESPACE_PREFIX, "checked-in",
                true, null, xmlBuffer);
        openXmlTag(DAV_NAMESPACE_PREFIX, "version-name",
                true, null, xmlBuffer);
        openXmlTag(DAV_NAMESPACE_PREFIX, "resourcetype",
                true, null, xmlBuffer);
        openXmlTag(DAV_NAMESPACE_PREFIX, "creationdate",
                true, null, xmlBuffer);
        openXmlTag(DAV_NAMESPACE_PREFIX, "creator-displayname",
                true, null, xmlBuffer);
        closeXmlTag(DAV_NAMESPACE_PREFIX, "prop", xmlBuffer);

        closeXmlTag(DAV_NAMESPACE_PREFIX, "merge", xmlBuffer);
        return xmlBuffer;

    }

    public SvnCommitSummary merge(String activityURL, boolean response) throws RadixSvnException {
        try {
            final String locationPath = SvnPath.uriEncode(getLocation().getPath());

            final StringBuilder mergeRequest = generateMergeRequest(locationPath, activityURL);
            final HttpPost merge = prepareRequest("MERGE", null, false);
            final HttpEntity entity = new org.apache.http.entity.StringEntity(mergeRequest.toString(), ContentType.TEXT_XML);
            merge.setEntity(entity);

            if (!response) {
                String value = "";
                if (!response) {
                    value += "no-merge-response";
                }
                merge.addHeader(SVN_OPTIONS_HEADER, value);
            }

            try {
                final HttpResponse rs = getConnection(repo).execute(merge);

                final SvnCommitSummary summary = new SvnCommitSummary();
                if (rs.getStatusLine().getStatusCode() == 200) {
                    try {

                        SAXParser parser = SAXParserFactory.newInstance().newSAXParser();

                        final boolean inBaseline[] = new boolean[]{false};
                        final int[] mode = new int[]{0};
                        final StringBuilder data = new StringBuilder();
                        parser.parse(rs.getEntity().getContent(), new DefaultHandler() {

                            @Override
                            public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                                if (qName.endsWith(":baseline")) {
                                    inBaseline[0] = true;
                                }
                                if (qName.endsWith(":propstat")) {
                                    inBaseline[0] = false;
                                }

                                data.setLength(0);
                            }

                            @Override
                            public void characters(char[] ch, int start, int length) throws SAXException {
                                data.append(ch, start, length);
                            }

                            @Override
                            public void endElement(String uri, String localName, String qName) throws SAXException {
                                if (inBaseline[0]) {
                                    if (qName.endsWith(":version-name")) {
                                        summary.revision = Long.parseLong(data.toString());
                                    } else if (qName.endsWith(":creator-displayname")) {
                                        summary.author = data.toString();
                                    } else if (qName.endsWith(":creationdate")) {
                                        summary.date = SvnUtil.parseDate(data.toString());
                                    }
                                }
                                if (qName.endsWith(":post-commit-err")) {
                                    summary.errorMessage = "Repository post-commit hook failed";
                                }
                            }
                        });

                        return summary;
                    } catch (IOException ex) {
                        throw new RadixSvnException("Unable to merge changes", ex);
                    } catch (SAXException ex) {
                        throw new RadixSvnException("Unable to merge changes", ex);
                    } catch (ParserConfigurationException ex) {
                        throw new RadixSvnException("Unable to merge changes", ex);
                    }
                } else {
                    throw new RadixSvnException("Unable to merge changes");
                }
            } catch (IOException ex) {
                throw new RadixSvnException("Unable to merge changes", ex);
            }
        } finally {
            cleanupConnection();
        }
    }

    public long log(String path, long startRevision, long endRevision,
            boolean includeChangedPaths, boolean strictNodes, boolean includeMergedRevisions,
            String[] revPropNames, long limit, String[] paths, final ISvnLogHandler logHandler) throws RadixSvnException {
        try {
            long count = 0;

            class Data {

                String message, author;
                Date date;
                long revision;
                Set<String> changedPathes;

                String flag;
                boolean in;

                void reset() {
                    message = null;
                    author = null;
                    date = null;
                    revision = -1;
                    changedPathes = null;
                    flag = null;
                    in = false;
                }

                void accept(String data) {
                    if (flag == null) {
                        return;
                    }
                    if (flag.endsWith(":version-name")) {
                        revision = Long.parseLong(data);
                    } else if (flag.endsWith(":date")) {
                        date = SvnUtil.parseDate(data);
                    } else if (flag.endsWith(":creator-displayname")) {
                        author = data;
                    }
                }
            }
            final Data data = new Data();
            report(path, getLogRequest(startRevision, endRevision, includeChangedPaths, strictNodes, includeMergedRevisions, revPropNames, limit, paths), new DefaultHandler() {

                @Override
                public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                    if (qName.endsWith(":log-item")) {
                        data.reset();
                        data.in = true;
                    } else {
                        if (data.in) {
                            data.flag = qName;
                        }
                    }
                }

                @Override
                public void characters(char[] ch, int start, int length) throws SAXException {
                    if (data.in && data.flag != null) {
                        data.accept(String.valueOf(ch, start, length));
                    }
                }

                @Override
                public void endElement(String uri, String localName, String qName) throws SAXException {
                    if (qName.endsWith(":log-item")) {
                        try {
                            SvnProperties props = null;
                            try {
                                props = repo.getRevisionProperties(data.revision, null);
                            } catch (RadixSvnException ex) {
                            }
                            SvnProperties.Value messageValue = props == null ? null : props.get("svn:log");
                            String message = messageValue == null ? "" : messageValue.getValue();
                            logHandler.accept(new SvnLogEntry(message, data.author, data.date, data.revision, data.changedPathes));
                        } catch (RadixSvnException ex) {
                            throw new SAXException(ex);
                        }
                        data.reset();
                    } else {
                        data.flag = null;
                    }
                }

            });
            return count;
        } finally {
            cleanupConnection();
        }
    }

    public void report(String path, HttpEntity request, DefaultHandler handler) throws RadixSvnException {
        HttpPost builder = prepareRequest("REPORT", path);
        builder.setEntity(request);
        builder.addHeader("Accept-Encoding", "svndiff1;q=0.9,svndiff;q=0.8");
        try {
            try {
                HttpResponse rs = getConnection(repo).execute(builder);
                if (rs.getStatusLine().getStatusCode() != 200) {
                    throw new RadixSvnException("Connection refused");
                }
                SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
                parser.parse(rs.getEntity().getContent(), handler);
            } catch (IOException | ParserConfigurationException ex) {
                throw new RadixSvnException(ex);
            } catch (SAXException ex) {
                if (ex.getCause() instanceof RadixSvnException) {
                    throw (RadixSvnException) ex.getCause();
                }
                throw new RadixSvnException(ex);
            }

        } finally {
            cleanupConnection();
        }
    }

    private static final long MAX_IN_MEMORY_BUFFER_SIZE = 1024 * 1024 * 10;

    public void putDiff(String repositoryPath, String path, InputStream data, long size, String baseChecksum, String textChecksum) throws RadixSvnException {
        try {
            HttpPost post = prepareRequest("PUT", path, false);
            post.addHeader(CONTENT_TYPE_HEADER, SVNDIFF_MIME_TYPE);

            if (baseChecksum != null) {
                post.addHeader(BASE_MD5, baseChecksum);
            }
            if (textChecksum != null) {
                post.addHeader(TEXT_MD5, textChecksum);
            }

            final HttpEntity entity;
            File tmp = null;
            if (size < MAX_IN_MEMORY_BUFFER_SIZE) {
                final ByteArrayOutputStream out = new ByteArrayOutputStream();
                try {
                    FileUtils.copyStream(data, out);
                    entity = new ByteArrayEntity(out.toByteArray());
                } finally {
                    out.close();
                }
            } else {
                tmp = File.createTempFile("svn", "tmp");
                final FileOutputStream out = new FileOutputStream(tmp);

                try {
                    FileUtils.copyStream(data, out);
                } finally {
                    out.close();
                }
                entity = new FileEntity(tmp);
            }
            post.setEntity(entity);
            try {
                final HttpResponse rs = getConnection(repo).execute(post);
                if (rs.getStatusLine().getStatusCode() == 204) {//no changes
                    return;
                }
                if (rs.getStatusLine().getStatusCode() != 201) {
                    throw new RadixSvnException("Unable to put diff window");
                }
            } catch (IOException ex) {
                throw new RadixSvnException("Unable to merge changes", ex);
            } finally {
                if (tmp != null) {
                    FileUtils.deleteFile(tmp);
                }
            }
        } catch (IOException ex) {
            throw new RadixSvnException(ex);
        } finally {
            cleanupConnection();
        }
    }
}
