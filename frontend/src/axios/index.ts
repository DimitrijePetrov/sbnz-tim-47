import axios from 'axios'

export const mainAxios = axios.create({
    baseURL: 'http://localhost:8090/api'
})

export const historicalDataAxios = axios.create({
    baseURL: 'https://api.open-meteo.com/v1'
})

export const geocodingAxios = axios.create({
    baseURL: 'https://geocoding-api.open-meteo.com/v1'
})

mainAxios.interceptors.response.use(
    response => response.data,
    error => Promise.reject(error.response.data)
)

historicalDataAxios.interceptors.response.use(
    response => response.data,
    error => Promise.reject(error.response.data)
)

geocodingAxios.interceptors.response.use(
    response => response.data,
    error => Promise.reject(error.response.data)
)
