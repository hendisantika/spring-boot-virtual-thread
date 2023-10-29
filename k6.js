import http from "k6/http";
import {sleep} from "k6";

// const ENDPOINT = "https://test.k6.io";
const ENDPOINT = "http://localhost:8081/thread";

export default function () {
  http.get(ENDPOINT);
  sleep(1);
}
