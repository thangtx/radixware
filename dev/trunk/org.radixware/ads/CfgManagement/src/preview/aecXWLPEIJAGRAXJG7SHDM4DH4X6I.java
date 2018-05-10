
/* Radix::CfgManagement::CfgItemRef - Server Executable*/

/*Radix::CfgManagement::CfgItemRef-Entity Class*/

package org.radixware.ads.CfgManagement.server;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItemRef")
public abstract published class CfgItemRef  extends org.radixware.ads.Types.server.Entity  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {

	private boolean findLoop(CfgItem start, java.util.Map<CfgItem, java.util.List<CfgItem>> id2IncomingRefs, java.util.List<CfgItem> loop) {
	    if (loop.contains(start)) {
	        while (loop.get(0) != start) {
	            loop.remove(0);
	        }
	        loop.add(start);
	        return true;
	    }

	    java.util.List<CfgItem> predecessors = new java.util.LinkedList<CfgItem>();
	    CfgItemRef[] refs = start.getAllRefs();
	    if (refs != null) {
	        //1. fill list if referenced items instead of list of references        
	        for (int i = 0; i < refs.length; i++) {
	            if (refs[i] == null) {
	                continue;
	            }
	            CfgItem item2 = refs[i].intRef;
	            if (item2 == null || item2.shouldSkip()) {
	                continue;
	            }
	            predecessors.add(item2);
	        }
	    }

	    if (start.parent != null && !start.parent.shouldSkip()) {
	        CfgItem parentItem = start.parent;
	        predecessors.add(parentItem);
	    }
	    
	    id2IncomingRefs.put(start, predecessors);

	    java.util.List<CfgItem> next = id2IncomingRefs.get(start);
	    if (next == null || next.isEmpty()) {
	        return false;
	    }

	    loop.add(start);
	    for (CfgItem item : next) {
	        if (findLoop(item, id2IncomingRefs, loop)) {
	            return true;
	        }
	    }
	    loop.remove(loop.size() - 1);
	    return false;
	}
	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return CfgItemRef_mi.rdxMeta;}

	/*Radix::CfgManagement::CfgItemRef:Nested classes-Nested Classes*/

	/*Radix::CfgManagement::CfgItemRef:Properties-Properties*/

	/*Radix::CfgManagement::CfgItemRef:classGuid-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItemRef:classGuid")
	public published  Str getClassGuid() {
		return classGuid;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItemRef:classGuid")
	public published   void setClassGuid(Str val) {
		classGuid = val;
	}

	/*Radix::CfgManagement::CfgItemRef:extClassGuid-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItemRef:extClassGuid")
	public published  Str getExtClassGuid() {
		return extClassGuid;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItemRef:extClassGuid")
	public published   void setExtClassGuid(Str val) {
		extClassGuid = val;
	}

	/*Radix::CfgManagement::CfgItemRef:extRefPid-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItemRef:extRefPid")
	public published  Str getExtRefPid() {
		return extRefPid;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItemRef:extRefPid")
	public published   void setExtRefPid(Str val) {

		internal[extRefPid] = val;
		calcState();

	}

	/*Radix::CfgManagement::CfgItemRef:intClassGuid-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItemRef:intClassGuid")
	public published  Str getIntClassGuid() {
		return intClassGuid;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItemRef:intClassGuid")
	public published   void setIntClassGuid(Str val) {
		intClassGuid = val;
	}

	/*Radix::CfgManagement::CfgItemRef:intRefId-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItemRef:intRefId")
	public published  Int getIntRefId() {
		return intRefId;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItemRef:intRefId")
	public published   void setIntRefId(Int val) {
		intRefId = val;
	}

	/*Radix::CfgManagement::CfgItemRef:isInheritable-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItemRef:isInheritable")
	public published  Bool getIsInheritable() {
		return isInheritable;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItemRef:isInheritable")
	public published   void setIsInheritable(Bool val) {
		isInheritable = val;
	}

	/*Radix::CfgManagement::CfgItemRef:isNilable-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItemRef:isNilable")
	public published  Bool getIsNilable() {
		return isNilable;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItemRef:isNilable")
	public published   void setIsNilable(Bool val) {
		isNilable = val;
	}

	/*Radix::CfgManagement::CfgItemRef:srcInherited-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItemRef:srcInherited")
	public published  Bool getSrcInherited() {
		return srcInherited;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItemRef:srcInherited")
	public published   void setSrcInherited(Bool val) {
		srcInherited = val;
	}

	/*Radix::CfgManagement::CfgItemRef:srcRid-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItemRef:srcRid")
	public published  Str getSrcRid() {
		return srcRid;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItemRef:srcRid")
	public published   void setSrcRid(Str val) {
		srcRid = val;
	}

	/*Radix::CfgManagement::CfgItemRef:state-Column-Based Property*/








			















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItemRef:state")
	public published  org.radixware.ads.CfgManagement.common.RefState getState() {
		return state;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItemRef:state")
	public published   void setState(org.radixware.ads.CfgManagement.common.RefState val) {
		state = val;
	}

	/*Radix::CfgManagement::CfgItemRef:type-Column-Based Property*/








			















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItemRef:type")
	public published  org.radixware.ads.CfgManagement.common.RefType getType() {
		return type;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItemRef:type")
	public published   void setType(org.radixware.ads.CfgManagement.common.RefType val) {

		internal[type] = val;
		calcState();

	}

	/*Radix::CfgManagement::CfgItemRef:upDefId-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItemRef:upDefId")
	public published  Str getUpDefId() {
		return upDefId;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItemRef:upDefId")
	public published   void setUpDefId(Str val) {
		upDefId = val;
	}

	/*Radix::CfgManagement::CfgItemRef:upOwnerPid-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItemRef:upOwnerPid")
	public published  Str getUpOwnerPid() {
		return upOwnerPid;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItemRef:upOwnerPid")
	public published   void setUpOwnerPid(Str val) {
		upOwnerPid = val;
	}

	/*Radix::CfgManagement::CfgItemRef:upOwnerEntityId-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItemRef:upOwnerEntityId")
	public published  Str getUpOwnerEntityId() {
		return upOwnerEntityId;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItemRef:upOwnerEntityId")
	public published   void setUpOwnerEntityId(Str val) {
		upOwnerEntityId = val;
	}

	/*Radix::CfgManagement::CfgItemRef:intRef-Parent Reference*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItemRef:intRef")
	public published  org.radixware.ads.CfgManagement.server.CfgItem getIntRef() {
		return intRef;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItemRef:intRef")
	public published   void setIntRef(org.radixware.ads.CfgManagement.server.CfgItem val) {

		CfgItem prev = null;
		try {
		    prev = internal[intRef];
		} catch (Exceptions::EntityObjectNotExistsError e) {
		    ;
		}

		internal[intRef] = val;
		java.util.List<CfgItem> loopedItems = new java.util.ArrayList<CfgItem>();
		if (findLoop(cfgItem, new java.util.HashMap<CfgItem, java.util.List<CfgItem>>(), loopedItems)) {
		    internal[intRef] = prev;
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
		    throw new AppError(Str.format("Circular dependence detected:...", sb.toString()));
		}
		calcState();
	}

	/*Radix::CfgManagement::CfgItemRef:cfgItem-Dynamic Property*/



	protected org.radixware.ads.CfgManagement.server.CfgItem cfgItem=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItemRef:cfgItem")
	public published  org.radixware.ads.CfgManagement.server.CfgItem getCfgItem() {

		return CfgItem.loadByPidStr(upOwnerPid, true);

	}

	/*Radix::CfgManagement::CfgItemRef:packetId-Parent Property*/






















@SuppressWarnings({"unused","unchecked"})
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItemRef:packetId")
public published  Int getPacketId() {
	return packetId;
}

@SuppressWarnings({"unused","unchecked"})
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItemRef:packetId")
public published   void setPacketId(Int val) {
	packetId = val;
}

/*Radix::CfgManagement::CfgItemRef:refObjectTitle-Dynamic Property*/



protected Str refObjectTitle=null;











@SuppressWarnings({"unused","unchecked"})
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItemRef:refObjectTitle")
public published  Str getRefObjectTitle() {

	Types::Entity r = getRefVal();
	if (r == null)
	    return "?";
	return r.calcTitle();
}

































































































































/*Radix::CfgManagement::CfgItemRef:Methods-Methods*/

/*Radix::CfgManagement::CfgItemRef:calcState-User Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItemRef:calcState")
private final  org.radixware.ads.CfgManagement.common.RefState calcState () {
	RefState s;
	if (type == null)
	    s = null;
	else
	    switch (type) {
	        case RefType:External:
	            s = extRefPid != null ? RefState:Ok : RefState:Broken;
	            break;
	        case RefType:Internal:
	            try {
	                s = intRef != null ? RefState:Ok : RefState:Broken;
	            } catch (Exceptions::EntityObjectNotExistsError e) {
	                s = RefState:Broken;
	            }
	            break;
	        default:
	            s = RefState:Ok;
	    }
	if (state == s)
	    return s;
	state = s;
	cfgItem.calcAllRefsState();
	return s;
}

/*Radix::CfgManagement::CfgItemRef:link-User Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItemRef:link")
public published  void link () {
	intRef = null;
	extRefPid = null;

	if (srcInherited == true)
	    type = RefType:Inherit;
	else if (srcRid == null)
	    type = RefType:Null;
	else {
	    CfgItemsBySrcCursor c = CfgItemsBySrcCursor.open(cfgItem.packetId, intClassGuid, srcRid);
	    try {
	        if (!c.next()) {
	            type = RefType:External;
	            state = null;
	            linkExt();
	            if (state == null)
	                state = extRefPid != null ? RefState:Ok : RefState:Broken;
	            return;
	        } else {
	            type = RefType:Internal;
	            intRef = (CfgItem.Single) c.item;
	            if (c.next())
	                state = RefState:Ambiguity;
	            else
	                state = RefState:Ok;
	        }
	    } finally {
	        c.close();
	    }
	}

}

/*Radix::CfgManagement::CfgItemRef:linkExt-User Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItemRef:linkExt")
protected abstract published  void linkExt ();

/*Radix::CfgManagement::CfgItemRef:getRefVal-User Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItemRef:getRefVal")
public abstract published  org.radixware.ads.Types.server.Entity getRefVal ();

/*Radix::CfgManagement::CfgItemRef:onCalcTitle-User Method*/

@Override
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItemRef:onCalcTitle")
protected published  Str onCalcTitle (Str title) {
	Str s = type.Title + " - " + state.Title;
	switch (type) {
	    case RefType:Internal: 
	    case RefType:External: 
	        Types::Entity r = getRefVal();
	        return s + " - '" + (r != null ? r.calcTitle() : "?") + "'";
	    default: 
	        return s; 
	}


}

/*Radix::CfgManagement::CfgItemRef:onCommand_AcceptAmbig-Command Handler Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItemRef:onCommand_AcceptAmbig")
public  void onCommand_AcceptAmbig (org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValHandlersById) {
	if (state == RefState:Ambiguity) {
	    state = RefState:Ok;
	    cfgItem.calcAllRefsState();
	}

}



@Override
public org.radixware.kernel.server.types.FormHandler.NextDialogsRequest execCommand(org.radixware.kernel.common.types.Id cmdId, org.radixware.kernel.common.types.Id propId, org.apache.xmlbeans.XmlObject input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValsById, org.apache.xmlbeans.XmlObject output) throws org.radixware.kernel.common.exceptions.AppException,java.lang.InterruptedException{
	if(cmdId == cmd3SYKP2HIFFCIJC4E63YCQXATE4){
		onCommand_AcceptAmbig(newPropValsById);
		return null;
	} else 
		return super.execCommand(cmdId,propId,input,newPropValsById,output);
}


}

/* Radix::CfgManagement::CfgItemRef - Server Meta*/

/*Radix::CfgManagement::CfgItemRef-Entity Class*/

package org.radixware.ads.CfgManagement.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class CfgItemRef_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecXWLPEIJAGRAXJG7SHDM4DH4X6I"),"CfgItemRef",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGLQNTIBT45H3PFSFKZGDTZOQZ4"),

						org.radixware.kernel.common.enums.EClassType.ENTITY,

						/*Radix::CfgManagement::CfgItemRef:Presentations-Entity Presentations*/
						/*Class presentation attributes*/
						 new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef(/*Owner Class Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("aecXWLPEIJAGRAXJG7SHDM4DH4X6I"),
							/*Owner Class Name*/
							"CfgItemRef",
							/*Title Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGLQNTIBT45H3PFSFKZGDTZOQZ4"),
							/*Property presentations*/

							/*Radix::CfgManagement::CfgItemRef:Properties-Properties*/
							new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef[]{

									/*Radix::CfgManagement::CfgItemRef:isInheritable:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIVUOAZCZTBFLBIGCFCP7XUTUWY"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::CfgManagement::CfgItemRef:isNilable:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colZIH6VRMCKRGOPLH3PC6KFWCQDE"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::CfgManagement::CfgItemRef:srcInherited:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNKM5TQLE5RHEZFGFLZYM7HRH3A"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::CfgManagement::CfgItemRef:srcRid:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKOXIJGBJTJGYJAAVR2ASCGVMDI"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::CfgManagement::CfgItemRef:state:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colBIPNKBLZUVATBPJTKZWEHGDGZE"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::CfgManagement::CfgItemRef:type:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOBNSAADYD5CKFFOIDSKQI7TGLQ"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::CfgManagement::CfgItemRef:intRef:PropertyPresentation-Parent Ref Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadParentTitlePropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXU6AK62EW5EWNE7KJGT4KVBNYQ"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("sprMQEXFCGPZJBLVA7BZDI5QHQKRY"),new org.radixware.kernel.server.meta.presentations.RadConditionDef("<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:PropSqlName TableId=\"aecJ4WZRLJ23BGMBKQ2JBSOBEGBP4\" PropId=\"colTUL7C3425VCCNC5ELKDFZBWJOY\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> = </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecXWLPEIJAGRAXJG7SHDM4DH4X6I\" PropId=\"colW2YXPCQ4XVCA5CCAGSBDHMOJEM\" Owner=\"CHILD\"/></xsc:Item><xsc:Item><xsc:Sql>\n--and </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecJ4WZRLJ23BGMBKQ2JBSOBEGBP4\" PropId=\"colIUQVQZZKPFBRLG7DTABUYGLYBQ\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> &lt; to_number(</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblXWLPEIJAGRAXJG7SHDM4DH4X6I\" PropId=\"colGNVHWA56DJBFXOLL7VL7I4THUY\" Owner=\"CHILD\"/></xsc:Item><xsc:Item><xsc:Sql>)\nand </xsc:Sql></xsc:Item><xsc:Item><xsc:DbFuncCall FuncId=\"dfn3OXAHT4FYLORDBBJABIFNQAABA\"/></xsc:Item><xsc:Item><xsc:Sql>(</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecJ4WZRLJ23BGMBKQ2JBSOBEGBP4\" PropId=\"colJQK7LYSZW5FS7NYEXFUKCIR4O4\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>, </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecXWLPEIJAGRAXJG7SHDM4DH4X6I\" PropId=\"colUAKFIHEYFFDSLFY4FFSYQN747Y\" Owner=\"CHILD\"/></xsc:Item><xsc:Item><xsc:Sql>)&lt;>0\n--Commented, because there is no need (RADIX-14545)\n--and ( --root of this class\n--    </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecJ4WZRLJ23BGMBKQ2JBSOBEGBP4\" PropId=\"colJ5L5OI3X4BHYBPJFGAWUO4MKZM\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> is null \n--    or </xsc:Sql></xsc:Item><xsc:Item><xsc:DbFuncCall FuncId=\"dfn3OXAHT4FYLORDBBJABIFNQAABA\"/></xsc:Item><xsc:Item><xsc:Sql>((select </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecJ4WZRLJ23BGMBKQ2JBSOBEGBP4\" PropId=\"colJQK7LYSZW5FS7NYEXFUKCIR4O4\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql> from </xsc:Sql></xsc:Item><xsc:Item><xsc:TableSqlName TableId=\"tblJ4WZRLJ23BGMBKQ2JBSOBEGBP4\"/></xsc:Item><xsc:Item><xsc:Sql> where </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecJ4WZRLJ23BGMBKQ2JBSOBEGBP4\" PropId=\"colIUQVQZZKPFBRLG7DTABUYGLYBQ\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql> = </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecJ4WZRLJ23BGMBKQ2JBSOBEGBP4\" PropId=\"colJ5L5OI3X4BHYBPJFGAWUO4MKZM\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>), </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecXWLPEIJAGRAXJG7SHDM4DH4X6I\" PropId=\"colUAKFIHEYFFDSLFY4FFSYQN747Y\" Owner=\"CHILD\"/></xsc:Item><xsc:Item><xsc:Sql>)=0 \n--)\nand </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecJ4WZRLJ23BGMBKQ2JBSOBEGBP4\" PropId=\"colIUQVQZZKPFBRLG7DTABUYGLYBQ\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> not in \n(\n    select </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblJ4WZRLJ23BGMBKQ2JBSOBEGBP4\" PropId=\"colIUQVQZZKPFBRLG7DTABUYGLYBQ\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql> from </xsc:Sql></xsc:Item><xsc:Item><xsc:TableSqlName TableId=\"tblJ4WZRLJ23BGMBKQ2JBSOBEGBP4\"/></xsc:Item><xsc:Item><xsc:Sql> \n    start with </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecJ4WZRLJ23BGMBKQ2JBSOBEGBP4\" PropId=\"colIUQVQZZKPFBRLG7DTABUYGLYBQ\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql> = to_number(</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblXWLPEIJAGRAXJG7SHDM4DH4X6I\" PropId=\"colGNVHWA56DJBFXOLL7VL7I4THUY\" Owner=\"CHILD\"/></xsc:Item><xsc:Item><xsc:Sql>) connect by </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecJ4WZRLJ23BGMBKQ2JBSOBEGBP4\" PropId=\"colJ5L5OI3X4BHYBPJFGAWUO4MKZM\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql> = prior </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecJ4WZRLJ23BGMBKQ2JBSOBEGBP4\" PropId=\"colIUQVQZZKPFBRLG7DTABUYGLYBQ\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql>     \n)</xsc:Sql></xsc:Item></xsc:Sqml>","<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),org.radixware.kernel.server.types.Restrictions.Factory.newInstance(3668991,null,null,null),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprO3XHJ6U74NDNHJ7GNTDUUNDEWE")},null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::CfgManagement::CfgItemRef:cfgItem:PropertyPresentation-Parent Ref Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadParentTitlePropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdPSWEGRV72ZAN3DQD7SMRUGHDXI"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,null,null,new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),org.radixware.kernel.server.types.Restrictions.Factory.newInstance(133693439,null,null,null),null,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.TITLE,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECTOR)),

									/*Radix::CfgManagement::CfgItemRef:refObjectTitle:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdRRJOFVA3AJC4FB6RHDR7S6ZL2A"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT))
							},
							/*Commands*/
							new org.radixware.kernel.server.meta.presentations.RadCommandDef[]{

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::CfgManagement::CfgItemRef:AcceptAmbig-Object Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmd3SYKP2HIFFCIJC4E63YCQXATE4"),"AcceptAmbig",org.radixware.kernel.common.enums.ECommandScope.OBJECT,null,org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_FIXED,org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,org.radixware.kernel.common.types.Id.Factory.loadFrom("aecXWLPEIJAGRAXJG7SHDM4DH4X6I"),false,true)
							},
							/*Sortings*/
							null,
							/*Filters*/
							null,
							/*Editor presentations*/
							new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef[]{

									new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef(
									/*Radix::CfgManagement::CfgItemRef:General-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprDF4Y6Z3KOJEWFMJZNEQ4BN6UGA"),
									"General",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
									null,
									2192,
									null,

									/*Radix::CfgManagement::CfgItemRef:General:Children-Explorer Items*/
										null
									,
									org.radixware.kernel.server.types.Restrictions.Factory.newInstance(35,null,null,null),
									(org.radixware.kernel.server.meta.presentations.RadPropertyPresentationAttributes[]) null,
									org.radixware.kernel.common.enums.EEditorPresentationRightsInheritanceMode.NONE,
									null,null)
							},
							/*Selector presentations*/
							new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef[]{

									new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef(
									/*Radix::CfgManagement::CfgItemRef:General-Selector Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("spr6WPTLLJ3ONAD7MSK66K5HMHBMA"),"General",null,144,new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn[]{

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colBIPNKBLZUVATBPJTKZWEHGDGZE"),true)
									},new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.Addons(null,false,new org.radixware.kernel.common.types.Id[]{},false,null,false,new org.radixware.kernel.common.types.Id[]{},false,false,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprDF4Y6Z3KOJEWFMJZNEQ4BN6UGA")},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprDF4Y6Z3KOJEWFMJZNEQ4BN6UGA")},org.radixware.kernel.server.types.Restrictions.Factory.newInstance(35,null,null,null),null)
							},
							/*Default selector presentation Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("spr6WPTLLJ3ONAD7MSK66K5HMHBMA"),
							/*Presentation Map*/
							null,
							/*Object title format*/

							/*Radix::CfgManagement::CfgItemRef:TitleFormat-Object Title Format*/

							new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecXWLPEIJAGRAXJG7SHDM4DH4X6I"),new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem[]{

									new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecXWLPEIJAGRAXJG7SHDM4DH4X6I"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colOBNSAADYD5CKFFOIDSKQI7TGLQ"),"\"see onCalcTitle\"",org.radixware.kernel.common.enums.ETitleNullFormat.SAME_AS_NOT_NULL,null)
							},null,org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.DefinitionContextType.ENTITY),
							/*Class catalogs*/
							null,
							/*Restrictions*/
							org.radixware.kernel.server.types.Restrictions.Factory.newInstance(0,null,null,null),null,0),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblXWLPEIJAGRAXJG7SHDM4DH4X6I"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("pdcEntity____________________"),

						/*Radix::CfgManagement::CfgItemRef:Properties-Properties*/
						new org.radixware.kernel.server.meta.clazzes.RadPropDef[]{

								/*Radix::CfgManagement::CfgItemRef:classGuid-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colYHOK2V3JZNGZVDAU2FSYF5VEBM"),"classGuid",null,org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::CfgManagement::CfgItemRef:extClassGuid-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMBDYSK4BYVCBHNDH537HYAYXRM"),"extClassGuid",null,org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::CfgManagement::CfgItemRef:extRefPid-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colBISYSN4WJ5AKXBKVP34WGF5EE4"),"extRefPid",null,org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::CfgManagement::CfgItemRef:intClassGuid-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colUAKFIHEYFFDSLFY4FFSYQN747Y"),"intClassGuid",null,org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::CfgManagement::CfgItemRef:intRefId-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colAP5NJI3EIRE6PEUIUEQF5SLZZI"),"intRefId",null,org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::CfgManagement::CfgItemRef:isInheritable-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIVUOAZCZTBFLBIGCFCP7XUTUWY"),"isInheritable",null,org.radixware.kernel.common.enums.EValType.BOOL,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::CfgManagement::CfgItemRef:isNilable-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colZIH6VRMCKRGOPLH3PC6KFWCQDE"),"isNilable",null,org.radixware.kernel.common.enums.EValType.BOOL,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::CfgManagement::CfgItemRef:srcInherited-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNKM5TQLE5RHEZFGFLZYM7HRH3A"),"srcInherited",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3CG3TFTGO5GR5JSA57MJO5RXUA"),org.radixware.kernel.common.enums.EValType.BOOL,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::CfgManagement::CfgItemRef:srcRid-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKOXIJGBJTJGYJAAVR2ASCGVMDI"),"srcRid",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsE3JJQR6U6NBSDAQEDTPVSXVQ2Y"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::CfgManagement::CfgItemRef:state-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colBIPNKBLZUVATBPJTKZWEHGDGZE"),"state",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsODNQFDCM2RBPVIDRPFZP727URY"),org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("acsUT33HR7H2BCG5NXG3J52FIV5AA"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::CfgManagement::CfgItemRef:type-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOBNSAADYD5CKFFOIDSKQI7TGLQ"),"type",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls54ZTX4GZEVGOPHONPRF37ROIIQ"),org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("acs7CM3W6GESFC4LH3RDDY6GITI7Q"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::CfgManagement::CfgItemRef:upDefId-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colV5EZXMQFDFDBLO7M53XCUCWJ4A"),"upDefId",null,org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::CfgManagement::CfgItemRef:upOwnerPid-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGNVHWA56DJBFXOLL7VL7I4THUY"),"upOwnerPid",null,org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::CfgManagement::CfgItemRef:upOwnerEntityId-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colTHM4RNL4JNBMZMT33HAY33ZU5U"),"upOwnerEntityId",null,org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::CfgManagement::CfgItemRef:intRef-Parent Reference*/

								new org.radixware.kernel.server.meta.clazzes.RadInnateRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXU6AK62EW5EWNE7KJGT4KVBNYQ"),"intRef",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGRKV6AOK4NELDCVZATCFB6FKEA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("refBTXNQ3WNBRH65CF3ZPZBGEO56Q"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ4WZRLJ23BGMBKQ2JBSOBEGBP4"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::CfgManagement::CfgItemRef:cfgItem-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdPSWEGRV72ZAN3DQD7SMRUGHDXI"),"cfgItem",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6SY6L3HI6JGJNIZAIOYXCHTCVQ"),org.radixware.kernel.common.enums.EValType.PARENT_REF,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ4WZRLJ23BGMBKQ2JBSOBEGBP4"),org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE),

								/*Radix::CfgManagement::CfgItemRef:packetId-Parent Property*/

								new org.radixware.kernel.server.meta.clazzes.RadParentPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colW2YXPCQ4XVCA5CCAGSBDHMOJEM"),"packetId",null,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("prdPSWEGRV72ZAN3DQD7SMRUGHDXI")},org.radixware.kernel.common.types.Id.Factory.loadFrom("colTUL7C3425VCCNC5ELKDFZBWJOY"),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::CfgManagement::CfgItemRef:refObjectTitle-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdRRJOFVA3AJC4FB6RHDR7S6ZL2A"),"refObjectTitle",null,org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE)
						},

						/*Radix::CfgManagement::CfgItemRef:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthTCH7T5RAZRHXJO5YWEZGEUF5XU"),"calcState",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,null,org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthRNKZ3O3N3JBRLPCU5C55Z7PSQM"),"link",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthYLZSFLG5WJBMTM2WGLATF7ANBY"),"linkExt",false,true,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthAKKIPLUDCBB65HWRAPB4WZGDYU"),"getRefVal",false,true,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth4D6ME6VX45BYVNJXZPBTAA7KP4"),"onCalcTitle",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("title",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprMRSVAQTXABAXNJJGSF3FX5NQBE"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthcmd3SYKP2HIFFCIJC4E63YCQXATE4"),"onCommand_AcceptAmbig",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("newPropValHandlersById",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprISMXPBZUCBD57E7FYDHKGZFKHU"))
								},null)
						},
						null,
						org.radixware.kernel.common.enums.EAccessAreaType.NONE,null,null,false);
}

/* Radix::CfgManagement::CfgItemRef - Desktop Executable*/

