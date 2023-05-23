import React from 'react'
import { ForecastCardStyled, ForestCardIconStyled } from './ForecastCard.styled'
import { Text } from '@chakra-ui/react'
import { Weather, WEATHER_MAP } from '../../util'

export interface ForecastHour {
    time: string
    temperature: number
    weather: Weather
}

export const ForecastCard = ({ time, temperature, weather }: ForecastHour) => {
    return (
        <ForecastCardStyled>
            <Text fontWeight={600} color='#4a6fa1'>
                {time}
            </Text>
            <ForestCardIconStyled>{WEATHER_MAP[weather]}</ForestCardIconStyled>
            <Text fontWeight={700} color='#4a6fa1'>
                {temperature + '° C'}
            </Text>
        </ForecastCardStyled>
    )
}
