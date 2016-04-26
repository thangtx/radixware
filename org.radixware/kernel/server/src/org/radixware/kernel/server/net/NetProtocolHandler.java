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

package org.radixware.kernel.server.net;

import java.util.Map;
import org.radixware.kernel.common.exceptions.AppException;
import org.radixware.kernel.common.types.Bin;
import org.radixware.schemas.netporthandler.OnConnectRsDocument;
import org.radixware.schemas.netporthandler.OnConnectTimeoutRsDocument;
import org.radixware.schemas.netporthandler.OnRecvRsDocument;
import org.radixware.schemas.netporthandler.OnRecvTimeoutRsDocument;

public interface NetProtocolHandler {
	/**
	 *Вызывается модулем при получении сетевого запроса на установку соединения.
	 * 
	 * Любое исключение вызывает disconnect;
	 * Если установлен sendPacket, он передается клиенту;
	 * Если установлен toClose, соединение разрывается;
	 * Callback сохраняется;
	 * @param channelId
	 * @param serverAddr
	 * @param clientAddr
	 * @param clientCertCn
	 * @param sid
	 * @return
	 * @throws AppException
	 */
	OnConnectRsDocument onConnect(Long channelId, String serverAddr, String clientAddr, String clientCertCn, String sid) throws AppException;
	
	/**
	 * Вызывается модулем при получении сообщения.
	 * Модуль при получении ответа:
	 * Любое исключение вызывает disconnect 
	 * Если установлен undelivered запоминает recvPacket для последующей передачи в ответ на ближайший process. В этом случае callback==null
	 * Если установлен sendPacket, передает его
	 * Если установлен toClose, разрывает соединение
	 * Сохраняет callback
	 * @param channelId
	 * @param serverAddr
	 * @param clientAddr
	 * @param clientCertCn
	 * @param sid
	 * @param сallbackPid
	 * @param сallbackWid
	 * @param recvPacket
	 * @param recvPacketHeaders
	 * @param connected
	 * @return
	 * @throws AppException
	 */
	OnRecvRsDocument onRecv(Long channelId, String serverAddr, String clientAddr, String clientCertCn, String sid, String callbackPid, String callbackWid, Map<String, String> recvPacketHeaders, Bin recvPacket, Boolean connected) throws AppException;

	/**
	 * Вызывается модулем при срабатывании таймаута.
	 * @param channelId
	 * @param serverAddr
	 * @param clientAddr
	 * @param clientCertCn
	 * @param sid
	 * @param сallbackPid
	 * @param сallbackWid
	 * @return
	 * @throws AppException
	 */
	OnRecvTimeoutRsDocument onRecvTimeout(Long channelId, String serverAddr, String clientAddr, String clientCertCn, String sid, String callbackPid, String callbackWid) throws AppException;
        
	/**
	 * Вызывается модулем при срабатывании таймаута при коннекте.
	 * @param channelId
	 * @param serverAddr
	 * @param clientAddr
	 * @param clientCertCn
	 * @param sid
	 * @param сallbackPid
	 * @param сallbackWid
	 * @return
	 * @throws AppException
	 */
        
	OnConnectTimeoutRsDocument onConnectTimeout(Long channelId, String serverAddr, String clientAddr, String clientCertCn, String sid, String callbackPid, String callbackWid) throws AppException;        
        
	/**
	 * Вызывается модулем при разрыве соединения по инициативе клиента
	 * @param channelId
	 * @param serverAddr
	 * @param clientAddr
	 * @param clientCertCn
	 * @param sid
	 * @param сallbackPid
	 * @param сallbackWid
	 */
	void onDisconnect(Long channelId, String serverAddr, String clientAddr, String clientCertCn, String sid, String callbackPid, String callbackWid);
	
}
