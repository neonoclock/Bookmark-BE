import { getToken } from "./storage.js";

const BASE_URL = "http://localhost:8080";

export async function http(method, url, data = null, isForm = false) {
  const headers = {};
  const token = getToken();

  if (!isForm) headers["Content-Type"] = "application/json";
  if (token) headers["Authorization"] = `Bearer ${token}`;

  const options = {
    method,
    headers,
  };

  if (data) {
    options.body = isForm ? data : JSON.stringify(data);
  }

  const res = await fetch(BASE_URL + url, options);

  if (!res.ok) {
    const msg = await res.text();
    throw new Error(msg || "요청 실패");
  }

  const contentType = res.headers.get("content-type");
  if (contentType?.includes("application/json")) {
    return res.json();
  }
  return res.text();
}

export const GET = (url) => http("GET", url);
export const POST = (url, data, isForm = false) =>
  http("POST", url, data, isForm);
export const PATCH = (url, data, isForm = false) =>
  http("PATCH", url, data, isForm);
export const PUT = (url, data, isForm = false) =>
  http("PUT", url, data, isForm);
export const DELETE = (url) => http("DELETE", url);
