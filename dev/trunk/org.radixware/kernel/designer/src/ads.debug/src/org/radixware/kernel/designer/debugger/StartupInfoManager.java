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

package org.radixware.kernel.designer.debugger;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import javax.swing.event.ChangeListener;
import org.openide.util.ChangeSupport;
import org.openide.util.Exceptions;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.kernel.designer.debugger.StartupInfo.EEnvironment;


public final class StartupInfoManager {

    private static StartupInfoManager instance;

    public synchronized static StartupInfoManager getInstance() {
        if (instance == null) {
            instance = new StartupInfoManager();
        }
        return instance;
    }

    /*
     *
     *
     */
    private static final String VERSION = "2.0";
    private static final String VERSION_KEY = "StartupInfoManagerVersion";
    private static final String configNodeNamePrefixOld = "DEBUGGER_LAUNCH_OPTIONS";
    private static final String configNodeNamePrefix = "DebuggerLaunchOptions";

    /*
     * FIELDS
     */
    public static final String DEFAULT_PROFILE_NAME = "Default";
    static final String BACKWARD_COMPATIBILITY_PROFILE_NAME = "";
    static final String PROP_NAME_CURR_PROFILE = "currProf";
    static final String PROP_NAME_PROFILES = "profiles";
    private final ChangeSupport changeSupport = new ChangeSupport(this);

    private StartupInfoManager() {
    }

    /*
     * PRIVATE METHODS
     */
    private boolean isActualVersion(Branch branch) {
        final String key = getConfigPathToBranch(branch);
        if (key != null) {
//            try {
            final Preferences node = Preferences.userRoot().node(key);
            return node.get(VERSION_KEY, "").equals(VERSION);
//            } catch (IllegalArgumentException | IllegalStateException e) {
//                Logger.getLogger(StartupInfoManager.class.getName()).log(Level.WARNING, String.format("Unable to load preference '%s', cause: %s", key, e.getMessage()));
//                return false;
//            }
        }
        return false;
    }

    private void actualizeVersion(Branch branch) {
        final String key = getConfigPathToBranch(branch);
        if (key != null) {
            final Preferences node = Preferences.userRoot().node(key);
            node.put(VERSION_KEY, VERSION);
        }
    }

    private String getConfigPathToBranchOld(Branch branch) {
        if (branch.getFile() == null) {
            return generatePath(configNodeNamePrefixOld, "UNKNOWN");
        } else {
            String path = generatePath(configNodeNamePrefixOld, branch.getFile().getPath().replace(File.separator, "_").replace("/", "_"));
            if (path.length() >= 80) {
                try {
                    path = generatePath(configNodeNamePrefix, UUID.nameUUIDFromBytes(branch.getFile().getPath().replace(File.separator, "$").replace(".", "$").replace("/", "$").getBytes(FileUtils.XML_ENCODING)).toString());
                } catch (UnsupportedEncodingException ex) {
                    path = "";
                }
            }
            return path;
        }
    }

    private String getConfigPathToBranch(Branch branch) {
        if (branch.getFile() == null) {
            return generatePath(configNodeNamePrefix, "UNKNOWN");
        } else {
            String path = generatePath(configNodeNamePrefix, branch.getFile().getPath().replace(File.separator, "$").replace(".", "$").replace("/", "$"));
            if (path.length() >= 80) {
                try {
                    path = generatePath(configNodeNamePrefix, UUID.nameUUIDFromBytes(branch.getFile().getPath().replace(File.separator, "$").replace(".", "$").replace("/", "$").getBytes(FileUtils.XML_ENCODING)).toString());
                } catch (UnsupportedEncodingException ex) {
                    path = "";
                }
            }
            return path;
        }
    }

    private String generatePath(String... parts) {
        return generatePath(false, parts);
    }

    private String generatePath(boolean firstSlash, String... parts) {
        StringBuilder path = new StringBuilder();
        boolean first = !firstSlash;
        for (String part : parts) {
            if (part != null && !part.isEmpty()) {
                if (!first) {
                    path.append('/');
                } else {
                    first = false;
                }
                path.append(part);
            }
        }
        return path.toString();
    }

    private Preferences getRootNode(Branch branch) {
        if (!isActualVersion(branch)) {
            migration(branch);
        }

        return isActualVersion(branch) ? getActualRootNode(branch) : getOldRootNode(branch);
    }

    private Preferences getActualRootNode(Branch branch) {
        return Preferences.userRoot().node(getConfigPathToBranch(branch));
    }

    private Preferences getOldRootNode(Branch branch) {
        return Preferences.userRoot().node(getConfigPathToBranchOld(branch));
    }

    private Preferences getNode(Preferences root, String... path) {
        return root.node(generatePath(path));
    }

