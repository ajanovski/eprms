using System;
using System.Collections.Generic;

#nullable disable

namespace info.ajanovski.eprms.net.Models
{
    public partial class Database
    {
        public long DatabaseId { get; set; }
        public string Name { get; set; }
        public string Owner { get; set; }
        public string Password { get; set; }
        public string Port { get; set; }
        public string Server { get; set; }
        public string TunnelPassword { get; set; }
        public string TunnelServer { get; set; }
        public string TunnelUser { get; set; }
        public string Type { get; set; }
        public long ProjectId { get; set; }
        public DateTime? DateCreated { get; set; }

        public virtual Project Project { get; set; }
    }
}
