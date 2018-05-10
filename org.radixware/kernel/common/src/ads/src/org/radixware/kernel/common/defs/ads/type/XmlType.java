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
package org.radixware.kernel.common.defs.ads.type;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.check.XsdCheckHistory;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.msdl.AdsMsdlSchemeDef;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.xml.XBeansType;
import org.radixware.kernel.common.defs.ads.src.xml.XBeansTypeSystem;
import org.radixware.kernel.common.defs.ads.xml.AbstractXmlDefinition;
import org.radixware.kernel.common.defs.ads.xml.AdsXmlSchemeDef;
import org.radixware.kernel.common.defs.ads.xml.IXmlDefinition;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.utils.Utils;

public class XmlType extends AdsDefinitionType {

    public static final class Factory {

        public static final XmlType newInstance(AdsXmlSchemeDef scheme, String suffix) {
            if (scheme == null) {
                return getDefault();
            } else {
                return new XmlType(scheme, suffix);
            }
        }

        public static final XmlType newInstance(AdsMsdlSchemeDef scheme, String suffix) {
            if (scheme == null) {
                return getDefault();
            } else {
                return new XmlType(scheme, suffix);
            }
        }

        public static final XmlType getDefault() {
            return defaultInstance;
        }
    }
    private static final XmlType defaultInstance = new XmlType();

    private XmlType() {
        super(null);
        this.suffix = null;
    }

    private XmlType(AdsXmlSchemeDef source, String suffix) {
        super(source);
        this.suffix = suffix;
    }

    private XmlType(AdsMsdlSchemeDef source, String suffix) {
        super(source);
        this.suffix = suffix;
    }
    private String suffix;

    private String getName(RadixObject context, boolean qualified) {
        if (source == null) {
            return "Xml";
        } else {
            StringBuilder builder = new StringBuilder(100);
            builder.append(qualified ? super.getQualifiedName(context) : super.getName());
            if (suffix != null && !suffix.isEmpty()) {
                builder.append(":");
                builder.append(suffix);
            }
            return builder.toString();
        }
    }

    @Override
    public String getName() {
        return getName(null, false);
    }

    @Override
    public String getQualifiedName(RadixObject context) {
        return getName(context, true);
    }
    private static final char[][] XML_BEANS_PACKAGE_NAME = new char[][]{"org".toCharArray(), "apache".toCharArray(), "xmlbeans".toCharArray()};
    private static final char[] XML_OBJECT_CLASS_NAME = "XmlObject".toCharArray();

    @Override
    public TypeJavaSourceSupport getJavaSourceSupport() {
        return new TypeJavaSourceSupport(this) {
            @Override
            public char[][] getPackageNameComponents(UsagePurpose env, boolean isHumanReadable) {
                if (source == null) {
                    return XML_BEANS_PACKAGE_NAME;
                }
                String packageName = ((IXmlDefinition) source).getJavaPackageName(isHumanReadable);

                String[] components = packageName.split("\\.");
                char[][] result = new char[components.length][];
                for (int i = 0; i < result.length; i++) {
                    result[i] = components[i].toCharArray();
                }
                return result;
            }

            @Override
            public char[] getLocalTypeName(UsagePurpose env, boolean isHumanReadable) {
                if (source == null) {
                    return XML_OBJECT_CLASS_NAME;
                }
                if (source instanceof AbstractXmlDefinition) {
                    List<String> path = new ArrayList<>();
                    XBeansTypeSystem sts = ((AbstractXmlDefinition) source).getSchemaTypeSystem();
                    XBeansType type = sts == null ? null : sts.findTypeOrInterface(suffix, path);
                    if (type != null) {
                        StringBuilder suffixBuilder = new StringBuilder();
                        boolean first = true;
                        for (String s : path) {
                            if (first) {
                                first = false;
                            } else {
                                suffixBuilder.append('.');
                            }
                            suffixBuilder.append(s);
                        }
                        return suffixBuilder.toString().toCharArray();
                    }
                }
                return suffix == null ? JavaSourceSupport.DEFAULT_PACKAGE_NAME : suffix.toCharArray();
            }
        };
    }

    public String getSuffix() {
        return suffix;
    }

