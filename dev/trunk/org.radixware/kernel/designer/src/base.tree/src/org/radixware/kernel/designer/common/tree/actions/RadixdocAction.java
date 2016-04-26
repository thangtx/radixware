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

import java.awt.Component;
import java.awt.Container;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.actions.CookieAction;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.repository.Segment;
import org.radixware.kernel.designer.common.dialogs.utils.ModalDisplayer;

public class RadixdocAction extends CookieAction {

    private static final String GENERATOR_CLASS_NAME = "org.radixware.kernel.designer.common.dialogs.radixdoc.RadixdocGenerator";

    public static class RadixdocCookie implements Node.Cookie {

        private Module module = null;
        private Segment segment = null;
        private Layer layer = null;
        private Branch branch = null;

        public RadixdocCookie(Module module, Segment segment, Layer layer, Branch branch) {
            this.branch = branch;
            this.module = module;
            this.layer = layer;
            this.segment = segment;
        }

        public RadixdocCookie(Module module) {
            this.module = module;
        }
    }

    @Override
    protected int mode() {
        return MODE_ANY;
    }

    @Override
    protected Class<?>[] cookieClasses() {
        return new Class[]{RadixdocCookie.class};
    }

    @Override
    protected void performAction(Node[] activatedNodes) {
        if (activatedNodes.length > 0) {
            final List<Module> modules = new ArrayList<>(activatedNodes.length);

            for (final Node node : activatedNodes) {
                final RadixdocCookie cookie = node.getLookup().lookup(RadixdocCookie.class);
                RadixdocSelectModulesPanel selectModules = null;

                if (cookie.branch != null) {
                    selectModules = new RadixdocSelectModulesPanel(cookie.branch);
                } else if (cookie.layer != null) {
                    selectModules = new RadixdocSelectModulesPanel(cookie.layer);
                } else if (cookie.segment != null) {
                    selectModules = new RadixdocSelectModulesPanel(cookie.segment);
                } else if (cookie.module != null) {
                    modules.add(cookie.module);
                }
                if (selectModules != null) {
                    if (!showSelectModulesDialog(selectModules)) {
                        return;
                    }
                    modules.addAll(selectModules.getSelectedModules());
                }
            }
            if (!modules.isEmpty()) {
                generate(modules);
            }
        }
    }

    public boolean showSelectModulesDialog(RadixdocSelectModulesPanel selectModules) {
        final ModalDisplayer selectModulesDisplayer = new ModalDisplayer(selectModules, "Select Modules");
        JButton okButton = getButtonByText(selectModulesDisplayer.getDialog(), "OK");
        selectModules.setOkButtonFromDialog(okButton);

        if (!selectModulesDisplayer.showModal()) {
            return false;
        }
        selectModules.setVisible(true);
        return true;
    }

    public JButton getButtonByText(Container c, String text) {
        Component[] components = c.getComponents();
        for (Component com : components) {
            if (com instanceof JButton) {
                if (((JButton) com).getText().equals(text)) {
                    return (JButton) com;
                }
            } else if (com instanceof Container) {
                JButton b = getButtonByText((Container) com, text);
                if (b != null) {
                    return b;
                }
            }
        }
        return null;
    }

    public boolean isAvailable() {
        return getGeneratorClass() != null;
    }

    public Class getGeneratorClass() {
        try {
            return Thread.currentThread().getContextClassLoader().loadClass(GENERATOR_CLASS_NAME);
        } catch (ClassNotFoundException ex) {
            return null;
        }
    }

    public Object createGenerator(List<Module> modules) {
        Class c = getGeneratorClass();
        if (c == null) {
            return null;

        }
        try {
            Constructor init = c.getConstructor(java.util.List.class
            );
            return init.newInstance(modules);
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            return null;
        }
    }

    public void generate(List<Module> modules) {
        Object generator = createGenerator(modules);
        if (generator != null) {
            try {
                Method method = generator.getClass().getMethod("generate");
                method.invoke(generator);
            } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                final StringWriter sw = new StringWriter();
                final PrintWriter pw = new PrintWriter(sw, true);
                ex.printStackTrace(pw);
                System.err.println(sw.getBuffer().toString());
            }
        }
    }

    @Override
    public String getName() {
        return "Generate Documentation";
    }

    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }
}
