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

package org.radixware.kernel.explorer.editors.jmleditor.dialogs;

import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QWidget;
import java.util.EnumSet;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.common.jml.Jml;
import org.radixware.kernel.explorer.dialogs.ExplorerDialog;
import org.radixware.kernel.explorer.editors.jmleditor.JmlEditor;
import org.radixware.kernel.explorer.editors.jmleditor.JmlProcessor;
import org.radixware.kernel.explorer.editors.xscmleditor.Highlighter;
import org.radixware.kernel.explorer.editors.xscmleditor.XscmlEditor;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.schemas.xscml.JmlType;


public class PreviewImportDialog extends  ExplorerDialog{
    private final JmlEditor editor;
    
     public PreviewImportDialog(final QWidget parent, final JmlEditor editor,final JmlType newJmlType){
         super(editor.getEnvironment(), parent, "PreviewImportDialog");
         this.setWindowTitle(Application.translate("JmlEditor", "Preview User Function"));
         this.editor=editor;
         createUI( newJmlType);
    }
     
      private void createUI(final JmlType newJmlType) {       
        this.setMinimumSize(100, 100);
        
        final JmlProcessor converter=new JmlProcessor(editor);
        final Jml newJml=Jml.Factory.loadFrom(editor.getUserFunc(), newJmlType, "newJmlTypeForPreview");
        converter.open(newJml);
        
        final XscmlEditor xscmlEditor=new XscmlEditor( getEnvironment(),  converter,  this);
        final Highlighter hightlighter = new Highlighter(getEnvironment(), xscmlEditor, converter, "org.radixware.explorer/S_E/SYNTAX_JML/");        
        xscmlEditor.setReadOnly(true);
        xscmlEditor.open(editor.getUserFunc());        
        newJml.delete();
        
        dialogLayout().addWidget(xscmlEditor);
        addButtons(EnumSet.of(EDialogButtonType.CLOSE),true);
        this.setWindowModality(Qt.WindowModality.WindowModal);
    }

    @Override
    public void reject() {
        saveGeometryToConfig();
        super.reject();
    }
    
}
