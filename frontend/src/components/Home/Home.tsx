import React, { useEffect, useState } from 'react'
import { ForecastStyled, HomeStyled, MainStyled, MapStyled } from './Home.styled'
import { GeometryLocation, Map } from '../Map/Map'
import { useReadHistoricalData } from '../../repositories'
import { InputQuery } from '../InputQuery/InputQuery'

const BELGRADE: GeometryLocation = { lat: 44.81, lng: 20.46 }

export const Home = () => {
    const [location, setLocation] = useState<GeometryLocation>(BELGRADE)
    const { data: historicalData } = useReadHistoricalData(location)

    useEffect(() => {
        console.log(historicalData?.hourly)
    }, [historicalData])

    return (
        <HomeStyled>
            <MainStyled>
                <ForecastStyled>
                    <InputQuery onChange={setLocation} />
                </ForecastStyled>
                <MapStyled>
                    <Map lat={location.lat} lng={location.lng} />
                </MapStyled>
            </MainStyled>
        </HomeStyled>
    )
}
