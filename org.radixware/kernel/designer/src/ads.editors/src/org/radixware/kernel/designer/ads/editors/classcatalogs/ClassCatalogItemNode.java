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

package org.radixware.kernel.designer.ads.editors.classcatalogs;

import java.awt.Color;
import java.awt.Image;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import javax.swing.Action;
import javax.swing.TransferHandler;
import org.openide.actions.EditAction;
import org.openide.cookies.EditCookie;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Node;
import org.openide.nodes.NodeTransfer;
import org.openide.util.NbBundle;
import org.openide.util.actions.SystemAction;
import org.openide.util.datatransfer.PasteType;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObjectIcon;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.Inheritance.ClassHierarchySupport;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsClassCatalogDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsClassCatalogDef.ClassReference;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsClassCatalogDef.IClassCatalogItem;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsClassCatalogDef.Topic;
import org.radixware.kernel.common.defs.ads.localization.AdsMultilingualStringDef;
import org.radixware.kernel.common.defs.ads.module.AdsSearcher;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.utils.events.IRadixEventListener;
import org.radixware.kernel.common.utils.events.RadixEvent;
import org.radixware.kernel.common.utils.events.RadixEventSource;
import org.radixware.kernel.designer.common.general.displaying.HtmlNameSupport;


public class ClassCatalogItemNode extends AbstractNode {

    class ItemNameSupport extends HtmlNameSupport {

        private RadixObject obj;

        public ItemNameSupport(RadixObject obj) {
            super(obj);
            this.obj = obj;
        }

        @Override
        public boolean isBold() {
            return ClassCatalogItemNode.this.inCurrentCatalog;
        }

        @Override
        public Color getColor() {
            if (getParentNode() != null) {
                if (obj instanceof IClassCatalogItem) {

                    if (obj instanceof ClassReference) {
                        AdsClassDef ref = ((ClassReference) obj).findReferencedClass();
                        if (ref != null
                                && ref.getAccessFlags().isAbstract()) {
                            return Color.RED;
                        }
                    }

                    ClassCatalogItemNode parent = (ClassCatalogItemNode) ClassCatalogItemNode.this.getParentNode();
                    if (parent.getRadixObject() instanceof AdsClassCatalogDef
                            && ((IClassCatalogItem) obj).getParentTopicId() != null) {
                        return Color.RED;
                    }

                    if (ClassCatalogItemNode.this.inOveriddenCatalog) {
                        return Color.LIGHT_GRAY;
                    }
                }
            }
            return super.getColor();
        }

        @Override
        public String getDisplayName() {
            if (obj != null) {
                if (obj instanceof ClassReference) {
                    AdsClassDef reference = ((ClassReference) obj).findReferencedClass();
                    if (reference != null) {
                        String toDisplay = reference.getName();
                        return toDisplay;
                    } else {
                        return "<broken reference: " + ((ClassReference) obj).getClassId() + ">";
                    }
                }

                if (obj instanceof Topic) {
                    Topic topic = (Topic) obj;
                    String title = topic.getTitleId() != null ? topic.getTitle(EIsoLanguage.ENGLISH) : "";
                    if (title == null) {
                        for (EIsoLanguage lang : EIsoLanguage.values()) {
                            title = topic.getTitle(lang);
                            if (title != null) {
                                break;
                            }
                        }
                    }
                    if (title != null && !title.isEmpty()) {
                        return title;
                    }

                    return "<Not Defined>";

                }
            }
            return super.getDisplayName();
        }
    }
    private RadixObject rdx;
    final static DataFlavor NODE_FLAVOR = new DataFlavor(ClassCatalogItemNode.class, "ClassCatalogItemNode");

    public ClassCatalogItemNode(boolean inOveridden, boolean inCurrentCatalog, boolean currentclassCatalogReadonly, RadixObject obj, org.openide.nodes.Children children, ClassHierarchySupport hSupport) {
        this(inOveridden, inCurrentCatalog, currentclassCatalogReadonly, obj, children, new InstanceContent(), hSupport);
    }
    private boolean inCurrentCatalog;
    private boolean inOveriddenCatalog;
    private ClassHierarchySupport hSupport;
    private boolean currentCatalogReadonly;

    public boolean getCurrentCatalogReaonly() {
        return currentCatalogReadonly;
    }

