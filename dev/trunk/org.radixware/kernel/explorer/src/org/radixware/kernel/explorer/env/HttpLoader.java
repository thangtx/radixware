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

package org.radixware.kernel.explorer.env;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import com.trolltech.qt.core.QObject;
import com.trolltech.qt.gui.QWidget;
import org.radixware.kernel.explorer.dialogs.ExplorerMessageBox;

/**
 *
 * Загрузчик файлов по протоколу http

 *
 */
public final class HttpLoader extends QObject {
	private QWidget parentWidget;

	public HttpLoader() {
		super();
	}

	public HttpLoader(final QWidget parent) {
		super(parent);
        parentWidget = parent;
	}

	/**
	 *
	 * @param url			-	загружаемый файл
	 * @param destination	-	назначение
	 * @return	количество записанных байт
	 * @throws OutOfMemoryError
	 * @throws Exception
	 */
	public int download(final String url, final OutputStream destination) throws IOException {
		final URL u = new URL(url);
		final URLConnection connection = u.openConnection();
//		connection.setDoOutput(true);
		final int avilable = connection.getContentLength();
		if (avilable>0){
			final InputStream stream = connection.getInputStream();
			try{
				final byte[] content = new byte[avilable];
				int n = 0;
				while (n < content.length) {
					int count = stream.read(content, n, content.length - n);
					if (count < 0){
						ExplorerMessageBox.critical(parentWidget, "Error", "Can't read HTTP message: EOF");
						return 0;

					}
					n += count;
				}
				destination.write(content);
				destination.flush();
			}
			catch (OutOfMemoryError err){
				ExplorerMessageBox.critical(parentWidget, "Error", "Cannot download \'"+url+"\' :\n"+err.getMessage());
				return 0;
			}
			catch (Exception e){
				ExplorerMessageBox.critical(parentWidget, "Error", "Cannot download \'"+url+"\' :\n"+e.getMessage());
				return 0;
			}
			finally{
				stream.close();
			}
		}
		return avilable;
	}


}
