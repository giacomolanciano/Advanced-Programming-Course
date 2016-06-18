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
	//student* s = new student("mike", 971);
	person* p = new person("mike");
	person* q = new person("mike");
	
	/*
	 * if person::print() is virtual then
	 * 		p->print() = mike
	 * 		s->print() = mike 971
	 * else
	 * 		p->print() = mike
	 * 		s->print() = mike
	 * */
	p->print();
	s->print();
	
	/*
	 * if person::print() is virtual then
	 * 		sizeof(person) = 8
	 * 		sizeof(student) = 16
	 * else
	 * 		sizeof(person) = 16
	 * 		sizeof(student) = 24
	 * 
	 * NOTE: in the second case, the size of objects grow because of the
	 * presence of vptr (because of print() is virtual, and then 
	 * overridable, vtable maintaints an entry for it)
	 * */
	cout << sizeof(person) << endl;	
	cout << sizeof(student) << endl;   
	
	/*
	 * if person::print() is virtual then
	 * 		*(long*)p = *(long*)q != *(long*)s	(different vtables)
	 * else
	 * 		*(long*)p = *(long*)q = *(long*)s	(same vtable ???)
	 * 
	 * vtable is used to implement late binding in c++. given a class A
	 * that declares some virtual functions, vtable of A has an entry for 
	 * (i.e. a pointer to) each of that functions. when a class B inherit
	 * from A, it "makes a copy" of A's vtable and overrides those entries
	 * related to A's virtual functions that are overridden by B.
	 * the pointer to vtable is called vptr. it is basically
	 * an hidden field of the class, that is placed at the beginning of
	 * the memory representation of the object (see notes for details).
	 * 
	 * following lines print the vptr, i.e. the 8 bytes number that is 
	 * the address at which vtable starts for each object. 
	 * it is always the same, since the table is created once for all 
	 * (for each class).
	 * 
	 * NOTE: to do that we need to dereference the pointers
	 * 
	 * see also: https://en.wikipedia.org/wiki/Virtual_method_table
	 * */
	cout << *(long*)p << endl;
	cout << *(long*)q << endl;
	cout << *(long*)s << endl;
	
	return 0;
}
