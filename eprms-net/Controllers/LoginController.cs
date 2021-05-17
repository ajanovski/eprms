using System.Threading.Tasks;
using Microsoft.AspNetCore.Authentication;
using Microsoft.AspNetCore.Authentication.Cookies;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Localization;
using Microsoft.AspNetCore.Http;
using Microsoft.Extensions.Logging;
using info.ajanovski.eprms.net.Extensions;
using Microsoft.AspNetCore.Authorization;

namespace info.ajanovski.eprms.net.Controllers {

    public class LoginController : Controller {

        private readonly IStringLocalizer<LoginController> _localizer;
        private readonly ILogger<LoginController> logger;

        public LoginController(IStringLocalizer<LoginController> localizer,
                            ILogger<LoginController> logger) {
            this.logger = logger;
            this._localizer = localizer;
        }

        [HttpGet("~/login")]
        public async Task<IActionResult> SignIn() => View("Login", await HttpContext.GetExternalProvidersAsync());

        [HttpPost("~/login")]
        public async Task<IActionResult> SignIn([FromForm] string provider) {
            logger.LogInformation("entered login with provider: " + provider);
            if (string.IsNullOrWhiteSpace(provider)) {
                return BadRequest();
            }
            if (!await HttpContext.IsProviderSupportedAsync(provider)) {
                return BadRequest();
            }
            return Challenge(new AuthenticationProperties { RedirectUri = "/" }, provider);
        }

        [AllowAnonymous]
        [Route("~/login")]
        public async Task Login(string returnUrl) {
            var props = new AuthenticationProperties { RedirectUri = returnUrl };
            await HttpContext.ChallengeAsync("CAS", props);
        }

        [HttpGet("~/logout")]
        [HttpPost("~/logout")]
        public override SignOutResult SignOut() {
            logger.LogInformation("entered logout");
            return SignOut(new AuthenticationProperties { RedirectUri = "/" },
                CookieAuthenticationDefaults.AuthenticationScheme);
        }
    }
}
