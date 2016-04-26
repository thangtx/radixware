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

package org.radixware.kernel.explorer.text;

import java.util.EnumSet;
import org.radixware.kernel.common.client.enums.ETextOptionsMarker;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.trace.ClientTracer;
import org.radixware.kernel.common.enums.ESelectorRowStyle;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.env.ExplorerSettings;


public class DefaultTextOptionsProvider extends org.radixware.kernel.common.client.text.DefaultTextOptionsProvider{
    
    private final static DefaultTextOptionsProvider INSTANCE = new DefaultTextOptionsProvider();
    
    private DefaultTextOptionsProvider(){
    }

    @Override
    protected ExplorerSettings getSettings() {
        return
            (ExplorerSettings)Application.getInstance().getEnvironment().getConfigStore();
    }

    @Override
    public ExplorerTextOptions getOptions(final EnumSet<ETextOptionsMarker> markers) {
        return (ExplorerTextOptions)super.getOptions(markers);
    }

    @Override
    public ExplorerTextOptions getOptions(final ETextOptionsMarker... markers) {
        return (ExplorerTextOptions)super.getOptions(markers);
    }

    @Override
    public ExplorerTextOptions getOptions(final ESelectorRowStyle style, ETextOptionsMarker... markers) {
        return (ExplorerTextOptions)super.getOptions(style, markers);
    }

    @Override
    public ExplorerTextOptions getOptions(final EnumSet<ETextOptionsMarker> markers, final ESelectorRowStyle style) {
        return (ExplorerTextOptions)super.getOptions(markers, style); //To change body of generated methods, choose Tools | Templates.
    }        

    @Override
    protected MessageProvider getMessageProvider() {
        return Application.getInstance().getMessageProvider();
    }

    @Override
    protected ClientTracer getTracer() {
        return Application.getInstance().getTracer();
    }

    public static DefaultTextOptionsProvider getInstance(){
        return INSTANCE;
    }
}
