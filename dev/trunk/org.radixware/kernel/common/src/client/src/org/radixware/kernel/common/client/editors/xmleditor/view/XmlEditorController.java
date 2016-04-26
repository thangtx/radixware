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
package org.radixware.kernel.common.client.editors.xmleditor.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SchemaTypeSystem;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;

import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.types.Icon;
import org.radixware.kernel.common.client.utils.XmlSchemaTypeSystemCompiler;
import org.radixware.kernel.common.client.views.IDialog;
import org.radixware.kernel.common.client.editors.xmleditor.IXmlSchemaProvider;
import org.radixware.kernel.common.client.editors.xmleditor.XmlEditorOperation;
import org.radixware.kernel.common.client.editors.xmleditor.model.EXmlItemType;
import org.radixware.kernel.common.client.editors.xmleditor.model.IXmlValueEditingOptionsProvider;
import org.radixware.kernel.common.client.editors.xmleditor.model.XmlDocument;
import org.radixware.kernel.common.client.editors.xmleditor.model.XmlDocumentItem;
import org.radixware.kernel.common.client.editors.xmleditor.model.XmlModelItem;
import org.radixware.kernel.common.client.editors.xmleditor.model.schema.XmlSchemaAttributeItem;
import org.radixware.kernel.common.client.editors.xmleditor.model.schema.XmlSchemaElementItem;
import org.radixware.kernel.common.client.editors.xmleditor.model.schema.XmlSchemaItem;
import org.radixware.kernel.common.client.editors.xmleditor.view.operations.ChangeXmlNameOperation;
import org.radixware.kernel.common.client.editors.xmleditor.view.operations.ChangeXmlValueOperation;
import org.radixware.kernel.common.client.editors.xmleditor.view.operations.ChoiceItemOperation;
import org.radixware.kernel.common.client.editors.xmleditor.view.operations.ChoiceRootOperation;
import org.radixware.kernel.common.client.editors.xmleditor.view.operations.CreateChildAttributeOperation;
import org.radixware.kernel.common.client.editors.xmleditor.view.operations.CreateChildElementOperation;
import org.radixware.kernel.common.client.editors.xmleditor.view.operations.CreateRootDocumentOperation;
import org.radixware.kernel.common.client.editors.xmleditor.view.operations.DeleteRootDocumentOperation;
import org.radixware.kernel.common.client.editors.xmleditor.view.operations.DeleteXmlNodeOperation;
import org.radixware.kernel.common.client.editors.xmleditor.view.operations.MoveXmlOperation;
import org.radixware.kernel.common.client.editors.xmleditor.view.operations.XmlOperation;
import org.radixware.kernel.common.client.widgets.actions.Action;

public final class XmlEditorController {

    private static class XmlEditorIcons extends ClientIcon {

        private XmlEditorIcons(final String fileName) {
            super(fileName, true);
        }
        public static final ClientIcon CREATE_XML_ATTRIBUTE = new XmlEditorIcons("classpath:images/create_xml_attribute.svg");
        public static final ClientIcon VALIDATE = new XmlEditorIcons("classpath:images/validate.svg");
        public static final ClientIcon TREE_MODE = new XmlEditorIcons("classpath:images/tree.svg");
        public static final ClientIcon TEXT_MODE = new XmlEditorIcons("classpath:images/text.svg");
        public static final ClientIcon PRETTY_TEXT_MODE = new XmlEditorIcons("classpath:images/pretty_text.svg");
        public static final ClientIcon SCHEMA_TEXT_MODE = new XmlEditorIcons("classpath:images/xml.svg");
    }

    public static enum EXmlEditorMode {

        XmlTreeMode, XmlTextMode, XmlPrettyTextMode, XmlSchemaMode
    }

    private final IClientEnvironment environment;
    private final IXmlEditorPresenter presenter;
    private final IXmlSchemaProvider schemaProvider;
    private final IXmlValueEditingOptionsProvider valueEditingOptionsProvider;
    private Action openAction;
    private Action saveAsAction;
    private Action createElementAction;
    private Action createAttributeAction;
    private Action renameNodeAction;
    private Action deleteNodeAction;
    private Action moveUpAction;
    private Action moveDownAction;
    private Action undoAction;
    private Action redoAction;
    private Action choiceAction;
    private Action validateAction;
    private Action treeModeAction;
    private Action textModeAction;
    private Action prettyTextModeAction;
    private Action xmlSchemeModeAction;
    private Action copyAction;
    private Action cutAction;
    private Action pasteAction;
    private final Stack<XmlOperation> undoStack = new Stack<>();
    private final Stack<XmlOperation> redoStack = new Stack<>();
    private final EnumSet<XmlEditorOperation> disabledOperations = EnumSet.noneOf(XmlEditorOperation.class);
    private XmlDocument document;
    private String scheme;
    private SchemaTypeSystem typeSystem;
    private boolean isReadOnly;
    private boolean wasModified;
    private EXmlEditorMode currentMode;
    private boolean isConform = true;

    public XmlEditorController(final IClientEnvironment environment,
            final IXmlEditorPresenter presenter,
            final IXmlValueEditingOptionsProvider valueEditingOptionsProvider,
            final SchemaTypeSystem schemaTypeSystem,
            final boolean isReadOnly,
            final IXmlSchemaProvider schemaProvider) {
        this.environment = environment;
        this.presenter = presenter;
        this.schemaProvider = schemaProvider;
        if (valueEditingOptionsProvider == null) {
            this.valueEditingOptionsProvider = new DefaultXmlValueEditingOptionsProvider(environment);
        } else {
            this.valueEditingOptionsProvider = valueEditingOptionsProvider;
        }
        this.isReadOnly = isReadOnly;
        typeSystem = schemaTypeSystem;
        setupUi();
    }

