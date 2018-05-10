/*
 * Copyright (c) 2008-2016, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * This Source Code is distributed WITHOUT ANY WARRANTY; including any 
 * implied warranties but not limited to warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
 * License, v. 2.0. for more details.
 */
package org.radixware.wps.views.admin;

import java.util.List;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.meta.mask.EditMaskList;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.starter.config.ConfigEntry;
import org.radixware.kernel.starter.config.ConfigFileParseException;
import org.radixware.wps.WebServerRunParams;
import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.views.editors.valeditors.ValBoolEditorController;
import org.radixware.wps.views.editors.valeditors.ValListEditorController;
import org.radixware.wps.views.editors.valeditors.ValStrEditorController;

public class TraceSettingsWidget extends GroupWidget implements ConfigWidget {

    private final WpsEnvironment env;
    private final ValStrEditorController traceDirEditorController;
    private final ValListEditorController<Long> traceProfileEditorController;
    private final ValListEditorController<Long> traceMinSeverityEditorController;
    private final ValBoolEditorController writeObjectNamesToHtmlEditorController;
    private String appliedTraceDir = null;
    private String appliedTraceProfile = null;
    private String appliedTraceMinSeverity = null;
    private boolean appliedWriteObjectNamesToHtml = false;
    private boolean wasSaved = false;
    
    public TraceSettingsWidget(WpsEnvironment env, String title) {
        super(title);
        this.env = env;
        MessageProvider mp = env.getMessageProvider();
        traceDirEditorController = new ValStrEditorController(env);
        final EditMaskList eventSeverityEditMask = new EditMaskList();
        for (EEventSeverity eventSeverity : EEventSeverity.values()) {
            eventSeverityEditMask.addItem(env.getMessageProvider().translate("ConnectionEditor", eventSeverity.getName()), eventSeverity.getValue());
        }
        traceProfileEditorController = new ValListEditorController<>(env, eventSeverityEditMask);
        traceMinSeverityEditorController = new ValListEditorController<>(env, eventSeverityEditMask);
        writeObjectNamesToHtmlEditorController = new ValBoolEditorController(env);
        writeObjectNamesToHtmlEditorController.setMandatory(true);
        addNewRow(mp.translate("AdminPanel", "Trace directory:"), traceDirEditorController);
        addNewRow(mp.translate("AdminPanel", "Trace profile:"), traceProfileEditorController);
        addNewRow(mp.translate("AdminPanel", "Trace min severity:"), traceMinSeverityEditorController);
        addNewRow(mp.translate("AdminPanel", "Write object names to HTML:"), writeObjectNamesToHtmlEditorController);
    }

    @Override
    public boolean validate() {
        return true;
    }

    @Override
    public void write(List<String> toRemove, List<ConfigEntry> toWrite) throws ConfigFileParseException {
        if (traceDirEditorController.getValue() == null || traceDirEditorController.getValue().isEmpty()) {
            toRemove.add("traceDir");
        } else {
            toWrite.add(new ConfigEntry("traceDir", traceDirEditorController.getValue()));
        }
        if (traceMinSeverityEditorController.getValue() == null) {
            toRemove.add("traceMinSeverity");
        } else {
            toWrite.add(new ConfigEntry("traceMinSeverity", EEventSeverity.getForValue(traceMinSeverityEditorController.getValue()).getName()));
        }
        if (traceProfileEditorController.getValue() == null) {
            toRemove.add("traceProfile");
        } else {
            toWrite.add(new ConfigEntry("traceProfile", EEventSeverity.getForValue(traceProfileEditorController.getValue()).getName()));
        }
        if (writeObjectNamesToHtmlEditorController.getValue().equals(Boolean.FALSE)) {
            toRemove.add("writeObjectNamesToHtml");
        } else {
            toWrite.add(new ConfigEntry("writeObjectNamesToHtml", null));
        }
    }

    @Override
    public void save() {
        appliedTraceDir = traceDirEditorController.getValue();
        WebServerRunParams.setTraceDir(appliedTraceDir);
        Long traceProfile = traceProfileEditorController.getValue();
        appliedTraceProfile = traceProfile == null ? null : EEventSeverity.getForValue(traceProfile).getName();
        WebServerRunParams.setTraceProfile(appliedTraceProfile);
        Long traceMinSeverity = traceMinSeverityEditorController.getValue();
        appliedTraceMinSeverity = traceMinSeverity == null ? null : EEventSeverity.getForValue(traceMinSeverity).getName();
        WebServerRunParams.setTraceMinSeverity(traceMinSeverity == null ? null : EEventSeverity.getForValue(traceMinSeverity));
        appliedWriteObjectNamesToHtml = writeObjectNamesToHtmlEditorController.getValue();
        WebServerRunParams.setWriteObjectNamesToHtml(appliedWriteObjectNamesToHtml);
        wasSaved = true;
    }

    @Override
    public void reread() {
        WebServerRunParams runParams = WebServerRunParams.readFromFile();
        traceDirEditorController.setValue(runParams.getTraceDir());
        String traceProfile = runParams.getTraceProfile();
        traceProfileEditorController.setValue(traceProfile == null ? null : EEventSeverity.getForName(traceProfile).getValue());
        String traceMinSeverity = runParams.getTraceMinSeverity();
        traceMinSeverityEditorController.setValue(traceMinSeverity == null ? null : EEventSeverity.getForName(traceMinSeverity).getValue());
        writeObjectNamesToHtmlEditorController.setValue(runParams.writeObjectNamesToHtml());
    }

    @Override
    public void load() {
        WebServerRunParams runParams = env.getRunParams();
        traceDirEditorController.setValue(wasSaved ? appliedTraceDir : runParams.getTraceDir());
        String traceProfile = wasSaved ? appliedTraceProfile : runParams.getTraceProfile();
        traceProfileEditorController.setValue(traceProfile == null ? null : EEventSeverity.getForName(traceProfile).getValue());
        String traceMinSeverity = wasSaved ? appliedTraceMinSeverity : runParams.getTraceMinSeverity();
        traceMinSeverityEditorController.setValue(traceMinSeverity == null ? null : EEventSeverity.getForName(traceMinSeverity).getValue());
        writeObjectNamesToHtmlEditorController.setValue(wasSaved ? appliedWriteObjectNamesToHtml : runParams.writeObjectNamesToHtml());
    }

}