    public ClassCatalogItemNode(boolean inOveridden, boolean inCurrentCatalog, boolean currentClassCatalogReadonly, RadixObject obj, org.openide.nodes.Children children, InstanceContent content, ClassHierarchySupport hSupport) {
        super(children, new AbstractLookup(content));
        this.inCurrentCatalog = inCurrentCatalog;
        this.currentCatalogReadonly = currentClassCatalogReadonly;
        this.inOveriddenCatalog = inOveridden;
        this.hSupport = hSupport;
        if (!obj.isReadOnly() && inCurrentCatalog) {
            content.add(new EditCookie() {

                @Override
                public void edit() {
                    TitleModalEditor editor = new TitleModalEditor();
                    if (rdx instanceof ClassReference) {
                        editor.editTitle(rdx, ((ClassReference) rdx).getOwnerClassCatalog(), false);
                    } else {
                        String oldName = ClassCatalogItemNode.this.getHtmlDisplayName();
                        editor.editTitle(rdx, ((Topic) rdx).getOwnerClassCatalog(), false);
                        ClassCatalogItemNode.this.fireDisplayNameChange(oldName, getHtmlDisplayName());
                    }
                }
            });
            content.add(new DeleteClassCatalogItemAction.Cookie(this));
        }
        content.add(children);
        this.rdx = obj;
        if (rdx != null) {
            htmlNameSupport = new ItemNameSupport(obj);
            htmlNameSupport.addEventListener(htmlNameChangeListener);

            if (rdx instanceof ClassReference) {
                content.add(new OpenReferenceInEditorAction.Cookie(rdx));
                content.add(new SelectReferenceInTreeAction.Cookie(rdx));
            }
            if (!inCurrentCatalog) {
                content.add(new OpenOwnerClassCatalogAction.Cookie(rdx));
            }
        }
    }

    public void doFireDisplayNameChange(String old, String newName) {
        fireDisplayNameChange(old, newName);
    }

    public boolean isInCurrentCatalog() {
        return inCurrentCatalog;
    }

    @Override
    public final Action[] getActions(boolean forEditorTitle) {
        return new Action[]{SystemAction.get(EditAction.class), null,
                    SystemAction.get(OpenReferenceInEditorAction.class),
                    SystemAction.get(SelectReferenceInTreeAction.class),
                    SystemAction.get(OpenOwnerClassCatalogAction.class), null,
                    SystemAction.get(DeleteClassCatalogItemAction.class)};
    }

    @Override
    public Action getPreferredAction() {
        return SystemAction.get(EditAction.class);
    }

    public int getNodesCount() {
        if (this.getChildren() instanceof ItemChildren
                && ((ItemChildren) this.getChildren()).getChildrenMap() != null) {
            return ((ItemChildren) this.getChildren()).getChildCount();

        } else {
            return 0;

        }
    }

    public ClassCatalogItemNode getNodeAt(int index) {
        final int nodesCount = this.getNodesCount();
        if (nodesCount > 0
                && index > -1
                && index < nodesCount) {
            return (ClassCatalogItemNode) this.getChildren().getNodes()[index];
        } else {
            return null;
        }
    }

    public RadixObject getRadixObject() {
        return rdx;
    }

    @Override
    public String getHtmlDisplayName() {
        final Color baseColor = htmlNameSupport.getColor();
        String displayName = htmlNameSupport != null ? htmlNameSupport.getHtmlName() : "";
        if (rdx instanceof ClassReference) {
            AdsMultilingualStringDef stringDef = null;
            final ClassReference classRef = (ClassReference) rdx;
            final AdsDefinition context = classRef.getOwnerClassCatalog();
            final boolean isTitleInherited = classRef.isTitleInherited();
            if (isTitleInherited) {
                final AdsClassDef ref = AdsSearcher.Factory.newAdsClassSearcher(context).findById(classRef.getClassId()).get();
                if (ref != null) {
                    stringDef = ref.findLocalizedString(ref.getTitleId());
                }
            } else {
                stringDef = context.findLocalizedString(classRef.getTitleId());
            }
            if (stringDef != null) {
                for (EIsoLanguage lang : stringDef.getLanguages()) {
                    final Color c = baseColor == Color.RED || baseColor == Color.LIGHT_GRAY ? baseColor : (isTitleInherited ? Color.LIGHT_GRAY : Color.GRAY);
                    final String color = String.format("#%02x%02x%02x", c.getRed(), c.getGreen(), c.getBlue());
                    displayName += "   <font color='" + color + "'><i><b>" + lang.getName() + " " + NbBundle.getMessage(ClassCatalogItemNode.class, "ClassCatalogTitleTip") + "</b>: " + stringDef.getValue(lang) + "</i></font>";
                }
            }
        }
        return displayName;
    }

