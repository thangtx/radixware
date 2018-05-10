/*
 * Copyright (c) 2008-2017, Compass Plus Limited. All rights reserved.
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

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.apache.commons.lang.StringUtils;
import org.radixware.kernel.common.enums.EMqKind;
import org.radixware.kernel.common.enums.EMqProcOrder;
import org.radixware.kernel.common.enums.EPortSecurityProtocol;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.server.units.mq.interfaces.IMqUnitDbQueries;
import org.radixware.kernel.server.jdbc.IDatabaseConnectionAccess;
import org.radixware.kernel.server.jdbc.ExtendedDbQueries;
import org.radixware.kernel.server.jdbc.Stmt;
import org.radixware.kernel.server.utils.OptionsGroup;

public class MqDbQueries extends ExtendedDbQueries implements IMqUnitDbQueries {

    private static final int SQL_OPTIONS_READ_OPTIONS = 1;
    private static final int SQL_OPTIONS_READ_MASTER = 2;

    private static final Stmt[] SQL_LIST = {
        new Stmt(SQL_OPTIONS_READ_OPTIONS, "select p.unitId, p.id as processorId, "
                + "p.title as processorTitle, p.parallelThreads, p.queueId, p.aasCallTimeoutSec, "
                + "nvl(u.scpName, (select i.scpName from rdx_instance i where i.id = u.instanceId)) as scpName, "
                + "q.queueKind, q.title as queueTitle, q.brokerAddress, q.queueName, q.partitionName, "
                + "q.login, q.password, q.consumerKey, q.procOrder, q.partitionSource, q.prefetchCount, "
                + "q.securityProtocol, q.serverCertAliases, q.clientKeyAliases, q.cipherSuites, "
                + "q.jndiInitialContextFactory, q.jndiProviderUrl, q.jndiQueueName, q.jndiOptions, "
                + "kafkaq.sessionTimeoutSec as kafkaSessionTimeoutSec, "
                + "kafkaq.brokerKerberosName as kafkaBrokerKerberosName, kafkaq.maxPartitionFetchBytes, "
                + "amqpq.virtualHost, "
                + "jmsq.jndiConnFactoryName as jmsJndiConnFactoryName, jmsq.connFactoryClassName as jmsConnFactoryClassName, "
                + "jmsq.connFactoryOptions as jmsConnFactoryOptions, jmsq.subscriptionName as jmsSubscriptionName, "
                + "jmsq.clientId as jmsClientId "
                + "from RDX_MESSAGEQUEUE q left join RDX_KAFKAQUEUE kafkaq on q.id = kafkaq.queueId "
                + "left join RDX_AMQPQUEUE amqpq on q.id=amqpq.queueId "
                + "left join RDX_JMSQUEUE jmsq on q.id = jmsq.queueId, "
                + "RDX_MESSAGEQUEUEPROCESSOR p, RDX_UNIT u where q.id = p.queueId and p.unitId = u.id and p.unitId = ?", Types.BIGINT),
        new Stmt(SQL_OPTIONS_READ_MASTER, "select mainUnitId from RDX_FALLBACKMQHANDLER where unitId = ?", Types.BIGINT)
    };

    static {
        Arrays.sort(SQL_LIST);
    }

    public MqDbQueries(Connection conn) {
        super(conn, SQL_LIST);
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
            } else {
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
        public String kafkaBrokerKerberosName;
        public long maxPartitionFetchBytes = 1 << 20;
        public String partitionSource;
        public EMqProcOrder procOrder;
        public int prefetchCount;
        public String virtualHost;
        // TLS
        public EPortSecurityProtocol securityProtocol;
        public List<String> serverCertAliases;
        public List<String> clientKeyAliases;
        public List<String> cipherSuites;
        // JNDI
        public String jndiInitialContextFactory;
        public String jndiProviderUrl;
        public String jndiQueueName;
        public Map<String, String> jndiOptions;
        // JMS
        public String jmsJndiConnFactoryName;
        public String jmsConnFactoryClassName;
        public Map<String, String> jmsConnFactoryOptions;
        public String jmsSubscriptionName;
        public String jmsClientId;
        //

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
            if (procOrder != null) {
                og.add("Processing order", procOrder);
            }
            if (partitionSource != null) {
                og.add("Partition source", partitionSource);
            }
            if (login != null) {
                og.add("Login", login);
            }
            if (queueKind == EMqKind.KAFKA) {
                og.add("Kafka session timeout (sec)", kafkaSessionTimeoutSec);
                og.add("Kafka maximum partition fetch bytes", maxPartitionFetchBytes);
                if (kafkaBrokerKerberosName != null) {
                    og.add("Kerberos pricipal name of the Kafka broker", kafkaBrokerKerberosName);
                    final String jaasFilePropName = "java.security.auth.login.config";
                    og.add("JAAS config file ('" + jaasFilePropName + "' system property value)", System.getProperty(jaasFilePropName));
                    final String krbFilePropName = "java.security.krb5.conf";
                    og.add("Kerberos config file ('" + krbFilePropName + "' system property value)", System.getProperty(krbFilePropName));
                }
                if (securityProtocol.isTls()) {
                    og.add("Security protocol", securityProtocol.getName());
                    og.add("TLS server certificates",  serverCertAliases == null ? "any" : StringUtils.join(serverCertAliases, ";"));
                    og.add("TLS client keys", clientKeyAliases == null ? "any" : StringUtils.join(clientKeyAliases, ";"));
                    og.add("TLS cipher suites", cipherSuites == null ? "strong only" : (cipherSuites.isEmpty() ? "any" : StringUtils.join(cipherSuites, ";")));
                }
            }
            if (queueKind == EMqKind.AMQP) {
                og.add("Virtual host", virtualHost);
            }
            if (queueKind == EMqKind.JMS) {
                og.add("JMS JNDI initial context factory", jndiInitialContextFactory);
                og.add("JMS JNDI provider URL", jndiProviderUrl);
                og.add("JMS JNDI connection factory lookup name", jmsJndiConnFactoryName);
                og.add("JMS JNDI topic lookup name", jndiQueueName);
                if (jndiOptions != null) {
                    og.add("JMS custom JNDI options", StringUtils.join(jndiOptions.entrySet(), ";"));
                }
                
                og.add("JMS connection factory class name", jmsConnFactoryClassName);
                if (jmsConnFactoryOptions != null) {
                    og.add("JMS custom connection factory options", StringUtils.join(jmsConnFactoryOptions.entrySet(), ";"));
                }
                og.add("JMS subscription name", jmsSubscriptionName);
                og.add("JMS client ID", jmsClientId);
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
