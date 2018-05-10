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
package org.radixware.wps;

import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import org.radixware.kernel.common.client.eas.IEasSession;
import org.radixware.kernel.common.client.enums.EFontWeight;
import org.radixware.kernel.common.client.env.ClientSettings;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.common.client.errors.AuthError;
import org.radixware.kernel.common.client.exceptions.ClientException;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.text.ITextOptions;
import org.radixware.kernel.common.client.text.TextOptions;
import org.radixware.kernel.common.client.types.Pid;
import org.radixware.kernel.common.client.utils.IContextEnvironment;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.settings.Prop;
import org.radixware.schemas.settings.SettingsDocument;
import org.radixware.wps.text.WpsTextOptions;

public class WpsSettings implements ClientSettings {

    private static final class Value {

        private String asStr;

        public Value(String asStr) {
            this.asStr = asStr;
        }

        public Value(boolean val) {
            this.asStr = String.valueOf(val);
        }

        public Value(int val) {
            this.asStr = String.valueOf(val);
        }

        public Value(Id val) {
            this.asStr = String.valueOf(val);
        }

        public Value(Pid val) {
            this.asStr = val.toStr();
        }

        public Value(double ps) {
            this.asStr = String.valueOf(ps);
        }

        public Value(Object ps) {
            this.asStr = String.valueOf(ps);
        }

        boolean getBoolean() {
            return Boolean.parseBoolean(asStr);
        }

        int getInt() {
            return Integer.parseInt(asStr);
        }

        Id getId() {
            return Id.Factory.loadFrom(asStr);
        }

        Pid getPid() {
            return Pid.fromStr(asStr);
        }

        double getDouble() {
            return Double.parseDouble(asStr);
        }
    }

    private static class Group {

        private final String name;

        Group(String name) {
            this.name = name;
        }

        String getName() {
            return name;
        }

        @Override
        public String toString() {
            return getName();
        }
    }

    private abstract static class ArrayGroup extends Group {

        private int idx = -1;

        ArrayGroup(String name) {
            super(name);
        }

        void setIndex(int idx) {
            this.idx = idx;
        }

        int getIndex() {
            return idx;
        }

        @Override
        public String toString() {
            return getName() + "/" + String.valueOf(idx);
        }
    }

    private static class ReadArrayGroup extends ArrayGroup {

        ReadArrayGroup(String name) {
            super(name);
        }
    }

    private static class WriteArrayGroup extends ArrayGroup {

        private int size = -1;
        private final Set<Integer> indexes = new HashSet<>();

        WriteArrayGroup(String name, int size) {
            super(name);
            this.size = size;
        }

        @Override
        void setIndex(int idx) {
            super.setIndex(idx);
            indexes.add(idx);
        }

        int getSize() {
            return size < 0 ? indexes.size() : size;
        }
    }
    
    private final static String FONT_WEIGTH_KEY = "FONT_WEIGTH";
    
    private final Stack<Group> groups = new Stack<>();
    private final Stack<Stack<Group>> groupsStack = new Stack<>();
    private final Map<String, Value> map = new HashMap<>();
    private final WpsEnvironment env;
    private String browser;
    private String browserVersion;
    private DerbyDb derby;
    private File customStorage;
    
    public WpsSettings(){//local settings in memory
        this(null);
    }

    public WpsSettings(WpsEnvironment env) {
        super();
        this.env = env;
        initCustomDBStorage();
    }

    public void close() {
        if (derby != null) {
            derby.shutdown();
            derby = null;
        }
    }

    private boolean customDBStoragePathIsValid() {
        if (customStorage != null) {
            if (!customStorage.exists()) {
                try {
                    if (!customStorage.mkdirs()) {
                        return false;
                    }                    
                } catch (SecurityException ex) {
                    return false;
                }
            }
            return customStorage.isDirectory() && customStorage.canRead() && customStorage.canWrite();
        }
        return false;
    }