    private synchronized void migration(Branch branch) {
        if (isActualVersion(branch)) {
            return;
        }

        final Preferences oldRootNode = getOldRootNode(branch);
        final List<String> profileNameList = getProfileNameList(oldRootNode);
        final List<StartupInfoProfile> profiles = new ArrayList<>();

        for (final String profile : profileNameList) {
            profiles.add(getProfile(branch, oldRootNode, profile));
        }

        final String currentProfileName = getCurrentProfileName(oldRootNode);
        final String profileList = oldRootNode.get(PROP_NAME_PROFILES, DEFAULT_PROFILE_NAME);

        actualizeVersion(branch);

        for (final StartupInfoProfile startupInfoProfile : profiles) {
            startupInfoProfile.save();
        }

        final Preferences actualRootNode = getActualRootNode(branch);
        actualRootNode.put(PROP_NAME_PROFILES, profileList);
        setCurrentProfile(branch, currentProfileName);

        try {
            actualRootNode.flush();
        } catch (BackingStoreException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    private StartupInfo createStartupInfo(Branch branch, Preferences root, EEnvironment environment, String profileName) {

        final StartupInfo info;
        switch (environment) {
            case EXPLORER:
                info = new StartupInfo.ExplorerInfo(branch);
                break;
            case SERVER:
                info = new StartupInfo.ServerInfo(branch);
                break;
            default:
                info = null;
        }

        if (DEFAULT_PROFILE_NAME.equals(profileName)) {
            initDefaultProfile(branch, root, info);
        } else {
            info.restore(getNode(root, profileName, info.getConfigPathSpec()));
        }

        return info;
    }

    private void initDefaultProfile(Branch branch, Preferences root, StartupInfo info) {
        boolean existNode;
        try {
            existNode = root.nodeExists(DEFAULT_PROFILE_NAME);
        } catch (BackingStoreException ex) {
            existNode = false;
        }

        final String key = existNode ? DEFAULT_PROFILE_NAME : "";
        info.restore(getNode(root, key, info.getConfigPathSpec()));
    }

    private boolean existProfile(Branch branch, String name) {
        return isValidName(branch, name) && getProfileNameList(branch).contains(name);
    }

    private boolean isValidName(Branch branch, String name) {
        return branch != null && name != null && !name.isEmpty();
    }

    private void addToProfileNameList(Branch branch, String profileName) {
        assert profileName != null && !profileName.isEmpty();
        final String profiles = getRootNode(branch).get(PROP_NAME_PROFILES, DEFAULT_PROFILE_NAME);
        getRootNode(branch).put(PROP_NAME_PROFILES, profiles + "*" + profileName);
    }

    private void removeFromProfileNameList(Branch branch, String profileName) {
        final List<String> profiles = getProfileNameList(branch);

        final StringBuilder builder = new StringBuilder();
        boolean first = true;
        for (final String prof : profiles) {
            if (!prof.equals(profileName)) {
                if (first) {
                    first = false;
                } else {
                    builder.append("*");
                }
                builder.append(prof);
            }
        }
        final Preferences rootNode = getRootNode(branch);
        rootNode.put(PROP_NAME_PROFILES, builder.toString());
    }

    private List<String> getProfileNameList(Preferences node) {
        if (node != null) {
            final String[] profs = node.get(PROP_NAME_PROFILES, DEFAULT_PROFILE_NAME).split("\\*");
            return Arrays.asList(profs);
        }
        return Collections.<String>emptyList();
    }

    private String getCurrentProfileName(Preferences node) {
        if (node != null) {
            return node.get(PROP_NAME_CURR_PROFILE, DEFAULT_PROFILE_NAME);
        }
        return null;
    }

    private StartupInfoProfile getProfile(Branch branch, Preferences root, String profileName) {

        if (isValidName(branch, profileName)) {

            final Map<EEnvironment, StartupInfo> infoMap = new EnumMap<>(EEnvironment.class);

            for (final EEnvironment environment : EEnvironment.values()) {
                infoMap.put(environment, createStartupInfo(branch, root, environment, profileName));
            }
            return new StartupInfoProfile(branch, profileName, infoMap);
        }
        return null;
    }


    /*
     * INTERNAL
     */
    Preferences getNode(Branch branch, String... path) {
        return getRootNode(branch).node(generatePath(path));
    }

    /*
     * PUBLIC METHODS
     */
    public StartupInfoProfile getCurrentProfile(Branch branch) {
        return getProfile(branch, getCurrentProfileName(branch));
    }

    public synchronized StartupInfoProfile getProfile(Branch branch, String profileName) {
        return getProfile(branch, getRootNode(branch), profileName);
    }

    public boolean addProfile(StartupInfoProfile profile) {
        if (!existProfile(profile.getBranch(), profile.getName())) {
            addToProfileNameList(profile.getBranch(), profile.getName());
            profile.save();
            changeSupport.fireChange();
            return true;
        }
        return false;
    }

    public void removeProfile(Branch branch, String profileName) {
        if (isValidName(branch, profileName) && !DEFAULT_PROFILE_NAME.equals(profileName)) {

            removeFromProfileNameList(branch, profileName);
            final Preferences rootNode = getRootNode(branch);

            try {
                if (rootNode.nodeExists(profileName)) {
                    rootNode.node(profileName).removeNode();
                    changeSupport.fireChange();
                }
            } catch (BackingStoreException ex) {
                //...
            }

            if (profileName.equals(getCurrentProfileName(branch))) {
                setCurrentProfile(branch, DEFAULT_PROFILE_NAME);
            }
        }
    }

    public synchronized List<String> getProfileNameList(Branch branch) {
        if (branch != null) {
            return getProfileNameList(getRootNode(branch));
        }
        return Collections.<String>emptyList();
    }

    public String getCurrentProfileName(Branch branch) {
        if (branch != null) {
            return getCurrentProfileName(getRootNode(branch));
        }
        return null;
    }

    public void setCurrentProfile(Branch branch, String name) {
        if (isValidName(branch, name)) {
            final String lastName = getRootNode(branch).get(PROP_NAME_CURR_PROFILE, DEFAULT_PROFILE_NAME);
            getRootNode(branch).put(PROP_NAME_CURR_PROFILE, name);

            if (!lastName.equals(name)) {
                changeSupport.fireChange();
            }
        }
    }

    public void removeChangeListener(ChangeListener listener) {
        changeSupport.removeChangeListener(listener);
    }

    public void addChangeListener(ChangeListener listener) {
        changeSupport.addChangeListener(listener);
    }
}
