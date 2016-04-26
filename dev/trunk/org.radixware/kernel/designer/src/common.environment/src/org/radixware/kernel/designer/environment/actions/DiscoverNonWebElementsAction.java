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

package org.radixware.kernel.designer.environment.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Action;
import org.openide.util.Lookup;
import org.openide.windows.IOProvider;
import org.openide.windows.InputOutput;
import org.openide.windows.OutputEvent;
import org.openide.windows.OutputListener;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.VisitorProviderFactory;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPresentationDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsSelectorPresentationDef;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsExplorerItemDef;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsParagraphExplorerItemDef;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsParentRefExplorerItemDef;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsSelectorExplorerItemDef;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.scml.Scml;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.general.editors.OpenInfo;
import org.radixware.kernel.designer.common.general.utils.RadixMutex;


public class DiscoverNonWebElementsAction extends AbstractContextAwareAction implements ActionListener {

    private final List<RadixObject> contexts;
    private boolean ioOpened = false;

    public DiscoverNonWebElementsAction(Collection<? extends RadixObject> contexts) {
        if (contexts == null) {
            this.contexts = Collections.emptyList();
        } else {
            this.contexts = new ArrayList<RadixObject>(contexts);
        }
    }

    public DiscoverNonWebElementsAction() {
        super();
        this.contexts = Collections.emptyList();
    }

    @Override
    public Action createContextAwareInstance(Lookup actionContext) {
        return new DiscoverNonWebElementsAction(actionContext.lookupAll(RadixObject.class));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (contexts.isEmpty()) {
            return;
        }
        final List<RadixObject> radixObjects = new ArrayList<RadixObject>(contexts);
        RadixMutex.readAccess(new Runnable() {
            @Override
            public void run() {

                Set<Branch> branches = new HashSet<Branch>();
                for (RadixObject obj : radixObjects) {
                    branches.add(obj.getBranch());
                }
                final Set<RadixObject> checkedObjects = new HashSet<RadixObject>();
                final Set<AdsEntityObjectClassDef> unusableClasses = new HashSet<AdsEntityObjectClassDef>();
                for (Branch branch : branches) {
                    if (branch != null) {
                        branch.visit(new IVisitor() {
                            @Override
                            public void accept(RadixObject radixObject) {
                                if (checkedObjects.contains(radixObject)) {
                                    return;
                                }
                                if (radixObject instanceof AdsEntityObjectClassDef) {
                                    AdsEntityObjectClassDef clazz = (AdsEntityObjectClassDef) radixObject;
                                    if (!checkClassPresentations(clazz)) {
                                        unusableClasses.add(clazz);
                                    }
                                }
                            }
                        }, VisitorProviderFactory.createDefaultVisitorProvider());
                    }
                }
                for (AdsExplorerItemDef ei : collectExplorerItems(radixObjects)) {

                    if (!processExplorerItem(ei, unusableClasses)) {
                        problem("Explorer item is unusable", ei);
                    }
                }

            }
        });

    }

    private Collection<AdsExplorerItemDef> collectExplorerItems(List<RadixObject> radixObjects) {
        final Set<AdsExplorerItemDef> result = new HashSet<AdsExplorerItemDef>();
        for (RadixObject obj : radixObjects) {
            obj.visit(new IVisitor() {
                @Override
                public void accept(RadixObject radixObject) {
                    result.add((AdsExplorerItemDef) radixObject);
                }
            }, new VisitorProvider() {
                @Override
                public boolean isTarget(RadixObject radixObject) {

                    if (radixObject instanceof AdsExplorerItemDef) {
                        if (!result.contains(((AdsExplorerItemDef) radixObject).getOwnerDef())) {
                            return radixObject instanceof AdsExplorerItemDef;
                        }
                    }
                    return false;

                }
            });
        }
        return result;
    }

    private boolean processExplorerItem(AdsExplorerItemDef explorerItem, Set<AdsEntityObjectClassDef> classes) {

        if (explorerItem instanceof AdsSelectorExplorerItemDef) {
            AdsSelectorExplorerItemDef sei = (AdsSelectorExplorerItemDef) explorerItem;
            AdsSelectorPresentationDef spr = sei.findReferencedSelectorPresentation().get();
            if (spr != null) {
                AdsEntityObjectClassDef clazz = spr.getOwnerClass();
                if (clazz != null && classes.contains(clazz)) {
                    return false;
                }
            }
        } else if (explorerItem instanceof AdsParentRefExplorerItemDef) {
            AdsParentRefExplorerItemDef sei = (AdsParentRefExplorerItemDef) explorerItem;
            AdsEntityObjectClassDef clazz = sei.findReferencedEntityClass();
            if (clazz != null && classes.contains(clazz)) {
                return false;
            }
        } else if (explorerItem instanceof AdsParagraphExplorerItemDef) {
            AdsParagraphExplorerItemDef par = (AdsParagraphExplorerItemDef) explorerItem;
            if (par.getCustomViewSupport().isUseCustomView(ERuntimeEnvironmentType.EXPLORER)) {
                return false;
            }
            for (AdsExplorerItemDef child : par.getExplorerItems().getChildren().get(EScope.ALL)) {
                if (!processExplorerItem(child, classes)) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean checkClassPresentations(AdsEntityObjectClassDef clazz) {
        boolean clazzIsUnusable = false;
        for (AdsEditorPresentationDef epr : clazz.getPresentations().getEditorPresentations().get(EScope.ALL)) {
            if (epr.getCustomViewSupport().isUseCustomView(ERuntimeEnvironmentType.EXPLORER)) {
                problem("Qt based custom dialog usage", epr);
            } else {
                AdsEditorPresentationDef.ModelClassInfo info = epr.findActualModelClass();
                if (info.clazz != null) {
                    if (!checkModelClass(info.clazz)) {
                        clazzIsUnusable = true;
                    }
                }
            }
        }
        return clazzIsUnusable;
    }

    private boolean checkModelClass(AdsClassDef clazz) {
        final boolean[] was_problems = new boolean[]{false};

        // final List<Scml.Item> problemItems = new LinkedList<Scml.Item>();
        clazz.visit(new IVisitor() {
            @Override
            public void accept(RadixObject radixObject) {
                if (radixObject instanceof Scml.Text) {
                    String text = radixObject.toString();
                    if (text.contains("com.trolltech.")) {
                        problem("Possible qt class reference", radixObject);
                        was_problems[0] = true;
                    }
                }
            }
        }, new VisitorProvider() {
            @Override
            public boolean isTarget(RadixObject radixObject) {
                return radixObject instanceof Scml.Text;
            }
        });
        return !was_problems[0];
    }

    private void problem(String message, final RadixObject location) {
        InputOutput io = IOProvider.getDefault().getIO("Discover Non Web Elements", false);
        if (!ioOpened) {
            try {
                io.getOut().reset();
            } catch (IOException ex) {
                Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
            }
            io.select();
            ioOpened = true;
        }
        StringBuilder name = new StringBuilder();
        name.append(location.getQualifiedName());
        while (name.length() < 60) {
            name.append(" ");
        }
        if (name.length() > 60) {
            name.setLength(57);
            name.append("...");
        }
        name.append(":    ");
        name.append(message);
        try {
            io.getOut().println(name.toString(), new OutputListener() {
                @Override
                public void outputLineSelected(OutputEvent oe) {
                }

                @Override
                public void outputLineAction(OutputEvent oe) {
                    DialogUtils.goToObject(location, new OpenInfo(location));
                }

                @Override
                public void outputLineCleared(OutputEvent oe) {
                }
            });
        } catch (IOException ex) {
            io.getOut().println(name.toString());
        }
    }
}
