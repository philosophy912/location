import request from '../utils/request';

// export const fetchData = () => {
//     return request({
//         url: './locations.json',
//         method: 'get'
//     });
// };

export const fetchData = () => {
    return request({
        url: '/tax/all',
        method: 'get'
    });
}

export const fetchChart = (id) => {
    return request({
        url: '/tax/chart?id=' + id,
        method: 'get'
    });
}