/*Radix::CfgManagement::CfgItemRef-Entity Class*/

package org.radixware.ads.CfgManagement.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItemRef")
public interface CfgItemRef {
	/*Group model adapter class. This is the base class for 
	all of selector presentation models of this entity*/
	public static class DefaultGroupModel extends org.radixware.kernel.common.client.models.GroupModel{
		public DefaultGroupModel(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}
		public org.radixware.ads.CfgManagement.explorer.CfgItemRef.CfgItemRef_DefaultModel getEntity(int i) throws InterruptedException, org.radixware.kernel.common.client.exceptions.BrokenEntityObjectException, org.radixware.kernel.common.exceptions.ServiceClientException { return (org.radixware.ads.CfgManagement.explorer.CfgItemRef.CfgItemRef_DefaultModel )  super.getEntity(i);}
	}








































































	/*Radix::CfgManagement::CfgItemRef:state:state-Presentation Property*/


	public class State extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public State(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.ads.CfgManagement.common.RefState dummy = x == null ? null : (x instanceof org.radixware.ads.CfgManagement.common.RefState ? (org.radixware.ads.CfgManagement.common.RefState)x : org.radixware.ads.CfgManagement.common.RefState.getForValue((Str)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.STR)));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.ads.CfgManagement.common.RefState> getValClass(){
			return org.radixware.ads.CfgManagement.common.RefState.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.ads.CfgManagement.common.RefState dummy = x == null ? null : (x instanceof org.radixware.ads.CfgManagement.common.RefState ? (org.radixware.ads.CfgManagement.common.RefState)x : org.radixware.ads.CfgManagement.common.RefState.getForValue((Str)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.STR)));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItemRef:state:state")
		public  org.radixware.ads.CfgManagement.common.RefState getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItemRef:state:state")
		public   void setValue(org.radixware.ads.CfgManagement.common.RefState val) {
			Value = val;
		}
	}
	public State getState();
	/*Radix::CfgManagement::CfgItemRef:isInheritable:isInheritable-Presentation Property*/


	public class IsInheritable extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public IsInheritable(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItemRef:isInheritable:isInheritable")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItemRef:isInheritable:isInheritable")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public IsInheritable getIsInheritable();
	/*Radix::CfgManagement::CfgItemRef:srcRid:srcRid-Presentation Property*/


	public class SrcRid extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public SrcRid(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItemRef:srcRid:srcRid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItemRef:srcRid:srcRid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public SrcRid getSrcRid();
	/*Radix::CfgManagement::CfgItemRef:srcInherited:srcInherited-Presentation Property*/


	public class SrcInherited extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public SrcInherited(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItemRef:srcInherited:srcInherited")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItemRef:srcInherited:srcInherited")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public SrcInherited getSrcInherited();
	/*Radix::CfgManagement::CfgItemRef:type:type-Presentation Property*/


	public class Type extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public Type(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.ads.CfgManagement.common.RefType dummy = x == null ? null : (x instanceof org.radixware.ads.CfgManagement.common.RefType ? (org.radixware.ads.CfgManagement.common.RefType)x : org.radixware.ads.CfgManagement.common.RefType.getForValue((Str)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.STR)));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.ads.CfgManagement.common.RefType> getValClass(){
			return org.radixware.ads.CfgManagement.common.RefType.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.ads.CfgManagement.common.RefType dummy = x == null ? null : (x instanceof org.radixware.ads.CfgManagement.common.RefType ? (org.radixware.ads.CfgManagement.common.RefType)x : org.radixware.ads.CfgManagement.common.RefType.getForValue((Str)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.STR)));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItemRef:type:type")
		public  org.radixware.ads.CfgManagement.common.RefType getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItemRef:type:type")
		public   void setValue(org.radixware.ads.CfgManagement.common.RefType val) {
			Value = val;
		}
	}
	public Type getType();
	/*Radix::CfgManagement::CfgItemRef:intRef:intRef-Presentation Property*/


	public class IntRef extends org.radixware.kernel.common.client.models.items.properties.PropertyRef{
		public IntRef(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
			super(model,def);
		}
		@Override
		public 
		org.radixware.ads.CfgManagement.explorer.CfgItem.CfgItem_DefaultModel openEntityModel()  throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{
		     return (org.radixware.ads.CfgManagement.explorer.CfgItem.CfgItem_DefaultModel)super.openEntityModel();
		}
		@Override
		public 
		org.radixware.ads.CfgManagement.explorer.CfgItem.DefaultGroupModel openGroupModel(){
		     return (org.radixware.ads.CfgManagement.explorer.CfgItem.DefaultGroupModel)super.openGroupModel();
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.client.types.Reference dummy = ((org.radixware.kernel.common.client.types.Reference)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItemRef:intRef:intRef")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItemRef:intRef:intRef")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public IntRef getIntRef();
	/*Radix::CfgManagement::CfgItemRef:isNilable:isNilable-Presentation Property*/


	public class IsNilable extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public IsNilable(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItemRef:isNilable:isNilable")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItemRef:isNilable:isNilable")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public IsNilable getIsNilable();
	/*Radix::CfgManagement::CfgItemRef:cfgItem:cfgItem-Presentation Property*/


	public class CfgItem extends org.radixware.kernel.common.client.models.items.properties.PropertyRef{
		public CfgItem(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
			super(model,def);
		}
		@Override
		public 
		org.radixware.ads.CfgManagement.explorer.CfgItem.CfgItem_DefaultModel openEntityModel()  throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{
		     return (org.radixware.ads.CfgManagement.explorer.CfgItem.CfgItem_DefaultModel)super.openEntityModel();
		}
		@Override
		public 
		org.radixware.ads.CfgManagement.explorer.CfgItem.DefaultGroupModel openGroupModel(){
		     return (org.radixware.ads.CfgManagement.explorer.CfgItem.DefaultGroupModel)super.openGroupModel();
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.client.types.Reference dummy = ((org.radixware.kernel.common.client.types.Reference)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItemRef:cfgItem:cfgItem")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItemRef:cfgItem:cfgItem")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public CfgItem getCfgItem();
	/*Radix::CfgManagement::CfgItemRef:refObjectTitle:refObjectTitle-Presentation Property*/


	public class RefObjectTitle extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public RefObjectTitle(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItemRef:refObjectTitle:refObjectTitle")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItemRef:refObjectTitle:refObjectTitle")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public RefObjectTitle getRefObjectTitle();
	public static class AcceptAmbig extends org.radixware.kernel.common.client.models.items.Command{
		protected AcceptAmbig(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public void send() throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,null);
			processResponse(response, null);
		}

	}



}

/* Radix::CfgManagement::CfgItemRef - Desktop Meta*/

/*Radix::CfgManagement::CfgItemRef-Entity Class*/

package org.radixware.ads.CfgManagement.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class CfgItemRef_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::CfgManagement::CfgItemRef:Presentations-Entity Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecXWLPEIJAGRAXJG7SHDM4DH4X6I"),
			"Radix::CfgManagement::CfgItemRef",
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("imgTHZUHZZKPBA63KXOYUIXHM74VQ"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("spr6WPTLLJ3ONAD7MSK66K5HMHBMA"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGLQNTIBT45H3PFSFKZGDTZOQZ4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3YU2NPLDLFAFFA4PSYQ7XDQQV4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGLQNTIBT45H3PFSFKZGDTZOQZ4"),0,

			/*Radix::CfgManagement::CfgItemRef:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::CfgManagement::CfgItemRef:isInheritable:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIVUOAZCZTBFLBIGCFCP7XUTUWY"),
						"isInheritable",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecXWLPEIJAGRAXJG7SHDM4DH4X6I"),
						org.radixware.kernel.common.enums.EValType.BOOL,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::CfgManagement::CfgItemRef:isInheritable:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecXWLPEIJAGRAXJG7SHDM4DH4X6I"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::CfgManagement::CfgItemRef:isNilable:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colZIH6VRMCKRGOPLH3PC6KFWCQDE"),
						"isNilable",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecXWLPEIJAGRAXJG7SHDM4DH4X6I"),
						org.radixware.kernel.common.enums.EValType.BOOL,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::CfgManagement::CfgItemRef:isNilable:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecXWLPEIJAGRAXJG7SHDM4DH4X6I"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::CfgManagement::CfgItemRef:srcInherited:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNKM5TQLE5RHEZFGFLZYM7HRH3A"),
						"srcInherited",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3CG3TFTGO5GR5JSA57MJO5RXUA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecXWLPEIJAGRAXJG7SHDM4DH4X6I"),
						org.radixware.kernel.common.enums.EValType.BOOL,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::CfgManagement::CfgItemRef:srcInherited:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecXWLPEIJAGRAXJG7SHDM4DH4X6I"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::CfgManagement::CfgItemRef:srcRid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKOXIJGBJTJGYJAAVR2ASCGVMDI"),
						"srcRid",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsE3JJQR6U6NBSDAQEDTPVSXVQ2Y"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecXWLPEIJAGRAXJG7SHDM4DH4X6I"),
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
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::CfgManagement::CfgItemRef:srcRid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::CfgManagement::CfgItemRef:state:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colBIPNKBLZUVATBPJTKZWEHGDGZE"),
						"state",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsODNQFDCM2RBPVIDRPFZP727URY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecXWLPEIJAGRAXJG7SHDM4DH4X6I"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acsUT33HR7H2BCG5NXG3J52FIV5AA"),
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::CfgManagement::CfgItemRef:state:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsUT33HR7H2BCG5NXG3J52FIV5AA"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::CfgManagement::CfgItemRef:type:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOBNSAADYD5CKFFOIDSKQI7TGLQ"),
						"type",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls54ZTX4GZEVGOPHONPRF37ROIIQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecXWLPEIJAGRAXJG7SHDM4DH4X6I"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acs7CM3W6GESFC4LH3RDDY6GITI7Q"),
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::CfgManagement::CfgItemRef:type:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acs7CM3W6GESFC4LH3RDDY6GITI7Q"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::CfgManagement::CfgItemRef:intRef:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXU6AK62EW5EWNE7KJGT4KVBNYQ"),
						"intRef",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGRKV6AOK4NELDCVZATCFB6FKEA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecXWLPEIJAGRAXJG7SHDM4DH4X6I"),
						org.radixware.kernel.common.enums.EValType.PARENT_REF,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						true,false,
						false,
						false,
						null,
						false,
						null,
						null,
						null,
						null,
						true,-1,-1,1,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ4WZRLJ23BGMBKQ2JBSOBEGBP4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblJ4WZRLJ23BGMBKQ2JBSOBEGBP4"),
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprO3XHJ6U74NDNHJ7GNTDUUNDEWE")},
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("sprMQEXFCGPZJBLVA7BZDI5QHQKRY"),
						3668991,
						133693439,false),

					/*Radix::CfgManagement::CfgItemRef:cfgItem:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdPSWEGRV72ZAN3DQD7SMRUGHDXI"),
						"cfgItem",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecXWLPEIJAGRAXJG7SHDM4DH4X6I"),
						org.radixware.kernel.common.enums.EValType.PARENT_REF,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
						21,
						null,
						null,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,false,
						false,
						false,
						null,
						false,
						null,
						null,
						null,
						null,
						true,-1,-1,1,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJ4WZRLJ23BGMBKQ2JBSOBEGBP4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblJ4WZRLJ23BGMBKQ2JBSOBEGBP4"),
						null,
						null,
						null,
						133693439,
						133693439,false),

					/*Radix::CfgManagement::CfgItemRef:refObjectTitle:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdRRJOFVA3AJC4FB6RHDR7S6ZL2A"),
						"refObjectTitle",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecXWLPEIJAGRAXJG7SHDM4DH4X6I"),
						org.radixware.kernel.common.enums.EValType.STR,
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::CfgManagement::CfgItemRef:refObjectTitle:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false)
			},
			new org.radixware.kernel.common.client.meta.RadCommandDef[]{
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::CfgManagement::CfgItemRef:AcceptAmbig-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmd3SYKP2HIFFCIJC4E63YCQXATE4"),
						"AcceptAmbig",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecXWLPEIJAGRAXJG7SHDM4DH4X6I"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSKELH7K6DRE2FAKQS2HG7ELXPI"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("img5DQS3N44UVGFTI47SRL7NI7A3U"),
						null,
						org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,
						true,
						false,
						false,
						org.radixware.kernel.common.enums.ECommandScope.OBJECT,
						null,
						org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_FIXED,
						true)
			},
			null,
			null,
			new org.radixware.kernel.common.client.meta.RadReferenceDef[]{

					new org.radixware.kernel.common.client.meta.RadReferenceDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("refBTXNQ3WNBRH65CF3ZPZBGEO56Q"),"ItemRef=>Item (intRef=>id)",org.radixware.kernel.common.types.Id.Factory.loadFrom("tblXWLPEIJAGRAXJG7SHDM4DH4X6I"),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblJ4WZRLJ23BGMBKQ2JBSOBEGBP4"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colAP5NJI3EIRE6PEUIUEQF5SLZZI")},new String[]{"intRef"},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colIUQVQZZKPFBRLG7DTABUYGLYBQ")},new String[]{"id"}),

					new org.radixware.kernel.common.client.meta.RadReferenceDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("refCJQCEOCYFVHAFCQ2B3E6V3F4BM"),"ItemRef=>UpValRef (upDefId=>defId, upOwnerEntityId=>ownerEntityId, upOwnerPid=>ownerPid)",org.radixware.kernel.common.types.Id.Factory.loadFrom("tblXWLPEIJAGRAXJG7SHDM4DH4X6I"),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblZVJUSFASRDNBDCBEABIFNQAABA"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colV5EZXMQFDFDBLO7M53XCUCWJ4A"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colTHM4RNL4JNBMZMT33HAY33ZU5U"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colGNVHWA56DJBFXOLL7VL7I4THUY")},new String[]{"upDefId","upOwnerEntityId","upOwnerPid"},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("col2BJUSFASRDNBDCBEABIFNQAABA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("col2FJUSFASRDNBDCBEABIFNQAABA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("col2JJUSFASRDNBDCBEABIFNQAABA")},new String[]{"defId","ownerEntityId","ownerPid"})
			},
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprDF4Y6Z3KOJEWFMJZNEQ4BN6UGA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("spr6WPTLLJ3ONAD7MSK66K5HMHBMA")},
			true,false,false);
}

/* Radix::CfgManagement::CfgItemRef - Web Meta*/

/*Radix::CfgManagement::CfgItemRef-Entity Class*/

package org.radixware.ads.CfgManagement.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class CfgItemRef_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::CfgManagement::CfgItemRef:Presentations-Entity Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecXWLPEIJAGRAXJG7SHDM4DH4X6I"),
			"Radix::CfgManagement::CfgItemRef",
			null,
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGLQNTIBT45H3PFSFKZGDTZOQZ4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3YU2NPLDLFAFFA4PSYQ7XDQQV4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGLQNTIBT45H3PFSFKZGDTZOQZ4"),0,
			null,
			null,
			null,
			null,
			null,
			null,
			false,false,false);
}

/* Radix::CfgManagement::CfgItemRef:General - Desktop Meta*/

/*Radix::CfgManagement::CfgItemRef:General-Editor Presentation*/

package org.radixware.ads.CfgManagement.explorer;
public final class General_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprDF4Y6Z3KOJEWFMJZNEQ4BN6UGA"),
	"General",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
	null,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("aecXWLPEIJAGRAXJG7SHDM4DH4X6I"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tblXWLPEIJAGRAXJG7SHDM4DH4X6I"),
	null,
	null,

	/*Radix::CfgManagement::CfgItemRef:General:Editor Pages-Editor Presentation Pages*/
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

			/*Radix::CfgManagement::CfgItemRef:General:General-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgKTXS2EQWTZDTDH2VGRT7PYFQSU"),"General",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecXWLPEIJAGRAXJG7SHDM4DH4X6I"),null,null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKOXIJGBJTJGYJAAVR2ASCGVMDI"),0,0,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNKM5TQLE5RHEZFGFLZYM7HRH3A"),0,1,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOBNSAADYD5CKFFOIDSKQI7TGLQ"),0,2,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXU6AK62EW5EWNE7KJGT4KVBNYQ"),0,3,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colBIPNKBLZUVATBPJTKZWEHGDGZE"),0,4,1,false,false)
			},null)
	},
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgKTXS2EQWTZDTDH2VGRT7PYFQSU"))}
	,

	/*Radix::CfgManagement::CfgItemRef:General:Children-Explorer Items*/
		null
	,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
	null,
	35,
	null,
	(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
	2192,0,0,null);
}
/* Radix::CfgManagement::CfgItemRef:General:Model - Desktop Executable*/

/*Radix::CfgManagement::CfgItemRef:General:Model-Entity Model Class*/

package org.radixware.ads.CfgManagement.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItemRef:General:Model")
public class General:Model  extends org.radixware.ads.CfgManagement.explorer.CfgItemRef.CfgItemRef_DefaultModel {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return General:Model_mi.rdxMeta; }



	public General:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::CfgManagement::CfgItemRef:General:Model:Nested classes-Nested Classes*/

	/*Radix::CfgManagement::CfgItemRef:General:Model:Properties-Properties*/

	/*Radix::CfgManagement::CfgItemRef:General:Model:type-Presentation Property*/




	public class Type extends org.radixware.ads.CfgManagement.explorer.CfgItemRef.colOBNSAADYD5CKFFOIDSKQI7TGLQ{
		public Type(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
			if (aemDF4Y6Z3KOJEWFMJZNEQ4BN6UGA.this.getDefinition().isPropertyDefExistsById(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXU6AK62EW5EWNE7KJGT4KVBNYQ"))) {
				this.addDependent(aemDF4Y6Z3KOJEWFMJZNEQ4BN6UGA.this.getProperty(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXU6AK62EW5EWNE7KJGT4KVBNYQ")));
			}
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.ads.CfgManagement.common.RefType dummy = x == null ? null : (x instanceof org.radixware.ads.CfgManagement.common.RefType ? (org.radixware.ads.CfgManagement.common.RefType)x : org.radixware.ads.CfgManagement.common.RefType.getForValue((Str)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.STR)));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.ads.CfgManagement.common.RefType> getValClass(){
			return org.radixware.ads.CfgManagement.common.RefType.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.ads.CfgManagement.common.RefType dummy = x == null ? null : (x instanceof org.radixware.ads.CfgManagement.common.RefType ? (org.radixware.ads.CfgManagement.common.RefType)x : org.radixware.ads.CfgManagement.common.RefType.getForValue((Str)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.STR)));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItemRef:General:Model:type")
		public published  org.radixware.ads.CfgManagement.common.RefType getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItemRef:General:Model:type")
		public published   void setValue(org.radixware.ads.CfgManagement.common.RefType val) {

			internal[type] = val;
			if (intRef.Value!=null && intRef.Value.isBroken())
			    intRef.Value = null;
		}
	}
	public Type getType(){return (Type)getProperty(colOBNSAADYD5CKFFOIDSKQI7TGLQ);}

	/*Radix::CfgManagement::CfgItemRef:General:Model:intRef-Presentation Property*/




	public class IntRef extends org.radixware.ads.CfgManagement.explorer.CfgItemRef.colXU6AK62EW5EWNE7KJGT4KVBNYQ{
		public IntRef(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
			super(model,def);
		}
		@Override
		public 
		org.radixware.ads.CfgManagement.explorer.CfgItem.CfgItem_DefaultModel openEntityModel()  throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{
		     return (org.radixware.ads.CfgManagement.explorer.CfgItem.CfgItem_DefaultModel)super.openEntityModel();
		}
		@Override
		public 
		org.radixware.ads.CfgManagement.explorer.CfgItem.DefaultGroupModel openGroupModel(){
		     return (org.radixware.ads.CfgManagement.explorer.CfgItem.DefaultGroupModel)super.openGroupModel();
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.client.types.Reference dummy = ((org.radixware.kernel.common.client.types.Reference)x);
			setValue(dummy);
		}

		/*Radix::CfgManagement::CfgItemRef:General:Model:intRef:EmbeddedClass-Property Presentation Embedded Class*/




		/*Radix::CfgManagement::CfgItemRef:General:Model:intRef:Nested classes-Nested Classes*/

		/*Radix::CfgManagement::CfgItemRef:General:Model:intRef:Properties-Properties*/

		/*Radix::CfgManagement::CfgItemRef:General:Model:intRef:Methods-Methods*/

		/*Radix::CfgManagement::CfgItemRef:General:Model:intRef:isReadonly-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItemRef:General:Model:intRef:isReadonly")
		public published  boolean isReadonly () {
			return type.Value != RefType:Internal || super.isReadonly();
		}













		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItemRef:General:Model:intRef")
		public published  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItemRef:General:Model:intRef")
		public published   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public IntRef getIntRef(){return (IntRef)getProperty(colXU6AK62EW5EWNE7KJGT4KVBNYQ);}










	/*Radix::CfgManagement::CfgItemRef:General:Model:Methods-Methods*/

	/*Radix::CfgManagement::CfgItemRef:General:Model:afterRead-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItemRef:General:Model:afterRead")
	protected published  void afterRead () {
		srcInherited.setVisible(isInheritable.Value == true);

		Explorer.EditMask::EditMaskConstSet typeMask = (Explorer.EditMask::EditMaskConstSet) type.getEditMask();
		Explorer.Meta::EnumItems excludeTypes = typeMask.getExcludedItems(getApplication());
		if (isInheritable.Value != true)
		    excludeTypes.addItem(RefType:Inherit);
		if (isNilable.Value != true)
		    excludeTypes.addItem(RefType:Null);
		typeMask.setExcludedItems(excludeTypes);

		getCommand(idof[CfgItemRef:AcceptAmbig]).setEnabled(state.Value == RefState:Ambiguity);

		super.afterRead();
	}

	/*Radix::CfgManagement::CfgItemRef:General:Model:onCommand_AcceptAmbig-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::CfgManagement::CfgItemRef:General:Model:onCommand_AcceptAmbig")
	protected  void onCommand_AcceptAmbig (org.radixware.ads.CfgManagement.explorer.CfgItemRef.AcceptAmbig command) {
		try {
		    command.send();
		} catch (Exceptions::InterruptedException e) {
		    showException(e);
		} catch (Exceptions::ServiceClientException e) {
		    showException(e);
		}
	}
	public final class AcceptAmbig extends org.radixware.ads.CfgManagement.explorer.CfgItemRef.AcceptAmbig{
		protected AcceptAmbig(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_AcceptAmbig( this );
		}

	}













}

/* Radix::CfgManagement::CfgItemRef:General:Model - Desktop Meta*/

/*Radix::CfgManagement::CfgItemRef:General:Model-Entity Model Class*/

package org.radixware.ads.CfgManagement.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class General:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aemDF4Y6Z3KOJEWFMJZNEQ4BN6UGA"),
						"General:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::CfgManagement::CfgItemRef:General:Model:Properties-Properties*/
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

/* Radix::CfgManagement::CfgItemRef:General - Desktop Meta*/

/*Radix::CfgManagement::CfgItemRef:General-Selector Presentation*/

package org.radixware.ads.CfgManagement.explorer;
public final class General_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new General_mi();
	private General_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("spr6WPTLLJ3ONAD7MSK66K5HMHBMA"),
		"General",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
		null,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aecXWLPEIJAGRAXJG7SHDM4DH4X6I"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tblXWLPEIJAGRAXJG7SHDM4DH4X6I"),
		null,
		null,
		null,
		new org.radixware.kernel.common.types.Id[]{},
		false,
		null,
		new org.radixware.kernel.common.types.Id[]{},
		false,
		false,
		null,
		35,
		null,
		144,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprDF4Y6Z3KOJEWFMJZNEQ4BN6UGA")},
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprDF4Y6Z3KOJEWFMJZNEQ4BN6UGA")},
		false,true,false,0,0);
		columns = new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn[]{

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colBIPNKBLZUVATBPJTKZWEHGDGZE"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null)
		};
	}
	@Override
	protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.CfgManagement.explorer.CfgItemRef.DefaultGroupModel(userSession,this);
	}
}
/* Radix::CfgManagement::CfgItemRef - Localizing Bundle */
package org.radixware.ads.CfgManagement.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class CfgItemRef - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"В системе-источнике наследуется");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Inherited in source system");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3CG3TFTGO5GR5JSA57MJO5RXUA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Связи");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"References");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3YU2NPLDLFAFFA4PSYQ7XDQQV4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Тип");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Type");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls54ZTX4GZEVGOPHONPRF37ROIIQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"RID в системе-источнике");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"RID in source system");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsE3JJQR6U6NBSDAQEDTPVSXVQ2Y"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Связь");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Reference");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGLQNTIBT45H3PFSFKZGDTZOQZ4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Объект в пакете");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Object in package");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGRKV6AOK4NELDCVZATCFB6FKEA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Между элементами пакета конфигурации обнаружена циклическая связь:  \n%s");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Circular dependence detected:\n%s");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJW72IQ7KZ5BTFLKFOUMZDTLJKE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Состояние");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"State");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsODNQFDCM2RBPVIDRPFZP727URY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Принять неоднозначность");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Accept Ambiguity");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSKELH7K6DRE2FAKQS2HG7ELXPI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(CfgItemRef - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbaecXWLPEIJAGRAXJG7SHDM4DH4X6I"),"CfgItemRef - Localizing Bundle",$$$items$$$);
}
