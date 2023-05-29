import { useQuery } from '@tanstack/react-query'
import { historicalDataAxios } from '../axios'

export interface HistoricalDataResponse {
    daily: {
        temperature_2m_max: number[]
        temperature_2m_min: number[]
    }
    hourly: {
        relativehumidity_2m: number[]
        surface_pressure: number[]
        temperature_2m: number[]
        windspeed_10m: number[]
        weathercode: number[]
    }
}

export type ForecastResponse = {
    time: number
    temperature: number
    precipitation: number
}[]

export type WarningsResponse = string[]

export const useReadCurrentWeather = () =>
    useQuery(
        ['historical-data'],
        (): Promise<HistoricalDataResponse> =>
            historicalDataAxios.get(
                `forecast?latitude=45.25&longitude=19.84&hourly=temperature_2m,weathercode,relativehumidity_2m,surface_pressure,windspeed_10m&daily=temperature_2m_max,temperature_2m_min&forecast_days=1&timezone=Europe%2FBerlin`
            ),
        { refetchInterval: 86400 }
    )

const MOCK_FORECAST: ForecastResponse = [
    { time: 0, temperature: 24, precipitation: 25 },
    { time: 1, temperature: 25, precipitation: 60 },
    { time: 13, temperature: 22, precipitation: 85 },
    { time: 21, temperature: 22, precipitation: 0 }
]

const MOCK_WARNINGS: WarningsResponse = [
    'Heat Wave Warning: High temperatures detected for each of the last 3 hours!',
    'Flood Warning: Heavy rainfall detected for each of the last 3 hours!',
    'Wind Storm Warning: High wind speeds detected for each of the last 3 hours!'
]

// export const useReadForecast = () =>
//     useQuery(['forecast'], (): Promise<ForecastResponse> => mainAxios.get('forecast'), { refetchInterval: 86400 })
//
// export const useReadWarnings = () =>
//     useQuery(['warnings'], (): Promise<WarningsResponse> => mainAxios.get('warnings'), { refetchInterval: 86400 })

export const useReadForecast = () =>
    useQuery(['forecast'], (): ForecastResponse => MOCK_FORECAST, { refetchInterval: 86400 })

export const useReadWarnings = () =>
    useQuery(['warnings'], (): WarningsResponse => MOCK_WARNINGS, { refetchInterval: 86400 })
