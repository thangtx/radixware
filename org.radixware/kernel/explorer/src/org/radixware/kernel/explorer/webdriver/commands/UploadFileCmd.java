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
package org.radixware.kernel.explorer.webdriver.commands;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.xml.bind.DatatypeConverter;
import org.json.simple.JSONObject;
import org.radixware.kernel.explorer.webdriver.EHttpMethod;
import org.radixware.kernel.explorer.webdriver.WebDrvSession;
import org.radixware.kernel.explorer.webdriver.WebDrvSessionCommandResult;
import org.radixware.kernel.explorer.webdriver.WebDrvUri;
import org.radixware.kernel.explorer.webdriver.exceptions.EWebDrvErrorCode;
import org.radixware.kernel.explorer.webdriver.exceptions.WebDrvException;

public class UploadFileCmd extends GuiCmd {

    public final static String NAME = "uploadFile";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getGroupName() {
        return null;
    }

    @Override
    public EHttpMethod getHttpMethod() {
        return EHttpMethod.POST;
    }

    @Override
    public JSONObject execute(final WebDrvSession session, final WebDrvUri uri, final JSONObject parameter) throws WebDrvException {
        File tempFile = null;
        try {
            // из парамтра file взять закодированный в base64 кусок данных
            // разжать его zip-ом, сохранить первый файл во временный файл (он там должен быть единственный)
            // вернуть полный путь ко временному файлу.
            // при закрытии сессии файл удалить.

            String base64str = (String) parameter.get("file");
            byte[] decoded = DatatypeConverter.parseBase64Binary(base64str);

            ByteArrayInputStream bis = new ByteArrayInputStream(decoded);
            ZipInputStream zis = new ZipInputStream(bis);
            tempFile = File.createTempFile("RadixExplorerWebDriver", null);

            session.registerFileToDeleteOnExit(tempFile);

            FileOutputStream fo = new FileOutputStream(tempFile);

            OutputStream out = new BufferedOutputStream(fo, 10000);
            try {
                ZipEntry entry;
                if ((entry = zis.getNextEntry()) != null) {
                    byte[] buffer = new byte[10000];
                    int read;
                    while ((read = zis.read(buffer)) != -1) {
                        out.write(buffer, 0, read);
                    }
                }
            } finally {
                out.close();
            }

            return new WebDrvSessionCommandResult(tempFile.getAbsolutePath());
        } catch (Exception ex) {
            throw new WebDrvException(EWebDrvErrorCode.UNKNOWN_ERROR, ex.getMessage());
        }
    }

}
