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

package org.radixware.kernel.designer.ads.editors.module;

import java.awt.Image;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.SwingUtilities;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ObjectLink;
import org.radixware.kernel.common.defs.RadixObjectIcon;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.ads.module.AdsPath;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;


abstract class DefinitionNode extends AbstractNode {

    private final AdsPath path;
    private final AdsModule module;

    public DefinitionNode(AdsPath path, AdsModule module, Children children) {
        super(children);
        this.path = path;
        this.module = module;
    }

    private class DefLink extends ObjectLink<Definition> {

        @Override
        protected Definition search() {
            return path.resolve(module).get();
        }

        public Definition get() {
            Definition def = find();
            if (def == null) {
                def = update();
            }
            return def;
        }
    }
    final private DefLink link = new DefLink();

    @Override
    public String getDisplayName() {
        final Definition definition = getDefinition();
        return definition != null ? definition.getQualifiedName(module) : "<unresolved class: " + path.toString() + ">";
    }

    @Override
    public Image getIcon(int type) {
        final Definition definition = getDefinition();
        return definition != null ? definition.getIcon().getImage() : RadixObjectIcon.UNKNOWN.getImage();
    }

    @Override
    public Image getOpenedIcon(int type) {
        return getIcon(type);
    }

    @Override
    public Action getPreferredAction() {

        return new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                final Definition definition = getDefinition();

                if (definition != null) {
                    SwingUtilities.invokeLater(new Runnable() {

                        @Override
                        public void run() {
                            DialogUtils.goToObject(definition);
                        }
                    });
                }
            }
        };
    }

    protected final Definition getDefinition() {
        return link.get();
    }

    public AdsPath getDefinitionIdPath() {
        return path;
    }
}
