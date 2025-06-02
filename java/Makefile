all: test

build:
	javac com/craftinginterpreters/lox/*.java

run: build
	java com.craftinginterpreters.lox.Lox

test: build
	java com.craftinginterpreters.lox.Lox test.lox

clean:
	rm com/craftinginterpreters/lox/*.class
