import { Link } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

function JobSeekerDashboard() {

  const { user } = useAuth();

  return (
    <div className="dashboard-page">

      <div className="dashboard-heading">
        <div>
          <span className="eyebrow">JOB SEEKER PORTAL</span>
          <h1>Welcome back, {user?.name}!</h1>
          <p>Find opportunities and manage your career journey.</p>
        </div>
      </div>

      <div className="dashboard-grid">

        <div className="dashboard-card">
          <span className="dashboard-card-icon">💼</span>
          <h3>Browse Jobs</h3>
          <p>Explore job opportunities posted by recruiters.</p>
          <Link to="/jobs">Explore Jobs →</Link>
        </div>

        <div className="dashboard-card">
          <span className="dashboard-card-icon">📄</span>
          <h3>My Applications</h3>
          <p>Track the status of your submitted applications.</p>
          <Link to="/my-applications">View Applications →</Link>
        </div>

        <div className="dashboard-card">
          <span className="dashboard-card-icon">📅</span>
          <h3>My Interviews</h3>
          <p>Check your scheduled interviews.</p>
          <Link to="/my-interviews">View Interviews →</Link>
        </div>

      </div>
    </div>
  );
}

export default JobSeekerDashboard;