import http from "k6/http";
import {check, group, sleep} from "k6";
import {Rate, Trend} from "k6/metrics";

// Define custom metrics
const successRate = new Rate("success_rate");
const errorRate = new Rate("error_rate");
const getResponseTime = new Trend("get_response_time");
const threadResponseTime = new Trend("thread_response_time");
const saveResponseTime = new Trend("save_response_time");

// Base URL for the API
const BASE_URL = "http://localhost:8080";

// Options for the load test
export const options = {
  stages: [
    {duration: "30s", target: 20}, // Ramp up to 20 users over 30 seconds
    {duration: "1m", target: 20},  // Stay at 20 users for 1 minute
    {duration: "30s", target: 0},  // Ramp down to 0 users over 30 seconds
  ],
  thresholds: {
    http_req_duration: ["p(95)<2000"], // 95% of requests should complete within 2s
    "success_rate": ["rate>0.95"],     // 95% of requests should be successful
    "error_rate": ["rate<0.05"],       // Less than 5% of requests should fail
  },
};

// Main test function
export default function () {
  group("Home Endpoint Test", function () {
    testHomeEndpoint();
  });

  group("Thread Endpoint Test", function () {
    testThreadEndpoint();
  });

  group("Save Products Endpoint Test", function () {
    testSaveEndpoint();
  });

  // Sleep between iterations
  sleep(1);
}

// Test the home endpoint (GET /)
function testHomeEndpoint() {
  const response = http.get(`${BASE_URL}/`);

  // Record metrics
  getResponseTime.add(response.timings.duration);

  // Check if the request was successful
  const success = check(response, {
    "status is 200": (r) => r.status === 200,
    "response is not empty": (r) => r.body.length > 0,
  });

  // Update success and error rates
  successRate.add(success);
  errorRate.add(!success);
}

// Test the thread endpoint (GET /thread)
function testThreadEndpoint() {
  const response = http.get(`${BASE_URL}/thread`);

  // Record metrics
  threadResponseTime.add(response.timings.duration);

  // Check if the request was successful and returned products
  const success = check(response, {
    "status is 200": (r) => r.status === 200,
    "response is JSON": (r) => r.headers["Content-Type"] && r.headers["Content-Type"].includes("application/json"),
    "response is an array": (r) => Array.isArray(JSON.parse(r.body)),
  });

  // Update success and error rates
  successRate.add(success);
  errorRate.add(!success);
}

// Test the save endpoint (POST /save)
function testSaveEndpoint() {
  const response = http.post(`${BASE_URL}/save`);

  // Record metrics
  saveResponseTime.add(response.timings.duration);

  // Check if the request was successful
  const success = check(response, {
    "status is 200": (r) => r.status === 200,
    "response contains 'hendi'": (r) => r.body.includes("hendi"),
  });

  // Update success and error rates
  successRate.add(success);
  errorRate.add(!success);
}
