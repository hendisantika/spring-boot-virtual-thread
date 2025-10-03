import http from 'k6/http';
import {check, sleep} from 'k6';

export const options = {
    stages: [
        {duration: '30s', target: 500},   // Ramp up to 500 users
        {duration: '2m', target: 1000},   // Stay at 1000 users
        {duration: '30s', target: 2000},  // Spike to 2000 users
        {duration: '1m', target: 2000},   // Hold at 2000
        {duration: '30s', target: 0},     // Ramp down
    ],
    thresholds: {
        http_req_failed: ['rate<0.05'],     // <5% errors
        http_req_duration: ['p(95)<500'],   // 95% of requests < 500ms
    },
};

export default function () {
    const url = 'http://localhost:8080/save-async';

    const res = http.post(url);

    check(res, {
        'status is 202': (r) => r.status === 202,
        'response time < 500ms': (r) => r.timings.duration < 500,
        'has accepted status': (r) => r.json('status') === 'accepted',
    });

    sleep(0.1); // Small sleep to simulate think time
}
