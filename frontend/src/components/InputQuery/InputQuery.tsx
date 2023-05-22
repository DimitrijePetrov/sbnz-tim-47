import React, { useEffect, useState } from 'react'
import { useDebounce } from '../../hooks/useDebounce'
import { Select, SingleValue } from 'chakra-react-select'
import { GeometryLocation } from '../Map/Map'
import { GeocodingResult, useReadGeocoding } from '../../repositories'
import { InputQueryStyled } from './InputQuery.styled'

interface InputQueryProps {
    onChange: (value: GeometryLocation) => void
}

interface SelectOption {
    label: string
    value: GeometryLocation
}

const getResultOptions = ({ name, country, longitude, latitude }: GeocodingResult): SelectOption => ({
    label: `${name}, ${country}`,
    value: { lat: +latitude, lng: +longitude }
})

export const InputQuery = ({ onChange }: InputQueryProps) => {
    const [query, setQuery] = useState<string>('')
    const debouncedValue = useDebounce(query)
    const { data: geocoding } = useReadGeocoding(debouncedValue)
    const [options, setOptions] = useState<SelectOption[]>([])

    useEffect(() => {
        const options = geocoding?.results?.map(getResultOptions)
        if (options) setOptions(options)
    }, [geocoding])

    const handleOnChange = (value: SingleValue<SelectOption>) => {
        if (value) onChange(value?.value)
    }

    return (
        <InputQueryStyled>
            <Select
                onInputChange={setQuery}
                options={options}
                onChange={handleOnChange}
                placeholder='Type to search...'
            />
        </InputQueryStyled>
    )
}
