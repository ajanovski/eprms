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

using System.Collections.Generic;
using System.Threading.Tasks;
using System.Security.Claims;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.RazorPages;
using Microsoft.Extensions.Localization;
using Microsoft.Extensions.Logging;
using info.ajanovski.eprms.net.Models;
using info.ajanovski.eprms.net.Services;

namespace info.ajanovski.eprms.net.Pages {

    [Authorize]
    public class MyDatabasesModel : PageModel {

        private readonly IStringLocalizer<MyDatabasesModel> _localizer;
        private readonly ILogger<MyDatabasesModel> logger;
        private ResourceManager resourceManager;
        private PersonManager personManager;

        public MyDatabasesModel(IStringLocalizer<MyDatabasesModel> localizer,
            ILogger<MyDatabasesModel> logger, ResourceManager resourceManager,
             PersonManager personManager) {
            this.logger = logger;
            this._localizer = localizer;
            this.resourceManager = resourceManager;
            this.personManager = personManager;
        }

        public Person getLoggedInPerson() {
            if (User.Identity.IsAuthenticated) {
                string cName = User.FindFirst(c => c.Type == ClaimTypes.Name)?.Value;
                return personManager.getPersonByUsername(cName);
            } else {
                return null;
            }
        }

        public List<Database> projectDatabases { get; set; }

        public void updateProjectDatabases(long personId) {
            projectDatabases = resourceManager.getActiveDatabasesByProject(personId);
        }

        public async Task<IActionResult> OnGetAsync() {
            logger.LogInformation("get async index");
            await Task.Run(() => {
                if (getLoggedInPerson()!=null) {
                    updateProjectDatabases(
                        getLoggedInPerson().PersonId
                    );
                } else {
                    logger.LogError("USER IS NOT LOGGED IN");
                }
            });
            return Page();
        }

    }

}
