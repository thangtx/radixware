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

package org.radixware.kernel.common.msdl;

import com.linuxense.javadbf.DBFReader;
import com.linuxense.javadbf.DBFWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.radixware.kernel.common.exceptions.SmioError;
import org.radixware.kernel.common.exceptions.SmioException;
import org.radixware.schemas.msdl.Structure;

/*
 * <xb:extension for= "...">
 *	<xb:interface  name="org.radixware.kernel.common.msdl.XmlObjectMessagingInterface">
 *		<xb:staticHandler> org.radixware.kernel.common.msdl.XmlObjectMessagingHandler </xb:staticHandler>
 *	</xb:interface>
 * </xb:extension>
 */
public interface XmlObjectMessagingInterface {

    void readStructuredMessageFromInput(InputStream inp) throws IOException, SmioException, SmioError;

    void readStructuredMessage(byte[] inp) throws SmioException, SmioError;

    void readStructuredMessageFromInputWithClear(InputStream inp) throws IOException, SmioException, SmioError;

    void readStructuredMessageWithClear(byte[] inp) throws SmioException, SmioError;

    void writeStructuredMessageToOutput(OutputStream out) throws IOException, SmioError, SmioException;

    byte[] writeStructuredMessage() throws SmioError, SmioException;

    void readStructuredMessageFromInputParam(InputStream inp, Structure defaults) throws IOException, SmioException, SmioError;

    void readStructuredMessageParam(byte[] inp, Structure defaults) throws SmioException, SmioError;

    void readStructuredMessageFromInputParamWithClear(InputStream inp, Structure defaults) throws IOException, SmioException, SmioError;

    void readStructuredMessageParamWithClear(byte[] inp, Structure defaults) throws SmioException, SmioError;

    void writeStructuredMessageToOutputParam(OutputStream out, Structure defaults) throws IOException, SmioError, SmioException;

    byte[] writeStructuredMessageParam(Structure defaults) throws SmioError, SmioException;

    void writeStructuredMessageSeparator(OutputStream out) throws IOException, SmioError;

    byte[] writeStructuredMessageSeparator() throws SmioError;

    DBFWriter openDbfFileWriter() throws SmioException;

    void writeNextRecordToDbf(DBFWriter writer) throws IOException, SmioException;

    void closeDbfFile(DBFWriter writer) throws SmioException;

    DBFReader openDbfReader(InputStream stream) throws SmioException;

    boolean readNextRecordFromDbf(DBFReader reader) throws IOException, SmioException;

    RootMsdlScheme getRootMsdlScheme();
    
    MsdlField getField(String name);
}
