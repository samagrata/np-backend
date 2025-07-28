#!/bin/bash

set -e

## Flyway related tasks (uncomment to run)
## ---------------------------------------
# gradle flywayInfo      # Prints the details and status information about all the migrations
# gradle flywayClean     # Drops all objects in the configured schemas
# gradle flywayValidate  # Validates the applied migrations against the ones available on the classpath
# gradle flywayRepair    # Repairs the schema history table
# gradle flywayUndo      # Undoes the most recently applied versioned migration
# gradle flywayMigrate   # Migrates the database

# echo $(ls ~/.gradle/caches/modules-2/files-2.1 | grep valiation)

# gradle -q dependencyInsight --dependency validation # check a specific dependency
# gradle dependencies --write-locks # Run after adding new dependency
# gradle clean build
gradle build

java \
  -Dspring.devtools.restart.enabled=true \
  -Dspring.devtools.remote.secret=${DEVTOOLS_SECRET} \
  -Dspring.devtools.remote.debug.enabled=true \
  -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005 \
  -jar build/libs/$(ls build/libs | grep .jar | head -1)