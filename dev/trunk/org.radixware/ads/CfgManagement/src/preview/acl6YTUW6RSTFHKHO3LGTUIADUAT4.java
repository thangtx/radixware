
/* Radix::CfgManagement::CfgPacket.Import - Server Executable*/

/*Radix::CfgManagement::CfgPacket.Import-Application Class*/

package org.radixware.ads.CfgManagement.server;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket.Import")
public published class CfgPacket.Import  extends org.radixware.ads.CfgManagement.server.CfgPacket  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {

	private boolean findLoop(CfgItem start, java.util.Map<CfgItem, java.util.List<CfgItem>> id2OutgoingRefs, java.util.List<CfgItem> loop) {
	    if(loop.contains(start)){
	        while(loop.get(0)!=start){
	            loop.remove(0);
	        }
	        loop.add(start);
	        return true;
	    }
	    java.util.List<CfgItem> next = id2OutgoingRefs.get(start);
	    if(next==null||next.isEmpty()){
	        return false;
	    }
	    
	    loop.add(start);
	    for(CfgItem item : next){
	        if(findLoop(item,id2OutgoingRefs,loop)){
	            return true;
	        }
	    }
	    loop.remove(loop.size()-1);
	    return false;    
	}
	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return CfgPacket.Import_mi.rdxMeta;}

	/*Radix::CfgManagement::CfgPacket.Import:Nested classes-Nested Classes*/

	/*Radix::CfgManagement::CfgPacket.Import:Properties-Properties*/

	/*Radix::CfgManagement::CfgPacket.Import:doNotRemoveAccept-Dynamic Property*/



	protected boolean doNotRemoveAccept=false;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket.Import:doNotRemoveAccept")
	private final  boolean getDoNotRemoveAccept() {
		return doNotRemoveAccept;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket.Import:doNotRemoveAccept")
	private final   void setDoNotRemoveAccept(boolean val) {
		doNotRemoveAccept = val;
	}



































	/*Radix::CfgManagement::CfgPacket.Import:Methods-Methods*/

	/*Radix::CfgManagement::CfgPacket.Import:onCommand_Accept-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket.Import:onCommand_Accept")
	  void onCommand_Accept (org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValHandlersById) {
		acceptTime = Utils::Timing.getCurrentTime();
		acceptUser = Arte::Arte.getUserName();
		doNotRemoveAccept = true;
		update();

		Arte::Trace.enterContext(Arte::EventContextType:CfgPackage, id);
		Arte::Trace.put(eventCode["User %1 accepted configuration package"], acceptUser);
		Arte::Trace.leaveContext(Arte::EventContextType:CfgPackage, id);

	}

	/*Radix::CfgManagement::CfgPacket.Import:link-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket.Import:link")
	public published  void link () {
		CfgItemsCursor c = CfgItemsCursor.open(id, false, null);
		try {
		    while (c.next()) {
		        c.item.link();
		        update();
		    }
		} finally {
		    c.close();
		}
		Arte::Trace.enterContext(Arte::EventContextType:CfgPackage, id);
		Arte::Trace.put(eventCode["Configuration package linked"]);
		Arte::Trace.leaveContext(Arte::EventContextType:CfgPackage, id);

	}

	/*Radix::CfgManagement::CfgPacket.Import:import-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket.Import:import")
	  void import (org.radixware.ads.CfgManagement.common.ImpExpXsd.Packet xml) {
		for (ImpExpXsd:Item xi : xml.ItemList) 
		    CfgItem.import(xi, this, null);

		link();

	}

	/*Radix::CfgManagement::CfgPacket.Import:onCommand_Relink-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket.Import:onCommand_Relink")
	public  org.radixware.kernel.server.types.FormHandler.NextDialogsRequest onCommand_Relink (org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValHandlersById) {
		link();
		int ambState = 0, newState = 0, brokenRef = 0, ambRef = 0;
		CfgItemsCursor c = CfgItemsCursor.open(id, false, null);
		try {
		    while (c.next()) {
		        if (c.item.objState != null)
		            switch (c.item.objState) {
		                case CfgObjState:Ambiguity:
		                    ambState++;
		                    break;
		                case CfgObjState:New:
		                    newState++;
		                    break;
		                case CfgObjState:Exists: ;   
		            }
		        if (c.item.allRefsState != null)
		            switch (c.item.allRefsState) {
		                case RefState:Broken:
		                    brokenRef++;
		                    break;
		                case RefState:Ambiguity:
		                    ambRef++;
		                    break;
		                case RefState:Ok:
		                    break;
		            }
		    }
		} finally {
		    c.close();
		}

		Str text = "<html><table>" 
		    + Str.format("<tr><td>Ambiguous objects:</td><td>%d</td></tr>...", ambState, newState, brokenRef, ambRef)
		    + "</table></html>";

		return new FormHandlerNextDialogsRequest(
		        new FormHandlerMessageBoxRequest(
		        (ambState + brokenRef + ambRef > 0 ? Client.Resources::DialogType:Error
		        : (newState > 0 ? Client.Resources::DialogType:Warning : Client.Resources::DialogType:Information)),
		        Client.Resources::DialogButtonType:Close, null,
		        text, null),
		        null);
	}

	/*Radix::CfgManagement::CfgPacket.Import:onCommand_Load-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket.Import:onCommand_Load")
	public  org.radixware.schemas.types.StrDocument onCommand_Load (org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValHandlersById) {
		if (acceptUser == null)
		    throw new InvalidEasRequestClientFault("Can't apply without preliminary execution of Accept command");
		if (System::System.loadByPK(1, true).dualControlForCfgMgmt == true && acceptUser == Arte::Arte.getUserName())
		    throw new InvalidEasRequestClientFault("Loading must be accepted by another user");

		Arte::Trace.enterContext(Arte::EventContextType:CfgPackage, id);
		Utils::Html rs;
		try {
		    Arte::Trace.put(eventCode["Configuration package application started"]);
		    removeAccept();
		    update();
		    Arte::Arte.commit();

		    CfgImportHelper.Base helper = new CfgImportHelper.Silent(ICfgImportHelper.Action.NEW);
		    rs = load(helper, "Apply Configuration");
		    
		    
		    final String colorCss;
		    final CfgPacketApplyStatus res = confirmLoad(helper);
		    final String resultAsStr = res.Title;
		    if (res == CfgPacketApplyStatus:APPLIED || res == CfgPacketApplyStatus:APPLIED_WITH_WARNINGS) {
		        Arte::Arte.commit();
		        helper.toTrace();
		        Arte::Trace.put(eventCode["Configuration package applied"]);
		        colorCss = res == CfgPacketApplyStatus:APPLIED ? "green" : "orange";
		    } else {
		        Arte::Arte.rollback();
		        helper.toTrace();
		        Arte::Trace.put(eventCode["Configuration package applying was cancelled"]);
		        colorCss = "red";
		    }
		    
		    Utils::HtmlTable statTable = (Utils::HtmlTable) rs.getChildAt(1).getChildAt(1);
		    Utils::HtmlTable.Row tr =  statTable.addRow();
		    tr.addCell().setInnerText("Operation status");
		    Utils::HtmlTable.DataCell cell = tr.addCell();
		    cell.setInnerText(resultAsStr);
		    cell.setCss("color", colorCss);
		} finally {
		    Arte::Trace.leaveContext(Arte::EventContextType:CfgPackage, id);
		}

		Arte::TypesXsd:StrDocument res = Arte::TypesXsd:StrDocument.Factory.newInstance();
		res.Str = rs.toString();
		return res;


	}

	/*Radix::CfgManagement::CfgPacket.Import:load-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket.Import:load")
	public published  void load () {
		CfgImportHelper.Silent helper = new CfgImportHelper.Silent(ICfgImportHelper.Action.NEW);
		load(helper, null);
	}

	/*Radix::CfgManagement::CfgPacket.Import:load-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket.Import:load")
	private final  org.radixware.kernel.common.html.Html load (org.radixware.ads.CfgManagement.server.CfgImportHelper.Base helper, Str progressCaption) {
		Client.Resources::ProgressDialogProcessResource progressProc = null;
		//int newObjs = 0, updatedObjs = 0;
		CfgItemsCursor c = CfgItemsCursor.open(id, false, null);
		try {
		    if (progressCaption != null)
		        progressProc = helper.createLoadProcessProgressBar(progressCaption);

		    java.util.List<CfgItem> allItems = new java.util.ArrayList<>();

		    while (c.next()) {
		        if (c.item.shouldSkip())
		            continue;
		        if (c.item.objState != null)
		            switch (c.item.objState) {
		                case CfgObjState:Ambiguity:
		                    throw new InvalidEasRequestClientFault(Str.format("Configuration item '%s' associated to an object ambiguously", c.item.calcTitle()));
		                case CfgObjState:New:
		                    //newObjs++;
		                    break;
		                default:
		                //updatedObjs++;

		            }
		        c.item.calcAllRefsState();
		        if (c.item.allRefsState != null)
		            switch (c.item.allRefsState) {
		                case RefState:Broken:
		                    throw new InvalidEasRequestClientFault(Str.format("Configuration item '%s' has broken link", c.item.calcTitle()));
		                case RefState:Ambiguity:
		                    throw new InvalidEasRequestClientFault(Str.format("Configuration item '%s' has ambiguous link", c.item.calcTitle()));
		                case RefState:Ok: ;
		            }
		        allItems.add(c.item);
		    }

		//perform topological sorting items by threi refs
		    final java.util.Map<CfgItem, java.util.List<CfgItem>> id2OutgoingRefs = new java.util.HashMap<>();
		    final java.util.Map<CfgItem, java.util.List<CfgItem>> id2IncomingRefs = new java.util.HashMap<>();

		    final java.util.List<CfgItem> itemsWithNoIncomingRefs = new java.util.ArrayList<>(allItems);
		    final java.util.List<CfgItem> sortedList = new java.util.ArrayList<>();

		    for (CfgItem item : allItems) {
		        CfgItemRef[] refs = item.getAllRefs();
		        java.util.List<CfgItem> predecessors = new java.util.LinkedList<CfgItem>();
		        if (refs != null) {
		            //1. fill list if referenced items instead of list of references        
		            for (int i = 0; i < refs.length; i++) {
		                if (refs[i] == null || refs[i].type != RefType:Internal) {
		                    continue;
		                }
		                CfgItem item2 = refs[i].intRef;
		                if (item2 == null || item2.shouldSkip()) {
		                    continue;
		                }
		                if (!predecessors.contains(item2))
		                    predecessors.add(item2);
		            }
		        }

		        java.util.List<CfgItem> internalDeps = item.getInternalDependencies();
		        if (internalDeps != null) {
		            for (CfgItem dep : internalDeps) {
		                if (dep == null || dep.shouldSkip()) {
		                    continue;
		                }
		                if (!predecessors.contains(dep))
		                    predecessors.add(dep);
		            }
		        }

		        if (item.parent != null && !item.parent.shouldSkip()) {
		            CfgItem parentItem = item.parent;
		            if (!predecessors.contains(parentItem))
		                predecessors.add(parentItem);
		        }

		        if (!predecessors.isEmpty()) {
		            itemsWithNoIncomingRefs.remove(item);
		            id2IncomingRefs.put(item, predecessors);

		            for (CfgItem item2 : predecessors) {
		                java.util.List<CfgItem> followers = id2OutgoingRefs.get(item2);
		                if (followers == null) {
		                    followers = new java.util.LinkedList<CfgItem>();
		                    id2OutgoingRefs.put(item2, followers);
		                }
		                if (!followers.contains(item)) {
		                    followers.add(item);
		                }
		            }
		        }
		    }

		    final java.util.PriorityQueue<CfgItem> rootItems = new java.util.PriorityQueue<>(11, new java.util.Comparator<CfgItem>() {
		        @Override
		        public int compare(CfgItem o1, CfgItem o2) {
		            return o1.id.compareTo(o2.id);
		        }
		    });
		    rootItems.addAll(itemsWithNoIncomingRefs);

		    while (!rootItems.isEmpty()) {
		        CfgItem item = rootItems.poll();
		        sortedList.add(item);
		        java.util.List<CfgItem> linkedItems = id2OutgoingRefs.remove(item);
		        if (linkedItems != null) {
		            for (CfgItem ref : new java.util.ArrayList<>(linkedItems)) {
		                linkedItems.remove(ref);
		                java.util.List<CfgItem> incomingRefsForItem = id2IncomingRefs.get(ref);
		                if (incomingRefsForItem != null) {
		                    incomingRefsForItem.remove(item);
		                    if (incomingRefsForItem.isEmpty()) {
		                        rootItems.add(ref);
		                    }
		                }
		            }
		        }
		    }

		    if (!id2OutgoingRefs.isEmpty()) {
		        java.util.List<CfgItem> loopedItems = new java.util.LinkedList<>();

		        for (CfgItem item : id2OutgoingRefs.keySet()) {
		            if (findLoop(item, id2OutgoingRefs, loopedItems)) {
		                break;
		            }
		        }
		        StringBuilder sb = new java.lang.StringBuilder();
		        boolean first = true;

		        for (CfgItem item : loopedItems) {
		            if (first) {
		                first = false;
		            } else {
		                sb.append(" -> ");
		            }
		            sb.append(item.calcTitle());
		        }

		        throw new InvalidEasRequestClientFault(Str.format("Circular dependence detected:...", sb.toString()));
		    }

		    int currProcessed = 1;
		    final int totalCnt = sortedList.size();
		    helper.setContextPacket(this);
		    helper.getUserFuncImportHelper().setCompileDeferred(true);
		    for (CfgItem item : sortedList) {
		        try {
		            helper.setContextItem(item);
		            final ICfgImportSettings settings;
		            if (item.settings != null) {
		                settings = new CfgImportSettings(item.settings);
		            } else {
		                settings = null;
		            }
		            helper.setImportSettings(settings);

		            if (progressProc != null) {
		                progressProc.set(Str.format("Load item: %s (%d of %d items processed)",
		                        item.calcTitle(), currProcessed, totalCnt),
		                        ((float) currProcessed / totalCnt) * 100, true);
		                if (progressProc.checkIsCancelled())
		                    throw new InvalidEasRequestClientFault("Canceled");
		            }
		            item.load(helper);
		        } catch (Exceptions::Exception e) {
		            String message = "Configuration item '%s' invalid: %s";
		            if (e instanceof Exceptions::EntityObjectNotExistsError) {
		                message += ('\n' + "Try to use the Re-link command");
		            }
		            final String excInfoStr = e.getMessage() != null ? e.getMessage() : e.getClass().getName();
		            throw new AppError(Str.format(message, item.calcTitle(), excInfoStr), e);
		        }
		        update();
		        currProcessed++;
		    }
		    helper.finalizeImport();
		} catch (Exceptions::InterruptedException | Exceptions::ResourceUsageException | Exceptions::ResourceUsageTimeout e) {
		    throw new AppError("", e);
		} finally {
		    c.close();
		    try {
		        if (progressProc != null)
		            progressProc.close();
		    } catch (Exceptions::Exception e) {
		    }
		}

		return buildLoadResultHtml(helper, progressCaption, false);
	}

	/*Radix::CfgManagement::CfgPacket.Import:onCommand_Check-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket.Import:onCommand_Check")
	public  org.radixware.schemas.types.StrDocument onCommand_Check (org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValHandlersById) {
		CfgImportHelper.Silent helper = new CfgImportHelper.Silent(ICfgImportHelper.Action.NEW);
		Arte::Trace.enterContext(Arte::EventContextType:CfgPackage, id);
		Utils::Html rs;
		try {
		    rs = load(helper, "Configuration Checking");
		    Arte::Arte.rollback();
		    Arte::Trace.put(eventCode["Configuration package checked"]);
		} finally {
		    Arte::Trace.leaveContext(Arte::EventContextType:CfgPackage, id);
		}

		Arte::TypesXsd:StrDocument res = Arte::TypesXsd:StrDocument.Factory.newInstance();
		res.Str = rs.toString();
		return res;
	}

	/*Radix::CfgManagement::CfgPacket.Import:removeAccept-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket.Import:removeAccept")
	  void removeAccept () {
		if (acceptUser != null) {
		    acceptTime = null;
		    acceptUser = null;
		    Arte::Trace.enterContext(Arte::EventContextType:CfgPackage, id);
		    Arte::Trace.put(eventCode["Accept canceled"]);
		    Arte::Trace.leaveContext(Arte::EventContextType:CfgPackage, id);
		}
	}

	/*Radix::CfgManagement::CfgPacket.Import:onUpdate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket.Import:onUpdate")
	  void onUpdate () {
		if (!doNotRemoveAccept)
		    removeAccept();
		doNotRemoveAccept = false;
		super.onUpdate();

	}

	/*Radix::CfgManagement::CfgPacket.Import:import-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket.Import:import")
	public static published  org.radixware.ads.CfgManagement.server.CfgPacket.Import import (org.radixware.ads.CfgManagement.common.ImpExpXsd.PacketDocument xml) {
		CfgPacket.Import p = new CfgPacket.Import();
		p.init();
		p.srcPacketId = xml.Packet.Id;
		p.srcExpTime = xml.Packet.ExpTime;
		p.srcExpUser = xml.Packet.ExpUser;
		p.srcAppVer = xml.Packet.AppVer;
		p.srcDbUrl = xml.Packet.DbUrl;
		p.title = xml.Packet.Title;
		p.notes = xml.Packet.Notes;
		p.create();

		p.import(xml.Packet);

		return p;
	}

	/*Radix::CfgManagement::CfgPacket.Import:check-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket.Import:check")
	public published  void check () {
		CfgImportHelper.Silent helper = new CfgImportHelper.Silent(ICfgImportHelper.Action.NEW);
		load(helper, null);
		Arte::Arte.rollback();

	}

	/*Radix::CfgManagement::CfgPacket.Import:confirmLoad-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket.Import:confirmLoad")
	private final  org.radixware.ads.CfgManagement.common.CfgPacketApplyStatus confirmLoad (org.radixware.ads.CfgManagement.server.CfgImportHelper.Base helper) {
		final Utils::Html warningsHtml = buildLoadResultHtml(helper, null, true);
		if (warningsHtml != null) {
		    try {
		        if (Client.Resources::MessageDialogResource.confirmation(Arte::Arte.getInstance(), "Problems", warningsHtml.toString())) {
		            return CfgPacketApplyStatus:APPLIED_WITH_WARNINGS;
		        } else {
		            return CfgPacketApplyStatus:CANCELLED_BY_USER;
		        }
		    } catch (Exceptions::InterruptedException | Exceptions::ResourceUsageException | Exceptions::ResourceUsageTimeout e) {
		        throw new AppError(e.getMessage());
		    }
		} else {
		    return CfgPacketApplyStatus:APPLIED;
		}
	}

	/*Radix::CfgManagement::CfgPacket.Import:buildLoadResultHtml-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket.Import:buildLoadResultHtml")
	private final  org.radixware.kernel.common.html.Html buildLoadResultHtml (org.radixware.ads.CfgManagement.server.CfgImportHelper.Base helper, Str operationTitle, boolean forConfirmation) {
		Utils::Html head = new Html("head");
		Utils::Html meta = new Html("meta");

		meta.setAttr("content", "text/html; charset=utf-8");
		meta.setAttr("http-equiv", "Content-Type");
		head.add(meta);
		Utils::Html html = new Html("html");

		html.add(head);
		Utils::Html body = new Html("body");
		html.add(body);

		if (!forConfirmation) {
		    Utils::Html statHeader = new Html("h4");
		    statHeader.setInnerText("Statistics:");
		    body.add(statHeader);
		    
		    Utils::HtmlTable generalInfoTable = CfgCommonUtils.createHtmlTable();
		    
		    Utils::HtmlTable.Row row = generalInfoTable.addRow();
		    row.addCell().setInnerText("Package");
		    row.addCell().setInnerText(title);
		    
		    row = generalInfoTable.addRow();
		    row.addCell().setInnerText("Date");
		    final java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("dd MMM yyyy HH:mm:ss", Arte::Arte.getClientLocale());
		    row.addCell().setInnerText(format.format(new DateTime(System.currentTimeMillis())));
		    
		    row = generalInfoTable.addRow();
		    row.addCell().setInnerText("Operation");
		    row.addCell().setInnerText(operationTitle);
		    
		    row = generalInfoTable.addRow();
		    row.addCell().setInnerText("Objects created");
		    row.addCell().setInnerText(Str.valueOf(helper.getObjsCountByKind(CfgImportHelper.Base.ResultKind.NEW)));
		    
		    row = generalInfoTable.addRow();
		    row.addCell().setInnerText("Objects updated");
		    row.addCell().setInnerText(Str.valueOf(helper.getObjsCountByKind(CfgImportHelper.Base.ResultKind.UPDATED)));
		    
		    row = generalInfoTable.addRow();
		    row.addCell().setInnerText("Warnings received");
		    row.addCell().setInnerText(Str.valueOf(helper.getObjsCountByKind(CfgImportHelper.Base.ResultKind.WARNINGS)));
		    
		    body.add(generalInfoTable);
		}

		final Utils::HtmlTable items;
		if (forConfirmation) {
		    items = helper.getResultsTable(true);
		    if (items == null || items.isEmpty()) {
		        return null;
		    }
		} else {
		    items = helper.getResultsTable(false);
		}
		if (items != null && !items.isEmpty()) {
		    Utils::Html itemsHeader = new Html("h4");
		    itemsHeader.setInnerText("Details:");
		    body.add(itemsHeader);
		    body.add(items);
		}

		if (forConfirmation) {
		    Utils::Html commitQuestion = new Html("h4");
		    commitQuestion.setInnerText("Problems occurred when applying the package. Do you want to apply the changes?");
		    body.add(commitQuestion);
		}
		return html;
	}






	@Override
	public org.radixware.kernel.server.types.FormHandler.NextDialogsRequest execCommand(org.radixware.kernel.common.types.Id cmdId, org.radixware.kernel.common.types.Id propId, org.apache.xmlbeans.XmlObject input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValsById, org.apache.xmlbeans.XmlObject output) throws org.radixware.kernel.common.exceptions.AppException,java.lang.InterruptedException{
		if(cmdId == cmdCGYLNR7DDVHDJPBSNLTFQ3P7GM){
			org.radixware.schemas.types.StrDocument result = onCommand_Load(newPropValsById);
			if(result != null)
				output.set(result);
			return null;
		} else if(cmdId == cmdGSSME7MUGVHM3LLVIQMKPULOZ4){
			onCommand_Accept(newPropValsById);
			return null;
		} else if(cmdId == cmdTSZWS2FC3ZBRRCTMBOKPSIA43M){
			org.radixware.kernel.server.types.FormHandler.NextDialogsRequest result = onCommand_Relink(newPropValsById);
		return result;
	} else if(cmdId == cmdX7RVL2KDZFCYPFFGOARQNX3RT4){
		org.radixware.schemas.types.StrDocument result = onCommand_Check(newPropValsById);
		if(result != null)
			output.set(result);
		return null;
	} else 
		return super.execCommand(cmdId,propId,input,newPropValsById,output);
}


}

/* Radix::CfgManagement::CfgPacket.Import - Server Meta*/

/*Radix::CfgManagement::CfgPacket.Import-Application Class*/

package org.radixware.ads.CfgManagement.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class CfgPacket.Import_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("acl6YTUW6RSTFHKHO3LGTUIADUAT4"),"CfgPacket.Import",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMGGRKV6IA5DBXJFVXV36IS3YIQ"),

						org.radixware.kernel.common.enums.EClassType.APPLICATION,

						/*Radix::CfgManagement::CfgPacket.Import:Presentations-Entity Object Presentations*/
						/*Class presentation attributes*/
						 new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef(/*Owner Class Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("acl6YTUW6RSTFHKHO3LGTUIADUAT4"),
							/*Owner Class Name*/
							"CfgPacket.Import",
							/*Title Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMGGRKV6IA5DBXJFVXV36IS3YIQ"),
							/*Property presentations*/

							/*Radix::CfgManagement::CfgPacket.Import:Properties-Properties*/
							null,
							/*Commands*/
							new org.radixware.kernel.server.meta.presentations.RadCommandDef[]{

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::CfgManagement::CfgPacket.Import:Relink-Object Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdTSZWS2FC3ZBRRCTMBOKPSIA43M"),"Relink",org.radixware.kernel.common.enums.ECommandScope.OBJECT,null,org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_FIXED,org.radixware.kernel.common.enums.ECommandNature.XML_IN_FORM_OUT,org.radixware.kernel.common.types.Id.Factory.loadFrom("acl6YTUW6RSTFHKHO3LGTUIADUAT4"),false,true),

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::CfgManagement::CfgPacket.Import:Check-Object Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdX7RVL2KDZFCYPFFGOARQNX3RT4"),"Check",org.radixware.kernel.common.enums.ECommandScope.OBJECT,null,org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_FIXED,org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,org.radixware.kernel.common.types.Id.Factory.loadFrom("acl6YTUW6RSTFHKHO3LGTUIADUAT4"),false,true),

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::CfgManagement::CfgPacket.Import:Accept-Object Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdGSSME7MUGVHM3LLVIQMKPULOZ4"),"Accept",org.radixware.kernel.common.enums.ECommandScope.OBJECT,null,org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_FIXED,org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,org.radixware.kernel.common.types.Id.Factory.loadFrom("acl6YTUW6RSTFHKHO3LGTUIADUAT4"),false,true),

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::CfgManagement::CfgPacket.Import:Load-Object Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdCGYLNR7DDVHDJPBSNLTFQ3P7GM"),"Load",org.radixware.kernel.common.enums.ECommandScope.OBJECT,null,org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_FIXED,org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,org.radixware.kernel.common.types.Id.Factory.loadFrom("acl6YTUW6RSTFHKHO3LGTUIADUAT4"),false,true)
							},
							/*Sortings*/
							null,
							/*Filters*/
							null,
							/*Editor presentations*/
							new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef[]{

									new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef(
									/*Radix::CfgManagement::CfgPacket.Import:General-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("epr72R6YVIN6RFHHKJZMJEK36TOO4"),
									"General",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprJHOQCOZIOBBWNABEIAT5XEG7M4"),
									35984,
									null,

									/*Radix::CfgManagement::CfgPacket.Import:General:Children-Explorer Items*/
										new org.radixware.kernel.server.meta.presentations.RadExplorerItemDef[]{

												/*Radix::CfgManagement::CfgPacket.Import:General:CfgItemImp-Child Ref Explorer Item*/

												new org.radixware.kernel.server.meta.presentations.RadChildRefExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiTZC7PNQ2F5DC3PZ77PEATNVWHY"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("acl6YTUW6RSTFHKHO3LGTUIADUAT4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ4WZRLJ23BGMBKQ2JBSOBEGBP4"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("sprE6NT4GPHVREKJJMNK6YDGWAZUQ"),
													new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>",new org.radixware.kernel.server.meta.presentations.RadConditionDef.Prop2ValueCondition(new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colJ5L5OI3X4BHYBPJFGAWUO4MKZM")},new String[]{null}),"org.radixware"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("refF34ZWBF4TBBCHIUDOFBRI67OVY"),
													null,
													null)
										}
									,
									org.radixware.kernel.server.types.Restrictions.Factory.newInstance(0,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdTSZWS2FC3ZBRRCTMBOKPSIA43M"),org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdGSSME7MUGVHM3LLVIQMKPULOZ4")},null,null),
									(org.radixware.kernel.server.meta.presentations.RadPropertyPresentationAttributes[]) null,
									org.radixware.kernel.common.enums.EEditorPresentationRightsInheritanceMode.FROM_REPLACED,
									null)
							},
							/*Selector presentations*/
							null,
							/*Default selector presentation Id*/
							null,
							/*Presentation Map*/
							org.radixware.kernel.common.utils.Maps.fromArrays(new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprJHOQCOZIOBBWNABEIAT5XEG7M4")},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr72R6YVIN6RFHHKJZMJEK36TOO4")}),
							/*Object title format*/
							null,
							/*Class catalogs*/
							null,
							/*Restrictions*/
							null,null,0),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblWMN3P5J5ONCFPLJVGBFAKZXZXU"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWMN3P5J5ONCFPLJVGBFAKZXZXU"),

						/*Radix::CfgManagement::CfgPacket.Import:Properties-Properties*/
						new org.radixware.kernel.server.meta.clazzes.RadPropDef[]{

								/*Radix::CfgManagement::CfgPacket.Import:doNotRemoveAccept-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd2XDRMD2VKVFPLC5SZHGJBYEQAA"),"doNotRemoveAccept",null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE)
						},

						/*Radix::CfgManagement::CfgPacket.Import:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthcmdGSSME7MUGVHM3LLVIQMKPULOZ4"),"onCommand_Accept",false,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("newPropValHandlersById",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprBNARVHDUHBBUVBRA6AFIB3WDVY"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthON2LJLUJF5FXLJRA56OFFX2VDI"),"link",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthNC5CDGJSRJDZHLLGI7C3UNRPRI"),"import",false,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("xml",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprB6TWZV6VMZHBLCQKELRLPKAAAU"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthcmdTSZWS2FC3ZBRRCTMBOKPSIA43M"),"onCommand_Relink",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("newPropValHandlersById",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr4TLPSGB2N5HLTNZ5VJACRCXVFI"))
								},org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthcmdCGYLNR7DDVHDJPBSNLTFQ3P7GM"),"onCommand_Load",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("newPropValHandlersById",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprA3VOXGY44NAN7MBIDDZR4KPZFI"))
								},org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthBQ6MHRGSEBAYBE4RWJOM3DKA6E"),"load",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthZEFXXSN6QFGWVK4RXOZYOQUNQI"),"load",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("helper",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprKOXGQMIM5VASFIIXD4NYLURIPU")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("progressCaption",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprQJZMTKBSIRC5RBGO36R5PAT2NU"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthcmdX7RVL2KDZFCYPFFGOARQNX3RT4"),"onCommand_Check",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("newPropValHandlersById",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprXDBYIFXXQVBARLZOTCAKRSBZ2M"))
								},org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth3KUFWG3WBBC4PEPECHP65JCRUI"),"removeAccept",false,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthQ7CATQH4YFELHHLR7F5B5Q2YBA"),"onUpdate",false,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthTFWB46W2QFGYNATKZ6MUSDESZA"),"import",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("xml",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprHDGSACID6BEIBHQQI4Y5XKFK3U"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthKME4MM77P5GHVAS3CV4PGC4FZA"),"check",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthOCX5N4N67VAM5H4OWKKG2WX5FI"),"confirmLoad",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("helper",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprFSMUG4YAOFDIHO3DCYHL2JXZHM"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthSRTMF4FKNFAAROPEOEH5OFVB7Y"),"buildLoadResultHtml",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("helper",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprHLQPFBVTKVB3HP3KSIQYZLBVAM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("operationTitle",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprJS2XF32Y7RA6XEDS4RG2CYLVUQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("forConfirmation",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprLCHEV3QODJABRKJQSATCPZ6TMA"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS)
						},
						null,
						null,null,null,false);
}

/* Radix::CfgManagement::CfgPacket.Import - Desktop Executable*/

/*Radix::CfgManagement::CfgPacket.Import-Application Class*/

package org.radixware.ads.CfgManagement.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket.Import")
public interface CfgPacket.Import   extends org.radixware.ads.CfgManagement.explorer.CfgPacket  {







































































	public static class Load extends org.radixware.kernel.common.client.models.items.Command{
		protected Load(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public org.radixware.schemas.types.StrDocument send() throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,null);
			org.apache.xmlbeans.XmlObject xmlResponse = processResponse(response, null);
			return (org.radixware.schemas.types.StrDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),xmlResponse,org.radixware.schemas.types.StrDocument.class);
		}

	}

	public static class Accept extends org.radixware.kernel.common.client.models.items.Command{
		protected Accept(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public void send() throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,null);
			processResponse(response, null);
		}

	}

	public static class Relink extends org.radixware.kernel.common.client.models.items.Command{
		protected Relink(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public void send() throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,null);
			processResponse(response, null);
		}

	}

	public static class Check extends org.radixware.kernel.common.client.models.items.Command{
		protected Check(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public org.radixware.schemas.types.StrDocument send() throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,null);
			org.apache.xmlbeans.XmlObject xmlResponse = processResponse(response, null);
			return (org.radixware.schemas.types.StrDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),xmlResponse,org.radixware.schemas.types.StrDocument.class);
		}

	}



}

