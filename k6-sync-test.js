import http from 'k6/http';
import {check, sleep} from 'k6';

export const options = {
    stages: [
        {duration: '30s', target: 100},   // Ramp up to 100 users
        {duration: '1m', target: 100},    // Hold at 100 users
        {duration: '30s', target: 0},     // Ramp down
    ],
    thresholds: {
        http_req_failed: ['rate<0.10'],     // <10% errors
        http_req_duration: ['p(95)<2000'],  // 95% of requests < 2s
    },
};

export default function () {
    const url = 'http://localhost:8080/thread';

    const res = http.get(url);

    check(res, {
        'status is 200': (r) => r.status === 200,
        'has products': (r) => r.json().length > 0,
    });

    sleep(0.5);
}
