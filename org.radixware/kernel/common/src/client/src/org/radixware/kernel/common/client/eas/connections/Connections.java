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

package org.radixware.kernel.common.client.eas.connections;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

import org.apache.xmlbeans.XmlException;

import org.radixware.schemas.connections.ConnectionsDocument;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.ClientSettings;
import org.radixware.kernel.common.client.exceptions.FileException;
import org.radixware.kernel.common.client.types.ExplorerRoot;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.kernel.common.utils.XmlUtils;
import org.radixware.schemas.connections.Users2ConnectionsMap;
import org.radixware.schemas.connections.Users2ConnectionsMap.Link;

public abstract class Connections implements Iterable<ConnectionOptions> {

    final public static String SETTING_NAME = "connections";
    final private static String CONNECTIONS_FILE_NAME = "connections.xml";
    //список общих соединений по имени:
    private final Map<String, ConnectionOptions> commonConnectionsByName = new HashMap<>();
    private String connectionsXml;
    private final String localConnectionsFilePath;
    //слитый список  соединений
    private final ArrayList<ConnectionOptions> connections = new ArrayList<>();
    private final IClientEnvironment environment;

    protected Map<String, String> user2connection;
    protected String defaultConnectionName;

    protected abstract ConnectionOptions createConnection(IClientEnvironment env, ConnectionsDocument.Connections.Connection xmlConnection, boolean isLocal);

    protected abstract ConnectionOptions createConnection(IClientEnvironment env, String connectionName);

    protected abstract ConnectionOptions createConnection(final IClientEnvironment environment, final ConnectionOptions source, final String connectionName);

    public IClientEnvironment getEnvironment() {
        return environment;
    }

    public Connections(IClientEnvironment environment, final String connectionsFileName) {
        this.environment = environment;
        if (connectionsFileName != null) {
            localConnectionsFilePath = connectionsFileName;
            loadConnections();
        } else {
            localConnectionsFilePath = null;
        }
    }

    public Connections(IClientEnvironment environment, final List<ConnectionOptions> predefied) {
        this.environment = environment;
        localConnectionsFilePath = null;
        if (predefied != null) {
            for (ConnectionOptions options : predefied) {
                connections.add(createConnection(environment, options, options.getName()));
            }
        }
    }

    private void loadConnections() {
        final File connectionsFile = localConnectionsFilePath == null ? null : new File(localConnectionsFilePath);
        if (connectionsFile!=null && connectionsFile.exists()
                && connectionsFile.isFile()
                && connectionsFile.canRead()) {
            ConnectionsDocument connectionsDocument = null;
            try {
                connectionsXml = FileUtils.readTextFile(connectionsFile, FileUtils.XML_ENCODING);
                if (connectionsXml!=null && !connectionsXml.isEmpty()){
                    connectionsDocument = ConnectionsDocument.Factory.parse(connectionsXml);
                }
            } catch (IOException | XmlException ex) {
                final FileException exception = 
                    new FileException(getEnvironment(), FileException.EExceptionCode.CANT_READ, connectionsFile.getAbsolutePath(), ex);
                final String msgTitle = getEnvironment().getMessageProvider().translate("ConnectionsDialog", "Failed to Load Connection Options");
                getEnvironment().processException(msgTitle,exception);
            }

            if (connectionsDocument != null && connectionsDocument.getConnections() != null) {
                final List<ConnectionsDocument.Connections.Connection> connectionsList = 
                    connectionsDocument.getConnections().getConnectionList();
                for (ConnectionsDocument.Connections.Connection connection : connectionsList) {
                    connections.add(createConnection(environment, connection, true));
                }
                if (connectionsDocument.getConnections().getUsers2Connection() != null) {
                    Users2ConnectionsMap xMap = connectionsDocument.getConnections().getUsers2Connection();
                    if (xMap.getDefaultConnection() != null) {
                        defaultConnectionName = xMap.getDefaultConnection();
                    }
                    user2connection = new HashMap<>();
                    for (Users2ConnectionsMap.Link xLink : xMap.getLinkList()) {
                        final String connectionName = xLink.getConnectionName();
                        if (connectionName!=null && !connectionName.isEmpty()){
                            if (xLink.getUsers()!=null){
                                final List<String> users = xLink.getUsers().getUserList();
                                if (users!=null && !users.isEmpty()){
                                    for (String user: users){
                                        user2connection.put(user, connectionName);
                                    }
                                }
                            }
                            if (xLink.getUser()!=null){
                                user2connection.put(xLink.getUser(), connectionName);
                            }
                        }
                    }
                }
            }
        }
    }

