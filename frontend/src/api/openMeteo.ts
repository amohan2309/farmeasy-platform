import { DEFAULT_WEATHER_LOCATION } from '../data/weatherLocation';

export type HourlyWeatherPoint = {
  time: string;
  temperature2m: number | null;
  relativeHumidity2m: number | null;
  dewPoint2m: number | null;
  apparentTemperature: number | null;
  precipitation: number | null;
  rain: number | null;
  showers: number | null;
  soilTemperature0cm: number | null;
  soilTemperature6cm: number | null;
  soilTemperature18cm: number | null;
  soilTemperature54cm: number | null;
  soilMoisture0To1cm: number | null;
  soilMoisture1To3cm: number | null;
  soilMoisture3To9cm: number | null;
  soilMoisture9To27cm: number | null;
  soilMoisture27To81cm: number | null;
  temperature80m: number | null;
  temperature120m: number | null;
  temperature180m: number | null;
  windSpeed10m: number | null;
  windSpeed80m: number | null;
  windSpeed120m: number | null;
  windSpeed180m: number | null;
  windDirection10m: number | null;
  windDirection80m: number | null;
  windDirection120m: number | null;
  windDirection180m: number | null;
  windGusts10m: number | null;
  et0FaoEvapotranspiration: number | null;
  evapotranspiration: number | null;
  visibility: number | null;
  weatherCode: number | null;
};

export type HourlyForecast = {
  latitude: number;
  longitude: number;
  timezone: string;
  timezoneAbbreviation: string;
  locationName: string;
  units: Record<string, string>;
  hours: HourlyWeatherPoint[];
};

export const OPEN_METEO_FORECAST_URL = 'https://api.open-meteo.com/v1/forecast';

export const HOURLY_VARIABLES = [
  'temperature_2m',
  'relative_humidity_2m',
  'dew_point_2m',
  'apparent_temperature',
  'precipitation',
  'rain',
  'showers',
  'soil_temperature_0cm',
  'soil_temperature_6cm',
  'soil_temperature_18cm',
  'soil_temperature_54cm',
  'soil_moisture_0_to_1cm',
  'soil_moisture_1_to_3cm',
  'soil_moisture_3_to_9cm',
  'soil_moisture_9_to_27cm',
  'soil_moisture_27_to_81cm',
  'temperature_80m',
  'temperature_120m',
  'temperature_180m',
  'wind_speed_10m',
  'wind_speed_80m',
  'wind_speed_120m',
  'wind_speed_180m',
  'wind_direction_10m',
  'wind_direction_80m',
  'wind_direction_120m',
  'wind_direction_180m',
  'wind_gusts_10m',
  'et0_fao_evapotranspiration',
  'evapotranspiration',
  'visibility',
  'weather_code',
] as const;

const FIELD_BY_VARIABLE = {
  temperature_2m: 'temperature2m',
  relative_humidity_2m: 'relativeHumidity2m',
  dew_point_2m: 'dewPoint2m',
  apparent_temperature: 'apparentTemperature',
  precipitation: 'precipitation',
  rain: 'rain',
  showers: 'showers',
  soil_temperature_0cm: 'soilTemperature0cm',
  soil_temperature_6cm: 'soilTemperature6cm',
  soil_temperature_18cm: 'soilTemperature18cm',
  soil_temperature_54cm: 'soilTemperature54cm',
  soil_moisture_0_to_1cm: 'soilMoisture0To1cm',
  soil_moisture_1_to_3cm: 'soilMoisture1To3cm',
  soil_moisture_3_to_9cm: 'soilMoisture3To9cm',
  soil_moisture_9_to_27cm: 'soilMoisture9To27cm',
  soil_moisture_27_to_81cm: 'soilMoisture27To81cm',
  temperature_80m: 'temperature80m',
  temperature_120m: 'temperature120m',
  temperature_180m: 'temperature180m',
  wind_speed_10m: 'windSpeed10m',
  wind_speed_80m: 'windSpeed80m',
  wind_speed_120m: 'windSpeed120m',
  wind_speed_180m: 'windSpeed180m',
  wind_direction_10m: 'windDirection10m',
  wind_direction_80m: 'windDirection80m',
  wind_direction_120m: 'windDirection120m',
  wind_direction_180m: 'windDirection180m',
  wind_gusts_10m: 'windGusts10m',
  et0_fao_evapotranspiration: 'et0FaoEvapotranspiration',
  evapotranspiration: 'evapotranspiration',
  visibility: 'visibility',
  weather_code: 'weatherCode',
} as const satisfies Record<(typeof HOURLY_VARIABLES)[number], keyof Omit<HourlyWeatherPoint, 'time'>>;

