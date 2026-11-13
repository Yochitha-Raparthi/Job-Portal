import { Link } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

function RecruiterDashboard() {

  const { user } = useAuth();

  return (
    <div className="dashboard-page">

      <div className="dashboard-heading">
        <div>
          <span className="eyebrow">RECRUITER PORTAL</span>
          <h1>Welcome back, {user?.name}!</h1>
          <p>Manage your hiring process from one place.</p>
        </div>
      </div>

      <div className="dashboard-grid">

        <div className="dashboard-card">
          <span className="dashboard-card-icon">🏢</span>
          <h3>My Companies</h3>
          <p>Create and manage your company profiles.</p>
          <Link to="/recruiter/companies">Manage Companies →</Link>
        </div>

        <div className="dashboard-card">
          <span className="dashboard-card-icon">💼</span>
          <h3>Manage Jobs</h3>
          <p>Post, update, and manage job opportunities.</p>
          <Link to="/recruiter/jobs">Manage Jobs →</Link>
        </div>

        <div className="dashboard-card">
          <span className="dashboard-card-icon">📋</span>
          <h3>Applications</h3>
          <p>Review applications submitted for your jobs.</p>
          <Link to="/recruiter/applications">View Applications →</Link>
        </div>

        <div className="dashboard-card">
          <span className="dashboard-card-icon">📅</span>
          <h3>Interviews</h3>
          <p>Schedule and manage candidate interviews.</p>
          <Link to="/recruiter/interviews">Manage Interviews →</Link>
        </div>

      </div>
    </div>
  );
}

export default RecruiterDashboard;