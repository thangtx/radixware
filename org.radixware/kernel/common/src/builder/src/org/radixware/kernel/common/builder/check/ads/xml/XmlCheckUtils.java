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
import java.util.Map;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.builder.check.ads.CheckUtils;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.check.ProblemAnnotationFactory;
import org.radixware.kernel.common.check.RadixProblem;
import org.radixware.kernel.common.check.XsdCheckHistory;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.DefinitionSearcher;
import org.radixware.kernel.common.defs.HierarchyWalker;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsXlsxReportInfo;
import org.radixware.kernel.common.defs.ads.common.AdsUtils;
import org.radixware.kernel.common.defs.ads.common.AdsVisitorProvider.AdsTopLevelDefVisitorProvider;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumDef;
import org.radixware.kernel.common.defs.ads.module.AdsSearcher;
import org.radixware.kernel.common.defs.ads.module.ModuleDefinitions;
import org.radixware.kernel.common.defs.ads.xml.AbstractXmlDefinition;
import org.radixware.kernel.common.defs.ads.xml.AdsXmlSchemeDef;
import org.radixware.kernel.common.defs.ads.xml.IXmlDefinition;
import org.radixware.kernel.common.defs.localization.ILocalizingBundleDef;
import org.radixware.kernel.common.enums.EXmlSchemaLinkMode;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.repository.ads.AdsSegment;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.XPathUtils;
import org.radixware.kernel.common.utils.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

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
                    
                    @Override
                    public boolean isClassContainer(Class c) {
                        if (ILocalizingBundleDef.class.isAssignableFrom(c)) {
                            return false;
                        }
                        return super.isClassContainer(c);
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

    static void checkLinkedSchemas(AdsXmlSchemeDef scheme, IProblemHandler problemHandler) {
        if (scheme.getLinkedSchemas() != null) {
            for (Map.Entry<AdsXmlSchemeDef, EXmlSchemaLinkMode> entry : scheme.getLinkedSchemas().entrySet()) {
                if (!scheme.isImportExist(entry.getKey().getId()) && entry.getValue() == EXmlSchemaLinkMode.IMPORT) {
                    problemHandler.accept(RadixProblem.Factory.newWarning(scheme, MessageFormat.format(
                            "Linked schema {0} with linked mode \"Import\" is not imported in schema {1}",
                            entry.getKey().getTargetNamespace(),
                            scheme.getTargetNamespace())));
                }
            }
        }

        List<IXmlDefinition> importedSchemas = scheme.getImportedDefinitions();
        for (IXmlDefinition importedSchema : importedSchemas) {
            if (importedSchema instanceof AdsXmlSchemeDef) {
                AdsXmlSchemeDef tmp = (AdsXmlSchemeDef) importedSchema;
                if (scheme.getLinkedSchemasIds() != null && !scheme.getLinkedSchemasIds().contains(tmp.getId())) {
                    problemHandler.accept(RadixProblem.Factory.newWarning(scheme, MessageFormat.format(
                            "Imported schema {0} is not linked to schema {1}",
                            tmp.getTargetNamespace(),
                            scheme.getTargetNamespace())));
                }
            }
        }
    }

    static void checkNamespacePrefix(final AdsXmlSchemeDef scheme, IProblemHandler problemHandler) {
        if (scheme.getNamespacePrefix() != null) {
            final String prefix = scheme.getNamespacePrefix();

            AdsXmlSchemeDef duplicatePrefixDef = (AdsXmlSchemeDef) scheme.getBranch().find(new VisitorProvider() {
                @Override
                public boolean isTarget(RadixObject radixObject) {
                    return (radixObject instanceof AdsXmlSchemeDef) && ((AdsXmlSchemeDef) radixObject).getId() != scheme.getId() && prefix.equals(((AdsXmlSchemeDef) radixObject).getNamespacePrefix());
                }

                @Override
                public boolean isContainer(RadixObject radixObject) {
                    return radixObject instanceof Branch || radixObject instanceof Layer || radixObject instanceof AdsSegment || radixObject instanceof Module || radixObject instanceof ModuleDefinitions;
                }

            });

            if (duplicatePrefixDef != null) {
                problemHandler.accept(RadixProblem.Factory.newError(scheme,
                        "Namespace prefix \"" + prefix + "\" for xsd scheme \"" + scheme.getQualifiedName()
                        + "\" is duplicated in \"" + duplicatePrefixDef.getQualifiedName() + "\" scheme"));
            }
        }
    }

    static void checkUndocumentedNodes(final AdsXmlSchemeDef scheme, IProblemHandler problemHandler) {
        XmlObject obj = scheme.getXmlDocument();
        if (obj == null) {
            obj = scheme.getXmlContent();
        }

        if (obj != null) {
            Element root = XmlUtils.findFirstElement(obj.getDomNode());
            if (root != null) {
                checkNode(root, scheme, problemHandler);
            }
        }
    }

    private static void checkNode(final Element node, final AdsXmlSchemeDef scheme, final IProblemHandler problemHandler) {
        List<String> documentedNodes = scheme.getDocumentedNodes();
        String xPath = XPathUtils.getXPath(node);
        if (XPathUtils.isElementNeedsDoc(node) && !documentedNodes.contains(xPath)) {
            problemHandler.accept(RadixProblem.Factory.newWarning(scheme,
                    "Element " + node.getAttribute("name") + " is not documented in scheme " + scheme.getName(),
                    ProblemAnnotationFactory.newXmlDocumentationAnnotation(xPath)));
        }

        for (Element child : XmlUtils.getChildElements(node)) {
            checkNode(child, scheme, problemHandler);
        }
    }

    static void checkSchemeForCyclicImport(final AdsXmlSchemeDef scheme, final IProblemHandler problemHandler, XsdCheckHistory xsdCheckHistory) {        
        String cyclicImportPath = xsdCheckHistory.getCyclicImportPath(scheme.getTargetNamespace());
        if (cyclicImportPath == null) {
            cyclicImportPath = scheme.getCyclicImportPath();
            xsdCheckHistory.addCyclicImportPath(scheme.getTargetNamespace(), cyclicImportPath);
        }
        
        if (!cyclicImportPath.isEmpty()) {
            String message = "Cyclic import found in schema " + scheme.getQualifiedName() + ": " + cyclicImportPath;
            problemHandler.accept(RadixProblem.Factory.newError(scheme, message));
        }
    }
}
