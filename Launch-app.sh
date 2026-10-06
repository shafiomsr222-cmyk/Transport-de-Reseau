#!/bin/bash

rm -rf bin/sae
javac -d bin \
  --module-path lib \
  --add-modules javafx.controls,javafx.fxml \
  -cp "lib/*" $(find src/sae \
-name "*.java")

cp src/sae/ihm/*.fxml bin/sae/ihm/
cp -r src/sae/ihm/ressources bin/sae/ihm/

java --module-path lib \
  --add-modules javafx.controls,javafx.fxml \
  -cp "bin:lib/*" \
  sae.ihm.accueil
