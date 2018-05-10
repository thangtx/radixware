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
import com.trolltech.qt.gui.QPlainTextEdit;
import com.trolltech.qt.gui.QWidget;
import java.util.EnumSet;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.explorer.dialogs.ExplorerDialog;
import org.radixware.kernel.explorer.env.Application;

/**
 *
 * @author npopov
 */
public class PreviewExecutableSourceDialog extends ExplorerDialog {

    private final JavaSourceSupport srcSupport;
    
    public PreviewExecutableSourceDialog(IClientEnvironment environment, QWidget parent, JavaSourceSupport srcSupport) {
        super(environment, parent);
        if (srcSupport == null) {
            throw new NullPointerException("Java source support is null");
        }
        this.srcSupport = srcSupport;
        createUI();
    }
    
    private void createUI() {
        QPlainTextEdit srcEditor = new TextPlainEditorWithLineNumbers(this);
        srcEditor.setReadOnly(true);
        srcEditor.setLineWrapMode(QPlainTextEdit.LineWrapMode.NoWrap);
        final CodePrinter printer = CodePrinter.Factory.newJavaPrinter();
        srcSupport.getCodeWriter(JavaSourceSupport.UsagePurpose.SERVER_EXECUTABLE).writeCode(printer);
        srcEditor.setPlainText(printer.toString());
        
        dialogLayout().addWidget(srcEditor);
        addButtons(EnumSet.of(EDialogButtonType.CLOSE),true);
        this.setWindowModality(Qt.WindowModality.WindowModal);
        setWindowTitle(Application.translate("JmlEditor", "Preview Executable Source Code"));
    }
    
    @Override
    public void reject() {
        saveGeometryToConfig();
        super.reject();
    }
}
