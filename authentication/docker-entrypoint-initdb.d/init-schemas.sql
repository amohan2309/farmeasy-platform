-- Create the schemas the application expects so Hibernate can create tables in them
-- This script runs automatically on first container init by the official Postgres image

-- Use the application DB user as the schema owner so Hibernate (running as that user)
-- can create/modify tables without extra grants. Replace `anandmohan` if your
-- POSTGRES_USER is different.

CREATE SCHEMA IF NOT EXISTS authentication AUTHORIZATION anandmohan;
CREATE SCHEMA IF NOT EXISTS farm_easy_dev AUTHORIZATION anandmohan;

-- Grant usage to the configured DB user (idempotent)
GRANT USAGE ON SCHEMA authentication TO anandmohan;
GRANT USAGE ON SCHEMA farm_easy_dev TO anandmohan;

-- Ensure the role uses the authentication schema first when connecting
ALTER ROLE anandmohan SET search_path = authentication,public;