    private File initCustomDBStorage() {
        String path = env==null ? null : env.getRunParams().getSettingsDatabaseDir();
        if (path == null) {
            customStorage = null;
        } else {
            customStorage = new File(path);
        }
        return customStorage;
    }

    private DerbyDb getDerby() {
        if (derby == null) {
            derby = new DerbyDb(env, map);
            derby.createConnection();
        }
        return derby;
    }

    public void acceptInitParams(String actionParam) {
        if (actionParam != null) {
            String[] parts = actionParam.split(",");
            for (String p : parts) {
                int index = p.indexOf('=');
                if (index > 0 && index < p.length()) {
                    String key = p.substring(0, index);
                    String value = p.substring(index + 1);
                    endAllGroups();
                    if ("ln".equals(key)) {
                        beginGroup("LogonDialog");
                        writeString("userName", value);
                        endGroup();
                    } else if ("ls".equals(key)) {
                        beginGroup("LogonDialog");
                        writeString("stationName", value);
                        endGroup();
                    } else if ("ll".equals(key)) {
                        beginGroup("LogonDialog");
                        writeString("language", value);
                        endGroup();
                    } else if ("initNodes".equals(key)) {
                        beginGroup("ExplorerTree");
                        writeString("initialNodes", value);
                        endGroup();
                    } else if ("browser".equals(key)) {
                        browser = value;
                    } else if ("browser_version".equals(key)) {
                        browserVersion = value;
                    }
                }
            }
        }
    }

    public String getBrowser() {
        return browser;
    }

    public int[] getBrowserVersion() {
        if (browserVersion == null || browserVersion.isEmpty()) {
            return new int[]{0};
        }
        String[] parts = browserVersion.split("\\.");
        int[] version = new int[parts.length];
        for (int i = 0; i < version.length; i++) {
            try {
                version[i] = Integer.parseInt(parts[i]);
            } catch (NumberFormatException e) {
                version[i] = 0;
            }
        }
        return version;
    }

    private class DerbyDb {

        static final String DB_URL = "jdbc:derby:wps;create=true;user=wps;password=wps";
        static final String DRIVER_NAME = "org.apache.derby.jdbc.EmbeddedDriver";
        final WpsEnvironment env;
        final Map<String, Value> map;
        Connection conn = null;
        PreparedStatement qryStmt = null;
        PreparedStatement qryAllStmt = null;
        PreparedStatement qryGroupStmt = null;
        PreparedStatement updStmt = null;
        PreparedStatement insStmt = null;
        PreparedStatement delAllStmt = null;
        PreparedStatement delGroupStmt = null;
        final Set<String> modifiedProps = new HashSet<>();

        DerbyDb(WpsEnvironment env, Map<String, Value> map) {
            this.env = env;
            this.map = map;
        }

        private String createDbUrl() {
            if (customStorage == null) {
                return DB_URL;
            }
            boolean customPathIsValid = customDBStoragePathIsValid();
            if (!customPathIsValid) {
                return null;
            }
            final String jdbcDerby = "jdbc:derby:";
            final String dbName = "/wps";
            final String createAttr = "create=true";
            final String credentials = "user=wps;password=wps";
            final String separator = ";";

            StringBuilder db_url = new StringBuilder();
            db_url.append(jdbcDerby);
            final String customDBStoragePath = customStorage.getAbsolutePath().replaceAll("\\\\", "/");
            db_url.append(customDBStoragePath);
            db_url.append(dbName);
            db_url.append(separator);
            db_url.append(createAttr);
            db_url.append(separator);
            db_url.append(credentials);
            return db_url.toString();
        }

