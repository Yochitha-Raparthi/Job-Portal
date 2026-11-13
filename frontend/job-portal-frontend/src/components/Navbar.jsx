
import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

function Navbar() {

  const navigate = useNavigate();

  const { isAuthenticated, user, logout } = useAuth();

  // Logout functionality
  const handleLogout = () => {
    logout();
    navigate("/login", { replace: true });
  };

  // Redirect users based on their role
  const dashboardPath =
    user?.role === "RECRUITER"
      ? "/recruiter/dashboard"
      : "/seeker/dashboard";

  return (
    <nav className="navbar">

      {/* Website Logo */}
      <Link to="/" className="logo">
        Career<span>Connect</span>
      </Link>

      <div className="nav-links">

        {/* Common navigation links */}
        <Link to="/">Home</Link>
        <Link to="/jobs">Find Jobs</Link>

        {/* Show Dashboard only after login */}
        {isAuthenticated && user?.role && (
          <Link to={dashboardPath}>
            Dashboard
          </Link>
        )}

        {/* Show Logout for authenticated users */}
        {isAuthenticated ? (
          <button
            type="button"
            className="logout-button"
            onClick={handleLogout}
          >
            Logout
          </button>
        ) : (
          <>
            {/* Show Login and Sign Up before login */}
            <Link to="/login">Login</Link>

            <Link to="/register" className="nav-button">
              Sign Up
            </Link>
          </>
        )}

      </div>

    </nav>
  );
}

export default Navbar;
