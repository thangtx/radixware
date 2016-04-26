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

package org.radixware.kernel.explorer.editors.jmleditor.completer;

import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QFont;
import com.trolltech.qt.gui.QFontMetrics;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.IFilter;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.common.defs.ads.type.AdsClassType;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.ads.type.XmlType;
import org.radixware.kernel.common.defs.ads.xml.IXmlDefinition;
import org.radixware.kernel.common.jml.JmlTagInvocation;
import org.radixware.kernel.common.jml.JmlTagTypeDeclaration;
import org.radixware.kernel.common.scml.Scml.Tag;


public class QuickCompleterGetter implements Callable<Map<String, HtmlCompletionItem>> {

    private final CompleterProcessor completerProcessor;
    private final Tag tag;
    private int itemWidth = 0;
    private final QFont font;

    public QuickCompleterGetter(final CompleterProcessor completerProcessor, Tag tag, QFont font) {
        this.completerProcessor = completerProcessor;
        this.tag = tag;
        this.font = font;
    }

    @Override
    public Map<String, HtmlCompletionItem> call() throws Exception {
        Map<String, HtmlCompletionItem> cl = quickReplas(tag);

        if (completerProcessor != null) {
            QApplication.postEvent(completerProcessor, new CompleterProcessor.ExposeQuickCompleterEvent(cl, itemWidth));
        }
        return cl;
    }

    private Map<String, HtmlCompletionItem> quickReplas(Tag tag) {
        if (tag instanceof JmlTagTypeDeclaration) {
            JmlTagTypeDeclaration decl = (JmlTagTypeDeclaration) tag;
            AdsType type = decl.getType().resolve(decl.getDefinition()).get();
            if (type instanceof AdsClassType) {
                AdsClassDef clazz = ((AdsClassType) type).getSource();
                Collection<AdsMethodDef> methods = clazz.getConstructors();
                if (methods.isEmpty()) {
                    return null;
                } else {
                    return createComplList(methods);
                }
            } else if (type instanceof XmlType) {
                XmlType xType = (XmlType) type;
                AdsDefinition def = xType.getSource();
                if (def instanceof IXmlDefinition) {
                    Collection<String> typeNames = ((IXmlDefinition) def).getSchemaTypeList();
                    if (typeNames.isEmpty()) {
                        return null;
                    } else {
                        return createComplList(typeNames, xType);
                    }
                }
            }
        } else if (tag instanceof JmlTagInvocation) {
            JmlTagInvocation inv = (JmlTagInvocation) tag;
            Definition def = inv.resolve(inv.getOwnerJml().getOwnerDef());
            if (def instanceof AdsMethodDef) {
                AdsMethodDef method = (AdsMethodDef) def;
                AdsClassDef clazz = method.getOwnerClass();
                if (clazz != null) {
                    if (method.isConstructor()) {
                        Collection<AdsMethodDef> methods = clazz.getConstructors();
                        if (methods.isEmpty()) {
                            return null;
                        } else {
                            return createComplList(methods);
                        }
                    } else {
                        final String name = method.getName();
                        List<AdsMethodDef> methods = clazz.getMethods().get(EScope.ALL, new IFilter<AdsMethodDef>() {

                            @Override
                            public boolean isTarget(AdsMethodDef radixObject) {
                                return radixObject.getName().equals(name);
                            }
                        });
                        if (methods.isEmpty()) {
                            return null;
                        } else {
                            return createComplList(methods);
                        }
                    }
                }
            }
        }
        return null;
    }

    private Map<String, HtmlCompletionItem> createComplList(Collection<AdsMethodDef> meths) {
        LinkedHashMap<String, HtmlCompletionItem> cl = new LinkedHashMap<>();
        List<HtmlCompletionItem> htmlCompletionItemList = new ArrayList<>();
        itemWidth = 0;
         QFontMetrics fontMetrics = new QFontMetrics(font);
         QFontMetrics boldMetrics = new QFontMetrics(font);
        for (AdsMethodDef m : meths) {
            SuggestCompletionItem complItem = new SuggestCompletionItem(m, null);
            font.setBold(true);            
            font.setBold(false);
            HtmlCompletionItem htmlitem = new HtmlCompletionItem(complItem, fontMetrics, boldMetrics,complItem.isDeprecated());
            htmlCompletionItemList.add(htmlitem);
            if (htmlitem.getLenght() > itemWidth) {
                itemWidth = htmlitem.getLenght();
            }
            //cl.put(HtmlCompletionItem.changeColor(complItem.getLeadDisplayText()),complItem);
        }
        //completerSize=completerSize>1000? 1000 : completerSize;
        for (HtmlCompletionItem item : htmlCompletionItemList) {
            cl.put(item.getPlainText(), item);
        }
        return cl;
    }

    private Map<String, HtmlCompletionItem> createComplList(Collection<String> typeNames, XmlType def) {
        LinkedHashMap<String, HtmlCompletionItem> cl = new LinkedHashMap<>();
        List<HtmlCompletionItem> htmlCompletionItemList = new ArrayList<>();
        itemWidth = 0;
        QFontMetrics fontMetrics = new QFontMetrics(font);
        QFontMetrics boldMetrics = new QFontMetrics(font);
        for (String type : typeNames) {
            SuggestCompletionItem complItem = new SuggestCompletionItem(def, type);
            
            font.setBold(true);            
            font.setBold(false);
            HtmlCompletionItem htmlitem = new HtmlCompletionItem(complItem, type, fontMetrics, boldMetrics,complItem.isDeprecated());
            htmlCompletionItemList.add(htmlitem);
            if (htmlitem.getLenght() > itemWidth) {
                itemWidth = htmlitem.getLenght();
            }
            //cl.put(HtmlCompletionItem.changeColor(complItem.getLeadDisplayText()),complItem);
        }
        //completerSize=completerSize>1000? 1000 : completerSize;
        for (HtmlCompletionItem item : htmlCompletionItemList) {
            cl.put(item.getPlainText()/*
                     * item.getText(completerSize)
                     */, item);
        }

        /*
         * for (String type:typeNames ){ SuggestCompletionItem complItem=new
         * SuggestCompletionItem(def,type); //HtmlCompletionItem item=new
         * HtmlCompletionItem(complItem,null);
         * cl.put(HtmlCompletionItem.changeColor(complItem.getLeadDisplayText()),complItem);
         * }
         */
        return cl;
    }

    public int getCompleterSize() {
        return itemWidth;
    }
}
