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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import org.openide.util.Exceptions;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.repository.Layer;


public abstract class StartupInfo {

    public enum EEnvironment {

        SERVER, EXPLORER
    }

    public static class Property {

        private final String name;
        private String value;

        public Property(String name, String value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

    }

    static class ServerInfo extends StartupInfo {

        public ServerInfo(Branch branch) {
            super(branch);
        }

        private ServerInfo(ServerInfo info) {
            super(info);
        }

        @Override
        public String[] getPropertyNames() {
            return new String[]{ PROP_NAME_JVM_ARGS, PROP_NAME_APP_ARGS, PROP_NAME_TOP_LAYER, PROP_NAME_STARTER_ARGS, PROP_NAME_CLASS_PATH, PROP_NAME_WORK_DIR };
        }

        @Override
        public String getConfigPathSpec() {
            return "server4";
        }

        @Override
        public String getAppArgsHint() {
            return "(-dbUrl, -user, -pwd, -dbSchema, -development, -instance, -autostart)";
        }

        @Override
        public StartupInfo.EEnvironment getEnvironment() {
            return StartupInfo.EEnvironment.SERVER;
        }

        @Override
        StartupInfo copy() {
            ServerInfo copy = new ServerInfo(this);
            return copy;
        }

    }

    static class ExplorerInfo extends StartupInfo {

        public ExplorerInfo(Branch branch) {
            super(branch);
        }

        private ExplorerInfo(ExplorerInfo info) {
            super(info);
        }

        @Override
        public String[] getPropertyNames() {
            return new String[]{ PROP_NAME_JVM_ARGS, PROP_NAME_APP_ARGS, PROP_NAME_TOP_LAYER, PROP_NAME_STARTER_ARGS, PROP_NAME_CLASS_PATH, PROP_NAME_WORK_DIR };
        }

        @Override
        public String getConfigPathSpec() {
            return "explorer4";
        }

        @Override
        public String getAppArgsHint() {
            return "(-language, -configPath, -consoleEncoding, -traceFile, -traceProfile, -development, -connection, -user, -pwd)";
        }

        @Override
        public StartupInfo.EEnvironment getEnvironment() {
            return StartupInfo.EEnvironment.EXPLORER;
        }

        @Override
        StartupInfo copy() {
            return new ExplorerInfo(this);
        }

    }

    /*
     * STATIC
     */
    static final String PROP_NAME_CLASS_PATH = "classpath";
    static final String PROP_NAME_JVM_ARGS = "jvmArgs";
    static final String PROP_NAME_APP_ARGS = "appArgs";
    static final String PROP_NAME_STARTER_ARGS = "starterArgs";
    static final String PROP_NAME_TOP_LAYER = "topLayer";
    static final String PROP_NAME_WORK_DIR = "workdir";

    /*
     * FIELDS
     */
    private final Map<String, Property> props;
    protected final Branch branch;

    StartupInfo(StartupInfo info) {
        this.branch = info.getBranch();
        this.props = info.getPropsCopy();
    }

    private Map<String, Property> getPropsCopy() {
        Map<String, Property> copy = new HashMap<>(props.size());

        for (Map.Entry<String, Property> entry : props.entrySet()) {
            copy.put(entry.getKey(), new Property(entry.getValue().name, entry.getValue().value));
        }
        return copy;
    }

    protected StartupInfo(Branch branch) {
        this.branch = branch;
        props = new HashMap<>();
    }

    public final Branch getBranch() {
        return branch;
    }


    public String getJVMArguments() {
        return getPropertyByName(PROP_NAME_JVM_ARGS).getValue();
    }

    public String getClassPath() {
        return getPropertyByName(PROP_NAME_CLASS_PATH).getValue();
    }

    public String getAppArguments() {
        return getPropertyByName(PROP_NAME_APP_ARGS).getValue();
    }

    public String getTopLayerURI() {
        return getPropertyByName(PROP_NAME_TOP_LAYER).getValue();
    }

    public String getWorkDir() {
        return getPropertyByName(PROP_NAME_WORK_DIR).getValue();
    }

    public void setAppArguments(String args) {
        getPropertyByName(PROP_NAME_APP_ARGS).setValue(args);
    }

    public String getStarterArguments() {
        return getPropertyByName(PROP_NAME_STARTER_ARGS).getValue();
    }

    public void setStarterArguments(String args) {
        getPropertyByName(PROP_NAME_STARTER_ARGS).setValue(args);
    }

    public void setTopLayerURI(String args) {
        getPropertyByName(PROP_NAME_TOP_LAYER).setValue(args);
    }

    public void setJVMArguments(String args) {
        getPropertyByName(PROP_NAME_JVM_ARGS).setValue(args);
    }

    public void setClassPath(String classpath) {
        getPropertyByName(PROP_NAME_CLASS_PATH).setValue(classpath);
    }

    public void setWorkDir(String workdir) {
        getPropertyByName(PROP_NAME_WORK_DIR).setValue(workdir);
    }

    public String getPropDefault(String name) {
        if (name != null) {
            switch (name) {
                case PROP_NAME_JVM_ARGS:
                    return "-server -Xmx1024m -XX:MaxPermSize=256m";
                case PROP_NAME_APP_ARGS:
                    return "-development";
                case PROP_NAME_TOP_LAYER:
                    List<Layer> tops = branch.getLayers().findTop();
                    return tops.isEmpty() ? "" : tops.get(0).getURI();
                case PROP_NAME_STARTER_ARGS:
                    if (branch.getDirectory() != null) {
                        return "-workDir=" + branch.getDirectory().getAbsolutePath();
                    } else {
                        return null;
                    }
                case PROP_NAME_CLASS_PATH:
                    return "";
                case PROP_NAME_WORK_DIR:
                    return "";
            }
        }
        return null;
    }

    public String getPropDisplayName(String name) {
        if (name != null) {
            switch (name) {
                case PROP_NAME_JVM_ARGS:
                    return "Java VM arguments";
                case PROP_NAME_APP_ARGS:
                    return "Server command line arguments";
                case PROP_NAME_TOP_LAYER:
                    return "Top layer URI";
                case PROP_NAME_STARTER_ARGS:
                    return "Starter arguments";
                case PROP_NAME_CLASS_PATH:
                    return "Classpath list";
                case PROP_NAME_WORK_DIR:
                    return "Working directory";
            }
        }
        return null;
    }

    public Property getPropertyByName(String name) {
        synchronized (props) {
            Property prop = props.get(name);
            if (prop == null) {
                prop = new Property(name, getPropDefault(name));
                props.put(name, prop);
            }
            return prop;
        }
    }

    public void restore(Preferences node) {
        String[] names = getPropertyNames();
        synchronized (props) {
            props.clear();
            for (String name : names) {
                String value = node.get(name, getPropDefault(name));
                props.put(name, new Property(name, value));
            }
        }
    }

    public void save(Preferences node) {
        synchronized (props) {
            for (Property prop : props.values()) {
                node.put(prop.getName(), prop.getValue());
            }
        }
        try {
            node.flush();
        } catch (BackingStoreException ex) {
            Exceptions.printStackTrace(ex);
        }
    }


    public abstract String[] getPropertyNames();

    public abstract String getAppArgsHint();

    public abstract String getConfigPathSpec();

    public abstract EEnvironment getEnvironment();

    abstract StartupInfo copy();
}
