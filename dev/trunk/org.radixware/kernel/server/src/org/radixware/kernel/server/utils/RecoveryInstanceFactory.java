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

package org.radixware.kernel.server.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class RecoveryInstanceFactory {

    public static int create(
            final Connection connection,
            final String instanceSapAddress,
            final String easSapAddress,
            final String scpName) throws InstanceCreationError {
        final RecoveryInstanceCreator creator = new RecoveryInstanceCreator(connection, instanceSapAddress, easSapAddress, scpName);
        return (int) creator.create();
    }

    private static class RecoveryInstanceCreator {

        private static final String EAS_SERVICE_URI = "http://schemas.radixware.org/eas.wsdl";
        private static final String INSTANCE_CONTROL_WSDL_URI = "http://schemas.radixware.org/systeminstancecontrol.wsdl";
        private static final long ARTE_UNIT_TYPE = 3001;
        private final Connection connection;
        private final String instanceSapAddress;
        private final String easSapAddress;
        private final String scpName;

        public RecoveryInstanceCreator(
                final Connection connection,
                final String instanceSapAddress,
                final String easSapAddress,
                final String scpName) {
            this.connection = connection;
            this.instanceSapAddress = instanceSapAddress;
            this.easSapAddress = easSapAddress;
            this.scpName = scpName;
        }

        public long create() throws InstanceCreationError {
            try {
                try {
                    final long instanceId = doCreate();
                    connection.commit();
                    return instanceId;
                } catch (Exception ex) {
                    connection.rollback();
                    throw new InstanceCreationError("Error while creating new instance", ex);
                }
            } catch (SQLException ex) {
                throw new InstanceCreationError("Error while creating new instance", ex);
            }
        }

        private long doCreate() throws InstanceCreationError {
            try {
                //get id for instance
                final long instanceId = getNewInstanceId();
                //generate URI for instance sap
                final String instanceSapServiceUri = createInstanceSapUri(instanceId);
                //get id for instance control sap
                final long instanceSapId = getNewSapId();
                //create instance control sap
                createSap(instanceSapId, instanceSapAddress, instanceSapServiceUri, instanceId);
                //create instance
                createInstance(instanceId, instanceSapId);
                //get id for eas sap
                long easSapId = getNewSapId();
                //create eas sap
                createSap(easSapId, easSapAddress, EAS_SERVICE_URI, instanceId);
                //get id for eas
                long easId = getNewEasId();
                //create eas
                createEas(easId, easSapId, instanceId);
                if (scpName != null && !scpName.isEmpty()) {
                    //link specified scp with created saps
                    associateSapAndScp(instanceSapId, scpName);
                    associateSapAndScp(easSapId, scpName);
                }
                return instanceId;
            } catch (SQLException ex) {
                throw new InstanceCreationError("Error while creating recovery instance", ex);
            }
        }

        private String createInstanceSapUri(final long instanceId) throws InstanceCreationError {
            final String serviceUri = INSTANCE_CONTROL_WSDL_URI + "#" + instanceId;
            try {
                PreparedStatement createServiceStatement = null;
                try {
                    createServiceStatement = connection.prepareStatement("insert into RDX_SERVICE (systemId, uri, wsdlUri, title) values (?, ?, ?, ?)");
                    createServiceStatement.setLong(1, 1);
                    createServiceStatement.setString(2, serviceUri);
                    createServiceStatement.setString(3, INSTANCE_CONTROL_WSDL_URI);
                    createServiceStatement.setString(4, "Recovery instance #" + instanceId + " control service");
                    createServiceStatement.execute();

                    return serviceUri;
                } finally {
                    if (createServiceStatement != null) {
                        createServiceStatement.close();
                    }
                }
            } catch (SQLException ex) {
                throw new InstanceCreationError("Can't create service uri for instance control sap", ex);
            }
        }

        private long getNewInstanceId() throws SQLException, InstanceCreationError {
            //SQN_RDX_INSTANCE_ID - sequence for INSTANCE IDs
            return getNewId("SQN_RDX_INSTANCEID");
        }

        private long getNewEasId() throws SQLException, InstanceCreationError {
            //SQN_RDX_UNIT_ID - sequence for UNIT IDs
            return getNewId("SQN_RDX_UNITID");
        }

        private long getNewSapId() throws SQLException, InstanceCreationError {
            //SQN_RDX_SAP_ID - sequence for SAP IDs
            return getNewId("SQN_RDX_SAP_ID");
        }

        private long getNewId(final String sequenceName) throws SQLException, InstanceCreationError {
            PreparedStatement createIdStatement = null;
            try {
                createIdStatement = connection.prepareStatement("select " + sequenceName + ".NEXTVAL from dual");
                final ResultSet rs = createIdStatement.executeQuery();
                try {
                    if (rs.next()) {
                        return rs.getLong(1);
                    } else {
                        throw new InstanceCreationError("Cant' get next value from sequence " + sequenceName);
                    }
                } finally {
                    rs.close();
                }
            } finally {
                if (createIdStatement != null) {
                    createIdStatement.close();
                }
            }
        }

        private long createSap(final long id, final String sapAddress, final String uri, final long instanceId) throws InstanceCreationError {
            try {
                final String name = "Recovery SAP #" + id;
                PreparedStatement createSapStatement = null;
                try {
                    createSapStatement = connection.prepareStatement("insert into RDX_SAP (id, name, address, uri, accessibility) values (?, ?, ?, ?, ?)");
                    createSapStatement.setLong(1, id);
                    createSapStatement.setString(2, name);
                    createSapStatement.setString(3, sapAddress);
                    createSapStatement.setString(4, uri);
                    createSapStatement.setString(5, "B");
                    createSapStatement.execute();
                    return id;
                } finally {
                    if (createSapStatement != null) {
                        createSapStatement.close();
                    }
                }
            } catch (SQLException ex) {
                throw new InstanceCreationError("Error  while creating SAP with address " + sapAddress, ex);
            }
        }

        private void createInstance(final long instanceId, final long sapId) throws InstanceCreationError {
            try {
                PreparedStatement createInstanceStatement = null;
                try {
                    createInstanceStatement = connection.prepareStatement("insert into RDX_INSTANCE (id, title, sapId) values (?, ?, ?)");
                    final String title = "Recovery instance #" + instanceId;
                    createInstanceStatement.setLong(1, instanceId);
                    createInstanceStatement.setString(2, title);
                    createInstanceStatement.setLong(3, sapId);
                    createInstanceStatement.execute();
                } finally {
                    if (createInstanceStatement != null) {
                        createInstanceStatement.close();
                    }
                }
            } catch (SQLException ex) {
                throw new InstanceCreationError("Error  while creating recovery instance ", ex);
            }
        }

        private void createEas(final long unitId, final long sapId, final long instanceId) throws InstanceCreationError {
            //aclFKCYABVIK7OBDCJAAALOMT5GDM - arte unit class id
            try {
                PreparedStatement createUnitStatement = null;
                try {
                    createUnitStatement = connection.prepareStatement("insert into RDX_UNIT (id, instanceId, type, classGuid, title) values (?, ?, ?, ?, ?)");
                    createUnitStatement.setLong(1, unitId);
                    createUnitStatement.setLong(2, instanceId);
                    createUnitStatement.setLong(3, ARTE_UNIT_TYPE);
                    createUnitStatement.setString(4, "aclFKCYABVIK7OBDCJAAALOMT5GDM");
                    createUnitStatement.setString(5, "EAS for Recovery Instance #" + instanceId);
                    createUnitStatement.execute();
                    PreparedStatement createArteDetailsStatement = null;
                    try {
                        createArteDetailsStatement = connection.prepareStatement("insert into RDX_ARTEUNIT (id, sapId, serviceUri, highArteInstCount) values (?, ?, ?, ?)");
                        createArteDetailsStatement.setLong(1, unitId);
                        createArteDetailsStatement.setLong(2, sapId);
                        createArteDetailsStatement.setString(3, EAS_SERVICE_URI);
                        createArteDetailsStatement.setLong(4, 10);
                        createArteDetailsStatement.execute();
                    } finally {
                        createArteDetailsStatement.close();
                    }
                } finally {
                    createUnitStatement.close();
                }
            } catch (SQLException ex) {
                throw new InstanceCreationError("Can't get new id for Unit", ex);
            }
        }

        private void associateSapAndScp(final long sapId, final String scpName) throws InstanceCreationError {
            try {
                PreparedStatement associateStatement = null;
                try {
                    associateStatement = connection.prepareStatement("insert into RDX_SCP2SAP (systemId, sapId, scpName) values (1, ?, ?)");
                    associateStatement.setLong(1, sapId);
                    associateStatement.setString(2, scpName);
                    associateStatement.execute();
                } finally {
                    if (associateStatement != null) {
                        associateStatement.close();
                    }
                }
            } catch (SQLException ex) {
                throw new InstanceCreationError("Error  while associating sap '" + sapId + "' and scp '" + scpName + "'", ex);
            }
        }
    }

    public static class InstanceCreationError extends Exception {

        public InstanceCreationError(final Throwable cause) {
            super(cause);
        }

        public InstanceCreationError(final String message, final Throwable cause) {
            super(message, cause);
        }

        public InstanceCreationError(final String message) {
            super(message);
        }

        public InstanceCreationError() {
        }
    }
}
