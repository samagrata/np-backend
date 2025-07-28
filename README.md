## NpBackend

This project was created using Java 17, Spring Boot 3.5, MariaDB 11.2 and Docker.

### Configuration

Environment values can be adjusted using the `.env` file. For production, it should be Git ignored.

### Development server

Use the following Docker Compose command:

```bash
docker compose --profile dev up
```

Change or add profiles using `resources/application-*.yml` files.

Once the server is running, it is available on `http://localhost:8080`.

### Database migrations

This project uses [Flyway](https://www.red-gate.com/products/flyway/community/) to manage database migrations. See the `run_command.sh` file to run Flyway commands when container fires up.

### Contributions

See the `CONTRIBUTING.md` file for details.

### License

Please see the `LICENCE.txt` file for license and copyright details.