    private HtmlNameSupport htmlNameSupport;
    private IRadixEventListener<RadixEvent> htmlNameChangeListener = new IRadixEventListener<RadixEvent>() {

        @Override
        public void onEvent(RadixEvent arg0) {
            ClassCatalogItemNode.this.fireDisplayNameChange(null, null);
        }
    };

    @Override
    public Image getIcon(int type) {
        if (rdx != null) {
            if (rdx instanceof ClassReference) {
                AdsClassDef reference = ((ClassReference) rdx).findReferencedClass();
                if (reference != null) {
                    RadixIcon icon = reference.getIcon();
                    if (icon != null) {
                        return icon.getImage(16, 16);
                    }
                }
                return RadixObjectIcon.UNKNOWN.getImage(16, 16);
            }

            if (rdx instanceof Topic) {
                return ((Topic) rdx).getIcon().getImage(16, 16);
            }
        }
        return super.getIcon(type);
    }

    @Override
    public Image getOpenedIcon(int type) {
        return this.getIcon(type);
    }

    @Override
    public String getName() {
        return rdx != null ? rdx.getName() : "<Not Defined>";
    }

    @Override
    public String getDisplayName() {
        return rdx != null ? rdx.getQualifiedName() : "<Not Defined>";
    }

    @Override
    public String getShortDescription() {
        String toolTip = null;
        StringBuilder sb = new StringBuilder();
        if (rdx != null) {
            if (rdx instanceof ClassReference) {
                ClassReference reference = (ClassReference) rdx;
                AdsClassDef clRef = reference.findReferencedClass();
                if (clRef != null) {
                    toolTip = clRef.getToolTip();
                    if (clRef.getAccessFlags().isAbstract()) {

                        sb.append("<html>");
                        if (toolTip != null) {
                            sb.append("<br>");
                        }
                        sb.append("<HR>");
                        sb.append("<font color=\"red\">" + NbBundle.getMessage(ClassCatalogItemNode.class, "AbstractSelfReferenceErrorTip") + "</font>");
                    }
                }
            } else if (rdx instanceof Topic) {
                toolTip = ((Topic) rdx).getToolTip();
            }

            if (rdx instanceof IClassCatalogItem) {
                ClassCatalogItemNode parent = (ClassCatalogItemNode) this.getParentNode();
                if (parent.getRadixObject() instanceof AdsClassCatalogDef
                        && ((IClassCatalogItem) rdx).getParentTopicId() != null) {
                    sb.append("<html>");
                    if (toolTip != null) {
                        sb.append("<br>");
                    }
                    sb.append("<HR>");
                    sb.append("<font color=\"red\">" + NbBundle.getMessage(ClassCatalogItemNode.class, "ParentNotFoundErrorTip") + "</font>");
                }
            }
        }

// for debuging purposes
//        if (rdx instanceof IClassCatalogItem){
//            sb.append("<html>");
//            sb.append("<br><HR>");
//            sb.append(((IClassCatalogItem) rdx).getOrder());
//        }

        if (sb.toString() != null
                && !sb.toString().isEmpty()) {
            toolTip = toolTip + sb.toString();
        }
        return toolTip;
    }

