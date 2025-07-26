{
  description = "'Kaldi: Support app' dev environment with Quarkus CLI";

  inputs = {
    nixpkgs.url = "github:NixOS/nixpkgs/nixos-24.05";
    flake-utils.url = "github:numtide/flake-utils";
  };

  outputs = { self, nixpkgs, flake-utils }:
  flake-utils.lib.eachSystem [ "aarch64-darwin" ] (system:
    let
      pkgs = import nixpkgs { inherit system; };
    in
    {
      devShell = pkgs.mkShell {
        buildInputs = [
          pkgs.jbang
          pkgs.jdk21
          pkgs.maven
          pkgs.quarkus
        ];

        shellHook = ''
          echo ""
          echo "🔵 mvn --version"
          mvn --version

          echo ""
          echo "🔵 java --version"
          java --version

          echo ""
          echo "🔵 quarkus --version"
          quarkus --version

          # Auto-generate VS Code Java settings if missing
          if [ ! -f .vscode/settings.json ]; then
            mkdir -p .vscode
            JDK_PATH=$(readlink -f $(dirname $(which java))/..)

            cat > .vscode/settings.json <<'EOF'
{
  "java.configuration.runtimes": [
    {
      "name": "JavaSE-21",
      "path": "$JDK_PATH",
      "default": true,
    }
  ],
  "java.import.gradle.enabled": false,
  "java.jdt.ls.java.home": "$JDK_PATH",
}
EOF

            echo ""
            echo "✅ Java path written to ./.vscode/settings.json"
          fi

          echo ""
          echo "Setting ENV VARS..."
          export DB_USERNAME=postgres && echo "    DB_USERNAME=postgres"
          export DB_PASSWORD=postgres && echo "    DB_PASSWORD=postgres"

          echo ""
          echo "✅ You may now use 'quarkus' CLI tool. See ./README.md for more."
          echo ""
        '';
      };
    });
}
