
/* Radix::SystemMonitor::InstanceStateHistory - Server Executable*/

/*Radix::SystemMonitor::InstanceStateHistory-Entity Class*/

package org.radixware.ads.SystemMonitor.server;

import java.util.Objects;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory")
public final class InstanceStateHistory  extends org.radixware.ads.Types.server.Entity  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {

	private final static String DATE_FORMAT_STR = "yyyy-MM-dd_HH-mm-ss.SSSXXX";
	private final static java.text.DateFormat DATE_FORMAT = new java.text  .SimpleDateFormat(DATE_FORMAT_STR);

	private static String formatDate(Int unixEpoch, Str defaultValue) {
	    return unixEpoch == null ? defaultValue : DATE_FORMAT.format(new java.util.Date(unixEpoch.longValue()));
	    
	}

	private static String getThreadStackAsHtml(String rawStack) {
	        final char nline = '\n';
	        final String stackTop = rawStack.indexOf(nline) == -1 ? rawStack : rawStack.substring(0, rawStack.indexOf(nline));
	        
	        java.lang.StringBuilder sb = new java.lang.StringBuilder();

	        sb.append(nline);
	        sb.append("<span class=\"closed\">\n");
	        sb.append("<a href=\"#recordInfo_").append("id").append("\" onclick=\"toggleThreadDetails(event)\">");
	        sb.append(stackTop);
	        sb.append("</a>");
	        sb.append(nline);

	        
	        sb.append("<span class=\"threadDetails\">");
	        sb.append(nline);
	        sb.append("<code>");
	        sb.append(nline);
	        
	        sb.append(rawStack.replace("\n", "<br/>"));
	        sb.append(nline);
	        sb.append("</code>");
	        sb.append(nline);

	        sb.append("</span>");
	        sb.append(nline);
	        sb.append("</span>");
	        sb.append(nline);

	        return sb.toString();
	    }
	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return InstanceStateHistory_mi.rdxMeta;}

	/*Radix::SystemMonitor::InstanceStateHistory:Nested classes-Nested Classes*/

	/*Radix::SystemMonitor::InstanceStateHistory:Properties-Properties*/

	/*Radix::SystemMonitor::InstanceStateHistory:arteSeq-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:arteSeq")
	public  Int getArteSeq() {
		return arteSeq;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:arteSeq")
	public   void setArteSeq(Int val) {
		arteSeq = val;
	}

	/*Radix::SystemMonitor::InstanceStateHistory:arteSerial-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:arteSerial")
	public  Int getArteSerial() {
		return arteSerial;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:arteSerial")
	public   void setArteSerial(Int val) {
		arteSerial = val;
	}

	/*Radix::SystemMonitor::InstanceStateHistory:cpuDiffMillis-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:cpuDiffMillis")
	public  Int getCpuDiffMillis() {
		return cpuDiffMillis;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:cpuDiffMillis")
	public   void setCpuDiffMillis(Int val) {
		cpuDiffMillis = val;
	}

	/*Radix::SystemMonitor::InstanceStateHistory:dbDiffMillis-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:dbDiffMillis")
	public  Int getDbDiffMillis() {
		return dbDiffMillis;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:dbDiffMillis")
	public   void setDbDiffMillis(Int val) {
		dbDiffMillis = val;
	}

	/*Radix::SystemMonitor::InstanceStateHistory:dbSerial-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:dbSerial")
	public  Str getDbSerial() {
		return dbSerial;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:dbSerial")
	public   void setDbSerial(Str val) {
		dbSerial = val;
	}

	/*Radix::SystemMonitor::InstanceStateHistory:dbSid-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:dbSid")
	public  Str getDbSid() {
		return dbSid;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:dbSid")
	public   void setDbSid(Str val) {
		dbSid = val;
	}

	/*Radix::SystemMonitor::InstanceStateHistory:extData-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:extData")
	public  Str getExtData() {
		return extData;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:extData")
	public   void setExtData(Str val) {
		extData = val;
	}

	/*Radix::SystemMonitor::InstanceStateHistory:extDiffMillis-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:extDiffMillis")
	public  Int getExtDiffMillis() {
		return extDiffMillis;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:extDiffMillis")
	public   void setExtDiffMillis(Int val) {
		extDiffMillis = val;
	}

	/*Radix::SystemMonitor::InstanceStateHistory:forced-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:forced")
	public  Bool getForced() {
		return forced;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:forced")
	public   void setForced(Bool val) {
		forced = val;
	}

	/*Radix::SystemMonitor::InstanceStateHistory:instanceId-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:instanceId")
	public  Int getInstanceId() {
		return instanceId;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:instanceId")
	public   void setInstanceId(Int val) {
		instanceId = val;
	}

	/*Radix::SystemMonitor::InstanceStateHistory:lockName-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:lockName")
	public  Str getLockName() {
		return lockName;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:lockName")
	public   void setLockName(Str val) {
		lockName = val;
	}

	/*Radix::SystemMonitor::InstanceStateHistory:lockOwnerName-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:lockOwnerName")
	public  Str getLockOwnerName() {
		return lockOwnerName;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:lockOwnerName")
	public   void setLockOwnerName(Str val) {
		lockOwnerName = val;
	}

	/*Radix::SystemMonitor::InstanceStateHistory:name-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:name")
	public  Str getName() {
		return name;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:name")
	public   void setName(Str val) {
		name = val;
	}

	/*Radix::SystemMonitor::InstanceStateHistory:queueDiffMillis-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:queueDiffMillis")
	public  Int getQueueDiffMillis() {
		return queueDiffMillis;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:queueDiffMillis")
	public   void setQueueDiffMillis(Int val) {
		queueDiffMillis = val;
	}

	/*Radix::SystemMonitor::InstanceStateHistory:regTimeMillis-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:regTimeMillis")
	public  Int getRegTimeMillis() {
		return regTimeMillis;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:regTimeMillis")
	public   void setRegTimeMillis(Int val) {
		regTimeMillis = val;
	}

	/*Radix::SystemMonitor::InstanceStateHistory:rqStartTimeMillis-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:rqStartTimeMillis")
	public  Int getRqStartTimeMillis() {
		return rqStartTimeMillis;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:rqStartTimeMillis")
	public   void setRqStartTimeMillis(Int val) {
		rqStartTimeMillis = val;
	}

	/*Radix::SystemMonitor::InstanceStateHistory:stackDigest-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:stackDigest")
	public  Str getStackDigest() {
		return stackDigest;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:stackDigest")
	public   void setStackDigest(Str val) {
		stackDigest = val;
	}

	/*Radix::SystemMonitor::InstanceStateHistory:threadId-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:threadId")
	public  Int getThreadId() {
		return threadId;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:threadId")
	public   void setThreadId(Int val) {
		threadId = val;
	}

	/*Radix::SystemMonitor::InstanceStateHistory:threadKind-Column-Based Property*/








			















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:threadKind")
	public  org.radixware.kernel.common.enums.EInstanceThreadKind getThreadKind() {
		return threadKind;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:threadKind")
	public   void setThreadKind(org.radixware.kernel.common.enums.EInstanceThreadKind val) {
		threadKind = val;
	}

	/*Radix::SystemMonitor::InstanceStateHistory:traceContexts-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:traceContexts")
	public  Str getTraceContexts() {
		return traceContexts;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:traceContexts")
	public   void setTraceContexts(Str val) {
		traceContexts = val;
	}

	/*Radix::SystemMonitor::InstanceStateHistory:unitId-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:unitId")
	public  Int getUnitId() {
		return unitId;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:unitId")
	public   void setUnitId(Int val) {
		unitId = val;
	}

	/*Radix::SystemMonitor::InstanceStateHistory:uptimeSec-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:uptimeSec")
	public  Int getUptimeSec() {
		return uptimeSec;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:uptimeSec")
	public   void setUptimeSec(Int val) {
		uptimeSec = val;
	}

	/*Radix::SystemMonitor::InstanceStateHistory:stackData-Parent Reference*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:stackData")
	public  org.radixware.ads.SystemMonitor.server.StackData getStackData() {
		return stackData;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:stackData")
	public   void setStackData(org.radixware.ads.SystemMonitor.server.StackData val) {
		stackData = val;
	}

	/*Radix::SystemMonitor::InstanceStateHistory:stack-Parent Property*/






















@SuppressWarnings({"unused","unchecked"})
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:stack")
public  Str getStack() {
	return stack;
}

@SuppressWarnings({"unused","unchecked"})
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:stack")
public   void setStack(Str val) {
	stack = val;
}

/*Radix::SystemMonitor::InstanceStateHistory:regTime-Dynamic Property*/



protected java.sql.Timestamp regTime=null;











@SuppressWarnings({"unused","unchecked"})
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:regTime")
public  java.sql.Timestamp getRegTime() {

	return new java.sql.Timestamp(regTimeMillis.longValue());
}

@SuppressWarnings({"unused","unchecked"})
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:regTime")
public   void setRegTime(java.sql.Timestamp val) {
	regTime = val;
}

/*Radix::SystemMonitor::InstanceStateHistory:rqStartTime-Dynamic Property*/



protected java.sql.Timestamp rqStartTime=null;











@SuppressWarnings({"unused","unchecked"})
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:rqStartTime")
public  java.sql.Timestamp getRqStartTime() {

	return rqStartTimeMillis == null ? null : new java.sql.Timestamp(rqStartTimeMillis.longValue());
}

@SuppressWarnings({"unused","unchecked"})
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:rqStartTime")
public   void setRqStartTime(java.sql.Timestamp val) {
	rqStartTime = val;
}

/*Radix::SystemMonitor::InstanceStateHistory:stackTop-Parent Property*/






















@SuppressWarnings({"unused","unchecked"})
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:stackTop")
public  Str getStackTop() {
	return stackTop;
}

@SuppressWarnings({"unused","unchecked"})
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:stackTop")
public   void setStackTop(Str val) {
	stackTop = val;
}

/*Radix::SystemMonitor::InstanceStateHistory:ancestorThreadId-Column-Based Property*/
















@SuppressWarnings({"unused","unchecked"})
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:ancestorThreadId")
public  Int getAncestorThreadId() {
	return ancestorThreadId;
}

@SuppressWarnings({"unused","unchecked"})
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:ancestorThreadId")
public   void setAncestorThreadId(Int val) {
	ancestorThreadId = val;
}





































































































































































































/*Radix::SystemMonitor::InstanceStateHistory:Methods-Methods*/

/*Radix::SystemMonitor::InstanceStateHistory:exportToXml-User Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:exportToXml")
  void exportToXml (javax.xml.stream.XMLStreamWriter writer) throws java.sql.SQLException,java.io.UnsupportedEncodingException,javax.xml.stream.XMLStreamException {
	class XmlWriter {
	    private final javax.xml.stream.XMLStreamWriter writer;
	    
	    public XmlWriter(javax.xml.stream.XMLStreamWriter writer) {
	        this.writer = writer;
	    }
	    
	    public XmlWriter attr(Str name, Str value) {
	        try {
	            if (value != null) {
	                writer.writeAttribute(name, org.radixware.kernel.common.build.xbeans.XmlEscapeStr.getSafeXmlString(value));
	            }
	        } catch (Exceptions::XmlStreamException ex) {
	            Arte::Trace.error("Bad luck, should never happen: " + ex, Arte::EventSource:Eas);
	        }
	        return this;
	    }
	    
	    public XmlWriter attr(Str name, Int value) {
	        if (value != null) {
	            attr(name, value.toString());
	        }
	        return this;
	    }
	    
	    public XmlWriter attr(Str name, Bool value) {
	        if (value != null) {
	            attr(name, value.toString());
	        }
	        return this;
	    }
	}

	final XmlWriter w = new XmlWriter(writer);
	writer.writeCharacters("\n\t");
	writer.writeStartElement("Record");

	w.attr("arteSeq", arteSeq);
	w.attr("arteSerial", arteSerial);
	w.attr("cpuDiffMillis", cpuDiffMillis);
	w.attr("dbDiffMillis", dbDiffMillis);
	w.attr("dbSerial", dbSerial);
	w.attr("dbSid", dbSid);
	//w.attr("extData", );
	w.attr("extDiffMillis", extDiffMillis);
	w.attr("forced", forced);
	w.attr("instanceId", instanceId);
	w.attr("lockName", lockName);
	w.attr("lockOwnerName", lockOwnerName);
	w.attr("name", name);
	w.attr("queueDiffMillis", queueDiffMillis);
	w.attr("regTime", formatDate(regTimeMillis, null));
	w.attr("regTimeMillis", regTimeMillis);
	w.attr("rqStartTime", formatDate(rqStartTimeMillis, null));
	w.attr("rqStartTimeMillis", rqStartTimeMillis);
	w.attr("stackDigest", stackDigest);
	w.attr("stackTop", stackTop);
	w.attr("threadId", threadId);
	w.attr("threadKind", threadKind.Name);
	w.attr("traceContexts", traceContexts);
	w.attr("unitId", unitId);
	w.attr("uptimeSec", uptimeSec);

	writer.writeCharacters("\n");
	writer.writeCharacters(org.radixware.kernel.common.build.xbeans.XmlEscapeStr.getSafeXmlString(stack));
	writer.writeCharacters("\n\t");
	writer.writeEndElement();

}

/*Radix::SystemMonitor::InstanceStateHistory:exportToPlain-User Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:exportToPlain")
  void exportToPlain (java.io.PrintWriter writer) throws java.sql.SQLException,java.io.UnsupportedEncodingException,javax.xml.stream.XMLStreamException {
	writer.write("\n");
	writer.write("\narteSeq = " + arteSeq);
	writer.write("\narteSerial = " + arteSerial);
	writer.write("\ncpuDiffMillis = " + cpuDiffMillis);
	writer.write("\ndbDiffMillis = " + dbDiffMillis);
	writer.write("\ndbSerial = " + dbSerial);
	writer.write("\ndbSid = " + dbSid);
	writer.write("\nextData = " + extData);
	writer.write("\nextDiffMillis = " + extDiffMillis);
	writer.write("\nforced = " + forced);
	writer.write("\ninstanceId = " + instanceId);
	writer.write("\nlockName = " + lockName);
	writer.write("\nlockOwnerName = " + lockOwnerName);
	writer.write("\nname = " + name);
	writer.write("\nqueueDiffMillis = " + queueDiffMillis);
	writer.write("\nregTime = " + regTime);
	writer.write("\nregTimeMillis = " + regTimeMillis);
	writer.write("\nrqStartTime = " + rqStartTime);
	writer.write("\nrqStartTimeMillis = " + rqStartTimeMillis);
	writer.write("\nstack = " + stack);
	writer.write("\nstackDigest = " + stackDigest);
	writer.write("\nstackTop = " + stackTop);
	writer.write("\nthreadId = " + threadId);
	writer.write("\nthreadKind = " + threadKind);
	writer.write("\ntraceContexts = " + traceContexts);
	writer.write("\nunitId = " + unitId);
	writer.write("\nuptimeSec = " + uptimeSec);

}

/*Radix::SystemMonitor::InstanceStateHistory:exportToHtml-User Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:exportToHtml")
  void exportToHtml (java.io.PrintWriter writer) throws java.sql.SQLException,java.io.UnsupportedEncodingException,javax.xml.stream.XMLStreamException {
	final char nline = '\n';
	StringBuilder sb = new StringBuilder();

	final String[] columnsValues = new String[] {
	    formatDate(regTimeMillis, ""),
	    Objects.toString(forced, ""),
	    threadKind == null ? "" : threadKind.Value,
	    "" + threadId,
	    "" + name,
	    Utils::String.isBlank(stack) ? "" : getThreadStackAsHtml(stack),
	    Objects.toString(ancestorThreadId, ""),
	    Objects.toString(unitId, ""),
	    Objects.toString(arteSeq, ""),
	    Objects.toString(arteSerial, ""),
	    Objects.toString(dbSid, ""),
	    Objects.toString(dbSerial, ""),
	    Objects.toString(traceContexts, ""),
	    Objects.toString(cpuDiffMillis, ""),
	    Objects.toString(dbDiffMillis, ""),
	    Objects.toString(extDiffMillis, ""),
	    Objects.toString(queueDiffMillis, ""),
	    Objects.toString(uptimeSec, ""),
	    formatDate(rqStartTimeMillis, ""),
	    Objects.toString(lockName, ""),
	    Objects.toString(lockOwnerName, "")
	};

	sb.append("<tr>").append(nline);
	for (String columnsValue: columnsValues) {
	    sb.append("<td>").append(columnsValue).append("</td>").append(nline);
	}
	sb.append("</tr>").append(nline);

	writer.append(sb.toString());

}


@Override
public org.radixware.kernel.server.types.FormHandler.NextDialogsRequest execCommand(org.radixware.kernel.common.types.Id cmdId, org.radixware.kernel.common.types.Id propId, org.apache.xmlbeans.XmlObject input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValsById, org.apache.xmlbeans.XmlObject output) throws org.radixware.kernel.common.exceptions.AppException,java.lang.InterruptedException{

		return super.execCommand(cmdId,propId,input,newPropValsById,output);
}


}

/* Radix::SystemMonitor::InstanceStateHistory - Server Meta*/

/*Radix::SystemMonitor::InstanceStateHistory-Entity Class*/

package org.radixware.ads.SystemMonitor.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class InstanceStateHistory_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHY4TVWIC5C4TJN2GKL2ABRKKQ"),"InstanceStateHistory",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls25Z2LCA2KFHWRH3LLGFWKJFXYU"),

						org.radixware.kernel.common.enums.EClassType.ENTITY,

						/*Radix::SystemMonitor::InstanceStateHistory:Presentations-Entity Presentations*/
						/*Class presentation attributes*/
						 new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef(/*Owner Class Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHY4TVWIC5C4TJN2GKL2ABRKKQ"),
							/*Owner Class Name*/
							"InstanceStateHistory",
							/*Title Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("mls25Z2LCA2KFHWRH3LLGFWKJFXYU"),
							/*Property presentations*/

							/*Radix::SystemMonitor::InstanceStateHistory:Properties-Properties*/
							new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef[]{

									/*Radix::SystemMonitor::InstanceStateHistory:arteSeq:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3KCBW5QZFREURM6WDQKR5JKHDQ"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::SystemMonitor::InstanceStateHistory:arteSerial:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colU4MHPYS6PVANJMDUSQ5W7RJCIY"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::SystemMonitor::InstanceStateHistory:cpuDiffMillis:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNKGIWVA7PZFZXEPGNO5JPZQRV4"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::SystemMonitor::InstanceStateHistory:dbDiffMillis:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colSMAJMWPRYBHHNCZF6WPLKUCGBQ"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::SystemMonitor::InstanceStateHistory:dbSerial:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLYPEWDCHDVBVVMMA4SFRKIKFW4"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::SystemMonitor::InstanceStateHistory:dbSid:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJBVEDU3XFFC3LLANH2EAOMVKV4"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::SystemMonitor::InstanceStateHistory:extData:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colI7V5NLFAUBA4HGY7VUF6JFLKRI"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::SystemMonitor::InstanceStateHistory:extDiffMillis:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHNUTRIYQXVA2FKJLMS25H2ZO54"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::SystemMonitor::InstanceStateHistory:forced:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colATYCTWD3AFBIVE3AS4TOYUPHSM"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::SystemMonitor::InstanceStateHistory:instanceId:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIFISIE3XWBD3LLQ3MZGOOG7N6M"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::SystemMonitor::InstanceStateHistory:lockName:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3ENNZJ2XUVHWJABO3YCDY4WD2Y"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::SystemMonitor::InstanceStateHistory:lockOwnerName:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col62UUOQ3FM5C3VETVE73AV6T2JI"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::SystemMonitor::InstanceStateHistory:name:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colB4Y5IM6SMBHIVEYIGJSXMALEPQ"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::SystemMonitor::InstanceStateHistory:queueDiffMillis:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPZ5AOPCVBVHYFKD5O3MLHV75LY"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::SystemMonitor::InstanceStateHistory:threadId:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEVE536NNOZBI7N7IOSAL5WOAIQ"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::SystemMonitor::InstanceStateHistory:threadKind:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4P6GRGWAKVF35LL2VSBKJRJ4O4"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::SystemMonitor::InstanceStateHistory:traceContexts:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col5UOR3QOSWNBPJCRIEZ7F7MZW3I"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::SystemMonitor::InstanceStateHistory:unitId:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col66UBVRCJQFEWPDJXIWUQN2EQP4"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::SystemMonitor::InstanceStateHistory:uptimeSec:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHNE7Z4ANKBECRGESFUUFQVCSQQ"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::SystemMonitor::InstanceStateHistory:stack:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colB6RB3PP5DBEHDIMUAG7LLMTTNU"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,true,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECT_CONDITION,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECTOR,org.radixware.kernel.common.enums.EPropAttrInheritance.HINT)),

									/*Radix::SystemMonitor::InstanceStateHistory:regTime:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdGAD64WTSOJEAPIDCFCJCN3PE5M"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::SystemMonitor::InstanceStateHistory:rqStartTime:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdO62TBSLFZZGXVFKTWKMATT5HQE"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::SystemMonitor::InstanceStateHistory:stackTop:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col63DF4VG74ZEUXLPQGA2IHROCJY"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECT_CONDITION,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECTOR,org.radixware.kernel.common.enums.EPropAttrInheritance.HINT)),

									/*Radix::SystemMonitor::InstanceStateHistory:ancestorThreadId:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKEUJLICU4JAIJHRZPG62HNGFGU"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT))
							},
							/*Commands*/
							null,
							/*Sortings*/
							new org.radixware.kernel.server.meta.presentations.RadSortingDef[]{

									new org.radixware.kernel.server.meta.presentations.RadSortingDef(
									/*Radix::SystemMonitor::InstanceStateHistory:RegTimeAsc-Sorting*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("srtR7SYZPQQZFBELKVU454IIOO3VY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHY4TVWIC5C4TJN2GKL2ABRKKQ"),"RegTimeAsc",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsL6GKKVCRTVCKBGM2GBLMY4OYM4"),new org.radixware.kernel.server.meta.presentations.RadSortingDef.Item[]{

											new org.radixware.kernel.server.meta.presentations.RadSortingDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIFISIE3XWBD3LLQ3MZGOOG7N6M"),org.radixware.kernel.common.enums.EOrder.ASC),

											new org.radixware.kernel.server.meta.presentations.RadSortingDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("colELAJRXSM7NCG7FLQYSVADANYP4"),org.radixware.kernel.common.enums.EOrder.ASC),

											new org.radixware.kernel.server.meta.presentations.RadSortingDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEVE536NNOZBI7N7IOSAL5WOAIQ"),org.radixware.kernel.common.enums.EOrder.ASC)
									},"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:Sql>FIRST_ROWS(50) INDEX_ASC(</xsc:Sql></xsc:Item><xsc:Item><xsc:ThisTableSqlName/></xsc:Item><xsc:Item><xsc:Sql> </xsc:Sql></xsc:Item><xsc:Item><xsc:DbName Path=\"tblRHY4TVWIC5C4TJN2GKL2ABRKKQ idxYSAVBTX2LFA4JMOBS5J6FMDZLY\"/></xsc:Item><xsc:Item><xsc:Sql>)</xsc:Sql></xsc:Item></xsc:Sqml>","org.radixware"),

									new org.radixware.kernel.server.meta.presentations.RadSortingDef(
									/*Radix::SystemMonitor::InstanceStateHistory:RegTimeDesc-Sorting*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("srtUOSIXGY46JGFFOLWZFJSSYLAZY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHY4TVWIC5C4TJN2GKL2ABRKKQ"),"RegTimeDesc",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDXOO3S2PIFHUJGSM6QAY7J4T54"),new org.radixware.kernel.server.meta.presentations.RadSortingDef.Item[]{

											new org.radixware.kernel.server.meta.presentations.RadSortingDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIFISIE3XWBD3LLQ3MZGOOG7N6M"),org.radixware.kernel.common.enums.EOrder.DESC),

											new org.radixware.kernel.server.meta.presentations.RadSortingDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("colELAJRXSM7NCG7FLQYSVADANYP4"),org.radixware.kernel.common.enums.EOrder.DESC),

											new org.radixware.kernel.server.meta.presentations.RadSortingDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEVE536NNOZBI7N7IOSAL5WOAIQ"),org.radixware.kernel.common.enums.EOrder.DESC)
									},"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:Sql>FIRST_ROWS(50) INDEX_DESC(</xsc:Sql></xsc:Item><xsc:Item><xsc:ThisTableSqlName/></xsc:Item><xsc:Item><xsc:Sql> </xsc:Sql></xsc:Item><xsc:Item><xsc:DbName Path=\"tblRHY4TVWIC5C4TJN2GKL2ABRKKQ idxYSAVBTX2LFA4JMOBS5J6FMDZLY\"/></xsc:Item><xsc:Item><xsc:Sql>)</xsc:Sql></xsc:Item></xsc:Sqml>","org.radixware")
							},
							/*Filters*/
							new org.radixware.kernel.server.meta.presentations.RadFilterDef[]{

									new org.radixware.kernel.server.meta.presentations.RadFilterDef(
									/*Radix::SystemMonitor::InstanceStateHistory:Period-Filter*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("fltUP3BGJU3RBGGTCTUYWEZESZ3EA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHY4TVWIC5C4TJN2GKL2ABRKKQ"),"Period",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRKMWHT7CPJH3PFAG322SJ4LFNY"),"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:PropSqlName TableId=\"tblRHY4TVWIC5C4TJN2GKL2ABRKKQ\" PropId=\"colELAJRXSM7NCG7FLQYSVADANYP4\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> between </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmNXMOS25C5ZB5XJPH4JGTQK23L4\"/></xsc:Item><xsc:Item><xsc:Sql> and </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prm2YKP7CT3JBGCNFO6SBZW2PJPYY\"/></xsc:Item><xsc:Item><xsc:Sql>\n</xsc:Sql></xsc:Item><xsc:Item><xsc:IfParam ParamId=\"prmE7W4HDTIKZCNHBYNU2VDOJB3UY\" Operation=\"NotNull\"/></xsc:Item><xsc:Item><xsc:Sql>\n    and </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecRHY4TVWIC5C4TJN2GKL2ABRKKQ\" PropId=\"colEVE536NNOZBI7N7IOSAL5WOAIQ\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> = </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmE7W4HDTIKZCNHBYNU2VDOJB3UY\"/></xsc:Item><xsc:Item><xsc:Sql>\n</xsc:Sql></xsc:Item><xsc:Item><xsc:EndIf/></xsc:Item><xsc:Item><xsc:Sql>\n</xsc:Sql></xsc:Item><xsc:Item><xsc:IfParam ParamId=\"prmWMXHTX47IBAPJMLZW23HZV7MKI\" Operation=\"NotNull\"/></xsc:Item><xsc:Item><xsc:Sql>\n    and </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecRHY4TVWIC5C4TJN2GKL2ABRKKQ\" PropId=\"col3KCBW5QZFREURM6WDQKR5JKHDQ\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> = </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmWMXHTX47IBAPJMLZW23HZV7MKI\"/></xsc:Item><xsc:Item><xsc:Sql>\n</xsc:Sql></xsc:Item><xsc:Item><xsc:EndIf/></xsc:Item><xsc:Item><xsc:Sql>\n</xsc:Sql></xsc:Item><xsc:Item><xsc:IfParam ParamId=\"prmROTPJ3XBMBHJJMV6HMAG6WBRRY\" Operation=\"NotEqual\"><xsc:Value/></xsc:IfParam></xsc:Item><xsc:Item><xsc:Sql>\n    and </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecRHY4TVWIC5C4TJN2GKL2ABRKKQ\" PropId=\"colJBVEDU3XFFC3LLANH2EAOMVKV4\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> = </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmROTPJ3XBMBHJJMV6HMAG6WBRRY\"/></xsc:Item><xsc:Item><xsc:Sql>\n</xsc:Sql></xsc:Item><xsc:Item><xsc:EndIf/></xsc:Item><xsc:Item><xsc:Sql>\n</xsc:Sql></xsc:Item><xsc:Item><xsc:IfParam ParamId=\"prmUWWMSPN7QNGAPCCDM2VV5NXS4A\" Operation=\"NotEqual\"><xsc:Value/></xsc:IfParam></xsc:Item><xsc:Item><xsc:Sql>\n    and </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecRHY4TVWIC5C4TJN2GKL2ABRKKQ\" PropId=\"colB4Y5IM6SMBHIVEYIGJSXMALEPQ\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> like \'%\' || </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmUWWMSPN7QNGAPCCDM2VV5NXS4A\"/></xsc:Item><xsc:Item><xsc:Sql> || \'%\'\n</xsc:Sql></xsc:Item><xsc:Item><xsc:EndIf/></xsc:Item></xsc:Sqml>","<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>",org.radixware.kernel.common.types.Id.Factory.loadFrom("srtUOSIXGY46JGFFOLWZFJSSYLAZY"),true,null,false,"org.radixware")
							},
							/*Editor presentations*/
							new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef[]{

									new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef(
									/*Radix::SystemMonitor::InstanceStateHistory:General-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("epr2ZU45DFBUFEPRHTN3QSLP7RAV4"),
									"General",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
									null,
									2192,
									null,

									/*Radix::SystemMonitor::InstanceStateHistory:General:Children-Explorer Items*/
										null
									,
									org.radixware.kernel.server.types.Restrictions.Factory.newInstance(0,null,null,null),
									(org.radixware.kernel.server.meta.presentations.RadPropertyPresentationAttributes[]) null,
									org.radixware.kernel.common.enums.EEditorPresentationRightsInheritanceMode.NONE,
									null,null)
							},
							/*Selector presentations*/
							new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef[]{

									new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef(
									/*Radix::SystemMonitor::InstanceStateHistory:General-Selector Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("sprZV4WYIIKSNGDRMXP5LUBQZ6XWY"),"General",null,144,new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn[]{

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIFISIE3XWBD3LLQ3MZGOOG7N6M"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdGAD64WTSOJEAPIDCFCJCN3PE5M"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colATYCTWD3AFBIVE3AS4TOYUPHSM"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4P6GRGWAKVF35LL2VSBKJRJ4O4"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEVE536NNOZBI7N7IOSAL5WOAIQ"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colB4Y5IM6SMBHIVEYIGJSXMALEPQ"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col66UBVRCJQFEWPDJXIWUQN2EQP4"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3KCBW5QZFREURM6WDQKR5JKHDQ"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colU4MHPYS6PVANJMDUSQ5W7RJCIY"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJBVEDU3XFFC3LLANH2EAOMVKV4"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLYPEWDCHDVBVVMMA4SFRKIKFW4"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col5UOR3QOSWNBPJCRIEZ7F7MZW3I"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNKGIWVA7PZFZXEPGNO5JPZQRV4"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colSMAJMWPRYBHHNCZF6WPLKUCGBQ"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHNUTRIYQXVA2FKJLMS25H2ZO54"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPZ5AOPCVBVHYFKD5O3MLHV75LY"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHNE7Z4ANKBECRGESFUUFQVCSQQ"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdO62TBSLFZZGXVFKTWKMATT5HQE"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3ENNZJ2XUVHWJABO3YCDY4WD2Y"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col62UUOQ3FM5C3VETVE73AV6T2JI"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col63DF4VG74ZEUXLPQGA2IHROCJY"),true)
									},new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.Addons(org.radixware.kernel.common.types.Id.Factory.loadFrom("srtUOSIXGY46JGFFOLWZFJSSYLAZY"),true,null,false,org.radixware.kernel.common.types.Id.Factory.loadFrom("fltUP3BGJU3RBGGTCTUYWEZESZ3EA"),true,null,false,true,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:Sql>FIRST_ROWS(50) INDEX_DESC(</xsc:Sql></xsc:Item><xsc:Item><xsc:ThisTableSqlName/></xsc:Item><xsc:Item><xsc:Sql> </xsc:Sql></xsc:Item><xsc:Item><xsc:DbName Path=\"tblRHY4TVWIC5C4TJN2GKL2ABRKKQ idxYSAVBTX2LFA4JMOBS5J6FMDZLY\"/></xsc:Item><xsc:Item><xsc:Sql>)</xsc:Sql></xsc:Item></xsc:Sqml>","org.radixware"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr2ZU45DFBUFEPRHTN3QSLP7RAV4")},null,org.radixware.kernel.server.types.Restrictions.Factory.newInstance(32,null,null,null),null),

									new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef(
									/*Radix::SystemMonitor::InstanceStateHistory:Instance-Selector Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("sprPWR74F5KQJH57IPCYXNIPARDT4"),"Instance",org.radixware.kernel.common.types.Id.Factory.loadFrom("sprZV4WYIIKSNGDRMXP5LUBQZ6XWY"),16569,new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn[]{

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIFISIE3XWBD3LLQ3MZGOOG7N6M"),false),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdGAD64WTSOJEAPIDCFCJCN3PE5M"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colATYCTWD3AFBIVE3AS4TOYUPHSM"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4P6GRGWAKVF35LL2VSBKJRJ4O4"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEVE536NNOZBI7N7IOSAL5WOAIQ"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colB4Y5IM6SMBHIVEYIGJSXMALEPQ"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col66UBVRCJQFEWPDJXIWUQN2EQP4"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3KCBW5QZFREURM6WDQKR5JKHDQ"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colU4MHPYS6PVANJMDUSQ5W7RJCIY"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJBVEDU3XFFC3LLANH2EAOMVKV4"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLYPEWDCHDVBVVMMA4SFRKIKFW4"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col5UOR3QOSWNBPJCRIEZ7F7MZW3I"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNKGIWVA7PZFZXEPGNO5JPZQRV4"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colSMAJMWPRYBHHNCZF6WPLKUCGBQ"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHNUTRIYQXVA2FKJLMS25H2ZO54"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPZ5AOPCVBVHYFKD5O3MLHV75LY"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHNE7Z4ANKBECRGESFUUFQVCSQQ"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdO62TBSLFZZGXVFKTWKMATT5HQE"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3ENNZJ2XUVHWJABO3YCDY4WD2Y"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col62UUOQ3FM5C3VETVE73AV6T2JI"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col63DF4VG74ZEUXLPQGA2IHROCJY"),true)
									},null,null,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr2ZU45DFBUFEPRHTN3QSLP7RAV4")},null,null,null),

									new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef(
									/*Radix::SystemMonitor::InstanceStateHistory:Arte-Selector Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("sprLIHXORNVCNH57M3VZOEXLJO3WY"),"Arte",org.radixware.kernel.common.types.Id.Factory.loadFrom("sprZV4WYIIKSNGDRMXP5LUBQZ6XWY"),16441,new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn[]{

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIFISIE3XWBD3LLQ3MZGOOG7N6M"),false),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdGAD64WTSOJEAPIDCFCJCN3PE5M"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colATYCTWD3AFBIVE3AS4TOYUPHSM"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4P6GRGWAKVF35LL2VSBKJRJ4O4"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEVE536NNOZBI7N7IOSAL5WOAIQ"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colB4Y5IM6SMBHIVEYIGJSXMALEPQ"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col66UBVRCJQFEWPDJXIWUQN2EQP4"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3KCBW5QZFREURM6WDQKR5JKHDQ"),false),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colU4MHPYS6PVANJMDUSQ5W7RJCIY"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJBVEDU3XFFC3LLANH2EAOMVKV4"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLYPEWDCHDVBVVMMA4SFRKIKFW4"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col5UOR3QOSWNBPJCRIEZ7F7MZW3I"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNKGIWVA7PZFZXEPGNO5JPZQRV4"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colSMAJMWPRYBHHNCZF6WPLKUCGBQ"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHNUTRIYQXVA2FKJLMS25H2ZO54"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPZ5AOPCVBVHYFKD5O3MLHV75LY"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHNE7Z4ANKBECRGESFUUFQVCSQQ"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdO62TBSLFZZGXVFKTWKMATT5HQE"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3ENNZJ2XUVHWJABO3YCDY4WD2Y"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col62UUOQ3FM5C3VETVE73AV6T2JI"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col63DF4VG74ZEUXLPQGA2IHROCJY"),true)
									},null,null,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr2ZU45DFBUFEPRHTN3QSLP7RAV4")},null,null,null)
							},
							/*Default selector presentation Id*/
							null,
							/*Presentation Map*/
							null,
							/*Object title format*/

							/*Radix::SystemMonitor::InstanceStateHistory:TitleFormat-Object Title Format*/

							new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHY4TVWIC5C4TJN2GKL2ABRKKQ"),null,null,org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.DefinitionContextType.ENTITY),
							/*Class catalogs*/
							null,
							/*Restrictions*/
							org.radixware.kernel.server.types.Restrictions.Factory.newInstance(28679,null,null,null),null,0),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblRHY4TVWIC5C4TJN2GKL2ABRKKQ"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("pdcEntity____________________"),

						/*Radix::SystemMonitor::InstanceStateHistory:Properties-Properties*/
						new org.radixware.kernel.server.meta.clazzes.RadPropDef[]{

								/*Radix::SystemMonitor::InstanceStateHistory:arteSeq-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3KCBW5QZFREURM6WDQKR5JKHDQ"),"arteSeq",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLMM2Q6AQHVAFRNKOTQ2S2ABORI"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::SystemMonitor::InstanceStateHistory:arteSerial-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colU4MHPYS6PVANJMDUSQ5W7RJCIY"),"arteSerial",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJGRIBFUJIVFNXHYLRI57G4B4R4"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::SystemMonitor::InstanceStateHistory:cpuDiffMillis-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNKGIWVA7PZFZXEPGNO5JPZQRV4"),"cpuDiffMillis",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsC2BF4MEY2BAU7O45G7AF643ZVA"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::SystemMonitor::InstanceStateHistory:dbDiffMillis-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colSMAJMWPRYBHHNCZF6WPLKUCGBQ"),"dbDiffMillis",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWPFHC4K63RC7XCA346WYXNGDYI"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::SystemMonitor::InstanceStateHistory:dbSerial-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLYPEWDCHDVBVVMMA4SFRKIKFW4"),"dbSerial",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRCUS43KE6ZEWLP7RL6KLPW7QUY"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::SystemMonitor::InstanceStateHistory:dbSid-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJBVEDU3XFFC3LLANH2EAOMVKV4"),"dbSid",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCTMEX26XRFCARHHAZPHD7YOX2Q"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::SystemMonitor::InstanceStateHistory:extData-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colI7V5NLFAUBA4HGY7VUF6JFLKRI"),"extData",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2XFJ4UTGMRBBBESLQIUOQOQ6DM"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::SystemMonitor::InstanceStateHistory:extDiffMillis-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHNUTRIYQXVA2FKJLMS25H2ZO54"),"extDiffMillis",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVWBLTNUUQFE2NKYZMQCDTPWX6M"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::SystemMonitor::InstanceStateHistory:forced-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colATYCTWD3AFBIVE3AS4TOYUPHSM"),"forced",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAANXTA3CCNGPXDIBUF2OPBVSPY"),org.radixware.kernel.common.enums.EValType.BOOL,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::SystemMonitor::InstanceStateHistory:instanceId-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIFISIE3XWBD3LLQ3MZGOOG7N6M"),"instanceId",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLYU2VUD56BGVFIS3NJC46ZM4ZM"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::SystemMonitor::InstanceStateHistory:lockName-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3ENNZJ2XUVHWJABO3YCDY4WD2Y"),"lockName",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAXCQEMVMXNFSJAXVBWBFQRV7QA"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::SystemMonitor::InstanceStateHistory:lockOwnerName-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col62UUOQ3FM5C3VETVE73AV6T2JI"),"lockOwnerName",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEUUJKFWJHVFB7DOT6N7VYL3NHE"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::SystemMonitor::InstanceStateHistory:name-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colB4Y5IM6SMBHIVEYIGJSXMALEPQ"),"name",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4FD2UHVTFRCATFQEA555O35FTI"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::SystemMonitor::InstanceStateHistory:queueDiffMillis-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPZ5AOPCVBVHYFKD5O3MLHV75LY"),"queueDiffMillis",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2QCWSEDDBVGRDKSJBNA2KRRHWI"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::SystemMonitor::InstanceStateHistory:regTimeMillis-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colELAJRXSM7NCG7FLQYSVADANYP4"),"regTimeMillis",null,org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::SystemMonitor::InstanceStateHistory:rqStartTimeMillis-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJFE25SOPHZDSXA3XU22NI623HQ"),"rqStartTimeMillis",null,org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::SystemMonitor::InstanceStateHistory:stackDigest-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col5NJRGYCSDZERZAQFIWHARWHNTQ"),"stackDigest",null,org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::SystemMonitor::InstanceStateHistory:threadId-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEVE536NNOZBI7N7IOSAL5WOAIQ"),"threadId",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSJKIP5BHMRDYXOJ3G7MODB3W4I"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::SystemMonitor::InstanceStateHistory:threadKind-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4P6GRGWAKVF35LL2VSBKJRJ4O4"),"threadKind",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQXL3HPSTQ5H3PPVG43X4G5AVBA"),org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("acsY3YBSE3JJVERJIKJXWM4X3E6UY"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::SystemMonitor::InstanceStateHistory:traceContexts-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col5UOR3QOSWNBPJCRIEZ7F7MZW3I"),"traceContexts",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAXTJP2SWKZHYPF4IKKDEZJFEPY"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::SystemMonitor::InstanceStateHistory:unitId-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col66UBVRCJQFEWPDJXIWUQN2EQP4"),"unitId",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNVTLSMGASNDBPBRT3A6FQRAOWM"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::SystemMonitor::InstanceStateHistory:uptimeSec-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHNE7Z4ANKBECRGESFUUFQVCSQQ"),"uptimeSec",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQOQPVZLAAVHTVIUP3QBWVNQH24"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::SystemMonitor::InstanceStateHistory:stackData-Parent Reference*/

								new org.radixware.kernel.server.meta.clazzes.RadInnateRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colG6CEIJ5GE5APXPGL422M4SPQMI"),"stackData",null,org.radixware.kernel.common.types.Id.Factory.loadFrom("refSNWJCJEAINHT7CN2I4NZQNAXXY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aec2R4PKYCFCVHGRP2YVVFOORGWIE"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::SystemMonitor::InstanceStateHistory:stack-Parent Property*/

								new org.radixware.kernel.server.meta.clazzes.RadParentPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colB6RB3PP5DBEHDIMUAG7LLMTTNU"),"stack",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6MPX4S5ZNZAIZM32KOGISEFG7Q"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colG6CEIJ5GE5APXPGL422M4SPQMI")},org.radixware.kernel.common.types.Id.Factory.loadFrom("prd222VBCWD3NE7FDJQTFYYZYZD2U"),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::SystemMonitor::InstanceStateHistory:regTime-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdGAD64WTSOJEAPIDCFCJCN3PE5M"),"regTime",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWXWFWBCK55GXRLHY53CO4BGALQ"),org.radixware.kernel.common.enums.EValType.DATE_TIME,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::SystemMonitor::InstanceStateHistory:rqStartTime-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdO62TBSLFZZGXVFKTWKMATT5HQE"),"rqStartTime",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQR7VBD44JNCVFG2VO77542ZJRY"),org.radixware.kernel.common.enums.EValType.DATE_TIME,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::SystemMonitor::InstanceStateHistory:stackTop-Parent Property*/

								new org.radixware.kernel.server.meta.clazzes.RadParentPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col63DF4VG74ZEUXLPQGA2IHROCJY"),"stackTop",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4D7KCUJ5OBG37HYW4C5W3VFW64"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colG6CEIJ5GE5APXPGL422M4SPQMI")},org.radixware.kernel.common.types.Id.Factory.loadFrom("colKLS76H6PRRGDTL2GZCCMYNFJXM"),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::SystemMonitor::InstanceStateHistory:ancestorThreadId-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKEUJLICU4JAIJHRZPG62HNGFGU"),"ancestorThreadId",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHA6ASVKG3JA75NKNYROR3MWDSE"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE)
						},

						/*Radix::SystemMonitor::InstanceStateHistory:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthROLNUCHEBVF57COIPCVVK4BRVU"),"exportToXml",false,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("writer",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprLRZMHIYOPVCEDCIC6ZYHVPML74"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthT7L4SZ5WCNAAFJNTZSU4JQQ6SY"),"exportToPlain",false,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("writer",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprLRZMHIYOPVCEDCIC6ZYHVPML74"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth2PCNXKTB2ZCQNC2SMQPXTIJ3KY"),"exportToHtml",false,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("writer",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprLRZMHIYOPVCEDCIC6ZYHVPML74"))
								},null)
						},
						null,
						org.radixware.kernel.common.enums.EAccessAreaType.NONE,null,null,false);
}

/* Radix::SystemMonitor::InstanceStateHistory - Desktop Executable*/

/*Radix::SystemMonitor::InstanceStateHistory-Entity Class*/

package org.radixware.ads.SystemMonitor.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory")
public interface InstanceStateHistory {
	/*Group model adapter class. This is the base class for 
	all of selector presentation models of this entity*/
	public static class DefaultGroupModel extends org.radixware.kernel.common.client.models.GroupModel{
		public DefaultGroupModel(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}





		public org.radixware.ads.SystemMonitor.explorer.InstanceStateHistory.InstanceStateHistory_DefaultModel getEntity(int i) throws InterruptedException, org.radixware.kernel.common.client.exceptions.BrokenEntityObjectException, org.radixware.kernel.common.exceptions.ServiceClientException { return (org.radixware.ads.SystemMonitor.explorer.InstanceStateHistory.InstanceStateHistory_DefaultModel )  super.getEntity(i);}
	}














































































































































	/*Radix::SystemMonitor::InstanceStateHistory:lockName:lockName-Presentation Property*/


	public class LockName extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public LockName(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:lockName:lockName")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:lockName:lockName")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public LockName getLockName();
	/*Radix::SystemMonitor::InstanceStateHistory:arteSeq:arteSeq-Presentation Property*/


	public class ArteSeq extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public ArteSeq(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:arteSeq:arteSeq")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:arteSeq:arteSeq")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public ArteSeq getArteSeq();
	/*Radix::SystemMonitor::InstanceStateHistory:threadKind:threadKind-Presentation Property*/


	public class ThreadKind extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public ThreadKind(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.kernel.common.enums.EInstanceThreadKind dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EInstanceThreadKind ? (org.radixware.kernel.common.enums.EInstanceThreadKind)x : org.radixware.kernel.common.enums.EInstanceThreadKind.getForValue((Str)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.STR)));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.kernel.common.enums.EInstanceThreadKind> getValClass(){
			return org.radixware.kernel.common.enums.EInstanceThreadKind.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.enums.EInstanceThreadKind dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EInstanceThreadKind ? (org.radixware.kernel.common.enums.EInstanceThreadKind)x : org.radixware.kernel.common.enums.EInstanceThreadKind.getForValue((Str)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.STR)));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:threadKind:threadKind")
		public  org.radixware.kernel.common.enums.EInstanceThreadKind getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:threadKind:threadKind")
		public   void setValue(org.radixware.kernel.common.enums.EInstanceThreadKind val) {
			Value = val;
		}
	}
	public ThreadKind getThreadKind();
	/*Radix::SystemMonitor::InstanceStateHistory:traceContexts:traceContexts-Presentation Property*/


	public class TraceContexts extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public TraceContexts(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:traceContexts:traceContexts")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:traceContexts:traceContexts")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public TraceContexts getTraceContexts();
	/*Radix::SystemMonitor::InstanceStateHistory:lockOwnerName:lockOwnerName-Presentation Property*/


	public class LockOwnerName extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public LockOwnerName(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:lockOwnerName:lockOwnerName")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:lockOwnerName:lockOwnerName")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public LockOwnerName getLockOwnerName();
	/*Radix::SystemMonitor::InstanceStateHistory:stackTop:stackTop-Presentation Property*/


	public class StackTop extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public StackTop(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:stackTop:stackTop")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:stackTop:stackTop")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public StackTop getStackTop();
	/*Radix::SystemMonitor::InstanceStateHistory:unitId:unitId-Presentation Property*/


	public class UnitId extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public UnitId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:unitId:unitId")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:unitId:unitId")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public UnitId getUnitId();
	/*Radix::SystemMonitor::InstanceStateHistory:forced:forced-Presentation Property*/


	public class Forced extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public Forced(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Bool dummy = ((Bool)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:forced:forced")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:forced:forced")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public Forced getForced();
	/*Radix::SystemMonitor::InstanceStateHistory:name:name-Presentation Property*/


	public class Name extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public Name(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:name:name")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:name:name")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Name getName();
	/*Radix::SystemMonitor::InstanceStateHistory:stack:stack-Presentation Property*/


	public class Stack extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public Stack(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:stack:stack")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:stack:stack")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Stack getStack();
	/*Radix::SystemMonitor::InstanceStateHistory:threadId:threadId-Presentation Property*/


	public class ThreadId extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public ThreadId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:threadId:threadId")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:threadId:threadId")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public ThreadId getThreadId();
	/*Radix::SystemMonitor::InstanceStateHistory:uptimeSec:uptimeSec-Presentation Property*/


	public class UptimeSec extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public UptimeSec(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:uptimeSec:uptimeSec")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:uptimeSec:uptimeSec")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public UptimeSec getUptimeSec();
	/*Radix::SystemMonitor::InstanceStateHistory:extDiffMillis:extDiffMillis-Presentation Property*/


	public class ExtDiffMillis extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public ExtDiffMillis(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:extDiffMillis:extDiffMillis")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:extDiffMillis:extDiffMillis")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public ExtDiffMillis getExtDiffMillis();
	/*Radix::SystemMonitor::InstanceStateHistory:extData:extData-Presentation Property*/


	public class ExtData extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public ExtData(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:extData:extData")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:extData:extData")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ExtData getExtData();
	/*Radix::SystemMonitor::InstanceStateHistory:instanceId:instanceId-Presentation Property*/


	public class InstanceId extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public InstanceId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:instanceId:instanceId")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:instanceId:instanceId")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public InstanceId getInstanceId();
	/*Radix::SystemMonitor::InstanceStateHistory:dbSid:dbSid-Presentation Property*/


	public class DbSid extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public DbSid(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:dbSid:dbSid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:dbSid:dbSid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public DbSid getDbSid();
	/*Radix::SystemMonitor::InstanceStateHistory:ancestorThreadId:ancestorThreadId-Presentation Property*/


	public class AncestorThreadId extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public AncestorThreadId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:ancestorThreadId:ancestorThreadId")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:ancestorThreadId:ancestorThreadId")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public AncestorThreadId getAncestorThreadId();
	/*Radix::SystemMonitor::InstanceStateHistory:dbSerial:dbSerial-Presentation Property*/


	public class DbSerial extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public DbSerial(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:dbSerial:dbSerial")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:dbSerial:dbSerial")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public DbSerial getDbSerial();
	/*Radix::SystemMonitor::InstanceStateHistory:cpuDiffMillis:cpuDiffMillis-Presentation Property*/


	public class CpuDiffMillis extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public CpuDiffMillis(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:cpuDiffMillis:cpuDiffMillis")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:cpuDiffMillis:cpuDiffMillis")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public CpuDiffMillis getCpuDiffMillis();
	/*Radix::SystemMonitor::InstanceStateHistory:queueDiffMillis:queueDiffMillis-Presentation Property*/


	public class QueueDiffMillis extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public QueueDiffMillis(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:queueDiffMillis:queueDiffMillis")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:queueDiffMillis:queueDiffMillis")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public QueueDiffMillis getQueueDiffMillis();
	/*Radix::SystemMonitor::InstanceStateHistory:dbDiffMillis:dbDiffMillis-Presentation Property*/


	public class DbDiffMillis extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public DbDiffMillis(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:dbDiffMillis:dbDiffMillis")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:dbDiffMillis:dbDiffMillis")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public DbDiffMillis getDbDiffMillis();
	/*Radix::SystemMonitor::InstanceStateHistory:arteSerial:arteSerial-Presentation Property*/


	public class ArteSerial extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public ArteSerial(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:arteSerial:arteSerial")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:arteSerial:arteSerial")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public ArteSerial getArteSerial();
	/*Radix::SystemMonitor::InstanceStateHistory:regTime:regTime-Presentation Property*/


	public class RegTime extends org.radixware.kernel.common.client.models.items.properties.PropertyDateTime{
		public RegTime(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			java.sql.Timestamp dummy = ((java.sql.Timestamp)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:regTime:regTime")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:regTime:regTime")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public RegTime getRegTime();
	/*Radix::SystemMonitor::InstanceStateHistory:rqStartTime:rqStartTime-Presentation Property*/


	public class RqStartTime extends org.radixware.kernel.common.client.models.items.properties.PropertyDateTime{
		public RqStartTime(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			java.sql.Timestamp dummy = ((java.sql.Timestamp)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:rqStartTime:rqStartTime")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:rqStartTime:rqStartTime")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public RqStartTime getRqStartTime();


}

/* Radix::SystemMonitor::InstanceStateHistory - Desktop Meta*/

/*Radix::SystemMonitor::InstanceStateHistory-Entity Class*/

package org.radixware.ads.SystemMonitor.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class InstanceStateHistory_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::SystemMonitor::InstanceStateHistory:Presentations-Entity Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHY4TVWIC5C4TJN2GKL2ABRKKQ"),
			"Radix::SystemMonitor::InstanceStateHistory",
			null,
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mls25Z2LCA2KFHWRH3LLGFWKJFXYU"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWPMI3QM6L5GWDEOXIDSST3XP64"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mls25Z2LCA2KFHWRH3LLGFWKJFXYU"),28679,

			/*Radix::SystemMonitor::InstanceStateHistory:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::SystemMonitor::InstanceStateHistory:arteSeq:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3KCBW5QZFREURM6WDQKR5JKHDQ"),
						"arteSeq",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLMM2Q6AQHVAFRNKOTQ2S2ABORI"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHY4TVWIC5C4TJN2GKL2ABRKKQ"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::SystemMonitor::InstanceStateHistory:arteSeq:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999999999999L,999999999999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYMTROZA43JCSNJYYLWO5U6SSIQ"),
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::SystemMonitor::InstanceStateHistory:arteSerial:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colU4MHPYS6PVANJMDUSQ5W7RJCIY"),
						"arteSerial",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJGRIBFUJIVFNXHYLRI57G4B4R4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHY4TVWIC5C4TJN2GKL2ABRKKQ"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::SystemMonitor::InstanceStateHistory:arteSerial:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999999999999L,999999999999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5W6YCXL5BRFETNGXEU5KVASSZU"),
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::SystemMonitor::InstanceStateHistory:cpuDiffMillis:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNKGIWVA7PZFZXEPGNO5JPZQRV4"),
						"cpuDiffMillis",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsC2BF4MEY2BAU7O45G7AF643ZVA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHY4TVWIC5C4TJN2GKL2ABRKKQ"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::SystemMonitor::InstanceStateHistory:cpuDiffMillis:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999999999999L,999999999999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::SystemMonitor::InstanceStateHistory:dbDiffMillis:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colSMAJMWPRYBHHNCZF6WPLKUCGBQ"),
						"dbDiffMillis",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWPFHC4K63RC7XCA346WYXNGDYI"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHY4TVWIC5C4TJN2GKL2ABRKKQ"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::SystemMonitor::InstanceStateHistory:dbDiffMillis:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999999999999L,999999999999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::SystemMonitor::InstanceStateHistory:dbSerial:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLYPEWDCHDVBVVMMA4SFRKIKFW4"),
						"dbSerial",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRCUS43KE6ZEWLP7RL6KLPW7QUY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHY4TVWIC5C4TJN2GKL2ABRKKQ"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::SystemMonitor::InstanceStateHistory:dbSerial:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,400,true),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJLUBYYOQQVF67AFKTUYGZSINOM"),
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::SystemMonitor::InstanceStateHistory:dbSid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJBVEDU3XFFC3LLANH2EAOMVKV4"),
						"dbSid",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCTMEX26XRFCARHHAZPHD7YOX2Q"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHY4TVWIC5C4TJN2GKL2ABRKKQ"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::SystemMonitor::InstanceStateHistory:dbSid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,400,true),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXEYCXRLNOJE4LD2525XZV3VNMI"),
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::SystemMonitor::InstanceStateHistory:extData:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colI7V5NLFAUBA4HGY7VUF6JFLKRI"),
						"extData",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2XFJ4UTGMRBBBESLQIUOQOQ6DM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHY4TVWIC5C4TJN2GKL2ABRKKQ"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::SystemMonitor::InstanceStateHistory:extData:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::SystemMonitor::InstanceStateHistory:extDiffMillis:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHNUTRIYQXVA2FKJLMS25H2ZO54"),
						"extDiffMillis",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVWBLTNUUQFE2NKYZMQCDTPWX6M"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHY4TVWIC5C4TJN2GKL2ABRKKQ"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::SystemMonitor::InstanceStateHistory:extDiffMillis:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999999999999L,999999999999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::SystemMonitor::InstanceStateHistory:forced:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colATYCTWD3AFBIVE3AS4TOYUPHSM"),
						"forced",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAANXTA3CCNGPXDIBUF2OPBVSPY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHY4TVWIC5C4TJN2GKL2ABRKKQ"),
						org.radixware.kernel.common.enums.EValType.BOOL,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::SystemMonitor::InstanceStateHistory:forced:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHY4TVWIC5C4TJN2GKL2ABRKKQ"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::SystemMonitor::InstanceStateHistory:instanceId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIFISIE3XWBD3LLQ3MZGOOG7N6M"),
						"instanceId",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLYU2VUD56BGVFIS3NJC46ZM4ZM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHY4TVWIC5C4TJN2GKL2ABRKKQ"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::SystemMonitor::InstanceStateHistory:instanceId:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::SystemMonitor::InstanceStateHistory:lockName:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3ENNZJ2XUVHWJABO3YCDY4WD2Y"),
						"lockName",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAXCQEMVMXNFSJAXVBWBFQRV7QA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHY4TVWIC5C4TJN2GKL2ABRKKQ"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::SystemMonitor::InstanceStateHistory:lockName:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,400,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::SystemMonitor::InstanceStateHistory:lockOwnerName:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col62UUOQ3FM5C3VETVE73AV6T2JI"),
						"lockOwnerName",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEUUJKFWJHVFB7DOT6N7VYL3NHE"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHY4TVWIC5C4TJN2GKL2ABRKKQ"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::SystemMonitor::InstanceStateHistory:lockOwnerName:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,400,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::SystemMonitor::InstanceStateHistory:name:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colB4Y5IM6SMBHIVEYIGJSXMALEPQ"),
						"name",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4FD2UHVTFRCATFQEA555O35FTI"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHY4TVWIC5C4TJN2GKL2ABRKKQ"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::SystemMonitor::InstanceStateHistory:name:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,400,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::SystemMonitor::InstanceStateHistory:queueDiffMillis:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPZ5AOPCVBVHYFKD5O3MLHV75LY"),
						"queueDiffMillis",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2QCWSEDDBVGRDKSJBNA2KRRHWI"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHY4TVWIC5C4TJN2GKL2ABRKKQ"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::SystemMonitor::InstanceStateHistory:queueDiffMillis:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999999999999L,999999999999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::SystemMonitor::InstanceStateHistory:threadId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEVE536NNOZBI7N7IOSAL5WOAIQ"),
						"threadId",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSJKIP5BHMRDYXOJ3G7MODB3W4I"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHY4TVWIC5C4TJN2GKL2ABRKKQ"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::SystemMonitor::InstanceStateHistory:threadId:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999999999999L,999999999999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::SystemMonitor::InstanceStateHistory:threadKind:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4P6GRGWAKVF35LL2VSBKJRJ4O4"),
						"threadKind",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQXL3HPSTQ5H3PPVG43X4G5AVBA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHY4TVWIC5C4TJN2GKL2ABRKKQ"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acsY3YBSE3JJVERJIKJXWM4X3E6UY"),
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::SystemMonitor::InstanceStateHistory:threadKind:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsY3YBSE3JJVERJIKJXWM4X3E6UY"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::SystemMonitor::InstanceStateHistory:traceContexts:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col5UOR3QOSWNBPJCRIEZ7F7MZW3I"),
						"traceContexts",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAXTJP2SWKZHYPF4IKKDEZJFEPY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHY4TVWIC5C4TJN2GKL2ABRKKQ"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::SystemMonitor::InstanceStateHistory:traceContexts:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::SystemMonitor::InstanceStateHistory:unitId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col66UBVRCJQFEWPDJXIWUQN2EQP4"),
						"unitId",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNVTLSMGASNDBPBRT3A6FQRAOWM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHY4TVWIC5C4TJN2GKL2ABRKKQ"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::SystemMonitor::InstanceStateHistory:unitId:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999999999999L,999999999999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::SystemMonitor::InstanceStateHistory:uptimeSec:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHNE7Z4ANKBECRGESFUUFQVCSQQ"),
						"uptimeSec",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQOQPVZLAAVHTVIUP3QBWVNQH24"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHY4TVWIC5C4TJN2GKL2ABRKKQ"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::SystemMonitor::InstanceStateHistory:uptimeSec:PropertyPresentation:Edit Options:-Edit Mask Time Interval*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskTimeInterval(1000L,"",null,null),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5RCRWTO4NNGZZOTIOCPD5KXVXM"),
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::SystemMonitor::InstanceStateHistory:stack:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colB6RB3PP5DBEHDIMUAG7LLMTTNU"),
						"stack",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6MPX4S5ZNZAIZM32KOGISEFG7Q"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHY4TVWIC5C4TJN2GKL2ABRKKQ"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.PARENT_PROP,
						60,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec2R4PKYCFCVHGRP2YVVFOORGWIE"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("prd222VBCWD3NE7FDJQTFYYZYZD2U"),
						null,
						true,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						true,
						null,
						false,

						/*Radix::SystemMonitor::InstanceStateHistory:stack:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						true,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::SystemMonitor::InstanceStateHistory:regTime:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdGAD64WTSOJEAPIDCFCJCN3PE5M"),
						"regTime",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWXWFWBCK55GXRLHY53CO4BGALQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHY4TVWIC5C4TJN2GKL2ABRKKQ"),
						org.radixware.kernel.common.enums.EValType.DATE_TIME,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::SystemMonitor::InstanceStateHistory:regTime:PropertyPresentation:Edit Options:-Edit Mask Date Time*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskDateTime(org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,null,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::SystemMonitor::InstanceStateHistory:rqStartTime:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdO62TBSLFZZGXVFKTWKMATT5HQE"),
						"rqStartTime",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQR7VBD44JNCVFG2VO77542ZJRY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHY4TVWIC5C4TJN2GKL2ABRKKQ"),
						org.radixware.kernel.common.enums.EValType.DATE_TIME,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::SystemMonitor::InstanceStateHistory:rqStartTime:PropertyPresentation:Edit Options:-Edit Mask Date Time*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskDateTime(org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,null,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::SystemMonitor::InstanceStateHistory:stackTop:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col63DF4VG74ZEUXLPQGA2IHROCJY"),
						"stackTop",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4D7KCUJ5OBG37HYW4C5W3VFW64"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHY4TVWIC5C4TJN2GKL2ABRKKQ"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.PARENT_PROP,
						60,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec2R4PKYCFCVHGRP2YVVFOORGWIE"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("colKLS76H6PRRGDTL2GZCCMYNFJXM"),
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						true,
						null,
						false,

						/*Radix::SystemMonitor::InstanceStateHistory:stackTop:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						true,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::SystemMonitor::InstanceStateHistory:ancestorThreadId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKEUJLICU4JAIJHRZPG62HNGFGU"),
						"ancestorThreadId",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHA6ASVKG3JA75NKNYROR3MWDSE"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHY4TVWIC5C4TJN2GKL2ABRKKQ"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::SystemMonitor::InstanceStateHistory:ancestorThreadId:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999999999999L,999999999999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false)
			},
			null,
			new org.radixware.kernel.common.client.meta.filters.RadFilterDef[]{

					new org.radixware.kernel.common.client.meta.filters.RadFilterDef(
					/*Radix::SystemMonitor::InstanceStateHistory:Period-Filter*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("fltUP3BGJU3RBGGTCTUYWEZESZ3EA"),
						"Period",
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHY4TVWIC5C4TJN2GKL2ABRKKQ"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRKMWHT7CPJH3PFAG322SJ4LFNY"),
						"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:PropSqlName TableId=\"tblRHY4TVWIC5C4TJN2GKL2ABRKKQ\" PropId=\"colELAJRXSM7NCG7FLQYSVADANYP4\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> between </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmNXMOS25C5ZB5XJPH4JGTQK23L4\"/></xsc:Item><xsc:Item><xsc:Sql> and </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prm2YKP7CT3JBGCNFO6SBZW2PJPYY\"/></xsc:Item><xsc:Item><xsc:Sql>\n</xsc:Sql></xsc:Item><xsc:Item><xsc:IfParam ParamId=\"prmE7W4HDTIKZCNHBYNU2VDOJB3UY\" Operation=\"NotNull\"/></xsc:Item><xsc:Item><xsc:Sql>\n    and </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecRHY4TVWIC5C4TJN2GKL2ABRKKQ\" PropId=\"colEVE536NNOZBI7N7IOSAL5WOAIQ\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> = </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmE7W4HDTIKZCNHBYNU2VDOJB3UY\"/></xsc:Item><xsc:Item><xsc:Sql>\n</xsc:Sql></xsc:Item><xsc:Item><xsc:EndIf/></xsc:Item><xsc:Item><xsc:Sql>\n</xsc:Sql></xsc:Item><xsc:Item><xsc:IfParam ParamId=\"prmWMXHTX47IBAPJMLZW23HZV7MKI\" Operation=\"NotNull\"/></xsc:Item><xsc:Item><xsc:Sql>\n    and </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecRHY4TVWIC5C4TJN2GKL2ABRKKQ\" PropId=\"col3KCBW5QZFREURM6WDQKR5JKHDQ\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> = </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmWMXHTX47IBAPJMLZW23HZV7MKI\"/></xsc:Item><xsc:Item><xsc:Sql>\n</xsc:Sql></xsc:Item><xsc:Item><xsc:EndIf/></xsc:Item><xsc:Item><xsc:Sql>\n</xsc:Sql></xsc:Item><xsc:Item><xsc:IfParam ParamId=\"prmROTPJ3XBMBHJJMV6HMAG6WBRRY\" Operation=\"NotEqual\"><xsc:Value/></xsc:IfParam></xsc:Item><xsc:Item><xsc:Sql>\n    and </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecRHY4TVWIC5C4TJN2GKL2ABRKKQ\" PropId=\"colJBVEDU3XFFC3LLANH2EAOMVKV4\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> = </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmROTPJ3XBMBHJJMV6HMAG6WBRRY\"/></xsc:Item><xsc:Item><xsc:Sql>\n</xsc:Sql></xsc:Item><xsc:Item><xsc:EndIf/></xsc:Item><xsc:Item><xsc:Sql>\n</xsc:Sql></xsc:Item><xsc:Item><xsc:IfParam ParamId=\"prmUWWMSPN7QNGAPCCDM2VV5NXS4A\" Operation=\"NotEqual\"><xsc:Value/></xsc:IfParam></xsc:Item><xsc:Item><xsc:Sql>\n    and </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecRHY4TVWIC5C4TJN2GKL2ABRKKQ\" PropId=\"colB4Y5IM6SMBHIVEYIGJSXMALEPQ\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> like \'%\' || </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmUWWMSPN7QNGAPCCDM2VV5NXS4A\"/></xsc:Item><xsc:Item><xsc:Sql> || \'%\'\n</xsc:Sql></xsc:Item><xsc:Item><xsc:EndIf/></xsc:Item></xsc:Sqml>",
						null,
						false,
						true,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("srtUOSIXGY46JGFFOLWZFJSSYLAZY"),
						new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef[]{

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmZPWMMLYX45FJXE3EWBDLU3VTAI"),
								"periodStart",
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQS6K7EXQ6JCTVFNGKPOOE2X24Q"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHY4TVWIC5C4TJN2GKL2ABRKKQ"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tblRHY4TVWIC5C4TJN2GKL2ABRKKQ"),
								null,
								org.radixware.kernel.common.enums.EValType.DATE_TIME,
								null,
								null,
								false,
								false,
								false,
								null,
								false,
								null,
								null,
								false,
								null,
								null),

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmYO7RVM4B3BDFFF6J2E5WX3N5UM"),
								"periodEnd",
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAJOIRGB7WFEW5JFP3IOHBG2VJM"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHY4TVWIC5C4TJN2GKL2ABRKKQ"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tblRHY4TVWIC5C4TJN2GKL2ABRKKQ"),
								null,
								org.radixware.kernel.common.enums.EValType.DATE_TIME,
								null,
								null,
								false,
								false,
								false,
								null,
								false,
								null,
								null,
								false,
								null,
								null),

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmUWWMSPN7QNGAPCCDM2VV5NXS4A"),
								"threadNameMask",
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2RU56LY3PZFGFNWU6CW4YBYD7U"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHY4TVWIC5C4TJN2GKL2ABRKKQ"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tblRHY4TVWIC5C4TJN2GKL2ABRKKQ"),
								null,
								org.radixware.kernel.common.enums.EValType.STR,
								null,
								null,
								false,
								false,
								false,
								null,
								false,

								/*Radix::SystemMonitor::InstanceStateHistory:Period:threadNameMask:Edit Options:-Edit Mask Str*/
								new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,400,true),
								null,
								false,
								null,
								null),

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmE7W4HDTIKZCNHBYNU2VDOJB3UY"),
								"threadId",
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsS6KOVFQWJJCZLOWACVB5DW3HOE"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHY4TVWIC5C4TJN2GKL2ABRKKQ"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tblRHY4TVWIC5C4TJN2GKL2ABRKKQ"),
								null,
								org.radixware.kernel.common.enums.EValType.INT,
								null,
								null,
								false,
								false,
								false,
								null,
								false,
								null,
								null,
								false,
								null,
								null),

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmROTPJ3XBMBHJJMV6HMAG6WBRRY"),
								"dbSid",
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWBN7TFP4Q5DCZL25LOXBSK4DOU"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHY4TVWIC5C4TJN2GKL2ABRKKQ"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tblRHY4TVWIC5C4TJN2GKL2ABRKKQ"),
								null,
								org.radixware.kernel.common.enums.EValType.STR,
								null,
								null,
								false,
								false,
								false,
								null,
								false,

								/*Radix::SystemMonitor::InstanceStateHistory:Period:dbSid:Edit Options:-Edit Mask Str*/
								new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,400,true),
								null,
								false,
								null,
								null),

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmWMXHTX47IBAPJMLZW23HZV7MKI"),
								"arteSeq",
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOEEM3JUUUNDHRLMHRHWW5WTTBI"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHY4TVWIC5C4TJN2GKL2ABRKKQ"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tblRHY4TVWIC5C4TJN2GKL2ABRKKQ"),
								null,
								org.radixware.kernel.common.enums.EValType.INT,
								null,
								null,
								false,
								false,
								false,
								null,
								false,
								null,
								null,
								false,
								null,
								null),

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmNXMOS25C5ZB5XJPH4JGTQK23L4"),
								"periodStartMillis",
								null,
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHY4TVWIC5C4TJN2GKL2ABRKKQ"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tblRHY4TVWIC5C4TJN2GKL2ABRKKQ"),
								null,
								org.radixware.kernel.common.enums.EValType.INT,
								null,
								null,
								false,
								false,
								false,
								null,
								false,
								null,
								null,
								false,
								null,
								null),

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prm2YKP7CT3JBGCNFO6SBZW2PJPYY"),
								"periodEndMillis",
								null,
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHY4TVWIC5C4TJN2GKL2ABRKKQ"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tblRHY4TVWIC5C4TJN2GKL2ABRKKQ"),
								null,
								org.radixware.kernel.common.enums.EValType.INT,
								null,
								null,
								false,
								false,
								false,
								null,
								false,
								null,
								null,
								false,
								null,
								null)
						},

						/*Radix::SystemMonitor::InstanceStateHistory:Period:Editor Pages-Filter Pages*/
						null,
						new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
						}
						,
						null)
			},
			new org.radixware.kernel.common.client.meta.RadSortingDef[]{

					new org.radixware.kernel.common.client.meta.RadSortingDef(
					/*Radix::SystemMonitor::InstanceStateHistory:RegTimeAsc-Sorting*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("srtR7SYZPQQZFBELKVU454IIOO3VY"),
						"RegTimeAsc",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHY4TVWIC5C4TJN2GKL2ABRKKQ"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsL6GKKVCRTVCKBGM2GBLMY4OYM4"),
						new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem[]{

								new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIFISIE3XWBD3LLQ3MZGOOG7N6M"),
								false),

								new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colELAJRXSM7NCG7FLQYSVADANYP4"),
								false),

								new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEVE536NNOZBI7N7IOSAL5WOAIQ"),
								false)
						}),

					new org.radixware.kernel.common.client.meta.RadSortingDef(
					/*Radix::SystemMonitor::InstanceStateHistory:RegTimeDesc-Sorting*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("srtUOSIXGY46JGFFOLWZFJSSYLAZY"),
						"RegTimeDesc",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHY4TVWIC5C4TJN2GKL2ABRKKQ"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDXOO3S2PIFHUJGSM6QAY7J4T54"),
						new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem[]{

								new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIFISIE3XWBD3LLQ3MZGOOG7N6M"),
								true),

								new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colELAJRXSM7NCG7FLQYSVADANYP4"),
								true),

								new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEVE536NNOZBI7N7IOSAL5WOAIQ"),
								true)
						})
			},
			new org.radixware.kernel.common.client.meta.RadReferenceDef[]{

					new org.radixware.kernel.common.client.meta.RadReferenceDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("refSNWJCJEAINHT7CN2I4NZQNAXXY"),"InstanceStateHistory=>StackData (stackDigest=>digest)",org.radixware.kernel.common.types.Id.Factory.loadFrom("tblRHY4TVWIC5C4TJN2GKL2ABRKKQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl2R4PKYCFCVHGRP2YVVFOORGWIE"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("col5NJRGYCSDZERZAQFIWHARWHNTQ")},new String[]{"stackDigest"},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colIAUYIKCLEBHP5MU4ELPXVEQWJU")},new String[]{"digest"})
			},
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr2ZU45DFBUFEPRHTN3QSLP7RAV4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("sprZV4WYIIKSNGDRMXP5LUBQZ6XWY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("sprPWR74F5KQJH57IPCYXNIPARDT4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("sprLIHXORNVCNH57M3VZOEXLJO3WY")},
			false,false,false);
}

/* Radix::SystemMonitor::InstanceStateHistory - Web Executable*/

/*Radix::SystemMonitor::InstanceStateHistory-Entity Class*/

package org.radixware.ads.SystemMonitor.web;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory")
public interface InstanceStateHistory {
	/*Group model adapter class. This is the base class for 
	all of selector presentation models of this entity*/
	public static class DefaultGroupModel extends org.radixware.kernel.common.client.models.GroupModel{
		public DefaultGroupModel(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}





		public org.radixware.ads.SystemMonitor.web.InstanceStateHistory.InstanceStateHistory_DefaultModel getEntity(int i) throws InterruptedException, org.radixware.kernel.common.client.exceptions.BrokenEntityObjectException, org.radixware.kernel.common.exceptions.ServiceClientException { return (org.radixware.ads.SystemMonitor.web.InstanceStateHistory.InstanceStateHistory_DefaultModel )  super.getEntity(i);}
	}














































































































































	/*Radix::SystemMonitor::InstanceStateHistory:lockName:lockName-Presentation Property*/


	public class LockName extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public LockName(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:lockName:lockName")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:lockName:lockName")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public LockName getLockName();
	/*Radix::SystemMonitor::InstanceStateHistory:arteSeq:arteSeq-Presentation Property*/


	public class ArteSeq extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public ArteSeq(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:arteSeq:arteSeq")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:arteSeq:arteSeq")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public ArteSeq getArteSeq();
	/*Radix::SystemMonitor::InstanceStateHistory:threadKind:threadKind-Presentation Property*/


	public class ThreadKind extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public ThreadKind(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.kernel.common.enums.EInstanceThreadKind dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EInstanceThreadKind ? (org.radixware.kernel.common.enums.EInstanceThreadKind)x : org.radixware.kernel.common.enums.EInstanceThreadKind.getForValue((Str)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.STR)));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.kernel.common.enums.EInstanceThreadKind> getValClass(){
			return org.radixware.kernel.common.enums.EInstanceThreadKind.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.enums.EInstanceThreadKind dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EInstanceThreadKind ? (org.radixware.kernel.common.enums.EInstanceThreadKind)x : org.radixware.kernel.common.enums.EInstanceThreadKind.getForValue((Str)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.STR)));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:threadKind:threadKind")
		public  org.radixware.kernel.common.enums.EInstanceThreadKind getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:threadKind:threadKind")
		public   void setValue(org.radixware.kernel.common.enums.EInstanceThreadKind val) {
			Value = val;
		}
	}
	public ThreadKind getThreadKind();
	/*Radix::SystemMonitor::InstanceStateHistory:traceContexts:traceContexts-Presentation Property*/


	public class TraceContexts extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public TraceContexts(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:traceContexts:traceContexts")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:traceContexts:traceContexts")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public TraceContexts getTraceContexts();
	/*Radix::SystemMonitor::InstanceStateHistory:lockOwnerName:lockOwnerName-Presentation Property*/


	public class LockOwnerName extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public LockOwnerName(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:lockOwnerName:lockOwnerName")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:lockOwnerName:lockOwnerName")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public LockOwnerName getLockOwnerName();
	/*Radix::SystemMonitor::InstanceStateHistory:stackTop:stackTop-Presentation Property*/


	public class StackTop extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public StackTop(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:stackTop:stackTop")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:stackTop:stackTop")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public StackTop getStackTop();
	/*Radix::SystemMonitor::InstanceStateHistory:unitId:unitId-Presentation Property*/


	public class UnitId extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public UnitId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:unitId:unitId")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:unitId:unitId")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public UnitId getUnitId();
	/*Radix::SystemMonitor::InstanceStateHistory:forced:forced-Presentation Property*/


	public class Forced extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public Forced(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Bool dummy = ((Bool)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:forced:forced")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:forced:forced")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public Forced getForced();
	/*Radix::SystemMonitor::InstanceStateHistory:name:name-Presentation Property*/


	public class Name extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public Name(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:name:name")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:name:name")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Name getName();
	/*Radix::SystemMonitor::InstanceStateHistory:stack:stack-Presentation Property*/


	public class Stack extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public Stack(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:stack:stack")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:stack:stack")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Stack getStack();
	/*Radix::SystemMonitor::InstanceStateHistory:threadId:threadId-Presentation Property*/


	public class ThreadId extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public ThreadId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:threadId:threadId")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:threadId:threadId")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public ThreadId getThreadId();
	/*Radix::SystemMonitor::InstanceStateHistory:uptimeSec:uptimeSec-Presentation Property*/


	public class UptimeSec extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public UptimeSec(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:uptimeSec:uptimeSec")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:uptimeSec:uptimeSec")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public UptimeSec getUptimeSec();
	/*Radix::SystemMonitor::InstanceStateHistory:extDiffMillis:extDiffMillis-Presentation Property*/


	public class ExtDiffMillis extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public ExtDiffMillis(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:extDiffMillis:extDiffMillis")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:extDiffMillis:extDiffMillis")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public ExtDiffMillis getExtDiffMillis();
	/*Radix::SystemMonitor::InstanceStateHistory:extData:extData-Presentation Property*/


	public class ExtData extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public ExtData(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:extData:extData")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:extData:extData")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ExtData getExtData();
	/*Radix::SystemMonitor::InstanceStateHistory:instanceId:instanceId-Presentation Property*/


	public class InstanceId extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public InstanceId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:instanceId:instanceId")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:instanceId:instanceId")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public InstanceId getInstanceId();
	/*Radix::SystemMonitor::InstanceStateHistory:dbSid:dbSid-Presentation Property*/


	public class DbSid extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public DbSid(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:dbSid:dbSid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:dbSid:dbSid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public DbSid getDbSid();
	/*Radix::SystemMonitor::InstanceStateHistory:ancestorThreadId:ancestorThreadId-Presentation Property*/


	public class AncestorThreadId extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public AncestorThreadId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:ancestorThreadId:ancestorThreadId")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:ancestorThreadId:ancestorThreadId")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public AncestorThreadId getAncestorThreadId();
	/*Radix::SystemMonitor::InstanceStateHistory:dbSerial:dbSerial-Presentation Property*/


	public class DbSerial extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public DbSerial(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:dbSerial:dbSerial")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:dbSerial:dbSerial")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public DbSerial getDbSerial();
	/*Radix::SystemMonitor::InstanceStateHistory:cpuDiffMillis:cpuDiffMillis-Presentation Property*/


	public class CpuDiffMillis extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public CpuDiffMillis(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:cpuDiffMillis:cpuDiffMillis")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:cpuDiffMillis:cpuDiffMillis")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public CpuDiffMillis getCpuDiffMillis();
	/*Radix::SystemMonitor::InstanceStateHistory:queueDiffMillis:queueDiffMillis-Presentation Property*/


	public class QueueDiffMillis extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public QueueDiffMillis(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:queueDiffMillis:queueDiffMillis")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:queueDiffMillis:queueDiffMillis")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public QueueDiffMillis getQueueDiffMillis();
	/*Radix::SystemMonitor::InstanceStateHistory:dbDiffMillis:dbDiffMillis-Presentation Property*/


	public class DbDiffMillis extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public DbDiffMillis(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:dbDiffMillis:dbDiffMillis")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:dbDiffMillis:dbDiffMillis")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public DbDiffMillis getDbDiffMillis();
	/*Radix::SystemMonitor::InstanceStateHistory:arteSerial:arteSerial-Presentation Property*/


	public class ArteSerial extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public ArteSerial(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:arteSerial:arteSerial")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:arteSerial:arteSerial")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public ArteSerial getArteSerial();
	/*Radix::SystemMonitor::InstanceStateHistory:regTime:regTime-Presentation Property*/


	public class RegTime extends org.radixware.kernel.common.client.models.items.properties.PropertyDateTime{
		public RegTime(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			java.sql.Timestamp dummy = ((java.sql.Timestamp)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:regTime:regTime")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:regTime:regTime")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public RegTime getRegTime();
	/*Radix::SystemMonitor::InstanceStateHistory:rqStartTime:rqStartTime-Presentation Property*/


	public class RqStartTime extends org.radixware.kernel.common.client.models.items.properties.PropertyDateTime{
		public RqStartTime(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			java.sql.Timestamp dummy = ((java.sql.Timestamp)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:rqStartTime:rqStartTime")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:rqStartTime:rqStartTime")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public RqStartTime getRqStartTime();


}

/* Radix::SystemMonitor::InstanceStateHistory - Web Meta*/

/*Radix::SystemMonitor::InstanceStateHistory-Entity Class*/

package org.radixware.ads.SystemMonitor.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class InstanceStateHistory_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::SystemMonitor::InstanceStateHistory:Presentations-Entity Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHY4TVWIC5C4TJN2GKL2ABRKKQ"),
			"Radix::SystemMonitor::InstanceStateHistory",
			null,
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mls25Z2LCA2KFHWRH3LLGFWKJFXYU"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWPMI3QM6L5GWDEOXIDSST3XP64"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mls25Z2LCA2KFHWRH3LLGFWKJFXYU"),28679,

			/*Radix::SystemMonitor::InstanceStateHistory:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::SystemMonitor::InstanceStateHistory:arteSeq:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3KCBW5QZFREURM6WDQKR5JKHDQ"),
						"arteSeq",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLMM2Q6AQHVAFRNKOTQ2S2ABORI"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHY4TVWIC5C4TJN2GKL2ABRKKQ"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::SystemMonitor::InstanceStateHistory:arteSeq:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999999999999L,999999999999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYMTROZA43JCSNJYYLWO5U6SSIQ"),
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::SystemMonitor::InstanceStateHistory:arteSerial:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colU4MHPYS6PVANJMDUSQ5W7RJCIY"),
						"arteSerial",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJGRIBFUJIVFNXHYLRI57G4B4R4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHY4TVWIC5C4TJN2GKL2ABRKKQ"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::SystemMonitor::InstanceStateHistory:arteSerial:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999999999999L,999999999999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5W6YCXL5BRFETNGXEU5KVASSZU"),
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::SystemMonitor::InstanceStateHistory:cpuDiffMillis:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNKGIWVA7PZFZXEPGNO5JPZQRV4"),
						"cpuDiffMillis",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsC2BF4MEY2BAU7O45G7AF643ZVA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHY4TVWIC5C4TJN2GKL2ABRKKQ"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::SystemMonitor::InstanceStateHistory:cpuDiffMillis:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999999999999L,999999999999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::SystemMonitor::InstanceStateHistory:dbDiffMillis:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colSMAJMWPRYBHHNCZF6WPLKUCGBQ"),
						"dbDiffMillis",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWPFHC4K63RC7XCA346WYXNGDYI"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHY4TVWIC5C4TJN2GKL2ABRKKQ"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::SystemMonitor::InstanceStateHistory:dbDiffMillis:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999999999999L,999999999999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::SystemMonitor::InstanceStateHistory:dbSerial:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLYPEWDCHDVBVVMMA4SFRKIKFW4"),
						"dbSerial",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRCUS43KE6ZEWLP7RL6KLPW7QUY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHY4TVWIC5C4TJN2GKL2ABRKKQ"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::SystemMonitor::InstanceStateHistory:dbSerial:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,400,true),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJLUBYYOQQVF67AFKTUYGZSINOM"),
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::SystemMonitor::InstanceStateHistory:dbSid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJBVEDU3XFFC3LLANH2EAOMVKV4"),
						"dbSid",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCTMEX26XRFCARHHAZPHD7YOX2Q"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHY4TVWIC5C4TJN2GKL2ABRKKQ"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::SystemMonitor::InstanceStateHistory:dbSid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,400,true),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXEYCXRLNOJE4LD2525XZV3VNMI"),
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::SystemMonitor::InstanceStateHistory:extData:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colI7V5NLFAUBA4HGY7VUF6JFLKRI"),
						"extData",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2XFJ4UTGMRBBBESLQIUOQOQ6DM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHY4TVWIC5C4TJN2GKL2ABRKKQ"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::SystemMonitor::InstanceStateHistory:extData:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::SystemMonitor::InstanceStateHistory:extDiffMillis:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHNUTRIYQXVA2FKJLMS25H2ZO54"),
						"extDiffMillis",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVWBLTNUUQFE2NKYZMQCDTPWX6M"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHY4TVWIC5C4TJN2GKL2ABRKKQ"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::SystemMonitor::InstanceStateHistory:extDiffMillis:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999999999999L,999999999999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::SystemMonitor::InstanceStateHistory:forced:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colATYCTWD3AFBIVE3AS4TOYUPHSM"),
						"forced",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAANXTA3CCNGPXDIBUF2OPBVSPY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHY4TVWIC5C4TJN2GKL2ABRKKQ"),
						org.radixware.kernel.common.enums.EValType.BOOL,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::SystemMonitor::InstanceStateHistory:forced:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHY4TVWIC5C4TJN2GKL2ABRKKQ"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::SystemMonitor::InstanceStateHistory:instanceId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIFISIE3XWBD3LLQ3MZGOOG7N6M"),
						"instanceId",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLYU2VUD56BGVFIS3NJC46ZM4ZM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHY4TVWIC5C4TJN2GKL2ABRKKQ"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::SystemMonitor::InstanceStateHistory:instanceId:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::SystemMonitor::InstanceStateHistory:lockName:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3ENNZJ2XUVHWJABO3YCDY4WD2Y"),
						"lockName",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAXCQEMVMXNFSJAXVBWBFQRV7QA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHY4TVWIC5C4TJN2GKL2ABRKKQ"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::SystemMonitor::InstanceStateHistory:lockName:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,400,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::SystemMonitor::InstanceStateHistory:lockOwnerName:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col62UUOQ3FM5C3VETVE73AV6T2JI"),
						"lockOwnerName",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEUUJKFWJHVFB7DOT6N7VYL3NHE"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHY4TVWIC5C4TJN2GKL2ABRKKQ"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::SystemMonitor::InstanceStateHistory:lockOwnerName:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,400,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::SystemMonitor::InstanceStateHistory:name:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colB4Y5IM6SMBHIVEYIGJSXMALEPQ"),
						"name",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4FD2UHVTFRCATFQEA555O35FTI"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHY4TVWIC5C4TJN2GKL2ABRKKQ"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::SystemMonitor::InstanceStateHistory:name:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,400,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::SystemMonitor::InstanceStateHistory:queueDiffMillis:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPZ5AOPCVBVHYFKD5O3MLHV75LY"),
						"queueDiffMillis",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2QCWSEDDBVGRDKSJBNA2KRRHWI"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHY4TVWIC5C4TJN2GKL2ABRKKQ"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::SystemMonitor::InstanceStateHistory:queueDiffMillis:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999999999999L,999999999999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::SystemMonitor::InstanceStateHistory:threadId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEVE536NNOZBI7N7IOSAL5WOAIQ"),
						"threadId",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSJKIP5BHMRDYXOJ3G7MODB3W4I"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHY4TVWIC5C4TJN2GKL2ABRKKQ"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::SystemMonitor::InstanceStateHistory:threadId:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999999999999L,999999999999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::SystemMonitor::InstanceStateHistory:threadKind:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4P6GRGWAKVF35LL2VSBKJRJ4O4"),
						"threadKind",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQXL3HPSTQ5H3PPVG43X4G5AVBA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHY4TVWIC5C4TJN2GKL2ABRKKQ"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acsY3YBSE3JJVERJIKJXWM4X3E6UY"),
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::SystemMonitor::InstanceStateHistory:threadKind:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsY3YBSE3JJVERJIKJXWM4X3E6UY"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::SystemMonitor::InstanceStateHistory:traceContexts:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col5UOR3QOSWNBPJCRIEZ7F7MZW3I"),
						"traceContexts",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAXTJP2SWKZHYPF4IKKDEZJFEPY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHY4TVWIC5C4TJN2GKL2ABRKKQ"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::SystemMonitor::InstanceStateHistory:traceContexts:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::SystemMonitor::InstanceStateHistory:unitId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col66UBVRCJQFEWPDJXIWUQN2EQP4"),
						"unitId",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNVTLSMGASNDBPBRT3A6FQRAOWM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHY4TVWIC5C4TJN2GKL2ABRKKQ"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::SystemMonitor::InstanceStateHistory:unitId:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999999999999L,999999999999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::SystemMonitor::InstanceStateHistory:uptimeSec:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHNE7Z4ANKBECRGESFUUFQVCSQQ"),
						"uptimeSec",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQOQPVZLAAVHTVIUP3QBWVNQH24"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHY4TVWIC5C4TJN2GKL2ABRKKQ"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::SystemMonitor::InstanceStateHistory:uptimeSec:PropertyPresentation:Edit Options:-Edit Mask Time Interval*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskTimeInterval(1000L,"",null,null),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5RCRWTO4NNGZZOTIOCPD5KXVXM"),
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::SystemMonitor::InstanceStateHistory:stack:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colB6RB3PP5DBEHDIMUAG7LLMTTNU"),
						"stack",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6MPX4S5ZNZAIZM32KOGISEFG7Q"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHY4TVWIC5C4TJN2GKL2ABRKKQ"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.PARENT_PROP,
						60,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec2R4PKYCFCVHGRP2YVVFOORGWIE"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("prd222VBCWD3NE7FDJQTFYYZYZD2U"),
						null,
						true,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						true,
						null,
						false,

						/*Radix::SystemMonitor::InstanceStateHistory:stack:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						true,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::SystemMonitor::InstanceStateHistory:regTime:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdGAD64WTSOJEAPIDCFCJCN3PE5M"),
						"regTime",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWXWFWBCK55GXRLHY53CO4BGALQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHY4TVWIC5C4TJN2GKL2ABRKKQ"),
						org.radixware.kernel.common.enums.EValType.DATE_TIME,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::SystemMonitor::InstanceStateHistory:regTime:PropertyPresentation:Edit Options:-Edit Mask Date Time*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskDateTime(org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,null,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::SystemMonitor::InstanceStateHistory:rqStartTime:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdO62TBSLFZZGXVFKTWKMATT5HQE"),
						"rqStartTime",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQR7VBD44JNCVFG2VO77542ZJRY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHY4TVWIC5C4TJN2GKL2ABRKKQ"),
						org.radixware.kernel.common.enums.EValType.DATE_TIME,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::SystemMonitor::InstanceStateHistory:rqStartTime:PropertyPresentation:Edit Options:-Edit Mask Date Time*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskDateTime(org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,null,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::SystemMonitor::InstanceStateHistory:stackTop:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col63DF4VG74ZEUXLPQGA2IHROCJY"),
						"stackTop",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4D7KCUJ5OBG37HYW4C5W3VFW64"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHY4TVWIC5C4TJN2GKL2ABRKKQ"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.PARENT_PROP,
						60,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec2R4PKYCFCVHGRP2YVVFOORGWIE"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("colKLS76H6PRRGDTL2GZCCMYNFJXM"),
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						true,
						null,
						false,

						/*Radix::SystemMonitor::InstanceStateHistory:stackTop:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						true,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::SystemMonitor::InstanceStateHistory:ancestorThreadId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKEUJLICU4JAIJHRZPG62HNGFGU"),
						"ancestorThreadId",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHA6ASVKG3JA75NKNYROR3MWDSE"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHY4TVWIC5C4TJN2GKL2ABRKKQ"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::SystemMonitor::InstanceStateHistory:ancestorThreadId:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999999999999L,999999999999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false)
			},
			null,
			new org.radixware.kernel.common.client.meta.filters.RadFilterDef[]{

					new org.radixware.kernel.common.client.meta.filters.RadFilterDef(
					/*Radix::SystemMonitor::InstanceStateHistory:Period-Filter*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("fltUP3BGJU3RBGGTCTUYWEZESZ3EA"),
						"Period",
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHY4TVWIC5C4TJN2GKL2ABRKKQ"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRKMWHT7CPJH3PFAG322SJ4LFNY"),
						"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:PropSqlName TableId=\"tblRHY4TVWIC5C4TJN2GKL2ABRKKQ\" PropId=\"colELAJRXSM7NCG7FLQYSVADANYP4\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> between </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmNXMOS25C5ZB5XJPH4JGTQK23L4\"/></xsc:Item><xsc:Item><xsc:Sql> and </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prm2YKP7CT3JBGCNFO6SBZW2PJPYY\"/></xsc:Item><xsc:Item><xsc:Sql>\n</xsc:Sql></xsc:Item><xsc:Item><xsc:IfParam ParamId=\"prmE7W4HDTIKZCNHBYNU2VDOJB3UY\" Operation=\"NotNull\"/></xsc:Item><xsc:Item><xsc:Sql>\n    and </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecRHY4TVWIC5C4TJN2GKL2ABRKKQ\" PropId=\"colEVE536NNOZBI7N7IOSAL5WOAIQ\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> = </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmE7W4HDTIKZCNHBYNU2VDOJB3UY\"/></xsc:Item><xsc:Item><xsc:Sql>\n</xsc:Sql></xsc:Item><xsc:Item><xsc:EndIf/></xsc:Item><xsc:Item><xsc:Sql>\n</xsc:Sql></xsc:Item><xsc:Item><xsc:IfParam ParamId=\"prmWMXHTX47IBAPJMLZW23HZV7MKI\" Operation=\"NotNull\"/></xsc:Item><xsc:Item><xsc:Sql>\n    and </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecRHY4TVWIC5C4TJN2GKL2ABRKKQ\" PropId=\"col3KCBW5QZFREURM6WDQKR5JKHDQ\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> = </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmWMXHTX47IBAPJMLZW23HZV7MKI\"/></xsc:Item><xsc:Item><xsc:Sql>\n</xsc:Sql></xsc:Item><xsc:Item><xsc:EndIf/></xsc:Item><xsc:Item><xsc:Sql>\n</xsc:Sql></xsc:Item><xsc:Item><xsc:IfParam ParamId=\"prmROTPJ3XBMBHJJMV6HMAG6WBRRY\" Operation=\"NotEqual\"><xsc:Value/></xsc:IfParam></xsc:Item><xsc:Item><xsc:Sql>\n    and </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecRHY4TVWIC5C4TJN2GKL2ABRKKQ\" PropId=\"colJBVEDU3XFFC3LLANH2EAOMVKV4\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> = </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmROTPJ3XBMBHJJMV6HMAG6WBRRY\"/></xsc:Item><xsc:Item><xsc:Sql>\n</xsc:Sql></xsc:Item><xsc:Item><xsc:EndIf/></xsc:Item><xsc:Item><xsc:Sql>\n</xsc:Sql></xsc:Item><xsc:Item><xsc:IfParam ParamId=\"prmUWWMSPN7QNGAPCCDM2VV5NXS4A\" Operation=\"NotEqual\"><xsc:Value/></xsc:IfParam></xsc:Item><xsc:Item><xsc:Sql>\n    and </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecRHY4TVWIC5C4TJN2GKL2ABRKKQ\" PropId=\"colB4Y5IM6SMBHIVEYIGJSXMALEPQ\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> like \'%\' || </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmUWWMSPN7QNGAPCCDM2VV5NXS4A\"/></xsc:Item><xsc:Item><xsc:Sql> || \'%\'\n</xsc:Sql></xsc:Item><xsc:Item><xsc:EndIf/></xsc:Item></xsc:Sqml>",
						null,
						false,
						true,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("srtUOSIXGY46JGFFOLWZFJSSYLAZY"),
						new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef[]{

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmZPWMMLYX45FJXE3EWBDLU3VTAI"),
								"periodStart",
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQS6K7EXQ6JCTVFNGKPOOE2X24Q"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHY4TVWIC5C4TJN2GKL2ABRKKQ"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tblRHY4TVWIC5C4TJN2GKL2ABRKKQ"),
								null,
								org.radixware.kernel.common.enums.EValType.DATE_TIME,
								null,
								null,
								false,
								false,
								false,
								null,
								false,
								null,
								null,
								false,
								null,
								null),

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmYO7RVM4B3BDFFF6J2E5WX3N5UM"),
								"periodEnd",
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAJOIRGB7WFEW5JFP3IOHBG2VJM"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHY4TVWIC5C4TJN2GKL2ABRKKQ"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tblRHY4TVWIC5C4TJN2GKL2ABRKKQ"),
								null,
								org.radixware.kernel.common.enums.EValType.DATE_TIME,
								null,
								null,
								false,
								false,
								false,
								null,
								false,
								null,
								null,
								false,
								null,
								null),

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmUWWMSPN7QNGAPCCDM2VV5NXS4A"),
								"threadNameMask",
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2RU56LY3PZFGFNWU6CW4YBYD7U"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHY4TVWIC5C4TJN2GKL2ABRKKQ"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tblRHY4TVWIC5C4TJN2GKL2ABRKKQ"),
								null,
								org.radixware.kernel.common.enums.EValType.STR,
								null,
								null,
								false,
								false,
								false,
								null,
								false,

								/*Radix::SystemMonitor::InstanceStateHistory:Period:threadNameMask:Edit Options:-Edit Mask Str*/
								new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,400,true),
								null,
								false,
								null,
								null),

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmE7W4HDTIKZCNHBYNU2VDOJB3UY"),
								"threadId",
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsS6KOVFQWJJCZLOWACVB5DW3HOE"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHY4TVWIC5C4TJN2GKL2ABRKKQ"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tblRHY4TVWIC5C4TJN2GKL2ABRKKQ"),
								null,
								org.radixware.kernel.common.enums.EValType.INT,
								null,
								null,
								false,
								false,
								false,
								null,
								false,
								null,
								null,
								false,
								null,
								null),

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmROTPJ3XBMBHJJMV6HMAG6WBRRY"),
								"dbSid",
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWBN7TFP4Q5DCZL25LOXBSK4DOU"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHY4TVWIC5C4TJN2GKL2ABRKKQ"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tblRHY4TVWIC5C4TJN2GKL2ABRKKQ"),
								null,
								org.radixware.kernel.common.enums.EValType.STR,
								null,
								null,
								false,
								false,
								false,
								null,
								false,

								/*Radix::SystemMonitor::InstanceStateHistory:Period:dbSid:Edit Options:-Edit Mask Str*/
								new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,400,true),
								null,
								false,
								null,
								null),

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmWMXHTX47IBAPJMLZW23HZV7MKI"),
								"arteSeq",
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOEEM3JUUUNDHRLMHRHWW5WTTBI"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHY4TVWIC5C4TJN2GKL2ABRKKQ"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tblRHY4TVWIC5C4TJN2GKL2ABRKKQ"),
								null,
								org.radixware.kernel.common.enums.EValType.INT,
								null,
								null,
								false,
								false,
								false,
								null,
								false,
								null,
								null,
								false,
								null,
								null),

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmNXMOS25C5ZB5XJPH4JGTQK23L4"),
								"periodStartMillis",
								null,
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHY4TVWIC5C4TJN2GKL2ABRKKQ"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tblRHY4TVWIC5C4TJN2GKL2ABRKKQ"),
								null,
								org.radixware.kernel.common.enums.EValType.INT,
								null,
								null,
								false,
								false,
								false,
								null,
								false,
								null,
								null,
								false,
								null,
								null),

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prm2YKP7CT3JBGCNFO6SBZW2PJPYY"),
								"periodEndMillis",
								null,
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHY4TVWIC5C4TJN2GKL2ABRKKQ"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tblRHY4TVWIC5C4TJN2GKL2ABRKKQ"),
								null,
								org.radixware.kernel.common.enums.EValType.INT,
								null,
								null,
								false,
								false,
								false,
								null,
								false,
								null,
								null,
								false,
								null,
								null)
						},

						/*Radix::SystemMonitor::InstanceStateHistory:Period:Editor Pages-Filter Pages*/
						null,
						new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
						}
						,
						null)
			},
			new org.radixware.kernel.common.client.meta.RadSortingDef[]{

					new org.radixware.kernel.common.client.meta.RadSortingDef(
					/*Radix::SystemMonitor::InstanceStateHistory:RegTimeAsc-Sorting*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("srtR7SYZPQQZFBELKVU454IIOO3VY"),
						"RegTimeAsc",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHY4TVWIC5C4TJN2GKL2ABRKKQ"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsL6GKKVCRTVCKBGM2GBLMY4OYM4"),
						new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem[]{

								new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIFISIE3XWBD3LLQ3MZGOOG7N6M"),
								false),

								new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colELAJRXSM7NCG7FLQYSVADANYP4"),
								false),

								new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEVE536NNOZBI7N7IOSAL5WOAIQ"),
								false)
						}),

					new org.radixware.kernel.common.client.meta.RadSortingDef(
					/*Radix::SystemMonitor::InstanceStateHistory:RegTimeDesc-Sorting*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("srtUOSIXGY46JGFFOLWZFJSSYLAZY"),
						"RegTimeDesc",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHY4TVWIC5C4TJN2GKL2ABRKKQ"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDXOO3S2PIFHUJGSM6QAY7J4T54"),
						new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem[]{

								new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIFISIE3XWBD3LLQ3MZGOOG7N6M"),
								true),

								new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colELAJRXSM7NCG7FLQYSVADANYP4"),
								true),

								new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEVE536NNOZBI7N7IOSAL5WOAIQ"),
								true)
						})
			},
			new org.radixware.kernel.common.client.meta.RadReferenceDef[]{

					new org.radixware.kernel.common.client.meta.RadReferenceDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("refSNWJCJEAINHT7CN2I4NZQNAXXY"),"InstanceStateHistory=>StackData (stackDigest=>digest)",org.radixware.kernel.common.types.Id.Factory.loadFrom("tblRHY4TVWIC5C4TJN2GKL2ABRKKQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl2R4PKYCFCVHGRP2YVVFOORGWIE"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("col5NJRGYCSDZERZAQFIWHARWHNTQ")},new String[]{"stackDigest"},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colIAUYIKCLEBHP5MU4ELPXVEQWJU")},new String[]{"digest"})
			},
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr2ZU45DFBUFEPRHTN3QSLP7RAV4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("sprZV4WYIIKSNGDRMXP5LUBQZ6XWY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("sprPWR74F5KQJH57IPCYXNIPARDT4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("sprLIHXORNVCNH57M3VZOEXLJO3WY")},
			false,false,false);
}

/* Radix::SystemMonitor::InstanceStateHistory:General - Desktop Meta*/

/*Radix::SystemMonitor::InstanceStateHistory:General-Editor Presentation*/

package org.radixware.ads.SystemMonitor.explorer;
public final class General_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epr2ZU45DFBUFEPRHTN3QSLP7RAV4"),
	"General",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
	null,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHY4TVWIC5C4TJN2GKL2ABRKKQ"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tblRHY4TVWIC5C4TJN2GKL2ABRKKQ"),
	null,
	null,

	/*Radix::SystemMonitor::InstanceStateHistory:General:Editor Pages-Editor Presentation Pages*/
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

			/*Radix::SystemMonitor::InstanceStateHistory:General:Main-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgSKJVL7WIONETDL6RPTBEJBGBG4"),"Main",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHY4TVWIC5C4TJN2GKL2ABRKKQ"),null,null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIFISIE3XWBD3LLQ3MZGOOG7N6M"),0,0,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdGAD64WTSOJEAPIDCFCJCN3PE5M"),0,1,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colATYCTWD3AFBIVE3AS4TOYUPHSM"),0,2,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4P6GRGWAKVF35LL2VSBKJRJ4O4"),0,3,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEVE536NNOZBI7N7IOSAL5WOAIQ"),0,4,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colB4Y5IM6SMBHIVEYIGJSXMALEPQ"),0,6,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col66UBVRCJQFEWPDJXIWUQN2EQP4"),0,7,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3KCBW5QZFREURM6WDQKR5JKHDQ"),0,8,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colU4MHPYS6PVANJMDUSQ5W7RJCIY"),0,9,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJBVEDU3XFFC3LLANH2EAOMVKV4"),0,10,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLYPEWDCHDVBVVMMA4SFRKIKFW4"),0,11,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col5UOR3QOSWNBPJCRIEZ7F7MZW3I"),0,12,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNKGIWVA7PZFZXEPGNO5JPZQRV4"),0,13,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colSMAJMWPRYBHHNCZF6WPLKUCGBQ"),0,14,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHNUTRIYQXVA2FKJLMS25H2ZO54"),0,15,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPZ5AOPCVBVHYFKD5O3MLHV75LY"),0,16,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHNE7Z4ANKBECRGESFUUFQVCSQQ"),0,17,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdO62TBSLFZZGXVFKTWKMATT5HQE"),0,18,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3ENNZJ2XUVHWJABO3YCDY4WD2Y"),0,19,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col62UUOQ3FM5C3VETVE73AV6T2JI"),0,20,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colI7V5NLFAUBA4HGY7VUF6JFLKRI"),0,21,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colB6RB3PP5DBEHDIMUAG7LLMTTNU"),0,22,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKEUJLICU4JAIJHRZPG62HNGFGU"),0,5,1,false,false)
			},null)
	},
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgSKJVL7WIONETDL6RPTBEJBGBG4"))}
	,

	/*Radix::SystemMonitor::InstanceStateHistory:General:Children-Explorer Items*/
		null
	,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
	null,
	0,
	null,
	(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
	2192,0,0,null);
}
/* Radix::SystemMonitor::InstanceStateHistory:General - Web Meta*/

