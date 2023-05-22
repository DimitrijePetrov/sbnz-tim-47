import React from 'react'
import { ChakraProvider } from '@chakra-ui/react'
import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import { Home } from './components/Home/Home'
import { Wrapper } from '@googlemaps/react-wrapper'

export const queryClient = new QueryClient()

export const App = () => (
    <QueryClientProvider client={queryClient}>
        <ChakraProvider>
            <Wrapper apiKey=''>
                <Home />
            </Wrapper>
        </ChakraProvider>
    </QueryClientProvider>
)
