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

package org.radixware.kernel.common.defs.ads.module;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


public class APISupport {

    public static class Header {

        public final String version;
        public final Id[] ids;

        Header(String version, Id[] ids) {
            this.version = version;
            this.ids = ids;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (this == obj) {
                return true;
            }
            if (obj.getClass() == getClass()) {
                Header h = (Header) obj;
                if (!Utils.equals(version, h.version)) {
                    return false;
                }
                return Arrays.equals(ids, h.ids);
            } else {
                return false;
            }

        }
    }
    private static final String URI = "http://schemas.radixware.org/adsdef.xsd";
    private static final String NAME = "API";
    private static final String NAME_IDS = "Ids";
    private static final String NAME_VERSION = "Version";

    private static class InfomationFound extends RuntimeException {
    }

    public static final Header readHeader(InputStream stream) throws IOException {
        try {
            final SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
            final List<Id> ids = new LinkedList<Id>();
            final String[] version = new String[]{null};

            try {
                parser.parse(stream, new DefaultHandler() {
                    @Override
                    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                        if (/*URI.equals(uri) && */NAME.equals(qName) || qName.endsWith(NAME)) {
                            int len = attributes.getLength();
                            boolean hasVersion = false;
                            boolean hasIds = false;
                            for (int i = 0; i < len; i++) {
                                String name = attributes.getLocalName(i);
                                if (NAME_IDS.equals(name)) {
                                    String value = attributes.getValue(i);
                                    if (value != null) {
                                        String[] idsArr = value.split(" ");
                                        for (String s : idsArr) {
                                            if (!s.isEmpty()) {
                                                ids.add(Id.Factory.loadFrom(s));
                                            }
                                        }
                                    }
                                    hasIds = true;
                                } else if (NAME_VERSION.equals(name)) {
                                    version[0] = attributes.getValue(i);
                                    hasVersion = true;
                                }
                                if (hasVersion && hasIds) {
                                    break;
                                }
                            }
                            throw new InfomationFound();
                        }


                    }
                });
            } catch (InfomationFound e) {
                return new Header(version[0], ids.toArray(new Id[ids.size()]));
            }

            return null;
        } catch (ParserConfigurationException | SAXException ex) {
            return null;
        }
    }
}
