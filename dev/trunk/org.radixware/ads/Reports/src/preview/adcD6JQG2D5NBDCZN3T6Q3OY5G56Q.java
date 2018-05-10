
/* Radix::Reports::ReportsServerUtils - Server Executable*/

/*Radix::Reports::ReportsServerUtils-Server Dynamic Class*/

package org.radixware.ads.Reports.server;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportsServerUtils")
public published class ReportsServerUtils  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {


	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return ReportsServerUtils_mi.rdxMeta;}

	/*Radix::Reports::ReportsServerUtils:Nested classes-Nested Classes*/

	/*Radix::Reports::ReportsServerUtils:Properties-Properties*/





























	/*Radix::Reports::ReportsServerUtils:Methods-Methods*/

	/*Radix::Reports::ReportsServerUtils:instantiateReportByClassId-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportsServerUtils:instantiateReportByClassId")
	public static published  org.radixware.ads.Types.server.Report instantiateReportByClassId (org.radixware.kernel.common.types.Id reportClassId) {
		final Class<?> reportClass = Arte::Arte.getDefManager().getClass(reportClassId);
		Arte::Arte.getInstance().DefManager.getClassDef(reportClassId); //check definition

		try{
		    return (Types::Report)reportClass.newInstance();
		}catch(IllegalAccessException ex){
		    throw new AppError("Unable to instantiate report", ex);
		}catch(InstantiationException ex){
		    throw new AppError("Unable to instantiate report", ex);
		}

	}

	/*Radix::Reports::ReportsServerUtils:loadParamFromXml-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportsServerUtils:loadParamFromXml")
	public static published  java.lang.Object loadParamFromXml (org.radixware.ads.Types.server.Report report, org.radixware.kernel.common.types.Id propId, org.radixware.schemas.eas.Property xProp) {
		if (xProp==null)
		    return null;

		final Meta::PropDef propDef = report.RadMeta.getPropById(propId);
		Object paramValue;

		if (propDef instanceof org.radixware.kernel.server.meta.clazzes.IRadRefPropertyDef){
		    org.radixware.kernel.server.meta.clazzes.IRadRefPropertyDef refPropDef =
		            (org.radixware.kernel.server.meta.clazzes.IRadRefPropertyDef)propDef;
		    final Types::Id refEntityId = refPropDef.DestinationEntityId;
		    paramValue = org.radixware.kernel.server.arte.services.eas.EasValueConverter.easPropXmlVal2ObjVal(report.Arte, xProp, propDef.ValType, refEntityId);
		    if (paramValue instanceof Types::Pid){
		        paramValue = report.Arte.getEntityObject((Types::Pid)paramValue);
		    }
		}else{
		    paramValue = org.radixware.kernel.server.arte.services.eas.EasValueConverter.easPropXmlVal2ObjVal(report.Arte, xProp, propDef.ValType, null);
		}

		return paramValue;

	}

	/*Radix::Reports::ReportsServerUtils:loadParamsFromXml-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportsServerUtils:loadParamsFromXml")
	public static published  java.util.HashMap<org.radixware.kernel.common.types.Id,java.lang.Object> loadParamsFromXml (org.radixware.ads.Types.server.Report report, java.util.List<? extends org.radixware.schemas.eas.Property> xmlProps) {
		final java.util.HashMap<Types::Id, Object> paramId2Value = new java.util.HashMap<Types::Id, Object>();

		for (org.radixware.schemas.eas.Property xProp : xmlProps) {
		    final Types::Id propId = xProp.getId();
		    final Object paramValue = loadParamFromXml(report, propId, xProp);
		    paramId2Value.put(propId, paramValue);
		}

		return paramId2Value;
	}

	/*Radix::Reports::ReportsServerUtils:openFileOutResource-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportsServerUtils:openFileOutResource")
	public static published  org.radixware.kernel.server.arte.resources.FileOutResource openFileOutResource (Str fileName) {
		try {
		    return new FileOutResource(
		            Arte::Arte.getInstance(),
		            fileName,
		            Client.Resources::FileOpenMode:TruncateOrCreate,
		            Client.Resources::FileOpenShareMode:Write);
		} catch (Exceptions::ResourceUsageException | Exceptions::ResourceUsageTimeout | InterruptedException ex) {
		    throw new AppError("Unable to open the file output resource", ex);
		}

	}

	/*Radix::Reports::ReportsServerUtils:generateReport-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportsServerUtils:generateReport")
	public static published  void generateReport (org.radixware.ads.Types.server.Entity context, org.radixware.schemas.reports.GenerateReportRqDocument input) {
		final String pidAsStr = input.GenerateReportRq.ReportPubPid;
		Types::Pid pid = new Pid(Arte::Arte.getInstance(), idof[Reports::ReportPub], pidAsStr);
		ReportPub pub = (ReportPub)Arte::Arte.getEntityObject(pid);
		pub.generateReport(context, input);

	}

	/*Radix::Reports::ReportsServerUtils:getClassInfo-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportsServerUtils:getClassInfo")
	public static published  Str getClassInfo (org.radixware.kernel.common.types.Id classId) {
		if (classId == null) {
		    return String.valueOf(classId);
		}

		try {
		    final Meta::ClassDef classDef = Arte::Arte.getInstance().DefManager.getClassDef(classId);
		    final String name = classDef.Name;
		    return "'" + name + "'" + " (#" + String.valueOf(classId) + ")";
		} catch (Exceptions::Throwable ex) {
		    return "#" + String.valueOf(classId);
		}

	}

	/*Radix::Reports::ReportsServerUtils:getClassInfo-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportsServerUtils:getClassInfo")
	public static published  Str getClassInfo (org.radixware.ads.Types.server.Entity entity) {
		if (entity != null) {
		    return "'" + entity.getClassDefinitionName() + "' (#" + entity.getClassDefinitionId() + ")";
		} else {
		    return String.valueOf(entity);
		}

	}

	/*Radix::Reports::ReportsServerUtils:findParameterBindingById-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportsServerUtils:findParameterBindingById")
	public static published  org.radixware.schemas.reports.ParametersBindingType.ParameterBinding findParameterBindingById (org.radixware.schemas.reports.ParametersBindingDocument parametersBindingDoc, org.radixware.kernel.common.types.Id paramId) {
		if (parametersBindingDoc!=null){
		    ReportsXsd:ParametersBindingType parametersBinding = parametersBindingDoc.getParametersBinding();
		    if (parametersBinding!=null){
		        final java.util.List<ReportsXsd:ParametersBindingType.ParameterBinding> bindingList = parametersBinding.ParameterBindingList;
		        if (bindingList!=null){
		            for (ReportsXsd:ParametersBindingType.ParameterBinding binding : bindingList){
		                if (paramId.equals(binding.ParameterId)){
		                    return binding;
		                }
		            }
		        }
		    }
		}
		return null;

	}

	/*Radix::Reports::ReportsServerUtils:isFileExists-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportsServerUtils:isFileExists")
	public static published  boolean isFileExists (Str fileName) {
		try {
		    return Client.Resources::FileResource.isExists(Arte::Arte.getInstance(), fileName, org.radixware.kernel.server.arte.resources.Resource.DEFAULT_TIMEOUT);
		} catch (Exceptions::ResourceUsageException | Exceptions::ResourceUsageTimeout | InterruptedException ex) {
		    throw new AppError("Unable to check the file for availability", ex);
		}
	}

	/*Radix::Reports::ReportsServerUtils:showReplaceFileDialog-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportsServerUtils:showReplaceFileDialog")
	public static published  org.radixware.kernel.common.enums.EDialogButtonType showReplaceFileDialog (Str fileName) {
		java.util.LinkedHashSet<Client.Resources::MessageDialogButton> replaceFileDialogVariants = new java.util.LinkedHashSet<Client.Resources::MessageDialogButton>();
		replaceFileDialogVariants.add(new MessageDialogButton(Client.Resources::DialogButtonType:Yes, null, idof[Explorer.Icons::button_ok]));
		replaceFileDialogVariants.add(new MessageDialogButton(Client.Resources::DialogButtonType:YesToAll, null, idof[Explorer.Icons::yes_all]));
		replaceFileDialogVariants.add(new MessageDialogButton(Client.Resources::DialogButtonType:No, null, idof[Explorer.Icons::button_cancel]));
		replaceFileDialogVariants.add(new MessageDialogButton(Client.Resources::DialogButtonType:NoToAll, null, idof[Explorer.Icons::no_all]));

		try {
		    Client.Resources::MessageDialogButton result = Client.Resources::MessageDialogResource.show(
		            Arte::Arte.getInstance(),
		            Client.Resources::DialogType:Information,
		            "Replace or Skip Files",
		            replaceFileDialogVariants,
		            Utils::MessageFormatter.format("File \"{0}\" already exists. Do you want to replace it?", fileName)
		    );
		    return result.getType();
		} catch (Exceptions::ResourceUsageException | Exceptions::ResourceUsageTimeout | InterruptedException ex) {
		    throw new AppError("Unable to open replace file dialog", ex);
		}
	}

	/*Radix::Reports::ReportsServerUtils:getColumnsWidthMap-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportsServerUtils:getColumnsWidthMap")
	public static  java.util.Map<org.radixware.kernel.common.types.Id,org.radixware.schemas.reports.ReportColumnsList.Column.Width> getColumnsWidthMap (org.radixware.ads.Types.server.Report report) {
		java.util.Map<Types::Id, ReportsXsd:ReportColumnsList.Column.Width> result = new java.util.HashMap<Types::Id, ReportsXsd:ReportColumnsList.Column.Width>();

		org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportForm form = report.createForm();
		for (org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportBand band : form.getColumnHeaderBands()) {
		    loadColumnsWidthFromContainer(band, result);
		}

		for (org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportBand band : form.getDetailBands()) {
		    loadColumnsWidthFromContainer(band, result);
		}

		for (org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportBand band : form.getSummaryBands()) {
		    loadColumnsWidthFromContainer(band, result);
		}

		for (org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportGroup group : form.getGroups()) {
		    if (group.getFooterBand() != null) {
		        loadColumnsWidthFromContainer(group.getFooterBand(), result);
		    }
		}

		return result;
	}

	/*Radix::Reports::ReportsServerUtils:loadColumnsWidthFromContainer-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportsServerUtils:loadColumnsWidthFromContainer")
	private static  void loadColumnsWidthFromContainer (org.radixware.kernel.common.defs.ads.clazz.sql.report.IReportWidgetContainer container, java.util.Map<org.radixware.kernel.common.types.Id,org.radixware.schemas.reports.ReportColumnsList.Column.Width> column2WidthMap) {
		for (org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportWidget widget : container.getWidgets()) {
		    if (widget instanceof org.radixware.kernel.common.defs.ads.clazz.sql.report.IReportWidgetContainer) {
		        loadColumnsWidthFromContainer((org.radixware.kernel.common.defs.ads.clazz.sql.report.IReportWidgetContainer) widget, column2WidthMap);
		    } else if (widget instanceof org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportCell) {
		        org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportCell cell = (org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportCell) widget;
		        Types::Id columnId = cell.getAssociatedColumnId();
		        
		        if (columnId == null || column2WidthMap.containsKey(columnId)) {
		            continue;
		        }
		        
		        ReportsXsd:ReportColumnsList.Column.Width xWidth = ReportsXsd:ReportColumnsList.Column.Width.Factory.newInstance();
		        
		        xWidth.setMillimeters(new java.math.BigDecimal(cell.getWidthMm()));
		        xWidth.setColumns(cell.getWidthCols());
		        
		        column2WidthMap.put(columnId, xWidth);
		    }
		}
	}

	/*Radix::Reports::ReportsServerUtils:isContainerHasColumns-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportsServerUtils:isContainerHasColumns")
	private static  boolean isContainerHasColumns (org.radixware.kernel.common.defs.ads.clazz.sql.report.IReportWidgetContainer container) {
		for (org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportWidget widget : container.getWidgets()) {
		    if (widget instanceof org.radixware.kernel.common.defs.ads.clazz.sql.report.IReportWidgetContainer) {
		        return isContainerHasColumns((org.radixware.kernel.common.defs.ads.clazz.sql.report.IReportWidgetContainer) widget);
		    } else if (widget instanceof org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportCell) {
		        org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportCell cell = (org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportCell) widget;
		        Types::Id columnId = cell.getAssociatedColumnId();
		        
		        if (columnId != null) {
		            return true;
		        }
		    }
		}
		return false;
	}

	/*Radix::Reports::ReportsServerUtils:isAnyReportCellAssotiateToColumn-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportsServerUtils:isAnyReportCellAssotiateToColumn")
	public static  boolean isAnyReportCellAssotiateToColumn (org.radixware.ads.Types.server.Report report) {
		org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportForm form = report.createForm();

		for (org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportBand band : form.getColumnHeaderBands()) {
		    if (isContainerHasColumns(band)) {
		        return true;
		    }
		}

		for (org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportBand band : form.getDetailBands()) {
		    if (isContainerHasColumns(band)) {
		        return true;
		    }
		}

		for (org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportBand band : form.getSummaryBands()) {
		    if (isContainerHasColumns(band)) {
		        return true;
		    }
		}

		for (org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportGroup group : form.getGroups()) {
		    if (group.getFooterBand() != null) {
		        if (isContainerHasColumns(group.getFooterBand())) {
		            return true;
		        }
		    }
		}

		return false;
	}


}

/* Radix::Reports::ReportsServerUtils - Server Meta*/

