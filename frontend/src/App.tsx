import React from 'react'
import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import { Home } from './components/Home/Home'

export const queryClient = new QueryClient()

export const App = () => (
    <QueryClientProvider client={queryClient}>
        <Home />
    </QueryClientProvider>
)
