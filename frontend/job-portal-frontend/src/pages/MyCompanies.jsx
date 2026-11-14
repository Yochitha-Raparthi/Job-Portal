
import { useEffect, useState } from "react";
import {
  createCompany,
  getMyCompanies,
  updateCompany,
  deleteCompany
} from "../services/CompanyService";

import "./MyCompanies.css";

const emptyCompany = {
  companyName: "",
  location: "",
  description: "",
  website: ""
};

function MyCompanies() {
  const [companies, setCompanies] = useState([]);
  const [form, setForm] = useState({ ...emptyCompany });
  const [editingId, setEditingId] = useState(null);
  const [showForm, setShowForm] = useState(false);

  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [deletingId, setDeletingId] = useState(null);
  const [error, setError] = useState("");
  const [message, setMessage] = useState("");

  const loadCompanies = async () => {
    setLoading(true);
    setError("");

    try {
      const response = await getMyCompanies();
      setCompanies(response.data);
    } catch (err) {
      setError("Unable to load your companies.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadCompanies();
  }, []);

  const handleChange = (e) => {
    const { name, value } = e.target;

    setForm((previous) => ({
      ...previous,
      [name]: value
    }));
  };

  const openCreateForm = () => {
    setEditingId(null);
    setForm({ ...emptyCompany });
    setError("");
    setMessage("");
    setShowForm(true);
  };

  const openEditForm = (company) => {
    setEditingId(company.id);

    setForm({
      companyName: company.companyName || "",
      location: company.location || "",
      description: company.description || "",
      website: company.website || ""
    });

    setError("");
    setMessage("");
    setShowForm(true);
  };

  const closeForm = () => {
    setShowForm(false);
    setEditingId(null);
    setForm({ ...emptyCompany });
    setError("");
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    setSaving(true);
    setError("");
    setMessage("");

    const companyData = {
      companyName: form.companyName.trim(),
      location: form.location.trim(),
      description: form.description.trim(),
      website: form.website.trim()
    };

    try {
      if (editingId !== null) {
        await updateCompany(editingId, companyData);
        setMessage("Company updated successfully!");
      } else {
        await createCompany(companyData);
        setMessage("Company created successfully!");
      }

      setShowForm(false);
      setEditingId(null);
      setForm({ ...emptyCompany });

      await loadCompanies();

    } catch (err) {
      setError(
        err.response?.data?.message ||
        "Unable to save company. Check your details."
      );
    } finally {
      setSaving(false);
    }
  };

  const handleDelete = async (id) => {
    const confirmed = window.confirm(
      "Are you sure you want to delete this company?"
    );

    if (!confirmed) return;

    setDeletingId(id);
    setError("");
    setMessage("");

    try {
      await deleteCompany(id);

      setCompanies((previous) =>
        previous.filter((company) => company.id !== id)
      );

      setMessage("Company deleted successfully!");

    } catch (err) {
      setError(
        err.response?.data?.message ||
        "Unable to delete company. It may have related jobs."
      );
    } finally {
      setDeletingId(null);
    }
  };

  return (
    <div className="companies-page">

      <div className="companies-heading">
        <div>
          <span className="eyebrow">RECRUITER WORKSPACE</span>
          <h1>My Companies</h1>
          <p>Create and manage your company profiles.</p>
        </div>

        {!showForm && (
          <button
            className="company-add-btn"
            onClick={openCreateForm}
          >
            + Add Company
          </button>
        )}
      </div>

      {message && (
        <p className="company-success" role="status">{message}</p>
      )}

      {error && (
        <p className="company-error" role="alert">{error}</p>
      )}

      {showForm && (
        <div className="company-form-card">

          <h2>
            {editingId !== null ? "Edit Company" : "Add New Company"}
          </h2>

          <form onSubmit={handleSubmit}>

            <div className="company-form-grid">

              <div className="company-field">
                <label htmlFor="companyName">Company Name</label>
                <input
                  id="companyName"
                  name="companyName"
                  value={form.companyName}
                  onChange={handleChange}
                  minLength={2}
                  maxLength={100}
                  required
                />
              </div>

              <div className="company-field">
                <label htmlFor="location">Location</label>
                <input
                  id="location"
                  name="location"
                  value={form.location}
                  onChange={handleChange}
                  required
                />
              </div>

              <div className="company-field company-full-width">
                <label htmlFor="website">Website</label>
                <input
                  id="website"
                  type="url"
                  name="website"
                  placeholder="https://example.com"
                  value={form.website}
                  onChange={handleChange}
                  pattern="https?://.+\..+"
                  required
                />
              </div>

              <div className="company-field company-full-width">
                <label htmlFor="description">Description</label>
                <textarea
                  id="description"
                  name="description"
                  rows={5}
                  value={form.description}
                  onChange={handleChange}
                  maxLength={1000}
                  required
                />
              </div>

            </div>

            <div className="company-form-actions">
              <button
                type="submit"
                className="company-save-btn"
                disabled={saving}
              >
                {saving
                  ? "Saving..."
                  : editingId !== null
                  ? "Update Company"
                  : "Create Company"}
              </button>

              <button
                type="button"
                className="company-cancel-btn"
                onClick={closeForm}
                disabled={saving}
              >
                Cancel
              </button>
            </div>

          </form>
        </div>
      )}

      {loading ? (
        <p className="companies-message">Loading companies...</p>
      ) : companies.length === 0 ? (
        <div className="companies-empty">
          <h2>No companies yet</h2>
          <p>Add your first company to start posting jobs.</p>
        </div>
      ) : (
        <div className="companies-grid">

          {companies.map((company) => (
            <div className="company-card" key={company.id}>

              <div className="company-card-top">
                <div className="company-avatar">
                  {company.companyName?.charAt(0).toUpperCase()}
                </div>

                <span className="company-location">
                  📍 {company.location}
                </span>
              </div>

              <h3>{company.companyName}</h3>

              <p className="company-description">
                {company.description}
              </p>

              <a
                href={company.website}
                target="_blank"
                rel="noopener noreferrer"
                className="company-website"
              >
                Visit Website
              </a>

              <div className="company-card-actions">
                <button
                  type="button"
                  className="company-edit-btn"
                  onClick={() => openEditForm(company)}
                >
                  Edit
                </button>

                <button
                  type="button"
                  className="company-delete-btn"
                  onClick={() => handleDelete(company.id)}
                  disabled={deletingId === company.id}
                >
                  {deletingId === company.id
                    ? "Deleting..."
                    : "Delete"}
                </button>
              </div>

            </div>
          ))}

        </div>
      )}
    </div>
  );
}

export default MyCompanies;
