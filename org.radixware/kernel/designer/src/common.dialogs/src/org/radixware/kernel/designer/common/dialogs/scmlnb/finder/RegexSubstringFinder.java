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

package org.radixware.kernel.designer.common.dialogs.scmlnb.finder;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.localization.AdsMultilingualStringDef;
import org.radixware.kernel.common.jml.JmlTagLocalizedString;
import org.radixware.kernel.common.scml.Scml;
import org.radixware.kernel.common.scml.Scml.Tag;
import org.radixware.kernel.designer.common.dialogs.scmlnb.finder.FindUtils.ContainingLineInfo;


class RegexSubstringFinder implements IFinder {

    private final Pattern pattern;
    private final ScmlTextCalculator textCalculator;
    private final TagTextFactory tagTextFactory;

    public RegexSubstringFinder(final Pattern pattern, final TagTextFactory tagTextFactory) {
        this.pattern = pattern;
        this.tagTextFactory = tagTextFactory;
        textCalculator = new ScmlTextCalculator(tagTextFactory);
    }

    @Override
    public boolean accept(RadixObject radixObject) {
        return radixObject instanceof Scml;
    }
    
    @Override
    public List<IOccurrence> list(final RadixObject radixObject) {
        if (radixObject == null) {
            return Collections.emptyList();
        }

        Scml scml = (Scml) radixObject;

        final ScmlTextInfo textInfo = textCalculator.calculate(scml);
        
        if (textInfo.getText() == null || textInfo.getText().isEmpty()) {
            return Collections.emptyList();
        }
        
        Matcher matcher = pattern.matcher(textInfo.getText());
        final List<IOccurrence> occurences = new LinkedList<IOccurrence>();
        while (matcher.find()) {
            final ScmlLocation scmlLocation = new ScmlLocation(scml, textInfo.getScmlOffsetAt(matcher.start()), 0);
            final ContainingLineInfo lineReturn = FindUtils.getContainingLine(textInfo.getText(), matcher.start());
            final ScmlOccurrence.ScmlOccurenceInfo occurenceInfo = new ScmlOccurrence.ScmlOccurenceInfo(lineReturn.getLine(), matcher.start() - lineReturn.getLineStartOffset(), matcher.end() - lineReturn.getLineStartOffset());
            occurences.add(new ScmlOccurrence(scmlLocation, occurenceInfo, matcher.group()));
        }
        int scmlOffset = 0;
        int line = 1;
        int column = 1;
        for (Scml.Item item : scml.getItems()) {
            if (item instanceof Scml.Tag) {
                if (item instanceof JmlTagLocalizedString) {
                    final JmlTagLocalizedString localizedString = (JmlTagLocalizedString) item;
                    final AdsMultilingualStringDef strinDef = localizedString.findLocalizedString(localizedString.getStringId());
                    if (strinDef != null) {
                        final List<AdsMultilingualStringDef.StringStorage> stringStorageList = strinDef.getValues(EScope.LOCAL);
                        if (stringStorageList != null) {
                            for (AdsMultilingualStringDef.StringStorage stringStorage : stringStorageList) {
                                matcher = pattern.matcher(stringStorage.getValue());
                                while (matcher.find()) {
                                    final ScmlLocation scmlLocation = new ScmlLocation(scml, scmlOffset, 0);
                                    final FindUtils.ContainingLineInfo lineInfo = FindUtils.getContainingLine(stringStorage.getValue(), matcher.start());
                                    final String prefix = "In localized string at " + line + ":" + column + " : ";
                                    final ScmlOccurrence.ScmlOccurenceInfo occurenceInfo = new ScmlOccurrence.ScmlOccurenceInfo(
                                            prefix + lineInfo.getLine(),
                                            matcher.start() - lineInfo.getLineStartOffset() + prefix.length(),
                                            matcher.end() - lineInfo.getLineStartOffset() + prefix.length());
                                    occurences.add(new ScmlOccurrence(scmlLocation, occurenceInfo, matcher.group()));
                                }
                            }
                        }
                    }
                }
                scmlOffset++;
                column += tagTextFactory.getText((Tag) item).length();
            } else {
                final String text = ((Scml.Text) item).getText();
                scmlOffset += text.length();
                line += text.length() - text.replace("\n", "").length();
                int lastIndexOfLr = text.lastIndexOf('\n');
                if (lastIndexOfLr != -1) {
                    column = text.length() - lastIndexOfLr;
                } else {
                    column += text.length();
                }
            }
        }
        return occurences;
    }
}
