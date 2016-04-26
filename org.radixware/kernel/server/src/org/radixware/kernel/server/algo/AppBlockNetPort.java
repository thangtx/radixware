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

package org.radixware.kernel.server.algo;

import java.math.BigDecimal;
import org.radixware.kernel.common.exceptions.AppError;
import org.radixware.kernel.common.exceptions.AppException;
import org.radixware.kernel.common.exceptions.ServiceCallException;
import org.radixware.kernel.common.exceptions.ServiceCallFault;
import org.radixware.kernel.common.exceptions.ServiceCallTimeout;
import org.radixware.kernel.common.types.Bin;
import org.radixware.kernel.server.arte.Arte;
import org.radixware.kernel.common.msdl.XmlObjectMessagingInterface;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.server.types.Algorithm;
import org.radixware.kernel.server.types.Entity;
import org.radixware.schemas.msdl.Structure;
import org.radixware.schemas.netporthandler.ProcessRq;
import org.radixware.schemas.netporthandlerWsdl.ProcessDocument;

public class AppBlockNetPort {

	public static final class PortData {
		// In
		public byte[] recvPacket;
		public boolean connected;
		public boolean inProcessed = false;
		// Out
		public byte[] sendPacket;
		public boolean toClose;
		public long waitId;
		public boolean outFilled = false;
	}
	
	private static final int EXIT_DISCONNECT = 0;
	private static final int EXIT_RECV = 1;
	private static final int EXIT_NONE = 2;
	private static final int EXIT_TIMEOUT = 3;

	private static final int NETPORT_SERVICE_TIMEOUT = 60;
        private static final int NETPORT_SERVICE_KEEP_CONNECTIONTIME = 10;

	// return 0 - wait
	static public int invoke(final Algorithm algo) throws Exception {		
		final Arte arte = algo.getArte();
                AlgorithmExecutor executor = algo.getExecutor();

		BigDecimal timeout = (BigDecimal)algo.getProperty("recvTimeout");
		Id strobId = Id.Factory.loadFrom((String)algo.getProperty("strobId"));
		
		// check sync mode
		if (executor.syncExecution)
			throw new AppError("Network port block can not be used in synchronous mode");
		
		Entity process = algo.getProcess();
		Boolean toSend = (Boolean)algo.getProperty("toSend");

		// clear receive message
		algo.setProperty("recvMess", null);
		
		// check port data
		PortData data = null;
		if (executor.getCurrentThread().clientData instanceof PortData)
			data = (PortData)executor.getCurrentThread().clientData;
		
		if (data != null && toSend.booleanValue()) {
			// send packet
			Object sendMess = algo.getProperty("sendMess");
			if (sendMess != null) {
				try {
					if (sendMess instanceof Bin)
						data.sendPacket = ((Bin)sendMess).get();
					else {
						Structure sendParam = (Structure)algo.getProperty("smioSendParameters");
						if (sendParam != null)
							data.sendPacket = ((XmlObjectMessagingInterface)sendMess).writeStructuredMessageParam(sendParam);
						else
							data.sendPacket = ((XmlObjectMessagingInterface)sendMess).writeStructuredMessage();
					}
				} catch (Exception e) {
					throw new AppException("Network port block SMIO exception: " + e.getMessage(), e);
				}
			}
			else
				data.sendPacket = null;

			// close connection flag
			Boolean cl = (Boolean)algo.getProperty("toClose");
			if (cl.booleanValue())
				data.toClose = cl.booleanValue();
			else
				data.toClose = false;

			// sync onEven			
			data.waitId = algo.scheduleEvent(null).longValue();
			data.outFilled = true;
		} else {
			// net port handler
			
			// check unit id
			Id unitParamId = Id.Factory.loadFrom((String)algo.getProperty("portUnitIdParamId"));
			if (unitParamId == null)
				throw new AppError("Network port block error: unit id parameter is not defined");
			Long unitId = (Long)algo.getData(unitParamId);
			if (unitId == null)
				throw new AppError("Network port block error: unit id is not defined");
			
			// check session id
			Id sidParamId = Id.Factory.loadFrom((String)algo.getProperty("sidParamId"));
			if (sidParamId == null)
				throw new AppError("Network port block error: session id parameter is not defined");
			String sessionId = (String)algo.getData(sidParamId);
			if (sessionId == null)
				throw new AppError("Network port block error: session id is not defined");
			// request generation
			ProcessDocument docRq = ProcessDocument.Factory.newInstance();			
			ProcessRq rq = docRq.addNewProcess().addNewProcessRq();
			
			rq.setSID(sessionId);
			
			Object sendMess = algo.getProperty("sendMess");
			if (sendMess != null && toSend.booleanValue()) {
				try {
					if (sendMess instanceof Bin)
						rq.setSendPacket(((Bin)sendMess).get());
					else {
						Structure sendParam = (Structure)algo.getProperty("smioSendParameters");
						if (sendParam != null)
							rq.setSendPacket(((XmlObjectMessagingInterface)sendMess).writeStructuredMessageParam(sendParam));
						else
							rq.setSendPacket(((XmlObjectMessagingInterface)sendMess).writeStructuredMessage());
					}
				} catch (Exception e) {
					throw new AppException("Network port block SMIO exception: " + e.getMessage(), e);
				}
			}
			else
				rq.setSendPacket(null);

			Boolean toClose = (Boolean)algo.getProperty("toClose");
//			rq.setToClose(toClose);
			
			rq.setCallbackPid(String.valueOf(process.getPid().getPkVals().get(0)));

			// async onEvent
			Long waitId = algo.scheduleEvent(null);
			rq.setCallbackWid(String.valueOf(waitId));
			
			ProcessDocument docRs;
			try {
				// process request to net port handler
				docRs = (ProcessDocument)arte.getArteSocket().invokeInternalService(
						/*obj*/docRq, 
						/*resultClass*/ProcessDocument.class, 
						/*service*/"http://schemas.radixware.org/netporthandler.wsdl#" + unitId,
						/*keepConnectTime*/NETPORT_SERVICE_KEEP_CONNECTIONTIME, 
						/*timeout*/NETPORT_SERVICE_TIMEOUT);
			} catch (ServiceCallFault e) {
				// disconnect
				return exit(EXIT_DISCONNECT, timeout);				
			} catch (ServiceCallException e) {
				throw new AppException("Network port block invoke service exception: " + e.getMessage(), e);
			} catch (ServiceCallTimeout e) {
				throw new AppException("Network port block invoke service timeout: " + e.getMessage(), e);
			} catch (Exception e) {
				throw new AppException("Network port block invoke service other exception: " + e.getMessage(), e);
			}
			
			Object recvMess = null;
			String recvClass = (String)algo.getProperty("recvMessClass");
				
			if (docRs != null)
				try {
					if (recvClass == null)
						recvMess = new Bin(docRs.getProcess().getProcessRs().getReceivedPacket());
					else {
						Structure recvParam = (Structure)algo.getProperty("smioRecvParameters");
						Class<?> factory = getClassFactory(algo, recvClass);
						recvMess = factory.newInstance();
						if (recvParam != null)
							((XmlObjectMessagingInterface)recvMess).readStructuredMessageParam(docRs.getProcess().getProcessRs().getReceivedPacket(), recvParam);
						else
							((XmlObjectMessagingInterface)recvMess).readStructuredMessage(docRs.getProcess().getProcessRs().getReceivedPacket());
					}
				} catch (Exception e) {
					throw new AppException("Network port block SMIO exception: " + e.getMessage(), e);
				}

			algo.setProperty("recvMess", recvMess);
			return exit(EXIT_RECV, timeout);
		}
		if (timeout != null) {
			if (timeout.compareTo(BigDecimal.valueOf(0)) <= 0)
				// exit now
				return exit(EXIT_TIMEOUT, timeout);
			else
				// onTimeout
				algo.scheduleTimeoutJob(timeout.doubleValue(), null);				
		}
		if (strobId != null)
			algo.scheduleStrob(strobId); //подписка на onStrob
		return -1;
	}