        void createConnection() {
            if (env==null){
                return;//local settings;
            }
            try {                
                Class.forName(DRIVER_NAME).newInstance();
                final String url = createDbUrl();
                if (url==null){
                    traceError(null);
                    return;
                }else{
                    conn = DriverManager.getConnection(url);
                }
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException e) {
                traceError(e);
                return;
            }

            Statement st = null;
            try {
                st = conn.createStatement();
                st.executeUpdate(
                        "CREATE TABLE APP.WEBSETTING("
                        + "USERNAME VARCHAR(250) not null,"
                        + "PROPNAME VARCHAR(1000) not null,"
                        + "PROPVALUE CLOB,"
                        + "PRIMARY KEY (USERNAME, PROPNAME))");
            } catch (SQLException e) {
                // do nothing
            } finally {
                if (st != null) {
                    try {
                        st.close();
                    } catch (Exception ex) {
                    }
                }
            }
        }
        
        private void traceError(final Throwable exception){            
            final String message;
            if (exception==null){
                message=env.getMessageProvider().translate("TraceMessage", "Local settings storage is not accessible");
            }else{
                final String messageTemplate=env.getMessageProvider().translate("TraceMessage", "Failed to open local settings storage\n %1$s");
                message = String.format(messageTemplate, ClientException.exceptionStackToString(exception));
            }
            env.getTracer().warning(message);
        }

        void shutdown() {
            if (env==null || conn==null){
                return;
            }
            try {
                if (qryStmt != null) {
                    qryStmt.close();
                }
                if (qryAllStmt != null) {
                    qryAllStmt.close();
                }
                if (qryGroupStmt != null) {
                    qryGroupStmt.close();
                }
                if (updStmt != null) {
                    updStmt.close();
                }
                if (insStmt != null) {
                    insStmt.close();
                }
                if (delAllStmt != null) {
                    delAllStmt.close();
                }
                if (delGroupStmt != null) {
                    delGroupStmt.close();
                }
                if (conn != null) {
//                    DriverManager.getConnection(DB_URL + ";shutdown=true");
                    conn.close();
                }
            } catch (SQLException e) {
                env.getTracer().error(e);
            }
        }

        Value getValue(String key) {
            if (key.length() > 1000) {
                throw new IllegalArgumentException("The key length is over 1000 symbols.");
            }
            if (getUserName() == null || map.containsKey(key)) {
                return map.get(key);
            }

            if (conn == null) {
                return null;
            }

            Value val = null;
            try {
                if (qryStmt == null) {
                    qryStmt = conn.prepareStatement("SELECT PROPVALUE from APP.WEBSETTING WHERE USERNAME = ? AND PROPNAME = ?");
                }
                qryStmt.setString(1, getUserName());
                qryStmt.setString(2, key);

                ResultSet rs = qryStmt.executeQuery();
                if (rs.next()) {
                    val = new Value(rs.getString("PROPVALUE"));
                }
                rs.close();
            } catch (SQLException e) {
                env.getTracer().error(e);
            }

            return val;
        }

        void setValue(String key, Value val) {
            if (key.length() > 1000) {
                throw new IllegalArgumentException("The key length is over 1000 symbols.");
            }
            if (getUserName() == null) { // local settings
                map.put(key, val);
                return;
            }

            if (conn == null) {
                return;
            }

            Value v = val;
            if (v == null) {
                v = new Value((String) null);
            }

            try {
                if (insStmt == null) {
                    insStmt = conn.prepareStatement("INSERT INTO APP.WEBSETTING(USERNAME, PROPNAME, PROPVALUE) VALUES(?, ?, ?)");
                }

                insStmt.setString(1, getUserName());
                insStmt.setString(2, key);
                insStmt.setString(3, v.asStr);

                insStmt.executeUpdate();
            } catch (SQLException e) {
                if (e.getSQLState().equals("23505")) { // duplicate
                    try {
                        if (updStmt == null) {
                            updStmt = conn.prepareStatement("UPDATE APP.WEBSETTING SET PROPVALUE = ? WHERE USERNAME = ? AND PROPNAME = ?");
                        }

                        updStmt.setString(1, v.asStr);
                        updStmt.setString(2, getUserName());
                        updStmt.setString(3, key);

                        updStmt.executeUpdate();
                    } catch (SQLException ex) {
                        env.getTracer().error(ex);
                    }
                } else {
                    env.getTracer().error(e);
                }
            }
            if (!LAST_CHANGE_TIME_PROP.equals(key)) {
                modifiedProps.add(key);
            }
        }

