/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.radixware.kernel.designer.common.dialogs.scmlnb.finder;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.openide.util.lookup.Lookups;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.xml.AdsXmlSchemeDef;
import org.radixware.kernel.designer.common.general.editors.EditorsManager;
import org.radixware.kernel.designer.common.general.editors.OpenInfo;

/**
 *
 * @author ilevandovskiy
 */
public class XmlSchemeFinder implements IFinder {

    private final Pattern pattern;

    public XmlSchemeFinder(final Pattern pattern) {
        this.pattern = pattern;
    }

    @Override
    public List<IOccurrence> list(RadixObject radixObject) {
        if (radixObject == null || !(radixObject instanceof AdsXmlSchemeDef)) {
            return Collections.emptyList();
        }

        AdsXmlSchemeDef schema = (AdsXmlSchemeDef) radixObject;
        XmlObject xmlObject = schema.getXmlDocument();
        if (xmlObject == null) {
            xmlObject = schema.getXmlContent();
        }

        final List<IOccurrence> occurences = new LinkedList<IOccurrence>();

        if (xmlObject != null) {
            XmlOptions options = new XmlOptions();
            // options.setSavePrettyPrint();
            options.setSaveNamespacesFirst();

            options.put(XmlOptions.LOAD_ADDITIONAL_NAMESPACES);
            options.put(XmlOptions.CHARACTER_ENCODING);
            options.put(XmlOptions.DOCUMENT_TYPE);

            String xmlObjectStr = xmlObject.xmlText(options);

            Matcher matcher = pattern.matcher(xmlObjectStr);
            while (matcher.find()) {
                final FindUtils.ContainingLineInfo lineInfo = FindUtils.getContainingLine(xmlObjectStr, matcher.start());
                occurences.add(new XmlSchemeOccurence(schema, lineInfo, pattern, matcher.start(), rowNumber(xmlObjectStr, matcher.start())));
            }
        } else {
            return Collections.emptyList();
        }
        return occurences;
    }

    private int rowNumber(String str, int barrier) {
        return str.substring(0, barrier).split("\n").length;
    }

    @Override
    public boolean accept(RadixObject radixObject) {
        return radixObject instanceof AdsXmlSchemeDef;
    }

    private static class XmlSchemeOccurence implements IOccurrence {

        private final AdsXmlSchemeDef def;
        private final FindUtils.ContainingLineInfo line;
        private final Pattern pattern;
        private final int matcherStart;
        private final int rowNumber;

        public XmlSchemeOccurence(AdsXmlSchemeDef def, FindUtils.ContainingLineInfo containigLine, Pattern pattern, int matcherStart, int rowNumber) {
            this.def = def;
            this.line = containigLine;
            this.pattern = pattern;
            this.matcherStart = matcherStart;
            this.rowNumber = rowNumber;
        }

        @Override
        public String getDisplayText() {
            return FindUtils.html("Row number " + rowNumber + ": " + FindUtils.markPatternBold(line.getLine().replaceAll(">", "&gt;").replaceAll("<", "&lt;"), pattern));
        }

        @Override
        public RadixObject getOwnerObject() {
            return def;
        }

        @Override
        public void goToObject() {
            EditorsManager.getDefault().open(def, new OpenInfo(def, Lookups.fixed(new XmlLocation(matcherStart))));
        }
    }
}
