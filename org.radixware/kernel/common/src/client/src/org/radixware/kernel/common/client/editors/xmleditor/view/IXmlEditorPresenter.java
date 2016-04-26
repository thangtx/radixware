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

import java.util.List;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.XmlError;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.editors.xmleditor.model.IXmlValueEditingOptionsProvider;
import org.radixware.kernel.common.client.editors.xmleditor.model.XmlDocument;
import org.radixware.kernel.common.client.editors.xmleditor.model.XmlModelItem;
import org.radixware.kernel.common.client.editors.xmleditor.model.schema.XmlSchemaAttributeItem;
import org.radixware.kernel.common.client.editors.xmleditor.model.schema.XmlSchemaElementItem;
import org.radixware.kernel.common.client.editors.xmleditor.model.schema.XmlSchemaItem;
import org.radixware.kernel.common.client.widgets.actions.Action;
import org.radixware.kernel.common.client.widgets.actions.IToolBar;


public interface IXmlEditorPresenter {        
    
    Action createOpenAction(final String title, final ClientIcon icon);
    ICreateAttributeDialog newCreateAttributeDialog(final XmlModelItem parentItem, final IXmlValueEditingOptionsProvider editingOptionsProvider, final List<XmlSchemaAttributeItem> allowedItems,final List<QName> restrictedNames);
    ICreateElementDialog newCreateElementDialog(final XmlModelItem parentItem, final IXmlValueEditingOptionsProvider editingOptionsProvider, List<XmlSchemaElementItem> allowedNames);
    IRenameDialog getNewReName(List<QName> allowedNames, List<QName> restrictedNames);
    IToolBar getToolBar();
    IXmlTree getXmlTree();
    XmlDocument getXmlText();
    void saveXmlDocument(final XmlDocument document, final boolean sameFile, final boolean isTreeMode, final boolean isPrettyTextMode);
    IChoiceScheme newCreateChoiceDialog(final XmlSchemaItem choiceItem, final int currentItemIndex);
    IChoiceScheme newCreateRootChoiceDialog(final List<XmlSchemaItem> choiceItems);
    void setReadOnly(final boolean isReadOnly);
    void showErrors(final List<XmlError> errors, final boolean isVisible);
    boolean switchToTreeMode();
    boolean switchToTextMode(final XmlDocument document, final boolean isReadOnly);
    boolean switchToPrettyTextMode(final XmlDocument document, final boolean isTreeCheked);
    boolean switchToSchemeTextMode(final String schemeText);  
    boolean isCurrentMode(final XmlEditorController.EXmlEditorMode posMode);
    void setCurrentMode(final XmlEditorController.EXmlEditorMode currentMode);   
    void setupClipboardActions(final Action copyAction, final Action cutAction, final Action pasteAction);
    XmlClipboard getClipboard();
    void close();
}
