import { ReactElement } from 'react'
import { Cloudy, CloudySunny, Rainy, RainySunny, Snowy, SnowySunny, Sunny, Thunderstorm } from '../assets'

const CLOUDY: number[] = [45, 48, 51, 53, 55, 56, 57]
const CLOUDY_SUNNY: number[] = [2, 3]
const RAINY: number[] = [63, 65, 66, 67, 80, 81, 82]
const RAINY_SUNNY: number[] = [61]
const SNOWY: number[] = [73, 75, 77, 85, 86]
const SNOWY_SUNNY: number[] = [71]
const SUNNY: number[] = [0, 1]
const THUNDERSTORM: number[] = [95, 96, 99]

export type Weather =
    | 'Cloudy'
    | 'Cloudy Sunny'
    | 'Rainy'
    | 'Rainy Sunny'
    | 'Snowy'
    | 'Snowy Sunny'
    | 'Sunny'
    | 'Thunderstorm'

export const getWeather = (code: number): Weather | undefined => {
    if (CLOUDY.includes(code)) return 'Cloudy'
    else if (CLOUDY_SUNNY.includes(code)) return 'Cloudy Sunny'
    else if (RAINY.includes(code)) return 'Rainy'
    else if (RAINY_SUNNY.includes(code)) return 'Rainy Sunny'
    else if (SNOWY.includes(code)) return 'Snowy'
    else if (SNOWY_SUNNY.includes(code)) return 'Snowy Sunny'
    else if (SUNNY.includes(code)) return 'Sunny'
    else if (THUNDERSTORM.includes(code)) return 'Thunderstorm'
}

export const getForecastWeather = (precipitation: number): Weather => {
    if (precipitation === 0) return 'Sunny'
    else if (precipitation < 30) return 'Rainy Sunny'
    else if (precipitation < 80) return 'Rainy'
    else return 'Thunderstorm'
}

export const WEATHER_MAP: Record<Weather, ReactElement> = {
    Cloudy: <Cloudy />,
    'Cloudy Sunny': <CloudySunny />,
    Rainy: <Rainy />,
    'Rainy Sunny': <RainySunny />,
    Snowy: <Snowy />,
    'Snowy Sunny': <SnowySunny />,
    Sunny: <Sunny />,
    Thunderstorm: <Thunderstorm />
}
