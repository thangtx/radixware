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

package org.radixware.kernel.designer.ads.common.dialogs;

import java.awt.EventQueue;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.designer.common.dialogs.utils.ModalDisplayer;


public class ChooseDefinitionMembers {

    private static class Displayer extends ModalDisplayer {

        public Displayer() {
            this(new ChooseDefinitionMembersPanel());
        }

        private Displayer(ChooseDefinitionMembersPanel panel) {
            super(panel);
            panel.displayer = this;
        }

        @Override
        protected void apply() {
        }

        @Override
        public ChooseDefinitionMembersPanel getComponent() {
            return (ChooseDefinitionMembersPanel) super.getComponent();
        }
    }

    public static abstract class ChooseDefinitionMembersCfg {

        boolean selfDisplaying = false;
        AdsDefinition initialDef;
        List<AdsDefinition> additionalDefinitions = null;

        public ChooseDefinitionMembersCfg(AdsDefinition initialDef) {
            this.initialDef = initialDef;
        }

        public ChooseDefinitionMembersCfg(AdsDefinition initialDef, boolean selfDisplaying) {
            this.selfDisplaying = selfDisplaying;
            this.initialDef = initialDef;
        }

        public ChooseDefinitionMembersCfg(AdsDefinition initialDef, boolean selfDisplaying, List<? extends AdsDefinition> additionalDefinitions) {
            this(initialDef, selfDisplaying);
            this.additionalDefinitions = additionalDefinitions == null ? null : Collections.unmodifiableList(additionalDefinitions);

        }
        
        public boolean incremental(){
            return true;
        }

        public abstract List<? extends AdsDefinition> listMembers(AdsDefinition def, boolean forOverwrite);

        public abstract List<? extends AdsDefinition> listBaseDefinitions(AdsDefinition def,Collection<AdsDefinition> seen);

        public abstract String getTitle();

        public boolean breakHierarchy(AdsDefinition def) {
            return false;
        }
    }

    public static final List<AdsDefinition> choose(final ChooseDefinitionMembersCfg cfg) {
        final List<AdsDefinition> list = new ArrayList<>();
        try {
            final Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    final Displayer displayer = new Displayer();

                    displayer.setTitle(cfg.getTitle());
                    displayer.getComponent().changeSupport.addChangeListener(new ChangeListener() {
                        @Override
                        public void stateChanged(ChangeEvent e) {
                            displayer.getDialogDescriptor().setValid(!displayer.getComponent().selection().isEmpty());
                        }
                    });
                    displayer.getComponent().open(cfg);
                    if (displayer.showModal()) {
                        list.addAll(displayer.getComponent().selection());
                    }
                }
            };

            if (!EventQueue.isDispatchThread()) {
                SwingUtilities.invokeAndWait(runnable);
            } else {
                runnable.run();
            }
        } catch (InterruptedException | InvocationTargetException ex) {
            Logger.getLogger(ChooseDefinitionMembers.class.getName()).log(Level.WARNING, null, ex);
        }
        return list;
    }
}
