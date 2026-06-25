# Workspace Setup

Most of the workspace is already configured: PostgreSQL is running,
JDK 25 is on the path, and Maven is wired up. One small step is left
that the workspace can't pre-create for you: the `banking` Postgres
role and database that every exercise connects to.

## Run the setup script

From the workspace terminal, run:

```
bash setup/setup-postgres.sh
```

The script is idempotent. You only need to run it once per workspace
session, but re-running it is safe.

## What it creates

- A Postgres role named `banking` with password `banking`
- A Postgres database named `banking` owned by the `banking` role

The application.yml in every exercise already points at this role
and database, so once the setup script completes, all 12 exercises
can connect without additional configuration.

## Verifying the setup

```
psql -U banking -d banking -c "SELECT 1;"
```

You should see `1` returned with no errors. If you get a connection
error, ensure PostgreSQL is running in the workspace before re-running
the setup script.

## When to re-run

Re-run the setup script if:
- You restart the workspace and the database state was reset
- You see "role 'banking' does not exist" when running an exercise
- You see "database 'banking' does not exist"
