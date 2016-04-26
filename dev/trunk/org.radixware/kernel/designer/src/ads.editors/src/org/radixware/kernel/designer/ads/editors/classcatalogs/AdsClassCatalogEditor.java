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

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.*;
import org.openide.nodes.Node;
import org.openide.util.Mutex;
import org.openide.util.NbBundle;
import org.openide.util.RequestProcessor;
import org.radixware.kernel.common.defs.*;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.Inheritance.ClassHierarchySupport;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsClassCatalogDef;
import org.radixware.kernel.common.defs.ads.common.AdsVisitorProviders;
import org.radixware.kernel.common.defs.ads.module.AdsSearcher;
import org.radixware.kernel.common.enums.EClassType;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.common.annotations.registrators.EditorFactoryRegistration;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinition;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinitionCfg;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.editors.RadixObjectEditor;
import org.radixware.kernel.designer.common.general.editors.IEditorFactory;
import org.radixware.kernel.designer.common.general.editors.IRadixObjectEditor;
import org.radixware.kernel.designer.common.general.editors.OpenInfo;

public class AdsClassCatalogEditor extends RadixObjectEditor<AdsClassCatalogDef> {

    @EditorFactoryRegistration
    public static final class Factory implements IEditorFactory<AdsClassCatalogDef> {

        @Override
        public IRadixObjectEditor<AdsClassCatalogDef> newInstance(AdsClassCatalogDef classCatalog) {
            return new AdsClassCatalogEditor(classCatalog);
        }
    }

    private static Comparator<OrderedNode> ORDER_COMPARATOR = new Comparator<OrderedNode>() {

        @Override
        public int compare(OrderedNode o1, OrderedNode o2) {
            int result = o2.compareTo(o1);
            if (result == 0) {
                String cn1 = getComparableName(o1.rdx);
                String n1 = cn1 != null ? cn1 : "";
                String cn2 = getComparableName(o2.rdx);
                String n2 = cn2 != null ? cn2 : "";
                result = n1.compareTo(n2);
            }
            return result != 0 ? result : 1;
        }

        private String getComparableName(AdsClassCatalogDef.IClassCatalogItem rdx) {
            if (rdx instanceof AdsClassCatalogDef.Topic) {
                final AdsClassCatalogDef.Topic topic = (AdsClassCatalogDef.Topic) rdx;
                final String name = topic.getTitle(EIsoLanguage.ENGLISH);
                return (name != null ? name : "") + topic.getId();
            } else if (rdx instanceof AdsClassCatalogDef.ClassReference) {
                AdsClassDef ref = ((AdsClassCatalogDef.ClassReference) rdx).findReferencedClass();
                if (ref != null) {
                    return ref.getQualifiedName();
                }
            }
            return "";
        }
    };

    private class OrderedNode implements Comparable<OrderedNode> {

        AdsClassCatalogDef.IClassCatalogItem rdx;

        public OrderedNode(AdsClassCatalogDef.IClassCatalogItem rdx) {
            this.rdx = rdx;
        }

        @Override
        public int compareTo(OrderedNode o) {
            return Double.compare(o.rdx.getOrder(), rdx.getOrder());
        }

        @Override
        public String toString() {
            return rdx.toString();
        }
    }

