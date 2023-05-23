import React, { useEffect, useState } from 'react'
import { CurrentWeatherStyled, CurrentWeatherIconStyled } from './CurrentWeather.styled'
import { Flex, Text } from '@chakra-ui/react'
import { BiDroplet, BiWind } from 'react-icons/bi'
import { FiArrowDown, FiArrowUp } from 'react-icons/fi'
import { TbGauge } from 'react-icons/tb'
import { useReadCurrentWeather } from '../../repositories'
import { getWeather, Weather, WEATHER_MAP } from '../../util'

interface CurrentWeatherData {
    temperature: number
    maxTemperature: number
    minTemperature: number
    wind: number
    humidity: number
    pressure: number
    name: Weather
}

export const CurrentWeather = () => {
    const [weather, setWeather] = useState<CurrentWeatherData>()
    const { data } = useReadCurrentWeather()

    useEffect(() => {
        if (!data) return
        const hour = new Date().getHours()
        setWeather({
            maxTemperature: data.daily.temperature_2m_max[0],
            minTemperature: data.daily.temperature_2m_min[0],
            humidity: data.hourly.relativehumidity_2m[hour],
            pressure: data.hourly.surface_pressure[hour],
            temperature: data.hourly.temperature_2m[hour],
            wind: data.hourly.windspeed_10m[hour],
            name: getWeather(data.hourly.weathercode[hour])
        })
    }, [data])

    return (
        <CurrentWeatherStyled>
            <Text fontWeight={600} margin={0} color='#7b96ae'>
                Current Weather
            </Text>
            <Flex justifyContent='space-between' alignItems='center' height='100%' margin='0 128px'>
                <Flex flexDirection='column'>
                    <Text fontWeight={600} color='#4a6fa1' fontSize='24px'>
                        Novi Sad
                    </Text>
                    <Flex alignItems='center' gap='32px' height='100px'>
                        <CurrentWeatherIconStyled>
                            {weather ? WEATHER_MAP[weather?.name] : null}
                        </CurrentWeatherIconStyled>
                        <Text fontSize='72px' color='#4a6fa1'>
                            {weather?.temperature}°
                        </Text>
                    </Flex>
                    <Text fontWeight={600} color='#7b98b2'>
                        {weather?.name}
                    </Text>
                </Flex>
                <Flex flexDirection='column'>
                    <Flex gap='32px'>
                        <Flex alignItems='center' gap='16px'>
                            <FiArrowUp color='#7b96ae' size={24} />
                            <Text color='#4a6fa1' fontWeight={700}>
                                {weather?.maxTemperature}°
                            </Text>
                        </Flex>
                        <Flex alignItems='center' gap='16px'>
                            <FiArrowDown color='#7b96ae' size={24} />
                            <Text color='#4a6fa1' fontWeight={700}>
                                {weather?.minTemperature}°
                            </Text>
                        </Flex>
                    </Flex>
                    <Flex gap='24px'>
                        <Flex flexDirection='column'>
                            <Flex alignItems='center' gap='24px'>
                                <BiWind color='#7b96ae' size={24} />
                                <Text color='#7b96ae'>Wind</Text>
                            </Flex>
                            <Flex alignItems='center' gap='24px'>
                                <BiDroplet color='#7b96ae' size={24} />
                                <Text color='#7b96ae'>Humidity</Text>
                            </Flex>
                            <Flex alignItems='center' gap='24px'>
                                <TbGauge color='#7b96ae' size={24} />
                                <Text color='#7b96ae'>Pressure</Text>
                            </Flex>
                        </Flex>
                        <Flex flexDirection='column'>
                            <Text color='#4a6fa1' fontWeight={700}>
                                {weather?.wind}km/h
                            </Text>
                            <Text color='#4a6fa1' fontWeight={700}>
                                {weather?.humidity}%
                            </Text>
                            <Text color='#4a6fa1' fontWeight={700}>
                                {weather?.pressure}hPa
                            </Text>
                        </Flex>
                    </Flex>
                </Flex>
            </Flex>
        </CurrentWeatherStyled>
    )
}
