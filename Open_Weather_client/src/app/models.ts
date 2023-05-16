export interface Weather {
    city: string
    temperature: string
    feels_like: string
    minimum_temperature: string
    maximum_temperature: string
    humidity: string
    wind_speed: string
    weather_timestamp: string
    sunrise: string
    sunset: string
    timezone: string
    weather: Condition[]
    cod: string
    message: string
}

export interface Condition {
    description: string
    icon: string
}