/*Radix::Reports::ReportsServerUtils-Server Dynamic Class*/

package org.radixware.ads.Reports.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class ReportsServerUtils_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("adcD6JQG2D5NBDCZN3T6Q3OY5G56Q"),"ReportsServerUtils",null,

						org.radixware.kernel.common.enums.EClassType.DYNAMIC,
						null,
						null,
						null,

						/*Radix::Reports::ReportsServerUtils:Properties-Properties*/
						null,

						/*Radix::Reports::ReportsServerUtils:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthGKHEGU6ORFBI5HAH3DRQS3HMM4"),"instantiateReportByClassId",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("reportClassId",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprPHRDC4WVOFFPJOZCWAOOD3X3TU"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth4ATAR5KP3VDFTPER44OASZQKJI"),"loadParamFromXml",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("report",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprE2VPZZLJHJHX7DYS2NBDXMR7FI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("propId",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprKNUAMU5ZVBBMBEL7IC4OMU73WE")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("xProp",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprWJQCC3BDOJHYDFHQ5ZF7RXHAOI"))
								},org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth3UTNXYSAWFFV3FITOB5FA4BHKU"),"loadParamsFromXml",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("report",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprNGL7DFYJYFHP5BWMSNN5WYRKSU")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("xmlProps",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr24ZCM3B3JZA4HF7PQU2ZYC23LY"))
								},org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthUE47P4422JHNJBMWVV6AOE6AME"),"openFileOutResource",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("fileName",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprRSHNDPO4MJD7JPBC4GVPOH4P2M"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth6XXQ4DVKYFEJNJ4HYZJWOXGGJE"),"generateReport",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("context",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprMSAGNQBC6BA3HORTK5XPNFKFWU")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("input",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr36WEAJISWBB2BNR5QWIKH6O2AA"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthWN47TGMGLRHKRNLMOLEFZ77LII"),"getClassInfo",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("classId",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprMSQOPCYBZVA6LJNLETYQP2YYKY"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthQJFTJCZP7VCMRD6SH5MI75TW2M"),"getClassInfo",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("entity",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprACZWNKSPXJDZZIXR5PT36APXMA"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth3553PKEF2FDX3G7BLQFB45KSDQ"),"findParameterBindingById",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("parametersBindingDoc",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprXNQBUNPM6RE7ZACIFGBGIHI7SU")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("paramId",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprSIHCZKHHZNFS3A4YH2AXXDQ7BQ"))
								},org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth4TB5R4XGVBF5PIYTIACSU5YQJI"),"isFileExists",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("fileName",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprBQG5SQP3CZGU7OFPCZCNHJ2RAA"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthFBJSRXGSYBHWTC4DBLY2RPYC2U"),"showReplaceFileDialog",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("fileName",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprNHLFDLFNOZHDTP7UQX4TJKKPNE"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthCXDY5FUCL5CHNIIGKEBZSCA4LI"),"getColumnsWidthMap",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("report",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprIMEB5ATYWVAS3MO4RZ57ANXTUI"))
								},org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthXBLDD4K3GRE3TCL4FMSMV7XPCA"),"loadColumnsWidthFromContainer",true,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("container",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr2WSUGMD7ENHTTB5XA6BE2JSUZU")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("column2WidthMap",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprDU4DFBSUYNFPHOJIBWPFZTEUJA"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthMY5JRISCX5FALO7GSC3GJNWEEI"),"isContainerHasColumns",true,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("container",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprOMWEOPVQFVFA3DXD3W2NOW5PXY"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthOB7AS3YH5VEVLAXQVHROAJJSDM"),"isAnyReportCellAssotiateToColumn",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("report",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprHLUV52MGQFGIVJ4PV2S37A6DTU"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE)
						},
						null,
						null,null,null,false);
}

/* Radix::Reports::ReportsServerUtils - Localizing Bundle */
package org.radixware.ads.Reports.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class ReportsServerUtils - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"File \"{0}\" already exists. Do you want to replace it?");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Файл \"{0}\" уже существует. Вы хотите его заменить?");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls57U5E2ZHDRHNVIWZOBU4KBZ5G4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Unable to check the file for availability");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Невозможно проверить наличие файла");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDIAEKLUFSZCBNNQEKF5ZEGEHRQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Unable to open the file output resource");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Не удалось открыть ресурс для вывода файлов");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsI7P363TWCBC75FV723K7REAHCQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Unable to open replace file dialog");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Не удалось открыть диалог замены файлов");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsILAJDRBJ2NGMZKSQB7DUZV6JCM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Replace or Skip Files");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Заменить или пропустить файлы");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXBCVVDCYVVCEZP3KLROLXGWSXA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(ReportsServerUtils - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbadcD6JQG2D5NBDCZN3T6Q3OY5G56Q"),"ReportsServerUtils - Localizing Bundle",$$$items$$$);
}
