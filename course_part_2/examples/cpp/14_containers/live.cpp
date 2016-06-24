#include <fstream>
#include <map>
#include <set>
#include <string>
#include <iostream>

typedef std::map<std::string, int> MyMap;

static void inserter_old(std::string& str, MyMap& map) {
	/*
	 * increase the frequency counter for word in str by 1
	 * cost = search + insert = 2*logn
	 * 
	 * 2 cases:
	 * - str is not in
	 * - str is already in (just increase value by 1)
	 * 
	 * */
	 
	 /*
	  * count return # elements with key = str (either 0 or 1 for map)
	  * */
	 if(map.count(str) == 0) {
		 /*
		  * two possible ways to insert in a map:
		  * - map.insert()
		  * - []
		  * */
		
		/*std::pair<std::string, int> p(str, 1);
		map.insert(p);*/
		
		/*
		 * faster, tmp variable is created (since we do not need to
		 * access it)
		 * */
		map.insert(std::pair<std::string, int>(str, 1));
		  
	 } else {
		map[str] = map[str] + 1;
	 }
	 
}

static void inserter(std::string& str, MyMap& map) {
	 /*
	  * optimized version
	  * 
	  * when inserting in a structure that forbid duplicates, perform
	  * insertion "with fail". in case of duplicates, it return a pointer
	  * to the "already in" element. otherwise it points to new element.
	  * 
	  * */
	
	std::pair<MyMap::iterator, bool> ret = 
		map.insert(std::pair<std::string, int>(str, 1));
	if(ret.second == false) {
		/*
		 * ret points to the entry already in map
		 * just increase the associated value by 1
		 * */
		ret.first->second++;
	}
	/*
	 * else:
	 * ret points to the new entry
	 * */
}

int main(int argc, char* argv[]) {
	
	/*
	 * to simulate the behaviour of the program from shell
	 * 
	 * $ cat input.txt | sort | unique | wc
	 * 
	 * wc is the linux command to perform word counting
	 * 
	 * */
	 
	 
	/*
	 *  	 op		|		vector		|		map
	 * ==============================================
	 * insert		|	O(1)			|	O(logn)
	 * ----------------------------------------------
	 * remove		|	O(1)			|	O(logn)
	 * ----------------------------------------------
	 * search		|	O(n)			|	O(logn)
	 * 
	 * 
	 * */
	
	if(argc != 2) {
		std::cerr << "Syntax: " << argv[0] << " <input.txt>" << std::endl;
		return 1;
	}
	
	std::ifstream file(argv[1]);
	std::string str;
	std::set<std::string> mySet;
	
	/*
	 * local variable -> automatic garbage collection
	 * */
	MyMap myMap;
	
	/*
	 * if the map comes as a parameter (not very good practice)
	 * when you allocate "by hand" you have to deal with pointers
	 * 
	 * NOTE: valgrind will complain if we do not call any delete
	 * */
	//MyMap* tmp = new MyMap();
	//MyMap& myMap = *tmp;
	
	
	while(std::getline(file, str)) {
		mySet.insert(str);
		
		inserter(str, myMap);
		
		//std::cout << str << std::endl;
	}
	
	std::cout << "Distinct elements (set): " << mySet.size() << std::endl;
	std::cout << "Distinct elements (map): " << myMap.size() << std::endl;
	
	/*
	 * get rid of some entry like articles and conjuctions, printing 
	 * how many times they appeared.
	 * 
	 * NOTE: if the element does not exist in the map -> SEGFAULT
	 * (since this is not a boost implementation)
	 * 
	 * we need an iterator for removing -> find()
	 * */
	MyMap::iterator delIt = myMap.find("the");
	if(delIt == myMap.end()) {
		std::cerr << "\"the\" was not found" << std::endl;
	} else {
		std::cerr << "\"the\" appeard " << delIt->second << " times"<< std::endl;
		myMap.erase(delIt);
	}
	
	/*
	 * of if interested in just removing (no printing)
	 * */
	//myMap.erase(myMap.find("the"));
	
	
	/*
	 * retreives word appearing at least 50 times
	 * 
	 * to optimize, exploit loop invariant code motion:
	 * since the map will be not modified during iterations, we create
	 * end at the beginning, avoiding to creating it all times
	 * */
	std::map<int, std::string> shortOrdMap;
	for(MyMap::iterator it = myMap.begin(), end = myMap.end(); 
			it != end; it++) {
		if(it->second >= 50)
			//std::cout << "[" << it -> second << "] " << it->first << std::endl;
			shortOrdMap.insert(std::pair<int, std::string>(it->second, it->first));
	}
	
	/*
	 * free some memory now, don't wait for return statement (if needed)
	 * */
	 myMap.clear();
	 
	
	for(std::map<int, std::string>::iterator it = shortOrdMap.begin(), end = shortOrdMap.end(); 
			it != end; it++) {
		
		std::cout << "[" << it->first << "] " << it->second << std::endl;
		
	}
	
	/*
	 * if you do not use the local variable
	 * */
	//delete tmp;
	

	return 0;
	
}
