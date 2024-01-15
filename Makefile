JAVAC=/usr/bin/javac
JAVA=/usr/bin/java
.SUFFIXES: .java .class
SRCDIR=src/MonteCarloMini
BINDIR=bin

$(BINDIR)/%.class:$(SRCDIR)/%.java
	$(JAVAC) -d $(BINDIR)/ -cp $(BINDIR) $<

CLASSES= TerrainArea.class SearchParallel.class\
	 MonteCarloMinimization.class\
	 MonteCarloMinimizationParallel.class Run.class		
	 
CLASS_FILES=$(CLASSES:%.class=$(BINDIR)/%.class)

default: $(CLASS_FILES)
	$(JAVA) -cp bin MonteCarloMini.Run

clean: 
	rm $(BINDIR)/MonteCarloMini/*.class

run-serial: $(CLASS_FILES)
	$(JAVA) -cp bin MonteCarloMini.MonteCarloMinimization 100 500 -30 60 -30 60 0.5

run-parallel: $(CLASS_FILES)
	$(JAVA) -cp bin MonteCarloMini.MonteCarloMinimizationParallel 100 500 -30 60 -30 60 0.5

run: $(CLASS_FILES)
	$(JAVA) -cp bin MonteCarloMini.Run