    public Connections(IClientEnvironment environment, final File workPath) {
        this.environment = environment;
        if (workPath != null) {
            //Прочитать локальные настройки соединений
            localConnectionsFilePath = new File(workPath, CONNECTIONS_FILE_NAME).getAbsolutePath();
            loadConnections();
        } else {
            localConnectionsFilePath = null;
        }
        /*
         //Прочитать общие настройки соединений
         ConnectionsDocument commonConnectionsDoc = null;        
         if (commonConnectionsDoc != null && commonConnectionsDoc.getConnections() != null) {
         final List<ConnectionsDocument.Connections.Connection> connectionsList = commonConnectionsDoc.getConnections().getConnectionList();
         ConnectionOptions commonConnection;
         for (ConnectionsDocument.Connections.Connection connection : connectionsList) {
         commonConnection = createConnection(environment, connection, false);
         //Пропускаем уже прочитанные соединения
         if (findByName(connection.getName()) == null) {
         connections.add(commonConnection);
         }
         commonConnectionsByName.put(commonConnection.getName(), commonConnection);
         }
         if (commonConnectionsDoc.getConnections().getUsers2Connection() != null) {
         Users2ConnectionsMap xMap = commonConnectionsDoc.getConnections().getUsers2Connection();
         if (xMap.getDefaultConnection() != null) {
         defaultConnectionName = xMap.getDefaultConnection();
         }
         if (user2connection == null) {
         user2connection = new HashMap<>();

         for (Users2ConnectionsMap.Link xLink : xMap.getLinkList()) {
         user2connection.put(xLink.getUser(), xLink.getConnectionName());
         }
         }
         }
         }*/
    }

    public ConnectionOptions create() {
        final ConnectionOptions newConnection = createConnection(environment, calcNewConnectionName());
        final List<String> connectionNames = new ArrayList<>(connections.size());
        for (ConnectionOptions connection : connections) {
            connectionNames.add(connection.getName());
        }
        if (newConnection.edit(connectionNames)) {
            connections.add(newConnection);
            return newConnection;
        }else{
            newConnection.deleteConfigFiles(true);
            return null;
        }
    }

    public ConnectionOptions createCopy(ConnectionOptions source) {
        final String copyName = calcCopyConnectionName(source.getName());
        return createConnection(environment, source, copyName);
    }

    public void add(final ConnectionOptions connection) {
        if (connection == null) {
            throw new NullPointerException();
        }
        if (findByName(connection.getName()) != null) {
            throw new IllegalArgumentException("connection with name \"" + connection.getName() + "\" already exists");
        }
        connections.add(connection);
    }

    public void override(final ConnectionOptions connection) {
        if (connection == null) {
            throw new NullPointerException();
        }
        for (int i = connections.size() - 1; i >= 0; i--) {
            if (connection.getName().equals(connections.get(i).getName())) {
                if (connections.get(i).isLocal()) {
                    throw new IllegalArgumentException("connection with name \"" + connection.getName() + "\" already exists");
                }
                connections.set(i, connection);
                return;
            }
        }
        throw new IllegalArgumentException("connection with name \"" + connection.getName() + "\" does not exists");
    }

