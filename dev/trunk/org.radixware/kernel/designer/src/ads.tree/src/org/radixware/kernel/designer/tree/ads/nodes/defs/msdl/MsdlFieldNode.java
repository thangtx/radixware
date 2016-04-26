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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.Action;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.actions.SystemAction;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.msdl.MsdlField;
import org.radixware.kernel.common.msdl.fields.AbstractFieldModel;
import org.radixware.kernel.common.msdl.MsdlField.MsdlFieldStructureChangedEvent;
import org.radixware.kernel.common.msdl.MsdlField.MsdlFieldStructureChangedListener;
import org.radixware.kernel.common.msdl.RootMsdlScheme;
import org.radixware.kernel.common.msdl.fields.SequenceFieldModel;
import org.radixware.kernel.designer.common.annotations.registrators.NodeFactoryRegistration;
import org.radixware.kernel.designer.common.general.nodes.INodeFactory;
import org.radixware.kernel.designer.common.tree.RadixObjectNode;
import org.radixware.kernel.designer.common.tree.RadixObjectNodeDeleteAction;
import org.radixware.kernel.designer.tree.ads.nodes.actions.ReuseMsdlTemplateFieldAction;
import org.radixware.kernel.designer.tree.ads.nodes.actions.UnuseMsdlTemplateFieldAction;

public class MsdlFieldNode extends RadixObjectNode implements MsdlFieldStructureChangedListener {

    MsdlField msdlField;
    private static ExecutorService colorerService = Executors.newFixedThreadPool(3);
    private final UnuseMsdlTemplateFieldAction.Cookie unuseCookie;
    private final ReuseMsdlTemplateFieldAction.Cookie reuseCookie;

    @Override
    public void onEvent(MsdlFieldStructureChangedEvent e) {
        setChildren(msdlField.hasChildren() ? new MsdlFieldNodeCustomChildren(msdlField.getFieldModel()) : Children.LEAF);
    }

    @NodeFactoryRegistration
    public static final class Factory implements INodeFactory<MsdlField> {

        @Override
        public Node newInstance(MsdlField object) {
            if (object.getContainer() instanceof SequenceFieldModel) {
                return new SequenceItemNode(object);
            }
            return new MsdlFieldNode(object);
        }
    }

    public MsdlFieldNode(final MsdlField field) {
        super(field, field.hasChildren() ? new MsdlFieldNodeCustomChildren(field.getFieldModel()) : Children.LEAF);
        msdlField = field;
        addCookie(unuseCookie = new UnuseMsdlTemplateFieldAction.Cookie(this));
        addCookie(reuseCookie = new ReuseMsdlTemplateFieldAction.Cookie(this));
        msdlField.getStructureChangedSupport().addEventListener(this);
        colorerService.submit(new Runnable() {
            @Override
            public void run() {
                updateNodesColors();
            }
        });
    }

    public static AbstractFieldModel getFieldModel(MsdlFieldNode node) {
        return node.msdlField.getFieldModel();
    }

    @Override
    public void addCustomActions(List<Action> actions) {
        actions.add(null);
        actions.add(SystemAction.get(RadixObjectNodeDeleteAction.class));

        if (isTemplateNodeInTemplateInstance()) {
            actions.add(SystemAction.get(UnuseMsdlTemplateFieldAction.class));
        }
        if (getFieldModel(this).getField().isSetTemplateFieldPath()
                && getFieldModel(this).getField().isSetRemovedTemplateFields()) {
            actions.add(SystemAction.get(ReuseMsdlTemplateFieldAction.class));
        }
    }

    public boolean isTemplateNodeInTemplateInstance() {
        final Node n = getTemplateDescriptorNode();
        if (n != null) {
            if (n instanceof MsdlFieldNode) {
                final RadixObject currentMsdlField = msdlField;
                final RadixObject descrMsdlField = ((MsdlFieldNode) n).msdlField;
                RadixObject last = currentMsdlField;
                while (true) {
                    if (last instanceof RootMsdlScheme) {
                        return true;
                    }
                    if (last == descrMsdlField) {
                        return false;
                    }
                    last = last.getContainer();
                }
            }
        }
        return false;
    }

    public Node getTemplateDescriptorNode() {
        Node parent = acquireParentNode();
        while (true) {
            if (!(parent instanceof MsdlFieldNode)) {
                if ((parent instanceof MsdlStructureFieldsNode)
                        || (parent instanceof MsdlVariantFieldsNode)
                        || (parent instanceof MsdlStructureHeaderFieldsNode)) {
                    parent = parent.getParentNode();
                    continue;
                } else {
                    return null;
                }
            } else {
                if (parent instanceof RadixObjectNode
                        && ((RadixObjectNode) parent).getRadixObject() instanceof MsdlField) {
                    final MsdlField f = (MsdlField) ((RadixObjectNode) parent).getRadixObject();
                    if (f.getFieldModel().getField().isSetTemplateFieldPath()) {
                        return parent;
                    }
                }
                parent = parent.getParentNode();
            }
        }
    }

    public Node acquireParentNode() {
        Node parent = null;
        int count = 0;
        do {
            try {
                parent = getParentNode();
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                return null;
            }
            count++;
        } while (parent == null && count < 20);
        return parent;
    }

    void updateNodesColors() {
        if (isTemplateNodeInTemplateInstance()) {
            setGrayed(true);
        }
    }
}
