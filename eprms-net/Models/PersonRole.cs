using System;
using System.Collections.Generic;

#nullable disable

namespace info.ajanovski.eprms.net.Models
{
    public partial class PersonRole
    {
        public long PersonRoleId { get; set; }
        public long PersonId { get; set; }
        public long RoleId { get; set; }

        public virtual Person Person { get; set; }
        public virtual Role Role { get; set; }
    }
}
