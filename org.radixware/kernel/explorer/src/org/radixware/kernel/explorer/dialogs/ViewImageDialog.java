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

package org.radixware.kernel.explorer.dialogs;

import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QAction;
import com.trolltech.qt.gui.QHBoxLayout;
import com.trolltech.qt.gui.QImage;
import com.trolltech.qt.gui.QToolButton;
import com.trolltech.qt.gui.QWidget;
import java.util.EnumSet;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.explorer.widgets.ImageViewWidget;
import org.radixware.kernel.explorer.utils.WidgetUtils;


public class ViewImageDialog extends ExplorerDialog{
    
    private final ImageViewWidget imageView;
    
    public ViewImageDialog(final QImage image, final IClientEnvironment environment, final QWidget parent){
        super(environment, parent);
        imageView = new ImageViewWidget(image, environment.getMessageProvider(), true, this);
        setupUi();
    }
    
    private void setupUi(){
        setWindowTitle(getEnvironment().getMessageProvider().translate("ExplorerDialog", "Image View"));
        final QHBoxLayout ltZoomButtons = WidgetUtils.createHBoxLayout(null);
        ltZoomButtons.setAlignment(new Qt.Alignment(Qt.AlignmentFlag.AlignLeft));
        dialogLayout().addLayout(ltZoomButtons);        
                
        ltZoomButtons.addWidget(createButtonForAction(imageView.zoomInAction));
        ltZoomButtons.addWidget(createButtonForAction(imageView.zoomOutAction));
        ltZoomButtons.addWidget(createButtonForAction(imageView.zoomSelAction));
        ltZoomButtons.addWidget(createButtonForAction(imageView.zoom2FitAction));
        
        imageView.setMinimumWidth(300);
        dialogLayout().addWidget(imageView);
        addButtons(EnumSet.of(EDialogButtonType.CLOSE),true);
    }
    
    private QToolButton createButtonForAction(final QAction action){
        final QToolButton button = new QToolButton(this);
        button.setIconSize(new QSize(24, 24));
        button.setAutoRaise(true);
        button.setDefaultAction(action);
        return button;
    }       
}
