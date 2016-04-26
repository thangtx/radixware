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

import java.util.Collections;
import java.util.List;
import org.openide.nodes.Node;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.exceptions.RadixObjectError;
import org.radixware.kernel.designer.ads.common.dialogs.ChooseDefinitionMembers;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.general.utils.RadixMutex;
import org.radixware.kernel.designer.tree.ads.nodes.actions.AdsDefinitionAction;


abstract class ClassInheritanceAction extends AdsDefinitionAction {

    public enum Target {

        CLASSES, METHODS, PROPERTIES, ENUM_FIELDS
    }

    protected static abstract class Cookie implements Node.Cookie {

        private final Object lock = new Object();

        public abstract boolean isReadOnly();

        public final void perform() {
            synchronized (lock) {
                try {
                    final List<AdsDefinition> select = select();

                    if (select != null && !select.isEmpty() && process(select)) {
                        RadixMutex.writeAccess(new Runnable() {

                            @Override
                            public void run() {
                                commit();
                            }
                        });
                    }
                } catch (RadixObjectError e) {
                    DialogUtils.messageError(new RadixObjectError(getErrorMessage(e), e));
                }
            }
        }

        private List<AdsDefinition> select() {
            ChooseDefinitionMembers.ChooseDefinitionMembersCfg config = createConfig();
            if (config == null) {
                DialogUtils.messageError("Nothing to override");
                return (List<AdsDefinition>) Collections.EMPTY_LIST;
            }
            return ChooseDefinitionMembers.choose(config);
        }

        protected void commit() {
        }

        protected abstract String getErrorMessage(RadixObjectError e);

        protected abstract boolean process(List<AdsDefinition> definitions);

        protected abstract ChooseDefinitionMembers.ChooseDefinitionMembersCfg createConfig();
    }

    @Override
    protected int mode() {
        return MODE_EXACTLY_ONE;
    }
}
