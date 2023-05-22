import { useMutation, useQuery } from '@tanstack/react-query'
import { geocodingAxios, historicalDataAxios, mainAxios } from '../axios'
import { GeometryLocation } from '../components/Map/Map'

export interface GeocodingResult {
    name: string
    country: string
    latitude: string
    longitude: string
}

export interface GeocodingResponse {
    results: GeocodingResult[]
}

export interface HistoricalDataHourly {
    temperature_2m: number[]
    time: string[]
}

export interface HistoricalDataResponse {
    hourly: HistoricalDataHourly
}

export const useReadGeocoding = (query: string) =>
    useQuery(
        ['geocoding', query],
        (): Promise<GeocodingResponse> => geocodingAxios.get(`search?name=${query}&count=10&language=en&format=json`),
        { enabled: Boolean(query) }
    )

export const useReadHistoricalData = ({ lat, lng }: GeometryLocation) =>
    useQuery(
        ['historical-data', `${lat}-${lng}`],
        (): Promise<HistoricalDataResponse> =>
            historicalDataAxios.get(
                `forecast?latitude=${lat}&longitude=${lng}&hourly=temperature_2m&past_days=7&forecast_days=2`
            )
    )

export const useRequestForecast = () =>
    useMutation((body: HistoricalDataHourly) => mainAxios.post('', body), { onSuccess: data => console.log(data) })