    @Override
    protected void check(RadixObject referenceContext, ERuntimeEnvironmentType env, IProblemHandler problemHandler, Map<Object, Object> checkHistory) {
        super.check(referenceContext, env, problemHandler);
        if (source != null) {
            ERuntimeEnvironmentType se = getSource().getUsageEnvironment();
            switch (env) {
                case EXPLORER:
                    if (se == ERuntimeEnvironmentType.SERVER) {
                        error(referenceContext, problemHandler, MessageFormat.format("Reference to server side xml scheme {0} is not allowed from client side context", getSource().getQualifiedName()));
                    }

                    break;
                case SERVER:
                    if (se == ERuntimeEnvironmentType.EXPLORER) {
                        error(referenceContext, problemHandler, MessageFormat.format("Reference to client side xml scheme {0} is not allowed from server side context", getSource().getQualifiedName()));
                    }

                    break;
                case COMMON:
                    if (se == ERuntimeEnvironmentType.EXPLORER) {
                        error(referenceContext, problemHandler, MessageFormat.format("Reference to client side xml scheme {0} is not allowed from common context", getSource().getQualifiedName()));
                    } else if (se == ERuntimeEnvironmentType.SERVER) {
                        error(referenceContext, problemHandler, MessageFormat.format("Reference to server side xml scheme {0} is not allowed from common context", getSource().getQualifiedName()));
                    }

                    break;
            }

            AbstractXmlDefinition xmlSource = (AbstractXmlDefinition) source;
            XsdCheckHistory xsdCheckHistory = XsdCheckHistory.getOrCreate(checkHistory);
            String cyclicImportPath = xsdCheckHistory == null ? null : xsdCheckHistory.getCyclicImportPath(xmlSource.getTargetNamespace());
            if (cyclicImportPath == null) {
                cyclicImportPath = xmlSource.getCyclicImportPath();
                if (xsdCheckHistory != null) {
                    xsdCheckHistory.addCyclicImportPath(xmlSource.getTargetNamespace(), cyclicImportPath);
                }
            }

            if (!cyclicImportPath.isEmpty()) {
                error(referenceContext, problemHandler, MessageFormat.format("Type {0} can not be found because scheme {1} contains cyclic import: {2}", suffix, getSource().getQualifiedName(), cyclicImportPath));
                return;
            }
        }
        if (suffix != null) {
            XBeansType xb = getXmlType();
            if (xb == null) {
                error(referenceContext, problemHandler, MessageFormat.format("Type {0} can not be found in referenced xml definition", suffix));
            }
        }
    }

    @Override
    protected void check(RadixObject referenceContext, ERuntimeEnvironmentType env, IProblemHandler problemHandler) {
        check(referenceContext, env, problemHandler, null);
    }

    private XBeansType iface = null;
    private boolean not_found = false;

    public XBeansType getXmlType() {
        if (iface == null) {
            if (suffix == null) {
                return null;
            } else {
                if (source == null) {
                    return null;
                } else {
                    if (not_found) {
                        return null;
                    }
                    XBeansTypeSystem ts = ((AbstractXmlDefinition) source).getSchemaTypeSystem();
                    if (ts == null) {
                        return null;
                    }
                    iface = ts.findTypeOrInterface(suffix);
                    if (iface == null) {
                        not_found = true;
                    }
                }
            }
        }
        return iface;
    }

    @Override
    public String getToolTip() {
        if (source == null) {
            return "<html><b>Base xml type reference<b><br>equals to usage of <br>org.apache.xmlbeans.XmlObject</html>";
        } else {
            if (suffix == null) {
                return source.getToolTip();
            } else {
                final StringBuilder b = new StringBuilder();
                b.append("<html>")
                        .append("Reference to xml type <b>")
                        .append(suffix)
                        .append("<p><b>")
                        .append("Declaring Schema: ")
                        .append(source.getQualifiedName())
                        .append("</html>");
                return b.toString();
            }
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (super.equals(obj)) {
            if (!(obj instanceof XmlType)) {
                return false;
            }
            return Utils.equals(this.suffix, ((XmlType) obj).suffix);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 73 * hash + (this.suffix != null ? this.suffix.hashCode() : 0);
        return hash;
    }

    @Override
    public AdsDefinition getSource() {
        return (AdsDefinition) super.getSource();
    }
}