/*Radix::SystemMonitor::InstanceStateHistory:General-Editor Presentation*/

package org.radixware.ads.SystemMonitor.web;
public final class General_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epr2ZU45DFBUFEPRHTN3QSLP7RAV4"),
	"General",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
	null,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHY4TVWIC5C4TJN2GKL2ABRKKQ"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tblRHY4TVWIC5C4TJN2GKL2ABRKKQ"),
	null,
	null,

	/*Radix::SystemMonitor::InstanceStateHistory:General:Editor Pages-Editor Presentation Pages*/
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

			/*Radix::SystemMonitor::InstanceStateHistory:General:Main-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgSKJVL7WIONETDL6RPTBEJBGBG4"),"Main",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHY4TVWIC5C4TJN2GKL2ABRKKQ"),null,null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIFISIE3XWBD3LLQ3MZGOOG7N6M"),0,0,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdGAD64WTSOJEAPIDCFCJCN3PE5M"),0,1,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colATYCTWD3AFBIVE3AS4TOYUPHSM"),0,2,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4P6GRGWAKVF35LL2VSBKJRJ4O4"),0,3,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEVE536NNOZBI7N7IOSAL5WOAIQ"),0,4,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colB4Y5IM6SMBHIVEYIGJSXMALEPQ"),0,6,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col66UBVRCJQFEWPDJXIWUQN2EQP4"),0,7,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3KCBW5QZFREURM6WDQKR5JKHDQ"),0,8,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colU4MHPYS6PVANJMDUSQ5W7RJCIY"),0,9,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJBVEDU3XFFC3LLANH2EAOMVKV4"),0,10,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLYPEWDCHDVBVVMMA4SFRKIKFW4"),0,11,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col5UOR3QOSWNBPJCRIEZ7F7MZW3I"),0,12,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNKGIWVA7PZFZXEPGNO5JPZQRV4"),0,13,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colSMAJMWPRYBHHNCZF6WPLKUCGBQ"),0,14,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHNUTRIYQXVA2FKJLMS25H2ZO54"),0,15,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPZ5AOPCVBVHYFKD5O3MLHV75LY"),0,16,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHNE7Z4ANKBECRGESFUUFQVCSQQ"),0,17,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdO62TBSLFZZGXVFKTWKMATT5HQE"),0,18,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3ENNZJ2XUVHWJABO3YCDY4WD2Y"),0,19,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col62UUOQ3FM5C3VETVE73AV6T2JI"),0,20,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colI7V5NLFAUBA4HGY7VUF6JFLKRI"),0,21,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colB6RB3PP5DBEHDIMUAG7LLMTTNU"),0,22,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKEUJLICU4JAIJHRZPG62HNGFGU"),0,5,1,false,false)
			},null)
	},
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgSKJVL7WIONETDL6RPTBEJBGBG4"))}
	,

	/*Radix::SystemMonitor::InstanceStateHistory:General:Children-Explorer Items*/
		null
	,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
	null,
	0,
	null,
	(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
	2192,0,0,null);
}
/* Radix::SystemMonitor::InstanceStateHistory:General:Model - Desktop Executable*/

