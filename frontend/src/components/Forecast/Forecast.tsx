import React from 'react'
import { ForecastHours, ForecastStyled } from './Forecast.styled'
import { ForecastCard } from '../ForecastCard/ForecastCard'
import { Text } from '@chakra-ui/react'
import { useReadForecast } from '../../repositories'

export const Forecast = () => {
    const { data: forecast } = useReadForecast()

    return (
        <ForecastStyled>
            <Text fontWeight={600} margin={0} color='#7b96ae'>
                Hourly Forecast
            </Text>
            <ForecastHours>
                {forecast?.map((hour, index) => (
                    <ForecastCard
                        key={index}
                        time={hour.time}
                        temperature={hour.temperature}
                        precipitation={hour.precipitation}
                    />
                ))}
            </ForecastHours>
        </ForecastStyled>
    )
}
