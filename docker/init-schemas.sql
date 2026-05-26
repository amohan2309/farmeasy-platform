CREATE SCHEMA IF NOT EXISTS authentication AUTHORIZATION anandmohan;
CREATE SCHEMA IF NOT EXISTS subscription AUTHORIZATION anandmohan;
CREATE SCHEMA IF NOT EXISTS catalog AUTHORIZATION anandmohan;
CREATE SCHEMA IF NOT EXISTS farm_easy_dev AUTHORIZATION anandmohan;

GRANT USAGE ON SCHEMA authentication TO anandmohan;
GRANT USAGE ON SCHEMA subscription TO anandmohan;
GRANT USAGE ON SCHEMA catalog TO anandmohan;
GRANT USAGE ON SCHEMA farm_easy_dev TO anandmohan;

ALTER ROLE anandmohan SET search_path = authentication,public;
