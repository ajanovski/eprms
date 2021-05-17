using System;
using System.Collections.Generic;

#nullable disable

namespace info.ajanovski.eprms.net.Models
{
    public partial class WorkReport
    {
        public WorkReport()
        {
            InverseContinuationOfWorkReport = new HashSet<WorkReport>();
            WorkEvaluations = new HashSet<WorkEvaluation>();
        }

        public long WorkReportId { get; set; }
        public string Description { get; set; }
        public float? PercentReported { get; set; }
        public string Title { get; set; }
        public long ActivityId { get; set; }
        public long? ContinuationOfWorkReportId { get; set; }
        public long? PersonId { get; set; }
        public long? TeamId { get; set; }
        public DateTime? SubmissionDate { get; set; }

        public virtual Activity Activity { get; set; }
        public virtual WorkReport ContinuationOfWorkReport { get; set; }
        public virtual Person Person { get; set; }
        public virtual Team Team { get; set; }
        public virtual ICollection<WorkReport> InverseContinuationOfWorkReport { get; set; }
        public virtual ICollection<WorkEvaluation> WorkEvaluations { get; set; }
    }
}
