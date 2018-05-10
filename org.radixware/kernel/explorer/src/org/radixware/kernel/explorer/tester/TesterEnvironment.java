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

package org.radixware.kernel.explorer.tester;

import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QFileDialog;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.common.client.exceptions.ExceptionMessage;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.defs.ads.userfunc.AdsUserFuncDef;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.env.ExplorerSettings;
import org.radixware.kernel.explorer.env.MessageFilter;
import org.radixware.kernel.explorer.env.trace.ExplorerTraceBuffer;
import org.radixware.kernel.explorer.env.trace.ExplorerTracer;

import org.radixware.kernel.explorer.tester.TesterEngine.TesterMessageFilter;


public class TesterEnvironment {

    private final ExplorerSettings nativeSettings;
    private final ExplorerTraceBuffer testerTraceBuffer;
    private final IClientEnvironment environment;
    private final ExplorerTraceBuffer nativeTraceBuffer;    
    private final MessageFilter messageFilter;
    private final List<Throwable> exceptions = new LinkedList<>();
    private final QFileDialog.DialogHandler fileDialogHandler = new QFileDialog.DialogHandler(){

        @Override
        public List<String> beforeOpen(final QFileDialog.AcceptMode mode, 
                                       final String caption, 
                                       final String dir, 
                                       final String filter, 
                                       final QFileDialog.Options options) {
            return Collections.emptyList();
        }

        @Override
        public List<String> afterOpen(final QFileDialog.AcceptMode mode, 
                                      final String caption, 
                                      final String dir, 
                                      final String filter, 
                                      final QFileDialog.Options options, 
                                      final List<String> result) {
            return result;
        }        
    };

    public TesterEnvironment(IClientEnvironment environment) {
        this.environment = environment;
        nativeSettings  = (ExplorerSettings)environment.getConfigStore();
        nativeTraceBuffer = (ExplorerTraceBuffer)environment.getTracer().getBuffer();
        messageFilter = new TesterMessageFilter(environment);
        testerTraceBuffer = (ExplorerTraceBuffer)environment.getTracer().createTraceBuffer();
    }
    
    public void registerException(final Throwable ex){
        exceptions.add(ex);
    }

    public IClientEnvironment getEnvironment() {
        return environment;
    }

    public MessageFilter getMessageFilter() {
        return messageFilter;
    }

    public ExplorerTraceBuffer getTraceBuffer() {
        return testerTraceBuffer;
    }

    public void init() {
        environment.getProgressHandleManager().blockProgress();
        ((ExplorerTracer)environment.getTracer()).setBuffer(testerTraceBuffer);
        final ExplorerSettings settings = new ExplorerSettings(environment);
        //Copy explorer tree manager settings
        settings.beginGroup(SettingNames.SYSTEM);
        settings.beginGroup(SettingNames.EXPLORER_TREE_GROUP);
        nativeSettings.beginGroup(SettingNames.SYSTEM);
        nativeSettings.beginGroup(SettingNames.EXPLORER_TREE_GROUP);
        try {
            settings.writeDouble(
                    SettingNames.ExplorerTree.SPLITTER_POSITION,
                    nativeSettings.readDouble(SettingNames.ExplorerTree.SPLITTER_POSITION, 1. / 3.));
            settings.beginGroup(SettingNames.ExplorerTree.COMMON_GROUP);
            nativeSettings.beginGroup(SettingNames.ExplorerTree.COMMON_GROUP);
            try {
                settings.writeQAlignmentFlag(
                        SettingNames.ExplorerTree.Common.TREE_AREA,
                        nativeSettings.readAlignmentFlag(SettingNames.ExplorerTree.Common.TREE_AREA, Qt.AlignmentFlag.AlignLeft));
            } finally {
                settings.endGroup();
                nativeSettings.endGroup();
            }
        } finally {
            settings.endGroup();
            settings.endGroup();
            nativeSettings.endGroup();
            nativeSettings.endGroup();
        }
        Application.applySettings(settings);

        Application.installMessageFilter(messageFilter);
        QFileDialog.installDialogHandler(fileDialogHandler);
    }

    public void restore() {
        environment.getProgressHandleManager().unblockProgress();
        ((ExplorerTracer)environment.getTracer()).setBuffer(nativeTraceBuffer);
        Application.applySettings(nativeSettings);
        Application.removeMessageFilter();
        QFileDialog.removeDialogHandler(fileDialogHandler);
        AdsUserFuncDef.Lookup.clearCaches();
        final MessageProvider mp = environment.getMessageProvider();
        for (Throwable ex: exceptions){
            new ExceptionMessage(environment, ex).trace(environment.getTracer(), mp.translate("TesterDialog", "Exception detected during test execution"));
        }
    }
}