/*Radix::SystemMonitor::InstanceStateHistory:General:Model-Entity Model Class*/

package org.radixware.ads.SystemMonitor.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:General:Model")
public class General:Model  extends org.radixware.ads.SystemMonitor.explorer.InstanceStateHistory.InstanceStateHistory_DefaultModel {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return General:Model_mi.rdxMeta; }



	public General:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::SystemMonitor::InstanceStateHistory:General:Model:Nested classes-Nested Classes*/

	/*Radix::SystemMonitor::InstanceStateHistory:General:Model:Properties-Properties*/

	/*Radix::SystemMonitor::InstanceStateHistory:General:Model:stackTop-Presentation Property*/




	public class StackTop extends org.radixware.ads.SystemMonitor.explorer.InstanceStateHistory.col63DF4VG74ZEUXLPQGA2IHROCJY{
		public StackTop(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}

		/*Radix::SystemMonitor::InstanceStateHistory:General:Model:stackTop:EmbeddedClass-Property Presentation Embedded Class*/




		/*Radix::SystemMonitor::InstanceStateHistory:General:Model:stackTop:Nested classes-Nested Classes*/

		/*Radix::SystemMonitor::InstanceStateHistory:General:Model:stackTop:Properties-Properties*/

		/*Radix::SystemMonitor::InstanceStateHistory:General:Model:stackTop:Methods-Methods*/

		/*Radix::SystemMonitor::InstanceStateHistory:General:Model:stackTop:createPropertyEditor-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:General:Model:stackTop:createPropertyEditor")
		public published  org.radixware.kernel.common.client.views.IPropEditor createPropertyEditor () {
			final Client.Types::IMemoController memoController = new Client.Types::IMemoController() {
			    public String getFinalText(final String finalText) {
			        return finalText;
			    }

			    public String prepareTextForMemo(final String originalText) {
			        return stack.Value;
			    }
			};
			return new PropStrEditor(this) {
			    
			    public void refresh(Explorer.Models::ModelItem it) {
			        super.refresh(it);
			        if (getValEditor() instanceof Explorer.ValEditors::ValStrEditor) {
			            ((Explorer.ValEditors::ValStrEditor) getValEditor()).setMemoController(memoController);
			        }
			    }
			    
			};

		}













		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:General:Model:stackTop")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:General:Model:stackTop")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public StackTop getStackTop(){return (StackTop)getProperty(col63DF4VG74ZEUXLPQGA2IHROCJY);}

	/*Radix::SystemMonitor::InstanceStateHistory:General:Model:stack-Presentation Property*/




	public class Stack extends org.radixware.ads.SystemMonitor.explorer.InstanceStateHistory.colB6RB3PP5DBEHDIMUAG7LLMTTNU{
		public Stack(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}

		/*Radix::SystemMonitor::InstanceStateHistory:General:Model:stack:EmbeddedClass-Property Presentation Embedded Class*/




		/*Radix::SystemMonitor::InstanceStateHistory:General:Model:stack:Nested classes-Nested Classes*/

		/*Radix::SystemMonitor::InstanceStateHistory:General:Model:stack:Properties-Properties*/

		/*Radix::SystemMonitor::InstanceStateHistory:General:Model:stack:Methods-Methods*/

		/*Radix::SystemMonitor::InstanceStateHistory:General:Model:stack:createPropertyEditor-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:General:Model:stack:createPropertyEditor")
		public published  org.radixware.kernel.common.client.views.IPropEditor createPropertyEditor () {
			Explorer.Widgets::PropTextEditor editor = new PropTextEditor(this);
			editor.setFixedHeight(150);
			return editor;
		}













		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:General:Model:stack")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:General:Model:stack")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Stack getStack(){return (Stack)getProperty(colB6RB3PP5DBEHDIMUAG7LLMTTNU);}










	/*Radix::SystemMonitor::InstanceStateHistory:General:Model:Methods-Methods*/

	/*Radix::SystemMonitor::InstanceStateHistory:General:Model:beforeOpenView-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:General:Model:beforeOpenView")
	protected published  void beforeOpenView () {
		super.beforeOpenView();

		final boolean isUnit = threadKind.Value == InstanceThreadKind:UNIT;
		final boolean isArte = threadKind.Value == InstanceThreadKind:ARTE;
		final boolean isThreadWithDbConnection = threadKind.Value != InstanceThreadKind:SERVER_CHILD;

		unitId.setVisible(isUnit);

		Explorer.Models.Properties::Property[] arteProps = new Explorer.Models.Properties::Property[] {
		    arteSeq, arteSerial, cpuDiffMillis, dbDiffMillis, extDiffMillis, queueDiffMillis, rqStartTime, traceContexts
		};
		for (Explorer.Models.Properties::Property arteProp: arteProps) {
		    arteProp.setVisible(isArte);
		}

		final boolean hasLock = lockName.Value != null || lockOwnerName.Value != null;
		lockName.setVisible(hasLock);
		lockOwnerName.setVisible(hasLock);

		dbSid.setVisible(isThreadWithDbConnection);
		dbSerial.setVisible(isThreadWithDbConnection);

		extData.setVisible(false);
	}


}

/* Radix::SystemMonitor::InstanceStateHistory:General:Model - Desktop Meta*/

/*Radix::SystemMonitor::InstanceStateHistory:General:Model-Entity Model Class*/

package org.radixware.ads.SystemMonitor.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class General:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aem2ZU45DFBUFEPRHTN3QSLP7RAV4"),
						"General:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::SystemMonitor::InstanceStateHistory:General:Model:Properties-Properties*/
						null,
						null,
						null,
						null,
						null,
						null,
						false,
						false,
						false
						);
}

