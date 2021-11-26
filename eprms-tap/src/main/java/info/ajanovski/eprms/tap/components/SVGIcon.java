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

package info.ajanovski.eprms.tap.components;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.AssetSource;

public class SVGIcon {

	@Property
	@Parameter(required = true, defaultPrefix = BindingConstants.LITERAL)
	private String path;

	@Property
	@Parameter(required = false, defaultPrefix = BindingConstants.LITERAL)
	private String width;

	@Property
	@Parameter(required = false, defaultPrefix = BindingConstants.LITERAL)
	private String height;

	@Property
	@Parameter(required = false, defaultPrefix = BindingConstants.LITERAL)
	private String title;

	@Inject
	private AssetSource assetSource;

	public void setupRender() {
		if (title == null) {
			title = "";
		}
		if (width == null && height != null) {
			width = height;
		}
		if (width != null && height == null) {
			height = width;
		}
		if (width == null && height == null) {
			height = "1.2em";
			width = "1.2em";
		}
	}
}
