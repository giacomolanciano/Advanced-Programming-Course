#include <iostream>

using namespace std;

class person {
	
public:
	char* name;
	person() {}
	person(char* n) : name(n) {}
	virtual void print() {
		cout << name << endl;
	}
};

class student : public person {
	
public:
	int id;
	student(char* n, int i) { 
		name = n; 
		id = i; 
	}
	void print() {
		cout << name << " " << id << endl;
	}
};

int main() {
	person* s = new student("mike", 971);
	person* p = new person("mike");
	person* q = new person("mike");
	p->print();
	cout << sizeof(person) << endl;	
	cout << sizeof(student) << endl;   
	cout << *(long*)p << endl;   //print the 8 byte number that is at the beginning of vptr (NB bisogna dereferenziare, mettere * prima della parentesi)
	cout << *(long*)q << endl;   //print the 8 byte number that is at the beginning of vptr
	cout << *(long*)s << endl;   //print the 8 byte number that is at the beginning of vptr
	
	return 0;
}
