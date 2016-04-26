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

package org.radixware.kernel.explorer.editors.scml;

import java.io.IOException;
import java.util.List;
import org.apache.xmlbeans.XmlObject;


public interface IScml {
    
    public List<IScmlItem> getItems();
    
    public void add(IScmlItem item);
    
    public void add(int position, IScmlItem item);
    
    public void add(int position,int offset, IScmlItem item);
    
    public void add(int position,int offset, List<IScmlItem> items);
    
    public IScml getCopy();
    
    public IScml getCopy(int from, int to);
    
    public IScml getCopy(int from, int fromOffset, int to, int toOffset);
    
    public boolean isReadOnly();
    
    public XmlObject saveToXml();
    
    public void loadFromXml(XmlObject xmlObject) throws IOException;
    
}
