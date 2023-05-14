export interface Weather {
    city: string
    temperature: string
    feels_like: string
    minimum_temperature: string
    maximum_temperature: string
    humidity: string
    wind_speed: string
    weather_timestamp: number
    sunrise: number
    sunset: number
    weather: Condition[]
    cod: string
    message: string
}

export interface Condition {
    description: string
    icon: string
}