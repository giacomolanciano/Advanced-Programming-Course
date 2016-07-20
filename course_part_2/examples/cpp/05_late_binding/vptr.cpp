#include <iostream>

using namespace std;

class person {
	
public:
	char* name;
	person() {}
	person(char* n) : name(n) {}
	/*virtual*/ void print() {
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
	
	/*
	 * NOTE:
	 * 
	 * person* t = student("mike", 971);
	 * student* t = student("mike", 971);
	 * 
	 * the above lines give error at compile time. it is not possible 
	 * to assign a pointer using that syntax for the constructor (we have
	 * to use 'new' and therefore allocate objects on heap)
	 * */
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
	 * to do that we need to dereference the pointers.
	 * see https://en.wikipedia.org/wiki/Virtual_method_table for details
	 * 
	 * NOTE: 
	 * in this particular example we have that:
	 * if person::print() is NOT virtual then *(long*)p = *(long*)q = *(long*)s
	 * 
	 * it does NOT mean that first field of student and person points to
	 * the same vtable. in this case we pass to all objects the same string
	 * ("mike") as name. hence, first field of all objects is a pointer 
	 * to the same string object, that is converted to char* (deprecated)
	 * */
	cout << *(long*)p << endl;
	cout << *(long*)q << endl;
	cout << *(long*)s << endl;
	
	/*
	 * since objects have been dynamically allocated, it is necessary to
	 * explicitly deallocate them with delete
	 * */
	delete s;
	delete p;
	delete q;
	
	return 0;
}
