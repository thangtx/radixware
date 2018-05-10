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
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.Callable;
import org.radixware.kernel.common.builder.completion.CompletionProviderFactoryManager;
import org.radixware.kernel.common.client.trace.ClientTracer;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.AdsLibUserFuncWrapper;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsSystemMethodDef;
import org.radixware.kernel.common.defs.ads.userfunc.AdsUserFuncDef;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.jml.Jml;
import org.radixware.kernel.common.scml.CompletionProviderFactory;
import org.radixware.kernel.common.scml.Scml;
import org.radixware.kernel.common.scml.ScmlCompletionProvider.CompletionItem;
import org.radixware.kernel.common.scml.ScmlCompletionProvider.CompletionRequestor;
import org.radixware.kernel.common.types.Id;

class CompleterGetter implements Callable<Map<String, HtmlCompletionItem>> {

    private static final int ITEM_CNT_TO_CALC_COMPLETER_WIDTH = 10;
    private static final int PROCESS_ITEMS_RATE = 250;

    private final CompleterProcessor completerProcessor;
    private final ClientTracer trace;
    private Jml jml;
    private int scmlItemIndex;
    private int scmlItemOffset;
    private final QFont font;
    private Scml.Item prevComplItem = null;
    private int itemWidth = CompleterProcessor.INITIAL_COMPLETER_ITEM_SIZE;
    //private String prefix;

    CompleterGetter(final CompleterProcessor completerProcessor, Jml jml, int info[], /*int caretOffset,*/ QFont font, ClientTracer trace) {
        this.completerProcessor = completerProcessor;
        this.jml = jml;
        this.font = font;
        scmlItemIndex = info[0];
        scmlItemOffset = info[1];
        this.trace = trace;
        //this.prefix=prefix;
    }

    private Scml.Item getPrevComplItem(RadixObjects<Scml.Item> items) {
        if (0 < scmlItemIndex && items.get(scmlItemIndex) instanceof Scml.Text) {
            final String text = ((Scml.Text) items.get(scmlItemIndex)).getText();
            final int offsetPos =  scmlItemOffset >= 1 ? scmlItemOffset - 1 : 0;
            final String textBeforeOffset = text.substring(0, offsetPos);
            if (!textBeforeOffset.contains(";")) {
                final String textAfterOffset = text.substring(offsetPos);
                if (!textAfterOffset.isEmpty() && (textAfterOffset.charAt(0) == '.' || textAfterOffset.charAt(0) == ':')) {
                    return items.get(scmlItemIndex - 1);
                }
            }
        }
        return null;
    }

