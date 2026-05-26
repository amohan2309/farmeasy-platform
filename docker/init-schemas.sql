CREATE SCHEMA IF NOT EXISTS authentication AUTHORIZATION anandmohan;
CREATE SCHEMA IF NOT EXISTS subscription AUTHORIZATION anandmohan;
CREATE SCHEMA IF NOT EXISTS catalog AUTHORIZATION anandmohan;
CREATE SCHEMA IF NOT EXISTS marketplace AUTHORIZATION anandmohan;
CREATE SCHEMA IF NOT EXISTS weather AUTHORIZATION anandmohan;
CREATE SCHEMA IF NOT EXISTS irrigation AUTHORIZATION anandmohan;
CREATE SCHEMA IF NOT EXISTS iot AUTHORIZATION anandmohan;
CREATE SCHEMA IF NOT EXISTS crop_selling AUTHORIZATION anandmohan;
CREATE SCHEMA IF NOT EXISTS live_prices AUTHORIZATION anandmohan;
CREATE SCHEMA IF NOT EXISTS farm_easy_dev AUTHORIZATION anandmohan;

GRANT USAGE ON SCHEMA authentication TO anandmohan;
GRANT USAGE ON SCHEMA subscription TO anandmohan;
GRANT USAGE ON SCHEMA catalog TO anandmohan;
GRANT USAGE ON SCHEMA marketplace TO anandmohan;
GRANT USAGE ON SCHEMA weather TO anandmohan;
GRANT USAGE ON SCHEMA irrigation TO anandmohan;
GRANT USAGE ON SCHEMA iot TO anandmohan;
GRANT USAGE ON SCHEMA crop_selling TO anandmohan;
GRANT USAGE ON SCHEMA live_prices TO anandmohan;
GRANT USAGE ON SCHEMA farm_easy_dev TO anandmohan;

ALTER ROLE anandmohan SET search_path = authentication,public;
