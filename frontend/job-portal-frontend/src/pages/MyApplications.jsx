
import { useEffect, useState } from "react";
import { Link } from "react-router-dom";

import {
  getMyApplications,
  withdrawApplication
} from "../services/ApplicationService";

import "./MyApplications.css";

function MyApplications() {

  const [applications, setApplications] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [actionError, setActionError] = useState("");
  const [withdrawingId, setWithdrawingId] = useState(null);

  useEffect(() => {
    const loadApplications = async () => {
      try {
        const response = await getMyApplications();
        setApplications(response.data);
      } catch (err) {
        setError("Unable to load your applications.");
      } finally {
        setLoading(false);
      }
    };

    loadApplications();
  }, []);

  const handleWithdraw = async (id) => {
    const confirmed = window.confirm(
      "Are you sure you want to withdraw this application?"
    );

    if (!confirmed) return;

    setActionError("");
    setWithdrawingId(id);

    try {
      await withdrawApplication(id);

      setApplications((previous) =>
        previous.filter((application) => application.id !== id)
      );

    } catch (err) {
      setActionError(
        err.response?.data?.message ||
        "Unable to withdraw the application."
      );
    } finally {
      setWithdrawingId(null);
    }
  };

  if (loading) {
    return (
      <div className="applications-page">
        <p>Loading applications...</p>
      </div>
    );
  }

  return (
    <div className="applications-page">

      <div className="applications-heading">
        <span className="eyebrow">YOUR CAREER JOURNEY</span>
        <h1>My Applications</h1>
        <p>Track and manage your submitted job applications.</p>
      </div>

      {error && <p className="auth-error" role="alert">{error}</p>}
      {actionError && (
        <p className="auth-error" role="alert">{actionError}</p>
      )}

      {!error && applications.length === 0 && (
        <div className="applications-empty">
          <h2>No applications yet</h2>
          <p>Start exploring jobs and apply for your next opportunity.</p>
          <Link to="/jobs">Browse Jobs</Link>
        </div>
      )}

      {!error && applications.length > 0 && (
        <div className="applications-grid">

          {applications.map((application) => (
            <div className="application-card" key={application.id}>

              <div className="application-card-top">
                <h3>{application.jobTitle}</h3>
                <span className="application-status">
                  {application.status}
                </span>
              </div>

              <p className="application-id">
                Application #{application.id}
              </p>

              <p className="application-date">
                Applied on:{" "}
                {application.appliedAt
                  ? new Date(application.appliedAt).toLocaleDateString()
                  : "N/A"}
              </p>

              <div className="application-actions">
                <Link to={`/jobs/${application.jobId}`}>
                  View Job
                </Link>

                <button
                  type="button"
                  onClick={() => handleWithdraw(application.id)}
                  disabled={withdrawingId === application.id}
                >
                  {withdrawingId === application.id
                    ? "Withdrawing..."
                    : "Withdraw"}
                </button>
              </div>

            </div>
          ))}

        </div>
      )}
    </div>
  );
}

export default MyApplications;