    @Override
    public Map<String, HtmlCompletionItem> call() throws Exception {
        final long t0 = System.currentTimeMillis();
        List<CompletionItem> resultItems = new ArrayList<>();
        RadixObjects<Scml.Item> items = jml.getItems();
        if (scmlItemIndex >= 0 && items.size() > scmlItemIndex) {
            Scml.Item item = items.get(scmlItemIndex);
            if (item instanceof Scml.Tag) {
                if (scmlItemOffset == 0) {
                    if (scmlItemIndex > 0) {
                        Scml.Item prev = items.get(scmlItemIndex - 1);
                        if (prev instanceof Scml.Text) {
                            item = prev;
                            scmlItemOffset = ((Scml.Text) item).getText().length();
                        }
                    }
                }
            }
            CompletionProviderFactory factory = CompletionProviderFactoryManager.getInstance().first(jml);
            org.radixware.kernel.common.scml.ScmlCompletionProvider provider = factory.findCompletionProvider(item);
            if (provider != null && completerProcessor != null) {
                provider.complete(scmlItemOffset, completerProcessor.createRequestorWrapper(new ScmlCompletionRequestor(resultItems)));
            }
        }
        final long compilerTime = System.currentTimeMillis() - t0;
                
        LinkedHashMap<String, HtmlCompletionItem> resMap = new LinkedHashMap<>();

        if (!resultItems.isEmpty()) {
            List<HtmlCompletionItem> htmlCompletionItemList = new ArrayList<>();
            final QFontMetrics fontMetrics = new QFontMetrics(font);
            font.setBold(true);
            final QFontMetrics boldMetrics = new QFontMetrics(font);
            font.setBold(false);

            SortedSet<HtmlCompletionItem> longestTextItems = new TreeSet<>(
                    new Comparator<HtmlCompletionItem>() {
                        @Override
                        public int compare(HtmlCompletionItem o1, HtmlCompletionItem o2) {
                            return Integer.compare(o2.getLenghtInChars(), o1.getLenghtInChars());
                        }
                    });

            for (int itemIdx = 0, size = resultItems.size(); itemIdx < size; itemIdx++) {
                checkInterrupt(itemIdx);
                CompletionItem item = resultItems.get(itemIdx);
                boolean isDeprecated = (item.getRadixObject() instanceof Definition) ? ((Definition) item.getRadixObject()).isDeprecated() : false;
                if (item.getRadixObject() instanceof AdsMethodDef) {
                    AdsMethodDef method = (AdsMethodDef) item.getRadixObject();
                    if (method.getId().toString().equals(method.getName())) {
                        continue;
                    }
                }
                String text = item.getLeadDisplayText();
                if ((text.equals(AdsSystemMethodDef.ID_REPORT_EXECUTE.toString()) || text.equals(AdsSystemMethodDef.ID_REPORT_OPEN.toString()))) {
                    continue;
                }
                HtmlCompletionItem htmlitem = new HtmlCompletionItem(item, fontMetrics, boldMetrics, isDeprecated);
                if (text.startsWith(EDefinitionIdPrefix.LIB_USERFUNC_PREFIX.getValue())) {
                    Definition owner = jml.getOwnerDefinition();
                    int index = text.indexOf('(');
                    AdsDefinition def = AdsUserFuncDef.Lookup.findTopLevelDefinition((AdsUserFuncDef) owner, Id.Factory.loadFrom(text.substring(0, index)));
                    if (def != null) {
                        text = ((AdsLibUserFuncWrapper) def).calsDisplayMethodName() + text.substring(index);
                        htmlitem.setColumn1(text);
                    }
                }

                htmlCompletionItemList.add(htmlitem);
                longestTextItems.add(htmlitem);
                if (longestTextItems.size() > ITEM_CNT_TO_CALC_COMPLETER_WIDTH) {
                    longestTextItems.remove(longestTextItems.last());
                }
            }

            for (HtmlCompletionItem item : longestTextItems) {
                int max = Math.max(itemWidth, item.getLenght());
                if (itemWidth < max) {
                    itemWidth = max;
                }
            }
            itemWidth = itemWidth <= CompleterProcessor.MAX_COMPLETER_SIZE ? itemWidth : CompleterProcessor.MAX_COMPLETER_SIZE;

            for (int index = 0, size = htmlCompletionItemList.size(); index < size; index++) {
                checkInterrupt(index);
                HtmlCompletionItem item = htmlCompletionItemList.get(index);
                resMap.put(item.getPlainText(), item);
            }
            
            prevComplItem = getPrevComplItem(items);
        }
        
        final long totalTime = System.currentTimeMillis() - t0;
        trace.debug(String.format("JmlEditor. Calculated %d completion items for %d ms (%d ms for compiler)", resultItems.size(), totalTime, compilerTime));
        if (completerProcessor != null) {
            QApplication.postEvent(completerProcessor, new CompleterProcessor.ExposeCompleterEvent(resMap, itemWidth, prevComplItem));
        }
        return resMap;
    }

    private void checkInterrupt(final int index) throws InterruptedException {
        if (index % PROCESS_ITEMS_RATE == 0) {
            if (Thread.interrupted()) {
                throw new InterruptedException();
            }
        }
    }

    private static class ScmlCompletionRequestor implements CompletionRequestor {

        List<CompletionItem> items;

        public ScmlCompletionRequestor(List<CompletionItem> items) {
            this.items = items;
        }

        @Override
        public void accept(org.radixware.kernel.common.scml.ScmlCompletionProvider.CompletionItem item) {
            items.add(item);
        }

        @Override
        public boolean isAll() {
            return true;
        }
    }
}