type OpenMeteoResponse = {
  latitude: number;
  longitude: number;
  timezone?: string;
  timezone_abbreviation?: string;
  hourly_units?: Record<string, string>;
  hourly?: Record<string, Array<number | string | null>>;
};

function numericAt(values: Array<number | string | null> | undefined, index: number): number | null {
  if (!values || index >= values.length) return null;
  const value = values[index];
  if (value == null || value === '') return null;
  const n = typeof value === 'number' ? value : Number(value);
  return Number.isFinite(n) ? n : null;
}

export type WeatherLocation = {
  latitude: number;
  longitude: number;
  timezone: string;
  name: string;
};

export function buildForecastUrl(location: WeatherLocation = DEFAULT_WEATHER_LOCATION): string {
  const params = new URLSearchParams();
  params.set('latitude', String(location.latitude ?? DEFAULT_WEATHER_LOCATION.latitude));
  params.set('longitude', String(location.longitude ?? DEFAULT_WEATHER_LOCATION.longitude));
  params.set('hourly', HOURLY_VARIABLES.join(','));
  params.set('timezone', location.timezone ?? DEFAULT_WEATHER_LOCATION.timezone);
  return `${OPEN_METEO_FORECAST_URL}?${params.toString()}`;
}

export function parseHourlyForecast(data: OpenMeteoResponse, locationName: string = DEFAULT_WEATHER_LOCATION.name): HourlyForecast {
  const hourly = data.hourly ?? {};
  const times = (hourly.time ?? []).map((t) => String(t));
  const unitSource = data.hourly_units ?? {};
  const units: Record<string, string> = {};
  for (const variable of HOURLY_VARIABLES) {
    const field = FIELD_BY_VARIABLE[variable];
    if (unitSource[variable]) units[field] = unitSource[variable];
  }

  const hours: HourlyWeatherPoint[] = times.map((time, index) => {
    const point: HourlyWeatherPoint = {
      time,
      temperature2m: null,
      relativeHumidity2m: null,
      dewPoint2m: null,
      apparentTemperature: null,
      precipitation: null,
      rain: null,
      showers: null,
      soilTemperature0cm: null,
      soilTemperature6cm: null,
      soilTemperature18cm: null,
      soilTemperature54cm: null,
      soilMoisture0To1cm: null,
      soilMoisture1To3cm: null,
      soilMoisture3To9cm: null,
      soilMoisture9To27cm: null,
      soilMoisture27To81cm: null,
      temperature80m: null,
      temperature120m: null,
      temperature180m: null,
      windSpeed10m: null,
      windSpeed80m: null,
      windSpeed120m: null,
      windSpeed180m: null,
      windDirection10m: null,
      windDirection80m: null,
      windDirection120m: null,
      windDirection180m: null,
      windGusts10m: null,
      et0FaoEvapotranspiration: null,
      evapotranspiration: null,
      visibility: null,
      weatherCode: null,
    };
    for (const variable of HOURLY_VARIABLES) {
      const field = FIELD_BY_VARIABLE[variable];
      point[field] = numericAt(hourly[variable] as Array<number | string | null> | undefined, index);
    }
    return point;
  });

  return {
    latitude: data.latitude,
    longitude: data.longitude,
    timezone: data.timezone ?? DEFAULT_WEATHER_LOCATION.timezone,
    timezoneAbbreviation: data.timezone_abbreviation ?? '',
    locationName,
    units,
    hours,
  };
}

export async function fetchOpenMeteoHourly(
  location: {
    latitude?: number;
    longitude?: number;
    timezone?: string;
    name?: string;
  } = DEFAULT_WEATHER_LOCATION,
): Promise<HourlyForecast> {
  const resolved = {
    ...DEFAULT_WEATHER_LOCATION,
    ...location,
  };
  const res = await fetch(buildForecastUrl(resolved));
  if (!res.ok) throw new Error(`Request failed: ${OPEN_METEO_FORECAST_URL}`);
  const data = (await res.json()) as OpenMeteoResponse;
  if (!data.hourly?.time?.length) {
    return parseHourlyForecast(data, resolved.name);
  }
  return parseHourlyForecast(data, resolved.name);
}