/* Radix::SystemMonitor::InstanceStateHistory:General:Model - Web Executable*/

/*Radix::SystemMonitor::InstanceStateHistory:General:Model-Entity Model Class*/

package org.radixware.ads.SystemMonitor.web;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:General:Model")
public class General:Model  extends org.radixware.ads.SystemMonitor.web.InstanceStateHistory.InstanceStateHistory_DefaultModel {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return General:Model_mi.rdxMeta; }



	public General:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::SystemMonitor::InstanceStateHistory:General:Model:Nested classes-Nested Classes*/

	/*Radix::SystemMonitor::InstanceStateHistory:General:Model:Properties-Properties*/

	/*Radix::SystemMonitor::InstanceStateHistory:General:Model:stackTop-Presentation Property*/




	public class StackTop extends org.radixware.ads.SystemMonitor.web.InstanceStateHistory.col63DF4VG74ZEUXLPQGA2IHROCJY{
		public StackTop(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}

		/*Radix::SystemMonitor::InstanceStateHistory:General:Model:stackTop:EmbeddedClass-Property Presentation Embedded Class*/




		/*Radix::SystemMonitor::InstanceStateHistory:General:Model:stackTop:Nested classes-Nested Classes*/

		/*Radix::SystemMonitor::InstanceStateHistory:General:Model:stackTop:Properties-Properties*/

		/*Radix::SystemMonitor::InstanceStateHistory:General:Model:stackTop:Methods-Methods*/

		/*Radix::SystemMonitor::InstanceStateHistory:General:Model:stackTop:createPropertyEditor-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:General:Model:stackTop:createPropertyEditor")
		public published  org.radixware.kernel.common.client.views.IPropEditor createPropertyEditor () {
			final Client.Types::IMemoController memoController = new Client.Types::IMemoController() {
			    public String getFinalText(final String finalText) {
			        return finalText;
			    }

			    public String prepareTextForMemo(final String originalText) {
			        return stack.Value;
			    }
			};
			return new PropStrEditor(this) {
			    
			    public void refresh(Explorer.Models::ModelItem it) {
			        super.refresh(it);
			        if (getValEditor() instanceof Web.Widgets::ValStrEditorController) {
			            ((Web.Widgets::ValStrEditorController) getValEditor()).setMemoController(memoController);
			        }
			    }
			    
			};
		}













		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:General:Model:stackTop")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:General:Model:stackTop")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public StackTop getStackTop(){return (StackTop)getProperty(col63DF4VG74ZEUXLPQGA2IHROCJY);}

	/*Radix::SystemMonitor::InstanceStateHistory:General:Model:stack-Presentation Property*/




	public class Stack extends org.radixware.ads.SystemMonitor.web.InstanceStateHistory.colB6RB3PP5DBEHDIMUAG7LLMTTNU{
		public Stack(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}

		/*Radix::SystemMonitor::InstanceStateHistory:General:Model:stack:EmbeddedClass-Property Presentation Embedded Class*/




		/*Radix::SystemMonitor::InstanceStateHistory:General:Model:stack:Nested classes-Nested Classes*/

		/*Radix::SystemMonitor::InstanceStateHistory:General:Model:stack:Properties-Properties*/

		/*Radix::SystemMonitor::InstanceStateHistory:General:Model:stack:Methods-Methods*/

		/*Radix::SystemMonitor::InstanceStateHistory:General:Model:stack:createPropertyEditor-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:General:Model:stack:createPropertyEditor")
		public published  org.radixware.kernel.common.client.views.IPropEditor createPropertyEditor () {
			Web.Widgets::PropTextEditor editor = new PropTextEditor(this);
			editor.setHeight(150);
			return editor;
		}













		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:General:Model:stack")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:General:Model:stack")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Stack getStack(){return (Stack)getProperty(colB6RB3PP5DBEHDIMUAG7LLMTTNU);}










	/*Radix::SystemMonitor::InstanceStateHistory:General:Model:Methods-Methods*/

	/*Radix::SystemMonitor::InstanceStateHistory:General:Model:beforeOpenView-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:General:Model:beforeOpenView")
	protected published  void beforeOpenView () {
		super.beforeOpenView();

		final boolean isUnit = threadKind.Value == InstanceThreadKind:UNIT;
		final boolean isArte = threadKind.Value == InstanceThreadKind:ARTE;
		final boolean isThreadWithDbConnection = threadKind.Value != InstanceThreadKind:SERVER_CHILD;

		unitId.setVisible(isUnit);

		Explorer.Models.Properties::Property[] arteProps = new Explorer.Models.Properties::Property[] {
		    arteSeq, arteSerial, cpuDiffMillis, dbDiffMillis, extDiffMillis, queueDiffMillis, rqStartTime, traceContexts
		};
		for (Explorer.Models.Properties::Property arteProp: arteProps) {
		    arteProp.setVisible(isArte);
		}

		final boolean hasLock = lockName.Value != null || lockOwnerName.Value != null;
		lockName.setVisible(hasLock);
		lockOwnerName.setVisible(hasLock);

		dbSid.setVisible(isThreadWithDbConnection);
		dbSerial.setVisible(isThreadWithDbConnection);

		extData.setVisible(false);
	}


}

/* Radix::SystemMonitor::InstanceStateHistory:General:Model - Web Meta*/

/*Radix::SystemMonitor::InstanceStateHistory:General:Model-Entity Model Class*/

package org.radixware.ads.SystemMonitor.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class General:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aem2ZU45DFBUFEPRHTN3QSLP7RAV4"),
						"General:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::SystemMonitor::InstanceStateHistory:General:Model:Properties-Properties*/
						null,
						null,
						null,
						null,
						null,
						null,
						false,
						false,
						false
						);
}

