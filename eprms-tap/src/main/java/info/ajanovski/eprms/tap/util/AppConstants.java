/*******************************************************************************
 * Copyright (C) 2021 Vangel V. Ajanovski
 *     
 * This file is part of the EPRMS - Educational Project and Resource 
 * Management System (hereinafter: EPRMS).
 *     
 * EPRMS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *     
 * EPRMS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *     
 * You should have received a copy of the GNU General Public License
 * along with EPRMS.  If not, see <https://www.gnu.org/licenses/>.
 ******************************************************************************/

package info.ajanovski.eprms.tap.util;

public class AppConstants {

	/**
	 * Pages
	 */
	public final static String PageIndex = "Index";

	public static final String SystemParameterDBServerType = "DBServerType";
	public static final String SystemParameterDBServerName = "DBServerName";
	public static final String SystemParameterDBServerPort = "DBServerPort";
	public final static String SystemParameterDBCreationPrefix = "DBCreationPrefix";
	public final static String SystemParameterDBCreationOwnerSuffix = "DBCreationOwnerSuffix";
	public static final String SystemParameterDBTunnelServerName = "DBTunnelServer";
	public final static String SystemParameterDBTunnelPrefix = "DBTunnelPrefix";
	public final static String[] AllSystemParameters = { SystemParameterDBServerType, SystemParameterDBServerName,
			SystemParameterDBServerPort, SystemParameterDBCreationPrefix, SystemParameterDBCreationOwnerSuffix,
			SystemParameterDBTunnelPrefix };

	public final static String SystemParameterPMOverviewTicketsURL = "PM-Overview-Tickets-URL";
	public final static String SystemParameterPMOverviewTimelineURL = "PM-Overview-Timeline-URL";
}
