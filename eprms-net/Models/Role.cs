using System;
using System.Collections.Generic;

#nullable disable

namespace info.ajanovski.eprms.net.Models
{
    public partial class Role
    {
        public Role()
        {
            PersonRoles = new HashSet<PersonRole>();
        }

        public long RoleId { get; set; }
        public string Name { get; set; }

        public virtual ICollection<PersonRole> PersonRoles { get; set; }
    }
}