/* Radix::SystemMonitor::InstanceStateHistory:General - Desktop Meta*/

/*Radix::SystemMonitor::InstanceStateHistory:General-Selector Presentation*/

package org.radixware.ads.SystemMonitor.explorer;
public final class General_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new General_mi();
	private General_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("sprZV4WYIIKSNGDRMXP5LUBQZ6XWY"),
		"General",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
		null,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHY4TVWIC5C4TJN2GKL2ABRKKQ"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tblRHY4TVWIC5C4TJN2GKL2ABRKKQ"),
		null,
		null,
		null,
		null,
		false,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("srtUOSIXGY46JGFFOLWZFJSSYLAZY"),
		null,
		false,
		true,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("fltUP3BGJU3RBGGTCTUYWEZESZ3EA"),
		32,
		null,
		144,
		null,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr2ZU45DFBUFEPRHTN3QSLP7RAV4")},
		false,false,false,0,0);
		columns = new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn[]{

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIFISIE3XWBD3LLQ3MZGOOG7N6M"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdGAD64WTSOJEAPIDCFCJCN3PE5M"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colATYCTWD3AFBIVE3AS4TOYUPHSM"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4P6GRGWAKVF35LL2VSBKJRJ4O4"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEVE536NNOZBI7N7IOSAL5WOAIQ"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colB4Y5IM6SMBHIVEYIGJSXMALEPQ"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col66UBVRCJQFEWPDJXIWUQN2EQP4"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3KCBW5QZFREURM6WDQKR5JKHDQ"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colU4MHPYS6PVANJMDUSQ5W7RJCIY"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJBVEDU3XFFC3LLANH2EAOMVKV4"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLYPEWDCHDVBVVMMA4SFRKIKFW4"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col5UOR3QOSWNBPJCRIEZ7F7MZW3I"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNKGIWVA7PZFZXEPGNO5JPZQRV4"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colSMAJMWPRYBHHNCZF6WPLKUCGBQ"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHNUTRIYQXVA2FKJLMS25H2ZO54"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPZ5AOPCVBVHYFKD5O3MLHV75LY"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHNE7Z4ANKBECRGESFUUFQVCSQQ"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdO62TBSLFZZGXVFKTWKMATT5HQE"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3ENNZJ2XUVHWJABO3YCDY4WD2Y"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col62UUOQ3FM5C3VETVE73AV6T2JI"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col63DF4VG74ZEUXLPQGA2IHROCJY"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null)
		};
	}
	@Override
	protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.SystemMonitor.explorer.InstanceStateHistory.DefaultGroupModel(userSession,this);
	}
}
/* Radix::SystemMonitor::InstanceStateHistory:General - Web Meta*/

