import React from 'react'
import { ForecastCardStyled } from './ForecastCard.styled'
import { Text } from '@chakra-ui/react'
import { Cloud } from '../../assets'

export interface ForecastHour {
    time: string
    temperature: number
}

export const ForecastCard = ({ time, temperature }: ForecastHour) => {
    return (
        <ForecastCardStyled>
            <Text fontWeight={600} color='#4a6fa1'>
                {time}
            </Text>
            <Cloud width={64} height={64} color='#2c76dbcc' />
            <Text fontWeight={700} color='#4a6fa1'>
                {temperature + '° C'}
            </Text>
        </ForecastCardStyled>
    )
}
