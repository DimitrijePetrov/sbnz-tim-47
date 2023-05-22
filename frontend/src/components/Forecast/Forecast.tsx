import React from 'react'
import { ForecastStyled } from './Forecast.styled'
import { ForecastCard, ForecastHour } from '../ForecastCard/ForecastCard'

const MOCK: ForecastHour[] = []
for (let i = 0; i < 24; i++) MOCK.push({ time: i.toString() + ':00', temperature: 24 })

export const Forecast = () => {
    return (
        <ForecastStyled>
            {MOCK.map((hour, index) => (
                <ForecastCard key={index} time={hour.time} temperature={hour.temperature} />
            ))}
        </ForecastStyled>
    )
}
