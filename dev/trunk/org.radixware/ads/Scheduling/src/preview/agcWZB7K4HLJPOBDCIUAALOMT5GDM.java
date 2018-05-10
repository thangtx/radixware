
/* Radix::Scheduling::TaskGroup - Server Executable*/

/*Radix::Scheduling::TaskGroup-Entity Group Class*/

package org.radixware.ads.Scheduling.server;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::TaskGroup")
public published class TaskGroup  extends org.radixware.ads.Types.server.EntityGroup<org.radixware.ads.Scheduling.server.Task>  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {


	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return TaskGroup_mi.rdxMeta;}

	public TaskGroup(boolean isContextWrapper){super(isContextWrapper);}
	/*Radix::Scheduling::TaskGroup:Nested classes-Nested Classes*/

	/*Radix::Scheduling::TaskGroup:Properties-Properties*/

	/*Radix::Scheduling::TaskGroup:taskToMoveId-Entity Group Parameter*/



	protected Int taskToMoveId=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::TaskGroup:taskToMoveId")
	public  Int getTaskToMoveId() {
		return taskToMoveId;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::TaskGroup:taskToMoveId")
	public   void setTaskToMoveId(Int val) {
		taskToMoveId = val;
	}

	/*Radix::Scheduling::TaskGroup:taskToMoveDirId-Entity Group Parameter*/



	protected Int taskToMoveDirId=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::TaskGroup:taskToMoveDirId")
	public  Int getTaskToMoveDirId() {
		return taskToMoveDirId;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::TaskGroup:taskToMoveDirId")
	public   void setTaskToMoveDirId(Int val) {
		taskToMoveDirId = val;
	}

	/*Radix::Scheduling::TaskGroup:contextUnitId-Entity Group Parameter*/



	protected Int contextUnitId=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::TaskGroup:contextUnitId")
	public  Int getContextUnitId() {
		return contextUnitId;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::TaskGroup:contextUnitId")
	public   void setContextUnitId(Int val) {
		contextUnitId = val;
	}















































	/*Radix::Scheduling::TaskGroup:Methods-Methods*/

	/*Radix::Scheduling::TaskGroup:onCommand_Import-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::TaskGroup:onCommand_Import")
	public  void onCommand_Import (org.radixware.ads.Scheduling.common.TaskExportXsd.TaskSetDocument input) {
		TaskImpExpUtils.importTasks(input);
	}

	/*Radix::Scheduling::TaskGroup:onListInstantiableClasses-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::TaskGroup:onListInstantiableClasses")
	public published  void onListInstantiableClasses (org.radixware.kernel.common.types.Id classCatalogId, java.util.List<org.radixware.kernel.server.types.EntityGroup.ClassCatalogItem> list) {
		final Object[] emptyParameters = new Object[]{};
		java.util.Iterator<Types::EntityClassCatalogItem> iterator = list.iterator();

		Task.AGroup parentGroup = null; 
		if (this.Context instanceof Types::EntityGroup.TreeContext) {
		    final Types::Pid parentPid = ((Types::EntityGroup.TreeContext) this.Context).getParentPid();
		    if (parentPid != null) {
		        final Types::Entity parent = Task.load(parentPid);
		        if (parent instanceof Task.AGroup) {
		            parentGroup = (Task.AGroup) parent;
		        }
		    }
		}

		while (iterator.hasNext()) {
		    try {
		        final Task task = (Task) Arte::Arte.getDefManager().newClassInstance(iterator.next().getClassId(), emptyParameters);
		        try {
		            if (idof[Task:TreeRootManual].equals(getPresentation().getId())) {
		                if (!task.canBeManual() || !task.canBeRoot()) {
		                    iterator.remove();
		                }
		            } else if (idof[Task:TreeRootForScheduler].equals(getPresentation().getId())) {
		                if (!task.canBeRoot()) {
		                    iterator.remove();
		                }
		            } else if (parentGroup != null && task instanceof Task.Dir) {
		                if (!parentGroup.canContainDirectory()) {
		                    iterator.remove();
		                }
		            }
		        } finally {
		            task.discard();
		        }
		    } catch (Exceptions::RuntimeException ex) {
		        //do nothing
		    }
		}
	}

	/*Radix::Scheduling::TaskGroup:onCommand_Export-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::TaskGroup:onCommand_Export")
	public  org.radixware.ads.Scheduling.common.TaskExportXsd.TaskSetDocument onCommand_Export (org.radixware.ads.Scheduling.common.TaskExportXsd.ExportRequestDocument input) {
		return TaskImpExpUtils.exportTasks(input);
	}

	/*Radix::Scheduling::TaskGroup:onCommand_MoveToDir-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::TaskGroup:onCommand_MoveToDir")
	public  org.radixware.kernel.server.types.FormHandler.NextDialogsRequest onCommand_MoveToDir () {
		return new FormHandlerNextDialogsRequest(null, new ChangeDirSettings(null));
	}





	@Override
	public org.radixware.kernel.server.types.FormHandler.NextDialogsRequest execCommand(org.radixware.kernel.common.types.Id cmdId, org.apache.xmlbeans.XmlObject input, org.apache.xmlbeans.XmlObject output) throws org.radixware.kernel.common.exceptions.AppException,java.lang.InterruptedException{
		if(cmdId == cmd2UHAXVOFHFHOBHJHA3MTO6RG4I){
			org.radixware.kernel.server.types.FormHandler.NextDialogsRequest result = onCommand_MoveToDir();
		return result;
	} else if(cmdId == cmdAPHHMXDSJFCETGLMMAP5YHD5G4){
		org.radixware.ads.Scheduling.common.TaskExportXsd.TaskSetDocument result = onCommand_Export((org.radixware.ads.Scheduling.common.TaskExportXsd.ExportRequestDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.getAppCommandInputData(getClass().getClassLoader(),input,org.radixware.ads.Scheduling.common.TaskExportXsd.ExportRequestDocument.class));if(result != null)
			output.set(result);
		return null;
	} else if(cmdId == cmdDTHSRLKFNBCZZNQNYOOIVOXCGI){
		onCommand_Import((org.radixware.ads.Scheduling.common.TaskExportXsd.TaskSetDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.getAppCommandInputData(getClass().getClassLoader(),input,org.radixware.ads.Scheduling.common.TaskExportXsd.TaskSetDocument.class));return null;
	} else 
		return super.execCommand(cmdId,input,output);
}


}

/* Radix::Scheduling::TaskGroup - Server Meta*/

/*Radix::Scheduling::TaskGroup-Entity Group Class*/

package org.radixware.ads.Scheduling.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class TaskGroup_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("agcWZB7K4HLJPOBDCIUAALOMT5GDM"),"TaskGroup",null,

						org.radixware.kernel.common.enums.EClassType.ENTITY_GROUP,

						/*Radix::Scheduling::TaskGroup:Presentations-Entity Group Presentations*/
						/*Class presentation attributes*/
						 new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef(/*Owner Class Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("agcWZB7K4HLJPOBDCIUAALOMT5GDM"),
							/*Owner Class Name*/
							"TaskGroup",
							/*Title Id*/
							null,
							/*Property presentations*/

							/*Radix::Scheduling::TaskGroup:Properties-Properties*/
							new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef[]{

									/*Radix::Scheduling::TaskGroup:taskToMoveId:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pgpA4UVFEA7ABACRAXLSXMAUADMSA"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Scheduling::TaskGroup:taskToMoveDirId:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pgpJYY3VQQLENCTXN54C4BF4AXOYU"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Scheduling::TaskGroup:contextUnitId:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pgpD3SFQIQMX5D3HADR4R5ASRTJS4"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT))
							},
							/*Commands*/
							new org.radixware.kernel.server.meta.presentations.RadCommandDef[]{

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::Scheduling::TaskGroup:Export-Group Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdAPHHMXDSJFCETGLMMAP5YHD5G4"),"Export",org.radixware.kernel.common.enums.ECommandScope.GROUP,null,org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS,org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,org.radixware.kernel.common.types.Id.Factory.loadFrom("agcWZB7K4HLJPOBDCIUAALOMT5GDM"),false,true),

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::Scheduling::TaskGroup:Import-Group Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdDTHSRLKFNBCZZNQNYOOIVOXCGI"),"Import",org.radixware.kernel.common.enums.ECommandScope.GROUP,null,org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS,org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,org.radixware.kernel.common.types.Id.Factory.loadFrom("agcWZB7K4HLJPOBDCIUAALOMT5GDM"),false,true),

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::Scheduling::TaskGroup:MoveToDir-Group Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmd2UHAXVOFHFHOBHJHA3MTO6RG4I"),"MoveToDir",org.radixware.kernel.common.enums.ECommandScope.GROUP,null,org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS,org.radixware.kernel.common.enums.ECommandNature.XML_IN_FORM_OUT,org.radixware.kernel.common.types.Id.Factory.loadFrom("agcWZB7K4HLJPOBDCIUAALOMT5GDM"),false,true)
							},
							/*Sortings*/
							null,
							/*Filters*/
							null,
							/*Editor presentations*/
							null,
							/*Selector presentations*/
							null,
							/*Default selector presentation Id*/
							null,
							/*Presentation Map*/
							null,
							/*Object title format*/
							null,
							/*Class catalogs*/
							null,
							/*Restrictions*/
							null,null,0),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblWZB7K4HLJPOBDCIUAALOMT5GDM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("pdcEntityGroup_______________"),

						/*Radix::Scheduling::TaskGroup:Properties-Properties*/
						new org.radixware.kernel.server.meta.clazzes.RadPropDef[]{

								/*Radix::Scheduling::TaskGroup:taskToMoveId-Entity Group Parameter*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pgpA4UVFEA7ABACRAXLSXMAUADMSA"),"taskToMoveId",null,org.radixware.kernel.common.enums.EValType.INT,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::TaskGroup:taskToMoveDirId-Entity Group Parameter*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pgpJYY3VQQLENCTXN54C4BF4AXOYU"),"taskToMoveDirId",null,org.radixware.kernel.common.enums.EValType.INT,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::TaskGroup:contextUnitId-Entity Group Parameter*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pgpD3SFQIQMX5D3HADR4R5ASRTJS4"),"contextUnitId",null,org.radixware.kernel.common.enums.EValType.INT,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE)
						},

						/*Radix::Scheduling::TaskGroup:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthcmdDTHSRLKFNBCZZNQNYOOIVOXCGI"),"onCommand_Import",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("input",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprA6ACGUBFXZA3RI76KKJM3J3TI4"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth7MKI5PVAVJHQBGNMFO246UGC4Y"),"onListInstantiableClasses",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("classCatalogId",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprXDD4AWUFHRBYLGNDAV3B43DTJE")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("list",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprSCS3B3766BELBKOAEP5HD5C5EQ"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthcmdAPHHMXDSJFCETGLMMAP5YHD5G4"),"onCommand_Export",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("input",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprVAW7PRCMQRDR7C7736QXM2EBSI"))
								},org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthcmd2UHAXVOFHFHOBHJHA3MTO6RG4I"),"onCommand_MoveToDir",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.JAVA_CLASS)
						},
						null,
						null,null,null,false);
}

/* Radix::Scheduling::TaskGroup - Desktop Executable*/

/*Radix::Scheduling::TaskGroup-Entity Group Class*/

package org.radixware.ads.Scheduling.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::TaskGroup")
public class TaskGroup {







	public static org.radixware.kernel.common.client.models.items.Command createCommand(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){
		@SuppressWarnings("unused")
		org.radixware.kernel.common.types.Id commandId = def.getId();
		if(cmd2UHAXVOFHFHOBHJHA3MTO6RG4I == commandId) return new TaskGroup.MoveToDir(model,def);
		else if(cmdAPHHMXDSJFCETGLMMAP5YHD5G4 == commandId) return new TaskGroup.Export(model,def);
		else if(cmdDTHSRLKFNBCZZNQNYOOIVOXCGI == commandId) return new TaskGroup.Import(model,def);
		else return null;
	}

	public static class MoveToDir extends org.radixware.kernel.common.client.models.items.Command{
		protected MoveToDir(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public void send() throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,null);
			processResponse(response, null);
		}

	}

	public static class Export extends org.radixware.kernel.common.client.models.items.Command{
		protected Export(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public org.radixware.ads.Scheduling.common.TaskExportXsd.TaskSetDocument send(org.radixware.ads.Scheduling.common.TaskExportXsd.ExportRequestDocument input ) throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,input);
			org.apache.xmlbeans.XmlObject xmlResponse = processResponse(response, null);
			return (org.radixware.ads.Scheduling.common.TaskExportXsd.TaskSetDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),xmlResponse,org.radixware.ads.Scheduling.common.TaskExportXsd.TaskSetDocument.class);
		}

	}

	public static class Import extends org.radixware.kernel.common.client.models.items.Command{
		protected Import(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public void send(org.radixware.ads.Scheduling.common.TaskExportXsd.TaskSetDocument input ) throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,input);
			processResponse(response, null);
		}

	}



}

/* Radix::Scheduling::TaskGroup - Desktop Meta*/

/*Radix::Scheduling::TaskGroup-Entity Group Class*/

package org.radixware.ads.Scheduling.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class TaskGroup_mi{
	public static final org.radixware.kernel.common.client.meta.RadGroupHandlerDef rdxMeta = 
	/*Radix::Scheduling::TaskGroup:Presentations-Entity Group Presentations*/
	new org.radixware.kernel.common.client.meta.RadGroupHandlerDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("agcWZB7K4HLJPOBDCIUAALOMT5GDM"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWZB7K4HLJPOBDCIUAALOMT5GDM"),
			new org.radixware.kernel.common.client.meta.RadCommandDef[]{
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::Scheduling::TaskGroup:Export-Group Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdAPHHMXDSJFCETGLMMAP5YHD5G4"),
						"Export",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("agcWZB7K4HLJPOBDCIUAALOMT5GDM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBG3MJAXFBVDCTMY73G3F3TR3AI"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("imgYGRKUM6D5NFB7HJSZFG3KGBC6M"),
						null,
						org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,
						true,
						false,
						false,
						org.radixware.kernel.common.enums.ECommandScope.GROUP,
						null,
						org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS),
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::Scheduling::TaskGroup:Import-Group Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdDTHSRLKFNBCZZNQNYOOIVOXCGI"),
						"Import",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("agcWZB7K4HLJPOBDCIUAALOMT5GDM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7IDNIBWQ2ZAWBLUQDRTBFH7T6A"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("imgEVEBHW62BFDDBLQVX2YPL7ZERM"),
						null,
						org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,
						true,
						false,
						false,
						org.radixware.kernel.common.enums.ECommandScope.GROUP,
						null,
						org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS),
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::Scheduling::TaskGroup:MoveToDir-Group Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmd2UHAXVOFHFHOBHJHA3MTO6RG4I"),
						"MoveToDir",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("agcWZB7K4HLJPOBDCIUAALOMT5GDM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNFBGST65HRGTZIWDSMVAJY4SJQ"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("imgLVVSMDCLPJGPXHR75NNSZ3HR4I"),
						null,
						org.radixware.kernel.common.enums.ECommandNature.XML_IN_FORM_OUT,
						true,
						false,
						false,
						org.radixware.kernel.common.enums.ECommandScope.GROUP,
						null,
						org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS)
			},

			/*Radix::Scheduling::TaskGroup:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::Scheduling::TaskGroup:taskToMoveId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pgpA4UVFEA7ABACRAXLSXMAUADMSA"),
						"taskToMoveId",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("agcWZB7K4HLJPOBDCIUAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.GROUP_PROPERTY,
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

						/*Radix::Scheduling::TaskGroup:taskToMoveId:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(null,null,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::TaskGroup:taskToMoveDirId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pgpJYY3VQQLENCTXN54C4BF4AXOYU"),
						"taskToMoveDirId",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("agcWZB7K4HLJPOBDCIUAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.GROUP_PROPERTY,
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

						/*Radix::Scheduling::TaskGroup:taskToMoveDirId:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(null,null,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::TaskGroup:contextUnitId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pgpD3SFQIQMX5D3HADR4R5ASRTJS4"),
						"contextUnitId",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("agcWZB7K4HLJPOBDCIUAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.GROUP_PROPERTY,
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

						/*Radix::Scheduling::TaskGroup:contextUnitId:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(null,null,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false)
			});
	}

	/* Radix::Scheduling::TaskGroup - Web Executable*/

	/*Radix::Scheduling::TaskGroup-Entity Group Class*/

	package org.radixware.ads.Scheduling.web;

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::TaskGroup")
	public class TaskGroup {







		public static org.radixware.kernel.common.client.models.items.Command createCommand(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){
			@SuppressWarnings("unused")
			org.radixware.kernel.common.types.Id commandId = def.getId();
			if(cmd2UHAXVOFHFHOBHJHA3MTO6RG4I == commandId) return new TaskGroup.MoveToDir(model,def);
			else if(cmdAPHHMXDSJFCETGLMMAP5YHD5G4 == commandId) return new TaskGroup.Export(model,def);
			else if(cmdDTHSRLKFNBCZZNQNYOOIVOXCGI == commandId) return new TaskGroup.Import(model,def);
			else return null;
		}

		public static class MoveToDir extends org.radixware.kernel.common.client.models.items.Command{
			protected MoveToDir(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
			public void send() throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
				org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,null);
				processResponse(response, null);
			}

		}

		public static class Export extends org.radixware.kernel.common.client.models.items.Command{
			protected Export(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
			public org.radixware.ads.Scheduling.common.TaskExportXsd.TaskSetDocument send(org.radixware.ads.Scheduling.common.TaskExportXsd.ExportRequestDocument input ) throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
				org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,input);
				org.apache.xmlbeans.XmlObject xmlResponse = processResponse(response, null);
				return (org.radixware.ads.Scheduling.common.TaskExportXsd.TaskSetDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),xmlResponse,org.radixware.ads.Scheduling.common.TaskExportXsd.TaskSetDocument.class);
			}

		}

		public static class Import extends org.radixware.kernel.common.client.models.items.Command{
			protected Import(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
			public void send(org.radixware.ads.Scheduling.common.TaskExportXsd.TaskSetDocument input ) throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
				org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,input);
				processResponse(response, null);
			}

		}



	}

	/* Radix::Scheduling::TaskGroup - Web Meta*/

	/*Radix::Scheduling::TaskGroup-Entity Group Class*/

	package org.radixware.ads.Scheduling.web;
	@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
	public final class TaskGroup_mi{
		public static final org.radixware.kernel.common.client.meta.RadGroupHandlerDef rdxMeta = 
		/*Radix::Scheduling::TaskGroup:Presentations-Entity Group Presentations*/
		new org.radixware.kernel.common.client.meta.RadGroupHandlerDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("agcWZB7K4HLJPOBDCIUAALOMT5GDM"),
				org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWZB7K4HLJPOBDCIUAALOMT5GDM"),
				null,

				/*Radix::Scheduling::TaskGroup:Properties-Properties*/
				new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

						/*Radix::Scheduling::TaskGroup:taskToMoveId:PropertyPresentation-Property Presentation*/
						new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pgpA4UVFEA7ABACRAXLSXMAUADMSA"),
							"taskToMoveId",
							null,
							null,
							org.radixware.kernel.common.types.Id.Factory.loadFrom("agcWZB7K4HLJPOBDCIUAALOMT5GDM"),
							org.radixware.kernel.common.enums.EValType.INT,
							org.radixware.kernel.common.enums.EPropNature.GROUP_PROPERTY,
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

							/*Radix::Scheduling::TaskGroup:taskToMoveId:PropertyPresentation:Edit Options:-Edit Mask Int*/
							new org.radixware.kernel.common.client.meta.mask.EditMaskInt(null,null,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
							null,
							null,
							null,
							true,-1,-1,1,
							false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

						/*Radix::Scheduling::TaskGroup:taskToMoveDirId:PropertyPresentation-Property Presentation*/
						new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pgpJYY3VQQLENCTXN54C4BF4AXOYU"),
							"taskToMoveDirId",
							null,
							null,
							org.radixware.kernel.common.types.Id.Factory.loadFrom("agcWZB7K4HLJPOBDCIUAALOMT5GDM"),
							org.radixware.kernel.common.enums.EValType.INT,
							org.radixware.kernel.common.enums.EPropNature.GROUP_PROPERTY,
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

							/*Radix::Scheduling::TaskGroup:taskToMoveDirId:PropertyPresentation:Edit Options:-Edit Mask Int*/
							new org.radixware.kernel.common.client.meta.mask.EditMaskInt(null,null,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
							null,
							null,
							null,
							true,-1,-1,1,
							false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

						/*Radix::Scheduling::TaskGroup:contextUnitId:PropertyPresentation-Property Presentation*/
						new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pgpD3SFQIQMX5D3HADR4R5ASRTJS4"),
							"contextUnitId",
							null,
							null,
							org.radixware.kernel.common.types.Id.Factory.loadFrom("agcWZB7K4HLJPOBDCIUAALOMT5GDM"),
							org.radixware.kernel.common.enums.EValType.INT,
							org.radixware.kernel.common.enums.EPropNature.GROUP_PROPERTY,
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

							/*Radix::Scheduling::TaskGroup:contextUnitId:PropertyPresentation:Edit Options:-Edit Mask Int*/
							new org.radixware.kernel.common.client.meta.mask.EditMaskInt(null,null,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
							null,
							null,
							null,
							true,-1,-1,1,
							false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false)
				});
		}

		/* Radix::Scheduling::TaskGroup - Localizing Bundle */
		package org.radixware.ads.Scheduling.common;
		@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
		public final class TaskGroup - Localizing Bundle_mi{
			@SuppressWarnings("unused")
			private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
			static{
				loadStrings1();
			}

			@SuppressWarnings("unused")
			private static void loadStrings1(){
				java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
				$$$strings$$$.clear();
				$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Import Tasks (add)");
				$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Импортировать задачи (добавить)");
				$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7IDNIBWQ2ZAWBLUQDRTBFH7T6A"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
				$$$strings$$$.clear();
				$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Export All");
				$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Экспортировать все");
				$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBG3MJAXFBVDCTMY73G3F3TR3AI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
				$$$strings$$$.clear();
				$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Move Task to Directory");
				$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Перенести задачу в директорию");
				$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNFBGST65HRGTZIWDSMVAJY4SJQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
			}

			public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(TaskGroup - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbagcWZB7K4HLJPOBDCIUAALOMT5GDM"),"TaskGroup - Localizing Bundle",$$$items$$$);
		}
