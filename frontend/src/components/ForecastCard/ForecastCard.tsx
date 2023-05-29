import React from 'react'
import { ForecastCardStyled, ForestCardIconStyled } from './ForecastCard.styled'
import { Flex, Text } from '@chakra-ui/react'
import { getForecastWeather, WEATHER_MAP } from '../../util'
import { SiRainmeter } from 'react-icons/si'
import { RiTempColdFill } from 'react-icons/ri'

export interface ForecastHour {
    time: number
    temperature: number
    precipitation: number
}

export const ForecastCard = ({ time, temperature, precipitation }: ForecastHour) => {
    const weather = getForecastWeather(precipitation)

    return (
        <ForecastCardStyled>
            <Text fontWeight={600} color='#4a6fa1'>
                {time.toString().length === 1 ? `0${time}` : time}:00
            </Text>
            <ForestCardIconStyled>{WEATHER_MAP[weather]}</ForestCardIconStyled>
            <Flex gap='8px'>
                <Flex flexDirection='column' justifyContent='center' gap='12px' height='100%'>
                    <SiRainmeter color='#7b98b2' />
                    <RiTempColdFill color='#4a6fa1' />
                </Flex>
                <Flex flexDirection='column' justifyContent='space-between' gap='8px' height='100%'>
                    <Text fontWeight={500} color='#7b98b2' margin='0'>
                        {precipitation}%
                    </Text>
                    <Text fontWeight={700} color='#4a6fa1' margin='0'>
                        {temperature + '°'}
                    </Text>
                </Flex>
            </Flex>
        </ForecastCardStyled>
    )
}
