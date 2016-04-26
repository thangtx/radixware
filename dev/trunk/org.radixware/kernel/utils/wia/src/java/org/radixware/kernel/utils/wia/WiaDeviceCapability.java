/*
 * Copyright (c) 2008-2016, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. This Source Code is distributed
 * WITHOUT ANY WARRANTY; including any implied warranties but not limited to
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Mozilla Public License, v. 2.0. for more details.
 */

package org.radixware.kernel.utils.wia;

import java.util.UUID;

public final class WiaDeviceCapability{
	
	private final UUID cmdGuid;
	private final String name;
	private final String description;
	private final String iconFileName;
	private final String cmdLineArgs;
	
	private WiaDeviceCapability(final String guidAsStr, final String name, final String desc, final String iconFile, final String cmd){//call from native code
		cmdGuid = UUID.fromString( guidAsStr.substring(1, guidAsStr.length() - 1) );
		this.name = name;
		description = desc;
		iconFileName = iconFile;
		cmdLineArgs = cmd;
	}

	public UUID getCommandGuid(){
		return cmdGuid;
	}
	
	public String getName(){
		return name;
	}
	
	public String getDescription(){
		return description;
	}
	
	public String getIconFileName(){
		return iconFileName;
	}
	
	public String getCommandLineArguments(){
		return cmdLineArgs;
	}
	
	@Override
	public String toString(){
		final StringBuilder strBuilder = new StringBuilder();
		strBuilder.append("{\n\t");
		printField("Name", name, strBuilder);

		strBuilder.append("\n\tCommand GUID: ");
		strBuilder.append(cmdGuid.toString());
		final EWiaDeviceCommand cmd = EWiaDeviceCommand.fromGuid(cmdGuid);
		strBuilder.append(" (");
		strBuilder.append(cmd.name());
		strBuilder.append(");");
		
		strBuilder.append("\n\t");
		printField("Description", description, strBuilder);		
		
		strBuilder.append("\n\t");
		printField("Icon file path", iconFileName, strBuilder);
		
		strBuilder.append("\n\t");
		printField("Command line arguments", cmdLineArgs, strBuilder);
		strBuilder.append("\n}");
		return strBuilder.toString();
	}
	
	private static void printField(final String fieldName, final String fieldValue, final StringBuilder strBuilder){
		strBuilder.append(fieldName);
		strBuilder.append(": ");
		if (fieldValue==null || fieldValue.isEmpty()){
			strBuilder.append("none");
		}else{
			strBuilder.append('"');
			strBuilder.append(fieldValue);
			strBuilder.append('"');
		}
		strBuilder.append(';');
	}
}