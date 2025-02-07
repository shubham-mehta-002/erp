// api.js
import axios from "../config/axiosConfig";

export const fetchOptedModules = async () => {
  try {
    const response = await axios.get("/api/opted-modules");
    return response.data;
  } catch (error) {
    throw new Error(error.response?.data?.message || "Couldn't fetch opted modules");
  }
};

export const fetchAllModules = async () => {
  try {
    const response = await axios.get("/modules/all");
    return response.data;
  } catch (error) {
    throw new Error(error.response?.data?.message || "Couldn't fetch module list");
  }
};