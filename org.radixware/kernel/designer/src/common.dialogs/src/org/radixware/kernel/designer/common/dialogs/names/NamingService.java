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

package org.radixware.kernel.designer.common.dialogs.names;

import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodParameters;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodThrowsList;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.MethodParameter;
import org.radixware.kernel.common.defs.ads.type.AdsAccessFlags;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;


public final class NamingService {
    
    final static NamingService NAMING_SERVICE = new NamingService();

    public static NamingService getDefault() {
        return NAMING_SERVICE;
    }

    private NamingService() {}
    
    public String getHtmlSignature(RadixObject object) {
        if (object instanceof MethodParameter) {
            final MethodParameter parameter = (MethodParameter) object;
            final StringBuilder sb = new StringBuilder();
            sb.append("<html><body>");
            sb.append(parameter.getType().getHtmlName(object, false));
            sb.append(" <b>");
            sb.append(parameter.getName());
            sb.append("</b>");
            sb.append("</html></body>");
            return sb.toString();
        } else if (object instanceof AdsMethodThrowsList.ThrowsListItem) {
            final AdsMethodThrowsList.ThrowsListItem item = (AdsMethodThrowsList.ThrowsListItem) object;
            final StringBuilder sb = new StringBuilder();
            sb.append(item.getException().getHtmlName(object, false));
            return sb.toString();
        } else if (object instanceof AdsMethodDef) {
            final AdsMethodDef method = (AdsMethodDef) object;
            final StringBuilder sb = new StringBuilder();
            sb.append("<html><body>");
            methodProfileHtml(method, sb);
            sb.append("</html></body>");
            return sb.toString();
        } else if (object instanceof AdsPropertyDef) {
            final AdsPropertyDef property = (AdsPropertyDef) object;
            final StringBuilder sb = new StringBuilder();
            sb.append("<html><body>");
            addAccessFlags(property.getAccessFlags(), sb);
            sb.append(property.getValue().getType().getHtmlName(property, false));
            sb.append(" ");
            addNameBold(property, sb);
            sb.append("</html></body>");
            return sb.toString();
        } else if (object instanceof AdsDefinition) {
            final StringBuilder sb = new StringBuilder();
            addName((AdsDefinition) object, sb);
        }

        return object.getName();
    }

    public String getHtmlName(RadixObject object) {
        return getHtmlName(object, null, null);
    }

    public String getHtmlName(RadixObject object, String prefix, String postfix) {
        final StringBuilder sb = new StringBuilder();
        sb.append("<html><body>");

        if (prefix != null) {
            sb.append(prefix);
        }

        if (object instanceof AdsDefinition) {
            addName((AdsDefinition) object, sb);

            if (object instanceof AdsClassDef) {
                final AdsClassDef cls = (AdsClassDef) object;
                final AdsTypeDeclaration.TypeArguments typeArguments = cls.getTypeArguments();
                if (!typeArguments.isEmpty()) {
                    sb.append("<");
                    boolean first = true;
                    for (final AdsTypeDeclaration.TypeArgument argument : typeArguments.getArgumentList()) {
                        if (first) {
                            first = false;
                        } else {
                            sb.append(", ");
                        }
                        sb.append(argument.getName(object));
                    }
                    sb.append(">");
                }
            }
        } else {
            sb.append(object.getName());
        }
        if (postfix != null) {
            sb.append(postfix);
        }
        sb.append("</body></html>");
        return sb.toString();
    }

    public String getName(RadixObject object) {
        if (object instanceof AdsClassDef) {
            final AdsClassDef cls = (AdsClassDef) object;
            final AdsTypeDeclaration.TypeArguments typeArguments = cls.getTypeArguments();
            if (typeArguments.isEmpty()) {
                return object.getName();
            }

            final StringBuilder sb = new StringBuilder();
            sb.append(cls.getName()).append("<");
            boolean first = true;
            for (final AdsTypeDeclaration.TypeArgument argument : typeArguments.getArgumentList()) {
                if (first) {
                    first = false;
                } else {
                    sb.append(", ");
                }
                sb.append(argument.getName(object));
            }
            sb.append(">");
            return sb.toString();
        }
        return object.getName();
    }

    public void addAccessFlags(AdsAccessFlags flags, StringBuilder profile) {
        if (flags.isProtected()) {
            profile.append("protected ");
        }

        if (flags.isPrivate()) {
            profile.append("private ");
        }

        if (flags.isPublic()) {
            profile.append("public ");
        }

        if (flags.isAbstract()) {
            profile.append("abstract ");
        }

        if (flags.isFinal()) {
            profile.append("final ");
        }

        if (flags.isStatic()) {
            profile.append("static ");
        }
    }
    
    private void methodProfileHtml(AdsMethodDef methd, StringBuilder profile) {

        addAccessFlags(methd.getAccessFlags(), profile);

        if (methd.getProfile().getReturnValue() != null) {
            profile.append(methd.getProfile().getReturnValue().getType().getHtmlName(methd, false));
            profile.append(' ');
        }

        addNameBold(methd, profile);
        profile.append('(');
        AdsMethodParameters.printProfileHtml(methd, false, methd.getProfile().getParametersList(), profile);
        profile.append(')');

        methd.getProfile().getThrowsList().printSignatureHtml(profile);
    }

    private void addName(AdsDefinition definition, StringBuilder profile) {
        boolean deprecated = definition.isDeprecated();
        profile.append(deprecated ? "<s>" : "").append(definition.getName()).append(deprecated ? "</s>" : "");
    }

    private void addNameBold(AdsDefinition definition, StringBuilder profile) {
        boolean deprecated = definition.isDeprecated();
        profile.append(deprecated ? "<s>" : "").append("<b>").append(definition.getName()).append("</b>").append(deprecated ? "</s>" : "");
    }
}
