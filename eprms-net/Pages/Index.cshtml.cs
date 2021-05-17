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
 * 
 ******************************************************************************/

using System.Threading.Tasks;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.RazorPages;
using Microsoft.Extensions.Localization;
using Microsoft.Extensions.Logging;

namespace info.ajanovski.eprms.net.Pages {

    [AllowAnonymous]
    public class IndexModel : PageModel {

        private readonly IStringLocalizer<IndexModel> _localizer;
        private readonly ILogger<IndexModel> logger;

        public IndexModel(IStringLocalizer<IndexModel> localizer,
            ILogger<IndexModel> logger) {
            this.logger = logger;
            this._localizer = localizer;
        }

        public async Task<IActionResult> OnGetAsync() {
            logger.LogInformation("get async index");
            await Task.Run(() => {
            });
            return Page();
        }

    }

}