/*Radix::SystemMonitor::InstanceStateHistory:General-Selector Presentation*/

package org.radixware.ads.SystemMonitor.web;
public final class General_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new General_mi();
	private General_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("sprZV4WYIIKSNGDRMXP5LUBQZ6XWY"),
		"General",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
		null,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHY4TVWIC5C4TJN2GKL2ABRKKQ"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tblRHY4TVWIC5C4TJN2GKL2ABRKKQ"),
		null,
		null,
		null,
		null,
		false,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("srtUOSIXGY46JGFFOLWZFJSSYLAZY"),
		null,
		false,
		true,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("fltUP3BGJU3RBGGTCTUYWEZESZ3EA"),
		32,
		null,
		144,
		null,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr2ZU45DFBUFEPRHTN3QSLP7RAV4")},
		false,false,false,0,0);
		columns = new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn[]{

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIFISIE3XWBD3LLQ3MZGOOG7N6M"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdGAD64WTSOJEAPIDCFCJCN3PE5M"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colATYCTWD3AFBIVE3AS4TOYUPHSM"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4P6GRGWAKVF35LL2VSBKJRJ4O4"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEVE536NNOZBI7N7IOSAL5WOAIQ"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colB4Y5IM6SMBHIVEYIGJSXMALEPQ"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col66UBVRCJQFEWPDJXIWUQN2EQP4"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3KCBW5QZFREURM6WDQKR5JKHDQ"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colU4MHPYS6PVANJMDUSQ5W7RJCIY"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJBVEDU3XFFC3LLANH2EAOMVKV4"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLYPEWDCHDVBVVMMA4SFRKIKFW4"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col5UOR3QOSWNBPJCRIEZ7F7MZW3I"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNKGIWVA7PZFZXEPGNO5JPZQRV4"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colSMAJMWPRYBHHNCZF6WPLKUCGBQ"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHNUTRIYQXVA2FKJLMS25H2ZO54"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPZ5AOPCVBVHYFKD5O3MLHV75LY"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHNE7Z4ANKBECRGESFUUFQVCSQQ"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdO62TBSLFZZGXVFKTWKMATT5HQE"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3ENNZJ2XUVHWJABO3YCDY4WD2Y"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col62UUOQ3FM5C3VETVE73AV6T2JI"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col63DF4VG74ZEUXLPQGA2IHROCJY"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null)
		};
	}
	@Override
	protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.SystemMonitor.web.InstanceStateHistory.DefaultGroupModel(userSession,this);
	}
}
/* Radix::SystemMonitor::InstanceStateHistory:Instance - Desktop Meta*/