	public static int resume(final Algorithm algo) throws Exception  {
                AlgorithmExecutor executor = algo.getExecutor();
		BigDecimal timeout = (BigDecimal)algo.getProperty("recvTimeout");

		// check port data
		PortData data = null;
		if (executor.getCurrentThread().clientData instanceof PortData)
			data = (PortData)executor.getCurrentThread().clientData;

		if (data != null) {
			data.inProcessed = true;
			
			// disconnect
			if (!data.connected)
				return exit(EXIT_DISCONNECT, timeout);

			// none
			if (data.recvPacket == null)
				return exit(EXIT_NONE, timeout);

			// recv
			Object recvMess = null;
			String recvClass = (String)algo.getProperty("recvMessClass");
				
			if (data.recvPacket != null)
				try {
					if (recvClass == null)
						recvMess = new Bin(data.recvPacket);
					else {
						Structure recvParam = (Structure)algo.getProperty("smioRecvParameters");
						Class<?> factory = getClassFactory(algo, recvClass);
						recvMess = factory.newInstance();
						if (recvParam != null)
							((XmlObjectMessagingInterface)recvMess).readStructuredMessageParam(data.recvPacket, recvParam);
						else
							((XmlObjectMessagingInterface)recvMess).readStructuredMessage(data.recvPacket);
					}
				} catch (Exception e) {
					throw new AppException("Network port block SMIO exception: " + e.getMessage(), e);
				}
			
			algo.setProperty("recvMess", recvMess);
			return exit(EXIT_RECV, timeout);
		}
		
		// timeout
		return exit(EXIT_TIMEOUT, timeout);
	}	

	// get class factory by name
	private static Class<?> getClassFactory(final Algorithm algo, final String xmlClass) throws Exception  {
		return algo.getClass().getClassLoader().loadClass(xmlClass + "$Factory");
//		return algo.getClass().getClassLoader().loadClass(xmlClass + ".MessageInstanceDocument$Factory");
	}

	// get class factory by name
	private static int exit(int exit, BigDecimal timeout){
//		return exit == EXIT_TIMEOUT ? EXIT_TIMEOUT : (exit - (timeout == null ? 1 : 0));
		return exit;
	}
}
