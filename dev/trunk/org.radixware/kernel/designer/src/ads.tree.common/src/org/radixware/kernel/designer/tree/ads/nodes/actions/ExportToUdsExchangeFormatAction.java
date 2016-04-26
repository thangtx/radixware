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

package org.radixware.kernel.designer.tree.ads.nodes.actions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;
import org.netbeans.api.progress.ProgressUtils;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.actions.CookieAction;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProviderFactory;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.UdsExportable;
import org.radixware.kernel.common.defs.ads.localization.AdsLocalizingBundleDef;
import org.radixware.kernel.common.defs.ads.localization.AdsMultilingualStringDef;
import org.radixware.kernel.designer.common.dialogs.usages.FindUsagesResults;
import org.radixware.kernel.designer.common.dialogs.usages.UsagesFinder;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;


public class ExportToUdsExchangeFormatAction extends CookieAction {

    public static final class Cookie implements Node.Cookie {

        private static File currentDirectory;
        private final UdsExportable def;

        public Cookie(UdsExportable def) {
            this.def = def;
        }

        private void perform() {
            if (def == null) {
                return;
            }
            JFileChooser fileChooser = new JFileChooser(currentDirectory);
            for (FileFilter f : fileChooser.getChoosableFileFilters()) {
                fileChooser.removeChoosableFileFilter(null);
            }
            FileFilter filter = new FileFilter() {
                @Override
                public boolean accept(File f) {
                    if (f.isDirectory()) {
                        return true;
                    } else {
                        return f.getName().endsWith(".xml");
                    }
                }

                @Override
                public String getDescription() {
                    return "User-Defined Definition Exchange File (*.xml)";
                }
            };

            fileChooser.addChoosableFileFilter(filter);
            fileChooser.setFileFilter(filter);
            fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
            if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                final File file = fileChooser.getSelectedFile();
                if (file.exists()) {
                    if (!DialogUtils.messageConfirmation("File " + file.getAbsolutePath() + " is already exists. Overwite?")) {
                        return;
                    }
                }
                ProgressUtils.showProgressDialogAndRun(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            FileOutputStream out = new FileOutputStream(file);
                            try {

                                try {
                                    final Map<RadixObject, List<Definition>> usedNotPublished = new HashMap<>();
                                    final List<Definition> dependences = new ArrayList<>();
                                    ((Definition) def).visit(new IVisitor() {
                                        @Override
                                        public void accept(RadixObject radixObject) {
                                            dependences.clear();
                                            radixObject.collectDependences(dependences);
                                            for (Definition dep : dependences) {
                                                if (!dep.isPublished()) {
                                                    if (dep instanceof AdsLocalizingBundleDef || dep instanceof AdsMultilingualStringDef) {
                                                        continue;
                                                    }
                                                    if (((Definition) def).isParentOf(dep)) {
                                                        continue;
                                                    }
                                                    List<Definition> usedDefs = usedNotPublished.get(radixObject);
                                                    if (usedDefs == null) {
                                                        usedDefs = new LinkedList<>();
                                                        usedNotPublished.put(radixObject, usedDefs);
                                                    }
                                                    if (!usedDefs.contains(dep)) {
                                                        usedDefs.add(dep);
                                                    }
                                                }
                                            }
                                        }
                                    }, VisitorProviderFactory.createDefaultVisitorProvider());

                                    if (!usedNotPublished.isEmpty()) {

                                        SwingUtilities.invokeLater(new Runnable() {
                                            @Override
                                            public void run() {
                                                final FindUsagesResults findUsagesResults = FindUsagesResults.findInstance();
                                                findUsagesResults.clear((Definition) def);
                                                for (RadixObject obj : usedNotPublished.keySet()) {
                                                    List<Definition> usedDefs = usedNotPublished.get(obj);
                                                    for (Definition usedDef : usedDefs) {
                                                        findUsagesResults.add((Definition) def, obj, usedDef);
                                                    }
                                                }
                                                findUsagesResults.open();
                                                findUsagesResults.requestVisible();
                                                findUsagesResults.requestActive();
                                            }
                                        });
                                        if (!DialogUtils.messageConfirmation("Exported definition depends from not published definitions. Result definition may become errorneous.\n Continue?")) {
                                            return;
                                        }
                                    }
                                    def.exportToUds(out);
                                } catch (final IOException ex) {
                                    SwingUtilities.invokeLater(new Runnable() {
                                        @Override
                                        public void run() {
                                            DialogUtils.messageError(ex);
                                        }
                                    });
                                }
                            } finally {
                                try {
                                    out.flush();
                                } catch (IOException ex) {
                                }
                                try {
                                    out.close();
                                } catch (IOException ex) {
                                }
                            }
                        } catch (final FileNotFoundException ex) {
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    DialogUtils.messageError(ex);
                                }
                            });
                        }
                    }
                }, "Export Definition...");
            }
        }
    }

    @Override
    protected int mode() {
        return MODE_EXACTLY_ONE;
    }

    @Override
    protected Class<?>[] cookieClasses() {
        return new Class[]{Cookie.class
        };
    }

    @Override
    protected void performAction(Node[] activatedNodes) {
        if (activatedNodes != null) {
            for (Node node : activatedNodes) {
                Cookie c = node.getCookie(Cookie.class);
                if (c
                        != null) {
                    c.perform();
                }
            }
        }
    }

    @Override
    public String getName() {
        return "Export to UDS Exchange File";
    }

    @Override
    public HelpCtx getHelpCtx() {
        return null;
    }
}