        void setModifiedProps(SettingsDocument doc) {
            for (Prop prop : doc.getSettings().getPropList()) {
                setValue(prop.getName(), new Value(prop.getValue()));
            }
        }

        SettingsDocument getModifiedProps() {
            SettingsDocument doc = SettingsDocument.Factory.newInstance();
            doc.addNewSettings();

            if (conn == null || modifiedProps.isEmpty()) {
                return doc;
            }

            for (String key : modifiedProps) {
                Value val = getValue(key);
                if (val != null) {
                    Prop prop = doc.getSettings().addNewProp();
                    prop.setName(key);
                    prop.setValue(val.asStr);
                }
            }
            modifiedProps.clear();
            return doc;
        }

        SettingsDocument getProps() {
            SettingsDocument doc = SettingsDocument.Factory.newInstance();
            doc.addNewSettings();

            if (conn == null) {
                return doc;
            }

            try {
                if (qryStmt == null) {
                    qryStmt = conn.prepareStatement("SELECT PROPNAME, PROPVALUE from APP.WEBSETTING WHERE USERNAME = ? ORDER BY PROPNAME");
                }
                qryStmt.setString(1, getUserName());

                ResultSet rs = qryStmt.executeQuery();
                while (rs.next()) {
                    Prop prop = doc.getSettings().addNewProp();
                    prop.setName(rs.getString("PROPNAME"));
                    prop.setValue(rs.getString("PROPVALUE"));
                }
                rs.close();
            } catch (SQLException e) {
                env.getTracer().error(e);
            }

            return doc;
        }

        SettingsDocument getGroupProps(String group) {
            SettingsDocument doc = SettingsDocument.Factory.newInstance();
            doc.addNewSettings();

            if (conn == null) {
                return doc;
            }

            try {
                if (qryGroupStmt == null) {
                    qryGroupStmt = conn.prepareStatement("SELECT PROPNAME, PROPVALUE from APP.WEBSETTING WHERE USERNAME = ? and PROPNAME LIKE  ? || '%' ORDER BY PROPNAME");
                }
                qryGroupStmt.setString(1, getUserName());
                qryGroupStmt.setString(2, group);

                ResultSet rs = qryGroupStmt.executeQuery();
                while (rs.next()) {
                    Prop prop = doc.getSettings().addNewProp();
                    prop.setName(rs.getString("PROPNAME"));
                    prop.setValue(rs.getString("PROPVALUE"));
                }
                rs.close();
            } catch (SQLException e) {
                env.getTracer().error(e);
            }

            return doc;
        }

        void clear() {
            if (conn == null) {
                return;
            }

            try {
                if (delAllStmt == null) {
                    delAllStmt = conn.prepareStatement("UPDATE APP.WEBSETTING SET PROPVALUE = NULL WHERE USERNAME = ?");
                }

                delAllStmt.setString(1, getUserName());
                delAllStmt.executeUpdate();
            } catch (SQLException e) {
                env.getTracer().error(e);
            }
        }

        void clearGroup(String group) {
            if (conn == null) {
                return;
            }

            try {
                if (delGroupStmt == null) {
                    delGroupStmt = conn.prepareStatement("UPDATE APP.WEBSETTING SET PROPVALUE = NULL WHERE USERNAME = ? AND PROPNAME LIKE ? || '%'");
                }

                delGroupStmt.setString(1, getUserName());
                delGroupStmt.setString(2, group);
                delGroupStmt.executeUpdate();
            } catch (SQLException e) {
                env.getTracer().error(e);
            }
        }

