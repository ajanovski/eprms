using System.Security.Claims;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Logging;
using Microsoft.AspNetCore.Localization;
using Microsoft.Extensions.Localization;

namespace info.ajanovski.eprms.net.Controllers {
    [Route("[controller]")]
    public class UserController : Controller {

        private readonly IStringLocalizer<UserController> _localizer;
        private readonly ILogger<UserController> logger;

        public UserController(IStringLocalizer<UserController> localizer, 
							ILogger<UserController> logger)
        {
            this._localizer = localizer;
            this.logger = logger;
        }

        [HttpGet]
        public ActionResult Get() {
            logger.LogInformation("get accessed");
            if (User.Identity.IsAuthenticated) {
                string cId = User.FindFirst(c => c.Type == ClaimTypes.NameIdentifier)?.Value;
                string cName = User.FindFirst(c => c.Type == ClaimTypes.Name)?.Value;
                string cEmail = User.FindFirst(c => c.Type == ClaimTypes.Email)?.Value;
                return Json(new { name = cName, id=cId, email=cEmail });
            } else {
                return new EmptyResult();
            }
        }
    }
}