    public XmlEditorController(final IClientEnvironment environment,
            final IXmlEditorPresenter presenter,
            final IXmlValueEditingOptionsProvider valueEditingOptionsProvider,
            final String scheme,
            final boolean isReadOnly,
            final IXmlSchemaProvider schemaProvider) {
        this.environment = environment;
        this.presenter = presenter;
        this.schemaProvider = schemaProvider;
        if (valueEditingOptionsProvider == null) {
            this.valueEditingOptionsProvider = new DefaultXmlValueEditingOptionsProvider(environment);
        } else {
            this.valueEditingOptionsProvider = valueEditingOptionsProvider;
        }
        this.scheme = scheme;
        this.isReadOnly = isReadOnly;
        setupUi();
    }

    private void setupUi() {
        currentMode = EXmlEditorMode.XmlTreeMode;
        openAction = presenter.createOpenAction(environment.getMessageProvider().translate("XmlEditor", "Open"), ClientIcon.CommonOperations.OPEN);
        saveAsAction = createAction(environment.getMessageProvider().translate("XmlEditor", "Save as..."), ClientIcon.CommonOperations.SAVE);
        saveAsAction.addActionListener(new Action.ActionListener() {
            @Override
            public void triggered(Action action) {
                presenter.saveXmlDocument(document, false, currentMode == EXmlEditorMode.XmlTreeMode, currentMode == EXmlEditorMode.XmlPrettyTextMode);
            }
        });
        createElementAction = createAction(environment.getMessageProvider().translate("XmlEditor", "Create Element"), ClientIcon.CommonOperations.CREATE);
        createElementAction.setEnabled(!isReadOnly);
        createElementAction.addActionListener(new Action.ActionListener() {
            @Override
            public void triggered(Action action) {
                createElementAction();
            }
        });
        createAttributeAction = createAction(environment.getMessageProvider().translate("XmlEditor", "Create Attribute"), XmlEditorIcons.CREATE_XML_ATTRIBUTE);
        createAttributeAction.addActionListener(new Action.ActionListener() {
            @Override
            public void triggered(Action action) {
                createAttributeAction();
            }
        });
        renameNodeAction = createAction(environment.getMessageProvider().translate("XmlEditor", "Rename Node"), ClientIcon.ValueTypes.STR);
        renameNodeAction.addActionListener(new Action.ActionListener() {
            @Override
            public void triggered(Action action) {
                renameNodeAction();
            }
        });
        deleteNodeAction = createAction(environment.getMessageProvider().translate("XmlEditor", "Delete Node"), ClientIcon.CommonOperations.DELETE);
        deleteNodeAction.addActionListener(new Action.ActionListener() {
            @Override
            public void triggered(Action action) {
                deleteNodeAction();
            }
        });
        moveUpAction = createAction(environment.getMessageProvider().translate("XmlEditor", "Move Up"), ClientIcon.CommonOperations.UP);
        moveUpAction.addActionListener(new Action.ActionListener() {
            @Override
            public void triggered(Action action) {
                moveUpAction();
            }
        });
        moveDownAction = createAction(environment.getMessageProvider().translate("XmlEditor", "Move Down"), ClientIcon.CommonOperations.DOWN);
        moveDownAction.addActionListener(new Action.ActionListener() {
            @Override
            public void triggered(Action action) {
                moveDownAction();
            }
        });
        undoAction = createAction(environment.getMessageProvider().translate("XmlEditor", "Undo"), ClientIcon.CommonOperations.UNDO);
        undoAction.addActionListener(new Action.ActionListener() {
            @Override
            public void triggered(Action action) {
                undoAction();
            }
        });
        redoAction = createAction(environment.getMessageProvider().translate("XmlEditor", "Redo"), ClientIcon.CommonOperations.REDO);
        redoAction.addActionListener(new Action.ActionListener() {
            @Override
            public void triggered(Action action) {
                redoAction();
            }
        });
        if (getSchemaTypeSystem() != null) {
            choiceAction = createAction(environment.getMessageProvider().translate("XmlEditor", "Replace"), ClientIcon.ValueModification.DEFINE);
            choiceAction.addActionListener(new Action.ActionListener() {
                @Override
                public void triggered(Action action) {
                    choiceAction();
                }
            });
        }
        validateAction = createAction(environment.getMessageProvider().translate("XmlEditor", "Validate"), XmlEditorIcons.VALIDATE);
        validateAction.setEnabled(!isReadOnly && document != null);
        validateAction.addActionListener(new Action.ActionListener() {
            @Override
            public void triggered(Action action) {
                validate();
            }
        });
        treeModeAction = createAction(environment.getMessageProvider().translate("XmlEditor", "Tree"), XmlEditorIcons.TREE_MODE);
        treeModeAction.setEnabled(true);
        treeModeAction.setCheckable(true);
        treeModeAction.setChecked(true);
        treeModeAction.addActionListener(new Action.ActionListener() {
            @Override
            public void triggered(Action action) {
                openTreeMode();
            }
        });
        textModeAction = createAction(environment.getMessageProvider().translate("XmlEditor", "Text Mode"), XmlEditorIcons.TEXT_MODE);
        textModeAction.setCheckable(false);
        textModeAction.addActionListener(new Action.ActionListener() {
            @Override
            public void triggered(Action action) {
                openTextMode();
            }
        });
        prettyTextModeAction = createAction(environment.getMessageProvider().translate("XmlEditor", "Pretty Text Mode"), XmlEditorIcons.PRETTY_TEXT_MODE);
        prettyTextModeAction.setCheckable(false);
        prettyTextModeAction.addActionListener(new Action.ActionListener() {
            @Override
            public void triggered(Action action) {
                openPrettyTextMode();
            }
        });
        xmlSchemeModeAction = createAction(environment.getMessageProvider().translate("XmlEditor", "XML Scheme"), XmlEditorIcons.SCHEMA_TEXT_MODE);
        xmlSchemeModeAction.setVisible(isSchemeTextAccessible());
        xmlSchemeModeAction.addActionListener(new Action.ActionListener() {
            @Override
            public void triggered(Action action) {
                openXmlScheme();
            }
        });
        if (isSchemeTextAccessible()) {
            xmlSchemeModeAction = createAction(environment.getMessageProvider().translate("XmlEditor", "XML Scheme"), XmlEditorIcons.SCHEMA_TEXT_MODE);
            xmlSchemeModeAction.setEnabled(true);
            xmlSchemeModeAction.addActionListener(new Action.ActionListener() {
                @Override
                public void triggered(Action action) {
                    openXmlScheme();
                }
            });
            xmlSchemeModeAction.setCheckable(true);
        }
        copyAction = createAction(environment.getMessageProvider().translate("XmlEditor", "Copy"), ClientIcon.CommonOperations.COPY);
        copyAction.addActionListener(new Action.ActionListener() {
            @Override
            public void triggered(Action action) {
                copyXml();
            }
        });
        cutAction = createAction(environment.getMessageProvider().translate("XmlEditor", "Cut"), ClientIcon.CommonOperations.CUT);
        cutAction.addActionListener(new Action.ActionListener() {
            @Override
            public void triggered(Action action) {
                cutXml();
            }
        });
        pasteAction = createAction(environment.getMessageProvider().translate("XmlEditor", "Paste"), ClientIcon.CommonOperations.PASTE);
        pasteAction.addActionListener(new Action.ActionListener() {
            @Override
            public void triggered(Action action) {
                pasteXml();
            }
        });
        presenter.getToolBar().addAction(openAction);
        presenter.getToolBar().addAction(saveAsAction);
        presenter.getToolBar().addAction(validateAction);
        presenter.getToolBar().insertSeparator(validateAction);
        presenter.getToolBar().addAction(createElementAction);
        presenter.getToolBar().addAction(createAttributeAction);
        presenter.getToolBar().addAction(renameNodeAction);
        presenter.getToolBar().addAction(deleteNodeAction);
        if (getSchemaTypeSystem() != null) {
            presenter.getToolBar().addAction(choiceAction);
        }
        presenter.getToolBar().insertSeparator(createElementAction);
        presenter.getToolBar().addAction(moveUpAction);
        presenter.getToolBar().addAction(moveDownAction);
        presenter.getToolBar().insertSeparator(moveUpAction);
        presenter.getToolBar().addAction(undoAction);
        presenter.getToolBar().addAction(redoAction);
        presenter.getToolBar().insertSeparator(undoAction);
        presenter.getToolBar().addAction(treeModeAction);
        presenter.getToolBar().addAction(textModeAction);
        presenter.getToolBar().addAction(prettyTextModeAction);
        if (isSchemeTextAccessible()) {
            presenter.getToolBar().addAction(xmlSchemeModeAction);
        }
        presenter.getToolBar().insertSeparator(treeModeAction);
        presenter.getXmlTree().addChangeCurrentItemListener(new IXmlTree.IChangeCurrentItemListener() {
            @Override
            public void currentItemChanged(IXmlTreeItem currentItem) {
                refreshActions();
            }
        });
        presenter.getToolBar().insertSeparator(copyAction);
        presenter.getToolBar().addAction(copyAction);
        presenter.getToolBar().addAction(cutAction);
        presenter.getToolBar().addAction(pasteAction);
        presenter.setupClipboardActions(copyAction, cutAction, pasteAction);
        final String title1 = environment.getMessageProvider().translate("XmlEditor", "Name");
        final String title2 = environment.getMessageProvider().translate("XmlEditor", "Value");
        presenter.getXmlTree().setColumns(new String[]{title1, title2});
        presenter.getClipboard().addListener(new XmlClipboard.Listener() {
            @Override
            public void clipboardChanged() {
                refreshActions();
            }
        });
    }

