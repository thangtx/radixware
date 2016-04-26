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

package org.radixware.kernel.common.defs.ads.build;

import java.io.File;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.prefs.Preferences;
import javax.imageio.spi.ServiceRegistry;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.types.Id;


public class BuildOptions {

    public static class Factory {

        public static final BuildOptions newInstance() {
            //try to restore previously used options
            return new BuildOptions();
        }
    }
    private EnumSet<ERuntimeEnvironmentType> env = EnumSet.allOf(ERuntimeEnvironmentType.class);
    private boolean preview;
    private boolean verifyClassLinkage = false;
    private boolean multythread = false;
    private boolean skipCheck = false;
    private boolean buildUds = false;
    private UserModeHandler userModeHandler;
    private Map<String, Object> props = new HashMap<>();

    private BuildOptions() {
        restoreConfig();
    }

    public interface UserModeHandler {

        public abstract void acceptUserDefinitionRuntime(Id id, File jarFile);

        public abstract void acceptBuildTarget(Definition def, Map<Id, Id> idsMap);

        public void startBuild();

        public void finishBuild();
    }

    public void putProperty(String name, Object val) {
        props.put(name, val);
    }

    public Object getProperty(String name) {
        return props.get(name);
    }

    public interface UserModeHandlerProvider {

        public UserModeHandler getUserModeHandler();
    }

    public static class UserModeHandlerLookup {

        public static UserModeHandler getUserModeHandler() {
            final Iterator<UserModeHandlerProvider> iter = ServiceRegistry.lookupProviders(UserModeHandlerProvider.class);
            while (iter.hasNext()) {
                UserModeHandler handler = iter.next().getUserModeHandler();
                if (handler != null) {
                    return handler;
                }
            }
            return null;
        }
    }

    public EnumSet<ERuntimeEnvironmentType> getEnvironment() {
        return env;
    }

    public void setEnvironment(EnumSet<ERuntimeEnvironmentType> env) {
        this.env = env;
    }
    private static final String option_name = "RadixBranchBuildOptions";
    private static final String VERIFY_LINKAGE_OPTION_NAME = "Verify";
    private static final String SKIP_CHECK_OPTION_NAME = "SkipCheck";
    private static final String BUILD_UDS_OPTION_NAME = "BuildUDS";
    private static final String MULTYTHREAD_LINKAGE_OPTION_NAME = "Multythread";

    public void saveConfig() {
        Preferences prefs = Preferences.userRoot().node(option_name);
        for (ERuntimeEnvironmentType e : ERuntimeEnvironmentType.values()) {
            if (env.contains(e)) {
                prefs.putBoolean(e.name(), true);
            } else {
                prefs.putBoolean(e.name(), false);
            }
        }
        prefs.putBoolean(VERIFY_LINKAGE_OPTION_NAME, verifyClassLinkage);
        prefs.putBoolean(MULTYTHREAD_LINKAGE_OPTION_NAME, multythread);
        prefs.putBoolean(SKIP_CHECK_OPTION_NAME, skipCheck);
        prefs.putBoolean(BUILD_UDS_OPTION_NAME, buildUds);
    }

    public void restoreConfig() {
        Preferences prefs = Preferences.userRoot().node(option_name);
        for (ERuntimeEnvironmentType e : ERuntimeEnvironmentType.values()) {
            if (prefs.getBoolean(e.name(), false)) {
                env.add(e);
            } else {
                env.remove(e);
            }
        }
        verifyClassLinkage = prefs.getBoolean(VERIFY_LINKAGE_OPTION_NAME, false);
        multythread = prefs.getBoolean(MULTYTHREAD_LINKAGE_OPTION_NAME, false);
        skipCheck = prefs.getBoolean(SKIP_CHECK_OPTION_NAME, false);
        buildUds = prefs.getBoolean(BUILD_UDS_OPTION_NAME, false);
    }

    public boolean isVerifyClassLinkage() {
        return verifyClassLinkage;
    }

    public void setVerifyClassLinkage(boolean verifyClassLinkage) {
        this.verifyClassLinkage = verifyClassLinkage;
    }

    public boolean isMultythread() {
        return multythread;
    }

    public void setMultythread(boolean multythread) {
        this.multythread = multythread;
    }

    public boolean isSkipCheck() {
        return skipCheck;
    }

    public void setSkipCheck(boolean skipCheck) {
        this.skipCheck = skipCheck;
    }

    public boolean isBuildUds() {
        return buildUds;
    }

    public void setBuildUds(boolean buildUds) {
        this.buildUds = buildUds;
    }

    public UserModeHandler getUserModeHandler() {
        return userModeHandler;
    }

    public void setUserMode(UserModeHandler userModeHandler) {
        this.userModeHandler = userModeHandler;
    }
}
