
/* Radix::Reports::ReportsExplorerUtils - Desktop Executable*/

/*Radix::Reports::ReportsExplorerUtils-Client-Common Dynamic Class*/

package org.radixware.ads.Reports.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportsExplorerUtils")
public published class ReportsExplorerUtils  {



	/*Radix::Reports::ReportsExplorerUtils:Nested classes-Nested Classes*/

	/*Radix::Reports::ReportsExplorerUtils:ReportTreeClassColumnDelegate-Desktop Dynamic Class*/


	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportsExplorerUtils:ReportTreeClassColumnDelegate")
	public static published class ReportTreeClassColumnDelegate  extends org.radixware.kernel.explorer.widgets.selector.WrapModelDelegate  {

		final Client.Env::ClientEnvironment environment;

		/*Radix::Reports::ReportsExplorerUtils:ReportTreeClassColumnDelegate:Nested classes-Nested Classes*/

		/*Radix::Reports::ReportsExplorerUtils:ReportTreeClassColumnDelegate:Properties-Properties*/

		/*Radix::Reports::ReportsExplorerUtils:ReportTreeClassColumnDelegate:Methods-Methods*/

		/*Radix::Reports::ReportsExplorerUtils:ReportTreeClassColumnDelegate:ReportTreeClassColumnDelegate-User Method*/

		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportsExplorerUtils:ReportTreeClassColumnDelegate:ReportTreeClassColumnDelegate")
		public  ReportTreeClassColumnDelegate (com.trolltech.qt.core.QObject parent, org.radixware.kernel.common.client.IClientEnvironment environment) {
			super(parent);
			this.environment = environment;
		}

		/*Radix::Reports::ReportsExplorerUtils:ReportTreeClassColumnDelegate:paint-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportsExplorerUtils:ReportTreeClassColumnDelegate:paint")
		public published  void paint (com.trolltech.qt.gui.QPainter painter, com.trolltech.qt.gui.QStyleOptionViewItem option, com.trolltech.qt.core.QModelIndex index) {
			super.paint(painter, option, index);

			Bool isUserReport = (Bool) index.data(com.trolltech.qt.core.Qt.ItemDataRole.UserRole);
			Types::Id iconId;
			if (isUserReport == null) {
			    iconId = idof[Explorer.Icons::cancel];
			} else {
			    iconId = isUserReport.booleanValue() ? idof[Explorer.Icons::uds_module] : idof[module];
			}

			com.trolltech.qt.gui.QPixmap icon = Explorer.Icons::ExplorerIcon.getQIcon(environment.getDefManager().getImage(iconId)).pixmap(16, 16);

			com.trolltech.qt.core.QPoint iconTopLeft = new com.trolltech.qt.core.QPoint(option.rect().right() - 5 - 16, option.rect().top());

			painter.save();
			painter.drawPixmap(iconTopLeft, icon);
			painter.restore();
		}

		/*Radix::Reports::ReportsExplorerUtils:ReportTreeClassColumnDelegate:drawDisplay-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportsExplorerUtils:ReportTreeClassColumnDelegate:drawDisplay")
		protected published  void drawDisplay (com.trolltech.qt.gui.QPainter painter, com.trolltech.qt.gui.QStyleOptionViewItem option, com.trolltech.qt.core.QRect rect, Str text) {
			com.trolltech.qt.core.QRect newRect = new com.trolltech.qt.core.QRect(rect.x(), rect.y(), rect.width() - 5 - 16 - 5, rect.height());
			super.drawDisplay(painter, option, newRect, text);
		}


	}

	/*Radix::Reports::ReportsExplorerUtils:Properties-Properties*/

	/*Radix::Reports::ReportsExplorerUtils:Methods-Methods*/

	/*Radix::Reports::ReportsExplorerUtils:findParameterBindingById-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportsExplorerUtils:findParameterBindingById")
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

	/*Radix::Reports::ReportsExplorerUtils:cfgReports-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportsExplorerUtils:cfgReports")
	public static published  void cfgReports (org.radixware.kernel.common.client.IClientEnvironment userSession, org.radixware.kernel.common.types.Id pubListDomainId, org.radixware.ads.Reports.common_client.IReportPubModel pubModel) {
		final CfgReportsDialog dialog = new CfgReportsDialog(userSession);
		dialog.Model.pubListDomainId = pubListDomainId;
		dialog.Model.pubModel = pubModel;
		dialog.exec();

	}

	/*Radix::Reports::ReportsExplorerUtils:buildPubTreeRqDocument-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportsExplorerUtils:buildPubTreeRqDocument")
	private static  org.radixware.schemas.reports.GetReportPubTreeRqDocument buildPubTreeRqDocument (org.radixware.kernel.common.client.IClientEnvironment env, org.radixware.ads.Reports.common_client.IReportPubModel reportPubModel, org.radixware.kernel.common.types.Id pubListClassId) {
		final ReportsXsd:GetReportPubTreeRqDocument doc = ReportsXsd:GetReportPubTreeRqDocument.Factory.newInstance();
		ReportsXsd:GetReportPubTreeRqDocument.GetReportPubTreeRq xRequest = doc.addNewGetReportPubTreeRq();
		xRequest.PubListClassId = pubListClassId;
		ReportsXsd:ReportPubContext xContext = xRequest.addNewContext();
		fillContext(env,reportPubModel, xContext);
		return doc;

	}

	/*Radix::Reports::ReportsExplorerUtils:generateReport-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportsExplorerUtils:generateReport")
	public static published  void generateReport (org.radixware.kernel.common.client.models.items.Command generateReportCmd, org.radixware.kernel.common.types.Id pubListClassId) {
		final IReportPubModel reportContext = (IReportPubModel) generateReportCmd.Owner;
		final ReportsXsd:GetReportPubTreeRqDocument getPubTreeRequestDoc = buildPubTreeRqDocument(generateReportCmd.Environment, reportContext, pubListClassId);
		final ReportsXsd:ReportPubTopic xRoot = requestPubTree(generateReportCmd.Environment, getPubTreeRequestDoc);
		if (xRoot == null) {
		    return;
		}

		final Explorer.Types::Pid reportPupPid = execChoosePubPidDialog(generateReportCmd.Environment, generateReportCmd, xRoot);
		if (reportPupPid == null) {
		    return;
		}

		try {
		    final ReportPub:General:Model reportPub = (ReportPub:General:Model) Explorer.Models::EntityModel.openContextlessModel(
		            generateReportCmd.Environment,
		            reportPupPid,
		            idof[ReportPub],
		            idof[ReportPub:General]);
		    reportPub.generateReport(generateReportCmd, null, null);
		} catch (Exceptions::InterruptedException ex) {
		    // NOTHING - cancelled
		} catch (Exceptions::ServiceClientException ex) {
		    generateReportCmd.Environment.processException(ex);
		}

	}

	/*Radix::Reports::ReportsExplorerUtils:requestPubTree-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportsExplorerUtils:requestPubTree")
	private static  org.radixware.schemas.reports.ReportPubTopic requestPubTree (org.radixware.kernel.common.client.IClientEnvironment userSession, org.radixware.schemas.reports.GetReportPubTreeRqDocument requestDoc) {
		try {
		    final ReportsXsd:GetReportPubTreeRpDocument replyDoc =
		            (ReportsXsd:GetReportPubTreeRpDocument) userSession.EasSession.executeContextlessCommand(
		            idof[GetReportPubTreeCmd],
		            requestDoc,
		            ReportsXsd:GetReportPubTreeRpDocument.class);
		    return replyDoc.GetReportPubTreeRp;
		} catch (Exceptions::InterruptedException ex) {
		    // nothing - cancel pressed
		    return null;
		} catch (Exceptions::ServiceClientException ex) {
		    userSession.processException(ex);
		    return null;
		}





	}

	/*Radix::Reports::ReportsExplorerUtils:fillReportsTree-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportsExplorerUtils:fillReportsTree")
	public static published  void fillReportsTree (org.radixware.ads.Reports.common_client.IReportPubTreeModel reportPubTreeModel, org.radixware.ads.Reports.common_client.IReportPubModel reportPubModel, org.radixware.kernel.common.types.Id pubListClassId) {
		final ReportsXsd:GetReportPubTreeRqDocument requestDoc = buildPubTreeRqDocument(reportPubTreeModel.Environment,reportPubModel, pubListClassId);
		final ReportsXsd:ReportPubTopic xRoot = requestPubTree(reportPubTreeModel.Environment, requestDoc); // can be null
		reportPubTreeModel.open(xRoot);

	}

	/*Radix::Reports::ReportsExplorerUtils:fillContext-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportsExplorerUtils:fillContext")
	private static  void fillContext (org.radixware.kernel.common.client.IClientEnvironment env, org.radixware.ads.Reports.common_client.IReportPubModel reportPubModel, org.radixware.schemas.reports.ReportPubContext xContext) {
		ReportsXsd:ReportPubContext xCurContext = xContext;

		StringBuilder inheritancePathForDebug = null;

		while (true) {
		    final String modelTitleForDebug = getReportPubModelTitleForDebug(reportPubModel);
		    if (inheritancePathForDebug != null)
		        inheritancePathForDebug.append(" => ");
		    else
		        inheritancePathForDebug = new StringBuilder();
		    inheritancePathForDebug.append(modelTitleForDebug);

		    xCurContext.PubContextClassId = reportPubModel.getReportContextClassId();
		    final String contextId = reportPubModel.getReportContextId();
		    if (contextId != null) {
		        xCurContext.PubContextId = contextId;
		    }
		    reportPubModel = reportPubModel.getReportParentContext();
		    if (reportPubModel != null) {
		        xCurContext = xCurContext.addNewParent();
		    } else {
		        break;
		    }
		}
		env.getTracer().put(Arte::EventSeverity:Debug, "Report publications inheritance path: " + inheritancePathForDebug, Arte::EventSource:ExplorerApp.getValue());

	}

	/*Radix::Reports::ReportsExplorerUtils:getReportPubModelTitleForDebug-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportsExplorerUtils:getReportPubModelTitleForDebug")
	private static  Str getReportPubModelTitleForDebug (org.radixware.ads.Reports.common_client.IReportPubModel reportPubModel) {
		if (reportPubModel instanceof Explorer.Models::EntityModel) {
		    final Explorer.Models::EntityModel entityModel = (Explorer.Models::EntityModel) reportPubModel;
		    return entityModel.getClassPresentationDef().getObjectTitle()
		            + " '" + entityModel.getTitle() + "'";
		} else if (reportPubModel instanceof Explorer.Models::Model) {
		    final Explorer.Models::Model model = (Explorer.Models::Model) reportPubModel;
		    return "'" + model.getTitle() + "'";
		} else if (reportPubModel instanceof CommonReportContext) {
		    return "'" + "Common Reports" + "'";
		} else {
		    return "#"+reportPubModel.getReportContextId();
		}

	}

	/*Radix::Reports::ReportsExplorerUtils:getClassInfo-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportsExplorerUtils:getClassInfo")
	public static published  Str getClassInfo (org.radixware.kernel.common.client.IClientEnvironment userSession, org.radixware.kernel.common.types.Id classId) {
		if (classId == null) {
		    return String.valueOf(classId);
		}

		try {
		    final Explorer.Meta::PresentationClassDef classDef = userSession.DefManager.getClassPresentationDef(classId);
		    final String name = classDef.Name;
		    return name + " (#" + String.valueOf(classId) + ")";
		} catch (Exceptions::Throwable ex) {
		    return "#" + String.valueOf(classId);
		}

	}

	/*Radix::Reports::ReportsExplorerUtils:execChoosePubPidDialog-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportsExplorerUtils:execChoosePubPidDialog")
	private static  org.radixware.kernel.common.client.types.Pid execChoosePubPidDialog (org.radixware.kernel.common.client.IClientEnvironment env, org.radixware.schemas.reports.ReportPubTopic requestDoc) {
		return ChooseReportPubDialog:Model.choose(env,requestDoc);
	}

	/*Radix::Reports::ReportsExplorerUtils:execChoosePubPidDialog-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportsExplorerUtils:execChoosePubPidDialog")
	private static  org.radixware.kernel.common.client.types.Pid execChoosePubPidDialog (org.radixware.kernel.common.client.IClientEnvironment env, org.radixware.kernel.common.client.models.items.Command generateReportCmd, org.radixware.schemas.reports.ReportPubTopic requestDoc) {
		return ChooseReportPubDialog:Model.choose(generateReportCmd, env,requestDoc);
	}

	/*Radix::Reports::ReportsExplorerUtils:printOrOpenFile-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportsExplorerUtils:printOrOpenFile")
	public static published  void printOrOpenFile (org.radixware.kernel.common.client.IClientEnvironment env, java.io.File file, boolean print, Str mimeType, boolean save) throws java.lang.Throwable {
		printOrOpenFile(env,file,print,mimeType,null,save);
	}

	/*Radix::Reports::ReportsExplorerUtils:chooseInheritedPid-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportsExplorerUtils:chooseInheritedPid")
	public static published  org.radixware.kernel.common.client.types.Pid chooseInheritedPid (org.radixware.kernel.common.client.IClientEnvironment userSession, org.radixware.ads.Reports.common_client.IReportPubModel reportPubModel, org.radixware.kernel.common.types.Id pubListClassId) {
		final IReportPubModel parentContext = reportPubModel.getReportParentContext();
		if (parentContext != null) {
		    final ReportsXsd:GetReportPubTreeRqDocument requestDoc = buildPubTreeRqDocument(userSession,parentContext, pubListClassId);
		    requestDoc.GetReportPubTreeRq.ForInheritance = true;
		    
		    final ReportsXsd:ReportPubTopic xRoot = requestPubTree(userSession, requestDoc);
		    if (xRoot != null) {
		        return execChoosePubPidDialog(userSession,xRoot);
		    } else {
		        return null;
		    }
		} else {
		    userSession.messageError("Parent context is absent");
		    return null;
		}


	}

	/*Radix::Reports::ReportsExplorerUtils:createSqmlExpression-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportsExplorerUtils:createSqmlExpression")
	public static  org.radixware.kernel.common.scml.SqmlExpression createSqmlExpression (org.radixware.kernel.common.types.Id tableId, org.radixware.kernel.common.types.Id propId, Str value) {
		if (value != null) {
		    return Explorer.Utils::SqmlExpression.equal(
		            Explorer.Utils::SqmlExpression.this_property(tableId, propId),
		            Explorer.Utils::SqmlExpression.valueStr(value));
		} else {
		    return Explorer.Utils::SqmlExpression.is_null(
		            Explorer.Utils::SqmlExpression.this_property(tableId, propId));
		}

	}

	/*Radix::Reports::ReportsExplorerUtils:calcPubModelTitle-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportsExplorerUtils:calcPubModelTitle")
	public static  Str calcPubModelTitle (org.radixware.ads.Reports.common_client.IReportPubModel pubModel) {
		if (pubModel==null){
		    return "Context-free";
		}
		if (pubModel instanceof Explorer.Models::EntityModel){
		    final Explorer.Models::EntityModel entityModel = (Explorer.Models::EntityModel)pubModel;
		    final Str definitionTitle = 
		        org.radixware.kernel.common.client.utils.ClientValueFormatter.capitalizeIfNecessary(entityModel.getEnvironment(), entityModel.getDefinition().getTitle());    
		    if (entityModel.isNew()){
		        return definitionTitle;
		    }else{
		        final Str entityTitle =
		            org.radixware.kernel.common.client.utils.ClientValueFormatter.capitalizeIfNecessary(entityModel.getEnvironment(), entityModel.getTitle());
		        return Str.format("%1$s \'%2$s\'",definitionTitle,entityTitle);
		    }
		}else if (pubModel instanceof Explorer.Models::GroupModel){
		    return ((Explorer.Models::GroupModel)pubModel).getSelectorPresentationDef().getTitle();
		}else if (pubModel instanceof Explorer.Models::Model){
		    return ((Explorer.Models::Model)pubModel).getTitle();
		}
		return null;
	}

	/*Radix::Reports::ReportsExplorerUtils:printOrOpenFile-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportsExplorerUtils:printOrOpenFile")
	public static published  void printOrOpenFile (org.radixware.kernel.common.client.IClientEnvironment env, java.io.File file, boolean print, Str mimeType, Str suggestedFileName, boolean save) throws java.lang.Throwable {
		if (print) {
		    java.awt.Desktop.getDesktop().print(file);
		} else {
		    java.awt.Desktop.getDesktop().open(file);
		}
	}

	/*Radix::Reports::ReportsExplorerUtils:requestColumnsList-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportsExplorerUtils:requestColumnsList")
	public static  org.radixware.schemas.reports.ReportColumnsList requestColumnsList (org.radixware.kernel.common.client.IClientEnvironment environment, org.radixware.kernel.common.types.Id reportId) {
		Arte::TypesXsd:IdDocument requestDoc = Arte::TypesXsd:IdDocument.Factory.newInstance();
		requestDoc.setId(reportId);

		try {
		    final ReportsXsd:ReportColumnsListDocument response =
		            (ReportsXsd:ReportColumnsListDocument) environment.EasSession.executeContextlessCommand(
		            idof[ReportColumnsListCmd],
		            requestDoc,
		            ReportsXsd:ReportColumnsListDocument.class);
		    return response == null ? null : response.ReportColumnsList;
		} catch (Exceptions::InterruptedException ex) {
		    // nothing - cancel pressed
		    return null;
		} catch (Exceptions::ServiceClientException ex) {
		    environment.processException(ex);
		    return null;
		}
	}

	/*Radix::Reports::ReportsExplorerUtils:execReprtPubsCheckCommand-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportsExplorerUtils:execReprtPubsCheckCommand")
	private static  org.radixware.ads.Reports.common.CommandsXsd.ReportPubCheckProblems execReprtPubsCheckCommand (org.radixware.kernel.common.client.IClientEnvironment environment) {
		try {
		    final CommandsXsd:ReportPubCheckProblemsDocument response =
		            (CommandsXsd:ReportPubCheckProblemsDocument) environment.EasSession.executeContextlessCommand(
		            idof[CheckAllReportPubsCmd],
		            null,
		            CommandsXsd:ReportPubCheckProblemsDocument.class);
		    return response == null ? null : response.ReportPubCheckProblems;    
		} catch (Exceptions::InterruptedException ex) {
		    // nothing - cancel pressed
		    return null;
		} catch (Exceptions::ServiceClientException ex) {
		    environment.processException(ex);
		    return null;
		}
	}

	/*Radix::Reports::ReportsExplorerUtils:getReportCheckProblemsHtml-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportsExplorerUtils:getReportCheckProblemsHtml")
	public static published  Str getReportCheckProblemsHtml (org.radixware.kernel.common.client.IClientEnvironment environment) {
		CommandsXsd:ReportPubCheckProblems problems = ReportsExplorerUtils.execReprtPubsCheckCommand(environment);
		int warnings = 0;
		int errors = 0;

		Utils::HtmlTable problemsTable = new HtmlTable();

		if (problems != null && !problems.EntryList.isEmpty()) {    
		    problemsTable.setAttr("border", 1);
		    problemsTable.setAttr("bordercolor", "GREY");
		    problemsTable.setAttr("cellpadding", 4);
		    
		    org.radixware.kernel.common.html.Table.Header header = problemsTable.getHeader();    
		    header.addCell().setInnerText("Report Publication");
		    header.addCell().setInnerText("Publication Context");
		    header.addCell().setInnerText("Problem");

		    for (CommandsXsd:ReportPubCheckProblemsEntry xEntry : problems.EntryList) {
		        if (xEntry.ProblemList.isEmpty()) {
		            continue;
		        }
		                
		        Utils::HtmlTable.Row row = problemsTable.addRow();        
		        row.addCell().setInnerText(xEntry.ReportPub);
		        row.addCell().setInnerText(xEntry.ReportPubContext);

		        Utils::HtmlTable.DataCell problemCell = row.addCell();
		        Utils::HtmlTable warningsTable = new HtmlTable();
		        Utils::HtmlTable errorsTable = new HtmlTable();        

		        for (CommandsXsd:ReportPubCheckProblem xProblem : xEntry.getProblemList()) {
		            if (xProblem.ProblemSeverity == Arte::EventSeverity:Warning) {
		                warningsTable.addRow().addCell().setInnerText("- " + xProblem.ProblemText);                
		            } else {
		                errorsTable.addRow().addCell().setInnerText("- " + xProblem.ProblemText);                
		            }
		        }
		        
		        Utils::Html font;
		        if (warningsTable.rowCount() > 0) {
		            warnings += warningsTable.rowCount();
		            
		            font = new Html("font");
		            font.setAttr("color", "ORANGE");
		            font.setInnerText("Warnings:");        

		            problemCell.add(font);  
		            problemCell.add(warningsTable);
		        }
		        
		        if (errorsTable.rowCount() > 0) {
		            errors += errorsTable.rowCount();
		            
		            font = new Html("font");
		            font.setAttr("color", "RED");
		            font.setInnerText("Errors:");

		            problemCell.add(font);
		            problemCell.add(errorsTable);
		        }
		    }        
		}

		DateTime checkDate = new DateTime(java.lang.System.currentTimeMillis());
		Explorer.EditMask::EditMaskDateTime editMask = new EditMaskDateTime();
		editMask.setTimeStyle(Meta::DateTimeStyle:DEFAULT);
		editMask.setDateStyle(Meta::DateTimeStyle:DEFAULT);

		String checkDateStr = editMask.toStr(environment, checkDate);

		Utils::HtmlTable statisticTable = new HtmlTable();
		statisticTable.setAttr("border", 1);
		statisticTable.setAttr("bordercolor", "GREY");
		statisticTable.setAttr("cellpadding", 4);

		Utils::HtmlTable.Row row = statisticTable.addRow();
		row.addCell().setInnerText("Date");
		row.addCell().setInnerText(checkDateStr);

		row = statisticTable.addRow();
		row.addCell().setInnerText("Operation");
		row.addCell().setInnerText("Report publications check");

		row = statisticTable.addRow();
		row.addCell().setInnerText("Warnings received");
		row.addCell().setInnerText(Str.valueOf(warnings));

		row = statisticTable.addRow();
		row.addCell().setInnerText("Errors received");
		row.addCell().setInnerText(Str.valueOf(errors));

		Utils::Html h4 = new Html("h4");
		h4.setInnerText("Statistics:");

		Utils::Html result = new Html("span");
		result.add(h4);
		result.add(statisticTable);

		if (problemsTable.rowCount() > 0) {
		    h4 = new Html("h4");
		    h4.setInnerText("Details:");
		    
		    result.add(h4);
		    result.add(problemsTable);
		}

		return result.toString();
	}

	/*Radix::Reports::ReportsExplorerUtils:isAnyReportCellAssotiateToColumn-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportsExplorerUtils:isAnyReportCellAssotiateToColumn")
	public static published  boolean isAnyReportCellAssotiateToColumn (org.radixware.kernel.common.client.IClientEnvironment environment, org.radixware.kernel.common.types.Id reportId) {

		final Arte::TypesXsd:IdDocument requestDoc = Arte::TypesXsd:IdDocument.Factory.newInstance();
		requestDoc.setId(reportId);

		try {
		    final Arte::TypesXsd:BoolDocument response =
		            (Arte::TypesXsd:BoolDocument) environment.EasSession.executeContextlessCommand(
		            idof[IsAnyReportCellAssotiateToColumnCmd],
		            requestDoc,
		            Arte::TypesXsd:BoolDocument.class);    
		    return response == null ? false : response.getBool().booleanValue();
		} catch (Exceptions::InterruptedException ex) {
		    // nothing - cancel pressed
		    return false;
		} catch (Exceptions::ServiceClientException ex) {
		    environment.processException(ex);
		    return false;
		}

	}


}

/* Radix::Reports::ReportsExplorerUtils - Desktop Meta*/

/*Radix::Reports::ReportsExplorerUtils-Client-Common Dynamic Class*/

package org.radixware.ads.Reports.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class ReportsExplorerUtils_mi{
}

/* Radix::Reports::ReportsExplorerUtils - Web Executable*/

/*Radix::Reports::ReportsExplorerUtils-Client-Common Dynamic Class*/

package org.radixware.ads.Reports.web;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportsExplorerUtils")
public published class ReportsExplorerUtils  {



	/*Radix::Reports::ReportsExplorerUtils:Nested classes-Nested Classes*/

	/*Radix::Reports::ReportsExplorerUtils:ReportTreeClassColumnRendererProvider-Web Dynamic Class*/


	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportsExplorerUtils:ReportTreeClassColumnRendererProvider")
	public static published class ReportTreeClassColumnRendererProvider  implements org.radixware.wps.rwt.tree.Tree.ICellRendererProvider  {

		final Client.Env::ClientEnvironment environment;
		final Bool isUserReport;

		/*Radix::Reports::ReportsExplorerUtils:ReportTreeClassColumnRendererProvider:Nested classes-Nested Classes*/

		/*Radix::Reports::ReportsExplorerUtils:ReportTreeClassColumnRendererProvider:ReportTreeClassColumnRenderer-Web Dynamic Class*/


		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportsExplorerUtils:ReportTreeClassColumnRendererProvider:ReportTreeClassColumnRenderer")
		private static class ReportTreeClassColumnRenderer  extends org.radixware.wps.rwt.tree.Node.DefaultTreeCellRenderer  {

			final Client.Env::ClientEnvironment environment;

			/*Radix::Reports::ReportsExplorerUtils:ReportTreeClassColumnRendererProvider:ReportTreeClassColumnRenderer:Nested classes-Nested Classes*/

			/*Radix::Reports::ReportsExplorerUtils:ReportTreeClassColumnRendererProvider:ReportTreeClassColumnRenderer:Properties-Properties*/

			/*Radix::Reports::ReportsExplorerUtils:ReportTreeClassColumnRendererProvider:ReportTreeClassColumnRenderer:Methods-Methods*/

			/*Radix::Reports::ReportsExplorerUtils:ReportTreeClassColumnRendererProvider:ReportTreeClassColumnRenderer:ReportTreeClassColumnRenderer-User Method*/

			@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportsExplorerUtils:ReportTreeClassColumnRendererProvider:ReportTreeClassColumnRenderer:ReportTreeClassColumnRenderer")
			public  ReportTreeClassColumnRenderer (org.radixware.kernel.common.client.IClientEnvironment environment, Bool isUserReport) {
				super();
				this.environment = environment;

				html.clear();

				Types::Id iconId;
				if (isUserReport == null) {
				    iconId = idof[Explorer.Icons::cancel];
				} else {
				    iconId = isUserReport.booleanValue() ? idof[Explorer.Icons::uds_module] : idof[module];
				}

				Utils::Html icon = createIcon(this, iconId, 16);
				icon.setCss("display", "inline-block");
				label.setCss("display", "inline-block");
				label.setCss("vertical-align", "middle");

				html.add(label);
				html.add(icon);
			}

			/*Radix::Reports::ReportsExplorerUtils:ReportTreeClassColumnRendererProvider:ReportTreeClassColumnRenderer:createIcon-User Method*/

			@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportsExplorerUtils:ReportTreeClassColumnRendererProvider:ReportTreeClassColumnRenderer:createIcon")
			private final  org.radixware.kernel.common.html.Html createIcon (org.radixware.wps.rwt.UIObject obj, org.radixware.kernel.common.types.Id imgId, Int size) {
				Utils::Html icon = new Html("img");
				icon.setAttr("src", ((org.radixware.wps.icons.WpsIcon) environment.getDefManager().getImage(imgId)).getURI(obj));
				icon.setCss("width", size + "px");
				icon.setCss("height", size + "px");

				Utils::Html div = new Html("div");
				div.setCss("display", "blocl");
				div.setCss("width", size + "px");
				div.setCss("height", size + "px");
				div.setCss("vertical-align", "middle");
				div.add(icon);

				return div;
			}


		}

		/*Radix::Reports::ReportsExplorerUtils:ReportTreeClassColumnRendererProvider:Properties-Properties*/

		/*Radix::Reports::ReportsExplorerUtils:ReportTreeClassColumnRendererProvider:Methods-Methods*/

		/*Radix::Reports::ReportsExplorerUtils:ReportTreeClassColumnRendererProvider:ReportTreeClassColumnRendererProvider-User Method*/

		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportsExplorerUtils:ReportTreeClassColumnRendererProvider:ReportTreeClassColumnRendererProvider")
		public  ReportTreeClassColumnRendererProvider (org.radixware.kernel.common.client.IClientEnvironment environment, Bool isUserReport) {
			this.environment = environment;
			this.isUserReport = isUserReport;
		}

		/*Radix::Reports::ReportsExplorerUtils:ReportTreeClassColumnRendererProvider:newCellRenderer-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportsExplorerUtils:ReportTreeClassColumnRendererProvider:newCellRenderer")
		public published  org.radixware.wps.rwt.tree.Node.INodeCellRenderer newCellRenderer (org.radixware.wps.rwt.tree.Node node, int column) {
			return new ReportTreeClassColumnRenderer(environment, isUserReport);
		}


	}

	/*Radix::Reports::ReportsExplorerUtils:Properties-Properties*/

	/*Radix::Reports::ReportsExplorerUtils:Methods-Methods*/

	/*Radix::Reports::ReportsExplorerUtils:findParameterBindingById-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportsExplorerUtils:findParameterBindingById")
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

	/*Radix::Reports::ReportsExplorerUtils:cfgReports-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportsExplorerUtils:cfgReports")
	public static published  void cfgReports (org.radixware.kernel.common.client.IClientEnvironment userSession, org.radixware.kernel.common.types.Id pubListDomainId, org.radixware.ads.Reports.common_client.IReportPubModel pubModel) {
		final CfgReportsDialogWeb dialog = new CfgReportsDialogWeb(userSession);
		dialog.Model.pubListDomainId = pubListDomainId;
		dialog.Model.pubModel = pubModel;
		dialog.execDialog();
	}

	/*Radix::Reports::ReportsExplorerUtils:buildPubTreeRqDocument-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportsExplorerUtils:buildPubTreeRqDocument")
	private static  org.radixware.schemas.reports.GetReportPubTreeRqDocument buildPubTreeRqDocument (org.radixware.kernel.common.client.IClientEnvironment env, org.radixware.ads.Reports.common_client.IReportPubModel reportPubModel, org.radixware.kernel.common.types.Id pubListClassId) {
		final ReportsXsd:GetReportPubTreeRqDocument doc = ReportsXsd:GetReportPubTreeRqDocument.Factory.newInstance();
		ReportsXsd:GetReportPubTreeRqDocument.GetReportPubTreeRq xRequest = doc.addNewGetReportPubTreeRq();
		xRequest.PubListClassId = pubListClassId;
		ReportsXsd:ReportPubContext xContext = xRequest.addNewContext();
		fillContext(env,reportPubModel, xContext);
		return doc;

	}

	/*Radix::Reports::ReportsExplorerUtils:generateReport-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportsExplorerUtils:generateReport")
	public static published  void generateReport (org.radixware.kernel.common.client.models.items.Command generateReportCmd, org.radixware.kernel.common.types.Id pubListClassId) {
		final IReportPubModel reportContext = (IReportPubModel) generateReportCmd.Owner;
		final ReportsXsd:GetReportPubTreeRqDocument getPubTreeRequestDoc = buildPubTreeRqDocument(generateReportCmd.Environment, reportContext, pubListClassId);
		final ReportsXsd:ReportPubTopic xRoot = requestPubTree(generateReportCmd.Environment, getPubTreeRequestDoc);
		if (xRoot == null) {
		    return;
		}

		final Explorer.Types::Pid reportPupPid = execChoosePubPidDialog(generateReportCmd.Environment, generateReportCmd, xRoot);
		if (reportPupPid == null) {
		    return;
		}

		try {
		    final ReportPub:General:Model reportPub = (ReportPub:General:Model) Explorer.Models::EntityModel.openContextlessModel(
		            generateReportCmd.Environment,
		            reportPupPid,
		            idof[ReportPub],
		            idof[ReportPub:General]);
		    reportPub.generateReport(generateReportCmd, null, null);
		} catch (Exceptions::InterruptedException ex) {
		    // NOTHING - cancelled
		} catch (Exceptions::ServiceClientException ex) {
		    generateReportCmd.Environment.processException(ex);
		}

	}

	/*Radix::Reports::ReportsExplorerUtils:requestPubTree-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportsExplorerUtils:requestPubTree")
	private static  org.radixware.schemas.reports.ReportPubTopic requestPubTree (org.radixware.kernel.common.client.IClientEnvironment userSession, org.radixware.schemas.reports.GetReportPubTreeRqDocument requestDoc) {
		try {
		    final ReportsXsd:GetReportPubTreeRpDocument replyDoc =
		            (ReportsXsd:GetReportPubTreeRpDocument) userSession.EasSession.executeContextlessCommand(
		            idof[GetReportPubTreeCmd],
		            requestDoc,
		            ReportsXsd:GetReportPubTreeRpDocument.class);
		    return replyDoc.GetReportPubTreeRp;
		} catch (Exceptions::InterruptedException ex) {
		    // nothing - cancel pressed
		    return null;
		} catch (Exceptions::ServiceClientException ex) {
		    userSession.processException(ex);
		    return null;
		}





	}

	/*Radix::Reports::ReportsExplorerUtils:fillReportsTree-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportsExplorerUtils:fillReportsTree")
	public static published  void fillReportsTree (org.radixware.ads.Reports.common_client.IReportPubTreeModel reportPubTreeModel, org.radixware.ads.Reports.common_client.IReportPubModel reportPubModel, org.radixware.kernel.common.types.Id pubListClassId) {
		final ReportsXsd:GetReportPubTreeRqDocument requestDoc = buildPubTreeRqDocument(reportPubTreeModel.Environment,reportPubModel, pubListClassId);
		final ReportsXsd:ReportPubTopic xRoot = requestPubTree(reportPubTreeModel.Environment, requestDoc); // can be null
		reportPubTreeModel.open(xRoot);

	}

	/*Radix::Reports::ReportsExplorerUtils:fillContext-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportsExplorerUtils:fillContext")
	private static  void fillContext (org.radixware.kernel.common.client.IClientEnvironment env, org.radixware.ads.Reports.common_client.IReportPubModel reportPubModel, org.radixware.schemas.reports.ReportPubContext xContext) {
		ReportsXsd:ReportPubContext xCurContext = xContext;

		StringBuilder inheritancePathForDebug = null;

		while (true) {
		    final String modelTitleForDebug = getReportPubModelTitleForDebug(reportPubModel);
		    if (inheritancePathForDebug != null)
		        inheritancePathForDebug.append(" => ");
		    else
		        inheritancePathForDebug = new StringBuilder();
		    inheritancePathForDebug.append(modelTitleForDebug);

		    xCurContext.PubContextClassId = reportPubModel.getReportContextClassId();
		    final String contextId = reportPubModel.getReportContextId();
		    if (contextId != null) {
		        xCurContext.PubContextId = contextId;
		    }
		    reportPubModel = reportPubModel.getReportParentContext();
		    if (reportPubModel != null) {
		        xCurContext = xCurContext.addNewParent();
		    } else {
		        break;
		    }
		}
		env.getTracer().put(Arte::EventSeverity:Debug, "Report publications inheritance path: " + inheritancePathForDebug, Arte::EventSource:ExplorerApp.getValue());

	}

	/*Radix::Reports::ReportsExplorerUtils:getReportPubModelTitleForDebug-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportsExplorerUtils:getReportPubModelTitleForDebug")
	private static  Str getReportPubModelTitleForDebug (org.radixware.ads.Reports.common_client.IReportPubModel reportPubModel) {
		if (reportPubModel instanceof Explorer.Models::EntityModel) {
		    final Explorer.Models::EntityModel entityModel = (Explorer.Models::EntityModel) reportPubModel;
		    return entityModel.getClassPresentationDef().getObjectTitle()
		            + " '" + entityModel.getTitle() + "'";
		} else if (reportPubModel instanceof Explorer.Models::Model) {
		    final Explorer.Models::Model model = (Explorer.Models::Model) reportPubModel;
		    return "'" + model.getTitle() + "'";
		} else if (reportPubModel instanceof CommonReportContext) {
		    return "'" + "Common Reports" + "'";
		} else {
		    return "#"+reportPubModel.getReportContextId();
		}

	}

	/*Radix::Reports::ReportsExplorerUtils:getClassInfo-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportsExplorerUtils:getClassInfo")
	public static published  Str getClassInfo (org.radixware.kernel.common.client.IClientEnvironment userSession, org.radixware.kernel.common.types.Id classId) {
		if (classId == null) {
		    return String.valueOf(classId);
		}

		try {
		    final Explorer.Meta::PresentationClassDef classDef = userSession.DefManager.getClassPresentationDef(classId);
		    final String name = classDef.Name;
		    return name + " (#" + String.valueOf(classId) + ")";
		} catch (Exceptions::Throwable ex) {
		    return "#" + String.valueOf(classId);
		}

	}

	/*Radix::Reports::ReportsExplorerUtils:execChoosePubPidDialog-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportsExplorerUtils:execChoosePubPidDialog")
	private static  org.radixware.kernel.common.client.types.Pid execChoosePubPidDialog (org.radixware.kernel.common.client.IClientEnvironment env, org.radixware.schemas.reports.ReportPubTopic requestDoc) {
		return ChooseReportPubDialogWeb:Model.choose(env,requestDoc);
	}

	/*Radix::Reports::ReportsExplorerUtils:execChoosePubPidDialog-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportsExplorerUtils:execChoosePubPidDialog")
	private static  org.radixware.kernel.common.client.types.Pid execChoosePubPidDialog (org.radixware.kernel.common.client.IClientEnvironment env, org.radixware.kernel.common.client.models.items.Command generateReportCmd, org.radixware.schemas.reports.ReportPubTopic requestDoc) {
		return ChooseReportPubDialogWeb:Model.choose(generateReportCmd,env,requestDoc);
	}

	/*Radix::Reports::ReportsExplorerUtils:printOrOpenFile-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportsExplorerUtils:printOrOpenFile")
	public static published  void printOrOpenFile (org.radixware.kernel.common.client.IClientEnvironment env, java.io.File file, boolean print, Str mimeType, boolean save) throws java.lang.Throwable {
		printOrOpenFile(env,file,print,mimeType,null,save);
	}

	/*Radix::Reports::ReportsExplorerUtils:chooseInheritedPid-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportsExplorerUtils:chooseInheritedPid")
	public static published  org.radixware.kernel.common.client.types.Pid chooseInheritedPid (org.radixware.kernel.common.client.IClientEnvironment userSession, org.radixware.ads.Reports.common_client.IReportPubModel reportPubModel, org.radixware.kernel.common.types.Id pubListClassId) {
		final IReportPubModel parentContext = reportPubModel.getReportParentContext();
		if (parentContext != null) {
		    final ReportsXsd:GetReportPubTreeRqDocument requestDoc = buildPubTreeRqDocument(userSession,parentContext, pubListClassId);
		    requestDoc.GetReportPubTreeRq.ForInheritance = true;
		    
		    final ReportsXsd:ReportPubTopic xRoot = requestPubTree(userSession, requestDoc);
		    if (xRoot != null) {
		        return execChoosePubPidDialog(userSession,xRoot);
		    } else {
		        return null;
		    }
		} else {
		    userSession.messageError("Parent context is absent");
		    return null;
		}


	}

	/*Radix::Reports::ReportsExplorerUtils:createSqmlExpression-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportsExplorerUtils:createSqmlExpression")
	public static  org.radixware.kernel.common.scml.SqmlExpression createSqmlExpression (org.radixware.kernel.common.types.Id tableId, org.radixware.kernel.common.types.Id propId, Str value) {
		if (value != null) {
		    return Explorer.Utils::SqmlExpression.equal(
		            Explorer.Utils::SqmlExpression.this_property(tableId, propId),
		            Explorer.Utils::SqmlExpression.valueStr(value));
		} else {
		    return Explorer.Utils::SqmlExpression.is_null(
		            Explorer.Utils::SqmlExpression.this_property(tableId, propId));
		}

	}

	/*Radix::Reports::ReportsExplorerUtils:calcPubModelTitle-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportsExplorerUtils:calcPubModelTitle")
	public static  Str calcPubModelTitle (org.radixware.ads.Reports.common_client.IReportPubModel pubModel) {
		if (pubModel==null){
		    return "Context-free";
		}
		if (pubModel instanceof Explorer.Models::EntityModel){
		    final Explorer.Models::EntityModel entityModel = (Explorer.Models::EntityModel)pubModel;
		    final Str definitionTitle = 
		        org.radixware.kernel.common.client.utils.ClientValueFormatter.capitalizeIfNecessary(entityModel.getEnvironment(), entityModel.getDefinition().getTitle());    
		    if (entityModel.isNew()){
		        return definitionTitle;
		    }else{
		        final Str entityTitle =
		            org.radixware.kernel.common.client.utils.ClientValueFormatter.capitalizeIfNecessary(entityModel.getEnvironment(), entityModel.getTitle());
		        return Str.format("%1$s \'%2$s\'",definitionTitle,entityTitle);
		    }
		}else if (pubModel instanceof Explorer.Models::GroupModel){
		    return ((Explorer.Models::GroupModel)pubModel).getSelectorPresentationDef().getTitle();
		}else if (pubModel instanceof Explorer.Models::Model){
		    return ((Explorer.Models::Model)pubModel).getTitle();
		}
		return null;
	}

	/*Radix::Reports::ReportsExplorerUtils:printOrOpenFile-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportsExplorerUtils:printOrOpenFile")
	public static published  void printOrOpenFile (org.radixware.kernel.common.client.IClientEnvironment env, java.io.File file, boolean print, Str mimeType, Str suggestedFileName, boolean save) throws java.lang.Throwable {
		if (file.isDirectory()) {
		    java.io.File tmpzip = java.io.File.createTempFile("report", ".zip");
		    org.radixware.kernel.common.utils.JarFiles.mkJar(file, tmpzip, new java.io.FileFilter() {
		        public boolean accept(java.io.File file) {
		            return true;
		        }
		    }, false);
		    org.radixware.kernel.common.utils.FileUtils.deleteDirectory(file);
		    file = tmpzip;
		    mimeType = Common::MimeType:Zip.Value;
		}
		final String fileName;
		if (suggestedFileName==null || suggestedFileName.isEmpty()){
		    fileName = "Save Report";
		}else{    
		    fileName = suggestedFileName;
		}
		((org.radixware.wps.WpsEnvironment) env).sendFileToTerminal(fileName, file, mimeType, !save);
	}

	/*Radix::Reports::ReportsExplorerUtils:requestColumnsList-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportsExplorerUtils:requestColumnsList")
	public static  org.radixware.schemas.reports.ReportColumnsList requestColumnsList (org.radixware.kernel.common.client.IClientEnvironment environment, org.radixware.kernel.common.types.Id reportId) {
		Arte::TypesXsd:IdDocument requestDoc = Arte::TypesXsd:IdDocument.Factory.newInstance();
		requestDoc.setId(reportId);

		try {
		    final ReportsXsd:ReportColumnsListDocument response =
		            (ReportsXsd:ReportColumnsListDocument) environment.EasSession.executeContextlessCommand(
		            idof[ReportColumnsListCmd],
		            requestDoc,
		            ReportsXsd:ReportColumnsListDocument.class);
		    return response == null ? null : response.ReportColumnsList;
		} catch (Exceptions::InterruptedException ex) {
		    // nothing - cancel pressed
		    return null;
		} catch (Exceptions::ServiceClientException ex) {
		    environment.processException(ex);
		    return null;
		}
	}

	/*Radix::Reports::ReportsExplorerUtils:execReprtPubsCheckCommand-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportsExplorerUtils:execReprtPubsCheckCommand")
	private static  org.radixware.ads.Reports.common.CommandsXsd.ReportPubCheckProblems execReprtPubsCheckCommand (org.radixware.kernel.common.client.IClientEnvironment environment) {
		try {
		    final CommandsXsd:ReportPubCheckProblemsDocument response =
		            (CommandsXsd:ReportPubCheckProblemsDocument) environment.EasSession.executeContextlessCommand(
		            idof[CheckAllReportPubsCmd],
		            null,
		            CommandsXsd:ReportPubCheckProblemsDocument.class);
		    return response == null ? null : response.ReportPubCheckProblems;    
		} catch (Exceptions::InterruptedException ex) {
		    // nothing - cancel pressed
		    return null;
		} catch (Exceptions::ServiceClientException ex) {
		    environment.processException(ex);
		    return null;
		}
	}

	/*Radix::Reports::ReportsExplorerUtils:getReportCheckProblemsHtml-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportsExplorerUtils:getReportCheckProblemsHtml")
	public static published  Str getReportCheckProblemsHtml (org.radixware.kernel.common.client.IClientEnvironment environment) {
		CommandsXsd:ReportPubCheckProblems problems = ReportsExplorerUtils.execReprtPubsCheckCommand(environment);
		int warnings = 0;
		int errors = 0;

		Utils::HtmlTable problemsTable = new HtmlTable();

		if (problems != null && !problems.EntryList.isEmpty()) {    
		    problemsTable.setAttr("border", 1);
		    problemsTable.setAttr("bordercolor", "GREY");
		    problemsTable.setAttr("cellpadding", 4);
		    
		    org.radixware.kernel.common.html.Table.Header header = problemsTable.getHeader();    
		    header.addCell().setInnerText("Report Publication");
		    header.addCell().setInnerText("Publication Context");
		    header.addCell().setInnerText("Problem");

		    for (CommandsXsd:ReportPubCheckProblemsEntry xEntry : problems.EntryList) {
		        if (xEntry.ProblemList.isEmpty()) {
		            continue;
		        }
		                
		        Utils::HtmlTable.Row row = problemsTable.addRow();        
		        row.addCell().setInnerText(xEntry.ReportPub);
		        row.addCell().setInnerText(xEntry.ReportPubContext);

		        Utils::HtmlTable.DataCell problemCell = row.addCell();
		        Utils::HtmlTable warningsTable = new HtmlTable();
		        Utils::HtmlTable errorsTable = new HtmlTable();        

		        for (CommandsXsd:ReportPubCheckProblem xProblem : xEntry.getProblemList()) {
		            if (xProblem.ProblemSeverity == Arte::EventSeverity:Warning) {
		                warningsTable.addRow().addCell().setInnerText("- " + xProblem.ProblemText);                
		            } else {
		                errorsTable.addRow().addCell().setInnerText("- " + xProblem.ProblemText);                
		            }
		        }
		        
		        Utils::Html font;
		        if (warningsTable.rowCount() > 0) {
		            warnings += warningsTable.rowCount();
		            
		            font = new Html("font");
		            font.setAttr("color", "ORANGE");
		            font.setInnerText("Warnings:");        

		            problemCell.add(font);  
		            problemCell.add(warningsTable);
		        }
		        
		        if (errorsTable.rowCount() > 0) {
		            errors += errorsTable.rowCount();
		            
		            font = new Html("font");
		            font.setAttr("color", "RED");
		            font.setInnerText("Errors:");

		            problemCell.add(font);
		            problemCell.add(errorsTable);
		        }
		    }        
		}

		DateTime checkDate = new DateTime(java.lang.System.currentTimeMillis());
		Explorer.EditMask::EditMaskDateTime editMask = new EditMaskDateTime();
		editMask.setTimeStyle(Meta::DateTimeStyle:DEFAULT);
		editMask.setDateStyle(Meta::DateTimeStyle:DEFAULT);

		String checkDateStr = editMask.toStr(environment, checkDate);

		Utils::HtmlTable statisticTable = new HtmlTable();
		statisticTable.setAttr("border", 1);
		statisticTable.setAttr("bordercolor", "GREY");
		statisticTable.setAttr("cellpadding", 4);

		Utils::HtmlTable.Row row = statisticTable.addRow();
		row.addCell().setInnerText("Date");
		row.addCell().setInnerText(checkDateStr);

		row = statisticTable.addRow();
		row.addCell().setInnerText("Operation");
		row.addCell().setInnerText("Report publications check");

		row = statisticTable.addRow();
		row.addCell().setInnerText("Warnings received");
		row.addCell().setInnerText(Str.valueOf(warnings));

		row = statisticTable.addRow();
		row.addCell().setInnerText("Errors received");
		row.addCell().setInnerText(Str.valueOf(errors));

		Utils::Html h4 = new Html("h4");
		h4.setInnerText("Statistics:");

		Utils::Html result = new Html("span");
		result.add(h4);
		result.add(statisticTable);

		if (problemsTable.rowCount() > 0) {
		    h4 = new Html("h4");
		    h4.setInnerText("Details:");
		    
		    result.add(h4);
		    result.add(problemsTable);
		}

		return result.toString();
	}

	/*Radix::Reports::ReportsExplorerUtils:isAnyReportCellAssotiateToColumn-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportsExplorerUtils:isAnyReportCellAssotiateToColumn")
	public static published  boolean isAnyReportCellAssotiateToColumn (org.radixware.kernel.common.client.IClientEnvironment environment, org.radixware.kernel.common.types.Id reportId) {

		final Arte::TypesXsd:IdDocument requestDoc = Arte::TypesXsd:IdDocument.Factory.newInstance();
		requestDoc.setId(reportId);

		try {
		    final Arte::TypesXsd:BoolDocument response =
		            (Arte::TypesXsd:BoolDocument) environment.EasSession.executeContextlessCommand(
		            idof[IsAnyReportCellAssotiateToColumnCmd],
		            requestDoc,
		            Arte::TypesXsd:BoolDocument.class);    
		    return response == null ? false : response.getBool().booleanValue();
		} catch (Exceptions::InterruptedException ex) {
		    // nothing - cancel pressed
		    return false;
		} catch (Exceptions::ServiceClientException ex) {
		    environment.processException(ex);
		    return false;
		}

	}


}

/* Radix::Reports::ReportsExplorerUtils - Web Meta*/

/*Radix::Reports::ReportsExplorerUtils-Client-Common Dynamic Class*/

package org.radixware.ads.Reports.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class ReportsExplorerUtils_mi{
}

/* Radix::Reports::ReportsExplorerUtils - Localizing Bundle */
package org.radixware.ads.Reports.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class ReportsExplorerUtils - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Details:");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Подробности:");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCJFESFORZRCAXJYZMWGQFOP73U"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Report Publication");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Публикация отчета");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCXOIG2MOIFES7DVQNRVHXQL74I"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Publication Context");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Контекст публикации");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDDFQTPSXSBA7NDLVEKWFRXXNIE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Context-free");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Бесконтекстные");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFJ3XKWRFAZENROINGDFUJS6JWQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Statistics:");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Статистика:");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGALTRIILHJEBBN45W37Z4ZASTQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Problem");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Проблема");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGED6RMQNFZEMJJAMP6UMAE2WQQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Save Report");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Загрузка отчета");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIYD6FZBKQJDR3O6WK3A245HSZE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Warnings received");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Получено предупреждений");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKHH7ZZZ7BBEQXN2WTY5QD52ALM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Errors received");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Получено ошибок");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMX2O2AYWMVBW7ASRDDUGBKOVJU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Errors:");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ошибки:");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNAT4PS43WRA55IQTM65D2UEB7I"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Warnings:");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Предупреждения:");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNZZGNGAESZH3BKO5T5TUCAUWNA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Parent context is absent");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Родительский контекст отсутствует");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQLLYYXY4OJGLXIMPXZ6NCIZ7BQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Common Reports");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Общие отчеты");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQMZ462LRJZDB3BIUAWUMAKCEWA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Operation");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Операция");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUP5BOQVB2BE3PNN6MDRQWQLY5A"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Date");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Дата");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVR5QSEQMGZGSNGJMEO64ZJM3VI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Report publications check");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Проверка публикаций отчетов");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYFZUNYXFO5DGXNTWZCUFVOBV5U"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(ReportsExplorerUtils - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbadcDHI5YQVUWZFTXMXT2QKFPDDVRY"),"ReportsExplorerUtils - Localizing Bundle",$$$items$$$);
}
