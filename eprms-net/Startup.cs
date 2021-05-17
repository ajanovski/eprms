using System.Net.Http;
using System.Net.Http.Headers;
using System.Text.Json;
using System.Security.Claims;
using System;

using Microsoft.AspNetCore.Builder;
using Microsoft.AspNetCore.Hosting;
using Microsoft.AspNetCore.Mvc.Razor;
using Microsoft.AspNetCore.Authentication.OAuth;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Authentication;
using Microsoft.AspNetCore.Authentication.Cookies;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Hosting;

using info.ajanovski.eprms.net.Data;
using info.ajanovski.eprms.net.Services;
using AspNetCore.Security.CAS;

namespace info.ajanovski.eprms.net {
    public class Startup {
        public Startup(IConfiguration configuration) {
            Configuration = configuration;
        }

        public IConfiguration Configuration { get; }

        public void ConfigureServices(IServiceCollection services) {
            services.AddDatabaseDeveloperPageExceptionFilter();

            services.AddDbContext<EPRMSNetDbContext>(options => {
                options.UseLazyLoadingProxies().UseNpgsql(Configuration.GetConnectionString("EPRMSDB")
                );
            });

            services.AddDistributedMemoryCache();

            services.AddSession(options => {
                options.IdleTimeout = TimeSpan.FromSeconds(10);
                options.Cookie.HttpOnly = true;
                options.Cookie.IsEssential = true;
            });

            services.AddLocalization(options => options.ResourcesPath = "Resources");

            services.AddRazorPages();

            services
            .AddAuthentication(options => {
                options.DefaultScheme = CookieAuthenticationDefaults.AuthenticationScheme;
            })
            .AddCookie(options => {
                options.LoginPath = "/login";
                options.LogoutPath = "/logout";
            })
            .AddOAuth("GitHub", options => {
                readAndSetOauthOptionsFromConfig("GitHub", options);
            })
            .AddOAuth("ORCID", options => {
                readAndSetOauthOptionsFromConfig("ORCID", options);
            })
            .AddCAS("CAS", options => {
                readAndSetCASOptionsFromConfig("CAS", options);
            });

            services.AddAuthorization();

            services.AddMvc()
                .AddViewLocalization(LanguageViewLocationExpanderFormat.Suffix)
                .AddDataAnnotationsLocalization();

            services.AddControllers();

            services.AddScoped<PersonManager, PersonManagerImpl>();
            services.AddScoped<ProjectManager, ProjectManagerImpl>();
            services.AddScoped<ResourceManager, ResourceManagerImpl>();

            services.AddScoped<PersonDao, PersonDaoImpl>();
            services.AddScoped<ProjectDao, ProjectDaoImpl>();
            services.AddScoped<ResourceDao, ResourceDaoImpl>();

            services.AddScoped<IClaimsTransformation, ClaimsTransformer>();

            services.Configure<RequestLocalizationOptions>(options => {
                var supportedCultures = new[] { "en", "mk" };
                options.SetDefaultCulture(supportedCultures[0])
                    .AddSupportedCultures(supportedCultures)
                    .AddSupportedUICultures(supportedCultures);
            });

        }

        public void Configure(IApplicationBuilder app, IWebHostEnvironment env) {
            if (env.IsDevelopment()) {
                app.UseDeveloperExceptionPage();
                app.UseMigrationsEndPoint();
            } else {
                app.UseExceptionHandler("/Error");
                app.UseHsts();
            }

            app.UseHttpsRedirection();

            var supportedCultures = new[] { "en", "mk" };
            var localizationOptions = new RequestLocalizationOptions().SetDefaultCulture(supportedCultures[0])
                .AddSupportedCultures(supportedCultures)
                .AddSupportedUICultures(supportedCultures);

            app.UseRequestLocalization(localizationOptions);
            app.UseStaticFiles();

            app.UseRouting();

            app.UseAuthentication();
            app.UseAuthorization();

            app.UseSession();

            app.UseEndpoints(endpoints => {
                endpoints.MapRazorPages();
                endpoints.MapControllers();
            });

            app.UseRequestLocalization();

        }

        public void readAndSetOauthOptionsFromConfig(string ProviderName, OAuthOptions o) {
            o.ClaimsIssuer = ProviderName;
            o.ClientId = Configuration[ProviderName + ":ClientId"];
            o.ClientSecret = Configuration[ProviderName + ":ClientSecret"];

            o.AuthorizationEndpoint = Configuration[ProviderName + ":AuthorizationEndpoint"];
            o.TokenEndpoint = Configuration[ProviderName + ":TokenEndpoint"];
            o.UserInformationEndpoint = Configuration[ProviderName + ":UserInformationEndpoint"];
            o.CallbackPath = new PathString(Configuration[ProviderName + ":CallbackPath"]);

            o.ClaimActions.MapJsonKey(ClaimTypes.NameIdentifier, Configuration[ProviderName + ":NameId"]);
            o.ClaimActions.MapJsonKey(ClaimTypes.Name, Configuration[ProviderName + ":Name"]);
            o.ClaimActions.MapJsonKey(ClaimTypes.Email, Configuration[ProviderName + ":Email"]);

            o.Scope.Add("openid");

            o.Events = new OAuthEvents {
                OnCreatingTicket = async context => {
                    var request = new HttpRequestMessage(HttpMethod.Get, context.Options.UserInformationEndpoint);
                    request.Headers.Accept.Add(new MediaTypeWithQualityHeaderValue("application/json"));
                    request.Headers.Authorization = new AuthenticationHeaderValue("Bearer", context.AccessToken);
                    var response = await context.Backchannel.SendAsync(request, HttpCompletionOption.ResponseHeadersRead, context.HttpContext.RequestAborted);
                    response.EnsureSuccessStatusCode();
                    using (var user = JsonDocument.Parse(await response.Content.ReadAsStringAsync())) {
                        context.RunClaimActions(user.RootElement);
                    }
                }
            };
        }

        public void readAndSetCASOptionsFromConfig(string ProviderName, CasOptions o) {
            o.CasServerUrlBase = Configuration[ProviderName + ":CasBaseUrl"];
        }

    }
}
