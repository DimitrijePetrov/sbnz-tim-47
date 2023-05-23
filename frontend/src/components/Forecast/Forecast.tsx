import React from 'react'
import { ForecastHours, ForecastStyled } from './Forecast.styled'
import { ForecastCard, ForecastHour } from '../ForecastCard/ForecastCard'
import { Text } from '@chakra-ui/react'

const MOCK: ForecastHour[] = []
for (let i = 0; i < 24; i++) MOCK.push({ time: i.toString() + ':00', temperature: 24, weather: 'Cloudy Sunny' })

export const Forecast = () => {
    return (
        <ForecastStyled>
            <Text fontWeight={600} margin={0} color='#7b96ae'>
                Hourly Forecast
            </Text>
            <ForecastHours>
                {MOCK.map((hour, index) => (
                    <ForecastCard key={index} time={hour.time} temperature={hour.temperature} weather={hour.weather} />
                ))}
            </ForecastHours>
        </ForecastStyled>
    )
}
