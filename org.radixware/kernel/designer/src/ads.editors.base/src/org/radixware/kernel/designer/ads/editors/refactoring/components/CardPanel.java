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


package org.radixware.kernel.designer.ads.editors.refactoring.components;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import org.openide.util.NbBundle;
import org.radixware.kernel.designer.ads.editors.refactoring.RefactoringStepPanel;


public abstract class CardPanel extends RefactoringStepPanel {
    public static final String MESSAGE_CARD = "Message";
    
//    private volatile boolean isReady = false;
    private final JLabel lblStatus;

    public CardPanel() {
        lblStatus = new JLabel();

        setLayout(new BorderLayout());
        super.setLayout(new CardLayout());

        lblStatus.setHorizontalAlignment(SwingConstants.CENTER);
        
        add(lblStatus, MESSAGE_CARD);
    }
    
    protected final void setStatus(String message) {
        lblStatus.setText(message);
        getCardLayout().show(this, MESSAGE_CARD);
    }
    
    public void open() {
        String message = NbBundle.getMessage(ObjectUsagesPanel.class, "ObjectUsagesPanel.lblStatus.Wait");
        setStatus(message);
    }

    private CardLayout getCardLayout() {
        return (CardLayout) getLayout();
    }
    
    protected final void showCard(String cardId) {
        getCardLayout().show(this, cardId);
    }
}
