
/* Radix::Reports::ReportPreview - Server Executable*/

/*Radix::Reports::ReportPreview-Server Dynamic Class*/

package org.radixware.ads.Reports.server;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPreview")
public class ReportPreview  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {

	private static final String MTH_OPEN_DYNANIC = "mth_report_open_dynamic______";
	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return ReportPreview_mi.rdxMeta;}

	/*Radix::Reports::ReportPreview:Nested classes-Nested Classes*/

	/*Radix::Reports::ReportPreview:ReportPreviewClassLoader-Server Dynamic Class*/


	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPreview:ReportPreviewClassLoader")
	private static class ReportPreviewClassLoader  extends java.lang.ClassLoader  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {


		/**Metainformation accessor method*/
		@SuppressWarnings("unused")
		public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return ReportPreview_mi.rdxMeta_adcIMU5HSNUZBHUHNQYB7ISMWHQ2M;}

		/*Radix::Reports::ReportPreview:ReportPreviewClassLoader:Nested classes-Nested Classes*/

		/*Radix::Reports::ReportPreview:ReportPreviewClassLoader:Properties-Properties*/

		/*Radix::Reports::ReportPreview:ReportPreviewClassLoader:reportJar-Dynamic Property*/



		protected java.io.File reportJar=null;











		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPreview:ReportPreviewClassLoader:reportJar")
		private final  java.io.File getReportJar() {
			return reportJar;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPreview:ReportPreviewClassLoader:reportJar")
		private final   void setReportJar(java.io.File val) {
			reportJar = val;
		}



































		/*Radix::Reports::ReportPreview:ReportPreviewClassLoader:Methods-Methods*/

		/*Radix::Reports::ReportPreview:ReportPreviewClassLoader:findClass-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPreview:ReportPreviewClassLoader:findClass")
		protected published  java.lang.Class<?> findClass (Str name) throws java.lang.ClassNotFoundException {
			Str classFileName = name.replace(".", "/") + ".class";
			org.radixware.kernel.common.repository.fs.JarFileDataProvider provider = org.radixware.kernel.common.repository.fs.JarFileDataProvider.getInstance(reportJar);

			if (provider.entryExists(classFileName)) {
			    try {
			        byte[] bytes = provider.getEntryData(classFileName);
			        return defineClass(name, bytes, 0, bytes.length);
			    } catch (Exceptions::IOException e) {
			        Arte::Trace.warning(Arte::Trace.exceptionStackToString(e), Arte::EventSource:ArteReports);
			    }
			}

			return null;
		}

		/*Radix::Reports::ReportPreview:ReportPreviewClassLoader:loadClass-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPreview:ReportPreviewClassLoader:loadClass")
		protected published  java.lang.Class<?> loadClass (Str name, boolean resolve) throws java.lang.ClassNotFoundException {
			Class c = findLoadedClass(name);
			if (c == null) {
			    c = findClass(name);
			    
			    if (c == null) {
			        c = getParent().loadClass(name);
			    }
			}

			if (resolve) {
			    resolveClass(c);
			}

			return c;
		}

		/*Radix::Reports::ReportPreview:ReportPreviewClassLoader:ReportPreviewClassLoader-User Method*/

		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPreview:ReportPreviewClassLoader:ReportPreviewClassLoader")
		public  ReportPreviewClassLoader (java.io.File reportJar) {
			super(ReportPreviewClassLoader.class.getClassLoader());
			reportJar = reportJar;
		}


	}

	/*Radix::Reports::ReportPreview:ReportPreviewEnvironment-Server Dynamic Class*/


	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPreview:ReportPreviewEnvironment")
	private static class ReportPreviewEnvironment  implements org.radixware.kernel.server.reports.IReportPreviewEnvironment,org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {

		private java.io.File tmpDir;
		private java.io.File reportJarFile;
		private final java.util.Map<Types::Id, Int> executionNumbers = new java.util.HashMap<Types::Id, Int>();
		/**Metainformation accessor method*/
		@SuppressWarnings("unused")
		public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return ReportPreview_mi.rdxMeta_adcJDVKLI72FFFBVB6B3Z23NZMQ6Y;}

		/*Radix::Reports::ReportPreview:ReportPreviewEnvironment:Nested classes-Nested Classes*/

		/*Radix::Reports::ReportPreview:ReportPreviewEnvironment:Properties-Properties*/

		/*Radix::Reports::ReportPreview:ReportPreviewEnvironment:TMP_DIR_NAME-Dynamic Property*/



		protected Str TMP_DIR_NAME=(Str)org.radixware.kernel.common.defs.value.ValAsStr.fromStr("REPORT_PREVIEW_DATA",org.radixware.kernel.common.enums.EValType.STR);











		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPreview:ReportPreviewEnvironment:TMP_DIR_NAME")
		private final  Str getTMP_DIR_NAME() {
			return TMP_DIR_NAME;
		}

		/*Radix::Reports::ReportPreview:ReportPreviewEnvironment:REPORT_JAR_NAME-Dynamic Property*/



		protected Str REPORT_JAR_NAME=(Str)org.radixware.kernel.common.defs.value.ValAsStr.fromStr("report.jar",org.radixware.kernel.common.enums.EValType.STR);











		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPreview:ReportPreviewEnvironment:REPORT_JAR_NAME")
		private final  Str getREPORT_JAR_NAME() {
			return REPORT_JAR_NAME;
		}

































		/*Radix::Reports::ReportPreview:ReportPreviewEnvironment:Methods-Methods*/

		/*Radix::Reports::ReportPreview:ReportPreviewEnvironment:findReportClassById-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPreview:ReportPreviewEnvironment:findReportClassById")
		public published  java.lang.Class<?> findReportClassById (org.radixware.kernel.common.types.Id reportId) {
			Str reportClassFileName = reportId.toString() + ".class";

			org.radixware.kernel.common.repository.fs.JarFileDataProvider provider = org.radixware.kernel.common.repository.fs.JarFileDataProvider.getInstance(reportJarFile);
			for (org.radixware.kernel.common.repository.fs.JarFileDataProvider.IJarEntry entry : provider.entries()) {
			    if (entry.getName().endsWith(reportClassFileName)) {
			        Str reportClassName = entry.getName().substring(0, entry.getName().lastIndexOf(".class")).replace("/", ".");
			        ReportPreview.ReportPreviewClassLoader loader = new ReportPreview.ReportPreviewClassLoader(reportJarFile);        
			        try {
			            return loader.loadClass(reportClassName, true);
			        } catch (Exceptions::ClassNotFoundException e) {
			            throw new DefinitionNotFoundError(reportId, e);
			        }
			    }
			}

			throw new DefinitionNotFoundError(reportId);
		}

		/*Radix::Reports::ReportPreview:ReportPreviewEnvironment:getTestResultSet-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPreview:ReportPreviewEnvironment:getTestResultSet")
		public published  java.sql.ResultSet getTestResultSet (org.radixware.kernel.server.types.Report reportInstance) {
			java.util.Map<Int, Str> fieldIndex2ColName = new java.util.HashMap<Int, Str>();
			for (java.lang.reflect.Field field : reportInstance.getClass().getDeclaredFields()) {
			    if (java.util.regex.Pattern.matches("\\$field_prf.*_index", field.getName())) {
			        try {                        
			            Str fieldName = field.getName();
			            Str strFieldId = field.getName().substring(fieldName.indexOf("_") + 1, fieldName.lastIndexOf("_"));
			            Str columnName = reportInstance.getRadMeta().getPropById(Types::Id.Factory.loadFrom(strFieldId)).getName();                        
			            
			            field.setAccessible(true);                                    
			            fieldIndex2ColName.put(field.getInt(reportInstance), columnName);
			            field.setAccessible(false);
			        } catch (java.lang.IllegalAccessException e) {
			            Arte::Trace.warning(Arte::Trace.exceptionStackToString(e), Arte::EventSource:ArteReports);
			        }
			    }
			}

			java.util.List<Int> sortedKeys = new java.util.ArrayList<Int>(fieldIndex2ColName.keySet());
			java.util.Collections.sort(sortedKeys);

			Str[] columnNames = new Str[fieldIndex2ColName.size()];
			for (int i = 0; i < columnNames.length; i++) {
			    columnNames[i] = fieldIndex2ColName.get(sortedKeys.get(i));
			} 

			try {
			   java.io.FileInputStream reportPreviewDataStream = new java.io.FileInputStream(getReportPreviewDataFile(reportInstance.getId()));   
			   
			   java.util.Map<org.radixware.kernel.common.utils.ResultSetOptions, Object> options = new java.util.HashMap<org.radixware.kernel.common.utils.ResultSetOptions, Object>();
			   options.put(org.radixware.kernel.common.utils.ResultSetOptions.RSO_COLUMN_NAMES, columnNames);
			   options.put(org.radixware.kernel.common.utils.ResultSetOptions.RSO_USE_FIRST_ROW_AS_COLUMN_NAMES, true);
			   options.put(org.radixware.kernel.common.utils.ResultSetOptions.RSO_LOB_PROVIDER, new ReportPreviewLobProvider(tmpDir.getAbsolutePath()));
			   
			   return org.radixware.kernel.common.utils.ResultSetFactory.buildScrollableResultSet(org.radixware.kernel.common.utils.ResultSetContentType.XLSX, reportPreviewDataStream, options);
			} catch (Exceptions::FileNotFoundException | Exceptions::SQLException e) {
			    Arte::Trace.warning(Arte::Trace.exceptionStackToString(e), Arte::EventSource:ArteReports);
			    throw new RuntimeException("Can't get test result set for report '" + getReportPreviewDataFileName(reportInstance.getId()) + "'", e);    
			}
		}

		/*Radix::Reports::ReportPreview:ReportPreviewEnvironment:close-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPreview:ReportPreviewEnvironment:close")
		public published  void close () {
			if (!org.radixware.kernel.common.utils.FileUtils.deleteDirectory(tmpDir)) {
			    Arte::Trace.warning("Unable to delete report preview temporary directory \"" + tmpDir.getAbsoluteFile() + "\"", Arte::EventSource:ArteReports);
			}
		}

		/*Radix::Reports::ReportPreview:ReportPreviewEnvironment:ReportPreviewEnvironment-User Method*/

		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPreview:ReportPreviewEnvironment:ReportPreviewEnvironment")
		public  ReportPreviewEnvironment (org.radixware.kernel.common.types.Bin reportJar, org.radixware.kernel.common.types.Bin testDataZip) {
			try {
			    tmpDir = java.io.File.createTempFile(TMP_DIR_NAME, "");
			    if (tmpDir.exists()) {
			        org.radixware.kernel.common.utils.FileUtils.deleteDirectory(tmpDir);
			    }
			    tmpDir.mkdir();

			//  Unzip reportJar
			    reportJarFile = new java.io.File(tmpDir, REPORT_JAR_NAME);
			    if (!reportJarFile.exists()) {
			        reportJarFile.createNewFile();
			    }

			    java.io.ByteArrayInputStream bais = new java.io.ByteArrayInputStream(reportJar.get());
			    java.io.FileOutputStream reportJarFos = new java.io.FileOutputStream(reportJarFile);
			    try (java.util.zip.ZipInputStream zis = new java.util.zip.ZipInputStream(bais)) {
			        java.util.zip.ZipEntry entry = zis.getNextEntry();
			        if (entry != null) {
			            org.radixware.kernel.common.utils.FileUtils.copyStream(zis, reportJarFos);
			        }
			    } finally {
			        reportJarFos.close();
			    }
			    
			//  Unzip testData 
			    bais = new java.io.ByteArrayInputStream(testDataZip.get());
			    try (java.util.zip.ZipInputStream zis = new java.util.zip.ZipInputStream(bais)) {
			        java.util.zip.ZipEntry entry = zis.getNextEntry();

			        while (entry != null) {
			            java.io.File tmpFile = new java.io.File(tmpDir, entry.getName());
			            if (!tmpFile.exists()) {
			                tmpFile.createNewFile();
			            }

			            try (java.io.FileOutputStream fos = new java.io.FileOutputStream(tmpFile)) {
			                org.radixware.kernel.common.utils.FileUtils.copyStream(zis, fos);
			            }
			            entry = zis.getNextEntry();
			        }
			    }
			} catch (Exceptions::IOException e) {
			    Arte::Trace.warning(Arte::Trace.exceptionStackToString(e), Arte::EventSource:ArteReports);
			}
		}

		/*Radix::Reports::ReportPreview:ReportPreviewEnvironment:getReportPreviewDataFile-User Method*/

		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPreview:ReportPreviewEnvironment:getReportPreviewDataFile")
		private final  java.io.File getReportPreviewDataFile (org.radixware.kernel.common.types.Id reportId) throws java.io.FileNotFoundException {
			Str reportPreviewDataFileName = getReportPreviewDataFileName(reportId) + ".xlsx";
			java.io.File result = new java.io.File(tmpDir, reportPreviewDataFileName);

			if (!result.exists()) {
			    result = new java.io.File(tmpDir, reportId.toString() + ".xlsx");
			    if (!result.exists()) {
			        throw new FileNotFoundException("Preview data for report '" + reportPreviewDataFileName + "' not found.");
			    }
			}

			return result;


		}

		/*Radix::Reports::ReportPreview:ReportPreviewEnvironment:getReportParameters-User Method*/

		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPreview:ReportPreviewEnvironment:getReportParameters")
		public  java.util.Map<org.radixware.kernel.common.types.Id,java.lang.Object> getReportParameters (org.radixware.ads.Types.server.Report report) throws java.io.FileNotFoundException {
			try {
			    java.io.File paramsFile = getReportParametersFile(report.getId());    
			    ReportsXsd:ParametersBindingDocument xParamsBindingDoc = ReportsXsd:ParametersBindingDocument.Factory.parse(paramsFile);
			    
			    final java.util.Map<Types::Id, Object> paramId2Value = new java.util.HashMap<Types::Id, Object>();
			    if (xParamsBindingDoc != null && xParamsBindingDoc.ParametersBinding != null) {
			        java.util.List<ReportsXsd:ParametersBindingType.ParameterBinding> xParamBindings = xParamsBindingDoc.ParametersBinding.getParameterBindingList();
			        if (xParamBindings != null) {
			            for (ReportsXsd:ParametersBindingType.ParameterBinding xParamBinding : xParamBindings) {
			                final Types::Id parameterId = xParamBinding.ParameterId;
			                final Object paramValue;
			                paramValue = ReportsServerUtils.loadParamFromXml(report, parameterId, xParamBinding.Value);
			                paramId2Value.put(parameterId, paramValue);
			            }
			        }
			    }
			    return paramId2Value;
			} catch (Exceptions::IOException | Exceptions::XmlException e) {    
			    throw new FileNotFoundException("Can not get report parameters. Reason: " + Arte::Trace.exceptionStackToString(e));
			}
		}

		/*Radix::Reports::ReportPreview:ReportPreviewEnvironment:getReportParametersFile-User Method*/

		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPreview:ReportPreviewEnvironment:getReportParametersFile")
		private final  java.io.File getReportParametersFile (org.radixware.kernel.common.types.Id reportId) throws java.io.FileNotFoundException {
			Str reportPreviewDataFileName = getReportPreviewDataFileName(reportId) + "_params.xml";
			java.io.File result = new java.io.File(tmpDir, reportPreviewDataFileName);

			if (!result.exists()) {
			    result = new java.io.File(tmpDir, reportId.toString() + "_params.xml");
			    if (!result.exists()) {
			        throw new FileNotFoundException("Parameters file for report '" + reportPreviewDataFileName + "' not found.");
			    }
			}

			return result;


		}

		/*Radix::Reports::ReportPreview:ReportPreviewEnvironment:getReportPreviewDataFileName-User Method*/

		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPreview:ReportPreviewEnvironment:getReportPreviewDataFileName")
		private final  Str getReportPreviewDataFileName (org.radixware.kernel.common.types.Id reportId) {
			java.lang.Integer executionNumber;
			if (executionNumbers.containsKey(reportId)) {
			    executionNumber = executionNumbers.get(reportId).intValue() + 1;
			} else {
			    executionNumber = 0;
			}
			executionNumbers.put(reportId, executionNumber);

			return reportId.toString() + "_" + executionNumber;
		}

		/*Radix::Reports::ReportPreview:ReportPreviewEnvironment:getReportColumnsFile-User Method*/

		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPreview:ReportPreviewEnvironment:getReportColumnsFile")
		private final  java.io.File getReportColumnsFile (org.radixware.kernel.common.types.Id reportId) {
			Str reportPreviewDataFileName = getReportPreviewDataFileName(reportId) + "_columns.xml";
			java.io.File result = new java.io.File(tmpDir, reportPreviewDataFileName);

			if (!result.exists()) {
			    result = new java.io.File(tmpDir, reportId.toString() + "_columns.xml");
			    if (!result.exists()) {
			        return null;
			    }
			}

			return result;


		}

		/*Radix::Reports::ReportPreview:ReportPreviewEnvironment:getReportColumnsSettings-User Method*/

		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPreview:ReportPreviewEnvironment:getReportColumnsSettings")
		public  org.radixware.schemas.reports.ColumnSettings getReportColumnsSettings (org.radixware.ads.Types.server.Report report) throws java.io.FileNotFoundException {
			try {
			    java.io.File columnsFile = getReportColumnsFile(report.getId());
			    if (columnsFile == null) {
			        return null;
			    }
			    
			    ReportsXsd:ColumnSettingsDocument xColumnSettingsDocument = ReportsXsd:ColumnSettingsDocument.Factory.parse(columnsFile);
			    return xColumnSettingsDocument == null ? null : xColumnSettingsDocument.ColumnSettings;
			} catch (Exceptions::IOException | Exceptions::XmlException e) {    
			    throw new FileNotFoundException("Can not parse report columns settings file. Reason: " + Arte::Trace.exceptionStackToString(e));
			}
		}


	}

	/*Radix::Reports::ReportPreview:ReportPreviewFileController-Server Dynamic Class*/


	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPreview:ReportPreviewFileController")
	private static class ReportPreviewFileController  implements org.radixware.kernel.server.reports.IReportFileController,org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {

		ReportExportFormat exportFormat;
		java.io.File dir;
		int fileNumber = 1;
		/**Metainformation accessor method*/
		@SuppressWarnings("unused")
		public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return ReportPreview_mi.rdxMeta_adcJCQNMJQR3JGTVGF5EAYMZPORWU;}

		/*Radix::Reports::ReportPreview:ReportPreviewFileController:Nested classes-Nested Classes*/

		/*Radix::Reports::ReportPreview:ReportPreviewFileController:Properties-Properties*/





























		/*Radix::Reports::ReportPreview:ReportPreviewFileController:Methods-Methods*/

		/*Radix::Reports::ReportPreview:ReportPreviewFileController:adjustFileName-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPreview:ReportPreviewFileController:adjustFileName")
		public published  Str adjustFileName (org.radixware.kernel.server.types.Report report, Str fileName) throws org.radixware.kernel.server.reports.ReportGenerationException {
			Str ext = exportFormat == ReportExportFormat:XLS ? ".xls" : "." + exportFormat.getExt();
			Str fileNumber = !report.isMultyFile() ? "" : Str.valueOf(fileNumber++);

			return report.getId().toString() + "_" + fileNumber + ext;
		}

		/*Radix::Reports::ReportPreview:ReportPreviewFileController:afterCloseFile-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPreview:ReportPreviewFileController:afterCloseFile")
		public published  void afterCloseFile (org.radixware.kernel.server.types.Report report, java.io.File file) throws org.radixware.kernel.server.reports.ReportGenerationException {

		}

		/*Radix::Reports::ReportPreview:ReportPreviewFileController:afterCreateFile-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPreview:ReportPreviewFileController:afterCreateFile")
		public published  void afterCreateFile (org.radixware.kernel.server.types.Report report, java.io.File file, java.io.OutputStream outputStream) throws org.radixware.kernel.server.reports.ReportGenerationException {

		}

		/*Radix::Reports::ReportPreview:ReportPreviewFileController:beforeCloseFile-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPreview:ReportPreviewFileController:beforeCloseFile")
		public published  void beforeCloseFile (org.radixware.kernel.server.types.Report report, java.io.File file, java.io.OutputStream outputStream) throws org.radixware.kernel.server.reports.ReportGenerationException {

		}

		/*Radix::Reports::ReportPreview:ReportPreviewFileController:getDirectory-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPreview:ReportPreviewFileController:getDirectory")
		public published  java.io.File getDirectory () throws org.radixware.kernel.server.reports.ReportGenerationException {
			return dir;
		}

		/*Radix::Reports::ReportPreview:ReportPreviewFileController:ReportPreviewFileController-User Method*/

		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPreview:ReportPreviewFileController:ReportPreviewFileController")
		public  ReportPreviewFileController (org.radixware.kernel.common.enums.EReportExportFormat exportFormat) throws java.io.IOException {
			this.exportFormat = exportFormat;
			dir = java.nio.file.Files.createTempDirectory("ReportPreviewFileControllerTmp").toFile();
		}

		/*Radix::Reports::ReportPreview:ReportPreviewFileController:deleteDirectory-User Method*/

		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPreview:ReportPreviewFileController:deleteDirectory")
		public  void deleteDirectory () throws java.io.IOException {
			org.apache.commons.io.FileUtils.deleteDirectory(dir);
		}


	}

	/*Radix::Reports::ReportPreview:ReportPreviewLobProvider-Server Dynamic Class*/


	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPreview:ReportPreviewLobProvider")
	public static class ReportPreviewLobProvider  implements org.radixware.kernel.common.utils.IResultSetLobProvider,org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {

		private java.net.URL previewDataDirPath;
		/**Metainformation accessor method*/
		@SuppressWarnings("unused")
		public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return ReportPreview_mi.rdxMeta_adcVEYIJHMLKREPBI7ORLEYMV3TVE;}

		/*Radix::Reports::ReportPreview:ReportPreviewLobProvider:Nested classes-Nested Classes*/

		/*Radix::Reports::ReportPreview:ReportPreviewLobProvider:Properties-Properties*/





























		/*Radix::Reports::ReportPreview:ReportPreviewLobProvider:Methods-Methods*/

		/*Radix::Reports::ReportPreview:ReportPreviewLobProvider:canGetBlobContent-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPreview:ReportPreviewLobProvider:canGetBlobContent")
		public published  boolean canGetBlobContent (Str blobReference) {
			boolean defaultResult = org.radixware.kernel.common.utils.FakeResultSet.DEFAULT_LOB_PROVIDER.canGetBlobContent(blobReference);
			if (defaultResult == false) {
			    defaultResult = org.radixware.kernel.common.utils.FakeResultSet.DEFAULT_LOB_PROVIDER.canGetBlobContent(previewDataDirPath + "/" + blobReference);
			}

			return defaultResult;
		}

		/*Radix::Reports::ReportPreview:ReportPreviewLobProvider:canGetClobContent-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPreview:ReportPreviewLobProvider:canGetClobContent")
		public published  boolean canGetClobContent (Str clobReference) {
			return true;
		}

		/*Radix::Reports::ReportPreview:ReportPreviewLobProvider:getBlobContent-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPreview:ReportPreviewLobProvider:getBlobContent")
		public published  byte[] getBlobContent (Str blobReference) throws java.sql.SQLException {
			boolean canGetBlob = org.radixware.kernel.common.utils.FakeResultSet.DEFAULT_LOB_PROVIDER.canGetBlobContent(blobReference);
			if (!canGetBlob) {
			    return org.radixware.kernel.common.utils.FakeResultSet.DEFAULT_LOB_PROVIDER.getBlobContent(previewDataDirPath + "/" + blobReference);
			} else {
			    return org.radixware.kernel.common.utils.FakeResultSet.DEFAULT_LOB_PROVIDER.getBlobContent(blobReference);
			}
		}

		/*Radix::Reports::ReportPreview:ReportPreviewLobProvider:getClobContent-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPreview:ReportPreviewLobProvider:getClobContent")
		public published  char[] getClobContent (Str clobReference) throws java.sql.SQLException {
			return clobReference == null ? null : clobReference.toCharArray();
		}

		/*Radix::Reports::ReportPreview:ReportPreviewLobProvider:ReportPreviewLobProvider-User Method*/

		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPreview:ReportPreviewLobProvider:ReportPreviewLobProvider")
		public  ReportPreviewLobProvider (Str previewDataDirPath) throws java.io.FileNotFoundException {
			try {
			    this.previewDataDirPath = new java.io.File(previewDataDirPath).toURI().toURL();
			} catch (java.net.MalformedURLException ex) {
			    throw new FileNotFoundException(ex.getMessage());
			}
		}


	}

	/*Radix::Reports::ReportPreview:Properties-Properties*/





























	/*Radix::Reports::ReportPreview:Methods-Methods*/

	/*Radix::Reports::ReportPreview:preview-User Method*/

	@org.radixware.kernel.common.lang.ReflectiveCallable
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPreview:preview")
	public static  org.radixware.kernel.common.types.Bin preview (Str reportId, Str reportLayerUri, Str exportFormat, org.radixware.kernel.common.types.Bin reportJar, org.radixware.kernel.common.types.Bin testDataZip, Str exportLocale) throws org.radixware.kernel.server.reports.ReportGenerationException {
		if (testDataZip == null) {
		    throw new ReportGenerationException("Report preview test data not found");
		}

		if (reportJar == null) {
		    throw new ReportGenerationException("Report binaries not found");
		}

		if (reportLayerUri != null
		        && !Arte::Arte.getDefManager().getReleaseCache().getRelease().getRevisionMeta().getAllLayersSortedFromBottom().contains(reportLayerUri)) {
		    throw new ReportGenerationException("Report layer '" + reportLayerUri + "' doesn't exists on server");
		}

		if (!org.radixware.kernel.common.utils.Utils.emptyOrNull(exportLocale)) {
		    Str language = exportLocale;
		    if (exportLocale.contains("_")) {
		        Str localeParts[] = exportLocale.split("_");
		        language = localeParts[0];

		        java.util.Locale locale = new java.util.Locale(localeParts[0], localeParts[1]);
		        Arte::Arte.getInstance().setClientLocale(locale);
		    }
		    Arte::Arte.getInstance().setClientLanguage(Common::Language.getForValue(language));
		}

		ReportPreview.ReportPreviewEnvironment env = new ReportPreviewEnvironment(reportJar, testDataZip);
		try {
		    Types::Report.setPreviewEnvironment(env);
		    Types::Report report = (Types::Report) env.findReportClassById(Types::Id.Factory.loadFrom(reportId)).newInstance();

		    ReportExportFormat format = getFormatForString(exportFormat);

		    java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
		    java.util.Map<Types::Id, java.lang.Object> parameters = isReportRequireParameters(report) ? env.getReportParameters(report) : null;

		    ReportPreview.ReportPreviewFileController controller = new ReportPreviewFileController(format);

		    ReportExportParameters exportParameters = new ReportExportParameters(controller);
		    exportParameters.setExportFormat(format);
		    exportParameters.setParamId2Value(parameters);
		    exportParameters.setColumnSettings(env.getReportColumnsSettings(report));

		    report.export(exportParameters);
		    addDirToZip(controller.getDirectory(), baos);
		    controller.deleteDirectory();

		    return Bin.wrap(baos.toByteArray());
		} catch (Exceptions::Exception e) {
		    Arte::Trace.warning(Arte::Trace.exceptionStackToString(e), Arte::EventSource:ArteReports);
		    throw new ReportGenerationException("Report preview can not be generated", e);
		} finally {
		    env.close();
		    Types::Report.setPreviewEnvironment(null);
		}
	}

	/*Radix::Reports::ReportPreview:isReportRequireParameters-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPreview:isReportRequireParameters")
	private static  boolean isReportRequireParameters (org.radixware.ads.Types.server.Report report) {
		java.lang.reflect.Method[] methods = report.getClass().getDeclaredMethods();
		for (java.lang.reflect.Method method : methods) {
		    if (method.getName().equals(MTH_OPEN_DYNANIC) && method.getParameterTypes().length > 0) {
		        return true;
		    }
		}

		return false;
	}

	/*Radix::Reports::ReportPreview:addDirToZip-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPreview:addDirToZip")
	private static  void addDirToZip (java.io.File dir, java.io.OutputStream zipBaseOutputStream) throws java.io.FileNotFoundException,java.io.IOException {
		try (java.util.zip.ZipOutputStream zos = new java.util.zip.ZipOutputStream(zipBaseOutputStream)) {
		    for (java.io.File file : dir.listFiles()) {
		        if (!file.isDirectory()) {
		            try (java.io.FileInputStream fis = new java.io.FileInputStream(file)) {
		                java.util.zip.ZipEntry entry = new java.util.zip.ZipEntry(file.getName());
		                zos.putNextEntry(entry);
		                org.radixware.kernel.common.utils.FileUtils.copyStream(fis, zos);
		                zos.closeEntry();
		            }
		        }
		    }
		}
	}

	/*Radix::Reports::ReportPreview:getFormatForString-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::ReportPreview:getFormatForString")
	private static  org.radixware.kernel.common.enums.EReportExportFormat getFormatForString (Str exportFormatString) {
		for (ReportExportFormat format : ReportExportFormat.values()) {
		    if (format.getName().equals(exportFormatString)) {
		        return format;                
		    }
		    
		    if (format == ReportExportFormat:XLS && exportFormatString.equals("XLS")) {
		        return format;
		    }
		}

		return ReportExportFormat:PDF;
	}


}

/* Radix::Reports::ReportPreview - Server Meta*/

