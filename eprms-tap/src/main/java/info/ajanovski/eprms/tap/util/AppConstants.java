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
	public static final String PageIndex = "Index";

	public static final String SystemParameterDBCreationCommand = "DB-Creation-Command";
	public static final String SystemParameterDBServerType = "DBServerType";
	public static final String SystemParameterDBServerName = "DBServerName";
	public static final String SystemParameterDBServerPort = "DBServerPort";
	public static final String SystemParameterDBCreationPrefix = "DBCreationPrefix";
	public static final String SystemParameterDBCreationOwnerSuffix = "DBCreationOwnerSuffix";
	public static final String SystemParameterDBSSHTunnelUserCreationCommand = "DB-SSH-Tunnel-User-Creation-Command";
	public static final String SystemParameterDBTunnelServerName = "DBTunnelServer";
	public static final String SystemParameterDBTunnelPrefix = "DBTunnelPrefix";

	public static final String SystemParameterPMOverviewTicketsURL = "PM-Overview-Tickets-URL";
	public static final String SystemParameterPMOverviewTimelineURL = "PM-Overview-Timeline-URL";
	public static final String SystemParameterPMCreationScript = "PM-CreationScript";

	public static final String SystemParameterPMProjectURLPrefix = "PM-Project-URL-Prefix";

	public static final String SystemParameterRepoCreationCommand = "Repo-Creation-Command";

	public static final String[] AllSystemParameters = { SystemParameterDBCreationCommand, SystemParameterDBServerType,
			SystemParameterDBServerName, SystemParameterDBServerPort, SystemParameterDBCreationPrefix,
			SystemParameterDBCreationOwnerSuffix, SystemParameterDBSSHTunnelUserCreationCommand,
			SystemParameterDBTunnelServerName, SystemParameterDBTunnelPrefix, SystemParameterPMOverviewTicketsURL,
			SystemParameterPMOverviewTimelineURL, SystemParameterPMCreationScript, SystemParameterPMProjectURLPrefix,
			SystemParameterRepoCreationCommand };

	public static final String SystemParameterTplNewWorkReportByAdmin = "TPL-Work-Report-Title-By-Admin";
	public static final String SystemParameterTplNewWorkEvaluation = "TPL-Work-Evaluation-Title";
}