/* Radix::CfgManagement::CfgPacket.Import - Desktop Meta*/

/*Radix::CfgManagement::CfgPacket.Import-Application Class*/

package org.radixware.ads.CfgManagement.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class CfgPacket.Import_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::CfgManagement::CfgPacket.Import:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("acl6YTUW6RSTFHKHO3LGTUIADUAT4"),
			"Radix::CfgManagement::CfgPacket.Import",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWMN3P5J5ONCFPLJVGBFAKZXZXU"),
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMGGRKV6IA5DBXJFVXV36IS3YIQ"),null,null,0,

			/*Radix::CfgManagement::CfgPacket.Import:Properties-Properties*/
			null,
			new org.radixware.kernel.common.client.meta.RadCommandDef[]{
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::CfgManagement::CfgPacket.Import:Relink-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdTSZWS2FC3ZBRRCTMBOKPSIA43M"),
						"Relink",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acl6YTUW6RSTFHKHO3LGTUIADUAT4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGSBYICCQOBEZTC3GFFVDTQF5EI"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("imgMXOBAWAW6VDYBOLG7ZKJTCTCGU"),
						null,
						org.radixware.kernel.common.enums.ECommandNature.XML_IN_FORM_OUT,
						true,
						true,
						false,
						org.radixware.kernel.common.enums.ECommandScope.OBJECT,
						null,
						org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_FIXED,
						true),
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::CfgManagement::CfgPacket.Import:Check-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdX7RVL2KDZFCYPFFGOARQNX3RT4"),
						"Check",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acl6YTUW6RSTFHKHO3LGTUIADUAT4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsN7NPGFUGEVEKLBSV3LXZMZJ7AU"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("imgF37SZTYSFFAFROGYEF4YEENOOI"),
						null,
						org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,
						true,
						true,
						false,
						org.radixware.kernel.common.enums.ECommandScope.OBJECT,
						null,
						org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_FIXED,
						true),
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::CfgManagement::CfgPacket.Import:Accept-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdGSSME7MUGVHM3LLVIQMKPULOZ4"),
						"Accept",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acl6YTUW6RSTFHKHO3LGTUIADUAT4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5G52K6DMI5B5LO6GB4WEMZW4CM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("imgWTSKYYJWOBCOJNGQJZN4OYDBNM"),
						null,
						org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,
						true,
						true,
						false,
						org.radixware.kernel.common.enums.ECommandScope.OBJECT,
						null,
						org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_FIXED,
						true),
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::CfgManagement::CfgPacket.Import:Load-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdCGYLNR7DDVHDJPBSNLTFQ3P7GM"),
						"Load",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acl6YTUW6RSTFHKHO3LGTUIADUAT4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEZLU6JADYVHDTOQBRHKEMJRHGU"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("imgOQFNHNXFXNB6PIEQS2W3W4FWZU"),
						null,
						org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,
						true,
						true,
						false,
						org.radixware.kernel.common.enums.ECommandScope.OBJECT,
						null,
						org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_FIXED,
						true)
			},
			null,
			null,
			null,
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr72R6YVIN6RFHHKJZMJEK36TOO4")},
			true,false,false);
}

