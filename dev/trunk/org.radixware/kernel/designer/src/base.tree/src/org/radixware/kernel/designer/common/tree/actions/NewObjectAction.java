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

package org.radixware.kernel.designer.common.tree.actions;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CookieAction;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.exceptions.RadixObjectError;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.dialogs.wizards.newobject.CreationWizard;
import org.radixware.kernel.designer.common.general.creation.ICreatureGroup;
import org.radixware.kernel.designer.common.general.creation.ICreatureGroup.ICreature;
import org.radixware.kernel.designer.common.general.editors.EditorsManager;
import org.radixware.kernel.designer.common.general.nodes.NodesManager;

public final class NewObjectAction extends CookieAction {

    private static class CreationContextProxy implements ICreatureGroup {

        String displayName;
        List<ICreature> creatures;

        private CreationContextProxy(ICreatureGroup c) {
            this.displayName = c.getDisplayName();
            this.creatures = new ArrayList<ICreature>(c.getCreatures());
        }

        @Override
        public String getDisplayName() {
            return displayName;
        }

        @Override
        public List<ICreature> getCreatures() {
            return creatures;
        }
    }

    private static class NewObjectImpl extends AbstractAction {

        private CreationContextProxy[] proxies;
        private ICreature creature;
        private static final NewObjectImpl SEPARATOR = new NewObjectImpl();

        protected NewObjectImpl(CreationContextProxy[] proxies, ICreature creature) {
            super(creature.getDisplayName());
            this.creature = creature;
            this.proxies = proxies;
        }

        private NewObjectImpl() {
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            processCreature(proxies, creature);
        }
    }

    private static void processCreature(CreationContextProxy[] contexts, ICreature creature) {
        ICreature result = CreationWizard.execute(contexts, creature);
        if (result != null) {
            try {
                final RadixObject radixObject = result.commit();
                if (radixObject != null) {

                    if (EditorsManager.getDefault().isOpeningAfterNewObjectCreationRequired(radixObject)) {
                        SwingUtilities.invokeLater(new Runnable() {

                            @Override
                            public void run() {
                                DialogUtils.goToObject(radixObject);
                            }
                        });
                    } else {

                        SwingUtilities.invokeLater(new Runnable() {

                            @Override
                            public void run() {
                                NodesManager.selectInProjects(radixObject);
                            }
                        });
                    }
                }
            } catch (RadixObjectError ex) {
                DialogDisplayer.getDefault().notify(new NotifyDescriptor.Message("Object setup is not completed correctly:\n" + ex.getMessage(), NotifyDescriptor.ERROR_MESSAGE));
            }
        }
    }

    private static final CreationContextProxy[] proxy(ICreatureGroup[] contexts) {
        if (contexts != null) {
            CreationContextProxy[] proxies = new CreationContextProxy[contexts.length];
            for (int i = 0; i
                < proxies.length; i++) {
                proxies[i] = contexts[i] == null ? null : new CreationContextProxy(contexts[i]);
            }

            return proxies;
        } else {
            return null;
        }

    }

    @Override
    protected void performAction(Node[] activatedNodes) {
        for (int i = 0; i < activatedNodes.length; i++) {

            NewObjectCookie cookie = activatedNodes[i].getCookie(NewObjectCookie.class);
            if (cookie != null) {
                CreationContextProxy[] proxies = proxy(cookie.getCreatureGroups());
                if (proxies.length == 0) {
                    DialogUtils.messageInformation("No objects can be created in current context");
                } else {
                    processCreature(proxies, null);
                }
            }
        }

    }

    @Override
    protected int mode() {
        return CookieAction.MODE_EXACTLY_ONE;
    }

    @Override
    public String getName() {
        return NbBundle.getMessage(NewObjectAction.class, "CTL_NewDefinitionAction");
    }

    @Override
    protected Class[] cookieClasses() {
        return new Class[]{ NewObjectCookie.class };
    }

    @Override
    protected String iconResource() {
        return "org/radixware/kernel/designer/common/tree/actions/newDefinition.png";
    }

    @Override
    public HelpCtx getHelpCtx() {
        return null;
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }

    @Override
    public JMenuItem getPopupPresenter() {
        List<NewObjectImpl> creators = createSubActions();
        JMenu result = new JMenu("New");
        if (isEnabled()) {
            for (NewObjectImpl c : creators) {
                if (c == NewObjectImpl.SEPARATOR) {
                    result.addSeparator();
                } else {
                    JMenuItem item = new JMenuItem(c);
                    RadixIcon icon = c.creature.getIcon();
                    if (icon != null) {
                        item.setIcon(icon.getIcon(16, 16));
                    }
                    item.setEnabled(c.creature.isEnabled());
                    result.add(item);
                }
            }
        } else {
            result.setEnabled(false);
        }
        return result;
    }

    private List<NewObjectImpl> createSubActions() {
        final List<NewObjectImpl> creators = new LinkedList<NewObjectImpl>();
        Node[] activeNodes = getActivatedNodes();
        if (activeNodes != null) {
            for (int n = 0; n < activeNodes.length; n++) {

                NewObjectCookie cookie = activeNodes[n].getCookie(NewObjectCookie.class);

                if (cookie != null) {
                    CreationContextProxy[] contexts = proxy(cookie.getCreatureGroups());
                    if (contexts != null) {
                        for (int i = 0; i < contexts.length; i++) {
                            ICreatureGroup provider = contexts[i];//.newInstance();
                            if (provider != null) {
                                if (i > 0) {
                                    creators.add(NewObjectImpl.SEPARATOR);
                                }
                                List<ICreature> creatures = provider.getCreatures();
                                if (creatures != null) {
                                    for (ICreature c : creatures) {
                                        if (c != null) {
                                            creators.add(new NewObjectImpl(contexts, c));
                                        } else {
                                            creators.add(NewObjectImpl.SEPARATOR);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

        }
        return creators;
    }
}
