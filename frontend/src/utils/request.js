import axios from 'axios'


// create an axios instance
const service = axios.create({
    baseURL: process.env.VUE_APP_BASE_API, // url = base url + request url
    timeout: 5000 // request timeout
})

// request interceptor
service.interceptors.request.use(
    config => {
        return config
    },
    error => {
        return Promise.reject(error)
    }
)

// response interceptor
service.interceptors.response.use(
    /**
     * If you want to get http information such as headers or status
     * Please return  response => response
     */

    /**
     * Determine the request status by custom code
     * Here is just an example
     * You can also judge the status by HTTP Status Code
     */
    response => {
        if (response.status === 200) {
            const res = response.data
            if (res.code !== 20000) {
                return Promise.reject(new Error(res.message || 'Error'));
            } else {
                return res;
            }
        } else {
            Promise.reject().then(r => console.log("rejected"));
        }
    },
    error => {
        return Promise.reject(error)
    }
)

export default service