    /**
     * Creates new form AdsClassCatalogEditorView
     */
    public AdsClassCatalogEditor(AdsClassCatalogDef classCatalog) {
        super(classCatalog);

        initComponents();

        ActionListener editListener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                ClassCatalogItemNode node = AdsClassCatalogEditor.this.navigator.getSelectedNode();
                if (node != null) {
                    RadixObject rdx = node.getRadixObject();
                    TitleModalEditor editor = new TitleModalEditor();
                    //if (rdx instanceof AdsClassCatalogDef.ClassReference) {
                    //    editor.editTitle(rdx, ((AdsClassCatalogDef.ClassReference) rdx).getOwnerClassCatalog(), false);
                    //} else {
                    if (rdx instanceof AdsClassCatalogDef.Topic) {
                        String oldName = node.getHtmlDisplayName();
                        editor.editTitle(rdx, ((AdsClassCatalogDef.Topic) rdx).getOwnerClassCatalog(), false);
                        node.doFireDisplayNameChange(oldName, node.getHtmlDisplayName());
                        //}
                    }
                }
            }
        };
        editBtn.addActionListener(editListener);

        ActionListener removeListener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                AdsClassCatalogEditor.this.remove();
            }
        };
        removeBtn.addActionListener(removeListener);

        ActionListener addTopicListener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                AdsClassCatalogEditor.this.addTopic();
            }
        };
        addTopicBtn.addActionListener(addTopicListener);
        ActionListener addClassListener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                AdsClassCatalogEditor.this.addClassReference();
            }
        };
        addClassBtn.addActionListener(addClassListener);

        ActionListener addChildTopicListener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                AdsClassCatalogEditor.this.addChildTopic();
            }
        };
        addChildTopicBtn.addActionListener(addChildTopicListener);
        ActionListener addChildRefListener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                AdsClassCatalogEditor.this.addChildClassReference();
            }
        };
        addChildClassBtn.addActionListener(addChildRefListener);

        PropertyChangeListener propertyListener = new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals("selectedNodes")) {
                    AdsClassCatalogEditor.this.onSelectionChange();
                }
            }
        };
        navigator.addPropertyChangeListener(propertyListener);
    }

    private void addChildClassReference() {
        ClassCatalogItemNode node = navigator.getSelectedNode();
        assert (node != null && node.getRadixObject() instanceof AdsClassCatalogDef.Topic);
        addChildRefToGivenNode(node);
    }

    private void addClassReference() {
        ClassCatalogItemNode selectedNode = navigator.getSelectedNode();
        ClassCatalogItemNode rootContext = (ClassCatalogItemNode) navigator.getExplorerManager().getRootContext();

        if (selectedNode != null && !selectedNode.getParentNode().equals(rootContext)) {
            addChildRefToGivenNode((ClassCatalogItemNode) selectedNode.getParentNode());
        } else {
            addChildRefToGivenNode(rootContext);
        }
    }

    private Collection<AdsEntityObjectClassDef> collectSutableClasses(final AdsClassCatalogDef catalog) {
        if (catalog.isInBranch()) {

            final Id contextEntityId = catalog.getOwnerClass().getEntityId();
            final ArrayList<AdsEntityObjectClassDef> classes = new ArrayList<>();
            final boolean isOvr = catalog instanceof AdsClassCatalogDef.Overwrite;

            final IVisitor visitor = new IVisitor() {

                @Override
                public void accept(RadixObject radixObject) {
                    classes.add((AdsEntityObjectClassDef) radixObject);
                }
            };

            final List<AdsClassDef> superClasses = collectSuperClasses(catalog.getOwnerClass());
            final Collection<AdsClassDef> directSubClasses = hSupport.findDirectSubclasses(catalog.getOwnerClass());

            final VisitorProvider provider = AdsVisitorProviders.newClassVisitorProvider(EnumSet.of(EClassType.ENTITY, EClassType.APPLICATION), new IFilter<AdsClassDef>() {

                @Override
                public boolean isTarget(AdsClassDef radixObject) {
                    AdsEntityObjectClassDef clazz = (AdsEntityObjectClassDef) radixObject;
                    if (clazz.getEntityId() == contextEntityId) {
                        if (clazz.getAccessFlags().isAbstract() || allReferences.contains(clazz.getId())) {
                            return false;
                        }
                        if (isOvr) {
                            return superClasses.contains(radixObject)
                                    || directSubClasses.contains(radixObject);
                        } else {
                            if (!(superClasses.contains(radixObject)
                                    || directSubClasses.contains(radixObject))) {
                                return false;
                            }

                            if (!clazz.isReadOnly()) {
                                AdsClassCatalogDef cc = clazz.getPresentations().getClassCatalogs().getLocal().findById(catalog.getId());
                                if (cc != null) {
                                    if (cc.isOverwrite()) {
                                        return false;
                                    } else {
                                        if (((AdsClassCatalogDef.Virtual) cc).isClassReferenceDefined()) {
                                            return false;
                                        } else {
                                            return true;
                                        }
                                    }
                                } else {
                                    return true;
                                }
                            } else {
                                return false;
                            }
                        }
                    } else {
                        return false;
                    }
                }
            });
            if (isOvr) {
                Layer.HierarchyWalker.walk(catalog.getModule().getSegment().getLayer(), new Layer.HierarchyWalker.Acceptor<Object>() {

                    @Override
                    public void accept(HierarchyWalker.Controller<Object> controller, Layer layer) {
                        layer.visit(visitor, provider);
                    }
                });

            } else {
                Branch branch = catalog.getModule().getSegment().getLayer().getBranch();
                branch.visit(visitor, provider);
            }
            if (!this.getClassCatalog().getOwnerClass().getAccessFlags().isAbstract()
                    && !allReferences.contains(this.getClassCatalog().getOwnerClass().getId())) {
                classes.add(this.getClassCatalog().getOwnerClass());
            }
            return classes;
        } else {
            return Collections.emptyList();
        }
    }

    private List<AdsClassDef> collectSuperClasses(AdsClassDef clazz) {
        LinkedList<AdsClassDef> superClazzes = new LinkedList<>();
        AdsClassDef superClassRef = clazz.getInheritance().findSuperClass().get();
        while (superClassRef != null
                && superClassRef instanceof AdsEntityObjectClassDef) {
            superClazzes.add(superClassRef);
            superClassRef = superClassRef.getInheritance().findSuperClass().get();
        }
        return superClazzes;
    }

    private void addChildRefToGivenNode(ClassCatalogItemNode node) {
        AdsClassCatalogDef classCatalog = getClassCatalog();

        if (classCatalog instanceof AdsClassCatalogDef.Overwrite) {
            Collection<AdsEntityObjectClassDef> collected = collectSutableClasses(classCatalog);
            ChooseDefinitionCfg cfg = ChooseDefinitionCfg.Factory.newInstance(collected);
            List<Definition> defs = ChooseDefinition.chooseDefinitions(cfg);
            if (defs != null && defs.size() > 0) {
                if (defs.size() == 1) {
                    AdsClassCatalogDef.ClassReference newReference = AdsClassCatalogDef.ClassReference.Factory.newInstance((AdsEntityObjectClassDef) defs.get(0));
                    if (node.getRadixObject() instanceof AdsClassCatalogDef.Topic) {
                        newReference.setParentTopicId(((AdsClassCatalogDef.Topic) node.getRadixObject()).getId());
                    }
                    setupOrderForItem(newReference, node, node.getNodesCount(), true);
                    allReferences.add(defs.get(0).getId());
                } else {
                    for (Definition d : defs) {
                        AdsClassCatalogDef.ClassReference newReference = AdsClassCatalogDef.ClassReference.Factory.newInstance((AdsEntityObjectClassDef) d);
                        if (node.getRadixObject() instanceof AdsClassCatalogDef.Topic) {
                            newReference.setParentTopicId(((AdsClassCatalogDef.Topic) node.getRadixObject()).getId());
                        }
                        setupOrderForItem(newReference, node, node.getNodesCount(), false);
                        allReferences.add(d.getId());
                    }
                }
            }
        } else {
            ChooseDefinitionCfg cfg = ChooseDefinitionCfg.Factory.newInstance(collectSutableClasses(classCatalog));
            List<Definition> defs = ChooseDefinition.chooseDefinitions(cfg);
            if (defs != null && defs.size() > 0) {
                for (Definition def : defs) {
                    AdsClassCatalogDef newCatalogRef = null;
                    if (getClassCatalog().getOwnerClass() == def) {
                        newCatalogRef = classCatalog;
                    } else {
                        if (!changeConfirmation()) {
                            return;
                        }

                        Definitions<AdsClassCatalogDef> localCatalogs = ((AdsEntityObjectClassDef) def).getPresentations().getClassCatalogs().getLocal();
                        Id currentId = classCatalog.getId();
                        AdsClassCatalogDef localCurrentCatalog = localCatalogs.findById(currentId);
                        if (localCurrentCatalog == null) {
                            newCatalogRef = AdsClassCatalogDef.Factory.newOverride(classCatalog);
                            localCatalogs.add(newCatalogRef);
                        } else {
                            newCatalogRef = localCurrentCatalog;
                        }
                    }
                    ((AdsClassCatalogDef.Virtual) newCatalogRef).defineClassReference();
                    if (node.getRadixObject() instanceof AdsClassCatalogDef.Topic) {
                        ((AdsClassCatalogDef.Virtual) newCatalogRef).getClassReference().setParentTopicId(((AdsClassCatalogDef.Topic) node.getRadixObject()).getId());
                    }
                    setupOrderForChildItem(((AdsClassCatalogDef.Virtual) newCatalogRef).getClassReference(), node);
                    ((ItemChildren) node.getChildren()).addNodeAt(((AdsClassCatalogDef.Virtual) newCatalogRef).getClassReference(), node.getNodesCount());
                    navigator.setSelectedNodes(new Node[]{node.getNodeAt(node.getNodesCount() - 1)});
                }
            }
        }
    }

    private void addChildTopic() {
        ClassCatalogItemNode node = navigator.getSelectedNode();
        assert (node != null && node.getRadixObject() instanceof AdsClassCatalogDef.Topic);
        addChildToGivenTopic(node);
    }

    private void addTopic() {
        ClassCatalogItemNode node = navigator.getSelectedNode();
        if (node != null) {
            assert (node.getParentNode() != null);
            if (!node.getParentNode().equals(navigator.getExplorerManager().getRootContext())) {
                ClassCatalogItemNode parent = (ClassCatalogItemNode) node.getParentNode();
                addChildToGivenTopic(parent);
            } else {
                addChildToGivenTopic((ClassCatalogItemNode) navigator.getExplorerManager().getRootContext());
            }
        } else {
            addChildToGivenTopic((ClassCatalogItemNode) navigator.getExplorerManager().getRootContext());
        }
    }

    private void addChildToGivenTopic(ClassCatalogItemNode node) {
        AdsClassCatalogDef.Topic newTopic = AdsClassCatalogDef.Topic.Factory.newInstance();
        if (node.getRadixObject() instanceof AdsClassCatalogDef.Topic) {
            newTopic.setParentTopicId(((AdsClassCatalogDef.Topic) node.getRadixObject()).getId());
        }

        int index = node.getNodesCount();
        setupOrderForItem(newTopic, node, index, true);
    }

    private void insertNewItemInto(ClassCatalogItemNode parent, int index, AdsClassCatalogDef.IClassCatalogItem item, boolean editTitle) {
        final AdsClassCatalogDef classCatalog = getClassCatalog();
        TitleModalEditor editor = new TitleModalEditor();
        boolean titleAccepted = true;
        if (editTitle) {
            titleAccepted = editor.editTitle((RadixObject) item, classCatalog, true);
        }
        if ((editTitle && titleAccepted) || !editTitle) {
            if (item instanceof AdsClassCatalogDef.ClassReference) {
                AdsDefinition classRef = AdsSearcher.Factory.newAdsClassSearcher(classCatalog).findById(((AdsClassCatalogDef.ClassReference) item).getClassId()).get();
                if (classRef != null && classRef.equals(classCatalog.getOwnerClass())) {
                    if (classCatalog instanceof AdsClassCatalogDef.Virtual) {
                        AdsClassCatalogDef.ClassReference newReference = ((AdsClassCatalogDef.Virtual) classCatalog).defineClassReference();
                        newReference.inheritTitle(editor.isTitleInherited());
                        if (!editor.isTitleInherited()) {
                            newReference.setTitleId(editor.getNewTitleId());
                        }
                        newReference.setParentTopicId(item.getParentTopicId());
                        newReference.setOrder(item.getOrder());
                        item = newReference;
                    } else {
                        ((AdsClassCatalogDef.Overwrite) classCatalog).getClassReferences().add((AdsClassCatalogDef.ClassReference) item);
                    }
                } else {
                    ((AdsClassCatalogDef.Overwrite) classCatalog).getClassReferences().add((AdsClassCatalogDef.ClassReference) item);
                }
            } else {
                classCatalog.getTopics().add((AdsClassCatalogDef.Topic) item);
            }

            ((ItemChildren) parent.getChildren()).addNodeAt((RadixObject) item, index);

            navigator.setSelectedNodes(new Node[]{parent.getNodeAt(index)});
        }
    }
    private final double ORDER_VALUE = 10D;

    private void setupOrderForItem(AdsClassCatalogDef.IClassCatalogItem item, ClassCatalogItemNode node, int index, boolean editTitle) {
        if (node.equals(navigator.getSelectedNode())) {
            setupOrderForChildItem(item, node);
            insertNewItemInto(node, index, item, editTitle);
        } else {
            ClassCatalogItemNode selectedNode = navigator.getSelectedNode();
            if (selectedNode == null) {
                setupOrderForChildItem(item, node);
                insertNewItemInto(node, index, item, editTitle);
            } else {
                AdsClassCatalogDef.IClassCatalogItem selectedUsr = (AdsClassCatalogDef.IClassCatalogItem) selectedNode.getRadixObject();
                RadixObject nextNode = ((ItemChildren) node.getChildren()).getChildAfter((RadixObject) selectedUsr);
                if (nextNode != null) {
                    AdsClassCatalogDef.IClassCatalogItem nextUsr = (AdsClassCatalogDef.IClassCatalogItem) nextNode;
                    if (nextUsr != null) {
                        assert (nextUsr.getOrder() >= nextUsr.getOrder());

                        double order = (nextUsr.getOrder() - selectedUsr.getOrder()) / 2 + selectedUsr.getOrder();
                        item.setOrder(order);
                        index = ((ItemChildren) node.getChildren()).getChildIndex(selectedNode) + 1;
                        insertNewItemInto(node, index, item, editTitle);
                    } else {
                        item.setOrder(selectedUsr.getOrder() + ORDER_VALUE);
                        insertNewItemInto(node, index, item, editTitle);
                    }
                } else {
                    item.setOrder(selectedUsr.getOrder() + ORDER_VALUE);
                    insertNewItemInto(node, index, item, editTitle);
                }

            }
        }
    }

    private void setupOrderForChildItem(AdsClassCatalogDef.IClassCatalogItem newItem, ClassCatalogItemNode node) {
        if (node.getNodesCount() > 0) {
            AdsClassCatalogDef.IClassCatalogItem usr = (AdsClassCatalogDef.IClassCatalogItem) (node.getNodeAt(node.getNodesCount() - 1)).getRadixObject();
            newItem.setOrder(usr.getOrder() + ORDER_VALUE);
        } else {
            if (node.equals((ClassCatalogItemNode) navigator.getExplorerManager().getRootContext())) {
                newItem.setOrder(10D);
            } else {
                AdsClassCatalogDef.IClassCatalogItem usr = (AdsClassCatalogDef.IClassCatalogItem) node.getRadixObject();
                newItem.setOrder(usr.getOrder() + ORDER_VALUE);
            }
        }
    }

    private void remove() {
        ClassCatalogItemNode node = navigator.getSelectedNode();
        if (node != null) {
            assert (node.getParentNode() != null);
            RadixObject item = node.getRadixObject();
            if (getClassCatalog() instanceof AdsClassCatalogDef.Overwrite) {
                actuallyRemove(node);
            } else if (item instanceof AdsClassCatalogDef.ClassReference) {

                AdsClassCatalogDef.ClassReference classReference = (AdsClassCatalogDef.ClassReference) item;
                ItemChildren children = (ItemChildren) node.getParentNode().getChildren();
                Id toDelete = classReference.getClassId();

                if (classReference.findReferencedClass().equals(getClassCatalog().getOwnerClass())) {
                    ((AdsClassCatalogDef.Virtual) getClassCatalog()).undefClassReference();
                    children.removeNodes(new Node[]{node});
                } else {
                    if (!changeConfirmation()) {
                        return;
                    }

                    ((AdsClassCatalogDef.Virtual) classReference.getOwnerClassCatalog()).undefClassReference();
                    children.removeNodes(new Node[]{node});
                }
                allReferences.remove(toDelete);
            } else {
                actuallyRemove(node);
            }
        }
    }

    private boolean changeConfirmation() {
        return DialogUtils.messageConfirmation(NbBundle.getMessage(AdsClassCatalogEditor.class, "ChangeConfirmationMessage"));
    }

    private void actuallyRemove(ClassCatalogItemNode node) {
        Id toDelete = null;
        if (node.getRadixObject() instanceof AdsClassCatalogDef.ClassReference) {
            toDelete = ((AdsClassCatalogDef.ClassReference) node.getRadixObject()).getClassId();
        }
        if (node.getRadixObject().delete()) {
            ItemChildren children = (ItemChildren) node.getParentNode().getChildren();
            children.removeNodes(new Node[]{node});

            if (toDelete != null) {
                allReferences.remove(toDelete);
            }
        } else {
            DialogUtils.messageError(NbBundle.getMessage(AdsClassCatalogEditor.class, "Errors-Delete"));
        }
    }

    private void onSelectionChange() {

        ClassCatalogItemNode node = navigator.getSelectedNode();
        if (node != null) {
            boolean enable = true;
            final RadixObject rdx = node.getRadixObject();
            AdsClassDef ownerClass = getClassCatalog().getOwnerClass();
            if (rdx instanceof AdsClassCatalogDef.Topic) {
                AdsClassCatalogDef.Topic topic = (AdsClassCatalogDef.Topic) rdx;
                enable = !topic.isReadOnly()
                        && topic.getOwnerClass().equals(ownerClass);
                removeBtn.setEnabled(!readonly && enable && node.getNodesCount() == 0);
            } else {
                AdsClassCatalogDef.ClassReference reference = (AdsClassCatalogDef.ClassReference) rdx;
                enable = !reference.isReadOnly();
//                    && reference.getOwnerClass().equals(ownerClass);
                removeBtn.setEnabled(!readonly && enable);
            }

            editBtn.setEnabled(!readonly && enable);

        } else {
            removeBtn.setEnabled(false);
            editBtn.setEnabled(false);
        }
        boolean isTopicNode = node != null && node.getRadixObject() instanceof AdsClassCatalogDef.Topic;
        boolean childCanBeAdded = isTopicNode
                && node.canBeAddedToCurrentTopic(((AdsClassCatalogDef.Topic) node.getRadixObject()).getOwnerClass(), getClassCatalog().getOwnerClass());

        if (node == null) {
            addClassBtn.setEnabled(!readonly);
            addTopicBtn.setEnabled(!readonly);
        } else {
            if (node.getParentNode() != null) {
                if (((ClassCatalogItemNode) node.getParentNode()).getRadixObject() instanceof AdsClassCatalogDef) {
                    addClassBtn.setEnabled(!readonly);
                    addTopicBtn.setEnabled(!readonly);
                } else {
                    ClassCatalogItemNode parentnode = (ClassCatalogItemNode) node.getParentNode();
                    AdsClassCatalogDef.Topic parenttopic = (AdsClassCatalogDef.Topic) parentnode.getRadixObject();
                    boolean canAddToCurrentTopic = parentnode.canBeAddedToCurrentTopic(parenttopic.getOwnerClass(), getClassCatalog().getOwnerClass());
                    addClassBtn.setEnabled(!readonly && canAddToCurrentTopic);
                    addTopicBtn.setEnabled(!readonly && canAddToCurrentTopic);
                }
            } else {
                addClassBtn.setEnabled(!readonly);
                addTopicBtn.setEnabled(!readonly);
            }
        }

        addChildTopicBtn.setEnabled(!readonly && childCanBeAdded);
        addChildClassBtn.setEnabled(!readonly && childCanBeAdded);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        contentPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        waitPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        navigator = new org.radixware.kernel.designer.ads.editors.classcatalogs.ClassCatalogsNavigatorPanel();
        jPanel1 = new javax.swing.JPanel();
        addChildClassBtn = new javax.swing.JButton();
        addTopicBtn = new javax.swing.JButton();
        addChildTopicBtn = new javax.swing.JButton();
        addClassBtn = new javax.swing.JButton();
        editBtn = new javax.swing.JButton();
        removeBtn = new javax.swing.JButton();
        overPanel = new org.radixware.kernel.designer.ads.editors.classcatalogs.OverridenCatalogPanel();

        setLayout(new java.awt.GridBagLayout());

        contentPanel.setLayout(new java.awt.CardLayout());

        waitPanel.setLayout(new java.awt.BorderLayout());

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText(org.openide.util.NbBundle.getMessage(AdsClassCatalogEditor.class, "PleaseWaitTip")); // NOI18N
        waitPanel.add(jLabel1, java.awt.BorderLayout.CENTER);

        jScrollPane1.setViewportView(waitPanel);

        contentPanel.add(jScrollPane1, "card2");

        jScrollPane2.setViewportView(navigator);

        contentPanel.add(jScrollPane2, "card3");

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(12, 12, 12, 12);
        add(contentPanel, gridBagConstraints);

        jPanel1.setLayout(new java.awt.GridBagLayout());

        addChildClassBtn.setIcon(RadixWareIcons.CREATE.ADD.getIcon(13, 13));
        addChildClassBtn.setText(org.openide.util.NbBundle.getMessage(AdsClassCatalogEditor.class, "ClassCatalogBtns-AddChildReference")); // NOI18N
        addChildClassBtn.setEnabled(false);
        addChildClassBtn.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 8, 0);
        jPanel1.add(addChildClassBtn, gridBagConstraints);

        addTopicBtn.setIcon(RadixWareIcons.CREATE.ADD.getIcon(13, 13));
        addTopicBtn.setText(org.openide.util.NbBundle.getMessage(AdsClassCatalogEditor.class, "ClassCatalogBtns-Add")); // NOI18N
        addTopicBtn.setEnabled(false);
        addTopicBtn.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 8, 0);
        jPanel1.add(addTopicBtn, gridBagConstraints);

        addChildTopicBtn.setIcon(RadixWareIcons.CREATE.ADD.getIcon(13, 13));
        addChildTopicBtn.setText(org.openide.util.NbBundle.getMessage(AdsClassCatalogEditor.class, "ClassCatalogBtns-AddChildTopic")); // NOI18N
        addChildTopicBtn.setEnabled(false);
        addChildTopicBtn.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 8, 0);
        jPanel1.add(addChildTopicBtn, gridBagConstraints);

        addClassBtn.setIcon(RadixWareIcons.CREATE.ADD.getIcon(13, 13));
        addClassBtn.setText(org.openide.util.NbBundle.getMessage(AdsClassCatalogEditor.class, "ClassCatalogBtns-AddRef")); // NOI18N
        addClassBtn.setEnabled(false);
        addClassBtn.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 8, 0);
        jPanel1.add(addClassBtn, gridBagConstraints);

        editBtn.setIcon(RadixWareIcons.EDIT.EDIT.getIcon(13, 13));
        editBtn.setText(org.openide.util.NbBundle.getMessage(AdsClassCatalogEditor.class, "ClassCatalogBtns-Edit")); // NOI18N
        editBtn.setEnabled(false);
        editBtn.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 8, 0);
        jPanel1.add(editBtn, gridBagConstraints);

        removeBtn.setIcon(RadixWareIcons.DELETE.DELETE.getIcon(13, 13));
        removeBtn.setText(org.openide.util.NbBundle.getMessage(AdsClassCatalogEditor.class, "ClassCatalogBtns-Remove")); // NOI18N
        removeBtn.setEnabled(false);
        removeBtn.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanel1.add(removeBtn, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(12, 0, 12, 12);
        add(jPanel1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 10);
        add(overPanel, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents
    private boolean readonly = false;

    public AdsClassCatalogDef getClassCatalog() {
        return getRadixObject();
    }
    private boolean expandProceeded = false;

    @Override
    public boolean open(OpenInfo info) {

        this.readonly = getClassCatalog().isReadOnly();
        expandProceeded = false;
        update();
        return super.open(info);
    }
    private Set<Id> allReferences;
    private Set<Id> allCollectedTopics;
    private Set<OrderedNode> allOrderedElements;
    private ClassHierarchySupport hSupport = new ClassHierarchySupport();
    private HashMap<RadixObject, Object> treeMap;
    private static RequestProcessor EDITOR_RP = new RequestProcessor(AdsClassCatalogEditor.class.getName());

//    static ClassHierarchySupport getClassHierarchySupport(){
//        return hSupport;
//    }
    @Override
    public void update() {
        overPanel.open(getClassCatalog());

        ((CardLayout) contentPanel.getLayout()).show(contentPanel, "card2");
        navigator.setEnabled(false);

        addTopicBtn.setEnabled(false);
        addClassBtn.setEnabled(false);
        editBtn.setEnabled(false);
        removeBtn.setEnabled(false);

        addChildTopicBtn.setEnabled(false);
        addChildClassBtn.setEnabled(false);

        hSupport = new ClassHierarchySupport();

        EDITOR_RP.post(new Runnable() {

            @Override
            public void run() {
                allOrderedElements = new TreeSet<>(ORDER_COMPARATOR);
                treeMap = new LinkedHashMap<>();
                allReferences = new HashSet<>();
                allCollectedTopics = new HashSet<>();
                updateContent();
                Mutex.EVENT.readAccess(new Runnable() {

                    @Override
                    public void run() {
                        AdsClassCatalogEditor.this.onSelectionChange();
                        ((CardLayout) contentPanel.getLayout()).show(contentPanel, "card3");
                        navigator.setEnabled(true);
                        if (!expandProceeded) {
                            navigator.expandOwnBranches();
                            expandProceeded = true;
                        }
                    }
                });
            }
        });

    }

    private void updateContent() {
        final AdsClassCatalogDef classCatalog = getClassCatalog();
        if (classCatalog.isOverwrite()) {
            for (AdsClassCatalogDef.Topic t : classCatalog.getTopicList()) {
                allOrderedElements.add(new OrderedNode(t));
                allCollectedTopics.add(t.getId());
            }
            for (AdsClassCatalogDef.ClassReference r : classCatalog.getClassRefList()) {
                allOrderedElements.add(new OrderedNode(r));
            }
        } else {
            collectAllTopicsAndRefs();
        }
        collectTreeMap();

        navigator.updateTree(classCatalog, treeMap, hSupport);
    }

    private void collectAllTopicsAndRefs() {
        AdsEntityObjectClassDef superClassRef = findUpperSuperClass(getClassCatalog().getOwnerClass());
        collectSubTopicsAndRefs(superClassRef);
    }

    private AdsEntityObjectClassDef findUpperSuperClass(AdsEntityObjectClassDef result) {
        AdsClassDef superClassRef = result.getInheritance().findSuperClass().get();
        if (superClassRef != null
                && superClassRef instanceof AdsEntityObjectClassDef) {
            return findUpperSuperClass((AdsEntityObjectClassDef) superClassRef);
        }
        return result;
    }

    private void collectSubTopicsAndRefs(AdsEntityObjectClassDef ownerClass) {
        final AdsClassCatalogDef classCatalog = ownerClass.getPresentations().getClassCatalogs().findById(getClassCatalog().getId(), EScope.LOCAL).get();
        if (classCatalog != null) {
            for (AdsClassCatalogDef.Topic topic : classCatalog.getTopicList()) {
                allOrderedElements.add(new OrderedNode(topic));
                allCollectedTopics.add(topic.getId());
            }
            for (AdsClassCatalogDef.ClassReference reference : classCatalog.getClassRefList()) {
                allOrderedElements.add(new OrderedNode(reference));
            }
        }
        final Collection<AdsClassDef> subClasses = hSupport.findDirectSubclasses(ownerClass);
        for (AdsClassDef subClass : subClasses) {
            assert (subClass instanceof AdsEntityObjectClassDef);
            collectSubTopicsAndRefs((AdsEntityObjectClassDef) subClass);
        }
    }

    private void collectTreeMap() {
        Set<OrderedNode> usedElements = new HashSet<>();

        for (OrderedNode t : allOrderedElements) {
            if (t.rdx instanceof AdsClassCatalogDef.Topic) {
                if (((AdsClassCatalogDef.Topic) t.rdx).getParentTopicId() == null) {
                    usedElements.add(t);
                    treeMap.put((RadixObject) t.rdx, new LinkedHashMap<RadixObject, Object>());
                } else if (((AdsClassCatalogDef.Topic) t.rdx).findParentTopic() == null
                        && ((AdsClassCatalogDef.Topic) t.rdx).getParentTopicId() != null) {
                    treeMap.put((RadixObject) t.rdx, new LinkedHashMap<RadixObject, Object>());
                }
            } else {
                allReferences.add(((AdsClassCatalogDef.ClassReference) t.rdx).getClassId());

                if (((AdsClassCatalogDef.ClassReference) t.rdx).getParentTopicId() == null) {
                    usedElements.add(t);
                    treeMap.put((RadixObject) t.rdx, null);
                } else if (((AdsClassCatalogDef.ClassReference) t.rdx).findParentTopic() == null
                        && ((AdsClassCatalogDef.ClassReference) t.rdx).getParentTopicId() != null) {
                    if (!allCollectedTopics.contains(((AdsClassCatalogDef.ClassReference) t.rdx).getParentTopicId())) {
                        treeMap.put((RadixObject) t.rdx, null);
                    }
                }
            }
        }
        allOrderedElements.removeAll(usedElements);

        collectTopicsToRefs(treeMap);

    }

    @SuppressWarnings("unchecked")
    private void collectTopicsToRefs(HashMap<RadixObject, Object> map) {
        if (allOrderedElements.size() > 0) {
            for (RadixObject rdx : map.keySet()) {
                if (rdx instanceof AdsClassCatalogDef.Topic) {
                    Set<OrderedNode> usedElements = new HashSet<>();
                    HashMap<RadixObject, Object> rdxMap = (HashMap<RadixObject, Object>) map.get(rdx);

                    for (OrderedNode t : allOrderedElements) {
                        if (t.rdx instanceof AdsClassCatalogDef.Topic) {
                            if (((AdsClassCatalogDef.Topic) t.rdx).getParentTopicId() == ((AdsClassCatalogDef.Topic) rdx).getId()) {
                                usedElements.add(t);
                                rdxMap.put((RadixObject) t.rdx, new LinkedHashMap<RadixObject, Object>());
                            }
                        } else {
                            allReferences.add(((AdsClassCatalogDef.ClassReference) t.rdx).getClassId());

                            if (((AdsClassCatalogDef.ClassReference) t.rdx).getParentTopicId() == ((AdsClassCatalogDef.Topic) rdx).getId()) {
                                usedElements.add(t);
                                rdxMap.put((RadixObject) t.rdx, null);
                            }
                        }
                    }
                    allOrderedElements.removeAll(usedElements);
                    collectTopicsToRefs(rdxMap);
                }
            }
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addChildClassBtn;
    private javax.swing.JButton addChildTopicBtn;
    private javax.swing.JButton addClassBtn;
    private javax.swing.JButton addTopicBtn;
    private javax.swing.JPanel contentPanel;
    private javax.swing.JButton editBtn;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private org.radixware.kernel.designer.ads.editors.classcatalogs.ClassCatalogsNavigatorPanel navigator;
    private org.radixware.kernel.designer.ads.editors.classcatalogs.OverridenCatalogPanel overPanel;
    private javax.swing.JButton removeBtn;
    private javax.swing.JPanel waitPanel;
    // End of variables declaration//GEN-END:variables
}
