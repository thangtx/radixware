
/* Radix::Reports.Scheduler::Task.Report - Server Executable*/

/*Radix::Reports.Scheduler::Task.Report-Application Class*/

package org.radixware.ads.Reports.Scheduler.server;

import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.File;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report")
public published class Task.Report  extends org.radixware.ads.Scheduling.server.Task.Atomic  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {


	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return Task.Report_mi.rdxMeta;}

	/*Radix::Reports.Scheduler::Task.Report:Nested classes-Nested Classes*/

	/*Radix::Reports.Scheduler::Task.Report:FileController-Server Dynamic Class*/


	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:FileController")
	public class FileController  implements org.radixware.kernel.server.reports.IReportFileController  {

		private final java.io.File dir;
		private final String clientDirName;
		/**Metainformation accessor method*/
		@SuppressWarnings("unused")
		public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return Task.Report_mi.rdxMeta_adcH3IVDHMMYNFDXKC5E7VBS3BDKI;}

		/*Radix::Reports.Scheduler::Task.Report:FileController:Nested classes-Nested Classes*/

		/*Radix::Reports.Scheduler::Task.Report:FileController:Properties-Properties*/

		/*Radix::Reports.Scheduler::Task.Report:FileController:Methods-Methods*/

		/*Radix::Reports.Scheduler::Task.Report:FileController:adjustFileName-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:FileController:adjustFileName")
		public published  Str adjustFileName (org.radixware.kernel.server.types.Report report, Str fileName) {
			return Task.Report.this.adjustFileName(report,fileName);
		}

		/*Radix::Reports.Scheduler::Task.Report:FileController:afterCloseFile-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:FileController:afterCloseFile")
		public published  void afterCloseFile (org.radixware.kernel.server.types.Report report, java.io.File file) {

			try {
			    if (format == Reports::ReportExportFormat:XML && xslt != null) {
			        final File tempFile = File.createTempFile("RadixReport", ".xml");
			        try {

			            final FileOutputStream tempOutputStream = new java.io.FileOutputStream(tempFile);
			            final FileInputStream inputStream = new java.io.FileInputStream(file);

			            try {
			                Utils::Xslt.performTransformation(inputStream, tempOutputStream, xslt);
			            } finally {
			                try {
			                    inputStream.close();
			                } catch (Exceptions::IOException e) {
			                }
			                try {
			                    tempOutputStream.close();
			                } catch (Exceptions::IOException e) {
			                }
			                org.radixware.kernel.common.utils.FileUtils.copyFile(tempFile, file);
			            }
			        } finally {
			            tempFile.delete();
			        }
			    }
			} catch (Exceptions::IOException | Exceptions::AppException ex) {
			    Arte::Trace.error(org.radixware.kernel.common.utils.ExceptionTextFormatter.exceptionStackToString(ex), Arte::EventSource:JobScheduler);
			}

			if (clientDirName != null) {//interactive eas mode
			    Client.Resources::FileOutResource fout = Reports::ReportsServerUtils.openFileOutResource(clientDirName + "/" + file.getName());
			    try {
			        final java.io.FileInputStream in = new java.io.FileInputStream(file);
			        try {
			            org.radixware.kernel.common.utils.FileUtils.copyStream(in, fout);
			        } finally {
			            try {
			                in.close();
			            } catch (Exceptions::IOException ex) {
			                Arte::Trace.error(org.radixware.kernel.common.utils.ExceptionTextFormatter.exceptionStackToString(ex), Arte::EventSource:JobScheduler);
			            }
			        }
			    } catch (Exceptions::IOException ex) {
			        Arte::Trace.error(org.radixware.kernel.common.utils.ExceptionTextFormatter.exceptionStackToString(ex), Arte::EventSource:JobScheduler);
			    } finally {
			        try {
			            fout.close();
			        } catch (Exceptions::IOException ex) {
			            Arte::Trace.error(org.radixware.kernel.common.utils.ExceptionTextFormatter.exceptionStackToString(ex), Arte::EventSource:JobScheduler);
			        }
			    }
			} else {
			    try {
			        if(!file.ParentFile.equals(dir)){
			            File target = new File(dir,file.getName());
			            org.radixware.kernel.common.utils.FileUtils.copyFile(file,target);
			            org.radixware.kernel.common.utils.FileUtils.deleteFile(file);
			            file = target;
			        }
			        Task.Report.this.execPostOsCommand(file);
			    } catch (Exceptions::Throwable ex) {
			        Arte::Trace.error(org.radixware.kernel.common.utils.ExceptionTextFormatter.exceptionStackToString(ex), Arte::EventSource:JobScheduler);
			    }
			}

		}

		/*Radix::Reports.Scheduler::Task.Report:FileController:afterCreateFile-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:FileController:afterCreateFile")
		public published  void afterCreateFile (org.radixware.kernel.server.types.Report report, java.io.File file, java.io.OutputStream outputStream) {

		}

		/*Radix::Reports.Scheduler::Task.Report:FileController:beforeCloseFile-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:FileController:beforeCloseFile")
		public published  void beforeCloseFile (org.radixware.kernel.server.types.Report report, java.io.File file, java.io.OutputStream outputStream) {

		}

		/*Radix::Reports.Scheduler::Task.Report:FileController:getDirectory-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:FileController:getDirectory")
		public published  java.io.File getDirectory () {
			return dir;
		}

		/*Radix::Reports.Scheduler::Task.Report:FileController:FileController-User Method*/

		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:FileController:FileController")
		public  FileController (java.io.File dir, Str clientDirName) {
			this.dir = dir;
			this.clientDirName = clientDirName;
		}


	}

	/*Radix::Reports.Scheduler::Task.Report:Properties-Properties*/

	/*Radix::Reports.Scheduler::Task.Report:reportClassGuid-Dynamic Property*/



	protected Str reportClassGuid=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:reportClassGuid")
	public published  Str getReportClassGuid() {
		return reportClassGuid;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:reportClassGuid")
	public published   void setReportClassGuid(Str val) {
		reportClassGuid = val;
	}

	/*Radix::Reports.Scheduler::Task.Report:dir-Dynamic Property*/



	protected Str dir=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:dir")
	public published  Str getDir() {
		return dir;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:dir")
	public published   void setDir(Str val) {
		dir = val;
	}

	/*Radix::Reports.Scheduler::Task.Report:fileNameFormat-Dynamic Property*/



	protected Str fileNameFormat=(Str)org.radixware.kernel.common.defs.value.ValAsStr.fromStr("yyyy_MM_dd_hh_mm_ss",org.radixware.kernel.common.enums.EValType.STR);











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:fileNameFormat")
	public published  Str getFileNameFormat() {
		return fileNameFormat;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:fileNameFormat")
	public published   void setFileNameFormat(Str val) {
		fileNameFormat = val;
	}

	/*Radix::Reports.Scheduler::Task.Report:format-Dynamic Property*/



	protected org.radixware.kernel.common.enums.EReportExportFormat format=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:format")
	public published  org.radixware.kernel.common.enums.EReportExportFormat getFormat() {
		return format;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:format")
	public published   void setFormat(org.radixware.kernel.common.enums.EReportExportFormat val) {
		format = val;
	}

	/*Radix::Reports.Scheduler::Task.Report:reportClassTitle-Dynamic Property*/



	protected Str reportClassTitle=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:reportClassTitle")
	public published  Str getReportClassTitle() {

		if (this.reportClassGuid == null)
		    return null;
		    
		final String title = Meta::Utils.getDefinitionTitle(Types::Id.Factory.loadFrom(this.reportClassGuid));
		if (title != null && !title.isEmpty())
		    return title;
		    
		return this.reportClassGuid.toString();

	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:reportClassTitle")
	public published   void setReportClassTitle(Str val) {

		boolean isRemoveColumnsSettings = !java.util.Objects.equals(internal[reportClassTitle], val);
		internal[reportClassTitle] = val;
		columnsSettingsDocument = isRemoveColumnsSettings ? null : columnsSettingsDocument;
	}

	/*Radix::Reports.Scheduler::Task.Report:xslt-Dynamic Property*/



	protected Str xslt=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:xslt")
	public published  Str getXslt() {
		return xslt;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:xslt")
	public published   void setXslt(Str val) {
		xslt = val;
	}

	/*Radix::Reports.Scheduler::Task.Report:xsltTitle-Dynamic Property*/



	protected Str xsltTitle=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:xsltTitle")
	public published  Str getXsltTitle() {

		if (this.xslt!=null)
		    return "<defined>";
		else
		    return null;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:xsltTitle")
	public published   void setXsltTitle(Str val) {
		xsltTitle = val;
	}

	/*Radix::Reports.Scheduler::Task.Report:xsd-Dynamic Property*/



	protected Str xsd=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:xsd")
	public published  Str getXsd() {
		return xsd;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:xsd")
	public published   void setXsd(Str val) {
		xsd = val;
	}

	/*Radix::Reports.Scheduler::Task.Report:xsdTitle-Dynamic Property*/



	protected Str xsdTitle=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:xsdTitle")
	public published  Str getXsdTitle() {

		if (this.xsd!=null)
		    return "<defined>";
		else
		    return null;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:xsdTitle")
	public published   void setXsdTitle(Str val) {
		xsdTitle = val;
	}

	/*Radix::Reports.Scheduler::Task.Report:postOsCommand-Dynamic Property*/



	protected Str postOsCommand=(Str)org.radixware.kernel.common.defs.value.ValAsStr.fromStr("",org.radixware.kernel.common.enums.EValType.STR);











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:postOsCommand")
	public published  Str getPostOsCommand() {
		return postOsCommand;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:postOsCommand")
	public published   void setPostOsCommand(Str val) {
		postOsCommand = val;
	}

	/*Radix::Reports.Scheduler::Task.Report:msdlTitle-Dynamic Property*/



	protected Str msdlTitle=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:msdlTitle")
	public published  Str getMsdlTitle() {

		if (this.msdl!=null)
		    return "<defined>";
		else
		    return null;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:msdlTitle")
	public published   void setMsdlTitle(Str val) {
		msdlTitle = val;
	}

	/*Radix::Reports.Scheduler::Task.Report:params-User Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:params")
	public  java.sql.Clob getParams() {
		return params;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:params")
	public   void setParams(java.sql.Clob val) {
		params = val;
	}

	/*Radix::Reports.Scheduler::Task.Report:lag-Dynamic Property*/



	protected Int lag=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:lag")
	public  Int getLag() {
		return lag;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:lag")
	public   void setLag(Int val) {
		lag = val;
	}

	/*Radix::Reports.Scheduler::Task.Report:parametersBinding-Dynamic Property*/



	protected org.radixware.schemas.reports.ParametersBindingDocument parametersBinding=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:parametersBinding")
	public  org.radixware.schemas.reports.ParametersBindingDocument getParametersBinding() {
		return parametersBinding;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:parametersBinding")
	public   void setParametersBinding(org.radixware.schemas.reports.ParametersBindingDocument val) {
		parametersBinding = val;
	}

	/*Radix::Reports.Scheduler::Task.Report:msdl-Dynamic Property*/



	protected org.radixware.schemas.reports.ReportMsdlDocument msdl=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:msdl")
	public  org.radixware.schemas.reports.ReportMsdlDocument getMsdl() {
		return msdl;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:msdl")
	public   void setMsdl(org.radixware.schemas.reports.ReportMsdlDocument val) {
		msdl = val;
	}

	/*Radix::Reports.Scheduler::Task.Report:writeReportFunction-User Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:writeReportFunction")
	public  org.radixware.ads.Reports.Scheduler.server.UserFunc.WriteReportBand getWriteReportFunction() {
		return writeReportFunction;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:writeReportFunction")
	public   void setWriteReportFunction(org.radixware.ads.Reports.Scheduler.server.UserFunc.WriteReportBand val) {
		writeReportFunction = val;
	}

	/*Radix::Reports.Scheduler::Task.Report:prepareParamsFunction-User Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:prepareParamsFunction")
	public  org.radixware.ads.Reports.Scheduler.server.UserFunc.PrepareReportParams getPrepareParamsFunction() {
		return prepareParamsFunction;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:prepareParamsFunction")
	public   void setPrepareParamsFunction(org.radixware.ads.Reports.Scheduler.server.UserFunc.PrepareReportParams val) {
		prepareParamsFunction = val;
	}

	/*Radix::Reports.Scheduler::Task.Report:calcFileName-User Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:calcFileName")
	public  org.radixware.ads.Reports.Scheduler.server.UserFunc.CalcFileName getCalcFileName() {
		return calcFileName;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:calcFileName")
	public   void setCalcFileName(org.radixware.ads.Reports.Scheduler.server.UserFunc.CalcFileName val) {
		calcFileName = val;
	}

	/*Radix::Reports.Scheduler::Task.Report:encoding-User Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:encoding")
	public published  Str getEncoding() {
		return encoding;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:encoding")
	public published   void setEncoding(Str val) {
		encoding = val;
	}

	/*Radix::Reports.Scheduler::Task.Report:adjustFileName-User Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:adjustFileName")
	public  org.radixware.ads.Reports.Scheduler.server.UserFunc.AdjustFileName getAdjustFileName() {
		return adjustFileName;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:adjustFileName")
	public   void setAdjustFileName(org.radixware.ads.Reports.Scheduler.server.UserFunc.AdjustFileName val) {
		adjustFileName = val;
	}

	/*Radix::Reports.Scheduler::Task.Report:isMultiFile-Dynamic Property*/



	protected Bool isMultiFile=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:isMultiFile")
	public  Bool getIsMultiFile() {

		try {
		    Meta::ClassDef clazz = Arte::Arte.getDefManager().getClassDef(Types::Id.Factory.loadFrom(this.reportClassGuid));
		    if (clazz != null && clazz.getPresentation() != null) {
		        return clazz.getPresentation().isMultiFileReport();
		    } else {
		        return false;
		    }
		} catch (Exceptions::Exception e) {
		    Arte::Trace.debug(Utils::ExceptionTextFormatter.exceptionStackToString(e), Arte::EventSource:ArteReports);
		    return false;
		}
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:isMultiFile")
	public   void setIsMultiFile(Bool val) {
		isMultiFile = val;
	}

	/*Radix::Reports.Scheduler::Task.Report:counter-User Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:counter")
	public published  org.radixware.ads.Utils.server.Counter getCounter() {
		return counter;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:counter")
	public published   void setCounter(org.radixware.ads.Utils.server.Counter val) {
		counter = val;
	}

	/*Radix::Reports.Scheduler::Task.Report:calcPdfSecurityOptions-User Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:calcPdfSecurityOptions")
	public  org.radixware.ads.Reports.server.UserFunc.CalcPdfSecurityOptions getCalcPdfSecurityOptions() {
		return calcPdfSecurityOptions;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:calcPdfSecurityOptions")
	public   void setCalcPdfSecurityOptions(org.radixware.ads.Reports.server.UserFunc.CalcPdfSecurityOptions val) {
		calcPdfSecurityOptions = val;
	}

	/*Radix::Reports.Scheduler::Task.Report:wasGenerationError-Dynamic Property*/



	protected boolean wasGenerationError=false;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:wasGenerationError")
	protected  boolean getWasGenerationError() {
		return wasGenerationError;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:wasGenerationError")
	protected   void setWasGenerationError(boolean val) {
		wasGenerationError = val;
	}

	/*Radix::Reports.Scheduler::Task.Report:failOnGenerationError-User Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:failOnGenerationError")
	public published  Bool getFailOnGenerationError() {
		return failOnGenerationError;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:failOnGenerationError")
	public published   void setFailOnGenerationError(Bool val) {
		failOnGenerationError = val;
	}

	/*Radix::Reports.Scheduler::Task.Report:columnsSettingsDocument-Dynamic Property*/



	protected org.radixware.schemas.reports.ColumnSettingsDocument columnsSettingsDocument=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:columnsSettingsDocument")
	public  org.radixware.schemas.reports.ColumnSettingsDocument getColumnsSettingsDocument() {
		return columnsSettingsDocument;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:columnsSettingsDocument")
	public   void setColumnsSettingsDocument(org.radixware.schemas.reports.ColumnSettingsDocument val) {
		columnsSettingsDocument = val;
	}

























































































































































































	/*Radix::Reports.Scheduler::Task.Report:Methods-Methods*/

	/*Radix::Reports.Scheduler::Task.Report:afterRead-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:afterRead")
	public published  void afterRead () {
		super.afterRead();

		loadParams();

	}

	/*Radix::Reports.Scheduler::Task.Report:getFile-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:getFile")
	private final  java.io.File getFile (Str dir, Str fileNameFormat, org.radixware.kernel.common.enums.EReportExportFormat format, java.util.Map<org.radixware.kernel.common.types.Id,java.lang.Object> paramId2Value) {
		final java.text.SimpleDateFormat dateTimeformat = new java.text.SimpleDateFormat(fileNameFormat);
		String name = dateTimeformat.format(new java.util.Date());

		if (calcFileName != null) {
		    name = calcFileName.calcFileName(paramId2Value, name);
		}

		return new java.io.File(dir, name);


	}

	/*Radix::Reports.Scheduler::Task.Report:save-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:save")
	private final  void save () {
		Reports::ReportsXsd:ReportScheduleDocument doc =
		        Reports::ReportsXsd:ReportScheduleDocument.Factory.newInstance();
		Reports::ReportsXsd:ReportScheduleDocument.ReportSchedule xReportSchedule =
		        doc.addNewReportSchedule();

		xReportSchedule.ReportClassGuid = this.reportClassGuid;
		xReportSchedule.Dir = this.dir;
		xReportSchedule.FileNameFormat = this.fileNameFormat;
		xReportSchedule.Format = this.format;

		if (this.lag != null && this.lag != 0) {
		    xReportSchedule.Lag = this.lag.longValue();
		}

		if (this.postOsCommand != null && !this.postOsCommand.isEmpty()) {
		    xReportSchedule.PostOsCommand = this.postOsCommand;
		}

		if (this.parametersBinding != null && this.parametersBinding.ParametersBinding != null) {
		    xReportSchedule.ParametersBinding = parametersBinding.ParametersBinding;
		}

		if (this.columnsSettingsDocument != null && this.columnsSettingsDocument.ColumnSettings != null) {
		    xReportSchedule.ColumnsSettings = this.columnsSettingsDocument.ColumnSettings;
		}

		if (this.xsd != null || this.xslt != null) {
		    Reports::ReportsXsd:ReportScheduleDocument.ReportSchedule.Transformation transformation =
		            xReportSchedule.addNewTransformation();
		    transformation.Xsd = this.xsd;
		    transformation.Xslt = this.xslt;
		}

		if (this.msdl != null && this.msdl.ReportMsdl != null) {
		    xReportSchedule.Msdl = this.msdl.ReportMsdl;
		}

		try {
		    if (this.params != null) {
		        this.params.free();
		    }
		    this.params = Utils::XmlObjectSerializer.asClob(doc);
		} catch (Exceptions::SQLException ex) {
		    throw new AppError("Unable to save report schedule parameters", ex);
		}
	}

	/*Radix::Reports.Scheduler::Task.Report:beforeUpdate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:beforeUpdate")
	protected published  boolean beforeUpdate () {
		if (super.beforeUpdate()) {
		    save();
		    return true;
		} else {
		    return false;
		}

	}

	/*Radix::Reports.Scheduler::Task.Report:generateReport-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:generateReport")
	private final  void generateReport (org.radixware.schemas.reports.GenerateReportRqDocument input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValHandlersById) {
		final String fileName = input.GenerateReportRq.File;
		final Types::Report report = getReportInstance();
		final java.util.Map<Types::Id, Object> paramId2Value = calcParams(report, this.prevExecTime, this.lastExecTime, null);

		if (report.isMultyFile()) {
		    final java.io.File tmpDir = new java.io.File(new File(System.getProperty("java.io.tmpdir")), java.util.UUID.randomUUID().toString());
		    tmpDir.mkdirs();
		    try {
		        generateToDir(tmpDir, report, paramId2Value, fileName);
		    } catch (Exceptions::Throwable ex) {
		        throw new AppError("Unable to generate report.", ex);
		    } finally {
		        org.radixware.kernel.common.utils.FileUtils.deleteDirectory(tmpDir);
		    }
		} else {

		    Client.Resources::FileOutResource fout = Reports::ReportsServerUtils.openFileOutResource(fileName);

		    try {
		        try {
		            generateToStream(
		                    fout, report, paramId2Value
		            /*this.,
		             this.*/);
		        } catch (Exceptions::Throwable ex) {
		            throw new AppError("Unable to generate report.", ex);
		        }
		    } finally {
		        try {
		            fout.close();
		        } catch (Exceptions::IOException ex) {
		            throw new AppError("Unable to close file out resource.", ex);
		        }
		    }
		}

	}

	/*Radix::Reports.Scheduler::Task.Report:generateToStream-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:generateToStream")
	public  void generateToStream (java.io.OutputStream outputStream, org.radixware.ads.Types.server.Report report, java.util.Map<org.radixware.kernel.common.types.Id,java.lang.Object> paramId2Value) throws java.lang.Throwable {
		final Reports::ReportsXsd:ColumnSettings columnSettings = columnsSettingsDocument == null ? null : columnsSettingsDocument.ColumnSettings;

		Reports::ReportExportParameters parameters = new ReportExportParameters(outputStream);
		parameters.setParamId2Value(paramId2Value);
		parameters.setColumnSettings(columnSettings);

		if (this.format == Reports::ReportExportFormat:MSDL) {
		    if (this.msdl == null || this.msdl.ReportMsdl == null) {
		        throw new IllegalUsageError("MSLD not defined");
		    }
		    
		    parameters.setReportMsdl(this.msdl.ReportMsdl);
		    report.export(parameters);
		//    report.(outputStream, paramId2Value, this..ReportMsdl);
		} else if (this.format == Reports::ReportExportFormat:XML && this.xslt != null) {
		    final File tempFile = File.createTempFile("RadixReport", ".xml");
		    try {
		        final FileOutputStream tempOutputStream = new java.io.FileOutputStream(tempFile);
		        parameters = new ReportExportParameters(tempOutputStream);
		        parameters.setParamId2Value(paramId2Value);
		        parameters.setColumnSettings(columnSettings);
		        parameters.setExportFormat(this.format);
		        
		        try {
		            report.export(parameters);
		//            report.(tempOutputStream, this., paramId2Value);
		        } finally {
		            tempOutputStream.close();
		        }
		        final FileInputStream tempInputStream = new java.io.FileInputStream(tempFile);
		        try {
		            Utils::Xslt.performTransformation(tempInputStream, outputStream, this.xslt);
		        } finally {
		            tempInputStream.close();
		        }
		    } finally {
		        tempFile.delete();
		    }
		} else if (this.format == Reports::ReportExportFormat:CUSTOM) {
		    if (writeReportFunction == null) {
		        throw new AppError("Write band function is not defined");
		    }
		    Types::IReportWriter reportWriter = new Types::IReportWriter() {
		        private java.util.Map<Str, Object> vars = new java.util.HashMap<Str, Object>();

		        public void begin(java.io.OutputStream outputStream) {
		            //do nothing
		        }

		        public void writeBand(org.radixware.schemas.reports.BandType xBand, java.io.OutputStream outputSteam) {
		            final long version = writeReportFunction.lockVersion();
		            try {
		                if (version == 0)
		                    writeReportFunction.writeBand(xBand, outputSteam);
		                else
		                    writeReportFunction.writeBand(xBand, outputSteam, vars);
		            } catch (Exceptions::IOException ex) {
		                throw new AppError("Unable to generate report", ex);
		            } finally {
		                writeReportFunction.unlockVersion();
		            }
		        }

		        public void end(java.io.OutputStream outputStream) {
		            //do nothing
		        }
		    };
		    parameters.setReportWriter(reportWriter);
		    
		    report.export(parameters);
		//    report.(outputStream, paramId2Value, reportWriter);
		} else {
		    if (calcPdfSecurityOptions != null) {
		        Types::Entity context = null;
		        final Types::Id contextParameterId = report.ContextParameterId;
		        final Object contextObject = (contextParameterId != null ? paramId2Value.get(report.ContextParameterId) : null);
		        if (contextObject instanceof Types::Entity) {
		            context = (Types::Entity) contextObject;
		        }
		        Reports::PdfSecurityOptions securityParams = calcPdfSecurityOptions.calcPdfSecurityOption(paramId2Value, context);
		        report.setPdfSecurityOptions(securityParams);
		    }
		    
		    parameters.setExportFormat(this.format);
		    parameters.setEncoding(this.encoding);
		    
		    report.export(parameters);
		    
		//    report.(outputStream, this., this., paramId2Value);
		}

	}

	/*Radix::Reports.Scheduler::Task.Report:parseParams-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:parseParams")
	private final  org.radixware.schemas.reports.ReportScheduleDocument.ReportSchedule parseParams () {
		try {
		    if (this.params == null) {
		        return null;
		    }
		    
		    final long len = this.params.length();
		    if (len == 0) {
		        return null;
		    }

		    final java.io.Reader reader = this.params.getCharacterStream(1, len);
		    try {
		        final Reports::ReportsXsd:ReportScheduleDocument doc =
		                Reports::ReportsXsd:ReportScheduleDocument.Factory.parse(reader);
		        return doc.ReportSchedule;
		    } finally {
		        reader.close();
		    }
		} catch (Exceptions::SQLException ex) {
		    //.(org.radixware.kernel.common.utils.ExceptionTextFormatter.exceptionStackToString(ex), );
		    throw new AppError("Unable to load report schedule parameters", ex);
		} catch (Exceptions::IOException ex) {
		    //.(org.radixware.kernel.common.utils.ExceptionTextFormatter.exceptionStackToString(ex), );
		    throw new AppError("Unable to load report schedule parameters", ex);
		} catch (org.apache.xmlbeans.XmlException ex) {
		    //.(org.radixware.kernel.common.utils.ExceptionTextFormatter.exceptionStackToString(ex), );
		    throw new AppError("Unable to load report schedule parameters", ex);
		}

	}

	/*Radix::Reports.Scheduler::Task.Report:execPostOsCommand-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:execPostOsCommand")
	private final  void execPostOsCommand (java.io.File reportFile) throws java.lang.InterruptedException,java.io.IOException {
		if (this.postOsCommand == null || this.postOsCommand.isEmpty()) {
		    return;
		}

		final String reportFilePath = reportFile.AbsolutePath;

		Types::Id reportClassId = Types::Id.Factory.loadFrom(this.reportClassGuid);
		String reportClassName = Meta::Utils.getDefinitionName(reportClassId);
		if (reportClassName == null) {
		    reportClassName = this.reportClassGuid;
		    if (reportClassName == null) {
		        reportClassName = "null";
		    }
		}

		final String jobTitle = (this.title != null && !this.title.isEmpty() ? this.title : "null");

		// exec
		final String[] params = new String[]{this.postOsCommand, reportFilePath, jobTitle, reportClassName};

		Arte::Trace.debug("External OS command: \"" + this.postOsCommand + "\" \"" + reportFilePath + "\" \"" + jobTitle + "\" \"" + reportClassName + "\"", Arte::EventSource:JobExecutor);

		final Process p = Runtime.getRuntime().exec(params);
		int res = p.waitFor();
		try {
		    p.getErrorStream().close();
		} catch (Exceptions::IOException e) { /*NOTHING*/ }
		try {
		    p.getOutputStream().close();
		} catch (Exceptions::IOException e) { /*NOTHING*/ }
		try {
		    p.getInputStream().close();
		} catch (Exceptions::IOException e) { /*NOTHING*/ }
		p.destroy();

		Arte::Trace.debug("OS command execution result: " + res, Arte::EventSource:JobExecutor);

	}

	/*Radix::Reports.Scheduler::Task.Report:execute-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:execute")
	public published  void execute (java.sql.Timestamp prevExecTime, java.sql.Timestamp curExecTime) {
		execute(prevExecTime, curExecTime, null);

	}

	/*Radix::Reports.Scheduler::Task.Report:beforeCreate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:beforeCreate")
	protected published  boolean beforeCreate (org.radixware.kernel.server.types.Entity src) {
		if (super.beforeCreate(src)) {
		    save();
		    return true;
		} else {
		    return false;
		}

	}

	/*Radix::Reports.Scheduler::Task.Report:getLagTime-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:getLagTime")
	private final  java.sql.Timestamp getLagTime (java.sql.Timestamp dateTime) {
		if (this.lag != null && lag != 0 && dateTime!=null) {
		    return new DateTime(dateTime.Time - lag.longValue() * 1000);
		}

		return dateTime;

	}

	/*Radix::Reports.Scheduler::Task.Report:beforeCreateOnImport-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:beforeCreateOnImport")
	protected published  void beforeCreateOnImport (org.radixware.ads.Scheduling.common.TaskExportXsd.Task xTask) {
		super.beforeCreateOnImport(xTask);
		loadParams();
	}

	/*Radix::Reports.Scheduler::Task.Report:loadParams-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:loadParams")
	private final  void loadParams () {
		final Reports::ReportsXsd:ReportScheduleDocument.ReportSchedule xReportSchedule = parseParams();
		if (xReportSchedule != null) {
		    this.reportClassGuid = xReportSchedule.ReportClassGuid;
		    this.dir = xReportSchedule.Dir;
		    this.fileNameFormat = xReportSchedule.FileNameFormat;
		    this.format = xReportSchedule.Format;
		    if (xReportSchedule.ParametersBinding != null) {
		        Reports::ReportsXsd:ParametersBindingDocument doc = Reports::ReportsXsd:ParametersBindingDocument.Factory.newInstance();
		        doc.ParametersBinding = xReportSchedule.ParametersBinding;
		        this.parametersBinding = doc;
		    }
		    if (xReportSchedule.ColumnsSettings != null) {
		        Reports::ReportsXsd:ColumnSettingsDocument doc = Reports::ReportsXsd:ColumnSettingsDocument.Factory.newInstance();
		        doc.ColumnSettings = xReportSchedule.ColumnsSettings;
		        this.columnsSettingsDocument = doc;
		    }
		    
		    this.xsd = (xReportSchedule.Transformation != null ? xReportSchedule.Transformation.Xsd : null);
		    this.xslt = (xReportSchedule.Transformation != null ? xReportSchedule.Transformation.Xslt : null);

		    if (xReportSchedule.Msdl != null) {
		        Reports::ReportsXsd:ReportMsdlDocument doc = Reports::ReportsXsd:ReportMsdlDocument.Factory.newInstance();
		        doc.ReportMsdl = xReportSchedule.Msdl;
		        this.msdl = doc;
		    }

		    this.postOsCommand = xReportSchedule.PostOsCommand;
		    this.lag = xReportSchedule.Lag;
		}
	}

	/*Radix::Reports.Scheduler::Task.Report:generateToStream-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:generateToStream")
	public  void generateToStream (java.io.OutputStream outputStream, java.sql.Timestamp prevExecTime, java.sql.Timestamp curExecTime, org.apache.xmlbeans.XmlObject params) {
		final Types::Report report = getReportInstance();
		final java.util.Map<Types::Id, Object> paramId2Value = calcParams(report, prevExecTime, curExecTime, params);

		try {
		    File file = null;
		    if (outputStream == null) {
		        file = getFile(this.dir, this.fileNameFormat, this.format, paramId2Value);
		        file.getParentFile().mkdirs();

		        Arte::Trace.debug(
		                "Executing report #" + String.valueOf(this.reportClassGuid) + " into '"
		                + file.getAbsolutePath() + "'", Arte::EventSource:JobExecutor);
		        outputStream = new FileOutputStream(file);
		    }
		    try {
		        generateToStream(outputStream, report, paramId2Value);
		    } finally {
		        outputStream.close();
		    }

		    Arte::Trace.debug("Report generated successfully", Arte::EventSource:JobExecutor);
		    if (file != null)
		        execPostOsCommand(file);
		} catch (Exceptions::Throwable ex) {
		    Arte::Trace.error(org.radixware.kernel.common.utils.ExceptionTextFormatter.exceptionStackToString(ex), Arte::EventSource:JobScheduler);
		}
	}

	/*Radix::Reports.Scheduler::Task.Report:calcParams-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:calcParams")
	public  java.util.Map<org.radixware.kernel.common.types.Id,java.lang.Object> calcParams (org.radixware.ads.Types.server.Report report, java.sql.Timestamp prevExecTime, java.sql.Timestamp curExecTime, org.apache.xmlbeans.XmlObject params) {
		final java.util.Map<Types::Id, Object> paramId2Value = new java.util.HashMap<Types::Id, Object>();
		if (parametersBinding != null && parametersBinding.ParametersBinding != null) {
		    java.util.List<Reports::ReportsXsd:ParametersBindingType.ParameterBinding> xParamBindings = parametersBinding.ParametersBinding.getParameterBindingList();
		    if (xParamBindings != null) {
		        for (Reports::ReportsXsd:ParametersBindingType.ParameterBinding xParamBinding : xParamBindings) {
		            final Types::Id parameterId = xParamBinding.ParameterId;
		            final Object paramValue;
		            if (xParamBinding.Type == Reports::ReportParameterBindingType:PrevExecTime) {
		                paramValue = getLagTime(prevExecTime);
		            } else if (xParamBinding.Type == Reports::ReportParameterBindingType:CurExecTime) {
		                paramValue = getLagTime(curExecTime);
		            } else {
		                paramValue = Reports::ReportsServerUtils.loadParamFromXml(report, parameterId, xParamBinding.Value);
		            }
		            paramId2Value.put(parameterId, paramValue);
		        }
		    }
		}

		if (params != null) {
		    Reports::ReportsXsd:ParametersBindingDocument parsOvr = (Reports::ReportsXsd:ParametersBindingDocument) Utils::XmlObjectProcessor.castToXmlClass(this.Arte.DefManager.ClassLoader, params, Reports::ReportsXsd:ParametersBindingDocument.class);
		    for (Reports::ReportsXsd:ParametersBindingType.ParameterBinding xParamBinding : parsOvr.ParametersBinding.ParameterBindingList) {
		        final Types::Id parameterId = xParamBinding.ParameterId;
		        final Object paramValue = Reports::ReportsServerUtils.loadParamFromXml(report, parameterId, xParamBinding.Value);
		        paramId2Value.put(parameterId, paramValue);
		    }
		}

		if (prepareParamsFunction != null)
		    prepareParamsFunction.prepareParams(paramId2Value, this, prevExecTime, curExecTime);

		return paramId2Value;
	}

	/*Radix::Reports.Scheduler::Task.Report:generateToDir-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:generateToDir")
	public  void generateToDir (java.sql.Timestamp prevExecTime, java.sql.Timestamp curExecTime, org.apache.xmlbeans.XmlObject params) {
		final Types::Report report = getReportInstance();
		final java.util.Map<Types::Id, Object> paramId2Value = calcParams(report, prevExecTime, curExecTime, params);

		File dir = new File(this.dir);
		dir.mkdirs();
		generateToDir(dir, report, paramId2Value, null);
	}

	/*Radix::Reports.Scheduler::Task.Report:adjustFileName-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:adjustFileName")
	public  Str adjustFileName (org.radixware.kernel.server.types.Report report, Str fileName) {
		if (adjustFileName != null) {
		    return adjustFileName.adjustFileName(report, fileName);
		} else
		    return fileName;
	}

	/*Radix::Reports.Scheduler::Task.Report:getReportInstance-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:getReportInstance")
	public  org.radixware.ads.Types.server.Report getReportInstance () {
		final Types::Id reportClassId = Types::Id.Factory.loadFrom(this.reportClassGuid);
		return Reports::ReportsServerUtils.instantiateReportByClassId(reportClassId);

	}

	/*Radix::Reports.Scheduler::Task.Report:generateToDir-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:generateToDir")
	public  void generateToDir (java.io.File dir, org.radixware.ads.Types.server.Report report, java.util.Map<org.radixware.kernel.common.types.Id,java.lang.Object> paramId2Value, Str clientFileDir) {
		final FileController controller = new FileController(dir, clientFileDir);

		final Reports::ReportsXsd:ColumnSettings columnSettings = columnsSettingsDocument == null ? null : columnsSettingsDocument.ColumnSettings;

		Reports::ReportExportParameters parameters = new ReportExportParameters(controller);
		parameters.setParamId2Value(paramId2Value);
		parameters.setColumnSettings(columnSettings);

		if (this.format == Reports::ReportExportFormat:MSDL) {
		    if (this.msdl == null || this.msdl.ReportMsdl == null) {
		        throw new IllegalUsageError("MSLD not defined");
		    }
		    parameters.setReportMsdl(this.msdl.ReportMsdl);
		    report.export(parameters);
		    
		//    report.(controller, paramId2Value, this..ReportMsdl);
		} else if (this.format == Reports::ReportExportFormat:CUSTOM) {
		    if (writeReportFunction == null) {
		        throw new AppError("Write band function is not defined");
		    }
		    
		    Types::IReportWriter reportWriter = new Types::IReportWriter() {
		        private java.util.Map<Str, Object> vars = new java.util.HashMap<Str, Object>();

		        public void begin(java.io.OutputStream outputStream) {
		            //do nothing
		        }

		        public void writeBand(org.radixware.schemas.reports.BandType xBand, java.io.OutputStream outputSteam) {
		            final long version = writeReportFunction.lockVersion();
		            try {
		                if (version == 0)
		                    writeReportFunction.writeBand(xBand, outputSteam);
		                else
		                    writeReportFunction.writeBand(xBand, outputSteam, vars);
		            } catch (Exceptions::IOException ex) {
		                throw new AppError("Unable to generate report", ex);
		            } finally {
		                writeReportFunction.unlockVersion();
		            }
		        }

		        public void end(java.io.OutputStream outputStream) {
		            //do nothing
		        }
		    };
		    
		    parameters.setReportWriter(reportWriter);
		    
		    report.export(parameters);
		//    report.(controller, paramId2Value, reportWriter);
		} else {
		    if (calcPdfSecurityOptions != null) {
		        Types::Entity context = null;
		        final Types::Id contextParameterId = report.ContextParameterId;
		        final Object contextObject = (contextParameterId != null ? paramId2Value.get(report.ContextParameterId) : null);
		        if (contextObject instanceof Types::Entity) {
		            context = (Types::Entity) contextObject;
		        }
		        Reports::PdfSecurityOptions securityParams = calcPdfSecurityOptions.calcPdfSecurityOption(paramId2Value, context);
		        report.setPdfSecurityOptions(securityParams);
		    }

		    parameters.setExportFormat(this.format);
		    parameters.setEncoding(this.encoding);
		    
		    report.export(parameters);
		//    report.(controller, this., this., paramId2Value);
		}

	}

	/*Radix::Reports.Scheduler::Task.Report:scheduleAsyncJob-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:scheduleAsyncJob")
	public published  org.radixware.ads.Scheduling.server.JobQueueItem scheduleAsyncJob (java.sql.Timestamp time, java.util.Map<org.radixware.kernel.common.types.Id,java.lang.Object> params) {
		Reports::ReportsXsd:ParametersBindingDocument paramsXml = null;
		if (params != null && params.size() > 0) {
		    paramsXml = Reports::ReportsXsd:ParametersBindingDocument.Factory.newInstance();
		    paramsXml.addNewParametersBinding();
		    Types::Id classId = Types::Id.Factory.loadFrom(this.reportClassGuid);
		    Meta::ClassDef classDef= Meta::Utils.getClassDef(classId);
		    for (java.util.Map.Entry<Types::Id, Object> e : params.entrySet()) {
		        Reports::ReportsXsd:ParametersBindingType.ParameterBinding p = paramsXml.ParametersBinding.addNewParameterBinding();
		        p.ParameterId = e.getKey();
		        final Meta::PropDef propDef = classDef.getPropById(e.getKey());
		        org.radixware.kernel.server.arte.services.eas.EasValueConverter.objVal2EasPropXmlVal(e.Value, null, propDef.getValType(), p.addNewValue());
		    }
		}

		Scheduling::JobQueueParam arrParam[] = new Scheduling::JobQueueParam[2];
		arrParam[0] = new JobQueueParam("taskId", Meta::ValType:Int, id);
		arrParam[1] = new JobQueueParam("params", Meta::ValType:Xml, paramsXml);
		return Scheduling::JobQueue.schedule(
		        "Async execution of report task #" + id,
		        this, time,
		        Task.Report.class.getName(),
		        idof[Task.Report:executeAsyncJob].toString(),
		        arrParam,
		        scpName,
		        priority.Value, priorityBoostingSpeed,
		        null, //taskId
		        true //rerun
		);

	}

	/*Radix::Reports.Scheduler::Task.Report:executeAsyncJob-User Method*/

	@org.radixware.kernel.common.lang.ReflectiveCallable
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:executeAsyncJob")
	public static  void executeAsyncJob (Int taskId, org.apache.xmlbeans.XmlObject params) {
		try {
		    Task.Report task = (Task.Report) loadByPK(taskId, true);
		    task.execute(null, null, params);
		} catch (Exceptions::Throwable ex) {
		    Arte::Trace.error(org.radixware.kernel.common.utils.ExceptionTextFormatter.exceptionStackToString(ex), Arte::EventSource:JobScheduler);
		}

	}

	/*Radix::Reports.Scheduler::Task.Report:execute-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:execute")
	  void execute (java.sql.Timestamp prevExecTime, java.sql.Timestamp curExecTime, org.apache.xmlbeans.XmlObject params) {
		wasGenerationError = false;
		try {
		    read();
		    if (isMultiFile == java.lang.Boolean.TRUE)
		        generateToDir(prevExecTime, curExecTime, params);
		    else
		        generateToStream(null, prevExecTime, curExecTime, params);

		} catch (Exceptions::Throwable ex) {
		    if (failOnGenerationError != null && failOnGenerationError.booleanValue()) {
		        setFaultMess(ex.getMessage());
		    }
		    wasGenerationError = true;
		    Arte::Trace.error(org.radixware.kernel.common.utils.ExceptionTextFormatter.exceptionStackToString(ex), Arte::EventSource:JobScheduler);
		}

	}

	/*Radix::Reports.Scheduler::Task.Report:refineStatusOnComplete-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:refineStatusOnComplete")
	public published  void refineStatusOnComplete () {
		if (wasGenerationError && failOnGenerationError != null && failOnGenerationError.booleanValue()) {
		    status =  Scheduling::TaskStatus:Failed;
		}
	}



	@Override
	public org.radixware.kernel.server.types.FormHandler.NextDialogsRequest execCommand(org.radixware.kernel.common.types.Id cmdId, org.radixware.kernel.common.types.Id propId, org.apache.xmlbeans.XmlObject input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValsById, org.apache.xmlbeans.XmlObject output) throws org.radixware.kernel.common.exceptions.AppException,java.lang.InterruptedException{
		if(cmdId == cmdVACIGPSCDZCMTFEAG4CITMDXSA){
			generateReport((org.radixware.schemas.reports.GenerateReportRqDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.getAppCommandInputData(getClass().getClassLoader(),input,org.radixware.schemas.reports.GenerateReportRqDocument.class),newPropValsById);
			return null;
		} else 
			return super.execCommand(cmdId,propId,input,newPropValsById,output);
	}


}

/* Radix::Reports.Scheduler::Task.Report - Server Meta*/

/*Radix::Reports.Scheduler::Task.Report-Application Class*/

package org.radixware.ads.Reports.Scheduler.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Task.Report_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("acl3JADSBDYJTOBDCIVAALOMT5GDM"),"Task.Report",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEDPFYTLQSVHQRAUWLQCGNHJRXU"),

						org.radixware.kernel.common.enums.EClassType.APPLICATION,

						/*Radix::Reports.Scheduler::Task.Report:Presentations-Entity Object Presentations*/
						/*Class presentation attributes*/
						 new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef(/*Owner Class Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("acl3JADSBDYJTOBDCIVAALOMT5GDM"),
							/*Owner Class Name*/
							"Task.Report",
							/*Title Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEDPFYTLQSVHQRAUWLQCGNHJRXU"),
							/*Property presentations*/

							/*Radix::Reports.Scheduler::Task.Report:Properties-Properties*/
							new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef[]{

									/*Radix::Reports.Scheduler::Task.Report:reportClassGuid:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd5JIP7T5DEJAL3MBQME2GHCEABA"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Reports.Scheduler::Task.Report:dir:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdNWADG4CG2REYXN4CIN7C47LMUI"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Reports.Scheduler::Task.Report:fileNameFormat:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd4G2OMFIOPJCM3D4TPKXW666GGE"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Reports.Scheduler::Task.Report:format:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdKUWC4G6GVRAGVCE5I7FKSDNGYQ"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Reports.Scheduler::Task.Report:reportClassTitle:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdEVCTPPHKUNHOPLJM5NYNAM7X4E"),org.radixware.kernel.common.enums.EEditPossibility.PROGRAMMATICALLY,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Reports.Scheduler::Task.Report:xslt:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdGELJ4T5XEBFILL4T4CEWFKNSKQ"),org.radixware.kernel.common.enums.EEditPossibility.PROGRAMMATICALLY,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Reports.Scheduler::Task.Report:xsltTitle:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdBYE35BUC3NAP7A3TPKHQ42MGRE"),org.radixware.kernel.common.enums.EEditPossibility.PROGRAMMATICALLY,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Reports.Scheduler::Task.Report:xsd:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdRKNFURILVBG2ZALIP753LRYBCI"),org.radixware.kernel.common.enums.EEditPossibility.PROGRAMMATICALLY,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Reports.Scheduler::Task.Report:xsdTitle:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdRKMVBC6O5JFTNKSPF7F2AH465M"),org.radixware.kernel.common.enums.EEditPossibility.PROGRAMMATICALLY,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Reports.Scheduler::Task.Report:postOsCommand:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdM54OFNWHO5CN5I3GKQ3JFMFRUM"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Reports.Scheduler::Task.Report:msdlTitle:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdEIRJGNE5NBHRFHF4FUB3WVUQLU"),org.radixware.kernel.common.enums.EEditPossibility.PROGRAMMATICALLY,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Reports.Scheduler::Task.Report:params:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pru6QWZVOCXGZBXHA6BFEQQRVSVVY"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Reports.Scheduler::Task.Report:lag:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdRMULELQWFVC25BIM7LQOOSZKRM"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Reports.Scheduler::Task.Report:parametersBinding:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdLG3TIEHK3VHBJEDR544Z3AA224"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Reports.Scheduler::Task.Report:msdl:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdIJWUYV3RDJDSTFF6BERXPEHXMI"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Reports.Scheduler::Task.Report:writeReportFunction:PropertyPresentation-Object Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadParentTitlePropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pru3Q2CZK3W3ZDTHBO4JIOPH7FBQI"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,null,null,null,null,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr7IKMB24XZHOBDCMTAALOMT5GDM")},null,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr7IKMB24XZHOBDCMTAALOMT5GDM")},java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.TITLE,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Reports.Scheduler::Task.Report:prepareParamsFunction:PropertyPresentation-Object Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadParentTitlePropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruA2Q7XRP4HJC2HNDXTLJKQLXV6Y"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,null,null,null,null,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr7IKMB24XZHOBDCMTAALOMT5GDM")},null,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr7IKMB24XZHOBDCMTAALOMT5GDM")},java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.TITLE,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Reports.Scheduler::Task.Report:calcFileName:PropertyPresentation-Object Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadParentTitlePropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pru33DBBUK5CJGEFEAZ7VOH5BDRG4"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,null,null,null,null,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr7IKMB24XZHOBDCMTAALOMT5GDM")},null,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr7IKMB24XZHOBDCMTAALOMT5GDM")},java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Reports.Scheduler::Task.Report:encoding:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruJGBA6I4VLNCWPH7HRQCGQLXVNE"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Reports.Scheduler::Task.Report:adjustFileName:PropertyPresentation-Object Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadParentTitlePropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruEYFORUCAPRCG7JHYPXNFTSC3XI"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,null,null,null,null,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr7IKMB24XZHOBDCMTAALOMT5GDM")},null,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr7IKMB24XZHOBDCMTAALOMT5GDM")},java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Reports.Scheduler::Task.Report:isMultiFile:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdK7ROH6D55ZADZBT5QWUJBOSJJ4"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Reports.Scheduler::Task.Report:counter:PropertyPresentation-Object Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadParentTitlePropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pru4GCLHE2775BXNNVJ7X64SX7K6M"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,null,null,null,null,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprPGEGCGCUIFBOPCIJ3GH2XBPWKA")},null,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprPGEGCGCUIFBOPCIJ3GH2XBPWKA")},java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Reports.Scheduler::Task.Report:calcPdfSecurityOptions:PropertyPresentation-Object Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadParentTitlePropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruIHUHMW56IVAN5DTG3BBIDR7HUQ"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,null,null,null,null,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr7IKMB24XZHOBDCMTAALOMT5GDM")},null,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr7IKMB24XZHOBDCMTAALOMT5GDM")},java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.TITLE,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Reports.Scheduler::Task.Report:failOnGenerationError:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruENQ5NCUPU5C53ALQLTU72TZD7Q"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Reports.Scheduler::Task.Report:columnsSettingsDocument:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdRU7U4H2AABER5KAGIAC5X2URPI"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT))
							},
							/*Commands*/
							new org.radixware.kernel.server.meta.presentations.RadCommandDef[]{

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::Reports.Scheduler::Task.Report:chooseReport-Property Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdPT4TV2AFZFHBRBZ2H6OT4WCSAU"),"chooseReport",org.radixware.kernel.common.enums.ECommandScope.PROPERTY,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("prdEVCTPPHKUNHOPLJM5NYNAM7X4E")},org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS,org.radixware.kernel.common.enums.ECommandNature.LOCAL,org.radixware.kernel.common.types.Id.Factory.loadFrom("acl3JADSBDYJTOBDCIVAALOMT5GDM"),false,true),

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::Reports.Scheduler::Task.Report:editXslt-Property Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdOLAGHY7YANHLXI7BSWV4DIBSTE"),"editXslt",org.radixware.kernel.common.enums.ECommandScope.PROPERTY,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("prdBYE35BUC3NAP7A3TPKHQ42MGRE")},org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS,org.radixware.kernel.common.enums.ECommandNature.LOCAL,org.radixware.kernel.common.types.Id.Factory.loadFrom("acl3JADSBDYJTOBDCIVAALOMT5GDM"),false,true),

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::Reports.Scheduler::Task.Report:editXsd-Property Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdFP33XLDWZVGD3L6D6I3DEF7RRM"),"editXsd",org.radixware.kernel.common.enums.ECommandScope.PROPERTY,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("prdRKMVBC6O5JFTNKSPF7F2AH465M")},org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS,org.radixware.kernel.common.enums.ECommandNature.LOCAL,org.radixware.kernel.common.types.Id.Factory.loadFrom("acl3JADSBDYJTOBDCIVAALOMT5GDM"),false,true),

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::Reports.Scheduler::Task.Report:generateReport-Object Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdVACIGPSCDZCMTFEAG4CITMDXSA"),"generateReport",org.radixware.kernel.common.enums.ECommandScope.OBJECT,null,org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_FIXED,org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,org.radixware.kernel.common.types.Id.Factory.loadFrom("acl3JADSBDYJTOBDCIVAALOMT5GDM"),false,true),

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::Reports.Scheduler::Task.Report:clearXsd-Property Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdCOY77PVTDREZ3GFJCRMXCL6AFM"),"clearXsd",org.radixware.kernel.common.enums.ECommandScope.PROPERTY,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("prdRKMVBC6O5JFTNKSPF7F2AH465M")},org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS,org.radixware.kernel.common.enums.ECommandNature.LOCAL,org.radixware.kernel.common.types.Id.Factory.loadFrom("acl3JADSBDYJTOBDCIVAALOMT5GDM"),false,true),

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::Reports.Scheduler::Task.Report:clearXslt-Property Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmd6ROEU2EOL5CL3GUCAPFYJOYYXU"),"clearXslt",org.radixware.kernel.common.enums.ECommandScope.PROPERTY,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("prdBYE35BUC3NAP7A3TPKHQ42MGRE")},org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS,org.radixware.kernel.common.enums.ECommandNature.LOCAL,org.radixware.kernel.common.types.Id.Factory.loadFrom("acl3JADSBDYJTOBDCIVAALOMT5GDM"),false,true),

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::Reports.Scheduler::Task.Report:editMsdl-Property Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdMCFKI7IZ2FBXXOFLCH7NIK4JZQ"),"editMsdl",org.radixware.kernel.common.enums.ECommandScope.PROPERTY,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("prdEIRJGNE5NBHRFHF4FUB3WVUQLU"),org.radixware.kernel.common.types.Id.Factory.loadFrom("prdIJWUYV3RDJDSTFF6BERXPEHXMI")},org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS,org.radixware.kernel.common.enums.ECommandNature.LOCAL,org.radixware.kernel.common.types.Id.Factory.loadFrom("acl3JADSBDYJTOBDCIVAALOMT5GDM"),false,true),

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::Reports.Scheduler::Task.Report:clearMsdl-Property Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdFPFOPWOAZ5CRXA7RTA6BS7HD3Q"),"clearMsdl",org.radixware.kernel.common.enums.ECommandScope.PROPERTY,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("prdEIRJGNE5NBHRFHF4FUB3WVUQLU")},org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS,org.radixware.kernel.common.enums.ECommandNature.LOCAL,org.radixware.kernel.common.types.Id.Factory.loadFrom("acl3JADSBDYJTOBDCIVAALOMT5GDM"),false,true)
							},
							/*Sortings*/
							null,
							/*Filters*/
							null,
							/*Editor presentations*/
							new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef[]{

									new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef(
									/*Radix::Reports.Scheduler::Task.Report:Edit-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprZU2CPFLYVJCXZCWOSIYQPC7P24"),
									"Edit",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprAIWBLDTSJTOBDCIVAALOMT5GDM"),
									36016,
									null,

									/*Radix::Reports.Scheduler::Task.Report:Edit:Children-Explorer Items*/
										null
									,
									null,
									(org.radixware.kernel.server.meta.presentations.RadPropertyPresentationAttributes[]) null,
									org.radixware.kernel.common.enums.EEditorPresentationRightsInheritanceMode.FROM_REPLACED,
									null),

									new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef(
									/*Radix::Reports.Scheduler::Task.Report:Create-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprOR3IPE5XXRDNFJMHE6BWYV7TZE"),
									"Create",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprZU2CPFLYVJCXZCWOSIYQPC7P24"),
									40112,
									null,

									/*Radix::Reports.Scheduler::Task.Report:Create:Children-Explorer Items*/
										null
									,
									null,
									(org.radixware.kernel.server.meta.presentations.RadPropertyPresentationAttributes[]) null,
									org.radixware.kernel.common.enums.EEditorPresentationRightsInheritanceMode.FROM_REPLACED,
									null)
							},
							/*Selector presentations*/
							null,
							/*Default selector presentation Id*/
							null,
							/*Presentation Map*/
							org.radixware.kernel.common.utils.Maps.fromArrays(new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprAIWBLDTSJTOBDCIVAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("eprXGQ6C3VMQPOBDCKCAALOMT5GDM")},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprZU2CPFLYVJCXZCWOSIYQPC7P24"),org.radixware.kernel.common.types.Id.Factory.loadFrom("eprOR3IPE5XXRDNFJMHE6BWYV7TZE")}),
							/*Object title format*/
							null,
							/*Class catalogs*/
							new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog[]{

									new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog(
									/*Radix::Reports.Scheduler::Task.Report:Default-Dynamic Class Catalog*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eccVXWRKQLYJTOBDCIVAALOMT5GDM"),org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.ELinkMode.VIRTUAL,new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.Item[]{
										new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.ClassRef(org.radixware.kernel.common.types.Id.Factory.loadFrom("acl3JADSBDYJTOBDCIVAALOMT5GDM"),null,62.5,true,org.radixware.kernel.common.types.Id.Factory.loadFrom("acl3JADSBDYJTOBDCIVAALOMT5GDM"),null)}
									)
							},
							/*Restrictions*/
							null,null,0),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblWZB7K4HLJPOBDCIUAALOMT5GDM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclZDLN466RV5FE3BQIWSX56ZAETQ"),

						/*Radix::Reports.Scheduler::Task.Report:Properties-Properties*/
						new org.radixware.kernel.server.meta.clazzes.RadPropDef[]{

								/*Radix::Reports.Scheduler::Task.Report:reportClassGuid-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd5JIP7T5DEJAL3MBQME2GHCEABA"),"reportClassGuid",null,org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Reports.Scheduler::Task.Report:dir-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdNWADG4CG2REYXN4CIN7C47LMUI"),"dir",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsICPTBCHW6RDX7ESCVIS226B5FQ"),org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Reports.Scheduler::Task.Report:fileNameFormat-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd4G2OMFIOPJCM3D4TPKXW666GGE"),"fileNameFormat",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFUUZCUIJQVFAVNV5YXV6YMNEEM"),org.radixware.kernel.common.enums.EValType.STR,null,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("yyyy_MM_dd_hh_mm_ss")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Reports.Scheduler::Task.Report:format-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdKUWC4G6GVRAGVCE5I7FKSDNGYQ"),"format",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMP3F64XJNVGZNDXWKXJQPLLQHE"),org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("acs4NSZFZYJC5AHVA3VWFI5QA3ECE"),null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Reports.Scheduler::Task.Report:reportClassTitle-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdEVCTPPHKUNHOPLJM5NYNAM7X4E"),"reportClassTitle",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsK4FTVFBHPZBBHJEAHTF7ZISDZ4"),org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Reports.Scheduler::Task.Report:xslt-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdGELJ4T5XEBFILL4T4CEWFKNSKQ"),"xslt",null,org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Reports.Scheduler::Task.Report:xsltTitle-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdBYE35BUC3NAP7A3TPKHQ42MGRE"),"xsltTitle",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMRQOLPVLS5FTZO2Z7LKVPO25OI"),org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Reports.Scheduler::Task.Report:xsd-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdRKNFURILVBG2ZALIP753LRYBCI"),"xsd",null,org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Reports.Scheduler::Task.Report:xsdTitle-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdRKMVBC6O5JFTNKSPF7F2AH465M"),"xsdTitle",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4CPFLZVBENGAJLYBY4QL6YMVVI"),org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Reports.Scheduler::Task.Report:postOsCommand-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdM54OFNWHO5CN5I3GKQ3JFMFRUM"),"postOsCommand",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAJCV7J3URVF4DDL3AZDWLNM4AI"),org.radixware.kernel.common.enums.EValType.STR,null,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Reports.Scheduler::Task.Report:msdlTitle-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdEIRJGNE5NBHRFHF4FUB3WVUQLU"),"msdlTitle",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTPPDSJ4OMZBQPDVBY7C6OGHTQM"),org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Reports.Scheduler::Task.Report:params-User Property*/

								new org.radixware.kernel.server.meta.clazzes.RadUserPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pru6QWZVOCXGZBXHA6BFEQQRVSVVY"),"params",null,org.radixware.kernel.common.enums.EValType.CLOB,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("tblWZB7K4HLJPOBDCIUAALOMT5GDM"),false,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Reports.Scheduler::Task.Report:lag-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdRMULELQWFVC25BIM7LQOOSZKRM"),"lag",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSIUW7JU6TBHVDNQR3DWPC4YKPQ"),org.radixware.kernel.common.enums.EValType.INT,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Reports.Scheduler::Task.Report:parametersBinding-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdLG3TIEHK3VHBJEDR544Z3AA224"),"parametersBinding",null,org.radixware.kernel.common.enums.EValType.XML,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Reports.Scheduler::Task.Report:msdl-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdIJWUYV3RDJDSTFF6BERXPEHXMI"),"msdl",null,org.radixware.kernel.common.enums.EValType.XML,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Reports.Scheduler::Task.Report:writeReportFunction-User Property*/

								new org.radixware.kernel.server.meta.clazzes.RadUserRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pru3Q2CZK3W3ZDTHBO4JIOPH7FBQI"),"writeReportFunction",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3KAJLIKZC5FWPFLQK2EYEYRD3Y"),org.radixware.kernel.common.enums.EValType.OBJECT,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("tblWZB7K4HLJPOBDCIUAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("acl7AVTV4I7RBEZ7H6KYFSKQMSFDM"),false,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Reports.Scheduler::Task.Report:prepareParamsFunction-User Property*/

								new org.radixware.kernel.server.meta.clazzes.RadUserRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruA2Q7XRP4HJC2HNDXTLJKQLXV6Y"),"prepareParamsFunction",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPZY3CRPQA5AGDFCI5VRX5CCVIU"),org.radixware.kernel.common.enums.EValType.OBJECT,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("tblWZB7K4HLJPOBDCIUAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aclRGDQOREUGZAJXEKEZ4WHSU2JQA"),false,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Reports.Scheduler::Task.Report:calcFileName-User Property*/

								new org.radixware.kernel.server.meta.clazzes.RadUserRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pru33DBBUK5CJGEFEAZ7VOH5BDRG4"),"calcFileName",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2PV5Q3BPTZETXHS2NLKJJ6W6LA"),org.radixware.kernel.common.enums.EValType.OBJECT,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("tblWZB7K4HLJPOBDCIUAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aclFGNKGM6TXFENBP5R7AXJAHCD2M"),false,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Reports.Scheduler::Task.Report:encoding-User Property*/

								new org.radixware.kernel.server.meta.clazzes.RadUserPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruJGBA6I4VLNCWPH7HRQCGQLXVNE"),"encoding",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMXUZ6XXHJZEQLOPLPP2J3GVK4I"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("tblWZB7K4HLJPOBDCIUAALOMT5GDM"),false,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Reports.Scheduler::Task.Report:adjustFileName-User Property*/

								new org.radixware.kernel.server.meta.clazzes.RadUserRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruEYFORUCAPRCG7JHYPXNFTSC3XI"),"adjustFileName",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCGG2VOWTNZFMJGJ26LALINUPQY"),org.radixware.kernel.common.enums.EValType.OBJECT,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("tblWZB7K4HLJPOBDCIUAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("acl57AB3P4FN5BSBB5SKUEYHPQWHI"),false,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Reports.Scheduler::Task.Report:isMultiFile-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdK7ROH6D55ZADZBT5QWUJBOSJJ4"),"isMultiFile",null,org.radixware.kernel.common.enums.EValType.BOOL,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Reports.Scheduler::Task.Report:counter-User Property*/

								new org.radixware.kernel.server.meta.clazzes.RadUserRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pru4GCLHE2775BXNNVJ7X64SX7K6M"),"counter",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVCVY7R4OBZEMDF37ZND6MPQOTE"),org.radixware.kernel.common.enums.EValType.OBJECT,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("tblWZB7K4HLJPOBDCIUAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aec44TUT3JSR5BPTNBRF3PO2HF6OU"),false,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Reports.Scheduler::Task.Report:calcPdfSecurityOptions-User Property*/

								new org.radixware.kernel.server.meta.clazzes.RadUserRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruIHUHMW56IVAN5DTG3BBIDR7HUQ"),"calcPdfSecurityOptions",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQEZ5JHCRGBENXMP3GT6NFZJQUM"),org.radixware.kernel.common.enums.EValType.OBJECT,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("tblWZB7K4HLJPOBDCIUAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("acl5CE6DSMOGVFXZHH2RMLQBDM4II"),false,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Reports.Scheduler::Task.Report:wasGenerationError-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdTR253IWVZJFA5PA6RVFY5P6QXE"),"wasGenerationError",null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Reports.Scheduler::Task.Report:failOnGenerationError-User Property*/

								new org.radixware.kernel.server.meta.clazzes.RadUserPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruENQ5NCUPU5C53ALQLTU72TZD7Q"),"failOnGenerationError",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGFEJWWPIGJA3PPNF3MMCRYZWMM"),org.radixware.kernel.common.enums.EValType.BOOL,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DEFINE_ALWAYS,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("1")),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblWZB7K4HLJPOBDCIUAALOMT5GDM"),false,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Reports.Scheduler::Task.Report:columnsSettingsDocument-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdRU7U4H2AABER5KAGIAC5X2URPI"),"columnsSettingsDocument",null,org.radixware.kernel.common.enums.EValType.XML,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE)
						},

						/*Radix::Reports.Scheduler::Task.Report:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthCVICCNQ5IJE25PKGOLZVB6Y54E"),"afterRead",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthDDBQZOW5KVCJ5IIEDHG4ESFYFA"),"getFile",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("dir",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprY57WKFDDLVBXNLUDAP36UWCJZE")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("fileNameFormat",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprZHK4XZNJPFGDXMVAVFULYG4MI4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("format",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr5TADIJD64ZBD7GMASFINWWO4E4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("paramId2Value",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprOECH72AWRBAKDCTLXNT7CWIPKU"))
								},org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthXO2LLRG6VBAB5JFPWNVPA7AN74"),"save",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthPT5PMP2WVZGUDB7OTLOE2SBWKE"),"beforeUpdate",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthcmdVACIGPSCDZCMTFEAG4CITMDXSA"),"generateReport",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("input",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprJDUAXJSFWNFFNM66LQBNPNRX4Q")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("newPropValHandlersById",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprLRP427PA6ZBTNLSKASMNNESOCA"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthTWMWCLYKXJAZ5ENQXNABT2UAMY"),"generateToStream",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("outputStream",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprBEPIKSXNQFDHVBOH656WYFU7UQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("report",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprXJFKGT4TRVGJJDTTCCBQNVZLUQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("paramId2Value",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr5MWRAXTASZGVFAVJWYZFZ46UFM"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthZ6BYZN6SYRAG7ACAHIE6QVHLQQ"),"parseParams",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,null,org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth4DJX5TDMMRGONM7QDFS3TWRS2U"),"execPostOsCommand",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("reportFile",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprDMKUU6UXWFFNZPJCG4XX4FWV2Y"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthN36NOGMSW5EGZIX2JAVCBWM2RU"),"execute",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("prevExecTime",org.radixware.kernel.common.enums.EValType.DATE_TIME,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr7ELC75KQKBEXDNAOMHY3TUIOPQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("curExecTime",org.radixware.kernel.common.enums.EValType.DATE_TIME,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprKUIZZRVBJJCSFCDDG53D7FXNGQ"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthFMXUFM5J25E4TNM4PR73F6V3E4"),"beforeCreate",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("src",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprK3W4KG57RBAFJDXRPYVPDS7GYE"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthCKKUF2DWONEZ3DRYMIKBVAJDKQ"),"getLagTime",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("dateTime",org.radixware.kernel.common.enums.EValType.DATE_TIME,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprARU3INO3EJALBCLLRK76BIQT3U"))
								},org.radixware.kernel.common.enums.EValType.DATE_TIME),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth6MXY6NXCG5FENER2I7MAVEY3QI"),"beforeCreateOnImport",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("xTask",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprFRORQWXRWNGFLKZPKRFYAZ3R7E"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthOSO4AOLJRVBF3LKXT6WE35MP3I"),"loadParams",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthIQJW24YT6JEONHWBW6UDE7JSJY"),"generateToStream",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("outputStream",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprWV5YB3RLVRDDPLI7LYAWU54ZZA")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("prevExecTime",org.radixware.kernel.common.enums.EValType.DATE_TIME,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprYAWK3MDPMRENDGNIQW5WAVAULI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("curExecTime",org.radixware.kernel.common.enums.EValType.DATE_TIME,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprFSHLWDH7TRDUZGRPHKJRZNTWZA")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("params",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprP4VE3S37C5FHLBT43D4WLVXGBM"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth5Z7K4M7KWRABVCHA4XP6MFR43M"),"calcParams",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("report",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr6EOK43GKNRELVNI3C4MM6S3NSM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("prevExecTime",org.radixware.kernel.common.enums.EValType.DATE_TIME,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprOCUQ64XIHJHJPMD4NWHR67CTXI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("curExecTime",org.radixware.kernel.common.enums.EValType.DATE_TIME,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprLPDEFNKFDZHLDGE46XWYLIT2L4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("params",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprJOHHPO4QQVB5XHN7QWXKNB3MXM"))
								},org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthPITQGS6DZNG2BDPPDWIBFUVQRI"),"generateToDir",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("prevExecTime",org.radixware.kernel.common.enums.EValType.DATE_TIME,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprVSQPOVMDIRC2RJUZH6MJGI3ZYA")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("curExecTime",org.radixware.kernel.common.enums.EValType.DATE_TIME,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprWJUFP6QA6RBZTIBRG2YO2GNMOA")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("params",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr5NR65VDBZRCE7LAX3CFBXCYPGY"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthKEMKYMCJPNHTVFLBMCL3OCHGLA"),"adjustFileName",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("report",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprDJNRS3MVKBANZETEDJBYXM6XRE")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("fileName",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprIUVTDVN5LNHCJCKTDJUZUULNQA"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth7SFFLRFPJFCAZGQ5543T7PWVLQ"),"getReportInstance",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthRRPMIXBE4FAWFISK5GNUQI43PE"),"generateToDir",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("dir",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprN55UCTWS5BFY5MVV2PX4LHCX7Q")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("report",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprZUCLDS4SPZGW5GWN7ODULVAQUI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("paramId2Value",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprPV3H6TEWEJFJJKOXJCDRN4MWKY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("clientFileDir",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprGLCOPIJKCNBNHEV3F6DHNXIL6E"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthKDB4STL65BA6LKWD3SDNE72VOQ"),"scheduleAsyncJob",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("time",org.radixware.kernel.common.enums.EValType.DATE_TIME,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprG6AOH6N2VRCYRBRR225Y7JQGOI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("params",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprNQ5UMAOYQVCZBDADUJ6G5QUAZM"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthGCIOZ3GADJH33M6OOGRNBAFTLE"),"executeAsyncJob",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("taskId",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprOM3XQIK2X5EMTKDOCDB2TTEVRY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("params",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr7FSUSBSX2JEQHIGTD6GCVJ3CFA"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthZI4YKW2O6ZEU7BT7FGCHAD3ZSA"),"execute",false,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("prevExecTime",org.radixware.kernel.common.enums.EValType.DATE_TIME,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr7ELC75KQKBEXDNAOMHY3TUIOPQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("curExecTime",org.radixware.kernel.common.enums.EValType.DATE_TIME,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprKUIZZRVBJJCSFCDDG53D7FXNGQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("params",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprDVPIDYXGBJFMXG5WNVOT34E73Q"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthKGQSXNEDNRCNTLI5TBHXE62D6U"),"refineStatusOnComplete",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,null)
						},
						null,
						null,null,null,false);
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta_adcH3IVDHMMYNFDXKC5E7VBS3BDKI = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("adcH3IVDHMMYNFDXKC5E7VBS3BDKI"),"FileController",null,

						org.radixware.kernel.common.enums.EClassType.APPLICATION,
						null,
						null,
						null,

						/*Radix::Reports.Scheduler::Task.Report:FileController:Properties-Properties*/
						null,

						/*Radix::Reports.Scheduler::Task.Report:FileController:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthMMHPPAI5T5EFHOO5OTAQN4DSWI"),"adjustFileName",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("report",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprWINVBAUXXBC4BHWSB5LAHNKPBI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("fileName",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprZ6YW23ZUBNESPCJMDCR33MH2TM"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthHCFFTVMPBFBMVJOMMDZMTW4LWU"),"afterCloseFile",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("report",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprRZDJMNZQIFHYBJSMZ7SRALAOPM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("file",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprWTYYOMRWGNHO5OM3VJFQ7G7J6I"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthPQ6T4E3WPFELTDJAEYHOVJIXMU"),"afterCreateFile",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("report",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprNPVZ27ACOBG6TCOJZ3WWEMCOTQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("file",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprONLQ2LPLTFCJJF42SHBW7GJPKQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("outputStream",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr7WBIQL2ZBVFORN4LQHMWZPXRGY"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthEQWZZHVBAZGUTNKSUVXAIQ4ZPI"),"beforeCloseFile",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("report",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr3JPNFMJYH5ARPOSDKXS3GJ6FJQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("file",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr7GJCVXLQBRDEVFBQG3POBRQYQM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("outputStream",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprRC7DLGLAAVCCHLYRMVB5OA77GI"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthNMVOW6LIMVH3LA5FQV52TIFPOI"),"getDirectory",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthJGBQJZ6DIJFYTLMSP7IBGDV72E"),"FileController",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("dir",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprZPCW44DMDRCIREVHMP2DEBL6OY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("clientDirName",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprDHF3LIJI7FBRNGVYILUC2SQPXA"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE)
						},
						null,
						null,null,null,false);
}

/* Radix::Reports.Scheduler::Task.Report - Desktop Executable*/

/*Radix::Reports.Scheduler::Task.Report-Application Class*/

package org.radixware.ads.Reports.Scheduler.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report")
public interface Task.Report   extends org.radixware.ads.Scheduling.explorer.Task.Atomic  {











































































































































































































































































































































































































	/*Radix::Reports.Scheduler::Task.Report:fileNameFormat:fileNameFormat-Presentation Property*/


	public class FileNameFormat extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public FileNameFormat(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:fileNameFormat:fileNameFormat")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:fileNameFormat:fileNameFormat")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public FileNameFormat getFileNameFormat();
	/*Radix::Reports.Scheduler::Task.Report:reportClassGuid:reportClassGuid-Presentation Property*/


	public class ReportClassGuid extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public ReportClassGuid(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:reportClassGuid:reportClassGuid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:reportClassGuid:reportClassGuid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ReportClassGuid getReportClassGuid();
	/*Radix::Reports.Scheduler::Task.Report:xsltTitle:xsltTitle-Presentation Property*/


	public class XsltTitle extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public XsltTitle(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:xsltTitle:xsltTitle")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:xsltTitle:xsltTitle")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public XsltTitle getXsltTitle();
	/*Radix::Reports.Scheduler::Task.Report:msdlTitle:msdlTitle-Presentation Property*/


	public class MsdlTitle extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public MsdlTitle(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:msdlTitle:msdlTitle")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:msdlTitle:msdlTitle")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public MsdlTitle getMsdlTitle();
	/*Radix::Reports.Scheduler::Task.Report:reportClassTitle:reportClassTitle-Presentation Property*/


	public class ReportClassTitle extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public ReportClassTitle(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:reportClassTitle:reportClassTitle")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:reportClassTitle:reportClassTitle")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ReportClassTitle getReportClassTitle();
	/*Radix::Reports.Scheduler::Task.Report:xslt:xslt-Presentation Property*/


	public class Xslt extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public Xslt(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:xslt:xslt")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:xslt:xslt")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Xslt getXslt();
	/*Radix::Reports.Scheduler::Task.Report:msdl:msdl-Presentation Property*/


	public class Msdl extends org.radixware.kernel.common.client.models.items.properties.PropertyXml{
		public Msdl(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Class<org.radixware.schemas.reports.ReportMsdlDocument> getValClass(){
			return org.radixware.schemas.reports.ReportMsdlDocument.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.schemas.reports.ReportMsdlDocument dummy = x == null ? null : (org.radixware.schemas.reports.ReportMsdlDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),(org.apache.xmlbeans.XmlObject)x,org.radixware.schemas.reports.ReportMsdlDocument.class);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:msdl:msdl")
		public  org.radixware.schemas.reports.ReportMsdlDocument getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:msdl:msdl")
		public   void setValue(org.radixware.schemas.reports.ReportMsdlDocument val) {
			Value = val;
		}
	}
	public Msdl getMsdl();
	/*Radix::Reports.Scheduler::Task.Report:isMultiFile:isMultiFile-Presentation Property*/


	public class IsMultiFile extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public IsMultiFile(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:isMultiFile:isMultiFile")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:isMultiFile:isMultiFile")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public IsMultiFile getIsMultiFile();
	/*Radix::Reports.Scheduler::Task.Report:format:format-Presentation Property*/


	public class Format extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public Format(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.kernel.common.enums.EReportExportFormat dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EReportExportFormat ? (org.radixware.kernel.common.enums.EReportExportFormat)x : org.radixware.kernel.common.enums.EReportExportFormat.getForValue((Str)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.STR)));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.kernel.common.enums.EReportExportFormat> getValClass(){
			return org.radixware.kernel.common.enums.EReportExportFormat.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.enums.EReportExportFormat dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EReportExportFormat ? (org.radixware.kernel.common.enums.EReportExportFormat)x : org.radixware.kernel.common.enums.EReportExportFormat.getForValue((Str)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.STR)));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:format:format")
		public  org.radixware.kernel.common.enums.EReportExportFormat getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:format:format")
		public   void setValue(org.radixware.kernel.common.enums.EReportExportFormat val) {
			Value = val;
		}
	}
	public Format getFormat();
	/*Radix::Reports.Scheduler::Task.Report:parametersBinding:parametersBinding-Presentation Property*/


	public class ParametersBinding extends org.radixware.kernel.common.client.models.items.properties.PropertyXml{
		public ParametersBinding(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Class<org.radixware.schemas.reports.ParametersBindingDocument> getValClass(){
			return org.radixware.schemas.reports.ParametersBindingDocument.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.schemas.reports.ParametersBindingDocument dummy = x == null ? null : (org.radixware.schemas.reports.ParametersBindingDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),(org.apache.xmlbeans.XmlObject)x,org.radixware.schemas.reports.ParametersBindingDocument.class);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:parametersBinding:parametersBinding")
		public  org.radixware.schemas.reports.ParametersBindingDocument getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:parametersBinding:parametersBinding")
		public   void setValue(org.radixware.schemas.reports.ParametersBindingDocument val) {
			Value = val;
		}
	}
	public ParametersBinding getParametersBinding();
	/*Radix::Reports.Scheduler::Task.Report:postOsCommand:postOsCommand-Presentation Property*/


	public class PostOsCommand extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public PostOsCommand(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:postOsCommand:postOsCommand")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:postOsCommand:postOsCommand")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public PostOsCommand getPostOsCommand();
	/*Radix::Reports.Scheduler::Task.Report:dir:dir-Presentation Property*/


	public class Dir extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public Dir(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:dir:dir")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:dir:dir")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Dir getDir();
	/*Radix::Reports.Scheduler::Task.Report:xsdTitle:xsdTitle-Presentation Property*/


	public class XsdTitle extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public XsdTitle(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:xsdTitle:xsdTitle")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:xsdTitle:xsdTitle")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public XsdTitle getXsdTitle();
	/*Radix::Reports.Scheduler::Task.Report:xsd:xsd-Presentation Property*/


	public class Xsd extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public Xsd(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:xsd:xsd")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:xsd:xsd")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Xsd getXsd();
	/*Radix::Reports.Scheduler::Task.Report:lag:lag-Presentation Property*/


	public class Lag extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public Lag(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:lag:lag")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:lag:lag")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public Lag getLag();
	/*Radix::Reports.Scheduler::Task.Report:columnsSettingsDocument:columnsSettingsDocument-Presentation Property*/


	public class ColumnsSettingsDocument extends org.radixware.kernel.common.client.models.items.properties.PropertyXml{
		public ColumnsSettingsDocument(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Class<org.radixware.schemas.reports.ColumnSettingsDocument> getValClass(){
			return org.radixware.schemas.reports.ColumnSettingsDocument.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.schemas.reports.ColumnSettingsDocument dummy = x == null ? null : (org.radixware.schemas.reports.ColumnSettingsDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),(org.apache.xmlbeans.XmlObject)x,org.radixware.schemas.reports.ColumnSettingsDocument.class);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:columnsSettingsDocument:columnsSettingsDocument")
		public  org.radixware.schemas.reports.ColumnSettingsDocument getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:columnsSettingsDocument:columnsSettingsDocument")
		public   void setValue(org.radixware.schemas.reports.ColumnSettingsDocument val) {
			Value = val;
		}
	}
	public ColumnsSettingsDocument getColumnsSettingsDocument();
	/*Radix::Reports.Scheduler::Task.Report:calcFileName:calcFileName-Presentation Property*/


	public class CalcFileName extends org.radixware.kernel.common.client.models.items.properties.PropertyObject{
		public CalcFileName(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
			super(model,def);
		}
		@Override
		public 
		org.radixware.ads.UserFunc.explorer.UserFunc.UserFunc_DefaultModel openEntityModel()  throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{
		     return (org.radixware.ads.UserFunc.explorer.UserFunc.UserFunc_DefaultModel)super.openEntityModel();
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.client.types.Reference dummy = ((org.radixware.kernel.common.client.types.Reference)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:calcFileName:calcFileName")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:calcFileName:calcFileName")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public CalcFileName getCalcFileName();
	/*Radix::Reports.Scheduler::Task.Report:writeReportFunction:writeReportFunction-Presentation Property*/


	public class WriteReportFunction extends org.radixware.kernel.common.client.models.items.properties.PropertyObject{
		public WriteReportFunction(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
			super(model,def);
		}
		@Override
		public 
		org.radixware.ads.UserFunc.explorer.UserFunc.UserFunc_DefaultModel openEntityModel()  throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{
		     return (org.radixware.ads.UserFunc.explorer.UserFunc.UserFunc_DefaultModel)super.openEntityModel();
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.client.types.Reference dummy = ((org.radixware.kernel.common.client.types.Reference)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:writeReportFunction:writeReportFunction")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:writeReportFunction:writeReportFunction")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public WriteReportFunction getWriteReportFunction();
	/*Radix::Reports.Scheduler::Task.Report:counter:counter-Presentation Property*/


	public class Counter extends org.radixware.kernel.common.client.models.items.properties.PropertyObject{
		public Counter(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
			super(model,def);
		}
		@Override
		public 
		org.radixware.ads.Utils.explorer.Counter.Counter_DefaultModel openEntityModel()  throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{
		     return (org.radixware.ads.Utils.explorer.Counter.Counter_DefaultModel)super.openEntityModel();
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.client.types.Reference dummy = ((org.radixware.kernel.common.client.types.Reference)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:counter:counter")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:counter:counter")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public Counter getCounter();
	/*Radix::Reports.Scheduler::Task.Report:params:params-Presentation Property*/


	public class Params extends org.radixware.kernel.common.client.models.items.properties.PropertyClob{
		public Params(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:params:params")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:params:params")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Params getParams();
	/*Radix::Reports.Scheduler::Task.Report:prepareParamsFunction:prepareParamsFunction-Presentation Property*/


	public class PrepareParamsFunction extends org.radixware.kernel.common.client.models.items.properties.PropertyObject{
		public PrepareParamsFunction(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
			super(model,def);
		}
		@Override
		public 
		org.radixware.ads.UserFunc.explorer.UserFunc.UserFunc_DefaultModel openEntityModel()  throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{
		     return (org.radixware.ads.UserFunc.explorer.UserFunc.UserFunc_DefaultModel)super.openEntityModel();
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.client.types.Reference dummy = ((org.radixware.kernel.common.client.types.Reference)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:prepareParamsFunction:prepareParamsFunction")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:prepareParamsFunction:prepareParamsFunction")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public PrepareParamsFunction getPrepareParamsFunction();
	/*Radix::Reports.Scheduler::Task.Report:failOnGenerationError:failOnGenerationError-Presentation Property*/


	public class FailOnGenerationError extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public FailOnGenerationError(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:failOnGenerationError:failOnGenerationError")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:failOnGenerationError:failOnGenerationError")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public FailOnGenerationError getFailOnGenerationError();
	/*Radix::Reports.Scheduler::Task.Report:adjustFileName:adjustFileName-Presentation Property*/


	public class AdjustFileName extends org.radixware.kernel.common.client.models.items.properties.PropertyObject{
		public AdjustFileName(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
			super(model,def);
		}
		@Override
		public 
		org.radixware.ads.UserFunc.explorer.UserFunc.UserFunc_DefaultModel openEntityModel()  throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{
		     return (org.radixware.ads.UserFunc.explorer.UserFunc.UserFunc_DefaultModel)super.openEntityModel();
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.client.types.Reference dummy = ((org.radixware.kernel.common.client.types.Reference)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:adjustFileName:adjustFileName")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:adjustFileName:adjustFileName")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public AdjustFileName getAdjustFileName();
	/*Radix::Reports.Scheduler::Task.Report:calcPdfSecurityOptions:calcPdfSecurityOptions-Presentation Property*/


	public class CalcPdfSecurityOptions extends org.radixware.kernel.common.client.models.items.properties.PropertyObject{
		public CalcPdfSecurityOptions(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
			super(model,def);
		}
		@Override
		public 
		org.radixware.ads.UserFunc.explorer.UserFunc.UserFunc_DefaultModel openEntityModel()  throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{
		     return (org.radixware.ads.UserFunc.explorer.UserFunc.UserFunc_DefaultModel)super.openEntityModel();
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.client.types.Reference dummy = ((org.radixware.kernel.common.client.types.Reference)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:calcPdfSecurityOptions:calcPdfSecurityOptions")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:calcPdfSecurityOptions:calcPdfSecurityOptions")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public CalcPdfSecurityOptions getCalcPdfSecurityOptions();
	/*Radix::Reports.Scheduler::Task.Report:encoding:encoding-Presentation Property*/


	public class Encoding extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public Encoding(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:encoding:encoding")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:encoding:encoding")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Encoding getEncoding();
	public static class ClearXslt extends org.radixware.kernel.common.client.models.items.Command{
		protected ClearXslt(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}

	}

	public static class ClearXsd extends org.radixware.kernel.common.client.models.items.Command{
		protected ClearXsd(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}

	}

	public static class EditXsd extends org.radixware.kernel.common.client.models.items.Command{
		protected EditXsd(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}

	}

	public static class ClearMsdl extends org.radixware.kernel.common.client.models.items.Command{
		protected ClearMsdl(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}

	}

	public static class EditMsdl extends org.radixware.kernel.common.client.models.items.Command{
		protected EditMsdl(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}

	}

	public static class EditXslt extends org.radixware.kernel.common.client.models.items.Command{
		protected EditXslt(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}

	}

	public static class ChooseReport extends org.radixware.kernel.common.client.models.items.Command{
		protected ChooseReport(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}

	}

	public static class GenerateReport extends org.radixware.kernel.common.client.models.items.Command{
		protected GenerateReport(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public void send(org.radixware.schemas.reports.GenerateReportRqDocument input ) throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,input);
			processResponse(response, null);
		}

	}



}

/* Radix::Reports.Scheduler::Task.Report - Desktop Meta*/

/*Radix::Reports.Scheduler::Task.Report-Application Class*/

package org.radixware.ads.Reports.Scheduler.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Task.Report_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::Reports.Scheduler::Task.Report:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("acl3JADSBDYJTOBDCIVAALOMT5GDM"),
			"Radix::Reports.Scheduler::Task.Report",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aclZDLN466RV5FE3BQIWSX56ZAETQ"),
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEDPFYTLQSVHQRAUWLQCGNHJRXU"),null,null,0,

			/*Radix::Reports.Scheduler::Task.Report:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::Reports.Scheduler::Task.Report:reportClassGuid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd5JIP7T5DEJAL3MBQME2GHCEABA"),
						"reportClassGuid",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acl3JADSBDYJTOBDCIVAALOMT5GDM"),
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

						/*Radix::Reports.Scheduler::Task.Report:reportClassGuid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Reports.Scheduler::Task.Report:dir:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdNWADG4CG2REYXN4CIN7C47LMUI"),
						"dir",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsICPTBCHW6RDX7ESCVIS226B5FQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acl3JADSBDYJTOBDCIVAALOMT5GDM"),
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
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Reports.Scheduler::Task.Report:dir:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Reports.Scheduler::Task.Report:fileNameFormat:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd4G2OMFIOPJCM3D4TPKXW666GGE"),
						"fileNameFormat",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFUUZCUIJQVFAVNV5YXV6YMNEEM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acl3JADSBDYJTOBDCIVAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("yyyy_MM_dd_hh_mm_ss"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						true,false,
						false,
						true,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("cpeMGEX4E4QHRAAHDYS2POFXW4P7Y"),
						false,

						/*Radix::Reports.Scheduler::Task.Report:fileNameFormat:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Reports.Scheduler::Task.Report:format:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdKUWC4G6GVRAGVCE5I7FKSDNGYQ"),
						"format",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMP3F64XJNVGZNDXWKXJQPLLQHE"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acl3JADSBDYJTOBDCIVAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
						4,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acs4NSZFZYJC5AHVA3VWFI5QA3ECE"),
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

						/*Radix::Reports.Scheduler::Task.Report:format:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acs4NSZFZYJC5AHVA3VWFI5QA3ECE"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.INCLUDE,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("aci3Y3ARXN4IZAONO76HQVJ2NRRUM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aciL63KG5SUZRCGBAIMS6WUX5IHAY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aciRO6YJVU2UJARPETZGVPPEDVGN4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aciKNDWK7PMWRD7JHQ25GV6BV7BEY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aciDWMML6BY4JAXJA35IYPXEMDLZE"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aciW2UXJEKEEJDCTEIMOBGJLBPZVU"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aciIL3YBS33E5GHDLWOROHNBPAC5M")}),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Reports.Scheduler::Task.Report:reportClassTitle:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdEVCTPPHKUNHOPLJM5NYNAM7X4E"),
						"reportClassTitle",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsK4FTVFBHPZBBHJEAHTF7ZISDZ4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acl3JADSBDYJTOBDCIVAALOMT5GDM"),
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
						org.radixware.kernel.common.enums.EEditPossibility.PROGRAMMATICALLY,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Reports.Scheduler::Task.Report:reportClassTitle:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Reports.Scheduler::Task.Report:xslt:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdGELJ4T5XEBFILL4T4CEWFKNSKQ"),
						"xslt",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acl3JADSBDYJTOBDCIVAALOMT5GDM"),
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
						org.radixware.kernel.common.enums.EEditPossibility.PROGRAMMATICALLY,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Reports.Scheduler::Task.Report:xslt:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Reports.Scheduler::Task.Report:xsltTitle:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdBYE35BUC3NAP7A3TPKHQ42MGRE"),
						"xsltTitle",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMRQOLPVLS5FTZO2Z7LKVPO25OI"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acl3JADSBDYJTOBDCIVAALOMT5GDM"),
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
						org.radixware.kernel.common.enums.EEditPossibility.PROGRAMMATICALLY,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Reports.Scheduler::Task.Report:xsltTitle:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Reports.Scheduler::Task.Report:xsd:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdRKNFURILVBG2ZALIP753LRYBCI"),
						"xsd",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acl3JADSBDYJTOBDCIVAALOMT5GDM"),
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
						org.radixware.kernel.common.enums.EEditPossibility.PROGRAMMATICALLY,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Reports.Scheduler::Task.Report:xsd:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Reports.Scheduler::Task.Report:xsdTitle:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdRKMVBC6O5JFTNKSPF7F2AH465M"),
						"xsdTitle",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4CPFLZVBENGAJLYBY4QL6YMVVI"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acl3JADSBDYJTOBDCIVAALOMT5GDM"),
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
						org.radixware.kernel.common.enums.EEditPossibility.PROGRAMMATICALLY,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Reports.Scheduler::Task.Report:xsdTitle:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Reports.Scheduler::Task.Report:postOsCommand:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdM54OFNWHO5CN5I3GKQ3JFMFRUM"),
						"postOsCommand",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAJCV7J3URVF4DDL3AZDWLNM4AI"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acl3JADSBDYJTOBDCIVAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom(""),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Reports.Scheduler::Task.Report:postOsCommand:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Reports.Scheduler::Task.Report:msdlTitle:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdEIRJGNE5NBHRFHF4FUB3WVUQLU"),
						"msdlTitle",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTPPDSJ4OMZBQPDVBY7C6OGHTQM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acl3JADSBDYJTOBDCIVAALOMT5GDM"),
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
						org.radixware.kernel.common.enums.EEditPossibility.PROGRAMMATICALLY,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Reports.Scheduler::Task.Report:msdlTitle:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Reports.Scheduler::Task.Report:params:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pru6QWZVOCXGZBXHA6BFEQQRVSVVY"),
						"params",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acl3JADSBDYJTOBDCIVAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.CLOB,
						org.radixware.kernel.common.enums.EPropNature.USER,
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

						/*Radix::Reports.Scheduler::Task.Report:params:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Reports.Scheduler::Task.Report:lag:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdRMULELQWFVC25BIM7LQOOSZKRM"),
						"lag",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSIUW7JU6TBHVDNQR3DWPC4YKPQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acl3JADSBDYJTOBDCIVAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.INT,
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

						/*Radix::Reports.Scheduler::Task.Report:lag:PropertyPresentation:Edit Options:-Edit Mask Time Interval*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskTimeInterval(1000L,"mm:ss",null,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Reports.Scheduler::Task.Report:parametersBinding:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdLG3TIEHK3VHBJEDR544Z3AA224"),
						"parametersBinding",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acl3JADSBDYJTOBDCIVAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.XML,
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
						null,
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Reports.Scheduler::Task.Report:msdl:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdIJWUYV3RDJDSTFF6BERXPEHXMI"),
						"msdl",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acl3JADSBDYJTOBDCIVAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.XML,
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
						null,
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Reports.Scheduler::Task.Report:writeReportFunction:PropertyPresentation-Object Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pru3Q2CZK3W3ZDTHBO4JIOPH7FBQI"),
						"writeReportFunction",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acl3JADSBDYJTOBDCIVAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.OBJECT,
						org.radixware.kernel.common.enums.EPropNature.USER,
						5,
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
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acl7AVTV4I7RBEZ7H6KYFSKQMSFDM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr7IKMB24XZHOBDCMTAALOMT5GDM")},
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr7IKMB24XZHOBDCMTAALOMT5GDM")},
						null,
						0L,
						0L,false,false),

					/*Radix::Reports.Scheduler::Task.Report:prepareParamsFunction:PropertyPresentation-Object Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruA2Q7XRP4HJC2HNDXTLJKQLXV6Y"),
						"prepareParamsFunction",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acl3JADSBDYJTOBDCIVAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.OBJECT,
						org.radixware.kernel.common.enums.EPropNature.USER,
						5,
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
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclRGDQOREUGZAJXEKEZ4WHSU2JQA"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr7IKMB24XZHOBDCMTAALOMT5GDM")},
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr7IKMB24XZHOBDCMTAALOMT5GDM")},
						null,
						0L,
						0L,false,false),

					/*Radix::Reports.Scheduler::Task.Report:calcFileName:PropertyPresentation-Object Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pru33DBBUK5CJGEFEAZ7VOH5BDRG4"),
						"calcFileName",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2PV5Q3BPTZETXHS2NLKJJ6W6LA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acl3JADSBDYJTOBDCIVAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.OBJECT,
						org.radixware.kernel.common.enums.EPropNature.USER,
						4,
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
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclFGNKGM6TXFENBP5R7AXJAHCD2M"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr7IKMB24XZHOBDCMTAALOMT5GDM")},
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr7IKMB24XZHOBDCMTAALOMT5GDM")},
						null,
						0L,
						0L,false,false),

					/*Radix::Reports.Scheduler::Task.Report:encoding:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruJGBA6I4VLNCWPH7HRQCGQLXVNE"),
						"encoding",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMXUZ6XXHJZEQLOPLPP2J3GVK4I"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acl3JADSBDYJTOBDCIVAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.USER,
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

						/*Radix::Reports.Scheduler::Task.Report:encoding:PropertyPresentation:Edit Options:-Edit Mask List*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskList(),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJL7Q4HEQMFEM3J3QNKHFH3FI54"),
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Reports.Scheduler::Task.Report:adjustFileName:PropertyPresentation-Object Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruEYFORUCAPRCG7JHYPXNFTSC3XI"),
						"adjustFileName",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCGG2VOWTNZFMJGJ26LALINUPQY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acl3JADSBDYJTOBDCIVAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.OBJECT,
						org.radixware.kernel.common.enums.EPropNature.USER,
						4,
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
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acl57AB3P4FN5BSBB5SKUEYHPQWHI"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr7IKMB24XZHOBDCMTAALOMT5GDM")},
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr7IKMB24XZHOBDCMTAALOMT5GDM")},
						null,
						0L,
						0L,false,false),

					/*Radix::Reports.Scheduler::Task.Report:isMultiFile:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdK7ROH6D55ZADZBT5QWUJBOSJJ4"),
						"isMultiFile",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acl3JADSBDYJTOBDCIVAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.BOOL,
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

						/*Radix::Reports.Scheduler::Task.Report:isMultiFile:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("acl3JADSBDYJTOBDCIVAALOMT5GDM"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Reports.Scheduler::Task.Report:counter:PropertyPresentation-Object Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pru4GCLHE2775BXNNVJ7X64SX7K6M"),
						"counter",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVCVY7R4OBZEMDF37ZND6MPQOTE"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acl3JADSBDYJTOBDCIVAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.OBJECT,
						org.radixware.kernel.common.enums.EPropNature.USER,
						4,
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
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec44TUT3JSR5BPTNBRF3PO2HF6OU"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl44TUT3JSR5BPTNBRF3PO2HF6OU"),
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprPGEGCGCUIFBOPCIJ3GH2XBPWKA")},
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprPGEGCGCUIFBOPCIJ3GH2XBPWKA")},
						null,
						0L,
						0L,false,false),

					/*Radix::Reports.Scheduler::Task.Report:calcPdfSecurityOptions:PropertyPresentation-Object Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruIHUHMW56IVAN5DTG3BBIDR7HUQ"),
						"calcPdfSecurityOptions",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acl3JADSBDYJTOBDCIVAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.OBJECT,
						org.radixware.kernel.common.enums.EPropNature.USER,
						5,
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
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acl5CE6DSMOGVFXZHH2RMLQBDM4II"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr7IKMB24XZHOBDCMTAALOMT5GDM")},
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr7IKMB24XZHOBDCMTAALOMT5GDM")},
						null,
						0L,
						0L,false,false),

					/*Radix::Reports.Scheduler::Task.Report:failOnGenerationError:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruENQ5NCUPU5C53ALQLTU72TZD7Q"),
						"failOnGenerationError",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGFEJWWPIGJA3PPNF3MMCRYZWMM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acl3JADSBDYJTOBDCIVAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.BOOL,
						org.radixware.kernel.common.enums.EPropNature.USER,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("1"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Reports.Scheduler::Task.Report:failOnGenerationError:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("acl3JADSBDYJTOBDCIVAALOMT5GDM"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Reports.Scheduler::Task.Report:columnsSettingsDocument:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdRU7U4H2AABER5KAGIAC5X2URPI"),
						"columnsSettingsDocument",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acl3JADSBDYJTOBDCIVAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.XML,
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
						null,
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false)
			},
			new org.radixware.kernel.common.client.meta.RadCommandDef[]{
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::Reports.Scheduler::Task.Report:chooseReport-Property Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdPT4TV2AFZFHBRBZ2H6OT4WCSAU"),
						"chooseReport",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acl3JADSBDYJTOBDCIVAALOMT5GDM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJZPU4Y5C5VBCVHXJ3H6ZZVK3AQ"),
						null,
						null,
						org.radixware.kernel.common.enums.ECommandNature.LOCAL,
						true,
						false,
						false,
						org.radixware.kernel.common.enums.ECommandScope.PROPERTY,
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("prdEVCTPPHKUNHOPLJM5NYNAM7X4E")},
						org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS),
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::Reports.Scheduler::Task.Report:editXslt-Property Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdOLAGHY7YANHLXI7BSWV4DIBSTE"),
						"editXslt",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acl3JADSBDYJTOBDCIVAALOMT5GDM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBH7CVHYWFVESXNTKJDTZAIUZFY"),
						null,
						null,
						org.radixware.kernel.common.enums.ECommandNature.LOCAL,
						true,
						false,
						false,
						org.radixware.kernel.common.enums.ECommandScope.PROPERTY,
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("prdBYE35BUC3NAP7A3TPKHQ42MGRE")},
						org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS),
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::Reports.Scheduler::Task.Report:editXsd-Property Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdFP33XLDWZVGD3L6D6I3DEF7RRM"),
						"editXsd",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acl3JADSBDYJTOBDCIVAALOMT5GDM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGVZX6ATPLJBLVFVG4BH6KGNNOU"),
						null,
						null,
						org.radixware.kernel.common.enums.ECommandNature.LOCAL,
						true,
						false,
						false,
						org.radixware.kernel.common.enums.ECommandScope.PROPERTY,
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("prdRKMVBC6O5JFTNKSPF7F2AH465M")},
						org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS),
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::Reports.Scheduler::Task.Report:generateReport-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdVACIGPSCDZCMTFEAG4CITMDXSA"),
						"generateReport",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acl3JADSBDYJTOBDCIVAALOMT5GDM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFIOMMFCELJHWTMXFIQN4F5VW2A"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("img3627K6GYUFGXFJYWMVCBQAPHUA"),
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
					/*Radix::Reports.Scheduler::Task.Report:clearXsd-Property Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdCOY77PVTDREZ3GFJCRMXCL6AFM"),
						"clearXsd",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acl3JADSBDYJTOBDCIVAALOMT5GDM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOVROYKPOMRFNTPCWDSZGV3W2HQ"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("img4F7PNQFL3RDF3ADULVZL3H2IIE"),
						null,
						org.radixware.kernel.common.enums.ECommandNature.LOCAL,
						true,
						false,
						false,
						org.radixware.kernel.common.enums.ECommandScope.PROPERTY,
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("prdRKMVBC6O5JFTNKSPF7F2AH465M")},
						org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS),
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::Reports.Scheduler::Task.Report:clearXslt-Property Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmd6ROEU2EOL5CL3GUCAPFYJOYYXU"),
						"clearXslt",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acl3JADSBDYJTOBDCIVAALOMT5GDM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2JHU3S6AYJCOJJWJN7PSILZJOA"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("img4F7PNQFL3RDF3ADULVZL3H2IIE"),
						null,
						org.radixware.kernel.common.enums.ECommandNature.LOCAL,
						true,
						false,
						false,
						org.radixware.kernel.common.enums.ECommandScope.PROPERTY,
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("prdBYE35BUC3NAP7A3TPKHQ42MGRE")},
						org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS),
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::Reports.Scheduler::Task.Report:editMsdl-Property Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdMCFKI7IZ2FBXXOFLCH7NIK4JZQ"),
						"editMsdl",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acl3JADSBDYJTOBDCIVAALOMT5GDM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsB6HYA7DRNFFG3MLV4SMKXGUXFM"),
						null,
						null,
						org.radixware.kernel.common.enums.ECommandNature.LOCAL,
						true,
						false,
						false,
						org.radixware.kernel.common.enums.ECommandScope.PROPERTY,
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("prdEIRJGNE5NBHRFHF4FUB3WVUQLU"),org.radixware.kernel.common.types.Id.Factory.loadFrom("prdIJWUYV3RDJDSTFF6BERXPEHXMI")},
						org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS),
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::Reports.Scheduler::Task.Report:clearMsdl-Property Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdFPFOPWOAZ5CRXA7RTA6BS7HD3Q"),
						"clearMsdl",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acl3JADSBDYJTOBDCIVAALOMT5GDM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFSFDDIYMWVFXLFXKBDANKBHCS4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("img4F7PNQFL3RDF3ADULVZL3H2IIE"),
						null,
						org.radixware.kernel.common.enums.ECommandNature.LOCAL,
						true,
						false,
						false,
						org.radixware.kernel.common.enums.ECommandScope.PROPERTY,
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("prdEIRJGNE5NBHRFHF4FUB3WVUQLU")},
						org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS)
			},
			null,
			null,
			null,
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprZU2CPFLYVJCXZCWOSIYQPC7P24"),org.radixware.kernel.common.types.Id.Factory.loadFrom("eprOR3IPE5XXRDNFJMHE6BWYV7TZE")},
			true,true,false);
}

/* Radix::Reports.Scheduler::Task.Report - Web Meta*/

/*Radix::Reports.Scheduler::Task.Report-Application Class*/

package org.radixware.ads.Reports.Scheduler.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Task.Report_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::Reports.Scheduler::Task.Report:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("acl3JADSBDYJTOBDCIVAALOMT5GDM"),
			"Radix::Reports.Scheduler::Task.Report",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aclZDLN466RV5FE3BQIWSX56ZAETQ"),
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEDPFYTLQSVHQRAUWLQCGNHJRXU"),null,null,0,
			null,
			null,
			null,
			null,
			null,
			null,
			false,false,false);
}

/* Radix::Reports.Scheduler::Task.Report:Edit - Desktop Meta*/

/*Radix::Reports.Scheduler::Task.Report:Edit-Editor Presentation*/

package org.radixware.ads.Reports.Scheduler.explorer;
public final class Edit_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprZU2CPFLYVJCXZCWOSIYQPC7P24"),
	"Edit",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("eprAIWBLDTSJTOBDCIVAALOMT5GDM"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("acl3JADSBDYJTOBDCIVAALOMT5GDM"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tblWZB7K4HLJPOBDCIUAALOMT5GDM"),
	null,
	null,

	/*Radix::Reports.Scheduler::Task.Report:Edit:Editor Pages-Editor Presentation Pages*/
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

			/*Radix::Reports.Scheduler::Task.Report:Edit:Report-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgILEUM5ETNRH3JPISGFOGCSSA6E"),"Report",org.radixware.kernel.common.types.Id.Factory.loadFrom("acl3JADSBDYJTOBDCIVAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDEPXU7V73VEKJIMMSUYIZPWU6Q"),org.radixware.kernel.common.types.Id.Factory.loadFrom("imgZV433DCBJRFDTFK7PCI7ZJURGU"),new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdNWADG4CG2REYXN4CIN7C47LMUI"),0,1,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd4G2OMFIOPJCM3D4TPKXW666GGE"),0,2,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdKUWC4G6GVRAGVCE5I7FKSDNGYQ"),0,5,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdEVCTPPHKUNHOPLJM5NYNAM7X4E"),0,0,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdBYE35BUC3NAP7A3TPKHQ42MGRE"),0,11,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdRKMVBC6O5JFTNKSPF7F2AH465M"),0,10,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdM54OFNWHO5CN5I3GKQ3JFMFRUM"),0,15,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdEIRJGNE5NBHRFHF4FUB3WVUQLU"),0,12,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("pru3Q2CZK3W3ZDTHBO4JIOPH7FBQI"),0,8,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruA2Q7XRP4HJC2HNDXTLJKQLXV6Y"),0,9,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("pru33DBBUK5CJGEFEAZ7VOH5BDRG4"),0,3,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruJGBA6I4VLNCWPH7HRQCGQLXVNE"),0,6,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruEYFORUCAPRCG7JHYPXNFTSC3XI"),0,4,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("pru4GCLHE2775BXNNVJ7X64SX7K6M"),0,13,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruIHUHMW56IVAN5DTG3BBIDR7HUQ"),0,7,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruENQ5NCUPU5C53ALQLTU72TZD7Q"),0,14,1,false,false)
			},null),

			/*Radix::Reports.Scheduler::Task.Report:Edit:ReportParams-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadCustomEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgOGF2VD3LLZFK7AU2NVYOSZBGYI"),"ReportParams",org.radixware.kernel.common.types.Id.Factory.loadFrom("acl3JADSBDYJTOBDCIVAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNAK55FD4CNBWHAGF5FUM3XQVNM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("imgQENRUP5P6VATVFFTBZ5Y6O6KEU"),null,org.radixware.kernel.common.types.Id.Factory.loadFrom("cepZIDYFRNUVJBNDG677SKDEMAGA4")),

			/*Radix::Reports.Scheduler::Task.Report:Edit:ReportColumns-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadCustomEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgMU6XTA6O2BAGBCKWQLOPIR3HCE"),"ReportColumns",org.radixware.kernel.common.types.Id.Factory.loadFrom("acl3JADSBDYJTOBDCIVAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3YRBO2KCKRCY7CEKTTONQAWTPI"),org.radixware.kernel.common.types.Id.Factory.loadFrom("imgOFJRBBHDTBFYVLDYSQI4VHGH3E"),null,org.radixware.kernel.common.types.Id.Factory.loadFrom("cepTYMUQW5DLBDAZN4WAN5AOKFNSA"))
	},
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgHHRMHELUJTOBDCIVAALOMT5GDM")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgILEUM5ETNRH3JPISGFOGCSSA6E")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgOGF2VD3LLZFK7AU2NVYOSZBGYI")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgMU6XTA6O2BAGBCKWQLOPIR3HCE")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgL6FUZ36EMVEU3BPQ4FLI3TZ6KA"))}
	,

	/*Radix::Reports.Scheduler::Task.Report:Edit:Children-Explorer Items*/
		null
	,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
	null,
	0,
	null,
	(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
	36016,0,0);
}
/* Radix::Reports.Scheduler::Task.Report:Edit:Model - Desktop Executable*/

/*Radix::Reports.Scheduler::Task.Report:Edit:Model-Entity Model Class*/

package org.radixware.ads.Reports.Scheduler.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:Edit:Model")
public class Edit:Model  extends org.radixware.ads.Reports.Scheduler.explorer.Task.Report.Task.Report_DefaultModel.eprAIWBLDTSJTOBDCIVAALOMT5GDM_ModelAdapter {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Edit:Model_mi.rdxMeta; }



	public Edit:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::Reports.Scheduler::Task.Report:Edit:Model:Nested classes-Nested Classes*/

	/*Radix::Reports.Scheduler::Task.Report:Edit:Model:Properties-Properties*/

	/*Radix::Reports.Scheduler::Task.Report:Edit:Model:format-Presentation Property*/




	public class Format extends org.radixware.ads.Reports.Scheduler.explorer.Task.Report.Task.Report_DefaultModel.eprAIWBLDTSJTOBDCIVAALOMT5GDM_ModelAdapter.prdKUWC4G6GVRAGVCE5I7FKSDNGYQ{
		public Format(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.kernel.common.enums.EReportExportFormat dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EReportExportFormat ? (org.radixware.kernel.common.enums.EReportExportFormat)x : org.radixware.kernel.common.enums.EReportExportFormat.getForValue((Str)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.STR)));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.kernel.common.enums.EReportExportFormat> getValClass(){
			return org.radixware.kernel.common.enums.EReportExportFormat.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.enums.EReportExportFormat dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EReportExportFormat ? (org.radixware.kernel.common.enums.EReportExportFormat)x : org.radixware.kernel.common.enums.EReportExportFormat.getForValue((Str)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.STR)));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:Edit:Model:format")
		public published  org.radixware.kernel.common.enums.EReportExportFormat getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:Edit:Model:format")
		public published   void setValue(org.radixware.kernel.common.enums.EReportExportFormat val) {

			internal[format] = val;
			updateReportFieldsVisibility();
		}
	}
	public Format getFormat(){return (Format)getProperty(prdKUWC4G6GVRAGVCE5I7FKSDNGYQ);}

	/*Radix::Reports.Scheduler::Task.Report:Edit:Model:msdl-Presentation Property*/




	public class Msdl extends org.radixware.ads.Reports.Scheduler.explorer.Task.Report.Task.Report_DefaultModel.eprAIWBLDTSJTOBDCIVAALOMT5GDM_ModelAdapter.prdIJWUYV3RDJDSTFF6BERXPEHXMI{
		public Msdl(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Class<org.radixware.schemas.reports.ReportMsdlDocument> getValClass(){
			return org.radixware.schemas.reports.ReportMsdlDocument.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.schemas.reports.ReportMsdlDocument dummy = x == null ? null : (org.radixware.schemas.reports.ReportMsdlDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),(org.apache.xmlbeans.XmlObject)x,org.radixware.schemas.reports.ReportMsdlDocument.class);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:Edit:Model:msdl")
		public  org.radixware.schemas.reports.ReportMsdlDocument getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:Edit:Model:msdl")
		public   void setValue(org.radixware.schemas.reports.ReportMsdlDocument val) {

			internal[msdl] = val;
			msdlTitle.afterModify();
		}
	}
	public Msdl getMsdl(){return (Msdl)getProperty(prdIJWUYV3RDJDSTFF6BERXPEHXMI);}

	/*Radix::Reports.Scheduler::Task.Report:Edit:Model:msdlTitle-Presentation Property*/




	public class MsdlTitle extends org.radixware.ads.Reports.Scheduler.explorer.Task.Report.Task.Report_DefaultModel.eprAIWBLDTSJTOBDCIVAALOMT5GDM_ModelAdapter.prdEIRJGNE5NBHRFHF4FUB3WVUQLU{
		public MsdlTitle(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:Edit:Model:msdlTitle")
		public  Str getValue() {

			if (msdl.Value!=null)
			    return "<defined>";
			else
			    return null;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:Edit:Model:msdlTitle")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public MsdlTitle getMsdlTitle(){return (MsdlTitle)getProperty(prdEIRJGNE5NBHRFHF4FUB3WVUQLU);}












	/*Radix::Reports.Scheduler::Task.Report:Edit:Model:Methods-Methods*/

	/*Radix::Reports.Scheduler::Task.Report:Edit:Model:chooseReport-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:Edit:Model:chooseReport")
	public published  void chooseReport (org.radixware.ads.Reports.Scheduler.explorer.Task.Report.ChooseReport command, org.radixware.kernel.common.types.Id propertyId) {
		final Reports::ChooseReportClassDialog dlg = new Reports::ChooseReportClassDialog(Environment);
		dlg.Model.selectedClassId = Types::Id.Factory.loadFrom(this.reportClassGuid.Value);
		dlg.Model.checkContextClassId = false;

		if (dlg.exec() == Explorer.Qt.Types::QDialog.DialogCode.Accepted.value()) {
		    final String selectedClassGuid = dlg.Model.selectedClassId.toString();
		    if (this.reportClassGuid.Value != selectedClassGuid) {
		        this.reportClassGuid.setValueObject(selectedClassGuid);
		        this.reportClassTitle.setValueObject(dlg.Model.selectedClassTitle);
		        this.parametersBinding.setValueObject(null);
		        this.columnsSettingsDocument.setValueObject(null);
		        updateParameterBinding();
		        updateColumnsSettings();
		        updateEnabledFormats();
		        updateReportFieldsVisibility();
		    }
		}

	}

	/*Radix::Reports.Scheduler::Task.Report:Edit:Model:editXslt-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:Edit:Model:editXslt")
	public published  void editXslt (org.radixware.ads.Reports.Scheduler.explorer.Task.Report.EditXslt command, org.radixware.kernel.common.types.Id propertyId) {
		//if (this..Value==null){
		//    .();
		//    return;
		//}

		final String oldXslt = this.xslt.Value;
		final String rootElementFrom = "Report";
		final String rootElementTo = null;

		final String newXslt = Explorer.Utils::DialogUtils.runXsltEditor(
		        Environment,
		        null,
		        "http://schemas.radixware.org/reports.xsd",
		        this.xsd.Value,
		        oldXslt,
		        rootElementFrom,
		        rootElementTo);

		if (newXslt != null && oldXslt!=newXslt) {
		    this.xslt.Value = newXslt;
		    this.xsltTitle.setValue("<defined>");
		}

	}

	/*Radix::Reports.Scheduler::Task.Report:Edit:Model:editXsd-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:Edit:Model:editXsd")
	public published  void editXsd (org.radixware.ads.Reports.Scheduler.explorer.Task.Report.EditXsd command, org.radixware.kernel.common.types.Id propertyId) {
		org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument xsdDoc = null;
		final String xsd = this.xsd.Value;
		if (xsd != null && !xsd.isEmpty()) {
		    try {
		        xsdDoc = org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Factory.parse(xsd);
		    } catch (Exceptions::XmlException ex) {
		        Explorer.Env::Application.processException(ex);
		    }
		}

		if (xsdDoc == null) {
		    xsdDoc = org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument.Factory.newInstance();
		    xsdDoc.addNewSchema();
		}

		final Explorer.Dialogs::XmlEditorDialog xmlEditor = new XmlEditorDialog(Environment,(Explorer.Qt.Types::QWidget)getEntityView(), "XSD Editor");
		org.apache.xmlbeans.XmlObject xmlObject = xmlEditor.edit(xsdDoc, (Types::Id) null, this.xsd.isReadonly());
		if (xmlObject != null) {
		    this.xsd.Value = xmlObject.xmlText();
		    this.xsdTitle.setValue("<defined>");
		    updateReportFieldsVisibility();
		}

	}

	/*Radix::Reports.Scheduler::Task.Report:Edit:Model:updateReportFieldsVisibility-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:Edit:Model:updateReportFieldsVisibility")
	protected  void updateReportFieldsVisibility () {
		final boolean isXml = (this.format.Value == Reports::ReportExportFormat:XML);
		final boolean isMsdl = (this.format.Value == Reports::ReportExportFormat:MSDL);
		final boolean isCustom = (this.format.Value == Reports::ReportExportFormat:CUSTOM);
		final boolean isCSV = (this.format.Value == Reports::ReportExportFormat:CSV);
		final boolean isTXT = (this.format.Value == Reports::ReportExportFormat:TXT);
		final boolean isPDF = (this.format.Value == Reports::ReportExportFormat:PDF);
		final boolean isMultiFile = this.isMultiFileReport();


		this.xsdTitle.setVisible(isXml);
		this.xsltTitle.setVisible(isXml);
		this.msdlTitle.setVisible(isMsdl);
		this.msdlTitle.setMandatory(isMsdl);
		this.encoding.setVisible(isTXT || isCSV);
		this.writeReportFunction.setVisible(isCustom);
		this.writeReportFunction.setMandatory(isCustom);
		this.calcFileName.setVisible(!isMultiFile);
		this.fileNameFormat.setVisible(!isMultiFile);
		this.adjustFileName.setVisible(isMultiFile);
		this.calcPdfSecurityOptions.setVisible(isPDF);
	}

	/*Radix::Reports.Scheduler::Task.Report:Edit:Model:generateReport-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:Edit:Model:generateReport")
	public published  void generateReport (org.radixware.ads.Reports.Scheduler.explorer.Task.Report.GenerateReport command) {
		try {
		    if (reportClassGuid.Value == null || reportClassGuid.Value.isEmpty()) {
		        Explorer.Env::Application.messageError("Report not defined");
		        return;
		    }

		    final Reports::ReportsXsd:GenerateReportRqDocument requestDoc = Reports::ReportsXsd:GenerateReportRqDocument.Factory.newInstance();
		    final Reports::ReportsXsd:GenerateReportRqDocument.GenerateReportRq request = requestDoc.addNewGenerateReportRq();

		    final String ext = getExt();
		    final java.io.File tempFile = java.io.File.createTempFile("Report-", (ext != null && !ext.isEmpty()) ? "." + ext : "");
		    if (isMultiFile.Value == Bool.TRUE) {
		        tempFile.delete();
		        tempFile.mkdirs();
		    } else {
		        tempFile.deleteOnExit();
		    }
		    request.setFile(tempFile.getAbsolutePath());

		    request.addNewParamValues();

		    command.send(requestDoc);

		    if (!java.awt.GraphicsEnvironment.isHeadless())
		        java.awt.Desktop.getDesktop().open(tempFile);
		} catch (Exceptions::ServiceClientException ex) {
		    Explorer.Env::Application.processException(ex);
		} catch (Exceptions::IOException ex) {
		    Explorer.Env::Application.processException(ex);
		} catch (Exceptions::InterruptedException ex) {
		    return; // cancelled
		}//catch (java.awt.HeadlessException ex){
		//    return;
		//}

	}

	/*Radix::Reports.Scheduler::Task.Report:Edit:Model:clearXsd-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:Edit:Model:clearXsd")
	public published  void clearXsd (org.radixware.ads.Reports.Scheduler.explorer.Task.Report.ClearXsd command, org.radixware.kernel.common.types.Id propertyId) {
		this.xsd.Value = null;
		this.xsdTitle.Value = null;
	}

	/*Radix::Reports.Scheduler::Task.Report:Edit:Model:clearXslt-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:Edit:Model:clearXslt")
	public published  void clearXslt (org.radixware.ads.Reports.Scheduler.explorer.Task.Report.ClearXslt command, org.radixware.kernel.common.types.Id propertyId) {
		this.xslt.Value = null;
		this.xsltTitle.Value = null;
	}

	/*Radix::Reports.Scheduler::Task.Report:Edit:Model:beforeOpenView-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:Edit:Model:beforeOpenView")
	protected published  void beforeOpenView () {
		super.beforeOpenView();
		updateReportFieldsVisibility();
		updateEncodingsList();
	}

	/*Radix::Reports.Scheduler::Task.Report:Edit:Model:clearMsdl-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:Edit:Model:clearMsdl")
	public  void clearMsdl (org.radixware.ads.Reports.Scheduler.explorer.Task.Report.ClearMsdl command, org.radixware.kernel.common.types.Id propertyId) {
		this.msdl.Value = null;
		this.msdlTitle.Value = null;
	}

	/*Radix::Reports.Scheduler::Task.Report:Edit:Model:editMsdl-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:Edit:Model:editMsdl")
	public  void editMsdl (org.radixware.ads.Reports.Scheduler.explorer.Task.Report.EditMsdl command, org.radixware.kernel.common.types.Id propertyId) {
		if (reportClassGuid.Value == null || reportClassGuid.Value.isEmpty()) {
		    Explorer.Env::Application.messageError("Report not defined.");
		    return;
		}

		try {
		    final ReportMsdlEditor dlg = new ReportMsdlEditor(Environment);
		    dlg.Model.reportClassGuid = this.reportClassGuid.Value;

		    if (this.msdl.Value != null) {
		        dlg.Model.reportMsdl = this.msdl.Value;
		    } else {
		        dlg.Model.reportMsdl = Reports::ReportsXsd:ReportMsdlDocument.Factory.newInstance();
		    }

		    if (dlg.exec() == Explorer.Qt.Types::QDialog.DialogCode.Accepted.value()) {
		        this.msdl.Value = dlg.Model.reportMsdl;
		    }
		} catch (Exceptions::Throwable ex) {
		    Explorer.Env::Application.processException(ex);
		}
	}

	/*Radix::Reports.Scheduler::Task.Report:Edit:Model:paramBindingEditor_changed-Presentation Slot Method*/

	@org.radixware.kernel.common.lang.ReflectiveCallable
	@SuppressWarnings("unused")
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:Edit:Model:paramBindingEditor_changed")
	public  void paramBindingEditor_changed () {
		Reports::ReportParametersBindingWidget editor = Task.Report:Edit:ReportParams:View:EditorPageView:object;
		Reports::ReportParametersBindingWidget:Model model = editor.Model;
		this.parametersBinding.setValue(model.parametersBindingDoc);

	}

	/*Radix::Reports.Scheduler::Task.Report:Edit:Model:updateParameterBinding-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:Edit:Model:updateParameterBinding")
	public  void updateParameterBinding () {
		if (getEditorPage(idof[Task.Report:Edit:ReportParams]).getView() == null) {
		    return; //page not opened yet.
		}
		final Reports::ReportParametersBindingWidget editor = Task.Report:Edit:ReportParams:View:EditorPageView:object;
		Reports::ReportParametersBindingWidget:Model model = editor.Model;
		model.parameters = new java.util.ArrayList<Explorer.Models.Properties::Property>();
		model.parametersBindingDoc = null;
		model.forScheduleJob = true;

		final Types::Id reportClassId = Types::Id.Factory.loadFrom(reportClassGuid.Value);
		if (reportClassId != null) {
		    final Explorer.Meta::ReportPresentationDef reportPresentationDef = Environment.DefManager.getReportPresentationDef(reportClassId);
		    final java.util.List<Types::Id> propIds = reportPresentationDef.getPropertyIds();

		    final Explorer.Models::Model reportModel =
		            reportPresentationDef.createModel(new ReportParametersContext(this, null));
		    for (Types::Id propId : propIds) {
		        final Explorer.Meta::PropertyDef propDef = reportPresentationDef.getPropertyDefById(propId);
		        if (propDef.getNature() == Meta::PropNature:SqlClassParameter) {
		            Explorer.Models.Properties::Property param = reportModel.getProperty(propId);
		            model.parameters.add(param);
		        }
		    }

		    model.parametersBindingDoc = parametersBinding.Value;

		    final ArrInt toRemove = new ArrInt();
		    if (parametersBinding.Value != null) {
		        int idx = 0;

		        for (org.radixware.schemas.reports.ParametersBindingType.ParameterBinding binding : parametersBinding.Value.ParametersBinding.ParameterBindingList) {
		            if (!propIds.contains(binding.ParameterId)) {
		                toRemove.add(idx);
		            }
		            idx++;
		        }

		        if (toRemove.size() > 0) {
		            if (getEnvironment().messageConfirmation("Invalid Parameters", "List of report parameters defined in this task contains unknown parameters (probably, they were removed from the report). It is not possible to generate a report with the unknown parameters. Delete the unknown parameters?")) {
		                for (Int i : toRemove) {
		                    model.parametersBindingDoc.ParametersBinding.removeParameterBinding(i.intValue());
		                }
		                paramBindingEditor_changed();
		            }
		        }
		    }
		}

		model.init();

		boolean dateTimeExist = false;
		for (Explorer.Models.Properties::Property param : model.parameters) {
		    if (param.getDefinition().getType() == Meta::ValType:DateTime) {
		        dateTimeExist = true;
		    }
		}
		this.lag.setVisible(dateTimeExist);


	}

	/*Radix::Reports.Scheduler::Task.Report:Edit:Model:EditorPageView_opened-Presentation Slot Method*/

	@org.radixware.kernel.common.lang.ReflectiveCallable
	@SuppressWarnings("unused")
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:Edit:Model:EditorPageView_opened")
	public  void EditorPageView_opened (com.trolltech.qt.gui.QWidget widget) {
		updateParameterBinding();
	}

	/*Radix::Reports.Scheduler::Task.Report:Edit:Model:afterRead-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:Edit:Model:afterRead")
	protected published  void afterRead () {
		super.afterRead();

		if (!isInSelectorRowContext()) {
		    updateParameterBinding();
		    updateColumnsSettings();
		    updateReportFieldsVisibility();
		    updateEnabledFormats();
		}
	}

	/*Radix::Reports.Scheduler::Task.Report:Edit:Model:getExt-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:Edit:Model:getExt")
	private final  Str getExt () {
		if (this.format.Value != Reports::ReportExportFormat:MSDL && this.format.Value != Reports::ReportExportFormat:CUSTOM && this.format.Value != null) {
		    return this.format.Value.getExt();
		}

		if (this.fileNameFormat.Value != null && !this.fileNameFormat.Value.isEmpty()) {
		    final java.text.SimpleDateFormat dateTimeformat = new java.text.SimpleDateFormat(this.fileNameFormat.Value);
		    final String name = dateTimeformat.format(new java.util.Date());
		    int i = name.lastIndexOf('.');
		    if (i > 0 && i < name.length() - 1) {
		        return name.substring(i + 1);
		    }
		}

		return "";
	}

	/*Radix::Reports.Scheduler::Task.Report:Edit:Model:beforeUpdate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:Edit:Model:beforeUpdate")
	protected published  boolean beforeUpdate () throws org.radixware.kernel.common.client.exceptions.ModelException {
		if (getEditorPage(idof[Task.Report:Edit:ReportParams]).getView()!=null) { // opened
		    // close current editor in table 
		    final Reports::ReportParametersBindingWidget editor = Task.Report:Edit:ReportParams:View:EditorPageView:object;
		    com.trolltech.qt.gui.QTableWidget table = editor.Reports::ReportParametersBindingWidget:Widget:table;
		    int row = table.currentRow();
		    int col = table.currentColumn();
		    table.setCurrentCell(row, 0);
		    table.setCurrentCell(row, col);
		}

		return super.beforeUpdate();
	}

	/*Radix::Reports.Scheduler::Task.Report:Edit:Model:finishEdit-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:Edit:Model:finishEdit")
	public published  void finishEdit () {
		if (getEditorPage(idof[Task.Report:Edit:ReportParams]).getView() != null && Task.Report:Edit:ReportParams:View:EditorPageView:object != null && Task.Report:Edit:ReportParams:View:EditorPageView:object.Model != null) {
		    Task.Report:Edit:ReportParams:View:EditorPageView:object.Model.finishEdit();
		}
		super.finishEdit();
	}

	/*Radix::Reports.Scheduler::Task.Report:Edit:Model:isCsvFormatSupported-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:Edit:Model:isCsvFormatSupported")
	public  boolean isCsvFormatSupported () {
		if (reportClassGuid.Value != null) {
		    try{
		        final Explorer.Meta::ReportPresentationDef reportPresentationDef = Environment.DefManager.getReportPresentationDef(Types::Id.Factory.loadFrom(reportClassGuid.Value));
		        return reportPresentationDef.isExportToCsvEnabled();
		    }catch(Exceptions::DefinitionError ex){
		        Environment.getTracer().error("Report class #"+ reportClassGuid.Value + " not found");                 
		        return false;
		    }
		} else {
		    return false;
		}
	}

	/*Radix::Reports.Scheduler::Task.Report:Edit:Model:updateEnabledFormats-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:Edit:Model:updateEnabledFormats")
	public  void updateEnabledFormats () {
		final Explorer.EditMask::EditMaskConstSet formatEditMask = (Explorer.EditMask::EditMaskConstSet) format.getEditMask();

		java.util.EnumSet<Reports::ReportExportFormat> exclude = java.util.EnumSet.noneOf(Reports::ReportExportFormat.class);

		Explorer.Meta::EnumItems excl = formatEditMask.getExcludedItems(getEnvironment().getApplication());
		excl.clear();
		excl.addItem(Reports::ReportExportFormat:RTF);
		if (!isCsvFormatSupported()) {
		    excl.addItem(Reports::ReportExportFormat:CSV);
		}
		if (!isTxtFormatSupported()) {
		    excl.addItem(Reports::ReportExportFormat:TXT);

		}
		if (!isXlsxFormatSupported()) {
		    excl.addItem(Reports::ReportExportFormat:XLSX);
		}

		formatEditMask.setExcludedItems(excl);
	}

	/*Radix::Reports.Scheduler::Task.Report:Edit:Model:updateEncodingsList-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:Edit:Model:updateEncodingsList")
	public  void updateEncodingsList () {
		Explorer.EditMask::EditMaskList editMask = (Explorer.EditMask::EditMaskList)encoding.getEditMask();

		final java.util.Map<String, java.nio.charset.Charset> allCharsets = java.nio.charset.Charset.availableCharsets();

		editMask.clearItems();
		for (java.util.Map.Entry<String, java.nio.charset.Charset> entry : allCharsets.entrySet()) {
		    editMask.addItem(entry.getValue().displayName(), entry.getKey());     
		}



	}

	/*Radix::Reports.Scheduler::Task.Report:Edit:Model:isMultiFileReport-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:Edit:Model:isMultiFileReport")
	public  boolean isMultiFileReport () {
		if (reportClassGuid.Value != null) {
		    try{
		        final Explorer.Meta::ReportPresentationDef reportPresentationDef = Environment.DefManager.getReportPresentationDef(Types::Id.Factory.loadFrom(reportClassGuid.Value));
		        return reportPresentationDef.isMultiFileReport();
		    }catch(Exceptions::DefinitionError ex){
		        Environment.getTracer().error("Report class #"+ reportClassGuid.Value + " not found");                 
		        return false;
		    }
		} else {
		    return false;
		}
	}

	/*Radix::Reports.Scheduler::Task.Report:Edit:Model:isTxtFormatSupported-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:Edit:Model:isTxtFormatSupported")
	public  boolean isTxtFormatSupported () {
		if (reportClassGuid.Value != null) {
		    try{
		        final Explorer.Meta::ReportPresentationDef reportPresentationDef = Environment.DefManager.getReportPresentationDef(Types::Id.Factory.loadFrom(reportClassGuid.Value));
		        return reportPresentationDef.isExportToTxtEnabled();
		    }catch(Exceptions::DefinitionError ex){
		        Environment.getTracer().error("Report class #"+ reportClassGuid.Value + " not found");                 
		        return false;
		    }
		} else {
		    return false;
		}
	}

	/*Radix::Reports.Scheduler::Task.Report:Edit:Model:isXlsxFormatSupported-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:Edit:Model:isXlsxFormatSupported")
	public  boolean isXlsxFormatSupported () {
		if (reportClassGuid.Value != null) {
		    try{
		        final Explorer.Meta::ReportPresentationDef reportPresentationDef = Environment.DefManager.getReportPresentationDef(Types::Id.Factory.loadFrom(reportClassGuid.Value));
		        return reportPresentationDef.isExportToXlsxEnabled();
		    }catch(Exceptions::DefinitionError ex){
		        Environment.getTracer().error("Report class #"+ reportClassGuid.Value + " not found");                 
		        return false;
		    }
		} else {
		    return false;
		}
	}

	/*Radix::Reports.Scheduler::Task.Report:Edit:Model:ColumnsSettingsPageView_opened-Presentation Slot Method*/

	@org.radixware.kernel.common.lang.ReflectiveCallable
	@SuppressWarnings("unused")
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:Edit:Model:ColumnsSettingsPageView_opened")
	public  void ColumnsSettingsPageView_opened (com.trolltech.qt.gui.QWidget widget) {
		updateColumnsSettings();
	}

	/*Radix::Reports.Scheduler::Task.Report:Edit:Model:updateColumnsSettings-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:Edit:Model:updateColumnsSettings")
	public  void updateColumnsSettings () {
		if (getEditorPage(idof[Task.Report:Edit:ReportColumns]).getView() == null || !getEditorPage(idof[Task.Report:Edit:ReportColumns]).isVisible()) {
		    return;
		}

		Reports::ReportColumnsSettingWidget editor = Task.Report:Edit:ReportColumns:View:ColumnsSettingsPageView:columnsSettingsEditor;
		Reports::ReportColumnsSettingWidget:Model model = editor.Model;

		final Types::Id reportClassId = Types::Id.Factory.loadFrom(reportClassGuid.Value);
		if (reportClassId != null) {
		    model.reportId = reportClassId;

		    try {           
		        model.columnsSettingsDocument = columnsSettingsDocument.Value;
		    } catch (Exceptions::Exception ex) {
		        Environment.processException(ex);            
		    }
		}

		model.init();
	}

	/*Radix::Reports.Scheduler::Task.Report:Edit:Model:columnsSettingsEditor_changed-Presentation Slot Method*/

	@org.radixware.kernel.common.lang.ReflectiveCallable
	@SuppressWarnings("unused")
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:Edit:Model:columnsSettingsEditor_changed")
	public  void columnsSettingsEditor_changed () {
		Reports::ReportColumnsSettingWidget editor = Task.Report:Edit:ReportColumns:View:ColumnsSettingsPageView:columnsSettingsEditor;
		Reports::ReportColumnsSettingWidget:Model model = editor.Model;
		columnsSettingsDocument.Value = (model.columnsSettingsDocument != null ? model.columnsSettingsDocument : null);
	}
	public final class ClearXslt extends org.radixware.ads.Reports.Scheduler.explorer.Task.Report.ClearXslt{
		protected ClearXslt(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_clearXslt( this, propertyId );
		}

	}

	public final class ClearXsd extends org.radixware.ads.Reports.Scheduler.explorer.Task.Report.ClearXsd{
		protected ClearXsd(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_clearXsd( this, propertyId );
		}

	}

	public final class EditXsd extends org.radixware.ads.Reports.Scheduler.explorer.Task.Report.EditXsd{
		protected EditXsd(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_editXsd( this, propertyId );
		}

	}

	public final class ClearMsdl extends org.radixware.ads.Reports.Scheduler.explorer.Task.Report.ClearMsdl{
		protected ClearMsdl(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_clearMsdl( this, propertyId );
		}

	}

	public final class EditMsdl extends org.radixware.ads.Reports.Scheduler.explorer.Task.Report.EditMsdl{
		protected EditMsdl(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_editMsdl( this, propertyId );
		}

	}

	public final class EditXslt extends org.radixware.ads.Reports.Scheduler.explorer.Task.Report.EditXslt{
		protected EditXslt(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_editXslt( this, propertyId );
		}

	}

	public final class ChooseReport extends org.radixware.ads.Reports.Scheduler.explorer.Task.Report.ChooseReport{
		protected ChooseReport(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_chooseReport( this, propertyId );
		}

	}

	public final class GenerateReport extends org.radixware.ads.Reports.Scheduler.explorer.Task.Report.GenerateReport{
		protected GenerateReport(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_generateReport( this );
		}

	}









































}

/* Radix::Reports.Scheduler::Task.Report:Edit:Model - Desktop Meta*/

/*Radix::Reports.Scheduler::Task.Report:Edit:Model-Entity Model Class*/

package org.radixware.ads.Reports.Scheduler.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Edit:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aemZU2CPFLYVJCXZCWOSIYQPC7P24"),
						"Edit:Model",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aemAIWBLDTSJTOBDCIVAALOMT5GDM"),
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::Reports.Scheduler::Task.Report:Edit:Model:Properties-Properties*/
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

/* Radix::Reports.Scheduler::Task.Report:Edit:ReportParams:View - Desktop Executable*/

/*Radix::Reports.Scheduler::Task.Report:Edit:ReportParams:View-Custom Page Editor for Desktop*/

package org.radixware.ads.Reports.Scheduler.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:Edit:ReportParams:View")
public class View extends org.radixware.kernel.explorer.views.EditorPageView {
	final private com.trolltech.qt.gui.QFont DEFAULT_FONT = org.radixware.kernel.explorer.text.ExplorerTextOptions.Factory.getDefault().getQFont();
	public View EditorPageView;
	public View getEditorPageView(){ return EditorPageView;}
	public org.radixware.ads.Reports.explorer.ReportParametersBindingWidget object;
	public org.radixware.ads.Reports.explorer.ReportParametersBindingWidget getObject(){ return object;}
	public com.trolltech.qt.gui.QHBoxLayout lagLayout;
	public org.radixware.kernel.explorer.widgets.PropLabel lagPropTitle;
	public org.radixware.kernel.explorer.widgets.PropLabel getLagPropTitle(){ return lagPropTitle;}
	public org.radixware.kernel.explorer.widgets.propeditors.AbstractPropEditor lagPropEditor;
	public org.radixware.kernel.explorer.widgets.propeditors.AbstractPropEditor getLagPropEditor(){ return lagPropEditor;}
	public View(org.radixware.kernel.common.client.IClientEnvironment userSession,
	org.radixware.kernel.common.client.views.IView parent, org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef page) {
		super(userSession,(org.radixware.kernel.explorer.views.IExplorerView)parent, page);
	}
	public void open(org.radixware.kernel.common.client.models.Model model) {
		super.open(model);
		EditorPageView = this;
		EditorPageView.setObjectName("EditorPageView");
		EditorPageView.setGeometry(new com.trolltech.qt.core.QRect(0, 0, 597, 351));
		EditorPageView.setSizePolicy(new com.trolltech.qt.gui.QSizePolicy(com.trolltech.qt.gui.QSizePolicy.Policy.Minimum, com.trolltech.qt.gui.QSizePolicy.Policy.Minimum));
		EditorPageView.setFont(DEFAULT_FONT);
		final com.trolltech.qt.gui.QVBoxLayout $layout1 = new com.trolltech.qt.gui.QVBoxLayout(EditorPageView);
		$layout1.setObjectName("verticalLayout");
		$layout1.setContentsMargins(9, 9, 9, 9);
		$layout1.setSizeConstraint(com.trolltech.qt.gui.QLayout.SizeConstraint.SetDefaultConstraint);
		$layout1.setSpacing(6);
		object = new org.radixware.ads.Reports.explorer.ReportParametersBindingWidget(model.getEnvironment(),EditorPageView);
		object.open(object.getModel());
		object.setObjectName("object");
		object.setFont(DEFAULT_FONT);
		object.sig7EBQC42NXNDJ3K6W6TW4T57VJA.connect(model, "mth4TAPVMP2YRC7ZIEPPK2JEFJEEM()");
		$layout1.addWidget(object);
		lagLayout = new com.trolltech.qt.gui.QHBoxLayout();
		lagLayout.setObjectName("lagLayout");
		lagLayout.setContentsMargins(0, 0, 0, 0);
		lagLayout.setSizeConstraint(com.trolltech.qt.gui.QLayout.SizeConstraint.SetDefaultConstraint);
		lagLayout.setSpacing(6);
		lagPropTitle = new org.radixware.kernel.explorer.widgets.PropLabel(EditorPageView);
		lagPropTitle.setGeometry(new com.trolltech.qt.core.QRect(127, 83, 120, 20));
		lagPropTitle.setProperty(model.getProperty(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdRMULELQWFVC25BIM7LQOOSZKRM")));
		lagPropTitle.setObjectName("lagPropTitle");
		lagLayout.addWidget(lagPropTitle);
		lagPropEditor = (org.radixware.kernel.explorer.widgets.propeditors.AbstractPropEditor)model.getProperty(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdRMULELQWFVC25BIM7LQOOSZKRM")).createPropertyEditor();
		lagPropEditor.setParent(EditorPageView);
		lagPropEditor.bind();
		lagPropEditor.setGeometry(new com.trolltech.qt.core.QRect(467, 96, 100, 25));
		lagPropEditor.setObjectName("lagPropEditor");
		lagLayout.addWidget(lagPropEditor);
		com.trolltech.qt.gui.QSpacerItem $spacer1 = new com.trolltech.qt.gui.QSpacerItem(40, 20, com.trolltech.qt.gui.QSizePolicy.Policy.Expanding, com.trolltech.qt.gui.QSizePolicy.Policy.Minimum);
		lagLayout.addSpacerItem($spacer1);
		$layout1.addLayout(lagLayout);
		EditorPageView.opened.connect(model, "mth5JGT7WLSLVA3VOTVESHG2OQ6WM(com.trolltech.qt.gui.QWidget)");
		opened.emit(this);
	}
	public final org.radixware.ads.Reports.Scheduler.explorer.Edit:Model getModel() {
		return (org.radixware.ads.Reports.Scheduler.explorer.Edit:Model) super.getModel();
	}

}

/* Radix::Reports.Scheduler::Task.Report:Edit:ReportParams:WebView - Web Executable*/

/*Radix::Reports.Scheduler::Task.Report:Edit:ReportParams:WebView-Rwt Custom Page Editor*/

package org.radixware.ads.Reports.Scheduler.web;
@SuppressWarnings({"unchecked","rawtypes"})
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:Edit:ReportParams:WebView")
public class WebView extends org.radixware.wps.views.editor.EditorPageView{
	public WebView widget;
	public WebView getWidget(){ return  widget;}
	public WebView(org.radixware.kernel.common.client.IClientEnvironment env,org.radixware.kernel.common.client.views.IView view
	,org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef page){
		super(env,view,page);
	}
	public void open(org.radixware.kernel.common.client.models.Model model){
		super.open(model);
		//============ Radix::Reports.Scheduler::Task.Report:Edit:ReportParams:WebView:widget ==============
		this.widget = this;
		fireOpened();
	}
}

/* Radix::Reports.Scheduler::Task.Report:Edit:ReportColumns:View - Desktop Executable*/

/*Radix::Reports.Scheduler::Task.Report:Edit:ReportColumns:View-Custom Page Editor for Desktop*/

package org.radixware.ads.Reports.Scheduler.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:Edit:ReportColumns:View")
public class View extends org.radixware.kernel.explorer.views.EditorPageView {
	final private com.trolltech.qt.gui.QFont DEFAULT_FONT = org.radixware.kernel.explorer.text.ExplorerTextOptions.Factory.getDefault().getQFont();
	public View ColumnsSettingsPageView;
	public View getColumnsSettingsPageView(){ return ColumnsSettingsPageView;}
	public org.radixware.ads.Reports.explorer.ReportColumnsSettingWidget columnsSettingsEditor;
	public org.radixware.ads.Reports.explorer.ReportColumnsSettingWidget getColumnsSettingsEditor(){ return columnsSettingsEditor;}
	public View(org.radixware.kernel.common.client.IClientEnvironment userSession,
	org.radixware.kernel.common.client.views.IView parent, org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef page) {
		super(userSession,(org.radixware.kernel.explorer.views.IExplorerView)parent, page);
	}
	public void open(org.radixware.kernel.common.client.models.Model model) {
		super.open(model);
		ColumnsSettingsPageView = this;
		ColumnsSettingsPageView.setGeometry(new com.trolltech.qt.core.QRect(0, 0, 600, 400));
		ColumnsSettingsPageView.setObjectName("ColumnsSettingsPageView");
		ColumnsSettingsPageView.setFont(DEFAULT_FONT);
		final com.trolltech.qt.gui.QGridLayout $layout1 = new com.trolltech.qt.gui.QGridLayout(ColumnsSettingsPageView);
		$layout1.setObjectName("gridLayout");
		$layout1.setContentsMargins(9, 9, 9, 9);
		$layout1.setSizeConstraint(com.trolltech.qt.gui.QLayout.SizeConstraint.SetDefaultConstraint);
		$layout1.setHorizontalSpacing(6);
		$layout1.setVerticalSpacing(6);
		columnsSettingsEditor = new org.radixware.ads.Reports.explorer.ReportColumnsSettingWidget(model.getEnvironment(),ColumnsSettingsPageView);
		columnsSettingsEditor.open(columnsSettingsEditor.getModel());
		columnsSettingsEditor.setObjectName("columnsSettingsEditor");
		columnsSettingsEditor.setFont(DEFAULT_FONT);
		columnsSettingsEditor.sigSMYKYBQUQJBRTM5YRYMLSTIISI.connect(model, "mthLGG3K23MTRCA7OPYKVAN6S62R4()");
		$layout1.addWidget(columnsSettingsEditor, 0, 0, 1, 1);
		ColumnsSettingsPageView.opened.connect(model, "mthBFDDJOVHNRDAPOQREE6M3YI46U(com.trolltech.qt.gui.QWidget)");
		opened.emit(this);
	}
	public final org.radixware.ads.Reports.Scheduler.explorer.Edit:Model getModel() {
		return (org.radixware.ads.Reports.Scheduler.explorer.Edit:Model) super.getModel();
	}

}

/* Radix::Reports.Scheduler::Task.Report:Edit:ReportColumns:WebView - Web Executable*/

/*Radix::Reports.Scheduler::Task.Report:Edit:ReportColumns:WebView-Rwt Custom Page Editor*/

package org.radixware.ads.Reports.Scheduler.web;
@SuppressWarnings({"unchecked","rawtypes"})
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.Scheduler::Task.Report:Edit:ReportColumns:WebView")
public class WebView extends org.radixware.wps.views.editor.EditorPageView{
	public WebView widget;
	public WebView getWidget(){ return  widget;}
	public org.radixware.wps.views.editor.PropertiesGrid widget;
	public org.radixware.wps.views.editor.PropertiesGrid getWidget(){ return  widget;}
	public WebView(org.radixware.kernel.common.client.IClientEnvironment env,org.radixware.kernel.common.client.views.IView view
	,org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef page){
		super(env,view,page);
	}
	public void open(org.radixware.kernel.common.client.models.Model model){
		super.open(model);
		//============ Radix::Reports.Scheduler::Task.Report:Edit:ReportColumns:WebView:widget ==============
		this.widget = this;
		widget.setWidth(600);
		widget.setHeight(400);
		//============ Radix::Reports.Scheduler::Task.Report:Edit:ReportColumns:WebView:widget:widget ==============
		this.widget = new org.radixware.wps.views.editor.PropertiesGrid();
		widget.setObjectName("widget");
		this.widget.add(this.widget);
		widget.getAnchors().setTop(new org.radixware.wps.rwt.UIObject.Anchors.Anchor(0.0f,0));
		widget.getAnchors().setLeft(new org.radixware.wps.rwt.UIObject.Anchors.Anchor(0.0f,0));
		widget.getAnchors().setBottom(new org.radixware.wps.rwt.UIObject.Anchors.Anchor(1.0f,0));
		widget.getAnchors().setRight(new org.radixware.wps.rwt.UIObject.Anchors.Anchor(1.0f,0));
		this.wdgAXTY3WI5RVHBTDBM7VFHCBZALU.bind();
		fireOpened();
	}
}

/* Radix::Reports.Scheduler::Task.Report:Create - Desktop Meta*/

/*Radix::Reports.Scheduler::Task.Report:Create-Editor Presentation*/

package org.radixware.ads.Reports.Scheduler.explorer;
public final class Create_mi{
	private static final class Create_DEF extends org.radixware.kernel.common.client.meta.RadEditorPresentationDef{
		Create_DEF(){
			super(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprOR3IPE5XXRDNFJMHE6BWYV7TZE"),
			"Create",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("eprZU2CPFLYVJCXZCWOSIYQPC7P24"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("acl3JADSBDYJTOBDCIVAALOMT5GDM"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("tblWZB7K4HLJPOBDCIUAALOMT5GDM"),
			null,
			null,
			null,
			null,

			/*Radix::Reports.Scheduler::Task.Report:Create:Children-Explorer Items*/
				null
			,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
			null,
			0,
			null,
			(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
			40112,0,0);
		}
		@Override
		protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.Reports.Scheduler.explorer.Edit:Model(userSession,this);
		}
	}
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new Create_DEF(); 
;
}
/* Radix::Reports.Scheduler::Task.Report - Localizing Bundle */
package org.radixware.ads.Reports.Scheduler.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Task.Report - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Clear");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Очистить");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2JHU3S6AYJCOJJWJN7PSILZJOA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<defined>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<задано>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2JUJEC4RYRBTVCJZQJ6NGSS6XI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"File name generation function");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Функция формирования имени файла");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2PV5Q3BPTZETXHS2NLKJJ6W6LA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Report not defined.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Отчет не задан.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3BFYSLRM5FG7FBG4JNL6T52IIY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Report Column Visibility Settings");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Настройки видимости колонок отчета");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3YRBO2KCKRCY7CEKTTONQAWTPI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"File XSD");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"XSD файла");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4CPFLZVBENGAJLYBY4QL6YMVVI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"XSD Editor");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"XSD редактор");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls66WJPHRAEFCXNEBWHWQ27EWDBI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Post-write OS command");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Команда ОС после записи");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAJCV7J3URVF4DDL3AZDWLNM4AI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Created job");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Созданное задание");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAU6AROPD2FHC7KSWKWZFXHPW2U"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Edit MSDL");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Редактировать MSDL");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsB6HYA7DRNFFG3MLV4SMKXGUXFM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Edit XSLT");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Редактировать XSLT");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBH7CVHYWFVESXNTKJDTZAIUZFY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"File name adjustment function");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Функция корректировки имени файла");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCGG2VOWTNZFMJGJ26LALINUPQY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Report not defined");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Отчет не задан");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCO44BFWHYNDM5ANDBWVGVVEQB4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Report");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Отчет");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDEPXU7V73VEKJIMMSUYIZPWU6Q"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Report");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Отчет");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEDPFYTLQSVHQRAUWLQCGNHJRXU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Generate Report");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Выполнить отчет");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFIOMMFCELJHWTMXFIQN4F5VW2A"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Clear");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Очистить");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFSFDDIYMWVFXLFXKBDANKBHCS4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"File name format");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Формат имени файла");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFUUZCUIJQVFAVNV5YXV6YMNEEM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Set status to \'Failed\' in case of generation error");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Установить статус в \'Не выполнено\' в случае ошибки при генерации ");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGFEJWWPIGJA3PPNF3MMCRYZWMM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<defined>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<задано>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGIXA4QLRGND6PLH3BX2QG5PWT4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Edit XSD");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Редактировать XSD");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGVZX6ATPLJBLVFVG4BH6KGNNOU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"List of report parameters defined in this task contains unknown parameters (probably, they were removed from the report). It is not possible to generate a report with the unknown parameters. Delete the unknown parameters?");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Список параметров отчета, заданных в задаче, содержит неизвестные параметры (возможно, они были удалены из отчета). Выполнение отчета при наличии неизвестных параметров невозможно. Удалить неизвестные параметры?");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHGRXI4JNSRGJPJNXJUCT2ZCOD4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Output directory");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Выходной каталог");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsICPTBCHW6RDX7ESCVIS226B5FQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"use system encoding");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"использовать кодировку по-умолчанию");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJL7Q4HEQMFEM3J3QNKHFH3FI54"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Select Report");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Выбор отчета");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJZPU4Y5C5VBCVHXJ3H6ZZVK3AQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Report class");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Класс отчета");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsK4FTVFBHPZBBHJEAHTF7ZISDZ4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Report file encoding (for CSV and TXT). If not defined, the system encoding is used. ");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Кодировка текстового представления отчета (применяется для форматов txt и csv). Если не задано - используется системная кодировка ");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLVSZQCVDU5D7VFD5MRDULALDUQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"File format");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Формат файла");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMP3F64XJNVGZNDXWKXJQPLLQHE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"XSLT");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"XSLT");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMRQOLPVLS5FTZO2Z7LKVPO25OI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"File encoding");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Кодировка файла");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMXUZ6XXHJZEQLOPLPP2J3GVK4I"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Report Parameters");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Параметры отчета");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNAK55FD4CNBWHAGF5FUM3XQVNM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Report parameters override those defined in the task");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Параметры отчета, перекрывают настроенные в задаче");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsORZJY5G4DZAWDATR5S33C3CYT4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Clear");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Очистить");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOVROYKPOMRFNTPCWDSZGV3W2HQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<defined>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<задано>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsP2FAIDLGPNDJHLV2JV6IZ7OLKI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<defined>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<задано>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQ6ZXCMR6VJHYDF2UIYUSAVNVHQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Invalid Parameters");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Некорректные параметры");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSAWBABKXQVA3JLSSGSQDJ32W44"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Lag for previous and current execution time (min:sec)");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Отставание для предыдущего и текущего времени выполнения (мин:сек)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSIUW7JU6TBHVDNQR3DWPC4YKPQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Export Settings");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Настройки экспорта");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTPPDSJ4OMZBQPDVBY7C6OGHTQM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<defined>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<задано>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUCMFG36SL5HZBIPJ5WXWO72OZU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Counter");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Счетчик");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVCVY7R4OBZEMDF37ZND6MPQOTE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<defined>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<задано>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYEJDVTHPHBDJNC3RZXFSCYRF3M"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Scheduled runtime");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Плановое время исполнения");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZ7GW5TSKDJGTHF2B5FVYBAZGRE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"XSD not defined.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"XSD не задано.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZH6ZZ52IYNGZBCI6X5U3EV2PZA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(Task.Report - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbacl3JADSBDYJTOBDCIVAALOMT5GDM"),"Task.Report - Localizing Bundle",$$$items$$$);
}
