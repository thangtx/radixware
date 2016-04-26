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

package org.radixware.kernel.designer.ads.localization.translation;

import java.awt.Rectangle;
import java.awt.event.MouseWheelListener;
import javax.swing.JScrollPane;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.designer.ads.localization.RowString;


public interface ITranslationPanel {   
    
    void setNextRowSting();
    void setPrevRowSting();
    void setNextUncheckedRowSting();
    void setPrevUncheckedRowSting();
    void  checkWarnings(final RowString rowString);

    /*public List<EIsoLanguage> getSourceLands();
    public List<EIsoLanguage> getTranslationLangs();*/

    void translationWasEdited(final EIsoLanguage lang);    
    void scroll(Rectangle rect);
    void updateScrollPanel();
    JScrollPane getTranslationPanelScrollPane();

    void updatePhrasesPanel();
    void addPhraseToPrompt(final RowString rowString);
    void removePhraseFromPrompt(final RowString rowString);

    boolean isReadOnly();
    void updateTargetLangsStatus(final RowString rowString);
    void save();
}
