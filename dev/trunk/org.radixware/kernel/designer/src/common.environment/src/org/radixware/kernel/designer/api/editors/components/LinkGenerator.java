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
package org.radixware.kernel.designer.api.editors.components;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.SearchResult;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodThrowsList;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.MethodParameter;
import org.radixware.kernel.common.defs.ads.type.AdsDefinitionType;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.designer.common.dialogs.names.NamingService;

public final class LinkGenerator {

    final static LinkGenerator LINK_GENERATOR = new LinkGenerator();

    public static LinkGenerator getDefault() {
        return LINK_GENERATOR;
    }

    private LinkGenerator() {
    }

    private Collection<LinkedString.Item> createLink(AdsTypeDeclaration typeDeclaration, Definition context, boolean space) {

        final SearchResult<AdsType> resolve = typeDeclaration == null ? SearchResult.<AdsType>empty() : typeDeclaration.resolve(context);
        RadixObject ref = null;
        if (!resolve.isEmpty()) {
            final AdsType type = resolve.get();

            if (type instanceof AdsDefinitionType) {
                ref = ((AdsDefinitionType) type).getSource();
            }
        }
        final List<LinkedString.Item> items = new ArrayList<>();
        final boolean isDepracate = ref instanceof Definition && ((Definition) ref).isDeprecated();
        items.add(new LinkedString.Item(new LinkedString.TextDecoration(false, false, isDepracate), ref, (space ? " " : "") + typeDeclaration.getRowName(context)));

        boolean first = true;
        if (typeDeclaration.isGeneric()) {
            items.add(new LinkedString.Item(LinkedString.TextDecoration.GENERAL, null, "<", true));

            for (final AdsTypeDeclaration.TypeArgument argument : typeDeclaration.getGenericArguments().getArgumentList()) {
                if (first) {
                    first = false;
                } else {
                    items.add(new LinkedString.Item(LinkedString.TextDecoration.GENERAL, null, ", "));
                }
                items.addAll(createLink(argument.getType(), context, false));
            }

            items.add(new LinkedString.Item(LinkedString.TextDecoration.GENERAL, null, ">", true));
        }
        return items;
    }

    LinkedString getLinkedString(RadixObject object) {
        if (object instanceof AdsPropertyDef) {
            return getLinkedString((AdsPropertyDef) object);
        } else if (object instanceof AdsMethodDef) {
            return getLinkedString((AdsMethodDef) object);
        }

        final LinkedString string = new LinkedString();
        string.add(new LinkedString.Item(LinkedString.TextDecoration.GENERAL, null, NamingService.getDefault().getHtmlSignature(object)));
        return string;
    }

    LinkedString getLinkedString(AdsPropertyDef prop) {
        final LinkedString string = new LinkedString();

        final StringBuilder sb = new StringBuilder();
        NamingService.getDefault().addAccessFlags(prop.getAccessFlags(), sb);
        string.add(new LinkedString.Item(LinkedString.TextDecoration.GENERAL, null, sb.toString()));
        string.add(createLink(prop.getValue().getType(), prop, true));
        string.add(new LinkedString.Item(new LinkedString.TextDecoration(true, false, prop.isDeprecated()), null, " " + prop.getName()));

        return string;
    }

    LinkedString getLinkedString(AdsMethodDef method) {
        final LinkedString string = new LinkedString();

        final StringBuilder sb = new StringBuilder();
        NamingService.getDefault().addAccessFlags(method.getAccessFlags(), sb);
        string.add(new LinkedString.Item(LinkedString.TextDecoration.GENERAL, null, sb.toString()));

        if (method.getProfile().getReturnValue() == null) {
            string.add(new LinkedString.Item(LinkedString.TextDecoration.GENERAL, null, " void"));
        } else {
            string.add(createLink(method.getProfile().getReturnValue().getType(), method, true));
        }

        string.add(new LinkedString.Item(new LinkedString.TextDecoration(true, false, method.isDeprecated()), null, " " + method.getName()));
        string.add(new LinkedString.Item(LinkedString.TextDecoration.GENERAL, null, "("));

        boolean first = true;
        for (final MethodParameter methodParameter : method.getProfile().getParametersList()) {
            if (first) {
                first = false;
            } else {
                string.add(new LinkedString.Item(LinkedString.TextDecoration.GENERAL, null, ", "));
            }

            string.add(createLink(methodParameter.getType(), method, false));
            string.add(new LinkedString.Item(LinkedString.TextDecoration.GENERAL, null, " " + methodParameter.getName()));
        }

        string.add(new LinkedString.Item(LinkedString.TextDecoration.GENERAL, null, ")"));

        first = true;
        for (final AdsMethodThrowsList.ThrowsListItem item : method.getProfile().getThrowsList()) {
            if (first) {
                first = false;
                string.add(new LinkedString.Item(LinkedString.TextDecoration.GENERAL, null, " throws "));
            } else {
                string.add(new LinkedString.Item(LinkedString.TextDecoration.GENERAL, null, ", "));
            }
            string.add(createLink(item.getException(), method, false));
        }
        return string;
    }
}
