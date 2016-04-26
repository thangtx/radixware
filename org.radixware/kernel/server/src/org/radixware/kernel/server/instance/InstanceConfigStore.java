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

package org.radixware.kernel.server.instance;

import java.io.File;
import javax.swing.JFrame;
import org.radixware.kernel.common.enums.ERadixApplication;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;

import org.radixware.kernel.server.config.ConfigStore;
import org.radixware.kernel.server.units.UnitView;
import org.radixware.kernel.common.utils.SystemTools;

/**
 * DBP Server configuration manager 
 * 
 */

public final class InstanceConfigStore {
	//private static final String FILE_NAME = RadixConfigDir.getFullName() + File.separator + "DbpServer" + File.separator + "server.ini";
	//private static final String APP_NAME = "Radix.Kernel.Server";
    	private static final String FILE_NAME = "server.properties";

	private static final String MAIN_WIN_BOUNDS = "main_win_bounds";
	private static final String TRACE_LIST_SETTINGS = "trace_list_settings";
	private static final String UNIT_VIEW_BOUNDS = "unit_view_bounds";

	static ConfigStore cfg = new ConfigStore(
            //yremizov
            new File( SystemTools.getRadixApplicationDataPath( ERadixApplication.SERVER ), FILE_NAME )
        );

	static void storeMainWindowBounds(final JFrame view){
		synchronized(cfg){
			cfg.storeWindowBounds(MAIN_WIN_BOUNDS, view);
		}
	}
	static void restoreMainWindowBounds(final JFrame view){
		synchronized(cfg){
			cfg.restoreWindowBounds(MAIN_WIN_BOUNDS, view);
		}
	}
	
	private static final String _VISIBLE = "_visible";
	public static void storeUnitViewBounds(final UnitView view){
		synchronized(cfg){
		    final String cfgKey = UNIT_VIEW_BOUNDS + "_" + String.valueOf(view.getUnit().getId());
		    cfg.storeWindowBounds(cfgKey, view.getDialog());
		    cfg.setProperty(cfgKey + _VISIBLE, String.valueOf(view.isVisible()));
		}
	}
	
	public static void restoreUnitViewBounds(final UnitView view){ 
		synchronized(cfg){
		    final String cfgKey = UNIT_VIEW_BOUNDS + "_" + String.valueOf(view.getUnit().getId());
		    cfg.restoreWindowBounds(cfgKey, view.getDialog());
		    view.setVisible(Boolean.valueOf(cfg.getProperty(cfgKey + _VISIBLE, Boolean.FALSE.toString())));
		}
	}
	
	static void storeTraceListSettings(){
		synchronized(cfg){
			cfg.storeTraceListSettings(TRACE_LIST_SETTINGS);
		}
	}
	static void restoreTraceListSettings(){
		synchronized(cfg){
			cfg.restoreTraceListSettings(TRACE_LIST_SETTINGS);
		}
	}
	public static void save() throws Exception {
		synchronized(cfg){
			cfg.save();
		}
	}
	
}