    //TRANSFER SUPPORT
    @Override
    protected void createPasteTypes(final Transferable t, List<PasteType> types) {
        types.add(new PasteType() {

            @Override
            public Transferable paste() throws IOException {
                Node theDropNode = NodeTransfer.node(t, TransferHandler.COPY_OR_MOVE);
                if (!(theDropNode instanceof ClassCatalogItemNode)) {
                    return null;
                }
                ClassCatalogItemNode dropNode = (ClassCatalogItemNode) theDropNode;
                Node dropNodeParent = dropNode.getParentNode();

                if (!isChildBranch(dropNode, ClassCatalogItemNode.this)) {
                    if (rdx instanceof ClassReference) {
                        ClassCatalogItemNode parent = (ClassCatalogItemNode) getParentNode();
                        if (parent.getRadixObject() instanceof Topic) {
                            ((IClassCatalogItem) dropNode.getRadixObject()).setParentTopicId(((Topic) parent.getRadixObject()).getId());
                        } else {
                            ((IClassCatalogItem) dropNode.getRadixObject()).setParentTopicId(null);
                        }
                        int targetIndex = ((ItemChildren) parent.getChildren()).indexOf(ClassCatalogItemNode.this);

                        ((ItemChildren) parent.getChildren()).addNodeAt(dropNode.getRadixObject(), targetIndex);
                        ((ItemChildren) dropNodeParent.getChildren()).removeNodes(new Node[]{dropNode});
                    } else {
                        if (rdx instanceof Topic) {
                            ((IClassCatalogItem) dropNode.getRadixObject()).setParentTopicId(((Topic) rdx).getId());
                        } else {
                            ((IClassCatalogItem) dropNode.getRadixObject()).setParentTopicId(null);
                        }
                        if (dropIndex == -1 || dropIndex > (ClassCatalogItemNode.this.getNodesCount() - 1)) {
                            int childCount = ClassCatalogItemNode.this.getNodesCount();
                            double order = ClassCatalogItemNode.this.getNodesCount() != 0 ? ((IClassCatalogItem) ClassCatalogItemNode.this.getNodeAt(childCount - 1).getRadixObject()).getOrder() + 10D : 100D;
                            ((IClassCatalogItem) dropNode.getRadixObject()).setOrder(order);
                            ((ItemChildren) getChildren()).addNodes(new Node[]{dropNode});
                        } else {
                            ClassCatalogItemNode targetNode = ClassCatalogItemNode.this.getNodeAt(dropIndex);
                            double targetOrder = ((IClassCatalogItem) targetNode.getRadixObject()).getOrder();
                            double prevOrder = calculatePrevOrder(targetNode);
                            if (prevOrder == 0D) {
                                ((IClassCatalogItem) dropNode.getRadixObject()).setOrder(targetOrder == 0D ? -100D : targetOrder / 2);
                            } else {
                                if (targetOrder < 0 && prevOrder < 0) {
                                    ((IClassCatalogItem) dropNode.getRadixObject()).setOrder(-((Math.abs(prevOrder) - Math.abs(targetOrder)) / 2 + Math.abs(targetOrder)));
                                } else if (prevOrder < 0 && targetOrder > 0) {
                                    ((IClassCatalogItem) dropNode.getRadixObject()).setOrder(0D);
                                } else if (prevOrder < 0 && targetOrder == 0) {
                                    ((IClassCatalogItem) dropNode.getRadixObject()).setOrder(prevOrder / 2);
                                } else {
                                    ((IClassCatalogItem) dropNode.getRadixObject()).setOrder((targetOrder - prevOrder) / 2 + prevOrder);
                                }
                            }
                            ((ItemChildren) getChildren()).addNodeAt(dropNode, dropIndex);
                        }
                        ((ItemChildren) dropNodeParent.getChildren()).removeNodes(new Node[]{dropNode});
                    }
                }
                return null;
            }
        });
    }

    private boolean isChildBranch(ClassCatalogItemNode drop, ClassCatalogItemNode target) {
        if (!target.equals(drop)) {
            Node parentNode = target.getParentNode();
            if (parentNode instanceof ClassCatalogItemNode) {
                ClassCatalogItemNode parent = (ClassCatalogItemNode) parentNode;
                if (parent != null) {
                    return isChildBranch(drop, parent);
                }
                return false;
            } else {
                return false;
            }
        }
        return true;
    }

    private double calculatePrevOrder(ClassCatalogItemNode target) {
        ClassCatalogItemNode parent = (ClassCatalogItemNode) target.getParentNode();
        ClassCatalogItemNode prevNode = parent.getNodeAt(((ItemChildren) parent.getChildren()).indexOf(target) - 1);
        if (prevNode != null) {
            double prevOrder = ((AdsClassCatalogDef.IClassCatalogItem) prevNode.getRadixObject()).getOrder();
            if (prevOrder == ((AdsClassCatalogDef.IClassCatalogItem) target.getRadixObject()).getOrder()) {
                double pno = calculatePrevOrder(prevNode);
                assert (pno == 0D || pno < ((AdsClassCatalogDef.IClassCatalogItem) target.getRadixObject()).getOrder());

                if (pno == 0D) {
                    prevOrder = prevOrder / 2;
                } else {
                    prevOrder = (prevOrder - pno) / 2 + pno;
                }
                ((AdsClassCatalogDef.IClassCatalogItem) prevNode.getRadixObject()).setOrder(prevOrder);
            }
            return prevOrder;
        }
        return 0D;
    }
    private int dropIndex = -1;