        String getUserName() {
            String userName = env==null ? null : env.getUserName();
            if (userName == null) {
                return null;
            }
            return userName + "_" + env.getStationName();
        }
        static final String LAST_CHANGE_TIME_PROP = "LastChangeTime";

        void setLastChangeTime(Timestamp time) {
            setValue(LAST_CHANGE_TIME_PROP, new Value(time != null ? time.toString() : (String) null));
        }

        Timestamp getLastChangeTime() {
            Value val = getValue(LAST_CHANGE_TIME_PROP);
            return (val != null && val.asStr != null) ? Timestamp.valueOf(val.asStr) : null;
        }
    }
    private final static Id SYNC_COMMAND_ID = Id.Factory.loadFrom("clcPYOHQ5AECZBBHAOZWF74OQQ6SE");
    public long lastSyncTime = 0;

    public void syncDb(boolean init) {
        IEasSession backgroundSession = null;
        try {
            if (env==null || env.getEasSession() == null) {
                return;
            }

            SettingsDocument rqDoc = getDerby().getModifiedProps();

            if (rqDoc.getSettings().getPropList().isEmpty() && !init) {
                return;
            }

            rqDoc.getSettings().setLastChangeTime(derby.getLastChangeTime());
            SettingsDocument rsDoc = null;
            try {
                if (Thread.currentThread() instanceof IContextEnvironment == false) {
                    backgroundSession = env.getEasSession().createBackgroundSession();
                    rsDoc = (SettingsDocument) backgroundSession.executeContextlessCommand(SYNC_COMMAND_ID, rqDoc, SettingsDocument.class);
                } else {
                    rsDoc = (SettingsDocument) env.getEasSession().executeContextlessCommand(SYNC_COMMAND_ID, rqDoc, SettingsDocument.class);
                }
            }catch (AuthError e) {
                env.getTracer().debug(e);
                return;                
            } catch (ServiceClientException | InterruptedException e) {
                env.getTracer().error(e);
                return;
            } 

            getDerby().setModifiedProps(rsDoc);
            derby.setLastChangeTime(rsDoc.getSettings().getLastChangeTime());
        } finally {
            lastSyncTime = System.currentTimeMillis();
            if (backgroundSession != null) {
                backgroundSession.close(true);
            }
        }
    }

    private String getKey(String key) {
        String group = group();
        if (group.isEmpty()) {
            return key;
        } else {
            return group + "/" + key;
        }
    }

    @Override
    public boolean contains(String key) {
        return getDerby().getValue(getKey(key)) != null;
    }

    @Override
    public void writeBoolean(String key, boolean b) {
        getDerby().setValue(getKey(key), new Value(b));
    }

    @Override
    public void writeInteger(String key, int i) {
        getDerby().setValue(getKey(key), new Value(i));
    }

    @Override
    public void writeString(String key, String s) {
        getDerby().setValue(getKey(key), new Value(s));
    }

    @Override
    public void writeId(String key, Id id) {
        getDerby().setValue(getKey(key), id==null ? null : new Value(id));
    }

    @Override
    public void writePid(String key, Pid pid) {
        getDerby().setValue(getKey(key), pid==null ? null : new Value(pid));
    }

    @Override
    public void writeDouble(String key, double d) {
        getDerby().setValue(getKey(key), new Value(d));
    }
    
    public void writeColor(final String key, final Color color){
        getDerby().setValue(getKey(key), color==null ? null : new Value(TextOptions.color2Str(color)));
    }

    @Override
    public void setValue(String key, Object value) {
        getDerby().setValue(getKey(key), value ==null ? null : new Value(value));
    }

    @Override
    public boolean readBoolean(String key, boolean defaultBoolean) {
        Value obj = getDerby().getValue(getKey(key));
        if (obj != null) {
            return obj.getBoolean();
        } else {
            return defaultBoolean;
        }
    }

