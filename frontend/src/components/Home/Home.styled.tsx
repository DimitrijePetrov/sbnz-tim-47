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
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    gap: 16px;
`
