import React from 'react'
import '../../index.css'
import { HomeStyled, MainStyled } from './Home.styled'
import { CurrentWeather } from '../CurrentWeather/CurrentWeather'
import { Forecast } from '../Forecast/Forecast'

export const Home = () => {
    return (
        <HomeStyled>
            <MainStyled>
                <CurrentWeather />
                <Forecast />
            </MainStyled>
        </HomeStyled>
    )
}
