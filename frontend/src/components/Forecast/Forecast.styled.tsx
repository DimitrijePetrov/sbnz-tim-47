import styled from 'styled-components'

export const ForecastStyled = styled.div`
    height: 35%;
    padding: 24px;
    background-color: #eeeeeeaa;
    border-radius: 12px;
    box-shadow: 0 16px 24px -8px rgba(29, 33, 48, 0.1);
    min-height: 204px;
`

export const ForecastHours = styled.div`
    display: flex;
    overflow-x: scroll;
    gap: 16px;
    &::-webkit-scrollbar {
        display: none;
    }
`
