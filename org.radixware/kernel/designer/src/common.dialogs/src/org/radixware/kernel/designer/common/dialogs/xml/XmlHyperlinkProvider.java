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

package org.radixware.kernel.designer.common.dialogs.xml;

import java.util.Collection;
import java.util.EnumSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import org.netbeans.lib.editor.hyperlink.spi.HyperlinkProviderExt;
import org.netbeans.lib.editor.hyperlink.spi.HyperlinkType;
import org.netbeans.modules.editor.NbEditorDocument;
import org.netbeans.modules.editor.NbEditorUtilities;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.module.AdsSearcher;
import org.radixware.kernel.common.defs.ads.xml.AdsXmlSchemeDef;
import org.radixware.kernel.common.defs.ads.xml.IXmlDefinition;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.repository.dds.DdsScript;
import org.radixware.kernel.common.scml.Scml;
import org.radixware.kernel.designer.common.dialogs.scmlnb.ScmlDocument;
import org.radixware.kernel.designer.common.general.editors.EditorsManager;
import org.radixware.kernel.designer.common.general.filesystem.RadixFileUtil;

/**
 * Provides tag's GoTo support
 *
 */
public class XmlHyperlinkProvider implements HyperlinkProviderExt {

    private static final Pattern xmlPattern = Pattern.compile("\\\"http://schemas.*?\\\"");
    private static final Pattern scmlPattern = Pattern.compile("http://schemas[-a-zA-Z0-9.+&@#/%?=~_|!:,;]*[-a-zA-Z]");
    private int startOffset;
    private int endOffset;

    public XmlHyperlinkProvider() {
    }

    @Override
    public boolean isHyperlinkPoint(Document doc, int offset, HyperlinkType type) {
        return getUrl(doc, offset) != null;
    }

    private String getUrl(Document doc, int offset) {
        try {
            int startLine = offset;
            int stopLine = offset;
            while (stopLine < doc.getLength()) {
                String text = doc.getText(stopLine++, 1);
                if ("\r\n".contains(text)) {
                    break;
                }
            }

            while (startLine > 0) {
                String text = doc.getText(startLine--, 1);
                if ("\r\n".contains(text)) {
                    break;
                }
            }
            final String text = doc.getText(startLine, stopLine - startLine);
            Matcher matcher = null;
            int quotes = 0;
            String mimeType = NbEditorUtilities.getMimeType(doc);
            if ("text/xml".equals(mimeType)){
                matcher = xmlPattern.matcher(text);
                quotes = 1; 
            } else {
                matcher  = scmlPattern.matcher(text);
            }

            
            while (matcher.find()) {
                int start = matcher.start();
                int stop = matcher.end();

                if (startLine + start <= offset && offset <= startLine + stop) {
                    startOffset = startLine + start + quotes;
                    endOffset = startLine + stop - quotes;
                    return doc.getText(startOffset, endOffset - startOffset);
                }
            }
        } catch (BadLocationException ex) {
            Logger.getLogger(XmlHyperlinkProvider.class.getName()).log(Level.INFO, null, ex);
        }

        return null;
    }

    @Override
    public int[] getHyperlinkSpan(Document doc, int offset, HyperlinkType type) {
        return new int[]{startOffset, endOffset};
    }

    @Override
    public void performClickAction(Document doc, int offset, HyperlinkType type) {
        final String url = getUrl(doc, offset);
        final IXmlDefinition scheme = findScheme(doc, url);
        
        if (scheme instanceof RadixObject) {
            EditorsManager.getDefault().open((RadixObject)scheme);
        }
    }

    private IXmlDefinition findScheme(Document doc, final String url) {
        
        if (url == null) {
            return null;
        }
        Branch branch = null;
        final String namespace = url;
        
        if (doc instanceof ScmlDocument){
                ScmlDocument scmlDocument = (ScmlDocument) doc;
                Scml scml = scmlDocument.getScml();
                branch = scml.getBranch();  
        } else{
                String mimeType = NbEditorUtilities.getMimeType(doc);
                if("text/xml".equals(mimeType)){
                    final AdsXmlSchemeDef  scheme = (AdsXmlSchemeDef) doc.getProperty(AdsXmlSchemeDef.class);
                    if (scheme == null) {
                        return null;
                    }
                    branch = scheme.getBranch();
                } else {
                    if ("text/x-sql".equals(mimeType) || "text/plain+xml".equals(mimeType)){
                        FileObject fileObject = NbEditorUtilities.getFileObject(doc);
                        if (fileObject == null) {
                            return null;
                        }

                        RadixObject radixObject = RadixFileUtil.findRadixObject(fileObject);
                        if (radixObject == null) {
                            return null;
                        }
                        branch = radixObject.getBranch();
                    }
                }
            }
        
        if (branch == null) {
            return null;
        }
        
        return (IXmlDefinition) branch.find(new VisitorProvider() {
            @Override
            public boolean isTarget(RadixObject radixObject) {
                return (radixObject instanceof IXmlDefinition) && namespace.equals(((IXmlDefinition) radixObject).getTargetNamespace());
            }
        });
        
    }

    @Override
    public Set<HyperlinkType> getSupportedHyperlinkTypes() {
        return EnumSet.of(HyperlinkType.GO_TO_DECLARATION);
    }

    @Override
    public String getTooltipText(Document doc, int offset, HyperlinkType type) {
        final String url = getUrl(doc, offset);
        final IXmlDefinition scheme = findScheme(doc, url);
        
        if (scheme instanceof Definition) {
            return ((Definition)scheme).getToolTip();
        }
        
        return null;
    }
}
