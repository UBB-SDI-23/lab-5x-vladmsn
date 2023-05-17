import axios from 'axios';

const instance = axios.create({
  baseURL: process.env.REACT_APP_AXIOS_API_URL
});

export default instance;