    private void storeSettings() {
        final ClientSettings settings = environment.getConfigStore();
        settings.beginWriteArray(Connections.SETTING_NAME);
        for (int i = 0; i < connections.size(); ++i) {
            final ConnectionOptions curConnection = connections.get(i);
            settings.setArrayIndex(i);
            settings.setValue("name", curConnection.getName());
            ExplorerRoot.storeToConfig(settings, curConnection.getExplorerRoots());
        }
        settings.endArray();

    }

    public void store() {
        if (localConnectionsFilePath != null) {
            ConnectionsDocument commonConnectionsDoc = ConnectionsDocument.Factory.newInstance();
            ConnectionsDocument.Connections xmlConnections = commonConnectionsDoc.addNewConnections();
            for (ConnectionOptions connection : connections) {
                connection.writeToXml(xmlConnections.addNewConnection());
            }
            
            if (user2connection != null && !user2connection.isEmpty()) {
                Users2ConnectionsMap users2ConnectionMap = xmlConnections.addNewUsers2Connection();
                if (defaultConnectionName != null && !defaultConnectionName.isEmpty()) {
                    users2ConnectionMap.setDefaultConnection(defaultConnectionName);
                }
                for (Entry<String, String> user2ConnectionEntry : user2connection.entrySet()) {
                    Link link = null;
                    for (Link users2ConnectionLink : users2ConnectionMap.getLinkList()) {
                        if (user2ConnectionEntry.getValue().equals(users2ConnectionLink.getConnectionName())) {
                            link = users2ConnectionLink;
                            break;
                        } 
                    }
                    if (link == null) {
                        link = users2ConnectionMap.addNewLink();
                        link.setConnectionName(user2ConnectionEntry.getValue());
                        link.addNewUsers();
                    }
                    
                    List<String> userList = link.getUsers().getUserList();
                    userList.add(user2ConnectionEntry.getKey());
                }
            }
            final String currentConntectionsXml = commonConnectionsDoc.xmlText(XmlUtils.getPrettyXmlOptions());
            if (!Objects.equals(connectionsXml, currentConntectionsXml)){                
                final File localConnectionsFile = new File(localConnectionsFilePath);
                try {
                    FileUtils.writeString(localConnectionsFile, currentConntectionsXml, FileUtils.XML_ENCODING);
                    connectionsXml = currentConntectionsXml;
                } catch (IOException e) {
                    getEnvironment().processException(new FileException(getEnvironment(), FileException.EExceptionCode.CANT_WRITE, localConnectionsFile.getAbsolutePath()));
                }
            }
        }
        storeSettings();
    }

    public boolean remove(String name) {
        for (int i = connections.size() - 1; i >= 0; i--) {
            if (connections.get(i).getName().equals(name)) {
                if (!connections.get(i).isLocal()) {
                    return false;
                } else if (commonConnectionsByName.containsKey(name)) {
                    connections.set(i, commonConnectionsByName.get(name));
                    return false;
                } else {
                    final ConnectionOptions options = connections.get(i);
                    if (!options.deleteConfigFiles(false)){
                        return false;
                    }
                    connections.remove(i);
                }
                return true;
            }
        }
        return false;
    }

    public final ConnectionOptions findByName(String name) {
        for (ConnectionOptions connection : connections) {
            if (connection.getName().equals(name)) {
                return connection;
            }
        }
        return null;
    }

    @Override
    public Iterator<ConnectionOptions> iterator() {
        return connections.iterator();
    }

    public int size() {
        return connections.size();
    }

    public String calcCopyConnectionName(final String connectionName) {
        String copyName = String.format(environment.getMessageProvider().translate("ConnectionsDialog", "%s - copy"), connectionName);
        for (int i = 1; findByName(copyName) != null; i++) {
            copyName = String.format("%s (%s)", copyName, String.valueOf(i));
        }
        return copyName;
    }

    private String calcNewConnectionName() {
        String connectionName = environment.getMessageProvider().translate("ConnectionEditor", "New Connection");
        for (int i = 1; findByName(connectionName) != null; i++) {
            connectionName = String.format("%s (%s)", connectionName, String.valueOf(i));
        }
        return connectionName;
    }
}
