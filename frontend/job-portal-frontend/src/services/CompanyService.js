
import api from "./api";

export const createCompany = (data) => {
  return api.post("/company/create", data);
};

export const getMyCompanies = () => {
  return api.get("/company/myCompanies");
};

export const getCompanyById = (id) => {
  return api.get(`/company/getCompanyById/${id}`);
};

export const updateCompany = (id, data) => {
  return api.put(`/company/updateCompany/${id}`, data);
};

export const deleteCompany = (id) => {
  return api.delete(`/company/deleteCompany/${id}`);
};
