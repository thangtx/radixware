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

package org.radixware.kernel.designer.common.dialogs.scmlnb;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import org.netbeans.api.editor.completion.Completion;
import org.netbeans.spi.editor.completion.CompletionDocumentation;
import org.netbeans.spi.editor.completion.CompletionProvider;
import org.netbeans.spi.editor.completion.CompletionResultSet;
import org.netbeans.spi.editor.completion.CompletionTask;
import org.netbeans.spi.editor.completion.LazyCompletionItem;
import org.netbeans.spi.editor.completion.support.AsyncCompletionQuery;
import org.netbeans.spi.editor.completion.support.AsyncCompletionTask;
import org.netbeans.spi.editor.completion.support.CompletionUtilities;
import org.openide.util.Exceptions;
import org.radixware.kernel.common.builder.completion.CompletionProviderFactoryManager;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.module.AdsPath;
import org.radixware.kernel.common.defs.dds.DdsDefinition;
import org.radixware.kernel.common.defs.dds.DdsPath;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.scml.CompletionProviderFactory;
import org.radixware.kernel.common.scml.Scml;
import org.radixware.kernel.common.scml.ScmlCompletionProvider.CompletionRequestor;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.general.utils.RadixMutex;


public class ScmlCompletionProvider implements CompletionProvider {

    private static class GoToAction implements Action {

        RadixObject obj;

        public GoToAction(RadixObject obj) {
            this.obj = obj;
        }

        @Override
        public Object getValue(String arg0) {
            return null;
        }

        @Override
        public void putValue(String arg0, Object arg1) {
        }

        @Override
        public void setEnabled(boolean arg0) {
        }

        @Override
        public boolean isEnabled() {
            return true;
        }

        @Override
        public void addPropertyChangeListener(PropertyChangeListener arg0) {
        }

        @Override
        public void removePropertyChangeListener(PropertyChangeListener arg0) {
        }

        @Override
        public void actionPerformed(ActionEvent arg0) {
            DialogUtils.goToObject(obj);
        }
    }

    private static class Documentation implements CompletionDocumentation {

        RadixObject obj;

        public Documentation(RadixObject obj) {
            this.obj = obj;
        }

        @Override
        public Action getGotoSourceAction() {
            return new GoToAction(obj);
        }

        @Override
        public String getText() {
            return obj.getToolTip();
        }

        @Override
        public URL getURL() {
            return null;
        }

        @Override
        public CompletionDocumentation resolveLink(String link) {
            if (link != null) {
                String[] idsStr = link.split(":");
                Id[] ids = new Id[idsStr.length];
                for (int i = 0; i < idsStr.length; i++) {
                    ids[i] = Id.Factory.loadFrom(idsStr[i]);
                }
                Definition ownerDef = obj.getDefinition();
                if (ownerDef instanceof AdsDefinition) {
                    Definition ref = AdsPath.resolve((AdsDefinition) ownerDef, ids).get();
                    if (ref != null) {
                        return new Documentation(ref);
                    }
                } else if (ownerDef instanceof DdsDefinition) {
                    Definition ref = DdsPath.resolve((DdsDefinition) ownerDef, ids).get();
                    if (ref != null) {
                        return new Documentation(ref);
                    }
                }
            }
            return null;
        }
    }

    private class CompletionItemProxy implements LazyCompletionItem {

        final org.radixware.kernel.common.scml.ScmlCompletionProvider.CompletionItem scml;
        final int caretOffset;
        final Scml.Item target;

        private CompletionItemProxy(org.radixware.kernel.common.scml.ScmlCompletionProvider.CompletionItem scml, int caretOffset, Scml.Item target) {
            this.scml = scml;
            this.caretOffset = caretOffset;
            this.target = target;
        }

