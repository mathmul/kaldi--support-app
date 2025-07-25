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

          echo ""
          echo "✅ You may now use 'quarkus' CLI tool. See ./README.md for more."
          echo ""
        '';
      };
    });
}