#!/usr/bin/env bash
# Postgres setup for the Hibernate and JPA exercises.
# Run once at the start of your session in the Udacity workspace.
#
# Creates:
#   - role:     banking (password 'banking', login)
#   - database: banking (owned by banking)
#
# Idempotent: safe to re-run. Existing role/db will be left alone.

set -e

ROLE_NAME="banking"
ROLE_PASSWORD="banking"
DB_NAME="banking"

echo "Setting up Postgres role and database for the exercises..."

# Create the role only if missing
psql -h localhost -U postgres -tAc "SELECT 1 FROM pg_roles WHERE rolname='${ROLE_NAME}'" | grep -q 1 \
  || psql -h localhost -U postgres -c "CREATE USER ${ROLE_NAME} WITH PASSWORD '${ROLE_PASSWORD}';"

# Create the database only if missing
psql -h localhost -U postgres -tAc "SELECT 1 FROM pg_database WHERE datname='${DB_NAME}'" | grep -q 1 \
  || psql -h localhost -U postgres -c "CREATE DATABASE ${DB_NAME} OWNER ${ROLE_NAME};"

# Ensure privileges (no-op if already granted)
psql -h localhost -U postgres -c "GRANT ALL PRIVILEGES ON DATABASE ${DB_NAME} TO ${ROLE_NAME};" > /dev/null

echo "Done. Role '${ROLE_NAME}' and database '${DB_NAME}' are ready."
echo "Connection string: postgresql://${ROLE_NAME}:${ROLE_PASSWORD}@localhost:5432/${DB_NAME}"
