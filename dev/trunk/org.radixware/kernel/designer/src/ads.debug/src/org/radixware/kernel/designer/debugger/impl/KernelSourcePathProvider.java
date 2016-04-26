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

package org.radixware.kernel.designer.debugger.impl;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.prefs.Preferences;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.JTextComponent;
import javax.swing.text.StyledDocument;

import org.netbeans.api.editor.EditorRegistry;
import org.netbeans.modules.editor.NbEditorUtilities;
import org.openide.filesystems.FileObject;
import org.openide.text.Line;
import org.openide.text.NbDocument;
import org.openide.util.Exceptions;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.general.filesystem.RadixFileUtil;


public abstract class KernelSourcePathProvider {

    protected final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    public static final String PROP_NAME_SOURCE_ROOTS = "sourceRoots";
    public static final String SOURCE_ROOTS_PREF_NAME = "rdxDebuggerSourceRoots";
    private static final KernelSourcePathProvider defaultProvider = new KernelSourcePathProvider() {

        private List<KernelSourcePath> pathes = null;
        private final Object lock = new Object();

        private String serialize() {
            if (pathes != null) {
                StringBuilder builder = new StringBuilder();
                boolean first = true;
                for (KernelSourcePath sp : pathes) {
                    if (first) {
                        first = false;
                    } else {
                        builder.append(';');
                    }
                    builder.append(sp.getPath());
                }
                return builder.toString();
            } else {
                return null;
            }
        }

        private List<String> deserialize(String value) {
            if (value == null) {
                return Collections.emptyList();
            } else {
                String[] values = value.split(";");
                return Arrays.asList(values);
            }
        }

        @Override
        public List<KernelSourcePath> getSourceRoots() {
            upload();
            return pathes;
        }

        private void save() {
            Preferences.userNodeForPackage(KernelSourcePathProvider.class).put(SOURCE_ROOTS_PREF_NAME, serialize());
        }

        private void upload() {
            synchronized (lock) {
                if (pathes == null) {
                    pathes = new ArrayList<KernelSourcePath>();
                    List<String> storedValues = deserialize(Preferences.userNodeForPackage(KernelSourcePathProvider.class).get(SOURCE_ROOTS_PREF_NAME, null));
                    for (String s : storedValues) {
                        pathes.add(new KernelSourcePath(s));
                    }
                    pcs.firePropertyChange(PROP_NAME_SOURCE_ROOTS, null, null);
                }
            }
        }

        @Override
        protected boolean registerSourceRoot(File root) {
            upload();
            synchronized (lock) {
                pathes.add(new KernelSourcePath(root.getAbsolutePath()));
                save();
                pcs.firePropertyChange(PROP_NAME_SOURCE_ROOTS, null, null);
            }
            return true;
        }

        @Override
        protected boolean unregisterSourceRoot(KernelSourcePath root) {
            upload();
            synchronized (lock) {
                List<KernelSourcePath> toRemove = new LinkedList<KernelSourcePath>();
                for (KernelSourcePath path : pathes) {
                    if (path.getPath().equals(root.getPath())) {
                        toRemove.add(path);
                    }
                }
                for (KernelSourcePath path : toRemove) {
                    pathes.remove(path);
                }
                save();
                pcs.firePropertyChange(PROP_NAME_SOURCE_ROOTS, null, null);
            }
            return true;
        }
    };

    public KernelSourcePathProvider() {
    }

    public abstract List<KernelSourcePath> getSourceRoots();
    private File curDir = null;

    public void removeSourceRoot(KernelSourcePath path) {
        unregisterSourceRoot(path);
    }

    public void addSourceRoot() {
        JFileChooser chooser = new JFileChooser(curDir);
        chooser.addChoosableFileFilter(new FileFilter() {

            @Override
            public boolean accept(File f) {
                return f.isDirectory();
            }

            @Override
            public String getDescription() {
                return "Sources folder";
            }
        });

        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            if (registerSourceRoot(chooser.getSelectedFile())) {
                pcs.firePropertyChange(PROP_NAME_SOURCE_ROOTS, null, null);
            }
            curDir = chooser.getCurrentDirectory();
        }
    }

    protected abstract boolean registerSourceRoot(File root);

    protected abstract boolean unregisterSourceRoot(KernelSourcePath root);

    public static KernelSourcePathProvider getInstance() {
        return defaultProvider;
    }

    public synchronized void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(propertyName, listener);
    }

    public synchronized void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }

    public synchronized void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(propertyName, listener);
    }

    public synchronized void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public Line openSourceFile(String className, int lineNumber) {
        String lookup = className;
        if (className.contains("$")) {
            lookup = lookup.substring(0, lookup.indexOf("$"));
        }
        String[] components = lookup.split("\\.");
        if (components.length > 0) {
            components[components.length - 1] = components[components.length - 1] + ".java";
        } else {
            return null;
        }
        File srcFile = null;
        for (KernelSourcePath path : getSourceRoots()) {
            File root = new File(path.getPath());
            for (String c : components) {
                File file = new File(root, c);
                if (file.exists()) {
                    root = file;
                } else {
                    root = null;
                    break;
                }
            }
            if (root != null) {
                srcFile = root;
                break;
            }
        }
        if (srcFile != null) {
            FileObject fo = RadixFileUtil.toFileObject(srcFile);
            if (DialogUtils.editFile(srcFile)) {
                int ic = 0;
                while (ic < 5) {
                    try {
                        for (JTextComponent c : EditorRegistry.componentList()) {
                            org.openide.filesystems.FileObject obj = NbEditorUtilities.getFileObject(c.getDocument());
                            if (obj == fo) {
                                if (c.getDocument() instanceof StyledDocument) {
                                    int offset = NbDocument.findLineOffset((StyledDocument) c.getDocument(), lineNumber - 1);
                                    c.setCaretPosition(offset);
                                    return NbEditorUtilities.getLine(c.getDocument(), offset, true);
                                }
                            }
                        }
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException ex) {
                            Exceptions.printStackTrace(ex);
                        }
                    } finally {
                        ic++;
                    }
                }
                return null;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
}
