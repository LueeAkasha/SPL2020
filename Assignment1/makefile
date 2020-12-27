# All Targets
all: Assignment1

#Tool invocations
# Executable "Assignment1" depends on the files main.o Agent.o Graph.o Session.o Tree.o
Assignment1: bin/main.o bin/Agent.o bin/Graph.o bin/Session.o bin/Tree.o
	@echo 'Building target: Assignment1'
	@echo 'Invoking: C++ Linker'
	g++ -o bin/cTrace.o bin/main.o bin/Agent.o bin/Graph.o bin/Session.o bin/Tree.o
	@echo 'Finished building target: Assignment1'
	@echo ' '

#Depends on the source and header files
bin/main.o: src/main.cpp
	g++ -g -Wall -Weffc++ -std=c++11 -c -Iinclude -o bin/main.o src/main.cpp

#Depends on the source and header files
bin/Agent.o: src/Agent.cpp
	g++ -g -Wall -Weffc++ -std=c++11 -c -Iinclude -o bin/Agent.o src/Agent.cpp
	
#Depends on the source and header files
bin/Graph.o: src/Graph.cpp
	g++ -g -Wall -Weffc++ -std=c++11 -c -Iinclude -o bin/Graph.o src/Graph.cpp
	
#Depends on the source and header files
bin/Session.o: src/Session.cpp
	g++ -g -Wall -Weffc++ -std=c++11 -c -Iinclude -o bin/Session.o src/Session.cpp
	
#Depends on the source and header files
bin/Tree.o: src/Tree.cpp
	g++ -g -Wall -Weffc++ -std=c++11 -c -Iinclude -o bin/Tree.o src/Tree.cpp
	
#Clean the build dictionary
clean:
	rm -f bin/*
