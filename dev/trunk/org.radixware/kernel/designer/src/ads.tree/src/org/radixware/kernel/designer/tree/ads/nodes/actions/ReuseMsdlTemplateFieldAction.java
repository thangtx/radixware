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
import java.util.List;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.actions.CookieAction;
import static org.openide.util.actions.CookieAction.MODE_EXACTLY_ONE;
import org.radixware.kernel.common.msdl.MsdlStructureField;
import org.radixware.kernel.common.msdl.MsdlVariantField;
import org.radixware.kernel.common.msdl.fields.AbstractFieldModel;
import org.radixware.kernel.designer.common.dialogs.utils.ModalDisplayer;
import org.radixware.kernel.designer.tree.ads.nodes.defs.msdl.MsdlFieldNode;
import org.radixware.kernel.designer.tree.ads.nodes.defs.msdl.misc.ReuseFieldsDialog;
import org.radixware.schemas.msdl.Field;


public class ReuseMsdlTemplateFieldAction extends CookieAction {
    //private ReuseFieldsDialog reuseFieldsDial = new ReuseFieldsDialog(null, true);

    public static class Cookie implements Node.Cookie {

        private MsdlFieldNode node;

        public Cookie(MsdlFieldNode node) {
            this.node = node;
        }

        public void perform() {
            ReuseFieldsDialog d = new ReuseFieldsDialog(node);

            ModalDisplayer.showModal(d);
            if (d.isAccepted()) {
                List<String> reusedFields = d.getReusedFieldsList();
                AbstractFieldModel afm = MsdlFieldNode.getFieldModel(node);
                if(afm.getField().isSetRemovedTemplateFields()) {
                    List<String> unusedFields = new ArrayList<>(afm.getField().getRemovedTemplateFields());
                    Iterator<String> unusedIterator = unusedFields.iterator();
                    while (unusedIterator.hasNext()) {
                        String unusedField = unusedIterator.next();
                        Iterator<String> reusedIterator = reusedFields.iterator();
                        while (reusedIterator.hasNext()) {
                            String reusedField = reusedIterator.next();
                            if (unusedField.equals(reusedField)) {
                                unusedIterator.remove();
                                reusedIterator.remove();
                                break;
                            }
                        }
                    }
                    afm.getField().setRemovedTemplateFields(unusedFields);
                    afm.setModified();
                }
            }
        }

        public boolean isEnabled() {
            if (node.getRadixObject() instanceof MsdlStructureField
                    || node.getRadixObject() instanceof MsdlVariantField) {
                AbstractFieldModel afm = MsdlFieldNode.getFieldModel(node);
                Field fullField = afm.getField();
                return fullField.isSetTemplateFieldPath() && 
                        fullField.isSetRemovedTemplateFields() &&
                        !fullField.getRemovedTemplateFields().isEmpty();
            }
            return false;
        }
    }

    @Override
    protected int mode() {
        return MODE_EXACTLY_ONE;
    }

    @Override
    protected Class<?>[] cookieClasses() {
        return new Class[]{ReuseMsdlTemplateFieldAction.Cookie.class};
    }

    @Override
    protected void performAction(Node[] nodes) {
        if (nodes.length == 0) {
            return;
        }
        Node n = nodes[0];
        ReuseMsdlTemplateFieldAction.Cookie c = n.getCookie(ReuseMsdlTemplateFieldAction.Cookie.class);
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

        ReuseMsdlTemplateFieldAction.Cookie c = n.getCookie(Cookie.class);
        if (c != null) {
            return c.isEnabled();
        }
        return false;
    }

    @Override
    public String getName() {
        return "Reuse removed template field";
    }

    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }
}
