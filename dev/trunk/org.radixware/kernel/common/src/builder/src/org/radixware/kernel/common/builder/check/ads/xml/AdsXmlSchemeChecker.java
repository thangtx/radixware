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
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.check.RadixProblem;
import org.radixware.kernel.common.defs.ads.xml.AdsXmlSchemeDef;
import org.radixware.kernel.common.builder.check.ads.AdsDefinitionChecker;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.builder.check.common.RadixObjectCheckerRegistration;
import org.radixware.kernel.common.check.ProblemAnnotationFactory;

@RadixObjectCheckerRegistration
public class AdsXmlSchemeChecker extends AdsDefinitionChecker<AdsXmlSchemeDef> {

    @Override
    public Class<? extends RadixObject> getSupportedClass() {
        return AdsXmlSchemeDef.class;
    }

    @Override
    public void check(AdsXmlSchemeDef scheme, IProblemHandler problemHandler) {
        super.check(scheme, problemHandler);
        XmlCheckUtils.checkNs(scheme, problemHandler);
        if (scheme.isTransparent()) {
            if (scheme.getXmlContent() == null) {
                problemHandler.accept(RadixProblem.Factory.newError(scheme, MessageFormat.format("Published xml schema {0} can not be found", scheme.getTargetNamespace())));
            }
        } else {
            XmlCheckUtils.checkImportsAndUsages(scheme, problemHandler);
        }
        List<XmlError> list = new LinkedList<>();

        XmlObject doc = scheme.getXmlDocument();
        if (doc != null) {
            String text = doc.xmlText();
            try {
                final XmlObject obj = XmlObject.Factory.parse(text, new XmlOptions().setLoadLineNumbers());
                obj.validate(new XmlOptions().setErrorListener(list));
            } catch (XmlException ex) {
                Logger.getLogger(AdsXmlSchemeChecker.class.getName()).log(Level.FINE, ex.getMessage(), ex);
            }
            for (XmlError e : list) {
                problemHandler.accept(RadixProblem.Factory.newError(scheme, e.getMessage(), new ProblemAnnotationFactory.TextPositionAnnotation(e.getLine(), e.getColumn())));
            }
        } else {
            problemHandler.accept(RadixProblem.Factory.newError(scheme, "Can not find XML content"));
        }
    }
}