/*Radix::SystemMonitor::InstanceStateHistory:Instance-Selector Presentation*/

package org.radixware.ads.SystemMonitor.explorer;
public final class Instance_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new Instance_mi();
	private Instance_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("sprPWR74F5KQJH57IPCYXNIPARDT4"),
		"Instance",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("sprZV4WYIIKSNGDRMXP5LUBQZ6XWY"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHY4TVWIC5C4TJN2GKL2ABRKKQ"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tblRHY4TVWIC5C4TJN2GKL2ABRKKQ"),
		null,
		null,
		null,
		null,
		false,
		null,
		null,
		false,
		false,
		null,
		0,null,
		16569,
		null,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr2ZU45DFBUFEPRHTN3QSLP7RAV4")},
		false,false,false,0,0);
		columns = new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn[]{

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIFISIE3XWBD3LLQ3MZGOOG7N6M"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdGAD64WTSOJEAPIDCFCJCN3PE5M"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colATYCTWD3AFBIVE3AS4TOYUPHSM"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4P6GRGWAKVF35LL2VSBKJRJ4O4"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEVE536NNOZBI7N7IOSAL5WOAIQ"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colB4Y5IM6SMBHIVEYIGJSXMALEPQ"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col66UBVRCJQFEWPDJXIWUQN2EQP4"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3KCBW5QZFREURM6WDQKR5JKHDQ"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colU4MHPYS6PVANJMDUSQ5W7RJCIY"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJBVEDU3XFFC3LLANH2EAOMVKV4"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLYPEWDCHDVBVVMMA4SFRKIKFW4"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col5UOR3QOSWNBPJCRIEZ7F7MZW3I"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNKGIWVA7PZFZXEPGNO5JPZQRV4"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colSMAJMWPRYBHHNCZF6WPLKUCGBQ"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHNUTRIYQXVA2FKJLMS25H2ZO54"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPZ5AOPCVBVHYFKD5O3MLHV75LY"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHNE7Z4ANKBECRGESFUUFQVCSQQ"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdO62TBSLFZZGXVFKTWKMATT5HQE"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3ENNZJ2XUVHWJABO3YCDY4WD2Y"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col62UUOQ3FM5C3VETVE73AV6T2JI"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col63DF4VG74ZEUXLPQGA2IHROCJY"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null)
		};
	}
	@Override
	protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.SystemMonitor.explorer.InstanceStateHistory.DefaultGroupModel(userSession,this);
	}
}
/* Radix::SystemMonitor::InstanceStateHistory:Instance - Web Meta*/

/*Radix::SystemMonitor::InstanceStateHistory:Instance-Selector Presentation*/

package org.radixware.ads.SystemMonitor.web;
public final class Instance_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new Instance_mi();
	private Instance_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("sprPWR74F5KQJH57IPCYXNIPARDT4"),
		"Instance",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("sprZV4WYIIKSNGDRMXP5LUBQZ6XWY"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHY4TVWIC5C4TJN2GKL2ABRKKQ"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tblRHY4TVWIC5C4TJN2GKL2ABRKKQ"),
		null,
		null,
		null,
		null,
		false,
		null,
		null,
		false,
		false,
		null,
		0,null,
		16569,
		null,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr2ZU45DFBUFEPRHTN3QSLP7RAV4")},
		false,false,false,0,0);
		columns = new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn[]{

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIFISIE3XWBD3LLQ3MZGOOG7N6M"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdGAD64WTSOJEAPIDCFCJCN3PE5M"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colATYCTWD3AFBIVE3AS4TOYUPHSM"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4P6GRGWAKVF35LL2VSBKJRJ4O4"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEVE536NNOZBI7N7IOSAL5WOAIQ"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colB4Y5IM6SMBHIVEYIGJSXMALEPQ"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col66UBVRCJQFEWPDJXIWUQN2EQP4"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3KCBW5QZFREURM6WDQKR5JKHDQ"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colU4MHPYS6PVANJMDUSQ5W7RJCIY"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJBVEDU3XFFC3LLANH2EAOMVKV4"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLYPEWDCHDVBVVMMA4SFRKIKFW4"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col5UOR3QOSWNBPJCRIEZ7F7MZW3I"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNKGIWVA7PZFZXEPGNO5JPZQRV4"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colSMAJMWPRYBHHNCZF6WPLKUCGBQ"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHNUTRIYQXVA2FKJLMS25H2ZO54"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPZ5AOPCVBVHYFKD5O3MLHV75LY"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHNE7Z4ANKBECRGESFUUFQVCSQQ"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdO62TBSLFZZGXVFKTWKMATT5HQE"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3ENNZJ2XUVHWJABO3YCDY4WD2Y"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col62UUOQ3FM5C3VETVE73AV6T2JI"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col63DF4VG74ZEUXLPQGA2IHROCJY"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null)
		};
	}
	@Override
	protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.SystemMonitor.web.InstanceStateHistory.DefaultGroupModel(userSession,this);
	}
}
/* Radix::SystemMonitor::InstanceStateHistory:Arte - Desktop Meta*/

/*Radix::SystemMonitor::InstanceStateHistory:Arte-Selector Presentation*/

package org.radixware.ads.SystemMonitor.explorer;
public final class Arte_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new Arte_mi();
	private Arte_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("sprLIHXORNVCNH57M3VZOEXLJO3WY"),
		"Arte",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("sprZV4WYIIKSNGDRMXP5LUBQZ6XWY"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHY4TVWIC5C4TJN2GKL2ABRKKQ"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tblRHY4TVWIC5C4TJN2GKL2ABRKKQ"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWJELU3IUQVEXZNB7EH5THHPBTM"),
		null,
		null,
		null,
		false,
		null,
		null,
		false,
		false,
		null,
		0,null,
		16441,
		null,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr2ZU45DFBUFEPRHTN3QSLP7RAV4")},
		false,false,false,0,0);
		columns = new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn[]{

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIFISIE3XWBD3LLQ3MZGOOG7N6M"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdGAD64WTSOJEAPIDCFCJCN3PE5M"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colATYCTWD3AFBIVE3AS4TOYUPHSM"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4P6GRGWAKVF35LL2VSBKJRJ4O4"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEVE536NNOZBI7N7IOSAL5WOAIQ"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colB4Y5IM6SMBHIVEYIGJSXMALEPQ"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col66UBVRCJQFEWPDJXIWUQN2EQP4"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3KCBW5QZFREURM6WDQKR5JKHDQ"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colU4MHPYS6PVANJMDUSQ5W7RJCIY"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJBVEDU3XFFC3LLANH2EAOMVKV4"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLYPEWDCHDVBVVMMA4SFRKIKFW4"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col5UOR3QOSWNBPJCRIEZ7F7MZW3I"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNKGIWVA7PZFZXEPGNO5JPZQRV4"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colSMAJMWPRYBHHNCZF6WPLKUCGBQ"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHNUTRIYQXVA2FKJLMS25H2ZO54"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPZ5AOPCVBVHYFKD5O3MLHV75LY"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHNE7Z4ANKBECRGESFUUFQVCSQQ"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdO62TBSLFZZGXVFKTWKMATT5HQE"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3ENNZJ2XUVHWJABO3YCDY4WD2Y"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col62UUOQ3FM5C3VETVE73AV6T2JI"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col63DF4VG74ZEUXLPQGA2IHROCJY"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null)
		};
	}
	@Override
	protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.SystemMonitor.explorer.InstanceStateHistory.DefaultGroupModel(userSession,this);
	}
}
/* Radix::SystemMonitor::InstanceStateHistory:Arte - Web Meta*/

/*Radix::SystemMonitor::InstanceStateHistory:Arte-Selector Presentation*/

package org.radixware.ads.SystemMonitor.web;
public final class Arte_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new Arte_mi();
	private Arte_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("sprLIHXORNVCNH57M3VZOEXLJO3WY"),
		"Arte",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("sprZV4WYIIKSNGDRMXP5LUBQZ6XWY"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHY4TVWIC5C4TJN2GKL2ABRKKQ"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tblRHY4TVWIC5C4TJN2GKL2ABRKKQ"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWJELU3IUQVEXZNB7EH5THHPBTM"),
		null,
		null,
		null,
		false,
		null,
		null,
		false,
		false,
		null,
		0,null,
		16441,
		null,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr2ZU45DFBUFEPRHTN3QSLP7RAV4")},
		false,false,false,0,0);
		columns = new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn[]{

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIFISIE3XWBD3LLQ3MZGOOG7N6M"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdGAD64WTSOJEAPIDCFCJCN3PE5M"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colATYCTWD3AFBIVE3AS4TOYUPHSM"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4P6GRGWAKVF35LL2VSBKJRJ4O4"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEVE536NNOZBI7N7IOSAL5WOAIQ"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colB4Y5IM6SMBHIVEYIGJSXMALEPQ"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col66UBVRCJQFEWPDJXIWUQN2EQP4"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3KCBW5QZFREURM6WDQKR5JKHDQ"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colU4MHPYS6PVANJMDUSQ5W7RJCIY"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJBVEDU3XFFC3LLANH2EAOMVKV4"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLYPEWDCHDVBVVMMA4SFRKIKFW4"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col5UOR3QOSWNBPJCRIEZ7F7MZW3I"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNKGIWVA7PZFZXEPGNO5JPZQRV4"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colSMAJMWPRYBHHNCZF6WPLKUCGBQ"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHNUTRIYQXVA2FKJLMS25H2ZO54"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPZ5AOPCVBVHYFKD5O3MLHV75LY"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHNE7Z4ANKBECRGESFUUFQVCSQQ"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdO62TBSLFZZGXVFKTWKMATT5HQE"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3ENNZJ2XUVHWJABO3YCDY4WD2Y"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col62UUOQ3FM5C3VETVE73AV6T2JI"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col63DF4VG74ZEUXLPQGA2IHROCJY"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null)
		};
	}
	@Override
	protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.SystemMonitor.web.InstanceStateHistory.DefaultGroupModel(userSession,this);
	}
}
/* Radix::SystemMonitor::InstanceStateHistory:Period:Model - Desktop Executable*/

