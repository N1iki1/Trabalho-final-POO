#!/bin/bash
# Linux / macOS
cd "$(dirname "$0")"
find src -name "*.java" > fontes.txt
javac -encoding UTF-8 -d out -cp "lib/*" @fontes.txt && rm fontes.txt && java -cp "out:lib/*" br.com.neuraxi.Main
