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

package org.radixware.kernel.designer.tree.ads.nodes.defs.msdl;

import java.util.List;
import javax.swing.Action;
import org.openide.nodes.Node;
import org.radixware.kernel.common.msdl.MsdlField;
import org.radixware.kernel.designer.common.general.nodes.INodeFactory;


public class SequenceItemNode extends MsdlFieldNode {

    public static final class Factory implements INodeFactory<MsdlField> {

        @Override
        public Node newInstance(MsdlField object) {
            return new SequenceItemNode(object);
        }
    }

    public SequenceItemNode(final MsdlField field) {
        super(field);
    }

    @Override
    public void addCustomActions(List<Action> actions) {
    }
}
