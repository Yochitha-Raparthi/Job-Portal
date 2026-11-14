import api from "./api";

export const getAllJobs = () =>
  api.get("/job/getAll");

export const getJobById = (id) =>
  api.get(`/job/getJobById/${id}`);

export const getJobsByCompany = (companyId) =>
  api.get(`/job/getJobByCompany/${companyId}`);

export const searchJobsByTitle = (title) =>
  api.get(`/job/getJobByTitle/${encodeURIComponent(title)}`);

export const createJob = (data) =>
  api.post("/job/create", data);

export const updateJob = (id, data) =>
  api.put(`/job/updateJob/${id}`, data);

export const deleteJob = (id) =>
  api.delete(`/job/deleteJob/${id}`);