    @Override
    public boolean readBoolean(String key) {
        final String actualKey = getKey(key);
        Value obj = getDerby().getValue(actualKey);
        if (obj != null) {
            return obj.getBoolean();
        } else {
            final String booleanAsStr = DefaultSettings.getInstance().getValue(actualKey);
            return booleanAsStr==null || booleanAsStr.isEmpty() ? false : Boolean.parseBoolean(booleanAsStr);
        }
    }

    @Override
    public int readInteger(String key, int defaultInteger) {
        Value obj = getDerby().getValue(getKey(key));
        if (obj != null) {
            try {
                return obj.getInt();
            } catch (NumberFormatException e) {
                return defaultInteger;
            }
        } else {
            return defaultInteger;
        }
    }

    @Override
    public int readInteger(String key) {
        final String actualKey = getKey(key);
        Value obj = getDerby().getValue(actualKey);
        if (obj != null) {
            return obj.getInt();
        } else {
            final String intAsStr = DefaultSettings.getInstance().getValue(actualKey);
            try{
                return intAsStr==null || intAsStr.isEmpty() ? 0 : Integer.parseInt(intAsStr);
            }catch(NumberFormatException ex){
                return 0;
            }
        }        
    }

    @Override
    public double readDouble(String key, double defaultDouble) {
        Value obj = getDerby().getValue(getKey(key));
        if (obj != null) {
            try {
                return obj.getDouble();
            } catch (NumberFormatException e) {
                return defaultDouble;
            }
        } else {
            return defaultDouble;
        }
    }

    @Override
    public double readDouble(String key) {
        final String actualKey = getKey(key);
        Value obj = getDerby().getValue(actualKey);
        if (obj != null) {
            return obj.getDouble();
        } else {
            final String doubleAsStr = DefaultSettings.getInstance().getValue(actualKey);
            try{
                return doubleAsStr==null || doubleAsStr.isEmpty() ? 0 : Double.parseDouble(doubleAsStr);
            }catch(NumberFormatException ex){
                return 0;
            }
        }
    }

    @Override
    public String readString(final String key) {
        final String actualKey = getKey(key);
        Value obj = getDerby().getValue(actualKey);
        if (obj != null) {
            return obj.asStr;
        } else {
            return DefaultSettings.getInstance().getValue(actualKey);
        }
    }

    @Override
    public String readString(String key, String defaultString) {
        Value obj = getDerby().getValue(getKey(key));
        if (obj != null) {
            return obj.asStr;
        } else {
            return defaultString;
        }
    }

    @Override
    public Dimension readSize(String key) {
        String size = null;
        final String actualKey = getKey(key);
        Value obj = getDerby().getValue(actualKey);
        if (obj != null) {
            size = obj.asStr;
        } else {
            size = DefaultSettings.getInstance().getValue(actualKey);
        }
        if (size != null) {

            int index = size.indexOf(";");
            if (index > 0) {
                String w = size.substring(0, index);
                String h = size.substring(index + 1);
                try {
                    return new Dimension(Integer.parseInt(w), Integer.parseInt(h));
                } catch (NumberFormatException e) {
                    return null;
                }
            } else {
                return null;
            }
        }
        return null;
    }

    @Override
    public Id readId(final String key) {
        final String actualKey = getKey(key);
        Value obj = getDerby().getValue(actualKey);
        if (obj != null) {
            return obj.getId();
        } else {            
            final String idAsStr = DefaultSettings.getInstance().getValue(key);
            return idAsStr==null || idAsStr.isEmpty() ? null : Id.Factory.loadFrom(idAsStr);
        }
    }

    @Override
    public Pid readPid(final String key) {
        final String actualKey = getKey(key);
        Value obj = getDerby().getValue(actualKey);
        if (obj != null) {
            return obj.getPid();
        } else {
            final String pidAsStr = DefaultSettings.getInstance().getValue(actualKey);
            return pidAsStr==null || pidAsStr.isEmpty() ? null : Pid.fromStr(pidAsStr);
        }
    }
    
