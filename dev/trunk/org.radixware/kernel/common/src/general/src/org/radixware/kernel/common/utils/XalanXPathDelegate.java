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

package org.radixware.kernel.common.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.transform.TransformerException;
import org.w3c.dom.Node;
import org.apache.xml.utils.PrefixResolver;
import org.apache.xml.utils.PrefixResolverWithDefaultNS;
import org.apache.xmlbeans.impl.store.PathDelegate;
import org.apache.xpath.XPath;
import org.apache.xpath.XPathContext;
import org.apache.xpath.objects.XObject;
import org.w3c.dom.NodeList;

public class XalanXPathDelegate
        implements PathDelegate.SelectPathInterface {

    private final String contextVar;
    private final String path;
    private final String defaultNS;
    private final Map namespaceMap;

    /**
     * Construct given an XPath expression string.
     * @param path The XPath expression
     * @param contextVar The name of the context variable
     * @param namespaceMap a map of prefix/uri bindings for NS support
     * @param defaultNS the uri for the default element NS, if any
     */
    public XalanXPathDelegate(String path, String contextVar,
            Map namespaceMap, String defaultNS) {
        this.path = path;
        this.contextVar = contextVar;
        this.defaultNS = defaultNS;
        this.namespaceMap = new HashMap(namespaceMap != null ? namespaceMap : Collections.emptyMap());
    }

    @Override
    public List selectPath(Object node) {
        return xalanImpl(node);
    }

    public List xalanImpl(Object node) {
        XPathContext xpathContext = new XPathContext(false);
        XPath xpath;
        try {
            final PrefixResolver resolver = new PrefixResolverWithDefaultNS(namespaceMap, defaultNS);
            xpath = new XPath(path, null, resolver, XPath.SELECT, null);
            final XObject xpathResult = xpath.execute(xpathContext, (Node) node, resolver);
            xpathContext.setNamespaceContext(resolver);
            final NodeList nodeList =  xpathResult.nodelist();
            final List result = new ArrayList();
            for(int i = 0; i < nodeList.getLength(); i++) {
                result.add(nodeList.item(i));
            }
            return result;
        } catch (TransformerException ex) {
            throw new RuntimeException(ex);//NOPMD
        }
    }
}