/*Radix::SystemMonitor::InstanceStateHistory:Period:Model-Filter Model Class*/

package org.radixware.ads.SystemMonitor.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:Period:Model")
public class Period:Model  extends org.radixware.kernel.common.client.models.FilterModel  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Period:Model_mi.rdxMeta; }



	public Period:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.filters.RadFilterDef def){super(userSession,def);}

	/*Radix::SystemMonitor::InstanceStateHistory:Period:Model:Nested classes-Nested Classes*/

	/*Radix::SystemMonitor::InstanceStateHistory:Period:Model:Properties-Properties*/

	/*Radix::SystemMonitor::InstanceStateHistory:Period:Model:periodStart-Presentation Property*/




	public class PeriodStart extends org.radixware.kernel.common.client.models.items.properties.PropertyDateTime{
		public PeriodStart(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			java.sql.Timestamp dummy = ((java.sql.Timestamp)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:Period:Model:periodStart")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:Period:Model:periodStart")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public PeriodStart getPeriodStart(){return (PeriodStart)getProperty(prmZPWMMLYX45FJXE3EWBDLU3VTAI);}

	/*Radix::SystemMonitor::InstanceStateHistory:Period:Model:periodEnd-Presentation Property*/




	public class PeriodEnd extends org.radixware.kernel.common.client.models.items.properties.PropertyDateTime{
		public PeriodEnd(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			java.sql.Timestamp dummy = ((java.sql.Timestamp)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:Period:Model:periodEnd")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:Period:Model:periodEnd")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public PeriodEnd getPeriodEnd(){return (PeriodEnd)getProperty(prmYO7RVM4B3BDFFF6J2E5WX3N5UM);}

	/*Radix::SystemMonitor::InstanceStateHistory:Period:Model:periodStartMillis-Presentation Property*/




	public class PeriodStartMillis extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public PeriodStartMillis(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:Period:Model:periodStartMillis")
		public  Int getValue() {

			final DateTime dt = (DateTime)periodStart.getValueObject();
			final Int millis = dt != null ? dt.Time : Int.MIN_VALUE;
			return millis;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:Period:Model:periodStartMillis")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public PeriodStartMillis getPeriodStartMillis(){return (PeriodStartMillis)getProperty(prmNXMOS25C5ZB5XJPH4JGTQK23L4);}

	/*Radix::SystemMonitor::InstanceStateHistory:Period:Model:periodEndMillis-Presentation Property*/




	public class PeriodEndMillis extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public PeriodEndMillis(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:Period:Model:periodEndMillis")
		public  Int getValue() {

			final DateTime dt = (DateTime)periodEnd.getValueObject();
			final Int millis = dt != null ? dt.Time + 999 : Int.MAX_VALUE;
			return millis;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:Period:Model:periodEndMillis")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public PeriodEndMillis getPeriodEndMillis(){return (PeriodEndMillis)getProperty(prm2YKP7CT3JBGCNFO6SBZW2PJPYY);}






















	/*Radix::SystemMonitor::InstanceStateHistory:Period:Model:Methods-Methods*/

	/*Radix::SystemMonitor::InstanceStateHistory:Period:Model:beforeOpenView-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:Period:Model:beforeOpenView")
	protected published  void beforeOpenView () {
		super.beforeOpenView();
		if (this.getFilterContext().getOwnerModel().getDefinition().getId() == idof[InstanceStateHistory:Arte]) {
		    this.getProperty(idof[InstanceStateHistory:Period:arteSeq]).setVisible(false);
		}

		periodStartMillis.setVisible(false);
		periodEndMillis.setVisible(false);

	}

	/*Radix::SystemMonitor::InstanceStateHistory:Period:threadNameMask:threadNameMask-Presentation Property*/




	public class ThreadNameMask extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public ThreadNameMask(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:Period:threadNameMask:threadNameMask")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:Period:threadNameMask:threadNameMask")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ThreadNameMask getThreadNameMask(){return (ThreadNameMask)getProperty(prmUWWMSPN7QNGAPCCDM2VV5NXS4A);}

	/*Radix::SystemMonitor::InstanceStateHistory:Period:threadId:threadId-Presentation Property*/




	public class ThreadId extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public ThreadId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:Period:threadId:threadId")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:Period:threadId:threadId")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public ThreadId getThreadId(){return (ThreadId)getProperty(prmE7W4HDTIKZCNHBYNU2VDOJB3UY);}

	/*Radix::SystemMonitor::InstanceStateHistory:Period:dbSid:dbSid-Presentation Property*/




	public class DbSid extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public DbSid(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:Period:dbSid:dbSid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:Period:dbSid:dbSid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public DbSid getDbSid(){return (DbSid)getProperty(prmROTPJ3XBMBHJJMV6HMAG6WBRRY);}

	/*Radix::SystemMonitor::InstanceStateHistory:Period:arteSeq:arteSeq-Presentation Property*/




	public class ArteSeq extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public ArteSeq(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:Period:arteSeq:arteSeq")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:Period:arteSeq:arteSeq")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public ArteSeq getArteSeq(){return (ArteSeq)getProperty(prmWMXHTX47IBAPJMLZW23HZV7MKI);}


}

/* Radix::SystemMonitor::InstanceStateHistory:Period:Model - Desktop Meta*/

/*Radix::SystemMonitor::InstanceStateHistory:Period:Model-Filter Model Class*/

package org.radixware.ads.SystemMonitor.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Period:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("fmcUP3BGJU3RBGGTCTUYWEZESZ3EA"),
						"Period:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::SystemMonitor::InstanceStateHistory:Period:Model:Properties-Properties*/
						null,
						null,
						null,
						null,
						null,
						null,
						false,
						false,
						false
						);
}

/* Radix::SystemMonitor::InstanceStateHistory:Period:Model - Web Executable*/

/*Radix::SystemMonitor::InstanceStateHistory:Period:Model-Filter Model Class*/

package org.radixware.ads.SystemMonitor.web;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:Period:Model")
public class Period:Model  extends org.radixware.kernel.common.client.models.FilterModel  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Period:Model_mi.rdxMeta; }



	public Period:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.filters.RadFilterDef def){super(userSession,def);}

	/*Radix::SystemMonitor::InstanceStateHistory:Period:Model:Nested classes-Nested Classes*/

	/*Radix::SystemMonitor::InstanceStateHistory:Period:Model:Properties-Properties*/

	/*Radix::SystemMonitor::InstanceStateHistory:Period:Model:periodStart-Presentation Property*/




	public class PeriodStart extends org.radixware.kernel.common.client.models.items.properties.PropertyDateTime{
		public PeriodStart(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			java.sql.Timestamp dummy = ((java.sql.Timestamp)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:Period:Model:periodStart")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:Period:Model:periodStart")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public PeriodStart getPeriodStart(){return (PeriodStart)getProperty(prmZPWMMLYX45FJXE3EWBDLU3VTAI);}

	/*Radix::SystemMonitor::InstanceStateHistory:Period:Model:periodEnd-Presentation Property*/




	public class PeriodEnd extends org.radixware.kernel.common.client.models.items.properties.PropertyDateTime{
		public PeriodEnd(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			java.sql.Timestamp dummy = ((java.sql.Timestamp)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:Period:Model:periodEnd")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:Period:Model:periodEnd")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public PeriodEnd getPeriodEnd(){return (PeriodEnd)getProperty(prmYO7RVM4B3BDFFF6J2E5WX3N5UM);}

	/*Radix::SystemMonitor::InstanceStateHistory:Period:Model:periodStartMillis-Presentation Property*/




	public class PeriodStartMillis extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public PeriodStartMillis(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:Period:Model:periodStartMillis")
		public  Int getValue() {

			final DateTime dt = (DateTime)periodStart.getValueObject();
			final Int millis = dt != null ? dt.Time : Int.MIN_VALUE;
			return millis;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:Period:Model:periodStartMillis")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public PeriodStartMillis getPeriodStartMillis(){return (PeriodStartMillis)getProperty(prmNXMOS25C5ZB5XJPH4JGTQK23L4);}

	/*Radix::SystemMonitor::InstanceStateHistory:Period:Model:periodEndMillis-Presentation Property*/




	public class PeriodEndMillis extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public PeriodEndMillis(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:Period:Model:periodEndMillis")
		public  Int getValue() {

			final DateTime dt = (DateTime)periodEnd.getValueObject();
			final Int millis = dt != null ? dt.Time + 999 : Int.MAX_VALUE;
			return millis;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:Period:Model:periodEndMillis")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public PeriodEndMillis getPeriodEndMillis(){return (PeriodEndMillis)getProperty(prm2YKP7CT3JBGCNFO6SBZW2PJPYY);}






















	/*Radix::SystemMonitor::InstanceStateHistory:Period:Model:Methods-Methods*/

	/*Radix::SystemMonitor::InstanceStateHistory:Period:Model:beforeOpenView-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:Period:Model:beforeOpenView")
	protected published  void beforeOpenView () {
		super.beforeOpenView();
		if (this.getFilterContext().getOwnerModel().getDefinition().getId() == idof[InstanceStateHistory:Arte]) {
		    this.getProperty(idof[InstanceStateHistory:Period:arteSeq]).setVisible(false);
		}

		periodStartMillis.setVisible(false);
		periodEndMillis.setVisible(false);

	}

	/*Radix::SystemMonitor::InstanceStateHistory:Period:threadNameMask:threadNameMask-Presentation Property*/




	public class ThreadNameMask extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public ThreadNameMask(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:Period:threadNameMask:threadNameMask")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:Period:threadNameMask:threadNameMask")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ThreadNameMask getThreadNameMask(){return (ThreadNameMask)getProperty(prmUWWMSPN7QNGAPCCDM2VV5NXS4A);}

	/*Radix::SystemMonitor::InstanceStateHistory:Period:threadId:threadId-Presentation Property*/




	public class ThreadId extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public ThreadId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:Period:threadId:threadId")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:Period:threadId:threadId")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public ThreadId getThreadId(){return (ThreadId)getProperty(prmE7W4HDTIKZCNHBYNU2VDOJB3UY);}

	/*Radix::SystemMonitor::InstanceStateHistory:Period:dbSid:dbSid-Presentation Property*/




	public class DbSid extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public DbSid(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:Period:dbSid:dbSid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:Period:dbSid:dbSid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public DbSid getDbSid(){return (DbSid)getProperty(prmROTPJ3XBMBHJJMV6HMAG6WBRRY);}

	/*Radix::SystemMonitor::InstanceStateHistory:Period:arteSeq:arteSeq-Presentation Property*/




	public class ArteSeq extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public ArteSeq(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:Period:arteSeq:arteSeq")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::InstanceStateHistory:Period:arteSeq:arteSeq")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public ArteSeq getArteSeq(){return (ArteSeq)getProperty(prmWMXHTX47IBAPJMLZW23HZV7MKI);}


}

/* Radix::SystemMonitor::InstanceStateHistory:Period:Model - Web Meta*/

/*Radix::SystemMonitor::InstanceStateHistory:Period:Model-Filter Model Class*/

package org.radixware.ads.SystemMonitor.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Period:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("fmcUP3BGJU3RBGGTCTUYWEZESZ3EA"),
						"Period:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::SystemMonitor::InstanceStateHistory:Period:Model:Properties-Properties*/
						null,
						null,
						null,
						null,
						null,
						null,
						false,
						false,
						false
						);
}

/* Radix::SystemMonitor::InstanceStateHistory - Localizing Bundle */
package org.radixware.ads.SystemMonitor.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class InstanceStateHistory - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Запись состояния потока инстанции");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Instance thread state record");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls25Z2LCA2KFHWRH3LLGFWKJFXYU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"C:\\svn\\radix.trunk1\\org.radixware\\ads\\SystemMonitor"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Разница потребления Queue (мс)");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Queue usage diff (ms)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2QCWSEDDBVGRDKSJBNA2KRRHWI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"C:\\svn\\radix.trunk1\\org.radixware\\ads\\SystemMonitor"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Маска имени потока");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Thread name mask");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2RU56LY3PZFGFNWU6CW4YBYD7U"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"C:\\svn\\radix.trunk1\\org.radixware\\ads\\SystemMonitor"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Расширенные данные");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Extended data");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2XFJ4UTGMRBBBESLQIUOQOQ6DM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"C:\\svn\\radix.trunk1\\org.radixware\\ads\\SystemMonitor"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Стек");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Stack");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4D7KCUJ5OBG37HYW4C5W3VFW64"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"C:\\svn\\radix.trunk1\\org.radixware\\ads\\SystemMonitor"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Имя потока");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Thread name");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4FD2UHVTFRCATFQEA555O35FTI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"C:\\svn\\radix.trunk1\\org.radixware\\ads\\SystemMonitor"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<неизвестно>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<unknown>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5RCRWTO4NNGZZOTIOCPD5KXVXM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"C:\\svn\\radix.trunk1\\org.radixware\\ads\\SystemMonitor"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<нет>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<none>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5W6YCXL5BRFETNGXEU5KVASSZU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"C:\\svn\\radix.trunk1\\org.radixware\\ads\\SystemMonitor"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Стек");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Stack");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6MPX4S5ZNZAIZM32KOGISEFG7Q"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"C:\\svn\\radix.trunk1\\org.radixware\\ads\\SystemMonitor"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Принудительно");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Forced");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAANXTA3CCNGPXDIBUF2OPBVSPY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"C:\\svn\\radix.trunk1\\org.radixware\\ads\\SystemMonitor"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Конец периода");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Period end");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAJOIRGB7WFEW5JFP3IOHBG2VJM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"C:\\svn\\radix.trunk1\\org.radixware\\ads\\SystemMonitor"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Имя блокировки");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Lock name");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAXCQEMVMXNFSJAXVBWBFQRV7QA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"C:\\svn\\radix.trunk1\\org.radixware\\ads\\SystemMonitor"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Контексты трассировки");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Trace Contexts");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAXTJP2SWKZHYPF4IKKDEZJFEPY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"C:\\svn\\radix.trunk1\\org.radixware\\ads\\SystemMonitor"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Разница потребления CPU (мс)");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"CPU usage diff (ms)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsC2BF4MEY2BAU7O45G7AF643ZVA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"C:\\svn\\radix.trunk1\\org.radixware\\ads\\SystemMonitor"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ид. сессии в БД");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"DB session ID");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCTMEX26XRFCARHHAZPHD7YOX2Q"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"C:\\svn\\radix.trunk1\\org.radixware\\ads\\SystemMonitor"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Сначала новые");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Recent first");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDXOO3S2PIFHUJGSM6QAY7J4T54"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"C:\\svn\\radix.trunk1\\org.radixware\\ads\\SystemMonitor"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Имя владельца блокировки");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Lock owner name");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEUUJKFWJHVFB7DOT6N7VYL3NHE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"C:\\svn\\radix.trunk1\\org.radixware\\ads\\SystemMonitor"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ид. родительского потока");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Ancestor thread ID");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHA6ASVKG3JA75NKNYROR3MWDSE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"C:\\svn\\radix.trunk1\\org.radixware\\ads\\SystemMonitor"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"ARTE serial no.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"ARTE serial no.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJGRIBFUJIVFNXHYLRI57G4B4R4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"C:\\svn\\radix.trunk1\\org.radixware\\ads\\SystemMonitor"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<нет>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<none>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJLUBYYOQQVF67AFKTUYGZSINOM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"C:\\svn\\radix.trunk1\\org.radixware\\ads\\SystemMonitor"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Сначала старые");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Oldest first");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsL6GKKVCRTVCKBGM2GBLMY4OYM4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"C:\\svn\\radix.trunk1\\org.radixware\\ads\\SystemMonitor"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Номер ARTE");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"ARTE no.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLMM2Q6AQHVAFRNKOTQ2S2ABORI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"C:\\svn\\radix.trunk1\\org.radixware\\ads\\SystemMonitor"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ид. инстанции");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Instance ID");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLYU2VUD56BGVFIS3NJC46ZM4ZM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"C:\\svn\\radix.trunk1\\org.radixware\\ads\\SystemMonitor"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ид. модуля");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Unit ID");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNVTLSMGASNDBPBRT3A6FQRAOWM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"C:\\svn\\radix.trunk1\\org.radixware\\ads\\SystemMonitor"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Номер ARTE");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"ARTE no.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOEEM3JUUUNDHRLMHRHWW5WTTBI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"C:\\svn\\radix.trunk1\\org.radixware\\ads\\SystemMonitor"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Время работы (с)");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Uptime (s)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQOQPVZLAAVHTVIUP3QBWVNQH24"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"C:\\svn\\radix.trunk1\\org.radixware\\ads\\SystemMonitor"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Время начала запроса");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Request start time");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQR7VBD44JNCVFG2VO77542ZJRY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"C:\\svn\\radix.trunk1\\org.radixware\\ads\\SystemMonitor"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Начало периода");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Period start");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQS6K7EXQ6JCTVFNGKPOOE2X24Q"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"C:\\svn\\radix.trunk1\\org.radixware\\ads\\SystemMonitor"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Тип потока");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Thread kind");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQXL3HPSTQ5H3PPVG43X4G5AVBA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"C:\\svn\\radix.trunk1\\org.radixware\\ads\\SystemMonitor"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"DB serial no.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"DB serial no.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRCUS43KE6ZEWLP7RL6KLPW7QUY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"C:\\svn\\radix.trunk1\\org.radixware\\ads\\SystemMonitor"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"За период");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"By period");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRKMWHT7CPJH3PFAG322SJ4LFNY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"C:\\svn\\radix.trunk1\\org.radixware\\ads\\SystemMonitor"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ид. потока");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Thread ID");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsS6KOVFQWJJCZLOWACVB5DW3HOE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"C:\\svn\\radix.trunk1\\org.radixware\\ads\\SystemMonitor"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ид. потока");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Thread ID");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSJKIP5BHMRDYXOJ3G7MODB3W4I"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"C:\\svn\\radix.trunk1\\org.radixware\\ads\\SystemMonitor"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Разница потребления Ext (мс)");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Ext usage diff (ms)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVWBLTNUUQFE2NKYZMQCDTPWX6M"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"C:\\svn\\radix.trunk1\\org.radixware\\ads\\SystemMonitor"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ид. сессии в БД");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"DB session ID");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWBN7TFP4Q5DCZL25LOXBSK4DOU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"C:\\svn\\radix.trunk1\\org.radixware\\ads\\SystemMonitor"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"История состояния потоков ARTE");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"ARTE threads state history");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWJELU3IUQVEXZNB7EH5THHPBTM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"C:\\svn\\radix.trunk1\\org.radixware\\ads\\SystemMonitor"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Разница потребления DB (мс)");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"DB usage diff (ms)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWPFHC4K63RC7XCA346WYXNGDYI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"C:\\svn\\radix.trunk1\\org.radixware\\ads\\SystemMonitor"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"История состояния потоков инстанции");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Instance threads state history");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWPMI3QM6L5GWDEOXIDSST3XP64"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"C:\\svn\\radix.trunk1\\org.radixware\\ads\\SystemMonitor"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Время");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Time");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWXWFWBCK55GXRLHY53CO4BGALQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"C:\\svn\\radix.trunk1\\org.radixware\\ads\\SystemMonitor"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<нет>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<none>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXEYCXRLNOJE4LD2525XZV3VNMI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"C:\\svn\\radix.trunk1\\org.radixware\\ads\\SystemMonitor"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<нет>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<none>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYMTROZA43JCSNJYYLWO5U6SSIQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"C:\\svn\\radix.trunk1\\org.radixware\\ads\\SystemMonitor"));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(InstanceStateHistory - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbaecRHY4TVWIC5C4TJN2GKL2ABRKKQ"),"InstanceStateHistory - Localizing Bundle",$$$items$$$);
}