        @Override
        public void defaultAction(JTextComponent component) {
            if (component instanceof ScmlEditorPane) {
                try {
                    ScmlEditorPane editor = (ScmlEditorPane) component;
                    ScmlDocument doc = editor.getScmlDocument();
                    Scml.Item itemToRemove = null;


                    int replaceStart = caretOffset - scml.getReplaceStartOffset();
                    int index = doc.getScml().getItems().indexOf(target);
                    if (index > 0) {
                        int replaced = replaceStart - doc.getItemStartOffset(index);
                        if (replaced == 0 || (replaced == 1 && ".".equals(doc.getText(replaceStart-1, 1)))) {
                            Scml.Item prev = doc.getScml().getItems().get(index - 1);
                            if (scml.removePrevious(prev)) {
                                replaceStart = doc.getItemStartOffset(index - 1);
                            }
                        }
                    }

                    int replaceEnd = caretOffset + scml.getReplaceEndOffset();
                    doc.remove(replaceStart, replaceEnd - replaceStart);
                    String suffix = scml.getEnclosingSuffix();
                    if (suffix != null && suffix.length() > 0) {
                        if (suffix.equals(editor.getText(replaceStart, suffix.length()))) {
                            editor.getDocument().remove(replaceStart, suffix.length());
                        }
                    }
                    //update module dependences
                    RadixObject obj = scml.getRadixObject();
                    if (obj != null && obj.getModule() != null) {
                        doc.getScml().getModule().getDependences().add(obj.getModule());
                    }

                    Scml.Item[] items = scml.getNewItems();
                    for (int i = items.length - 1; i >= 0; i--) {
                        try {
                            Scml.Item item = items[i];
                            if (item instanceof Scml.Tag) {
                                doc.insertTag(replaceStart, (Scml.Tag) item);
                            } else {
                                doc.insertText(replaceStart, (Scml.Text) item);
                            }
                        } catch (BadLocationException ex) {
                            break;
                        }
                    }
                    if (itemToRemove != null) {
                        doc.getScml().getItems().remove(itemToRemove);
                        doc.update();
                    }

                    Completion.get().hideCompletion();
                    Completion.get().hideDocumentation();


                } catch (BadLocationException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        }

        @Override
        public void processKeyEvent(KeyEvent evt) {
        }

        @Override
        public int getPreferredWidth(Graphics g, Font defaultFont) {
            return CompletionUtilities.getPreferredWidth(scml.getLeadDisplayText(), scml.getTailDisplayText(), g, defaultFont);
        }

        @Override
        public void render(Graphics g, Font defaultFont, Color defaultColor, Color backgroundColor, int width, int height, boolean selected) {
            RadixIcon icon = scml.getIcon();
            ImageIcon dispalyIcon = null;
            if (icon != null) {
                Image image = icon.getImage(16, 16);
                if (image != null) {
                    dispalyIcon = new ImageIcon(image);
                }
            }
            CompletionUtilities.renderHtml(dispalyIcon, scml.getLeadDisplayText(), scml.getTailDisplayText(), g, defaultFont, defaultColor, width, height, selected);
        }

        @Override
        public CompletionTask createDocumentationTask() {
            final RadixObject obj = scml.getRadixObject();
            if (obj == null) {
                return null;
            }
            
            return new AsyncCompletionTask(new AsyncCompletionQuery() {
                @Override
                protected void query(CompletionResultSet resultSet, Document doc, int caretOffset) {
                    resultSet.setDocumentation(new Documentation(obj));
                    resultSet.finish();
                }
            });
        }

        @Override
        public CompletionTask createToolTipTask() {
            return null;
        }

        @Override
        public boolean instantSubstitution(JTextComponent component) {
            return false;
        }

        @Override
        public int getSortPriority() {
            return  1000000-scml.getRelevance();
        }

        @Override
        public CharSequence getSortText() {
            return scml.getSortText();
        }

        @Override
        public CharSequence getInsertPrefix() {
            return scml.getSortText();
        }

        @Override
        public boolean accept() {
            return true;
        }
    }

    private class ScmlCompletionRequestor implements CompletionRequestor {

        CompletionResultSet resultSet;
        int caretOffset;
        Scml.Item item;
        private final boolean all;
        private final Set<String> acceptedLabels = new HashSet<String>();

        ScmlCompletionRequestor(CompletionResultSet resultSet, int caretOffset, Scml.Item item, boolean all) {
            this.caretOffset = caretOffset;
            this.resultSet = resultSet;
            this.item = item;
            this.all = all;
        }

        @Override
        public void accept(org.radixware.kernel.common.scml.ScmlCompletionProvider.CompletionItem item) {

            StringBuilder uuid = new StringBuilder();

            uuid.append(item.getLeadDisplayText()).append(" ").append(item.getTailDisplayText());

            String key = uuid.toString();
            if (acceptedLabels.contains(key)) {
                return;
            }
            acceptedLabels.add(key);

            CompletionItemProxy proxy = new CompletionItemProxy(item, caretOffset, this.item);
            resultSet.addItem(proxy);
        }

        @Override
        public boolean isAll() {
            return all;
        }
    }

    @Override
    public CompletionTask createTask(int queryType, JTextComponent component) {
        switch (queryType) {
            case CompletionProvider.COMPLETION_QUERY_TYPE:
                return createTaskImpl(component, false);
            case CompletionProvider.COMPLETION_ALL_QUERY_TYPE:
                return createTaskImpl(component, true);
            default:
                return null;
        }
    }

    private CompletionTask createTaskImpl(final JTextComponent component, final boolean all) {

        return new AsyncCompletionTask(new AsyncCompletionQuery() {
            @Override
            protected void query(CompletionResultSet resultSet, Document doc, int caretOffset) {
                final Lock lock = RadixMutex.getLongProcessLock();
                try {
                    try {
                        resultSet.setWaitText("Build running. Please wait...");
                        lock.lock();
                        resultSet.setWaitText("Please wait...");
                        
                        if (doc instanceof ScmlDocument) {
                            final ScmlDocument sdoc = (ScmlDocument) doc;
                            final CompletionProviderFactory factory = CompletionProviderFactoryManager.getInstance().first(sdoc.getScml());
                            if (factory != null) {
                                int info[] = sdoc.itemIndexAndOffset(caretOffset);
                                if (info == null) {
                                    return;
                                }
                                assert info.length == 2;
                                int scmlItemIndex = info[0];
                                int scmlItemOffset = info[1];
                                RadixObjects<Scml.Item> items = sdoc.getScml().getItems();
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
                                    org.radixware.kernel.common.scml.ScmlCompletionProvider provider = factory.findCompletionProvider(item);
                                    if (provider != null) {
                                        provider.complete(scmlItemOffset, new ScmlCompletionRequestor(resultSet, caretOffset, item, all));
                                    }
                                }
                            }
                        }

                    } finally {
                        lock.unlock();
                    }
                } finally {
                    resultSet.finish();
                }
            }
        },
                component);
    }

    @Override
    public int getAutoQueryTypes(JTextComponent component, String typedText) {
        return 0;
    }
}