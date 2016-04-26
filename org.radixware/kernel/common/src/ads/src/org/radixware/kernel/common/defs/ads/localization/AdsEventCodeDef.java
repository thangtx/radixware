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
package org.radixware.kernel.common.defs.ads.localization;

import java.util.Set;
import org.apache.commons.lang.StringEscapeUtils;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.enums.ELocalizedStringKind;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.adsdef.LocalizedString;

public class AdsEventCodeDef extends AdsMultilingualStringDef {
 
    private EEventSeverity eventSeverity = EEventSeverity.EVENT;
    private String eventSource = "App";

    public AdsEventCodeDef(Id id) {
        super(id);
    }

    protected AdsEventCodeDef(AdsMultilingualStringDef src, boolean forOverwrite) {
        super(src, forOverwrite);
        if (src instanceof AdsEventCodeDef) {
            this.eventSeverity = ((AdsEventCodeDef) src).eventSeverity;
            this.eventSource = ((AdsEventCodeDef) src).eventSource == null ? null : new String(((AdsEventCodeDef) src).eventSource);
        }
    }

    protected AdsEventCodeDef(LocalizedString xDef) {
        super(xDef);
        //try {
        this.eventSeverity = xDef.getEventSeverity();
        //} catch (NoConstItemWithSuchValueError e) {
        //    this.eventSeverity = EEventSeverity.ERROR;
        //}
        this.eventSource = xDef.getEventSource();
    }

    public EEventSeverity getEventSeverity() {
        return eventSeverity;
    }

    public void setEventSeverity(EEventSeverity severity) {
        this.eventSeverity = severity;
        setEditState(EEditState.MODIFIED);
    }

    public String getEventSource() {
        return eventSource;
    }

    public void setEventSource(String source) {
        this.eventSource = source;
        setEditState(EEditState.MODIFIED);
    }

    @Override
    public void appendTo(LocalizedString xDef, ESaveMode saveMode, EIsoLanguage lang) {
        super.appendTo(xDef, saveMode, lang);
        // FIXME: xDef.setEventCode(eventCode);
        xDef.setEventSource(eventSource);
        xDef.setEventSeverity(eventSeverity);
    }

    @Override
    public ELocalizedStringKind getSrcKind() {
        return ELocalizedStringKind.EVENT_CODE;
    }

    @Override
    public EDefType getDefinitionType() {
        return EDefType.MULTILINGUAL_EVENT_CODE;
    }

    @Override
    public void appendAdditionalToolTip(StringBuilder sb) {

        final String eventSrc = getEventSource();
        if (eventSrc != null) {
            sb.append("<br><br><b>Event Source:</b>&nbsp;").append(eventSrc);
        }

        EEventSeverity s = getEventSeverity();
        if (s != null) {
            sb.append("<br><b>Severity:</b>&nbsp;").append(s.getName());
        }

        final Set<EIsoLanguage> languages = getLanguages();
        if (languages != null) {
            sb.append("<br><br><b>Message:</b><br>");

            for (final EIsoLanguage lang : languages) {
                final String value = getValue(lang);
                if (value != null && !value.isEmpty()) {
                    sb.append(StringEscapeUtils.escapeHtml(value)).append("<br>");
                }
            }
        }
    }
}