    private Action createAction(final String title, final ClientIcon clientIcon) {
        final Icon icon = environment.getApplication().getImageManager().getIcon(clientIcon);
        final Action action = environment.getApplication().getWidgetFactory().newAction(icon, title);
        action.setToolTip(title);
        action.setEnabled(false);
        return action;
    }

    public void openXmlDocument(final XmlDocument document) throws XmlException {
        if (document != null) {
            wasModified = true;
            this.document = document;
            final SchemaTypeSystem types = getSchemaTypeSystem();
            if (types != null) {
                document.typify(types);
            } else if (scheme != null) {
                return;//failed to compile scheme
            }
            isConform = types != null ? document.getRootSchemaItem() != null : true;
            IXmlTreeItem rootItem
                    = presenter.getXmlTree().createRootItem(document.getRootElement(), document.getRootSchemaItem(), this, isConform);
            rootItem.expand();
            redoStack.removeAllElements();
            undoStack.removeAllElements();
            undoAction.setEnabled(false);
            redoAction.setEnabled(false);
            if (treeModeAction.isChecked()) {
                openTreeMode();
            } else if (prettyTextModeAction.isChecked()) {
                openPrettyTextMode();
            } else {
                openTextMode();
            }
            if (isSchemeTextAccessible() && !xmlSchemeModeAction.isVisible()) {
                xmlSchemeModeAction.setVisible(isSchemeTextAccessible());
                presenter.getToolBar().addAction(xmlSchemeModeAction);
            }
            refreshActions();
        }
    }

    public void changeXmlDocument(final XmlDocument document) throws XmlException {
        if (document != null) {
            wasModified = true;
            this.document = document;
            final SchemaTypeSystem types = getSchemaTypeSystem();
            if (types != null) {
                document.typify(types);
            } else if (scheme != null) {
                return;//failed to compile scheme
            }
            IXmlTreeItem rootItem
                    = presenter.getXmlTree().createRootItem(document.getRootElement(), document.getRootSchemaItem(), this, isConform);
            rootItem.expand();
            redoStack.removeAllElements();
            undoStack.removeAllElements();
            undoAction.setEnabled(false);
            redoAction.setEnabled(false);
            refreshActions();
        }
    }

