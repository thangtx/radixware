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

package org.radixware.kernel.common.exceptions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.xml.namespace.QName;

import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.utils.XmlUtils;
import org.xmlsoap.schemas.soap.envelope.Detail;

public class ServiceCallFault extends ServiceClientException {
//Consts	

    private static final String FLT_DET_XSD = "http://schemas.radixware.org/faultdetail.xsd";
    private static final QName DET_MESS_QNAME = new QName(FLT_DET_XSD, "Message");
    private static final QName DET_EX_QNAME = new QName(FLT_DET_XSD, "Exception");
    private static final QName DET_EX_CLASS = new QName(FLT_DET_XSD, "Class");
    private static final QName DET_EX_MESS = new QName(FLT_DET_XSD, "Message");
    private static final QName DET_EX_STACK = new QName(FLT_DET_XSD, "Stack");
    private static final QName DET_TRC_QNAME = new QName(FLT_DET_XSD, "Trace");
    private static final QName DET_TRC_ITEM_QNAME = new QName(FLT_DET_XSD, "Item");
    private static final QName DET_TRC_ITEM_LEVEL_QNAME = new QName("Level");
//Fields    
    private static final long serialVersionUID = 3413280097272895736L;
    private final String faultString;
    private final String faultCode;
    private final Detail detail;

    public ServiceCallFault(String faultCode, String faultString, Detail detail) {
        this(faultCode, faultString, detail, null);
    }

    public ServiceCallFault(String faultCode, String faultString, Detail detail, Exception cause) {
        super(getMessage(faultCode, faultString, detail), cause);
        this.faultCode = faultCode;
        this.faultString = faultString;
        this.detail = detail;
    }

    public String getFaultCode() {
        return faultCode;
    }

    public String getFaultString() {
        return faultString;
    }

    public Detail getDetail() {
        return detail;
    }

    public String getCauseExClass() {
        return getCauseAttr(DET_EX_CLASS);
    }

    public String getCauseExStack() {
        return getCauseAttr(DET_EX_STACK);
    }

    public String getCauseExMessage() {
        return getCauseAttr(DET_EX_MESS);
    }
    
    @Override
    public Throwable getCause() {
        final String    stack = getCauseExStack();
        
        return stack == null || stack.isEmpty() ? super.getCause() : new RemoteThrowable().setStackTrace(stack);
    }

//Private methods    
    private String getCauseAttr(QName attrName) {
        final XmlObject dbpEx = getCauseException();
        if (dbpEx != null) {
            String res = null;
            final XmlObject[] attr = dbpEx.selectChildren(attrName);
            if (attr != null && attr.length > 0) {
                final XmlCursor c = attr[0].newCursor();
                try {
                    c.toFirstChild();
                    res = c.getTextValue();
                } finally {
                    c.dispose();
                }
            }
            return res;
        }
        return null;
    }

    private static final String getMessage(String faultCode, String faultString, Detail detail) {
        if (detail != null) {
            final XmlObject[] mess = detail.selectChildren(DET_MESS_QNAME);
            if (mess != null && mess.length > 0) {
                final XmlCursor c = mess[0].newCursor();
                final String res;
                try {
                    c.toFirstChild();
                    res = c.getTextValue();
                } finally {
                    c.dispose();
                }
                return res;
            }
        }
        return "SOAP Fault {Code = \"" + faultCode + "\", Reason = \"" + faultString + "\""
                + (detail != null ? ", Detail = \"" + detail.toString() + "\"" : "") + "}";
    }

    private final XmlObject getCauseException() {
        if (detail != null) {
            final XmlObject[] dbpEx = detail.selectChildren(DET_EX_QNAME);
            if (dbpEx != null && dbpEx.length > 0) {
                return dbpEx[0];
            }
        }
        return null;
    }

    static public final class TraceItem {

        public final String level;
        public final String text;

        public TraceItem(final String level, final String text) {
            this.level = level;
            this.text = text;
        }
    }

