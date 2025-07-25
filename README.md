# Readme

App created with:

```shell
quarkus create app com.kaldi.support \
  --no-code \
  --extensions="resteasy-reactive,jdbc-postgresql,hibernate-orm-panache"
```

## Development

```shell
nix develop
cd com.kaldi.support
quarkus dev
```
