import styled from 'styled-components'

export const HomeStyled = styled.div`
    position: fixed;
    inset: 0;
    height: 100vh;
    width: 100vw;
    display: flex;
    justify-content: center;
    align-items: center;
    background: url('/background.jpg') no-repeat center/cover, #fff;
`

export const MainStyled = styled.div`
    width: 900px;
    height: 600px;
    border-radius: 8px;
    overflow: hidden;
    background-color: #eee;
    display: flex;
    justify-content: space-between;
`

export const ForecastStyled = styled.div`
    padding: 16px;
    width: 50%;
    height: 100%;
`

export const MapStyled = styled.div`
    width: 50%;
    height: 100%;
    padding: 16px 16px 16px 0;

    & > div {
        border-radius: 8px;
        width: 100%;
        height: 100%;
    }
`
