#include <iostream>
#include "anagrams.hpp"

int main() {
	anagrams a("anna");
	for (anagrams::iterator it = a.begin(); it != a.end(); ++it)
		std::cout << *it << std::endl;
	return 0;
}
