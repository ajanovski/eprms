using System;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Localization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Localization;
using Microsoft.Extensions.Logging;

namespace info.ajanovski.eprms.net.Controllers {

    public class LanguageController : Controller {

        private readonly IStringLocalizer<LanguageController> _localizer;
        private readonly ILogger<LanguageController> logger;

        public LanguageController(IStringLocalizer<LanguageController> localizer,
                            ILogger<LanguageController> logger) {
            logger.LogInformation("constructor");
            this._localizer = localizer;
            this.logger = logger;
        }

        [HttpGet("~/setculture")]
        public IActionResult SetCulture(string id = "en") {
            logger.LogInformation("set culture");
            string culture = id;
            Response.Cookies.Append(
               CookieRequestCultureProvider.DefaultCookieName,
               CookieRequestCultureProvider.MakeCookieValue(new RequestCulture(culture)),
               new CookieOptions { Expires = DateTimeOffset.UtcNow.AddYears(1) }
           );
            ViewData["Message"] = "Culture set to " + culture;
            return View("Index");
        }

        [HttpPost("~/setlanguage")]
        public IActionResult SetLanguage(string culture, string returnUrl) {
            logger.LogInformation("set language");
            Response.Cookies.Append(
                CookieRequestCultureProvider.DefaultCookieName,
                CookieRequestCultureProvider.MakeCookieValue(new RequestCulture(culture)),
                new CookieOptions { Expires = DateTimeOffset.UtcNow.AddYears(1) }
            );
            return RedirectToPage("/Index");
        }

    }

}
