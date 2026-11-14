
import { useEffect, useState } from "react";
import { Link } from "react-router-dom";

import { getMyCompanies } from "../services/CompanyService";
import { getJobsByCompany } from "../services/JobService";

import {
  getAllApplications,
  updateApplicationStatus
} from "../services/ApplicationService";

import "./RecruiterApplications.css";

function RecruiterApplications() {
  const [companies, setCompanies] = useState([]);
  const [jobs, setJobs] = useState([]);
  const [applications, setApplications] = useState([]);

  const [companyFilter, setCompanyFilter] = useState("ALL");
  const [jobFilter, setJobFilter] = useState("ALL");
  const [statusFilter, setStatusFilter] = useState("ALL");

  const [loading, setLoading] = useState(true);
  const [updatingId, setUpdatingId] = useState(null);
  const [error, setError] = useState("");
  const [message, setMessage] = useState("");

  useEffect(() => {
    const loadData = async () => {
      setLoading(true);
      setError("");

      try {
        // Get companies owned by recruiter
        const companyResponse = await getMyCompanies();
        const myCompanies = companyResponse.data;
        setCompanies(myCompanies);

        // Get jobs for those companies
        const jobResponses = await Promise.all(
          myCompanies.map((company) =>
            getJobsByCompany(company.id)
          )
        );

        const allJobs = jobResponses.flatMap(
          (response) => response.data
        );

        setJobs(
          Array.from(
            new Map(
              allJobs.map((job) => [job.id, job])
            ).values()
          )
        );

        // Get applications for recruiter's jobs
        const applicationResponse = await getAllApplications();
        setApplications(applicationResponse.data);

      } catch (err) {
        setError("Unable to load recruiter applications.");
      } finally {
        setLoading(false);
      }
    };

    loadData();
  }, []);

  const handleStatusUpdate = async (id, newStatus) => {
    setUpdatingId(id);
    setError("");
    setMessage("");

    try {
      const response = await updateApplicationStatus(
        id,
        newStatus
      );

      setApplications((previous) =>
        previous.map((application) =>
          application.id === id
            ? response.data
            : application
        )
      );

      setMessage(
        `Application status updated to ${newStatus}.`
      );

    } catch (err) {
      setError(
        err.response?.data?.message ||
        "Unable to update application status."
      );
    } finally {
      setUpdatingId(null);
    }
  };

  const filteredJobs = jobs.filter((job) =>
    companyFilter === "ALL"
      ? true
      : String(job.companyId) === companyFilter
  );

  const filteredApplications = applications.filter(
    (application) => {
      const job = jobs.find(
        (item) => item.id === application.jobId
      );

      const matchesCompany =
        companyFilter === "ALL" ||
        String(job?.companyId) === companyFilter;

      const matchesJob =
        jobFilter === "ALL" ||
        String(application.jobId) === jobFilter;

      const matchesStatus =
        statusFilter === "ALL" ||
        application.status === statusFilter;

      return matchesCompany && matchesJob && matchesStatus;
    }
  );

  const pendingCount = applications.filter(
    (a) => a.status === "PENDING"
  ).length;

  const shortlistedCount = applications.filter(
    (a) => a.status === "SHORTLISTED"
  ).length;

  const rejectedCount = applications.filter(
    (a) => a.status === "REJECTED"
  ).length;

  return (
    <div className="recruiter-applications-page">

      <div className="ra-heading">
        <span className="eyebrow">RECRUITER WORKSPACE</span>
        <h1>Review Applications</h1>
        <p>
          Review candidates and manage their application statuses.
        </p>
      </div>

      {message && (
        <p className="ra-success" role="status">{message}</p>
      )}

      {error && (
        <p className="ra-error" role="alert">{error}</p>
      )}

      {loading ? (
        <p className="ra-loading">Loading applications...</p>
      ) : (
        <>
          <div className="ra-stats">
            <div className="ra-stat-card">
              <span>Total Applications</span>
              <strong>{applications.length}</strong>
            </div>

            <div className="ra-stat-card">
              <span>Pending</span>
              <strong>{pendingCount}</strong>
            </div>

            <div className="ra-stat-card">
              <span>Shortlisted</span>
              <strong>{shortlistedCount}</strong>
            </div>

            <div className="ra-stat-card">
              <span>Rejected</span>
              <strong>{rejectedCount}</strong>
            </div>
          </div>

          <div className="ra-filters">
            <div>
              <label htmlFor="companyFilter">Company</label>
              <select
                id="companyFilter"
                value={companyFilter}
                onChange={(e) => {
                  setCompanyFilter(e.target.value);
                  setJobFilter("ALL");
                }}
              >
                <option value="ALL">All Companies</option>

                {companies.map((company) => (
                  <option key={company.id} value={company.id}>
                    {company.companyName}
                  </option>
                ))}
              </select>
            </div>

            <div>
              <label htmlFor="jobFilter">Job</label>
              <select
                id="jobFilter"
                value={jobFilter}
                onChange={(e) => setJobFilter(e.target.value)}
              >
                <option value="ALL">All Jobs</option>

                {filteredJobs.map((job) => (
                  <option key={job.id} value={job.id}>
                    {job.title}
                  </option>
                ))}
              </select>
            </div>

            <div>
              <label htmlFor="statusFilter">Status</label>
              <select
                id="statusFilter"
                value={statusFilter}
                onChange={(e) => setStatusFilter(e.target.value)}
              >
                <option value="ALL">All Statuses</option>
                <option value="PENDING">Pending</option>
                <option value="SHORTLISTED">Shortlisted</option>
                <option value="REJECTED">Rejected</option>
              </select>
            </div>
          </div>

          <div className="ra-list-heading">
            <h2>Candidate Applications</h2>
            <p>{filteredApplications.length} applications found</p>
          </div>

          {filteredApplications.length === 0 ? (
            <div className="ra-empty">
              <h3>No applications found</h3>
              <p>
                Applications will appear here when candidates
                apply for your jobs.
              </p>
            </div>
          ) : (
            <div className="ra-grid">
              {filteredApplications.map((application) => {
                const job = jobs.find(
                  (item) => item.id === application.jobId
                );

                return (
                  <div
                    className="ra-card"
                    key={application.id}
                  >
                    <div className="ra-card-top">
                      <div className="ra-avatar">
                        {application.applicantName
                          ?.charAt(0)
                          .toUpperCase() || "A"}
                      </div>

                      <span
                        className={`ra-status ra-status-${application.status?.toLowerCase()}`}
                      >
                        {application.status}
                      </span>
                    </div>

                    <h3>{application.applicantName}</h3>

                    <p className="ra-job-title">
                      Applied for: {application.jobTitle}
                    </p>

                    <p className="ra-company-name">
                      {job?.companyName || "Company"}
                    </p>

                    <p className="ra-date">
                      Applied on:{" "}
                      {application.appliedAt
                        ? new Date(
                            application.appliedAt
                          ).toLocaleDateString()
                        : "N/A"}
                    </p>

                    <div className="ra-card-links">
                      <Link to={`/jobs/${application.jobId}`}>
                        View Job
                      </Link>

                      {application.resumeUrl && (
                        <a
                          href={application.resumeUrl}
                          target="_blank"
                          rel="noopener noreferrer"
                        >
                          View Resume
                        </a>
                      )}
                    </div>

                    <div className="ra-actions">
                      <button
                        type="button"
                        className="ra-shortlist-btn"
                        disabled={
                          updatingId === application.id ||
                          application.status === "SHORTLISTED"
                        }
                        onClick={() =>
                          handleStatusUpdate(
                            application.id,
                            "SHORTLISTED"
                          )
                        }
                      >
                        Shortlist
                      </button>

                      <button
                        type="button"
                        className="ra-reject-btn"
                        disabled={
                          updatingId === application.id ||
                          application.status === "REJECTED"
                        }
                        onClick={() =>
                          handleStatusUpdate(
                            application.id,
                            "REJECTED"
                          )
                        }
                      >
                        Reject
                      </button>
                    </div>
                  </div>
                );
              })}
            </div>
          )}
        </>
      )}
    </div>
  );
}

export default RecruiterApplications;