    public Color readColor(final String key, final Color defaultColor){
        final String value = readString(key);
        if (value==null || value.isEmpty()){
            return defaultColor;
        }
        try{
            return Color.decode(value);
        }catch(NumberFormatException exception){
            traceWarning(Color.class, key, exception); //config corrupted
            return defaultColor;
        }
    }

    @Override
    public Object value(String key) {
        final String actualKey = getKey(key);
        Value obj = getDerby().getValue(actualKey);
        if (obj != null) {
            return obj.asStr;
        } else {
            return DefaultSettings.getInstance().getValue(actualKey);
        }
    }

    @Override
    public Object value(String key, Object defaultValue) {
        Value obj = getDerby().getValue(getKey(key));
        if (obj != null) {
            return obj.asStr;
        } else {
            return defaultValue;
        }
    }

    public String getValue(String key) {
        Value obj = getDerby().getValue(getKey(key));
        if (obj != null) {
            return obj.asStr;
        } else {
            return DefaultSettings.getInstance().getValue(key);
        }
    }

    @Override
    public List<String> allKeys() {
        final List<String> keys = new ArrayList<>();
        final SettingsDocument doc = getDerby().getProps();
        for (Prop prop : doc.getSettings().getPropList()) {
            if (prop.isSetValue()) {
                keys.add(prop.getName());
            }
        }
        return keys;
    }

    @Override
    public void beginGroup(String group) {
        groups.push(new Group(group));
    }

    @Override
    public void pushGroup() {                
        final Stack<Group> keepGroups = new Stack<>();
        keepGroups.addAll(groups);
        groupsStack.add(keepGroups);
    }

    @Override
    public String popGroup() {
        final Stack<Group> keepGroups = groupsStack.pop();        
        groups.clear();
        groups.addAll(keepGroups);
        return group();
    }

    @Override
    public void setConfigProfile(String profile) {
    }

    @Override
    public String getConfigProfile() {
        return "";
    }        

    @Override
    public int beginReadArray(String array) {
        int size = readInteger(array + "/size", -1);
        groups.push(new ReadArrayGroup(array));
        return size;
    }

    @Override
    public void beginWriteArray(String array) {
        beginWriteArray(array, -1);
    }

    @Override
    public void beginWriteArray(String array, int size) {
        groups.push(new WriteArrayGroup(array, size));
    }

    @Override
    public void setArrayIndex(int i) {
        ArrayGroup g = (ArrayGroup) groups.peek();
        g.setIndex(i);
    }

    @Override
    public void endArray() {
        ArrayGroup g = (ArrayGroup) groups.pop();
        if (g instanceof WriteArrayGroup) {
            int size = ((WriteArrayGroup) g).getSize();
            writeInteger(g.getName() + "/size", size);
        }
    }

    @Override
    public List<String> childGroups() {
        final String key = getKey("");
        final List<String> keys = new ArrayList<>();
        final SettingsDocument doc = getDerby().getGroupProps(key);
        for (Prop prop : doc.getSettings().getPropList()) {
            if (!prop.isSetValue()) {
                continue;
            }
            final String[] parts = prop.getName().substring(key.length()).split("/");
            if (parts.length > 1 && !keys.contains(parts[0])) {
                keys.add(parts[0]);
            }
        }
        return keys;
    }

    @Override
    public List<String> childKeys() {
        String key = getKey("");
        List<String> keys = new ArrayList<>();
        SettingsDocument doc = getDerby().getGroupProps(key);
        for (Prop prop : doc.getSettings().getPropList()) {
            if (!prop.isSetValue()) {
                continue;
            }
            final String[] parts = prop.getName().substring(key.length()).split("/");
            if (parts.length == 1) {
                keys.add(parts[0]);
            }
        }
        return keys;
    }

    @Override
    public void clear() {
        getDerby().clear();
    }

    @Override
    public String group() {
        if (groups.isEmpty()) {
            return "";
        } else {
            StringBuilder sb = new StringBuilder();
            boolean f = true;
            for (Group g : groups) {
                if (f) {
                    f = false;
                } else {
                    sb.append('/');
                }
                sb.append(g);
            }
            return sb.toString();
        }
    }

