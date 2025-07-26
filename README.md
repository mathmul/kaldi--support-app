# Readme

## Development

```shell
nix develop
cd com.kaldi.support
quarkus dev
```

### Database

See application.properties (quarkus.datasource.devservices.*) for DB credentials.

### VS Code extensions

- Language Support for Java(TM) by Red Hat
- Debugger for Java
- Maven for Java
- Quarkus
- and much more...

## History

App created with:

```shell
quarkus create app com.kaldi.support \
  --no-code \
  --extensions="resteasy-reactive,jdbc-postgresql,hibernate-orm-panache"

Added later in that order

quarkus extension add quarkus-security
quarkus extension add quarkus-security-jpa
quarkus extension add io.quarkus:quarkus-jackson
```