/*Radix::Reports::ReportPreview-Server Dynamic Class*/

package org.radixware.ads.Reports.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class ReportPreview_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("adcJHEA3ICKKVALVDGIC6OUI34B2U"),"ReportPreview",null,

						org.radixware.kernel.common.enums.EClassType.DYNAMIC,
						null,
						null,
						null,

						/*Radix::Reports::ReportPreview:Properties-Properties*/
						null,

						/*Radix::Reports::ReportPreview:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthAK2Z4XDFTNGVPDCL2U2EIXMJPQ"),"preview",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("reportId",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprPATNMYDUN5BF7GLISRUUSHMTSQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("reportLayerUri",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprLQMQS44LJRDHZN3Z46VDU2BZOI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("exportFormat",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprQXCFP5BEHVDA7CTGQJ6RD3R6YM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("reportJar",org.radixware.kernel.common.enums.EValType.BIN,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprZG2OGBBTEZDSPNWTMP22ONIASU")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("testDataZip",org.radixware.kernel.common.enums.EValType.BIN,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprLVBV4OQ22VGJPLL6SX3MARNBOE")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("exportLocale",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprGBVGJSROL5ANRMDZZ2VGVFKFHQ"))
								},org.radixware.kernel.common.enums.EValType.BIN),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthUZM3QLBOUZCUDNQHUZ7ASAUX2Y"),"isReportRequireParameters",true,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("report",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprH66B4SVO7BDMZC6U7UVH3EJASU"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthLL4A3QXU25HUJAD67P77K3IYHI"),"addDirToZip",true,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("dir",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprLLOSG6SWOZATJCQGFMHFSCIG7E")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("zipBaseOutputStream",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprG3X655XVPNHBRJNPRRLCBQOYYU"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthYQQ5JQPGYJDALFPJCNFSU3SW5M"),"getFormatForString",true,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("exportFormatString",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprLKUOQERIXZBQVNSCTZONVFHWVM"))
								},org.radixware.kernel.common.enums.EValType.STR)
						},
						null,
						null,null,null,false);
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta_adcIMU5HSNUZBHUHNQYB7ISMWHQ2M = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("adcIMU5HSNUZBHUHNQYB7ISMWHQ2M"),"ReportPreviewClassLoader",null,

						org.radixware.kernel.common.enums.EClassType.DYNAMIC,
						null,
						null,
						null,

						/*Radix::Reports::ReportPreview:ReportPreviewClassLoader:Properties-Properties*/
						new org.radixware.kernel.server.meta.clazzes.RadPropDef[]{

								/*Radix::Reports::ReportPreview:ReportPreviewClassLoader:reportJar-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdYTEHOONB2FHPBD4R6LQHRQYZXA"),"reportJar",null,org.radixware.kernel.common.enums.EValType.JAVA_CLASS,null,null,null)
						},

						/*Radix::Reports::ReportPreview:ReportPreviewClassLoader:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthV7CXA75LCRA3FCBFOOHRYPNOPE"),"findClass",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("name",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr6RTOEVHVAFB7ZPU4FXXJLOHB6A"))
								},org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthF4OQYIOGO5HC3GFJ3TFMUHVTYU"),"loadClass",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("name",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprUPIVBFTTTBCFVDX5WRHBJJBSZU")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("resolve",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr5AC6UBMCEJDJZJJIAF3PLCKIEY"))
								},org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthVQN47T3WCFAHZGAAF5I3ACO3AE"),"ReportPreviewClassLoader",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("reportJar",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprP4IA3P7RRBBSRJW2KTHNRSN2KE"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE)
						},
						null,
						null,null,null,false);
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta_adcJDVKLI72FFFBVB6B3Z23NZMQ6Y = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("adcJDVKLI72FFFBVB6B3Z23NZMQ6Y"),"ReportPreviewEnvironment",null,

						org.radixware.kernel.common.enums.EClassType.DYNAMIC,
						null,
						null,
						null,

						/*Radix::Reports::ReportPreview:ReportPreviewEnvironment:Properties-Properties*/
						new org.radixware.kernel.server.meta.clazzes.RadPropDef[]{

								/*Radix::Reports::ReportPreview:ReportPreviewEnvironment:TMP_DIR_NAME-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdXUXSGY2IEVAXXMI7W6Y7ACWSUE"),"TMP_DIR_NAME",null,org.radixware.kernel.common.enums.EValType.STR,null,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("REPORT_PREVIEW_DATA")),null),

								/*Radix::Reports::ReportPreview:ReportPreviewEnvironment:REPORT_JAR_NAME-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdQCOAPMPKPNCRDI4OFCX7O3DOIM"),"REPORT_JAR_NAME",null,org.radixware.kernel.common.enums.EValType.STR,null,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("report.jar")),null)
						},

						/*Radix::Reports::ReportPreview:ReportPreviewEnvironment:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthBGWE5ME3QRHB7LGCNPOJMRE454"),"findReportClassById",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("reportId",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprT7EFFJPOGRD3LA6XIHKXPZ26U4"))
								},org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthQYCKE5W545BS3LYITE5TL5I3C4"),"getTestResultSet",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("reportInstance",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprX7CXOGTOOVHSRPTF5WHLL7LQW4"))
								},org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthUSMGZVXNEZDOXPQX2HB4FIF4DQ"),"close",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthS4VUDT6WPZEIVOJEROU4JSP7RQ"),"ReportPreviewEnvironment",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("reportJar",org.radixware.kernel.common.enums.EValType.BIN,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprKJNZ4VGEEZFK7P3CYRGWWO7H6E")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("testDataZip",org.radixware.kernel.common.enums.EValType.BIN,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprWWW2GH42NNHPJK5OP3QWNPRLEM"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth2AQXB6RZA5DELDV3HLDHG7QFLY"),"getReportPreviewDataFile",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("reportId",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprKOVRKDMNWZE5NJWOCMUFSYLO2Q"))
								},org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthY72W5O5I35BXNC4BSG7YUE7GSE"),"getReportParameters",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("report",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprIP3K6GMTCVDGTP76MS66RM2FBU"))
								},org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthBQJAPDFZIFGVLOPQRSNCEEWDUI"),"getReportParametersFile",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("reportId",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprKOVRKDMNWZE5NJWOCMUFSYLO2Q"))
								},org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthTTOFCMI5BBF2LDZOP6C2AHCC2Y"),"getReportPreviewDataFileName",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("reportId",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprDGZDAGWCSFANTCTQL62I5T7TAM"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthFHLWOPXQJ5CH3EWYDYRS3XY7MA"),"getReportColumnsFile",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("reportId",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprKOVRKDMNWZE5NJWOCMUFSYLO2Q"))
								},org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthUGCVZMS5LFHNHIBISMH4VGQDNU"),"getReportColumnsSettings",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("report",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprADJU7R4KXZDD7E2EM4C3TPYINQ"))
								},org.radixware.kernel.common.enums.EValType.XML)
						},
						null,
						null,null,null,false);
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta_adcJCQNMJQR3JGTVGF5EAYMZPORWU = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("adcJCQNMJQR3JGTVGF5EAYMZPORWU"),"ReportPreviewFileController",null,

						org.radixware.kernel.common.enums.EClassType.DYNAMIC,
						null,
						null,
						null,

						/*Radix::Reports::ReportPreview:ReportPreviewFileController:Properties-Properties*/
						null,

						/*Radix::Reports::ReportPreview:ReportPreviewFileController:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthMMHPPAI5T5EFHOO5OTAQN4DSWI"),"adjustFileName",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("report",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprAJ2ZEDNI2BG5FCI5QGOQNAVITA")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("fileName",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprJX2A5J3255GRTK3LABRASXMHPI"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthHCFFTVMPBFBMVJOMMDZMTW4LWU"),"afterCloseFile",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("report",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprA77LJSSNKFGUZGH43Y4R5ISQJA")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("file",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprEOPPLXXW5NESBG76PJPPP43YOI"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthPQ6T4E3WPFELTDJAEYHOVJIXMU"),"afterCreateFile",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("report",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprJGTPRTPKYFEBJF7QYCMJE4SQZU")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("file",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprBTT2FSNIBJDNDHOXOH3SZC77II")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("outputStream",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr4SS43J6N3VD4VAYV3UT4B2KZV4"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthEQWZZHVBAZGUTNKSUVXAIQ4ZPI"),"beforeCloseFile",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("report",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprWI4ZU7M7JRA55PT5V5LKY3OXKU")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("file",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprAQH6WEZ2DVFE7CZO7UP63IEJRQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("outputStream",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprVM5ZXHNY6NCT7CEWIHY3UXC63U"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthNMVOW6LIMVH3LA5FQV52TIFPOI"),"getDirectory",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthBGZBRKEFYZG3DBTQVJ7CEXTEII"),"ReportPreviewFileController",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("exportFormat",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprJRKAHAKJZRGJHJ4UDOVJIY7IJ4"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth4CVL5JYCPNGW3PMSTDX72AO6JM"),"deleteDirectory",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,null)
						},
						null,
						null,null,null,false);
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta_adcVEYIJHMLKREPBI7ORLEYMV3TVE = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("adcVEYIJHMLKREPBI7ORLEYMV3TVE"),"ReportPreviewLobProvider",null,

						org.radixware.kernel.common.enums.EClassType.DYNAMIC,
						null,
						null,
						null,

						/*Radix::Reports::ReportPreview:ReportPreviewLobProvider:Properties-Properties*/
						null,

						/*Radix::Reports::ReportPreview:ReportPreviewLobProvider:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthWXNA6U7G5BBLZAUMKKZIQ3CHOQ"),"canGetBlobContent",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("blobReference",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprBJJG6EGXUVC7DC4Q77TYX62KII"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthVRAEMTHN5NEU7PPSPE5BZFI36U"),"canGetClobContent",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("clobReference",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprYG5YLSZADBCSZJR23YDG4Y5PNU"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthLN3KD5DIR5HXVOBOCX3I5WGM3Y"),"getBlobContent",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("blobReference",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprIM3PAJFNUZBWVHMLVX6LPOD54M"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthCDS72QE4T5G7RECEZYPN5OBE6Q"),"getClobContent",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("clobReference",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprBJ2PCJXIFVD3JB2RYCHRXM2IVI"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthJ2WAQNTJJRE5RCAGQOPZHBRYWI"),"ReportPreviewLobProvider",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("previewDataDirPath",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprRW3O6UTQJNDUHCLNT4VFLFYZVI"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE)
						},
						null,
						null,null,null,false);
}

/* Radix::Reports::ReportPreview - Localizing Bundle */
package org.radixware.ads.Reports.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class ReportPreview - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(ReportPreview - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbadcJHEA3ICKKVALVDGIC6OUI34B2U"),"ReportPreview - Localizing Bundle",$$$items$$$);
}
