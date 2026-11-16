
import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import { getRecruiterDashboard } from "../services/DashboardService";
import "./Dashboard.css";

const getError = (error) =>
  error.response?.data?.message ||
  "Unable to load dashboard statistics.";

function RecruiterDashboard() {
  const { user } = useAuth();

  const [stats, setStats] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    let active = true;

    const loadDashboard = async () => {
      try {
        const response = await getRecruiterDashboard();

        if (active) {
          setStats(response.data);
        }
      } catch (err) {
        if (active) setError(getError(err));
      } finally {
        if (active) setLoading(false);
      }
    };

    loadDashboard();

    return () => {
      active = false;
    };
  }, []);

  const applicationStats = [
    {
      label: "Pending",
      count: stats?.pendingApplications ?? 0,
      color: "#f59e0b"
    },
    {
      label: "Shortlisted",
      count: stats?.shortlistedApplications ?? 0,
      color: "#0d9488"
    },
    {
      label: "Rejected",
      count: stats?.rejectedApplications ?? 0,
      color: "#ef4444"
    }
  ];

  const interviewStats = [
    {
      label: "Scheduled",
      count: stats?.scheduledInterviews ?? 0,
      color: "#2563eb"
    },
    {
      label: "Rescheduled",
      count: stats?.rescheduledInterviews ?? 0,
      color: "#f59e0b"
    },
    {
      label: "Completed",
      count: stats?.completedInterviews ?? 0,
      color: "#0d9488"
    },
    {
      label: "Cancelled",
      count: stats?.cancelledInterviews ?? 0,
      color: "#ef4444"
    }
  ];

  const renderBars = (items, total) => (
    <div className="dashboard-bars">
      {items.map((item) => {
        const percentage =
          total > 0 ? (item.count / total) * 100 : 0;

        return (
          <div className="dashboard-bar-item" key={item.label}>
            <div className="dashboard-bar-label">
              <span>{item.label}</span>
              <strong>{item.count}</strong>
            </div>

            <div className="dashboard-bar-track">
              <div
                className="dashboard-bar-fill"
                style={{
                  width: `${percentage}%`,
                  backgroundColor: item.color
                }}
              />
            </div>
          </div>
        );
      })}
    </div>
  );

  return (
    <main className="dashboard-page">
      <div className="dashboard-heading">
        <div>
          <span className="eyebrow">RECRUITER PORTAL</span>
          <h1>Welcome back, {user?.name}!</h1>
          <p>Manage your hiring process from one place.</p>
        </div>
      </div>

      {loading && (
        <p className="dashboard-loading">
          Loading dashboard statistics...
        </p>
      )}

      {error && (
        <p className="dashboard-error" role="alert">
          {error}
        </p>
      )}

      {stats && (
        <>
          <h2 className="dashboard-section-title">
            Recruitment Overview
          </h2>

          <div className="dashboard-stats-grid">
            <div className="dashboard-stat-card">
              <span className="dashboard-stat-icon">🏢</span>
              <h3>{stats.totalCompanies}</h3>
              <p>My Companies</p>
            </div>

            <div className="dashboard-stat-card">
              <span className="dashboard-stat-icon">💼</span>
              <h3>{stats.totalJobs}</h3>
              <p>Jobs Posted</p>
            </div>

            <div className="dashboard-stat-card">
              <span className="dashboard-stat-icon">📋</span>
              <h3>{stats.totalApplications}</h3>
              <p>Total Applications</p>
            </div>

            <div className="dashboard-stat-card">
              <span className="dashboard-stat-icon">📅</span>
              <h3>{stats.totalInterviews}</h3>
              <p>Total Interviews</p>
            </div>
          </div>

          <div className="dashboard-charts-grid">
            <section className="dashboard-chart-card">
              <h3>Application Status</h3>
              <p>Overview of candidate applications</p>

              {renderBars(
                applicationStats,
                stats.totalApplications
              )}
            </section>

            <section className="dashboard-chart-card">
              <h3>Interview Status</h3>
              <p>Overview of recruitment interviews</p>

              {renderBars(
                interviewStats,
                stats.totalInterviews
              )}
            </section>
          </div>
        </>
      )}

      <h2 className="dashboard-section-title">
        Quick Actions
      </h2>

      <div className="dashboard-grid">
        <div className="dashboard-card">
          <span className="dashboard-card-icon">🏢</span>
          <h3>My Companies</h3>
          <p>Create and manage your company profiles.</p>
          <Link to="/recruiter/companies">
            Manage Companies →
          </Link>
        </div>

        <div className="dashboard-card">
          <span className="dashboard-card-icon">💼</span>
          <h3>Manage Jobs</h3>
          <p>Post, update, and manage job opportunities.</p>
          <Link to="/recruiter/jobs">
            Manage Jobs →
          </Link>
        </div>

        <div className="dashboard-card">
          <span className="dashboard-card-icon">📋</span>
          <h3>Applications</h3>
          <p>Review applications submitted for your jobs.</p>
          <Link to="/recruiter/applications">
            View Applications →
          </Link>
        </div>

        <div className="dashboard-card">
          <span className="dashboard-card-icon">📅</span>
          <h3>Interviews</h3>
          <p>Schedule and manage candidate interviews.</p>
          <Link to="/recruiter/interviews">
            Manage Interviews →
          </Link>
        </div>
      </div>
    </main>
  );
}

export default RecruiterDashboard;
