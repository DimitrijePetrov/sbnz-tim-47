import React, { ReactNode } from 'react'
import { WarningStyled } from './Warning.styled'
import { WarningIcon } from '../../assets'

interface WarningProps {
    children: ReactNode
}

export const Warning = ({ children }: WarningProps) => {
    return (
        <WarningStyled>
            <WarningIcon />
            {children}
        </WarningStyled>
    )
}
