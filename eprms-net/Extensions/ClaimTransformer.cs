using System.Security.Claims;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Authentication;
using Microsoft.Extensions.Logging;
using info.ajanovski.eprms.net.Data;
using System.Linq;

public class ClaimsTransformer : IClaimsTransformation {

    private readonly EPRMSNetDbContext context;
    private readonly ILogger<ClaimsTransformer> logger;

    public ClaimsTransformer(EPRMSNetDbContext context,
    ILogger<ClaimsTransformer> logger) {
        this.logger = logger;
        this.context = context;
    }

    public Task<ClaimsPrincipal> TransformAsync(ClaimsPrincipal principal) {
        var ci = (ClaimsIdentity)principal.Identity;
        logger.LogInformation("Principal identity name: "+ci.Name);
        var user = context.People.Where(p => p.UserName.Equals(ci.Name)).FirstOrDefault();
        if (user!=null) {
            foreach (var r in user.PersonRoles) {
                var c = new Claim(ci.RoleClaimType, r.Role.Name);
                ci.AddClaim(c);
            }
            return Task.FromResult(principal);
        } else {
            logger.LogError("Can not find user");
            return null;
        }
    }
}