/* Radix::CfgManagement::CfgPacket.Import - Web Meta*/

/*Radix::CfgManagement::CfgPacket.Import-Application Class*/

package org.radixware.ads.CfgManagement.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class CfgPacket.Import_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::CfgManagement::CfgPacket.Import:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("acl6YTUW6RSTFHKHO3LGTUIADUAT4"),
			"Radix::CfgManagement::CfgPacket.Import",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWMN3P5J5ONCFPLJVGBFAKZXZXU"),
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMGGRKV6IA5DBXJFVXV36IS3YIQ"),null,null,0,
			null,
			null,
			null,
			null,
			null,
			null,
			false,false,false);
}

/* Radix::CfgManagement::CfgPacket.Import:General - Desktop Meta*/

/*Radix::CfgManagement::CfgPacket.Import:General-Editor Presentation*/

package org.radixware.ads.CfgManagement.explorer;
public final class General_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epr72R6YVIN6RFHHKJZMJEK36TOO4"),
	"General",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("eprJHOQCOZIOBBWNABEIAT5XEG7M4"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("acl6YTUW6RSTFHKHO3LGTUIADUAT4"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tblWMN3P5J5ONCFPLJVGBFAKZXZXU"),
	null,
	null,

	/*Radix::CfgManagement::CfgPacket.Import:General:Editor Pages-Editor Presentation Pages*/
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

			/*Radix::CfgManagement::CfgPacket.Import:General:General-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgLB467MBHIRAKXEEF2YUVLYF5GI"),"General",org.radixware.kernel.common.types.Id.Factory.loadFrom("acl6YTUW6RSTFHKHO3LGTUIADUAT4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHY5LIEN2ZFHQJG3ED5346EWCGE"),null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDZOPCTNIURBCBPLCP75JMZCFYM"),0,0,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colT6AXXF6DBVAQDFOGFG4QARZYJU"),0,2,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colCQOZYZGSGZBLNNG6KX255SLN5Q"),0,7,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4ESP7TDW4JGG7FZ4PMVZ4IUO74"),0,8,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMCRBKZP4GNADJJLP2J7ITEGELY"),1,8,1,true,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colAJ6PVNFK3FBIVPACM2HG6NHSMU"),0,3,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNLZ5BGYZ7BGVRCQYC6WJWZIBM4"),0,4,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLQHEBRDALNHZHNCQB76A3BEP6U"),1,6,1,true,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6GQBFOYQYFCABDAMANXVXFGU3Q"),0,6,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colCY7ILVPBW5FCRBUUHJPC7LRBJY"),0,5,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLMJ5EPIAOVAPHJFUNG2BTNME6A"),0,9,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEF5WPJPV7JBATHO6TXOMAGWJEI"),1,9,1,true,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdT42SQ7RCQ5CYRIIBFO7TVMVA4A"),0,1,2,false,false)
			},null),

			/*Radix::CfgManagement::CfgPacket.Import:General:Items-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadContainerEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgICTYXZ3BK5A3DMAG42WNJCFXLY"),"Items",null,null,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiTZC7PNQ2F5DC3PZ77PEATNVWHY"))
	},
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgLB467MBHIRAKXEEF2YUVLYF5GI")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgICTYXZ3BK5A3DMAG42WNJCFXLY")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgROII5GGFWJGS3AHGCDQTDEQ3ME"))}
	,

	/*Radix::CfgManagement::CfgPacket.Import:General:Children-Explorer Items*/
		new org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemDef[]{

				/*Radix::CfgManagement::CfgPacket.Import:General:CfgItemImp-Child Ref Explorer Item*/

				new org.radixware.kernel.common.client.meta.explorerItems.RadChildRefExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiTZC7PNQ2F5DC3PZ77PEATNVWHY"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("acl6YTUW6RSTFHKHO3LGTUIADUAT4"),null,
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ4WZRLJ23BGMBKQ2JBSOBEGBP4"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("sprE6NT4GPHVREKJJMNK6YDGWAZUQ"),
					0,
					null,
					16560,false)
		}
	, new 
	org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[]{new org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings(org.radixware.kernel.common.types.Id.Factory.loadFrom("epr72R6YVIN6RFHHKJZMJEK36TOO4"),
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiTZC7PNQ2F5DC3PZ77PEATNVWHY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiC3WF6WEXGZDUTAKXY4G3E4LNUE")},null)}
	,
	null,
	0,
	null,
	(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
	35984,0,0);
}
/* Radix::CfgManagement::CfgPacket.Import:General:Model - Desktop Executable*/

/*Radix::CfgManagement::CfgPacket.Import:General:Model-Entity Model Class*/

package org.radixware.ads.CfgManagement.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket.Import:General:Model")
public class General:Model  extends org.radixware.ads.CfgManagement.explorer.CfgPacket.Import.CfgPacket.Import_DefaultModel.eprJHOQCOZIOBBWNABEIAT5XEG7M4_ModelAdapter {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return General:Model_mi.rdxMeta; }



	public General:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::CfgManagement::CfgPacket.Import:General:Model:Nested classes-Nested Classes*/

	/*Radix::CfgManagement::CfgPacket.Import:General:Model:Properties-Properties*/

	/*Radix::CfgManagement::CfgPacket.Import:General:Model:pkgState-Presentation Property*/




	public class PkgState extends org.radixware.ads.CfgManagement.explorer.General:Model.PkgState{
		public PkgState(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.ads.CfgManagement.common.CfgPacketState dummy = x == null ? null : (x instanceof org.radixware.ads.CfgManagement.common.CfgPacketState ? (org.radixware.ads.CfgManagement.common.CfgPacketState)x : org.radixware.ads.CfgManagement.common.CfgPacketState.getForValue((Str)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.STR)));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.ads.CfgManagement.common.CfgPacketState> getValClass(){
			return org.radixware.ads.CfgManagement.common.CfgPacketState.class;
		}

		/*Radix::CfgManagement::CfgPacket.Import:General:Model:pkgState:EmbeddedClass-Property Presentation Embedded Class*/




		/*Radix::CfgManagement::CfgPacket.Import:General:Model:pkgState:Nested classes-Nested Classes*/

		/*Radix::CfgManagement::CfgPacket.Import:General:Model:pkgState:Properties-Properties*/

		/*Radix::CfgManagement::CfgPacket.Import:General:Model:pkgState:Methods-Methods*/

		/*Radix::CfgManagement::CfgPacket.Import:General:Model:pkgState:isVisible-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket.Import:General:Model:pkgState:isVisible")
		public published  boolean isVisible () {
			return super.isVisible() && !isNew();
		}

		/*Radix::CfgManagement::CfgPacket.Import:General:Model:pkgState:isReadonly-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket.Import:General:Model:pkgState:isReadonly")
		public published  boolean isReadonly () {
			return true;
		}


		@Override
		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket.Import:General:Model:pkgState")
		public  org.radixware.ads.CfgManagement.common.CfgPacketState getValue() {
			return super.Value;
		}

		@Override
		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket.Import:General:Model:pkgState")
		public   void setValue(org.radixware.ads.CfgManagement.common.CfgPacketState val) {
			super.Value = val;
		}
	}
	public PkgState getPkgState(){return (PkgState)getProperty(colDKBRCEEYTFDBBAQ3ZE5GOMIXR4);}








	/*Radix::CfgManagement::CfgPacket.Import:General:Model:Methods-Methods*/

	/*Radix::CfgManagement::CfgPacket.Import:General:Model:onCommand_Accept-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket.Import:General:Model:onCommand_Accept")
	protected  void onCommand_Accept (org.radixware.ads.CfgManagement.explorer.CfgPacket.Import.Accept command) {
		try {
		    command.send();
		} catch (Exceptions::Exception e) {
		    Environment.processException(e);
		}


	}

	/*Radix::CfgManagement::CfgPacket.Import:General:Model:onCommand_Relink-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket.Import:General:Model:onCommand_Relink")
	protected  void onCommand_Relink (org.radixware.ads.CfgManagement.explorer.CfgPacket.Import.Relink command) {
		try {
		    command.send();
		    if (getView() != null)
		        getView().reread();
		} catch (Exceptions::InterruptedException e) {
		    showException(e);
		} catch (Exceptions::ServiceClientException e) {
		    showException(e);
		}
	}

	/*Radix::CfgManagement::CfgPacket.Import:General:Model:onCommand_Load-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket.Import:General:Model:onCommand_Load")
	protected  void onCommand_Load (org.radixware.ads.CfgManagement.explorer.CfgPacket.Import.Load command) {
		try {
		    Common.Dlg::ClientUtils.viewText(command.send(), "Result");
		    if (getView() != null)
		        getView().reread();
		} catch (Exceptions::InterruptedException e) {
		} catch (Exceptions::ServiceClientException e) {
		    showException(e);
		}
	}

	/*Radix::CfgManagement::CfgPacket.Import:General:Model:onCommand_Check-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket.Import:General:Model:onCommand_Check")
	protected  void onCommand_Check (org.radixware.ads.CfgManagement.explorer.CfgPacket.Import.Check command) {
		try {
		    Common.Dlg::ClientUtils.viewText(command.send(), "Check Result");
		} catch (Exceptions::InterruptedException e) {
		} catch (Exceptions::ServiceClientException e) {
		    showException(e);
		}
	}

	/*Radix::CfgManagement::CfgPacket.Import:General:Model:afterOpenEditorPageView-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgPacket.Import:General:Model:afterOpenEditorPageView")
	public published  void afterOpenEditorPageView (org.radixware.kernel.common.types.Id pageId) {
		if (pageId == idof[CfgPacket.Import:General:Items]) {
		    final Explorer.Models::EditorPageModelItem pageModel = getEditorPage(idof[CfgPacket.Import:General:Items]);
		    final org.radixware.kernel.explorer.views.StandardEditorPage page = (org.radixware.kernel.explorer.views.StandardEditorPage) pageModel.getView();
		    final Client.Views::IEmbeddedView itemsSelector = (Client.Views::IEmbeddedView) page.getWidget();
		    itemsSelector.setSynchronizedWithParentView(true);
		}

		super.afterOpenEditorPageView(pageId);
	}
	public final class Load extends org.radixware.ads.CfgManagement.explorer.CfgPacket.Import.Load{
		protected Load(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_Load( this );
		}

	}

	public final class Accept extends org.radixware.ads.CfgManagement.explorer.CfgPacket.Import.Accept{
		protected Accept(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_Accept( this );
		}

	}

	public final class Relink extends org.radixware.ads.CfgManagement.explorer.CfgPacket.Import.Relink{
		protected Relink(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_Relink( this );
		}

	}

	public final class Check extends org.radixware.ads.CfgManagement.explorer.CfgPacket.Import.Check{
		protected Check(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_Check( this );
		}

	}



















}

/* Radix::CfgManagement::CfgPacket.Import:General:Model - Desktop Meta*/

/*Radix::CfgManagement::CfgPacket.Import:General:Model-Entity Model Class*/

package org.radixware.ads.CfgManagement.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class General:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aem72R6YVIN6RFHHKJZMJEK36TOO4"),
						"General:Model",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aemJHOQCOZIOBBWNABEIAT5XEG7M4"),
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::CfgManagement::CfgPacket.Import:General:Model:Properties-Properties*/
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

/* Radix::CfgManagement::CfgPacket.Import - Localizing Bundle */
package org.radixware.ads.CfgManagement.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class CfgPacket.Import - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Objects updated");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Обновлено объектов");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3KZASCPHHBAYTDHAVIC54IDHJE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Result");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Результат");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3MWDD6I4KFB5BBCBEX3IXEMMOQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Package");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Пакет");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls56DLHEC2YZHXTNWCGJBTAJW7HE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Accept");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Акцептовать");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5G52K6DMI5B5LO6GB4WEMZW4CM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Objects created");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Новых объектов");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls62IGGK5SDFARRKYRMYJ7SSPIX4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Can\'t apply without preliminary execution of Accept command");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Применение не разрешено без предварительного выполнения команды \"Акцептовать\"");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6HZBWKEKJ5HLZCZQPVTWXCON2I"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.EVENT_CODE,org.radixware.kernel.common.enums.EEventSeverity.EVENT,"App",java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Loading must be accepted by another user");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Загрузка должна быть акцептована другим пользователем");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6OCIRZI3DNCBNAA2UOSYBGY57U"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Load item: %s (%d of %d items processed)");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Загрузка элемента: %s (обработано %d из %d элементов)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6WSCFOETP5GZZOMZTPTWGJ7FTI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Configuration item \'%s\' invalid: %s");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Элемент конфигурации \'%s\' не корректный: %s");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAYDQCKPFE5GMJNR2YC4HFA3RUI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Problems occurred when applying the package. Do you want to apply the changes?");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Возникли проблемы в ходе применения пакета. Вы хотите применить изменения?");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBFTUJHRNOBE2PPACO3PVQHPQBU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Accept canceled");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Акцепт отменен");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsC4I3BKOJDJCQVBKW5X2YYERI4Q"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.EVENT_CODE,org.radixware.kernel.common.enums.EEventSeverity.EVENT,"App",java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Configuration item \'%s\' has ambiguous link");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Элемент конфигурации \'%s\' имеет неоднозначную связь");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCHNKQ3HEAZDDVLIT6LNY6SLLYE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Apply");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Применить");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEZLU6JADYVHDTOQBRHKEMJRHGU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Details:");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Подробности:");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGQFX5S7KPRGO5G5QIBOVFARF6Y"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Re-link");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Связать");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGSBYICCQOBEZTC3GFFVDTQF5EI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Date");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Дата");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHNQRQA2CBBBW7FQPSZK5HLFGKE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Configuration package applied");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Пакет конфигурации применен");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHPNKUYKMYRGN3DAEAKS2ATTPRU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.EVENT_CODE,org.radixware.kernel.common.enums.EEventSeverity.EVENT,"App",java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Operation");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Операция");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHRCR24JKQNF35LVQWNU7M6U42A"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"General");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Общее");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHY5LIEN2ZFHQJG3ED5346EWCGE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Canceled");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Прервано");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKDMYA5VSJBEMTL4PQKQNTHYH6Q"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Configuration package linked");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Выполнено связывание пакета конфигурации");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLJYPMZC36JGR5OTDXGO6SM6JVA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.EVENT_CODE,org.radixware.kernel.common.enums.EEventSeverity.EVENT,"App",java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Import Configuration Package");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Пакет импорта конфигурации");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMGGRKV6IA5DBXJFVXV36IS3YIQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Circular dependence detected:\n%s ");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Между элементами пакета конфигурации обнаружена циклическая связь:  \n%s");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMZ3JNQSFZ5GOPHKOVFPQYJIQ74"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Check");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Проверка");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsN7NPGFUGEVEKLBSV3LXZMZJ7AU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Configuration item \'%s\' has broken link");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Элемент конфигурации \'%s\' имеет оборванную связь");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNESDPNNCPRG3BHMXTSHTL7MM3I"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Configuration package applying was cancelled");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Применение пакета конфигурации было отменено");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOAETWRI7NNHAXOD3KZVVPHGH2I"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.EVENT_CODE,org.radixware.kernel.common.enums.EEventSeverity.EVENT,"App",java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Apply Configuration");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Применение конфигурации");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOZV3UL2DKNDUHIO55LZEA5SSBQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Configuration package checked");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Пакет конфигурации проверен");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPZDIZ4YWBREMPBORAR3ORM2IUI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.EVENT_CODE,org.radixware.kernel.common.enums.EEventSeverity.EVENT,"App",null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Configuration package application started");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Начато применение пакета конфигурации");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQCPNYEKAIJC6ZK3FN547UQTPKY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.EVENT_CODE,org.radixware.kernel.common.enums.EEventSeverity.EVENT,"App",java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<tr><td>Ambiguous objects:</td><td>%d</td></tr>\n<tr><td>New objects:</td><td>%d</td></tr>\n<tr><td>Objects with broken references:</td><td>%d</td></tr>\n<tr><td>Objects with ambiguous references:</td><td>%d</td></tr>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<tr><td>Неоднозначных объектов:</td><td>%d</td></tr>\n<tr><td>Новых объектов:</td><td>%d</td></tr>\n<tr><td>Объектов с неустановленными связями:</td><td>%d</td></tr>\n<tr><td>Объектов с неоднозначными связями:</td><td>%d</td></tr>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRVHLMOBS5JHQHPR5I2JETXBCPE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Configuration item \'%s\' associated to an object ambiguously");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Элемент конфигурации \'%s\' ассоциирован с объектом неоднозначно");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSDVIIFNJLZDRFGBXFNMLZHCOBY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Operation status");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Статус операции");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsU33QK7XIOZFKVPM4LDA6EH6644"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Warnings received");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Получено предупреждений");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVF7H3426AZAHDPUORI6NBZUDWQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"User %1 accepted configuration package");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Пользователь %1 акцептовал пакет конфигурации");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVFDRHMALENHDXGR7UM3SC7QV2U"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.EVENT_CODE,org.radixware.kernel.common.enums.EEventSeverity.EVENT,"App",java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Configuration Checking");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Проверка конфигурации");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXL43MCQXRVEELOV2KVAB6QWGZE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Statistics:");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Статистика:");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYC277TFT5JHWHLLY3KBSRRHJ4Y"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Problems");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Проблемы");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYCJYRBVIBZHRPGLRNF4BYQ42KA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Try to use the Re-link command");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Используйте команду \"Связать\"");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYXWM5OOQNVGAPG4LWTZNSF5HOI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Check Result");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Результат проверки");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZM34S5SOWREJXNHIOVF5XQLGUI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(CfgPacket.Import - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbacl6YTUW6RSTFHKHO3LGTUIADUAT4"),"CfgPacket.Import - Localizing Bundle",$$$items$$$);
}
