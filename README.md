# Readme

## Development

```shell
nix develop
cd com.kaldi.support
quarkus dev
```

### Install Nix

[Nix](https://nixos.org/download.html) package manager
    - [MacOS](https://nixcademy.com/posts/nix-on-macos/) - Install via the official installer
    - [Linux](https://nixos.org/download.html#nix-install-linux) - Use the recommended multi-user installation
    ```bash
    sh <(curl -L https://nixos.org/nix/install) --daemon
    ```

### Database

See application.properties (quarkus.datasource.devservices.*) for DB credentials.

### VS Code extensions

- Language Support for Java(TM) by Red Hat
- Debugger for Java
- Maven for Java
- Quarkus
- httpYac - Rest Client
- and much more...

## History

App created with:

```shell
quarkus create app com.kaldi.support \
  --no-code \
  --extensions="resteasy-reactive,jdbc-postgresql,hibernate-orm-panache"
```

Added later in this order:
```shell
quarkus extension add quarkus-security
quarkus extension add quarkus-security-jpa
quarkus extension add io.quarkus:quarkus-jackson
quarkus extension add quarkus-rest-jackson
```
