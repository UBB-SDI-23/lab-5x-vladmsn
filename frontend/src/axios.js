import axios from 'axios';

const instance = axios.create({
  baseURL: process.env.REACT_APP_AXIOS_API_URL,

});

instance.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token'); // get token from local storage
    if (token) {
      config.headers['Authorization'] = 'Bearer ' + token; // set jwt token
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

export default instance;