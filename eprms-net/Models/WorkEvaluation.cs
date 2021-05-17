using System;
using System.Collections.Generic;

#nullable disable

namespace info.ajanovski.eprms.net.Models
{
    public partial class WorkEvaluation
    {
        public long WorkEvaluationId { get; set; }
        public string Description { get; set; }
        public float? PercentEvaluated { get; set; }
        public float? Points { get; set; }
        public string Title { get; set; }
        public long WorkReportId { get; set; }
        public DateTime? EvaluationDate { get; set; }
        public long? PersonId { get; set; }

        public virtual Person Person { get; set; }
        public virtual WorkReport WorkReport { get; set; }
    }
}
