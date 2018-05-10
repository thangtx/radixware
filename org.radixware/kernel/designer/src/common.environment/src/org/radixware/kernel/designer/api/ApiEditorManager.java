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

package org.radixware.kernel.designer.api;

import java.awt.Container;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.Stack;
import java.util.WeakHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.exceptions.RadixObjectError;


public class ApiEditorManager {
    
    private enum EClickStreamDirection {

        NEW, PREV, NEXT
    }

    private static class WeakClickPair {

        private final WeakReference<RadixObject> object;
        private final EditorState state;

        public WeakClickPair(WeakReference<RadixObject> object, EditorState state) {
            this.object = object;
            this.state = state;
        }
    }

    private static class ClickPair {

        private final RadixObject object;
        private final EditorState state;

        public ClickPair(RadixObject object, EditorState state) {
            this.object = object;
            this.state = state;
        }
    }

    private static class ClickStream {

        private final Stack<WeakClickPair> prev = new Stack<>();
        private final Stack<WeakClickPair> next = new Stack<>();

        public ClickStream() {
        }

        public synchronized void add(WeakClickPair pair, EClickStreamDirection direction) {
            switch (direction) {
                case NEW:
                    if (!prev.isEmpty() && pair.object.get() == prev.peek().object.get()) {
                        prev.pop();
                    }
                    prev.push(pair);
                    next.clear();
                    break;
                case NEXT:

                    if (!prev.isEmpty() && pair.object.get() == prev.peek().object.get()) {
                        prev.pop();
                    }
                    prev.push(pair);
                    break;
                case PREV:
                    if (!next.isEmpty() && pair.object.get() == next.peek().object.get()) {
                        next.pop();
                    }
                    next.push(pair);
                    break;
            }
        }

        public synchronized ClickPair next() {
            if (next.isEmpty()) {
                return null;
            }
            final WeakClickPair pair = next.pop();
            final WeakReference<RadixObject> obj = pair.object;
            final RadixObject ref = obj.get();
            if (ref == null) {
                return next();
            }

            return new ClickPair(ref, pair.state);
        }

        public synchronized ClickPair prev() {
            if (prev.isEmpty()) {
                return null;
            }

            WeakClickPair pair = prev.pop();
            RadixObject ref = pair.object.get();
            while (ref == null && !prev.isEmpty()) {
                pair = prev.pop();
                ref = pair.object.get();
            }

            return new ClickPair(ref, pair.state);
        }

        public synchronized boolean canPrev() {
            return !prev.isEmpty();
        }

        public synchronized boolean canNext() {
            return !next.isEmpty();
        }
    }

        
    private final static Map<ApiBrowserTopComponent, ApiEditorManager> managers;
    static final ApiBrowserTopComponent defaultBrowser;
    
    static {
        defaultBrowser = ApiBrowserTopComponent.Factory.findInstance();
        managers = new WeakHashMap<>();
        
        managers.put(defaultBrowser, new ApiEditorManager(defaultBrowser));
    }
    
    public static ApiEditorManager getDefault() {
        return managers.get(defaultBrowser);
    }
    
    static ApiEditorManager find(ApiBrowserTopComponent browser) {
        if (browser == null) {
            return getDefault();
        }
        return managers.get(browser);
    }
    
    public static ApiEditorManager find(JComponent context) {
        Container parent = context;
        while (parent != null && !(parent instanceof ApiBrowserTopComponent)) {
            parent = parent.getParent();
        }
        
        return find((ApiBrowserTopComponent) parent);
    }
    
    public static ApiEditorManager create() {
        final ApiBrowserTopComponent browser = ApiBrowserTopComponent.Factory.createInstance();
        final ApiEditorManager manager = new ApiEditorManager(browser);
        managers.put(browser, manager);
        
        return manager;
    }
    
    private final ClickStream history = new ClickStream();
    private IApiEditor<? extends RadixObject> currentEditor;
    private ApiBrowserTopComponent browser;
    
    private ApiEditorManager(ApiBrowserTopComponent browser) {
        this.browser = browser;
    }

    public boolean open(final RadixObject object) {
        return open(object, EClickStreamDirection.NEW, null);
    }

    private boolean open(final RadixObject object, EClickStreamDirection direction, final EditorState state) {
        if (object == null) {
            return false;
        }

        if (!object.isInBranch()) {
            return false;
        }

        try {

            final IApiEditor<?> editor = findTopLevelEditor(object);

            if (editor == null) {
                Logger.getLogger(ApiEditorManager.class.getName()).log(
                        Level.INFO, "Can't open Api editor for '{0}'", new Object[]{object.getQualifiedName()});
                return false;
            }

            if (currentEditor != null) {
                if (currentEditor.getSource() != editor.getSource()) {
                    history.add(new WeakClickPair(new WeakReference<>(currentEditor.getSource()), currentEditor.getState()), direction);
                }
            }

            currentEditor = editor;

            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    browser.open(editor, object, state);
                }
            });

            return true;

        } catch (RadixObjectError error) {
            return false;
        }
    }

    boolean openPrev() {
        final ClickPair prev = history.prev();
        if (prev != null) {
            return open(prev.object, EClickStreamDirection.PREV, prev.state);
        }

        return false;
    }

    boolean openNext() {
        final ClickPair next = history.next();
        if (next != null) {
            return open(next.object, EClickStreamDirection.NEXT, next.state);
        }

        return false;
    }

    boolean canPrev() {
        return history.canPrev();
    }

    boolean canNext() {
        return history.canNext();
    }

    public static <T extends RadixObject> IApiEditor<T> findEditor(T object) {
        if (object == null) {
            return null;
        }
        final IApiEditorFactory factory = ApiEditorFactoriesManager.getDefault().get(object);
        if (factory != null) {
            return ApiEditorFactoriesManager.getDefault().get(object).newInstance(object);
        }

        return null;
    }

    public static <T extends RadixObject> IApiEditor<T> findTopLevelEditor(T object) {
        if (object == null) {
            return null;
        }

        IApiEditorFactory factory = ApiEditorFactoriesManager.getDefault().get(object);
        if (factory == null) {
            return null;
        }
        IApiEditor<T> editor = factory.newInstance(object);
        RadixObject current = object;
        while (editor != null && editor.isEmbedded()) {
            current = current.getOwnerDefinition();
            if(current == null)
                return null;
            factory = ApiEditorFactoriesManager.getDefault().get(current);

            if (factory == null) {
                return null;
            }
            editor = factory.newInstance(current);

        }

        return editor;
    }
    
    public void refreshBrowser(){
        defaultBrowser.updateEditor();
    }
}
