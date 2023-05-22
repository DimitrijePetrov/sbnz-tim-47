import React from 'react'
import { GoogleMap, MarkerF } from '@react-google-maps/api'

export interface GeometryLocation {
    lat: number
    lng: number
}

export const Map = ({ lng, lat }: GeometryLocation) => {
    const location: GeometryLocation = { lng, lat }

    return (
        <GoogleMap center={location} zoom={10}>
            <MarkerF position={location} />
        </GoogleMap>
    )
}