    public final List<TraceItem> getTrace() {
        final List<TraceItem> trc = new LinkedList<ServiceCallFault.TraceItem>();
        if (detail != null) {
            final XmlObject[] dbpEx = detail.selectChildren(DET_TRC_QNAME);
            if (dbpEx != null && dbpEx.length > 0) {
                for (XmlObject trcItem : dbpEx[0].selectChildren(DET_TRC_ITEM_QNAME)) {
                    final XmlCursor c = trcItem.newCursor();
                    try {
                        c.toFirstChild();
                        trc.add(new TraceItem( c.getAttributeText(DET_TRC_ITEM_LEVEL_QNAME), XmlUtils.parseSafeXmlString(c.getTextValue()) ));
                    } finally {
                        c.dispose();
                    }

                }
            }
        }
        return trc;
    }
    
    private static class RemoteThrowable extends Throwable {
	private static final long serialVersionUID = -942110421439611384L;
	
	private String remoteClass = this.getClass().getName(), remoteMessage = null;
	private Throwable remoteCause = null;

	public RemoteThrowable() {
            super(null,null,false,true);
	}
	
	public RemoteThrowable(final String message) {
            super(message,null,false,true);
	}

	public RemoteThrowable(final String message, final Throwable t) {
            super(message,t,false,true);
	}

	public RemoteThrowable(final Throwable t) {
            super(null,t,false,true);
	}

	@Override
	public String getMessage() {
            return remoteMessage != null ? remoteMessage : super.getMessage();
	}

	@Override
	public Throwable getCause() {
            return remoteCause != null ? remoteCause : super.getCause();
	}
	
	@Override
	public String toString() {
            return new StringBuilder().append(remoteClass).append(": ").append(getMessage()).toString();
	}
	
	RemoteThrowable setStackTrace(final String serializedSstackTrace) {	
            if (serializedSstackTrace == null || serializedSstackTrace.isEmpty()) {
                throw new IllegalArgumentException("Serialized stack trace can't be null");
            }
            else {
                try(final Reader rdr = new StringReader(serializedSstackTrace);
                    final BufferedReader brdr = new BufferedReader(rdr)) {

                    processNested(brdr,brdr.readLine(),new ArrayList<StackTraceElement>(),this);
                } catch (IOException e) {
                    throw new IllegalArgumentException("Error processing serialized stack trace: ["+serializedSstackTrace+"]");
                }
            }
            return this;
	}
	
	private void processNested(final BufferedReader brdr, final String firstLine, final List<StackTraceElement> collection, final RemoteThrowable target) throws IOException {
            int		colonIndex = firstLine.indexOf(':'); 

            if (colonIndex >= 0) {
                target.remoteClass = firstLine.substring(0,colonIndex);
                target.remoteMessage = firstLine.substring(colonIndex+1);
            }
            else {
                target.remoteMessage = firstLine;
            }

            String 	buffer;

            while ((buffer = brdr.readLine()) != null) {
                if (buffer.trim().startsWith("at ")) {				// Usual trace line
                    collection.add(toStackTraceElement(buffer.substring("at ".length()).trim()));
                }
                else if (buffer.trim().startsWith("Caused by: ")) {	// Nested exception was detected
                    target.setStackTrace(collection.toArray(new StackTraceElement[collection.size()]));
                    target.remoteCause = new RemoteThrowable();
                    processNested(brdr,buffer.substring("Caused by: ".length()).trim(),collection,(RemoteThrowable)target.remoteCause);
                    return;
                }
            }
            target.setStackTrace(collection.toArray(new StackTraceElement[collection.size()]));
	}
	
	private StackTraceElement toStackTraceElement(final String description) {
            int 		bracket = description.indexOf('('), dot, line = -1;
            String		processing, className = "unknown", methodName = "unknown", fileName = null; 

            if (bracket >= 0) {
                final String[]	fileAndLine = description.substring(bracket+1,description.length()-1).split("\\:");

                if (fileAndLine.length == 2) {
                    fileName = fileAndLine[0];
                    line = Integer.valueOf(fileAndLine[1]);
                }
                processing = description.substring(0,bracket);
            }
            else {
                processing = description;
            }

            if ((dot = processing.lastIndexOf('.')) >= 0) {
                className = processing.substring(0,dot);
                methodName = processing.substring(dot+1);
            }

            return new StackTraceElement(className,methodName,fileName,line);
	}
    }
}