    @Override
    public PasteType getDropType(Transferable t, int action, int index) {
        this.dropIndex = index;
        Node dn = NodeTransfer.node(t, TransferHandler.COPY_OR_MOVE);
        if (dn instanceof ClassCatalogItemNode) {
            ClassCatalogItemNode dropNode = (ClassCatalogItemNode) dn;
            if (rdx instanceof Topic
                    || (rdx instanceof ClassReference
                    && ((ClassCatalogItemNode) getParentNode()).getRadixObject() instanceof Topic)) {
                final RadixObject current = rdx instanceof Topic ? rdx : ((ClassCatalogItemNode) getParentNode()).getRadixObject();
                final RadixObject drop = dropNode.getRadixObject();

                AdsClassDef currentOwner = ((Topic) current).getOwnerClass();
                AdsClassDef dropOwner = drop instanceof ClassReference ? ((ClassReference) drop).getOwnerClass() : ((Topic) drop).getOwnerClass();
                if (!canBeAddedToCurrentTopic(currentOwner, dropOwner)) {
                    return null;
                }
            }
        }
        return super.getDropType(t, action, index);
    }

    boolean canBeAddedToCurrentTopic(final AdsClassDef currentOwner, AdsClassDef dropOwner) {
        if (currentOwner.equals(dropOwner)) {
            return true;
        }

        if (searchInSupers(currentOwner, dropOwner)) {
            return true;
        }

        if (searchAmongSubs(currentOwner, dropOwner)) {
            return true;
        }
        return false;
    }

    private boolean searchInSupers(final AdsClassDef currentOwner, AdsClassDef drop) {
        AdsClassDef superClass = drop.getInheritance().findSuperClass().get();
        if (superClass != null
                && superClass instanceof AdsEntityObjectClassDef) {
            boolean match = superClass.equals(currentOwner);
            if (!match) {
                return searchInSupers(currentOwner, superClass);
            } else {
                return true;
            }
        }
        return false;
    }

    private boolean searchAmongSubs(final AdsClassDef currenOwner, AdsClassDef sub) {
        boolean result = false;
        Collection<AdsClassDef> directSubs = hSupport.findDirectSubclasses(sub);
        for (AdsClassDef s : directSubs) {
            if (s.equals(currenOwner)) {
                result = true;
            } else {
                result = searchAmongSubs(currenOwner, s);
            }
        }

        return result;
    }

    @Override
    public Transferable clipboardCopy() throws IOException {
        getCopySupport().fireEvent(new ClassCatalogCopyEvent(this));
        return this.clipboardCut();
    }

    @Override
    public Transferable clipboardCut() throws IOException {
        return NodeTransfer.transferable(this, TransferHandler.COPY_OR_MOVE);
    }

    @Override
    public boolean canDestroy() {
        return !rdx.isReadOnly() && !currentCatalogReadonly;
    }

    @Override
    public boolean canCopy() {
        return !rdx.isReadOnly() && !currentCatalogReadonly;
    }

    @Override
    public boolean canCut() {
        return !rdx.isReadOnly() && !currentCatalogReadonly;
    }
    //COPY EVENT processing
    private ClassCatalogCopySupport copySupport;

    static class ClassCatalogCopyEvent extends RadixEvent {

        ClassCatalogItemNode dragged;

        public ClassCatalogCopyEvent(ClassCatalogItemNode dragged) {
            this.dragged = dragged;
        }
    }

    interface ClassCatalogCopyListener extends IRadixEventListener<ClassCatalogCopyEvent> {
    }

    static class ClassCatalogCopySupport extends RadixEventSource<ClassCatalogCopyListener, ClassCatalogCopyEvent> {
    }

    ClassCatalogCopySupport getCopySupport() {
        if (copySupport == null) {
            copySupport = new ClassCatalogCopySupport();
        }
        return copySupport;
    }
}
