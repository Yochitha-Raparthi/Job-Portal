import { useAuth } from "../context/AuthContext";

function Dashboard() {

  const { logout } = useAuth();

  return (
    <div className="dashboard-page">
      <h1>Welcome to CareerConnect!</h1>
      <p>You have successfully logged in.</p>
      <p>Your dashboard is protected by React Router.</p>
    </div>
  );
}

export default Dashboard;