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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.actions.CookieAction;
import org.radixware.kernel.common.msdl.fields.StructureFieldModel;
import org.radixware.kernel.designer.tree.ads.nodes.defs.msdl.MsdlFieldNode;
import org.radixware.kernel.designer.tree.ads.nodes.defs.msdl.MsdlStructureFieldsNode;
import java.util.List;
import org.radixware.kernel.common.msdl.fields.AbstractFieldModel;
import org.radixware.kernel.common.msdl.fields.ChoiceFieldModel;
import org.radixware.kernel.common.msdl.MsdlStructureField;
import org.radixware.kernel.common.msdl.MsdlVariantField;
import org.radixware.kernel.designer.common.dialogs.utils.ModalDisplayer;
import org.radixware.kernel.designer.tree.ads.nodes.defs.msdl.MsdlVariantFieldsNode;
import org.radixware.schemas.msdl.Field;


public class UnuseMsdlTemplateFieldAction extends CookieAction {

    public static class Cookie implements Node.Cookie {

        private MsdlFieldNode node;

        public Cookie(MsdlFieldNode node) {
            this.node = node;
        }

        public void perform() {
            Node templateInstanceNode = node.getTemplateDescriptorNode();
            if (templateInstanceNode instanceof MsdlFieldNode) {
                MsdlFieldNode mfn = (MsdlFieldNode) templateInstanceNode;
                AbstractFieldModel afm = null;
                if (mfn.getRadixObject() instanceof MsdlStructureField
                        || mfn.getRadixObject() instanceof MsdlVariantField) {
                    afm = MsdlFieldNode.getFieldModel(mfn);
                } else {
                    return;
                }
                List<String> hiddenFields = null;
                if (afm.getField().getRemovedTemplateFields() == null) {
                    hiddenFields = new ArrayList<>();
                } else {
                    hiddenFields = new ArrayList<>(afm.getField().getRemovedTemplateFields());
                }

                hiddenFields.add(node.getName());
                afm.getField().setRemovedTemplateFields(hiddenFields);
                afm.setModified();
            }
        }

        public boolean isEnabled() {
            return node.isTemplateNodeInTemplateInstance() && (node.getParentNode().getParentNode() == node.getTemplateDescriptorNode());
        }
    }

    @Override
    protected int mode() {
        return MODE_EXACTLY_ONE;
    }

    @Override
    protected Class<?>[] cookieClasses() {
        return new Class[]{Cookie.class};
    }

    @Override
    protected void performAction(Node[] nodes) {

        if (nodes.length == 0) {
            return;
        }
        Node n = nodes[0];
        Cookie c = n.getCookie(Cookie.class);
        if (c != null) {
            c.perform();
        }
    }

    @Override
    protected boolean enable(Node[] activatedNodes) {
        if (activatedNodes.length == 0) {
            return false;
        }
        Node n = activatedNodes[0];
        Cookie c = n.getCookie(Cookie.class);
        if (c != null) {
            return c.isEnabled();
        }


        return false;
    }

    @Override
    public String getName() {
        return "Unuse template field";
    }

    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }
}