    public SchemaTypeSystem getSchemaTypeSystem() {
        if (typeSystem == null) {
            if (scheme != null) {
                final List<String> errors = new ArrayList<>();
                typeSystem = compileSchemaTypeSystem(scheme, errors);
            }
        }
        return typeSystem;
    }

    public boolean isConform() {
        return isConform;
    }

    private SchemaTypeSystem compileSchemaTypeSystem(final String schemaAsStr,
            final List<String> errors) {
        return XmlSchemaTypeSystemCompiler.compile(Thread.currentThread().getContextClassLoader(), schemaAsStr, errors);
    }

    private boolean isSchemeTextAccessible() {
        if (scheme == null || scheme.isEmpty()) {
            if (schemaProvider != null
                    && document != null
                    && document.getSchemaType() != null) {
                final QName docName = document.getSchemaType().getDocumentElementName();
                return docName != null && docName.getNamespaceURI() != null && !docName.getNamespaceURI().isEmpty();
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    private String getSchemeText() {
        if (scheme == null) {
            if (schemaProvider != null
                    && document != null
                    && document.getSchemaType() != null) {
                final QName docName = document.getSchemaType().getDocumentElementName();
                final String nameSpaceUri = docName == null ? null : docName.getNamespaceURI();
                if (nameSpaceUri != null && !nameSpaceUri.isEmpty()) {
                    scheme = schemaProvider.getSchemaForNamespaceUri(nameSpaceUri);
                }
            }
        }
        return scheme;
    }

    public void changeItemValue(final IXmlTreeItem item, final String value) {
        execAction(new ChangeXmlValueOperation(item, value));
    }

    private void undoAction() {
        if (!undoStack.isEmpty()) {
            final XmlOperation operation = undoStack.peek();
            final XmlOperation redoOperation = execOperation(operation);
            if (redoOperation != null) {
                undoStack.pop();
                redoStack.add(redoOperation);
            }
            redoAction.setEnabled(true);
            if (undoStack.isEmpty()) {
                undoAction.setEnabled(false);
            }
            refreshActions();
        }
    }

    private void redoAction() {
        if (!redoStack.isEmpty()) {
            final XmlOperation operation = redoStack.peek();
            final XmlOperation undoOperation = execOperation(operation);
            if (undoOperation != null) {
                redoStack.pop();
                undoStack.add(undoOperation);
            }
            undoAction.setEnabled(true);
            if (redoStack.isEmpty()) {
                redoAction.setEnabled(false);
            }
            refreshActions();
        }
    }

    private void execAction(final XmlOperation operation) {
        final XmlOperation undoOperation = execOperation(operation);
        if (undoOperation != null) {
            if (undoStack.isEmpty() || !undoStack.peek().merge(operation)) {
                undoStack.push(undoOperation);
                undoAction.setEnabled(true);
            }
        }
    }

    private XmlOperation execOperation(final XmlOperation operation) {
        final IXmlTree tree = presenter.getXmlTree();
        final IXmlTreeItem parentItem;
        final int countOfChild;
        if (operation.needToReinitParent()) {
            final IXmlTreeItem currentItem = tree.getCurrentItem();
            if (currentItem == null) {
                parentItem = null;
            } else if (currentItem.getParentItem() == null) {
                parentItem = currentItem;
            } else {
                parentItem = currentItem.getParentItem();
            }
            countOfChild = parentItem == null ? 0 : parentItem.getChildCount();
        } else {
            parentItem = null;
            countOfChild = 0;
        }
        final XmlOperation undoOperation = operation.execute(tree);
        if (undoOperation != null && parentItem != null) {
            wasModified = true;
            final IXmlTreeItem currentItem = tree.getCurrentItem();
            final int currentItemIndex = (currentItem == null || parentItem.equals(currentItem)) ? -1 : currentItem.getIndexInParent();
            final boolean needForExpand;
            if (countOfChild == parentItem.getChildCount()) {
                needForExpand = operation instanceof DeleteXmlNodeOperation == false
                        && (currentItem != null && currentItem.isExpanded());
            } else {
                needForExpand = operation instanceof DeleteXmlNodeOperation == false
                        || (currentItem != null && currentItem.isExpanded());
            }
            parentItem.reinitChildren();
            if (currentItemIndex > -1 && currentItemIndex < parentItem.getChildCount()) {
                final IXmlTreeItem childItem = parentItem.equals(currentItem) ? parentItem : parentItem.getChildItem(currentItemIndex);
                tree.setSelectedItem(childItem);
                if (needForExpand) {
                    childItem.expand();
                }
            }
        }
        return undoOperation;
    }

    private void createElementAction() {
        final IXmlTreeItem currentTreeItem = presenter.getXmlTree().getCurrentItem();
        final XmlModelItem currentItem = currentTreeItem == null ? null : currentTreeItem.getXmlItem();
        final ICreateElementDialog dialog;
        if (currentItem != null) {
            if (currentItem.getSchemaItem() == null || currentItem.elementIsAnyType()) {
                dialog = presenter.newCreateElementDialog(currentItem, valueEditingOptionsProvider, null);
            } else {
                final List<XmlSchemaElementItem> possibleElements
                        = currentItem.getPossibleElements();
                dialog = presenter.newCreateElementDialog(currentItem, valueEditingOptionsProvider, possibleElements);
            }
            if (dialog.execDialog() == IDialog.DialogResult.ACCEPTED) {
                execAction(new CreateChildElementOperation(currentTreeItem, dialog.getChildName(), dialog.getChildText()));
            }
            refreshActions();
        } else {
            final Map<XmlSchemaElementItem, SchemaType> rootTypes = new HashMap<>();
            if (getSchemaTypeSystem() != null) {
                final SchemaTypeSystem types = getSchemaTypeSystem();
                if (types == null) {
                    dialog = presenter.newCreateElementDialog(null, valueEditingOptionsProvider, null);
                } else {
                    final List<XmlSchemaElementItem> roots = new LinkedList<>();
                    XmlSchemaElementItem rootSchemaElement;
                    for (SchemaType rootType : types.documentTypes()) {
                        rootSchemaElement = new XmlSchemaElementItem(rootType.getContentModel());
                        roots.add(rootSchemaElement);
                        rootTypes.put(rootSchemaElement, rootType);
                    }
                    dialog = presenter.newCreateElementDialog(null, valueEditingOptionsProvider, roots);
                }
            } else {
                dialog = presenter.newCreateElementDialog(null, valueEditingOptionsProvider, null);
            }
            if (dialog.execDialog() == IDialog.DialogResult.ACCEPTED) {
                execAction(new CreateRootDocumentOperation(dialog.getChildName(), getRootType(rootTypes, dialog.getChildName()), this, dialog.getChildText()));
            }
            refreshActions();
        }
    }

    private SchemaType getRootType(final Map<XmlSchemaElementItem, SchemaType> rootTypes, QName choosenName) {
        if (rootTypes != null && !rootTypes.isEmpty()) {
            XmlSchemaElementItem choosenRootItem = null;
            for (XmlSchemaElementItem rootItem : rootTypes.keySet()) {
                if (choosenName.equals(rootItem.getFullName())) {
                    choosenRootItem = rootItem;
                    break;
                }
            }
            return choosenRootItem == null ? null : rootTypes.get(choosenRootItem);
        }
        return null;
    }

    private void createAttributeAction() {
        final IXmlTreeItem currentTreeItem = presenter.getXmlTree().getCurrentItem();
        final XmlModelItem currentItem = currentTreeItem == null ? null : currentTreeItem.getXmlItem();
        final XmlDocumentItem currentNode = currentItem == null ? null : currentItem.getXmlNode();
        if (currentNode != null) {
            final ICreateAttributeDialog dialog;
            if (currentItem.getSchemaItem() == null || currentItem.elementIsAnyType()) {
                dialog = presenter.newCreateAttributeDialog(currentItem, valueEditingOptionsProvider, null, getAttributes(currentNode));
            } else {
                final List<XmlSchemaAttributeItem> possibleAttributes
                        = currentItem.getPossibleAttributes();
                dialog = presenter.newCreateAttributeDialog(currentItem, valueEditingOptionsProvider, possibleAttributes, null);
            }
            if (dialog.execDialog() == IDialog.DialogResult.ACCEPTED) {
                execAction(new CreateChildAttributeOperation(currentTreeItem, dialog.getAttrName(), dialog.getAttrValue()));
            }
            refreshActions();
        }
    }

    private void moveUpAction() {
        final IXmlTreeItem currentTreeItem = presenter.getXmlTree().getCurrentItem();
        final XmlModelItem currentItem = currentTreeItem == null ? null : currentTreeItem.getXmlItem();
        final XmlDocumentItem currentNode = currentItem == null ? null : currentItem.getXmlNode();
        if (currentNode != null) {
            IXmlTreeItem previousChild = currentTreeItem.getPreviousSiblingItem();
            final XmlModelItem previousItem = previousChild == null ? null : previousChild.getXmlItem();
            final XmlDocumentItem previousNode = previousItem == null ? null : previousItem.getXmlNode();
            if (previousNode != null) {
                execAction(new MoveXmlOperation(currentTreeItem, previousChild));
            }
        }
    }

    private void moveDownAction() {
        final IXmlTreeItem currentTreeItem = presenter.getXmlTree().getCurrentItem();
        final XmlModelItem currentItem = currentTreeItem == null ? null : currentTreeItem.getXmlItem();
        final XmlDocumentItem currentNode = currentItem == null ? null : currentItem.getXmlNode();
        if (currentNode != null) {
            final IXmlTreeItem nextChild = currentTreeItem.getNextSiblingItem();
            final XmlModelItem nextItem = nextChild == null ? null : nextChild.getXmlItem();
            final XmlDocumentItem nextNode = nextItem == null ? null : nextItem.getXmlNode();
            if (nextNode != null) {
                execAction(new MoveXmlOperation(currentTreeItem, nextChild));
            }
        }
    }

    private List<QName> getAttributes(XmlDocumentItem parentElement) {
        List<QName> attChilren;
        attChilren = new ArrayList<>();
        for (XmlDocumentItem i : parentElement.getChildItems()) {
            if (i.getType() == EXmlItemType.Attribute) {
                attChilren.add(i.getFullName());
            }
        }
        return attChilren;
    }

    private void renameNodeAction() {
        final IXmlTreeItem currentTreeItem = presenter.getXmlTree().getCurrentItem();
        final XmlModelItem currentItem = currentTreeItem == null ? null : currentTreeItem.getXmlItem();
        final XmlDocumentItem currentNode = currentItem == null ? null : currentItem.getXmlNode();
        if (currentNode != null) {
            final IRenameDialog dialog;
            if (currentNode.getType() == EXmlItemType.Element) {
                if (currentItem.getSchemaItem() == null) {
                    dialog = presenter.getNewReName(null, Collections.<QName>emptyList());
                } else {
                    final List<QName> allowedNames = new ArrayList<>();
                    final List<XmlSchemaElementItem> possibleElements
                            = currentItem.getParent().getPossibleElements();
                    for (XmlSchemaElementItem item : possibleElements) {
                        allowedNames.add(item.getFullName());
                    }
                    dialog = presenter.getNewReName(allowedNames, null);
                }
            } else {
                if (currentItem.getParent().getSchemaItem() == null || currentItem.getParent().elementIsAnyType()) {
                    dialog = presenter.getNewReName(null, currentItem.getParent().getExistingAttributes());
                } else {
                    final List<XmlSchemaAttributeItem> possibleAttributes
                            = currentItem.getParent().getPossibleAttributes();
                    final List<QName> allowedNames = new ArrayList<>();
                    for (XmlSchemaAttributeItem item : possibleAttributes) {
                        allowedNames.add(item.getFullName());
                    }
                    dialog = presenter.getNewReName(allowedNames, null);
                }
            }
            if (dialog.execDialog() == IDialog.DialogResult.ACCEPTED) {
                execAction(new ChangeXmlNameOperation(currentTreeItem, dialog.getNewName()));
            }
        }
    }

    private void deleteNodeAction() {
        final IXmlTreeItem currentTreeItem = presenter.getXmlTree().getCurrentItem();
        final XmlModelItem currentItem = currentTreeItem == null ? null : currentTreeItem.getXmlItem();
        final XmlDocumentItem currentNode = currentItem == null ? null : currentItem.getXmlNode();
        if (currentNode != null && environment.messageConfirmation(environment.getMessageProvider().translate("XmlEditor", "Confirm To Delete"), environment.getMessageProvider().translate("XmlEditor", "Do you really want to delete current item?"))) {
            if (currentTreeItem.getParentItem() != null) {
                execAction(new DeleteXmlNodeOperation(currentTreeItem));
            } else {
                execAction(new DeleteRootDocumentOperation(currentTreeItem, this));
                isConform = true;
            }
        }
    }

    private void choiceAction() {
        final IXmlTreeItem currentTreeItem = presenter.getXmlTree().getCurrentItem();
        //boolean dialogIsAccepted = false;
        XmlModelItem currentItem = currentTreeItem == null ? null : currentTreeItem.getXmlItem();
        final XmlDocumentItem currentNode = currentItem == null ? null : currentItem.getXmlNode();
        final XmlModelItem choiceItem = currentItem == null ? null : currentItem.findParentChoiceItem();
        if (currentNode != null) {
            if (choiceItem != null) {
                IChoiceScheme options;
                options = presenter.newCreateChoiceDialog(choiceItem.getSchemaItem(), choiceItem.getIndexOfCurrentChoiceItem());
                if (options.execDialog() == IDialog.DialogResult.ACCEPTED) {
                    execAction(new ChoiceItemOperation(currentTreeItem, options.getName()));
                   // dialogIsAccepted = true;
                }
            } else {
                IChoiceScheme options;
                final SchemaType schemaType = document.getSchemaType();
                if (schemaType != null
                        && schemaType.getTypeSystem() != null
                        && schemaType.getTypeSystem().documentTypes() != null
                        && schemaType.getTypeSystem().documentTypes().length > 1) {
                    List<XmlSchemaItem> choiceRoot = new LinkedList<>();
                    for (SchemaType itemType : schemaType.getTypeSystem().documentTypes()) {
                        if (!itemType.getDocumentElementName().equals(currentNode.getFullName())) {
                            choiceRoot.add(new XmlSchemaElementItem(itemType.getContentModel()));
                        }
                    }
                    options = presenter.newCreateRootChoiceDialog(choiceRoot);
                    if (options.execDialog() == IDialog.DialogResult.ACCEPTED) {
                        execAction(new ChoiceRootOperation(currentTreeItem, options.getName(), schemaType, this));
                        currentItem = presenter.getXmlTree().getCurrentItem().getXmlItem();
                        //dialogIsAccepted = true;
                        refreshActions(currentItem);
                    }
                }
            }
           // if (dialogIsAccepted) {
               // 
          //  }
        }
    }

    public List<XmlError> validate(final boolean isVisible) {
        List<XmlError> errors = new LinkedList<>();
        if (treeModeAction.isChecked()) {
            errors = document.validate();
        } else {
            if (presenter.getXmlText() != null) {
                errors = presenter.getXmlText().validate();
            } else {
                return errors;
            }
        }
        presenter.showErrors(errors, isVisible);
        return errors;
    }

    public List<XmlError> validate() {
        return validate(true);
    }

    private void copyXml() {
        if (isCurrentMode(EXmlEditorMode.XmlTreeMode)) {
            final IXmlTreeItem currentTreeItem = presenter.getXmlTree().getCurrentItem();
            final XmlDocumentItem currentNode = currentTreeItem == null || currentTreeItem.getXmlItem() == null ? null : currentTreeItem.getXmlItem().getXmlNode();
            if (currentNode != null && presenter.getClipboard() != null) {
                presenter.getClipboard().putXml(currentNode.getXmlObject(), currentNode.getFullName(), currentNode.getType());
                refreshActions();
            }
        }
    }

    private void cutXml() {
        if (isCurrentMode(EXmlEditorMode.XmlTreeMode)) {
            final IXmlTreeItem currentTreeItem = presenter.getXmlTree().getCurrentItem();
            final XmlDocumentItem currentNode = currentTreeItem == null || currentTreeItem.getXmlItem() == null ? null : currentTreeItem.getXmlItem().getXmlNode();
            if (currentNode != null && presenter.getClipboard() != null) {
                presenter.getClipboard().putXml(currentNode.getXmlObject(), currentNode.getFullName(), currentNode.getType());
                execAction(currentTreeItem.getParentItem() != null ? new DeleteXmlNodeOperation(currentTreeItem) : new DeleteRootDocumentOperation(currentTreeItem, this));
                refreshActions();
            }
        }
    }

    private void pasteXml() {
        if (isCurrentMode(EXmlEditorMode.XmlTreeMode)) {
            final IXmlTreeItem currentTreeItem = presenter.getXmlTree().getCurrentItem();
            final XmlDocumentItem currentNode = currentTreeItem == null || currentTreeItem.getXmlItem() == null ? null : currentTreeItem.getXmlItem().getXmlNode();
            if (currentNode != null) {
                final XmlObject xml = presenter.getClipboard().getXml();
                final QName itemName = presenter.getClipboard().getNameItem();
                final XmlCursor cursor = xml.newCursor();
                try {
                    if (presenter.getClipboard().getType() == EXmlItemType.Element) {
                        if (cursor.isStartdoc() && cursor.toNextToken() == XmlCursor.TokenType.START) {
                            XmlObject createdItem = cursor.getObject();
                            execAction(new CreateChildElementOperation(currentTreeItem, itemName, createdItem));
                        }
                    } else {
                        if (cursor.isAttr()) {
                            final String value = cursor.getTextValue();
                            execAction(new CreateChildAttributeOperation(currentTreeItem, itemName, value));
                        }
                    }
                    refreshActions();
                } finally {
                    cursor.dispose();
                }
            }
        }
    }

    public boolean wasModified() {
        return wasModified;
    }

    private void refreshActions() {
        final XmlModelItem currentItem
                = presenter.getXmlTree() == null || presenter.getXmlTree().getCurrentItem() == null ? null : presenter.getXmlTree().getCurrentItem().getXmlItem();
        refreshActions(currentItem);
    }

    private void refreshActions(final XmlModelItem item) {
        final boolean hasParentForRoot
                = presenter.getXmlTree().getRootItem() != null && presenter.getXmlTree().getRootItem().getXmlItem() != null && presenter.getXmlTree().getRootItem().getXmlItem().getParent() != null;
        final boolean canSave
                = document != null && hasParentForRoot && !disabledOperations.contains(XmlEditorOperation.SAVE) && !isCurrentMode(EXmlEditorMode.XmlSchemaMode);
        if (isReadOnly()) {
            if (item == null || item.getXmlNode() == null) {
                saveAsAction.setEnabled(canSave);
                validateAction.setEnabled(false);
                textModeAction.setEnabled(textModeAction.isChecked());
                prettyTextModeAction.setEnabled(prettyTextModeAction.isChecked());
            } else {
                saveAsAction.setEnabled(!disabledOperations.contains(XmlEditorOperation.SAVE) && !isCurrentMode(EXmlEditorMode.XmlSchemaMode));
                validateAction.setEnabled(false);
                textModeAction.setCheckable(true);
                textModeAction.setEnabled(true);
                prettyTextModeAction.setEnabled(true);
            }
            createElementAction.setEnabled(false);
            createAttributeAction.setEnabled(false);
            renameNodeAction.setEnabled(false);
            deleteNodeAction.setEnabled(false);
            moveDownAction.setEnabled(false);
            moveUpAction.setEnabled(false);
            if (choiceAction != null) {
                choiceAction.setEnabled(false);
            }
            undoAction.setEnabled(false);
            redoAction.setEnabled(false);
            openAction.setEnabled(!isCurrentMode(EXmlEditorMode.XmlSchemaMode));
            cutAction.setEnabled(false);
            copyAction.setEnabled(false);
            pasteAction.setEnabled(false);
        } else {
            if (item == null || item.getXmlNode() == null) {
                saveAsAction.setEnabled(canSave);
                validateAction.setEnabled(false);
                createElementAction.setEnabled(!disabledOperations.contains(XmlEditorOperation.CHANGE_ROOT) && (document == null || !hasParentForRoot) && treeModeAction.isChecked());
                createAttributeAction.setEnabled(false);
                renameNodeAction.setEnabled(false);
                deleteNodeAction.setEnabled(false);
                moveDownAction.setEnabled(false);
                moveUpAction.setEnabled(false);
                if (choiceAction != null) {
                    choiceAction.setEnabled(false);
                }
                treeModeAction.setEnabled(treeModeAction.isChecked());
                textModeAction.setEnabled(textModeAction.isChecked());
                prettyTextModeAction.setEnabled(prettyTextModeAction.isChecked());
                cutAction.setEnabled(false);
                copyAction.setEnabled(false);
                pasteAction.setEnabled(false);
            } else {
                final boolean isRoot = item == presenter.getXmlTree().getRootItem().getXmlItem();//NOPMD
                final boolean canModifyRoot = !disabledOperations.contains(XmlEditorOperation.CHANGE_ROOT);
                final boolean canModifyItem = !disabledOperations.contains(XmlEditorOperation.MODIFY_ITEM);
                final boolean canModifiCurrentItem = isRoot ? canModifyRoot : canModifyItem;
                final XmlClipboard clipboard = presenter.getClipboard();
                saveAsAction.setEnabled(!disabledOperations.contains(XmlEditorOperation.SAVE) && !isCurrentMode(EXmlEditorMode.XmlSchemaMode));
                validateAction.setEnabled(getSchemaTypeSystem() != null && (isCurrentMode(EXmlEditorMode.XmlTextMode) || isCurrentMode(EXmlEditorMode.XmlTreeMode)));
                createAttributeAction.setEnabled(canModifyItem && item.canCreateAttribute() && treeModeAction.isChecked());
                createElementAction.setEnabled(canModifyItem && item.canCreateChild() && treeModeAction.isChecked());
                deleteNodeAction.setEnabled(canModifiCurrentItem && item.canDelete() && treeModeAction.isChecked());
                renameNodeAction.setEnabled(canModifiCurrentItem && item.canRename() && treeModeAction.isChecked());
                moveUpAction.setEnabled(canModifyItem && item.canMoveUp() && treeModeAction.isChecked());
                moveDownAction.setEnabled(canModifyItem && item.canMoveDown() && treeModeAction.isChecked());
                if (choiceAction != null) {
                    if (item.getParent() == null && document != null) {
                        if (canModifyRoot) {
                            final SchemaType schemaType = document.getSchemaType();
                            final boolean canChoose = schemaType != null
                                    && schemaType.getTypeSystem() != null
                                    && schemaType.getTypeSystem().documentTypes() != null
                                    && schemaType.getTypeSystem().documentTypes().length > 1;
                            choiceAction.setEnabled(canChoose && treeModeAction.isChecked());
                        } else {
                            choiceAction.setEnabled(false);
                        }
                    } else {
                        choiceAction.setEnabled(canModifyItem && item.canChoiceScheme() && treeModeAction.isChecked());
                    }
                }
                textModeAction.setCheckable(true);
                textModeAction.setEnabled(document != null);
                prettyTextModeAction.setCheckable(true);
                prettyTextModeAction.setEnabled(document != null);
                cutAction.setEnabled(!isCurrentMode(EXmlEditorMode.XmlSchemaMode) && !isCurrentMode(EXmlEditorMode.XmlPrettyTextMode) && item.canDelete());
                copyAction.setEnabled(!isCurrentMode(EXmlEditorMode.XmlSchemaMode));
                pasteAction.setEnabled(((isCurrentMode(EXmlEditorMode.XmlTreeMode) && item.canPasteItem(clipboard.getNameItem(), clipboard.getType() == EXmlItemType.Attribute))
                        || isCurrentMode(EXmlEditorMode.XmlTextMode)) && !clipboard.isEmptyClipboard());
            }
            openAction.setEnabled(!disabledOperations.contains(XmlEditorOperation.OPEN) && !isCurrentMode(EXmlEditorMode.XmlSchemaMode));
            undoAction.setEnabled(!disabledOperations.contains(XmlEditorOperation.UNDO_REDO) && !undoStack.isEmpty() && treeModeAction.isChecked());
            redoAction.setEnabled(!disabledOperations.contains(XmlEditorOperation.UNDO_REDO) && !redoStack.isEmpty() && treeModeAction.isChecked());
        }
        if (xmlSchemeModeAction != null) {
            xmlSchemeModeAction.setEnabled(true);
        }
        treeModeAction.setEnabled(treeModeAction.isCheckable());
    }

    public IClientEnvironment getEnvironment() {
        return environment;
    }

    public IXmlValueEditingOptionsProvider getValueEditingOptionsProvider() {
        return valueEditingOptionsProvider;
    }

    public IXmlSchemaProvider getXmlSchemaProvider() {
        return schemaProvider;
    }

    public XmlDocument getDocument() {
        return document;
    }

    public void setDocument(XmlDocument document) {
        this.document = document;
    }

    public void setReadOnly(final boolean readOnly) {
        if (isReadOnly != readOnly) {
            isReadOnly = readOnly;
            presenter.setReadOnly(isReadOnly);
        }
        refreshActions();
    }

    public boolean isReadOnly() {
        return isReadOnly;
    }

    public void setValue(XmlObject value) {
        if (value != null) {
            try {
                openXmlDocument(new XmlDocument(value));
                wasModified = false;
            } catch (XmlException exception) {
                environment.getTracer().error(exception);
            }
        } else {
            if (document != null) {
                final IXmlTreeItem rootItem = presenter.getXmlTree().getRootItem();
                if (rootItem != null) {
                    rootItem.delete();
                }
                document = null;
                refreshActions(null);
            }
        }
    }

    public void setDisabledOperations(final EnumSet<XmlEditorOperation> operations) {
        disabledOperations.clear();
        if (operations != null) {
            disabledOperations.addAll(operations);
        }
        refreshActions();
    }

    public boolean isOperationDisabled(XmlEditorOperation operation) {
        return disabledOperations.contains(operation);
    }

    public boolean close() {
        presenter.getClipboard().close();
        presenter.close();
        return true;
    }

    private void openTreeMode() {
        if (!isCurrentMode(EXmlEditorMode.XmlTreeMode)
                && presenter.switchToTreeMode()) {
            treeModeAction.setChecked(true);
            textModeAction.setChecked(false);
            prettyTextModeAction.setChecked(false);
            xmlSchemeModeAction.setChecked(false);
            setCurrentMode(EXmlEditorMode.XmlTreeMode);
            refreshActions();
        } else if (isCurrentMode(EXmlEditorMode.XmlTreeMode)) {
            treeModeAction.setChecked(true);
        } else {
            treeModeAction.setChecked(false);
        }
    }

    private void openTextMode() {
        if (!isCurrentMode(EXmlEditorMode.XmlTextMode)
                && presenter.switchToTextMode(document, isReadOnly())) {
            treeModeAction.setChecked(false);
            textModeAction.setChecked(true);
            prettyTextModeAction.setChecked(false);
            xmlSchemeModeAction.setChecked(false);
            setCurrentMode(EXmlEditorMode.XmlTextMode);
            refreshActions();
        } else if (isCurrentMode(EXmlEditorMode.XmlTextMode)) {
            textModeAction.setChecked(true);
        } else {
            textModeAction.setChecked(false);
        }
    }

    private void openPrettyTextMode() {
        if (!isCurrentMode(EXmlEditorMode.XmlPrettyTextMode)
                && presenter.switchToPrettyTextMode(document, treeModeAction.isChecked())) {
            treeModeAction.setChecked(false);
            textModeAction.setChecked(false);
            prettyTextModeAction.setChecked(true);
            xmlSchemeModeAction.setChecked(false);
            setCurrentMode(EXmlEditorMode.XmlPrettyTextMode);
            refreshActions();
        } else if (isCurrentMode(EXmlEditorMode.XmlPrettyTextMode)) {
            prettyTextModeAction.setChecked(true);
        } else {
            prettyTextModeAction.setChecked(false);
        }
    }

    private void openXmlScheme() {
        if (!isCurrentMode(EXmlEditorMode.XmlSchemaMode)
                && presenter.switchToSchemeTextMode(getSchemeText())) {
            xmlSchemeModeAction.setChecked(true);
            treeModeAction.setChecked(false);
            textModeAction.setChecked(false);
            prettyTextModeAction.setChecked(false);
            setCurrentMode(EXmlEditorMode.XmlSchemaMode);
            refreshActions();
        } else if (isCurrentMode(EXmlEditorMode.XmlSchemaMode)) {
            xmlSchemeModeAction.setChecked(true);
        } else {
            xmlSchemeModeAction.setChecked(false);
        }
    }

    public void setCurrentMode(final EXmlEditorMode currentMode) {
        presenter.setCurrentMode(currentMode);
    }

    public boolean isCurrentMode(final EXmlEditorMode posMode) {
        return presenter.isCurrentMode(posMode);
    }
}
