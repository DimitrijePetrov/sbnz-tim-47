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

export const useReadCurrentWeather = () =>
    useQuery(
        ['historical-data'],
        (): Promise<HistoricalDataResponse> =>
            historicalDataAxios.get(
                `forecast?latitude=45.25&longitude=19.84&hourly=temperature_2m,weathercode,relativehumidity_2m,surface_pressure,windspeed_10m&daily=temperature_2m_max,temperature_2m_min&forecast_days=1&timezone=Europe%2FBerlin`
            )
    )
