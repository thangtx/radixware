/*
 * Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * This Source Code is distributed WITHOUT ANY WARRANTY; including any 
 * implied warranties but not limited to warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
 * License, v. 2.0. for more details.
 */
package org.radixware.kernel.server.units.mq;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import org.radixware.kernel.common.enums.EMqKind;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.server.units.mq.interfaces.IMqUnitDbQueries;
import org.radixware.kernel.server.units.persocomm.interfaces.IDatabaseConnectionAccess;
import org.radixware.kernel.server.units.persocomm.tools.BasicDbQuery;
import org.radixware.kernel.server.utils.OptionsGroup;

public class MqDbQueries extends BasicDbQuery implements IMqUnitDbQueries {

    private static final int SQL_OPTIONS_READ_OPTIONS = 1;
    private static final int SQL_OPTIONS_READ_MASTER = 2;

    private static final Stmt[] SQL_LIST = {
        new Stmt(SQL_OPTIONS_READ_OPTIONS, "select p.unitId, p.id as processorId, p.title as processorTitle, p.parallelThreads, p.queueId, p.aasCallTimeoutSec, nvl(u.scpName, (select i.scpName from rdx_instance i where i.id = u.instanceId)) as scpName, q.queueKind, q.title as queueTitle, q.brokerAddress, q.queueName, q.partitionName, q.login, q.password, q.consumerKey, kafkaq.sessionTimeoutSec as kafkaSessionTimeoutSec from RDX_MESSAGEQUEUE q left join RDX_KAFKAQUEUE kafkaq on q.id = kafkaq.queueId, RDX_MESSAGEQUEUEPROCESSOR p, RDX_UNIT u where q.id = p.queueId and p.unitId = u.id and p.unitId = ?"),
        new Stmt(SQL_OPTIONS_READ_MASTER, "select mainUnitId from RDX_FALLBACKMQHANDLER where unitId = ?")
    };

    static {
        Arrays.sort(SQL_LIST);
    }

    public MqDbQueries(IDatabaseConnectionAccess access) {
        super(access, SQL_LIST);
    }

    @Override
    public MqUnitOptions[] readOptions(long unitId) throws SQLException {
        final List<MqUnitOptions> result = new ArrayList<>();

        try (final IDbCursor<MqUnitOptions> query = query(MqUnitOptions.class, SQL_OPTIONS_READ_OPTIONS, unitId)) {
            for (MqUnitOptions item : query.list(MqUnitOptions.class)) {
                result.add(item);
            }
        } catch (Exception exc) {
            throw new IllegalUsageError("Error reading options for module #" + String.valueOf(unitId) + ": " + ExceptionTextFormatter.throwableToString(exc));
        }
        return result.toArray(new MqUnitOptions[result.size()]);
    }

    @Override
    public long defineMastertModule(long moduleId) throws SQLException {
        try (final IDbCursor<ModuleId> query = query(ModuleId.class, SQL_OPTIONS_READ_MASTER, moduleId)) {
            if (!query.isEmpty()) {
                for (ModuleId item : query.list(ModuleId.class)) {
                    return item.mainUnitId;
                }
                return moduleId;
            }
            else {
                return moduleId;
            }
        } catch (Exception exc) {
            throw new IllegalUsageError("Error reading main module for module #" + String.valueOf(moduleId) + ": " + ExceptionTextFormatter.throwableToString(exc));
        }
    }

    public static class MqUnitOptions {
        public long unitId;
        public String scpName;
        public long sapId;
        public long processorId;
        public String processorTitle;
        public int parallelThreads;
        public long queueId;
        public EMqKind queueKind;
        public String queueTitle;
        public String brokerAddress;
        public String queueName;
        public String partitionName;
        public String consumerKey;
        public String login;
        public String password;
        public int aasCallTimeoutSec;
        public long kafkaSessionTimeoutSec;

        public OptionsGroup toOptionsGroup() {
            OptionsGroup og = new OptionsGroup();
            og.add("Processor", processorId + ") " + processorTitle);
            og.add("Queue kind", queueKind.getValue());
            og.add("Queue", queueId + ") " + queueTitle);
            og.add("Broker address", brokerAddress);
            og.add("Queue name", queueName);
            if (consumerKey != null) {
                og.add("Consumer key", consumerKey);
            }
            if (login != null) {
                og.add("Login", login);
            }
            if (queueKind == EMqKind.KAFKA) {
                og.add("Kafka session timeout (sec)", kafkaSessionTimeoutSec);
            }
            og.add("Parallel processing threads", parallelThreads);
            og.add("AAS call timeout", aasCallTimeoutSec);
            og.add("SCP name", scpName);
            return og;
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 71 * hash + (int) (this.unitId ^ (this.unitId >>> 32));
            hash = 71 * hash + Objects.hashCode(this.scpName);
            hash = 71 * hash + (int) (this.sapId ^ (this.sapId >>> 32));
            hash = 71 * hash + (int) (this.processorId ^ (this.processorId >>> 32));
            hash = 71 * hash + this.parallelThreads;
            hash = 71 * hash + (int) (this.queueId ^ (this.queueId >>> 32));
            hash = 71 * hash + Objects.hashCode(this.queueKind);
            hash = 71 * hash + Objects.hashCode(this.brokerAddress);
            hash = 71 * hash + Objects.hashCode(this.queueName);
            hash = 71 * hash + Objects.hashCode(this.partitionName);
            hash = 71 * hash + Objects.hashCode(this.consumerKey);
            hash = 71 * hash + Objects.hashCode(this.login);
            hash = 71 * hash + Objects.hashCode(this.password);
            hash = 71 * hash + this.aasCallTimeoutSec;
            hash = 71 * hash + (int) (this.kafkaSessionTimeoutSec ^ (this.kafkaSessionTimeoutSec >>> 32));
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final MqUnitOptions other = (MqUnitOptions) obj;
            if (this.unitId != other.unitId) {
                return false;
            }
            if (!Objects.equals(this.scpName, other.scpName)) {
                return false;
            }
            if (this.sapId != other.sapId) {
                return false;
            }
            if (this.processorId != other.processorId) {
                return false;
            }
            if (this.parallelThreads != other.parallelThreads) {
                return false;
            }
            if (this.queueId != other.queueId) {
                return false;
            }
            if (this.queueKind != other.queueKind) {
                return false;
            }
            if (!Objects.equals(this.brokerAddress, other.brokerAddress)) {
                return false;
            }
            if (!Objects.equals(this.queueName, other.queueName)) {
                return false;
            }
            if (!Objects.equals(this.partitionName, other.partitionName)) {
                return false;
            }
            if (!Objects.equals(this.consumerKey, other.consumerKey)) {
                return false;
            }
            if (!Objects.equals(this.login, other.login)) {
                return false;
            }
            if (!Objects.equals(this.password, other.password)) {
                return false;
            }
            if (this.aasCallTimeoutSec != other.aasCallTimeoutSec) {
                return false;
            }
            if (this.kafkaSessionTimeoutSec != other.kafkaSessionTimeoutSec) {
                return false;
            }
            return true;
        }
        
        

        @Override
        public String toString() {
            return toOptionsGroup().toString();
        }
    }
    
    public static class ModuleId {
        public long mainUnitId;
    }
}
