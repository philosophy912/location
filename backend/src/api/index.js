import request from '../utils/request';

export const fetchData = () => {
    return request({
        url: './locations.json',
        method: 'get'
    });
};