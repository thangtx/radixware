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

package org.radixware.kernel.common.builder.check.ads.xml;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import org.radixware.kernel.common.builder.check.ads.CheckUtils;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.check.RadixProblem;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.DefinitionSearcher;
import org.radixware.kernel.common.defs.HierarchyWalker;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.common.AdsUtils;
import org.radixware.kernel.common.defs.ads.common.AdsVisitorProvider.AdsTopLevelDefVisitorProvider;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumDef;
import org.radixware.kernel.common.defs.ads.module.AdsSearcher;
import org.radixware.kernel.common.defs.ads.xml.AdsXmlSchemeDef;
import org.radixware.kernel.common.defs.ads.xml.IXmlDefinition;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.XmlUtils;


class XmlCheckUtils {

    static void checkNs(final IXmlDefinition scheme, final IProblemHandler problemHandler) {
        if (!((AdsDefinition) scheme).isInBranch()) {
            problemHandler.accept(RadixProblem.Factory.newError((AdsDefinition) scheme, "Definition is already deleted"));
            return;
        }
        Layer l = ((AdsDefinition) scheme).getModule().getSegment().getLayer();
        final String ns = scheme.getTargetNamespace();
        final Id id = ((AdsDefinition) scheme).getId();

        Layer.HierarchyWalker.walk(l, new Layer.HierarchyWalker.Acceptor<Object>() {
            @Override
            public void accept(HierarchyWalker.Controller<Object> controller, Layer l) {
                l.visit(new IVisitor() {
                    @Override
                    public void accept(RadixObject radixObject) {
                        if (radixObject instanceof IXmlDefinition) {
                            IXmlDefinition xml = (IXmlDefinition) radixObject;
                            if (xml.getTargetNamespace() != null && xml.getTargetNamespace().equals(ns)) {
                                if (xml instanceof Definition && !((Definition) xml).getId().equals(id)) {
                                    problemHandler.accept(RadixProblem.Factory.newError((AdsDefinition) scheme, MessageFormat.format("Xml namespace duplication with {0}", radixObject.getQualifiedName())));
                                }
                            }
                        }

                    }
                }, new AdsTopLevelDefVisitorProvider() {
                    @Override
                    public boolean isTarget(RadixObject radixObject) {
                        return radixObject instanceof IXmlDefinition;
                    }
                });
            }
        });

    }

    static void checkImportsAndUsages(IXmlDefinition scheme, IProblemHandler problemHandler) {
        List<String> importedNamespaces = scheme.getImportedNamespaces();
        AdsDefinition schemeDef = (AdsDefinition) scheme;
        AdsSearcher.Factory.XmlDefinitionSearcher searcher = AdsSearcher.Factory.newXmlDefinitionSearcher((AdsDefinition) scheme);
        for (String ns : importedNamespaces) {
            IXmlDefinition def = searcher.findByNs(ns).get();
            if (def == null) {
                problemHandler.accept(RadixProblem.Factory.newError((AdsDefinition) scheme, MessageFormat.format("Imported xml schema {0} can not be found", ns)));
            } else {
                AdsUtils.checkAccessibility(schemeDef, (AdsDefinition) def, false, problemHandler);
                CheckUtils.checkExportedApiDatails(schemeDef, (AdsDefinition) def, problemHandler);
            }
        }
        DefinitionSearcher<AdsEnumDef> searcher1 = AdsSearcher.Factory.newAdsEnumSearcher(((AdsDefinition) scheme).getModule());
        List<Id> enums = new ArrayList<>();
        XmlUtils.collectUsedEnumIds(scheme.getXmlContent(), enums);
        for (Id id : enums) {
            AdsEnumDef en = searcher1.findById(id).get();
            if (en == null) {
                problemHandler.accept(RadixProblem.Factory.newError((AdsDefinition) scheme, "Enumeration not found: #" + id));
            } else {
                AdsUtils.checkAccessibility(schemeDef, en, false, problemHandler);
                CheckUtils.checkExportedApiDatails(schemeDef, en, problemHandler);
            }
        }
    }
}
