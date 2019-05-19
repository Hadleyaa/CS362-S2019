CFLAGS= -Wall -fpic -fprofile-arcs -ftest-coverage -lm -std=c99

testme: testme.c
	gcc -o testme -g testme.c $(CFLAGS)

run: testme
	./testme

out: testme
	./testme >> results.out	

cov:
	gcov -f -b testme.c >> coverage.out
	cat testme.c.gcov >> coverage.out

clean:
	rm -f *.o *.gcov *.gcda *.gcno *.so *.out testme testme.exe run run.exe out out.exe cov cov.exe
	rm -r *dSYM
