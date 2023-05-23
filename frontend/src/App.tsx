import React from 'react'
import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import { Home } from './components/Home/Home'
import { ReactQueryDevtools } from '@tanstack/react-query-devtools'

export const queryClient = new QueryClient()

export const App = () => (
    <QueryClientProvider client={queryClient}>
        <ReactQueryDevtools />
        <Home />
    </QueryClientProvider>
)