    @Override
    public void endGroup() {
        if (!groups.empty()) {
            groups.pop();
        }
    }

    @Override
    public void endAllGroups() {
        groups.clear();
    }

    @Override
    public boolean isWritable() {
        return true;
    }

    @Override
    public boolean isInvalid() {
        return false;
    }

    @Override
    public void sync() {
//        File appDir = new File(SystemTools.getApplicationDataPath("radixware.org"), "wps");
//        File settingsFile = new File(appDir, "settings");
    }

    @Override
    public void remove(String group) {
        getDerby().clearGroup(getKey(group));
    }
    private final Map<String, ITextOptions> textOptionsCache = new HashMap<>();

    @Override
    public WpsTextOptions readTextOptions(final String key) {
        final String cacheKey = getCaheKey(key);
        WpsTextOptions options = (WpsTextOptions) textOptionsCache.get(cacheKey);
        if (options == null || !readOptions(key).equals(options)) {
            options = readOptions(key);
            textOptionsCache.put(cacheKey, options);
        }
        return options;
    }

    @Override
    public void writeTextOptions(final String key, final ITextOptions options) {
        if (options.getForegroundColor() != null) {
            writeString(key + "/" + SettingNames.TextOptions.FCOLOR, TextOptions.color2Str(options.getForegroundColor()));
        } else {
            writeString(key + "/" + SettingNames.TextOptions.FCOLOR, null);
        }
        if (options.getBackgroundColor() != null) {
            writeString(key + "/" + SettingNames.TextOptions.BCOLOR, TextOptions.color2Str(options.getBackgroundColor()));
        } else {
            writeString(key + "/" + SettingNames.TextOptions.BCOLOR, null);
        }
        textOptionsCache.put(getCaheKey(key), options);
    }

    private WpsTextOptions readOptions(final String cfgGroups) {        
        final String bColorAsStr = getValue(cfgGroups + "/" + SettingNames.TextOptions.BCOLOR);
        final String fColorAsStr = getValue(cfgGroups + "/" + SettingNames.TextOptions.FCOLOR);
        Color bgrnd, frgrnd;
        bgrnd = bColorAsStr == null || bColorAsStr.isEmpty() ? null : Color.decode(bColorAsStr);
        frgrnd = fColorAsStr == null || fColorAsStr.isEmpty() ? null : Color.decode(fColorAsStr);
        final WpsTextOptions options = WpsTextOptions.Factory.getOptions(frgrnd, bgrnd);
        final String fontAsStr = getValue(cfgGroups + "/" + FONT_WEIGTH_KEY);
        if (fontAsStr==null || fontAsStr.isEmpty()){
            return options;
        }else{
            final EFontWeight fontWeigth = EFontWeight.forCssValue(fontAsStr);
            return fontWeigth==EFontWeight.NORMAL ? options : options.changeFontWeight(fontWeigth);
        }
    }
    
    private void traceWarning(final Class<?> valClass, final String key, final Exception ex) {
        final MessageProvider mp = env.getMessageProvider();
        final String warningMess = mp.translate("ExplorerSettings", "Error occurred during reading %s value from \'%s\':\n %s");
        env.getTracer().put(EEventSeverity.WARNING, String.format(warningMess, valClass.getSimpleName(), key, ClientException.getExceptionReason(mp, ex)), EEventSource.CLIENT);
    }    

    private String getCaheKey(final String key) {
        return normalizeKey(group() != null && !group().isEmpty() ? group() + "/" + key : key);
    }

    private static String normalizeKey(final String key) {
        String result = key.replace('\\', '/');
        while (result.contains("//")) {
            result = result.replaceFirst("//", "/");
        }
        return result;
    }

    @Override
    public String getDescription() {
        return "WPS Config Store";
    }
}
